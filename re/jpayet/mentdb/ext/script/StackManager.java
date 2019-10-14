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

package re.jpayet.mentdb.ext.script;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.Misc;

public class StackManager {

	public static boolean lock = false;
	public static boolean lock_count = false;
	public static ConcurrentHashMap<String, Integer> loadedProcess = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<String, SessionThread> loadedProcessThread = new ConcurrentHashMap<String, SessionThread>();
	static public int PROCESS_LIMIT = 0;

	public static synchronized boolean locked() {

		if (!lock) {

			lock = true;

			return false;

		} else return true;

	}

	public static synchronized boolean locked_count() {

		if (!lock_count) {

			lock_count = true;

			return false;

		} else return true;

	}

	public static Long count_to_process() throws Exception {

		//Bloc the execution
		if (locked_count()) {

			return 0L;

		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `stack_wait` WHERE `state`='W' and nbattempt<nbmaxattempt and dtexe<=CURRENT_TIMESTAMP");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
		} catch (Exception e) {

			

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

		lock_count = false;
		
		return r;
		
	}

	public static void process() throws Exception {

		//Bloc the execution
		if (locked()) {

			return;

		}

		//Execute only if process limit is OK
		if (loadedProcess.size()<PROCESS_LIMIT) {

			Object[] cmo = null;
			Statement stm = null;
			ResultSet rs = null;

			try {

				cmo = MYSQLManager.select("SELECT "
						+ "`pid`,\n" 
						+ "`script`,\n" 
						+ "`login`\n" 
						+ "FROM `stack_wait` WHERE "
						+ "`state`='W' "
						+ "and nbattempt<nbmaxattempt "
						+ "and dtexe<=CURRENT_TIMESTAMP "
						+ "order by dtexe limit 0, "+PROCESS_LIMIT);
				
				stm = (Statement) cmo[0];
				rs = (ResultSet) cmo[1];
				
				while (rs.next()) {

					String pid = rs.getString(1);
					String script = rs.getString(2);
					String login = rs.getString(3);
					
					if (loadedProcess.size()<PROCESS_LIMIT) {

						loadedProcess.put(pid, 0);

						Runnable r = new StackProcess(pid, script, login);
						Thread t = new Thread(r);
						t.start();

					} else break;

				}

			} catch (Exception e) {

				//Nothing to do

			} finally {

				try {
					if (rs!=null) rs.close();
				} catch (Exception g) {}

				try {
					if (stm!=null) stm.close();
				} catch (Exception g) {}

			}

		}

		lock = false;

	}

	//Stack process execution
	public static class StackProcess implements Runnable {

		private String pid;
		private String script;
		private String login;

		public StackProcess(String pid, String script, String login) {
			this.pid = pid;
			this.script = script;
			this.login = login;
		}

		public void run() {

			//Initialization
			long sessionId = 0;
			
			SessionThread thread = new SessionThread(null);
			thread.user = this.login;
			
			try {
				
				MYSQLManager.executeUpdate("UPDATE `stack_wait`\n" + 
						"SET\n" + 
						"`state` = 'E'," + 
						"`lastattempt` = CURRENT_TIMESTAMP(),\n" + 
						"`nbattempt` = `nbattempt`+1" + 
						" WHERE `pid` = "+SQLManager.encode(pid), true);
				
				String json = CommandSyncAccess.execute(0, thread, null, null, 5, null, null, null, this.script, MYSQLManager.select_param(pid), pid);
				JSONObject rObj = (JSONObject) JsonManager.load(json);
				String mentalScript = rObj.get("mentalScript")+"";
				String delay_condition = rObj.get("delay_condition")+"";
				String delay_type = rObj.get("delay_type")+"";
				String delay_value = rObj.get("delay_value")+"";
				
				//Select the record
				JSONObject row = MYSQLManager.select_row("SELECT `stack_wait`.`pid`,\n" + 
						"    `stack_wait`.`script`,\n" + 
						"    `stack_wait`.`dtcreate`,\n" + 
						"    `stack_wait`.`lastattempt`,\n" + 
						"    `stack_wait`.`nbattempt`,\n" + 
						"    `stack_wait`.`nbmaxattempt`,\n" + 
						"    `stack_wait`.`dtexe`,\n" + 
						"    `stack_wait`.`login`,\n" + 
						"    `stack_wait`.`lasterrormsg`,\n" + 
						"    `stack_wait`.`dtclosed`,\n" + 
						"    `stack_wait`.`dterror`\n" + 
						"FROM `stack_wait` where pid="+SQLManager.encode(pid));
				
				String condResult = CommandManager.executeAllCommands(true, thread, Misc.splitCommand(delay_condition), thread.env, null, pid);
				
				if (condResult!=null && condResult.equals("1")) {
				
					sessionId = thread.idConnection;
					
					SessionThreadAgent agent = new SessionThreadAgent(thread);
					SessionThreadAgent.allServerThread.put(sessionId, agent);
					
					loadedProcessThread.put(pid, thread);
					
					CommandManager.executeAllCommands(true, thread, Misc.splitCommand(mentalScript), thread.env, null, pid);
					
					//Move the record into stack_closed
					MYSQLManager.executeUpdate("INSERT INTO `stack_closed`\n" + 
							"(`pid`,\n" + 
							"`script`,\n" + 
							"`dtcreate`,\n" + 
							"`lastattempt`,\n" + 
							"`nbattempt`,\n" + 
							"`nbmaxattempt`,\n" + 
							"`dtexe`,\n" + 
							"`login`,\n" + 
							"`lasterrormsg`,\n" + 
							"`dtclosed`,\n" + 
							"`dterror`)\n" + 
							"VALUES\n" + 
							"(" +
							SQLManager.encode((String) row.get("pid"))+",\n" + 
							SQLManager.encode((String) row.get("script"))+",\n" + 
							SQLManager.encode((String) row.get("dtcreate"))+",\n" + 
							SQLManager.encode((String) row.get("lastattempt"))+",\n" + 
							SQLManager.encode((String) row.get("nbattempt"))+",\n" + 
							SQLManager.encode((String) row.get("nbmaxattempt"))+",\n" + 
							SQLManager.encode((String) row.get("dtexe"))+",\n" + 
							SQLManager.encode((String) row.get("login"))+",\n" + 
							SQLManager.encode((String) row.get("lasterrormsg"))+",\n" + 
							"CURRENT_TIMESTAMP(),\n" + 
							SQLManager.encode((String) row.get("dterror"))+");"
							+ "delete from `stack_wait` where pid="+SQLManager.encode(pid), true);
				} else {
					
					MYSQLManager.executeUpdate("UPDATE `stack_wait`\n" + 
							"SET\n" + 
							"`state` = 'W'," + 
							"`lastattempt` = CURRENT_TIMESTAMP(),\n" + 
							"`nbattempt` = `nbattempt`-1," + 
							"`dtexe` = '"+DateFx.addt(row.get("dtexe")+"", delay_type, delay_value)+"'" + 
							" WHERE `pid` = "+SQLManager.encode(pid), true);
					
				}
				
			} catch (Exception e) {
				
				//Move the record into stack_error
				JSONObject row;
				try {
					
					row = MYSQLManager.select_row("SELECT `stack_wait`.`pid`,\n" + 
							"    `stack_wait`.`script`,\n" + 
							"    `stack_wait`.`dtcreate`,\n" + 
							"    `stack_wait`.`lastattempt`,\n" + 
							"    `stack_wait`.`nbattempt`,\n" + 
							"    `stack_wait`.`nbmaxattempt`,\n" + 
							"    `stack_wait`.`dtexe`,\n" + 
							"    `stack_wait`.`login`,\n" + 
							"    `stack_wait`.`lasterrormsg`,\n" + 
							"    `stack_wait`.`dtclosed`,\n" + 
							"    `stack_wait`.`dterror`\n" + 
							"FROM `stack_wait` where pid="+SQLManager.encode(pid));
					
					if (Integer.parseInt((String) row.get("nbattempt"))<Integer.parseInt((String) row.get("nbmaxattempt"))) {
						
						MYSQLManager.executeUpdate("UPDATE `stack_wait`\n" + 
							"SET\n" + 
							"`state` = 'W'"+
							" WHERE `pid` = "+SQLManager.encode(pid), true);
						
					} else {
					
						MYSQLManager.executeUpdate("INSERT INTO `stack_error`\n" + 
							"(`pid`,\n" + 
							"`script`,\n" + 
							"`dtcreate`,\n" + 
							"`lastattempt`,\n" + 
							"`nbattempt`,\n" + 
							"`nbmaxattempt`,\n" + 
							"`dtexe`,\n" + 
							"`login`,\n" + 
							"`lasterrormsg`,\n" + 
							"`dtclosed`,\n" + 
							"`dterror`)\n" + 
							"VALUES\n" + 
							"(" +
							SQLManager.encode((String) row.get("pid"))+",\n" + 
							SQLManager.encode((String) row.get("script"))+",\n" + 
							SQLManager.encode((String) row.get("dtcreate"))+",\n" + 
							SQLManager.encode((String) row.get("lastattempt"))+",\n" + 
							SQLManager.encode((String) row.get("nbattempt"))+",\n" + 
							SQLManager.encode((String) row.get("nbmaxattempt"))+",\n" + 
							SQLManager.encode((String) row.get("dtexe"))+",\n" + 
							SQLManager.encode((String) row.get("login"))+",\n" + 
							SQLManager.encode(e.getMessage()+"")+",\n" + 
							"null,\n" + 
							"CURRENT_TIMESTAMP());"
							+ "delete from `stack_wait` where pid="+SQLManager.encode(pid), true);
						
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			} finally {
				
				if (sessionId>0) {
					
					try {
						SessionThread.closeSession(thread.env, sessionId);
					} catch (Exception e) {}
					try {
						SessionThreadAgent.allServerThread.remove(sessionId);
					} catch (Exception e) {}
					
				}
				
			}

			loadedProcess.remove(pid);
			loadedProcessThread.remove(pid);

		}

	}
	


	//Insert the execution into the stack
	public static String insert_stack(SessionThread session, Vector<MQLValue> inputVector, EnvManager env) throws Exception {
		
		//Initialization
		String dtExe = inputVector.get(0).value;
		inputVector.remove(0);
		
		String scriptName = inputVector.get(0).value;
		
		if (DateFx.is_valid_timestamp(dtExe).equals("0")) {
			
			throw new Exception("Sorry, the date to execute is not valid (example: date now).");
			
		}
		
		int nbParamDiviseBy2 = (inputVector.size()-1)/2;
		if ((nbParamDiviseBy2*2)!=(inputVector.size()-1)) {

			throw new Exception("Sorry, the script '"+scriptName+"' does not has a valid number of parameters.");

		}
		
		//Get script object
		
		JSONObject script = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "script get \""+scriptName.replace("\"", "\\\"")+"\";"));
		
		String pid = SequenceManager.incr(0, "pid");
		Transaction.commit(0);
		String nbmaxattempt = (String) script.get("nbAttempt");
		
		MYSQLManager.executeUpdate("INSERT INTO `stack_wait`" + 
				"(`pid`," + 
				"`script`," + 
				"`lastattempt`," + 
				"`nbattempt`," + 
				"`nbmaxattempt`," + 
				"`dtexe`," + 
				"`login`," + 
				"`lasterrormsg`," + 
				"`dtclosed`," + 
				"`dterror`)" + 
				"VALUES" + 
				"("+SQLManager.encode(pid)+"," + 
				SQLManager.encode(scriptName)+"," + 
				"null," + 
				"0," + 
				SQLManager.encode(nbmaxattempt)+"," + 
				SQLManager.encode(dtExe)+"," + 
				SQLManager.encode(session.user)+"," + 
				"null," + 
				"null," + 
				"null);", true);
		
		try {
			
			//Get all parameters
			for(int i=1;i<inputVector.size();i=i+2) {
				
				//Get the variable and the value
				String varName = inputVector.get(i).value;
				String val = inputVector.get(i+1).value;
				
				env.set(varName, val);
				
				MYSQLManager.executeUpdate("INSERT INTO `stack_var`" + 
						"(`pid`," + 
						"`param`," + 
						"`value`)" + 
						"VALUES" + 
						"("+SQLManager.encode(pid)+"," + 
						SQLManager.encode(varName)+"," + 
						SQLManager.encode(val)+");", true);
				
			}
			
		} catch (Exception e) {
			
			MYSQLManager.executeUpdate("delete from `stack_var` where `pid`="+SQLManager.encode(pid)+";", false);
			MYSQLManager.executeUpdate("delete from `stack_wait` where `pid`="+SQLManager.encode(pid)+";", false);
			
			throw e;
			
		}
		
		return pid;
		
	}

	public static String count_wait() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `stack_wait` where state='W';");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String count_closed() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `stack_closed`;");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String count_error() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `stack_error`;");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String count_running() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `stack_wait` where state='E';");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}
	
	public static String delete_wait_id(String pid) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_wait` WHERE pid="+SQLManager.encode(pid), true);

	}
	
	public static String delete_wait_script(String scriptName) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_wait` WHERE script="+SQLManager.encode(scriptName), true);

	}
	
	public static String delete_closed_id(String pid) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_closed` WHERE pid="+SQLManager.encode(pid), true);

	}
	
	public static String delete_closed_script(String scriptName) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_closed` WHERE script="+SQLManager.encode(scriptName), true);

	}
	
	public static String delete_error_id(String pid) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_error` WHERE pid="+SQLManager.encode(pid), true);

	}
	
	public static String delete_error_script(String scriptName) throws Exception {

		return MYSQLManager.executeUpdate("DELETE FROM `stack_error` WHERE script="+SQLManager.encode(scriptName), true);

	}
	
	public static String reset_nb_attempt_id(String pid) throws Exception {

		return MYSQLManager.executeUpdate("Update `stack_error` set nbattempt=0 WHERE pid="+SQLManager.encode(pid), true);

	}
	
	public static String reset_nb_attempt_script(String scriptName) throws Exception {

		return MYSQLManager.executeUpdate("Update `stack_error` set nbattempt=0 WHERE script="+SQLManager.encode(scriptName), true);

	}
	
	public static String replay_error_id(String pid) throws Exception {
		
		JSONObject row = MYSQLManager.select_row("SELECT `pid`,\n" + 
				"    `script`,\n" + 
				"    `dtcreate`,\n" + 
				"    `lastattempt`,\n" + 
				"    `nbattempt`,\n" + 
				"    `nbmaxattempt`,\n" + 
				"    `dtexe`,\n" + 
				"    `login`,\n" + 
				"    `lasterrormsg`,\n" + 
				"    `dtclosed`,\n" + 
				"    `dterror`\n" + 
				"FROM `stack_error` where pid="+SQLManager.encode(pid));
		
		if (row.size()==0) {
			
			throw new Exception("Sorry, the error (pid="+pid+") does not exist.");
			
		}
		
		if (((String) row.get("nbattempt")).equals(((String) row.get("nbmaxattempt")))) {
			
			throw new Exception("Sorry, the number of attempt is full.");
			
		}
		
		MYSQLManager.executeUpdate("INSERT INTO `stack_wait`\n" + 
				"(`pid`,\n" + 
				"`state`,\n" + 
				"`script`,\n" + 
				"`dtcreate`,\n" + 
				"`lastattempt`,\n" + 
				"`nbattempt`,\n" + 
				"`nbmaxattempt`,\n" + 
				"`dtexe`,\n" + 
				"`login`,\n" + 
				"`lasterrormsg`,\n" + 
				"`dtclosed`,\n" + 
				"`dterror`)\n" + 
				"VALUES\n" + 
				"(" +
				SQLManager.encode((String) row.get("pid"))+",\n" + 
				"'W',\n" + 
				SQLManager.encode((String) row.get("script"))+",\n" + 
				SQLManager.encode((String) row.get("dtcreate"))+",\n" + 
				SQLManager.encode((String) row.get("lastattempt"))+",\n" + 
				SQLManager.encode((String) row.get("nbattempt"))+",\n" + 
				SQLManager.encode((String) row.get("nbmaxattempt"))+",\n" + 
				SQLManager.encode((String) row.get("dtexe"))+",\n" + 
				SQLManager.encode((String) row.get("login"))+",\n" + 
				"null,\n" + 
				"null,\n" + 
				"null);"
				+ "delete from `stack_error` where pid="+SQLManager.encode(pid), true);
		
		return "1";

	}
	
	public static String replay_error_script(String scriptName) throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		long replay_all = 0;
		long replay_nb_ok = 0;
		
		try {

			cmo = MYSQLManager.select("SELECT `pid`\n" + 
					"FROM `stack_error` WHERE script="+SQLManager.encode(scriptName)+" ORDER BY `dtexe`");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {

				String pid = rs.getString(1);
				
				try {
					
					replay_error_id(pid);
					
					replay_nb_ok++;
					
				} catch (Exception e) {
					
					//Nothing to do
					
				}
				
				replay_all++;
				
			}
			
			return "OK: "+replay_nb_ok+"/"+replay_all;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}
	
	public static JSONObject get_row(String pid) throws Exception {
		
		JSONObject row = MYSQLManager.select_row("SELECT `pid`,\n" + 
				"    `script`,\n" + 
				"    `state`,\n" + 
				"    `dtcreate`,\n" + 
				"    `lastattempt`,\n" + 
				"    `nbattempt`,\n" + 
				"    `nbmaxattempt`,\n" + 
				"    `dtexe`,\n" + 
				"    `login`,\n" + 
				"    `lasterrormsg`,\n" + 
				"    `dtclosed`,\n" + 
				"    `dterror`\n" + 
				"FROM `stack_wait` where pid="+SQLManager.encode(pid));
		
		if (row.size()==0) {
			
			row = MYSQLManager.select_row("SELECT `pid`,\n" + 
					"    `script`,\n" + 
					"    `dtcreate`,\n" + 
					"    `lastattempt`,\n" + 
					"    `nbattempt`,\n" + 
					"    `nbmaxattempt`,\n" + 
					"    `dtexe`,\n" + 
					"    `login`,\n" + 
					"    `lasterrormsg`,\n" + 
					"    `dtclosed`,\n" + 
					"    `dterror`\n" + 
					"FROM `stack_closed` where pid="+SQLManager.encode(pid));
			
			if (row.size()==0) {
				
				row = MYSQLManager.select_row("SELECT `pid`,\n" + 
						"    `script`,\n" + 
						"    `dtcreate`,\n" + 
						"    `lastattempt`,\n" + 
						"    `nbattempt`,\n" + 
						"    `nbmaxattempt`,\n" + 
						"    `dtexe`,\n" + 
						"    `login`,\n" + 
						"    `lasterrormsg`,\n" + 
						"    `dtclosed`,\n" + 
						"    `dterror`\n" + 
						"FROM `stack_error` where pid="+SQLManager.encode(pid));
				
			}
			
		}
		
		if (row.size()==0) {
			
			throw new Exception("Sorry, the process '"+pid+"' does not exist.");
			
		}
		
		return row;

	}

	@SuppressWarnings("unchecked")
	public static String search(String tableType, String script, String dtType, 
			String dtMin, String dtMax, String dtOrder, String page, String nbByPage) throws Exception {
		
		//Initialization
		String condition = "";
		String orderby = "";
		String limit = "";
		String dateTable = "";
		
		if (script!=null && !script.equals("")) {
			
			condition += " and script like "+SQLManager.encode_like(script);
			
		}
		
		//Generate an error if the table type is null or empty
		if (tableType==null || tableType.equals("")) {
			
			throw new Exception("Sorry, the table type cannot be null or empty.");
			
		}
		
		tableType = tableType.toLowerCase();
		
		//Generate an error if the table type is not valid
		if (!tableType.equals("wait") && !tableType.equals("running") 
				&& !tableType.equals("closed") && !tableType.equals("error")) {
			
			throw new Exception("Sorry, the table type must be 'wait|running|closed|error'.");
			
		}
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "stack <"+tableType+">");
		table.put("mql", "in out_editor {\n" + 
				"	stack search \""+tableType+"\" \n" + 
				"	"+(script==null?"null":"\""+script+"\"")+" \n" + 
				"	\""+dtType+"\" \n" + 
				"	\""+dtMin+"\"\n" + 
				"	\""+dtMax+"\"\n" + 
				"	"+dtOrder+" "+page+" "+nbByPage+";\n" + 
				"}");
		JSONArray data = new JSONArray();
		table.put("data", data);
		
		//Generate an error if the date type is null or empty
		if (dtType==null || dtType.equals("")) {
			
			throw new Exception("Sorry, the date type cannot be null or empty.");
			
		}
		
		dtType = dtType.toLowerCase();
		
		//Generate an error if the date type is not valid
		if (!dtType.equals("exe") && !dtType.equals("closed") 
				&& !dtType.equals("error") && !dtType.equals("create") 
				&& !dtType.equals("lastattempt")) {
			
			throw new Exception("Sorry, the date type must be 'exe|closed|error|create|lastattempt'.");
			
		}
		
		if (dtType.equals("exe")) dateTable = "dtexe";
		else if (dtType.equals("closed")) dateTable = "dtclosed";
		else if (dtType.equals("error")) dateTable = "dterror";
		else if (dtType.equals("create")) dateTable = "dtcreate";
		else if (dtType.equals("lastattempt")) dateTable = "lastattempt";
		
		//Generate an error if the min date
		if (DateFx.is_valid_timestamp(dtMin).equals("0")) {
			
			throw new Exception("Sorry, the min date is not a valid datetime.");
			
		}
		
		//Generate an error if the max date
		if (DateFx.is_valid_timestamp(dtMax).equals("0")) {
			
			throw new Exception("Sorry, the max date is not a valid datetime.");
			
		}
		
		//Generate an error if the ordered date is null or empty
		if (dtOrder==null || dtOrder.equals("")) {
			
			throw new Exception("Sorry, the ordered date cannot be null or empty.");
			
		}
		
		dtOrder = dtOrder.toLowerCase();
		
		//Generate an error if the ordered date is not valid
		if (!dtOrder.equals("asc") && !dtOrder.equals("desc")) {
			
			throw new Exception("Sorry, the ordered date must be 'asc|desc'.");
			
		}
		
		//Generate an error if the page is null or empty
		if (page==null || page.equals("")) {
			
			throw new Exception("Sorry, the page cannot be null or empty.");
			
		}
		
		//Generate an error if the number by page is null or empty
		if (nbByPage==null || nbByPage.equals("")) {
			
			throw new Exception("Sorry, the number by page cannot be null or empty.");
			
		}
		
		//The page must be a number
		try {
			
			if (Integer.parseInt(page)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the page must be a valid number (>=1).");
			
		}
		
		//The number by page must be a number
		try {
			
			if (Integer.parseInt(nbByPage)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number by page must be a valid number (>=1).");
			
		}
		
		condition += " and ("+dateTable+" BETWEEN "+SQLManager.encode(dtMin)+" AND "+SQLManager.encode(dtMax)+")";
		orderby = " order by "+dateTable+" "+dtOrder;
		limit = " limit "+((Integer.parseInt(page)-1)*Integer.parseInt(nbByPage))+", "+nbByPage;
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			if (tableType.equals("wait")) {
				
				cmo = MYSQLManager.select("SELECT `pid`,\n" + 
						"    `script`,\n" + 
						"    `dtcreate`,\n" + 
						"    `lastattempt`,\n" + 
						"    `nbattempt`,\n" + 
						"    `nbmaxattempt`,\n" + 
						"    `dtexe`,\n" + 
						"    `login`,\n" + 
						"    `lasterrormsg`,\n" + 
						"    `dtclosed`,\n" + 
						"    `dterror`\n" + 
						"FROM `stack_wait` WHERE `state`='W'"+condition+orderby+limit);
				
			} else if (tableType.equals("running")) {
				
				cmo = MYSQLManager.select("SELECT `pid`,\n" + 
						"    `script`,\n" + 
						"    `dtcreate`,\n" + 
						"    `lastattempt`,\n" + 
						"    `nbattempt`,\n" + 
						"    `nbmaxattempt`,\n" + 
						"    `dtexe`,\n" + 
						"    `login`,\n" + 
						"    `lasterrormsg`,\n" + 
						"    `dtclosed`,\n" + 
						"    `dterror`\n" + 
						"FROM `stack_wait` WHERE `state`='E'"+condition+orderby+limit);
				
			} else if (tableType.equals("closed")) {
				
				cmo = MYSQLManager.select("SELECT `pid`,\n" + 
						"    `script`,\n" + 
						"    `dtcreate`,\n" + 
						"    `lastattempt`,\n" + 
						"    `nbattempt`,\n" + 
						"    `nbmaxattempt`,\n" + 
						"    `dtexe`,\n" + 
						"    `login`,\n" + 
						"    `lasterrormsg`,\n" + 
						"    `dtclosed`,\n" + 
						"    `dterror`\n" + 
						"FROM `stack_closed` WHERE 1=1"+condition+orderby+limit);
				
			} else if (tableType.equals("error")) {
				
				cmo = MYSQLManager.select("SELECT `pid`,\n" + 
						"    `script`,\n" + 
						"    `dtcreate`,\n" + 
						"    `lastattempt`,\n" + 
						"    `nbattempt`,\n" + 
						"    `nbmaxattempt`,\n" + 
						"    `dtexe`,\n" + 
						"    `login`,\n" + 
						"    `lasterrormsg`,\n" + 
						"    `dtclosed`,\n" + 
						"    `dterror`\n" + 
						"FROM `stack_error` WHERE 1=1"+condition+orderby+limit);
				
			}
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name);
				
				switch (meta.getColumnType(i+1)) {
				case Types.BIGINT: case Types.INTEGER: case Types.SMALLINT: case Types.TINYINT: 
					column_types.add("LONG");
					break;
				case Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT:  case Types.NUMERIC: case Types.REAL: 
					column_types.add("DOUBLE");
					break;
				default : 
					column_types.add("STRING");
				}
				

			}

			while (rs.next()) {
				
				String l_pid = rs.getString(1);
				String l_script = rs.getString(2);
				String l_dtcreate = rs.getString(3); if (l_dtcreate==null) l_dtcreate = "";
				if (!l_dtcreate.equals("")) l_dtcreate = l_dtcreate.substring(0, 19);
				String l_lastattempt = rs.getString(4); if (l_lastattempt==null) l_lastattempt = "";
				if (!l_lastattempt.equals("")) l_lastattempt = l_lastattempt.substring(0, 19);
				String l_nbattempt = rs.getString(5);
				String l_nbmaxattempt = rs.getString(6);
				String l_dtexe = rs.getString(7).substring(0, 19);
				String l_login = rs.getString(8);
				String l_lasterrormsg = rs.getString(9);if (l_lasterrormsg==null) l_lasterrormsg = "";
				String l_dtclosed = rs.getString(10); if (l_dtclosed==null) l_dtclosed = "";
				if (!l_dtclosed.equals("")) l_dtclosed = l_dtclosed.substring(0, 19);
				String l_dterror = rs.getString(11); if (l_dterror==null) l_dterror = "";
				if (!l_dterror.equals("")) l_dterror = l_dterror.substring(0, 19);
				
				JSONObject line = new JSONObject();
				line.put("pid", l_pid);
				line.put("script", l_script);
				line.put("dtcreate", l_dtcreate);
				line.put("lastattempt", l_lastattempt);
				line.put("nbattempt", l_nbattempt);
				line.put("nbmaxattempt", l_nbmaxattempt);
				line.put("dtexe", l_dtexe);
				line.put("login", l_login);
				line.put("lasterrormsg", l_lasterrormsg);
				line.put("dtclosed", l_dtclosed);
				line.put("dterror", l_dterror);
				
				data.add(line);
				
			}
			
			return table.toJSONString();

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

}
