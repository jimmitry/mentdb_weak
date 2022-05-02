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

package re.jpayet.mentdb.core.db.brain;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.Session;

//The brain
public class Brain extends Thread {
	
	public static MentDBConnector aiConnection = null;
	
	public static String user = "ai";

	//Constructor
	public Brain() {

	}

	//Thread run method ...
	public void run () {

		if (aiConnection==null) {
			
			connectAiUser();
		
		}

	}

	public static void connectAiUser() {

		//Connect to the database
		String password = "aipwd";

		try {
			
			//Connect to the local mentdb server
			aiConnection = new MentDBConnector("127.0.0.1", Start.SERVER_PORT, Start.SESSION_CONNECT_TIMEOUT, Start.SESSION_TIMEOUT, Database.execute_admin_mql(null, "user secret_key \"ai\";"));
			
			//Connect with 'ai' login
			if (aiConnection.connect(Brain.user, password)) {

				//The ai user was connected
				Session.allConnections.put(Brain.user, aiConnection);

			} else {

				throw new Exception(aiConnection.clientConnectionState);

			}
			
		} catch (Exception e) {

			Log.trace("AI Connection: Fatal error: "+e.getMessage());

		}

	}

}