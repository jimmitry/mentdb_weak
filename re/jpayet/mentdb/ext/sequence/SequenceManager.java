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

package re.jpayet.mentdb.ext.sequence;

import java.math.BigInteger;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;

public class SequenceManager {
	
	//Create the sequence object
	@SuppressWarnings("unchecked")
	public static void init(long sessionId) throws Exception {
		
		JSONObject seq = new JSONObject();
		seq.put("thought", "-1");
		seq.put("relation-L", "-1");
		seq.put("circle", "-1");
		seq.put("pid", "-1");
		
		Record.add(sessionId, "SQ[]", seq.toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static String incr(long sessionId, String key) throws Exception {
		
		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		//Load the node
		BigInteger seq = new BigInteger(""+recNode.get(key), 36);
		BigInteger one = new BigInteger("1");
		result = seq.add(one).toString(36);
		recNode.put(key, result);
		
		Record.update(sessionId, "SQ[]", recNode.toJSONString());
		
		return result;
		
	}
	
	public static String showAllSeqs(long sessionId) throws Exception {
		
		return Record.getNode(sessionId, "SQ[]").toJSONString();
		
	}

	//Generate update
	public static String generateUpdate(long sessionId, String key) throws Exception {
		
		//Generate an error if the sequence does not exist
		if (!exist(sessionId, key)) {

			throw new Exception("Sorry, the sequence "+key+" does not exist.");

		}
		
		JSONObject s = Record.getNode(sessionId, "SQ[]");
		
		String result = "sequence update \""+key+"\" \""+s.get(key)+"\";";
		
		return result;
		
	}
	
	public static String get_current(long sessionId, String key) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the sequence id "+key+" does not exist.");
			
		}
		
		return recNode.get(key)+"";
		
	}
	
	public static boolean exist(long sessionId, String key) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		return recNode.containsKey(key);
		
	}
	
	@SuppressWarnings("unchecked")
	public static String add(long sessionId, String key, String val) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		if (recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the sequence id "+key+" already exist.");
			
		}
		
		recNode.put(key, val);
		
		Record.update(sessionId, "SQ[]", recNode.toJSONString());
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static String update(long sessionId, String key, String val) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the sequence id "+key+" does not exist.");
			
		}
		
		recNode.put(key, val);
		
		Record.update(sessionId, "SQ[]", recNode.toJSONString());
		
		return result;
		
	}
	
	public static String remove(long sessionId, String key) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "SQ[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the sequence id "+key+" does not exist.");
			
		}
		
		recNode.remove(key);
		
		Record.update(sessionId, "SQ[]", recNode.toJSONString());
		
		return result;
		
	}

}
