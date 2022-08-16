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
import java.util.Base64;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import re.jpayet.mentdb.core.db.basic.BasicResult;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;

//Web socket management
@WebSocket
public class WebSocketThread extends WebSocketAdapter {
	
	//Static
	public static HashMap<Long, WebSocketThread> allSessions = new HashMap<Long, WebSocketThread>();
	
	//Object
	public Session session ;
	public long idConnection = 0;
	public long mentdbIdConnection = 0;
	public static long nbConnection = 0;
	public static long nbOpenConnection = 0;
	public static long exceededSessions = 0;
	public long cmdId = 0;
	public String user = "", password = "", bot = "";
	public MentDBConnector mentdb = null;
	public boolean isValidSession = false;
	boolean alreadyOpenSession = false;

	//A user cannot open 2 sessions in the same time
	public static boolean openCloseSession(String user, boolean openSession, boolean isValidSession, WebSocketThread session) {
		
		//Initialization
		boolean alreadyOpen = false;
		
		if (openSession) {
			
			//If open the session
			if (re.jpayet.mentdb.ext.session.Session.allSessions.containsKey(user)) {
				
				alreadyOpen = true;
				
			} else {
				
				//Add the user in the list
				re.jpayet.mentdb.ext.session.Session.allSessions.put(user, session);
				
			}
			
		} else {
			
			//If close the session
			//Close the session if the session is valid
			if (isValidSession) {
				
				re.jpayet.mentdb.ext.session.Session.allSessions.remove(user);
			
			}
			
		}
		
		return alreadyOpen;
		
	}
	
	@Override
	public void onWebSocketConnect(Session sess) {
		
		//Increment the number of thread
		nbOpenConnection++;
		
		nbConnection++;
		this.idConnection = nbConnection;
		this.session = sess;

		allSessions.put(this.idConnection, this);
		
		//Check the max session
		try {
			
			if (nbOpenConnection>Start.MAX_SESSION) {
				
				//Increment the number of exceeded sessions
				exceededSessions++;

				//Trace in log file
				Log.trace("Web Socket: Maximum number of sessions exceeded.");
				
				this.session.close(0, "Sorry, maximum number of sessions exceeded.");
				
			}
		} catch (NumberFormatException e1) {
		} catch (Exception e1) {
		}
		
	}
	
	public static String encrypt(String data) {

        try {
        	
	        return Base64.getEncoder().encodeToString(data.getBytes());
	        
        } catch (Exception e) {
        		return "";
        }
        
    }
     
	public static String decrypt(String data) {
    	
        try {
        	
	        return new String(Base64.getDecoder().decode(data));
	        
		} catch (Exception e) {
			return "";
		}
        
    }
	
	//Check if the command is valid or not
	public boolean isCmdSession(String cmd0, Vector<MQLValue> param, org.eclipse.jetty.websocket.api.Session session, int size) throws IOException {

		if (param.size()<1) return false; 
		else if (param.get(0).value.toLowerCase().equals(cmd0)) {
			if (param.size()!=size) {
				BasicResult br = new BasicResult("", "ai", 0, "The number of parameter is not valid ("+size+", found "+param.size()+").", 0);
				session.getRemote().sendString(encrypt(br.toString()));
				return false;
			} else return true;
		} else return false;
		
	}

	@Override
    public void onWebSocketText(String inputText) {
		
		//Only 'LF' for the end of lines and LRTRIM
		inputText = Misc.lrtrim(decrypt(inputText).replace("\r\n", "\n").replace("\r", "\n"));

		//New command
		cmdId++;
		
		if (inputText.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht8")) {
			
			//Is not a valid connection
			BasicResult br = new BasicResult("", "ai", 0, "j12hki95orm35hrm62vni90tkmr33sdy1", 0);
			
			try {
				session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
				
			} catch (Exception e) {
				
			}
			
			session.close(0, "exit");
			
			return;
			
		}
		
		//Try to get streams
		try {
			
			//Initialize the command line object
			Vector<Vector<MQLValue>> inputVector = null;
			
			if (cmdId==1) {
				
				inputVector = Misc.splitCommand(inputText);
				
				try {
					
					if (isCmdSession("#login_ws", inputVector.get(0), session, 4)) {
						
						//Get the user and the password
						bot = inputVector.get(0).get(1).value;
						user = inputVector.get(0).get(2).value;
						password = inputVector.get(0).get(3).value;
						
					} else {
						
						throw new Exception("Sorry, Invalid first command.");
						
					}
					
					try {
						
						if (BotManager.connect_ai(bot, user, password)) {
							
							BasicResult br = new BasicResult(bot, 1, "", 0, "", "0", "fr") {};
							session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
							
						}
						
					} catch (Exception f) {
						
						//Database connection error
						BasicResult br = new BasicResult("", "ai", 0, f.getMessage(), 0);
						session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
						session.close(0, "exit");
						
					}
					
				} catch (Exception e) {
					
					//Is not a valid connection
					BasicResult br = new BasicResult("", "ai", 0, "j12hki95orm35hrm62vni90tkmr33sdy8", 0);
					session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
					session.close(0, "exit");
					
				}
				
			} else {

				inputVector = Misc.splitCommand(inputText);
				re.jpayet.mentdb.ext.session.Session.execute_commandes(this, bot, user, 1, inputText, inputVector);
								
			}

		} catch (Exception e) {
			
			try {
				re.jpayet.mentdb.ext.session.Session.mentdbSendMessageToMe(this, user, 0, ""+e.getMessage(), 0);
			} catch (Exception f) {
				
			}

		}
        
    }
	
	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		
		closeSession();
        
	}
	
	public void closeSession() {
		
		//Decrement the number of thread
		nbOpenConnection--;
		
		//Remove the session
		try {
			
			if (isValidSession && re.jpayet.mentdb.ext.session.Session.allConnections.containsKey(user)) {
				try {re.jpayet.mentdb.ext.session.Session.allConnections.get(user).execute("exit;");} catch (Exception e) {};
				try {re.jpayet.mentdb.ext.session.Session.allConnections.get(user).close();} catch (Exception e) {};
				try {re.jpayet.mentdb.ext.session.Session.allConnections.remove(user);} catch (Exception e) {};
			}
			
		} catch (Exception e) {};
		
		allSessions.remove(this.idConnection);
		
		//Check if the session is already open
		if (!alreadyOpenSession) {
			
			openCloseSession(user, false, isValidSession, this);
		
		}
		
		try {

			SessionThreadAgent.allServerThread.get(mentdbIdConnection).serverThread.in.close();
			
		} catch (Exception e) {};
        
	}

}
