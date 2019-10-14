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

package re.jpayet.mentdb.ext.json;

import java.util.Map.Entry;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class JsonManager {
	
	public static void parse_obj(EnvManager env, SessionThread session, String docId, String jsonPath, String varKey, String varValue, String mqlAction, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		docId = re.jpayet.mentdb.ext.statement.Statement.eval(session, docId, env, parent_pid, current_pid);
		jsonPath = re.jpayet.mentdb.ext.statement.Statement.eval(session, jsonPath, env, parent_pid, current_pid);
		
		//Generate an error if docId is null or empty
		if (docId==null || docId.equals("")) {

			throw new Exception("Sorry, the document id cannot be null or empty.");

		}

		//Generate an error if JSON path is null or empty
		if (jsonPath==null || jsonPath.equals("")) {

			throw new Exception("Sorry, the JSON path cannot be null or empty.");

		}

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}
		
		JSONObject obj = (JSONObject) JsonManager.load(JsonManager.select(env, docId, jsonPath));

		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(mqlAction);
		
		try {
		
			for (Object o : obj.keySet()) {
				
				//Save the document
				String k = (String) o;
				env.set(varKey, k);
				if (varValue.startsWith("[")) {
					if (obj.get(k)==null) env.set(varValue, null);
					else env.set(varValue, ""+obj.get(k));
				} else {
					Object oo = obj.get(k);
					if (oo instanceof JSONObject) {
						env.jsonObj.put(varValue, (JSONObject) oo);
					} else if (oo instanceof JSONArray) {
						env.jsonObj.put(varValue, (JSONArray) oo);
					} else {
						throw new Exception("Sorry, the target is not an object or an array.");
					}
				}
				
				try {
				
					//Execute action
					re.jpayet.mentdb.ext.statement.Statement.eval(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);
					
				} catch (Exception e) {
					if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw e;
					}
				};
				
			}
			
		}  catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}
	
	public static void parse_array(EnvManager env, SessionThread session, String docId, String jsonPath, String varValue, String mqlAction, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		docId = re.jpayet.mentdb.ext.statement.Statement.eval(session, docId, env, parent_pid, current_pid);
		jsonPath = re.jpayet.mentdb.ext.statement.Statement.eval(session, jsonPath, env, parent_pid, current_pid);
		
		//Generate an error if docId is null or empty
		if (docId==null || docId.equals("")) {

			throw new Exception("Sorry, the document id cannot be null or empty.");

		}

		//Generate an error if JSON path is null or empty
		if (jsonPath==null || jsonPath.equals("")) {

			throw new Exception("Sorry, the JSON path cannot be null or empty.");

		}

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}
		
		JSONArray array = (JSONArray) JsonManager.load(JsonManager.select(env, docId, jsonPath));

		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(mqlAction);
		
		try {
		
			for (int i=0;i<array.size();i++) {
				
				//Save the document
				if (varValue.startsWith("[")) {
					if (array.get(i)==null) env.set(varValue, null);
					else env.set(varValue, ""+array.get(i));
				} else {
					Object oo = array.get(i);
					if (oo instanceof JSONObject) {
						env.jsonObj.put(varValue, (JSONObject) oo);
					} else if (oo instanceof JSONArray) {
						env.jsonObj.put(varValue, (JSONArray) oo);
					} else {
						throw new Exception("Sorry, the target is not an object or an array.");
					}
				}
				
				try {
				
					//Execute action
					re.jpayet.mentdb.ext.statement.Statement.eval(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);
					
				} catch (Exception e) {
					if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw e;
					}
				};
				
			}
			
		}  catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}

	public static String exist(EnvManager env, String docId) throws Exception {

		if (env.jsonObj.containsKey(docId)) {

			return "1";

		} else {
			
			return "0";
			
		}

	}

	//Load a json object
	public static Object load(String json) throws ParseException {
		
		//Load the document
		JSONParser jsonParser = new JSONParser();
		return jsonParser.parse(json);
		
	}

	public static String load(EnvManager env, String docId, String jsonStrDoc) throws Exception {

		//Generate an error if docId is null or empty
		if (docId==null || docId.equals("")) {

			throw new Exception("Sorry, the document id cannot be null or empty.");

		}

		//Generate an error if JSON document is null or empty
		if (jsonStrDoc==null || jsonStrDoc.equals("")) {

			throw new Exception("Sorry, the JSON document cannot be null or empty.");

		}

		//Try to load the document
		try {
			
			//Load the document
			Object obj = JsonManager.load(jsonStrDoc);
			
			//Check the type of document
			if (obj instanceof JSONArray) {
				
			    // It's an array
				JSONArray array = (JSONArray) obj;

				//Save the document
				env.jsonObj.put(docId, array);
			    
			} else if (obj instanceof JSONObject) {
				
			    // It's an object
				JSONObject json = (JSONObject) obj;

				//Save the document
				env.jsonObj.put(docId, json);
			    
			}

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

		return "1";

	}

	public static String loadByRef(EnvManager env, String fromDocId, String newDocId, String jPath) throws Exception {

		//Generate an error if the from document id does not exist
		if (!env.jsonObj.containsKey(fromDocId)) {

			throw new Exception("Sorry, the document id '"+fromDocId+"' does not exist.");

		}
		
		//Generate an error if fromDocId is null or empty
		if (fromDocId==null || fromDocId.equals("")) {

			throw new Exception("Sorry, the from document id cannot be null or empty.");

		}

		//Generate an error if newDocId is null or empty
		if (newDocId==null || newDocId.equals("")) {

			throw new Exception("Sorry, the new document id cannot be null or empty.");

		}

		//Generate an error if path is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the path cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the path must starts with '/'.");

		}
		
		String result = "";

		//Try to select
		try {

			Object obj = env.jsonObj.get(fromDocId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = format_Gson(array.toJSONString());
						
						//Save the document
						env.jsonObj.put(newDocId, array);
						
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = format_Gson(json.toJSONString());
						
						//Save the document
						env.jsonObj.put(newDocId, json);
						
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					if (obj==null) result = null;
					else result = ""+obj;
					
					throw new Exception("Sorry, the target ("+result+") is not a JSON object.");
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String load(EnvManager env, String docId, String jsonStrDoc, String jPath) throws Exception {

		//Generate an error if docId is null or empty
		if (docId==null || docId.equals("")) {

			throw new Exception("Sorry, the document id cannot be null or empty.");

		}

		//Generate an error if JSON document is null or empty
		if (jsonStrDoc==null || jsonStrDoc.equals("")) {

			throw new Exception("Sorry, the JSON document cannot be null or empty.");

		}

		//Try to load the document
		try {
			
			//Load the document
			Object obj = JsonManager.load(jsonStrDoc);
			
			//Check the type of document
			if (obj instanceof JSONArray) {
				
			    // It's an array
				JSONArray array = (JSONArray) obj;

				//Save the document
				env.jsonObj.put(docId, array);
			    
			} else if (obj instanceof JSONObject) {
				
			    // It's an object
				JSONObject json = (JSONObject) obj;

				//Save the document
				env.jsonObj.put(docId, json);
			    
			}

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

		return JsonManager.select(env, docId, jPath);

	}

	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {

		JSONArray table = new JSONArray();
		
		for (Object entry : env.jsonObj.keySet()) {
	        table.add(entry.toString());
	    }

		return table;

	}
	
	//Format the output
	public static String format_Gson(String json) {
		
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);
		return gson.toJson(je);
	    
	}

	public static String count(EnvManager env, String docId, String jPath) throws Exception {
		
		//Initialization
		String result = null;
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = ""+array.size();
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = ""+json.size();
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the result does not match with an object or an array.");
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String select(EnvManager env, String docId, String jPath) throws Exception {

		//Initialization
		String result = null;
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to select
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = format_Gson(array.toJSONString());
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = format_Gson(json.toJSONString());
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					if (obj==null) result = null;
					else result = ""+obj;
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String is_obj(EnvManager env, String docId, String jPath) throws Exception {

		//Initialization
		String result = null;
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = "0";
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = "1";
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					result = "0";
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String is_array(EnvManager env, String docId, String jPath) throws Exception {

		//Initialization
		String result = null;
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = "1";
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = "0";
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					result = "0";
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String fields(EnvManager env, String docId, String jPath) throws Exception {

		//Initialization
		String result = null;
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						JSONArray table = new JSONArray();
						
						for (Object entry : json.keySet()) {
					        table.add(entry.toString());
					    }
						
						result = table.toJSONString();
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					if (obj==null) result = null;
					else result = ""+obj;
					
				}
				
			}

			return result;

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String ainsert(EnvManager env, String docId, String jPath, String value, String type) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the type is not valid
		if (type==null || jPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("INT") && !type.equals("NUM") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.add(null);
						else if (type.equals("INT")) array.add(Long.parseLong(value));
						else if (type.equals("NUM")) array.add(Double.parseDouble(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) array.add("1");
							else array.add("0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) array.add(true);
							else array.add(false);
						} else if (type.equals("STR")) array.add(value);
						else if (type.equals("ARRAY")) {
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(value);
							array.add(a);
						} else {
							JSONParser jsonParser = new JSONParser();
							JSONObject o = (JSONObject) jsonParser.parse(value);
							array.add(o);
						}
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String ainsert(EnvManager env, String docId, String jPath, String index, String value, String type) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//The node id must be a number
		try {
		
			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);
		
		} catch (Exception e) {
		
			throw new Exception("Sorry, the index must be a number.");
		
		}

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the type is not valid
		if (type==null || jPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("INT") && !type.equals("NUM") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (INT,NUM,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.add(Integer.parseInt(index), null);
						else if (type.equals("INT")) array.add(Integer.parseInt(index), Long.parseLong(value));
						else if (type.equals("NUM")) array.add(Integer.parseInt(index), Double.parseDouble(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) array.add(Integer.parseInt(index), "1");
							else array.add(Integer.parseInt(index), "0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) array.add(Integer.parseInt(index), true);
							else array.add(Integer.parseInt(index), false);
						} else if (type.equals("STR")) array.add(Integer.parseInt(index), value);
						else if (type.equals("ARRAY")) {
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(value);
							array.add(Integer.parseInt(index), a);
						} else {
							JSONParser jsonParser = new JSONParser();
							JSONObject o = (JSONObject) jsonParser.parse(value);
							array.add(Integer.parseInt(index), o);
						}
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String oinsert(EnvManager env, String docId, String jPath, String fieldName, String value, String type) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the type is not valid
		if (type==null || jPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("INT") && !type.equals("NUM") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (INT,NUM,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						if (value==null) json.put(fieldName, null);
						else if (type.equals("INT")) json.put(fieldName, Long.parseLong(value));
						else if (type.equals("NUM")) json.put(fieldName, Double.parseDouble(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) json.put(fieldName, "1");
							else json.put(fieldName, "0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) json.put(fieldName, true);
							else json.put(fieldName, false);
						} else if (type.equals("STR")) json.put(fieldName, value);
						else if (type.equals("ARRAY")) {
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(value);
							json.put(fieldName, a);
						} else {
							JSONParser jsonParser = new JSONParser();
							JSONObject o = (JSONObject) jsonParser.parse(value);
							json.put(fieldName, o);
						}
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String aupdate(EnvManager env, String docId, String jPath, String index, String value, String type) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//The index number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);

		} catch (Exception e) {

			throw new Exception("Sorry, the index must be a number.");

		}

		//Generate an error if the type is not valid
		if (type==null || jPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("INT") && !type.equals("NUM") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (INT,NUM,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.set(Integer.parseInt(index), null);
						else if (type.equals("INT")) array.set(Integer.parseInt(index), Long.parseLong(value));
						else if (type.equals("NUM")) array.set(Integer.parseInt(index), Double.parseDouble(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) array.set(Integer.parseInt(index), "1");
							else array.set(Integer.parseInt(index), "0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) array.set(Integer.parseInt(index), true);
							else array.set(Integer.parseInt(index), false);
						} else if (type.equals("STR")) array.set(Integer.parseInt(index), value);
						else if (type.equals("ARRAY")) {
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(value);
							array.set(Integer.parseInt(index), a);
						} else {
							JSONParser jsonParser = new JSONParser();
							JSONObject o = (JSONObject) jsonParser.parse(value);
							array.set(Integer.parseInt(index), o);
						}
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static String oupdate(EnvManager env, String docId, String jPath, String fieldName, String value, String type) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the type is not valid
		if (type==null || jPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("INT") && !type.equals("NUM") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (INT,NUM,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						if (value==null) json.put(fieldName, null);
						else if (type.equals("INT")) json.put(fieldName, Long.parseLong(value));
						else if (type.equals("NUM")) json.put(fieldName, Double.parseDouble(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) json.put(fieldName, "1");
							else json.put(fieldName, "0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) json.put(fieldName, true);
							else json.put(fieldName, false);
						} else if (type.equals("STR")) json.put(fieldName, value);
						else if (type.equals("ARRAY")) {
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(value);
							json.put(fieldName, a);
						} else {
							JSONParser jsonParser = new JSONParser();
							JSONObject o = (JSONObject) jsonParser.parse(value);
							json.put(fieldName, o);
						}
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String adelete(EnvManager env, String docId, String jPath, String index) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//The index number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);

		} catch (Exception e) {

			throw new Exception("Sorry, the index must be a number.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						array.remove(Integer.parseInt(index));
						break;
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String odelete(EnvManager env, String docId, String jPath, String fieldName) throws Exception {

		//Initialization
		jPath = StringFx.lrtrim(jPath);

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Generate an error if the jPath is null or empty
		if (jPath==null || jPath.equals("")) {

			throw new Exception("Sorry, the jPath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the jPath does not starts with '/'
		if (!jPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the jPath must starts with '/'.");

		}
		
		if (jPath.equals("/")) {
			
			jPath = "";
			
		}

		//Try to count
		try {

			Object obj = env.jsonObj.get(docId);
			
			int nb = Integer.parseInt(AtomFx.size(jPath, "/"));
			
			for(int i=1;i<=nb;i++) {
				
				String currentKey = AtomFx.get(jPath, ""+(i+1), "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (!AtomFx.size(currentKey, "[").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}
						if (!AtomFx.size(currentKey, "]").equals("2")) {
							throw new Exception("Sorry, the jPath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						json.remove(fieldName);
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(AtomFx.get(currentKey, "1", "["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(AtomFx.get(AtomFx.get(currentKey, "2", "["), "1", "]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}

			return "1";

		} catch (Exception e) {

			if ((""+e.getMessage()).startsWith("Sorry")) throw new Exception(""+e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String doc(EnvManager env, String docId) throws Exception {
		
		//Initialization
		String result = null;

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Try to get the document
		try {

			Object obj = env.jsonObj.get(docId);
			
			//Check the type of document
			if (obj instanceof JSONArray) {
				
			    // It's an array
				JSONArray array = (JSONArray) obj;

				//Save the document
				result = array.toJSONString();
			    
			} else if (obj instanceof JSONObject) {
				
			    // It's an object
				JSONObject json = (JSONObject) obj;

				//Save the document
				result = json.toJSONString();
				
			}
			
			return format_Gson(result);

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String unload(EnvManager env, String docId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the document id does not exist
		if (!env.jsonObj.containsKey(docId)) {

			throw new Exception("Sorry, the document id '"+docId+"' does not exist.");

		}

		//Unload the document
		try {

			env.jsonObj.remove(docId);

		} catch (Exception e) {

			throw new Exception("Sorry, cannot unload the document.");

		}

		return result;

	}

	public static String unloadall(EnvManager env) throws Exception {

		//Initialization
		int nbUnloaded = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to unload
		for (Entry<String, Object> e : env.jsonObj.entrySet()) {

			allKeysToDelete.add(e.getKey());

		}

		//Parse all keys to unload from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {

			try {

				//Unload the document
				unload(env, allKeysToDelete.get(i));
				nbUnloaded++;

			} catch (Exception e) {

				//Nothing to do

			}

		}

		return ""+nbUnloaded;

	}

}