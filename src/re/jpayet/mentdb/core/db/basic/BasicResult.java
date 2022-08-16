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

import re.jpayet.mentdb.ext.server.Start;

//The basic result JSON object
public class BasicResult {
	
	//Properties
	public JSONObject resultNode = null;
	
	//Constructor
	@SuppressWarnings("unchecked")
	public BasicResult(String userSession, String user, int type, String value, long seconds) {
		
		//Initialization
		resultNode = new JSONObject();
		
		//Add fields
		resultNode.put("user", user);
		resultNode.put("type", type);
		resultNode.put("msg", value);
		resultNode.put("s", seconds);
		resultNode.put("s", seconds);
		resultNode.put("ai", Start.AI_FIRST_NAME);
		
	}
	
	//Constructor
	@SuppressWarnings("unchecked")
	public BasicResult(String userSession, String user, int type, String value, long seconds, String strategy, String bot_is_male, String bot_lang) {
		
		//Initialization
		resultNode = new JSONObject();
		
		//Add fields
		resultNode.put("bot_is_male", bot_is_male);
		resultNode.put("bot_lang", bot_lang);
		resultNode.put("user", user);
		resultNode.put("type", type);
		resultNode.put("msg", value);
		resultNode.put("s", seconds);
		resultNode.put("strategy", strategy);
		resultNode.put("ai", Start.AI_FIRST_NAME);
		
	}
	
	//Constructor
	@SuppressWarnings("unchecked")
	public BasicResult(String user, int type, String value, long seconds, String strategy, String bot_is_male, String bot_lang) {
		
		//Initialization
		resultNode = new JSONObject();
		
		//Add fields
		resultNode.put("bot_is_male", bot_is_male);
		resultNode.put("bot_lang", bot_lang);
		resultNode.put("user", user);
		resultNode.put("type", type);
		resultNode.put("msg", value);
		resultNode.put("s", seconds);
		resultNode.put("strategy", strategy);
		resultNode.put("ai", user);
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return resultNode.toJSONString();
		
	}

}