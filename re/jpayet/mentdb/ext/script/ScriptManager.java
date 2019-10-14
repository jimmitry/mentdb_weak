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

package re.jpayet.mentdb.ext.script;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.URIUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;

//The script class
public class ScriptManager {
	
	//The constructor
	public ScriptManager() {
		
	}

	//Get a new token
	public static String get_token(long sessionId, String x_user, String x_password, int timeout) throws Exception {
		
		if (x_user==null || x_user.equals("")) {
			throw new Exception("401-The user is required.");
		}

		if (x_password==null || x_password.equals("")) {
			throw new Exception("401-The password is required for the user '"+x_user+"'.");
		}

		if (Record.getNode(sessionId, "U["+x_user.toLowerCase()+"]")==null) {
			throw new Exception("401-The user '"+x_user+"' does not exist.");
		}

		if (!UserManager.checkPassword(sessionId, x_user, x_password)) {
			throw new Exception("401-Bad password for the user '"+x_user+"'.");
		}
		
		if (!x_user.equals("mentdb") && 
				!GroupManager.isGrantedUser(sessionId, "api-rest", x_user) && 
				!GroupManager.isGrantedUser(sessionId, "sys", x_user)) {
			
			throw new Exception("403-Sorry, '"+x_user+"' is not in 'api-rest' group (REST API)."); 
			
		}
		
		Date d = new Date(System.currentTimeMillis() + timeout);
		
		JSONObject groups = GroupManager.userGetGroup(sessionId, x_user);
		String key = Database.execute_admin_mql(null, "user secret_key \""+x_user.replace("\"", "\\\"")+"\";");
		
		String result = Jwts.builder()
				.setSubject(x_user+"/token")
				.setExpiration(d)
				.claim("scope", groups.toJSONString())
				.claim("login", x_user)
				.signWith(
						SignatureAlgorithm.HS256,
						key.getBytes("UTF-8")
						)
						.compact();

		return result;

	}

	//Check the token and return groups
	public static String check_token_return_claims(String token) throws Exception {

		JSONObject o = (JSONObject) JsonManager.load(StringFx.decode_b64(Misc.atom(token, 2, ".")));
		String user = (String) o.get("login");
		String key = Database.execute_admin_mql(null, "user secret_key \""+user.replace("\"", "\\\"")+"\";");
		
		Jwts.parser()
                .setSigningKey(key.getBytes("UTF-8"))
                .parseClaimsJws(token);
		
		return user;

	}
	
	//This function is used by the JSP page for execute a specific container
	@SuppressWarnings("unchecked")
	public static String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//Initialization
		Date d1 = new Date();
		String result = "";
		
		String user = "";
		
		SessionThread thread = new SessionThread(null);
		
		String json = CommandSyncAccess.execute(0, thread, request, response, 2, null, null, null, null, null, null);
		JSONObject rObj = (JSONObject) JsonManager.load(json);
		
		String mentalScript = rObj.get("mentalScript")+"";
		
		user = rObj.get("user")+"";
		String token = rObj.get("token")+"";
		long sessionId = thread.idConnection;
		
		SessionThreadAgent agent = new SessionThreadAgent(thread);
		SessionThreadAgent.allServerThread.put(sessionId, agent);
		
