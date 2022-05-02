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
import java.util.ArrayList;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;

public class SequenceManager {
	
	//Create the sequence object
	public static void init() throws Exception {

		add("thought", "-1");
		add("relation-L", "-1");
		add("circle", "-1");
		add("pid", "-1");
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add(String key, String val) throws Exception {
		
		synchronized ("SQ["+key+"]") {
			
			if (Record2.countRows("mql_sequence", "SQ["+key+"]")>0) {
				
				throw new Exception("Sorry, the sequence "+key+" already exist.");
				
			}
			
			JSONObject node = new JSONObject();
			node.put("k", key);
			node.put("v", val);
			Record2.add("mql_sequence","SQ["+key+"]", node.toJSONString());
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void update(String key, String val) throws Exception {
		
		synchronized ("SQ["+key+"]") {

			if (Record2.countRows("mql_sequence", "SQ["+key+"]")==0) {
	
				throw new Exception("Sorry, the sequence "+key+" does not exist.");
				
			}
			
			JSONObject node = new JSONObject();
			node.put("k", key);
			node.put("v", val);
			Record2.update("SQ["+key+"]", node.toJSONString());
			
		}
		
	}
	
	public static String remove(String key) throws Exception {
		
		synchronized ("SQ["+key+"]") {

			if (Record2.countRows("mql_sequence", "SQ["+key+"]")==0) {
	
				throw new Exception("Sorry, the sequence "+key+" does not exist.");
				
			}
			
			String result = (String) Record2.getNode("SQ["+key+"]").get("v");
			Record2.remove("SQ["+key+"]");
			
			return result;
			
		}
		
	}
	
	public static String get_current(String key) throws Exception {
		
		synchronized ("SQ["+key+"]") {

			JSONObject param = Record2.getNode("SQ["+key+"]");
			
			if (param==null) {
				
				throw new Exception("Sorry, the sequence "+key+" does not exist.");
				
			}
			
			return (String) param.get("v");
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject showAllSeqs() throws Exception {
		
		JSONObject result = new JSONObject();
		
		ArrayList<JSONObject> list = Record2.getRows("mql_sequence");
		
		for(JSONObject param : list) {
			
			result.put((String) param.get("k"), param.get("v"));
			
		}
		
		return result;
		
	}
	
	public static boolean exist(String key) throws Exception {
		
		if (Record2.getNode("SQ["+key+"]")==null) {
			
			return false;
			
		} else {
			
			return true;
			
		}
		
	}
	@SuppressWarnings("unchecked")
	public static synchronized String incr(String key) throws Exception {
		
		synchronized ("SQ["+key+"]") {
		
			//Initialization
			String result = "";
			
			String cur_val = get_current(key);
			
			//Load the node
			BigInteger seq = new BigInteger(""+cur_val, 36);
			BigInteger one = new BigInteger("1");
			result = seq.add(one).toString(36);
			
			JSONObject node = new JSONObject();
			node.put("k", key);
			node.put("v", result);
			Record2.update("SQ["+key+"]", node.toJSONString());
			
			return result;
			
		}
		
	}
	
	//Generate update
	public static String generateUpdate(String key) throws Exception {
		
		//Get the JOB object
		JSONObject p = Record2.getNode("SQ["+key+"]");
		
		//Generate an error if the parameter does not exist
		if (p==null) {

			throw new Exception("Sorry, the sequence "+key+" does not exist.");

		}
		
		return "sequence update \""+key+"\" \""+p.get("v")+"\";";
		
	}

}
