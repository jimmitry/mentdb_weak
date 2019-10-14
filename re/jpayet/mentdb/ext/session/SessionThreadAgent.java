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

package re.jpayet.mentdb.ext.session;

import java.util.concurrent.ConcurrentHashMap;

import re.jpayet.mentdb.ext.server.Start;

//The server thread agents
public class SessionThreadAgent extends Thread {

	//Initialization
	public static ConcurrentHashMap<Long, SessionThreadAgent> allServerThread = new ConcurrentHashMap<Long, SessionThreadAgent>();
	public SessionThread serverThread = null;
	public static long nbOpenAgent = 0;

	//Constructor
	public SessionThreadAgent(SessionThread serverThread) {

		//Reset the default value
		this.serverThread = serverThread;

	}

	//Thread run method ...
	public void run () {
		
		//Increment the number of loaded agent
		nbOpenAgent++;
		
		//Save the agent for another access
		allServerThread.put(this.serverThread.idConnection, this);

		//Loop for test the main thread
		while (serverThread.isAlive()) {

			//Break loop if timeout
			if ((serverThread.life+Start.SESSION_TIMEOUT) < System.currentTimeMillis() || !Start.listening) {

				if (!serverThread.user.equals("ai") || !Start.listening) {
					break;
				}

			}

			//Wait
			try {

				Thread.sleep(500);

			} catch (InterruptedException t) {}

		}

		//Try to close the socket
		try {

			this.serverThread.socket.close();

		} catch (Exception e) {
			
		} finally {
			
			this.serverThread.isInterrupted = true;
			
			//Decrement the number of loaded agent
			nbOpenAgent--;
			
		}

	}

}