		try {
			
			result = CommandManager.executeAllCommands(true, thread, Misc.splitCommand(mentalScript), thread.env, null, null);
			
			JSONObject valid = new JSONObject();
			valid.put("MentDB", Start.version);
			valid.put("status", "OK");
			valid.put("session", sessionId+"");
			Date d2 = new Date();
			long seconds = (d2.getTime()-d1.getTime())/1000;
			valid.put("time", seconds+"s");
			valid.put("user", user);
			valid.put("timestamp", DateFx.current_timestamp());
			valid.put("token", token);
			JSONParser jsonParser = new JSONParser();
			
			try {
				
				Object r = jsonParser.parse(result);
				
				if(r instanceof JSONObject) {
					
					valid.put("result", (JSONObject) r);
					
				} else if(r instanceof JSONArray) {
					
					valid.put("result", (JSONArray) r);
					
				} else valid.put("result", result);
				
			} catch (Exception a) {
				
				valid.put("result", result);
				
			}
			
			return JsonFormatter.format(valid.toJSONString());
			
		} catch (Exception e) {
			
			throw e;
			
		} finally {
			
			if (sessionId>0) {
				
				try {
					SessionThread.closeSession(thread.env, sessionId);
				} catch (Exception e) {}
				try {
					SessionThreadAgent.allServerThread.remove(sessionId);
				} catch (Exception e) {}
				
			}
			
		}
		
	}
	
	//This function is used by script engine to initialize the parameters
	@SuppressWarnings("unchecked")
	public static JSONObject var(String name, String type, String isNull, String isEmpty, String desc, String example) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();

		result.put("name", name);
		result.put("desc", desc);
		result.put("example", example);
		result.put("type", type);
		if (isNull.equals("is_null:true") || isNull.equals("true") || isNull.equals("1")) result.put("isNull", "1");
		else result.put("isNull", "0");
		if (isEmpty.equals("is_empty:true") || isEmpty.equals("true") || isEmpty.equals("1")) result.put("isEmpty", "1");
		else result.put("isEmpty", "0");
		
		return result;
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray param(Vector<MQLValue> parameters) throws Exception {
		//Initialization
		JSONArray result = new JSONArray();
		
		//Delete the first and the last
		parameters.remove(0);
		parameters.remove(parameters.size()-1);
		
		//Concat all parameters
		for(int i=0;i<parameters.size();i++) {
			
			Vector<Vector<MQLValue>> v = Misc.splitCommand(parameters.get(i).value);
			
			result.add(
				var(v.get(0).get(1).value, 
				v.get(0).get(2).value, 
				v.get(0).get(4).value, 
				v.get(0).get(5).value, 
				v.get(0).get(3).value, 
				v.get(0).get(6).value));

		}
		
		return result;

	}

	//Generate an URL to execute a mental script
	public static String generateUrl(long sessionId, EnvManager env, String scriptName, String user, String parent_pid) throws Exception {
		
		String result = "Method\n";
		if (scriptName.endsWith(".post")) {
			result += "  post";
		} else if (scriptName.endsWith(".get")) {
			result += "  get";
		} else if (scriptName.endsWith(".put")) {
			result += "  put";
		} else if (scriptName.endsWith(".delete")) {
			result += "  delete";
		}
		
		result += "\nHeader\n  x-token = (empty or not define the first time)";
		String script = "";
		if (scriptName.endsWith(".post")) {
			script = scriptName.substring(0, scriptName.length()-5);
		} else if (scriptName.endsWith(".get")) {
			script = scriptName.substring(0, scriptName.length()-4);
		} else if (scriptName.endsWith(".put")) {
			script = scriptName.substring(0, scriptName.length()-4);
		} else if (scriptName.endsWith(".delete")) {
			script = scriptName.substring(0, scriptName.length()-7);
		}
		
		result += "\n  x-user = "+user+" (required the first time)";
		result += "\n  x-password = ***** (required the first time)";
		result += "\nURL\n  https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/api/"+script.replace(".", "/")+"?x-token=&x-user=&x-password=";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);
			
			String parameterName = Misc.lrtrim(""+oVar.get("name"));
			String param = parameterName.substring(1, parameterName.length()-1);

			result += "&"+URIUtil.encodePath(param)+"=";
			
		}
		
		return result;
		
	}

	//Generate stack script
	public static String generateStack(long sessionId, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		String result = "stack (date now) \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);

			String parameterName = Misc.lrtrim(""+oVar.get("name"));
			String example = Misc.lrtrim(""+oVar.get("example"));

			result += "\n\t\""+parameterName+"\" \""+example.replace("\"", "\\\"")+"\"";
			
		}
		
		if (variables.size()>0) return result+"\n;";
		else return result+";";
		
	}

	//Generate execute script
	public static String generateExecute(long sessionId, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		String result = "execute \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);

			String parameterName = Misc.lrtrim(""+oVar.get("name"));
			String example = Misc.lrtrim(""+oVar.get("example"));

			result += "\n\t\""+parameterName+"\" \""+example.replace("\"", "\\\"")+"\"";
			
		}
		
		if (variables.size()>0) return result+"\n;";
		else return result+";";
		
	}

	//Generate call script
	public static String generateCall(long sessionId, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		String result = "call \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);
			
			String parameterName = Misc.lrtrim(""+oVar.get("name"));
			String example = Misc.lrtrim(""+oVar.get("example"));

			result += "\n\t\""+parameterName+"\" \""+example.replace("\"", "\\\"")+"\"";
			
		}
		
		if (variables.size()>0) return result+"\n;";
		else return result+";";
		
	}

	//Generate include script
	public static String generateInclude(long sessionId, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		String result = "include \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);
			
			String parameterName = Misc.lrtrim(""+oVar.get("name"));
			String example = Misc.lrtrim(""+oVar.get("example"));

			result += "\n\t\""+parameterName+"\" \""+example.replace("\"", "\\\"")+"\"";
			
		}
		
		if (variables.size()>0) return result+"\n;";
		else return result+";";
		
	}

	//Generate sub-include script
	public static String generateSubInclude(long sessionId, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		String result = "concat \"include \\\""+scriptName.replace("\"", "\\\"")+"\\\"";
		
		String allArguments = ScriptManager.get(sessionId, scriptName).get("vars")+"";
		Vector<Vector<MQLValue>> v = Misc.splitCommand(allArguments);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Set all variables
		for(int i=1;i<=variables.size();i++) {
			
			JSONObject oVar = (JSONObject) variables.get(i-1);
			
			String parameterName = Misc.lrtrim(""+oVar.get("name"));

			result += "\n\t\\\""+parameterName+"\\\" \\\"\" (mql encode "+parameterName+") \"\\\"";
			
		}
		
		if (variables.size()>0) return result+"\n;\"";
		else return result+";\"";
		
	}
	
	//Initialize the script manager
	public static void init(long sessionId) throws Exception {

		JSONObject dataNode = new JSONObject();
		
		Record.add(sessionId, "SCRIPT[]", dataNode.toJSONString());

	}

	//Check if a script already exist
	public static boolean exist(long sessionId, String scriptName) throws Exception {
		
		//Get the SCRIPT object
		JSONObject bd = Record.getNode(sessionId, "SCRIPT[]");
		
		scriptName = scriptName.replace("[", "").replace("]", "");
		
		return bd.containsKey(scriptName);
		
	}

	//Get the MQL script
	public static JSONObject get(long sessionId, String scriptName) throws Exception {
		
		//Generate an error if the script does not exist
		if (!exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		return Record.getNode(sessionId, "script["+scriptName+"]");
		
	}
	
	//Execute a script
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, boolean closeSessionAfter, String parent_pid, String current_pid) throws Exception {
		
		boolean log = false;
		String scriptName = "", pid = null;
		
		try {
			
			String rStr = CommandSyncAccess.execute(0, null, null, null, 1, session, inputVector, env, parent_pid, null, current_pid);
			
			JSONObject r = (JSONObject) JsonManager.load(rStr);

			String activateLog = r.get("activateLog")+"";
			scriptName = r.get("scriptName")+"";
			
			if (activateLog.equals("1")) {
				
				pid = r.get("pid")+"";
				env.set("[PID]", pid);
				log = true;
								
			}

			Vector<Vector<MQLValue>> mqlParsed = Misc.splitCommand((String) r.get("mql"));
			
			//Execute MQL
			String result = CommandManager.executeAllCommands(false, session, mqlParsed, env, pid, current_pid);
			
			if (log) {
				
				MYSQLManager.addLog(scriptName, parent_pid, pid, "[---- End script ----]", "OK", null, null);
				
			}
			
			return result;
			
		} catch (Exception e) {
			
			if (log) {
				
				MYSQLManager.addLog(scriptName, parent_pid, pid, ""+e.getMessage(), "KO", null, null);
				
			}
			
			throw new Exception(""+e.getMessage());
			
		} finally {
			
			//Close session if 'execute function', not 'call'
			if (closeSessionAfter) {
				try {
					SessionThread.closeEnv(env, session.idConnection);
				} catch (Exception e) {}
			}
			
		}
		
	}
	
	//Check if it is a variable name
	public static boolean is_var(String varName) {
		
		//Test
		if (varName.startsWith("[") && varName.endsWith("]") && varName.length()>2) {

			//Return the boolean
			if (varName.substring(1, varName.length()-1).indexOf("[")>-1 || varName.substring(1, varName.length()-1).indexOf("]")>-1) return false;
			else return true;

		} else {

			//Is not a valid string
			return false;

		}
	
	}
	
	//Delete all
	public static void delete_all(SessionThread session, EnvManager env, long sessionId, String startsWithScriptName) throws Exception {
		
		JSONObject s = (JSONObject) JsonManager.load(ScriptManager.show(sessionId).toJSONString());
		
		for(Object o : s.keySet()) {
			
			String scriptName = (String) o;
			
			if (scriptName.startsWith(startsWithScriptName)) {
				
				ScriptManager.delete(sessionId, scriptName);
				
			}
			
		}
		
	}
	
	//Rename all
	public static void rename_all(SessionThread session, EnvManager env, long sessionId, String startsWithScriptName, String replacement, String parent_pid) throws Exception {
		
		JSONObject s = (JSONObject) JsonManager.load(ScriptManager.show(sessionId).toJSONString());
		
		if (replacement.endsWith(".")) replacement = replacement.substring(0, replacement.length()-1);
		
		for(Object o : s.keySet()) {
			
			String scriptName = (String) o;
			
			if (scriptName.startsWith(startsWithScriptName)) {
				
				if (scriptName.endsWith(".post")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "post", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".put")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "put", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".get")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "get", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".delete")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "delete", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".exe")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "exe", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".conf")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "conf", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".app")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "app", replacement+newstr2, parent_pid);
				}

				ScriptManager.delete(sessionId, scriptName);
				
			}
			
		}
		
	}
	
	//Rename
	public static void rename(EnvManager env, long sessionId, String oldScriptName, String method, String newScriptName, String parent_pid) throws Exception {
		
		copy(env, sessionId, oldScriptName, method, newScriptName, parent_pid);
		
		ScriptManager.delete(sessionId, oldScriptName);
		
	}
	
	//Copy all
	public static void copy_all(EnvManager env, long sessionId, String startsWithScriptName, String replacement, String parent_pid) throws Exception {
		
		JSONObject s = (JSONObject) JsonManager.load(ScriptManager.show(sessionId).toJSONString());
		
		if (replacement.endsWith(".")) replacement = replacement.substring(0, replacement.length()-1);
		
		for(Object o : s.keySet()) {
			
			String scriptName = (String) o;

			if (scriptName.startsWith(startsWithScriptName)) {
				
				if (scriptName.endsWith(".post")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "post", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".put")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "put", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".get")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "get", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".delete")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "delete", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".exe")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "exe", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".conf")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "conf", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".app")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(env, sessionId, scriptName, "app", replacement+newstr2, parent_pid);
				}
				
			}
			
			
		}
		
	}
	
	//Copy
	public static void copy(EnvManager env, long sessionId, String oldScriptName, String method, String newScriptName, String parent_pid) throws Exception {
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (!ScriptManager.isGrantedToUser(sessionId, user, oldScriptName)) {
			
			throw new Exception("Sorry, the script '"+oldScriptName+"' is not granted to the user '"+user+"'."); 
			
		}
		
		//Create the new script
		JSONObject s = ScriptManager.get(sessionId, oldScriptName);
		String mql = (String) s.get("mql");
		JSONObject groups = (JSONObject) JsonManager.load(((JSONObject) s.get("groups")).toJSONString());
		String vars = (String) s.get("vars");
		String returnStr = (String) s.get("return");
		String desc = (String) s.get("desc");
		String activateLog = (String) s.get("activateLog");
		String nbAttempt = (String) s.get("nbAttempt");
		
		ScriptManager.add(env, sessionId, method, newScriptName, 
				vars, mql, desc, returnStr, activateLog, nbAttempt, parent_pid);

		for(Object g : groups.keySet()) {
			
			if (!GroupManager.isGrantedScript(sessionId, (String) g, newScriptName+"."+method.toLowerCase())) {

				try {
					GroupManager.grantScript(sessionId, (String) g, newScriptName+"."+method.toLowerCase(), true);
				} catch (Exception e) {}
			
			}
			
		}
				
	}
	
	//Generate update
	public static String generateUpdate(long sessionId, String scriptName) throws Exception {
		
		//Initialization
		JSONObject script = ScriptManager.get(sessionId, scriptName);
		
		String result = "script update \""+scriptName.replace("\"", "\\\"")+"\" "+((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
		result += "\n  ("+(script.get("vars")+"")+") ";
		result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
		result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
		
		return result;
		
	}
	
	//Generate update delay
	public static String generateUpdateDelay(long sessionId, String scriptName) throws Exception {
		
		//Initialization
		JSONObject script = ScriptManager.get(sessionId, scriptName);
		
		String result = "script set delay \""+scriptName.replace("\"", "\\\"")+"\" "+(script.get("delay_value")+" ")+(script.get("delay_type")+" ");
		result += "{"+(script.get("delay_condition")+"")+"};";
		
		return result;
		
	}
	
	//Generate create
	public static String generateCreate(long sessionId, String scriptName) throws Exception {
		
		//Initialization
		JSONObject script = ScriptManager.get(sessionId, scriptName);
		
		String result = "script create ";
		
		int lastPosition = scriptName.lastIndexOf(".");
		String s = scriptName.substring(0, lastPosition);
		String m = scriptName.substring(lastPosition+1);
		
		result += m+" \""+s.replace("\"", "\\\"")+"\" ";
		
		result += ((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
		result += "\n  ("+(script.get("vars")+"")+") ";
		result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
		result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
		
		return result;
		
	}
	
	//Show all scripts
	public static JSONObject show(long sessionId) throws Exception {
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (GroupManager.isGrantedUser(sessionId, "sys", user)) 
			return Record.getNode(sessionId, "SCRIPT[]");
		else 
			return GroupManager.userGetScript(sessionId, user);
		
	}
	
	//Show all scripts
	@SuppressWarnings("unchecked")
	public static JSONObject showGhost(long sessionId) throws Exception {
		
		JSONObject result = new JSONObject();
		
		JSONObject allScripts = Record.getNode(sessionId, "SCRIPT[]");
		
		//Parse
		for(Object o : allScripts.keySet()) {
			
			String key = (String) o;
			
			if (((JSONObject) ScriptManager.get(sessionId, key).get("groups")).size()==0) {
				
				result.put(key, 0);
				
			}
			
		}
		
		return result;
		
	}
	
	//Export all scripts
	public static void exportAll(long sessionId) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "Export all scripts");
		
		//Get all scripts
		JSONObject scripts = Record.getNode(sessionId, "SCRIPT[]");
		
		//Parse all scripts
		for(Object o:scripts.keySet()) {
			
			String result = "";
			
			//Get the current script name
			String scriptName = (String) o;

			result += generateCreate(sessionId, scriptName)+"\n";
			result += generateUpdateDelay(sessionId, scriptName)+"\n";
			
			if (new File("mql"+File.separator+"scripts"+File.separator+scriptName+".mql").exists()) {

				String md5_1 = StringFx.md5(FileFx.load("mql"+File.separator+"scripts"+File.separator+scriptName+".mql"));
				String md5_2 = StringFx.md5(result);
				
				if (!md5_1.equals(md5_2)) {
					
					Misc.create("mql"+File.separator+"scripts"+File.separator+scriptName+".mql", result);
					
				}
				
			} else Misc.create("mql"+File.separator+"scripts"+File.separator+scriptName+".mql", result);
			
		}
		
		//Delete the file if not exist
		String[] fileList = (new File("mql"+File.separator+"scripts")).list();
		
		for(int i=0;i<fileList.length;i++) {
			
			String f = fileList[i];
			
			if (!ScriptManager.exist(sessionId, f.substring(0, f.length()-4))) {
				
				FileFx.delete("mql"+File.separator+"scripts"+File.separator+f);
				
			}
			
		}
		
	}
	
	//Export all scripts
	public static void exportGroup(long sessionId, String group) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, group, "Export scripts");
		
		//Get all scripts
		JSONObject scripts = GroupManager.getScript(sessionId, group);

		String path_s = "data"+File.separator+"scripts";
		String path_mql = "data"+File.separator+"scripts"+File.separator+group;
		
		if (FileFx.exist(path_s).equals("0")) {
			
			FileFx.mkdir(path_s);
			
		}
		
		if (FileFx.exist(path_mql).equals("0")) {
			
			FileFx.mkdir(path_mql);
			
		}
		
		//Parse all scripts
		for(Object o:scripts.keySet()) {
			
			String result = "";
			
			//Get the current script name
			String scriptName = (String) o;

			result += "if (script exist \""+scriptName+"\";) {script delete \""+scriptName+"\";};\n";
			result += generateCreate(sessionId, scriptName)+"\n";
			result += generateUpdateDelay(sessionId, scriptName)+"\n";
			
			if (new File(path_mql+File.separator+scriptName+".mql").exists()) {

				String md5_1 = StringFx.md5(FileFx.load(path_mql+File.separator+scriptName+".mql"));
				String md5_2 = StringFx.md5(result);
				
				if (!md5_1.equals(md5_2)) {
					
					Misc.create(path_mql+File.separator+scriptName+".mql", result);
					
				}
				
			} else Misc.create(path_mql+File.separator+scriptName+".mql", result);
			
		}
		
		//Delete the file if not exist
		String[] fileList = (new File(path_mql)).list();
		
		for(int i=0;i<fileList.length;i++) {
			
			String f = fileList[i];
			
			if (f.endsWith(".mql")) {

				String scriptName = f.substring(0, f.length()-4);
			
				if (!ScriptManager.exist(sessionId, scriptName) || !GroupManager.isGrantedScript(sessionId, group, scriptName)) {
					
					FileFx.delete(path_mql+File.separator+f);
					
				}
				
			}
			
		}
		
	}
	
	//Import scripts
	@SuppressWarnings("unchecked")
	public static void importGroup(SessionThread session, EnvManager env, long sessionId, String group, String parent_pid, String current_pid) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, group, "Import scripts");
		
		String path_s = "data"+File.separator+"scripts";
		String path_mql = "data"+File.separator+"scripts"+File.separator+group;
		
		if (FileFx.exist(path_s).equals("0")) {
			
			throw new Exception("Sorry, the export for the group '"+group+"' does not exist");
			
		}
		
		if (FileFx.exist(path_mql).equals("0")) {
			
			throw new Exception("Sorry, the export for the group '"+group+"' does not exist");
			
		}
		
		//Import the script
		String[] fileList = (new File(path_mql)).list();
		HashMap<String, String> already = new HashMap<String, String>();
		
		for(int i=0;i<fileList.length;i++) {
			
			String f = fileList[i];
			
			if (f.endsWith(".mql")) {

				String scriptName = f.substring(0, f.length()-4);
				already.put(scriptName, null);
				
				if (ScriptManager.exist(sessionId, scriptName)) {
					
					//Get all groups
					JSONObject g = GroupManager.scriptGetGroup(session.idConnection, scriptName);
					
					g.put(group, 0);
					
					//Create the script
					re.jpayet.mentdb.ext.statement.Statement.eval(session, Misc.load(path_mql+File.separator+f), env, parent_pid, current_pid);
					
					//Give rights to the script
					for(Object o : g.keySet()) {
						
						String cur_g = (String) o;
						
						if (!GroupManager.isGrantedScript(session.idConnection, cur_g, scriptName)) {
							GroupManager.grantScript(session.idConnection, cur_g, scriptName, true);
						}
						
					}
					
				} else {
					
					//Create the script
					re.jpayet.mentdb.ext.statement.Statement.eval(session, Misc.load(path_mql+File.separator+f), env, parent_pid, current_pid);
					
				}
				
			}
			
		}
		
	}
	
	//Get all scripts
	public static String getAllScript(long sessionId) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "Import all scripts");
		
		//Initialization
		String result = "";
		
		//Get all scripts
		JSONObject scripts = Record.getNode(sessionId, "SCRIPT[]");
		
		//Parse all scripts
		for(Object o:scripts.keySet()) {
			
			//Get the current script name
			String scriptName = (String) o;
			
			result += "if (script exist \""+scriptName+"\";) {script delete \""+scriptName+"\";};\n";
			result += generateCreate(sessionId, scriptName)+"\n";
			result += generateUpdateDelay(sessionId, scriptName)+"\n\n";
			
		}
		
		return result;
		
	}

	//Add a script
	@SuppressWarnings("unchecked")
	public static void add(EnvManager env, long sessionId, String method, String scriptName, String varNames, String mql, String desc, String example, String activateLog, String nbAttempt, String parent_pid) throws Exception {
		
		method = method.toLowerCase();
		
		if (activateLog!=null && (activateLog.equals("true") || activateLog.equals("1"))) {
			activateLog = "1";
		} else {
			activateLog = "0";
		}
		
		try {
		
			@SuppressWarnings("unused")
			int i = Integer.parseInt(nbAttempt);
		
		} catch (Exception e) {
		
			throw new Exception("Sorry, the nb attempt must be a number.");
		
		}
		
		//Generate an error if the method is not valid
		if (!method.equals("app") && !method.equals("conf") && !method.equals("exe") && !method.equals("post") && !method.equals("get") && !method.equals("put") && !method.equals("delete")) {

			throw new Exception("Sorry, the method must be 'get|post|put|delete|exe|conf|app'.");

		}
		
		scriptName = scriptName.replace("[", "").replace("]", "")+"."+method;
		
		//Generate an error if the script already exist
		if (exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" already exist.");

		}
		
		Vector<Vector<MQLValue>> v = Misc.splitCommand(varNames);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Check the variable name format
		for(int i=0;i<variables.size();i++) {
			
			JSONObject o = (JSONObject) variables.get(i);
			
			String curVarName = Misc.lrtrim(""+o.get("name"));
			//String curType = Misc.lrtrim(""+o.get("type"));
			//String curDesc = Misc.lrtrim(""+o.get("desc"));
			String curIsNull = Misc.lrtrim(""+o.get("isNull")).toLowerCase();
			String curIsEmpty = Misc.lrtrim(""+o.get("isEmpty")).toLowerCase();
			//String curExample = Misc.lrtrim(""+o.get("example"));
			if (!is_var(curVarName)) {
				throw new Exception("Sorry, "+curVarName+" is not a valid var name (ex: {var}).");
			}
			if (!curIsNull.equals("1") && !curIsNull.equals("0")) {
				throw new Exception("Sorry, isNull must be true|false (found: "+curIsNull+").");
			}
			if (!curIsEmpty.equals("1") && !curIsEmpty.equals("0")) {
				throw new Exception("Sorry, isEmpty must be true|false (found: "+curIsEmpty+").");
			}
		}
		
		//Get the script object
		JSONObject bd = Record.getNode(sessionId, "SCRIPT[]");
		
		JSONObject obj = new JSONObject();
		obj.put("k", scriptName);
		obj.put("vars", varNames);
		obj.put("mql", mql);
		obj.put("desc", desc);
		obj.put("return", example);
		obj.put("activateLog", activateLog);
		obj.put("nbAttempt", nbAttempt);
		obj.put("delay_value", "0");
		obj.put("delay_type", "day");
		obj.put("delay_condition", "1");

		obj.put("groups", new JSONObject());
		
		Record.add(sessionId, "script["+scriptName+"]", obj.toJSONString());
		
		bd.put(scriptName, 0);
		
		Record.update(sessionId, "SCRIPT[]", bd.toJSONString());
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		GroupManager.grantScript(sessionId, user, scriptName, true);
		
	}

	//Update a script
	@SuppressWarnings("unchecked")
	public static void update(EnvManager env, long sessionId, String scriptName, String varNames, String mql, String desc, String example, String activateLog, String nbAttempt, String parent_pid) throws Exception {
		
		//Initialization
		if (activateLog!=null && (activateLog.equals("true") || activateLog.equals("1"))) {
			activateLog = "1";
		} else {
			activateLog = "0";
		}
		
		try {
		
			@SuppressWarnings("unused")
			int i = Integer.parseInt(nbAttempt);
		
		} catch (Exception e) {
		
			throw new Exception("Sorry, the nb attempt must be a number.");
		
		}
		
		//Generate an error if the script does not exist
		if (!exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (!ScriptManager.isGrantedToUser(sessionId, user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+user+"'."); 
			
		}
		
		JSONObject groups = GroupManager.scriptGetGroup(sessionId, scriptName);
		
		Vector<Vector<MQLValue>> v = Misc.splitCommand(varNames);
		JSONArray variables = ScriptManager.param(v.get(0));
		
		//Check the variable name format
		for(int i=0;i<variables.size();i++) {
			
			JSONObject o = (JSONObject) variables.get(i);
			
			String curVarName = Misc.lrtrim(""+o.get("name"));
			//String curType = Misc.lrtrim(""+o.get("type"));
			//String curDesc = Misc.lrtrim(""+o.get("desc"));
			String curIsNull = Misc.lrtrim(""+o.get("isNull")).toLowerCase();
			String curIsEmpty = Misc.lrtrim(""+o.get("isEmpty")).toLowerCase();
			//String curExample = Misc.lrtrim(""+o.get("example"));
			if (!is_var(curVarName)) {
				throw new Exception("Sorry, "+curVarName+" is not a valid var name (ex: {var}).");
			}
			if (!curIsNull.equals("1") && !curIsNull.equals("0")) {
				throw new Exception("Sorry, isNull must be true|false (found: "+curIsNull+").");
			}
			if (!curIsEmpty.equals("1") && !curIsEmpty.equals("0")) {
				throw new Exception("Sorry, isEmpty must be true|false (found: "+curIsEmpty+").");
			}
		}
		
		scriptName = scriptName.replace("[", "").replace("]", "");
		
		//Overwrite the script object
		JSONObject obj = Record.getNode(sessionId, "script["+scriptName+"]");
		obj.put("k", scriptName);
		obj.put("vars", varNames);
		obj.put("mql", mql);
		obj.put("desc", desc);
		obj.put("return", example);
		obj.put("activateLog", activateLog);
		obj.put("nbAttempt", nbAttempt);
		
		obj.put("groups", groups);
		
		Record.update(sessionId, "script["+scriptName+"]", obj.toJSONString());
		
	}

	//Set delay
	@SuppressWarnings("unchecked")
	public static void set_delay(SessionThread session, EnvManager env, long sessionId, String scriptName, String condition, String val, String type) throws Exception {
		
		//Generate an error if the script does not exist
		if (!exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		//Generate an error if the condition|type|value is null
		if (condition==null || type==null || val==null) {

			throw new Exception("Sorry, the delay condition|type|value cannot be null.");

		}
		
		try {
			
			Integer.parseInt(val);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the delay value is not a valid number.");
			
		}
		
		type = type.toLowerCase();
		
		//Generate an error if the type is not valid
		if (!type.equals("sec") && !type.equals("min") && !type.equals("hour")
				 && !type.equals("day") && !type.equals("month") && !type.equals("year")) {

			throw new Exception("Sorry, the the delay type must be sec|min|hour|day|month|year. found("+type+")");

		}
		
		//Get the script object
		JSONObject obj = Record.getNode(sessionId, "script["+scriptName+"]");
		
		obj.put("delay_value", val);
		obj.put("delay_type", type);
		obj.put("delay_condition", condition);
		
		Record.update(sessionId, "script["+scriptName+"]", obj.toJSONString());
		
	}

	//Delete a script
	public static void delete(long sessionId, String scriptName) throws Exception {

		//Generate an error if the script does not exist
		if (!exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;

		if (!ScriptManager.isGrantedToUser(sessionId, user, scriptName)) {

			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+user+"'."); 
			
		}

		scriptName = scriptName.replace("[", "").replace("]", "");
		JSONObject groups = (JSONObject) JsonManager.load(GroupManager.scriptGetGroup(sessionId, scriptName).toJSONString());
		for(Object group : groups.keySet()) {

			if (GroupManager.isGrantedScript(sessionId, (String) group, scriptName)) {
				GroupManager.ungrantScript(sessionId, (String) group, scriptName);
			}
			
		}
		
		//Get the script object
		JSONObject bd = Record.getNode(sessionId, "SCRIPT[]");
		
		bd.remove(scriptName);
		
		Record.update(sessionId, "SCRIPT[]", bd.toJSONString());
		
		Record.remove(sessionId, "script["+scriptName+"]");
		
	}
	
	//Check if a script is granted to a user
	public static boolean isGrantedToUser(long sessionId, String login, String scriptName) throws Exception {
		
		if (login.length()==0) {
			
			throw new Exception("Sorry, the login is required.");
			
		}
		
		if (scriptName.length()==0) {
			
			throw new Exception("Sorry, the script name is required.");
			
		}
		
		if (!UserManager.exist(sessionId, login)) {
			
			throw new Exception("Sorry, the user <"+login+"> does not exist.");
			
		} else if (!ScriptManager.exist(sessionId, scriptName)) {
			
			throw new Exception("Sorry, the script <"+scriptName+"> does not exist.");
			
		} else {
			
			boolean result = false;
			
			//Get all groups
			JSONObject userGroups = GroupManager.userGetGroup(sessionId, login);
			
			if (userGroups.containsKey("sys")) {
			
				return true;
				
			} else {
				
				for (Object userGroup : userGroups.keySet()) {
					String groupName = (String) userGroup;
					
					JSONObject bdGroup = Record.getNode(sessionId, "G["+groupName+"]");
					
					if (((JSONObject) bdGroup.get("users")).containsKey(login) 
							&& ((JSONObject) bdGroup.get("scripts")).containsKey(scriptName)) {
						
						result = true;
						break;
						
					}
					
				}
				
				return result;
				
			}
			
		}
		
	}
	
	//Check if a script is granted to a user
	public static boolean isGrantedToUser(long sessionId, JSONObject userGroups, String scriptName) throws Exception {
		
		boolean result = false;
		
		for (Object userGroup : userGroups.keySet()) {
			String groupName = (String) userGroup;
			
			JSONObject bdGroup = Record.getNode(sessionId, "G["+groupName+"]");
			
			if (((JSONObject) bdGroup.get("scripts")).containsKey(scriptName)) {
				
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}

}
