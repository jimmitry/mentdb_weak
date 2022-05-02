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

import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

//The node class to store basic data
public class BasicData {
	
	//Properties
	public JSONObject dataNode = null;
	
	//Constructor
	public BasicData() {
		
	}
	
	//Load a JSON string
	public void load(String json) {
		
		dataNode = (JSONObject) JSONValue.parse(json);
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return dataNode.toJSONString();
		
	}
	
	//Get property
	@SuppressWarnings("unchecked")
	public String getProperties() throws Exception {
		
		JSONArray array = new JSONArray();
		
		Set<String> entrySet = dataNode.keySet();
		for(String entry : entrySet){
	        
			array.add(entry);
			
	    }
		
		return array.toJSONString();
		
	}
	
	//Exist property
	public boolean existProperty(String name) {
		
		if (dataNode.containsKey(name)) return true;
		else return false;
		
	}
	
	//Remove property
	public void removeProperty(String name) throws Exception {
		
		//Generate an error if the property does not exist
		if (!existProperty(name)) {
			
			throw new Exception("Sorry, the property '"+name+"' does not exist.");
			
		}
		
		dataNode.remove(name);
		
	}

}