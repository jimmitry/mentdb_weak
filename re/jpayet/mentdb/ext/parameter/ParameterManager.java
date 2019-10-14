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

package re.jpayet.mentdb.ext.parameter;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;

public class ParameterManager {
	
	public static JSONObject allParameters = null;
	
	//Create the parameter object
	public static void init(long sessionId) throws Exception {
		
		JSONObject param = new JSONObject();
		
		Record.add(sessionId, "P[]", param.toJSONString());

		add(sessionId, "ELEVATION_MAX_%_ACCEPTANCE", "30", "1");
		add(sessionId, "AIML_DEFAULT_RESPONSE", "@Change your AIML_DEFAULT_RESPONSE default response in parameter...", "1");
		add(sessionId, "REST_HEADER_RESPONSE", "{}", "1");
		
	}
	
	//Load the parameter object
	public static void load(long sessionId) throws Exception {
		
		allParameters = Record.getNode(sessionId, "P[]");
		
	}

	//Generate update
	public static String generateUpdate(long sessionId, String param) throws Exception {
		
		//Generate an error if the parameter does not exist
		if (!exist(param)) {

			throw new Exception("Sorry, the parameter "+param+" does not exist.");

		}
		
		//Get the JOB object
		JSONObject p = Record.getNode(sessionId, "P[]");
		
		String v = (String) ((JSONObject) p.get(param)).get("value");
		
		String result = "";
		
		if (v==null) {
			result = "parameter update \""+param.replace("\"", "\\\"")+"\" null;";
		} else {
			result = "parameter update \""+param.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\";";
		}
		
		return result;
		
	}

	//Generate update
	public static String generateMerge(long sessionId, String param) throws Exception {
		
		//Generate an error if the parameter does not exist
		if (!exist(param)) {

			throw new Exception("Sorry, the parameter "+param+" does not exist.");

		}
		
		//Get the JOB object
		JSONObject p = Record.getNode(sessionId, "P[]");
		
		String v = (String) ((JSONObject) p.get(param)).get("value");
		
		String result = "";
		
		if (v==null) {
			result = "parameter merge \""+param.replace("\"", "\\\"")+"\" null "+((""+((JSONObject) p.get(param)).get("locked")).equals("1")?"true":"false")+";";
		} else {
			result = "parameter merge \""+param.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" "+((""+((JSONObject) p.get(param)).get("locked")).equals("1")?"true":"false")+";";
		}
		
		return result;
		
	}
	
	public static JSONObject showAllParams() throws Exception {
		
		return allParameters;
		
	}
	
	public static String get_value(String key) throws Exception {
		
		
		if (!allParameters.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}
		
		return (String) ((JSONObject) allParameters.get(key)).get("value");
		
	}
	
	public static String get_locked(String key) throws Exception {
		
		if (!allParameters.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}
		
		return ((JSONObject) allParameters.get(key)).get("locked")+"";
		
	}
	
	public static boolean exist(String key) throws Exception {
		
		return allParameters.containsKey(key);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add(long sessionId, String key, String val, String locked) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "P[]");
		
		if (recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" already exist.");
			
		}
		
		JSONObject value = new JSONObject();
		value.put("value", val);
		value.put("locked", locked);

		recNode.put(key, value);
		
		Record.update(sessionId, "P[]", recNode.toJSONString());
		
		allParameters = recNode;
		
	}
	
	public static void merge(long sessionId, String key, String val, String locked) throws Exception {
		
		if (ParameterManager.exist(key)) {
			
			ParameterManager.update(sessionId, key, val);
			
		} else {
			
			ParameterManager.add(sessionId, key, val, locked);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void update(long sessionId, String key, String val) throws Exception {

		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "P[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}
		
		if ((((JSONObject) recNode.get(key)).get("locked")+"").equals("1")) {
			
			throw new Exception("Sorry, the parameter "+key+" is locked.");
			
		}

		((JSONObject) recNode.get(key)).put("value", val);
		
		Record.update(sessionId, "P[]", recNode.toJSONString());
		
		allParameters = recNode;
		
	}
	
	public static synchronized String lock_if_null(long sessionId, String key, String val) throws Exception {
		
		String cur_val = get_value(key);
		
		if (cur_val==null) {
			
			update(sessionId, key, val);
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String lock_dml(long sessionId, String key) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "P[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}

		((JSONObject) recNode.get(key)).put("locked", "1");
		
		Record.update(sessionId, "P[]", recNode.toJSONString());
		
		allParameters = recNode;
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static String unlock_dml(long sessionId, String key) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "P[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}

		((JSONObject) recNode.get(key)).put("locked", "0");
		
		Record.update(sessionId, "P[]", recNode.toJSONString());
		
		allParameters = recNode;
		
		return result;
		
	}
	
	public static String remove(long sessionId, String key) throws Exception {

		//Initialization
		String result = "";
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "P[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the parameter "+key+" does not exist.");
			
		}
		
		if ((((JSONObject) recNode.get(key)).get("locked")+"").equals("1")) {
			
			throw new Exception("Sorry, the parameter "+key+" is locked.");
			
		}

		recNode.remove(key);
		
		Record.update(sessionId, "P[]", recNode.toJSONString());
		
		allParameters = recNode;
		
		return result;
		
	}

}
