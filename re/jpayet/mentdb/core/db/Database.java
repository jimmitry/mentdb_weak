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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandFullAccess;
import re.jpayet.mentdb.core.db.file.data.DataFile;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.index.IndexFile;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
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
	public static HashMap<String, Integer> synchronizedCmd = new HashMap<String, Integer>();
	
	public static void loadSynchronizedCommand() {

		synchronizedCmd.put("=>", 0);
		synchronizedCmd.put("database_close", 0);
		synchronizedCmd.put("login", 0);
		synchronizedCmd.put("stop", 0);
		synchronizedCmd.put("shutdown", 0);
		synchronizedCmd.put("get_param", 0);
		synchronizedCmd.put("cooperation", 0);

		synchronizedCmd.put("cluster", 0);
		synchronizedCmd.put("language", 0);
		synchronizedCmd.put("symbol", 0);
		synchronizedCmd.put("data", 0);
		synchronizedCmd.put("word", 0);
		synchronizedCmd.put("thought", 0);
		synchronizedCmd.put("relation", 0);
		synchronizedCmd.put("circle", 0);
		synchronizedCmd.put("link", 0);
		synchronizedCmd.put("stimulation", 0);
		synchronizedCmd.put("concentration", 0);
		synchronizedCmd.put("brain", 0);
		synchronizedCmd.put("script", 0);
		synchronizedCmd.put("user", 0);
		synchronizedCmd.put("group", 0);
		synchronizedCmd.put("job", 0);
		synchronizedCmd.put("sequence", 0);
		synchronizedCmd.put("node", 0);
		synchronizedCmd.put("transaction", 0);
		synchronizedCmd.put("commit", 0);
		synchronizedCmd.put("rollback", 0);
		synchronizedCmd.put("fs", 0);
		synchronizedCmd.put("parameter", 0);
		synchronizedCmd.put("cm", 0);
		synchronizedCmd.put("graph", 0);
		synchronizedCmd.put("dq", 0);
		synchronizedCmd.put("file_watcher", 0);

		synchronizedCmd.put("app vhost", 0);
		synchronizedCmd.put("app create", 0);
		synchronizedCmd.put("app delete", 0);

		synchronizedCmd.put("refresh admin", 0);
		synchronizedCmd.put("refresh devel", 0);
		synchronizedCmd.put("refresh config", 0);
		synchronizedCmd.put("count sessions", 0);
		synchronizedCmd.put("metric sessions", 0);
		synchronizedCmd.put("metric index files", 0);
		synchronizedCmd.put("metric data files", 0);
		
	}
	
	public static boolean isSynchronized(Vector<MQLValue> inputVector) {
		
		try {
		
			if (synchronizedCmd.containsKey(inputVector.get(0).value) 
					|| synchronizedCmd.containsKey(inputVector.get(0).value + " " +inputVector.get(1).value)
					|| synchronizedCmd.containsKey(inputVector.get(0).value + " " +inputVector.get(1).value + " " +inputVector.get(2).value)) {
				
				return true;
				
			} else return false;
			
		} catch (Exception e) {
			
			return false;
			
		}
		
	}
	
	//Initialization
	public static void init() throws Exception {

		//######################################################################
		//#INITIALIZATION#######################################################
		//######################################################################
		
		//Initialize synchronized functions
		loadSynchronizedCommand();
		
		//Initialize the global parameter
		ParameterManager.init(0);
		
		//Initialize the global cluster
		ClusterManager.init(0);
		
		//Initialize the virtual host manager
		VHostManager.init(0);
		
		//Initialize the global connection
		CMManager.init(0);
		
		//Initialize the sequence manager
		SequenceManager.init(0);
		
		//Initialize the job manager
		JobManager.init(0);
		
		//Initialize the group manager
		GroupManager.init(0);

		//Initialize the user manager
		UserManager.init(0);
		
		//Initialize the script manager
		ScriptManager.init(0);
		
		//Initialize the DQ manager
		DQManager.init(0);
		
		//Initialize the language manager
		LanguageManager.init(0);
		
		//Initialize the concentration manager
		ConcentrationManager.init(0);

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
						Transaction.commit(0);
						
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

		//######################################################################
		//#BASIC EMOTIONS#######################################################
		//######################################################################
		/*
		//Create the joie emotion
		String thought_joie = EmotionManager.create(user, "joie", "fr", 0, "positive", false, null, 0);
		EmotionManager.create(user, "sérénité", "fr", 0, "positive", false, thought_joie, 0);
		EmotionManager.create(user, "extase", "fr", 2, "positive", true, thought_joie, 0);
		
		//Create the confiance emotion
		String thought_confiance = EmotionManager.create(user, "confiance", "fr", 0, "positive", false, null, 0);
		EmotionManager.create(user, "acceptation", "fr", 2, "positive", false, thought_confiance, 0);
		EmotionManager.create(user, "admiration", "fr", 2, "positive", true, thought_confiance, 0);
		
		//Create the peur emotion
		String thought_peur = EmotionManager.create(user, "peur", "fr", 0, "negative", false, null, 0);
		EmotionManager.create(user, "appréhension", "fr", 2, "negative", false, thought_peur, 0);
		EmotionManager.create(user, "terreur", "fr", 0, "negative", true, thought_peur, 0);
		
		//Create the surprise emotion
		String thought_surprise = EmotionManager.create(user, "surprise", "fr", 0, "neutral", false, null, 0);
		EmotionManager.create(user, "distration", "fr", 0, "neutral", false, thought_surprise, 0);
		EmotionManager.create(user, "étonnement", "fr", 2, "neutral", true, thought_surprise, 0);
		
		//Create the tristesse emotion
		String thought_tristesse = EmotionManager.create(user, "tristesse", "fr", 0, "negative", false, null, 0);
		EmotionManager.create(user, "songerie", "fr", 0, "negative", false, thought_tristesse, 0);
		EmotionManager.create(user, "chagrin", "fr", 1, "negative", true, thought_tristesse, 0);
		
		//Create the dégoût emotion
		String thought_dégoût = EmotionManager.create(user, "dégoût", "fr", 1, "negative", false, null, 0);
		EmotionManager.create(user, "ennui", "fr", 2, "negative", false, thought_dégoût, 1);
		EmotionManager.create(user, "aversion", "fr", 2, "negative", true, thought_dégoût, 1);
		
		//Create the colère emotion
		String thought_colère = EmotionManager.create(user, "colère", "fr", 0, "negative", false, null, 0);
		EmotionManager.create(user, "contrariété", "fr", 0, "negative", false, thought_colère, 0);
		EmotionManager.create(user, "rage", "fr", 0, "negative", true, thought_colère, 0);
		
		//Create the anticipation emotion
		String thought_anticipation = EmotionManager.create(user, "anticipation", "fr", 2, "neutral", false, null, 0);
		EmotionManager.create(user, "intérêt", "fr", 2, "neutral", false, thought_anticipation, 0);
		EmotionManager.create(user, "vigilance", "fr", 0, "neutral", true, thought_anticipation, 0);
		*/
		//Commit the transaction
		Transaction.commit(0);
		
	}
	
	//Rebuild the index directory
	public static void rebuild_index1() throws Exception {
		
		Misc.system_out_print("Save data to rebuild index > ", false, "", "");
		
		String tmpStr = "";
		if (((int)(1)/10)<10) tmpStr+=" ";
		if (((int)(1)/10)<100) tmpStr+=" ";
		
		System.out.print("\rSave data to rebuild index > "+tmpStr+((int)(1)/10)+"/100% ");
		
		if (FileFx.exist("data"+File.separator+"data.save").equals("1")) {
			FileFx.delete("data"+File.separator+"data.save");
		}
		
		File file = new File("data"+File.separator+"data.save");
		//Create the new file
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		
		for(int i=0; i<Start.NB_INDEX_FILE; i++) {
			
			tmpStr = "";
			int percent = ((int)(i+1)*100/Start.NB_INDEX_FILE);
			if (percent<10) tmpStr+=" ";
			if (percent<100) tmpStr+=" ";
				
			System.out.print("\rSave data to rebuild index > "+tmpStr+percent+"/100% ");
			
			for(int j=0; j<IndexFile.MAX_INDEX_BLOCK_SIZE; j++) {
				
				Long[] i_block = IndexFile.if_toString_obj(i, 8+(12*j));
				
				if (i_block[0]>-1) {
					
					String str_obj = StringFx.decode_b64(Record.getData(DataFilePool.dfp_get(i_block[0].intValue()), i_block[1]));
					
					JSONArray collisions = (JSONArray) JsonManager.load(str_obj);
					for(int i_colli=0; i_colli<collisions.size(); i_colli++) {
						
						String key = (String) ((JSONArray) collisions.get(i_colli)).get(0);
						
						fw.write(StringFx.encode_b64(key)+" "+StringFx.encode_b64(Record.getNode(0, key).toJSONString())+"\n");
						
					}
					
				}
				
			}
			
			fw.flush();
			
		}
		
		fw.close();
		
		Misc.system_out_print("[OK]", true, "", "100/100%");
		Log.trace("Rebuild index files > [OK]");
		
		System.out.println("Update 'server.conf' set parameters 'EXTEND_FS_FORCE' to 2, 'NB_INDEX_FILE' (if you want) and 'MAX_INDEX_BLOCK_SIZE'x2. Restart the server.");
		
		System.exit(0);
		
	}
	
	//Rebuild the index directory
	public static void rebuild_index2() throws Exception {
		
		Misc.system_out_print("Rebuild data > ", false, "", "");
		
		int count_lines = FileFx.count_lines("data"+File.separator+"data.save")-1;
		
		try (BufferedReader br = new BufferedReader(new FileReader("data"+File.separator+"data.save"))) {
		    String line;
		    int i=0;
		    while ((line = br.readLine()) != null) {
		    		
		    		String key = StringFx.decode_b64(AtomFx.get(line, "1", " "));
		    		String data = StringFx.decode_b64(AtomFx.get(line, "2", " "));
		    		
		    		if (Record.getNode(0, key)==null) {
		    			Record.add(0, key, data);
		    		} else {
		    			Record.update(0, key, data);
		    		}
		    		
				System.out.print("\rRebuild data > "+i+"/"+count_lines);
				
				i++;
		    		
		    }
		    
		} catch (Exception e) {
			
			throw e;
			
		}
		
		Misc.system_out_print("[OK]", true, "", count_lines+"/"+count_lines);
		Log.trace("Rebuild data > [OK]");
		
	}
	
	//Open the database
	public void open() throws IOException {
		
		//Create the data folder if does not exist
		if (!(new File("data").exists())) {

			(new File("data")).mkdir();
			(new File("data"+File.separator+"dl")).mkdir();
			
		}
		
		//Create the data/index folder if does not exist
		if (!(new File("data"+File.separator+"index").exists())) {
			
			(new File("data"+File.separator+"index")).mkdir();
			
			//Create the data/data/0 folder if does not exist
			if (!(new File("data"+File.separator+"index"+File.separator+"0").exists())) {
				
				(new File("data"+File.separator+"index"+File.separator+"0")).mkdir();
				
			}
			
			String tmpStr = "";
			if (((int)(1)/10)<10) tmpStr+=" ";
			if (((int)(1)/10)<100) tmpStr+=" ";
				
			System.out.print("\rCreate MentDB database > "+tmpStr+((int)(1)/10)+"/100%");
			
			//Create the first index file
			(new File("tools"+File.separator+"brain0.idx")).delete();
			IndexFile indexFile = new IndexFile(0, "tools"+File.separator+"brain0.idx");
			indexFile.open();
			indexFile.indexFile.writeLong(0);
			indexFile.addExtraEntries(IndexFile.MAX_INDEX_BLOCK_SIZE);
			indexFile.close();

			FileUtils.copyFile(new File("tools"+File.separator+"brain0.idx"), new File("data"+File.separator+"index"+File.separator+"0"+File.separator+"brain0.idx"));
			
			for(int i=1;i<Start.NB_INDEX_FILE;i++) {

				tmpStr = "";
				int percent = ((int)(i+1)*100/Start.NB_INDEX_FILE);
				if (percent<10) tmpStr+=" ";
				if (percent<100) tmpStr+=" ";
					
				System.out.print("\rCreate MentDB database > "+tmpStr+percent+"/100%");
				
				//Create the data/data/id folder if does not exist
				if (!(new File("data"+File.separator+"index"+File.separator+i).exists())) {
					
					(new File("data"+File.separator+"index"+File.separator+i)).mkdir();
					
				}
				
				//Copy the first file
				FileUtils.copyFile(new File("data"+File.separator+"index"+File.separator+"0"+File.separator+"brain0.idx"), new File("data"+File.separator+"index"+File.separator+i+File.separator+"brain"+i+".idx"));
				
			}
			
		}
		
		//Create the data/data folder if does not exist
		if (!(new File("data"+File.separator+"data").exists())) {
			
			(new File("data"+File.separator+"data")).mkdir();
			
			DataFile dataFile = new DataFile(0, "data"+File.separator+"data"+File.separator+"0"+File.separator+"brain0.dat");
			dataFile.open();
			dataFile.close();
			
		}
		
		//Create the data/transaction folder if does not exist
		if (!(new File("data"+File.separator+"transaction").exists())) {
			
			(new File("data"+File.separator+"transaction")).mkdir();
			
		}
		
		//Create the data/transaction folder if does not exist
		if (!(new File("data"+File.separator+"transaction"+File.separator+"0").exists())) {
			
			(new File("data"+File.separator+"transaction"+File.separator+"0")).mkdir();
			
		}
		
		//Open the log file
		Transaction.logFiles.put(0L, new RandomAccessFile("data"+File.separator+"transaction"+File.separator+"0"+File.separator+"rollback.log", "rw"));
		
		SessionThread session = new SessionThread();
		SessionThreadAgent sessionAgent = new SessionThreadAgent(session);
		SessionThreadAgent.allServerThread.put(0L, sessionAgent);
		
	}
	
	//Execute mql
	public static String execute_admin_mql(SessionThread session, String mql) throws Exception {
		
		Vector<Vector<MQLValue>> inputVectorCmds = null;
		
		try {
			
			SessionThread mentdbInternalSession = null;
			
			if (session==null) {
				
				mentdbInternalSession = new SessionThread();
				
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
