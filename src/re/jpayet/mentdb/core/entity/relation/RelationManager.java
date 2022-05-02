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

package re.jpayet.mentdb.core.entity.relation;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.BasicNode;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.cognition.CognSearch;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL2;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.stimulation.StimulationManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class RelationManager {

	//If you add a new type then add also in the editor (function: selectBranchNode)
	//P = Pilot
	//S = Strategy
	//C = Context
	//F = Filter
	//G = Generic Group
	//O = Logic Group
	//1 = Article Group
	//2 = Other Group
	//L = Logic (always true) > Others
	//T = Logic (always true) > Data type
	//A = Article
	//I = Input
	//O = Output
	//U = Usage
	//D = Directional
	//R = Response
	//N = Need
	public static String allTypeList = "L";
	
	//Get info
	public static String getInfo(String relationId, EnvManager env, SessionThread session) throws Exception {
		
		String result = relationId+": ";

		JSONObject bd = Record2.getNode(relationId);
		String lang = ""+bd.get("l");
		
		JSONArray words = RelationManager.showWords(relationId, lang, new Vector<MQLValue>(), env, session);
		for(int i=0;i<words.size();i++) {
			
			Random ran = new Random();
			JSONArray wlist = ((JSONArray) words.get(i));
			String selectedWord = (""+((JSONObject) wlist.get(ran.nextInt(wlist.size()))).get("word"));
			
			String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
			
			for(int z=0;z<splitedWord.length;z++) {
				
				//Get the splited word
				String w = splitedWord[z];
				
				result += w+" ";
				
			}
			
		}
		result += "\n";

		result += "thoughts: ";
		JSONArray thoughts = RelationManager.showThoughtsRecursivelyRLTH(relationId);
		for(int i=0;i<thoughts.size();i++) {
			String key = thoughts.get(i)+"";
			result += key+" ";
		}
		result += "\n";
		
		result += "nodes: ";
		thoughts = RelationManager.showThoughtNodes(relationId);
		for(int i=0;i<thoughts.size();i++) {
			String key = thoughts.get(i)+"";
			result += key+" ";
		}
		result += "\n";
		
		return result;
		
	}
	
	//Show network
	public static int firstNotSomethingPosition(String relationId) throws Exception {
		
		//Initialization
		int result = 0;
		JSONArray a = RelationManager.showThoughtNodes(relationId);
		
		for(int i=0;i<a.size();i++) {
			
			//Get the current value
			String curVal = a.get(i)+"";
			if (!curVal.equals("TH[]")) {
				result = i+1;
				break;
			}
			
		}
		
		return result;
		
	}
	

	
	//Show network
	@SuppressWarnings("unchecked")
	public static void showNetwork_getTabLinks(String relationId, String type, JSONObject tlResult) throws Exception {
		
		//Get all tab links
		JSONArray tl = RelationManager.showTabLinks(relationId, type, ConcentrationManager.getConcentrationDepth("C[relation]"));
		for(int i=0;i<tl.size();i++) {
			
			String key = ""+tl.get(i);
			key = Misc.atom(key, 1, " ").substring(1);
			
			if (tlResult.containsKey(key)) {
				tlResult.put(key, ((Integer) tlResult.get(key))+1);
			} else {
				tlResult.put(key, 1);
			}
			
		}
		
	}
	
	//Show network
	@SuppressWarnings("unchecked")
	public static JSONObject showNetwork(String relationId) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		JSONObject tlResult = new JSONObject(new LinkedHashMap<String, Integer>());

		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			showNetwork_getTabLinks(relationId, RelationManager.allTypeList.charAt(iType)+"", tlResult);
		}
		
		//Initialization
		JSONObject thResult = new JSONObject(new LinkedHashMap<String, Integer>());
		
		//Get all thoughts
		JSONArray tl = RelationManager.showThoughtNodes(relationId);
		
		for(int i=0;i<tl.size();i++) {
			
			String key = ""+tl.get(i);
			
			if (key.startsWith("R")) {
				
				//Only if this is a relation
				
				if (thResult.containsKey(key)) {
					thResult.put(key, ((Integer) thResult.get(key))+1);
				} else {
					thResult.put(key, 1);
				}
				
			}
			
		}
		
		result .put("tl", tlResult);
		result .put("th", thResult);
		
		return result;
		
	}
	
	//Show words in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showWords(String relationId, String lang, Vector<MQLValue> inputVector, EnvManager env, SessionThread session) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get thoughts
		JSONArray thoughts = showThoughtNodes(relationId);
		
		//Get all words
		for(int i=0;i<thoughts.size();i++) {
			
			if (!(""+thoughts.get(i)).startsWith("R")) result.add(ThoughtManager.getWords(""+thoughts.get(i), lang, inputVector));
			else {
				JSONArray array = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("lang", lang);
				
				String w = showWordsRecurcive(""+thoughts.get(i), lang, inputVector, env, session);
				if (w.endsWith(" ")) w = w.substring(0, w.length()-1);
				obj.put("word", "W["+w+"]");
				array.add(obj);
				result.add(array);
			}

		}

		return result;
		
	}
	
	//Show words in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showWords_no_translate(String relationId, String lang, Vector<MQLValue> inputVector, EnvManager env, SessionThread session) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get thoughts
		JSONArray thoughts = showThoughtNodes(relationId);
		
		//Get all words
		for(int i=0;i<thoughts.size();i++) {
			
			if (!(""+thoughts.get(i)).startsWith("R")) result.add(ThoughtManager.getWords(""+thoughts.get(i), lang, inputVector));
			else {
				
				String res = "";
				Random ran = new Random();
				JSONArray words = showWords_no_translate(""+thoughts.get(i), lang, new Vector<MQLValue>(), env, session);
				for(int j=0;j<words.size();j++) {
					
					JSONArray wlist = ((JSONArray) words.get(j));
					String selectedWord = (""+((JSONObject) wlist.get(ran.nextInt(wlist.size()))).get("word"));
					
					String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
					
					for(int z=0;z<splitedWord.length;z++) {
						
						//Get the splited word
						String ww = splitedWord[z];
						
						res+=" "+ww;
						
					}
					
				}
				
				if (!res.equals("")) res = res.substring(1);
				
				JSONArray array = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("lang", lang);
				obj.put("word", "W["+res+"]");
				array.add(obj);
				result.add(array);
				
			}

		}

		return result;
		
	}
	
	//Show words in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showWordsForTranslation(String relationId, String lang, Vector<MQLValue> inputVector, EnvManager env, SessionThread session, String lock_lang) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get thoughts
		JSONArray thoughts = showThoughtNodes(relationId);
		
		//Get all words
		for(int i=0;i<thoughts.size();i++) {
			
			if (!(""+thoughts.get(i)).startsWith("R")) result.add(ThoughtManager.getWordsForTranslation(""+thoughts.get(i), lang, inputVector, lock_lang));
			else {
				JSONArray array = new JSONArray();
				JSONObject obj = new JSONObject();
				obj.put("lang", lang);
				
				String w = showWordsRecurcive(""+thoughts.get(i), lang, inputVector, env, session);
				if (w.endsWith(" ")) w = w.substring(0, w.length()-1);
				obj.put("word", "W["+w+"]");
				array.add(obj);
				result.add(array);
			}

		}

		return result;
		
	}
	
	//Show words in a relation
	public static String showWordsRecurcive(String relationId, String lang, Vector<MQLValue> inputVector, EnvManager env, SessionThread session) throws Exception {
		
		return translate(relationId, lang, false, inputVector, env, session, 100, "");
		
	}
	
	//Show thought nodes in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showThoughtNodes(String relationId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get the node
		JSONObject recNode = Record2.getNode(relationId);
		
		if (recNode==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		String[] thoughts = (""+recNode.get("lth")).split(" ", -1);
		
		for(int i=0;i<thoughts.length;i++) {
			
			result.add(thoughts[i]);
			
		}
		
		return result;
		
	}
	
	//Show thoughts recursively in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showThoughtsRecursively(String relationId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get the node
		JSONObject recNode = Record2.getNode(relationId);
		
		if (recNode==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		String[] thoughts = (""+recNode.get("lth")).split(" ", -1);
		
		for(int i=0;i<thoughts.length;i++) {
			
			if (thoughts[i].startsWith("T")) result.add(thoughts[i]);
			else {
				JSONArray array = showThoughtsRecursively(thoughts[i]);
				for(int z=0;z<array.size();z++) {
					result.add(array.get(z)+"");
				}
			}
			
		}
		
		return result;
		
	}
	
	//Show thoughts recursively in a relation
	@SuppressWarnings("unchecked")
	public static void showTh(String relationId, JSONObject result, int pos) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(relationId);
		
		if (recNode==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		if (pos==0) {
			
			String parent = (""+recNode.get("p"));
			showTh(parent, result, 1);
			
		}
		
		String[] thoughts = (""+recNode.get("lth")).split(" ", -1);
		boolean change_th = true;
		
		for(int i=0;i<thoughts.length;i++) {
			
			JSONObject ud = (JSONObject) Record2.getNode("["+relationId+" "+(i+1)+"/"+thoughts.length+"]").get("ud");
			
			Set<String> entrySet = ud.keySet();
			for(String entry : entrySet) {
				
				if (entry!=null && !entry.equals("")) {
					
					if (entry.startsWith("TH") || entry.startsWith("R")) {
						
						if (change_th) {
							pos++;
						}
						
						if (result.containsKey("TH"+pos)) result.put("TH"+pos, result.get("TH"+pos)+" "+entry);
						else result.put("TH"+pos, entry);
						
						change_th = false;
						
					} else {
						change_th = true;
					}
					
					break;
					
				}
				
			}
			
		}
		
	}
	
	//Show thoughts recursively in a relation
	@SuppressWarnings("unchecked")
	public static JSONArray showThoughtsRecursivelyRLTH(String relationId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get the node
		JSONObject recNode = Record2.getNode(relationId);
		
		if (recNode==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		String[] thoughts = (""+recNode.get("rlth")).split(" ", -1);
		
		for(int i=0;i<thoughts.length;i++) {
			
			result.add(thoughts[i]);
			
		}
		
		return result;
		
	}
	
	//Show a relation
	@SuppressWarnings("unchecked")
	public static JSONArray listTabLinks(String relationId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get the node
		JSONObject recNode = Record2.getNode(relationId);
		
		if (recNode==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		long nb = (Long) recNode.get("n");
		
		for(int i=1;i<=nb;i++) {
			
			result.add("["+relationId+" "+i+"/"+nb+"]");
			
		}
		
		return result;
		
	}
	
	//Show a translation of a relation
	@SuppressWarnings("unchecked")
	public static String translate(String relationId, String lang, boolean first, Vector<MQLValue> inputVector, EnvManager env, SessionThread session, int cooperation, String level) throws Exception {
		
		if (relationId.indexOf(" ")>-1 || relationId.startsWith("TH")) {
			
			String result = "";
			String[] tab = relationId.split(" ");
			Random ran = new Random();
			for(int i=0;i<tab.length;i++) {
				
				String t_r = tab[i];
				if (t_r.startsWith("TH")) {
					
					JSONArray wlist = ThoughtManager.getWordsForTranslation(t_r, lang, inputVector, lang);
					
					int rand = ran.nextInt(wlist.size());
					String selectedWord = (""+((JSONObject) wlist.get(rand)).get("word"));
					
					String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
					
					for(int z=0;z<splitedWord.length;z++) {
						
						//Get the splited word
						String w = splitedWord[z];
						
						if (result.endsWith("'") || result.endsWith("-")  || w.equals("'")  || w.equals("-") || w.equals("?") || w.equals(".")) result+=w;
						result+=" "+w;
						
					}
					
				} else {
					
					String w = translate(relationId, lang, first, inputVector, env, session, cooperation, level);
					if (result.endsWith("'") || result.endsWith("-")  || w.equals("'")  || w.equals("-") || w.equals("?") || w.equals(".")) result += w;
					else result += " "+w;
					
				}
				
			}	
			
			if (first) return StringFx.first_letter_upper(result.substring(1));
			else return result.substring(1);
					
		}
		
		//Initialization
		Random ran = new Random();
		
		if (ran.nextInt(100)>=cooperation) {
			
			return null;
			
		}
		
		String selectedRelationId = relationId;
		JSONArray direct_level_ok_lang_ok = new JSONArray();
		JSONArray direct_level_ko_lang_ok = new JSONArray();
		String parentRelationId = null;
		JSONArray parent_level_ok_lang_ok = new JSONArray();
		JSONArray parent_level_ko_lang_ok = new JSONArray();
		JSONArray level_ok_lang_ko = new JSONArray();
		JSONArray level_ko_lang_ko = new JSONArray();
		
		//Circle id
		JSONArray circleIds = CircleManager.getIds("r", relationId);
		for(int i_c=0;i_c<circleIds.size();i_c++) {

			String r_key = (String) circleIds.get(i_c);
			JSONObject r_json = Record2.getNode(r_key);
			String r_lang = (String) r_json.get("l");
			
			if (level.equals("")) {
				
				if (r_lang.equals(lang)) {
					
					direct_level_ok_lang_ok.add(r_key);
					
				} else {
					
					level_ok_lang_ko.add(r_key);
					
				}
				
			} else {
				
				String lv = (String) r_json.get("lv");
				
				if (lv.equals("") || lv.equals(level)) {
				
					if (r_lang.equals(lang)) {
						
						direct_level_ok_lang_ok.add(r_key);
						
					} else {
						
						level_ok_lang_ko.add(r_key);
						
					}
					
				} else {
					
					if (r_lang.equals(lang)) {
						
						direct_level_ko_lang_ok.add(r_key);
						
					} else {
						
						level_ko_lang_ko.add(r_key);
						
					}
					
				}
				
			}
			
		}
		
		//Try to get the parent id
		JSONObject recRelation = Record2.getNode(relationId);
		
		if (recRelation.containsKey("p")) {
			
			parentRelationId = ""+recRelation.get("p");
			
			//Circle id
			circleIds = CircleManager.getIds("r", parentRelationId);
			for(int i_c=0;i_c<circleIds.size();i_c++) {

				String r_key = (String) circleIds.get(i_c);
				JSONObject r_json = Record2.getNode(r_key);
				String r_lang = (String) r_json.get("l");
				
				if (level.equals("")) {
					
					if (r_lang.equals(lang)) {
						
						parent_level_ok_lang_ok.add(r_key);
						
					} else {
						
						level_ok_lang_ko.add(r_key);
						
					}
					
				} else {
					
					String lv = (String) r_json.get("lv");
					
					if (lv.equals("") || lv.equals(level)) {
					
						if (r_lang.equals(lang)) {
							
							parent_level_ok_lang_ok.add(r_key);
							
						} else {
							
							level_ok_lang_ko.add(r_key);
							
						}
						
					} else {
						
						if (r_lang.equals(lang)) {
							
							parent_level_ko_lang_ok.add(r_key);
							
						} else {
							
							level_ko_lang_ko.add(r_key);
							
						}
						
					}
					
				}
				
			}
			
		}
		
		if (direct_level_ok_lang_ok.size()>0) {
			
			while (direct_level_ok_lang_ok.size()>0) {
			
				int g_pos = ran.nextInt(direct_level_ok_lang_ok.size());
				selectedRelationId = (String) direct_level_ok_lang_ok.get(g_pos);
				direct_level_ok_lang_ok.remove(g_pos);
				
				JSONObject selected_direct = Record2.getNode(selectedRelationId);
				JSONObject mqls = (JSONObject) selected_direct.get("dco");
				
				boolean b = true;
				
				//Check conditions
				for(int i=0;i<inputVector.size();i++) {
					
					env.set("[TH"+(i+1)+"]", inputVector.get(i).value);
					
					String mql = (String) mqls.get("TH"+(i+1)+"");
					
					if (mql!=null && !mql.equals("")) {
						
						try {
							
							if (CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, null, null).equals("1")) { 
								
							} else {
								b = false;
								break;
							}
							
						} catch (Exception e) {
							b = false;
							break;
						}
						
					}
					
				}
				
				if (b) {
					
					if (first) return StringFx.first_letter_upper(showSentenceWithInput(selectedRelationId, env, session, inputVector));
					else return showSentenceWithInput(selectedRelationId, env, session, inputVector);
				
				}
				
			}
			
		}
		
		if (parent_level_ok_lang_ok.size()>0) {
			
			while (parent_level_ok_lang_ok.size()>0) {
			
				int g_pos = ran.nextInt(parent_level_ok_lang_ok.size());
				selectedRelationId = (String) parent_level_ok_lang_ok.get(g_pos);
				parent_level_ok_lang_ok.remove(g_pos);
				
				JSONArray parent_thoughts = showThoughtNodes(parentRelationId);
				JSONArray relation_thoughts = showThoughtNodes(relationId);
	
				JSONObject thought_words = new JSONObject();
				
				//Search variables from parent relation
				int j=1;
				for(int i=0;i<parent_thoughts.size();i++) {
					
					if ((parent_thoughts.get(i)+"").equals("TH[]")) {
						
						JSONArray t_w = new JSONArray();
						thought_words.put("[TH"+j+"]", t_w);
						String r_or_th = (relation_thoughts.get(i)+"");
						
						if (r_or_th.startsWith("TH")) {
							
							JSONArray a_w = ThoughtManager.getWords(r_or_th, lang, inputVector);
							for(int z=0;z<a_w.size();z++) {
								
								JSONObject o = (JSONObject) a_w.get(z);
								String w = (String) o.get("word");
								w = w.substring(2, w.length()-1);
								t_w.add(w);
								
							}
							
						} else {
							
							t_w.add(translate(r_or_th, lang, false, inputVector, env, session, 100, level));
							
						}
						
						j++;
						
					}
					
				}
				
				//Check condition
				JSONObject selected_parent = Record2.getNode(selectedRelationId);
				JSONObject mqls = (JSONObject) selected_parent.get("dco");
				boolean b = true;
				
				if (mqls.size()>0) {
					
					for(int i=0;i<thought_words.size();i++) {
						
						JSONArray words = (JSONArray) thought_words.get("[TH"+(i+1)+"]");
						String mql = (String) mqls.get("TH"+(i+1)+"");
						
						if (mql!=null && !mql.equals("")) {
							
							while (words.size()>0) {
								
								int pos = ran.nextInt(words.size());
								String w = (String) words.get(pos);
								
								env.set("[TH"+(i+1)+"]", w);
								
								try {
									
									if (CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, null, null).equals("1")) { 
										words.clear();
										words.add(w);
										break;
									} else {
										words.remove(pos);
									}
									
								} catch (Exception e) {
									
									words.remove(pos);
								}
								
							}
							
							if (words.size()==0) {
								b = false;
								break;
							}
							
						}
						
					}
					
				};
				
				if (b) {
					
					//Load selected parent thought nodes and add TH[]
					JSONArray selected_parent_thoughts = showThoughtNodes(selectedRelationId);
					int jj = 1;
					String sentence = "";
					for(int i=0;i<selected_parent_thoughts.size();i++) {
						
						String r_or_th = (selected_parent_thoughts.get(i)+"");
						
						if (r_or_th.equals("TH[]")) {
							
							String txt = (String) ((JSONArray) thought_words.get("[TH"+jj+"]")).get(0);
							
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
							jj++;
							
						} else if (r_or_th.startsWith("TH")) {
							
							JSONArray a_w = ThoughtManager.getWords(r_or_th, lang, inputVector);
							String txt = "";
							for(int z=0;z<a_w.size();z++) {
								
								String w = (String) ((JSONObject) a_w.get(z)).get("word");
								w = w.substring(2, w.length()-1);
								txt+=" "+w;
								
							}
							
							if (txt.length()>0) txt = txt.substring(1);
							
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
						} else {
							
							String txt = translate(r_or_th, lang, false, inputVector, env, session, 100, level);
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
						}
						
					}
					
					if (sentence.length()>0) sentence = sentence.substring(1);
					
					if (first) return StringFx.first_letter_upper(sentence);
					else return sentence;
					
				}
				
			}
			
		}
		
		if (direct_level_ko_lang_ok.size()>0) {
			
			while (direct_level_ko_lang_ok.size()>0) {
				
				int g_pos = ran.nextInt(direct_level_ko_lang_ok.size());
				selectedRelationId = (String) direct_level_ko_lang_ok.get(g_pos);
				direct_level_ko_lang_ok.remove(g_pos);
				
				JSONObject selected_direct = Record2.getNode(selectedRelationId);
				JSONObject mqls = (JSONObject) selected_direct.get("dco");
				
				boolean b = true;
				
				//Check conditions
				for(int i=0;i<inputVector.size();i++) {
					
					env.set("[TH"+(i+1)+"]", inputVector.get(i).value);
					
					String mql = (String) mqls.get("TH"+(i+1)+"");
					
					if (mql!=null && !mql.equals("")) {
						
						try {
							
							if (CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, null, null).equals("1")) { 
								
							} else {
								b = false;
								break;
							}
							
						} catch (Exception e) {
							b = false;
							break;
						}
						
					}
					
				}
				
				if (b) {
					
					if (first) return StringFx.first_letter_upper(showSentenceWithInput(selectedRelationId, env, session, inputVector));
					else return showSentenceWithInput(selectedRelationId, env, session, inputVector);
				
				}
				
			}
			
		}
		
		if (parent_level_ko_lang_ok.size()>0) {
			
			while (parent_level_ko_lang_ok.size()>0) {
				
				int g_pos = ran.nextInt(parent_level_ko_lang_ok.size());
				selectedRelationId = (String) parent_level_ko_lang_ok.get(g_pos);
				parent_level_ko_lang_ok.remove(g_pos);
				
				JSONArray parent_thoughts = showThoughtNodes(parentRelationId);
				JSONArray relation_thoughts = showThoughtNodes(relationId);
	
				JSONObject thought_words = new JSONObject();
				
				//Search variables from parent relation
				int j=1;
				for(int i=0;i<parent_thoughts.size();i++) {
					
					if ((parent_thoughts.get(i)+"").equals("TH[]")) {
						
						JSONArray t_w = new JSONArray();
						thought_words.put("[TH"+j+"]", t_w);
						String r_or_th = (relation_thoughts.get(i)+"");
						
						if (r_or_th.startsWith("TH")) {
							
							JSONArray a_w = ThoughtManager.getWords(r_or_th, lang, inputVector);
							for(int z=0;z<a_w.size();z++) {
								
								JSONObject o = (JSONObject) a_w.get(z);
								String w = (String) o.get("word");
								w = w.substring(2, w.length()-1);
								t_w.add(w);
								
							}
							
						} else {
							
							t_w.add(translate(r_or_th, lang, false, inputVector, env, session, 100, level));
							
						}
						
						j++;
						
					}
					
				}
				
				//Check condition
				JSONObject selected_parent = Record2.getNode(selectedRelationId);
				JSONObject mqls = (JSONObject) selected_parent.get("dco");
				boolean b = true;
				
				if (mqls.size()>0) {
					
					for(int i=0;i<thought_words.size();i++) {
						
						JSONArray words = (JSONArray) thought_words.get("[TH"+(i+1)+"]");
						String mql = (String) mqls.get("TH"+(i+1)+"");
						
						if (mql!=null && !mql.equals("")) {
							
							while (words.size()>0) {
								
								int pos = ran.nextInt(words.size());
								String w = (String) words.get(pos);
								
								env.set("[TH"+(i+1)+"]", w);
								
								try {
									
									if (CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, null, null).equals("1")) { 
										words.clear();
										words.add(w);
										break;
									} else {
										words.remove(pos);
									}
									
								} catch (Exception e) {
									
									words.remove(pos);
								}
								
							}
							
							if (words.size()==0) {
								b = false;
								break;
							}
							
						}
						
					}
					
				};
				
				if (b) {
					
					//Load selected parent thought nodes and add TH[]
					JSONArray selected_parent_thoughts = showThoughtNodes(selectedRelationId);
					int jj = 1;
					String sentence = "";
					for(int i=0;i<selected_parent_thoughts.size();i++) {
						
						String r_or_th = (selected_parent_thoughts.get(i)+"");
						
						if (r_or_th.equals("TH[]")) {
							
							String txt = (String) ((JSONArray) thought_words.get("[TH"+jj+"]")).get(0);
							
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
							jj++;
							
						} else if (r_or_th.startsWith("TH")) {
							
							JSONArray a_w = ThoughtManager.getWords(r_or_th, lang, inputVector);
							String txt = "";
							for(int z=0;z<a_w.size();z++) {
								
								String w = (String) ((JSONObject) a_w.get(z)).get("word");
								w = w.substring(2, w.length()-1);
								txt+=" "+w;
								
							}
							
							if (txt.length()>0) txt = txt.substring(1);
							
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
						} else {
							
							String txt = translate(r_or_th, lang, false, inputVector, env, session, 100, level);
							if (sentence.endsWith("'") || sentence.endsWith("-")  || txt.equals("'")  || txt.equals("-") || txt.equals("?") || txt.equals(".")) sentence += txt;
							else sentence += " "+txt;
							
						}
						
					}
					
					if (sentence.length()>0) sentence = sentence.substring(1);
					
					if (first) return StringFx.first_letter_upper(sentence);
					else return sentence;
					
				}
				
			}
			
		}
		
		if (level_ok_lang_ko.size()>0) {
			
			int g_pos = ran.nextInt(level_ok_lang_ko.size());
			selectedRelationId = (String) level_ok_lang_ko.get(g_pos);
			level_ok_lang_ko.remove(g_pos);
			
			if (first) return StringFx.first_letter_upper(showSentenceWithInputForTranslation(selectedRelationId, lang, env, session, inputVector));
			else return showSentenceWithInputForTranslation(selectedRelationId, lang, env, session, inputVector);
			
		}
		
		if (level_ko_lang_ko.size()>0) {
			
			int g_pos = ran.nextInt(level_ko_lang_ko.size());
			selectedRelationId = (String) level_ko_lang_ko.get(g_pos);
			level_ko_lang_ko.remove(g_pos);
			
			if (first) return StringFx.first_letter_upper(showSentenceWithInputForTranslation(selectedRelationId, lang, env, session, inputVector));
			else return showSentenceWithInputForTranslation(selectedRelationId, lang, env, session, inputVector);
			
		}
		
		if (first) return StringFx.first_letter_upper(showSentenceWithInputForTranslation(relationId, lang, env, session, inputVector));
		else return showSentenceWithInputForTranslation(relationId, lang, env, session, inputVector);
		
	}
	
	//Show sentence
	public static String showSentence(String relationId, EnvManager env, SessionThread session) throws Exception {

		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(relationId);

		if (recRelation==null) {

			throw new Exception("Sorry, the relation "+relationId+" does not exist.");

		}

		JSONObject bd = Record2.getNode(relationId);
		String lang = ""+bd.get("l");
		
		String result = "";
		Random ran = new Random();
		JSONArray words = showWords_no_translate(relationId, lang, new Vector<MQLValue>(), env, session);
		for(int i=0;i<words.size();i++){
			
			JSONArray wlist = ((JSONArray) words.get(i));
			String selectedWord = (""+((JSONObject) wlist.get(ran.nextInt(wlist.size()))).get("word"));
			
			String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
			
			for(int z=0;z<splitedWord.length;z++) {
				
				//Get the splited word
				String w = splitedWord[z];
				
				result+=" "+w;
				
			}
			
		}
		
		if (!result.equals("")) result = result.substring(1);
		
		return result;
		
	}
	
	//Show sentence
	public static String showSentenceWithInput(String relationId, EnvManager env, SessionThread session, Vector<MQLValue> inputVector) throws Exception {

		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(relationId);

		if (recRelation==null) {

			throw new Exception("Sorry, the relation "+relationId+" does not exist.");

		}

		JSONObject bd = Record2.getNode(relationId);
		String lang = ""+bd.get("l");
		
		String result = "";
		Random ran = new Random();
		JSONArray words = showWords(relationId, lang, inputVector, env, session);
		for(int i=0;i<words.size();i++){
			
			JSONArray wlist = ((JSONArray) words.get(i));
			String selectedWord = (""+((JSONObject) wlist.get(ran.nextInt(wlist.size()))).get("word"));
			
			String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
			
			for(int z=0;z<splitedWord.length;z++) {
				
				//Get the splited word
				String w = splitedWord[z];
				
				if (result.endsWith("'") || result.endsWith("-")  || w.equals("'")  || w.equals("-") || w.equals("?") || w.equals(".")) result+=w;
				else result+=" "+w;
				
			}
			
		}
		
		if (!result.equals("")) result = result.substring(1);
		
		return result;
		
	}
	
	//Show sentence
	public static String showSentenceWithInputForTranslation(String relationId, String lang, EnvManager env, SessionThread session, Vector<MQLValue> inputVector) throws Exception {

		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(relationId);

		if (recRelation==null) {

			throw new Exception("Sorry, the relation "+relationId+" does not exist.");

		}

		String result = "";
		Random ran = new Random();
		JSONArray words = showWordsForTranslation(relationId, lang, inputVector, env, session, lang);
		for(int i=0;i<words.size();i++) {
			
			JSONArray wlist = ((JSONArray) words.get(i));
			String selectedWord = (""+((JSONObject) wlist.get(ran.nextInt(wlist.size()))).get("word"));
			
			String[] splitedWord = selectedWord.substring(2, selectedWord.length()-1).split(" ");
			
			for(int z=0;z<splitedWord.length;z++) {
				
				//Get the splited word
				String w = splitedWord[z];
				
				if (result.endsWith("'") || result.endsWith("-")  || w.equals("'")  || w.equals("-") || w.equals("?") || w.equals(".")) result+=w;
				else result+=" "+w;
				
			}
			
		}
		
		if (!result.equals("")) result = result.substring(1);
		
		return result;
		
	}
	
	//Delete a basic relation
	public static void delete(String relationId) throws Exception {

		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Get all tab link id into a relation
		JSONArray tabLinkIds = RelationManager.listTabLinks(relationId);
		String type = relationId.substring(1, 2);
		
		//Parse all tab link id in the relation
		for(int i=0;i<tabLinkIds.size();i++) {

			//Get the current parent id
			String relationTL = tabLinkIds.get(i)+"";
			JSONObject bd = Record2.getNode(relationTL);
			String parentId = "";
			JSONObject ud = ((JSONObject) bd.get("ud"));
			for(Object key : ud.keySet()) {
				parentId = (String)key;
			}
			if (((JSONObject) bd.get("fl"))!=null) {
				if (((JSONObject) ((JSONObject) bd.get("fl")).get("L"))!=null) {
					if (((JSONObject) ((JSONObject) bd.get("fl")).get("L")).get("ftl")!=null) {
						
						throw new Exception("Sorry, there is a sub relation for "+relationTL+".");
						
					}
				}
			}
			
			ExperienceManagerL2.deleteNodeTabLink(parentId, relationTL, type);

		}
		
		JSONObject bd = Record2.getNode(relationId);
		
		if (((JSONObject) bd.get("fl"))!=null) {
			if (((JSONObject) ((JSONObject) bd.get("fl")).get("L"))!=null) {
				if (((JSONObject) ((JSONObject) bd.get("fl")).get("L")).get("ftl")!=null) {
					
					throw new Exception("Sorry, the relation is already used by another.");
					
				}
			}
		}
		
		//Delete the relation
		Record2.remove(relationId);
		
	}

	//Stimulate a relation
	@SuppressWarnings("unchecked")
	public static void stimulate(String relationId, boolean stimulateTheParent) throws Exception {

		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id "+relationId+" is not valid [required].");

		}

		//Try to get the relation
		JSONObject recRelation = Record2.getNode(relationId);

		if (recRelation==null) {

			throw new Exception("Sorry, the relation "+relationId+" does not exist.");

		}
		
		//Stimulate thoughts and sub relations
		JSONArray thNodes = RelationManager.showThoughtNodes(relationId);
		
		for(int i=0;i<thNodes.size();i++) {
			
			String n = (String) thNodes.get(i);
			
			if (n.startsWith("TH")) {
				
				ThoughtManager.stimulate(n);
				
			} else {

				stimulate(n, false);
				
			}
			
		}
		
		//Stimulation +1
		recRelation.put("w", Integer.parseInt(""+recRelation.get("w"))+1);
		Record2.update(relationId, recRelation.toJSONString());
		String parentRelationId = "";
		
		//Only if not a parent relation
		if (stimulateTheParent) {
			
			//Stimulate the parent relation
			
			if (recRelation.containsKey("p")) {
				
				parentRelationId = ""+recRelation.get("p");
				
			}
			
		}
		
		JSONArray tabLinkIds = RelationManager.listTabLinks(relationId);
		String type = relationId.substring(1, 2);
		
		//Parse all tab link id in the relation
		for(int i=0;i<tabLinkIds.size();i++) {

			//Get the current parent id
			String relationTL = tabLinkIds.get(i)+"";
			JSONObject bd = Record2.getNode(relationTL);
			String parentId = "";
			JSONObject ud = ((JSONObject) bd.get("ud"));
			for(Object key : ud.keySet()) {
				parentId = (String)key;
			}
			
			Long parentPosition = -1L;
			if (bd.containsKey("pp")) {
				parentPosition = (Long) bd.get("pp");
			}
			
			ExperienceManagerL2.deleteNodeTabLink(parentId, relationTL, type);
			
			ExperienceManagerL2.addStimulationNodeTabLink(parentId, relationTL, ((JSONObject) bd.get("fl")), parentPosition, type);
			
		}
		
		if (!parentRelationId.equals("")) {
			stimulate(parentRelationId, false);
		}

	}
	
	//Create a basic relation
	@SuppressWarnings("unchecked")
	public static String create(String thoughtOrRelations, String lang, String context, String mql) throws Exception {
		
		if (thoughtOrRelations.length()==0) {

			throw new Exception("Sorry, at least one thought/relation id needed.");

		}
		
		String type = "L";
		
		//The default
		String relationId = "R"+type+"["+SequenceManager.incr("relation-"+type)+"]";
		while (Record2.getNode(relationId)!=null) {
			
			relationId = "R"+type+"["+SequenceManager.incr("relation-"+type)+"]";
			
		}
		
		int nbThought = Misc.size(thoughtOrRelations, " ");
		
		//Parse all words in the relation
		for(int i=1;i<=nbThought;i++) {

			//Get the current thought
			String id = Misc.atom(thoughtOrRelations, i, " ");

			if (Record2.getNode(id)==null) {

				throw new Exception("Sorry, the thought/relation id "+id+" does not exist.");

			} else {
				
				ExperienceManagerL2.addNodeTabLink(id, "["+relationId+" "+i+"/"+nbThought+"]", -1, type);
			
			}

		}
		
		//Add the record
		BasicNode br = null;
		br = new BasicNode(relationId, BasicNode.RELATION_TYPE, lang);

		br.dataNode.put("n", nbThought);
		br.dataNode.put("st", new JSONArray());
		br.dataNode.put("l", lang);
		br.dataNode.put("ct", context);
		br.dataNode.put("mql", mql);
		br.dataNode.put("sty", "");
		
		String[] array = thoughtOrRelations.split(" ", -1);
		String lth = "";
		for(int i=0;i<array.length;i++) {
			if (array[i].startsWith("T")) lth+=" "+array[i];
			else {
				JSONArray ar = RelationManager.showThoughtsRecursively(array[i]);
				for(int z=0;z<ar.size();z++) lth+=" "+ar.get(z);
			}
		}
		lth = lth.substring(1);
		br.dataNode.put("rlth", lth);
		br.dataNode.put("lth", thoughtOrRelations);
		br.dataNode.put("a", new JSONArray());
		br.dataNode.put("dco", new JSONObject());
		br.dataNode.put("t", new JSONObject());
		br.dataNode.put("e", new JSONObject());
		JSONObject cir = new JSONObject();
		cir.put("r", null);
		cir.put("i", null);
		cir.put("o", null);
		br.dataNode.put("c", cir);
		br.dataNode.put("lv", "");
		
		br.dataNode.put("lnk", "");
		br.dataNode.put("groups", new JSONObject());
		br.dataNode.put("n_s", new JSONObject());
		br.dataNode.put("n_p", new JSONObject());
		
		Record2.add("record", relationId, br.dataNode.toJSONString());
		
		return relationId;
		
	}
	
	//Check if a relation already exist
	public static boolean exist(String relationId) throws Exception {
		
		if (Record2.getNode(relationId)==null) {
			return false;
		} else {
			return true;
		}
		
	}
	
	//Set a condition
	@SuppressWarnings("unchecked")
	public static void setCondition(SessionThread session, String relationId, String target, String mql, EnvManager env) throws Exception {
		
		relationId = CommandManager.executeAllCommands(session, Misc.splitCommand(relationId), env, null, null);
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}

		if (target==null ||target.length()==0) {

			throw new Exception("Sorry, the target is required (R|TH1|TH2|...).");

		}
		
		if (!target.equals("R")) {
			
			if (target.length()<2) throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			
			try {
				
				Integer.parseInt(target.substring(2));
				
			} catch (Exception e) {
				throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			}
			
		}

		if (mql.length()==0) {

			throw new Exception("Sorry, the MQL condition is required.");

		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);
		
		((JSONObject) recRelationId.get("dco")).put(target, mql);
		
		Record2.update(relationId, recRelationId.toJSONString());
		
	}
	
	//Set a weight
	@SuppressWarnings("unchecked")
	public static void setWeight(String relationId, String weight) throws Exception {
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		try {
			
			@SuppressWarnings("unused")
			int w = Integer.parseInt(weight);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the weight is required.");
			
		}
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
		
			//Try to get the relation tab link id
			JSONObject recRelationId = Record2.getNode(rc);
			
			recRelationId.put("wt", weight);
			
			Record2.update(rc, recRelationId.toJSONString());
			
		}
		
	}
	
	//Get the weight
	public static double getWeight(String relationId) throws Exception {
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);
		
		String result = (String) recRelationId.get("wt");
		
		if (result==null) return 0;
		else return Double.parseDouble(result);
		
	}
	
	//Delete a condition
	public static void deleteCondition(SessionThread session, String relationId, String target, EnvManager env) throws Exception {
		
		relationId = CommandManager.executeAllCommands(session, Misc.splitCommand(relationId), env, null, null);
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}

		if (target==null ||target.length()==0) {

			throw new Exception("Sorry, the target is required (R|TH1|TH2|...).");

		}
		
		if (!target.equals("R")) {
			
			if (target.length()<2) throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			
			try {
				
				Integer.parseInt(target.substring(2));
				
			} catch (Exception e) {
				throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			}
			
		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);
		
		((JSONObject) recRelationId.get("dco")).remove(target);
		
		Record2.update(relationId, recRelationId.toJSONString());
		
	}
	
	//Delete a type
	public static void deleteType(SessionThread session, String relationId, String target, EnvManager env) throws Exception {
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}

		if (target==null ||target.length()==0) {

			throw new Exception("Sorry, the target is required (R|TH1|TH2|...).");

		}
		
		if (!target.equals("R")) {
			
			if (target.length()<2) throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			
			try {
				
				Integer.parseInt(target.substring(2));
				
			} catch (Exception e) {
				throw new Exception("Sorry, the target is not valid (R|TH1|THN).");
			}
			
		}
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
		
			//Try to get the relation tab link id
			JSONObject recRelationId = Record2.getNode(rc);
			
			((JSONObject) recRelationId.get("t")).remove(target);
			
			Record2.update(rc, recRelationId.toJSONString());
			
		}
		
	}
	
	//Show conditions
	public static JSONObject showConditions(SessionThread session, String relationId, EnvManager env) throws Exception {
		
		relationId = CommandManager.executeAllCommands(session, Misc.splitCommand(relationId), env, null, null);
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		return ((JSONObject) recRelationId.get("dco"));
		
	}
	
	//Show conditions
	@SuppressWarnings("unchecked")
	public static JSONObject showTypes(SessionThread session, String relationId, EnvManager env) throws Exception {
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONObject result = ((JSONObject) recRelationId.get("t"));
		
		int j=0;
		JSONArray a = RelationManager.showThoughtNodes(relationId);
		for(int i=0;i<a.size();i++) {
			
			if (((String) a.get(i)).equals("TH[]")) {
				
				j++;
				
				if (!result.containsKey("TH"+j)) {
					
					JSONObject o = new JSONObject();
					o.put("t", "string");
					o.put("r", "1");
					result.put("TH"+j, o);
					
				}
				
			}
			
		}
		
		return ((JSONObject) recRelationId.get("t"));
		
	}
	
	//Show conditions
	public static JSONArray showAction(SessionThread session, String relationId, EnvManager env) throws Exception {
		
		relationId = CommandManager.executeAllCommands(session, Misc.splitCommand(relationId), env, null, null);
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation id is required.");

		}
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		return (JSONArray) recRelationId.get("a");
		
	}
	
	//Set a next probability
	@SuppressWarnings("unchecked")
	public static void next_probability_set(String relationId, String r) throws Exception {
		
		//Get the relation id
		JSONObject rec = Record2.getNode(relationId);

		if (rec==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			JSONObject recRelation = Record2.getNode(rc);
			
			JSONObject n = ((JSONObject) recRelation.get("n_p"));
			
			if (n.containsKey(r)) {
				
				n.put(r, 1+Integer.parseInt(n.get(r)+""));
				
			} else {
				
				n.put(r, 1);
				
			}
			
			Record2.update(rc, recRelation.toJSONString());
			
		}
		
	}
	
	//Delete a next probability
	public static void next_probability_delete(String relationId, String r) throws Exception {
		
		if (!next_probability_exist(relationId, r)) {
			
			throw new Exception("Sorry, the next probability '"+r+"' does not exist.");
			
		}
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			JSONObject recRelation = Record2.getNode(rc);
			
			((JSONObject) recRelation.get("n_p")).remove(r);
			
			Record2.update(rc, recRelation.toJSONString());
			
		}
		
	}
	
	//Check if a next probability already exist
	public static boolean next_probability_exist(String relationId, String r) throws Exception {
		
		try {
			
			//Get the relation id
			JSONObject rec = Record2.getNode(relationId);

			if (rec==null) {

				throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

			}
			
			if (((JSONObject) rec.get("n_p")).containsKey(r)) return true;
			else return false;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	//Show next probabilities
	@SuppressWarnings("unchecked")
	public static JSONArray next_probability_show(String relationId) throws Exception {
		
		//Get the relation id
		JSONObject rec = Record2.getNode(relationId);

		if (rec==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONObject r = ((JSONObject) rec.get("n_p"));
		
		List<Map.Entry<String, Long>> list = new LinkedList<>( r.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getValue() ).compareTo( o2.getValue() );
			}
		} );
		
		JSONArray a = new JSONArray();
		
		for (Map.Entry<String, Long> entry : list) {
			
			a.add(entry.getKey()+"-"+entry.getValue());
			
		}
		
		return a;
		
	}
	
	//Check if a type exist
	public static boolean existType(String relationId) throws Exception {
		
		try {
			
			//Get the relation tab link id
			JSONObject rec = Record2.getNode(relationId);
			
			if (((JSONObject) rec.get("t")).size()==0) return false;
			else return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	//Generate conditions
	public static String generateConditions(String relationId) throws Exception {
		
		String result = "";
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONObject dco = ((JSONObject) recRelationId.get("dco"));
		
		for(Object o : dco.keySet()) {
			
			String key = (String) o;
			
			String mql = (String) dco.get(key);
			
			result += "\nrelation condition set "+relationId+" \n"+key+" {"+mql+"};";
			
		}
		
		if (result.length()>0) result = result.substring(1);
		
		if (result.equals("")) throw new Exception("Sorry, no condition found.");
		else return "#[sentence] = the input string;\n" + 
				"#[R] = The relation id;\n" + 
				"#[targetUser] = The current user;\n" + 
				"#[R1...N] = The thought or sub relation from the relation;\n" + 
				"#[NB_R] = The number of thought or sub relation from the relation;\n" + 
				"#[W1...N] = The word from the input;\n" + 
				"#[NB_W] = The number of word from the input;\n" + 
				"#[TH1...N] = The word linked with the best TH[] in the relation;\n\n"+
				result;
		
	}
	
	//Generate types
	public static String generateTypes(String relationId) throws Exception {
		
		String result = "";
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONObject obj = ((JSONObject) recRelationId.get("t"));
		
		for(Object o : obj.keySet()) {
			
			String key = (String) o;
			
			String t = (String) ((JSONObject) obj.get(key)).get("t");
			String required = (String) ((JSONObject) obj.get(key)).get("r");
			String multiple = (String) ((JSONObject) obj.get(key)).get("m");
			
			result += "\nrelation type set "+relationId+" "+key+" \""+t.replace("\"", "\\\"")+"\" "+(required.equals("1")?"true":"false")+" "+(multiple.equals("1")?"true":"false")+";";
			
		}
		
		if (result.length()>0) result = result.substring(1);
		
		if (result.equals("")) throw new Exception("Sorry, no condition found.");
		else return result;
		
	}
	
	//Generate conditions
	public static String generateAction(String relationId) throws Exception {
		
		String result = "";
		
		//Try to get the relation tab link id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONArray mqls = ((JSONArray) recRelationId.get("a"));
		
		for(int i=0;i<mqls.size();i++) {
			
			JSONObject currentAction = (JSONObject) mqls.get(i);
			
			result += "\n#Action "+i+";\nrelation action update "+relationId+" "+i+"\n" + 
					"{"+currentAction.get("c")+"}\n" + 
					" \n" + 
					"{"+currentAction.get("a")+"}\n" + 
					" \n" + 
					"{"+currentAction.get("e")+"};\n";
			
		}
		
		if (result.equals("")) throw new Exception("Sorry, no action found.");
		else return "#[sentence] = the input string;\n" + 
				"#[R] = The relation id;\n" + 
				"#[targetUser] = The target user;\n" + 
				"#[aiIsSender] = Boolean to check if the AI is the sender (else it is the user);\n" + 
				"#[R1...N] = The thought or sub relation from the relation;\n" + 
				"#[NB_R] = The number of thought or sub relation from the relation;\n" + 
				"#[W1...N] = The word from the input;\n" + 
				"#[NB_W] = The number of word from the input;\n" + 
				"#[TH1...N] = The word linked with the best TH[] in the relation;\n\n"+
				result.substring(1);
		
	}
	
	//Get the TH type weight value
	@SuppressWarnings("unchecked")
	public static double get_TH_TypeWeightValue(String g_l, String relationId, JSONArray sentenceArray, SessionThread session, EnvManager env, JSONObject n_input) throws Exception {
		
		//Initialization
		String sentence = "";
		JSONArray input = new JSONArray();
		
		String[] inWords = new String[sentenceArray.size()];
		
		for(int i=0;i<sentenceArray.size();i++) {
			
			String val = (String) ((JSONObject) sentenceArray.get(i)).get("val");
			sentence+= " "+val;
			input.add(val);
			
			inWords[i] = (String) ((JSONObject) ((JSONObject) sentenceArray.get(i)).get("type")).get("val");
			
		};
		sentence = sentence.substring(1);
		
		JSONArray thoughts = RelationManager.showThoughtsRecursivelyRLTH(relationId);
		
		JSONArray groups = new JSONArray();
		
		//Parse all thoughts
		for(int i=1;i<=thoughts.size();i++) {
			
			String th = thoughts.get(i-1)+"";
			
			if (th.equals("TH[]")) {
				
				JSONObject o = new JSONObject();
				o.put("key", i-1);
				o.put("type", "TH");
				o.put("th", new JSONArray());
				groups.add(o);
				
			} else if (groups.size()==0 || (((JSONObject) groups.get(groups.size()-1)).get("type")+"").equals("TH")) {
				
				JSONObject o = new JSONObject();
				o.put("key", i-1);
				o.put("type", "W");
				o.put("th", new JSONArray());
				groups.add(o);
				
			}
			
			if (!th.equals("TH[]")) {
				
				JSONObject o = new JSONObject();
				o.put("thoughtId", th);
				JSONArray a = ThoughtManager.getWords(th);
				JSONObject words = new JSONObject();
				for(int j=0;j<a.size();j++) {
					String curWord = (((JSONObject) a.get(j)).get("word")+"");
					curWord = curWord.substring(2, curWord.length()-1);
					words.put(curWord, j);
				}
				o.put("words", words);
				((JSONArray) ((JSONObject) groups.get(groups.size()-1)).get("th")).add(o);
				
			}
			
		}
		
		double value = 0;
		
		//Cognitive search
		if (groups.size()>1) {
			
			//Cognitive search by probability
			CognSearch.byProbability(groups, inWords);
			
			//System.out.println("groups="+JsonManager.format_Gson(groups.toJSONString()));
			
			JSONObject typesFromRelation = RelationManager.showTypes(session, relationId, env);
			
			int j = 0;
			for(int i=0;i<groups.size();i++) {
				
				if ((((JSONObject) groups.get(i)).get("type")+"").equals("TH")) {
						
						j++;

					String str = CognSearch.get(i);
					//System.out.println("i="+i);

					if (!str.equals("")) {
					
						env.set("[TH"+j+"]", str);
						
						if (typesFromRelation.containsKey("TH"+j)) {
							
							String r_type = ((JSONObject) typesFromRelation.get("TH"+j)).get("t")+"";
							
							if (relationId.substring(1, 2).equals("N")) {
								
								if (n_input.containsKey("T_"+r_type)) {
									value += 100;
								}
								
							}
							
						}
						
					}
					
				}
				
			}

		}
		
		return value;
		
	}
	
	//Get the TH
	@SuppressWarnings("unchecked")
	public static JSONObject get_th(String relationId, String sentence, SessionThread session, EnvManager env) throws Exception {
		
		//Initialization
		JSONArray input = new JSONArray();
		JSONObject result = new JSONObject();
		
		JSONArray sentenceArray = (JSONArray) ((JSONObject) StimulationManager.splitStimulation(session, env, sentence, true).get(0)).get("s");
		
		String[] inWords = new String[sentenceArray.size()];
		
		for(int i=0;i<sentenceArray.size();i++) {
			
			String val = (String) ((JSONObject) sentenceArray.get(i)).get("val");
			input.add(val);
			
			inWords[i] = (String) ((JSONObject) ((JSONObject) sentenceArray.get(i)).get("type")).get("val");
			
		};
		
		JSONArray thoughts = RelationManager.showThoughtsRecursivelyRLTH(relationId);
		
		JSONArray groups = new JSONArray();
		int nbTh = 0;
		
		//Parse all thoughts
		for(int i=1;i<=thoughts.size();i++) {
			
			String th = thoughts.get(i-1)+"";
			
			if (th.equals("TH[]")) {
				
				JSONObject o = new JSONObject();
				o.put("key", i-1);
				o.put("type", "TH");
				o.put("th", new JSONArray());
				groups.add(o);
				nbTh++;
				
			} else if (groups.size()==0 || (((JSONObject) groups.get(groups.size()-1)).get("type")+"").equals("TH")) {
				
				JSONObject o = new JSONObject();
				o.put("key", i-1);
				o.put("type", "W");
				o.put("th", new JSONArray());
				groups.add(o);
				
			}
			
			if (!th.equals("TH[]")) {
				
				JSONObject o = new JSONObject();
				o.put("thoughtId", th);
				JSONArray a = ThoughtManager.getWords(th);
				JSONObject words = new JSONObject();
				for(int j=0;j<a.size();j++) {
					String curWord = (((JSONObject) a.get(j)).get("word")+"");
					curWord = curWord.substring(2, curWord.length()-1);
					words.put(curWord, j);
				}
				o.put("words", words);
				((JSONArray) ((JSONObject) groups.get(groups.size()-1)).get("th")).add(o);
				
			}
			
		}
		
		//Cognitive search
		if (groups.size()>1) {
			
			//Cognitive search by probability
			CognSearch.byProbability(groups, inWords);
			
			//System.out.println("groups="+JsonManager.format_Gson(groups.toJSONString()));
			
			int j = 0;
			for(int i=0;i<groups.size();i++) {
				
				if ((((JSONObject) groups.get(i)).get("type")+"").equals("TH")) {
					
					j++;

					String str = CognSearch.get(i);
					//System.out.println("i="+i);

					result.put("TH"+j, str);
					
				}
				
			}

		}
		
		if (result.size()==0 && nbTh==1) {
			
			result.put("TH1", sentence);
			
		}
		
		return result;
		
	}
	
	//Check if an action exist
	public static boolean existAction(SessionThread session, String rIdOrTabLinkId) throws Exception {
		
		JSONObject rec = Record2.getNode(rIdOrTabLinkId);
		if (((JSONArray) rec.get("a")).size()==0) {
			return false;
		} else {
			return true;
		}
		
	}
	
	//Contains in string
	public static int containsInString(JSONObject group, String in, int lastFound) throws Exception {
		
		JSONArray a = ((JSONArray) group.get("th"));
		
		for(int u=lastFound+1;u<a.size();u++) {

			JSONObject obj = ((JSONObject) ((JSONObject) a.get(u)).get("reader_found_word"));
			
			if (obj.containsKey(in)) {

				return u;
				
			}
			
		}
		
		return -1;
		
	}
	
	//Set an action
	@SuppressWarnings("unchecked")
	public static void setAction(SessionThread session, String relationId, String position, String mql_condition, String mql_action, String mql_else_action, EnvManager env, boolean set) throws Exception {
		
		relationId = CommandManager.executeAllCommands(session, Misc.splitCommand(relationId), env, null, null);
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation is required.");

		}

		if (mql_condition.length()==0) {

			throw new Exception("Sorry, the MQL condition is required.");

		}

		if (mql_action.length()==0) {

			throw new Exception("Sorry, the MQL action is required.");

		}

		if (mql_else_action.length()==0) {

			throw new Exception("Sorry, the MQL else action is required.");

		}
		
		try {
			
			if (Integer.parseInt(position)<0) new Exception("the position must be >=0 .");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the position must be >=0 .");
			
		};
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			//Try to get the relation tab link id
			JSONObject recRelationTabLinkId = Record2.getNode(rc);
			
			JSONObject action = new JSONObject();
			action.put("c", mql_condition);
			action.put("a", mql_action);
			action.put("e", mql_else_action);
			action.put("t", "ACTION");
			
			if (set) ((JSONArray) recRelationTabLinkId.get("a")).set(Integer.parseInt(position), action);
			else ((JSONArray) recRelationTabLinkId.get("a")).add(Integer.parseInt(position), action);

			Record2.update(rc, recRelationTabLinkId.toJSONString());
			
		}
		
	}
	
	//Set a sub action
	@SuppressWarnings("unchecked")
	public static void setSubAction(SessionThread session, String parentRelationId, String position, String relationId, EnvManager env, boolean set) throws Exception {
		
		if (parentRelationId.length()==0) {

			throw new Exception("Sorry, the relation is required.");

		}
		
		try {
			
			if (Integer.parseInt(position)<0) new Exception("the position must be >=0 .");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the position must be >=0 .");
			
		};
		
		//Try to get the relation id
		JSONObject recRelationId = Record2.getNode(relationId);

		if (recRelationId==null) {

			throw new Exception("Sorry, the relation id "+relationId+" does not exist.");

		}
		
		JSONArray circle_ids = CircleManager.getIds("r", parentRelationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			//Try to get the relation tab link id
			JSONObject recRelationTabLinkId = Record2.getNode(rc);
			
			JSONObject action = new JSONObject();
			action.put("r", relationId);
			action.put("t", "SUB_ACTION");
			
			if (set) ((JSONArray) recRelationTabLinkId.get("a")).set(Integer.parseInt(position), action);
			else ((JSONArray) recRelationTabLinkId.get("a")).add(Integer.parseInt(position), action);

			Record2.update(rc, recRelationTabLinkId.toJSONString());
			
		}
		
	}
	
	//Remove an action
	public static void removeAction(String relationId, String position) throws Exception {
		
		if (relationId.length()==0) {

			throw new Exception("Sorry, the relation/tab link id is required.");

		}
		
		try {
			
			if (Integer.parseInt(position)<0) new Exception("the position must be >=0 .");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the position must be >=0 .");
			
		};
		
		JSONArray circle_ids = CircleManager.getIds("r", relationId);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			//Try to get the relation tab link id
			JSONObject recRelationTabLinkId = Record2.getNode(rc);
			
			((JSONArray) recRelationTabLinkId.get("a")).remove(Integer.parseInt(position));

			Record2.update(rc, recRelationTabLinkId.toJSONString());
			
		}
		
	}
	
	//Get the first relation tab link
	public static String firstTabLink(String relationTabLink, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}
		
		if (relationTabLink.length()==0) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode(relationTabLink);

		if (rec==null) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" does not exist.");

		}
		
		return ExperienceManagerL2.getFirstTabLink(relationTabLink, type);

	}
	
	//Get the last relation tab link
	public static String lastTabLink(String relationTabLink, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}

		if (relationTabLink.length()==0) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode(relationTabLink);

		if (rec==null) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" does not exist.");

		}
		
		return ExperienceManagerL2.getLastTabLink(relationTabLink, type);

	}
	
	//Check if contains tab links
	public static boolean containsTabLink(String relationId, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}

		//Initialization
		boolean result = false;
		
		//list all tab links
		JSONArray list = RelationManager.listTabLinks(relationId);
		
		//Parse the list
		for(int i=0;i<list.size();i++) {
			
			if (firstTabLink(""+list.get(i), type)!=null) {
				result = true;
				
				break;
			}
			
		}
		
		return result;
		
	}
	
	//Get relation tab link
	public static String getTabLink(String relationTabLink, int position, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}
		
		if (position>=ConcentrationManager.getConcentrationDepth("C[relation]")) {

			throw new Exception("Sorry, the position > concentration depth.");

		}
		
		JSONArray tab = showTabLinks(relationTabLink, type, ConcentrationManager.getConcentrationDepth("C[relation]"));
		
		if (position>=tab.size()) {

			throw new Exception("Sorry, not found tab link at the position "+position+" for the relation tab link "+relationTabLink+".");

		}
		
		return ""+tab.get(position);

	}
	
	//Find relation tab link
	public static String findTabLink(String relationId, int position, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}
		
		JSONArray thoughtNodes = RelationManager.showThoughtNodes(relationId);
		
		int concentration = ConcentrationManager.getConcentrationDepth("C[relation]");

		if (position>=concentration) {

			throw new Exception("Sorry, the position > concentration depth.");

		}
		
		int i = 1;
		
		JSONArray tab = showTabLinks("["+relationId+" "+i+"/"+thoughtNodes.size()+"]", type, concentration);

		while (tab.size()==0 && i<=thoughtNodes.size()) {
			
			tab = showTabLinks("["+relationId+" "+i+"/"+thoughtNodes.size()+"]", type, concentration);
			i++;
			
		}

		if (position>=tab.size()) {

			throw new Exception("Sorry, not found tab link at the position "+position+" for the relation "+relationId+".");

		}

		return ""+tab.get(position);

	}
	
	//Show relation tab links
	@SuppressWarnings("unchecked")
	public static JSONArray showTabLinks(String relationTabLink, String type, int depth) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}

		if (relationTabLink.length()==0) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode(relationTabLink);

		if (rec==null) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" does not exist.");

		}
		
		JSONArray result = new JSONArray();
		
		//Get the first
		String tl = ExperienceManagerL2.getFirstTabLink(relationTabLink, type);
		
		//Add only if null
		if (tl!=null) {
			
			result.add(tl);
			
			//Get the down node
			tl = ExperienceManagerL2.getDownTabLink(relationTabLink, tl, type);
			int i = 0;
			
			while(tl!=null && i+1<depth) {

				result.add(tl);
				
				i++;

				//Get the next down node
				tl = ExperienceManagerL2.getDownTabLink(relationTabLink, tl, type);
				
			}

		}
		
		return result;

	}
	
	//Show relation tab links
	@SuppressWarnings("unchecked")
	public static JSONArray showTabLinkPerceptions(String relationTabLink, String type) throws Exception {
		
		if (RelationManager.allTypeList.indexOf(type)==-1) {
			
			throw new Exception("Sorry, the type is not valid ("+RelationManager.allTypeList+").");
			
		}

		if (relationTabLink.length()==0) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode(relationTabLink);

		if (rec==null) {

			throw new Exception("Sorry, the relation tab link "+relationTabLink+" does not exist.");

		}
		
		JSONArray result = new JSONArray();
		int maxPerception = 0;
		
		//Get the first
		String tl = ExperienceManagerL2.getFirstTabLink(relationTabLink, type);
		
		//Add only if null
		if (tl!=null) {
			
			JSONObject bd = Record2.getNode(Misc.atom(tl, 1, " ").substring(1));
			int perception = Integer.parseInt(""+bd.get("w"));
			
			maxPerception += perception;
			JSONObject obj = new JSONObject();
			obj.put("k", tl);
			obj.put("w", perception);
			result.add(obj);
			
			//Get the down node
			tl = ExperienceManagerL2.getDownTabLink(relationTabLink, tl, type);
			
			int i = 0;
			
			while(tl!=null && i+1<ConcentrationManager.getConcentrationDepth("C[relation]")) {
				
				bd = Record2.getNode(Misc.atom(tl, 1, " ").substring(1));
				
				perception = Integer.parseInt(""+bd.get("w"));
				
				maxPerception += perception;
				obj = new JSONObject();
				obj.put("k", tl);
				obj.put("w", perception);
				result.add(obj);
				
				i++;

				//Get the next down node
				tl = ExperienceManagerL2.getDownTabLink(relationTabLink, tl, type);
				
			}
			
			for(i=0;i<result.size();i++) {
				
				//Get the current object
				obj = (JSONObject) result.get(i);
				
				obj.put("mw", maxPerception);

				if (maxPerception==0) obj.put("v", 0D);
				else obj.put("v", Double.parseDouble(""+obj.get("w"))/Double.parseDouble(""+maxPerception)*100);
				
			}

		}
		
		return result;

	}
	
	public static JSONObject env_show(String login, String relationId, String index) throws Exception {
		
		JSONObject r_u = Record2.getNode("ENV_"+relationId+"_"+login);
		if (r_u==null) {
			
			return new JSONObject();
			
		} else {
			
			int i = Integer.parseInt(index);
			
			JSONArray a = ((JSONArray) r_u.get("e"));
			return ((JSONObject) a.get(i));
			
		}
		
	}
	
	public static String env_size(String login, String relationId) throws Exception {
		
		JSONObject r_u = Record2.getNode("ENV_"+relationId+"_"+login);
		if (r_u==null) {
			
			return "0";
			
		} else {
			
			JSONArray a = ((JSONArray) r_u.get("e"));
			return ""+a.size();
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void env_new(String login, String relationId) throws Exception {
		
		JSONObject r_u = Record2.getNode("ENV_"+relationId+"_"+login);
		if (r_u==null) {

			r_u = new JSONObject();
			JSONArray a = new JSONArray();
			a.add(0, new JSONObject());
			r_u.put("e", a);
			
			Record2.add("record", "ENV_"+relationId+"_"+login, r_u.toJSONString());
			
		} else {
			
			((JSONArray) r_u.get("e")).add(0, new JSONObject());
			
			Record2.add("record", "ENV_"+relationId+"_"+login, r_u.toJSONString());
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void env_set(String login, String relationId, String var, String val) throws Exception {
		
		JSONObject r_u = Record2.getNode("ENV_"+relationId+"_"+login);
		if (r_u==null) {

			r_u = new JSONObject();
			JSONArray a = new JSONArray();
			a.add(0, new JSONObject());
			r_u.put("e", a);
			
		}
		
		JSONArray a = ((JSONArray) r_u.get("e"));
		
		JSONObject env = ((JSONObject) a.get(0));
		
		env.put(var, val);
		
		int max = ConcentrationManager.getConcentrationDepth("C[relation-env]");
		
		while (a.size()>max) {
			a.remove(a.size()-1);
		}
		
		Record2.add("record", "ENV_"+relationId+"_"+login, r_u.toJSONString());
		
	}
	
	public static String env_get(String login, String relationId, String var) throws Exception {
		
		JSONObject r_u = Record2.getNode("ENV_"+relationId+"_"+login);
		if (r_u==null) {
			
			throw new Exception("Sorry, the node 'ENV_"+relationId+"_"+login+"' does not exist.");
			
		} else {
			
			JSONArray a = ((JSONArray) r_u.get("e"));
			JSONObject env = ((JSONObject) a.get(0));
			return (String) env.get(var);
			
		}
		
	}

}
