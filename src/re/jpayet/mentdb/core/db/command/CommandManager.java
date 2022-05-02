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

package re.jpayet.mentdb.core.db.command;

import java.util.Date;
import java.util.Vector;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.session.Session;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

//Command
public class CommandManager {

	//Execute the command
	public static void execute(String bot, String user, String inputText, Vector<Vector<MQLValue>> inputVector) throws Exception {
		
		String result = "";
		boolean err = false;
		long seconds = 0;
		
		if (Session.allSessions.get(user).mqlQueryMode) {
			
			if (!Session.allSessions.get(user).isGrantedApiMql) {
				
				Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 0, "Sorry, '"+user+"' is not in 'api-mql' group.", seconds);
				return;
				
			}
			
			try {
				
				//MentDB mode
				Date d1 = new Date();
				result = Session.allConnections.get(user).execute(inputText);
				Date d2 = new Date();
				seconds = (d2.getTime()-d1.getTime())/1000;
				
			} catch (Exception e) {
				
				err = true;
				result = ""+e.getMessage();
				
			}
			
			if (err) Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 0, result, seconds);
			else {
				if (result==null) {
					Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 1, result, seconds);
				} else if (result.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht1")) {
					Session.close(user);
				} else {
					Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 1, result, seconds);
				}
			}
			
		} else {
			
			if (!Session.allSessions.get(user).isGrantedApiAi) {
				
				Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 0, "Sorry, '"+user+"' is not in 'api-ai' group.", seconds);
				return;
				
			}
		
			for(int i=0;i<inputVector.size();i++) {
				
				try {

					Date d1 = new Date();
					result = executeCmd(Session.allSessions.get(user).mentdbIdConnection, bot, user, Misc.vectorToStringMsg(inputVector.get(i)));
					Date d2 = new Date();
					seconds = (d2.getTime()-d1.getTime())/1000;
					
				} catch (Exception e) {
					
					err = true;
					result = ""+e.getMessage();
					
				}
				
				if (result!=null) {
				
					if (err) Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 0, result, seconds);
					else Session.mentdbSendMessageToMe(Session.allSessions.get(user).mentdbIdConnection, user, 1, result, seconds);
					
				}
				
			}
			
		}
		
	}

	//Execute the command
	public static String executeCmd(long sessionId, String bot, String user, String inputText) throws Exception {
		
		//Brain mode
		String response = BotManager.execute(bot, user, inputText).get("response")+"";
		String mql = null;
		if (response.indexOf("|")>-1) {
			mql = response.substring(0, response.indexOf("|"));
			response = response.substring(response.indexOf("|")+1);
		}
		
		if (!StringFx.lrtrim(response).equals("")) {
			
			Session.aiSendMessageToMe(sessionId, bot, user, 1, 
					response, ""
					);
			
		}
		
		if (mql!=null) {
			
			response = Session.allConnections.get(user).execute(mql);
			
			if (response!=null && !StringFx.lrtrim(response).equals("")) {
				Session.aiSendMessageToMe(sessionId, bot, user, 1, 
						response, ""
					);
			}
			
		}
		
		return null;

	}

	//Execute the command
	public static String executeAllCommands(SessionThread session, Vector<Vector<MQLValue>> inputVectorCmds, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		String result = "";
		int line = 0;
		Vector<MQLValue> begin = null;
		
		try {
			
			//Parse all commands
			for(int i=0;i<inputVectorCmds.size();i++) {
				
				if (inputVectorCmds.get(i).size()>0 && !inputVectorCmds.get(i).get(0).value.equals(";")) {

					line = inputVectorCmds.get(i).get(0).line;
					begin = inputVectorCmds.get(i);
					
					if (session.sessionThreadAgent!=null) {
						session.sessionThreadAgent.current_function = Misc.vectorToStringMsg(inputVectorCmds.get(i));
						result = CommandFullAccess.execute(session, inputVectorCmds.get(i), env, parent_pid, current_pid);
					} else {
						result = CommandFullAccess.execute(session, inputVectorCmds.get(i), env, parent_pid, current_pid);
					}
					
				}

			}
			
			return result;

		} catch (Exception e) {
			
			String strBegin = Misc.vectorToStringMsg(begin);
			if (strBegin.length()>80) {
				strBegin = strBegin.substring(0,  80)+"...";
			}
			
			int posNewLine = strBegin.indexOf("\n");
			if (posNewLine>=0) strBegin = strBegin.substring(0,  posNewLine)+"...";
			
			String err = ""+e.getMessage();
			if (err.startsWith("Sorry")) {
				err = "\n\n[[[ "+err+" ]]]";
			}
			
			if (line>0 && !err.startsWith("403-")) throw new Exception("\nline "+line + " >>> " + strBegin+ " " + err);
			else throw new Exception(err);

		}

	}

}
