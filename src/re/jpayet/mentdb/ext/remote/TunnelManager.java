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

package re.jpayet.mentdb.ext.remote;

import java.util.Vector;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.client.MentDBConnector;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;

public class TunnelManager {

	public static String connect(EnvManager env, String sessionId, String json) throws Exception {

		//Generate an error if session id is null or empty
		if (sessionId==null || sessionId.equals("")) {

			throw new Exception("Sorry, the MentDB session id cannot be null or empty.");

		}

		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String key = (String) conf.get("key");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String readTimeout = (String) conf.get("readTimeout");
		String subTunnels = (String) conf.get("subTunnels");

		//Generate an error if host name is null or empty
		if (hostname==null || hostname.equals("")) {

			throw new Exception("Sorry, the host name cannot be null or empty.");

		}

		//Generate an error if the port is not a number
		try {

			Integer.parseInt(port);

		} catch (Exception e) {

			throw new Exception("Sorry, the port must be numbers.");

		}

		//Generate an error if user is null or empty
		if (user==null || user.equals("")) {

			throw new Exception("Sorry, the user cannot be null or empty.");

		}

		//Generate an error if password is null or empty
		if (password==null || password.equals("")) {

			throw new Exception("Sorry, the password cannot be null or empty.");

		}

		//Generate an error if key is null or empty
		if (key==null || key.equals("")) {

			throw new Exception("Sorry, the key cannot be null or empty.");

		}

		//Generate an error if the session id already exist
		if (env.mentdb.containsKey(sessionId)) {

			throw new Exception("Sorry, the MentDB session id '"+sessionId+"' already exist.");

		}

		MentDBConnector connector = null;

		//Try to create the session
		try {

			//Server connection
			connector = new MentDBConnector(hostname, Integer.parseInt(port), Integer.parseInt(connectTimeout), Integer.parseInt(readTimeout), key);
			
			connector.subTunnels = subTunnels;
			
			//User connection
			if (connector.connect(user, password)) {

				//Save the session
				env.mentdb.put(sessionId, connector);

			} else {

				throw new Exception(connector.clientConnectionState);

			}

		} catch (Exception e) {

			//Try to close the session
			try {connector.close();} catch (Exception f) {};
			
			throw new Exception("TunnelCntError: "+e.getMessage()+".");

		}

		return connector.idConnection+"";

	}

