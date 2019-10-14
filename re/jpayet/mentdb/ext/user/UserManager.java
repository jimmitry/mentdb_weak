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
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record;
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
	public static void init(long sessionId) throws Exception {
		
		JSONObject users = new JSONObject();
		
		Record.add(sessionId, "U[]", users.toJSONString());
		
	}
	
	//disable a user
	public static void disable(long sessionId, String login) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "User");
		
		JSONObject groups = (JSONObject) JsonManager.load(GroupManager.userGetGroup(sessionId, login).toJSONString());
		
		for(Object group : groups.keySet()) {
			
			if (GroupManager.isGrantedUser(sessionId, (String) group, login)) {
				
				GroupManager.ungrantUser(sessionId, (String) group, login);
			
			}
			
		}
		
	}
	
	//Show all users
	public static JSONArray showAllSynchronized(long sessionId) throws Exception {
		
		String json = CommandSyncAccess.execute(sessionId, null, null, null, 3, null, null, null, null, null, null);
		
		return (JSONArray) JsonManager.load(json);
		
	}
	
	//Show all users
	@SuppressWarnings("unchecked")
	public static JSONArray showAll(long sessionId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		JSONObject recNode = Record.getNode(sessionId, "U[]");
		
		//Parse all users
		for(Object o : recNode.keySet()) {
			
			result.add(o);
			
		}
		
		return result;
		
	}
	
	//Create a new user
	@SuppressWarnings("unchecked")
	public static void create(long sessionId, String login, String separator, String password, boolean force) throws Exception {

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
		
		//Get the node
		JSONObject recNode = Record.getNode(sessionId, "U[]");
		
		recNode.put(login, Misc.md5(password));
		
		Record.update(sessionId, "U[]", recNode.toJSONString());
		
		//Get the user node
		JSONObject rec = Record.getNode(sessionId, "U["+login+"]");
		if (rec!=null) {
			Record.remove(sessionId, "U["+login+"]");
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

		if (login.equals("admin")) {
			userDirectAccess.put("sKey", Misc.conf_value("conf"+File.separator+"server.conf", "ADMIN", "SERVER_ENCRYPT_KEY"));
		} else {
			userDirectAccess.put("sKey", StringFx.generate_random_str("24"));
		}
		
		Record.add(sessionId, "U["+login+"]", userDirectAccess.toJSONString());
		
		//Create the user group
		if (!GroupManager.exist(sessionId, login)) {
			
			GroupManager.add(sessionId, login, login);
			
		} else {
			if (!force) {
				
				String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
				
				if (!user.equals("mentdb")) {
					throw new Exception("Sorry, the group '"+login+"' already exist. Use force option to create the user ...");
				}
			}
		}
		
		//Grant the user to his group
		if (!GroupManager.isGrantedUser(sessionId, login, login)) {
			
			GroupManager.grantUser(sessionId, login, login);
		
		}
		
	}
	
	public static String secret_key_force(long sessionId, String user) throws Exception {
		
		return ""+Record.getNode(sessionId, "U["+user+"]").get("sKey");
		
	}
	
	public static String secret_key(long sessionId, String user) throws Exception {
		
		String u = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (user.equals(u) || GroupManager.isGrantedUser(sessionId, "sys", u)) {
			
			return ""+Record.getNode(sessionId, "U["+user+"]").get("sKey");
			
		} else {
		
			throw new Exception("Sorry, you are not is the 'sys' group or the current user.");
			
		}
		
	}
	
	//Check the password of a specific user
	public static boolean checkPassword(long sessionId, String login, String password) {
		
		try {
			
			//Get the user node
			JSONObject bd = Record.getNode(sessionId, "U["+login.toLowerCase()+"]");
			
			if ((bd.get("password")+"").equals(Misc.md5(password))) return true;
			else return false;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	//Update the password of a specific user
	@SuppressWarnings("unchecked")
	public static void updatePassword(long sessionId, String login, String password) throws Exception {

		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "User");
		
		//Check parameters
		if (Misc.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required."); 
		}
		
		if (Misc.lrtrim(password).equals("")) {
			throw new Exception("Sorry, the password is required."); 
		}
		
		login = login.toLowerCase();
		
		//Get the user node
		JSONObject rec = Record.getNode(sessionId, "U["+login+"]");
		if (rec==null) {
			throw new Exception("Sorry, the user "+login+" does not exist."); 
		}
		
		rec.put("password", Misc.md5(password));
		
		Record.update(sessionId, "U["+login+"]", rec.toJSONString());
		
	}
	
	//Check if a user already exist
	public static boolean exist(long sessionId, String login) throws Exception {
		
		if (Record.getNode(sessionId, "U["+login+"]")==null) {
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
