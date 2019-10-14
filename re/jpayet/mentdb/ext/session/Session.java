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

package re.jpayet.mentdb.ext.session;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import re.jpayet.mentdb.core.db.basic.BasicResult;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.UserManager;

//The session
public class Session {
	
	//Properties
	public static ConcurrentHashMap<String, WebSocketThread> allSessions = new ConcurrentHashMap<String, WebSocketThread>();
	public static ConcurrentHashMap<String, MentDBConnector> allConnections = new ConcurrentHashMap<String, MentDBConnector>();
	
	//Send target to me and another
	public static void sendTargetToMeAndAnother(long sessionId, String user, String target, int type, String value) {
		
		try {
			
			String[] allUsers = Misc.JSONArray2StringTab(UserManager.showAllSynchronized(sessionId));
			
			//Initialization
			BasicResult br = new BasicResult(user, "ai", type, value, allUsers, WebSocketThread.target.get(user), UserManager.allUsersWhoTalkingWith(user), 0, "", "0", "fr");
			
			Session.allSessions.get(user).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));

			br = new BasicResult(target, "ai", type, value, allUsers, WebSocketThread.target.get(target), UserManager.allUsersWhoTalkingWith(target), 0, "", "0", "fr");
			
			Session.allSessions.get(target).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
			
		} catch (Exception f) {};
		
	}
	
	//Send message to me
	public static void sendMessageToMe(long sessionId, String user, int type, String value) {
		
		try {
			
			String[] allUsers = Misc.JSONArray2StringTab(UserManager.showAllSynchronized(sessionId));
			
			//Initialization
			BasicResult br = new BasicResult(user, "ai", type, value, allUsers, WebSocketThread.target.get(user), UserManager.allUsersWhoTalkingWith(user), 0, "", "0", "fr");
			
			Session.allSessions.get(user).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));

		} catch (Exception f) {};
		
	}

	//Send a message to other users
	public static void sendMessageToOthers(long sessionId, String user, int type, String value) throws Exception {
		
		String[] allUsers = Misc.JSONArray2StringTab(UserManager.showAllSynchronized(sessionId));
		
		//Parse all connected users
		for (Entry<String, WebSocketThread> e : Session.allSessions.entrySet()) {
			
			try {
				
				//All excepted me
				if (!e.getKey().equals(user)) {
					
					//Initialization
					BasicResult br = new BasicResult(e.getKey(), "ai", type, value, allUsers, WebSocketThread.target.get(e.getKey()), UserManager.allUsersWhoTalkingWith(e.getKey()), 0, "", "0", "fr");
					
					e.getValue().session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
				}
				
			} catch (Exception f) {};
			
		}
		
	}

	//Ai send a message to me
	public static void aiSendMessageToMe(long sessionId, String bot, String user, int type, String value, String strategy) throws Exception {
		
		String[] allUsers = Misc.JSONArray2StringTab(UserManager.showAllSynchronized(sessionId));
		
		//Parse all connected users
		for (Entry<String, WebSocketThread> e : Session.allSessions.entrySet()) {
			
			try {
				
				//Only me
				if (e.getKey().equals(user)) {
					
					//Initialization
					BasicResult br = null;

					String bot_is_male = FileFx.ini("bots"+File.separator+bot+File.separator+"bot.conf", "BOT", "IS_MALE");
					String bot_lang = FileFx.ini("bots"+File.separator+bot+File.separator+"bot.conf", "BOT", "LANG");
					
					if (type==1) br = new BasicResult(e.getKey(), bot, type, value, allUsers, WebSocketThread.target.get(e.getKey()), UserManager.allUsersWhoTalkingWith(e.getKey()), 0, strategy, bot_is_male, bot_lang);
					else br = new BasicResult(e.getKey(), bot, type, value, allUsers, WebSocketThread.target.get(e.getKey()), UserManager.allUsersWhoTalkingWith(e.getKey()), 0, strategy, bot_is_male, bot_lang);
					
					e.getValue().session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
				}
				
			} catch (Exception f) {System.out.println("####err="+f.getMessage());};
			
		}
		
	}

	//MentDB send a message to me
	public static void mentdbSendMessageToMe(long sessionId, String user, int type, String value, long seconds) throws Exception {
		
		String[] allUsers = Misc.JSONArray2StringTab(UserManager.showAllSynchronized(sessionId));
		
		//Parse all connected users
		for (Entry<String, WebSocketThread> e : Session.allSessions.entrySet()) {
			
			try {
				
				//Only me
				if (e.getKey().equals(user)) {
					
					//Initialization
					BasicResult br = new BasicResult(e.getKey(), "mentdb", type, value, allUsers, WebSocketThread.target.get(e.getKey()), UserManager.allUsersWhoTalkingWith(e.getKey()), seconds, "", "0", "fr");
					
					e.getValue().session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
				}
				
			} catch (Exception f) {};
			
		}
		
	}
	
	//Send a message to all target and me
	public static void sendMessageToTargetsAndMe(String bot, String user, int type, String inputText, Vector<Vector<MQLValue>> inputVector) {
		
		//Initialization
		Vector<String> target = WebSocketThread.target.get(user);
		
		//Send to the current user
		try {

			BasicResult br = new BasicResult(user, user, type, inputText, 0);
			if (Session.allSessions.get(user).mqlQueryMode && !inputText.startsWith("refresh ") && !inputText.startsWith("in editor ")) 
				Session.allSessions.get(user).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
			else if (!Session.allSessions.get(user).mqlQueryMode) Session.allSessions.get(user).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
			
		} catch (Exception f) {};
		
		//Send to all target except ai
		for (int i=0;i<target.size();i++) {
				
			if (!target.elementAt(i).equals("ai")) {
				
				try {

					BasicResult br = new BasicResult(target.elementAt(i), user, type, inputText, 0);
					Session.allSessions.get(target.elementAt(i)).session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
				} catch (Exception f) {};
			
			}
			
		}
		
		//Send to ai
		try {
			
			if (target.contains("ai")) {
				
				CommandManager.execute(bot, user, inputText, inputVector);
			
			}
			
		} catch (Exception f) {};
		
	}
	
	//Send a message to all users
	public static void aiSendMessage(int type, String value) {
		
		//Parse all connected users
		for (Entry<String, WebSocketThread> e : Session.allSessions.entrySet()) {
			
			try {
				
				//Initialization
				BasicResult br = new BasicResult("", "ai", type, value, 0);
				
				e.getValue().session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
			} catch (Exception f) {};
			
		}
		
	}
	
	//Close a session
	public static void close(String user) throws IOException {
		
		allSessions.get(user).session.close(0, "exit");
		
	}

}