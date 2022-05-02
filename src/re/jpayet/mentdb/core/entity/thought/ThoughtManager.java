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

package re.jpayet.mentdb.core.entity.thought;

import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL1;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class ThoughtManager {
	
	@SuppressWarnings("unchecked")
	public static JSONObject pearlNecklaceRelations(String key) throws Exception {
		
		//Initialization
		int[] autoincrement = {0};
		JSONObject result = new JSONObject();
		JSONObject nodes = new JSONObject();result.put("n", nodes);
		JSONArray edges = new JSONArray();result.put("e", edges);
		String lastKey = "";
		
		int concentration = ConcentrationManager.getConcentrationDepth("C[graph-relation]");
		
		JSONObject rec = Record2.getNode(key);
		
		if (rec==null) {

			throw new Exception("Sorry, the thought/relation id "+key+" does not exist.");

		}

		int id_thought2 = 0;
		
		if (key.startsWith("TH[")) {
			
			String currentThouhght = "Y"+autoincrement[0]+"_"+key;
			nodes.put(currentThouhght, key);
			lastKey = currentThouhght;
			
			JSONArray c = CircleManager.getIds("th", key);
			for(int u=0;u<c.size();u++) {
				
				String th = (String) c.get(u);
				
				if (!th.equals(key)) {
					
					nodes.put("t"+id_thought2+"_"+th, th);
					
					JSONObject e = new JSONObject();
					e.put("k", "#"+id_thought2+"_"+th+"_ct");
					e.put("n1", currentThouhght);
					e.put("n2", "t"+id_thought2+"_"+th);
					edges.add(e);
					
					id_thought2++;
					
				}
				
			}
			
		} else {
			
			String currentRelation = "V"+autoincrement[0]+"_"+key;
			nodes.put(currentRelation, key);
			lastKey = currentRelation;

			ThoughtManager.findParent(key, nodes, edges, autoincrement, lastKey);
			ThoughtManager.findCircle(key, nodes, edges, autoincrement, lastKey);
			
			JSONArray thoughts = RelationManager.showThoughtsRecursivelyRLTH(key);
			String thoughtLastKey = currentRelation;
			for(int i=0;i<thoughts.size();i++) {
				
				String thoughtId = thoughts.get(i)+"";
				
				autoincrement[0]++;
				String currentThouhght = "T"+autoincrement[0]+"_"+thoughtId;
				
				nodes.put(currentThouhght, thoughtId);
				JSONObject e = new JSONObject();
				e.put("k", "$"+thoughtLastKey+"_"+currentThouhght);
				e.put("n1", thoughtLastKey);
				e.put("n2", currentThouhght);
				edges.add(e);
				
				JSONArray c = CircleManager.getIds("th", thoughtId);
				for(int u=0;u<c.size();u++) {
					
					String th = (String) c.get(u);
					
					if (!th.equals(thoughtId)) {
						
						nodes.put("t"+id_thought2+"_"+th, th);
						
						e = new JSONObject();
						e.put("k", "#"+id_thought2+"_"+th+"_cr");
						e.put("n1", currentThouhght);
						e.put("n2", "t"+id_thought2+"_"+th);
						edges.add(e);
						
						id_thought2++;
						
					}
					
				}
				
				thoughtLastKey = currentThouhght;
				
			}
			
		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			
			branch(key, RelationManager.allTypeList.charAt(iType)+"", nodes, edges, autoincrement, lastKey, concentration);
			
		}
		
		if (key.startsWith("R")) {
			
			JSONArray tabLinks = RelationManager.listTabLinks(key);
			
			for(int i=0;i<tabLinks.size();i++) {
				
				String curTabLink = (String) tabLinks.get(i);
				autoincrement[0]++;
				nodes.put("M"+autoincrement[0]+"_"+curTabLink, (i+1)+"");
				JSONObject e = new JSONObject();
				e.put("k", "$"+lastKey+"_"+autoincrement[0]);
				e.put("n1", lastKey);
				e.put("n2", "M"+autoincrement[0]+"_"+curTabLink);
				edges.add(e);
				lastKey = "M"+autoincrement[0]+"_"+curTabLink;
				
				//Parse all relation types (Branchs)
				for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
					
					branch(curTabLink, RelationManager.allTypeList.charAt(iType)+"", nodes, edges, autoincrement, lastKey, concentration);
					
				}
				
			}
			
		}
		
		return result;
		
	}
	

	@SuppressWarnings("unchecked")
	public static void findCircle(String key, JSONObject nodes, JSONArray edges, int[] autoincrement, String lastKey) throws Exception {
		
		JSONObject bd = Record2.getNode(key);
		JSONObject currentCircle = null;
		String circle_id = (String) ((JSONObject) bd.get("c")).get("r");
		if (circle_id==null) currentCircle =  new JSONObject();
		else {
			
			currentCircle =  Record2.getNode(circle_id);
			
		}
		
		JSONObject langNode = new JSONObject();
		JSONObject rNode = new JSONObject();
		
		for(Object o1 : currentCircle.keySet()) {
			
			//get the current key
			String k = (String) o1;
			String l = Misc.atom(k, 1, " ");
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			if (!langNode.containsKey(l)) {
				
				autoincrement[0]++;
				String currentBranch = "C"+autoincrement[0]+"_"+l;
				
				JSONObject e = null;
				nodes.put(currentBranch, l);
				e = new JSONObject();
				e.put("k", lastKey+"_"+currentBranch);
				e.put("n1", lastKey);
				e.put("n2", currentBranch);
				edges.add(e);
				
				langNode.put(l, currentBranch);
				
			}
			
			if (!rNode.containsKey(r2)) {
				
				if (r1.equals(key)) {
					
					autoincrement[0]++;
					String currentRelation = "R"+autoincrement[0]+"_"+r2;
					nodes.put(currentRelation, r2);
					JSONObject e = new JSONObject();
					e.put("k", langNode.get(l)+"_"+currentRelation);
					e.put("n1", langNode.get(l));
					e.put("n2", currentRelation);
					edges.add(e);
					
					rNode.put(r2, null);
					
				}
				
			}
			
		}
		
	}
	

	@SuppressWarnings("unchecked")
	public static void findParent(String key, JSONObject nodes, JSONArray edges, int[] autoincrement, String lastKey) throws Exception {
		
		JSONObject bd = Record2.getNode(key);
		String parentId = (String) bd.get("p");
		
		if (parentId!=null && !parentId.equals("")) {
			
			autoincrement[0]++;
			nodes.put("V"+autoincrement[0]+"_"+parentId, parentId);
			JSONObject e = new JSONObject();
			e.put("k", "$"+lastKey+"_"+autoincrement[0]);
			e.put("n1", lastKey);
			e.put("n2", "V"+autoincrement[0]+"_"+parentId);
			edges.add(e);
			
			lastKey = "V"+autoincrement[0]+"_"+parentId;
			
			findParent(parentId, nodes, edges, autoincrement, lastKey);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void branch(String key, String branch, JSONObject nodes, JSONArray edges, int[] autoincrement, String lastKey, int depth) throws Exception {
		
		JSONArray list = RelationManager.showTabLinks(key, branch, depth);
		
		if (list.size()>0) {
			
			autoincrement[0]++;
			String currentBranch = "B"+autoincrement[0]+"_"+branch;
			
			nodes.put(currentBranch, branch);
			JSONObject e = new JSONObject();
			e.put("k", lastKey+"_"+currentBranch);
			e.put("n1", lastKey);
			e.put("n2", currentBranch);
			edges.add(e);
			
			lastKey = currentBranch;
			
		}
		
		for(int i=0;i<list.size();i++) {
			
			String tabLink = ""+list.get(i);
			String rel = Misc.atom(tabLink, 1, " ").substring(1);
			
			autoincrement[0]++;
			String currentRelation = "R"+autoincrement[0]+"_"+rel;
			nodes.put(currentRelation, rel);
			JSONObject e = new JSONObject();
			e.put("k", lastKey+"_"+currentRelation);
			e.put("n1", lastKey);
			e.put("n2", currentRelation);
			edges.add(e);
						
			lastKey = currentRelation;
			
		}
		
	}
	
	//Add a new thought
	public static String create(String wordName, String separator, String lang, String lock_translation, SessionThread session, EnvManager env) throws Exception {
		
		if (separator==null || separator.equals("")) {
			
			String thoughtId = createThought(wordName, lang, lock_translation);
			
			return thoughtId;
			
		} else {
		
			//Split words
			String[] words = Misc.splitWords(wordName, separator);
			
			String thoughtId = createThoughtComposed(wordName, separator, lang, words, lock_translation, session, env);
			
			return thoughtId;
			
		}
		
	}
	
	//Add a new thought
	@SuppressWarnings("unchecked")
	public static String createThought(String wordName, String lang, String lock_translation) throws Exception {
		
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		if (lock_translation==null || (!lock_translation.equals("1") && !lock_translation.equals("0"))) {

			throw new Exception("Sorry, the lock_translation parameter is not valid [must be 1 or 0].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}
		
		//The default something thought
		String thoughtId = "";
		
		if (wordName.equals("quelque chose") && lang.equals("fr") && !NodeManager.existNode("TH[]")) {
			
			thoughtId = "TH[]";
			
		} else {
			
			thoughtId = "TH["+SequenceManager.incr("thought")+"]";
			while (Record2.getNode(thoughtId)!=null) {
				
				thoughtId = "TH["+SequenceManager.incr("thought")+"]";
				
			}
			
		}
		
		ExperienceManagerL1.addNodeTabLink("W["+wordName+"]", thoughtId, lang, "word", true, true, 0);
		
		JSONObject bd = Record2.getNode(thoughtId);
		
		JSONObject ltr_obj = new JSONObject();
		if (lock_translation.equals("1")) {
			JSONObject o = new JSONObject();
			o.put("w", wordName);
			ltr_obj.put(lang, o);
			
		}
		bd.put("ltr", ltr_obj);
		bd.put("l", lang);
		JSONObject cir = new JSONObject();
		cir.put("th", null);
		bd.put("c", cir);
		
		Record2.update(thoughtId, bd.toJSONString());
		
		return thoughtId;

	}
	
	//Add a new thought (composed)
	@SuppressWarnings("unchecked")
	public static String createThoughtComposed(String wordName, String separator, String lang, String[] words, String lock_translation, SessionThread session, EnvManager env) throws Exception {
		
		if (lock_translation==null || (!lock_translation.equals("1") && !lock_translation.equals("0"))) {

			throw new Exception("Sorry, the lock_translation parameter is not valid [must be 1 or 0].");

		}
		
		//Initialization
		String gWordName = wordName;
		Vector<String> allThoughtIds = new Vector<String>();
		
		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word '"+wordName+"' does not exist.");

		}
		
		for(int iWord = 0;iWord<words.length;iWord++) {
			
			wordName = words[iWord];
			
			//The default something thought
			String thoughtId = "";
			
			thoughtId = "TH["+SequenceManager.incr("thought")+"]";
			while (Record2.getNode(thoughtId)!=null) {
				
				thoughtId = "TH["+SequenceManager.incr("thought")+"]";
				
			}
			
			ExperienceManagerL1.addNodeTabLink("W["+wordName+"]", thoughtId, lang, "word", true, true, 0);
			
			JSONObject bd = Record2.getNode(thoughtId);
			((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" W["+wordName+"]")).put("cw", gWordName);
			bd.put("l", lang);

			JSONObject cir = new JSONObject();
			cir.put("th", null);
			bd.put("c", cir);
			
			Record2.update(thoughtId, bd.toJSONString());
			
			allThoughtIds.add(thoughtId);
			
		}
		
		//Merge thoughts for all separate words
		for(int j=1;j<allThoughtIds.size();j++) {
			ThoughtManager.merge("th", allThoughtIds.get(0), allThoughtIds.get(j), session, env);
		}
		
		String thoughtId = "";
		
		if (gWordName.equals("quelque chose") && lang.equals("fr") && !NodeManager.existNode("TH[]")) {
			
			thoughtId = "TH[]";
			
		} else {
		
			thoughtId = "TH["+SequenceManager.incr("thought")+"]";
			while (Record2.getNode(thoughtId)!=null) {
				
				thoughtId = "TH["+SequenceManager.incr("thought")+"]";
				
			}
			
		}
		
		ExperienceManagerL1.addNodeTabLink("W["+gWordName+"]", thoughtId, lang, "word", true, true, 0);
		
		JSONObject bd = Record2.getNode(thoughtId);
		((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" W["+gWordName+"]")).put("cw", gWordName);
		
		JSONObject ltr_obj = new JSONObject();
		if (lock_translation.equals("1")) {
			JSONObject o = new JSONObject();
			o.put("w", wordName);
			ltr_obj.put(lang, o);
			
		}
		bd.put("ltr", ltr_obj);
		bd.put("l", lang);

		JSONObject cir = new JSONObject();
		cir.put("th", null);
		bd.put("c", cir);
		
		Record2.update(thoughtId, bd.toJSONString());
		
		ThoughtManager.merge("th", thoughtId, allThoughtIds.get(0), session, env);
		
		return thoughtId;

	}
	
	//Search a thought for a specific word
	public static String search(String wordName, String lang, String typeFilter) throws Exception {

		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}
		
		//Get the first tab link
		String currentTabLink = ExperienceManagerL1.getFirstTabLink("W["+wordName+"]", lang, true);
		
		String result = "";
		
		//Parse all tab links
		while (currentTabLink!=null) {
			
			//Get the current thought id
			String currentThoughtId = currentTabLink;
			
			//If not filter then select all thoughts
			if (typeFilter==null || typeFilter.equals("")) {
				
				result += "|"+currentThoughtId;
				
			} else {
			
				//Initialize the boolean to select the element
				boolean selectTheThought = false;
				
				String[] filters = typeFilter.split(",");
				for(int i=0;i<filters.length;i++) {
				
					//Get the current filter
					String currentFilterType = Misc.lrtrim(filters[i]);
					
					//Get the parent type of the filter
					JSONObject p_type_node = Record2.getNode("DT["+currentFilterType+"]");
					
					if (p_type_node==null) {
						throw new Exception("Sorry, the data type "+currentFilterType+" does not exist.");
					}
					
					String PARENT_TYPE = (String) p_type_node.get("p");
					
					selectTheThought = search_type_boolean(currentThoughtId, PARENT_TYPE);
					
					if (selectTheThought) break;
					
				}
				
				//Check if the thought is selected
				if (selectTheThought) {
					
					result += "|"+currentThoughtId;
	
				} else {

					result += search_type(currentThoughtId, filters, "U");
					result += search_type(currentThoughtId, filters, "A");
					
				}
				
			}
				
			currentTabLink = ExperienceManagerL1.getDownTabLink("W["+wordName+"]", currentTabLink, lang, true);
		}
		
		if (!result.equals("")) result = result.substring(1);
		
		return result;

	}
	
	public static String search_type(String currentThoughtId, String[] filters, String r_type) throws Exception {
		
		JSONArray relations = RelationManager.showTabLinks(currentThoughtId, r_type, ConcentrationManager.getConcentrationDepth("C[relation]"));
		
		boolean selectTheThought = false;
		
		//Parse all relation
		for(int j=0;j<relations.size();j++) {
			
			//Get the current relation
			String currentRelation = Misc.atom((String) relations.get(j), 1, " ").substring(1);
			
			//Initialize the boolean to select the element
			selectTheThought = false;
			
			for(int i=0;i<filters.length;i++) {
			
				//Get the current filter
				String currentFilterType = Misc.lrtrim(filters[i]);
				
				//Get the parent type of the filter
				JSONObject p_type_node = Record2.getNode("DT["+currentFilterType+"]");
				
				if (p_type_node==null) {
					throw new Exception("Sorry, the data type "+currentFilterType+" does not exist.");
				}
				
				String PARENT_TYPE = (String) p_type_node.get("p");
				
				selectTheThought = search_type_boolean(currentRelation, PARENT_TYPE);
				
				if (selectTheThought) break;
				
			}
			
			if (selectTheThought) break;
			
		}
		
		if (selectTheThought) {
			
			return "|"+currentThoughtId;

		} else return "";
		
	}
	
	@SuppressWarnings("unchecked")
	public static double probability_in_words(String ths, String words) throws Exception {
		
		//Get all thoughts from words
		JSONObject thJson = new JSONObject();
		
		int nbWord = Integer.parseInt(AtomFx.size(words, " "));
		
		for(int i=1;i<=nbWord;i++) {
		
			String curWord = AtomFx.get(words, ""+i, " ");
			
			if (NodeManager.existNode("W["+curWord+"]")) {
				JSONArray a = ThoughtManager.list(curWord);
				
				for(int j=0;j<a.size();j++) {
					
					String thoughtId = (String) ((JSONObject) a.get(j)).get("thoughtId");
					
					JSONArray c = CircleManager.getIds("th", thoughtId);
					for(int z=0;z<c.size();z++) {
						thJson.put(c.get(z)+"", 0);
					}
					
				}
			}
		
		}
		
		double inThought = 0;
		int nbThought = Integer.parseInt(AtomFx.size(ths, " "));
		
		for(int i=1;i<=nbThought;i++) {
		
			String curTh = AtomFx.get(ths, ""+i, " ");
			
			if (curTh.startsWith("TH")) {
			
				JSONArray c = CircleManager.getIds("th", curTh);
				
				for(int z=0;z<c.size();z++) {
					
					String t = c.get(z)+"";
					
					if (thJson.containsKey(t)) {
						
						inThought++;
						break;
						
					}
					
				}
				
			} else if (curTh.startsWith("R")) {
				
				JSONArray c = CircleManager.getIds("r", curTh);
				
				for(int z=0;z<c.size();z++) {
					
					String r = c.get(z)+"";
					
					String[] thoughts = ((String) Record2.getNode(r).get("rlth")).split(" ");
					int nb_r = thoughts.length;
					int i_r = 0;
					
					for(int y=0;y<thoughts.length;y++) {
						
						if (thJson.containsKey(thoughts[y])) {
							
							i_r++;
							
						}
						
					}
					//System.out.println(r+":"+"######"+(i_r/nb_r*100)+"%, "+i_r+"/"+nb_r);
					if (i_r/nb_r*100>80) {
						
						inThought++;
						break;
						
					}
					
				}
				
			}
			
		}
		//System.out.println("######"+(inThought/nbThought*100)+"%, "+inThought+"/"+nbThought);
		return inThought/nbThought*100;
		
	}
	
	public static boolean search_type_boolean(String target, String PARENT_TYPE) throws Exception {
		
		boolean selectRelation = false;
		
		//Search all type relations for the current thought id
		JSONArray relationTypes = RelationManager.showTabLinks(target, "T", ConcentrationManager.getConcentrationDepth("C[relation]"));
		
		//Parse all relation types
		for(int j=0;j<relationTypes.size();j++) {
			
			//Get the current relation type
			String currentRelationType = Misc.atom((String) relationTypes.get(j), 1, " ").substring(1);
			
			//Get the parent type of the current relation type
			String CUR_PARENT_TYPE = (String) Record2.getNode(currentRelationType).get("p");
			
			//Mark as selected if equals
			if (CUR_PARENT_TYPE!=null && PARENT_TYPE.equals(CUR_PARENT_TYPE)) {
				selectRelation = true;
				break;
			}
			
		}
		
		return selectRelation;
		
	}
	
	//Get all thoughts linked with a word
	public static JSONArray list(String wordName, String lang) throws Exception {

		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}
		
		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");
		
		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}
		
		return ExperienceManagerL1.getAllNodeTabLinks("W["+wordName+"]", lang);

	}
	
	//Get all thoughts linked with a word
	@SuppressWarnings("unchecked")
	public static JSONArray list(String wordName) throws Exception {

		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Get all languages
		JSONArray languages = LanguageManager.showAll();
		for(int i=0;i<languages.size();i++) {
			
			//Get the thought
			String lang = languages.get(i)+"";
			
			try {
			
				JSONArray array = ExperienceManagerL1.getAllNodeTabLinksForStimulation("W["+wordName+"]", lang);
				for(int k=0;k<array.size();k++) {
					result.add(array.get(k));
				}
				
			} catch (Exception e) {}
			
		}
		
		return result;

	}
	
	//Get words from thought
	@SuppressWarnings("unchecked")
	public static JSONArray getWords(String thoughtId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		if (thoughtId==null) {
			
			return result;
		
		} else {
			
			JSONArray cir = CircleManager.getIds("th", thoughtId);
			for(int i=0;i<cir.size();i++) {
				
				String th = (String) cir.get(i);
		
				JSONObject bwTmp = Record2.getNode(th);
				
				if (bwTmp==null) {
					
					return result;
					
				}
				
				Set<String> entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
				for(String langTmp : entrySetTmp) {
					
					JSONObject obj = new JSONObject();
					String lnk_sep = (""+bwTmp.get("lnk"));
					if (lnk_sep.equals("@[th_fr_separator_']") || lnk_sep.equals("@[th_fr_separator_']")) {
						obj.put("sep", "1");
					}
					JSONObject curNode = ((JSONObject) ((JSONObject) bwTmp.get("ud")).get(langTmp));
					if (curNode.containsKey("cw")) {
						obj.put("word", "W["+curNode.get("cw")+"]");
					} else {
						String w = langTmp.substring(3);
						obj.put("word", w);
					}
					obj.put("lang", Misc.atom(langTmp, 1, " "));
					if (!result.contains(obj)) result.add(obj);
					
				}
			
			}
			
			return result;
		}
		
	}
	
	//Get words from thought
	@SuppressWarnings("unchecked")
	public static JSONArray getWords(String thoughtId, String lang, Vector<MQLValue> inputVector) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		if (thoughtId==null) {
			
			return result;
		
		} else {
			
			if (inputVector.size()>0 && thoughtId.equals("TH[]") && !inputVector.get(0).value.equals("")) {
				
				JSONObject obj = new JSONObject();
				obj.put("word", "W["+inputVector.get(0).value+"]");
				obj.put("lang", lang);
				result.add(obj);
				
				inputVector.remove(0);
				
				return result;
				
			}
			
			JSONArray cir = CircleManager.getIds("th", thoughtId);
			for(int i=0;i<cir.size();i++) {
				
				String th = (String) cir.get(i);
		
				JSONObject bwTmp = Record2.getNode(th);
				
				if (bwTmp==null) {
					
					return result;
					
				}
				
				Set<String> entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
				
				for(String langTmp : entrySetTmp) {
					
					if (Misc.atom(langTmp, 1, " ").equals(lang)) {
						
						JSONObject obj = new JSONObject();
						String lnk_sep = (""+bwTmp.get("lnk"));
						if (lnk_sep.equals("@[th_fr_separator_']") || lnk_sep.equals("@[th_fr_separator_']")) {
							obj.put("sep", "1");
						}
						JSONObject curNode = ((JSONObject) ((JSONObject) bwTmp.get("ud")).get(langTmp));
						if (curNode.containsKey("cw")) obj.put("word", "W["+curNode.get("cw")+"]");
						else {
							String w = langTmp.substring(3);
							obj.put("word", w);
						}
						obj.put("lang", Misc.atom(langTmp, 1, " "));
						if (!result.contains(obj)) result.add(obj);
						
					}
					
				}
				
			}
			
			if (result.size()==0) {
				
				for(int i=0;i<cir.size();i++) {
					
					String th = (String) cir.get(i);
			
					JSONObject bwTmp = Record2.getNode(th);
					
					if (bwTmp==null) {
						
						return result;
						
					}
					
					Set<String> entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
				
					for(String langTmp : entrySetTmp) {
						
						JSONObject obj = new JSONObject();
						String lnk_sep = (""+bwTmp.get("lnk"));
						if (lnk_sep.equals("@[th_fr_separator_']") || lnk_sep.equals("@[th_fr_separator_']")) {
							obj.put("sep", "1");
						}
						JSONObject curNode = ((JSONObject) ((JSONObject) bwTmp.get("ud")).get(langTmp));
						if (curNode.containsKey("cw")) obj.put("word", "W["+curNode.get("cw")+"]");
						else {
							String w = langTmp.substring(3);
							obj.put("word", w);
						}
						obj.put("lang", Misc.atom(langTmp, 1, " "));
						if (!result.contains(obj)) result.add(obj);
						
					}
					
				}
				
			}
			
			return result;
			
		}
		
	}
	
	//Get words from thought
	@SuppressWarnings("unchecked")
	public static JSONArray getWordsForTranslation(String thoughtId, String lang, Vector<MQLValue> inputVector, String lock_lang) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		if (thoughtId==null) {
			
			return result;
		
		} else {
			
			if (inputVector.size()>0 && thoughtId.equals("TH[]") && !inputVector.get(0).value.equals("")) {
				
				JSONObject obj = new JSONObject();
				obj.put("word", "W["+inputVector.get(0).value+"]");
				obj.put("lang", lang);
				result.add(obj);
				
				inputVector.remove(0);
				
				return result;
				
			}
		
			JSONObject bwTmp = Record2.getNode(thoughtId);
			
			if (bwTmp==null) {
				
				return result;
				
			}
			
			Set<String> entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
			
			JSONObject ltr_obj = (JSONObject) bwTmp.get("ltr");
			if (ltr_obj.containsKey(lock_lang)) {
				
				JSONObject obj = new JSONObject();
				obj.put("word", "W[\""+((JSONObject) ltr_obj.get(lock_lang)).get("w")+"\"]");
				obj.put("lang", lock_lang);
				obj.put("force", "1");
				result.add(obj);
				
				return result;
				
			} else if (ltr_obj.containsKey(lang)) {
				
				JSONObject obj = new JSONObject();
				obj.put("word", "W[\""+((JSONObject) ltr_obj.get(lang)).get("w")+"\"]");
				obj.put("lang", lang);
				obj.put("force", "1");
				result.add(obj);
				
				return result;
				
			} else if (ltr_obj.size()>0) {
				
				for(Object ltr_other: ltr_obj.keySet()) {
					
					String other_lang = (String) ltr_other;
					
					JSONObject obj = new JSONObject();
					obj.put("word", "W[\""+((JSONObject) ltr_obj.get(other_lang)).get("w")+"\"]");
					obj.put("lang", other_lang);
					obj.put("force", "1");
					result.add(obj);
					
					break;
					
				}
				
				return result;
				
			} else {
				
				JSONArray cir = CircleManager.getIds("th", thoughtId);
				for(int i=0;i<cir.size();i++) {
					
					String th = (String) cir.get(i);
					
					bwTmp = Record2.getNode(th);
					
					entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
			
					for(String langTmp : entrySetTmp) {
						
						if (Misc.atom(langTmp, 1, " ").equals(lang)) {
							
							JSONObject obj = new JSONObject();
							String lnk_sep = (""+bwTmp.get("lnk"));
							if (lnk_sep.equals("@[th_fr_separator_']") || lnk_sep.equals("@[th_fr_separator_']")) {
								obj.put("sep", "1");
							}
							JSONObject curNode = ((JSONObject) ((JSONObject) bwTmp.get("ud")).get(langTmp));
							if (curNode.containsKey("cw")) obj.put("word", "W["+curNode.get("cw")+"]");
							else {
								String w = langTmp.substring(3);
								obj.put("word", w);
							}
							obj.put("lang", Misc.atom(langTmp, 1, " "));
							if (!result.contains(obj)) result.add(obj);
							
						}
						
					}
					
				}
				
				if (result.size()==0) {
					
					for(int i=0;i<cir.size();i++) {
						
						String th = (String) cir.get(i);
						
						bwTmp = Record2.getNode(th);
						
						entrySetTmp = ((JSONObject) bwTmp.get("ud")).keySet();
					
						for(String langTmp : entrySetTmp) {
							
							JSONObject obj = new JSONObject();
							String lnk_sep = (""+bwTmp.get("lnk"));
							if (lnk_sep.equals("@[th_fr_separator_']") || lnk_sep.equals("@[th_fr_separator_']")) {
								obj.put("sep", "1");
							}
							JSONObject curNode = ((JSONObject) ((JSONObject) bwTmp.get("ud")).get(langTmp));
							if (curNode.containsKey("cw")) obj.put("word", "W["+curNode.get("cw")+"]");
							else {
								String w = langTmp.substring(3);
								obj.put("word", w);
							}
							obj.put("lang", Misc.atom(langTmp, 1, " "));
							if (!result.contains(obj)) result.add(obj);
							
						}
						
					}
					
				}
				
				return result;
				
			}
			
		}
		
	}

	//Stimulate relations
	@SuppressWarnings("unchecked")
	public static void stimulateRelations(String thoughtId, JSONArray rels, EnvManager env, SessionThread session) throws Exception {

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}

		//Try to get the thought
		JSONObject recThought = Record2.getNode(thoughtId);

		if (recThought==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Get all relations
		JSONArray relations = RelationManager.showTabLinks(thoughtId, "L", ConcentrationManager.getConcentrationDepth("C[relation]"));
		
		//Parse all relations
		for(int i = relations.size()-1;i>=0;i--) {
			
			String relationId = Misc.atom((relations.get(i)+""), 1, " ").substring(1);
			
			//Stimulate the relation
			RelationManager.stimulate(relationId, true);

			JSONObject bd = Record2.getNode(relationId);
			String l = ""+bd.get("l");
			
			JSONArray words = RelationManager.showWords(relationId, l, new Vector<MQLValue>(), env, session);
			
			for(int j=0;j<words.size();j++) {
				
				JSONArray cur = (JSONArray) words.get(j);
				
				for(int k=0;k<cur.size();k++) {
					
					JSONObject obj = (JSONObject) cur.get(k);

					String lang = obj.get("lang")+"";
					String word = (obj.get("word")+"");
					word = word.substring(2, word.length()-1);
					
					String[] wordList = word.split(" ");

					for(int z=0;z<wordList.length;z++) {
						WordManager.stimulate(wordList[z], lang);
					}
					
				}
				
			}
			
			rels.add(0, relationId);
			
		}
		
	}

	//Stimulate a thought
	@SuppressWarnings("unchecked")
	public static void stimulate(String thoughtId) throws Exception {

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}
		
		JSONObject th = Record2.getNode(thoughtId);

		if (th==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Stimulate all words
		JSONArray words = ThoughtManager.getWords(thoughtId);

		for(int i=0;i<words.size();i++) {
			
			JSONObject w = (JSONObject) words.get(i);
			
			String str_w = ((String) w.get("word"));
			
			WordManager.stimulate(str_w.substring(2, str_w.length()-1), (String) w.get("lang"));
			
		}

		//Try to get the thought
		JSONObject recThought = ((JSONObject) JsonManager.load(th.toJSONString()));
		
		//Get all links
		Set<String> entrySet = ((JSONObject) recThought.get("ud")).keySet();
		for(String entry : entrySet) {
			
			String lang = entry.substring(0, 2);
			String word = entry.substring(3, entry.length());
			
			//Delete the node
			ExperienceManagerL1.deleteNodeTabLink(word, thoughtId, lang);
			
			String firstThought = ExperienceManagerL1.getFirstTabLink(word, lang, true);
			JSONObject obj = new JSONObject();
			obj.put("utl", null); //First tab link
			obj.put("dtl", firstThought); //Last tab link
			
			if (((JSONObject) ((JSONObject) recThought.get("ud")).get(entry)).containsKey("cw")) {
				obj.put("cw", ""+((JSONObject) ((JSONObject) recThought.get("ud")).get(entry)).get("cw"));
			}
			
			((JSONObject) recThought.get("ud")).put(entry, obj);
			
			recThought.put("w", Integer.parseInt(""+recThought.get("w"))+1);
			
			Record2.update(thoughtId, recThought.toJSONString());
			
			//Recreate the thought
			ExperienceManagerL1.mergeNodeTabLink(word, thoughtId, lang, "word", true, false);
			
		}
		
	}

	//Merge two thought
	public static void merge(String level, String thoughtId1, String thoughtId2, SessionThread session, EnvManager env) throws Exception {

		CircleManager.mergeCircle(level, thoughtId1, thoughtId2, env, session);

	}
	
	//Delete a thought
	public static void delete(String thoughtId) throws Exception {

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}

		//Try to get the thought
		JSONObject recThought = Record2.getNode(thoughtId);

		if (recThought==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			
			if (((JSONObject) ((JSONObject) recThought.get("fl")).get(RelationManager.allTypeList.charAt(iType)+"")).get("ftl")!=null) {
				
				throw new Exception("Sorry, the thought <"+thoughtId+"> cannot be deleted due to under tab links ("+RelationManager.allTypeList.charAt(iType)+""+"). Please, delete the relations associated before.");
				
			}
			
		}
		
		JSONObject ud = ((JSONObject) recThought.get("ud"));
		
		for (Object key : ud.keySet()) {
			
	        //based on you key types
	        String keyStr = (String)key;

	        String lang = keyStr.substring(0, 2);
	        keyStr = keyStr.substring(3, keyStr.length());
	        
			ExperienceManagerL1.deleteNodeTabLink(keyStr, thoughtId, lang);
			
			//Search in the word if there is thought
			JSONObject w = Record2.getNode(keyStr);
			
			boolean b = true;
			for (Object keyW : ((JSONObject) w.get("fl")).keySet()) {

		        //based on you key types
		        String keyStrW = (String)keyW;

		        if (((JSONObject) ((JSONObject) w.get("fl")).get(keyStrW)).get("ftl")!=null) {
		        	
					b = false;
		        	break;
		        	
		        }

			}

			//Delete the word
			if (b) {
				
				keyStr = keyStr.substring(2, keyStr.length()-1);
				
				//Parse all letters in the word
				for(int i=0;i<keyStr.length();i++) {
					
					//Get the current letter
					String currentLetter = keyStr.substring(i, i+1);
					ExperienceManagerL1.deleteNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(keyStr)+" "+i+" "+lang+"]", lang);
					
				}
				
				Record2.remove("W["+keyStr+"]");
				
			}
	        
		}
		
	}
	
	//Delete a thought
	public static void deleteLang(String thoughtId, String searchLang) throws Exception {

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}

		//Try to get the thought
		JSONObject recThought = Record2.getNode(thoughtId);

		if (recThought==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			
			if (((JSONObject) ((JSONObject) recThought.get("fl")).get(RelationManager.allTypeList.charAt(iType)+"")).get("ftl")!=null) {
				
				throw new Exception("Sorry, the thought <"+thoughtId+"> cannot be deleted due to under tab links ("+RelationManager.allTypeList.charAt(iType)+""+"). Please, delete the relations associated before.");
				
			}
			
		}
		
		JSONObject ud = ((JSONObject) recThought.get("ud"));
		
		for (Object key : ud.keySet()) {
			
	        //based on you key types
	        String keyStr = (String)key;

	        String lang = keyStr.substring(0, 2);
	        
	        if (lang.equals(searchLang)) {
	        
		        keyStr = keyStr.substring(3, keyStr.length());
		        
				ExperienceManagerL1.deleteNodeTabLink(keyStr, thoughtId, lang);
				
				//Search in the word if there is thought
				JSONObject w = Record2.getNode(keyStr);
				
				boolean b = true;
				for (Object keyW : ((JSONObject) w.get("fl")).keySet()) {
	
			        //based on you key types
			        String keyStrW = (String)keyW;
	
			        if (((JSONObject) ((JSONObject) w.get("fl")).get(keyStrW)).get("ftl")!=null) {
			        	
						b = false;
			        	break;
			        	
			        }
	
				}
	
				//Delete the word
				if (b) {
					
					keyStr = keyStr.substring(2, keyStr.length()-1);
					
					//Parse all letters in the word
					for(int i=0;i<keyStr.length();i++) {
						
						//Get the current letter
						String currentLetter = keyStr.substring(i, i+1);
						ExperienceManagerL1.deleteNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(keyStr)+" "+i+" "+lang+"]", lang);
			
					}
					
					Record2.remove("W["+keyStr+"]");
					
				}
				
			}
	        
		}
		
	}
	
	//Delete a thought
	public static void deleteWord(String thoughtId, String searchWord) throws Exception {

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}

		//Try to get the thought
		JSONObject recThought = Record2.getNode(thoughtId);

		if (recThought==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			
			if (((JSONObject) ((JSONObject) recThought.get("fl")).get(RelationManager.allTypeList.charAt(iType)+"")).get("ftl")!=null) {
				
				throw new Exception("Sorry, the thought <"+thoughtId+"> cannot be deleted due to under tab links ("+RelationManager.allTypeList.charAt(iType)+""+"). Please, delete the relations associated before.");
				
			}
			
		}
		
		JSONObject ud = ((JSONObject) recThought.get("ud"));
		
		for (Object key : ud.keySet()) {
			
	        //based on you key types
	        String keyStr = (String)key;

	        String lang = keyStr.substring(0, 2);
	        
	        keyStr = keyStr.substring(3, keyStr.length());
	        
	        if (keyStr.substring(2, keyStr.length()-1).equals(searchWord)) {
		        
				ExperienceManagerL1.deleteNodeTabLink(keyStr, thoughtId, lang);
				
				//Search in the word if there is thought
				JSONObject w = Record2.getNode(keyStr);
				
				boolean b = true;
				for (Object keyW : ((JSONObject) w.get("fl")).keySet()) {
	
			        //based on you key types
			        String keyStrW = (String)keyW;
	
			        if (((JSONObject) ((JSONObject) w.get("fl")).get(keyStrW)).get("ftl")!=null) {
			        	
						b = false;
			        	break;
			        	
			        }
	
				}
	
				//Delete the word
				if (b) {
					
					keyStr = keyStr.substring(2, keyStr.length()-1);
					
					//Parse all letters in the word
					for(int i=0;i<keyStr.length();i++) {
						
						//Get the current letter
						String currentLetter = keyStr.substring(i, i+1);
						ExperienceManagerL1.deleteNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(keyStr)+" "+i+" "+lang+"]", lang);
			
					}
					
					Record2.remove("W["+keyStr+"]");
					
				}
				
			}
	        
		}
		
	}
	
	//Delete a thought
	public static void delete(String wordName, String thoughtId, String lang) throws Exception {

		if (wordName.length()==0) {

			throw new Exception("Sorry, the word "+wordName+" is not valid [required].");

		}

		if (thoughtId.length()==0) {

			throw new Exception("Sorry, the thought id "+thoughtId+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record2.getNode("W["+wordName+"]");

		if (rec==null) {

			throw new Exception("Sorry, the word "+wordName+" does not exist.");

		}

		//Try to get the thought
		JSONObject recThought = Record2.getNode(thoughtId);

		if (recThought==null) {

			throw new Exception("Sorry, the thought "+thoughtId+" does not exist.");

		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			
			if (((JSONObject) ((JSONObject) recThought.get("fl")).get(RelationManager.allTypeList.charAt(iType)+"")).get("ftl")!=null) {
				
				throw new Exception("Sorry, the thought <"+thoughtId+"> cannot be deleted due to under tab links ("+RelationManager.allTypeList.charAt(iType)+""+"). Please, delete the relations associated before.");
				
			}
			
		}

		ExperienceManagerL1.deleteNodeTabLink("W["+wordName+"]", thoughtId, lang);
		
		//Search in the word if there is thought
		String keyStr = "W["+wordName+"]";
		JSONObject w = Record2.getNode(keyStr);
		
		boolean b = true;
		for (Object keyW : ((JSONObject) w.get("fl")).keySet()) {

	        //based on you key types
	        String keyStrW = (String)keyW;

	        if (((JSONObject) ((JSONObject) w.get("fl")).get(keyStrW)).get("ftl")!=null) {
	        	
				b = false;
	        	break;
	        	
	        }

		}

		//Delete the word
		if (b) {
			
			keyStr = keyStr.substring(2, keyStr.length()-1);
			
			//Parse all letters in the word
			for(int i=0;i<keyStr.length();i++) {
				
				//Get the current letter
				String currentLetter = keyStr.substring(i, i+1);
				ExperienceManagerL1.deleteNodeTabLink("S["+currentLetter+"]", "TL["+StringFx.str_to_hex(keyStr)+" "+i+" "+lang+"]", lang);
	
			}
			
			Record2.remove("W["+keyStr+"]");
			
		}

	}

}
