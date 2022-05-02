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

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

//The node class to store basic index collision
public class BasicIndex {
	
	//Properties
	public JSONArray collisionNode = null;
	
	//Constructor
	public BasicIndex() {
		
		//Initialization
		collisionNode = new JSONArray();
		
	}
	
	//Add collision
	@SuppressWarnings("unchecked")
	public void addCollision(String key, int fileId, long position) {
		
		//Get the collision object
		
		//Create the collision
		JSONArray collision = new JSONArray();
		collision.add(key);
		collision.add(fileId);
		collision.add(position);
		
		collisionNode.add(collision);
		
	}
	
	//Add collision
	public void deleteCollision(String key) {
		
		//Initialization
		int indexToDelete = -1;
		
		//Parse all values
		for(int i=0;i<collisionNode.size();i++) {
			
			//Check if delete
			if (((JSONArray) collisionNode.get(i)).get(0).equals(key)) {
				
				//Mark the index to deleted
				indexToDelete = i;
				
			}
			
		}
		
		//Delete if found
		if (indexToDelete!=-1) {
			
			collisionNode.remove(indexToDelete);
			
		}
		
	}
	
	//Count collision
	public int countCollision() {
		
		return collisionNode.size();
		
	}
	
	//Get a collision key by index
	public String getCollisionKey(int indexCollision) {
		
		return ""+((JSONArray) collisionNode.get(indexCollision)).get(0);
		
	}
	
	//Get a collision file id by index
	public int getCollisionFileId(int indexCollision) {
		
		return Integer.parseInt(""+((JSONArray) collisionNode.get(indexCollision)).get(1));
		
	}
	
	//Get a collision position by index
	public long getCollisionPosition(int indexCollision) {
		
		return Long.parseLong(""+((JSONArray) collisionNode.get(indexCollision)).get(2));
		
	}
	
	//Get a collision file id
	public int getCollisionFileId(String key) {
		
		//Initialization
		int result = -1;
		
		//Parse all values
		for(int i=0;i<collisionNode.size();i++) {
			
			//Check if exist
			if (((JSONArray) collisionNode.get(i)).get(0).equals(key)) {
				
				result = Integer.parseInt(""+((JSONArray) collisionNode.get(i)).get(1));
				break;
				
			}
			
		}
		
		return result;
		
	}
	
	//Get a collision position
	public long getCollisionPosition(String key) {
		
		//Initialization
		long result = -1;
		
		//Parse all values
		for(int i=0;i<collisionNode.size();i++) {
			
			//Check if exist
			if (((JSONArray) collisionNode.get(i)).get(0).equals(key)) {

				result = (long) ((JSONArray) collisionNode.get(i)).get(2);
				break;
				
			}
			
		}
		
		return result;
		
	}
	
	//Load a JSON string
	public void load(String json) {

		collisionNode = (JSONArray) JSONValue.parse(json);
		
	}
	
	/**
	 * @name toString
	 * @description Get the JSON string
	 */
	public String toString() {
		
		return collisionNode.toJSONString();
		
	}

}