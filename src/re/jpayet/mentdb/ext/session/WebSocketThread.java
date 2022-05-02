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
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.basic.BasicResult;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.UserManager;

//Web socket management
@WebSocket
public class WebSocketThread extends WebSocketAdapter {
	
	//Static
	public static HashMap<Long, WebSocketThread> allSessions = new HashMap<Long, WebSocketThread>();
	public static HashMap<String, Vector<String>> target = new HashMap<String, Vector<String>>();
	
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
	public boolean mqlQueryMode = true;
	public boolean isGrantedApiMql = false;
	public boolean isGrantedApiAi = false;

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
					
					if (isCmdSession("#login_ws", inputVector.get(0), session, 4) || isCmdSession("#login", inputVector.get(0), session, 4)) {
						
						//Get the user and the password
						bot = inputVector.get(0).get(1).value;
						user = inputVector.get(0).get(2).value;
						password = inputVector.get(0).get(3).value;
						
						if (inputVector.get(0).get(0).value.toLowerCase().equals("#login_ws")) {
							
							this.mqlQueryMode = false;
						
						}
						
					} else {
						
						throw new Exception("Sorry, Invalid first command.");
						
					}
					
					try {
						
						if (UserManager.connect(user, password)) {
							
							mentdb = re.jpayet.mentdb.ext.session.Session.allConnections.get(user);
							
							mentdbIdConnection = mentdb.idConnection;
							
							//Add the new session
							isValidSession = true;
							
							openCloseSession(user, true, isValidSession, this);
							
							//Add default target
							Vector<String> targetUserList = new Vector<String>();
							targetUserList.add("ai");
							target.put(user, targetUserList);
							
							String result = mentdb.execute("group is granted user \""+user.replace("\"", "\\\"")+"\" \"api-mql\";");
							if (result.equals("1")) {
								
								isGrantedApiMql = true;
								
							}
							
							result = mentdb.execute("group is granted user \""+user.replace("\"", "\\\"")+"\" \"api-ai\";");
							if (result.equals("1")) {
								
								isGrantedApiAi = true;
								
							}
							
							//Granted connection
							result = mentdb.execute("user show;");
							JSONParser jsonParser = new JSONParser();
							JSONArray a = (JSONArray) jsonParser.parse(result);
							BasicResult br = null;
							if (this.mqlQueryMode) {
								
								br = new BasicResult(user, bot, 1, 
									"#-------------------------------------------------;\n"+
									" #-  Welcome to MentDB Weak!             v_"+Start.version+"  -;\n"+
									" #-  Generalized Interoperability                 -;\n"+
									" #-                                               -;\n"+
									" #-  https://www.mentdb.org                       -;\n"+
									" #-  © "+Start.copyright+", contact@mentdb.org            -;\n"+
									" #-                                               -;\n"+
								    Misc.rpad(" #-  SID: "+mentdb.idConnection+", User: "+user, " ", "50")+"-;\n"+
								    " #------------------------------------------------;", Misc.JSONArray2StringTab(a), WebSocketThread.target.get(user), UserManager.allUsersWhoTalkingWith(user), 0, "", "0", "fr");
								
							} else {
								
								br = new BasicResult(user, bot, 1, 
										"", Misc.JSONArray2StringTab(a), WebSocketThread.target.get(user), UserManager.allUsersWhoTalkingWith(user), 0, "", "0", "fr");
								
							}
							session.getRemote().sendString(WebSocketThread.encrypt(br.toString()));
							
						} else {
							
							//This clause is never pass
							
						}
						
					} catch (Exception f) {
						
						//Database connection error
						BasicResult br = null;
						if ((f.getMessage()+"").startsWith("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht") || (f.getMessage()+"").startsWith("j12hki95orm35hrm62vni90tkmr33sdy")) br = new BasicResult("", "ai", 0, f.getMessage(), 0);
						else br = new BasicResult("", "ai", 0, "j12hki95orm35hrm62vni90tkmr33sdy9", 0);
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
				
				//Handle the command
				if (!inputText.startsWith("#j8i9m0m5i5t3r2y1p9a6y4e5t") || inputText.indexOf(" ")>-1) {
					
					inputVector = Misc.splitCommand(inputText);
					re.jpayet.mentdb.ext.session.Session.sendMessageToTargetsAndMe(bot, user, 1, inputText, inputVector);
					
				} else {
					
					if (inputText.startsWith("#j8i9m0m5i5t3r2y1p9a6y4e5t%")) {
						
						try {
							
							re.jpayet.mentdb.ext.session.Session.allSessions.get(user).mqlQueryMode=false;
							
						} catch (Exception e) {}
						
						try {
							re.jpayet.mentdb.ext.session.Session.sendMessageToMe(mentdbIdConnection, user, 4, "Refresh.");
						} catch (Exception e) {}
						
					} else if (inputText.startsWith("#j8i9m0m5i5t3r2y1p9a6y4e5t$")) {
						
						try {
							
							re.jpayet.mentdb.ext.session.Session.allSessions.get(user).mqlQueryMode=true;
							
						} catch (Exception e) {}
						
						try {
							re.jpayet.mentdb.ext.session.Session.sendMessageToMe(mentdbIdConnection, user, 4, "Refresh.");
						} catch (Exception e) {}
						
						
					} else {
						
						//Update target
						if (target.get(user).contains(inputText.substring(27))) {
							
							target.get(user).remove(inputText.substring(27));
							
						} else {
							target.get(user).add(inputText.substring(27));
						}
						
						re.jpayet.mentdb.ext.session.Session.sendTargetToMeAndAnother(mentdbIdConnection, user, inputText.substring(27), 4, "Target <"+inputText.substring(27)+"> updated.");
						
					}
					
				}
								
			}

		} catch (Exception e) {
			
			try {
				re.jpayet.mentdb.ext.session.Session.mentdbSendMessageToMe(mentdbIdConnection, user, 0, ""+e.getMessage(), 0);
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
		
		//Clear target list
		target.remove(user);
		
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
