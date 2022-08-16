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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;

public class ParameterManager {
	
	public static ConcurrentHashMap<String, ParamCache> all_params = new ConcurrentHashMap<String, ParamCache>();
	
	//Create the parameter object
	public static void init() throws Exception {
		
		add("ELEVATION_MAX_%_ACCEPTANCE", "30");
		add("WEB_SERVER_PORT_APP_TIMEOUT_1", "{}");
		add("WEB_SERVER_PORT_APP_TIMEOUT_0", "{}");
		add("WEB_SERVER_INVALIDATE_IF_DISCONNECT", "{}");
		add("SERVER_MODE", "DEV");
		add("SERVER_NAME", "YOUR_SERVER_NAME (Change in parameter)");
		add("PARAMETER_MAX_SIZE_IN_MEM", "5000");
		add("LOG_WRITE_MAX_SIZE_IN_STACK", "5000");
		add("SESSION_SCRIPT_CACHE", "200");
		
	}
	
	public static void reset_params() throws Exception {
		
		all_params = new ConcurrentHashMap<String, ParamCache>();
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add(String key, String val) throws Exception {
		
		synchronized ("P["+key+"]") {
		
			if (Record2.countRows("mql_param", "P["+key+"]")>0) {
				
				throw new Exception("Sorry, the parameter "+key+" already exist.");
				
			}
			
			JSONObject node = new JSONObject();
			node.put("k", key);
			node.put("v", val);
			Record2.add("mql_param","P["+key+"]", node.toJSONString());
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void update(String key, String val) throws Exception {
		
		synchronized ("P["+key+"]") {

			if (Record2.countRows("mql_param", "P["+key+"]")==0) {
	
				throw new Exception("Sorry, the parameter "+key+" does not exist.");
				
			}
			
			JSONObject node = new JSONObject();
			node.put("k", key);
			node.put("v", val);
			Record2.update("P["+key+"]", node.toJSONString());
			
			if (all_params.containsKey(key)) {
				all_params.remove(key);
			}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void merge(String key, String val) throws Exception {
		
		synchronized ("P["+key+"]") {

			if (Record2.countRows("mql_param", "P["+key+"]")==0) {
				
				JSONObject node = new JSONObject();
				node.put("k", key);
				node.put("v", val);
				Record2.add("mql_param","P["+key+"]", node.toJSONString());
				
			} else {
				
				JSONObject node = new JSONObject();
				node.put("k", key);
				node.put("v", val);
				Record2.update("P["+key+"]", node.toJSONString());
				
			}

			if (all_params.containsKey(key)) {
				all_params.remove(key);
			}
			
		}
		
	}
	
	public static String remove(String key) throws Exception {
		
		synchronized ("P["+key+"]") {

			if (Record2.countRows("mql_param", "P["+key+"]")==0) {
	
				throw new Exception("Sorry, the parameter "+key+" does not exist.");
				
			}
			
			String result = (String) Record2.getNode("P["+key+"]").get("v");
			Record2.remove("P["+key+"]");

			if (all_params.containsKey(key)) {
				all_params.remove(key);
			}
			
			return result;
			
		}
		
	}
	
	public static String get_value(String key) throws Exception {
		
		if (all_params.containsKey(key)) {
			
			return all_params.get(key).value;
			
		} else {
			
			JSONObject param = Record2.getNode("P["+key+"]");
			
			if (param==null) {
				
				throw new Exception("Sorry, the parameter "+key+" does not exist.");
				
			}
			
			String result = (String) param.get("v");
			
			int max_size = 5000;
			if (key.equals("PARAMETER_MAX_SIZE_IN_MEM") && !all_params.containsKey("PARAMETER_MAX_SIZE_IN_MEM")) {
				
			} else if (all_params.containsKey("PARAMETER_MAX_SIZE_IN_MEM")) {
				max_size = Integer.parseInt(all_params.get("PARAMETER_MAX_SIZE_IN_MEM").value);
			} else {
				max_size = Integer.parseInt(get_value("PARAMETER_MAX_SIZE_IN_MEM"));
			}
			
			if (result==null || result.length()<=max_size) {
				all_params.put(key, new ParamCache(result));
			}
			
			return result;
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject showAllParams() throws Exception {
		
		JSONObject result = new JSONObject();
		
		ArrayList<JSONObject> list = Record2.getRows("mql_param");
		
		for(JSONObject param : list) {
			
			JSONObject p = new JSONObject();
			p.put("value", param.get("v"));
			String key = (String) param.get("k");
			result.put(key, p);
			
			if (all_params.containsKey(key)) {
				p.put("in_mem", "1");
			} else {
				p.put("in_mem", "0");
			}
			
		}
		
		return result;
		
	}
	
	public static boolean exist(String key) throws Exception {
		
		if (all_params.containsKey(key)) {
			
			return true;
			
		} else {
			
			if (Record2.getNode("P["+key+"]")==null) {
				
				return false;
				
			} else {
				
				return true;
				
			}
			
		}
		
	}
	
	public static synchronized String lock_if_null(String key, String val) throws Exception {
		
		if (get_value(key)==null) {
			
			update(key, val);
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}
	
	//Generate update
	public static String generateUpdate(String key) throws Exception {
		
		//Get the JOB object
		JSONObject p = Record2.getNode("P["+key+"]");
		
		//Generate an error if the parameter does not exist
		if (p==null) {

			throw new Exception("Sorry, the parameter "+key+" does not exist.");

		}
		
		String v = (String) p.get("v");
		
		String result = "";
		
		if (v==null) {
			result = "parameter update \""+key.replace("\"", "\\\"")+"\" null;";
		} else {
			result = "parameter update \""+key.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\";";
		}
		
		return result;
		
	}

	//Generate update
	public static String generateMerge(String key) throws Exception {
		
		//Get the JOB object
		JSONObject p = Record2.getNode("P["+key+"]");
		
		//Generate an error if the parameter does not exist
		if (p==null) {

			throw new Exception("Sorry, the parameter "+key+" does not exist.");

		}

		String v = (String) p.get("v");
		
		String result = "";
		
		if (v==null) {
			result = "parameter merge \""+key.replace("\"", "\\\"")+"\" null;";
		} else {
			result = "parameter merge \""+key.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\";";
		}
		
		return result;
		
	}

}
