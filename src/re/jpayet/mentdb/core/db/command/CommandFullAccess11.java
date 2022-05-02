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
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess11 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
		case "signal deploy":
			
			//Get params
			String cluster_id = inputVector.get(2).value;
			String hostname = inputVector.get(3).value;
			String port = inputVector.get(4).value;
			String user = inputVector.get(5).value;
			String user_key = inputVector.get(6).value;
			String password = inputVector.get(7).value;
			String connectTimeout = inputVector.get(8).value;
			String readTimeout = inputVector.get(9).value;
			String mql_signal = inputVector.get(10).value;
			
			return ClusterManager.signals_deploy(env, session.idConnection, cluster_id, hostname, port, user, user_key, password, connectTimeout, readTimeout, mql_signal);

		case "app menu":
			
			String jPath = inputVector.get(2).value;
			String id = inputVector.get(3).value;
			String title = inputVector.get(4).value;
			String icon = inputVector.get(5).value;
			String url = inputVector.get(6).value;
			String method = inputVector.get(7).value;
			String topMenu = inputVector.get(8).value;
			String groups = inputVector.get(9).value;
			String adminType = inputVector.get(10).value;
			
			AppManager.menu(env, jPath, id, title, icon, url, method, topMenu, groups, adminType);
			
			return "1";
		
		default:
		
			//Script execution
			inputVector.remove(inputVector.size()-1);

			return CommandFullAccess.concatOrUnknow(inputVector);
			
		}

	}

}