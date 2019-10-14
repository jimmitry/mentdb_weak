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

import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.Session;

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
		try {
			resultNode.put("mqlQueryMode", Session.allSessions.get(userSession).mqlQueryMode);
		} catch (Exception e) {resultNode.put("mqlQueryMode", false);};
		resultNode.put("ai", Start.AI_FIRST_NAME);
		
	}
	
	//Constructor
	@SuppressWarnings("unchecked")
	public BasicResult(String userSession, String user, int type, String value, String[] currentUsers, Vector<String> target, Vector<String> userWhoTalkingWith, long seconds, String strategy, String bot_is_male, String bot_lang) {
		
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
		resultNode.put("api-mql", Session.allSessions.get(userSession).isGrantedApiMql);
		resultNode.put("api-ai", Session.allSessions.get(userSession).isGrantedApiAi);
		try {
			resultNode.put("mqlQueryMode", Session.allSessions.get(userSession).mqlQueryMode);
		} catch (Exception e) {resultNode.put("mqlQueryMode", false);};
		resultNode.put("ai", Start.AI_FIRST_NAME);
		
		JSONArray array = new JSONArray();
		for(int i = 0;i<currentUsers.length;i++) {
			array.add(currentUsers[i]);
		}
		
		resultNode.put("currentUsers", array);
		
		JSONArray t = new JSONArray();
		for(int i = 0;i<target.size();i++) {
			t.add(target.get(i));
		}
		
		resultNode.put("target", t);
		
		JSONArray u = new JSONArray();
		u.add("ai");
		for(int i = 0;i<userWhoTalkingWith.size();i++) {
			u.add(userWhoTalkingWith.get(i));
		}
		
		resultNode.put("userWhoTalkingWith", u);
		
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return resultNode.toJSONString();
		
	}

}