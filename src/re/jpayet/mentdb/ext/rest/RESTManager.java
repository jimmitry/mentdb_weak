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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import re.jpayet.mentdb.ext.script.ScriptManager;

//The REST class
public class RESTManager extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            
            response.getWriter().println(ScriptManager.execute(request, response));
            
            
		} catch (Exception e) {

			String msg = (e.getMessage()+"");
			if (msg.startsWith("\n")) {
				msg = msg.substring(1);
			}
			msg = msg.replace("\n", "<br>");
			
			if ((e.getMessage()+"").startsWith("400-")) response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg.substring(4));
			else if ((e.getMessage()+"").startsWith("401-")) response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg.substring(4));
			else if ((e.getMessage()+"").startsWith("403-")) response.sendError(HttpServletResponse.SC_FORBIDDEN, msg.substring(4));
			else response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
			
		}
		
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		execute(request, response);
		
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		execute(request, response);
		
    }
	
	@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		execute(request, response);
		
    }
	
	@Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		
		execute(request, response);
		
    }

}