	public static String connect_hot(EnvManager env, String sessionId, String json) throws Exception {
		
		MentDBConnector connector = null;

		//Generate an error if session id is null or empty
		if (sessionId==null || sessionId.equals("")) {

			throw new Exception("Sorry, the MentDB session id cannot be null or empty.");

		}
		
		try {
			
			//Check if the tunnel is already open
			if (env.mentdb.containsKey(sessionId)) {
				
				try {
					
					//Check if valid
					connector = env.mentdb.get(sessionId);
					connector.execute("1");
					
					//The tunnel is valid
					
				} catch (Exception e) {
					
					//Reopen the tunnel
					
					//Close the old tunnel
					close(env, sessionId);
					
					JSONObject conf = (JSONObject) JsonManager.load(json);

					String hostname = (String) conf.get("hostname");
					String port = (String) conf.get("port");
					String key = (String) conf.get("key");
					String user = (String) conf.get("user");
					String password = (String) conf.get("password");
					String connectTimeout = (String) conf.get("connectTimeout");
					String readTimeout = (String) conf.get("readTimeout");
					String subTunnels = (String) conf.get("subTunnels");

					//Generate an error if host name is null or empty
					if (hostname==null || hostname.equals("")) {

						throw new Exception("Sorry, the host name cannot be null or empty.");

					}

					//Generate an error if the port is not a number
					try {

						Integer.parseInt(port);

					} catch (Exception f) {

						throw new Exception("Sorry, the port must be numbers.");

					}

					//Generate an error if user is null or empty
					if (user==null || user.equals("")) {

						throw new Exception("Sorry, the user cannot be null or empty.");

					}

					//Generate an error if password is null or empty
					if (password==null || password.equals("")) {

						throw new Exception("Sorry, the password cannot be null or empty.");

					}

					//Generate an error if key is null or empty
					if (key==null || key.equals("")) {

						throw new Exception("Sorry, the key cannot be null or empty.");

					}
					
					try {
						
						//Server connection
						connector = new MentDBConnector(hostname, Integer.parseInt(port), Integer.parseInt(connectTimeout), Integer.parseInt(readTimeout), key);

						connector.subTunnels = subTunnels;
						
						//User connection
						if (connector.connect(user, password)) {
							
							//Add the connector
							env.mentdb.put(sessionId, connector);
							
						} else {
							
							throw new Exception(connector.clientConnectionState);
							
						}
						
					} catch (Exception z) {
						
						//Try to close the session
						try {connector.close();} catch (Exception f) {};

						throw new Exception("TunnelCntError: "+z.getMessage()+".");
						
					}
					
				}
				
			} else {
				
				JSONObject conf = (JSONObject) JsonManager.load(json);

				String hostname = (String) conf.get("hostname");
				String port = (String) conf.get("port");
				String key = (String) conf.get("key");
				String user = (String) conf.get("user");
				String password = (String) conf.get("password");
				String connectTimeout = (String) conf.get("connectTimeout");
				String readTimeout = (String) conf.get("readTimeout");
				String subTunnels = (String) conf.get("subTunnels");

				//Generate an error if host name is null or empty
				if (hostname==null || hostname.equals("")) {

					throw new Exception("Sorry, the host name cannot be null or empty.");

				}

				//Generate an error if the port is not a number
				try {

					Integer.parseInt(port);

				} catch (Exception f) {

					throw new Exception("Sorry, the port must be numbers.");

				}

				//Generate an error if user is null or empty
				if (user==null || user.equals("")) {

					throw new Exception("Sorry, the user cannot be null or empty.");

				}

				//Generate an error if password is null or empty
				if (password==null || password.equals("")) {

					throw new Exception("Sorry, the password cannot be null or empty.");

				}

				//Generate an error if key is null or empty
				if (key==null || key.equals("")) {

					throw new Exception("Sorry, the key cannot be null or empty.");

				}
				
				try {
				
					//Server connection
					connector = new MentDBConnector(hostname, Integer.parseInt(port), Integer.parseInt(connectTimeout), Integer.parseInt(readTimeout), key);
	
					connector.subTunnels = subTunnels;
					
					//User connection
					if (connector.connect(user, password)) {
						
						//Add the connector
						env.mentdb.put(sessionId, connector);
						
					} else {
						
						throw new Exception(connector.clientConnectionState);
						
					}
				
				} catch (Exception z) {
					
					//Try to close the session
					try {connector.close();} catch (Exception f) {};

					throw new Exception("TunnelCntError: "+z.getMessage()+".");
					
				}
				
			}
			
		} catch (Exception e) {
			
			//If the connector is not null
			if (connector!=null) {
				
				//Try to close the connector
				try {connector.close();} catch (Exception f) {};
				
			}
			
			if (env.mentdb.containsKey(sessionId)) {
				
				env.mentdb.remove(sessionId);
				
			}
			
			throw e;
			
		}

		return connector.idConnection+"";

	}
	
	public static String execute(EnvManager env, String sessionId, String request) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.mentdb.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		MentDBConnector connector = env.mentdb.get(sessionId);
		
		return connector.execute(connector.subTunnels.replace("[MQL_TO_REPLACE]", request));
		
	}
	
	public static String execute_hot(EnvManager env, String sessionId, String json, String request) throws Exception {
		
		TunnelManager.connect_hot(env, sessionId, json);
		
		return TunnelManager.execute(env, sessionId, request);
		
	}
	
	public static String close(EnvManager env, String sessionId) throws Exception {

		//Initialization
		String result = "1";
		
		//Generate an error if the session id does not exist
		if (!env.mentdb.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Close the session
		try {

			env.mentdb.get(sessionId).close();
			env.mentdb.remove(sessionId);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot close the MentDB session.");
			
		}
		
		return result;
		
	}
	
	public static String disconnectall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, MentDBConnector> e : env.mentdb.entrySet()) {
			
			allKeysToDelete.add(e.getKey());
			
		}
		
		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {
			
			try {
			
				//Close the session
				close(env, allKeysToDelete.get(i));
				nbClosed++;
				
			} catch (Exception e) {
				
				//Nothing to do
				
			}
			
		}
		
		return ""+nbClosed;
		
	}

}
