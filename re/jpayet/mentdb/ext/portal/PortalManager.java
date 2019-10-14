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

package re.jpayet.mentdb.ext.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.server.Start;

//The DOC REST class
public class PortalManager extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter pw = response.getWriter();
        
        String top = "<html lang=\"en\">\n"+
    		"<head>\n"+
    		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"+
    		"    <!-- Meta, title, CSS, favicons, etc. -->\n"+
    		"    <meta charset=\"utf-8\">\n"+
    		"    <link rel=\"icon\" type=\"image/png\" href=\"../web/images/db32x32.png\" />\n"+
    		"    <title>Portal | MentDB</title>"
    		+ "	 <style type=\"text/css\">"
    		+ 	 ".portal_a {"
    		+ "	 	color:#313131;"
    		+ "	 	background-color:#F9F9F9;"
    		+ "	 	border:1px #E0E0E0 solid;"
    		+ "	 	width:146px;"
    		+ "	 	height:146px;"
    		+ "	 	background-size:64px 64px;"
    		+ "		background-repeat: no-repeat;"
    		+ "		background-position: 50% 40%;"
    		+ "	 	display: inline-block;"
    		+ "	 	margin-right: 10px;"
    		+ "	 	margin-bottom: 10px;"
    		+ "	 	text-align: center;"
    		+ "	 	line-height: 252px;"
    		+ "	 	text-decoration: none;"
    		+ "		-webkit-border-radius: 5px;"
    		+ "		-moz-border-radius: 5px;"
    		+ "		border-radius: 5px;"
    		+ "	  }" 
    		+ 	 ".portal_a:hover {"
    		+ "	 	background-color:#E9E9E9;"
    		+ "	 	border:1px #D0D0D0 solid;"
    		+ "	  }" 
    		+ 	 ".portal_app_a {"
    		+ "	 	color:#313131;"
    		+ "	 	background-color:#fcffe2;"
    		+ "	 	border:1px #E0E0E0 solid;"
    		+ "	 	width:146px;"
    		+ "	 	height:146px;"
    		+ "	 	background-size:64px 64px;"
    		+ "		background-repeat: no-repeat;"
    		+ "		background-position: 50% 40%;"
    		+ "	 	display: inline-block;"
    		+ "	 	margin-right: 10px;"
    		+ "	 	margin-bottom: 10px;"
    		+ "	 	text-align: center;"
    		+ "	 	line-height: 252px;"
    		+ "	 	text-decoration: none;"
    		+ "		-webkit-border-radius: 5px;"
    		+ "		-moz-border-radius: 5px;"
    		+ "		border-radius: 5px;"
    		+ "	  }" 
    		+ 	 ".portal_app_a:hover {"
    		+ "	 	background-color:#ebeed1;"
    		+ "	 	border:1px #D0D0D0 solid;"
    		+ "	  }" + 
    		"	 </style>\n"+
    		"</head>\n"+
    		"<body id='top' style=\"font-size:13px;margin:0px;font-family: 'Helvetica Neue', Roboto, Arial, 'Droid Sans', sans-serif;margin:0px;\">\n"+
    		"<div style='display: table;width: 800px;margin-left:auto;margin-right:auto;'>\n"+
    		"  <div style='width: 800px;-webkit-border-radius: 34px;-moz-border-radius: 34px;border-radius: 34px;float: right;background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b6479), color-stop(100%, #4c5566)), #686e78;\n"+
    		"    background: -webkit-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: -moz-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: -o-linear-gradient(#444, #333), #3F3F3F;\n"+
    		"    background: linear-gradient(#444, #333), #3F3F3F;background-color: #F0F0F0;height:67px;color:#E7E7E7;line-height:65px;'>\n"+
    		"    <a href='../' style='float: left;'><img src=\"../web/images/db128x128.png\" style='margin-top: 1px;width: 65px;vertical-align: middle;margin-left: 1px;'></a>\n"+
    		"    &nbsp; &nbsp; &nbsp; <span style='font-size:22px'>Portal<small style='float:right;margin-right:30px;font-size: 16px;'>contact[at]mentdb.org</small></span>&nbsp; \n";
    		top += "  </div>\n"+
    			"</div>\n<div style='width: 800px;margin-left:auto;margin-right:auto;margin-top:20px'>";
    		
    		String result = "<div style='margin-left:10px'>";
            result += "<a class='portal_a' style='background-image:url(../web/images/db128x128.png)' href='https://www.mentdb.org' target='_blank'>MentDB Project</a>";
            result += "<a class='portal_a' style='background-image:url(../web/images/apiportal.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/docs' target='_blank'>REST API</a>";
            result += "<a class='portal_a' style='background-image:url(../web/images/docportal.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/web/mql.html' target='_blank'>MQL Documentation</a>";
            result += "<a class='portal_a' style='background-image:url(../web/images/db128x128.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/bots' target='_blank'>Bots</a>";
            
        try {
        	
	        	String app = Database.execute_admin_mql(null, "app show \"http\";");
	    		
	    		JSONArray a = (JSONArray) JsonManager.load(app);
	    		
	    		for(int i=0;i<a.size();i++) {
	    			
	    			String curapp = (String) a.get(i);
	    			result += "<a class='portal_app_a' style='background-image:url(../web/images/appportal.png)' href='http://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTP+"/"+curapp+"' target='_blank'>"+curapp+"</a>";
	              
	    		}
	    		
	    		app = Database.execute_admin_mql(null, "app show \"https\";");
	    		
	    		a = (JSONArray) JsonManager.load(app);
	    		
	    		for(int i=0;i<a.size();i++) {
	    			
	    			String curapp = (String) a.get(i);
	    			result += "<a class='portal_app_a' style='background-image:url(../web/images/appportal.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTPS+"/"+curapp+"' target='_blank'>"+curapp+"</a>";
	              
	    		}
        		
        } catch (Exception e) {
        	
        		
        	
        }
        
        result += "</div>";
        
        pw.println(top+result+"</div><div style='display: table;width: 800px;margin-left:auto;margin-right:auto;'><br><br><br><hr><center>&copy; "+Start.copyright+" - <a href='https://www.mentdb.org' style='color:#333;text-decoration:underline'>MentDB</a>.</center><br><br>&nbsp;</div>");
        pw.println("</body></html>");
        response.flushBuffer();
		
    }

}
