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

package re.jpayet.mentdb.ext.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.file.data.DataFile;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.index.IndexFile;
import re.jpayet.mentdb.core.db.file.index.IndexFilePool;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.doc.MQLDocumentation;
import re.jpayet.mentdb.ext.doc.MentDBFeature;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.portal.PortalManager;
import re.jpayet.mentdb.ext.rest.RESTManager;
import re.jpayet.mentdb.ext.rest.REST_DOCManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.UserManager;

//MentDB Server
public class Start
{
	
	//To change the MentDB Server version
	static public String version = "2.1.9";
	static public String copyright = "2012 - "+thisYear();
	static public boolean newDatabase = false;
	
	//Initialization
	public static Database db = null;
	public static ServerSocket serverSocket = null;
	public static boolean listening = true;
	public static long exceededSessions = 0;
	public static Server webServerHttps = null;
	public static Server webServerAppHttp = null;
	public static Server webServerAppHttps = null;
	
	//Configuration
	static public String REMOTE_ACCESS = "DENIED";
	static public int SERVER_PORT = 0;
	static public int SESSION_TIMEOUT = 0;
	static public int SESSION_CONNECT_TIMEOUT = 0;
	static public int NB_INDEX_FILE = 0;
	static public int MAX_SESSION = 0;
	static public int WEB_SERVER_PORT_HTTPS = 0;
	static public int WEB_SERVER_PORT_APP_TIMEOUT = 0;
	static public int WEB_SERVER_PORT_APP_HTTP = 0;
	static public int WEB_SERVER_PORT_APP_HTTPS = 0;
	static public String WEB_SERVER_HOST = "";
	static public int JWT_TIMEOUT = 0;
	static public String NATIVE_LANGUAGE = "";
	static public String AI_FIRST_NAME = "";
	static public String AI_LAST_NAME = "";
	static public int CHECK_DOCUMENTATION = 0;
	static public int CERT_KEY_SIZE = 0;
	static public long CLUSTER_LIFE_BEFORE_EXPULSION = 30000;
	static public long CA_DAY_LIMIT = 0;
	static public String CA_EMAIL_ADDRESS = "";
	static public String CA_CN = "";
	static public String CA_OU = "";
	static public String CA_O = "";
	static public String CA_L = "";
	static public String CA_ST = "";
	static public String CA_C = "";
	static public long CU_DAY_LIMIT = 0;
	static public String CU_EMAIL_ADDRESS = "";
	static public String CU_CN = "";
	static public String CU_OU = "";
	static public String CU_O = "";
	static public String CU_L = "";
	static public String CU_ST = "";
	static public String CU_C = "";
	static public String CU_KEYSTORE_PASSWORD = "";
	static public String CU_ALT_HOSTNAME = "";
	static public String LOG_RETENTION_DAYS = "";
	static public String LOG_ARCHIVE_PATH = "";
	static public String LOG_ARCHIVE_SIZE = "";

	static public String MySQL_DB = "";
	static public String MySQL_USER = "";
	static public String MySQL_PWD = "";
	static public String MySQL_HOST = "";
	static public String MySQL_PORT = "";
	
	//Get the current year
	public static String thisYear() {
		
		try {
			return DateFx.year(DateFx.curdate());
		} catch (Exception e) {
			return "2050";
		}
		
	}
	
