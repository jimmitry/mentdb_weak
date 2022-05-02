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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.ext.fx.FileFx;
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

        String title1 = "MentDB Weak";
        String title2 = "Smart-Earth tool";
        String title3 = "Download and install more applications from <a href='https://www.mentdb.org' style='color:#fff'>www.mentdb.org</a>";
        
        try {title1 = FileFx.ini(null, "conf"+File.separator+"server.conf", "WEBSERVER", "WEB_PORTAL_TITLE_1");} catch (Exception e) {}
        try {title2 = FileFx.ini(null, "conf"+File.separator+"server.conf", "WEBSERVER", "WEB_PORTAL_TITLE_2");} catch (Exception e) {}
        try {title3 = FileFx.ini(null, "conf"+File.separator+"server.conf", "WEBSERVER", "WEB_PORTAL_TITLE_3");} catch (Exception e) {}
        
        String top = "<html lang=\"en\">\n"+
    		"<head>\n"+
    		"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"+
    		"    <!-- Meta, title, CSS, favicons, etc. -->\n"+
    		"    <meta charset=\"utf-8\">\n"+
    		"    <link rel=\"icon\" type=\"image/png\" href=\"../web/images/db32x32.png\" />\n"+
    		"    <title>Portal | MentDB Weak</title>"
    		+ "	 <style type=\"text/css\">"
    		+ 	 ".portal_a {\r\n" + 
    		"        color:#131313;\r\n" + 
    		"        background-color:#e2e2e2;\r\n" + 
    		"        border:1px #dcdcdc solid;\r\n" + 
    		"        width:138px;\r\n" + 
    		"        height:138px;\r\n" + 
    		"        background-size:50px 50px;\r\n" + 
    		"        background-repeat: no-repeat;\r\n" + 
    		"        background-position: 50% 40%;	\r\n" + 
    		"        display: inline-block;\r\n" + 
    		"        margin-right: 5px;\r\n" + 
    		"        margin-bottom: 10px;\r\n" + 
    		"        text-align: center;	\r\n" + 
    		"        line-height: 234px;\r\n" + 
    		"        text-decoration: none;\r\n" + 
    		"        -webkit-border-radius: 5px;\r\n" + 
    		"        -moz-border-radius: 5px;\r\n" + 
    		"        border-radius: 5px;\r\n" + 
    		"    }\r\n" + 
    		"    .portal_a:hover {\r\n" + 
    		"        background-color:#FFF;\r\n" + 
    		"        border: 1px #D0D0D0 solid;\r\n" + 
    		"        color:#222;\r\n" + 
    		"    }" + 
    		"	 </style>\n"+
    		"</head>\n"+
    		"<body id='top' style=\"background-color: #3d3c3c;background-image: url(../web/images/bg.png);background-repeat: repeat-x;font-size:13px;margin:0px;font-family: 'Helvetica Neue', Roboto, Arial, 'Droid Sans', sans-serif;margin:0px;\">\n"+
    		"<div class='top_screean' style=\"background-image: -webkit-linear-gradient(top, #272727 0%, #212121 100%);\r\n" + 
    		"    background-image: -o-linear-gradient(top, #272727 0%, #212121 100%);\r\n" + 
    		"    background-image: -webkit-gradient(linear, left top, left bottom, from(#272727), to(#212121));\r\n" + 
    		"    background-image: linear-gradient(to bottom, #3c3c3c 0%, #101010 100%);\r\n" + 
    		"    margin-bottom: 0px;\r\n" + 
    		"    height: 71px;\r\n" + 
    		"    border: 1px #080808 solid;\r\n" + 
    		"    border-bottom: 0px;\r\n" + 
    		"    width: 100%;\">\r\n" + 
    		"      <div style='width: 100%;color: #E7E7E7;margin-top: 12px;margin-left: 20px;'>\r\n" + 
    		"          <img src='../web/images/db128x128.png' style='width: 56px;\r\n" + 
    		"    float: left;\r\n" + 
    		"    margin-left: -14px;\r\n" + 
    		"    margin-top: -5px;\r\n" + 
    		"    margin-right: 10px;'> " + 
    		"          <span class='top_title' style='font-size: 22px;display: inline-block;line-height: 20px;vertical-align: middle;'>"+title1+"<small style='font-size: 55%;'>&nbsp;"+title2+"</small><br><small style='font-size: 65%;'>"+title3+"</small></span>\r\n" + 
    		"      </div>\r\n" + 
    		"</div>\n<div style='width: 100%;padding-top:10px;background-color: #cccccc;'>";
    		
    		String result = "<div style='margin-left:10px'>";
            
        try {
        	
	        	String app = Database.execute_admin_mql(null, "app show \"http\";");
	    		
	    		JSONArray a = (JSONArray) JsonManager.load(app);
	    		
	    		for(int i=0;i<a.size();i++) {
	    			
	    			String curapp = (String) a.get(i);
	    			
	    			String conf = Database.execute_admin_mql(null, "execute (concat \"app.100.template.\" (json load \"http_node\" (node show \"APP[http_"+curapp+"]\");\n" + 
	    					"json select \"http_node\" /template) \".conf\");");
	    			
	    			JSONObject c = (JSONObject) JsonManager.load(conf);
	    			
	    			String logo = (String) c.get("login_logo");
	    			String title = (String) c.get("title");
	    			
	    			result += "<a class='portal_a' style='height: 130px;border-bottom: 9px #D70000 solid;background-image:url(http://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTP+"/"+curapp+"/images/"+logo+")' href='http://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTP+"/"+curapp+"'>"+title+"</a>";
	              
	    		}
	    		
	    		app = Database.execute_admin_mql(null, "app show \"https\";");
	    		
	    		a = (JSONArray) JsonManager.load(app);
	    		
	    		for(int i=0;i<a.size();i++) {
	    			
	    			String curapp = (String) a.get(i);
	    			
	    			String conf = Database.execute_admin_mql(null, "execute (concat \"app.100.template.\" (json load \"https_node\" (node show \"APP[https_"+curapp+"]\");\n" + 
	    					"json select \"https_node\" /template) \".conf\");");
	    			
	    			JSONObject c = (JSONObject) JsonManager.load(conf);
	    			
	    			String logo = (String) c.get("login_logo");
	    			String title = (String) c.get("title");
	    			
	    			result += "<a class='portal_a' style='height: 130px;border-bottom: 9px #D70000 solid;background-image:url(https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTPS+"/"+curapp+"/images/"+logo+")' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_APP_HTTPS+"/"+curapp+"'>"+title+"</a>";
	              
	    		}
	    		
	    		result += "<a class='portal_a' style='background-image:url(../web/images/bot128.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/bots' target='_blank'>Bots</a>";
	        result += "<a class='portal_a' style='background-image:url(../web/images/apiportal.png)' href='https://"+Start.WEB_SERVER_HOST+":"+Start.WEB_SERVER_PORT_HTTPS+"/docs' target='_blank'>REST API</a>";
            
        } catch (Exception e) {
        	
        		System.out.println("Portal err: "+e.getMessage());
        	
        }
        
        result += "</div>";
        
        pw.println(top+result+"</div><div style='display: table;width: 800px;margin-left:auto;margin-right:auto;color:#fff'><br><br><center>&copy; "+Start.copyright+" - <a href='https://www.mentdb.org' style='color:#fff'>www.mentdb.org</a>.</center><br><br>&nbsp;</div>");
        pw.println("</body></html>");
        response.flushBuffer();
		
    }

}
