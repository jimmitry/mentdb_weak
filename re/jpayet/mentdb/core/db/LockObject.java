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

package re.jpayet.mentdb.core.db;

import java.util.HashMap;
import java.util.Vector;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.session.SessionThreadAgent;

//Manage locks
public class LockObject {

	public long sessionId = 0;
	public JSONObject oldVal = null;
	
	//Lock object
	public static HashMap<String, LockObject> lock_record_by_key = new HashMap<String, LockObject>();
	
	public LockObject(long sessionId, JSONObject oldVal) {
		
		this.sessionId=sessionId;
		this.oldVal=oldVal;
		
	}
	
	public static void clearLocker(long sessionId) {
		
		SessionThreadAgent sessAgent = SessionThreadAgent.allServerThread.get(sessionId);
		
		if (sessAgent!=null) {
			Vector<String> lock_records = sessAgent.serverThread.lock_records;
			
			for(int i=0;i<lock_records.size();i++) {
				
				lock_record_by_key.remove(lock_records.get(i));
				
			}
			
			SessionThreadAgent.allServerThread.get(sessionId).serverThread.lock_records = new Vector<String>();
		}

	}
	
	public static JSONObject read(String key, long sessionId) {
		
		LockObject lo = lock_record_by_key.get(key);
		
		if (lo!=null) {
			
			if (lo.sessionId==sessionId) {
				
				//It's me who lock the record
				
				return null;
				
			} else {
				
				//It's another who lock the record
				
				return lo.oldVal;
				
			}
			
		} else return null;
		
	}
	
	public static void put(String key, long sessionId, JSONObject oldVal) {
		
		if (!lock_record_by_key.containsKey(key)) {
			
			SessionThreadAgent.allServerThread.get(sessionId).serverThread.lock_records.add(key);
			lock_record_by_key.put(key, new LockObject(sessionId, oldVal));
		
		}
		
	}
	
	public static LockObject generateException(String key, long sessionId) throws Exception {
		
		LockObject lo = lock_record_by_key.get(key);

		if (lo!=null && lo.sessionId!=sessionId) {
			
			throw new Exception("Sorry, the record '"+key+"' was locked by '"+SessionThreadAgent.allServerThread.get(lo.sessionId).serverThread.user+"' (locker sid="+lo.sessionId+" | my sid="+sessionId+".");
			
		}
		
		return lo;
		
	}
	
}
