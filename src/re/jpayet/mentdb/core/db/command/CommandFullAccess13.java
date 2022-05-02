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

package re.jpayet.mentdb.core.db.command;

import java.util.Vector;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess13 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
		case "cluster signal set":
			
			//Get params
			String cluster_id = inputVector.get(3).value;
			String node_id = inputVector.get(4).value;
			String hostname = inputVector.get(5).value;
			String port = inputVector.get(6).value;
			String user = inputVector.get(7).value;
			String user_key = inputVector.get(8).value;
			String password = inputVector.get(9).value;
			String connectTimeout = inputVector.get(10).value;
			String readTimeout = inputVector.get(11).value;
			String mql_signal = inputVector.get(12).value;
			
			ClusterManager.signal_set(session.idConnection, cluster_id, node_id, hostname, port, user, user_key, password, connectTimeout, readTimeout, mql_signal);

			return "Signal added/updated with successful.";
			
		case "cluster node set":
			
			//Get params
			cluster_id = inputVector.get(3).value;
			node_id = inputVector.get(4).value;
			hostname = inputVector.get(5).value;
			port = inputVector.get(6).value;
			user = inputVector.get(7).value;
			user_key = inputVector.get(8).value;
			password = inputVector.get(9).value;
			connectTimeout = inputVector.get(10).value;
			readTimeout = inputVector.get(11).value;
			String active_signal = inputVector.get(12).value;
			
			ClusterManager.node_set(session.idConnection, cluster_id, node_id, hostname, port, user, user_key, password, connectTimeout, readTimeout, active_signal);
			
			return "Node added/updated with successful.";

		default:
			
			//Script execution
			inputVector.remove(inputVector.size()-1);

			return CommandFullAccess.concatOrUnknow(inputVector);
			
		}

	}

}