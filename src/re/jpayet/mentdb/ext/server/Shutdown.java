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

import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.fx.FileFx;

//Shutdown MentDB Server
public class Shutdown
{
	
	//The main method to shutdown the database
	public static void main(String args[]) throws Exception {

		//Initialization
		MentDBConnector connector = null;

		//Try to connect and execute MQL queries
		try {
			
			//Server connection
			connector = new MentDBConnector("localhost", 
					Integer.parseInt(FileFx.ini(null, "conf/server.conf", "DATABASE", "SERVER_PORT")), 10000, 10000, 
					FileFx.ini(null, "conf/server.conf", "ADMIN", "SERVER_ENCRYPT_KEY"));
			
			//User connection
			if (connector.connect("admin", FileFx.ini(null, "conf/server.conf", "ADMIN", "PWD"))) {
				
				try {
					
					//Execute a MQL query
					connector.execute("stop (id);");
					
				} catch (Exception e) {
					
					//Exception
					System.out.println(e.getMessage());

				}
				
			} else {
				
				//Display connection information
				System.out.println(connector.serverConnectionState);
				System.out.println(connector.clientConnectionState);

			}
			
		} catch (Exception e) {
			
			//A connection error was generated
			System.out.println(e.getMessage());

		} finally {

			try {

				//Close the connector
				connector.close();

			} catch (Exception e) {

			}

		}
		
	}

}
