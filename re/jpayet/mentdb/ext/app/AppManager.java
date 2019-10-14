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

package re.jpayet.mentdb.ext.app;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.tools.Misc;

public class AppManager {

	@SuppressWarnings("unchecked")
	public static String create_context(SessionThread session, String protocol, String context, String template, String version) throws Exception {

		if (protocol!=null) {
			protocol = protocol.toLowerCase();
		}

		if (context!=null) {
			context = context.toLowerCase();
		}
		

		//Generate an error if the context already exist
		if (exist_context(protocol, context).equals("1")) {

			throw new Exception("Sorry, the context '"+context+"' already exist.");

		}

		FileFx.copy_dir("web"+File.separator+"template", "web"+File.separator+protocol.toLowerCase()+File.separator+context.toLowerCase());
		
		FileFx.create("web"+File.separator+protocol.toLowerCase()+File.separator+context.toLowerCase()+File.separator+"index.jsp", "<%@ page pageEncoding=\"UTF-8\" contentType=\"text/html; charset=UTF-8\" import=\"re.jpayet.mentdb.ext.app.AppManager,re.jpayet.mentdb.ext.log.Log\" %><%\n" + 
				"try {\n	out.print(AppManager.execute(request, response, \""+protocol+"\", \""+context+"\", \"MENTDB\", \"web\"));\n} catch (Exception e) {\n	System.out.println((\"\"+e.getMessage()).replace(\"\\n\", \"<br>\"));\n	Log.trace((\"\"+e.getMessage()));\n}\n" + 
				"%>\n");
		
		JSONObject configuration = new JSONObject();
		configuration.put("protocol", protocol);
		configuration.put("context", context);
		configuration.put("template", template);
		configuration.put("version", version);
		configuration.put("vhost", new JSONObject());
		
		Record.add(session.idConnection, "APP["+protocol+"_"+context+"]", configuration.toJSONString());
		
		restart_webserver(session);

		return "Application created with successful.";
		
	}

	public static String exist_context(String protocol, String context) throws Exception {

		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}

