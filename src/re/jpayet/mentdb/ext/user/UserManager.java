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

package re.jpayet.mentdb.ext.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.Session;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.tools.Misc;

//User management
public class UserManager {
	
	//Create the user object
	public static void init() throws Exception {
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray showAll() throws Exception {
		
		JSONArray result = new JSONArray();
		
		ArrayList<JSONObject> list = Record2.getRows("mql_user");
		
		for(JSONObject user : list) {
			
			result.add((String) user.get("login"));
			
		}
		
		return result;
		
	} 
	
	//disable a user
	public static void delete(long sessionId, String login) throws Exception {
		
		synchronized ("U["+login+"]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "sys", "User");
			
			JSONObject groups = GroupManager.userGetGroup(login);
			
			for(Object group : groups.keySet()) {
				
				if (GroupManager.isGrantedUser((String) group, login)) {
					GroupManager.ungrantUser(sessionId, (String) group, login);
				}
				
			}
			
			Record2.remove("U["+login+"]");
			
		}
		
	}
	
	//Show all users
	public static JSONArray showAllSynchronized(long sessionId) throws Exception {
		
		String json = CommandSyncAccess.execute(sessionId, null, null, null, 3, null, null, null, null, null, null);
		
		return (JSONArray) JsonManager.load(json);
		
	}
	
	//Create a new user
	@SuppressWarnings("unchecked")
	public static String create(long sessionId, String login, String password) throws Exception {

		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "User");
		
		//Check parameters
		if (Misc.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required."); 
		}
		
		if (Misc.lrtrim(password).equals("")) {
			throw new Exception("Sorry, the password is required."); 
		}
		
		login = login.toLowerCase();

		if (login==null || login.equals("")) {
			throw new Exception("Sorry, the login is required."); 
		}
		
		synchronized ("U["+login+"]") {

			//Get the user node
			JSONObject rec = Record2.getNode("U["+login+"]");
			if (rec!=null) {
				throw new Exception("Sorry, the login '"+login+"' already exist."); 
			}
			
			JSONObject userDirectAccess = new JSONObject();
			
			userDirectAccess.put("lw", new JSONArray());
			
			password = Misc.md5(password);
			
			//Add the password
			userDirectAccess.put("login", login);
			userDirectAccess.put("password", password);
			
			JSONObject objFl = new JSONObject();
			objFl.put("ftl", null); //First tab link
			objFl.put("ltl", null); //Last tab link
			
			userDirectAccess.put("fl", objFl);
	
			userDirectAccess.put("groups", new JSONObject());
	
			String kryp = "";
			if (login.equals("admin")) {
				kryp = Misc.conf_value("conf"+File.separator+"server.conf", "ADMIN", "SERVER_ENCRYPT_KEY");
				userDirectAccess.put("sKey", kryp);
			} else {
				kryp = StringFx.generate_random_str("24");
				userDirectAccess.put("sKey", kryp);
			}
			
			Record2.add("mql_user", "U["+login+"]", userDirectAccess.toJSONString());
			
			//Create the user group
			if (!GroupManager.exist(login)) {
				
				GroupManager.add(sessionId, login, login);
				
			}
			
			//Grant the user to his group
			if (!GroupManager.isGrantedUser(login, login)) {
				
				GroupManager.grantUser(sessionId, login, login);
			
			}
			
			return kryp;
			
		}
		
	}
	
	public static String secret_key_force(String user) throws Exception {
		
		return ""+Record2.getNode("U["+user+"]").get("sKey");
		
	}
	
	public static String secret_key(long sessionId, String user) throws Exception {
		
		String u = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (user.equals(u) || GroupManager.isGrantedUser("sys", u)) {
			
			return ""+Record2.getNode("U["+user+"]").get("sKey");
			
		} else {
		
			throw new Exception("Sorry, you are not is the 'sys' group or the current user.");
			
		}
		
	}
	
	//Check the password of a specific user
	public static boolean checkPassword(String login, String password) {
		
		try {
			
			//Get the user node
			JSONObject bd = Record2.getNode("U["+login.toLowerCase()+"]");
			
			if ((bd.get("password")+"").equals(Misc.md5(password))) return true;
			else return false;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	//Update the password of a specific user
	@SuppressWarnings("unchecked")
	public static void updatePassword(long sessionId, String login, String password) throws Exception {
		
		synchronized ("U["+login.toLowerCase()+"]") {

			//Check parameters
			if (Misc.lrtrim(login).equals("")) {
				throw new Exception("Sorry, the login is required."); 
			}
			
			if (Misc.lrtrim(password).equals("")) {
				throw new Exception("Sorry, the password is required."); 
			}
			
			login = login.toLowerCase();
			
			//Get the user node
			JSONObject rec = Record2.getNode("U["+login+"]");
			if (rec==null) {
				throw new Exception("Sorry, the user "+login+" does not exist."); 
			}
			
			rec.put("password", Misc.md5(password));
			
			Record2.update("U["+login+"]", rec.toJSONString());
			
		}
		
	}
	
	//Check if a user already exist
	public static boolean exist(String login) throws Exception {
		
		if (Record2.getNode("U["+login+"]")==null) {
			return false;
		} else {
			return true;
		}
		
	}
	
	//Connect a user
	public static boolean connect(String user, String password) throws Exception {
		
		if (Session.allSessions.containsKey(user)) {

			throw new Exception("j12hki95orm35hrm62vni90tkmr33sdy4");
			
		}
		
		//Connect to the database
		MentDBConnector mentdb = new MentDBConnector("127.0.0.1", Start.SERVER_PORT, Start.SESSION_CONNECT_TIMEOUT, Start.SESSION_TIMEOUT, Database.execute_admin_mql(null, "user secret_key \""+user.replace("\"", "\\\"")+"\";"));
		
		if (user.equals("ai")) {
			
			throw new Exception("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht6");
			
		} else if (mentdb.connect(user, password)) {
			
			Session.allConnections.put(user, mentdb);
			return true;

		} else {
			
			throw new Exception(mentdb.serverConnectionStateId);
			
		}
		
	}
	
	//Disconnect a user
	public static void disconnect(String user) {
		
		try {
			
			if (Session.allConnections.containsKey(user)) {
				
				try {Session.allConnections.get(user).close();} catch (Exception e) {};
				try {Session.allConnections.remove(user);} catch (Exception e) {};

			}
		} catch (Exception e) {};
		
	}
	
	//Get all user who talking with a specific user
	public static Vector<String> allUsersWhoTalkingWith(String user) {
		
		//Initialization
		Set<Entry<String, WebSocketThread>> set = Session.allSessions.entrySet();
		Vector<String> result = new Vector<String>();
		
		//Parse all connected users
		for (Entry<String, WebSocketThread> e : set) {
			
			try {
				
				//Get the user
				String username = e.getKey();
				
				if (WebSocketThread.target.get(username).contains(user)) {
					
					result.add(username);
				
				}
			
			} catch (Exception f) {};
			
		}
		
		return result;
		
	}
	
}
