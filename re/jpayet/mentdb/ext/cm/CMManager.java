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

package re.jpayet.mentdb.ext.cm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.ext.json.JsonManager;

public class CMManager {
	
	//Create the connection object
	public static void init(long sessionId) throws Exception {
		
		JSONObject param = new JSONObject();
		
		Record.add(sessionId, "CM[]", param.toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(long sessionId, String type) throws Exception {
		
	    JSONArray sortedJsonArray = new JSONArray();
	    List<String> jsonValues = new ArrayList<String>();
		
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		for(Object o : recNode.keySet()) {
			
			if (type==null || type.equals("")) jsonValues.add((String) o); 
			else {
				if (((String) ((JSONObject) o).get("type")).equals(type)) {
					jsonValues.add((String) o); 
				}
			}
			
		}
		
	    Collections.sort( jsonValues, new Comparator<String>() {
	        @Override
	        public int compare(String a, String b) {

	            return a.compareTo(b);
	            
	        }
	    });

	    for (int i = 0; i < jsonValues.size(); i++) {
	        sortedJsonArray.add(jsonValues.get(i));
	    }
		
		return sortedJsonArray;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject show_obj(long sessionId, String type) throws Exception {
		
	    JSONObject obj = new JSONObject();
		
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		for(Object o : recNode.keySet()) {
			
			if (type==null || type.equals("")) obj.put((String) o, 0); 
			else {
				if (((String) get(sessionId, (String) o).get("type")).equals(type)) {
					obj.put((String) o, 0); 
				}
			}
			
		}
		
		return obj;
		
	}
	
	@SuppressWarnings("unchecked")
	public static void set(long sessionId, String key, String cntJson) throws Exception {
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		recNode.put(key, (JSONObject) JsonManager.load(cntJson));
		
		Record.update(sessionId, "CM[]", recNode.toJSONString());
		
	}
	
	public static void remove(long sessionId, String key) throws Exception {

		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the connection '"+key+"' does not exist.");
			
		}

		recNode.remove(key);
		
		Record.update(sessionId, "CM[]", recNode.toJSONString());
		
	}
	
	public static JSONObject get(long sessionId, String key) throws Exception {

		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the connection '"+key+"' does not exist.");
			
		}
		
		return (JSONObject) recNode.get(key);
		
	}
	
	public static String generate_update(long sessionId, String key) throws Exception {
		
		String result = "json load \"tmpCm\" (cm get \""+key.replace("\"", "\\\"")+"\");\n";
		
		JSONObject obj = get(sessionId, key);
		String type = (String) obj.get("type");
		
		for(Object o : obj.keySet()) {

			String k = (String) o;
			
			if (!k.equals("properties")) {
				
				String v = (String) obj.get(k);
				
				result += "json uobject \"tmpCm\" / \""+k.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" STR;\n";
			
			}
			
		}
		
		if (type.equals("db")) {
			
			JSONObject props = ((JSONObject) obj.get("properties"));
			
			for(Object o : props.keySet()) {

				String k = (String) o;
				String v = (String) props.get(k);
				
				result += "json uobject \"tmpCm\" /properties \""+k.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" STR;\n";
				
			}
			
		}
		
		return result+"cm set \""+key.replace("\"", "\\\"")+"\" {json doc \"tmpCm\"};";
		
	}
	
	public static String exist(long sessionId, String key) throws Exception {

		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		if (recNode.containsKey(key)) {
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}
	
	public static String exist_type(long sessionId, String key, String type) throws Exception {

		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "CM[]");
		
		if (recNode.containsKey(key)) {
			
			if (((String) get(sessionId, key).get("type")).equals(type)) {
				return "1";
			} else return "0";
			
		} else {
			
			return "0";
			
		}
		
	}

}
