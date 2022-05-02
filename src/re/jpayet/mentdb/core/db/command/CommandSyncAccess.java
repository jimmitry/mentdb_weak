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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.UserManager;

public class CommandSyncAccess {

	//Execute the command
	@SuppressWarnings("unchecked")
	public static String execute(long sid, SessionThread thread, HttpServletRequest request, HttpServletResponse response, int actionId, SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, JSONObject option1, String current_pid) throws Exception {
		
		//Stop the execution if the session was interrupted
		if (session!=null && session.isInterrupted && inputVector!=null) {

			throw new Exception("Sorry, the action has been canceled.");
			
		}
		
		switch (actionId) {
		case 7: 
			
			//Close the environment
			try {
				
				ExcelManager.closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.database_unloadall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.collection_unloadall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.client_disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				ExcelxManager.closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				TunnelManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FileFx.reader_closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FileFx.writer_closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FtpManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FtpsManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SftpManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SshManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SQLManager.disconnectAll(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SQLManager.allStatements.remove(sid);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SessionThreadAgent.allServerThread.get(sid).serverThread.in.close();
				
			} catch (Exception e) {
				
			}
			
			return null;
			
		case 8: 
			
			try {
				
				ExcelManager.closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.database_unloadall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.collection_unloadall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				MongoDBManager.client_disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				ExcelxManager.closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				TunnelManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FileFx.reader_closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FileFx.writer_closeall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FtpManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				FtpsManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SftpManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SshManager.disconnectall(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SQLManager.disconnectAll(env);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			try {
				
				SQLManager.allStatements.remove(sid);
				
			} catch (Exception e) {
				//e.printStackTrace();
			}
			
			return null;
			
		case 6: 
			
			String script = parent_pid;
			
			long sessionId = SessionThread.nbConnection.incrementAndGet();
			if (sessionId==0) sessionId = SessionThread.nbConnection.incrementAndGet();
			thread.idConnection = sessionId;
			
			return null;
			
		case 5: 
			
			script = parent_pid;
			
			//Return a message if the script is null or empty
			if (script==null || script.equals("")) {

				throw new Exception("400-Sorry, the script cannot be null or empty.");

			}
			
			String mentalScript = "call \""+script+"\"";
			
			sessionId = SessionThread.nbConnection.incrementAndGet();
			if (sessionId==0) sessionId = SessionThread.nbConnection.incrementAndGet();
			thread.idConnection = sessionId;
			
			JSONObject scriptObj = ScriptManager.get(session, script);
			
			String allArguments = scriptObj.get("vars")+"";
			Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
			JSONArray variables = ScriptManager.param(v.get(0));
			
			//Set all variables
			for(int i=1;i<=variables.size();i++) {
				
				JSONObject oVar = (JSONObject) variables.get(i-1);
				
				String parameterName = Misc.lrtrim(""+oVar.get("name"));
				
				if (!option1.containsKey(parameterName)) {

					throw new Exception("400-Sorry, the parameter '"+parameterName+"' is required.");
					
				}
				
				//Load variables
				if (option1.get(parameterName)==null) thread.env.set(parameterName, null);
				else thread.env.set(parameterName, ""+option1.get(parameterName));

				mentalScript += " \""+parameterName+"\" \""+(""+option1.get(parameterName)).replace("\"", "\\\"")+"\"";
				
			}
			
			JSONObject r = new JSONObject();
			r.put("mentalScript", mentalScript);
			r.put("sessionId", sessionId);
			r.put("delay_condition", (String) scriptObj.get("delay_condition"));
			r.put("delay_type", (String) scriptObj.get("delay_type"));
			r.put("delay_value", (String) scriptObj.get("delay_value"));
			
			return r.toJSONString();
			
		case 4: 
			
			HttpSession http_session = request.getSession();
			
			if (request.getParameter("x-user")!=null && !request.getParameter("x-user").equals("") &&
					request.getParameter("x-password")!=null && !request.getParameter("x-password").equals("")) {
				JSONObject o = (JSONObject) JsonManager.load(ParameterManager.get_value("WEB_SERVER_PORT_APP_TIMEOUT_1"));
				if (o.containsKey(current_pid)) {
					http_session.setMaxInactiveInterval(Integer.parseInt(o.get(current_pid)+""));
				} else {
					http_session.setMaxInactiveInterval(Start.WEB_SERVER_PORT_APP_TIMEOUT);
				}
			} else {
				JSONObject o = (JSONObject) JsonManager.load(ParameterManager.get_value("WEB_SERVER_PORT_APP_TIMEOUT_0"));
				if (o.containsKey(current_pid)) {
					http_session.setMaxInactiveInterval(Integer.parseInt(o.get(current_pid)+""));
				} else {
					http_session.setMaxInactiveInterval(1);
				}
			}
			
			thread.user = "admin";
			sessionId = SessionThread.nbConnection.incrementAndGet();
			if (sessionId==0) sessionId = SessionThread.nbConnection.incrementAndGet();
			thread.idConnection = sessionId;
			
			return "";
			
		case 3: 
			
			return UserManager.showAll().toJSONString();
			
		case 2: 
			
			String user = "";
			
			//Load the authentication
			String token = request.getHeader("x-token");
			if (token==null || token.equals("")) token = request.getParameter("x-token");
			
			if (token==null || token.equals("")) {
				
				//Get user and password
				String x_user = request.getHeader("x-user");
				if (x_user==null || x_user.equals("")) x_user = request.getParameter("x-user");
				String x_password = request.getHeader("x-password");
				if (x_password==null || x_password.equals("")) x_password = request.getParameter("x-password");
				
				//Get a new token
				token = ScriptManager.get_token(x_user, x_password, Start.JWT_TIMEOUT);
				
				user = x_user;
				
			} else {
				
				//Check the token
				user = ScriptManager.check_token_return_claims(token);
				
			}
			
			String url = ((HttpServletRequest)request).getRequestURL().toString();
			
			script = url.substring(url.indexOf("/api/")+5).replace("/", ".");
			if (script==null || script.equals("")) script = request.getParameter("x-script");
			script = script+"."+request.getMethod().toLowerCase();
			
			//Return a message if the script is null or empty
			if (script==null || script.equals("")) {

				throw new Exception("400-Sorry, the script cannot be null or empty.");

			}
			
			mentalScript = "call \""+script+"\"";
			
			thread.user = user;
			sessionId = SessionThread.nbConnection.incrementAndGet();
			if (sessionId==0) sessionId = SessionThread.nbConnection.incrementAndGet();
			thread.idConnection = sessionId;
			thread.env.set("[x-user]", user);
			thread.env.set("[x-token]", token);
			thread.env.set("[x-script]", script);
			thread.env.set("[x-groups]", StringFx.decode_b64(Misc.atom(token, 2, ".")));

			thread.groups = (JSONObject) JsonManager.load(StringFx.decode_b64(Misc.atom(token, 2, ".")));
			
			allArguments = ScriptManager.get(session, script).get("vars")+"";
			v = Misc.splitCommand(allArguments);
			variables = ScriptManager.param(v.get(0));
			
			//Set all variables
			for(int i=1;i<=variables.size();i++) {
				
				JSONObject oVar = (JSONObject) variables.get(i-1);
				
				String parameterName = Misc.lrtrim(""+oVar.get("name"));
				String param = parameterName.substring(1, parameterName.length()-1);
				
				if (!request.getParameterMap().containsKey(param)) {

					throw new Exception("400-Sorry, the parameter '"+param+"' is required.");
					
				}

				mentalScript += " \""+parameterName+"\" \""+request.getParameter(param).replace("\"", "\\\"")+"\"";
				
			}
			
			r = new JSONObject();
			r.put("user", user);
			r.put("mentalScript", mentalScript);
			r.put("token", token);
			r.put("sessionId", sessionId);
			
			return r.toJSONString();
			
		case 1: 
			
			//Initialization
			String scriptName = inputVector.get(0).value;
			
			//Get script object
			JSONObject jscript = ScriptManager.get(session, scriptName);
			
			String vars = (String) jscript.get("vars");
			v = Misc.splitCommand(vars);
			variables = ScriptManager.param(v.get(0));
			
			String mql = (String) jscript.get("mql");
			String activateLog = (String) jscript.get("activateLog");
			String pid = null;
			
			if (activateLog.equals("1")) {
				
				if (current_pid==null) {
					pid = SequenceManager.incr("pid");
				} else pid = current_pid;
				
				MYSQLManager.addLog(scriptName, parent_pid, pid, "[---- Begin script ----]", "OK", null, null);
				
			}
			
			try {
				
				int nbParamDiviseBy2 = (inputVector.size()-1)/2;
				if ((nbParamDiviseBy2*2)!=(inputVector.size()-1)) {

					throw new Exception("Sorry, the script '"+scriptName+"' does not has a valid number of parameters.");

				}
				
				if (variables.size()!=nbParamDiviseBy2) {

					throw new Exception("Sorry, the script '"+scriptName+"' does not has a valid number of parameters.");

				}

				//Set all parameters into the variable environment
				int posVar = 1;
				for(int i=1;i<inputVector.size();i=i+2) {
					
					JSONObject oVar = (JSONObject) variables.get(posVar-1);
					
					//Get the variable and the value
					String varName = inputVector.get(i).value;
					String varRequired = Misc.lrtrim(""+oVar.get("name"));
					String varTypeToCheck = Misc.lrtrim(""+oVar.get("type"));
					String varIsNull = Misc.lrtrim(""+oVar.get("isNull")).toLowerCase();
					String varIsEmpty = Misc.lrtrim(""+oVar.get("isEmpty")).toLowerCase();
					
					if (!varName.equals(varRequired)) {
						throw new Exception("Sorry, the variable '"+varRequired+"' in the script '"+scriptName+"' is required in position "+posVar+" (found '"+varName+"').");
					}
					
					String val = inputVector.get(i+1).value;
					
					if (!varIsNull.equals("1") && val==null) {
						throw new Exception("Sorry, the variable '"+varRequired+"' in the script '"+scriptName+"' cannot not be null.");
					}
					
					if (!varIsEmpty.equals("1") && val.equals("")) {
						throw new Exception("Sorry, the variable '"+varRequired+"' in the script '"+scriptName+"' cannot not be empty.");
					}
					
					env.set(varName, val);
					
					if (!Statement.eval(session, varTypeToCheck, env, parent_pid, current_pid).equals("1")) {
						throw new Exception("Sorry, the variable '"+varRequired+"' in the script '"+scriptName+"' is not valid ("+varTypeToCheck+").");
					}
					
					posVar++;
				}
				
				r = new JSONObject();
				r.put("mql", mql);
				r.put("activateLog", activateLog);
				r.put("scriptName", scriptName);
				if (activateLog.equals("1")) {
					r.put("pid", pid);
				}
				
				return r.toJSONString();
				
			} catch (Exception z) {
				
				if (activateLog.equals("1")) {
					
					MYSQLManager.addLog(scriptName, parent_pid, pid, ""+z.getMessage(), "KO", null, null);
					
				}
				
				throw new Exception(""+z.getMessage());
				
			}
		
		}
		
		return null;
		
	}

}
