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

package re.jpayet.mentdb.ext.rest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;

//The DOC REST class
public class REST_DOCManager extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public static void checkMethod(EnvManager env, String method, String s, JSONObject scripts, StringBuilder result, boolean[] first, String user, String password) throws Exception {

		if (scripts.containsKey(s+"."+method)) {

			JSONObject scriptObj = ScriptManager.get(null, s+"."+method);
			
			if (first[0]) result.append("<table id='"+s+"."+method+"' style='display:table;width:100%;border:1px #ddd solid;border-collapse: collapse;'>");
			else result.append("<table id='"+s+"."+method+"' style='display:none;width:100%;border:1px #ddd solid;border-collapse: collapse;'>");

			first[0] = false;
			
			if (method.equals("post")) result.append("<tr><td colspan='2' style='border:0px;font-weight:bold;padding:10px 10px 7px 10px;font-size:12px;color:#fff;text-align:center;background-color: #3A87AD;'>"+method.toUpperCase()+"</td></tr>");
			else if (method.equals("put")) result.append("<tr><td colspan='2' style='border:0px;font-weight:bold;padding:10px 10px 7px 10px;font-size:12px;color:#fff;text-align:center;background-color: #F89406;'>"+method.toUpperCase()+"</td></tr>");
			else if (method.equals("get")) result.append("<tr><td colspan='2' style='border:0px;font-weight:bold;padding:10px 10px 7px 10px;font-size:12px;color:#fff;text-align:center;background-color: #468847;'>"+method.toUpperCase()+"</td></tr>");
			else result.append("<tr><td colspan='2' style='border:0px;font-weight:bold;padding:10px 10px 7px 10px;font-size:12px;color:#fff;text-align:center;background-color: #B94A48;'>"+method.toUpperCase()+"</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Target</td><td style='font-size:13px;padding:0px 10px 0px 10px'>https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Path</td><td style='color:#d14;font-weight:bold;font-size:13px;padding:0px 10px 0px 10px'>/api/"+s.replace(".", "/")+"</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Description</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>"+scriptObj.get("desc")+"</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Produces</td><td style='color:#d14;font-weight:bold;font-size:13px;padding:0px 10px 0px 10px'>application/json</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Header</td><td style='padding:6px 10px 8px 10px;color:#333;font-size:13px;'>");
			result.append("<b>x-token</b> : the JSON Web Token (empty or not define the first time)<br>");
			result.append("<b>x-user</b> : the user (required the first time)<br>");
			result.append("<b>x-password</b> : the password (required the first time)");
			result.append("</td></tr>");
			result.append("<tr><td colspan='2' style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Parameters</td></tr>");
			StringBuilder form = new StringBuilder();
			form.append("<tr><td colspan='2' style='font-weight:bold;padding:4px 10px 6px 10px'>Form</td></tr>");
			form.append("<form name='f_"+s+"."+method+"' id='f_"+s+"."+method+"'>");
			
			String mqlVars = (String) scriptObj.get("vars");
			Vector<Vector<MQLValue>> vv = Misc.splitCommand(mqlVars);
			JSONArray vars = ScriptManager.param(vv.get(0));
			JSONObject variables = new JSONObject();
			
			for(int y=0;y<vars.size();y++) {
				
				JSONObject v = (JSONObject) vars.get(y);
				String name = (String) v.get("name");
				String desc = (String) v.get("desc");
				String type = (String) v.get("type");
				String isNull = (String) v.get("isNull");
				String isEmpty = (String) v.get("isEmpty");
				String example = (String) v.get("example");
				
				variables.put(name.substring(1, name.length()-1), "t_"+s+"."+method+"___"+name.substring(1, name.length()-1));
				
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;' colspan='2'><img style='vertical-align: middle;' src='../web/images/p.png'> "+name.substring(1, name.length()-1)+"</td></tr>");
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;'>&nbsp; &nbsp; &nbsp; - Description</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>"+desc+"</td></tr>");
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;'>&nbsp; &nbsp; &nbsp; - MQL Type</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>"+type+"</td></tr>");
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;'>&nbsp; &nbsp; &nbsp; - Null is allowed</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>"+isNull+"</td></tr>");
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;'>&nbsp; &nbsp; &nbsp; - Empty is allowed</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>"+isEmpty+"</td></tr>");
				result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;'>&nbsp; &nbsp; &nbsp; - Example</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'><pre style='padding-top: 10px;padding-bottom: 10px;overflow-x: auto;width: 524px;'>"+example.replace("<", "&lt;")+"</pre></td></tr>");
				form.append("<tr><td colspan='2' style='font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;'><img style='vertical-align: middle;' src='../web/images/p.png'>  "+name.substring(1, name.length()-1)+"<br><textarea name='t_"+s+"."+method+"___"+name.substring(1, name.length()-1)+"' id='t_"+s+"."+method+"___"+name.substring(1, name.length()-1)+"' style='border: 1px #E0E0E0 solid;resize:vertical;-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;width:100%;padding: 8px;'>"+example.replace("<", "&lt;")+"</textarea></td></tr>");
				
			}

			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;'>Return example:</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'><pre style='padding-top: 10px;padding-bottom: 10px;overflow-x: auto;width: 524px;color:#090;font-weight:bold'>"+(""+scriptObj.get("return")).replace("<", "&lt;")+"</pre></td></tr>");

			form.append("<tr><td colspan='2' style='font-weight:bold;padding:4px 10px 6px 10px'><button id='b_"+s+"."+method+"' type='button' onclick='return callApi(\"https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"\", \""+s.replace(".", "/").replace("\"", "\\\"")+"\", \""+method.toUpperCase()+"\", \""+variables.toJSONString().replace("\"", "\\\"")+"\", \""+user.replace("\"", "\\\"")+"\", \""+password.replace("\"", "\\\"")+"\", \"r_"+s+"."+method+"\", \"b_"+s+"."+method+"\");'  style='-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;padding: 7px;display: inline-block;width: 100%;margin-top: 3px;color: #fff;background-color: #337ab7;border-color: #2e6da4;'>TRY</button></td></tr>");
			form.append("</form>");
			form.append("<tr><td colspan='2' style='width:200px;font-weight:bold;padding:6px 10px 6px 10px;font-size: 13px;'><textarea name='r_"+s+"."+method+"' id='r_"+s+"."+method+"' style='height: 240px;border: 1px #E0E0E0 solid;resize:vertical;-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;width:100%;padding: 8px;'></textarea></td></tr>");
			
			result.append("<tr><td colspan='2' style='width:200px;font-weight:bold;padding:4px 10px 6px 10px'>Errors</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;'>&nbsp; &nbsp; &nbsp; 400</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>Bad Request</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;'>&nbsp; &nbsp; &nbsp; 401</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>Unauthorized</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;'>&nbsp; &nbsp; &nbsp; 403</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>Forbidden</td></tr>");
			result.append("<tr><td style='width:200px;font-weight:bold;padding:4px 10px 6px 10px;font-size: 13px;color:#d14;'>&nbsp; &nbsp; &nbsp; 501</td><td style='color:#333;font-size:13px;padding:0px 10px 0px 10px'>Internal Server Error</td></tr>");
			
			result.append("<tr><td colspan='2' style='height:1px;background-color:#555'></td></tr>");
			result.append(form.toString());
			
			result.append("<tr><td colspan='2' style='height:1px;background-color:#555'></td></tr>");
			result.append("</table>");
		
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static String private_api_doc(EnvManager env, String x_user, String x_password) throws Exception {
		
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
		
		//Initialization
		StringBuilder result = new StringBuilder("<style>table, th, td {border: 1px solid #ddd;}</style>");
		
		JSONObject scripts = GroupManager.userGetScript(0, x_user);
		JSONObject scriptsWithoutMethod = new JSONObject();
		
		for(Object o : scripts.keySet()) {
			
			String s = (String) o;
			
			if (s.endsWith(".get") || s.endsWith(".put") || s.endsWith(".post") || s.endsWith(".delete")) {
				s = s.substring(0, s.lastIndexOf("."));
				scriptsWithoutMethod.put(s, 0L);
			}
			
		}
		
		List<Map.Entry<String, Long>> list = new LinkedList<>( scriptsWithoutMethod.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(int i=0;i<list.size();i++) {
			
			String s = list.get(i).getKey();
			
			result.append("<div style='background-color: #f5f5f5;padding: 10px;margin-top:20px;border:1px #e5e5e5 solid;-webkit-border-top-left-radius: 7px;-webkit-border-top-right-radius: 7px;-moz-border-radius-topleft: 7px;-moz-border-radius-topright: 7px;border-top-left-radius: 7px;border-top-right-radius: 7px;'>");
			result.append("<a id='"+s+"' href='javascript:;' onclick='showToggle(\"_."+s+"\");' style='padding: 10px;color:#08c;font-size: 18px;font-weight: bold;'>"+s.replace(".", "/")+"</a>");
			if (scripts.containsKey(s+".delete")) result.append("<a href='javascript:;' onclick='selectMethod(\"delete\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #B94A48;display:inline-block;float:right'>DELETE</a>");
			if (scripts.containsKey(s+".get")) result.append("<a href='javascript:;' onclick='selectMethod(\"get\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #468847;display:inline-block;float:right'>GET</a>");
			if (scripts.containsKey(s+".put")) result.append("<a href='javascript:;' onclick='selectMethod(\"put\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #F89406;display:inline-block;float:right'>PUT</a>");
			if (scripts.containsKey(s+".post")) result.append("<a href='javascript:;' onclick='selectMethod(\"post\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #3A87AD;display:inline-block;float:right'>POST</a>");
			result.append("</div><div id='_."+s+"' style='margin-bottom: 80px;display:none;border-left:1px #e5e5e5 solid;border-right:1px #e5e5e5 solid;border-bottom:1px #e5e5e5 solid;padding:10px;'>");

			boolean[] first = {true};
			REST_DOCManager.checkMethod(env, "post", s, scripts, result, first, x_user, x_password);
			REST_DOCManager.checkMethod(env, "put", s, scripts, result, first, x_user, x_password);
			REST_DOCManager.checkMethod(env, "get", s, scripts, result, first, x_user, x_password);
			REST_DOCManager.checkMethod(env, "delete", s, scripts, result, first, x_user, x_password);
			
			result.append("</div>");
			
		}

		result.append("\n<script>\n");
		result.append("function selectMethod(m, s) {\n");
		result.append("  if (document.getElementById(s+'.post')!=null) document.getElementById(s+'.post').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.put')!=null) document.getElementById(s+'.put').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.get')!=null) document.getElementById(s+'.get').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.delete')!=null) document.getElementById(s+'.delete').style.display='none';\n");
		result.append("  document.getElementById(s+'.'+m).style.display='table';\n");
		result.append("  document.getElementById('_.'+s).style.display='block';\n");
		result.append("}\n");
		result.append("function showToggle(id) {\n");
		result.append("  if (document.getElementById(id).style.display==='none') \n");
		result.append("    document.getElementById(id).style.display='block';\n");
		result.append("  else document.getElementById(id).style.display='none';\n");
		result.append("}\n");
		result.append("String.prototype.replaceAll = function(search, replacement) {\n");
		result.append("    var target = this;\n");
		result.append("    return target.replace(new RegExp(search, 'g'), replacement);\n");
		result.append("};\n");
		result.append("function callApi(https, url, method, variables, user, password, id, tryId) {\n"+
				"\n"+
				"  document.getElementById(tryId).innerHTML = \"Running ...\";\n"+
				"  document.getElementById(tryId).style.backgroundColor = \"#090\";\n"+
				"  document.getElementById(id).style.backgroundColor = '#fff';\n"+
				"  document.getElementById(id).style.color = '#000';\n"+
				"  var strVars = \"\";\n"+
				"  var jsonVars = JSON.parse(variables);\n"+
				"  for (var key in jsonVars) {\n"+
				"    strVars += \"&\"+key+\"=\"+encodeURIComponent(document.getElementById(jsonVars[key]).value);\n"+
				"  }\n"+
				"  if (strVars.length>0) strVars = strVars.substring(1);\n"+
				"  \n"+
				"    var xhttp = new XMLHttpRequest();\n"+
				"    if ((method==='GET' || method==='DELETE') && strVars.length>0) xhttp.open(method, https+\"/api/\"+url+\"?\"+strVars, true);\n"+
				"    else xhttp.open(method, https+\"/api/\"+url, true);\n"+
				"    xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");\n"+
				"    xhttp.setRequestHeader(\"x-user\", user);\n"+
				"    xhttp.setRequestHeader(\"x-password\", password);\n"+
				"    xhttp.onreadystatechange = function (oEvent) {  \n"+
				"    if (xhttp.readyState === 4) {  \n"+
				"      if (xhttp.status === 200) {  \n"+
				"        document.getElementById(id).value = ''+xhttp.responseText;\n"+
				"        document.getElementById(id).style.backgroundColor = '#090';\n"+
				"        document.getElementById(id).style.color = '#fff';\n"+
				"  		 document.getElementById(tryId).innerHTML = \"TRY\";\n"+
				"  		 document.getElementById(tryId).style.backgroundColor = \"#337ab7\";\n"+
				"      } else {  \n"+
				"        document.getElementById(id).value = ''+xhttp.statusText.replaceAll('<br>', '\\n');\n"+
				"        document.getElementById(id).style.backgroundColor = '#900';\n"+
				"        document.getElementById(id).style.color = '#fff';\n"+
				"  		 document.getElementById(tryId).innerHTML = \"TRY\";\n"+
				"  		 document.getElementById(tryId).style.backgroundColor = \"#337ab7\";\n"+
				"      }  \n"+
				"    }  \n"+
				"  }; \n"+
				"  if ((method==='GET' || method==='DELETE') && strVars.length>0) xhttp.send(null);\n"+
				"  else xhttp.send(strVars);\n"+
				"  return false;\n"+
				"}\n");
		result.append("</script>\n");
		
		return result.toString();
		
	}
	
	@SuppressWarnings("unchecked")
	public static String public_api_doc(EnvManager env) throws Exception {
		
		//Initialization
		StringBuilder result = new StringBuilder("<style>table, th, td {border: 1px solid #ddd;}</style>");
		
		JSONObject scripts = GroupManager.getScript(0, "public");
		JSONObject scriptsWithoutMethod = new JSONObject();
		
		for(Object o : scripts.keySet()) {
			
			String s = (String) o;
			if (s.endsWith(".get") || s.endsWith(".put") || s.endsWith(".post") || s.endsWith(".delete")) {
				s = s.substring(0, s.lastIndexOf("."));
				scriptsWithoutMethod.put(s, 0L);
			}
			
		}
		
		List<Map.Entry<String, Long>> list = new LinkedList<>( scriptsWithoutMethod.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(int i=0;i<list.size();i++) {
			
			String s = list.get(i).getKey();
			
			result.append("<div style='background-color: #f5f5f5;padding: 10px;margin-top:20px;border:1px #e5e5e5 solid;-webkit-border-top-left-radius: 7px;-webkit-border-top-right-radius: 7px;-moz-border-radius-topleft: 7px;-moz-border-radius-topright: 7px;border-top-left-radius: 7px;border-top-right-radius: 7px;'>");
			result.append("<a id='"+s+"' href='javascript:;' onclick='showToggle(\"_."+s+"\");' style='padding: 10px;color:#08c;font-size: 18px;font-weight: bold;'>"+s.replace(".", "/")+"</a>");
			if (scripts.containsKey(s+".delete")) result.append("<a href='javascript:;' onclick='selectMethod(\"delete\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #B94A48;display:inline-block;float:right'>DELETE</a>");
			if (scripts.containsKey(s+".get")) result.append("<a href='javascript:;' onclick='selectMethod(\"get\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #468847;display:inline-block;float:right'>GET</a>");
			if (scripts.containsKey(s+".put")) result.append("<a href='javascript:;' onclick='selectMethod(\"put\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #F89406;display:inline-block;float:right'>PUT</a>");
			if (scripts.containsKey(s+".post")) result.append("<a href='javascript:;' onclick='selectMethod(\"post\", \""+s.replace("\"", "&quot;")+"\")' style='margin-left:15px;margin-top: 2px;-webkit-border-radius: 3px;-moz-border-radius: 3px;border-radius: 3px;color:#fff;padding: 3px 8px 2px 8px;background-color: #3A87AD;display:inline-block;float:right'>POST</a>");
			result.append("</div><div id='_."+s+"' style='margin-bottom: 80px;display:none;border-left:1px #e5e5e5 solid;border-right:1px #e5e5e5 solid;border-bottom:1px #e5e5e5 solid;padding:10px;'>");

			boolean[] first = {true};
			REST_DOCManager.checkMethod(env, "post", s, scripts, result, first, "public", "pwd");
			REST_DOCManager.checkMethod(env, "put", s, scripts, result, first, "public", "pwd");
			REST_DOCManager.checkMethod(env, "get", s, scripts, result, first, "public", "pwd");
			REST_DOCManager.checkMethod(env, "delete", s, scripts, result, first, "public", "pwd");
			
			result.append("</div>");
			
		}

		result.append("\n<script>\n");
		result.append("function selectMethod(m, s) {\n");
		result.append("  if (document.getElementById(s+'.post')!=null) document.getElementById(s+'.post').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.put')!=null) document.getElementById(s+'.put').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.get')!=null) document.getElementById(s+'.get').style.display='none';\n");
		result.append("  if (document.getElementById(s+'.delete')!=null) document.getElementById(s+'.delete').style.display='none';\n");
		result.append("  document.getElementById(s+'.'+m).style.display='table';\n");
		result.append("  document.getElementById('_.'+s).style.display='block';\n");
		result.append("}\n");
		result.append("function showToggle(id) {\n");
		result.append("  if (document.getElementById(id).style.display==='none') \n");
		result.append("    document.getElementById(id).style.display='block';\n");
		result.append("  else document.getElementById(id).style.display='none';\n");
		result.append("}\n");
		result.append("String.prototype.replaceAll = function(search, replacement) {\n");
		result.append("    var target = this;\n");
		result.append("    return target.replace(new RegExp(search, 'g'), replacement);\n");
		result.append("};\n");
		result.append("function callApi(https, url, method, variables, user, password, id, tryId) {\n"+
				"\n"+
				"  document.getElementById(tryId).innerHTML = \"Running ...\";\n"+
				"  document.getElementById(tryId).style.backgroundColor = \"#090\";\n"+
				"  document.getElementById(id).style.backgroundColor = '#fff';\n"+
				"  document.getElementById(id).style.color = '#000';\n"+
				"  var strVars = \"\";\n"+
				"  var jsonVars = JSON.parse(variables);\n"+
				"  for (var key in jsonVars) {\n"+
				"    strVars += \"&\"+key+\"=\"+encodeURIComponent(document.getElementById(jsonVars[key]).value);\n"+
				"  }\n"+
				"  if (strVars.length>0) strVars = strVars.substring(1);\n"+
				"  \n"+
				"    var xhttp = new XMLHttpRequest();\n"+
				"    if ((method==='GET' || method==='DELETE') && strVars.length>0) xhttp.open(method, https+\"/api/\"+url+\"?\"+strVars, true);\n"+
				"    else xhttp.open(method, https+\"/api/\"+url, true);\n"+
				"    xhttp.setRequestHeader(\"Content-type\", \"application/x-www-form-urlencoded\");\n"+
				"    xhttp.setRequestHeader(\"x-user\", user);\n"+
				"    xhttp.setRequestHeader(\"x-password\", password);\n"+
				"    xhttp.onreadystatechange = function (oEvent) {  \n"+
				"    if (xhttp.readyState === 4) {  \n"+
				"      if (xhttp.status === 200) {  \n"+
				"        document.getElementById(id).value = ''+xhttp.responseText;\n"+
				"        document.getElementById(id).style.backgroundColor = '#090';\n"+
				"        document.getElementById(id).style.color = '#fff';\n"+
				"  		 document.getElementById(tryId).innerHTML = \"TRY\";\n"+
				"  		 document.getElementById(tryId).style.backgroundColor = \"#337ab7\";\n"+
				"      } else {  \n"+
				"        document.getElementById(id).value = ''+xhttp.statusText.replaceAll('<br>', '\\n');\n"+
				"        document.getElementById(id).style.backgroundColor = '#900';\n"+
				"        document.getElementById(id).style.color = '#fff';\n"+
				"  		 document.getElementById(tryId).innerHTML = \"TRY\";\n"+
				"  		 document.getElementById(tryId).style.backgroundColor = \"#337ab7\";\n"+
				"      }  \n"+
				"    }  \n"+
				"  }; \n"+
				"  if ((method==='GET' || method==='DELETE') && strVars.length>0) xhttp.send(null);\n"+
				"  else xhttp.send(strVars);\n"+
				"  return false;\n"+
				"}\n");
		result.append("</script>\n");
		
		return result.toString();
		
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		if (request.getParameter("action")!=null && request.getParameter("action").equals("login")) {
			getApiLoginPage(request, response, null);
		} else if (request.getParameter("x-user")==null || request.getParameter("x-user").equals("")) {
			try {
				getApiDoc(request, response, null, null);
			} catch (Exception e) {
				throw new ServletException(e.getMessage()+"");
			}
		} else {
			
			try {
				getApiDoc(request, response, request.getParameter("x-user"), request.getParameter("x-password")+"");
			} catch (Exception e) {
				getApiLoginPage(request, response, e.getMessage()+"");
			}
			
		}
		
    }

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		if (request.getParameter("action")!=null && request.getParameter("action").equals("login")) {
			getApiLoginPage(request, response, null);
		} else if (request.getParameter("x-user")==null || request.getParameter("x-user").equals("")) {
			try {
				getApiDoc(request, response, null, null);
			} catch (Exception e) {
				throw new ServletException(e.getMessage()+"");
			}
		} else {
			
			try {
				getApiDoc(request, response, request.getParameter("x-user"), request.getParameter("x-password")+"");
			} catch (Exception e) {
				getApiLoginPage(request, response, e.getMessage()+"");
			}
			
		}
		
    }
	
    protected void getApiLoginPage(HttpServletRequest request, HttpServletResponse response, String errMsg) throws ServletException {
    	
    	try {

			response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw = response.getWriter();
            
            pw.println("<!DOCTYPE html>\n"+
        			"<html>\n"+
        			"<head>\n"+
            		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"+
            		"    <!-- Meta, title, CSS, favicons, etc. -->\n"+
            		"    <meta charset=\"utf-8\">\n"+
            		"    <link rel=\"icon\" type=\"image/png\" href=\"../web/images/db32x32.png\" />\n"+
            		"    <link rel='stylesheet' type='text/css' href='../web/css/style.css'/>\n"+
            		"    <title>REST API | MentDB</title>\n"+
            		"</head>\n"+
        			"  <body class='signBg'>\n"+
        			"    <div id='divSignInCenter'>\n"+
        			"        <br><br><br><br>\n"+
        			"        <div id='divSignIn'>\n"+
        			"            <div id='divSignInImg'><a href='..'><img src='../web/images/db128x128.png' height='70px' alt=''></a></div>\n"+
        			"            <div style='padding:10px 10px 28px 10px'><b>REST API</b></div>\n"+
        			(errMsg!=null?"<div style='padding:10px;color:#C00'>"+errMsg+"</div>":"")+
        			"            <form action='index.jsp' method='post'>\n"+
        			"                <input class='style-1' style='border: 1px #777 solid;' type='text' placeholder='User' name='x-user' autofocus><br>\n"+
        			"                <input class='style-1' style='border: 1px #777 solid;' type='password' placeholder='Password' name='x-password'><br><br><br>\n"+
        			"                <input type='submit' value='Login' class='btn btn-danger'>\n"+
        			"            </form>\n"+
        			"        </div>\n"+
        			"        <div style='margin-top: 10px;'>&copy; "+Start.copyright+" - <a href='https://www.mentdb.org' style='color:#333'><b>MentDB</b></a></div>\n"+
        			"    </div>\n"+
        			"  </body>\n"+
        			"</html>\n");
            
            
            response.flushBuffer();
            
		} catch (Exception e) {
			throw new ServletException(e);
		}
    	
    }

    protected void getApiDoc(HttpServletRequest request, HttpServletResponse response, String login, String password) throws Exception {
		
		response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter pw = response.getWriter();
        
        String title = "REST API";
        String msg = "";
        
        try {title = FileFx.ini(null, "conf"+File.separator+"server.conf", "WEBSERVER", "WEB_API_TITLE");} catch (Exception e) {}
        try {msg = FileFx.ini(null, "conf"+File.separator+"server.conf", "WEBSERVER", "WEB_API_MSG");} catch (Exception e) {}
        
        String top = "<html lang=\"en\">\n"+
    		"<head>\n"+
    		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"+
    		"    <!-- Meta, title, CSS, favicons, etc. -->\n"+
    		"    <meta charset=\"utf-8\">\n"+
    		"    <link rel=\"icon\" type=\"image/png\" href=\"../web/images/db32x32.png\" />\n"+
    		"    <title>"+title+"</title>\n"+
    		"</head>\n"+
    		"<body id='top' style=\"font-size:13px;margin:0px;font-family: 'Helvetica Neue', Roboto, Arial, 'Droid Sans', sans-serif;margin:0px;\">\n"+
    		"<div style='display: table;width: 800px;margin-left:auto;margin-right:auto;'>\n"+
    		"  <div style='width: 800px;-webkit-border-radius: 34px;-moz-border-radius: 34px;border-radius: 34px;float: right;background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b6479), color-stop(100%, #4c5566)), #686e78;\n"+
    		"    background: -webkit-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: -moz-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: -o-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: linear-gradient(#444, #333), #3F3F3F;background-color: #F0F0F0;height:67px;color:#E7E7E7;line-height:65px;'>\n"+
    		"    <img src=\"../web/images/db128x128.png\" style='margin-top: -7px;width: 65px;vertical-align: middle;margin-left: 1px;'>\n"+
    		"    &nbsp; &nbsp; &nbsp; <span style='font-size:22px'>"+title+"</span>&nbsp; \n";
        if (login==null || login.equals("")) top += "    <a style='text-decoration:none;padding: 0px 20px 0px 10px;color: #fff;float: right;margin-right: 10px;font-size:15px' href='/docs?action=login'>Login</a>\n";
        else top += "    <a style='text-decoration:none;padding: 0px 20px 0px 10px;color: #fff;float: right;margin-right: 10px;font-size:15px' href='/docs'>Logout</a>\n";
        top += "  </div>\n"+
    			"</div><div style='font-size: 20px;display: table;width: 800px;margin-left:auto;margin-right:auto;'>" + 
    			"<br>"+msg 
    			+ "<hr></div>"
    			+ "<div style='width: 800px;margin-left:auto;margin-right:auto;margin-top:20px'>";
        
        String result = "";
        
        if (login==null || login.equals("")) result = Database.execute_admin_mql(null, "script public api doc;");
        else result = Database.execute_admin_mql(null, "script api doc \""+login.replace("\"", "\\\"")+"\" \""+password.replace("\"", "\\\"")+"\";");
        
        pw.println(top+result+"</div><div style='font-size: 20px;display: table;width: 800px;margin-left:auto;margin-right:auto;'>"
        		+ "<hr><center>&copy; "+Start.copyright+" - <a href='https://www.mentdb.org' style='color:#333;text-decoration:underline'>MentDB</a>.</center><br><br>&nbsp;</div>");
        pw.println("</body></html>");
        response.flushBuffer();
		
    }

}