		if ((new File("web"+File.separator+protocol.toLowerCase()+File.separator+context.toLowerCase())).exists()) {
			return "1";
		} else {
			return "0";
		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray show_context(String protocol) throws Exception {

		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}

		JSONArray result = new JSONArray();

		String[] files = (new File("web"+File.separator+protocol.toLowerCase())).list();

		for(int i=0;i<files.length;i++) {

			if (!files[i].equals(".DS_Store")) {

				result.add(files[i]);

			}

		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static void menu(EnvManager env, String jPath, String id, String title, String icon, String url, String method, String topMenu, String groups, String adminType) throws Exception {

		//Generate an error if the id is null or empty
		if (id==null || id.equals("")) {

			throw new Exception("Sorry, the id cannot be null or empty.");

		}

		//Generate an error if the groups is null or empty
		if (groups==null || groups.equals("")) {

			throw new Exception("Sorry, the groups cannot be null or empty (default: *).");

		}

		//Generate an error if the icon is null or empty
		if (icon==null || icon.equals("")) {

			throw new Exception("Sorry, the icon cannot be null or empty.");

		}

		//Generate an error if the title is null or empty
		if (title==null || title.equals("")) {

			throw new Exception("Sorry, the title cannot be null or empty.");

		}

		//Generate an error if the topMenu is null or empty
		if (topMenu==null || topMenu.equals("")) {

			throw new Exception("Sorry, the topMenuId cannot be null or empty.");

		}

		//Generate an error if the url is null or empty
		if (url==null || url.equals("")) {

			throw new Exception("Sorry, the url cannot be null or empty.");

		}
		
		//Generate an error if the method is null or empty
		if (method==null || method.equals("")) {

			throw new Exception("Sorry, the method cannot be null or empty.");

		}
		
		method = method.toLowerCase();
		
		//Generate an error if the method is not valid
		if (!method.equals("post") && !method.equals("get")) {

			throw new Exception("Sorry, the method must be 'GET' or 'POST'.");

		}

		JSONObject m = new JSONObject();

		m.put("id", id);
		m.put("icon", icon);
		m.put("title", title);
		m.put("groups", groups);
		m.put("menu", new JSONArray());
		m.put("url", url);
		m.put("method", method);
		m.put("topMenu", topMenu);
		m.put("adminType", adminType);

		JsonManager.ainsert(env, "configuration", jPath, m.toJSONString(), "OBJ");

	}

	public static String menuShow(EnvManager env) throws Exception {
		
		//Initialization
		JSONObject rights = (JSONObject) ((JSONObject) env.jsonObj.get("app")).get("groups");
		JSONArray menu = (JSONArray) ((JSONObject) env.jsonObj.get("app")).get("menu");
		String top_background_color = (String) ((JSONObject) env.jsonObj.get("app")).get("top_background_color");
		String pageId = (String) ((JSONObject) env.jsonObj.get("app")).get("current_page");

		return showMenu(rights, 0, menu, pageId, top_background_color);
		
	}

	public static String showMenu(JSONObject grants, int level, JSONArray menu, String pageId, String top_background_color) throws Exception {
		
		//Initialization
		StringBuilder result = new StringBuilder("");

		if (level == 0) result.append("<ul id=\"menu-v\">"); else {
			result.append("<ul class=\"sub\">");
		}

		for (int i = 0; i < menu.size(); i++) {
			
			JSONObject curMenu = (JSONObject)menu.get(i);

			String id = (String) curMenu.get("id");
			String icon = (String) curMenu.get("icon");
			String title = (String) curMenu.get("title");
			String groups = (String) curMenu.get("groups");
			String url = (String) curMenu.get("url");
			String method = (String) curMenu.get("method");
			String topMenu = (String) curMenu.get("topMenu");
			String adminType = (String) curMenu.get("adminType");
			
			if (adminType.equals("*-")) {
				
				if (grants.containsKey(groups)) {
					
					
					
				} else {
					
					if (grants.containsKey("*+")) {
						
						
						
					} else if (grants.containsKey("*-")) {
						
						
						
					} else {
						
						continue;
						
					}
					
				}
				
			} else {
				
				if (grants.containsKey(groups)) {
					
					
					
				} else {
					
					if (grants.containsKey("*+")) {
						
						
						
					} else if (grants.containsKey("*-")) {
						
						continue;
						
					} else {
						
						continue;
						
					}
					
				}
				
			}
			
			result.append("<li");
			if (level == 0 && topMenu.equals(pageId)) {
				result.append(" style='border-right: 5px "+top_background_color+" solid;'");
			}
			
			JSONArray subMenu = (JSONArray)curMenu.get("menu");
			
			if (method.equals("post")) {
				
				result.append("><form action=\"index.jsp\" id='menu_page_" + id + "' method='POST' style='margin:0px;padding:0px'>");
				int nb = Integer.parseInt(AtomFx.size(url, "&"));
				for(int u=1;u<=nb;u++) {
					
					String keyVal = AtomFx.get(url, ""+u, "&");
					int iEqual = keyVal.indexOf("=");
					String name = keyVal.substring(0, iEqual);
					if (name.startsWith("index.jsp?")) name = name.substring(10);
					String value = keyVal.substring(iEqual+1);
					result.append("<input type=\"hidden\" name=\""+name+"\"  value='"+value.replace("'", "&#39;")+"'>");
				
				}
				result.append("</form>");
				result.append("<a href='javascript:document.getElementById(\"menu_page_"+id+"\").submit();'><i class='fa " + icon + "' style='color:"+top_background_color+";font-size: 23px;\n" + 
						"    vertical-align: middle;\n" + 
						"    padding-left: 8px;\n" + 
						"    padding-right: 7px;'></i> &nbsp;");
				
				if (level>0) result.append("<span>"+title+"</span>");
				
				if (subMenu.size() > 0 && level>0) {
					result.append("<span style='float:right;margin-right: 11px;\n" + 
							"    color: #8e8e8e;\n" + 
							"    font-size: 12px;'>&gt;</span>");
				}
				
				result.append("</a>");
				
			} else {
				
				result.append("><a id='" + id + "' href='" + url + "'><i class='fa " + icon + "' style='color:"+top_background_color+";font-size: 23px;\n" + 
						"    vertical-align: middle;\n" + 
						"    padding-left: 8px;\n" + 
						"    padding-right: 7px;'></i> &nbsp;");
				
				if (level>0) result.append("<span>"+title+"</span>");
				
				if (subMenu.size() > 0 && level>0) {
					result.append("<span"+(level==0?" class='hidden-xs'":"")+" style='float:right;margin-right: 11px;\n" + 
							"    color: #8e8e8e;\n" + 
							"    font-size: 12px;'>&gt;</span>");
				}
				
				result.append("</a>");
				
			}

			if (subMenu.size() > 0) {
				result.append(showMenu(grants, level + 1, subMenu, pageId, top_background_color));
			}
			result.append("</li>");
			
		}

		result.append("</ul>");

		return result.toString();
	}

	public static String delete_context(SessionThread session, String protocol, String context) throws Exception {

		if (protocol!=null) {
			protocol = protocol.toLowerCase();
		}

		if (context!=null) {
			context = context.toLowerCase();
		}

		//Generate an error if the context does not exist
		if (exist_context(protocol, context).equals("0")) {

			throw new Exception("Sorry, the context '"+context+"' does not exist.");

		}

		FileFx.delete("web"+File.separator+protocol.toLowerCase()+File.separator+context.toLowerCase());

		Record.remove(session.idConnection, "APP["+protocol+"_"+context+"]");
		
		JSONObject rec = Record.getNode(session.idConnection, "VHOST[]");
		JSONObject obj = (JSONObject) rec.get(protocol);
		
		Vector<String> to_delete = new Vector<String>();
		for(Object o : obj.keySet()) {
			
			String k = (String) o;
			
			if (obj.get(k).equals(context)) {
				
				to_delete.add(k);
			
			}
			
		}
		
		for(int i=0;i<to_delete.size();i++) {
			obj.remove(to_delete.get(i));
		}
		
		Record.update(session.idConnection, "VHOST[]", rec.toJSONString());
		
		restart_webserver(session);
		
		return "Application deleted with successful.";

	}
	
	public static String is_granted_object_a(EnvManager env, String tag) {
		
		JSONObject grants = (JSONObject) ((JSONObject) env.jsonObj.get("app")).get("groups");
		
		if (grants.containsKey(tag)) {
			
			return "1";
			
		} else {
			
			if (grants.containsKey("*+")) {
				
				return "1";
				
			} else if (grants.containsKey("*-")) {
				
				return "1";
				
			} else {
				
				return "0";
				
			}
			
		}
		
	}
	
	public static String is_granted_object_sa(EnvManager env, String tag) {
		
		JSONObject grants = (JSONObject) ((JSONObject) env.jsonObj.get("app")).get("groups");
		
		if (grants.containsKey(tag)) {
			
			return "1";
			
		} else {
			
			if (grants.containsKey("*+")) {
				
				return "1";
				
			} else if (grants.containsKey("*-")) {
				
				return "0";
				
			} else {
				
				return "0";
				
			}
			
		}
		
	}

	//This function is used by the JSP page for execute a specific container
	@SuppressWarnings("unchecked")
	public static String execute(HttpServletRequest request, HttpServletResponse response, String protocol, String contextId, String mysql_database, String mentdb_user) throws Exception {
		
		HttpSession http_session = request.getSession();

		//Initialization
		SessionThread thread = (SessionThread) http_session.getAttribute("env");

		if (thread==null) {
			
			try {

				thread = new SessionThread(null);
				thread.user = mentdb_user;
				
				http_session.setAttribute("env", thread);
				
				CommandSyncAccess.execute(0, thread, request, response, 4, null, null, null, null, null, null);
				
				SessionThreadAgent agent = new SessionThreadAgent(thread);
				SessionThreadAgent.allServerThread.put(thread.idConnection, agent);
				
				//Save the application environment
				JSONObject config = (JSONObject) JsonManager.load(
					CommandManager.executeAllCommands(true, thread, Misc.splitCommand("node show (concat \"APP[\" "+protocol+" \"_\" "+contextId+" \"]\");"), thread.env, null, null)
				);
	
				String template = (String) config.get("template");
				String version = (String) config.get("version");
				
				JSONObject app_env = (JSONObject) JsonManager.load(
						CommandManager.executeAllCommands(true, thread, Misc.splitCommand("include \""+"app."+version+".template."+template+".conf"+"\""), thread.env, null, null)
					);
	
				app_env.put("protocol", protocol);
				app_env.put("context", contextId);
				app_env.put("version", version);
				app_env.put("template", template);
				app_env.put("database", mysql_database);
				app_env.put("connected", "0");
	
				JsonManager.load(thread.env, "app", app_env.toJSONString());
				JsonManager.oinsert(thread.env, "app", "/", "objects", "{}", "OBJ");
				
				thread.env.set("[app_version]", (String) app_env.get("version"));
	
				JsonManager.oinsert(thread.env, "app", "/", "groups", "{}", "OBJ");
				
			} catch (Exception e) {
				
				thread = null;
				
				http_session.invalidate();
				
				return (""+e.getMessage()).replace("\n", "<br>");
				
			}

		}
		
		synchronized(thread) {
		
			EnvManager env = thread.env;
			JsonManager.oinsert(env, "app", "/", "param", "{}", "OBJ");
			JsonManager.load(env, "result", "[]");
			
			String mql = null;
			
			if (request.getParameter("mentdb_ajax_call")==null) {
	
				//Load all parameters
				Enumeration<String> enumeration = request.getParameterNames();
				while (enumeration.hasMoreElements()) {
					String parameterName = (String) enumeration.nextElement();
					
					String[] values = request.getParameterValues(parameterName);
		
					if (values==null) {
						JsonManager.oinsert(env, "app", "/param", parameterName, null, "STR");
					} else if (values.length==1) {
						JsonManager.oinsert(env, "app", "/param", parameterName, values[0], "STR");
					} else {
		
						JSONArray a = new JSONArray();
						for(int i=0;i<values.length;i++) {
							a.add(values[i]);
						}
						JsonManager.oinsert(env, "app", "/param", parameterName, a.toJSONString(), "ARRAY");
					}
		
				}
				
			} else {
				
				mql = StringFx.decode_b64(request.getParameter("mql"));
				
			}
	
			try {
	
				String result = null;
				
				if (mql!=null) {
					
					if (JsonManager.select(env, "app", "/connected").equals("0")) {
						throw new Exception("Sorry, you are no longer connected. SAJAX is available only when you are logged in.");
					}
					
					
					CommandManager.executeAllCommands(true, thread, Misc.splitCommand(mql), thread.env, null, null);
					result = JsonManager.doc(env, "result");
					JsonManager.unload(env, "result");
					
				} else {
					result = CommandManager.executeAllCommands(true, thread, Misc.splitCommand("include \"app."+env.get("[app_version]")+".start.exe\""), thread.env, null, null);
				}
	
				//Invalidate the session if disconnected
				String disconnect = JsonManager.select(env, "app", "/disconnected");
				if (disconnect!=null && disconnect.equals("1")) {
					
					http_session.invalidate();
	
					JsonManager.oinsert(thread.env, "app", "/", "groups", "{}", "OBJ");
					
				}
	
				if (env.exist("[on_load_functions]")) env.remove("[on_load_functions]");
				if (env.exist("[page]")) env.remove("[page]");
				
				return result;
				
			} catch (Exception e) {
				System.out.println("###err="+e.getMessage());
				if (env.exist("[on_load_functions]")) env.remove("[on_load_functions]");
				if (env.exist("[page]")) env.remove("[page]");
	
				http_session.invalidate();
				
				return (""+e.getMessage()).replace("\n", "<br>");
	
			}
			
		}

	}

	public static void restart_webserver(SessionThread session) throws Exception {

		stop_webserver_http();
		stop_webserver_https();
		start_webserver_http(session);
		start_webserver_https(session);

	}

	public static void stop_webserver_http() throws Exception {

		Start.webServerAppHttp.setStopTimeout(1);
		Start.webServerAppHttp.stop();
		Start.webServerAppHttp.destroy();

	}

	public static void stop_webserver_https() throws Exception {

		Start.webServerAppHttps.setStopTimeout(1);
		Start.webServerAppHttps.stop();
		Start.webServerAppHttps.destroy();

	}

	public static void start_webserver_http(SessionThread session) throws Exception {

		//Generate the http configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(Start.WEB_SERVER_PORT_HTTPS);
		http_config.setOutputBufferSize(32768);

		String[] configuration = new String[] {
				"org.eclipse.jetty.webapp.WebInfConfiguration",
				"org.eclipse.jetty.webapp.WebXmlConfiguration",
				"org.eclipse.jetty.webapp.MetaInfConfiguration",
				"org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration",
		"org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };

		//Create the port
		Start.webServerAppHttp = new Server(Start.WEB_SERVER_PORT_APP_HTTP);
		@SuppressWarnings("resource")
		ServerConnector http = new ServerConnector(Start.webServerAppHttp,new HttpConnectionFactory(http_config));       
		http.setPort(Start.WEB_SERVER_PORT_APP_HTTP);

		Connector[] connectorAppHttp = {null};
		connectorAppHttp[0] = http;

		//Set the connectors
		Start.webServerAppHttp.setConnectors(connectorAppHttp);

		ContextHandlerCollection contextsAppHttp = new ContextHandlerCollection();

		String[] context = (new File("web"+File.separator+"http")).list();
		for(int icontext = 0;icontext<context.length;icontext++) {

			String c = context[icontext];

			WebAppContext webapp = new WebAppContext();
			webapp.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
			webapp.setWelcomeFiles(new String[] { 
					"index.html", "index.jsp"
			});
			webapp.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
			webapp.setResourceBase("web/http/"+c);
			webapp.setContextPath("/"+c);
			webapp.setParentLoaderPriority(true);
			webapp.setErrorHandler(new ErrorHandler());
			webapp.getSessionHandler().getSessionManager().addEventListener(new HttpSessionCollector());
			contextsAppHttp.addHandler(webapp);

		}
		
		//Load all virtual hosts
		JSONObject vhosts = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "app vhost show \"http\";"));
		
		for(Object o : vhosts.keySet()) {
			
			String cur_hostname = (String) o;
			String cur_context = (String) vhosts.get(cur_hostname);
			
			WebAppContext webapp = new WebAppContext();
			webapp.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
			webapp.setWelcomeFiles(new String[] { 
					"index.html", "index.jsp"
			});
			webapp.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
			webapp.setResourceBase("web/http/"+cur_context);
			webapp.setContextPath("/");
			String[] virtualHost = {cur_hostname};
			webapp.setVirtualHosts(virtualHost);
			webapp.setParentLoaderPriority(true);
			webapp.setErrorHandler(new ErrorHandler());
			webapp.getSessionHandler().getSessionManager().addEventListener(new HttpSessionCollector());
			contextsAppHttp.addHandler(webapp);
			
		}

		Start.webServerAppHttp.setHandler(contextsAppHttp);
		Start.webServerAppHttp.start();

	}

	public static void start_webserver_https(SessionThread session) throws Exception {

		Start.webServerAppHttps = new Server(Start.WEB_SERVER_PORT_APP_HTTPS);

		//Set the ssl context factory
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStoreType("PKCS12");
		sslContextFactory.setKeyStorePath("cert/cu.p12");
		sslContextFactory.setKeyStorePassword(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_KEYSTORE_PASSWORD"));

		//Generate the http configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(Start.WEB_SERVER_PORT_APP_HTTPS);
		http_config.setOutputBufferSize(32768);

		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());

		@SuppressWarnings("resource")
		ServerConnector https = new ServerConnector(Start.webServerAppHttps,
				new SslConnectionFactory(sslContextFactory,"http/1.1"),
				new HttpConnectionFactory(https_config));
		https.setPort(Start.WEB_SERVER_PORT_APP_HTTPS);

		Connector[] connectorHttps = {null};
		connectorHttps[0] = https;

		//Set the connectors
		Start.webServerAppHttps.setConnectors(connectorHttps);

		String[] configuration = new String[] {
				"org.eclipse.jetty.webapp.WebInfConfiguration",
				"org.eclipse.jetty.webapp.WebXmlConfiguration",
				"org.eclipse.jetty.webapp.MetaInfConfiguration",
				"org.eclipse.jetty.webapp.FragmentConfiguration",
				"org.eclipse.jetty.plus.webapp.EnvConfiguration",
				"org.eclipse.jetty.plus.webapp.PlusConfiguration",
				"org.eclipse.jetty.annotations.AnnotationConfiguration",
		"org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };

		ContextHandlerCollection contextsAppHttps = new ContextHandlerCollection();

		String[] context = (new File("web"+File.separator+"https")).list();
		for(int icontext = 0;icontext<context.length;icontext++) {

			String c = context[icontext];

			WebAppContext webapp = new WebAppContext();
			webapp.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
			webapp.setWelcomeFiles(new String[] { 
					"index.html", "index.jsp"
			});
			webapp.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
			webapp.setResourceBase("web/https/"+c);
			webapp.setContextPath("/"+c);
			webapp.setParentLoaderPriority(true);
			webapp.setErrorHandler(new ErrorHandler());
			webapp.getSessionHandler().getSessionManager().addEventListener(new HttpSessionCollector());
			contextsAppHttps.addHandler(webapp);

		}
		
		//Load all virtual hosts
		JSONObject vhosts = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "app vhost show \"https\";"));
		
		for(Object o : vhosts.keySet()) {
			
			String cur_hostname = (String) o;
			String cur_context = (String) vhosts.get(cur_hostname);
			
			WebAppContext webapp = new WebAppContext();
			webapp.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
			webapp.setWelcomeFiles(new String[] { 
					"index.html", "index.jsp"
			});
			webapp.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
			webapp.setResourceBase("web/https/"+cur_context);
			webapp.setContextPath("/");
			String[] virtualHost = {cur_hostname};
			webapp.setVirtualHosts(virtualHost);
			webapp.setParentLoaderPriority(true);
			webapp.setErrorHandler(new ErrorHandler());
			webapp.getSessionHandler().getSessionManager().addEventListener(new HttpSessionCollector());
			contextsAppHttps.addHandler(webapp);
			
		}

		Start.webServerAppHttps.setHandler(contextsAppHttps);
		Start.webServerAppHttps.start();

	}

}
