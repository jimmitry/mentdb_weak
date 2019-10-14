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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL1;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL2;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.stimulation.StimulationManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;

public class SearchEngine {
	
	//Search relations
	@SuppressWarnings("unchecked")
	public static JSONArray execute(long sessionId, String text, String lang, boolean searchPunctuation, SessionThread session, EnvManager env) throws Exception {
		
		JSONArray input_text = new JSONArray();
		JSONArray thoughtFilter = new JSONArray();
		
		String[] list = text.split(" ");
		
		for(int i=0;i<list.length;i++) {
			
			if (!list[i].equals("")) {
				
				input_text.add(list[i]);
				thoughtFilter.add("");
				
			}
			
		}
		
		JSONArray sentenceArray = (JSONArray) ((JSONObject) StimulationManager.splitStimulation(sessionId, session, env, text, true).get(0)).get("s");
		
		return execute(text, session.user, sessionId, "", lang, input_text, sentenceArray, "*", thoughtFilter, searchPunctuation, session, env, new JSONObject());
		
	}
	
	//Search
	@SuppressWarnings("unchecked")
	public static JSONArray execute(String inputText, String targetUser, long sessionId, String filter_search, String g_l, JSONArray inputVector, JSONArray sentenceArray, String searchTypeList, JSONArray thoughtFilter, boolean searchPunctuation, SessionThread session, EnvManager env, JSONObject n_input) throws Exception {
		
		//Initialization
		double mpa = 0.0+ConcentrationManager.getConcentrationDepth("C[elevation_max_acceptance]");
		int concentration = ConcentrationManager.getConcentrationDepth("C[deep-search]");
		String typeList = RelationManager.allTypeList;
		//System.out.println("1#####");
		JSONObject context_search = new JSONObject();
		//System.out.println(inputText+"2#####context_search="+context_search.toJSONString());
		if (StringFx.lrtrim(searchTypeList).equals("*")) searchTypeList = RelationManager.allTypeList;
		
		for(int z=0;z<typeList.length();z++) {
			if (RelationManager.allTypeList.indexOf(""+typeList.charAt(z))==-1) {
				
				throw new Exception("Sorry, the type '"+typeList.charAt(z)+"' is not valid ("+RelationManager.allTypeList+").");
				
			}
		}
		
		//Get all thoughts
		HashMap<String, HashMap<String, Vector<String>>> allThoughts = new HashMap<String, HashMap<String, Vector<String>>>();
		Vector<HashMap<String, Double>> allThoughtString = new Vector<HashMap<String, Double>>();
		for(int i=0;i<inputVector.size();i++) {
			String wordName = inputVector.get(i)+"";
			if (searchPunctuation || ("?,.;!:".indexOf(wordName)==-1)) {
				allThoughtString.add(getAllThoughts(sessionId, inputVector, i, wordName, allThoughts, mpa, thoughtFilter.get(i)+""));
			}
        }
		
		HashMap<String, Double> alreadyUsed = new HashMap<String, Double>();
		
		//Parse all words
		for(int j=0;j<inputVector.size();j++) {
			
			String wordName = inputVector.get(j)+"";
			
			if (searchPunctuation || ("?,.;!:".indexOf(wordName)==-1)) {
			
				HashMap<String, Vector<String>> allThoughtsL = allThoughts.get(wordName);
				
				//Parse all languages
				for(Entry<String, Vector<String>> e: allThoughtsL.entrySet()) {
		
					Vector<String> vectorOfThoughts = e.getValue();
									
					for(int i=0;i<vectorOfThoughts.size();i++) {
						
						String thoughtId = vectorOfThoughts.get(i);
						
						//Search relation
						for(int z=0;z<typeList.length();z++) {
							
							searchFromThought(sessionId, g_l, thoughtId, sentenceArray, ""+typeList.charAt(z), i+1, alreadyUsed, allThoughtString, concentration, typeList, session, env, filter_search, context_search, n_input);
	
						}
						
					}
									
				}
				
			}
			
		}
		
		List<Map.Entry<String, Double>> list = new LinkedList<>( alreadyUsed.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
			{
				return ( o2.getValue() ).compareTo( o1.getValue() );
			}
		} );
		
