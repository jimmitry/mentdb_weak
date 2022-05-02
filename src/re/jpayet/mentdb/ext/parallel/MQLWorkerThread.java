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

package re.jpayet.mentdb.ext.parallel;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.statement.Statement;

public class MQLWorkerThread implements Runnable {

	private String command;
	public String result = "", parent_pid = "", current_pid = "";
	public boolean isError = false;
	private EnvManager env;
	private SessionThread session;

	public MQLWorkerThread(EnvManager env, String s, SessionThread session, String parent_pid, String current_pid) {
		this.command = s;
		this.env = env;
		this.session = session;
		this.parent_pid = parent_pid;
		this.current_pid = current_pid;
	}

	@Override
	public void run() {

		try {

			result = Statement.eval(session, command, env, parent_pid, current_pid);
			
		} catch (Exception e) {

			isError = true;
			result = e.getMessage();

		}

	}
}

