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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.URIUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;

//The script class
public class ScriptManager {
	
	//The constructor
	public ScriptManager() {
		
	}
	
	//Initialize the script manager
	public static void init() throws Exception {
		
	}
	
	//Show all scripts
	@SuppressWarnings("unchecked")
	public static JSONObject show(long sessionId) throws Exception {
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (GroupManager.isGrantedUser("sys", user)) {
			
			JSONObject result = new JSONObject();
			
			ArrayList<JSONObject> list = Record2.getRows("mql_script");
			
			for(JSONObject script : list) {
				
				result.put((String) script.get("k"), 0);
				
			}
			
			return result;
			
		} else 
			return GroupManager.userGetScript(sessionId, user);
		
	}

	//Check if a script already exist
	public static boolean exist(String scriptName) throws Exception {
		
		if (Record2.getNode("script["+scriptName+"]")==null) {
			
			return false;
			
		} else {
			
			return true;
			
		}
		
	}
	
	//Show all scripts
	@SuppressWarnings("unchecked")
	public static JSONObject showGhost() throws Exception {
		
		JSONObject result = new JSONObject();
		
		ArrayList<JSONObject> list = Record2.getRows("mql_script");
		
		for(JSONObject script : list) {
			
			if (((JSONObject) script.get("groups")).size()==0) {
				result.put((String) script.get("k"), 0);
			}
			
		}
		
		return result;
		
	}
	
