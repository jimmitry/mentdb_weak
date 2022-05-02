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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;

public class CMManager {
	
	//Create the connection object
	public static void init() throws Exception {
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static void set(String user, String key, String val) throws Exception {
		
		synchronized ("CM["+key+"]") {
		
			if (Record2.countRows("mql_cm", "CM["+key+"]")>0) {
				
				boolean is_system_user = GroupManager.isGrantedUser("sys", user);
				
				if (!is_system_user) {
					throw new Exception("Sorry, the connection '"+key+"' already exist, only a granted user with 'sys' can overwrite a connection.");
				}
				
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(val);
				obj.put("k", key);
				Record2.update("CM["+key+"]", obj.toJSONString());
				
			} else {
				
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(val);
				obj.put("k", key);
				Record2.add("mql_cm","CM["+key+"]", obj.toJSONString());
				
			}
			
		}
		
	}
	
	public static JSONObject get(String user, String key) throws Exception {
		
		if (Misc.lrtrim(key).equals("MENTDB") && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, the user '"+user+"' is not granted with 'sys'.");
		}

		JSONObject cm = Record2.getNode("CM["+key+"]");
		
		if (cm==null) {
			
			throw new Exception("Sorry, the connection manager "+key+" does not exist.");
			
		}
		
		return cm;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show_scrud(String user) throws Exception {
		
		JSONArray sortedJsonArray = new JSONArray();
	    
	    if (!GroupManager.isGrantedUser("api-mql", user)) {
	    	return sortedJsonArray;
	    }
	    
		boolean is_system_user = GroupManager.isGrantedUser("sys", user);
		
		ArrayList<JSONObject> list = Record2.getRows("mql_cm");
		
		for(JSONObject cm : list) {
			
			if (is_system_user) {
				
				if (((String) cm.get("type")).equals("db")) {
					if (!AtomFx.find("SQLServer,PostgreSQL,H2,MySQL,Derby,Oracle,HSQL", ((String) cm.get("subType")), ",").equals("0")) {
						sortedJsonArray.add((String) cm.get("k"));
					}
				}
				
			} else {
				
				if (!((String) cm.get("k")).equals("MENTDB")) {
					if (((String) cm.get("type")).equals("db")) {
						if (!AtomFx.find("SQLServer,PostgreSQL,H2,MySQL,Derby,Oracle,HSQL", ((String) cm.get("subType")), ",").equals("0")) {
							sortedJsonArray.add((String) cm.get("k"));
						}
					}
				}
				
			}
			
		}
		
		return sortedJsonArray;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(String user, String type) throws Exception {
		
		JSONArray sortedJsonArray = new JSONArray();
	    
	    if (!GroupManager.isGrantedUser("api-mql", user)) {
	    	return sortedJsonArray;
	    }
	    
		boolean is_system_user = GroupManager.isGrantedUser("sys", user);
		
		ArrayList<JSONObject> list = Record2.getRows("mql_cm");
		
		for(JSONObject cm : list) {
			
			if (is_system_user) {
				
				if (type==null || type.equals("")) sortedJsonArray.add((String) cm.get("k"));
				else if (((String) cm.get("type")).equals(type)) {
					sortedJsonArray.add((String) cm.get("k"));
				}
				
			} else {
				
				if (!((String) cm.get("k")).equals("MENTDB")) {
					
					if (type==null || type.equals("")) {
						if (((String) cm.get("type")).equals("mentdb")) {
							if (((String) cm.get("mql_users"))!=null) {
								JSONParser jp = new JSONParser();
								JSONArray mql_users = (JSONArray) jp.parse((String) cm.get("mql_users"));
								boolean b = false;
								for(int i=0;i<mql_users.size();i++) {
									String u = (String) mql_users.get(i);
									if (user.equals(u)) {
										b = true;
										break;
									}
								}
								if (b) {
									sortedJsonArray.add((String) cm.get("k"));
								}
							}
						} else {
							sortedJsonArray.add((String) cm.get("k"));
						}
					} else if (((String) cm.get("type")).equals(type)) {
						if (((String) cm.get("type")).equals("mentdb")) {
							if (((String) cm.get("mql_users"))!=null) {
								JSONParser jp = new JSONParser();
								JSONArray mql_users = (JSONArray) jp.parse((String) cm.get("mql_users"));
								boolean b = false;
								for(int i=0;i<mql_users.size();i++) {
									String u = (String) mql_users.get(i);
									if (user.equals(u)) {
										b = true;
										break;
									}
								}
								if (b) {
									sortedJsonArray.add((String) cm.get("k"));
								}
							}
						} else {
							sortedJsonArray.add((String) cm.get("k"));
						}
					}
					
				}
				
			}
			
		}
		
		return sortedJsonArray;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject show_obj(String type) throws Exception {
		
		JSONObject obj = new JSONObject();
	    
		ArrayList<JSONObject> list = Record2.getRows("mql_cm");
		
		for(JSONObject cm : list) {
			
			if (type==null || type.equals("")) obj.put((String) cm.get("k"), "0");
			else if (((String) cm.get("type")).equals(type)) {
				obj.put((String) cm.get("k"), "0");
			}
			
		}
		
		return obj;
		
	}
	
	public static void remove(String user, String key) throws Exception {
		
		synchronized ("CM["+key+"]") {

			if (Record2.countRows("mql_cm", "CM["+key+"]")==0) {
	
				throw new Exception("Sorry, the connection manager "+key+" does not exist.");
				
			}
			
			if (Misc.lrtrim(key).equals("MENTDB")) {
				
				throw new Exception("Sorry, the connection manager MENTDB cannot be deleted.");
				
			}
			
			if (!GroupManager.isGrantedUser("sys", user)) {
				throw new Exception("Sorry, the user '"+user+"' is not granted with 'sys'.");
			}
			
			Record2.remove("CM["+key+"]");
			
		}
		
	}
	
	public static String generate_update(String user, String key) throws Exception {
		
		String result = "json load \"tmpCm\" \"{}\";\n";
		
		JSONObject obj = get(user, key);
		
		for(Object o : obj.keySet()) {

			String k = (String) o;
			
			if (k.equals("properties")) {
				
				result += "json iobject \"tmpCm\" / \"properties\" \"{}\" OBJ;\n";
				
			} else if (k.equals("target_files")) {
				
				result += "json iobject \"tmpCm\" / \"target_files\" \"[]\" ARRAY;\n";
				
			} else {
				
				String v = (String) obj.get(k);
				
				result += "json iobject \"tmpCm\" / \""+k.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" STR;\n";
			
			}
			
		}
		
		JSONObject props = ((JSONObject) obj.get("properties"));
		
		if (props != null) {
			
			for(Object o : props.keySet()) {
	
				String k = (String) o;
				String v = (String) props.get(k);
				
				result += "json iobject \"tmpCm\" /properties \""+k.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" STR;\n";
				
			}
			
		}

		JSONArray target_files = ((JSONArray) obj.get("target_files"));
		
		if (target_files != null && target_files.size()>0) {
			
			int i = 0;
			for(Object o : target_files) {
	
				JSONObject o2 = (JSONObject) o;

				result += "json iarray \"tmpCm\" /target_files \"{}\" OBJ;\n";
				
				for(Object o22 : o2.keySet()) {
					
					String k = (String) o22;
					String v = (String) o2.get(k);
					
					result += "json iobject \"tmpCm\" /target_files["+i+"] \""+k.replace("\"", "\\\"")+"\" \""+v.replace("\"", "\\\"")+"\" STR;\n";
					
				}
				
				i++;
			}
			
		}
		
		return result+"cm set \""+key.replace("\"", "\\\"")+"\" {json doc \"tmpCm\"};";
		
	}
	
	public static String exist(String key) throws Exception {
		
		if (Record2.getNode("CM["+key+"]")==null) {
			
			return "0";
			
		} else {
			
			return "1";
			
		}
		
	}
	
	public static String exist_type(String key, String type) throws Exception {

		//Get the node
		JSONObject recNode = Record2.getNode("CM["+key+"]");
		
		if (recNode!=null) {
			
			if (((String) recNode.get("type")).equals(type)) {
				return "1";
			} else return "0";
			
		} else {
			
			return "0";
			
		}
		
	}

}
