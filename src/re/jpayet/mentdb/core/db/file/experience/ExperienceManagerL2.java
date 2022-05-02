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

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.entity.relation.RelationManager;

//The node class to store basic tab link to simulate the experience (without language)
public class ExperienceManagerL2 {
	
	//Properties
	public JSONObject dataNode = null;
	
	//Constructor
	@SuppressWarnings("unchecked")
	public ExperienceManagerL2(String word, String key) {
		
		//Initialization
		dataNode = new JSONObject();
		
		//Add fields
		dataNode.put("k", key); //Key
		
		JSONObject langObj = new JSONObject();
		JSONObject obj = new JSONObject();
		obj.put("utl", null); //Up tab link
		obj.put("dtl", null); //Down tab link
		langObj.put(word, obj);
		
		dataNode.put("ud", langObj); //Lang id
		
		JSONObject objFl = new JSONObject();
		objFl.put("ftl", null); //First tab link
		objFl.put("ltl", null); //Last tab link
		
		JSONObject objType = new JSONObject();
		
		//Parse all relation types (Branchs)
		for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
			objType.put(RelationManager.allTypeList.charAt(iType)+"", objFl);
		}
		
		dataNode.put("fl", objType);
		
	}
	
	//Add a new tab link
	@SuppressWarnings("unchecked")
	public static void addNewTabLink(String key, String tabLinkKey, String dtl, JSONObject defaultFL, long parentPosition) throws Exception {
		
		//Tab link initialization
		ExperienceManagerL2 tl = new ExperienceManagerL2(key, tabLinkKey);
		
		((JSONObject) ((JSONObject) tl.dataNode.get("ud")).get(key)).put("utl", null);
		((JSONObject) ((JSONObject) tl.dataNode.get("ud")).get(key)).put("dtl", dtl);
		
		if (defaultFL!=null) {
			tl.dataNode.put("fl", defaultFL);
		}
		
		if (parentPosition>0) {
			tl.dataNode.put("pp", parentPosition);
		}
		
		Record2.add("record", tabLinkKey, tl.dataNode.toJSONString());
		
	}
	
	//Update the second tab link
	@SuppressWarnings("unchecked")
	public static void updateSecondTabLink(String key, String tabLinkKey, String utl) throws Exception {
		
		//Get the second tab link
		JSONObject obj = Record2.getNode(tabLinkKey);
		
		((JSONObject) ((JSONObject) obj.get("ud")).get(key)).put("utl", utl);
		
		Record2.update(tabLinkKey, obj.toJSONString());
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return dataNode.toJSONString();
		
	}

	//Merge a new node tab link
	@SuppressWarnings("unchecked")
	public static void mergeNodeTabLink(String nodeKey, String tabLinkKey, String type) throws Exception {
		
		//Get the node
		JSONObject obj = Record2.getNode(nodeKey);
		
		//Check if the tab link already exist
		if (((JSONObject) ((JSONObject) obj.get("fl")).get(type)).get("ftl")==null) {
			((JSONObject) ((JSONObject) obj.get("fl")).get(type)).put("ftl", tabLinkKey);
			((JSONObject) ((JSONObject) obj.get("fl")).get(type)).put("ltl", tabLinkKey);
			Record2.update(nodeKey, obj.toJSONString());
		} else {
			updateSecondTabLink(nodeKey, ((JSONObject) ((JSONObject) obj.get("fl")).get(type)).get("ftl")+"", tabLinkKey);
			((JSONObject) ((JSONObject) obj.get("fl")).get(type)).put("ftl", tabLinkKey);
			Record2.update(nodeKey, obj.toJSONString());
		}
		
	}

	//Delete a node tab link
	@SuppressWarnings("unchecked")
	public static void deleteNodeTabLink(String nodeKey, String tabLinkKey, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		//Get the tab link
		JSONObject recTLink = Record2.getNode(tabLinkKey);
		
		if (((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("utl")==null && ((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("dtl")==null) {
			
			//Delete a first tab link without other links
			((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).put("ftl", null);
			((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).put("ltl", null);
			Record2.update(nodeKey, recNode.toJSONString());
			
			if (((JSONObject) recTLink.get("ud")).size()==1) Record2.remove(tabLinkKey);
			else {
				((JSONObject) recTLink.get("ud")).remove(nodeKey);
				Record2.update(tabLinkKey, recTLink.toJSONString());
			}
			
		} else if (((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("utl")==null) {
			
			//Delete the first tab link
			
			//Update the node
			String downTabLink = ((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("dtl")+"";
			((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).put("ftl", downTabLink);
			Record2.update(nodeKey, recNode.toJSONString());
			
			//Update the down tab link
			JSONObject recDownTLink = Record2.getNode(downTabLink);
			((JSONObject) ((JSONObject) recDownTLink.get("ud")).get(nodeKey)).put("utl", null);
			Record2.update(downTabLink, recDownTLink.toJSONString());
			
			//Remove the tab link
			if (((JSONObject) recTLink.get("ud")).size()==1) Record2.remove(tabLinkKey);
			else {
				((JSONObject) recTLink.get("ud")).remove(nodeKey);
				Record2.update(tabLinkKey, recTLink.toJSONString());
			}
			
		} else if (((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("dtl")==null) {
			
			//Delete the last tab link
			
			//Update the node
			String upTabLink = ((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("utl")+"";
			((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).put("ltl", upTabLink);
			Record2.update(nodeKey, recNode.toJSONString());
			
			//Update the up tab link
			JSONObject recUpTLink = Record2.getNode(upTabLink);
			((JSONObject) ((JSONObject) recUpTLink.get("ud")).get(nodeKey)).put("dtl", null);
			Record2.update(upTabLink, recUpTLink.toJSONString());
			
			//Remove the tab link
			if (((JSONObject) recTLink.get("ud")).size()==1) Record2.remove(tabLinkKey);
			else {
				((JSONObject) recTLink.get("ud")).remove(nodeKey);
				Record2.update(tabLinkKey, recTLink.toJSONString());
			}
			
		} else {
			
			//Delete a middle tab link
			String upTabLink = ((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("utl")+"";
			String downTabLink = ((JSONObject) ((JSONObject) recTLink.get("ud")).get(nodeKey)).get("dtl")+"";
			
			//Update the up tab link
			JSONObject recUpTLink = Record2.getNode(upTabLink);
			((JSONObject) ((JSONObject) recUpTLink.get("ud")).get(nodeKey)).put("dtl", downTabLink);
			Record2.update(upTabLink, recUpTLink.toJSONString());
			
			//Update the down tab link
			JSONObject recDownTLink = Record2.getNode(downTabLink);
			((JSONObject) ((JSONObject) recDownTLink.get("ud")).get(nodeKey)).put("utl", upTabLink);
			Record2.update(downTabLink, recDownTLink.toJSONString());
			
			//Remove the tab link
			if (((JSONObject) recTLink.get("ud")).size()==1) Record2.remove(tabLinkKey);
			else {
				((JSONObject) recTLink.get("ud")).remove(nodeKey);
				Record2.update(tabLinkKey, recTLink.toJSONString());
			}
			
		}
		
	}

	//Add a new node tab link
	@SuppressWarnings("unchecked")
	public static void addNodeTabLink(String nodeKey, String tabLinkKey, int parentPosition, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		if (recNode==null) {
			throw new Exception("Sorry, the node '"+nodeKey+"' does not exist.");
		}
		
		if (((JSONObject) recNode.get("fl")).get(type)==null) {
			((JSONObject) recNode.get("fl")).put(type, new JSONObject());
		}
		
		//Check if the tab link already exist
		if (((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")==null) {
			addNewTabLink(nodeKey, tabLinkKey, null, null, parentPosition);
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ftl", tabLinkKey);
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ltl", tabLinkKey);
			Record2.update(nodeKey, recNode.toJSONString());
		} else {
			addNewTabLink(nodeKey, tabLinkKey, ((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")+"", null, parentPosition);
			updateSecondTabLink(nodeKey, ((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")+"", tabLinkKey);
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ftl", tabLinkKey);
			Record2.update(nodeKey, recNode.toJSONString());
		}
		
	}

	//Add a new stimulation node tab link
	@SuppressWarnings("unchecked")
	public static void addStimulationNodeTabLink(String nodeKey, String tabLinkKey, JSONObject stimfl, long parentPosition, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		//Check if the tab link already exist
		if (((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")==null) {
			
			addNewTabLink(nodeKey, tabLinkKey, null, stimfl, parentPosition);
			
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ftl", tabLinkKey);
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ltl", tabLinkKey);
			
			Record2.update(nodeKey, recNode.toJSONString());
		} else {
			
			addNewTabLink(nodeKey, tabLinkKey, ((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")+"", stimfl, parentPosition);
			
			updateSecondTabLink(nodeKey, ((JSONObject)((JSONObject) recNode.get("fl")).get(type)).get("ftl")+"", tabLinkKey);
			
			((JSONObject)((JSONObject) recNode.get("fl")).get(type)).put("ftl", tabLinkKey);
			Record2.update(nodeKey, recNode.toJSONString());
		}

	}

	//Get the last tab link
	public static String getLastTabLink(String nodeKey, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).get("ltl")==null) return null;
		else return ((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).get("ltl")+"";
		
	}

	//Get the first tab link
	public static String getFirstTabLink(String nodeKey, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).get("ftl")==null) return null;
		else return ((JSONObject) ((JSONObject) recNode.get("fl")).get(type)).get("ftl")+"";
		
	}

	//Get the down tab link
	public static String getDownTabLink(String relationTabLink, String nodeKey, String type) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode(nodeKey);
		
		//Generate an error if the node does not exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the node "+nodeKey+" does not exist.");
			
		}
		
		if (((JSONObject) ((JSONObject) recNode.get("ud")).get(relationTabLink)).get("dtl")==null) return null;
		else return ((JSONObject) ((JSONObject) recNode.get("ud")).get(relationTabLink)).get("dtl")+"";
		
	}

}