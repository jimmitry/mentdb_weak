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

package re.jpayet.mentdb.core.db.basic;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import re.jpayet.mentdb.core.entity.relation.RelationManager;

//The node class to store nodes
public class BasicNode {
	
	//Basic node type
	public static final int SYMBOL_TYPE = 0;
	public static final int WORD_TYPE = 1;
	public static final int RELATION_TYPE = 2;
	public static final int STRATEGY_TYPE = 3;
	
	//Properties
	public JSONObject dataNode = null;
	
	//Constructor
	public BasicNode() {
		
	}
	
	//Constructor
	@SuppressWarnings("unchecked")
	public BasicNode(String key, int type, String lang) {
		
		//Initialization
		dataNode = new JSONObject();
		
		switch (type) {
		case SYMBOL_TYPE:  case STRATEGY_TYPE:  
        	
			//Add fields
			dataNode.put("k", key); //Key
			dataNode.put("w", 0); //Perception
			
			JSONObject langObj = new JSONObject();
			JSONObject obj = new JSONObject();
			obj.put("ftl", null); //First tab link
			obj.put("ltl", null); //Last tab link
			langObj.put(lang, obj);
			
			dataNode.put("fl", langObj); //Lang id
            break;
		                 
		case WORD_TYPE:  
			
			//Add fields
			dataNode.put("k", key); //Key
			dataNode.put("w", 0); //Perception
			
			langObj = new JSONObject();
			obj = new JSONObject();
			obj.put("ftl", null); //First tab link
			obj.put("ltl", null); //Last tab link
			langObj.put(lang, obj);
			
			dataNode.put("fl", langObj); //Lang id
			
            break;
		         
		case RELATION_TYPE: 
        	
			//Add fields
			dataNode.put("k", key); //Key
			dataNode.put("w", 0); //Perception
			dataNode.put("n", 0); //The number of node
			
			obj = new JSONObject();
			obj.put("ftl", null); //First tab link
			obj.put("ltl", null); //Last tab link
			
			JSONObject objType = new JSONObject();
			
			//Parse all relation types (Branchs)
			for(int iType=0;iType<RelationManager.allTypeList.length();iType++) {
				objType.put(RelationManager.allTypeList.charAt(iType)+"", obj);
			}
			
			dataNode.put("fl", objType);
			
            break;
		                 
		default: 

			//Add fields
			dataNode.put("k", key); //Key
			dataNode.put("w", 0); //Perception
			
			langObj = new JSONObject();
			obj = new JSONObject();
			obj.put("ftl", null); //First tab link
			obj.put("ltl", null); //Last tab link
			langObj.put(lang, obj);
			
			dataNode.put("fl", langObj); //Lang id
            break;
            
		}
		
	}
	
	//Load a JSON string
	public void load(String json) {
		
		dataNode = (JSONObject) JSONValue.parse(json);
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return dataNode.toJSONString();
		
	}

}