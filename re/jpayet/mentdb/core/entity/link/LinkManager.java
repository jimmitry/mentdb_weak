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

package re.jpayet.mentdb.core.entity.link;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.BasicData;

public class LinkManager {
	
	//Create a new link
	@SuppressWarnings("unchecked")
	public static String create_thought(long sessionId, String lang, String key, String value) throws Exception {
		
		key = key.replace(" ", "_");
		
		//Generate an error if the link already exist
		if (exist_thought(sessionId, lang, key)) {
			
			throw new Exception("Sorry, the link @[th_"+lang+"_"+key+"] already exist.");
			
		}
		
		JSONObject th = Record.getNode(sessionId, value);
		
		//Generate an error if the thought does not exist
		if (th==null) {
			
			throw new Exception("Sorry, the thought '"+value+"' does not exist.");
			
		}
		
		//Generate an error if a link already exist into the node
		if (!((String) th.get("lnk")).equals("")) {
			
			throw new Exception("Sorry, the link '"+((String) th.get("lnk"))+"' is already added into the thought.");
			
		}
		
		th.put("lnk", "@[th_"+lang+"_"+key+"]");
		Record.update(sessionId, value, th.toJSONString());
		
		BasicData bd = new BasicData();
		bd.load("{}");
		bd.dataNode.put("k", "@[th_"+lang+"_"+key+"]");
		bd.dataNode.put("v", value);

		Record.add(sessionId, "@[th_"+lang+"_"+key+"]", bd.dataNode.toJSONString());
		
		return "@[th_"+lang+"_"+key+"]";

	}
	
	//Create a new link
	@SuppressWarnings("unchecked")
	public static String create_relation(long sessionId, String lang, String key, String value) throws Exception {
		
		key = key.replace(" ", "_");
		
		//Generate an error if the link already exist
		if (exist_relation(sessionId, lang, key)) {
			
			throw new Exception("Sorry, the link @[r_"+lang+"_"+key+"] already exist.");
			
		}
		
		JSONObject r = Record.getNode(sessionId, value);
		
		//Generate an error if the relation does not exist
		if (r==null) {
			
			throw new Exception("Sorry, the relation '"+value+"' does not exist.");
			
		}
		
		//Generate an error if a link already exist into the node
		if (!((String) r.get("lnk")).equals("")) {
			
			throw new Exception("Sorry, the link '"+((String) r.get("lnk"))+"' is already added into the relation.");
			
		}
		
		r.put("lnk", "@[r_"+lang+"_"+key+"]");
		Record.update(sessionId, value, r.toJSONString());
		
		BasicData bd = new BasicData();
		bd.load("{}");
		bd.dataNode.put("k", "@[r_"+lang+"_"+key+"]");
		bd.dataNode.put("v", value);

		Record.add(sessionId, "@[r_"+lang+"_"+key+"]", bd.dataNode.toJSONString());
		
		return "@[r_"+lang+"_"+key+"]";

	}
	
	//Delete a new link
	public static void delete_thought(long sessionId, String lang, String key) throws Exception {
		
		key = key.replace(" ", "_");
		
		//Generate an error if the link already exist
		if (!exist_thought(sessionId, lang, key)) {
			
			throw new Exception("Sorry, the link @[th_"+lang+"_"+key+"] does not exist.");
			
		}
		
		Record.remove(sessionId, "@[th_"+lang+"_"+key+"]");

	}
	
	//Delete a new link
	public static void delete_relation(long sessionId, String lang, String key) throws Exception {
		
		key = key.replace(" ", "_");
		
		//Generate an error if the link already exist
		if (!exist_relation(sessionId, lang, key)) {
			
			throw new Exception("Sorry, the link @[r_"+lang+"_"+key+"] does not exist.");
			
		}
		
		Record.remove(sessionId, "@[r_"+lang+"_"+key+"]");

	}
	
	//Get a link
	public static String get_value(long sessionId, String key) throws Exception {

		key = key.replace(" ", "_");
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, key);
		
		//Generate an error if the link already exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the link "+key+" does not exist.");
			
		}
		
		return recNode.get("v")+"";

	}
	
	//Get a link
	public static String get_thought(long sessionId, String lang, String key) throws Exception {

		key = key.replace(" ", "_");
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "@[th_"+lang+"_"+key+"]");
		
		//Generate an error if the link already exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the link @[th_"+lang+"_"+key+"] does not exist.");
			
		}
		
		return recNode.get("v")+"";

	}
	
	//Get a link
	public static String get_relation(long sessionId, String lang, String key) throws Exception {

		key = key.replace(" ", "_");
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "@[r_"+lang+"_"+key+"]");
		
		//Generate an error if the link already exist
		if (recNode==null) {
			
			throw new Exception("Sorry, the link @[r_"+lang+"_"+key+"] does not exist.");
			
		}
		
		return recNode.get("v")+"";

	}
	
	//Check if a link already exist
	public static boolean exist_thought(long sessionId, String lang, String key) throws Exception {

		key = key.replace(" ", "_");
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "@[th_"+lang+"_"+key+"]");
		
		if (recNode==null) {
			
			return false;
			
		} else {
			
			return true;
			
		}

	}
	
	//Check if a link already exist
	public static boolean exist_relation(long sessionId, String lang, String key) throws Exception {

		key = key.replace(" ", "_");
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "@[r_"+lang+"_"+key+"]");
		
		if (recNode==null) {
			
			return false;
			
		} else {
			
			return true;
			
		}

	}

}
