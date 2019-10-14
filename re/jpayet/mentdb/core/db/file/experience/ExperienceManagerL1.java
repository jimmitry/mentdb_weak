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

package re.jpayet.mentdb.core.db.file.experience;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.ext.tools.Misc;

//The node class to store basic tab link to simulate the experience (with language)
public class ExperienceManagerL1 {
	
	//Properties
	public JSONObject dataNode = null;
	
	//Constructor
	@SuppressWarnings("unchecked")
	public ExperienceManagerL1(String word, String key, String lang, boolean withfl, boolean withfllang, int perception) {
		
		//Initialization
		dataNode = new JSONObject();
		
		//Add fields
		dataNode.put("k", key); //Key
		dataNode.put("w", perception); //Key

		//Emotion
		dataNode.put("e", new JSONObject());
		dataNode.put("lnk", "");
		
		JSONObject langObj = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("utl", null); //Up tab link
		obj.put("dtl", null); //Down tab link
		langObj.put(lang+" "+word, obj);

		dataNode.put("ud", langObj); //Lang id
		
		if (withfl) {
			
			if (withfllang) {
		
				JSONObject objFl = new JSONObject();
				objFl.put("ftl", null); //First tab link
				objFl.put("ltl", null); //Last tab link
				
				JSONObject objType = new JSONObject();
				
				//Parse all relation types (Branchs)
				for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
					objType.put(RelationManager.allTypeList.charAt(iType)+"", objFl);
				}
				
				dataNode.put("fl", objType); //Lang id
				
			} else {
				
				JSONObject objFl = new JSONObject();
				objFl.put("ftl", null); //First tab link
				objFl.put("ltl", null); //Last tab link
				
				dataNode.put("fl", objFl); //Lang id
				
			}
			
		}
		
	}
	
	//Search thoughts into relations
	@SuppressWarnings("unchecked")
	public static JSONObject searchThoughts(long sessionId, String thoughtId1, String type) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		
		//Get relations
		JSONArray a = RelationManager.showTabLinks(sessionId, thoughtId1, type, ConcentrationManager.getConcentrationDepth("C[relation]"));
		
		//Parse relations
		for(int i=0;i<a.size();i++) {
			
			//Get the current relation id
			String currentRel = Misc.atom((String) a.get(i), 1, " ").substring(1);
			String thoughtsToSearch = RelationManager.showThoughtsRecursivelyRLTH(sessionId, currentRel).toJSONString();
			result.put(thoughtsToSearch, currentRel);
			
		}

		return result;
		
	}
	
	//Move relations
	@SuppressWarnings("unchecked")
	public static void moveRelations(long sessionId, String thoughtId1, String thoughtId2, String type) throws Exception {
		
		//Get all relations
		//Get the first
		String tl2 = ExperienceManagerL2.getFirstTabLink(sessionId, thoughtId2, type);
		
		//Add only if null
		if (tl2!=null) {
			
			//Get the node
			String nextTl2 = ExperienceManagerL2.getDownTabLink(sessionId, thoughtId2, tl2, type);
			String tl1 = ExperienceManagerL2.getFirstTabLink(sessionId, thoughtId1, type);
			JSONObject btl2 = Record.getNode(sessionId, tl2);
			JSONObject obj = new JSONObject();
			obj.put("utl", null); //First tab link
			obj.put("dtl", tl1); //Last tab link
			
			((JSONObject) btl2.get("ud")).put(thoughtId1, obj);
			((JSONObject) btl2.get("ud")).remove(thoughtId2);
			String r = Misc.atom(tl2, 1, " ").substring(1);
			NodeManager.oupdate(sessionId, r, "/", "lth", NodeManager.select(sessionId, r, "/lth").replace(thoughtId2, thoughtId1), "STR");
			NodeManager.oupdate(sessionId, r, "/", "rlth", NodeManager.select(sessionId, r, "/rlth").replace(thoughtId2, thoughtId1), "STR");
			Record.update(sessionId, tl2, btl2.toJSONString());
			ExperienceManagerL2.mergeNodeTabLink(sessionId, thoughtId1, tl2, type);
			
			//Get the down node
			tl2 = nextTl2;
			
			while(tl2!=null) {
				
				//Get the node
				nextTl2 = ExperienceManagerL2.getDownTabLink(sessionId, thoughtId2, tl2, type);
				tl1 = ExperienceManagerL2.getFirstTabLink(sessionId, thoughtId1, type);
				btl2 = Record.getNode(sessionId, tl2);
				obj = new JSONObject();
				obj.put("utl", null); //First tab link
				obj.put("dtl", tl1); //Last tab link
				
				((JSONObject) btl2.get("ud")).put(thoughtId1, obj);
				((JSONObject) btl2.get("ud")).remove(thoughtId2);
				r = Misc.atom(tl2, 1, " ").substring(1);
				NodeManager.oupdate(sessionId, r, "/", "lth", NodeManager.select(sessionId, r, "/lth").replace(thoughtId2, thoughtId1), "STR");
				NodeManager.oupdate(sessionId, r, "/", "rlth", NodeManager.select(sessionId, r, "/rlth").replace(thoughtId2, thoughtId1), "STR");
				Record.update(sessionId, tl2, btl2.toJSONString());
				ExperienceManagerL2.mergeNodeTabLink(sessionId, thoughtId1, tl2, type);
				
				//Get the next down node
				tl2 = nextTl2;
				
			}

		}
		
	}
	
	/*
	//Merge thoughts
	@SuppressWarnings("unchecked")
	public static void merge(long sessionId, String thoughtId1, String thoughtId2) throws Exception {

		JSONObject th2 = Record.getNode(sessionId, thoughtId2);
		JSONObject ltr_th2 = (JSONObject) th2.get("ltr");
		
		if (ltr_th2!=null) {
			
			JSONObject th1 = Record.getNode(sessionId, thoughtId1);
			JSONObject ltr_th1 = (JSONObject) th1.get("ltr");
		
			//Save lock translations
			for(Object ltr_obj : ltr_th2.keySet()) {
				
				String key = (String) ltr_obj;
				
				if (!ltr_th1.containsKey(key)) {
					
					ltr_th1.put(key, ltr_th2.get(key));
					
				}
				
			}
			
		}
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			moveRelations(sessionId, thoughtId1, thoughtId2, RelationManager.allTypeList.charAt(iType)+"");
		}
		
		JSONObject bd2 = ((JSONObject) JsonManager.load(Record.getNode(sessionId, thoughtId2).toJSONString()));
		
		//Get all links
		Set<String> entrySet = ((JSONObject) bd2.get("ud")).keySet();
		for(String entry : entrySet) {

			String lang = entry.substring(0, 2);
			String word = entry.substring(3, entry.length());
			
			//Delete the 2th thought
			ExperienceManagerL1.deleteNodeTabLink(sessionId, word, thoughtId2, lang);

			JSONObject bd1 = Record.getNode(sessionId, thoughtId1);
			
			if (!((JSONObject) bd1.get("ud")).containsKey(entry)) {
			
				//The second word must pass into the thoughtId1
				String firstThought = ExperienceManagerL1.getFirstTabLink(sessionId, word, lang, true);
				JSONObject obj = new JSONObject();
				obj.put("utl", null); //First tab link
				obj.put("dtl", firstThought); //Last tab link
				
				if (((JSONObject) ((JSONObject) bd2.get("ud")).get(lang+" "+word)).containsKey("cw")) {
					obj.put("cw", ""+((JSONObject) ((JSONObject) bd2.get("ud")).get(lang+" "+word)).get("cw"));
				}
				
				((JSONObject) bd1.get("ud")).put(entry, obj);
				
				//Save the first thought
				Record.update(sessionId, thoughtId1, bd1.toJSONString());
				
				//The word must pointed to the thoughtId1
				ExperienceManagerL1.mergeNodeTabLink(sessionId, word, thoughtId1, lang, "word", true, false);
				
			} else {
				
				if (((JSONObject) ((JSONObject) bd2.get("ud")).get(lang+" "+word)).containsKey("cw")) {
					((JSONObject) ((JSONObject) bd1.get("ud")).get(lang+" "+word)).put("cw", ""+((JSONObject) ((JSONObject) bd2.get("ud")).get(lang+" "+word)).get("cw"));
				}
				
				//Save the first thought
				Record.update(sessionId, thoughtId1, bd1.toJSONString());
				
			}
			
		}
		
		//Remove the old thought
		Record.remove(sessionId, thoughtId2);
		
	}*/
	
	//Add a new tab link
	@SuppressWarnings("unchecked")
	public static void addNewTabLink(long sessionId, String key, String tabLinkKey, String dtl, String lang, boolean withfl, boolean withfllang, int perception) throws Exception {
		
		//Tab link initialization
		ExperienceManagerL1 tl = new ExperienceManagerL1(key, tabLinkKey, lang, withfl, withfllang, perception);
		
		((JSONObject) ((JSONObject) tl.dataNode.get("ud")).get(lang+" "+key)).put("utl", null);
		((JSONObject) ((JSONObject) tl.dataNode.get("ud")).get(lang+" "+key)).put("dtl", dtl);
		
		Record.add(sessionId, tabLinkKey, tl.dataNode.toJSONString());
		
	}
	
	//Update the second tab link
	@SuppressWarnings("unchecked")
	public static void updateSecondTabLink(long sessionId, String key, String tabLinkKey, String utl, String lang) throws Exception {
		
		//Get the second tab link
		JSONObject bd = Record.getNode(sessionId, tabLinkKey);
		
		((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+key)).put("utl", utl);
		
		Record.update(sessionId, tabLinkKey, bd.toJSONString());
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return dataNode.toJSONString();
		
	}

	//Delete all node tab links by lang
	public static void deleteAllNodeTabLinksByLang(long sessionId, String nodeKey, String lang) throws Exception {
		
		//Get the first tab link
		String currentTabLink = getFirstTabLink(sessionId, nodeKey, lang, true);
		
		//Parse all tab links
		while (currentTabLink!=null) {
			
			//Delete the tab link
			deleteNodeTabLink(sessionId, nodeKey, currentTabLink, lang);
			
			currentTabLink = getFirstTabLink(sessionId, nodeKey, lang, true);
			
		}
		
	}

	//Get all node tab links
	@SuppressWarnings("unchecked")
	public static JSONArray getAllNodeTabLinks(long sessionId, String nodeKey, String lang) throws Exception {
		
		//Get the first tab link
		String currentTabLink = getFirstTabLink(sessionId, nodeKey, lang, true);
		
		JSONArray result = new JSONArray();
		
		//Parse all tab links
		while (currentTabLink!=null) {
			
			result.add(currentTabLink);
			currentTabLink = getDownTabLink(sessionId, nodeKey, currentTabLink, lang, true);
		}
		
		return result;
		
	}

	//Get all node tab links
	@SuppressWarnings("unchecked")
	public static JSONArray getAllNodeTabLinksForStimulation(long sessionId, String nodeKey, String lang) throws Exception {
		
		//Get the first tab link
		String currentTabLink = getFirstTabLink(sessionId, nodeKey, lang, true);
		JSONArray result = new JSONArray();
		
		//Parse all tab links
		while (currentTabLink!=null) {
			
			JSONObject obj = new JSONObject();
			obj.put("thoughtId", currentTabLink);
			obj.put("lang", lang);
			
			result.add(obj);
			
			currentTabLink = getDownTabLink(sessionId, nodeKey, currentTabLink, lang, true);
			
		}
		
		return result;
		
	}

	//Delete a node tab link
	@SuppressWarnings("unchecked")
	public static void deleteNodeTabLink(long sessionId, String nodeKey, String tabLinkKey, String lang) throws Exception {
		
		//Get the node
		JSONObject node = Record.getNode(sessionId, nodeKey);
		
		//Get the tab link
		JSONObject bd = Record.getNode(sessionId, tabLinkKey);
		
		if (((JSONObject) node.get("fl")).containsKey(lang)) {
		
			if (((JSONObject) bd.get("ud")).containsKey(lang+" "+nodeKey)) {
			
				if (((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("utl")==null && ((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("dtl")==null) {
					
					//Delete a first tab link without other links
					((JSONObject) ((JSONObject) node.get("fl")).get(lang)).put("ftl", null);
					((JSONObject) ((JSONObject) node.get("fl")).get(lang)).put("ltl", null);
					Record.update(sessionId, nodeKey, node.toJSONString());
					
					if (((JSONObject) bd.get("ud")).size()==1) Record.remove(sessionId, tabLinkKey);
					else {
						((JSONObject) bd.get("ud")).remove(lang+" "+nodeKey);
						Record.update(sessionId, tabLinkKey, bd.toJSONString());
					}
					
				} else if (((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("utl")==null) {
					
					//Delete the first tab link
					
					//Update the node
					String downTabLink = ((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("dtl")+"";
					((JSONObject) ((JSONObject) node.get("fl")).get(lang)).put("ftl", downTabLink);
					Record.update(sessionId, nodeKey, node.toJSONString());
					
					//Update the down tab link
					JSONObject downbd = Record.getNode(sessionId, downTabLink);
					
					((JSONObject) ((JSONObject) downbd.get("ud")).get(lang+" "+nodeKey)).put("utl", null);
					Record.update(sessionId, downTabLink, downbd.toJSONString());
					
					//Remove the tab link
					if (((JSONObject) bd.get("ud")).size()==1) Record.remove(sessionId, tabLinkKey);
					else {
						((JSONObject) bd.get("ud")).remove(lang+" "+nodeKey);
						Record.update(sessionId, tabLinkKey, bd.toJSONString());
					}
					
				} else if (((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("dtl")==null) {
					
					//Delete the last tab link
					
					//Update the node
					String upTabLink = ((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("utl")+"";
					((JSONObject) ((JSONObject) node.get("fl")).get(lang)).put("ltl", upTabLink);
					Record.update(sessionId, nodeKey, node.toJSONString());
					
					//Update the up tab link
					JSONObject upbd = Record.getNode(sessionId, upTabLink);
					
					((JSONObject) ((JSONObject) upbd.get("ud")).get(lang+" "+nodeKey)).put("dtl", null);
					Record.update(sessionId, upTabLink, upbd.toJSONString());
					
					//Remove the tab link
					if (((JSONObject) bd.get("ud")).size()==1) Record.remove(sessionId, tabLinkKey);
					else {
						((JSONObject) bd.get("ud")).remove(lang+" "+nodeKey);
						Record.update(sessionId, tabLinkKey, bd.toJSONString());
					}
					
				} else {
					
					//Delete a middle tab link
					String upTabLink = ((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("utl")+"";
					String downTabLink = ((JSONObject) ((JSONObject) bd.get("ud")).get(lang+" "+nodeKey)).get("dtl")+"";
					
					//Update the up tab link
					JSONObject upbd = Record.getNode(sessionId, upTabLink);
					
					((JSONObject) ((JSONObject) upbd.get("ud")).get(lang+" "+nodeKey)).put("dtl", downTabLink);
					Record.update(sessionId, upTabLink, upbd.toJSONString());
					
					//Update the down tab link
					JSONObject downbd = Record.getNode(sessionId, downTabLink);
					
					((JSONObject) ((JSONObject) downbd.get("ud")).get(lang+" "+nodeKey)).put("utl", upTabLink);
					Record.update(sessionId, downTabLink, downbd.toJSONString());
					
					//Remove the tab link
					if (((JSONObject) bd.get("ud")).size()==1) Record.remove(sessionId, tabLinkKey);
					else {
						((JSONObject) bd.get("ud")).remove(lang+" "+nodeKey);
						Record.update(sessionId, tabLinkKey, bd.toJSONString());
					}
					
				}
				
			} else {
					
				throw new Exception("Sorry, not found "+tabLinkKey+" for the "+lang+" language.");
					
			}
			
		} else {
				
			throw new Exception("Sorry, not found "+nodeKey+" for the "+lang+" language.");
				
		}
		
	}

	//Merge a new node tab link
	@SuppressWarnings("unchecked")
	public static void mergeNodeTabLink(long sessionId, String nodeKey, String tabLinkKey, String lang, String type, boolean withfl, boolean withfllang) throws Exception {
		
		//Get the node
		JSONObject bd = Record.getNode(sessionId, nodeKey);
		
		if (((JSONObject) bd.get("fl")).containsKey(lang)) {
			
			//Check if the tab link already exist
			if (((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).get("ftl")==null) {
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ftl", tabLinkKey);
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ltl", tabLinkKey);
				Record.update(sessionId, nodeKey, bd.toJSONString());
			} else {
				updateSecondTabLink(sessionId, nodeKey, ((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).get("ftl")+"", tabLinkKey, lang);
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ftl", tabLinkKey);
				Record.update(sessionId, nodeKey, bd.toJSONString());
			}
			
		} else {
			
			throw new Exception("Sorry, not found "+type+" "+nodeKey+" for the "+lang+" language.");
			
		}
		
	}

	//Add a new node tab link
	@SuppressWarnings("unchecked")
	public static void addNodeTabLink(long sessionId, String nodeKey, String tabLinkKey, String lang, String type, boolean withfl, boolean withfllang, int perception) throws Exception {
		
		//Get the node
		JSONObject bd = Record.getNode(sessionId, nodeKey);
		
		if (bd==null) {
			throw new Exception("Sorry, the node '"+nodeKey+"' does not exist.");
		}
		
		if (((JSONObject) bd.get("fl")).containsKey(lang)) {
			
			//Check if the tab link already exist
			if (((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).get("ftl")==null) {
				addNewTabLink(sessionId, nodeKey, tabLinkKey, null, lang, withfl, withfllang, perception);
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ftl", tabLinkKey);
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ltl", tabLinkKey);
				bd.put("w", Integer.parseInt(""+bd.get("w"))+1);
				Record.update(sessionId, nodeKey, bd.toJSONString());
			} else {
				addNewTabLink(sessionId, nodeKey, tabLinkKey, ((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).get("ftl")+"", lang, withfl, withfllang, perception);
				updateSecondTabLink(sessionId, nodeKey, ((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).get("ftl")+"", tabLinkKey, lang);
				((JSONObject) ((JSONObject) bd.get("fl")).get(lang)).put("ftl", tabLinkKey);
				bd.put("w", Integer.parseInt(""+bd.get("w"))+1);
				Record.update(sessionId, nodeKey, bd.toJSONString());
			}
			
		} else {
			
			throw new Exception("Sorry, not found "+type+" "+nodeKey+" for the "+lang+" language.");
			
		}
		
	}

	//Get the last tab link
	public static String getLastTabLink(long sessionId, String nodeKey, String lang, boolean withlang) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (withlang) {
		
			if (!((JSONObject) recNode.get("fl")).containsKey(lang)) {
				
				throw new Exception("Sorry, not found "+nodeKey+" for the language '"+lang+"'.");
				
			}
			
			if (((JSONObject) ((JSONObject) recNode.get("fl")).get(lang)).get("ltl")==null) return null;
			else return ((JSONObject) ((JSONObject) recNode.get("fl")).get(lang)).get("ltl")+"";
			
		} else {
			
			if (((JSONObject) recNode.get("fl")).get("ltl")==null) return null;
			else return ((JSONObject) recNode.get("fl")).get("ltl")+"";
			
		}
		
	}

	//Get the first tab link
	public static String getFirstTabLink(long sessionId, String nodeKey, String lang, boolean withlang) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (withlang) {
		
			if (!((JSONObject) recNode.get("fl")).containsKey(lang)) {
				
				throw new Exception("Sorry, not found "+nodeKey+" for the language '"+lang+"'.");
				
			}
			
			if (((JSONObject) ((JSONObject) recNode.get("fl")).get(lang)).get("ftl")==null) return null;
			else return ((JSONObject) ((JSONObject) recNode.get("fl")).get(lang)).get("ftl")+"";
			
		} else {
			
			if ( ((JSONObject) recNode.get("fl")).get("ftl")==null) return null;
			else return ((JSONObject) recNode.get("fl")).get("ftl")+"";
			
		}
		
	}

	//Get the down tab link
	public static String getDownTabLink(long sessionId, String key, String nodeKey, String lang, boolean withlang) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (withlang) {
		
			if (((JSONObject) ((JSONObject) recNode.get("ud")).get(lang+" "+key)).get("dtl")==null) return null;
			else return ((JSONObject) ((JSONObject) recNode.get("ud")).get(lang+" "+key)).get("dtl")+"";
			
		} else {
			
			if (((JSONObject) ((JSONObject) recNode.get("ud")).get(key)).get("dtl")==null) return null;
			else return ((JSONObject) ((JSONObject) recNode.get("ud")).get(key)).get("dtl")+"";
			
		}
		
	}

}