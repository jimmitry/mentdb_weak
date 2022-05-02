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

package re.jpayet.mentdb.core.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandFullAccess;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;

//Manage the database
public class Database {

	public static boolean isNotInit = true;
	
	//Initialization
	public static void init() throws Exception {

		//######################################################################
		//#INITIALIZATION#######################################################
		//######################################################################
		
		//Initialize the global parameter
		ParameterManager.init();
		
		//Initialize the global cluster
		ClusterManager.init();
		
		//Initialize the virtual host manager
		VHostManager.init();
		
		//Initialize the global connection
		CMManager.init();
		
		//Initialize the sequence manager
		SequenceManager.init();
		
		//Initialize the job manager
		JobManager.init();
		
		//Initialize the group manager
		GroupManager.init();

		//Initialize the user manager
		UserManager.init();
		
		//Initialize the script manager
		ScriptManager.init();
		
		//Initialize the DQ manager
		DQManager.init();
		
		//Initialize the language manager
		LanguageManager.init();
		
		//Initialize the concentration manager
		ConcentrationManager.init();

		//######################################################################
		//#BASIC INTEGRATION####################################################
		//######################################################################
		
		isNotInit = false;
		
		FileInputStream fis = new FileInputStream(new File("mql"+File.separator+"basic-integration.mql"));
		
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		SessionThread session = SessionThreadAgent.allServerThread.get(0L).serverThread;
		
		String line = null, mql = "";
		while ((line = br.readLine()) != null) {
			
			if (Misc.lrtrim(line).equals("#VALID MQL BLOCK;")) {

				Vector<Vector<MQLValue>> inputVectorCmds = Misc.splitCommand(mql);
				
				for(int i=0;i<inputVectorCmds.size();i++) {
				
					try {
						
						Vector<MQLValue> inputVectorCmd = inputVectorCmds.get(i);
						CommandFullAccess.execute(session, inputVectorCmd, session.env, null, null);
						
					} catch (Exception e) {
						
						br.close();
						
						throw new Exception(e.getMessage()+" / "+Misc.vectorToStringMsg(inputVectorCmds.get(i))); 
						
					}

				}
				
				mql = "";
				
			} else {
				
				mql += line+"\n";
				
			}	
		}
		
		br.close();
		
		isNotInit = true;
		
	}
	
	//Open the database
	public void open() throws IOException {
		
		//Create the data folder if does not exist
		if (!(new File("data").exists())) {

			(new File("data")).mkdir();
			(new File("data"+File.separator+"dl")).mkdir();
			
		}
		
		SessionThread session = new SessionThread();
		SessionThreadAgent sessionAgent = new SessionThreadAgent(session, "MENTDB", "", "");
		SessionThreadAgent.allServerThread.put(0L, sessionAgent);
		
	}
	
	//Execute mql
	public static String execute_admin_mql(SessionThread session, String mql) throws Exception {
		
		Vector<Vector<MQLValue>> inputVectorCmds = null;
		
		try {
			
			SessionThread mentdbInternalSession = null;
			
			if (session==null) {
				
				mentdbInternalSession = Start.agent.serverThread;
				mentdbInternalSession.sessionThreadAgent = Start.agent;
				
			} else {
				
				mentdbInternalSession = session;
				
			}
			
			inputVectorCmds = Misc.splitCommand(mql);
			
			Vector<MQLValue> inputVectorCmd = inputVectorCmds.get(0);
			return CommandFullAccess.execute(mentdbInternalSession, inputVectorCmd, mentdbInternalSession.env, null, null);
			
		} catch (Exception e) {
			
			throw new Exception(e.getMessage()+" / "+mql); 
			
		}
		
	}

}
