/**
 * Project: MentDB
 * License: GPL v_3
 * Description: Mentalese Database Engine
 * Website: https://www.mentdb.org
 * Twitter: https://twitter.com/mentalese_db
 * Facebook: https://www.facebook.com/mentdb
 * Author: Jimmitry Payet
 * Mail: contact@mentdb.org
 * Locality: Reunion Island (French)
 */

package re.jpayet.mentdb.core.entity.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.BasicNode;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL1;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.symbol.SymbolManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class WordManager {
	
	@SuppressWarnings("unchecked")
	public static JSONObject pearlNecklaceWord(String wordName) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		JSONObject nodes = new JSONObject();result.put("n", nodes);
		JSONArray edges = new JSONArray();result.put("e", edges);
		
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}

		Set<String> entrySet = ((JSONObject) rec.get("fl")).keySet();
		int[] autoincrement = {0};
		int id_word = autoincrement[0]++;
		
		nodes.put("X"+id_word+"_"+wordName, wordName);
		HashMap<String, Integer> globalLangKey = new HashMap<String, Integer>();
		HashMap<String, Integer> globalThoughtKey = new HashMap<String, Integer>();
		
		//Parse all languages
		for(String lang : entrySet) {
						
			int id_lang = autoincrement[0]++;
			
			nodes.put("L"+id_lang+"_"+lang, "L["+lang+"]");
			globalLangKey.put(lang, id_lang);
			
			JSONObject e = new JSONObject();
			e.put("k", "#"+id_word+"_"+id_lang);
			e.put("n1", "X"+id_word+"_"+wordName);
			e.put("n2", "L"+id_lang+"_"+lang);
			edges.add(e);
			
			String lang_last_node_id = "L"+id_lang+"_"+lang;
			int lang_last_id = id_lang;
			
			JSONArray list = ThoughtManager.list(wordName, lang);
			
			for(int i=0;i<list.size();i++) {
				
				String thoughtId = ""+list.get(i);
				int id_thought = 0;
				
				if (globalThoughtKey.containsKey(thoughtId)) {
					id_thought = globalThoughtKey.get(thoughtId);
				} else {
					id_thought = autoincrement[0]++;
				}
				globalThoughtKey.put(thoughtId, id_thought);
				
				nodes.put("T"+id_thought+"_"+thoughtId, thoughtId);
				
				e = new JSONObject();
				e.put("k", "#"+lang_last_id+"_"+id_thought);
				e.put("n1", lang_last_node_id);
				e.put("n2", "T"+id_thought+"_"+thoughtId);
				edges.add(e);
				
				int id_thought2 = 0;
				JSONArray c = CircleManager.getIds("th", thoughtId);
				for(int u=0;u<c.size();u++) {
					
					String th = (String) c.get(u);
					
					if (!th.equals(thoughtId)) {
						
						nodes.put("t"+id_thought2+"_"+th, th);
						
						e = new JSONObject();
						e.put("k", "#"+lang_last_id+"_"+id_thought2+"_c");
						e.put("n1", "T"+id_thought+"_"+thoughtId);
						e.put("n2", "t"+id_thought2+"_"+th);
						edges.add(e);
						
						id_thought2++;
						
					}
					
				}
				
				JSONArray words = ThoughtManager.getWords(thoughtId);
				HashMap<String, Integer> langKey = new HashMap<String, Integer>();
				
				if (i==0) {
					
					langKey = globalLangKey;
					
				}
				
				for(int j=0;j<words.size();j++) {
					String curWord = (((JSONObject) words.get(j)).get("word")+"");
					String curLang = (((JSONObject) words.get(j)).get("lang")+"");
					curWord = curWord.substring(2, curWord.length()-1);
					
					if (!wordName.equals(curWord)) {
						
						if (!langKey.containsKey(curLang)) {
							
							id_lang = autoincrement[0]++;
							nodes.put("L"+id_lang+"_"+curLang, "L["+curLang+"]");
							
							e = new JSONObject();
							e.put("k", id_thought+"_"+id_lang);
							e.put("n1", "T"+id_thought+"_"+thoughtId);
							e.put("n2", "L"+id_lang+"_"+curLang);
							edges.add(e);
							
							langKey.put(curLang, id_lang);
							
						}
						
						int id_sub_word = autoincrement[0]++;
						nodes.put("X"+id_sub_word+"_"+curWord, curWord);
						
						e = new JSONObject();
						e.put("k", id_sub_word+"_"+langKey.get(curLang));
						e.put("n1", "X"+id_sub_word+"_"+curWord);
						e.put("n2", "L"+langKey.get(curLang)+"_"+curLang);
						edges.add(e);
						
					}
					
				}
				
				lang_last_node_id = "T"+id_thought+"_"+thoughtId;
				lang_last_id = id_thought;
				
			}
			
		}
		
		return result;
		
	}

	//Check if a word already exist
	public static boolean exist(String wordName, String lang) throws Exception {
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec!=null) {
			
			//Check if the word already exist
			if (((JSONObject) rec.get("fl")).containsKey(lang)) {

				return true;
				
			} else {
				
				return false;
				
			}

		} else {
			
			return false;
			
		}

	}
	
	//Get the language probability
	@SuppressWarnings("unchecked")
	public static JSONArray getLangProbability(Vector<MQLValue> inputVector) throws Exception {
		
		HashMap<String, Integer> p = new HashMap<String, Integer>();
		int max = 0;
		
		//Parse all words
		for(int i=0;i<inputVector.size();i++) {
			
			String w = inputVector.elementAt(i).value;
			JSONObject rec = Record2.getNode("W["+w.toLowerCase()+"]");
			if (rec!=null) {
				JSONObject obj = ((JSONObject) rec.get("fl"));
				for(Object o : obj.keySet()) {
					String k = (String) o;
					if (p.containsKey(k)) {
						p.put(k, p.get(k)+1);
					} else {
						p.put(k, 1);
					}
					max++;
				}
				
			} else {
				rec = Record2.getNode("W["+StringFx.first_letter_upper(w)+"]");
				if (rec!=null) {
					JSONObject obj = ((JSONObject) rec.get("fl"));
					for(Object o : obj.keySet()) {
						String k = (String) o;
						if (p.containsKey(k)) {
							p.put(k, p.get(k)+1);
						} else {
							p.put(k, 1);
						}
						max++;
					}
					
				}
			}
			
		}
		
		List<Map.Entry<String, Integer>> list = new LinkedList<>( p.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return ( o2.getValue() ).compareTo( o1.getValue() );
			}
		} );
		
		JSONArray result = new JSONArray();
		for(int i=0;i<list.size();i++) {
			if (!list.get(i).getKey().equals("io")) {
				JSONObject obj = new JSONObject();
				obj.put("k", list.get(i).getKey());
				obj.put("v", Double.parseDouble(""+list.get(i).getValue())/Double.parseDouble(""+max)*100);
				result.add(obj);
			}
		}
		
		if (result.size()==0) {
			JSONObject obj = new JSONObject();
			obj.put("k", Start.NATIVE_LANGUAGE);
			obj.put("v", 100D);
			result.add(obj);
		}
		
		return result;
		
	}

	//Add a new word (or composed)
	public static String create(String wordName, String separator, String lang, String lock_translation, SessionThread session, EnvManager env) throws Exception {
		
		if (separator==null || separator.equals("")) {
			
			return createWord(wordName, lang, lock_translation, session, env);
			
		} else {
			
			//Split words
			String[] words = Misc.splitWords(wordName, separator);
			
			return createWordComposed(wordName, separator, lang, words, lock_translation, session, env);
		
		}
		
	}

	//Add a new word
	@SuppressWarnings("unchecked")
	public static String createWord(String wordName, String lang, String lock_translation, SessionThread session, EnvManager env) throws Exception {

		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec!=null) {
			
			//Check if the word already exist
			if (((JSONObject) rec.get("fl")).containsKey(lang)) {

				return WordManager.firstTabLink(wordName, lang);
				
			} else {

				//The word does not exist for the lang
				
				//Create the word in another lang
				JSONObject langObj = (JSONObject) rec.get("fl");
				JSONObject obj = new JSONObject();
				obj.put("ftl", null); //First tab link
				obj.put("ltl", null); //Last tab link
				langObj.put(lang, obj);

				//Parse all letters in the word
				for(int i=0;i<wordName.length();i++) {
					
					//Get the current letter
					String currentLetter = wordName.substring(i, i+1);

					ExperienceManagerL1.addNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+i+" "+lang+"]", lang, "symbol", false, false, 0);
		
				}

				Record2.update("W["+wordName+"]", rec.toJSONString());
				
				return ThoughtManager.create(wordName, "", lang, lock_translation, session, env);
				
			}

		} else {
			
			//Build the value of the word
			BasicNode bw = new BasicNode("W["+wordName+"]", BasicNode.WORD_TYPE, lang);
			
			//Parse all letters in the word
			for(int i=0;i<wordName.length();i++) {
				
				//Get the current letter
				String currentLetter = wordName.substring(i, i+1);
				
				ExperienceManagerL1.addNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+i+" "+lang+"]", lang, "symbol", false, false, 0);

			}
			
			//Add the record
			Record2.add("record", "W["+wordName+"]", bw.dataNode.toJSONString());
			
			return ThoughtManager.create(wordName, "", lang, lock_translation, session, env);
			
		}

	}

	//Add a new composed word
	@SuppressWarnings("unchecked")
	public static String createWordComposed(String wordName, String separator, String lang, String[] words, String lock_translation, SessionThread session, EnvManager env) throws Exception {

		//Initialization
		String gWordName = wordName;
		
		if (words.length<=1) {

			throw new Exception("Sorry, the word "+wordName+" is not a composed word.");

		}
		
		//Try to get the record
		JSONObject recGWord = Record2.getNode("W["+wordName+"]");
		
		
		if (recGWord!=null && ((JSONObject) recGWord.get("fl")).containsKey(lang)) {
			
			return WordManager.firstTabLink(wordName, lang);
			
		} else {
			
			for(int iWord = 0;iWord<words.length;iWord++) {
				
				wordName = words[iWord];
				
				//Try to get the record
				JSONObject rec = Record2.getNode("W["+wordName+"]");
				
				if (rec!=null) {
					
					//Check if the word already exist
					if (((JSONObject) rec.get("fl")).containsKey(lang)) {
						
						//Nothing to do ...
						
					} else {
		
						//The word does not exist for the lang
						
						//Create the word in another lang
						JSONObject langObj = (JSONObject) rec.get("fl");
						JSONObject obj = new JSONObject();
						obj.put("ftl", null); //First tab link
						obj.put("ltl", null); //Last tab link
						langObj.put(lang, obj);
						
						//Parse all letters in the word
						for(int i=0;i<wordName.length();i++) {
							
							//Get the current letter
							String currentLetter = wordName.substring(i, i+1);
							
							ExperienceManagerL1.addNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+i+" "+lang+"]", lang, "symbol", false, false, 0);
				
						}
						
						Record2.update("W["+wordName+"]", rec.toJSONString());
						
					}
		
				} else {
					
					//Build the value of the word
					BasicNode bw = new BasicNode("W["+wordName+"]", BasicNode.WORD_TYPE, lang);
					
					//Parse all letters in the word
					for(int i=0;i<wordName.length();i++) {
		
						//Get the current letter
						String currentLetter = wordName.substring(i, i+1);
						
						ExperienceManagerL1.addNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+i+" "+lang+"]", lang, "symbol", false, false, 0);
						
					}
					
					//Add the record
					Record2.add("record", "W["+wordName+"]", bw.dataNode.toJSONString());
					
				}
				
			}
			
			//Build the value of the word
			BasicNode bw = new BasicNode("W["+gWordName+"]", BasicNode.WORD_TYPE, lang);
			
			//Parse all letters in the word
			for(int i=0;i<gWordName.length();i++) {

				//Get the current letter
				String currentLetter = gWordName.substring(i, i+1);

				ExperienceManagerL1.addNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(gWordName)+" "+i+" "+lang+"]", lang, "symbol", false, false, 0);

			}
			
			//Add the record
			Record2.add("record", "W["+gWordName+"]", bw.dataNode.toJSONString());

			return ThoughtManager.create(gWordName, separator, lang, lock_translation, session, env);
			
		}

	}

	//Stimulate a word
	@SuppressWarnings("unchecked")
	public static void stimulate(String wordName, String lang) throws Exception {
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		} else {
			
			//Check if the word already exist
			if (!((JSONObject) rec.get("fl")).containsKey(lang)) {

				throw new Exception("Sorry, the word "+wordName+" does not exist in "+lang+".");
				
			} else {

				//Parse all letters in the word
				for(int i=0;i<wordName.length();i++) {
					
					SymbolManager.stimulate("TL["+StringFx.str_to_hex(wordName)+" "+i+" "+lang+"]");
					
				}
				
				rec.put("w", Integer.parseInt(""+rec.get("w"))+1);
				Record2.update("W["+wordName+"]", rec.toJSONString());
			
			}
		
		}

	}

	//Search words
	@SuppressWarnings("unchecked")
	public static JSONArray searchLevenshteinDistance(String symbolChars, int concentrationDepth, int orderDirection, String lang) throws Exception {
		
		if (symbolChars.length()==0) {

			throw new Exception("Sorry, the symbol char "+symbolChars+" is not valid [required].");

		}

		LinkedHashMap<String, Double> selectedTabLinkId = new LinkedHashMap<String, Double>();
		
		symbolChars = symbolChars.toLowerCase();

		//Parse all letters in the string
		for(int i=0;i<symbolChars.length();i++) {

			//Get the current letter
			String currentSymbolChar = symbolChars.substring(i, i+1);

			if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {

				int iDepth = 0;
				double localDistance = 0;

				String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);

				if (firstTL!=null) {

					//Handle the first tab link
					JSONObject bd = Record2.getNode(firstTL);

					String key = (bd.get("k")+"");
					String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
					Object downTL = null;

					//Check the word
					if (!selectedTabLinkId.containsKey(word)) {
						Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
						localDistance = l.doubleValue()/word.length()*100;
						selectedTabLinkId.put(word, localDistance);
					}
					iDepth++;

					//Handle other tab link
					downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
					
					while (downTL!=null && iDepth<=concentrationDepth) {

						bd = Record2.getNode(downTL+"");

						key = (bd.get("k")+"");
						word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);

						//Check the word
						if (!selectedTabLinkId.containsKey(word)) {
							Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
							localDistance = l.doubleValue()/word.length()*100;
							selectedTabLinkId.put(word, localDistance);
						}

						iDepth++;
						downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");

					}

				}

			}

		}
		
		symbolChars = symbolChars.toUpperCase();

		//Parse all letters in the string
		for(int i=0;i<symbolChars.length();i++) {

			//Get the current letter
			String currentSymbolChar = symbolChars.substring(i, i+1);

			if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {

				int iDepth = 0;
				double localDistance = 0;

				String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);

				if (firstTL!=null) {

					//Handle the first tab link
					JSONObject bd = Record2.getNode(firstTL);

					String key = (bd.get("k")+"");
					String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
					Object downTL = null;

					//Check the word
					if (!selectedTabLinkId.containsKey(word)) {
						Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
						localDistance = l.doubleValue()/word.length()*100;
						selectedTabLinkId.put(word, localDistance);
					}
					iDepth++;

					//Handle other tab link
					downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
					
					while (downTL!=null && iDepth<=concentrationDepth) {

						bd = Record2.getNode(downTL+"");

						key = (bd.get("k")+"");
						word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);

						//Check the word
						if (!selectedTabLinkId.containsKey(word)) {
							Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
							localDistance = l.doubleValue()/word.length()*100;
							selectedTabLinkId.put(word, localDistance);
						}

						iDepth++;
						downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");

					}

				}

			}

		}

		JSONArray selectedTabLink = new JSONArray();
		
		if (orderDirection==1) {
			
			List<Map.Entry<String, Double>> entries =
					new ArrayList<Map.Entry<String, Double>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b){
					return a.getValue().compareTo(b.getValue());
				}
			});
			
			for (Map.Entry<String, Double> entry : entries) {
				
				JSONObject obj = new JSONObject();obj.put(entry.getKey(), entry.getValue());
				selectedTabLink.add(obj);
				
			}
			
		} else if (orderDirection==2) {
			
			List<Map.Entry<String, Double>> entries =
					new ArrayList<Map.Entry<String, Double>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b){
					return b.getValue().compareTo(a.getValue());
				}
			});
			
			for (Map.Entry<String, Double> entry : entries) {
				
				JSONObject obj = new JSONObject();obj.put(entry.getKey(), entry.getValue());
				selectedTabLink.add(obj);
				
			}
			
		} else {
			
			//Build the JSON and calculate the percent
			for (Map.Entry<String, Double> entry : selectedTabLinkId.entrySet()) {

				JSONObject obj = new JSONObject();obj.put(entry.getKey(), entry.getValue());
				selectedTabLink.add(obj);

			}
			
		}

		return selectedTabLink;

	}

	//Search words
	@SuppressWarnings("unchecked")
	public static JSONArray searchLevenshteinDistance(String symbolChars, int concentrationDepth, int orderDirection) throws Exception {

		if (symbolChars.length()==0) {

			throw new Exception("Sorry, the symbol char "+symbolChars+" is not valid [required].");

		}

		LinkedHashMap<String, Double> selectedTabLinkId = new LinkedHashMap<String, Double>();
		
		//Parse all languges
		JSONArray allLanguages = LanguageManager.showAll();
		for(int iLang = 0;iLang<allLanguages.size();iLang++) {
			
			//Get the current language
			String lang = (String) allLanguages.get(iLang);
			
			if (!lang.equals("io")) {
				
				symbolChars = symbolChars.toLowerCase();

				//Parse all letters in the string
				for(int i=0;i<symbolChars.length();i++) {
		
					//Get the current letter
					String currentSymbolChar = symbolChars.substring(i, i+1);
		
					if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {
		
						int iDepth = 0;
						double localDistance = 0;
						
						try {
		
							String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);
			
							if (firstTL!=null) {
			
								//Handle the first tab link
								JSONObject bd = Record2.getNode(firstTL);
			
								String key = (bd.get("k")+"");
								String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
								Object downTL = null;
			
								//Check the word
								if (!selectedTabLinkId.containsKey(word)) {
									Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
									localDistance = l.doubleValue()/word.length()*100;
									selectedTabLinkId.put(word, localDistance);
								}
								iDepth++;
			
								//Handle other tab link
								downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
								
								while (downTL!=null && iDepth<=concentrationDepth) {
			
									bd = Record2.getNode(downTL+"");
			
									key = (bd.get("k")+"");
									word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
			
									//Check the word
									if (!selectedTabLinkId.containsKey(word)) {
										Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
										localDistance = l.doubleValue()/word.length()*100;
										selectedTabLinkId.put(word, localDistance);
									}
			
									iDepth++;
									downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
			
								}
			
							}
							
						} catch (Exception e) {};
		
					}
		
				}
				
				symbolChars = symbolChars.toUpperCase();

				//Parse all letters in the string
				for(int i=0;i<symbolChars.length();i++) {
		
					//Get the current letter
					String currentSymbolChar = symbolChars.substring(i, i+1);
		
					if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {
		
						int iDepth = 0;
						double localDistance = 0;
						
						try {
		
							String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);
			
							if (firstTL!=null) {
			
								//Handle the first tab link
								JSONObject bd = Record2.getNode(firstTL);
			
								String key = (bd.get("k")+"");
								String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
								Object downTL = null;
			
								//Check the word
								if (!selectedTabLinkId.containsKey(word)) {
									Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
									localDistance = l.doubleValue()/word.length()*100;
									selectedTabLinkId.put(word, localDistance);
								}
								iDepth++;
			
								//Handle other tab link
								downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
								
								while (downTL!=null && iDepth<=concentrationDepth) {
			
									bd = Record2.getNode(downTL+"");
			
									key = (bd.get("k")+"");
									word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
			
									//Check the word
									if (!selectedTabLinkId.containsKey(word)) {
										Integer l = StringUtils.getLevenshteinDistance(symbolChars.toLowerCase(), word.toLowerCase());
										localDistance = l.doubleValue()/word.length()*100;
										selectedTabLinkId.put(word, localDistance);
									}
			
									iDepth++;
									downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
			
								}
			
							}
							
						} catch (Exception e) {};
		
					}
		
				}
			
			}
			
			if (iLang == 4) break;
			
		}

		JSONArray selectedTabLink = new JSONArray();
		
		if (orderDirection==1) {
			
			List<Map.Entry<String, Double>> entries =
					new ArrayList<Map.Entry<String, Double>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b){
					return a.getValue().compareTo(b.getValue());
				}
			});
			
			for (Map.Entry<String, Double> entry : entries) {
				
				String key = entry.getKey();
				JSONObject obj = new JSONObject();obj.put(key, entry.getValue());
				selectedTabLink.add(obj);
				
			}
			
		} else if (orderDirection==2) {
			
			List<Map.Entry<String, Double>> entries =
					new ArrayList<Map.Entry<String, Double>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
				public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b){
					return b.getValue().compareTo(a.getValue());
				}
			});
			
			for (Map.Entry<String, Double> entry : entries) {
				
				String key = entry.getKey();
				JSONObject obj = new JSONObject();obj.put(key, entry.getValue());
				selectedTabLink.add(obj);
				
			}
			
		} else {
			
			//Build the JSON and calculate the percent
			for (Map.Entry<String, Double> entry : selectedTabLinkId.entrySet()) {

				String key = entry.getKey();
				JSONObject obj = new JSONObject();obj.put(key, entry.getValue());
				selectedTabLink.add(obj);

			}
			
		}

		return selectedTabLink;

	}

	//Search words
	@SuppressWarnings("unchecked")
	public static JSONArray search(String symbolChars, String regex, int concentrationDepth, int orderDirection, String lang) throws Exception {

		if (symbolChars.length()==0) {

			throw new Exception("Sorry, the symbol char "+symbolChars+" is not valid [required].");

		}

		if (regex.length()==0) {

			throw new Exception("Sorry, the regex "+regex+" is not valid [required].");

		}

		if (!regex.startsWith("(?i)")) regex = "(?i)"+regex;

		Pattern p = Pattern.compile(regex);
		LinkedHashMap<String, Integer> selectedTabLinkId = new LinkedHashMap<String, Integer>();
		int nbMaxConcentration = 0;
		
		symbolChars = symbolChars.toLowerCase();

		//Parse all letters in the string
		for(int i=0;i<symbolChars.length();i++) {

			//Get the current letter
			String currentSymbolChar = symbolChars.substring(i, i+1);

			if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {

				int iDepth = 0;
				int localNbMaxConcentration = 0;

				String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);

				if (firstTL!=null) {

					//Handle the first tab link
					JSONObject bd = Record2.getNode(firstTL);

					String key = (bd.get("k")+"");
					String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
					Object downTL = null;

					//Check the word
					if ((p.matcher(word.toLowerCase())).matches()) {
						if (selectedTabLinkId.containsKey(word)) {
							localNbMaxConcentration = Integer.parseInt(selectedTabLinkId.get(word)+"")+(concentrationDepth-iDepth);
							if (localNbMaxConcentration>selectedTabLinkId.get(word)) selectedTabLinkId.put(word, localNbMaxConcentration);
							if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
						} else {
							localNbMaxConcentration = (concentrationDepth-iDepth);
							selectedTabLinkId.put(word, localNbMaxConcentration);
							if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
						}
					}
					iDepth++;

					//Handle other tab link
					downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
					
					while (downTL!=null && iDepth<=concentrationDepth) {

						bd = Record2.getNode(downTL+"");

						key = (bd.get("k")+"");
						word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);

						//Check the word
						if ((p.matcher(word.toLowerCase())).matches()) {
							if (selectedTabLinkId.containsKey(word)) {
								localNbMaxConcentration = Integer.parseInt(selectedTabLinkId.get(word)+"")+(concentrationDepth-iDepth);
								if (localNbMaxConcentration>selectedTabLinkId.get(word)) selectedTabLinkId.put(word, localNbMaxConcentration);
								if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
							} else {
								localNbMaxConcentration = (concentrationDepth-iDepth);
								selectedTabLinkId.put(word, localNbMaxConcentration);
								if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
							}
						}

						iDepth++;
						downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");

					}

				}

			}

		}
		
		symbolChars = symbolChars.toUpperCase();

		//Parse all letters in the string
		for(int i=0;i<symbolChars.length();i++) {

			//Get the current letter
			String currentSymbolChar = symbolChars.substring(i, i+1);

			if ("<([{\\^-=$!|]})?*+.>".indexOf(currentSymbolChar)==-1) {

				int iDepth = 0;
				int localNbMaxConcentration = 0;

				String firstTL = ExperienceManagerL1.getFirstTabLink("S["+currentSymbolChar+"]", lang, true);

				if (firstTL!=null) {

					//Handle the first tab link
					JSONObject bd = Record2.getNode(firstTL);

					String key = (bd.get("k")+"");
					String word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);
					Object downTL = null;

					//Check the word
					if ((p.matcher(word.toLowerCase())).matches()) {
						if (selectedTabLinkId.containsKey(word)) {
							localNbMaxConcentration = Integer.parseInt(selectedTabLinkId.get(word)+"")+(concentrationDepth-iDepth);
							if (localNbMaxConcentration>selectedTabLinkId.get(word)) selectedTabLinkId.put(word, localNbMaxConcentration);
							if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
						} else {
							localNbMaxConcentration = (concentrationDepth-iDepth);
							selectedTabLinkId.put(word, localNbMaxConcentration);
							if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
						}
					}
					iDepth++;

					//Handle other tab link
					downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");
					
					while (downTL!=null && iDepth<=concentrationDepth) {

						bd = Record2.getNode(downTL+"");

						key = (bd.get("k")+"");
						word = StringFx.hex_to_str(key.substring(3, key.length()-1).split(" ", -1)[0]);

						//Check the word
						if ((p.matcher(word.toLowerCase())).matches()) {
							if (selectedTabLinkId.containsKey(word)) {
								localNbMaxConcentration = Integer.parseInt(selectedTabLinkId.get(word)+"")+(concentrationDepth-iDepth);
								if (localNbMaxConcentration>selectedTabLinkId.get(word)) selectedTabLinkId.put(word, localNbMaxConcentration);
								if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
							} else {
								localNbMaxConcentration = (concentrationDepth-iDepth);
								selectedTabLinkId.put(word, localNbMaxConcentration);
								if (localNbMaxConcentration>nbMaxConcentration) nbMaxConcentration = localNbMaxConcentration;
							}
						}

						iDepth++;
						downTL=((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" S["+currentSymbolChar+"]")).get("dtl");

					}

				}

			}

		}

		JSONArray selectedTabLink = new JSONArray();
		
		if (orderDirection==1) {
		
			List<Map.Entry<String, Integer>> entries =
					new ArrayList<Map.Entry<String, Integer>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
					return a.getValue().compareTo(b.getValue());
				}
			});
			
			for (Map.Entry<String, Integer> entry : entries) {
				
				String key = entry.getKey();
				Integer value = entry.getValue();
				JSONObject obj = new JSONObject();obj.put(key, (value.doubleValue()*100/(nbMaxConcentration)));
				selectedTabLink.add(obj);
				
			}
			
		} else if (orderDirection==2) {
		
			List<Map.Entry<String, Integer>> entries =
					new ArrayList<Map.Entry<String, Integer>>(selectedTabLinkId.entrySet());
			Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
					return b.getValue().compareTo(a.getValue());
				}
			});
			
			for (Map.Entry<String, Integer> entry : entries) {
				
				String key = entry.getKey();
				Integer value = entry.getValue();
				JSONObject obj = new JSONObject();obj.put(key, (value.doubleValue()*100/(nbMaxConcentration)));
				selectedTabLink.add(obj);
				
			}
			
		} else {
		
			//Build the JSON and calculate the percent
			for (Map.Entry<String, Integer> entry : selectedTabLinkId.entrySet()) {

				String key = entry.getKey();
				Integer value = entry.getValue();
				
				JSONObject obj = new JSONObject();obj.put(key, (value.doubleValue()*100/(nbMaxConcentration)));
				selectedTabLink.add(obj);

			}
			
		}

		return selectedTabLink;

	}

	//Delete a word
	public static void deleteLang(String wordName, String lang) throws Exception {
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec==null) {
			
			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		} else {
			
			//Check if the word already exist
			if (!((JSONObject) rec.get("fl")).containsKey(lang)) {

				throw new Exception("Sorry, the word "+wordName+" does not exist in "+lang+".");
				
			} else {
				
				//Get all thoughts
				JSONArray thoughtList = ThoughtManager.list(wordName, lang);
				
				//Delete all thoughts
				for(int i=0;i<thoughtList.size();i++) {
					
					ThoughtManager.delete(wordName, ""+thoughtList.get(i), lang);
					
				}
				
			}
			
		}

	}

	//Delete a word
	public static void delete(String wordName) throws Exception {
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec==null) {
			
			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		} else {
			
			for(Object key : ((JSONObject) rec.get("fl")).keySet()) {
				
				String lang = (String)key;
			
				//Get all thoughts
				JSONArray thoughtList = ThoughtManager.list(wordName, lang);
				
				//Delete all thoughts
				for(int i=0;i<thoughtList.size();i++) {
					
					ThoughtManager.delete(wordName, ""+thoughtList.get(i), lang);
					
				}
				
			}
			
		}

	}
	
	//Get the first word tab link
	public static String firstTabLink(String word, String lang) throws Exception {
		
		if (word.length()==0) {

			throw new Exception("Sorry, the word "+word+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+word+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+word+" does not exist.");

		}
		
		return ExperienceManagerL1.getFirstTabLink("W["+word+"]", lang, true);

	}
	
	//Get the last word tab link
	public static String lastTabLink(String word, String lang) throws Exception {
		
		if (word.length()==0) {

			throw new Exception("Sorry, the word "+word+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+word+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+word+" does not exist.");

		}
		
		return ExperienceManagerL1.getLastTabLink("W["+word+"]", lang, true);

	}
	
	//Get word tab link
	public static String getTabLink(String word, int position, String lang) throws Exception {
				
		JSONArray tab = ThoughtManager.list(word, lang);
		
		if (position>=tab.size()) {

			throw new Exception("Sorry, not found tab link at the position "+position+" for the word W["+word+"].");

		}
		
		return ""+tab.get(position);

	}
	
	//Show word languages
	@SuppressWarnings("unchecked")
	public static JSONArray showLanguages(String word) throws Exception {
				
		//Initialization
		JSONArray result = new JSONArray();
		
		JSONObject rec = Record2.getNode("W["+word+"]");
		
		if (rec==null) {
			throw new Exception("Sorry, the word W["+word+"] does not exist.");
		}
		
		for(Object key : ((JSONObject) rec.get("fl")).keySet()) {
			
			String lang = (String)key;
			
			result.add(lang);
			
		}

		return result;

	}
	
	//Show word tab links
	public static JSONArray showTabLinks(String word, String lang) throws Exception {
		
		return ThoughtManager.list(word, lang);

	}
	
	//Show symbol perception
	@SuppressWarnings("unchecked")
	public static JSONArray showSymbolPerception(String word, String lang) throws Exception {
				
		//Initialization
		JSONArray result = new JSONArray();
		
		JSONObject rec = Record2.getNode("W["+word+"]");
		
		if (rec==null) {
			throw new Exception("Sorry, the word W["+word+"] does not exist.");
		}
		
		int maxPerception = 0;
		
		//Parse all letters in the word
		for(int i=0;i<word.length();i++) {
			
			JSONObject obj = new JSONObject();
			String k="S["+word.charAt(i)+"]";
			obj.put("k", k);
			
			JSONObject bd = Record2.getNode(k);
			int perception = Integer.parseInt(""+bd.get("w"));
			maxPerception += perception;
			obj.put("w", perception);
			
			result.add(obj);
			
		}
		
		for(int i=0;i<result.size();i++) {
			
			//Get the current object
			JSONObject obj = (JSONObject) result.get(i);
			
			obj.put("mw", maxPerception);

			if (maxPerception==0) obj.put("v", 0D);
			else obj.put("v", Double.parseDouble(""+obj.get("w"))/Double.parseDouble(""+maxPerception)*100);
			
		}

		return result;

	}
	
	//Show symbol perception
	@SuppressWarnings("unchecked")
	public static JSONArray showTabLinkPerceptions(String word, String lang) throws Exception {
				
		//Initialization
		JSONArray result = new JSONArray();
		
		JSONObject rec = Record2.getNode("W["+word+"]");
		
		if (rec==null) {
			throw new Exception("Sorry, the word W["+word+"] does not exist.");
		}
		
		int maxPerception = 0;
		
		//Parse all letters in the word
		for(int i=0;i<word.length();i++) {
			
			JSONObject obj = new JSONObject();
			String k="TL["+StringFx.str_to_hex(word)+" "+i+" "+lang+"]";
			obj.put("k", k);
			
			JSONObject bd = Record2.getNode(k);
			int perception = Integer.parseInt(""+bd.get("w"));
			maxPerception += perception;
			obj.put("w", perception);
			
			result.add(obj);
			
		}
		
		for(int i=0;i<result.size();i++) {
			
			//Get the current object
			JSONObject obj = (JSONObject) result.get(i);
			
			obj.put("mw", maxPerception);

			if (maxPerception==0) obj.put("v", 0D);
			else obj.put("v", Double.parseDouble(""+obj.get("w"))/Double.parseDouble(""+maxPerception)*100);
			
		}

		return result;

	}

	//Show thought perception
	@SuppressWarnings("unchecked")
	public static JSONArray showThoughtPerceptions(String word, String lang) throws Exception {
				
		//Initialization
		JSONArray result = new JSONArray();
		
		JSONObject rec = Record2.getNode("W["+word+"]");
		
		if (rec==null) {
			throw new Exception("Sorry, the word W["+word+"] does not exist.");
		}
		
		int maxPerception = 0;
		
		//Get the first tab link
		String currentTabLink = ExperienceManagerL1.getFirstTabLink("W["+word+"]", lang, true);
		
		//Parse all tab links
		while (currentTabLink!=null) {
			
			JSONObject bd = Record2.getNode(currentTabLink);
			int perception = Integer.parseInt(""+bd.get("w"));
			maxPerception += perception;
			JSONObject obj = new JSONObject();
			obj.put("k", currentTabLink);
			obj.put("w", perception);
			
			result.add(obj);
			currentTabLink = ExperienceManagerL1.getDownTabLink("W["+word+"]", currentTabLink, lang, true);
			
		}
		
		for(int i=0;i<result.size();i++) {
			
			//Get the current object
			JSONObject obj = (JSONObject) result.get(i);
			
			obj.put("mw", maxPerception);

			if (maxPerception==0) obj.put("v", 0D);
			else obj.put("v", Double.parseDouble(""+obj.get("w"))/Double.parseDouble(""+maxPerception)*100);
			
		}
		
		return result;
		
	}
	
	//Show word tab links
	public static JSONArray showTabLinks(String word) throws Exception {
		
		return ThoughtManager.list(word);

	}

}
