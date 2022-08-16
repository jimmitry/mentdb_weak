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

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.BasicResult;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.client.MentDBConnector;

//The session
public class Session {
	
	//Properties
	public static ConcurrentHashMap<String, WebSocketThread> allSessions = new ConcurrentHashMap<String, WebSocketThread>();
	public static ConcurrentHashMap<String, MentDBConnector> allConnections = new ConcurrentHashMap<String, MentDBConnector>();
	
	//Ai send a message to me
	public static void aiSendMessageToMe(WebSocketThread session, String bot, String user, int type, String value, String strategy) throws Exception {
		
		try {
			
			//Initialization
			BasicResult br = null;
			
			JSONObject o = BotManager.get_bot(bot);
			String bot_is_male = ""+o.get("is_male");
			String bot_lang = ""+o.get("lang");
			
			if (type==1) br = new BasicResult(bot, type, value, 0, strategy, bot_is_male, bot_lang);
			else br = new BasicResult(bot, type, value, 0, strategy, bot_is_male, bot_lang);
			
			session.session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
			
		} catch (Exception f) {
			System.out.println("####err="+f.getMessage());
		};
		
	}

	//MentDB send a message to me
	public static void mentdbSendMessageToMe(WebSocketThread session, String user, int type, String value, long seconds) throws Exception {
		
		try {
			
			//Initialization
			BasicResult br = new BasicResult(user, "mentdb", type, value, seconds, "", "0", "fr");
			
			session.session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
			
		} catch (Exception f) {};
		
	}
	
	//Send a message to all target and me
	public static void execute_commandes(WebSocketThread session, String bot, String user, int type, String inputText, Vector<Vector<MQLValue>> inputVector) throws Exception {
		
		CommandManager.execute(session, bot, user, inputText, inputVector);
		
	}
	
	//Close a session
	public static void close(String user) throws IOException {
		
		allSessions.get(user).session.close(0, "exit");
		
	}

}