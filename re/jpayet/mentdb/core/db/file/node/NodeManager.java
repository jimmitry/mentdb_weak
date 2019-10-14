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

package re.jpayet.mentdb.core.db.file.node;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.tools.Misc;

public class NodeManager {
	
	//Check if a node already exist
	public static boolean existNode(long sessionId, String key) throws Exception {
		
		if (Record.getNode(sessionId, key)!=null) return true;
		else return false;
		
	}
	
	public static void remove(long sessionId, String key) throws Exception {
		
		//Try to get the record
		JSONObject record = Record.getNode(sessionId, key);
		
		//Check if the key already exist
		if (record==null) {
			
			throw new Exception("Sorry, the node "+key+" does not exist.");
			
		} else {
			
			Record.remove(sessionId, key);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void insertNode(long sessionId, String key) throws Exception {
		
		//Try to get the record
		JSONObject record = Record.getNode(sessionId, key);
		
		//Check if the key already exist
		if (record==null) {
			
			//Initialization of the node
			JSONObject node = new JSONObject();
			
			//The key
			node.put("key", key);
			
			Record.add(sessionId, key, node.toJSONString());
			
		} else {
			
			throw new Exception("Sorry, the node "+key+" already exist.");
			
		}
		
	}

	//Format the output
	public static String format(String json) {
		
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);
		return gson.toJson(je);
	    
	}
	
	//Count element into a json object
	public static String count(long sessionId, String key,String xPath) throws Exception {
		
		//Initialization
		Object obj = Record.getNode(sessionId, key);
		
		//Initialization
		String result = null;
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = ""+array.size();
						break;
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
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

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Get array, data or object from a main JSON structure
	public static String select(long sessionId, String key,String xPath) throws Exception {
		
		//Initialization
		Object obj = Record.getNode(sessionId, key);

		//Initialization
		String result = null;
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = NodeManager.format(array.toJSONString());
						break;
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						result = NodeManager.format(json.toJSONString());
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
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

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Check if it is a JSON object
	public static String is_obj(long sessionId, String key,String xPath) throws Exception {
		
		//Initialization
		Object obj = Record.getNode(sessionId, key);

		//Initialization
		String result = null;
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = "0";
						break;
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
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

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Check if it is a JSON array
	public static String is_array(long sessionId, String key,String xPath) throws Exception {
		
		//Initialization
		Object obj = Record.getNode(sessionId, key);

		//Initialization
		String result = null;
		xPath = Misc.lrtrim(xPath);


		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						result = "1";
						break;
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
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

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Get all fields from a JSON object
	@SuppressWarnings("unchecked")
	public static String fields(long sessionId, String key,String xPath) throws Exception {
		
		//Initialization
		Object obj = Record.getNode(sessionId, key);

		//Initialization
		JSONArray result = new JSONArray();
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						for (Object entry : json.keySet()) {
							result.add(entry.toString());
					    }
						
						break;
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					if (obj==null) result.add(null);
					else result.add(""+obj);
					
				}
				
			}

			return result.toJSONString();

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Insert an element into a JSON array
	@SuppressWarnings("unchecked")
	public static void ainsert(long sessionId, String key,String xPath, String value, String type) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the type is not valid
		if (type==null || xPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("NUM") && !type.equals("INT") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.add(null);
						else if (type.equals("NUM")) array.add(Double.parseDouble(value));
						else if (type.equals("INT")) array.add(Long.parseLong(value));
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
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Insert an element into a JSON array
	@SuppressWarnings("unchecked")
	public static void ainsert(long sessionId, String key,String xPath, String index, String value, String type) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);
		
		//The node id must be a number
		try {
		
			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);
		
		} catch (Exception e) {
		
			throw new Exception("Sorry, the index must be a number.");
		
		}

		//Generate an error if the type is not valid
		if (type==null || xPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("NUM") && !type.equals("INT") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.add(Integer.parseInt(index), null);
						else if (type.equals("NUM")) array.add(Integer.parseInt(index), Double.parseDouble(value));
						else if (type.equals("INT")) array.add(Integer.parseInt(index), Long.parseLong(value));
						else if (type.equals("BOOL")) {
							if (value.equals("1")) array.add(Integer.parseInt(index), "1");
							else array.add(Integer.parseInt(index), "0");
						} else if (type.equals("BOOL2")) {
							if (value.equals("1")) array.add(Integer.parseInt(index), true);
							else array.add(Integer.parseInt(index), true);
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
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Insert an element into a JSON object
	@SuppressWarnings("unchecked")
	public static void oinsert(long sessionId, String key,String xPath, String fieldName, String value, String type) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the type is not valid
		if (type==null || xPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("NUM") && !type.equals("INT") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";
		
		//Try to count
		try {
			
			int nb = Misc.size(xPath, "/");
			for(int i=2;i<=nb;i++) {

				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					if (i==nb) {
						
						if (value==null) json.put(fieldName, null);
						else if (type.equals("NUM")) json.put(fieldName, Double.parseDouble(value));
						else if (type.equals("INT")) json.put(fieldName, Long.parseLong(value));
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}
			
			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Update an element into a JSON array
	@SuppressWarnings("unchecked")
	public static void aupdate(long sessionId, String key,String xPath, String index, String value, String type) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//The index number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);

		} catch (Exception e) {

			throw new Exception("Sorry, the index must be a number.");

		}

		//Generate an error if the type is not valid
		if (type==null || xPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("NUM") && !type.equals("INT") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						if (value==null) array.set(Integer.parseInt(index), null);
						else if (type.equals("NUM")) array.set(Integer.parseInt(index), Double.parseDouble(value));
						else if (type.equals("INT")) array.set(Integer.parseInt(index), Long.parseLong(value));
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
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Update an element into a JSON object
	@SuppressWarnings("unchecked")
	public static void oupdate(long sessionId, String key,String xPath, String fieldName, String value, String type) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the type is not valid
		if (type==null || xPath.equals("")) {

			throw new Exception("Sorry, the type cannot be null or empty.");

		}

		//Generate an error if the type is not valid
		if (!type.equals("NUM") && !type.equals("INT") && !type.equals("STR") && !type.equals("BOOL") && !type.equals("BOOL2") && !type.equals("ARRAY") && !type.equals("OBJ")) {

			throw new Exception("Sorry, the type is not valid (NUM,INT,STR,BOOL,BOOL2,ARRAY,OBJ).");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						if (value==null) json.put(fieldName, null);
						else if (type.equals("NUM")) json.put(fieldName, Double.parseDouble(value));
						else if (type.equals("INT")) json.put(fieldName, Long.parseLong(value));
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Delete an element from a JSON array
	public static void adelete(long sessionId, String key,String xPath, String index) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//The index number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(index);

		} catch (Exception e) {

			throw new Exception("Sorry, the index must be a number.");

		}

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						array.remove(Integer.parseInt(index));
						break;
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
					}
				    
				} else if (obj instanceof JSONObject) {
					
				    // It's an object
					JSONObject json = (JSONObject) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an array.");
						
					} else {
						
						if (currentKey.indexOf("[")>-1) {
							
							//It is an array
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an array.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

	//Delete an element from a JSON object
	public static void odelete(long sessionId, String key,String xPath, String fieldName) throws Exception {
		
		//Initialization
		Object mainObj = JsonManager.load(Record.getNode(sessionId, key).toJSONString());
		Object obj = mainObj;

		//Initialization
		xPath = Misc.lrtrim(xPath);

		//Generate an error if the xpath is null or empty
		if (xPath==null || xPath.equals("")) {

			throw new Exception("Sorry, the xpath cannot be null or empty.");

		}

		//Generate an error if the field name is null or empty
		if (fieldName==null || fieldName.equals("")) {

			throw new Exception("Sorry, the field name cannot be null or empty.");

		}

		//Generate an error if the xpath does not starts with '/'
		if (!xPath.substring(0, 1).equals("/")) {

			throw new Exception("Sorry, the xpath must starts with '/'.");

		}

		if (!xPath.endsWith("/")) xPath+="/";

		//Try to count
		try {

			int nb = Misc.size(xPath, "/");
			
			for(int i=2;i<=nb;i++) {
				
				String currentKey = Misc.atom(xPath, i, "/");
				
				//Check the type of document
				if (obj instanceof JSONArray) {
					
				    // It's an array
					JSONArray array = (JSONArray) obj;
					
					if (i==nb) {
						
						throw new Exception("Sorry, the target must be an object.");
						
					} else {
						
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\[")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}
						if (Misc.size(Misc.atom(xPath, i-1, "/"), "\\]")!=2) {
							throw new Exception("Sorry, the xpath must use [index] at the end.");
						}

						obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(Misc.atom(xPath, i-1, "/"), 2, "\\["), 1, "\\]")));
						
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
							obj = json.get(Misc.atom(currentKey, 1, "\\["));
							JSONArray array = (JSONArray) obj;
							obj = array.get(Integer.parseInt(Misc.atom(Misc.atom(currentKey, 2, "\\["), 1, "\\]")));
							
						} else {
							
							//It is an object
							obj = json.get(currentKey);
							
						}
						
					}
					
				} else {
					
					throw new Exception("Sorry, the target must be an object.");
					
				}
				
			}

			//Re create the new key
			Record.update(sessionId, key, ((JSONObject) mainObj).toJSONString());

		} catch (Exception e) {

			if (e.getMessage().startsWith("Sorry, ")) throw new Exception(e.getMessage());
			else throw new Exception("Sorry, "+e.getMessage());

		}

	}

}
