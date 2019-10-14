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

package re.jpayet.mentdb.ext.tools;

import re.jpayet.mentdb.ext.client.MentDBConnector;

public class test {

	public static void main(String[] args) {

		//Initialization
		MentDBConnector connector = null;

		//Try to connect and execute MQL queries
		try {
			
			//Server connection
			connector = new MentDBConnector("localhost", 9998, 10000, 10000, "pwd");
			
			//User connection
			if (connector.connect("admin", "pwd")) {
				
				//Display connection information
				System.out.println(connector.serverConnectionState);
				System.out.println(connector.clientConnectionState);
				
				try {
					
					//Execute a MQL query
					System.out.println(connector.execute("who"));
					
					//Execute another MQL query
					System.out.println(connector.execute("sessions"));
					
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