	//Export all scripts
	public static void exportAll(SessionThread session, long sessionId) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "Export all scripts");
		
		ArrayList<JSONObject> list = Record2.getRows("mql_script");
		
		for(JSONObject script : list) {
			
			String result = "";
			
			//Get the current script name
			String scriptName = (String) script.get("k");

			result += generateCreate(session, scriptName)+"\n";
			result += generateUpdateDelay(session, scriptName)+"\n";
			
			if (new File("mql"+File.separator+"scripts"+File.separator+scriptName+".mql").exists()) {

				String md5_1 = StringFx.md5(FileFx.load(session.user, "mql"+File.separator+"scripts"+File.separator+scriptName+".mql"));
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
			
			if (!ScriptManager.exist(f.substring(0, f.length()-4))) {
				
				FileFx.delete("mql"+File.separator+"scripts"+File.separator+f);
				
			}
			
		}
		
	}
	
	//Get all scripts
	public static String getAllScript(SessionThread session, long sessionId) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "sys", "Import all scripts");
		
		//Initialization
		String result = "";
		
		ArrayList<JSONObject> list = Record2.getRows("mql_script");
		
		for(JSONObject script : list) {
			
			//Get the current script name
			String scriptName = (String) script.get("k");
			
			result += "if (script exist \""+scriptName+"\";) {script delete \""+scriptName+"\";};\n";
			result += generateCreate(session, scriptName)+"\n";
			result += generateUpdateDelay(session, scriptName)+"\n\n";
			
		}
		
		return result;
		
	}
	
	//Get a new token
	public static String get_token(String x_user, String x_password, int timeout) throws Exception {
		
		if (x_user==null || x_user.equals("")) {
			throw new Exception("401-The user is required.");
		}

		if (x_password==null || x_password.equals("")) {
			throw new Exception("401-The password is required for the user '"+x_user+"'.");
		}

		if (Record2.getNode("U["+x_user.toLowerCase()+"]")==null) {
			throw new Exception("401-The user '"+x_user+"' does not exist.");
		}

		if (!UserManager.checkPassword(x_user, x_password)) {
			throw new Exception("401-Bad password for the user '"+x_user+"'.");
		}
		
		if (!x_user.equals("mentdb") && 
				!GroupManager.isGrantedUser("api-rest", x_user) && 
				!GroupManager.isGrantedUser("sys", x_user)) {
			
			throw new Exception("403-Sorry, '"+x_user+"' is not in 'api-rest' group (REST API)."); 
			
		}
		
		Date d = new Date(System.currentTimeMillis() + timeout);
		
		JSONObject groups = GroupManager.userGetGroup(x_user);
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
	
	//This function is used by the WS engine
	@SuppressWarnings("unchecked")
	public static String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (Start.maintenance_ws) {
			String url = ((HttpServletRequest)request).getRequestURL().toString();
			re.jpayet.mentdb.ext.log.Log.trace("Call WS stopped by maintenance ("+url+").");
			throw new Exception("Sorry, MentDB is in maintenance.");
		}
		
		//Initialization
		Date d1 = new Date();
		String result = "";
		
		String user = "";
		
		SessionThread thread = new SessionThread(null);
		
		thread.env.set("[REQUEST_HOST_ADDRESS]", request.getRemoteAddr());
		thread.env.set("[REQUEST_HOST_NAME]", request.getRemoteHost());
		thread.env.set("[REQUEST_PORT]", ""+request.getRemotePort());
		
		for (Enumeration<?> e = request.getHeaderNames(); e.hasMoreElements();) {
		    String nextHeaderName = (String) e.nextElement();
		    String headerValue = request.getHeader(nextHeaderName);
			thread.env.set("[REQUEST_HEADER_"+(nextHeaderName.toUpperCase().replace(" ", "_"))+"]", headerValue);
		}
		
		try {
			
			String json = CommandSyncAccess.execute(0, thread, request, response, 2, null, null, null, null, null, null);
			JSONObject rObj = (JSONObject) JsonManager.load(json);
			
			String mentalScript = rObj.get("mentalScript")+"";
			
			user = rObj.get("user")+"";
			String token = rObj.get("token")+"";
			
			SessionThreadAgent agent = new SessionThreadAgent(thread, "WS", mentalScript, "");
			SessionThreadAgent.allServerThread.put(thread.idConnection, agent);
			agent.current_function = "WS start";
			
			result = CommandManager.executeAllCommands(thread, Misc.splitCommand(mentalScript), thread.env, null, null);
			
			//Load response headers;
            for(String key: thread.env.varEnv.keySet()) {
            	if (key.startsWith("[RESPONSE_HEADER_")) {
	            	response.setHeader(key.substring(17, key.length()-1), thread.env.get(key));
            	}
            }
            
			SessionThreadAgent.allServerThread.get(thread.idConnection).reset_origin("WS", "", "", result);
			SessionThreadAgent.allServerThread.get(thread.idConnection).close_transaction();
			
			JSONObject valid = new JSONObject();
			valid.put("MentDB", Start.version);
			valid.put("status", "OK");
			valid.put("session", thread.idConnection+"");
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

			agent.current_function = "WS end";
			
			return JsonFormatter.format(valid.toJSONString());
			
		} catch (Exception e) {

			try {thread.sessionThreadAgent.current_function = "WS error > "+e.getMessage();} catch (Exception e1) {}
			
			try {SessionThreadAgent.allServerThread.get(thread.idConnection).reset_origin("WS", "", "", ""+e.getMessage());} catch (Exception f) {Log.trace("WS ERROR1: "+e.getMessage());}
			try {SessionThreadAgent.allServerThread.get(thread.idConnection).close_transaction();} catch (Exception e1) {Log.trace("WS ERROR2: "+e1.getMessage());};
			
			throw e;
			
		} finally {
			
			if (thread.idConnection>0) {
				
				try {
					SessionThread.closeSession(thread.env, thread.idConnection);
				} catch (Exception e) {Log.trace("WS ERROR3: "+e.getMessage());}
				try {
					SessionThreadAgent.allServerThread.remove(thread.idConnection);
				} catch (Exception e) {Log.trace("WS ERROR4: "+e.getMessage());}
				
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
	public static String generateUrl(SessionThread session, EnvManager env, String scriptName, String user, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
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
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
		
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
	public static String generateStack(SessionThread session, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		String result = "stack_execute \""+DateFx.systimestamp()+"\" \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
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
	public static String generateExecute(SessionThread session, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		String result = "execute \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
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
	public static String generateCall(SessionThread session, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		String result = "call \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
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
	public static String generateInclude(SessionThread session, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		String result = "include \""+scriptName.replace("\"", "\\\"")+"\"";
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
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
	public static String generateSubInclude(SessionThread session, EnvManager env, String scriptName, String parent_pid) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		String result = "concat \"include \\\""+scriptName.replace("\"", "\\\"")+"\\\"";
		
		String allArguments = ScriptManager.get(session, scriptName).get("vars")+"";
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

	//Get the MQL script
	public static void reset_cache() throws Exception {
		
		for (Entry<Long, SessionThreadAgent> e : SessionThreadAgent.allServerThread.entrySet()) {

			try {
				e.getValue().serverThread.script_cache = new ConcurrentHashMap<String, JSONObject>();
			} catch (Exception f) {}
			
		}
		
	}

	//Get the MQL script
	public static JSONObject get(SessionThread session, String scriptName) throws Exception {
		
		if (session!=null) {
			
			JSONObject obj = session.script_cache.get(scriptName);
			if (obj==null) {
				
				if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
					
					throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
					
				}
				
				//Generate an error if the script does not exist
				if (!exist(scriptName)) {
		
					throw new Exception("Sorry, the script "+scriptName+" does not exist.");
		
				}
				
				obj = Record2.getNode("script["+scriptName+"]");
				
				if (session.script_cache.size()>Integer.parseInt(ParameterManager.get_value("SESSION_SCRIPT_CACHE"))) {
					session.script_cache = new ConcurrentHashMap<String, JSONObject>();
				}

				session.script_cache.put(scriptName, obj);
				
				return obj;
				
			} else {

				return obj;
				
			}
			
		} else {
			
			//Generate an error if the script does not exist
			if (!exist(scriptName)) {
	
				throw new Exception("Sorry, the script "+scriptName+" does not exist.");
	
			}
			
			return Record2.getNode("script["+scriptName+"]");
		
		}
		
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
			
			String old_current_scriptname = session.current_scriptname;
			session.current_scriptname = scriptName;
			String result = CommandManager.executeAllCommands(session, mqlParsed, env, pid, current_pid);
			session.current_scriptname = old_current_scriptname;
			
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
					ScriptManager.copy(session, env, sessionId, scriptName, "post", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".put")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "put", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".get")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "get", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".delete")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "delete", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".exe")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "exe", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".conf")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "conf", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".app")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "app", replacement+newstr2, parent_pid);
				}

				ScriptManager.delete(sessionId, scriptName);
				
			}
			
		}
		
	}
	
	//Rename
	public static void rename(SessionThread session, EnvManager env, long sessionId, String oldScriptName, String method, String newScriptName, String parent_pid) throws Exception {
		
		copy(session, env, sessionId, oldScriptName, method, newScriptName, parent_pid);
		
		ScriptManager.delete(sessionId, oldScriptName);
		
	}
	
	//Copy all
	public static void copy_all(SessionThread session, EnvManager env, long sessionId, String startsWithScriptName, String replacement, String parent_pid) throws Exception {
		
		JSONObject s = (JSONObject) JsonManager.load(ScriptManager.show(sessionId).toJSONString());
		
		if (replacement.endsWith(".")) replacement = replacement.substring(0, replacement.length()-1);
		
		for(Object o : s.keySet()) {
			
			String scriptName = (String) o;

			if (scriptName.startsWith(startsWithScriptName)) {
				
				if (scriptName.endsWith(".post")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "post", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".put")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "put", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".get")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "get", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".delete")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "delete", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".exe")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "exe", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".conf")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "conf", replacement+newstr2, parent_pid);
				} else if (scriptName.endsWith(".app")) {
					String newstr = scriptName.substring(0, scriptName.lastIndexOf("."));
					String newstr2 = "";
					try {newstr2 = newstr.substring(startsWithScriptName.length(), newstr.length());} catch (Exception f) {};
					ScriptManager.copy(session, env, sessionId, scriptName, "app", replacement+newstr2, parent_pid);
				}
				
			}
			
			
		}
		
	}
	
	//Copy
	public static void copy(SessionThread session, EnvManager env, long sessionId, String oldScriptName, String method, String newScriptName, String parent_pid) throws Exception {
		
		String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		
		if (!ScriptManager.isGrantedToUser(user, oldScriptName)) {
			
			throw new Exception("Sorry, the script '"+oldScriptName+"' is not granted to the user '"+user+"'."); 
			
		}
		
		//Create the new script
		JSONObject s = ScriptManager.get(session, oldScriptName);
		String mql = (String) s.get("mql");
		JSONObject groups = (JSONObject) JsonManager.load(((JSONObject) s.get("groups")).toJSONString());
		String vars = (String) s.get("vars");
		String returnStr = (String) s.get("return");
		String desc = (String) s.get("desc");
		String activateLog = (String) s.get("activateLog");
		String nbAttempt = (String) s.get("nbAttempt");
		
		ScriptManager.add(env, session, method, newScriptName, 
				vars, mql, desc, returnStr, activateLog, nbAttempt, parent_pid);

		for(Object g : groups.keySet()) {
			
			if (!GroupManager.isGrantedScript((String) g, newScriptName+"."+method.toLowerCase())) {

				try {
					GroupManager.grantScript(sessionId, (String) g, newScriptName+"."+method.toLowerCase(), true);
				} catch (Exception e) {}
			
			}
			
		}
				
	}
	
	//Generate update
	public static String generateUpdate(SessionThread session, String scriptName) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		//Initialization
		JSONObject script = ScriptManager.get(session, scriptName);
		
		String result = "script update \""+scriptName.replace("\"", "\\\"")+"\" "+((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
		result += "\n  ("+(script.get("vars")+"")+") ";
		result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
		result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
		
		return result;
		
	}
	
	//Generate update
	public static String generateMerge(SessionThread session, String scriptName) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		//Initialization
		JSONObject script = ScriptManager.get(session, scriptName);
		
		String result = "script merge \""+scriptName.replace("\"", "\\\"")+"\" "+((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
		result += "\n  ("+(script.get("vars")+"")+") ";
		result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
		result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
		
		return result;
		
	}
	
	//Generate update delay
	public static String generateUpdateDelay(SessionThread session, String scriptName) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		//Initialization
		JSONObject script = ScriptManager.get(session, scriptName);
		
		String result = "script set delay \""+scriptName.replace("\"", "\\\"")+"\" "+(script.get("delay_value")+" ")+(script.get("delay_type")+" ");
		result += "{"+(script.get("delay_condition")+"")+"};";
		
		return result;
		
	}
	
	//Generate create
	public static String generateCreate(SessionThread session, String scriptName) throws Exception {
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		//Initialization
		JSONObject script = ScriptManager.get(session, scriptName);
		
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
	
	//Export all scripts
	@SuppressWarnings("unchecked")
	public static void exportGroup(SessionThread session, long sessionId, String group) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, group, "Export scripts");
		
		JSONArray res = new JSONArray();
		
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
			
			res.add(scriptName);
			
			result += "if (script exist \""+scriptName+"\";) {script delete \""+scriptName+"\";};\n";
			result += generateCreate(session, scriptName)+"\n";
			result += generateUpdateDelay(session, scriptName)+"\n";
			
			if (new File(path_mql+File.separator+scriptName+".mql").exists()) {

				String md5_1 = StringFx.md5(FileFx.load(session.user, path_mql+File.separator+scriptName+".mql"));
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
			
				if (!ScriptManager.exist(scriptName) || !GroupManager.isGrantedScript(group, scriptName)) {
					
					FileFx.delete(path_mql+File.separator+f);
					
				}
				
			}
			
		}
		
		Misc.create(path_mql+File.separator+"scripts.json", res.toJSONString());
		
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
				
				if (ScriptManager.exist(scriptName)) {
					
					//Get all groups
					JSONObject g = GroupManager.scriptGetGroup(scriptName);
					
					g.put(group, 0);
					
					//Create the script
					re.jpayet.mentdb.ext.statement.Statement.eval(session, Misc.load(path_mql+File.separator+f), env, parent_pid, current_pid);
					
					//Give rights to the script
					for(Object o : g.keySet()) {
						
						String cur_g = (String) o;
						
						if (!GroupManager.isGrantedScript(cur_g, scriptName)) {
							GroupManager.grantScript(sessionId, cur_g, scriptName, true);
						}
						
					}
					
				} else {
					
					//Create the script
					re.jpayet.mentdb.ext.statement.Statement.eval(session, Misc.load(path_mql+File.separator+f), env, parent_pid, current_pid);
					
				}
				
			}
			
		}
		
	}
	
	public static String reload_commit_scripts(String user, long sessionId, String group, String commit_id) throws Exception {
		
		String path_group = "commits"+File.separator+group;
		String path_scripts = path_group+File.separator+"scripts";
		String path_ids = path_group+File.separator+"ids";
		
		if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
		if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
		if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
		
		if (FileFx.exist(path_ids+File.separator+commit_id).equals("0")) {
			throw new Exception("Sorry, the commit id '"+commit_id+"' does not exist.");
		}
		
		String[] dir_list = (new File(path_ids+File.separator+commit_id)).list();
		Arrays.sort(dir_list);
		
		StringBuilder result = new StringBuilder("");
		for(int i=0;i<dir_list.length;i++) {
			
			String scriptname = dir_list[i];
			
			if (scriptname.endsWith(".mql")) {
			
				result.append(FileFx.load(user, path_ids+File.separator+commit_id+File.separator+scriptname)+"\n\n");
			
			}
			
		}
		
		return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+result.toString();
		
	}
	
	public static String show_mql(String user, long sessionId, String group, String commit_id, String scriptname) throws Exception {
		
		String path_group = "commits"+File.separator+group;
		String path_scripts = path_group+File.separator+"scripts";
		String path_ids = path_group+File.separator+"ids";
		
		if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
		if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
		if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
		
		if (FileFx.exist(path_ids+File.separator+commit_id).equals("0")) {
			throw new Exception("Sorry, the commit id '"+commit_id+"' does not exist.");
		}
		
		if (FileFx.exist(path_ids+File.separator+commit_id+File.separator+scriptname).equals("0")) {
			throw new Exception("Sorry, the script '"+scriptname+"' does not exist into the commit.");
		}
		
		return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+FileFx.load(user, path_ids+File.separator+commit_id+File.separator+scriptname);
		
	}
	
	public static String show_commit_scripts(long sessionId, String group, String commit_id) throws Exception {
		
		String path_group = "commits"+File.separator+group;
		String path_scripts = path_group+File.separator+"scripts";
		String path_ids = path_group+File.separator+"ids";
		
		if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
		if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
		if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
		
		if (FileFx.exist(path_ids+File.separator+commit_id).equals("0")) {
			throw new Exception("Sorry, the commit id '"+commit_id+"' does not exist.");
		}
		
		String[] dir_list = (new File(path_ids+File.separator+commit_id)).list();
		Arrays.sort(dir_list);
		
		StringBuilder result = new StringBuilder("<"+commit_id+">\n");
		for(int i=0;i<dir_list.length;i++) {
			
			String scriptname = dir_list[i];
			
			if (scriptname.endsWith(".mql")) {
			
				result.append("version show_script "+group+" "+commit_id+" "+scriptname+";\n");
			
			}
			
		}
		
		return result.toString();
		
	}
	
	//Export all scripts
	public static String show_all_commits(String userSession, long sessionId, String group, String filter, String nb_row) throws Exception {
		
		String path_group = "commits"+File.separator+group;
		String path_scripts = path_group+File.separator+"scripts";
		String path_ids = path_group+File.separator+"ids";
		
		int nb = Integer.parseInt(nb_row);
		
		filter = Misc.lrtrim(filter);

		if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
		if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
		if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
		
		String[] dir_list = (new File(path_ids)).list();
		Arrays.sort(dir_list, Collections.reverseOrder());
		
		StringBuilder result = new StringBuilder("COMMITS\n");
		for(int i=0;i<dir_list.length && i<nb;i++) {
			
			String commit_id = dir_list[i];
			
			if (commit_id.endsWith("_CMT")) {
			
				JSONObject commit = (JSONObject) JsonManager.load(FileFx.load(userSession, path_ids+File.separator+commit_id+File.separator+"commit.json"));
				
				String user = (String) commit.get("___sys_user___");
				String message = (String) commit.get("___sys_message___");
				
				if (StringFx.matches(message, filter).equals("1")) {

					result.append("<"+commit_id+">\n");
					result.append("      "+user+" - "+message+"\n");
					result.append("      version show_scriptnames "+group+" "+commit_id+";\n");
					result.append("      version show_scripts "+group+" "+commit_id+";\n");
					
				}
			
			}
			
		}
		
		return result.toString();
		
	}
	
	//Export all scripts
	public static String commitCheck(SessionThread session, long sessionId, String group) throws Exception {
		
		HashMap<String, String> resultScripts = new HashMap<String, String>();
		GroupManager.generateErrorIfNotGranted(sessionId, group, "Versioning");
		
		//Get all scripts
		JSONObject scripts = GroupManager.getScript(sessionId, group);

		String path_group = "commits"+File.separator+group;
		String path_scripts = path_group+File.separator+"scripts";
		String path_ids = path_group+File.separator+"ids";

		if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
		if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
		if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
		
		//Parse all scripts
		for(Object o:scripts.keySet()) {
			
			String result = "";
			
			//Get the current script name
			String scriptName = (String) o;
			
			result += generateMerge(session, scriptName)+"\n";
			result += generateUpdateDelay(session, scriptName)+"\n";
			
			if (new File(path_scripts+File.separator+scriptName+".mql").exists()) {

				String md5_1 = StringFx.md5(FileFx.load(session.user, path_scripts+File.separator+scriptName+".mql"));
				String md5_2 = StringFx.md5(result);
				
				if (!md5_1.equals(md5_2)) {
					
					resultScripts.put(scriptName, "U");
					
				} else {
					//resultScripts.put(scriptName, "N");
				}
				
			} else {
				resultScripts.put(scriptName, "I");
			}
			
		}
		
		//Delete the file if not exist
		String[] fileList = (new File(path_scripts)).list();
		
		for(int i=0;i<fileList.length;i++) {
			
			String f = fileList[i];
			
			if (f.endsWith(".mql")) {

				String scriptName = f.substring(0, f.length()-4);
			
				if (!ScriptManager.exist(scriptName) || !GroupManager.isGrantedScript(group, scriptName)) {

					resultScripts.put(scriptName, "D");
					
				}
				
			}
			
		}
		
		TreeMap<String, String> sorted = new TreeMap<>(); 
        sorted.putAll(resultScripts); 
        
        String result = "STATE : SCRIPT\n";
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
        		result+=entry.getValue()+" : "+entry.getKey()+"\n";
        }
        
		return result;
		
	}
	
	//Export all scripts
	@SuppressWarnings("unchecked")
	public static String commitGroup(SessionThread session, String user, long sessionId, String group, String message) throws Exception {
		
		String path_mql = null;
		
		try {
		
			String dt = DateFx.systimestamp_min();
			String commit_id = dt+"_"+StringFx.generate_random_str("6")+"_"+StringFx.generate_random_str("6")+"_CMT";
			JSONObject resultJson = new JSONObject();
			JSONObject resultScripts = new JSONObject();
			resultJson.put("___sys_date___", dt);
			resultJson.put("___sys_commit_id___", commit_id);
			resultJson.put("___sys_user___", user);
			resultJson.put("___sys_message___", message);
			resultJson.put("___sys_script___", resultScripts);
			GroupManager.generateErrorIfNotGranted(sessionId, group, "Versioning");
			
			//Get all scripts
			JSONObject scripts = GroupManager.getScript(sessionId, group);
	
			String path_group = "commits"+File.separator+group;
			String path_scripts = path_group+File.separator+"scripts";
			String path_ids = path_group+File.separator+"ids";
			path_mql = path_ids+File.separator+commit_id;
	
			if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
			if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
			if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
			if (FileFx.exist(path_mql).equals("0")) {FileFx.mkdir(path_mql);}
			
			int nb_change = 0;
			
			//Parse all scripts
			for(Object o:scripts.keySet()) {
				
				String result = "";
				
				//Get the current script name
				String scriptName = (String) o;
				
				result += generateMerge(session, scriptName)+"\n";
				result += generateUpdateDelay(session, scriptName)+"\n";
				
				if (new File(path_scripts+File.separator+scriptName+".mql").exists()) {
	
					String md5_1 = StringFx.md5(FileFx.load(session.user, path_scripts+File.separator+scriptName+".mql"));
					String md5_2 = StringFx.md5(result);
					
					if (!md5_1.equals(md5_2)) {
						
						Misc.create(path_mql+File.separator+scriptName+".mql", result);
						Misc.create(path_scripts+File.separator+scriptName+".mql", result);
						resultScripts.put(scriptName, "U");
						nb_change++;
						
					} else {
						resultScripts.put(scriptName, "N");
					}
					
				} else {
					Misc.create(path_mql+File.separator+scriptName+".mql", result);
					Misc.create(path_scripts+File.separator+scriptName+".mql", result);
					resultScripts.put(scriptName, "I");
					nb_change++;
				}
				
			}
			
			//Delete the file if not exist
			String[] fileList = (new File(path_scripts)).list();
			
			for(int i=0;i<fileList.length;i++) {
				
				String f = fileList[i];
				
				if (f.endsWith(".mql")) {
	
					String scriptName = f.substring(0, f.length()-4);
				
					if (!ScriptManager.exist(scriptName) || !GroupManager.isGrantedScript(group, scriptName)) {
	
						if (FileFx.exist(path_mql+File.separator+f).equals("1")) {
							FileFx.delete(path_mql+File.separator+f);
						}
						if (FileFx.exist(path_scripts+File.separator+f).equals("1")) {
							FileFx.delete(path_scripts+File.separator+f);
						}
						resultScripts.put(scriptName, "D");
						nb_change++;
						
					}
					
				}
				
			}
			
			if (nb_change == 0) {
				throw new Exception("Sorry, no change detected.");
			}
	
			Misc.create(path_mql+File.separator+"commit.json", resultJson.toJSONString());
			
			return commit_id;
			
		} catch (Exception e) {
			
			try {
				FileFx.delete(path_mql);
			} catch (Exception f) {}
			
			throw e;
			
		}
		
	}
	
	//Export all scripts
	@SuppressWarnings("unchecked")
	public static String commitScript(SessionThread session, String user, long sessionId, String group, String scriptFilter, String message) throws Exception {
		
		String path_mql = null;
		
		try {
		
			String dt = DateFx.systimestamp_min();
			String commit_id = dt+"_"+StringFx.generate_random_str("6")+"_"+StringFx.generate_random_str("6")+"_CMT";
			JSONObject resultJson = new JSONObject();
			JSONObject resultScripts = new JSONObject();
			resultJson.put("___sys_date___", dt);
			resultJson.put("___sys_commit_id___", commit_id);
			resultJson.put("___sys_user___", user);
			resultJson.put("___sys_message___", message);
			resultJson.put("___sys_script___", resultScripts);
			GroupManager.generateErrorIfNotGranted(sessionId, group, "Versioning");
			
			//Get all scripts
			JSONObject scripts = GroupManager.getScript(sessionId, group);
	
			String path_group = "commits"+File.separator+group;
			String path_scripts = path_group+File.separator+"scripts";
			String path_ids = path_group+File.separator+"ids";
			path_mql = path_ids+File.separator+commit_id;
	
			if (FileFx.exist(path_group).equals("0")) {FileFx.mkdir(path_group);}
			if (FileFx.exist(path_scripts).equals("0")) {FileFx.mkdir(path_scripts);}
			if (FileFx.exist(path_ids).equals("0")) {FileFx.mkdir(path_ids);}
			if (FileFx.exist(path_mql).equals("0")) {FileFx.mkdir(path_mql);}
			
			int nb_change = 0;
			
			//Parse all scripts
			for(Object o:scripts.keySet()) {
				
				String result = "";
				
				//Get the current script name
				String scriptName = (String) o;
				
				if (scriptName.startsWith(scriptFilter))  {
				
					result += generateMerge(session, scriptName)+"\n";
					result += generateUpdateDelay(session, scriptName)+"\n";
					
					if (new File(path_scripts+File.separator+scriptName+".mql").exists()) {
		
						String md5_1 = StringFx.md5(FileFx.load(session.user, path_scripts+File.separator+scriptName+".mql"));
						String md5_2 = StringFx.md5(result);
						
						if (!md5_1.equals(md5_2)) {
							
							Misc.create(path_mql+File.separator+scriptName+".mql", result);
							Misc.create(path_scripts+File.separator+scriptName+".mql", result);
							resultScripts.put(scriptName, "U");
							nb_change++;
							
						} else {
							resultScripts.put(scriptName, "N");
						}
						
					} else {
						Misc.create(path_mql+File.separator+scriptName+".mql", result);
						Misc.create(path_scripts+File.separator+scriptName+".mql", result);
						resultScripts.put(scriptName, "I");
						nb_change++;
					}
					
				}
				
			}
			
			//Delete the file if not exist
			String[] fileList = (new File(path_scripts)).list();
			
			for(int i=0;i<fileList.length;i++) {
				
				String f = fileList[i];
				
				if (f.endsWith(".mql")) {
	
					String scriptName = f.substring(0, f.length()-4);
					
					if (scriptName.startsWith(scriptFilter))  {
				
						if (!ScriptManager.exist(scriptName) || !GroupManager.isGrantedScript(group, scriptName)) {
		
							if (FileFx.exist(path_mql+File.separator+f).equals("1")) {
								FileFx.delete(path_mql+File.separator+f);
							}
							if (FileFx.exist(path_scripts+File.separator+f).equals("1")) {
								FileFx.delete(path_scripts+File.separator+f);
							}
							resultScripts.put(scriptName, "D");
							nb_change++;
							
						}
						
					}
					
				}
				
			}
			
			if (nb_change == 0) {
				throw new Exception("Sorry, no change detected.");
			}
	
			Misc.create(path_mql+File.separator+"commit.json", resultJson.toJSONString());
			
			return commit_id;
			
		} catch (Exception e) {
			
			try {
				FileFx.delete(path_mql);
			} catch (Exception f) {}
			
			throw e;
			
		}
		
	}

	//Add a script
	@SuppressWarnings("unchecked")
	public static void add(EnvManager env, SessionThread session, String method, String scriptName, String varNames, String mql, String desc, String example, String activateLog, String nbAttempt, String parent_pid) throws Exception {
		
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
		
		synchronized ("script["+scriptName+"]") {
			
			//Generate an error if the script already exist
			if (exist(scriptName)) {
	
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
			obj.put("priority", "0");
			obj.put("nb_in_thread", "1");
	
			obj.put("groups", new JSONObject());
			
			Record2.add("mql_script", "script["+scriptName+"]", obj.toJSONString());
			
			String user = SessionThreadAgent.allServerThread.get(session.idConnection).serverThread.user;
			
			GroupManager.grantScript(session.idConnection, user, scriptName, true);

			JSONObject script = ScriptManager.get(session, scriptName);
			String result = "script merge \""+scriptName.replace("\"", "\\\"")+"\" "+((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
			result += "\n  ("+(script.get("vars")+"")+") ";
			result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
			result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
			
			save_trace(session.CLIENT_HOST_NAME, session.CLIENT_HOST_ADDRESS, session.CLIENT_PORT, session.user, scriptName, result);
			
			reset_cache();
			
		}
		
	}

	public static void save_trace(String client_host_name, String client_host_ip, String client_port, String mql_user, String mql_script, String mql) throws Exception {
		
		try {
			
			String sql = "INSERT INTO history (\n"
					+ "				dt_insert,\n"
					+ "				client_host_name,\n"
					+ "				client_host_ip,\n"
					+ "				client_port,\n"
					+ "				mql_user,\n"
					+ "				mql_script,\n"
					+ "				mql\n"
					+ "			) VALUES (\n"
					+ "				'"+DateFx.systimestamp()+"' ,\n"
					+ "				"+SQLManager.encode(client_host_name)+" ,\n"
					+ "				"+SQLManager.encode(client_host_ip)+" ,\n"
					+ "				"+SQLManager.encode(client_port)+" ,\n"
					+ "				"+SQLManager.encode(mql_user)+" ,\n"
					+ "				"+SQLManager.encode(mql_script)+" ,\n"
					+ "				"+SQLManager.encode(mql)+"\n"
					+ "			)";
			
			MYSQLManager.executeUpdate(sql, false);
			
		} catch (Exception e) {}
		
	}

	//Update a script
	public static void merge(EnvManager env, SessionThread session, String scriptName, String varNames, String mql, String desc, String example, String activateLog, String nbAttempt, String parent_pid) throws Exception {
		
		if (exist(scriptName)) {
			update(env, session, scriptName, varNames, mql, desc, example, activateLog, nbAttempt, parent_pid);
		} else {
			int last = scriptName.lastIndexOf(".");
			add(env, session, scriptName.substring(last+1), scriptName.substring(0, last), varNames, mql, desc, example, activateLog, nbAttempt, parent_pid);
		}
	}

	//Update a script
	@SuppressWarnings("unchecked")
	public static void update(EnvManager env, SessionThread session, String scriptName, String varNames, String mql, String desc, String example, String activateLog, String nbAttempt, String parent_pid) throws Exception {
		
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
		if (!exist(scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		String user = session.user;
		
		if (!ScriptManager.isGrantedToUser(user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+user+"'."); 
			
		}
		
		JSONObject groups = GroupManager.scriptGetGroup(scriptName);
		
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
		
		synchronized ("script["+scriptName+"]") {
		
			//Overwrite the script object
			JSONObject obj = Record2.getNode("script["+scriptName+"]");
			obj.put("k", scriptName);
			obj.put("vars", varNames);
			obj.put("mql", mql);
			obj.put("desc", desc);
			obj.put("return", example);
			obj.put("activateLog", activateLog);
			obj.put("nbAttempt", nbAttempt);
			
			obj.put("groups", groups);
			
			Record2.update("script["+scriptName+"]", obj.toJSONString());
			
			JSONObject script = ScriptManager.get(session, scriptName);
			String result = "script merge \""+scriptName.replace("\"", "\\\"")+"\" "+((script.get("activateLog")+"").equals("1")?"true":"false")+" "+script.get("nbAttempt")+" ";
			result += "\n  ("+(script.get("vars")+"")+") ";
			result += "\n  \""+(script.get("desc")+"").replace("\"", "\\\"")+"\" ";
			result += "\n{"+(script.get("mql")+"")+"} \""+(script.get("return")+"").replace("\"", "\\\"")+"\";";
			
			save_trace(session.CLIENT_HOST_NAME, session.CLIENT_HOST_ADDRESS, session.CLIENT_PORT, session.user, scriptName, result);
			
			reset_cache();
			
		}
		
	}

	//Set delay
	@SuppressWarnings("unchecked")
	public static void set_delay(SessionThread session, EnvManager env, String scriptName, String condition, String val, String type) throws Exception {
		
		//Generate an error if the script does not exist
		if (!exist(scriptName)) {

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
		
		if (!ScriptManager.isGrantedToUser(session.user, scriptName)) {
			
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'."); 
			
		}
		
		synchronized ("script["+scriptName+"]") {
		
			//Get the script object
			JSONObject obj = Record2.getNode("script["+scriptName+"]");
			
			obj.put("delay_value", val);
			obj.put("delay_type", type);
			obj.put("delay_condition", condition);
			
			Record2.update("script["+scriptName+"]", obj.toJSONString());
			
			reset_cache();
			
		}
		
	}

	//Delete a script
	public static void delete(long sessionId, String scriptName) throws Exception {
		
		synchronized ("script["+scriptName+"]") {

			//Generate an error if the script does not exist
			if (!exist(scriptName)) {
	
				throw new Exception("Sorry, the script "+scriptName+" does not exist.");
	
			}
			
			String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
	
			if (!ScriptManager.isGrantedToUser(user, scriptName)) {
	
				throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+user+"'."); 
				
			}
			
			JSONObject groups = GroupManager.scriptGetGroup(scriptName);
			for(Object group : groups.keySet()) {
	
				if (GroupManager.isGrantedScript((String) group, scriptName)) {
					GroupManager.ungrantScript(sessionId, (String) group, scriptName);
				}
				
			}
			
			Record2.remove("script["+scriptName+"]");
			
			reset_cache();
			
		}
		
	}
	
	//Check if a script is granted to a user
	public static boolean isGrantedToUser(String login, String scriptName) throws Exception {
		
		if (login.length()==0) {
			
			throw new Exception("Sorry, the login is required.");
			
		}
		
		if (scriptName.length()==0) {
			
			throw new Exception("Sorry, the script name is required.");
			
		}
		
		if (!UserManager.exist(login)) {
			
			throw new Exception("Sorry, the user <"+login+"> does not exist.");
			
		} else if (!ScriptManager.exist(scriptName)) {
			
			throw new Exception("Sorry, the script <"+scriptName+"> does not exist.");
			
		} else {
			
			boolean result = false;
			
			//Get all groups
			JSONObject userGroups = GroupManager.userGetGroup(login);
			
			if (userGroups.containsKey("sys")) {
			
				return true;
				
			} else {
				
				for (Object userGroup : userGroups.keySet()) {
					String groupName = (String) userGroup;
					
					JSONObject bdGroup = Record2.getNode("G["+groupName+"]");
					
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
	public static boolean isGrantedToUser(JSONObject userGroups, String scriptName) throws Exception {
		
		boolean result = false;
		
		for (Object userGroup : userGroups.keySet()) {
			String groupName = (String) userGroup;
			
			JSONObject bdGroup = Record2.getNode("G["+groupName+"]");
			
			if (((JSONObject) bdGroup.get("scripts")).containsKey(scriptName)) {
				
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}

}
