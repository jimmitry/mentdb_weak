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

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Vector;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.app.HttpSessionCollector;
import re.jpayet.mentdb.ext.doc.MQLDocumentation;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.Session;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.tools.Misc;

//Command full access
public class CommandFullAccess1 {

	//Execute the command
	@SuppressWarnings("unchecked")
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value) {
		case "Connected.": 

			return "Connected.";

		case "version": 

			return Start.version;

		case "break":

			throw new Exception("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at");

		case "continue":

			throw new Exception("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at");

		case "exit": case "quit": case "bye":

			return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht1";

		case "mentdb":

			return "#-------------------------------------------------;\n"+
			" #-  Welcome to MentDB Weak!             v_"+Start.version+"  -;\n"+
			" #-  Generalized Interoperability                 -;\n"+
			" #-                                               -;\n"+
			" #-  https://www.mentdb.org                       -;\n"+
			" #-  © "+Start.copyright+", contact@mentdb.org            -;\n"+
			" #-                                               -;\n"+
			Misc.rpad(" #-  SID: "+session.idConnection+", User: "+session.user, " ", "50")+"-;\n"+
			" #-------------------------------------------------;";

		case "true":

			return "1";

		case "false":

			return "0";

		case "null": 

			return null;

		case "name":

			return Start.AI_FIRST_NAME+" "+Start.AI_LAST_NAME;

		case "@chat": 

			try {

				Session.allSessions.get(session.user).mqlQueryMode=false;

				return "CHAT mode activated (do not use directly with the editor, only in your application with the web socket driver).";
			} catch (Exception e) {
				if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
				else throw new Exception("Sorry, an error appears: "+e.getMessage());
			}

		case "@sid": 

			try {
				return ""+Session.allSessions.get(session.user).idConnection;
			} catch (Exception e) {
				if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
				else throw new Exception("Sorry, an error appears: "+e.getMessage());
			}

		case "@cmdid": 

			try {
				return ""+Session.allSessions.get(session.user).cmdId;
			} catch (Exception e) {
				if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
				else throw new Exception("Sorry, an error appears: "+e.getMessage());
			}

		case "@sessions": 

			JSONArray jsonResult = new JSONArray();

			for (Entry<Long, WebSocketThread> e : WebSocketThread.allSessions.entrySet()) {

				if (!e.getValue().user.equals("")) {
					JSONObject sess = new JSONObject();
					sess.put("user", e.getValue().user);
					sess.put("nbExecution", e.getValue().cmdId);
					sess.put("id", e.getValue().idConnection);
					sess.put("life", ((Start.SESSION_TIMEOUT/1000)-(e.getValue().session.getIdleTimeout())/1000)+"/"+Start.SESSION_TIMEOUT/1000+"s");
					sess.put("used", ((Start.SESSION_TIMEOUT/1000)-(e.getValue().session.getIdleTimeout())/1000));
					sess.put("maxUsable", Start.SESSION_TIMEOUT/1000);
					jsonResult.add(sess);
				}

			}

			return JsonFormatter.format(jsonResult.toJSONString());

		case "who": 

			return session.user;

		case "sid": 

			return ""+session.idConnection;

		case "pid": 
			
			return ""+ManagementFactory.getRuntimeMXBean().getName().split("@", -1)[0];

		case "id": 

			return Misc.load("data"+File.separator+".id").replace("\r", "").replace("\n", "");

		case "cmdid": 

			return ""+session.nbExecution;

		case "sessions": 

			jsonResult = new JSONArray();
			int size = SessionThreadAgent.allServerThread.size();
			for (Entry<Long, SessionThreadAgent> e : SessionThreadAgent.allServerThread.entrySet()) {
				
				if (!e.getValue().serverThread.user.equals("")) {
					JSONObject sess = new JSONObject();
					sess.put("user", e.getValue().serverThread.user);
					sess.put("threadSize", size);
					sess.put("clientHostAddress", e.getValue().serverThread.CLIENT_HOST_ADDRESS);
					sess.put("clientHostName", e.getValue().serverThread.CLIENT_HOST_NAME);
					sess.put("clientPort", e.getValue().serverThread.CLIENT_PORT);
					sess.put("scriptCacheSize", e.getValue().serverThread.script_cache.size());
					sess.put("isInterrupted", e.getValue().serverThread.isInterrupted);
					sess.put("currentScriptname", e.getValue().serverThread.current_scriptname);
					if (e.getValue().serverThread.http_session!=null) {
						sess.put("httpSid", e.getValue().serverThread.http_session.getId());
						sess.put("httpCreationTime", DateFx.long_to_ts(""+e.getValue().serverThread.http_session.getCreationTime()));
						sess.put("httpLastAccessedTime", DateFx.long_to_ts(""+e.getValue().serverThread.http_session.getLastAccessedTime()));
						sess.put("httpMaxInactiveInterval", e.getValue().serverThread.http_session.getMaxInactiveInterval());
						sess.put("httpSessionCollector", HttpSessionCollector.sessions.size());
					} else {
						sess.put("httpSid", null);
						sess.put("httpCreationTime", null);
						sess.put("httpLastAccessedTime", null);
						sess.put("httpMaxInactiveInterval", null);
						sess.put("httpSessionCollector", HttpSessionCollector.sessions.size());
					}
					sess.put("nbExecution", e.getValue().serverThread.nbExecution);
					sess.put("life", (System.currentTimeMillis()-e.getValue().serverThread.life)/1000+"/"+Start.SESSION_TIMEOUT/1000+"s");
					sess.put("used", (System.currentTimeMillis()-e.getValue().serverThread.life)/1000);
					sess.put("maxUsable", Start.SESSION_TIMEOUT/1000);
					sess.put("id", e.getValue().serverThread.idConnection);
					sess.put("origin", e.getValue().origin);
					if (session.user.equals("admin")) {
						sess.put("curr_mql", e.getValue().current_mql);
						sess.put("last_mql", e.getValue().last_mql);
						sess.put("last_result", e.getValue().last_result);
						sess.put("cur_fx", e.getValue().current_function);
					}
					jsonResult.add(sess);
				}

			}

			return JsonFormatter.format(jsonResult.toJSONString());

		case "help":

			StringBuilder result = new StringBuilder("");


			for (Entry<String, Vector<MQLDocumentation>> e : MQLDocumentation.functions.entrySet()) {

				result.append(e.getKey().toUpperCase()+"\n");

				for(int i=0;i<e.getValue().size();i++) {


					//Get the current MQLDocumentation object
					MQLDocumentation currentFunction = e.getValue().get(i);

					result.append((currentFunction.webSocket?"@":"")+currentFunction.functionId);

					for(int r=0;r<currentFunction.parameters.size();r++) {

						result.append(" <"+currentFunction.parameters.get(r).name+">");

					}

					result.append(" : "+currentFunction.description+"\n");

					if (i==e.getValue().size()-1) result.append("\n");

				}
			}

			return result.toString();

		default:

			return inputVector.get(0).value;

		}

	}

}