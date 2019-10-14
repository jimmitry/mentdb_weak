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
import java.io.RandomAccessFile;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.LockObject;
import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.data.Block;
import re.jpayet.mentdb.core.db.file.data.DataFile;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.data.UnusedFile;
import re.jpayet.mentdb.core.db.file.index.IndexFile;
import re.jpayet.mentdb.core.db.file.index.IndexFilePool;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.relation.SearchEngine;
import re.jpayet.mentdb.core.entity.symbol.SymbolManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.rest.REST_DOCManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;

//Command synchronized access
public class CommandSyncAccess {

	//Execute the command
	public static synchronized String execute(long sid, SessionThread thread, HttpServletRequest request, HttpServletResponse response, int actionId, SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, JSONObject option1, String current_pid) throws Exception {
		
		String result = execute_not_auto_commit(sid, thread, request, response, actionId, session, inputVector, env, parent_pid, option1, current_pid);
		
		//Make an auto commit if auto commit was activated
		if (env!=null && env.autoCommit) {
			
			if (result==null || !result.startsWith("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht")) {
				Transaction.commit(session.idConnection);
			}
			
		}
		
		return result;
		
	}

	//Execute the command
	@SuppressWarnings({ "unchecked", "resource" })
	public static String execute_not_auto_commit(long sid, SessionThread thread, HttpServletRequest request, HttpServletResponse response, int actionId, SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, JSONObject option1, String current_pid) throws Exception {
		
		//Stop the execution if the session was interrupted
		if (session!=null && session.isInterrupted && inputVector!=null && !inputVector.get(0).value.equals("rollback")) {

			throw new Exception("Sorry, the action has been canceled.");
			
		}
		
		if (actionId!=0) {
			
			switch (actionId) {
			case 7: 
				
				//Close the environment
				try {
					
					ExcelManager.closeall(env);
					
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
					
					Transaction.rollback(sid);
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
				try {
					
					Transaction.logFiles.get(sid).close();
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
				try {
					
					Misc.deleteFile("data"+File.separator+"transaction"+File.separator+sid);
					
				} catch (Exception e) {
					//e.printStackTrace();
				}
				
				try {
					
					Transaction.logFiles.remove(sid);
					
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
				
				SessionThread.nbConnection++;
				if (SessionThreadAgent.allServerThread.containsKey(SessionThread.nbConnection)) SessionThread.nbConnection++;
				long sessionId = SessionThread.nbConnection;
				
				thread.idConnection = sessionId;

				//Create the data/transaction/user folder if does not exist
				if (!(new File("data"+File.separator+"transaction"+File.separator+sessionId).exists())) {

					(new File("data"+File.separator+"transaction"+File.separator+sessionId)).mkdir();

				}
				
				//Open the log file
				Transaction.logFiles.put(sessionId, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback.log", "rw"));
				
				String allArguments = ScriptManager.get(thread.idConnection, script).get("vars")+"";
				Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
				JSONArray variables = ScriptManager.param(v.get(0));
				
				return null;
				
			case 5: 
				
				script = parent_pid;
				
				//Return a message if the script is null or empty
				if (script==null || script.equals("")) {

					throw new Exception("400-Sorry, the script cannot be null or empty.");

				}
				
				String mentalScript = "call \""+script+"\"";
				
				SessionThread.nbConnection++;
				if (SessionThreadAgent.allServerThread.containsKey(SessionThread.nbConnection)) SessionThread.nbConnection++;
				sessionId = SessionThread.nbConnection;
				thread.idConnection = sessionId;
				
				//Create the data/transaction/user folder if does not exist
				if (!(new File("data"+File.separator+"transaction"+File.separator+sessionId).exists())) {

					(new File("data"+File.separator+"transaction"+File.separator+sessionId)).mkdir();

				}
				
				//Open the log file
				Transaction.logFiles.put(sessionId, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback.log", "rw"));
				
				JSONObject scriptObj = ScriptManager.get(thread.idConnection, script);
				
				allArguments = scriptObj.get("vars")+"";
				v = Misc.splitCommand(allArguments);
				variables = ScriptManager.param(v.get(0));
				
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
				http_session.setMaxInactiveInterval(Start.WEB_SERVER_PORT_APP_TIMEOUT);
				
				thread.user = "admin";
				SessionThread.nbConnection++;
				if (SessionThreadAgent.allServerThread.containsKey(SessionThread.nbConnection)) SessionThread.nbConnection++;
				sessionId = SessionThread.nbConnection;
				thread.idConnection = sessionId;

				//Create the data/transaction/user folder if does not exist
				if (!(new File("data"+File.separator+"transaction"+File.separator+sessionId).exists())) {

					(new File("data"+File.separator+"transaction"+File.separator+sessionId)).mkdir();

				}
				
				//Open the log file
				Transaction.logFiles.put(sessionId, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback.log", "rw"));
				
				return "";
				
			case 3: 
				
				return UserManager.showAll(sid).toJSONString();
				
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
					token = ScriptManager.get_token(thread.idConnection, x_user, x_password, Start.JWT_TIMEOUT);
					
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
				SessionThread.nbConnection++;
				if (SessionThreadAgent.allServerThread.containsKey(SessionThread.nbConnection)) SessionThread.nbConnection++;
				sessionId = SessionThread.nbConnection;
				thread.idConnection = sessionId;
				thread.env.set("[x-user]", user);
				thread.env.set("[x-token]", token);
				thread.env.set("[x-script]", script);
				thread.env.set("[x-groups]", StringFx.decode_b64(Misc.atom(token, 2, ".")));

				thread.groups = (JSONObject) JsonManager.load(StringFx.decode_b64(Misc.atom(token, 2, ".")));
				
				//Create the data/transaction/user folder if does not exist
				if (!(new File("data"+File.separator+"transaction"+File.separator+sessionId).exists())) {

					(new File("data"+File.separator+"transaction"+File.separator+sessionId)).mkdir();

				}
				
				//Open the log file
				Transaction.logFiles.put(sessionId, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback.log", "rw"));
				
				allArguments = ScriptManager.get(thread.idConnection, script).get("vars")+"";
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
				JSONObject jscript = ScriptManager.get(session.idConnection, scriptName);
				
				String vars = (String) jscript.get("vars");
				v = Misc.splitCommand(vars);
				variables = ScriptManager.param(v.get(0));
				
				String mql = (String) jscript.get("mql");
				String activateLog = (String) jscript.get("activateLog");
				String pid = null;
				
				if (activateLog.equals("1")) {
					
					if (current_pid==null) {
						pid = SequenceManager.incr(0, "pid");
						Transaction.commit(0);
					} else pid = current_pid;
					
					MYSQLManager.addLog(scriptName, parent_pid, pid, "[---- Begin script ----]", "OK", null, null);
					
				}
				
				try {
				
					if (!session.user.equals("ai")) {
						if (session.groups!=null) {
							
							JSONObject groups = (JSONObject) JsonManager.load(((String) session.groups.get("scope")));
							
							if (!ScriptManager.isGrantedToUser(session.idConnection, groups, scriptName)) {
		
								throw new Exception("403-Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'.");
								
							}
							
						} else {
						
							//Check if a script is granted to a user
							if (!session.user.equals("mentdb") && 
									!GroupManager.isGrantedUser(session.idConnection, "sys", session.user) && 
									!ScriptManager.isGrantedToUser(session.idConnection, session.user, scriptName)) {
									
									throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'.");
								
							}
							
						}
					}
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
			
		}
		
		//Translations ...
		if (inputVector.size()>2) {
			
			if (inputVector.get(0).value.equals("relation") && inputVector.get(1).value.equals("translate")) {
				
				if (inputVector.size()<7) {
					
					throw new Exception("Sorry, the translate function must have 4 arguments min.");
					
				}
				
				String relationId = inputVector.get(2).value;
				String cooperation = inputVector.get(3).value;
				String lang = inputVector.get(4).value;
				String level = inputVector.get(5).value;

				inputVector.remove(0);
				inputVector.remove(0);
				inputVector.remove(0);
				inputVector.remove(0);
				inputVector.remove(0);
				inputVector.remove(0);
				inputVector.remove(inputVector.size()-1);
				
				LanguageManager.exceptionIfNotExist(lang);
				
				return RelationManager.translate(session.idConnection, relationId, lang, true, inputVector, env, session, Integer.parseInt(cooperation), level);
				
			}
			
		}
		
		/*String rrr = "";
		for(int iii=0;iii<inputVector.size();iii++) {
			
			rrr+= " "+inputVector.get(iii).value;
			
		}
		System.out.println("##########CommandSyncAccess: "+rrr);
		*/
		
		switch (inputVector.size()-1) {
		case 1:
			
			switch (inputVector.get(0).value) {
			case "commit":
				
				Transaction.commit(session.idConnection);
				
				return "Commit done.";

			case "rollback":
				
				Transaction.rollback(session.idConnection);
				
				return "Rollback done.";
			
			default:

				return inputVector.get(0).value;

			}
	
		case 2:
	
			switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
			case "concentration show":
				
				return NodeManager.format(ConcentrationManager.show().toJSONString());
	
			case "language show":
				
				return JsonFormatter.format(LanguageManager.showAll().toJSONString());
	
			case "cluster show":
				
				return JsonManager.format_Gson(ClusterManager.show_ids(session.idConnection).toJSONString());

			case "file_watcher show": 
				
				return JsonManager.format_Gson(FileFx.show_watch_service().toJSONString());

			case "refresh admin":
				
				return GroupManager.adminToHtml(session.idConnection, session, env, session.user, parent_pid);

			case "refresh config":
				
				return GroupManager.configToHtml(session.idConnection, session, env, session.user, parent_pid);

			case "job help":
				
				return JobManager.help();
			
			case "metric sessions":
				
				String r = "Open sessions: "+(Transaction.logFiles.size()-1)+"/"+Start.MAX_SESSION+"\n";
				for (Entry<Long, SessionThreadAgent> e : SessionThreadAgent.allServerThread.entrySet()) {
	
					if (!e.getValue().serverThread.user.equals("")) {
						r+= "  - ";
						r+= "id: "+e.getValue().serverThread.idConnection;
						r+= ", user: "+e.getValue().serverThread.user;
						r+= ", nbExecution="+e.getValue().serverThread.nbExecution;
						r+= ", life: "+(System.currentTimeMillis()-e.getValue().serverThread.life)/1000+"/"+Start.SESSION_TIMEOUT/1000+"s";
						r+= ", used: "+(System.currentTimeMillis()-e.getValue().serverThread.life)/1000;
						r+= ", maxUsable: "+Start.SESSION_TIMEOUT/1000;
						r+= "\n";
					}
	
				}
				
				return r;
			
			case "group show":

				return NodeManager.format(GroupManager.showAllGroups(session.idConnection).toJSONString());

			case "script show":
				
				return NodeManager.format(ScriptManager.show(session.idConnection).toJSONString());
				
			case "script export_all":
				
				ScriptManager.exportAll(session.idConnection);
				
				return "Scripts exported with successfully.";
				
			case "script get_all":
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.getAllScript(session.idConnection);
				
			case "user show":
				
				return JsonFormatter.format(UserManager.showAll(session.idConnection).toJSONString());
	
			case "sequence show":
				
				return JsonFormatter.format(SequenceManager.showAllSeqs(session.idConnection));
	
			case "job show":
				
				return JsonFormatter.format(JobManager.show(session.idConnection).toJSONString());
	
			case "parameter show":
				
				return JsonFormatter.format(ParameterManager.showAllParams().toJSONString());
	
			case "cm show":
				
				return JsonFormatter.format(CMManager.show(session.idConnection, null).toJSONString());
	
			case "cm show_obj":
				
				return JsonFormatter.format(CMManager.show_obj(session.idConnection, null).toJSONString());
	
			case "count sessions":
				
				return ""+(Transaction.logFiles.size()-1);
	
			default:
				
				switch (inputVector.get(0).value) {
				case "get_param":
					
					//Get key
					String key = inputVector.get(1).value;
	
					return ParameterManager.get_value(key);
	
				case "stop": case "shutdown":
					
					String idCode = inputVector.get(1).value;
					
					if (!idCode.equals(Misc.load("data"+File.separator+".id").replace("\r", "").replace("\n", ""))) {
						
						throw new Exception("Sorry, the identification code is invalid."); 
						
					}
					
					GroupManager.generateErrorIfNotGranted(session.idConnection, "sys", "Stop");
					
					Start.listening = false;
					Start.serverSocket.close();
					
					return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht0";
	
				default:
					
					//Script execution
					inputVector.remove(inputVector.size()-1);
					
					return CommandFullAccess.concatOrUnknow(inputVector);
					
				}
	
			}
	
		case 3:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
			case "dq algorithm show":
				
				return JsonManager.format_Gson(DQManager.show(session.idConnection).toJSONString());
				
			case "job show activate":
				
				return JsonManager.format_Gson(JobManager.showActivate().toJSONString());
				
			case "script show ghost":
				
				return NodeManager.format(ScriptManager.showGhost(session.idConnection).toJSONString());
				
			case "fs cache size": 
				
				return Record.cache.size()+"";

			case "fs lock size": 
				
				return LockObject.lock_record_by_key.size()+"";

			case "job scheduler start":
				
				JobManager.scheduler_start(session.idConnection);
				
				return "Job scheduler started with successful.";

			case "job scheduler stop":
				
				JobManager.scheduler_stop();
				
				return "Job scheduler stopped with successful.";

			case "job scheduler restart":
				
				JobManager.scheduler_restart(session.idConnection);
				
				return "Job scheduler restarted with successful.";

			case "job scheduler status":
				
				return JobManager.scheduler_status();
			
			default: 
	
				switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
				case "concentration depth":
					
					String key = inputVector.get(2).value;
	
					return ""+ConcentrationManager.getConcentrationDepth(key);
	
				case "relation stimulate":
					
					//Get keys
					String relationId = inputVector.get(2).value;
					
					RelationManager.stimulate(session.idConnection, relationId, true);
	
					return "Relation "+relationId+" stimulated with successful.";
	
				case "relation delete":
					
					//Get relation id
					relationId = inputVector.get(2).value;
					
					RelationManager.delete(session.idConnection, relationId);
					
					return "Relation '"+relationId+"' deleted with successful.";
	
				case "thought delete":
					
					//Get keys
					String thoughtId = inputVector.get(2).value;
					
					ThoughtManager.delete(session.idConnection, thoughtId);
	
					return "Thought "+thoughtId+" deleted with successful.";
	
				case "thought stimulate":
					
					//Get keys
					thoughtId = inputVector.get(2).value;
					
					ThoughtManager.stimulate(session.idConnection, thoughtId);
	
					return "Thought "+thoughtId+" stimulated with successful.";
	
				case "thought show":
					
					//Get key
					String word = inputVector.get(2).value;
	
					return JsonFormatter.format(ThoughtManager.list(session.idConnection, word).toJSONString());
	
				case "word delete":
					
					//Get key
					word = inputVector.get(2).value;
	
					WordManager.delete(session.idConnection, word);
	
					return "Word W["+inputVector.get(2).value+"] deleted with successful.";
	
				case "word show":
					
					word = inputVector.get(2).value;
					
					return JsonFormatter.format(WordManager.showTabLinks(session.idConnection, word).toJSONString());
					
				case "symbol stimulate":
					
					String tabLinkId = inputVector.get(2).value;
					
					SymbolManager.stimulate(session.idConnection, tabLinkId);
					
					return "Symbol tab link stimulated with successful.";
	
				case "language insert": case "language create": case "language add": 
					
					//Get parameters
					String lang = inputVector.get(2).value;
					
					LanguageManager.create(session.idConnection, lang, session, env);
	
					return "Language "+lang+" created with successful.";
	
				case "cluster create":
					
					//Get parameters
					String cluster_id = inputVector.get(2).value;
					
					ClusterManager.add(session.idConnection, cluster_id);
	
					return "Cluster created with successful.";
	
				case "cluster exist":
					
					//Get parameters
					cluster_id = inputVector.get(2).value;
					
					if (ClusterManager.exist(cluster_id)) return "1";
					else return "0";
	
				case "cluster delete":
					
					//Get parameters
					cluster_id = inputVector.get(2).value;
					
					ClusterManager.remove(session.idConnection, cluster_id);
	
					return "Cluster deleted with successful.";
	
				case "file_watcher kill": 
					
					//Get key
					key = inputVector.get(2).value;
					
					FileFx.stop_watch_service(key);
					
					return "1";

				case "file_watcher exist": 
					
					//Get key
					key = inputVector.get(2).value;
					
					if (FileFx.exist_watch_service(key)) return "1";
					else return "0";

				case "cm show":
					
					String type = inputVector.get(2).value;
					
					return JsonFormatter.format(CMManager.show(session.idConnection, type).toJSONString());
		
				case "cm show_obj":
					
					type = inputVector.get(2).value;
					
					return JsonFormatter.format(CMManager.show_obj(session.idConnection, type).toJSONString());
		
				case "script export_group":
					
					String group = inputVector.get(2).value;
					
					ScriptManager.exportGroup(session.idConnection, group);
					
					return "Scripts exported with successfully.";
					
				case "script import_group":

					group = inputVector.get(2).value;
					
					ScriptManager.importGroup(session, env, session.idConnection, group, parent_pid, current_pid);
					
					return "Scripts imported with successfully.";
					
				case "job pause":
					
					String jobid = inputVector.get(2).value;
					
					JobManager.job_pause(session.idConnection, jobid);
					
					return "1";

				case "job resume":
					
					jobid = inputVector.get(2).value;
					
					JobManager.job_resume(session.idConnection, jobid);
					
					return "1";

				case "user secret_key":
					
					String struser = inputVector.get(2).value;
					
					return UserManager.secret_key(session.idConnection, struser);

				case "refresh devel":
					
					String search = inputVector.get(2).value;

					return GroupManager.develToHtml(session.idConnection, session, env, session.user, parent_pid, search);

				case "cm get":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					
					return JsonManager.format_Gson(CMManager.get(session.idConnection, key).toJSONString());

				case "cm generate_update":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					
					return CMManager.generate_update(session.idConnection, key);

				case "cm exist":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					
					return CMManager.exist(session.idConnection, key);

				case "cm remove":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					
					CMManager.remove(session.idConnection, key);
					
					return "1";

				case "node insert": case "node create": case "node add":
					
					//Get key
					key = inputVector.get(2).value;
	
					NodeManager.insertNode(session.idConnection, key);
	
					return "Node "+inputVector.get(2).value+" created with successful.";
	
				case "node exist":
					
					//Get key
					key = inputVector.get(2).value;
	
					if (NodeManager.existNode(session.idConnection, key)) return "1";
					else return "0";
	
				case "node delete":
					
					//Get key
					key = inputVector.get(2).value;
					
					NodeManager.remove(session.idConnection, key);
					return "1";
	
				case "group add": case "group insert": case "group create":
					
					String groupName = inputVector.get(2).value;
					
					GroupManager.add(session.idConnection, session.user, groupName);
					
					return "Group added with successful.";
	
				case "group remove": 
					
					groupName = inputVector.get(2).value;
					
					GroupManager.delete(session.idConnection, groupName);
					
					return "Group removed with successful.";
	
				case "group exist": 
					
					groupName = inputVector.get(2).value;
					
					if (GroupManager.exist(session.idConnection, groupName)) return "1";
					else return "0";
	
				case "group get":
					
					groupName = inputVector.get(2).value;
					
					return NodeManager.format(GroupManager.get(session.idConnection, groupName).toJSONString());
	
				case "transaction logs":
					
					//Initialization
					int limit = 0;
	
					//Check if the limit is valid
					try {
	
						limit = Integer.parseInt(inputVector.get(2).value);
	
						if (limit<0) {
	
							throw new Exception("err");
	
						}
	
					} catch (Exception e) {
	
						throw new Exception("Sorry, the limit is not valid.");
	
					}
	
					return JsonFormatter.format(Transaction.logList(session.idConnection, limit).toJSONString());
	
				case "sequence remove":
					
					//Get key
					key = inputVector.get(2).value;
				
					SequenceManager.remove(session.idConnection, key);
					
					return "Sequence removed with successful.";
				
				case "job remove":
					
					//Get key
					key = inputVector.get(2).value;
				
					JobManager.delete(session.idConnection, key);
					
					return "Job removed with successful.";
				
				case "parameter remove":
					
					//Get key
					key = inputVector.get(2).value;
				
					ParameterManager.remove(session.idConnection, key);
					
					return "Parameter removed with successful.";
				
				case "sequence increment":
					
					//Get key
					key = inputVector.get(2).value;
				
					return SequenceManager.incr(session.idConnection, key);
				
				case "sequence exist":
					
					//Get key
					key = inputVector.get(2).value;
				
					if (SequenceManager.exist(session.idConnection, key)) return "1";
					else return "0";
				
				case "job exist":
					
					//Get key
					key = inputVector.get(2).value;
				
					if (JobManager.exist(session.idConnection, key)) return "1";
					else return "0";
				
				case "parameter exist":
					
					//Get key
					key = inputVector.get(2).value;
				
					if (ParameterManager.exist(key)) return "1";
					else return "0";
				
				case "parameter lock_dml":
					
					//Get key
					key = inputVector.get(2).value;
				
					ParameterManager.lock_dml(session.idConnection, key);
					
					return "Parameter locked for DML with successful.";
				
				case "parameter unlock_dml":
					
					//Get key
					key = inputVector.get(2).value;
				
					ParameterManager.unlock_dml(session.idConnection, key);
					
					return "Parameter unlocked for DML with successful.";
				
				case "user exist":
					
					//Get key
					String login = inputVector.get(2).value;
				
					if (UserManager.exist(session.idConnection, login)) return "1";
					else return "0";
				
				case "user disable":
					
					//Get key
					login = inputVector.get(2).value;
					
					UserManager.disable(session.idConnection, login);
				
					return "1";
				
				case "script delete":
					
					String id = inputVector.get(2).value;
					
					ScriptManager.delete(session.idConnection, id);
					
					return "Script deleted with successful.";
				
				case "script exist":
					
					id = inputVector.get(2).value;
					
					if (ScriptManager.exist(session.idConnection, id)) return "1";
					else return "0";
				
				case "script get":
					
					id = inputVector.get(2).value;
					
					return JsonFormatter.format(ScriptManager.get(session.idConnection, id).toJSONString());
				
				case "node show":
					
					//Get key
					key = inputVector.get(2).value;
					
					JSONObject rec = Record.getNode(session.idConnection, key);
	
					//SHOW RECORD
					if (rec==null) throw new Exception("Sorry, the node '"+key+"' does not exist.");
					else return JsonFormatter.format(rec.toJSONString());
	
				default:
	
					switch (inputVector.get(0).value) {
					case "login":
						
						String adminUser = inputVector.get(1).value.toLowerCase();
						String adminPassword = inputVector.get(2).value;
						
						if (adminUser.equals("mentdb")) {
							
							return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht5";
	
						} else if (!adminPassword.equals("") && UserManager.checkPassword(session.idConnection, adminUser, adminPassword)) {
							
							session.user = adminUser;
							
							//Create the data/transaction/user folder if does not exist
							if (!(new File("data"+File.separator+"transaction"+File.separator+session.idConnection).exists())) {
								
								(new File("data"+File.separator+"transaction"+File.separator+session.idConnection)).mkdir();
	
							}
							
							//Open the log file
							Transaction.logFiles.put(session.idConnection, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+session.idConnection+File.separator+"rollback.log", "rw"));
							
							return "Connected:"+session.idConnection;
	
						} else {
							
							//Bad password
							return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht3";
							
						}
	
					default:
						
						//Script execution
						inputVector.remove(inputVector.size()-1);
						
						return CommandFullAccess.concatOrUnknow(inputVector);
						
					}
					
				}
	
			}
	
		case 4:
	
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
			case "script public api doc": 
				
				return REST_DOCManager.public_api_doc(env);

			default:
				
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
				case "relation show thoughts":
					
					String relationId = inputVector.get(3).value;
					
					return NodeManager.format(RelationManager.showThoughtsRecursivelyRLTH(session.idConnection, relationId).toJSONString());
				
				case "thought show words":
					
					String thoughtId = inputVector.get(3).value;
					
					return NodeManager.format(ThoughtManager.getWords(session.idConnection, thoughtId).toJSONString());
				
				case "word show languages":
					
					//Get word
					String word = inputVector.get(3).value;
		
					return NodeManager.format(WordManager.showLanguages(session.idConnection, word).toJSONString());
		
				case "word lang probability":
					
					//Get sentence
					String sentence = inputVector.get(3).value;
					
					Vector<Vector<MQLValue>> s = Misc.splitCommand(sentence);
					
					if (s.size()==0) {
						
						return new JSONArray().toJSONString();
						
					}
		
					return NodeManager.format(WordManager.getLangProbability(session.idConnection, s.get(0)).toJSONString());
		
				case "symbol show languages":
					
					String symbolName = inputVector.get(3).value;
					
					return JsonFormatter.format(SymbolManager.showLanguages(session.idConnection, symbolName).toJSONString());
	
				case "cluster signal show":
					
					//Get params
					String cluster_id = inputVector.get(3).value;
					
					return ClusterManager.signal_show(session.idConnection, cluster_id);
					
				case "cluster nodes show_obj":
					
					//Get params
					cluster_id = inputVector.get(3).value;
					
					return JsonManager.format_Gson(ClusterManager.node_show_obj(session.idConnection, cluster_id).toJSONString());

				case "cluster node generate_set":
					
					//Get params
					cluster_id = inputVector.get(3).value;
					
					return ClusterManager.node_generate_update(session.idConnection, cluster_id);

				case "cluster nodes show_txt":
					
					//Get params
					cluster_id = inputVector.get(3).value;
					
					return ClusterManager.node_show_text(session.idConnection, cluster_id);

				case "app vhost show":
					
					//Get params
					String protocol = inputVector.get(3).value;
					
					return JsonManager.format_Gson(VHostManager.show_all(session.idConnection, protocol).toJSONString());

				case "dq algorithm exist":
					
					//Get params
					String key = inputVector.get(3).value;
					
					if (DQManager.exist(session.idConnection, key)) return "1";
					else return "0";

				case "dq algorithm get":
					
					//Get params
					key = inputVector.get(3).value;
					
					return DQManager.get_algo(session.idConnection, key);

				case "dq algorithm remove":
					
					//Get params
					key = inputVector.get(3).value;
					
					DQManager.remove(session.idConnection, key);
					
					return "1";

				case "script delete all":
					
					//Get params
					String startsWithScriptName = inputVector.get(3).value;

					ScriptManager.delete_all(session, env, session.idConnection, startsWithScriptName);
					
					return "1";

				case "fs index count":
					
					//Initialization
					int indexFileId = 0;
	
					//Check if the file id is valid
					try {
	
						indexFileId = Integer.parseInt(inputVector.get(3).value);
	
						if (indexFileId<0) {
	
							throw new Exception("err");
	
						}
	
					} catch (Exception e) {
	
						throw new Exception("Sorry, the file id is not valid.");
	
					}
	
					return ""+IndexFile.if_current_block_size(IndexFilePool.if_get(indexFileId).indexFile);
	
				case "sequence generate update":
					
					key = inputVector.get(3).value;
					
					return SequenceManager.generateUpdate(session.idConnection, key);
	
				case "parameter generate update":
					
					key = inputVector.get(3).value;
					
					return ParameterManager.generateUpdate(session.idConnection, key);
	
				case "parameter generate merge":
					
					key = inputVector.get(3).value;
					
					return ParameterManager.generateMerge(session.idConnection, key);
	
				case "job generate update":
					
					String jobId = inputVector.get(3).value;
					
					return JobManager.generateUpdate(session.idConnection, jobId);
	
				case "script generate url":
					
					String scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateUrl(session.idConnection, env, scriptName, session.user, parent_pid);
	
				case "script generate delay":
					
					scriptName = inputVector.get(3).value;
					
					return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateUpdateDelay(session.idConnection, scriptName);
	
				case "script generate execute":
					
					scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateExecute(session.idConnection, env, scriptName, parent_pid);
	
				case "script generate stack":
					
					scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateStack(session.idConnection, env, scriptName, parent_pid);
	
				case "script generate call":
					
					scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateCall(session.idConnection, env, scriptName, parent_pid);
	
				case "script generate sub_include":
					
					scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateSubInclude(session.idConnection, env, scriptName, parent_pid);
	
				case "script generate include":
					
					scriptName = inputVector.get(3).value;
					
					return ScriptManager.generateInclude(session.idConnection, env, scriptName, parent_pid);
	
				case "user show groups":
					
					String login = inputVector.get(3).value;
					
					return NodeManager.format(GroupManager.userGetGroup(session.idConnection, login).toJSONString());
	
				case "user show scripts":
					
					login = inputVector.get(3).value;
					
					return NodeManager.format(GroupManager.userGetScript(session.idConnection, login).toJSONString());
	
				case "script show groups":
					
					scriptName = inputVector.get(3).value;
					
					return NodeManager.format(GroupManager.scriptGetGroup(session.idConnection, scriptName).toJSONString());
	
				case "script show users":
					
					scriptName = inputVector.get(3).value;
					
					return NodeManager.format(GroupManager.scriptGetUser(session.idConnection, scriptName).toJSONString());
	
				case "group get user":
					
					String groupName = inputVector.get(3).value;
					
					return NodeManager.format(GroupManager.getUser(session.idConnection, groupName).toJSONString());
	
				case "group get script":

					groupName = inputVector.get(3).value;

					return NodeManager.format(GroupManager.getScript(session.idConnection, groupName).toJSONString());

				case "script generate update":
					
					String id = inputVector.get(3).value;
					
					return ScriptManager.generateUpdate(session.idConnection, id);
				
				case "script generate create":
					
					id = inputVector.get(3).value;
					
					return ScriptManager.generateCreate(session.idConnection, id);
				
				case "parameter get value":
					
					//Get key
					key = inputVector.get(3).value;
	
					return ParameterManager.get_value(key);
	
				case "parameter get locked_dml":
					
					//Get key
					key = inputVector.get(3).value;
	
					return ParameterManager.get_locked(key);
	
				case "sequence get current":
					
					//Get key
					key = inputVector.get(3).value;
	
					return SequenceManager.get_current(session.idConnection, key);
	
				case "node show detailed":
					
					//Get keys
					key = inputVector.get(3).value;
	
					//SHOW RECORD
					return JsonFormatter.format(Record.getDetail(key).toJSONString());
	
				case "relation show sentence":
					
					relationId = inputVector.get(3).value;
					
					return RelationManager.showSentence(session.idConnection, relationId, env, session);
	
				default:
	
					switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
					case "circle delete":

						String level = inputVector.get(2).value;
						String r_th = inputVector.get(3).value;
						
						CircleManager.deleteCircle(session.idConnection, level, r_th);
						
						return "The relation/thought is out of the circle.";
		
					case "circle show":

						level = inputVector.get(2).value;
						r_th = inputVector.get(3).value;
						
						return JsonFormatter.format(CircleManager.showCircle(session.idConnection, level, r_th).toJSONString());
		
					case "circle ids":

						level = inputVector.get(2).value;
						r_th = inputVector.get(3).value;
						
						return JsonFormatter.format(CircleManager.getIds(session.idConnection, level, r_th).toJSONString());
		
					case "circle exist":

						level = inputVector.get(2).value;
						r_th = inputVector.get(3).value;
						
						return CircleManager.existCircle(session.idConnection, level, r_th);
		
					case "relation insert": case "relation create": case "relation add":
						
						//Get thoughts
						String thoughts = inputVector.get(2).value;
						String lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return RelationManager.create(session.idConnection, thoughts, lang);
		
					case "thought prob_in_words":
						
						//Get key
						String ths = inputVector.get(2).value;
						String words = inputVector.get(3).value;
						
						return ThoughtManager.probability_in_words(session.idConnection, ths, words)+"";
	
					case "word stimulate":
						
						//Get word
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
	
						WordManager.stimulate(session.idConnection, word, lang);
	
						return "Word W["+inputVector.get(2).value+"] stimulated with successful in the language '"+lang+"'.";
	
					case "word levenshtein":
						
						//Get string
						String symbolChars = inputVector.get(2).value;
						String order = inputVector.get(3).value;
						int iorder = 0;
						
						if (order.toLowerCase().equals("desc")) {
							
							iorder = 2;
							
						} else if (order.toLowerCase().equals("asc")) {
							
							iorder = 1;
							
						} else if (order.toLowerCase().equals("top")) {
							
							iorder = 0;
							
						} else {
							
							throw new Exception("Sorry, the order field is not valid (asc|desc|top).");
							
						}
	
						return WordManager.searchLevenshteinDistance(session.idConnection, symbolChars, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder).toJSONString();
	
					case "word exist":
						
						//Get key
						key = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						if(WordManager.exist(session.idConnection, key, lang)) return "1";
						else return "0";
	
					case "thought show":
						
						//Get key
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
	
						return JsonFormatter.format(ThoughtManager.list(session.idConnection, word, lang).toJSONString());
	
					case "thought first":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.firstTabLink(session.idConnection, word, lang)+"";
	
					case "thought last":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.lastTabLink(session.idConnection, word, lang)+"";
	
					case "word delete":
						
						//Get key
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
		
						WordManager.deleteLang(session.idConnection, word, lang);
		
						return "Word W["+inputVector.get(2).value+"] deleted with successful in the language '"+lang+"'.";
		
					case "word first":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.firstTabLink(session.idConnection, word, lang)+"";
		
					case "word last":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.lastTabLink(session.idConnection, word, lang)+"";
		
					case "word show":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(WordManager.showTabLinks(session.idConnection, word, lang).toJSONString());
		
					case "word perception":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(WordManager.showTabLinkPerceptions(session.idConnection, word, lang).toJSONString());
					
					case "symbol perception":
						
						String symbol = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(SymbolManager.showPerceptionTabLinks(session.idConnection, symbol, lang).toJSONString());
					
					case "symbol insert": case "symbol create": case "symbol add": 
						
						//Get key
						symbol = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
	
						SymbolManager.create(session.idConnection, symbol, lang);
	
						return "Symbol S["+inputVector.get(2).value+"] created with successful in the language '"+lang+"'.";
	
					case "symbol first":
						
						symbol = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return SymbolManager.firstTabLink(session.idConnection, symbol, lang);
		
					case "symbol last":
						
						symbol = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return SymbolManager.lastTabLink(session.idConnection, symbol, lang);
		
					case "symbol show":
						
						symbol = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(SymbolManager.showTabLinks(session.idConnection, symbol, lang, ConcentrationManager.getConcentrationDepth("C[symbol]")).toJSONString());
		
					case "cluster node":

						cluster_id = inputVector.get(2).value;
						String method = inputVector.get(3).value;

						return JsonFormatter.format(ClusterManager.get_node(session.idConnection, cluster_id, method).toJSONString());

					case "cm exist":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String type = inputVector.get(3).value;
						
						return CMManager.exist_type(session.idConnection, key, type);

					case "app delete":
						
						//Get parameters
						type = inputVector.get(2).value;
						String context = inputVector.get(3).value;
		
						return AppManager.delete_context(session, type, context);
		
					case "cm set":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String json = inputVector.get(3).value;
		
						CMManager.set(session.idConnection, key, json);
						
						return "1";
	
					case "node select":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String xPath = inputVector.get(3).value;
		
						return NodeManager.select(session.idConnection, key, xPath);
	
					case "node count":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
		
						return NodeManager.count(session.idConnection, key, xPath);
	
					case "user insert": case "user create": case "user add": 
						
						//Get parameters
						login = inputVector.get(2).value;
						String password = inputVector.get(3).value;
						
						UserManager.create(session.idConnection, login, "", password, false);

						return "User "+login+" created with successful.";

					case "node fields":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String jPath = inputVector.get(3).value;
	
						return JsonFormatter.format(NodeManager.fields(session.idConnection, key, jPath));
	
					case "sequence add": case "sequence create": case "sequence insert":
						
						//Get key
						key = inputVector.get(2).value;
						String value = inputVector.get(3).value;
	
						SequenceManager.add(session.idConnection, key, value);
						
						return "Sequence added with successful.";
					
					case "sequence update":
						
						//Get key
						key = inputVector.get(2).value;
						value = inputVector.get(3).value;
					
						SequenceManager.update(session.idConnection, key, value);
						
						return "Sequence updated with successful.";
					
					case "parameter update":
						
						//Get key
						key = inputVector.get(2).value;
						value = inputVector.get(3).value;
					
						ParameterManager.update(session.idConnection, key, value);
						
						return "Parameter updated with successful.";
					
					case "parameter lock_if_null":
						
						//Get key
						key = inputVector.get(2).value;
						value = inputVector.get(3).value;
					
						return ParameterManager.lock_if_null(session.idConnection, key, value);
					
					default:
						
						//Script execution
						inputVector.remove(inputVector.size()-1);
						
						return CommandFullAccess.concatOrUnknow(inputVector);
						
					}
					
				}
				
			}
	
		case 5:
	
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
			default:
				
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				case "relation show thought nodes":
					
					String relationId = inputVector.get(4).value;
					
					return NodeManager.format(RelationManager.showThoughtNodes(session.idConnection, relationId).toJSONString());
				
				case "fs unused last block":
					
					int fileId = 0;
					
					//Check if the file id is valid
					try {
		
						fileId = Integer.parseInt(inputVector.get(4).value);
		
						if (fileId<0) {
		
							throw new Exception("err");
		
						}
		
					} catch (Exception e) {
		
						throw new Exception("Sorry, the file id is not valid.");
		
					}
		
					return ""+UnusedFile.last_unused_block(DataFilePool.dfp_get(fileId));
		
				case "fs unused nb block":
					
					fileId = 0;
					
					//Check if the file id is valid
					try {
		
						fileId = Integer.parseInt(inputVector.get(4).value);
		
						if (fileId<0) {
		
							throw new Exception("err");
		
						}
		
					} catch (Exception e) {
		
						throw new Exception("Sorry, the file id is not valid.");
		
					}
		
					return ""+UnusedFile.nb_unused_block(DataFilePool.dfp_get(fileId));
		
				case "fs index calcul pos":
					
					//Get keys
					String key = inputVector.get(4).value;
					
					JSONObject obj = new JSONObject();
					
					//Get the index file position
					int indexFileToWrite = IndexFile.indexFileId(key);
					
					//Get the position into the index file tab
					long indexFileTabPosition = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileToWrite).indexFile));
	
					obj.put("key", key);
					obj.put("indexFileId", indexFileToWrite);
					obj.put("indexPosition", indexFileTabPosition*12+8);
	
					//SHOW RECORD
					return JsonFormatter.format(obj.toJSONString());
					
				default:
					
					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "concentration set depth":
						
						key = inputVector.get(3).value;
						int depth = 0;
					
						//Check if the depth is valid
						try {
					
							depth = Integer.parseInt(inputVector.get(4).value);
					
							if (depth<=0) {
					
								throw new Exception("err");
					
							}
					
						} catch (Exception e) {
					
							throw new Exception("Sorry, the depth value is not valid.");
					
						}
						
						ConcentrationManager.setConcentrationDepth(session.idConnection, key, depth);
						
						return "Concentration depth saved with successful.";
					
					case "thought show words":
						
						String thoughtId = inputVector.get(3).value;
						String lang = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return NodeManager.format(ThoughtManager.getWords(session.idConnection, thoughtId, lang, new Vector<MQLValue>()).toJSONString());
					
					case "symbol show words":
						
						String symbol = inputVector.get(3).value;
						lang = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(SymbolManager.showWords(session.idConnection, symbol, lang));
	
					case "cluster signal delete":
						
						//Get params
						String cluster_id = inputVector.get(3).value;
						String node_id = inputVector.get(4).value;
						
						ClusterManager.signal_delete(session.idConnection, cluster_id, node_id);
						
						return "Signal deleted with successful.";

					case "cluster node delete":
						
						//Get params
						cluster_id = inputVector.get(3).value;
						node_id = inputVector.get(4).value;
						
						ClusterManager.node_remove(session.idConnection, cluster_id, node_id);
						
						return "Node deleted with successful.";

					case "cluster node reinstate":
						
						//Get params
						cluster_id = inputVector.get(3).value;
						node_id = inputVector.get(4).value;
						
						ClusterManager.node_reinstate(session.idConnection, cluster_id, node_id);
						
						return "1";

					case "app vhost show":
						
						//Get params
						String protocol = inputVector.get(3).value;
						String context = inputVector.get(4).value;
						
						return JsonManager.format_Gson(VHostManager.show(session.idConnection, protocol, context).toJSONString());

					case "dq algorithm set":
						
						//Get params
						key = inputVector.get(3).value;
						String algo = inputVector.get(4).value;
						
						DQManager.set(session.idConnection, key, algo);
						
						return "1";

					case "user check password": 
						
						//Get parameters
						String login = inputVector.get(3).value;
						String password = inputVector.get(4).value;
						
						if (UserManager.checkPassword(session.idConnection, login, password)) return "1";
						else return "0";

					case "script api doc": 
						
						//Get parameters
						login = inputVector.get(3).value;
						password = inputVector.get(4).value;
						
						return REST_DOCManager.private_api_doc(env, login, password);

					case "fs data block":
						
						GroupManager.generateErrorIfNotGranted(session.idConnection, "sys", "File system");
						
						//Initialization
						long pos = 0;
						fileId = 0;
		
						//Check if the file id is valid
						try {
		
							fileId = Integer.parseInt(inputVector.get(3).value);
		
							if (fileId<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the file id is not valid.");
		
						}
		
						//Check if the position is valid
						try {
		
							pos = Long.parseLong(inputVector.get(4).value);
		
							if (pos<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the position is not valid.");
		
						}
						
						return Block.read_block(DataFilePool.dfp_get(fileId), pos).toString();
		
					case "fs data record":
						
						GroupManager.generateErrorIfNotGranted(session.idConnection, "sys", "File system");
						
						//Initialization
						pos = 0;
						fileId = 0;
		
						//Check if the file id is valid
						try {
		
							fileId = Integer.parseInt(inputVector.get(3).value);
		
							if (fileId<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the file id is not valid.");
		
						}
		
						//Check if the position is valid
						try {
		
							pos = Long.parseLong(inputVector.get(4).value);
		
							if (pos<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the position is not valid.");
		
						}
		
						JSONArray result = new JSONArray();
						String recString = Record.read(DataFilePool.dfp_get(fileId), 1, pos, result);
						result.add(Block.decodeb64(recString));
		
						return JsonFormatter.format(result.toJSONString());
		
					case "fs index block":
						
						//Initialization
						pos = 0;
						fileId = 0;
		
						//Check if the file id is valid
						try {
		
							fileId = Integer.parseInt(inputVector.get(3).value);
		
							if (fileId<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the file id is not valid.");
		
						}
		
						//Check if the position is valid
						try {
		
							pos = Long.parseLong(inputVector.get(4).value);
		
							if (pos<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the position is not valid.");
		
						}
		
						return IndexFile.if_toString(fileId, pos);
		
					case "user force insert": case "user force create": case "user force add": 
						
						//Get parameters
						login = inputVector.get(3).value;
						password = inputVector.get(4).value;
						
						UserManager.create(session.idConnection, login, "", password, true);

						return "User "+login+" created with successful.";

					case "group grant user":
						
						login = inputVector.get(3).value;
						String groupName = inputVector.get(4).value;
						
						GroupManager.grantUser(session.idConnection, groupName, login);
		
						return "User granted with successful.";
		
					case "group grant script":
						
						String scriptId = inputVector.get(3).value;
						groupName = inputVector.get(4).value;
						
						GroupManager.grantScript(session.idConnection, groupName, scriptId, false);
		
						return "Script granted with successful.";
		
					case "group ungrant user":
						
						login = inputVector.get(3).value;
						groupName = inputVector.get(4).value;
						
						GroupManager.ungrantUser(session.idConnection, groupName, login);
		
						return "User ungranted with successful.";
		
					case "group grant_all script":
						
						scriptId = inputVector.get(3).value;
						groupName = inputVector.get(4).value;
						
						GroupManager.grantAllScript(session.idConnection, groupName, scriptId, false);
		
						return "Scripts granted with successful.";
		
					case "group ungrant_all script":
						
						scriptId = inputVector.get(3).value;
						groupName = inputVector.get(4).value;
						
						GroupManager.ungrantAllScript(session.idConnection, groupName, scriptId);
		
						return "Scripts ungranted with successful.";
		
					case "group ungrant script":
						
						scriptId = inputVector.get(3).value;
						groupName = inputVector.get(4).value;
						
						GroupManager.ungrantScript(session.idConnection, groupName, scriptId);
		
						return "Script ungranted with successful.";
		
					case "script is granted":

						String scriptName = inputVector.get(3).value;
						login = inputVector.get(4).value;
						
						if (ScriptManager.isGrantedToUser(session.idConnection, login, scriptName)) return "1";
						else return "0";
						
					case "fs data size":
						
						//Initialization
						int page = 0;
						int nbByPage = 100;
	
						//Check if the page is valid
						try {
	
							page = Integer.parseInt(inputVector.get(3).value)-1;
	
							if (page<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the page is not valid.");
	
						}
	
						//Check if the number by page is valid
						try {
	
							nbByPage = Integer.parseInt(inputVector.get(4).value);
	
							if (nbByPage<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the number by page is not valid.");
	
						}
	
						//Parse all data files
						int index = page*nbByPage;
						int counter = nbByPage;
						JSONArray jsonResult = new JSONArray();
						while (true) {
	
							if (counter==0) {
	
								break;
	
							}
	
							//Check if the file does not exist
							if (!(new File("data"+File.separator+"data"+File.separator+index+File.separator+"brain"+index+".dat")).exists()) {
	
								break;
	
							}
	
							//Get the data file
							DataFile dataFile = DataFilePool.dfp_get(index);
	
							JSONObject sess = new JSONObject();
							sess.put("dataFileId", index);
							sess.put("percent_graph", DataFile.df_per30_used(dataFile.dataFile));
							sess.put("current_block_size", DataFile.df_current_block_size(dataFile.dataFile));
							sess.put("current_max_block_size", DataFile.df_current_max_block_size(dataFile.dataFile));
							sess.put("percent_used", DataFile.df_percent_used(dataFile.dataFile));
							sess.put("size", DataFile.df_get_size(dataFile.dataFile));
							sess.put("lastUnusedBlock", UnusedFile.last_unused_block(dataFile));
							sess.put("nbUnusedBlock", UnusedFile.nb_unused_block(dataFile));
							
							jsonResult.add(sess);
							
							index++;
							counter--;
	
						}
						
						return JsonFormatter.format(jsonResult.toJSONString());
	
					case "fs index size":
						
						//Initialization
						page = 0;
						nbByPage = 100;
	
						//Check if the page is valid
						try {
	
							page = Integer.parseInt(inputVector.get(3).value)-1;
	
							if (page<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the page is not valid.");
	
						}
	
						//Check if the number by page is valid
						try {
	
							nbByPage = Integer.parseInt(inputVector.get(4).value);
	
							if (nbByPage<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the number by page is not valid.");
	
						}
	
						//Parse all data files
						index = page*nbByPage;
						counter = nbByPage;
						jsonResult = new JSONArray();
						while (true) {
	
							if (counter==0) {
	
								break;
	
							}
	
							//Check if the file does not exist
							if (!(new File("data"+File.separator+"index"+File.separator+index+File.separator+"brain"+index+".idx")).exists()) {
	
								break;
	
							}
	
							//Get the index file
							IndexFile indexFile = IndexFilePool.if_get(index);
	
							JSONObject sess = new JSONObject();
							sess.put("indexFileId", index);
							sess.put("percent_graph", IndexFile.if_per30_used(indexFile.indexFile));
							sess.put("current_block_size", IndexFile.if_current_block_size(indexFile.indexFile));
							sess.put("current_max_block_size", IndexFile.if_current_max_block_size(indexFile.indexFile));
							sess.put("percent_used", IndexFile.if_percent_used(indexFile.indexFile));
							sess.put("size", IndexFile.if_get_size(indexFile.id));
							
							jsonResult.add(sess);
							
							index++;
							counter--;
	
						}
						
						return JsonFormatter.format(jsonResult.toJSONString());
	
					case "node is object":
							
						//Get key, name and value
						key = inputVector.get(3).value;
						String xPath = inputVector.get(4).value;
		
						return NodeManager.is_obj(session.idConnection, key, xPath);
	
					case "node is array":
							
						//Get key, name and value
						key = inputVector.get(3).value;
						xPath = inputVector.get(4).value;
		
						return NodeManager.is_array(session.idConnection, key, xPath);
		
					case "user set password":
						
						//Get user info
						String user = inputVector.get(3).value;
						String pwd = inputVector.get(4).value;
	
						UserManager.updatePassword(session.idConnection, user, pwd);
						
						return "Password updated with successful.";
					
					case "metric index files":
						
						//Initialization
						page = 0;
						nbByPage = 100;
	
						//Check if the page is valid
						try {
	
							page = Integer.parseInt(inputVector.get(3).value)-1;
	
							if (page<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the page is not valid.");
	
						}
	
						//Check if the number by page is valid
						try {
	
							nbByPage = Integer.parseInt(inputVector.get(4).value);
	
							if (nbByPage<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the number by page is not valid.");
	
						}
	
						//Parse all data files
						index = page*nbByPage;
						counter = nbByPage;
						String r = "";
						while (true) {
	
							if (counter==0) {
	
								break;
	
							}
	
							//Check if the file does not exist
							if (!(new File("data"+File.separator+"index"+File.separator+index+File.separator+"brain"+index+".idx")).exists()) {
	
								break;
	
							}
	
							//Get the index file
							IndexFile indexFile = IndexFilePool.if_get(index);
							
							if (r.equals("")) {
								r += "Total block: "+Start.NB_INDEX_FILE+"x"+IndexFile.if_current_max_block_size(indexFile.indexFile)+"="+(Start.NB_INDEX_FILE*IndexFile.if_current_max_block_size(indexFile.indexFile))+"\n";
								r += "Total size: "+Start.NB_INDEX_FILE+"x"+IndexFile.if_get_size(indexFile.id)+"="+(Start.NB_INDEX_FILE*IndexFile.if_get_size(indexFile.id))+"b\n";
							}
	
							r += IndexFile.if_per30_used(indexFile.indexFile)+" "+IndexFile.if_percent_used(indexFile.indexFile)+"%";
							r += " - "+index+" - used: "+IndexFile.if_current_block_size(indexFile.indexFile);
							r += ", free: "+(IndexFile.if_current_max_block_size(indexFile.indexFile)-IndexFile.if_current_block_size(indexFile.indexFile));
							r += ", total block: "+IndexFile.if_current_max_block_size(indexFile.indexFile);
							r += ", size: "+IndexFile.if_get_size(indexFile.id)+"b";
							r += "\n";
							
							index++;
							counter--;
	
						}
						
						return r;
	
					case "metric data files":
						
						//Initialization
						page = 0;
						nbByPage = 100;
	
						//Check if the page is valid
						try {
	
							page = Integer.parseInt(inputVector.get(3).value)-1;
	
							if (page<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the page is not valid.");
	
						}
	
						//Check if the number by page is valid
						try {
	
							nbByPage = Integer.parseInt(inputVector.get(4).value);
	
							if (nbByPage<0) {
	
								throw new Exception("err");
	
							}
	
						} catch (Exception e) {
	
							throw new Exception("Sorry, the number by page is not valid.");
	
						}
	
						//Parse all data files
						index = page*nbByPage;
						counter = nbByPage;
						r = "";
						while (true) {
	
							if (counter==0) {
	
								break;
	
							}
							
							//Check if the file does not exist
							if (!(new File("data"+File.separator+"data"+File.separator+index+File.separator+"brain"+index+".dat")).exists()) {
	
								break;
	
							}
	
							//Get the data file
							DataFile dataFile = DataFilePool.dfp_get(index);
	
							r += DataFile.df_per30_used(dataFile.dataFile)+" "+DataFile.df_percent_used(dataFile.dataFile)+"%";
							r += " - "+index+" - used: "+DataFile.df_current_block_size(dataFile.dataFile);
							r += ", free: "+(DataFile.df_current_max_block_size(dataFile.dataFile)-DataFile.df_current_block_size(dataFile.dataFile));
							r += ", total block: "+DataFile.df_current_max_block_size(dataFile.dataFile);
							r += ", size: "+DataFile.df_get_size(dataFile.dataFile)+"b";
							r += ", max size: "+((DataFile.df_current_max_block_size(dataFile.dataFile)*DataFile.df_get_size(dataFile.dataFile))/DataFile.df_current_block_size(dataFile.dataFile)/1000000)+"Mb";
							r += ", last unused block: "+UnusedFile.last_unused_block(dataFile);
							r += ", nb unused block: "+UnusedFile.nb_unused_block(dataFile);
							r += "\n";
							
							index++;
							counter--;
	
						}
						
						return r;
	
					case "script copy all":
						
						//Get params
						String startsWithScriptName = inputVector.get(3).value;
						String replacement = inputVector.get(4).value;
						
						ScriptManager.copy_all(env, session.idConnection, startsWithScriptName, replacement, parent_pid);
						
						return "1";
	
					case "script rename all":
						
						//Get params
						startsWithScriptName = inputVector.get(3).value;
						replacement = inputVector.get(4).value;
						
						ScriptManager.rename_all(session, env, session.idConnection, startsWithScriptName, replacement, parent_pid);
						
						return "1";
	
					case "word perception thought":
						
						String word = inputVector.get(3).value;
						lang = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(WordManager.showThoughtPerceptions(session.idConnection, word, lang).toJSONString());
					
					case "word perception symbol":
						
						word = inputVector.get(3).value;
						lang = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return JsonFormatter.format(WordManager.showSymbolPerception(session.idConnection, word, lang).toJSONString());
					
					default:
						
						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "circle ids":

							String level = inputVector.get(2).value;
							lang = inputVector.get(3).value;
							String r_th = inputVector.get(4).value;
							
							return JsonFormatter.format(CircleManager.getIds(session.idConnection, level, lang, r_th).toJSONString());
			
						case "circle id":

							level = inputVector.get(2).value;
							lang = inputVector.get(3).value;
							r_th = inputVector.get(4).value;
							
							return CircleManager.getId(session.idConnection, level, lang, r_th);
			
						case "circle contains":

							level = inputVector.get(2).value;
							String r_th_1 = inputVector.get(3).value;
							String r_th_2 = inputVector.get(4).value;
							
							if (CircleManager.contains(session.idConnection, level, r_th_1, r_th_2)) {
								
								return "1";
								
							} else {
								
								return "0";
								
							}

						case "circle merge": 

							level = inputVector.get(2).value;
							String th_1 = inputVector.get(3).value;
							String th_2 = inputVector.get(4).value;
							
							return CircleManager.mergeCircle(session.idConnection, level, th_1, th_2, env, session);
						
						case "relation search":
							
							//Get key
							String text = inputVector.get(2).value;
							lang = inputVector.get(3).value;
							String searchPunctuation = inputVector.get(4).value;
							
							LanguageManager.exceptionIfNotExist(lang);
							
							if (searchPunctuation.equals("1")) 
								return JsonFormatter.format(SearchEngine.execute(session.idConnection, text, lang, true, session, env).toJSONString());
							else 
								return JsonFormatter.format(SearchEngine.execute(session.idConnection, text, lang, false, session, env).toJSONString());
			
						case "thought merge":
							
							//Get keys
							level = inputVector.get(2).value;
							String thoughtId1 = inputVector.get(3).value;
							String thoughtId2 = inputVector.get(4).value;
							
							ThoughtManager.merge(session.idConnection, level, thoughtId1, thoughtId2, session, env);
		
							return "Thoughts merged with successful.";
		
						case "thought get":
							
							word = inputVector.get(2).value;
							lang = inputVector.get(4).value;
							int positionInt = 0;
							
							LanguageManager.exceptionIfNotExist(lang);
	
							//Check if the file id is valid
							try {
	
								positionInt = Integer.parseInt(inputVector.get(3).value);
	
								if (positionInt<0) {
	
									throw new Exception("err");
	
								}
	
							} catch (Exception e) {
	
								throw new Exception("Sorry, the position is not valid.");
	
							}
							
							return WordManager.getTabLink(session.idConnection, word, positionInt, lang);
	
						case "word levenshtein":
							
							//Get string
							String symbolChars = inputVector.get(2).value;
							String order = inputVector.get(3).value;
							lang = inputVector.get(4).value;
							int iorder = 0;
							
							LanguageManager.exceptionIfNotExist(lang);
							
							if (order.toLowerCase().equals("desc")) {
								
								iorder = 2;
								
							} else if (order.toLowerCase().equals("asc")) {
								
								iorder = 1;
								
							} else if (order.toLowerCase().equals("top")) {
								
								iorder = 0;
								
							} else {
								
								throw new Exception("Sorry, the order field is not valid (asc|desc|top).");
								
							}
		
							return WordManager.searchLevenshteinDistance(session.idConnection, symbolChars, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder, lang).toJSONString();
		
						case "word get":
							
							word = inputVector.get(2).value;
							lang = inputVector.get(4).value;
							int position = 0;
							
							LanguageManager.exceptionIfNotExist(lang);
			
							//Check if the file id is valid
							try {
			
								position = Integer.parseInt(inputVector.get(3).value);
			
								if (position<0) {
			
									throw new Exception("err");
			
								}
			
							} catch (Exception e) {
			
								throw new Exception("Sorry, the position is not valid.");
			
							}
							
							return WordManager.getTabLink(session.idConnection, word, position, lang);
			
						case "word insert": case "word create": case "word add": 
							
							//Get key
							key = inputVector.get(2).value;
							lang = inputVector.get(3).value;
							String lock_translation = inputVector.get(4).value;
							
							LanguageManager.exceptionIfNotExist(lang);
							
							return WordManager.create(session.idConnection, key, "", lang, lock_translation, session, env);
		
						case "symbol get":
							
							symbol = inputVector.get(2).value;
							lang = inputVector.get(4).value;
							position = 0;
							
							LanguageManager.exceptionIfNotExist(lang);
			
							//Check if the file id is valid
							try {
			
								position = Integer.parseInt(inputVector.get(3).value);
			
								if (position<0) {
			
									throw new Exception("err");
			
								}
			
							} catch (Exception e) {
			
								throw new Exception("Sorry, the position is not valid.");
			
							}
							
							return SymbolManager.getTabLink(session.idConnection, symbol, position, lang);
			
						case "script copy":
							
							//Get params
							String oldScriptName = inputVector.get(2).value;
							String method = inputVector.get(3).value;
							String newScriptName = inputVector.get(4).value;
			
							ScriptManager.copy(env, session.idConnection, oldScriptName, method, newScriptName, parent_pid);
							
							return "1";
		
						case "script rename":
							
							//Get params
							oldScriptName = inputVector.get(2).value;
							method = inputVector.get(3).value;
							newScriptName = inputVector.get(4).value;
			
							ScriptManager.rename(env, session.idConnection, oldScriptName, method, newScriptName, parent_pid);
							
							return "1";
		
						case "node dobject":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;
							String fieldName = inputVector.get(4).value;
			
							NodeManager.odelete(session.idConnection, key, xPath, fieldName);
			
							return "Deleted with successful.";
			
						case "node darray":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;
							String i = inputVector.get(4).value;
			
							NodeManager.adelete(session.idConnection, key, xPath, i);
			
							return "Deleted with successful.";
			
						case "parameter add": case "parameter create": case "parameter insert":
							
							//Get key
							key = inputVector.get(2).value;
							String value = inputVector.get(3).value;
							String locked = inputVector.get(4).value;
							
							if (!locked.equals("1") && !locked.equals("0")) {
								
								throw new Exception("Sorry, the locked field must be a boolean (1 or 0).");
								
							}
	
							ParameterManager.add(session.idConnection, key, value, locked);
							
							return "Parameter added with successful.";
						
						case "parameter merge":
							
							//Get key
							key = inputVector.get(2).value;
							value = inputVector.get(3).value;
							locked = inputVector.get(4).value;
							
							if (!locked.equals("1") && !locked.equals("0")) {
								
								throw new Exception("Sorry, the locked field must be a boolean (1 or 0).");
								
							}
	
							ParameterManager.merge(session.idConnection, key, value, locked);
							
							return "Parameter merged with successful.";
						
						case "user insert": case "user create": case "user add": 
							
							//Get parameters
							login = inputVector.get(2).value;
							password = inputVector.get(3).value;
							String separator = inputVector.get(4).value;
							
							UserManager.create(session.idConnection, login, separator, password, false);
	
							return "User "+login+" created with successful.";
	
						default: 
							
							switch (inputVector.get(0).value) {
								
							default:
								
								//Script execution
								inputVector.remove(inputVector.size()-1);
								
								return CommandFullAccess.concatOrUnknow(inputVector);
								
							}
							
						}
						
					}
					
				}
				
			}
	
		case 6:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
			default :
	
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				case "thought delete by word":
					
					//Get key
					String word = inputVector.get(4).value;
					String thoughtId = inputVector.get(5).value;
	
					ThoughtManager.deleteWord(session.idConnection, thoughtId, word);
	
					return "Thought "+thoughtId+" deleted with successful for the word '"+word+"'.";
	
				case "thought delete by lang":
					
					//Get key
					String lang = inputVector.get(4).value;
					thoughtId = inputVector.get(5).value;
					
					LanguageManager.exceptionIfNotExist(lang);
	
					ThoughtManager.deleteLang(session.idConnection, thoughtId, lang);
	
					return "Thought "+thoughtId+" deleted with successful in the language '"+lang+"'.";
	
				case "fs unused data block":
					
					//Initialization
					int limit = 0;
					int fileId = 0;
		
					//Check if the file id is valid
					try {
		
						fileId = Integer.parseInt(inputVector.get(4).value);
		
						if (fileId<0) {
		
							throw new Exception("err");
		
						}
		
					} catch (Exception e) {
		
						throw new Exception("Sorry, the file id is not valid.");
		
					}
		
					//Check if the limit is valid
					try {
		
						limit = Integer.parseInt(inputVector.get(5).value);
		
						if (limit<0) {
		
							throw new Exception("err");
		
						}
		
					} catch (Exception e) {
		
						throw new Exception("Sorry, the limit is not valid.");
		
					}
		
					return UnusedFile.unused_list(DataFilePool.dfp_get(fileId), limit);
		
				case "group is granted user":
					
					String login = inputVector.get(4).value;
					String groupName = inputVector.get(5).value;
					
					if (GroupManager.isGrantedUser(session.idConnection, groupName, login)) return "1";
					return "0";
	
				case "group is granted script":
					
					String scriptId = inputVector.get(4).value;
					groupName = inputVector.get(5).value;
					
					if (GroupManager.isGrantedScript(session.idConnection, groupName, scriptId)) return "1";
					else return "0";
	
				default:
					
					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "cluster node expels":
						
						//Get params
						String cluster_id = inputVector.get(3).value;
						String node_id = inputVector.get(4).value;
						String error = inputVector.get(5).value;
						
						ClusterManager.node_expels(session.idConnection, cluster_id, node_id, error);
						
						return "1";

					case "app vhost exist":
						
						//Get params
						String protocol = inputVector.get(3).value;
						String context = inputVector.get(4).value;
						String hostname = inputVector.get(5).value;
						
						if (VHostManager.exist(session.idConnection, protocol, hostname, context)) return "1";
						else return "0";

					case "app vhost add":
						
						//Get params
						protocol = inputVector.get(3).value;
						context = inputVector.get(4).value;
						hostname = inputVector.get(5).value;
						
						VHostManager.add(session.idConnection, protocol, hostname, context);
						
						return "1";
						
					case "app vhost delete":
						
						//Get params
						protocol = inputVector.get(3).value;
						context = inputVector.get(4).value;
						hostname = inputVector.get(5).value;
						
						VHostManager.remove(session.idConnection, protocol, hostname, context);
						
						return "1";

					case "user force insert": case "user force create": case "user force add": 
						
						//Get parameters
						login = inputVector.get(3).value;
						String password = inputVector.get(4).value;
						String separator = inputVector.get(5).value;
						
						UserManager.create(session.idConnection, login, separator, password, true);

						return "User "+login+" created with successful.";

					default: 
					
						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "word search":
							
							//Get string
							String symbolChars = inputVector.get(2).value;
							String regex = inputVector.get(3).value;
							String order = inputVector.get(4).value;
							lang = inputVector.get(5).value;
							int iorder = 0;
							
							LanguageManager.exceptionIfNotExist(lang);
							
							if (order.toLowerCase().equals("desc")) {
								
								iorder = 2;
								
							} else if (order.toLowerCase().equals("asc")) {
								
								iorder = 1;
								
							} else if (order.toLowerCase().equals("top")) {
								
								iorder = 0;
								
							} else {
								
								throw new Exception("Sorry, the order field is not valid (asc|desc|top).");
								
							}
	
							return WordManager.search(session.idConnection, symbolChars, regex, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder, lang).toJSONString();
	
						case "word insert": case "word create": case "word add": 
							
							//Get key
							String key = inputVector.get(2).value;
							lang = inputVector.get(3).value;
							separator = inputVector.get(4).value;
							String lock_translation = inputVector.get(5).value;
							
							LanguageManager.exceptionIfNotExist(lang);
							
							return WordManager.create(session.idConnection, key, separator, lang, lock_translation, session, env);
		
						case "thought insert": case "thought create":  case "thought add": 
							
							//Get key
							word = inputVector.get(2).value;
							separator = inputVector.get(3).value;
							lang = inputVector.get(4).value;
							lock_translation = inputVector.get(5).value;
							
							LanguageManager.exceptionIfNotExist(lang);
		
							return ThoughtManager.create(session.idConnection, word, separator, lang, lock_translation, session, env);
		
						case "file_watcher start": 
							
							//Get key
							key = inputVector.get(2).value;
							String user = inputVector.get(3).value;
							String directory = inputVector.get(4).value;
							String scriptName = inputVector.get(5).value;
							
							FileFx.start_watch_service(key, user, directory, scriptName);
							
							return "1";
		
						case "app create":
							
							//Get parameters
							String type = inputVector.get(2).value;
							context = inputVector.get(3).value;
							String template = inputVector.get(4).value;
							String version = inputVector.get(5).value;
			
							return AppManager.create_context(session, type, context, template, version);
			
						case "dq analyse":
							
							//Get key, name and value
							String cmId = inputVector.get(2).value;
							String json = inputVector.get(3).value;
							String title = inputVector.get(4).value;
							String query = inputVector.get(5).value;
							
							return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.analyse(env, session, cmId, json, title, query, parent_pid, current_pid).toJSONString();
			
						case "job add": case "job create": case "job insert":
							
							//Get parameters
							String jobId = inputVector.get(2).value;
							scriptName = inputVector.get(3).value;
							String pattern = inputVector.get(4).value;
							String activate = inputVector.get(5).value;
		
							JobManager.add(session.idConnection, jobId, scriptName, pattern, activate, session.user);
							
							return "Job added with successful.";
						
						case "job update":
							
							//Get parameters
							jobId = inputVector.get(2).value;
							scriptName = inputVector.get(3).value;
							pattern = inputVector.get(4).value;
							activate = inputVector.get(5).value;
		
							JobManager.update(session.idConnection, jobId, scriptName, pattern, activate);
							
							return "Job updated with successful.";
						
						case "node iarray":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							String xPath = inputVector.get(3).value;
							String value = inputVector.get(4).value;
							type = inputVector.get(5).value;
				
							NodeManager.ainsert(session.idConnection, key, xPath, value, type);
				
							return "Inserted with successful.";
				
						default:
							
							switch (inputVector.get(0).value) {
							default:
								
								//Script execution
								inputVector.remove(inputVector.size()-1);
								
								return CommandFullAccess.concatOrUnknow(inputVector);
								
							}
							
						}
						
					}
					
				}
				
			}
	
		case 7:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
			default:
				
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				default:
					
					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "cluster signal give":

						String cluster_id = inputVector.get(3).value;
						String node_id = inputVector.get(4).value;
						String signal = inputVector.get(5).value;
						String current_time = inputVector.get(6).value;

						return ClusterManager.signal_give(cluster_id, node_id, signal, current_time);

					case "script set delay":
						
						String scriptName = inputVector.get(3).value;
						String delayValue = inputVector.get(4).value;
						String delayType = inputVector.get(5).value;
						String delayCondition = inputVector.get(6).value;
						
						ScriptManager.set_delay(session, env, session.idConnection, scriptName, delayCondition, delayValue, delayType);
						
						return "1";
		
					default:
					
						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "dq generate":
							
							//Get key, name and value
							String cmId = inputVector.get(2).value;
							String tablename = inputVector.get(3).value;
							String fieldname = inputVector.get(4).value;
							String jsonArrayAlgoId = inputVector.get(5).value;
							String sql = inputVector.get(6).value;
							
							return DQManager.generate(session.idConnection, cmId, tablename, fieldname, jsonArrayAlgoId, sql);
			
						case "node uarray":
							
							//Get key, name and value
							String key = inputVector.get(2).value;
							String xPath = inputVector.get(3).value;
							String index = inputVector.get(4).value;
							String value = inputVector.get(5).value;
							String type = inputVector.get(6).value;
							
							NodeManager.aupdate(session.idConnection, key, xPath, index, value, type);
			
							return "Updated with successful.";
			
						case "node uobject":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;
							String fieldName = inputVector.get(4).value;
							value = inputVector.get(5).value;
							type = inputVector.get(6).value;
			
							NodeManager.oupdate(session.idConnection, key, xPath, fieldName, value, type);
			
							return "Updated with successful.";
			
						case "node iobject":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;
							fieldName = inputVector.get(4).value;
							value = inputVector.get(5).value;
							type = inputVector.get(6).value;
			
							NodeManager.oinsert(session.idConnection, key, xPath, fieldName, value, type);
			
							return "Inserted with successful.";
			
						case "node iarray":
							
							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;
							index = inputVector.get(4).value;
							value = inputVector.get(5).value;
							type = inputVector.get(6).value;
				
							NodeManager.ainsert(session.idConnection, key, xPath, index, value, type);
							
							return "Inserted with successful.";
				
						default:
							
							//Script execution
							inputVector.remove(inputVector.size()-1);
							
							return CommandFullAccess.concatOrUnknow(inputVector);
							
						}
						
					}
					
				}
				
			}
			
		case 8:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
			case "thought delete by word lang":
				
				//Get key
				String word = inputVector.get(5).value;
				String lang = inputVector.get(6).value;
				String thoughtId = inputVector.get(7).value;
				
				LanguageManager.exceptionIfNotExist(lang);
	
				ThoughtManager.delete(session.idConnection, word, thoughtId, lang);
	
				return "Thought "+thoughtId+" deleted with successful for the word '"+word+"' and the language '"+lang+"'.";
	
			default:
				
				//Script execution
				inputVector.remove(inputVector.size()-1);
				
				return CommandFullAccess.concatOrUnknow(inputVector);
				
			}
			
		case 9:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
			case "dq analyse show":
				
				//Get key, name and value
				String cmId = inputVector.get(3).value;
				String json = inputVector.get(4).value;
				String algokey = inputVector.get(5).value;
				String fieldkey = inputVector.get(6).value;
				String title = inputVector.get(7).value;
				String query = inputVector.get(8).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.analyse_show(env, session, cmId, json, algokey, fieldkey, title, query, parent_pid, current_pid).toJSONString();

			default:
			
				switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
				case "script update":
					
					String scriptName = inputVector.get(2).value;
					String activateLog = inputVector.get(3).value;
					String nbAttempt = inputVector.get(4).value;
					String variables = inputVector.get(5).value;
					String desc = inputVector.get(6).value;
					String mql = inputVector.get(7).value;
					String example = inputVector.get(8).value;
					
					ScriptManager.update(env, session.idConnection, scriptName, variables, mql, desc, example, activateLog, nbAttempt, parent_pid);
					
					return "Script updated with successful.";
				
				default:
				
					//Script execution
					inputVector.remove(inputVector.size()-1);
			
					return CommandFullAccess.concatOrUnknow(inputVector);
					
				}
				
			}
		
		case 10:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
			default:
			
				switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
				case "script add": case "script create": case "script insert":

					String method = inputVector.get(2).value;
					String scriptName = inputVector.get(3).value;
					String activateLog = inputVector.get(4).value;
					String nbAttempt = inputVector.get(5).value;
					String variables = inputVector.get(6).value;
					String desc = inputVector.get(7).value;
					String mql = inputVector.get(8).value;
					String example = inputVector.get(9).value;
					
					ScriptManager.add(env, session.idConnection, method, scriptName, variables, mql, desc, example, activateLog, nbAttempt, parent_pid);
					
					return "Script added with successful.";

				default:
				
					//Script execution
					inputVector.remove(inputVector.size()-1);

					return CommandFullAccess.concatOrUnknow(inputVector);
					
				}
				
			}
		
		case 13:
			
			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
			case "cluster signal set":
				
				//Get params
				String cluster_id = inputVector.get(3).value;
				String node_id = inputVector.get(4).value;
				String hostname = inputVector.get(5).value;
				String port = inputVector.get(6).value;
				String user = inputVector.get(7).value;
				String user_key = inputVector.get(8).value;
				String password = inputVector.get(9).value;
				String connectTimeout = inputVector.get(10).value;
				String readTimeout = inputVector.get(11).value;
				String mql_signal = inputVector.get(12).value;
				
				ClusterManager.signal_set(session.idConnection, cluster_id, node_id, hostname, port, user, user_key, password, connectTimeout, readTimeout, mql_signal);

				return "Signal added/updated with successful.";
				
			case "cluster node set":
				
				//Get params
				cluster_id = inputVector.get(3).value;
				node_id = inputVector.get(4).value;
				hostname = inputVector.get(5).value;
				port = inputVector.get(6).value;
				user = inputVector.get(7).value;
				user_key = inputVector.get(8).value;
				password = inputVector.get(9).value;
				connectTimeout = inputVector.get(10).value;
				readTimeout = inputVector.get(11).value;
				String active_signal = inputVector.get(12).value;
				
				ClusterManager.node_set(session.idConnection, cluster_id, node_id, hostname, port, user, user_key, password, connectTimeout, readTimeout, active_signal);
				
				return "Node added/updated with successful.";

			default:
				
				//Script execution
				inputVector.remove(inputVector.size()-1);

				return CommandFullAccess.concatOrUnknow(inputVector);
				
			}
		
		default:
	
			//Script execution
			inputVector.remove(inputVector.size()-1);
			
			return CommandFullAccess.concatOrUnknow(inputVector);
	
		}
	
	}

}