		JSONArray r = new JSONArray();
		JSONArray rr = new JSONArray();
		
		//Get the first value
		double d = -1;
		if (list.size()>0) d = list.get(0).getValue(); 
		
		for (Map.Entry<String, Double> entry : list) {
			
			if (entry.getValue()>0D) {
				
				if (searchTypeList.indexOf(""+entry.getKey().charAt(1))>-1) {
					
					if (d==entry.getValue()) {
						r.add(entry.getKey());
					} else {
						rr.add(entry.getKey()+" "+entry.getValue());
					}
					
				}
			
			}
			
		}
		
		Random ran = new Random();
		while (r.size()>0) {
			
			int z = ran.nextInt(r.size());
			rr.add(0, r.get(z)+" "+d);
			r.remove(z);
			
		}
		
		return rr;
		
	}
	
	//Get all thoughts for a basic search
	@SuppressWarnings("unchecked")
	public static HashMap<String, Double> getAllThoughts(long sessionId, JSONArray inputVector, int index, String wordName, HashMap<String, HashMap<String, Vector<String>>> allThoughts, double mpa, String thoughtFilter) throws Exception {
		
		JSONArray languages = new JSONArray();
		HashMap<String, Double> allThoughtString = new HashMap<String, Double>();
		
		try {
			languages = WordManager.showLanguages(sessionId, wordName);
		} catch (Exception e) {};
		
		if (languages.size()==0 || (languages.size()==1 && (languages.get(0)+"").equals("io"))) {
			
			languages = new JSONArray();
			
			//Levenshtein distance
			JSONArray a = WordManager.searchLevenshteinDistance(sessionId, wordName, ConcentrationManager.getConcentrationDepth("C[symbol]"), 1);
			
			if (a.size()>0) {
				
				JSONObject o = (JSONObject) a.get(0);
				String key = "";
				for(Object k : o.keySet()) {
					key = (String) k;
				}
				double v = (Double) o.get(key);
				
				//2 errors max
				if (v<=mpa) {
					wordName = key;
					
					inputVector.set(index, key);
					try {
						languages = WordManager.showLanguages(sessionId, wordName);
					} catch (Exception e) {};
				}
				
			}
			
		}
		
		try {
			
			if (!allThoughts.containsKey(wordName)) {
				allThoughts.put(wordName, new HashMap<String, Vector<String>>());
			}
			
			HashMap<String, Vector<String>> allThoughtsL = allThoughts.get(wordName);
		
			//Make a basic search for the word in all languages
			for(int i=0;i<languages.size();i++) {
				
				//Get the current language
				String curLang = ""+languages.get(i);
				
				//Get all filtered thoughts
				String filteredThoughts = ThoughtManager.search(sessionId, wordName, curLang, thoughtFilter);
				
				if (!allThoughtsL.containsKey(curLang)) {
					allThoughtsL.put(curLang, new Vector<String>());
				}
				
				Vector<String> curLangThoughts = allThoughtsL.get(curLang);
				
				//Get all thoughts
				//Get the first tab link
				String thoughtId = ExperienceManagerL1.getFirstTabLink(sessionId, "W["+wordName+"]", curLang, true);
				int depth = 1;
				
				//Parse all tab links
				while (thoughtId!=null) {
					
					JSONObject recNode = Record.getNode(sessionId, thoughtId);
					if (!((JSONObject) ((JSONObject) recNode.get("ud")).get(curLang+" "+"W["+wordName+"]")).containsKey("cw")) {
					
						//Filter on thoughts
						if (filteredThoughts.indexOf(thoughtId)>-1) {
							
							JSONArray cir = CircleManager.getIds(sessionId, "th", thoughtId);
							for(int c=0;c<cir.size();c++) {
								
								String th = (String) cir.get(c);
								
								curLangThoughts.add(th);
								allThoughtString.put(th, 1.0/depth);
								
							}
							
						}
					};
					
					//Get the next tab link
					thoughtId = ExperienceManagerL1.getDownTabLink(sessionId, "W["+wordName+"]", thoughtId, curLang, true);
					
					depth++;
				
				}
				
			}
			
		} catch (Exception e) {};
		
		return allThoughtString;
		
	}
	
	//Search relation into a thought
	public static void searchFromThought(long sessionId, String g_l, String thoughtId, JSONArray sentenceArray, String type, int depth, HashMap<String, Double> alreadyUsed, Vector<HashMap<String, Double>> allThoughtString, int concentration, String typeList, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {
		
		//Get the first
		String tl = ExperienceManagerL2.getFirstTabLink(sessionId, thoughtId, type);
		
		while(tl!=null && depth<=concentration) {
			
			//Get the relation id into a thought
			String relationId = Misc.atom(tl, 1, " ").substring(1);
			
			searchInCircle(sessionId, g_l, relationId, sentenceArray, typeList, allThoughtString, alreadyUsed, concentration-depth, session, env, filter_search, context_search, n_input);
			
			//Get the next down node
			tl = ExperienceManagerL2.getDownTabLink(sessionId, thoughtId, tl, type);
			
			depth++;
			
		}
		
	}
	
	public static void searchInCircle(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {
		
		JSONArray circle_ids = CircleManager.getIds(sessionId, "r", r);
		for(int i=0;i<circle_ids.size();i++) {
			
			String rc = (String) circle_ids.get(i);
			
			if (!alreadyUsed.containsKey(rc)) {
				checker(sessionId, g_l, rc, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth, session, env, filter_search, context_search, n_input);
			}
			
		}
		
	}
	
	public static void checker(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {
		
		check_relation(sessionId, g_l, r, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth, session, env, filter_search, context_search, n_input);
		
		check_parent(sessionId, g_l, r, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth, session, env, filter_search, context_search, n_input);
		
		check_in_relation(sessionId, g_l, r, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth, session, env, filter_search, context_search, n_input);
		
		check_sub_relation(sessionId, g_l, r, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth, session, env, filter_search, context_search, n_input);
		
	}
	
	public static void check_relation(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {
		
		if (typeFilter.indexOf(r.charAt(1)+"")>-1) {
			
			JSONObject obj = Record.getNode(sessionId, r);
			
			String rlth = (obj.get("rlth")+"");
			String ct = (String) obj.get("ct");
			String r_l = (String) obj.get("l");
			
			String wt = (String) obj.get("wt");
			double weight = 0;
			if (wt!=null) weight = Double.parseDouble(wt);
			
			double calcul = cognitiveDistance(weight, g_l, r, (String) obj.get("sty"), r_l, ct, sentenceArray, rlth, allThoughtString, depth, sessionId, session, env, filter_search, context_search, n_input);
			
			if (!alreadyUsed.containsKey(r)) {
				alreadyUsed.put(r, calcul);
			}
			
		}
		
	}
	
	public static void check_parent(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {

		JSONObject obj = Record.getNode(sessionId, r);
		String parentId = (String) obj.get("p");
		
		if (parentId!=null && !alreadyUsed.containsKey(parentId) && typeFilter.indexOf(parentId.charAt(1)+"")>-1) {
			
			obj = Record.getNode(sessionId, parentId);

			String rlth = (obj.get("rlth")+"");
			String ct = (String) obj.get("ct");
			String r_l = (String) obj.get("l");
			
			String wt = (String) obj.get("wt");
			double weight = 0;
			if (wt!=null) weight = Double.parseDouble(wt);
			
			double calcul = cognitiveDistance(weight, g_l, parentId, (String) obj.get("sty"), r_l, ct, sentenceArray, rlth, allThoughtString, depth, sessionId, session, env, filter_search, context_search, n_input);
			
			if (!alreadyUsed.containsKey(parentId)) {
				alreadyUsed.put(parentId, calcul);
			}
			
		}
		
		if (parentId!=null) {
			
			check_parent(sessionId, g_l, parentId, sentenceArray, typeFilter, allThoughtString, alreadyUsed, depth-1, session, env, filter_search, context_search, n_input);
		
		}
		
	}
	
	public static void check_in_relation(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {
		
		//Search in branch
		for(int iBranch=0;iBranch<RelationManager.allTypeList.length();iBranch++) {
		
			String type = RelationManager.allTypeList.charAt(iBranch)+"";
			String tl = ExperienceManagerL2.getFirstTabLink(sessionId, r, type);
			
			int ii=depth;
			while(tl!=null && ii>0) {
				
				//Get the relation id
				String relationId = Misc.atom(tl, 1, " ").substring(1);
				
				//Search in circle
				searchInCircle(sessionId, g_l, relationId, sentenceArray, typeFilter, allThoughtString, alreadyUsed, ii, session, env, filter_search, context_search, n_input);
				
				ii=ii-1;
				
				//Get the next down node
				tl = ExperienceManagerL2.getDownTabLink(sessionId, r, tl, type);
				
			}
			
		}
		
	}
	
	public static void check_sub_relation(long sessionId, String g_l, String r, JSONArray sentenceArray, String typeFilter, Vector<HashMap<String, Double>> allThoughtString, HashMap<String, Double> alreadyUsed, int depth, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws Exception {

		JSONArray tabLinkIds = RelationManager.listTabLinks(sessionId, r);
		for(int i=0;i<tabLinkIds.size();i++) {
			
			String currentRelationTabLink = ""+tabLinkIds.get(i);
			
			//Search in branch
			for(int iBranch=0;iBranch<RelationManager.allTypeList.length();iBranch++) {

				String type = RelationManager.allTypeList.charAt(iBranch)+"";
				String tl = ExperienceManagerL2.getFirstTabLink(sessionId, currentRelationTabLink, type);
				
				int ii = depth;
				while(tl!=null && ii>0) {
					
					//Get the relation id
					String relationId = Misc.atom(tl, 1, " ").substring(1);
					
					//Search in circle
					searchInCircle(sessionId, g_l, relationId, sentenceArray, typeFilter, allThoughtString, alreadyUsed, ii, session, env, filter_search, context_search, n_input);
					
					ii=ii-1;
					
					//Get the next down node
					tl = ExperienceManagerL2.getDownTabLink(sessionId, currentRelationTabLink, tl, type);
					
				}
				
			}
		
		}
		
	}
	
	//Calculate the cognitive distance
	public static double cognitiveDistance(double weight, String g_l, String r, String SUBTYPE, String r_l, String ct, JSONArray sentenceArray, String rlth, Vector<HashMap<String, Double>> allThoughtString, int depth, long sessionId, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws NumberFormatException, Exception {
		
		double result = 0;
		double percent = 0;
		double next = weight;
		
		if (r_l.equals(g_l)) {
			
			next += 1000000;
			
		}
		
		if (!filter_search.equals("")) {

			env.set("[R]", r);
			env.set("[SUBTYPE]", SUBTYPE);
			
			String b = Statement.eval(session, filter_search, env, "", "");
			if (b!=null && b.equals("1")) {
				
				next += 10;
			
			} else next -= 10;
			
		}

		//Search where start to search
		//Parse all thoughts from the relation
		String[] listThoughts = rlth.split(" ", -1);
		
		double[] res = calculate_for_each(allThoughtString, rlth, listThoughts, r);
		double found_th = res[1];
		result += res[0];
		
		listThoughts = rlth.replace(" TH[] ", "").replace("TH[] ", "").replace(" TH[]", "").split(" ", -1);
		int size_rThoughts = listThoughts.length;
		
		if (found_th>size_rThoughts) {
			percent = -1D/100D*(0D+found_th)/size_rThoughts;
		} else if (found_th<size_rThoughts) {
			percent = -1D/100D*(0D+size_rThoughts)/found_th;
		}
		
		// Search if need relations
		if (n_input.containsKey("C_"+r.replace("[", "-").replace("]", "-"))) {
			result += 1000;
		}
		
		//if (r.equals("RT[e]") || r.equals("RT[6]")) System.out.println(allThoughtString.size()+"#"+found_th+"/"+size_rThoughts+"#######r="+r+":result="+result+",percent="+percent+",next="+next+",depth="+depth+",th="+RelationManager.get_TH_TypeWeightValue(g_l, r, sentenceArray, sessionId, session, env));
		
		if (result>0) {
			
			if (rlth.indexOf("TH[]")>-1) return percent+result+next+depth+RelationManager.get_TH_TypeWeightValue(g_l, r, sentenceArray, sessionId, session, env, n_input);
			else return percent+result+next+depth;
			
		} else return 0D;
		
	}
	
	//Calculate the cognitive distance
	public static double simpleCognitiveDistance(double weight, String g_l, String r, String r_l, String ct, JSONArray sentenceArray, String rlth, Vector<HashMap<String, Double>> allThoughtString, int depth, long sessionId, SessionThread session, EnvManager env, String filter_search, JSONObject context_search, JSONObject n_input) throws NumberFormatException, Exception {
		
		double result = weight;
		double percent = 0;
		
		String[] listThoughts = rlth.split(" ", -1);
		
		double[] res = calculate_for_each(allThoughtString, rlth, listThoughts, r);
		double found_th = res[1];
		result += res[0];
		
		listThoughts = rlth.replace(" TH[] ", "").replace("TH[] ", "").replace(" TH[]", "").split(" ", -1);
		int size_rThoughts = listThoughts.length;
		
		if (found_th>size_rThoughts) {
			percent = -1D/100D*(0D+found_th)/size_rThoughts;
		} else if (found_th<size_rThoughts) {
			percent = -1D/100D*(0D+size_rThoughts)/found_th;
		}
		
		//System.out.println(allThoughtString.size()+"##"+r_l+"########r="+r+":result="+result+",percent="+percent+",depth="+depth+",th_dist="+RelationManager.get_TH_TypeWeightValue(g_l, r, sentenceArray, sessionId, session, env));
		
		if (result>0) {
			
			if (rlth.indexOf("TH[]")>-1) return percent+result+depth+RelationManager.get_TH_TypeWeightValue(g_l, r, sentenceArray, sessionId, session, env, n_input);
			else return percent+result+depth;
			
		} else return 0D;
		
	}
	
	//Calculate the cognitive distance
	public static double[] calculate_for_each(Vector<HashMap<String, Double>> allThoughtString, String rlth, String[] listThoughts, String r) throws Exception {
		
		HashMap<Double, Double> to_ordered = new HashMap<Double, Double>();
		
		for(int z = 0;z<listThoughts.length;z++) {
			
			double[] res = calculate(allThoughtString, rlth, listThoughts, z, r);
			to_ordered.put(res[0], res[1]);
			
		}
		
		List<Map.Entry<Double, Double>> ordered = new LinkedList<>( to_ordered.entrySet() );
		Collections.sort(ordered, new Comparator<Map.Entry<Double, Double>>() {
			@Override
			public int compare( Map.Entry<Double, Double> o1, Map.Entry<Double, Double> o2 )
			{
				return ( o2.getKey() ).compareTo( o1.getKey() );
			}
		} );
		
		double[] result = {ordered.get(0).getKey(), ordered.get(0).getValue()};
		
		return result;
		
	}
	
	//Calculate the cognitive distance
	public static double[] calculate(Vector<HashMap<String, Double>> allThoughtString, String rlth, String[] listThoughts, int lastPos, String r) throws Exception {
		
		int newPosition = -1;
		int lastPosition = -1;
		
		double found_th = 0;
		
		double[] result = {0, 0};
		
		for(int z = lastPos;z<listThoughts.length;z++) {
			//if (r.equals("RT[e]") || r.equals("RT[6]")) System.out.println(">>>>>>>>>>>>th>>>"+listThoughts[z]);
			boolean localAsNext = false;
			
			for(int j=lastPosition+1;j<allThoughtString.size();j++) {
				
				if (allThoughtString.get(j).containsKey(listThoughts[z])) {
					
					result[0] += 1000;
					//if (r.equals("RT[e]") || r.equals("RT[6]")) System.out.println(j+"=j---r: +100 >>> "+listThoughts[z]+"==="+listThoughts[z]);
					found_th++;
					
					localAsNext = true;
					
					newPosition = j;
					break;
					
				}
				
			}
			
			if (localAsNext) {
				
				if (lastPosition!=-1 && lastPosition+1==newPosition) {
					result[0] += 100;
					//if (r.equals("RT[e]") || r.equals("RT[6]")) System.out.println("---r: +50");
				}
				
				lastPosition = newPosition;
				
			}
			
		}
		
		result[1] = found_th;
		//if (r.equals("RT[e]") || r.equals("RT[6]")) System.out.println("---result[0]="+result[0]);
		
		return result;
		
	}

}
