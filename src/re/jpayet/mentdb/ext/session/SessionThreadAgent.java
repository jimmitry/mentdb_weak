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

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;

//The server thread agents
public class SessionThreadAgent extends Thread {

	//Initialization
	public static ConcurrentHashMap<Long, SessionThreadAgent> allServerThread = new ConcurrentHashMap<Long, SessionThreadAgent>();
	public SessionThread serverThread = null;
	public static long nbOpenAgent = 0;
	public String origin = "";
	public String current_mql = "";
	public String last_mql = "";
	public String last_result = "";
	public String current_function = "";

	//Constructor
	public SessionThreadAgent(SessionThread serverThread, String origin, String current_mql, String last_result) {

		//Reset the default value
		this.serverThread = serverThread;
		serverThread.sessionThreadAgent = this;
		this.origin = origin;
		this.current_mql = StringFx.substr(current_mql, "0", "2000");
		this.last_mql = this.current_mql;
		this.last_result = StringFx.right(last_result, "5000");
		
		try {
			Misc.create("data"+File.separator+"transaction"+File.separator+"sid_"+this.serverThread.idConnection+".txt", 
					"################# Timestamp #################\n"+DateFx.current_timestamp()+"\n\n"+
					"################# Origin #################\n"+this.origin+"\n\n"+
					"################# User #################\n"+this.serverThread.user+"\n\n"+
					"################# Current MQL #################\n"+this.current_mql+"\n\n"+
					"################# Last MQL #################\n"+this.last_mql+"\n\n"+
					"################# Last result #################\n"+this.last_result);
		} catch (Exception e) {}

	}
	
	public void reset_origin(String origin, String current_mql, String last_mql, String last_result) {
		
		this.origin = origin;
		this.current_mql = StringFx.substr(current_mql, "0", "2000");
		if (!last_mql.equals("")) {
			this.last_mql = StringFx.substr(last_mql, "0", "2000");
		}
		this.last_result = StringFx.right(last_result, "5000");
		
		try {
			Misc.create("data"+File.separator+"transaction"+File.separator+"sid_"+this.serverThread.idConnection+".txt", 
					"################# Timestamp #################\n"+DateFx.current_timestamp()+"\n\n"+
					"################# Origin #################\n"+this.origin+"\n\n"+
					"################# User #################\n"+this.serverThread.user+"\n\n"+
					"################# Current MQL #################\n"+this.current_mql+"\n\n"+
					"################# Last MQL #################\n"+this.last_mql+"\n\n"+
					"################# Last result #################\n"+this.last_result);
		} catch (Exception e) {}
		
	}
	
	public void close_transaction() {
		
		try {
			Misc.deleteFile("data"+File.separator+"transaction"+File.separator+"sid_"+this.serverThread.idConnection+".txt");
		} catch (Exception e) {}
		
	}

	//Thread run method ...
	public void run () {
		
		//Increment the number of loaded agent
		nbOpenAgent++;

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