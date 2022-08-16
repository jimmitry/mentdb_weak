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
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.tools.Misc;

//Command
public class CommandManager {

	//Execute the command
	public static void execute(WebSocketThread session, String bot, String user, String inputText, Vector<Vector<MQLValue>> inputVector) throws Exception {
		
		String result = "";
		boolean err = false;
		long seconds = 0;
		
		for(int i=0;i<inputVector.size();i++) {
			
			try {

				Date d1 = new Date();
				
				result = executeCmd(session, bot, user, Misc.vectorToStringMsg(inputVector.get(i)));
				Date d2 = new Date();
				seconds = (d2.getTime()-d1.getTime())/1000;
				
			} catch (Exception e) {
				
				err = true;
				result = ""+e.getMessage();
				
			}
			
			if (result!=null) {
				
				if (err) Session.mentdbSendMessageToMe(session, user, 0, result, seconds);
				else Session.mentdbSendMessageToMe(session, user, 1, result, seconds);
				
			}
			
		}
		
	}

	//Execute the command
	public static String executeCmd(WebSocketThread session, String bot, String user, String inputText) throws Exception {
		
		//Brain mode
		
		String response = (String) BotManager.execute(bot, user, inputText).get("msg");
		
		if (!StringFx.lrtrim(StringFx.lrtrim(response)).equals("")) {
			
			Session.aiSendMessageToMe(session, bot, user, 1, 
					response, ""
					);
			
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
