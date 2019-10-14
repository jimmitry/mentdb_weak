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

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;

public class HttpSessionCollector implements HttpSessionListener {
	
	//Initialization
	public static HashMap<String, HttpSession> sessions = new HashMap<String, HttpSession>();

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		
		//When the session was created
		HttpSession session = event.getSession();
		sessions.put(session.getId(), session);
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		
		//Initialization
		SessionThread thread = (SessionThread) sessions.get(event.getSession().getId()).getAttribute("env");
		
		if (thread!=null) {
			
			if (thread.idConnection>0) {
				
				try {
					SessionThread.closeSession(thread.env, thread.idConnection);
				} catch (Exception e) {}
				try {
					SessionThreadAgent.allServerThread.remove(thread.idConnection);
				} catch (Exception e) {}
				
			}
			
		}
		
		sessions.remove(event.getSession().getId());
		
	}
}