	public static void reload_config() throws NumberFormatException, Exception {
		
		SmtpManager.PROCESS_LIMIT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "MAIL", "process_limit"));
		StackManager.PROCESS_LIMIT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "STACK", "process_limit"));
		LOG_ARCHIVE_SIZE = Misc.conf_value("conf"+File.separator+"server.conf", "LOG", "ARCHIVE_SIZE");
		LOG_RETENTION_DAYS = Misc.conf_value("conf"+File.separator+"server.conf", "LOG", "RETENTION_DAYS");
		JWT_TIMEOUT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "JWT_TIMEOUT"));
		REMOTE_ACCESS = Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "REMOTE_ACCESS");
		SESSION_CONNECT_TIMEOUT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "SESSION_CONNECT_TIMEOUT"));
		SESSION_TIMEOUT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "SESSION_TIMEOUT"));
		WEB_SERVER_HOST = Misc.conf_value("conf"+File.separator+"server.conf", "WEBSERVER", "WEB_SERVER_HOST");
		WEB_SERVER_PORT_HTTPS = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "WEBSERVER", "WEB_SERVER_PORT_HTTPS"));
		WEB_SERVER_PORT_APP_TIMEOUT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "WEBSERVER", "WEB_SERVER_PORT_APP_TIMEOUT"));
		WEB_SERVER_PORT_APP_HTTP = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "WEBSERVER", "WEB_SERVER_PORT_APP_HTTP"));
		WEB_SERVER_PORT_APP_HTTPS = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "WEBSERVER", "WEB_SERVER_PORT_APP_HTTPS"));
		LOG_ARCHIVE_PATH = Misc.conf_value("conf"+File.separator+"server.conf", "LOG", "ARCHIVE_PATH");
		
		MAX_SESSION = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "MAX_SESSION"));
		CLUSTER_LIFE_BEFORE_EXPULSION = Long.parseLong(Misc.conf_value("conf"+File.separator+"server.conf", "CLUSTER", "life_before_expulsion"));
		
	}
	
	//Constructor
	public Start() throws Exception {
		
		//Create the log directory if not exist
		if (!(new File("logs")).exists()) {
			
			(new File("logs")).mkdir();
			
		}

		System.out.println("/////////////////////////////////////////////////////////////////");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("+ Mentalese Database Engine (MentDB)                    v_"+Start.version+" +");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("-----------------------------------------------------------------");

		//Display MentDB information
		Log.trace("/////////////////////////////////////////////////////////////////");
		Log.trace("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Log.trace("+ Mentalese Database Engine (MentDB)                    v_"+Start.version+" +");
		Log.trace("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Log.trace("-----------------------------------------------------------------");
		
		//Load from the config file
		Misc.system_out_print("Loading global config from 'server.conf' > ", false, "", "");
		
		CHECK_DOCUMENTATION = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DOCUMENTATION", "check_documentation"));
		SERVER_PORT = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "SERVER_PORT"));
		NB_INDEX_FILE = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "NB_INDEX_FILE"));
		IndexFilePool.NB_INDEX_FILE_OPEN = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "NB_INDEX_FILE_OPEN"));
		DataFilePool.NB_DATA_FILE_OPEN = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "NB_DATA_FILE_OPEN"));
		DataFile.BLOCK_SIZE = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "DATA_BLOCK_SIZE"));
		DataFile.BLOCK_SIZE_ADD_TO_BLOCK = DataFile.BLOCK_SIZE+DataFile.ADD_TO_BLOCK;
		DataFile.EMPTY_BLOCK = StringUtils.repeat(" ", DataFile.BLOCK_SIZE);
		DataFile.MAX_BLOCK = Long.parseLong(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "MAX_DATA_BLOCK_SIZE"));
		IndexFile.MAX_INDEX_BLOCK_SIZE = Long.parseLong(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "MAX_INDEX_BLOCK_SIZE"));
		IndexFile.EXTEND_FS_FORCE = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "EXTEND_FS_FORCE"));
		Record.RECORD_CACHE_SIZE = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "RECORD_CACHE_SIZE"));
		Start.NATIVE_LANGUAGE = Misc.conf_value("conf"+File.separator+"server.conf", "DATABASE", "NATIVE_LANGUAGE");
		Start.AI_FIRST_NAME = Misc.conf_value("conf"+File.separator+"server.conf", "AI", "FIRSTNAME");
		Start.AI_LAST_NAME = Misc.conf_value("conf"+File.separator+"server.conf", "AI", "LASTNAME");
		CERT_KEY_SIZE = Integer.parseInt(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CERT_KEY_SIZE"));
		CA_DAY_LIMIT = Long.parseLong(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_DAY_LIMIT"));
		CA_EMAIL_ADDRESS = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_EMAIL_ADDRESS");
		CA_CN = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_CN");
		CA_OU = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_OU");
		CA_O = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_O");
		CA_L = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_L");
		CA_ST = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_ST");
		CA_C = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CA_C");
		CU_DAY_LIMIT = Long.parseLong(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_DAY_LIMIT"));
		CU_EMAIL_ADDRESS = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_EMAIL_ADDRESS");
		CU_CN = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_CN");
		CU_OU = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_OU");
		CU_O = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_O");
		CU_L = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_L");
		CU_ST = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_ST");
		CU_C = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_C");
		CU_KEYSTORE_PASSWORD = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_KEYSTORE_PASSWORD");
		CU_ALT_HOSTNAME = Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_ALT_HOSTNAME");
		MySQL_HOST = Misc.conf_value("conf"+File.separator+"server.conf", "MYSQL", "host");
		MySQL_PORT = Misc.conf_value("conf"+File.separator+"server.conf", "MYSQL", "port");
		MySQL_DB = Misc.conf_value("conf"+File.separator+"server.conf", "MYSQL", "db");
		MySQL_USER = Misc.conf_value("conf"+File.separator+"server.conf", "MYSQL", "user");
		MySQL_PWD = Misc.conf_value("conf"+File.separator+"server.conf", "MYSQL", "pwd");
		
		Start.reload_config();
		
		Misc.system_out_print("[OK]", true, "", "");
		Log.trace("Loading global config from 'server.conf' > OK");
		
		try {
			
			//Allow remote connections or denied
			if (REMOTE_ACCESS.equals("ALLOW")) {
				serverSocket = new ServerSocket(Start.SERVER_PORT);
			}
			else serverSocket = new ServerSocket(Start.SERVER_PORT, 0, InetAddress.getByName(null));
			
			//Check if create a new database
			if (!(new File("data").exists())) {
				
				Start.newDatabase = true;
			
			}
			
			if (IndexFile.EXTEND_FS_FORCE==2) {

				Start.newDatabase = true;

				FileFx.delete("data"+File.separator+"data");
				FileFx.delete("data"+File.separator+"index");
				
			}
			
			Security.addProvider(new BouncyCastleProvider());
			
			//Get sha1 for CA and CU
			String ca_saved_md5 = "", cu_saved_md5 = "";
			if ((new File("cert"+File.separator+"ca.md5")).exists()) ca_saved_md5 = Misc.load("cert"+File.separator+"ca.md5").replace("\n", "").replace("\r", "");
			if ((new File("cert"+File.separator+"cu.md5")).exists()) cu_saved_md5 = Misc.load("cert"+File.separator+"cu.md5").replace("\n", "").replace("\r", "");

			if (!ca_saved_md5.equals(StringFx.md5(Misc.build_CA_DN()))) {
				Misc.system_out_print("Generate CA and CU > ", false, "", "");
		        
				Misc.generate_ca();
				Misc.generate_cu();
				
				Misc.system_out_print("[OK]", true, "", "");
		        Log.trace("Generate CA and CU > [OK]");
			} else if (!cu_saved_md5.equals(StringFx.md5(Misc.build_CU_DN()))) {
				Misc.system_out_print("Generate CU > ", false, "", "");
				
				Misc.generate_cu();
				
				Misc.system_out_print("[OK]", true, "", "");
		        Log.trace("Generate CU > [OK]");
		        
			}
			
			Misc.system_out_print("Open MySQL connection > ", false, "", "");
			MYSQLManager.open(newDatabase);
			Misc.system_out_print("[OK]", true, "", "");
			Log.trace("Open MySQL connection > [OK]");
			
			if (newDatabase) Misc.system_out_print("Create MentDB database > ", false, "", "");
			else Misc.system_out_print("Open MentDB database > ", false, "", "");
			
			//Open the database
			db = new Database();
			db.open();
			
			if (newDatabase) Misc.system_out_print("[OK]", true, "", "100/100%");
			else Misc.system_out_print("[OK]", true, "", "");
			
			if (newDatabase) Log.trace("Create MentDB database > [OK]");
			else Log.trace("Open MentDB database > [OK]");
			
			Misc.system_out_print("Rollback last transactions > ", false, "", "");
			
			Misc.create("data"+File.separator+".id", StringFx.generate_random_str("12"));
			
			//Roll back
			String[] userTransactions = (new File("data"+File.separator+"transaction")).list();
			//Parse all transactions
			for(int itransaction = 0;itransaction<userTransactions.length;itransaction++) {
				if ((new File("data"+File.separator+"transaction"+File.separator+userTransactions[itransaction]+File.separator+"rollback.log")).exists()) {
					long sessionId = Long.parseLong(userTransactions[itransaction]);
					//Open the log file
					Transaction.logFiles.put(sessionId, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback.log", "rw"));
					Transaction.rollback(sessionId);
					if (sessionId!=0) {
						Transaction.logFiles.get(sessionId).close();
						Misc.deleteFile("data"+File.separator+"transaction"+File.separator+sessionId);
					}
				}
			}
			
			Misc.system_out_print("[OK]", true, "", "");
			
			Log.trace("Rollback last transactions > [OK]");
			
			if (newDatabase) {

				System.out.println("-----------------------------------------------------------------");
				System.out.println("DATABASE INITIALIZATION                                      [..]");
				System.out.println("BEGIN -----------------------------------------------------------");
				
				//Initialization
				Database.init();

				System.out.println("END -------------------------------------------------------------");

				Log.trace("Database initialization > [OK]");
				
			} else {
			
				Misc.system_out_print("Load global parameters > ", false, "", "");
				
				//Initialize synchronized functions
				Database.loadSynchronizedCommand();
				
				//Load the global parameter
				ParameterManager.load(0);
				
				//Load the global cluster
				ClusterManager.load(0);
				
				//Load the language manager
				LanguageManager.load(0);
				
				//Load the concentration manager
				ConcentrationManager.load();
				
				Misc.system_out_print("[OK]", true, "", "");
				
				Log.trace("Load global parameters > [OK]");
			
			}
			
			Misc.system_out_print("Load index files > ", false, "", "");
			for(int i=0;i<IndexFilePool.NB_INDEX_FILE_OPEN;i++) {
				IndexFilePool.if_get(i);
			}
			Misc.system_out_print("[OK]", true, "", "");
			Log.trace("Load index files > [OK]");
			
			if (IndexFile.EXTEND_FS_FORCE==1) {

				Database.rebuild_index1();
				
			} else if (IndexFile.EXTEND_FS_FORCE==2) {

				Database.rebuild_index2();
				
			}
			
			Misc.system_out_print("Load bots > ", false, "", "");
			
			BotManager.init();
			
			Misc.system_out_print("[OK]", true, "", "");
			
			//Update the admin password
			UserManager.updatePassword(0, "admin", Misc.conf_value("conf"+File.separator+"server.conf", "ADMIN", "PWD"));
			NodeManager.oinsert(0, "U[admin]", "/", "sKey", Misc.conf_value("conf"+File.separator+"server.conf", "ADMIN", "SERVER_ENCRYPT_KEY"), "STR");
			
			//Starting the web server
			Misc.system_out_print("Start web server (mentdb:"+WEB_SERVER_PORT_HTTPS+", https:"+WEB_SERVER_PORT_APP_HTTPS+", http:"+WEB_SERVER_PORT_APP_HTTP+") > ", false, "", "");
			webServerHttps = new Server(WEB_SERVER_PORT_HTTPS);
			
			//Set the ssl context factory
			SslContextFactory sslContextFactory = new SslContextFactory();
	        sslContextFactory.setKeyStoreType("PKCS12");
	        sslContextFactory.setKeyStorePath("cert/cu.p12");
	        sslContextFactory.setKeyStorePassword(Misc.conf_value("conf"+File.separator+"server.conf", "SECURITY", "CU_KEYSTORE_PASSWORD"));
	        
	        //Generate the http configuration
			HttpConfiguration http_config = new HttpConfiguration();
			http_config.setSecureScheme("https");
			http_config.setSecurePort(WEB_SERVER_PORT_HTTPS);
			http_config.setOutputBufferSize(32768);
	 
	        HttpConfiguration https_config = new HttpConfiguration(http_config);
	        https_config.addCustomizer(new SecureRequestCustomizer());
	        
	        @SuppressWarnings("resource")
			ServerConnector https = new ServerConnector(webServerHttps,
	            new SslConnectionFactory(sslContextFactory,"http/1.1"),
	            new HttpConnectionFactory(https_config));
	        https.setPort(WEB_SERVER_PORT_HTTPS);
	        
	        Connector[] connectorHttps = {null};
	        connectorHttps[0] = https;
			
			//Set the connectors
			webServerHttps.setConnectors(connectorHttps);
			
			String[] configuration = new String[] {
		    	     "org.eclipse.jetty.webapp.WebInfConfiguration",
		    	     "org.eclipse.jetty.webapp.WebXmlConfiguration",
		    	     "org.eclipse.jetty.webapp.MetaInfConfiguration",
		    	     "org.eclipse.jetty.webapp.FragmentConfiguration",
		    	     "org.eclipse.jetty.plus.webapp.EnvConfiguration",
		    	     "org.eclipse.jetty.plus.webapp.PlusConfiguration",
		    	     "org.eclipse.jetty.annotations.AnnotationConfiguration",
		    	     "org.eclipse.jetty.webapp.JettyWebXmlConfiguration" };
	        ContextHandlerCollection contextsHttps = new ContextHandlerCollection();
	        
	        ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context0.setContextPath("/api");
	        context0.addServlet(new ServletHolder(new RESTManager()),"/*");
	        contextsHttps.addHandler(context0);
	        
	        ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context1.setContextPath("/docs");
	        context1.addServlet(new ServletHolder(new REST_DOCManager()),"/*");
	        contextsHttps.addHandler(context1);
	        
	        ServletContextHandler context2 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context2.setContextPath("/");
	        context2.addServlet(new ServletHolder(new PortalManager()),"/*");
	        contextsHttps.addHandler(context2);
	        
	        WebAppContext webappdocroot = new WebAppContext();
	        webappdocroot.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
	        webappdocroot.setWelcomeFiles(new String[] { 
			        "index.html", "index.jsp"
			    });
	        webappdocroot.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
	        webappdocroot.setResourceBase("docs");
	        webappdocroot.setContextPath("/web");
	        webappdocroot.setParentLoaderPriority(true);
	        webappdocroot.setErrorHandler(new ErrorHandler());
	        webappdocroot.getSessionHandler().getSessionManager().addEventListener(new MySessionListener());
	        contextsHttps.addHandler(webappdocroot);
	        
	        WebAppContext webappbrain = new WebAppContext();
	        webappbrain.getInitParams().put("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
	        webappbrain.setWelcomeFiles(new String[] { 
			        "index.html", "index.jsp"
			    });
	        webappbrain.setAttribute("org.eclipse.jetty.webapp.configuration", configuration);
	        webappbrain.setResourceBase("lisa");
	        webappbrain.setContextPath("/bots");
	        webappbrain.setParentLoaderPriority(true);
	        webappbrain.setErrorHandler(new ErrorHandler());
	        webappbrain.getSessionHandler().getSessionManager().addEventListener(new MySessionListener());
	        contextsHttps.addHandler(webappbrain);
	        
	        WebSocketServlet wsHandler = new WebSocketServlet()
            {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public void configure(WebSocketServletFactory factory)
                {
					try {
						factory.getPolicy().setIdleTimeout(Start.SESSION_TIMEOUT);
					} catch ( Exception e) {}
                    factory.register(WebSocketThread.class);
                }
                
            };
            
            ServletContextHandler sockContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
            sockContext.setErrorHandler(new ErrorHandler());
            sockContext.setContextPath("/mentdb/");
            
            ServletHolder holderEvents = new ServletHolder("ws", wsHandler);
            sockContext.addServlet(holderEvents, "/");
	        contextsHttps.addHandler(sockContext);
	        
	        
	        webServerHttps.setHandler(contextsHttps);
	        webServerHttps.start();

			AppManager.start_webserver_http(null);
			AppManager.start_webserver_https(null);
			
			Misc.system_out_print("[OK]", true, "", "");
			Log.trace("Starting the web server > [OK]");
			
			Misc.system_out_print("Start all jobs > ", false, "", "");
			JobManager.scheduler_start(0);
			Misc.system_out_print("[OK]", true, "", "");
			Log.trace("Start all jobs > [OK]");
			
			Misc.system_out_print("Execute 'boot.execute.on.start.post' > ", false, "", "");
			String mentalScript = "call \"boot.execute.on.start.post\"";
			Database.execute_admin_mql(null, mentalScript);
			Misc.system_out_print("[OK]", true, "", "");
			Log.trace("Execute 'boot.execute.on.start.post' > [OK]");
			
			Transaction.commit(0);
			//WARNING >>> "mentdb" after this point, mentdb must be use exclusively in read only
			
			System.out.println("-----------------------------------------------------------------");
			Log.trace("-----------------------------------------------------------------");
			Misc.system_out_print("Port "+SERVER_PORT+", Pid "+ManagementFactory.getRuntimeMXBean().getName().split("@", -1)[0]+" > ", false, "", "");
			Misc.system_out_print("[LISTENING]", true, "", "");
			Log.trace("Port "+SERVER_PORT+", Pid "+ManagementFactory.getRuntimeMXBean().getName().split("@", -1)[0]+" > [LISTENING]");
			
			//Try to start the server
			try {
				
				System.out.println("-----------------------------------------------------------------");
				Log.trace("-----------------------------------------------------------------");
				Misc.system_out_print("Documentation > ", false, "", "");
				Misc.system_out_print("1 ", false, "", "");
				MQLDocumentation.init();
				Misc.system_out_print("2 ", false, "", "");
				MentDBFeature.init();
				Misc.system_out_print("\r"+StringFx.rpad("Documentation >", " ", "61"), false, "", "");
				Misc.system_out_print("[OK]", true, "", "");
				Log.trace("Documentation > [OK]");
				System.out.println("-----------------------------------------------------------------");
				Log.trace("-----------------------------------------------------------------");
				System.out.println("[READY]");
				Log.trace("[READY]");
				
				//Waiting from standard in ...
				while (listening) {
					
					//Try to start a thread
					try {
						
						//Accept a connection and start a new thread
						Socket s = serverSocket.accept();
						
						if ((Transaction.logFiles.size()-1)>Start.MAX_SESSION) {
							
							//Increment the number of exceeded sessions
							exceededSessions++;

							//Trace in log file
							Log.trace("MentDB: Maximum number of sessions exceeded.");
							
							s.close();
							
						} else {
							
							s.setSoTimeout(SESSION_CONNECT_TIMEOUT);
							
							//Start the thread
							SessionThread thread = new SessionThread(s);
							thread.start();
							
							//STart the agent
							SessionThreadAgent agent = new SessionThreadAgent(thread);
							agent.start();

						}

					} catch (Exception e) {
						//Nothing
						//System.out.println("err: "+e.getMessage());
					}
				}

				System.out.println("-----------------------------------------------------------------");
				Log.trace("-----------------------------------------------------------------");
				
				Misc.system_out_print("Execute 'boot.execute.on.shutdown.post' > ", false, "", "");
				mentalScript = "call \"boot.execute.on.shutdown.post\"";
				Database.execute_admin_mql(null, mentalScript);
				Misc.system_out_print("[OK]", true, "", "");
				Log.trace("Execute 'boot.execute.on.shutdown.post' > [OK]");
				
				//Stop the web server
				Misc.system_out_print("Stop the web server > ", false, "", "");
				Start.webServerHttps.setStopTimeout(1);
				Start.webServerHttps.stop();
				Start.webServerHttps.destroy();

				AppManager.stop_webserver_http();
				AppManager.stop_webserver_https();
				
				Misc.system_out_print("[OK]", true, "", "");
				Log.trace("Stop the web server > [OK]");
				
				Misc.system_out_print("Stop all jobs > ", false, "", "");
				JobManager.scheduler_stop();
				Misc.system_out_print("[OK]", true, "", "");
				Log.trace("Stop all jobs > [OK]");
				
				Misc.system_out_print("Close database > ", false, "", "");
				
				//Close the database
				Database.execute_admin_mql(null, "database_close");
				
				userTransactions = (new File("data"+File.separator+"transaction")).list();
				//Parse all transactions
				for(int itransaction = 0;itransaction<userTransactions.length;itransaction++) {
					try {
						if ((new File("data"+File.separator+"transaction"+File.separator+userTransactions[itransaction]+File.separator+"rollback.log")).exists()) {
							long sessionId = Long.parseLong(userTransactions[itransaction]);
							
							Transaction.logFiles.get(sessionId).close();
							
						}
					} catch (Exception e) {};
				}
				
				Misc.system_out_print("[OK]", true, "", "");
				
				Log.trace("Close database > [OK]");
				
				Misc.system_out_print("Close MySQL connection > ", false, "", "");
				MYSQLManager.close();
				Misc.system_out_print("[OK]", true, "", "");
				Log.trace("Close MySQL connection > [OK]");

				System.out.println("-----------------------------------------------------------------");
				System.out.println("Bye.");

				Log.trace("-----------------------------------------------------------------");
				Log.trace("Bye.");

			} catch (IOException e) {

				//An error was generated
				System.err.println("MentDB : Could not listen on port " + Start.SERVER_PORT);

				//Exit
				System.exit(-1);
			}

		} catch(Exception e) {

			e.printStackTrace();

		}

	}

	//The main method to start the database
	public static void main(String args[]) throws Exception {

		new Start();
		
	}

    private static class MySessionListener implements HttpSessionListener {

		@Override
		public void sessionCreated(HttpSessionEvent se) {
			
		}

		@Override
		public void sessionDestroyed(HttpSessionEvent se) {
			
			if (((String)se.getSession().getAttribute("target"))!=null) {
				
				if (!((String)se.getSession().getAttribute("target")).equals("mql_editor")) {
					
					UserManager.disconnect((String)se.getSession().getAttribute("username"));
					
				}
			}
			
		}
	}
    
    public static void kill_session(long sessionId) throws Exception {
    	
    		try {
    			
    			SessionThreadAgent.allServerThread.get(sessionId).serverThread.isInterrupted = true;
    			
    		} catch (Exception e) {
    			
    		}
    		
    		try {
    			
    			ConcurrentHashMap<Integer, Statement> stms = SQLManager.allStatements.get(sessionId);
    			
    			for(Integer i : stms.keySet()) {
    				
    				try {
    					
    					Statement stm = stms.get(i);
    					
    					stm.cancel();
    					
    				} catch (Exception f) {}
    				
    			}
    			
    		} catch (Exception e) {
    			
    		}
    		
    }
    
    public static void kill_process(String pid) throws Exception {
    	
    		try {
    			
    			StackManager.loadedProcessThread.get(pid).isInterrupted = true;
    			
    		} catch (Exception e) {
    			
    			
    			
    		}
    		
    }

}
