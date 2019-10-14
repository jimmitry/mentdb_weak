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

import java.io.File;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.index.IndexFilePool;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.compress.CompressManager;
import re.jpayet.mentdb.ext.csv.CSVManager;
import re.jpayet.mentdb.ext.dl.BayesianNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.CSVNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.TextCatManager;
import re.jpayet.mentdb.ext.doc.MQLDocumentation;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.ConstantFx;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.fx.MyRSA;
import re.jpayet.mentdb.ext.fx.OperatorFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.fx.TypeFx;
import re.jpayet.mentdb.ext.html.HTMLManager;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mail.ImapManager;
import re.jpayet.mentdb.ext.mail.Pop3Manager;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.os.OsManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.parallel.ParallelManager;
import re.jpayet.mentdb.ext.pdf.PDFManager;
import re.jpayet.mentdb.ext.remote.CifsManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.rest.REST;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.scrud.ScrudManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.Session;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.soap.SOAPManager;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stat.Statistic;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess {

	//Execute the command
	@SuppressWarnings({ "unchecked" })
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {

		//Stop the execution if the session was interrupted
		if (session!=null && session.isInterrupted && inputVector!=null && !inputVector.get(0).value.equals("rollback")) {

			throw new Exception("Sorry, the action has been canceled.");
			
		}
		
		//Check if this is a comment
		if (inputVector.get(0).value.startsWith("#")) {

			if (inputVector.get(0).value.startsWith("#:")) System.out.println(inputVector.get(0).value);

			return "This is a comment ...";
		}

		//Add ; at the end
		if (!(inputVector.get(inputVector.size()-1).value).equals(";")) {
			inputVector.add(new MQLValue(";", 0, 0));
		}

		//Return the variable value
		if (inputVector.size()==2 && inputVector.get(0).cmdBlockType==1) {
			if (env.exist(inputVector.get(0).value)) 
				return env.get(inputVector.get(0).value);
			else if (inputVector.get(0).value.equals("[_n_]")) 
				return "\n";
			else if (inputVector.get(0).value.equals("[_r_]")) 
				return "\r";
			else throw new Exception("Sorry, the variable name "+inputVector.get(0).value+" does not exist.");
		}

		//Load variables
		for(int i=0;i<inputVector.size()-1;i++) {

			switch (inputVector.get(i).cmdBlockType) {
			case 1:

				//Get a variable name
				if (env.exist(inputVector.get(i).value)) 
					inputVector.get(i).value = env.get(inputVector.get(i).value);
				else if (inputVector.get(i).value.equals("[_n_]")) 
					inputVector.get(i).value = "\n";
				else if (inputVector.get(i).value.equals("[_r_]")) 
					inputVector.get(i).value = "\r";
				else {
					throw new Exception("Sorry, the variable name "+inputVector.get(i).value+" does not exist.");
				}

				break;

			case 3:
				
				switch (inputVector.get(i).value) {
				case "false":
					
					inputVector.get(i).value = "0";
					
					break;
					
				case "true":
					
					inputVector.get(i).value = "1";
					
					break;
				case "null":
					
					inputVector.get(i).value = null;

					if (i==0 && inputVector.size()==2) {
						return null;
					}
					
					break;
					
				}

				break;

			case -1:

				inputVector.get(i).value = CommandManager.executeAllCommands(false, session, Misc.splitCommand("link get value \""+inputVector.get(i).value.replace("\"", "\\\"")+"\""), env, parent_pid, current_pid);

				break;

			case 2: 

				inputVector.get(i).value = CommandManager.executeAllCommands(false, session, Misc.splitCommand(inputVector.get(i).value), env, parent_pid, current_pid);

				break;

			}

		}
		
		if (inputVector.get(0).value.equals("stack") && inputVector.size()>2 && DateFx.is_valid_timestamp(inputVector.get(1).value).equals("1")) {
			
			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return StackManager.insert_stack(session, inputVector, env);
			
		}

		switch (inputVector.get(0).value) {
		case "concat_var":

			//Script concat
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return StringFx.concat_var(inputVector, env);

		case "include":

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			String scriptName = inputVector.get(0).value;
			if (!session.user.equals("ai")) {
				
				//Check if a script is granted to a user
				if (!session.user.equals("mentdb") && 
						Database.execute_admin_mql(session, "group is granted user \""+session.user.replace("\"", "\\\"")+"\" \"sys\";").equals("0") && 
						Database.execute_admin_mql(session, "script is granted \""+scriptName.replace("\"", "\\\"")+"\" \""+session.user.replace("\"", "\\\"")+"\";").equals("0")) {

						throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+session.user+"'.");
					
				}
				
			}
			
			int nbParamDiviseBy2 = (inputVector.size()-1)/2;
			if ((nbParamDiviseBy2*2)!=(inputVector.size()-1)) {

				throw new Exception("Sorry, the script '"+scriptName+"' does not has a valid number of parameters.");

			}
			
			//Set all parameters into the variable environment
			for(int i=1;i<inputVector.size();i=i+2) {
				
				env.set(inputVector.get(i).value, inputVector.get(i+1).value);
				
			}
			
			JSONObject o = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "script get \""+scriptName.replace("\"", "\\\"")+"\";"));
			env.set("[PID]", current_pid);
			env.set("[PPID]", parent_pid);
			return Statement.eval(session, Misc.splitCommand((String) o.get("mql")), env, parent_pid, current_pid);

		case "call":

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			env.set("[PID]", current_pid);
			env.set("[PPID]", parent_pid);
			return ScriptManager.execute(session, inputVector, env, false, parent_pid, current_pid);

		case "execute":

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			EnvManager newEnv = new EnvManager();
			newEnv.set("[PPID]", parent_pid);
			return ScriptManager.execute(session, inputVector, newEnv, true, parent_pid, null);

		case "concat":

			//Script concat
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return StringFx.concat(inputVector);

		case "parallel":

			//Script parallel execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return JsonManager.format_Gson(ParallelManager.exe(env, inputVector, session, parent_pid, current_pid).toJSONString());

		case "case":

			//Case
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return Statement.case_statement(session, inputVector, env, parent_pid, current_pid);

		case "switch":

			//Case
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return Statement.switch_statement(session, inputVector, env, parent_pid, current_pid);

		case "+":

			//Addition
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return OperatorFx.add(inputVector, env);

		case "bi+":

			//Addition
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return OperatorFx.add_big_int(inputVector, env);

		default :

			//Synchronized or not
			if (Database.isSynchronized(inputVector)) {

				return CommandSyncAccess.execute(0, null, null, null, 0, session, inputVector, env, parent_pid, null, current_pid);

			}
			
			switch (inputVector.size()-1) {
			case 1:

				switch (inputVector.get(0).value) {
				case "break":

					throw new Exception("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at");

				case "continue":

					throw new Exception("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at");

				case "exit": case "quit": case "bye":

					return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht1";

				case "database_close": 

					//Close all data files
					DataFilePool.close();

					//Close all index files
					IndexFilePool.close();

					return "1";

				case "mentdb":

					return "#-------------------------------------------------;\n"+
					" #-  Welcome to MentDB !                 v_"+Start.version+"  -;\n"+
					" #-  The Mentalese Database Engine                -;\n"+
					" #-                                               -;\n"+
					" #-  https://www.mentdb.org                       -;\n"+
					" #-  © "+Start.copyright+", contact@mentdb.org            -;\n"+
					" #-                                               -;\n"+
					Misc.rpad(" #-  SID: "+session.idConnection+", User: "+session.user, " ", "50")+"-;\n"+
					" #-------------------------------------------------;";

				case "true":

					return "1";

				case "false":

					return "0";

				case "null": 

					return null;

				case "name":

					return Start.AI_FIRST_NAME+" "+Start.AI_LAST_NAME;

				case "@chat": 

					try {

						Session.allSessions.get(session.user).mqlQueryMode=false;

						return "CHAT mode activated (do not use directly with the editor, only in your application with the web socket driver).";
					} catch (Exception e) {
						if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
						else throw new Exception("Sorry, an error appears: "+e.getMessage());
					}

				case "@sid": 

					try {
						return ""+Session.allSessions.get(session.user).idConnection;
					} catch (Exception e) {
						if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
						else throw new Exception("Sorry, an error appears: "+e.getMessage());
					}

				case "@cmdid": 

					try {
						return ""+Session.allSessions.get(session.user).cmdId;
					} catch (Exception e) {
						if (!Session.allSessions.containsKey(session.user)) throw new Exception("Sorry, the user '"+session.user+"' is not connected through web-socket canal.");
						else throw new Exception("Sorry, an error appears: "+e.getMessage());
					}

				case "@sessions": 

					JSONArray jsonResult = new JSONArray();

					for (Entry<Long, WebSocketThread> e : WebSocketThread.allSessions.entrySet()) {

						if (!e.getValue().user.equals("")) {
							JSONObject sess = new JSONObject();
							sess.put("user", e.getValue().user);
							sess.put("nbExecution", e.getValue().cmdId);
							sess.put("id", e.getValue().idConnection);
							sess.put("life", ((Start.SESSION_TIMEOUT/1000)-(e.getValue().session.getIdleTimeout())/1000)+"/"+Start.SESSION_TIMEOUT/1000+"s");
							sess.put("used", ((Start.SESSION_TIMEOUT/1000)-(e.getValue().session.getIdleTimeout())/1000));
							sess.put("maxUsable", Start.SESSION_TIMEOUT/1000);
							jsonResult.add(sess);
						}

					}

					return JsonFormatter.format(jsonResult.toJSONString());

				case "who": 

					return session.user;

				case "sid": 

					return ""+session.idConnection;

				case "id": 

					return Misc.load("data"+File.separator+".id").replace("\r", "").replace("\n", "");

				case "version": 

					return Start.version;

				case "cmdid": 

					return ""+session.nbExecution;

				case "sessions": 

					jsonResult = new JSONArray();

					for (Entry<Long, SessionThreadAgent> e : SessionThreadAgent.allServerThread.entrySet()) {

						if (!e.getValue().serverThread.user.equals("")) {
							JSONObject sess = new JSONObject();
							sess.put("user", e.getValue().serverThread.user);
							sess.put("nbExecution", e.getValue().serverThread.nbExecution);
							sess.put("life", (System.currentTimeMillis()-e.getValue().serverThread.life)/1000+"/"+Start.SESSION_TIMEOUT/1000+"s");
							sess.put("used", (System.currentTimeMillis()-e.getValue().serverThread.life)/1000);
							sess.put("maxUsable", Start.SESSION_TIMEOUT/1000);
							sess.put("id", e.getValue().serverThread.idConnection);
							jsonResult.add(sess);
						}

					}

					return JsonFormatter.format(jsonResult.toJSONString());

				case "help":

					StringBuilder result = new StringBuilder("");


					for (Entry<String, Vector<MQLDocumentation>> e : MQLDocumentation.functions.entrySet()) {

						result.append(e.getKey().toUpperCase()+"\n");

						for(int i=0;i<e.getValue().size();i++) {


							//Get the current MQLDocumentation object
							MQLDocumentation currentFunction = e.getValue().get(i);

							result.append((currentFunction.webSocket?"@":"")+currentFunction.functionId);

							for(int r=0;r<currentFunction.parameters.size();r++) {

								result.append(" <"+currentFunction.parameters.get(r).name+">");

							}

							result.append(" : "+currentFunction.description+"\n");

							if (i==e.getValue().size()-1) result.append("\n");

						}
					}

					return result.toString();

				default:

					return inputVector.get(0).value;

				}

			case 2:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
				case "stack process": 
					
					StackManager.process();
					
					return "1";
									
				case "signal process": 
					
					ClusterManager.process_signal();
					return "1";

				case "bot show":
					
					return JsonManager.format_Gson(BotManager.show().toJSONString());

				case "auto commit":
					
					if (env.autoCommit) return "1";
					else return "0";

				case "stack process_limit": 
					
					return ""+StackManager.PROCESS_LIMIT;
									
				case "stack count_wait": 
					
					return StackManager.count_wait();
									
				case "stack count_running": 
					
					return StackManager.count_running();
									
				case "stack count_closed": 
					
					return StackManager.count_closed();
									
				case "stack count_error": 
					
					return StackManager.count_error();
									
				case "excel show":

					return JsonManager.format_Gson(ExcelManager.show(env).toJSONString());

				case "html show":

					return JsonManager.format_Gson(HTMLManager.show(env).toJSONString());

				case "excelx show":

					return JsonManager.format_Gson(ExcelxManager.show(env).toJSONString());

				case "html close_all": 

					return HTMLManager.closeall(env);

				case "excel close_all": 

					return ExcelManager.closeall(env);

				case "excelx close_all": 

					return ExcelxManager.closeall(env);

				case "excel build_in_format": 

					return JsonManager.format_Gson(ExcelManager.build_in_format().toJSONString());

				case "config reload": 

					Start.reload_config();

					return "1";

				case "os version": 

					return OsManager.version();

				case "os arch": 

					return OsManager.arch();

				case "os name": 

					return OsManager.name();

				case "os type": 

					return OsManager.type();

				case "os hostname": 

					return OsManager.hostname();

				case "os user_timezone": 

					return OsManager.user_timezone();

				case "os user_name": 

					return OsManager.user_name();

				case "os user_lang": 

					return OsManager.user_lang();

				case "os user_home": 

					return OsManager.user_home();

				case "os user_dir": 

					return OsManager.user_dir();

				case "mail replay_error_all": 

					SmtpManager.replay_all();

					return "1";

				case "mail delete_error_all": 

					SmtpManager.delete_all();

					return "1";

				case "mail process_limit": 

					return ""+SmtpManager.PROCESS_LIMIT;

				case "mail count_loaded": 

					return ""+SmtpManager.loadedProcess.size();

				case "mail count_all": 

					return SmtpManager.count_all();

				case "mail count_error": 

					return SmtpManager.count_error();

				case "mail min_error_date": 

					return SmtpManager.get_min_date();

				case "log remove": 

					MYSQLManager.removeLog();

					return "1";

				case "file reader_closeall": 

					return JsonManager.format_Gson(FileFx.reader_closeall(env).toJSONString());

				case "file writer_closeall": 

					return JsonManager.format_Gson(FileFx.writer_closeall(env).toJSONString());

				case "file reader_show": 

					return JsonManager.format_Gson(FileFx.reader_show(env).toJSONString());

				case "file writer_show": 

					return JsonManager.format_Gson(FileFx.writer_show(env).toJSONString());

				case "file cur_abs_dir": 

					return FileFx.cur_abs_dir();

				case "file cur_canonical_dir": 

					return FileFx.cur_canonical_dir();

				case "file pwd": 

					return FileFx.pwd();

				case "constant physics_z0": 

					return ConstantFx.physics_z0();

				case "constant physics_tp": 

					return ConstantFx.physics_tp();

				case "constant physics_stefan_boltzmann": 

					return ConstantFx.physics_stefan_boltzmann();

				case "constant physics_rinf": 

					return ConstantFx.physics_rinf();

				case "constant physics_r": 

					return ConstantFx.physics_r();

				case "constant physics_planck_over_two_pi": 

					return ConstantFx.physics_planck_over_two_pi();

				case "constant physics_planck": 

					return ConstantFx.physics_planck();

				case "constant physics_perm_vac_magnetic": 

					return ConstantFx.physics_perm_vac_magnetic();

				case "constant physics_perm_vac_electric": 

					return ConstantFx.physics_perm_vac_electric();

				case "constant physics_nuclear_magneton": 

					return ConstantFx.physics_nuclear_magneton();

				case "constant physics_newtonian_g": 

					return ConstantFx.physics_newtonian_g();

				case "constant physics_n": 

					return ConstantFx.physics_n();

				case "constant physics_mp": 

					return ConstantFx.physics_mp();

				case "constant physics_magn_flux_quantum": 

					return ConstantFx.physics_magn_flux_quantum();

				case "constant physics_m_proton": 

					return ConstantFx.physics_m_proton();

				case "constant physics_m_neutron": 

					return ConstantFx.physics_m_neutron();

				case "constant physics_m_muon": 

					return ConstantFx.physics_m_muon();

				case "constant physics_m_electron": 

					return ConstantFx.physics_m_electron();

				case "constant physics_m_deuteron": 

					return ConstantFx.physics_m_deuteron();

				case "constant physics_lp": 

					return ConstantFx.physics_lp();

				case "constant physics_k": 

					return ConstantFx.physics_k();

				case "constant physics_g0": 

					return ConstantFx.physics_g0();

				case "constant physics_g": 

					return ConstantFx.physics_g();

				case "constant physics_fine_structure": 

					return ConstantFx.physics_fine_structure();

				case "constant physics_f": 

					return ConstantFx.physics_f();

				case "constant physics_eh": 

					return ConstantFx.physics_eh();

				case "constant physics_e": 

					return ConstantFx.physics_e();

				case "constant physics_c_square": 

					return ConstantFx.physics_c_square();

				case "constant physics_c": 

					return ConstantFx.physics_c();

				case "constant physics_bohr_magneton": 

					return ConstantFx.physics_bohr_magneton();

				case "constant physics_amu": 

					return ConstantFx.physics_amu();

				case "constant physics_a0": 

					return ConstantFx.physics_a0();

				case "constant math_two_pi": 

					return ConstantFx.math_two_pi();

				case "constant math_pi_square": 

					return ConstantFx.math_pi_square();

				case "constant math_pi": 

					return ConstantFx.math_pi();

				case "constant math_four_pi": 

					return ConstantFx.math_four_pi();

				case "constant math_half_pi": 

					return ConstantFx.math_half_pi();

				case "json show": 

					return JsonManager.format_Gson(JsonManager.show(env).toJSONString());

				case "xml show": 

					return JsonManager.format_Gson(XmlManager.show(env).toJSONString());

				case "sql show":

					return JsonManager.format_Gson(SQLManager.show(env).toJSONString());

				case "json unload_all":

					return JsonManager.unloadall(env);

				case "xml unload_all":

					return XmlManager.unloadall(env);

				case "src count":

					int i1 = Integer.parseInt(FileFx.count_dir("src", ".java"));
					int i2 = FileFx.count_lines("mql"+File.separator+"basic-integration.mql");
					int i3 = 0;

					String[] files = (new File("mql"+File.separator+"scripts")).list();
					for(int z=0;z<files.length;z++) {

						i3+= FileFx.count_lines("mql"+File.separator+"scripts"+File.separator+files[z]);

					}

					return (i1+i2+i3)+"";

				case "function count":

					return MQLDocumentation.nbFunction+"";

				case "math e":

					return MathFx.e();

				case "math pi":

					return MathFx.pi();

				case "date current_ms":

					return System.currentTimeMillis()+"";

				case "date current_ns":

					return System.nanoTime()+"";

				case "date curdate":

					return DateFx.curdate();

				case "date current_date":

					return DateFx.current_date();

				case "date current_time":

					return DateFx.current_time();

				case "date current_timestamp":

					return DateFx.current_timestamp();

				case "date curtime":

					return DateFx.curtime();

				case "date curtimestamp":

					return DateFx.curtimestamp();

				case "date full_systimestamp":

					return DateFx.full_systimestamp();

				case "date sysdate":

					return DateFx.sysdate();

				case "date systime":

					return DateFx.systime();

				case "date systimestamp": case "date now":

					return DateFx.systimestamp();

				case "date systimestamp_min":

					return DateFx.systimestamp_min();

				case "env show":

					return NodeManager.format(env.show().toJSONString());

				case "@count sessions":

					return ""+WebSocketThread.allSessions.size();

				case "@exceeded sessions":

					return ""+WebSocketThread.exceededSessions;

				case "metric system":

					String r = "AI: "+Start.AI_FIRST_NAME.substring(0, 1).toUpperCase()+Start.AI_FIRST_NAME.substring(1).toLowerCase()+" "+Start.AI_LAST_NAME.substring(0, 1).toUpperCase()+Start.AI_LAST_NAME.substring(1).toLowerCase()+"\n";
					r += "ID: "+Misc.load("data"+File.separator+".id").replace("\r", "").replace("\n", "")+"\n";
					r += "MentDB: v_"+Start.version+"\n";
					r += "Author: Jimmitry PAYET, Programmer Analyst\n";
					r += "Contact: jim@mentdb.org\n";
					r += "Java: v_"+Statistic.systemJavaVersion()+", "+Statistic.systemJavaVendor()+"\n";
					r += Statistic.systemName()+": v_"+Statistic.systemVersion()+", "+Statistic.systemArchitecture()+", nb_proc: "+Statistic.systemNbProcessor()+"\n";
					r += "Cpu jvm: "+Statistic.currentJvmCpuValue()+"%, Cpu sys: "+Statistic.currentSystemCpuValue()+"%, Used jvm mem: "+Statistic.currentMemJvm()+"M, Used mem: "+Statistic.getUsedPhysicalMemorySize()+"/"+Statistic.getTotalPhysicalMemorySize()+"M, Used swap mem: "+Statistic.getUsedSwapSpaceSize()+"/"+Statistic.getTotalSwapSpaceSize()+"M\n";

					r += "File system roots:\n";
					JSONArray array = Misc.loadArray(Statistic.fileSystemRoots());
					for(int i=0;i<array.size();i++) {
						JSONObject obj = (JSONObject) array.get(i);

						r += "  - ["+obj.get("absolutePath")+"] "+((100*Integer.parseInt(""+obj.get("usedSpace")))/Integer.parseInt(""+obj.get("totalSpace")))+"%";
						r += ", usedSpace: "+obj.get("usedSpace")+"M";
						r += ", freeSpace: "+obj.get("freeSpace")+"M";
						r += ", totalSpace: "+obj.get("totalSpace")+"M";

						r += "\n";
					};

					return r;

				case "mail process": 

					SmtpManager.process();

					return "1";

				case "exceeded sessions":

					return ""+Start.exceededSessions;

				case "metric date":

					return Statistic.stat_date.toString();

				case "log show":

					return Log.show(0);

				case "ai firstname":

					return Start.AI_FIRST_NAME;

				case "ai lastname":

					return Start.AI_LAST_NAME;

				case "ai name":

					return Start.AI_FIRST_NAME+" "+Start.AI_LAST_NAME;

				default:

					switch (inputVector.get(0).value) {
					case "kill":

						//Get time
						long sid = 0;

						try {

							sid = Long.parseLong(inputVector.get(1).value);

						} catch (Exception e) {

							throw new Exception("Sorry, the session id is not valid (ex: 234).");

						}

						Start.kill_session(sid);

						return "1";

					case "kill_process":

						String process_id = inputVector.get(1).value;

						Start.kill_process(process_id);

						return "The process will be stoped at the next MQL command ...";

					case "wait":

						//Get time
						int time = 0;

						try {

							time = Integer.parseInt(inputVector.get(1).value);

						} catch (Exception e) {

							throw new Exception("Sorry, the time is not valid (ex: 10000 for 10s).");

						}

						Thread.sleep(time);

						return "1";

					case "$":

						String str = inputVector.get(1).value;

						if (str.equals("[OK]")) {
							System.out.println("[OK]");
						} else {
							System.out.print(" + "+StringFx.rpad(str, " ", "58"));
						}

						return "1";

					case "eval":

						//Get parameters
						String mql = inputVector.get(1).value;

						return Statement.eval(session, mql, env, parent_pid, current_pid);

					case "mql":

						return inputVector.get(1).value;

					case "++":

						//Get parameters
						String varName = inputVector.get(1).value;

						return env.incr(varName, "1");

					case "--":

						//Get parameters
						varName = inputVector.get(1).value;

						return env.decr(varName, "1");

					case "not":

						//Get parameters
						String bool = inputVector.get(1).value;

						return OperatorFx.not(bool);

					default:

						//Script execution
						inputVector.remove(inputVector.size()-1);

						return concatOrUnknow(inputVector);

					}

				}

			case 3:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
				case "dl bayesian show": 

					return JsonManager.format_Gson(BayesianNeuralNetworkManager.show().toJSONString());

				case "dl n_bayesian show": 

					return JsonManager.format_Gson(TextCatManager.show().toJSONString());

				case "ml cluster show":
					
					return JsonManager.format_Gson(MLManager.showClusters(env).toJSONString());
					
				case "ml h_node show":
					
					return JsonManager.format_Gson(MLManager.showHeuristicNode(env).toJSONString());
					
				case "pa rl show":
					
					return JsonManager.format_Gson(PALinearRegression.show(env).toJSONString());
					
				case "pa rm show":
					
					return JsonManager.format_Gson(PAMultipleRegressionOLS.show(env).toJSONString());
					
				case "ml cluster close_all":
					
					return MLManager.closeall(env);
					
				case "ml h_node close_all":
					
					return MLManager.closeallHeuristicNode(env);
					
				case "pa rl close_all":
					
					return PALinearRegression.closeall(env);
					
				case "pa rm close_all":
					
					return PAMultipleRegressionOLS.closeall(env);
					
				case "app webserver restart":
					
					AppManager.restart_webserver(session);
					
					return "1";

				case "app menu show":
					
					return AppManager.menuShow(env);

				case "log archive path":

					return Start.LOG_ARCHIVE_PATH;

				case "log archive size":

					return Start.LOG_ARCHIVE_SIZE;

				case "log retention day":

					return Start.LOG_RETENTION_DAYS;

				case "@reset exceeded sessions":

					WebSocketThread.exceededSessions = 0;

					return "Exceeded sessions has been reset successfully.";

				case "metric java vendor":

					return Statistic.systemJavaVendor();

				case "metric java version":

					return Statistic.systemJavaVersion();

				case "metric system name":

					return Statistic.systemName();

				case "metric system architecture":

					return Statistic.systemArchitecture();

				case "metric system version":

					return Statistic.systemVersion();

				case "reset exceeded sessions":

					Start.exceededSessions = 0;

					return "Exceeded sessions has been reset successfully.";

				case "metric total mem":

					return Statistic.getTotalPhysicalMemorySize()+"";

				case "log current id":

					return Log.currentLogFileId+"";

				case "tunnel disconnect all":

					return TunnelManager.disconnectall(env);

				case "sql disconnect all":

					return JsonManager.format_Gson(SQLManager.disconnectAll(env).toJSONString());

				case "ftp disconnect all":

					return FtpManager.disconnectall(env);

				case "ftps disconnect all":

					return FtpsManager.disconnectall(env);

				case "sftp disconnect all":

					return SftpManager.disconnectall(env);

				case "ssh disconnect all":

					return SshManager.disconnectall(env);

				default: 

					switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
					case "string mql_to_html":

						return Misc.splitCommandHtml(inputVector.get(2).value);

					case "string encode_des_generate_key":
						
						//Get params
						String keysize = inputVector.get(2).value;
						
						return StringFx.encode_des_generate_key(keysize);

					case "string encode_rsa_generate_keypair":
						
						//Get params
						keysize = inputVector.get(2).value;
						
						return JsonFormatter.format(MyRSA.generate_key(keysize).toJSONString());

					case "string encode_sign_generate_keypair":
						
						//Get params
						keysize = inputVector.get(2).value;
						
						return JsonFormatter.format(StringFx.encode_sign_generate_key(keysize).toJSONString());

					case "signal remote_show":
						
						//Get params
						String cluster_id = inputVector.get(2).value;
						
						return ClusterManager.signals_remote_show(env, session.idConnection, cluster_id);

					case "bot remove":
						
						//Get parameters
						String botName = inputVector.get(2).value;
						
						BotManager.delete_bot(botName);
		
						return "1";
		
					case "bot exist":
						
						//Get parameters
						botName = inputVector.get(2).value;
						
						return BotManager.exist_bot(botName);
		
					case "app is_granted_a":
						
						//Get parameters
						String tag = inputVector.get(2).value;
		
						return AppManager.is_granted_object_a(env, tag);
		
					case "app is_granted_sa":
						
						//Get parameters
						tag = inputVector.get(2).value;
		
						return AppManager.is_granted_object_sa(env, tag);
		
					case "mql encode":

						String str = inputVector.get(2).value;

						return StringFx.mql_encode(str);

					case "sql set_timeout":
						
						//Get parameters
						String timeout = inputVector.get(2).value;
						
						SQLManager.set_timeout(env, timeout);
		
						return "1";
		
					case "pa xy_scatter":
						
						//Get parameters
						String json = inputVector.get(2).value;
		
						return "j23i88m90m76i39t04r09y35p14a96y09e57t43"+PALinearRegression.xy_scatter(json);
		
					case "stack delete_wait_id":
						
						String pid = inputVector.get(2).value;
						
						return StackManager.delete_wait_id(pid);
		
					case "stack delete_wait_script":
						
						String script = inputVector.get(2).value;
						
						return StackManager.delete_wait_script(script);
		
					case "stack delete_closed_id":
						
						pid = inputVector.get(2).value;
						
						return StackManager.delete_closed_id(pid);
		
					case "stack delete_closed_script":
						
						script = inputVector.get(2).value;
						
						return StackManager.delete_closed_script(script);
		
					case "stack delete_error_id":
						
						pid = inputVector.get(2).value;
						
						return StackManager.delete_error_id(pid);
		
					case "stack delete_error_script":
						
						script = inputVector.get(2).value;
						
						return StackManager.delete_error_script(script);
		
					case "stack reset_error_nbattempt_id":
						
						pid = inputVector.get(2).value;
						
						return StackManager.reset_nb_attempt_id(pid);
		
					case "stack reset_error_nbattempt_script":
						
						script = inputVector.get(2).value;
						
						return StackManager.reset_nb_attempt_script(script);
					
					case "stack replay_error_id":
						
						pid = inputVector.get(2).value;
						
						return StackManager.replay_error_id(pid);
		
					case "stack replay_error_script":
						
						script = inputVector.get(2).value;
						
						return StackManager.replay_error_script(script);
		
					case "stack get":
						
						pid = inputVector.get(2).value;
						
						return JsonManager.format_Gson(StackManager.get_row(pid).toJSONString());
		
					case "app show":
						
						String context = inputVector.get(2).value;
						
						return JsonManager.format_Gson(AppManager.show_context(context).toJSONString());
		
					case "excel exist":

						String excelId = inputVector.get(2).value;

						return ExcelManager.exist(env, excelId);

					case "html exist":

						String domId = inputVector.get(2).value;

						return HTMLManager.exist(env, domId);

					case "excelx exist":

						excelId = inputVector.get(2).value;

						return ExcelxManager.exist(env, excelId);

					case "excel close":

						excelId = inputVector.get(2).value;

						return ExcelManager.close(env, excelId);

					case "html close":

						domId = inputVector.get(2).value;

						return HTMLManager.close(env, domId);

					case "excelx close":

						excelId = inputVector.get(2).value;

						return ExcelxManager.close(env, excelId);

					case "log show_time":

						pid = inputVector.get(2).value;

						return Log.show_time(pid);

					case "mail show":

						String limit = inputVector.get(2).value;

						return JsonManager.format_Gson(SmtpManager.show(limit));

					case "mail show_error":

						limit = inputVector.get(2).value;

						return JsonManager.format_Gson(SmtpManager.show_error(limit));

					case "mail replay_error_id":

						String mailId = inputVector.get(2).value;

						SmtpManager.replay_id(mailId);

						return "1";

					case "mail replay_error_cm":

						String cm = inputVector.get(2).value;

						SmtpManager.replay_cm(cm);

						return "1";

					case "mail delete_error_id":

						mailId = inputVector.get(2).value;

						SmtpManager.delete_id(mailId);

						return "1";

					case "mail delete_error_cm":

						cm = inputVector.get(2).value;

						SmtpManager.delete_cm(cm);

						return "1";

					case "mail get_body":

						mailId = inputVector.get(2).value;

						return SmtpManager.get_body(mailId);

					case "mail get_error":

						mailId = inputVector.get(2).value;

						return SmtpManager.get_error(mailId);

					case "tunnel disconnect":

						String sessionId = inputVector.get(2).value;

						return TunnelManager.close(env, sessionId);

					case "log reset":

						String code = inputVector.get(2).value;

						if (code.equals("18061980")) {

							MYSQLManager.resetLog();

							return "1";
						} else return "0";

					case "file reader_get_line":

						String readerId = inputVector.get(2).value;

						return FileFx.reader_get_line(env, readerId);

					case "file reader_close":

						readerId = inputVector.get(2).value;

						return FileFx.reader_close(env, readerId);

					case "file reader_exist":

						readerId = inputVector.get(2).value;

						return FileFx.reader_exist(env, readerId);

					case "file writer_exist":

						String writerId = inputVector.get(2).value;

						return FileFx.writer_exist(env, writerId);

					case "file writer_flush":

						writerId = inputVector.get(2).value;

						return FileFx.writer_flush(env, writerId);

					case "file writer_close":

						writerId = inputVector.get(2).value;

						return FileFx.writer_close(env, writerId);

					case "file b64_read":

						String filePath = inputVector.get(2).value;

						return FileFx.b64_read(filePath);

					case "file is_directory":

						filePath = inputVector.get(2).value;

						return FileFx.is_directory(filePath);

					case "file mkdir":

						String dirPath = inputVector.get(2).value;

						return FileFx.mkdir(dirPath);

					case "file dir_list":

						dirPath = inputVector.get(2).value;

						return JsonManager.format_Gson(FileFx.dir_list(dirPath).toJSONString());

					case "file delete":

						String path = inputVector.get(2).value;

						return FileFx.delete(path);

					case "file size":

						filePath = inputVector.get(2).value;

						return FileFx.size(filePath);

					case "file last_modified":

						filePath = inputVector.get(2).value;

						return FileFx.last_modified(filePath);

					case "file exist":

						filePath = inputVector.get(2).value;

						return FileFx.exist(filePath);

					case "file count_lines":

						filePath = inputVector.get(2).value;

						return ""+FileFx.count_lines(filePath);

					case "ftps active":

						sessionId = inputVector.get(2).value;

						return FtpsManager.active(env, sessionId);

					case "ftps passive":

						sessionId = inputVector.get(2).value;

						return FtpsManager.passive(env, sessionId);

					case "ftps ls":

						sessionId = inputVector.get(2).value;

						return JsonManager.format_Gson(FtpsManager.ls_files(env, sessionId).toJSONString());

					case "ftp disconnect":

						sessionId = inputVector.get(2).value;

						return FtpManager.disconnect(env, sessionId);

					case "ssh disconnect":

						sessionId = inputVector.get(2).value;

						return SshManager.disconnect(env, sessionId);

					case "ftps disconnect":

						sessionId = inputVector.get(2).value;

						return FtpsManager.disconnect(env, sessionId);

					case "sftp disconnect":

						sessionId = inputVector.get(2).value;

						return SftpManager.disconnect(env, sessionId);

					case "ftp active":

						sessionId = inputVector.get(2).value;

						return FtpManager.active(env, sessionId);

					case "ftp passive":

						sessionId = inputVector.get(2).value;

						return FtpManager.passive(env, sessionId);

					case "ftp pwd":

						sessionId = inputVector.get(2).value;

						return FtpManager.pwd(env, sessionId);

					case "sftp pwd":

						sessionId = inputVector.get(2).value;

						return SftpManager.pwd(env, sessionId);

					case "sftp home":

						sessionId = inputVector.get(2).value;

						return SftpManager.gethome(env, sessionId);

					case "sftp version":

						sessionId = inputVector.get(2).value;

						return SftpManager.getversion(env, sessionId);

					case "sftp lpwd":

						sessionId = inputVector.get(2).value;

						return SftpManager.lpwd(env, sessionId);

					case "ftps pwd":

						sessionId = inputVector.get(2).value;

						return FtpsManager.pwd(env, sessionId);

					case "xml escape_10":

						String data = inputVector.get(2).value;

						return XmlManager.escape_10(data);

					case "xml escape_11":

						data = inputVector.get(2).value;

						return XmlManager.escape_11(data);

					case "json exist":

						String key = inputVector.get(2).value;

						return JsonManager.exist(env, key);

					case "xml doc":

						key = inputVector.get(2).value;

						return XmlManager.doc(env, key);

					case "xml exist":

						key = inputVector.get(2).value;

						return XmlManager.exist(env, key);

					case "sql encode":

						data = inputVector.get(2).value;

						return SQLManager.encode(data);

					case "sql commit":

						String sqlId = inputVector.get(2).value;

						SQLManager.commit(env, sqlId);

						return "1";

					case "sql rollback":

						sqlId = inputVector.get(2).value;

						SQLManager.rollback(env, sqlId);

						return "1";

					case "sql disconnect":

						sqlId = inputVector.get(2).value;

						SQLManager.disconnect(env, sqlId);

						return "1";

					case "file load":

						filePath = inputVector.get(2).value;

						return FileFx.load(filePath);

					case "json doc":

						key = inputVector.get(2).value;

						return JsonManager.doc(env, key);

					case "json unload":

						key = inputVector.get(2).value;

						return JsonManager.unload(env, key);

					case "xml unload":

						key = inputVector.get(2).value;

						return XmlManager.unload(env, key);

					case "in editor":

						str = inputVector.get(2).value;

						return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+str;

					case "in clipboard":

						str = inputVector.get(2).value;

						return "j23i88m90m76i39t04r09y35p14a96y09e57t40"+str;

					case "in out_editor":

						str = inputVector.get(2).value;

						return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+str;

					case "in activity":

						str = inputVector.get(2).value;

						return "j23i88m90m76i39t04r09y35p14a96y09e57t42"+str;

					case "in scatter":

						str = inputVector.get(2).value;

						return "j23i88m90m76i39t04r09y35p14a96y09e57t43"+str;

					case "string ascii":

						String chr = inputVector.get(2).value;

						return StringFx.ascii( chr);

					case "string md5":

						str = inputVector.get(2).value;

						return StringFx.md5(str);

					case "string sha":

						str = inputVector.get(2).value;

						return StringFx.sha(str);

					case "string bin":

						String num = inputVector.get(2).value;

						return StringFx.bin( num);

					case "string bit_length":

						str = inputVector.get(2).value;

						return StringFx.bit_length( str);

					case "string char":

						num = inputVector.get(2).value;

						return StringFx.char_str( num);

					case "string char_length":

						str = inputVector.get(2).value;

						return StringFx.char_length( str);

					case "string char_to_int":

						chr = inputVector.get(2).value;

						return StringFx.char_to_int( chr);

					case "string encode_b64":

						String string = inputVector.get(2).value;

						return StringFx.encode_b64( string);

					case "string decode_b64":

						string = inputVector.get(2).value;

						return StringFx.decode_b64( string);

					case "string first_letter_upper":

						str = inputVector.get(2).value;

						return StringFx.first_letter_upper( str);

					case "string first_letter":

						str = inputVector.get(2).value;

						return StringFx.first_letter(str);

					case "string generate_random_str":

						String size = inputVector.get(2).value;

						return StringFx.generate_random_str( size);

					case "string hex":

						num = inputVector.get(2).value;

						return StringFx.hex( num);

					case "string hex_to_int":

						String hex = inputVector.get(2).value;

						return StringFx.hex_to_int( hex);

					case "string hex_to_str":

						hex = inputVector.get(2).value;

						return StringFx.hex_to_str( hex);

					case "string int_to_char":

						num = inputVector.get(2).value;

						return StringFx.int_to_char( num);

					case "string int_to_hex":

						num = inputVector.get(2).value;

						return StringFx.int_to_hex( num);

					case "string int_to_oct":

						num = inputVector.get(2).value;

						return StringFx.int_to_oct( num);

					case "string is_letter":

						String value = inputVector.get(2).value;

						return StringFx.is_letter( value);

					case "string is_alpha_num_uds":

						value = inputVector.get(2).value;

						return StringFx.is_alpha_num_uds( value);

					case "string is_alpha_num":

						value = inputVector.get(2).value;

						return StringFx.is_alpha_num( value);

					case "string is_number_char":

						value = inputVector.get(2).value;

						return StringFx.is_number_char( value);

					case "string itrim":

						str = inputVector.get(2).value;

						return StringFx.itrim( str);

					case "string lcase":

						str = inputVector.get(2).value;

						return StringFx.lcase( str);

					case "string length":

						str = inputVector.get(2).value;

						return StringFx.length( str);

					case "string lower":

						str = inputVector.get(2).value;

						return StringFx.lower( str);

					case "string lrtrim":

						str = inputVector.get(2).value;

						return StringFx.lrtrim( str);

					case "string lrtrim0":

						str = inputVector.get(2).value;

						return StringFx.lrtrim0( str);

					case "string ltrim":

						str = inputVector.get(2).value;

						return StringFx.ltrim( str);

					case "string oct":

						num = inputVector.get(2).value;

						return StringFx.oct( num);

					case "string oct_to_int":

						String oct = inputVector.get(2).value;

						return StringFx.oct_to_int( oct);

					case "string reverse":

						str = inputVector.get(2).value;

						return StringFx.reverse( str);

					case "string rtrim":

						str = inputVector.get(2).value;

						return StringFx.rtrim( str);

					case "string space":

						String count = inputVector.get(2).value;

						return StringFx.space( count);

					case "string str_to_hex":

						str = inputVector.get(2).value;

						return StringFx.str_to_hex( str);

					case "string to_string":

						str = inputVector.get(2).value;

						return StringFx.to_string( str);

					case "string empty_if_null":

						str = inputVector.get(2).value;

						return StringFx.empty_if_null( str);

					case "string null_if_empty":

						str = inputVector.get(2).value;

						return StringFx.null_if_empty( str);

					case "string trim":

						str = inputVector.get(2).value;

						return StringFx.trim( str);

					case "string txt":

						data = inputVector.get(2).value;

						return StringFx.txt( data);

					case "string txt2":

						data = inputVector.get(2).value;

						return StringFx.txt2( data);

					case "string ucase":

						str = inputVector.get(2).value;

						return StringFx.ucase( str);

					case "string unhex":

						hex = inputVector.get(2).value;

						return StringFx.unhex( hex);

					case "string upper":

						str = inputVector.get(2).value;

						return StringFx.upper( str);

					case "string zero":

						str = inputVector.get(2).value;

						return StringFx.zero( str);

					case "type is_date":

						String date = inputVector.get(2).value;

						return TypeFx.is_date( date);

					case "type is_email":

						String emailAddress = inputVector.get(2).value;

						return TypeFx.is_email( emailAddress);

					case "type is_hour":

						String stringHour = inputVector.get(2).value;

						return TypeFx.is_hour( stringHour);

					case "type is_time":

						stringHour = inputVector.get(2).value;

						return TypeFx.is_time( stringHour);

					case "type is_hour_without_sec":

						stringHour = inputVector.get(2).value;

						return TypeFx.is_hour_without_sec( stringHour);

					case "type is_number":

						value = inputVector.get(2).value;

						return TypeFx.is_number( value);

					case "type is_byte":

						value = inputVector.get(2).value;

						return TypeFx.is_byte( value);

					case "type is_small_int":

						value = inputVector.get(2).value;

						return TypeFx.is_small_int( value);

					case "type is_medium_int":

						value = inputVector.get(2).value;

						return TypeFx.is_medium_int( value);

					case "type is_int":

						value = inputVector.get(2).value;

						return TypeFx.is_int( value);

					case "type is_big_int":

						value = inputVector.get(2).value;

						return TypeFx.is_big_int( value);

					case "type is_float":

						value = inputVector.get(2).value;

						return TypeFx.is_float( value);

					case "type is_double":

						value = inputVector.get(2).value;

						return TypeFx.is_double( value);

					case "type is_timestamp":

						value = inputVector.get(2).value;

						return TypeFx.is_timestamp( value);

					case "type is_valid_date":

						String dateToValidate = inputVector.get(2).value;

						return TypeFx.is_valid_date( dateToValidate);

					case "type is_valid_time":

						String timeToValidate = inputVector.get(2).value;

						return TypeFx.is_valid_time( timeToValidate);

					case "type is_valid_timestamp":

						String timestampToValidate = inputVector.get(2).value;

						return TypeFx.is_valid_timestamp( timestampToValidate);

					case "math abs":

						String number = inputVector.get(2).value;

						return MathFx.abs( number);

					case "math avg":

						String array = inputVector.get(2).value;

						return MathFx.avg(array);

					case "math acos":

						number = inputVector.get(2).value;

						return MathFx.acos( number);

					case "math asin":

						number = inputVector.get(2).value;

						return MathFx.asin( number);

					case "math atan":

						number = inputVector.get(2).value;

						return MathFx.atan( number);

					case "math cbrt":

						number = inputVector.get(2).value;

						return MathFx.cbrt( number);

					case "math ceil":

						number = inputVector.get(2).value;

						return MathFx.ceil( number);

					case "math ceiling":

						number = inputVector.get(2).value;

						return MathFx.ceiling( number);

					case "math cos":

						number = inputVector.get(2).value;

						return MathFx.cos( number);

					case "math cosh":

						number = inputVector.get(2).value;

						return MathFx.cosh( number);

					case "math cot":

						number = inputVector.get(2).value;

						return MathFx.cot( number);

					case "math deg_to_rad":

						number = inputVector.get(2).value;

						return MathFx.deg_to_rad( number);

					case "math exp":

						number = inputVector.get(2).value;

						return MathFx.exp( number);

					case "math expm1":

						number = inputVector.get(2).value;

						return MathFx.expm1( number);

					case "math floor":

						number = inputVector.get(2).value;

						return MathFx.floor( number);

					case "math log":

						number = inputVector.get(2).value;

						return MathFx.log( number);

					case "math log10":

						number = inputVector.get(2).value;

						return MathFx.log10( number);

					case "math log1p":

						number = inputVector.get(2).value;

						return MathFx.log1p( number);

					case "math rad_to_deg":

						number = inputVector.get(2).value;

						return MathFx.rad_to_deg( number);

					case "math random":

						number = inputVector.get(2).value;

						return MathFx.random( number);

					case "math rint":

						number = inputVector.get(2).value;

						return MathFx.rint( number);

					case "math sign":

						number = inputVector.get(2).value;

						return MathFx.sign( number);

					case "math signum":

						number = inputVector.get(2).value;

						return MathFx.signum( number);

					case "math sin":

						number = inputVector.get(2).value;

						return MathFx.sin( number);

					case "math sinh":

						number = inputVector.get(2).value;

						return MathFx.sinh( number);

					case "math sqrt":

						number = inputVector.get(2).value;

						return MathFx.sqrt( number);

					case "math tan":

						number = inputVector.get(2).value;

						return MathFx.tan( number);

					case "math tanh":

						number = inputVector.get(2).value;

						return MathFx.tanh( number);

					case "math ulp":

						number = inputVector.get(2).value;

						return MathFx.ulp( number);

					case "date ts_to_long":

						String timestamp = inputVector.get(2).value;

						return DateFx.ts_to_long( timestamp);

					case "date dt_to_long":

						date = inputVector.get(2).value;

						return DateFx.dt_to_long( date);

					case "date long_to_dt":

						date = inputVector.get(2).value;

						return DateFx.long_to_dt( date);

					case "date long_to_ts":

						date = inputVector.get(2).value;

						return DateFx.long_to_ts( date);

					case "date day_of_month":

						date = inputVector.get(2).value;

						return DateFx.day_of_month( date);

					case "date day_of_week":

						date = inputVector.get(2).value;

						return DateFx.day_of_week( date);

					case "date day_of_year":

						date = inputVector.get(2).value;

						return DateFx.day_of_year( date);

					case "date dayname":

						date = inputVector.get(2).value;

						return DateFx.dayname( date);

					case "date hour":

						String time = inputVector.get(2).value;

						return DateFx.hour( time);

					case "date is_valid_date":

						dateToValidate = inputVector.get(2).value;

						return DateFx.is_valid_date( dateToValidate);

					case "date is_valid_time":

						timeToValidate = inputVector.get(2).value;

						return DateFx.is_valid_time( timeToValidate);

					case "date is_valid_timestamp":

						timestampToValidate = inputVector.get(2).value;

						return DateFx.is_valid_timestamp( timestampToValidate);

					case "date minute":

						time = inputVector.get(2).value;

						return DateFx.minute( time);

					case "date month":

						date = inputVector.get(2).value;

						return DateFx.month( date);

					case "date monthname":

						date = inputVector.get(2).value;

						return DateFx.monthname( date);

					case "date seconde":

						time = inputVector.get(2).value;

						return DateFx.seconde( time);

					case "date time":

						timestamp = inputVector.get(2).value;

						return DateFx.time( timestamp);

					case "date year":

						date = inputVector.get(2).value;

						return DateFx.year( date);

					case "is empty":

						//Get parameters
						str = inputVector.get(2).value;

						return OperatorFx.is_empty(str);

					case "is null":

						//Get parameters
						str = inputVector.get(2).value;

						return OperatorFx.is_null(str);

					case "log show":

						String nbLine = inputVector.get(2).value;

						//Check if the number by page is valid
						try {

							Integer.parseInt(nbLine);

						} catch (Exception e) {

							throw new Exception("Sorry, the number of line is not valid.");

						}

						return Log.show(Integer.parseInt(nbLine));

					case "log trace":

						//Get text
						String text = inputVector.get(2).value;

						Log.trace(text);

						return "1";

					default:

						switch (inputVector.get(0).value) {
						case "->":

							String var = inputVector.get(1).value;
							String val = inputVector.get(2).value;

							//Generate an error if the variable name is not valid
							if (!EnvManager.is_valid_varname(var)) {
								throw new Exception("Sorry, the variable name "+var+" is not valid (example: [var1]).");
							}

							env.set(var, val);

							return val;

						case ">":

							//Get parameters
							String number1 = inputVector.get(1).value;
							String number2 = inputVector.get(2).value;

							return OperatorFx.greater(number1, number2);

						case ">=":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.greater_or_equal(number1, number2);

						case "<":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.less(number1, number2);

						case "<=":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.less_or_equal(number1, number2);

						case "==":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.num_equal(number1, number2);

						case "!=":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.not_num_equal(number1, number2);

						case "equal":

							//Get parameters
							String str1 = inputVector.get(1).value;
							String str2 = inputVector.get(2).value;

							return OperatorFx.equal(str1, str2);

						case "-":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.sub(number1, number2);

						case "bi-":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.sub_big_int(number1, number2);

						case "*":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.mul(number1, number2);

						case "bi*":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.mul_big_int(number1, number2);

						case "/":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.div(number1, number2);

						case "bi/":

							//Get parameters
							number1 = inputVector.get(1).value;
							number2 = inputVector.get(2).value;

							return OperatorFx.div_big_int(number1, number2);

						case "and":

							//Get parameters
							String boolean1 = inputVector.get(1).value;
							String boolean2 = inputVector.get(2).value;

							return OperatorFx.and(session, boolean1, boolean2, env, parent_pid, current_pid);

						case "or":

							//Get parameters
							boolean1 = inputVector.get(1).value;
							boolean2 = inputVector.get(2).value;

							return OperatorFx.or(session, boolean1, boolean2, env, parent_pid, current_pid);

						case "xor":

							//Get parameters
							boolean1 = inputVector.get(1).value;
							boolean2 = inputVector.get(2).value;

							return OperatorFx.xor(boolean1, boolean2);

						case "if":

							//Get parameters
							String condition = inputVector.get(1).value;
							String trueAction = inputVector.get(2).value;

							return Statement.if_mql(session, condition, trueAction, env, parent_pid, current_pid);

						case "while":

							//Get parameters
							condition = inputVector.get(1).value;
							String action = inputVector.get(2).value;

							Statement.while_mql(session, condition, action, env, parent_pid, current_pid);
							
							return "";

						case "repeat":

							//Get parameters
							condition = inputVector.get(1).value;
							action = inputVector.get(2).value;

							Statement.repeat_mql(session, condition, action, env, parent_pid, current_pid);
							
							return "";

						case "exception":

							//Get parameters
							String id = inputVector.get(1).value;
							String message = inputVector.get(2).value;

							return Statement.exception_mql(session, id, message, env, parent_pid, current_pid);

						default:

							//Script execution
							inputVector.remove(inputVector.size()-1);

							return concatOrUnknow(inputVector);

						}

					}

				}

			case 4:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				case "metric system nb processor":

					return Statistic.systemNbProcessor()+"";

				case "metric file system roots":

					return Statistic.fileSystemRoots();

				case "metric total swap mem":

					return Statistic.getTotalSwapSpaceSize()+"";

				case "metric current used mem":

					return Statistic.getUsedPhysicalMemorySize()+"";

				case "metric current free mem":

					return Statistic.getFreePhysicalMemorySize()+"";

				case "metric current cpu jvm":

					return Statistic.currentJvmCpuValue();

				case "metric current mem jvm":

					return Statistic.currentMemJvm();

				case "metric current cpu system":

					return Statistic.currentSystemCpuValue()+"";

				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "dl bayesian exist":
						
						//Get parameters
						String bayesianId = inputVector.get(3).value;
						
						return BayesianNeuralNetworkManager.exist(bayesianId);
		
					case "dl bayesian delete":
						
						//Get parameters
						bayesianId = inputVector.get(3).value;
						
						BayesianNeuralNetworkManager.delete(bayesianId);
						
						return "1";
		
					case "dl n_bayesian exist":
						
						//Get parameters
						bayesianId = inputVector.get(3).value;
						
						return TextCatManager.exist(bayesianId);
		
					case "dl n_bayesian delete":
						
						//Get parameters
						bayesianId = inputVector.get(3).value;
						
						TextCatManager.delete(bayesianId);
						
						return "1";
		
					case "auto commit set":
						
						//Get parameters
						String bool = inputVector.get(3).value;
						
						if (bool!=null && bool.equals("1")) env.autoCommit = true;
						else env.autoCommit = false;
						
						return "1";
		
					case "dl csv predict":
						
						//Get parameters
						String jsonInputArray = inputVector.get(3).value;
						
						return CSVNeuralNetworkManager.predict(env, jsonInputArray);
		
					case "dl img load_network":
						
						//Get parameters
						String filePath = inputVector.get(3).value;
						
						ImageNeuralNetworkManager.load_network(env, filePath);
						
						return "1";
		
					case "dl csv execute_config":
						
						//Get parameters
						String jsonConfig = inputVector.get(3).value;
						
						CSVNeuralNetworkManager.run(jsonConfig);
						
						return "1";
		
					case "dl img execute_config":
						
						//Get parameters
						filePath = inputVector.get(3).value;
						
						return ImageNeuralNetworkManager.exe(env, filePath);
		
					case "ml h_node show_problem":
						
						//Get parameters
						String key = inputVector.get(3).value;
		
						return JsonManager.format_Gson(MLManager.showHeuristicProblem(env, key).toJSONString());
		
					case "ml cluster xy_scatter":
						
						//Get parameters
						String clusterId = inputVector.get(3).value;
		
						return "j23i88m90m76i39t04r09y35p14a96y09e57t43"+MLManager.xy_scatter(env, clusterId);
		
					case "pa rl intercept_std_err":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getInterceptStdErr(env, key);

					case "pa rl mean_square_error":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getMeanSquareError(env, key);

					case "pa rl count":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getN(env, key);

					case "pa rl r":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getR(env, key);

					case "pa rl sum_squares":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getRegressionSumSquares(env, key);

					case "pa rl r_square":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getRSquare(env, key);

					case "pa rl significance":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getSignificance(env, key);

					case "pa rl slope_confidence_interval":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getSlopeConfidenceInterval(env, key);

					case "pa rl slope_std_err":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getSlopeStdErr(env, key);

					case "pa rl sum_squared_errors":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getSumSquaredErrors(env, key);

					case "pa rl total_sum_squares":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getTotalSumSquares(env, key);

					case "pa rl x_sum_squares":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.getXSumSquares(env, key);

					case "pa rl close":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.close(env, key);

					case "ml cluster close":
						
						//Get params
						key = inputVector.get(3).value;
						
						return MLManager.close(env, key);

					case "ml h_node close":
						
						//Get params
						key = inputVector.get(3).value;
						
						return MLManager.closeHeuristicNode(env, key);

					case "ml cluster nb":
						
						//Get params
						key = inputVector.get(3).value;
						
						return MLManager.nbCluster(env, key);

					case "pa rm close":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.close(env, key);

					case "pa rl load_empty":
						
						//Get params
						key = inputVector.get(3).value;
						
						PALinearRegression.load_empty(env, key);
						
						return "1";

					case "pa rl exist":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.exist(env, key);

					case "ml cluster exist":
						
						//Get params
						key = inputVector.get(3).value;
						
						return MLManager.existClusters(env, key);

					case "ml h_node exist":
						
						//Get params
						key = inputVector.get(3).value;
						
						return MLManager.existHeuristicNode(env, key);

					case "pa rm exist":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.exist(env, key);

					case "pa rl slope":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.get_slope(env, key);

					case "pa rl intercept":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PALinearRegression.get_intercept(env, key);

					case "pa rm calculate_adjusted_r_squared":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.calculateAdjustedRSquared(env, key);

					case "pa rm calculate_residual_sum_of_squares":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.calculateResidualSumOfSquares(env, key);

					case "pa rm calculate_r_squared":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.calculateRSquared(env, key);

					case "pa rm calculate_total_sum_of_squares":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.calculateTotalSumOfSquares(env, key);

					case "pa rm estimate_error_variance":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.estimateErrorVariance(env, key);

					case "pa rm estimate_regressand_variance":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.estimateRegressandVariance(env, key);

					case "pa rm estimate_regression_standard_error":
						
						//Get params
						key = inputVector.get(3).value;
						
						return PAMultipleRegressionOLS.estimateRegressionStandardError(env, key);

					case "pa rm estimate_regression_parameters_variance":
						
						//Get params
						key = inputVector.get(3).value;
						
						return JsonManager.format_Gson(PAMultipleRegressionOLS.estimateRegressionParametersVariance(env, key).toJSONString());

					case "pa rm estimate_residuals":
						
						//Get params
						key = inputVector.get(3).value;
						
						return JsonManager.format_Gson(PAMultipleRegressionOLS.estimateResiduals(env, key).toJSONString());

					case "pa rm estimate_regression_parameters_standard_errors":
						
						//Get params
						key = inputVector.get(3).value;
						
						return JsonManager.format_Gson(PAMultipleRegressionOLS.estimateRegressionParametersStandardErrors(env, key).toJSONString());

					case "pa rm estimate_regression_parameters":
						
						//Get params
						key = inputVector.get(3).value;
						
						return JsonManager.format_Gson(PAMultipleRegressionOLS.estimateRegressionParameters(env, key).toJSONString());

					case "sql show tables":

						String cmId = inputVector.get(3).value;
						
						return DQManager.db_show_tables(session, cmId).toJSONString();

					case "excel sheet show":

						String excelId = inputVector.get(3).value;

						return JsonManager.format_Gson(ExcelManager.sheet_show(env, excelId).toJSONString());

					case "excelx sheet show":

						excelId = inputVector.get(3).value;

						return JsonManager.format_Gson(ExcelxManager.sheet_show(env, excelId).toJSONString());

					case "excel cell ref":

						String cell = inputVector.get(3).value;

						return JsonManager.format_Gson(ExcelManager.cell_ref(cell).toJSONString());

					case "excelx cell ref":

						cell = inputVector.get(3).value;

						return JsonManager.format_Gson(ExcelxManager.cell_ref(cell).toJSONString());

					case "excel load empty":

						excelId = inputVector.get(3).value;

						return ExcelManager.create(env, excelId);

					case "excelx load empty":

						excelId = inputVector.get(3).value;

						return ExcelxManager.create(env, excelId);

					case "ftps ls dirs":

						String sessionId = inputVector.get(3).value;

						return JsonManager.format_Gson(FtpsManager.ls_dirs(env, sessionId).toJSONString());

					case "ftp set timeout":

						//Get parameters
						String timeout = inputVector.get(3).value;

						return FtpManager.set_timeout(env, timeout);

					case "is not null":

						//Get parameters
						String str = inputVector.get(3).value;

						return OperatorFx.is_not_null(str);

					case "is not empty":

						//Get parameters
						str = inputVector.get(3).value;

						return OperatorFx.is_not_empty(str);

					case "env del var":

						String var = inputVector.get(3).value;

						env.remove(var);

						return "Variable deleted with successful.";

					case "env get var":

						var = inputVector.get(3).value;

						return env.get(var);

					case "env exist var":

						var = inputVector.get(3).value;

						if (env.exist(var)) return "1";
						else return "0";

					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "string encode_des":

							String data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.encode_des(data, key);

						case "string decode_des":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.decode_des(data, key);

						case "string encode_sign":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.encode_sign(data, key);

						case "string decode_rsa":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return MyRSA.decode(data, key);

						case "string encode_blowfish":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.encode_blowfish(data, key);

						case "string decode_blowfish":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.decode_blowfish(data, key);

						case "string encode_pbe":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.encode_pbe(data, key);

						case "string decode_pbe":

							data = inputVector.get(2).value;
							key = inputVector.get(3).value;

							return StringFx.decode_pbe(data, key);

						case "html load_from_str":

							String domId = inputVector.get(2).value;
							String html = inputVector.get(3).value;

							HTMLManager.load_from_string(env, domId, html);
							
							return "1";

						case "string split_mql":

							str = inputVector.get(2).value;
							String index = inputVector.get(3).value;

							return StringFx.split_mql(str, index);

						case "string split_sentence":

							str = inputVector.get(2).value;
							String chars = inputVector.get(3).value;

							return StringFx.split_sentence(str, chars);

						case "app exist":
							
							//Get parameters
							String type = inputVector.get(2).value;
							String context = inputVector.get(3).value;
			
							return AppManager.exist_context(type, context);
			
						case "date nb_day":
							
							String date1 = inputVector.get(2).value;
							String date2 = inputVector.get(3).value;

							return DateFx.nb_day(date1, date2);

						case "scrud export":

							cmId = inputVector.get(2).value;
							String tablename = inputVector.get(3).value;

							return ScrudManager.db_export(session, cmId, tablename);

						case "scrud create":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;
							
							return ScrudManager.db_create(session, cmId, tablename);

						case "scrud select":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_select(session, cmId, tablename);

						case "scrud parse":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_parse(session, cmId, tablename);

						case "scrud db_to_db":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_db_to_db(session, cmId, tablename);

						case "scrud insert":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_insert(session, cmId, tablename);

						case "scrud update":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_update(session, cmId, tablename);

						case "scrud merge":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_merge(session, cmId, tablename);

						case "scrud delete":

							cmId = inputVector.get(2).value;
							tablename = inputVector.get(3).value;

							return ScrudManager.db_delete(session, cmId, tablename);

						case "type is_enum":

							String value = inputVector.get(2).value;
							String values = inputVector.get(3).value;

							return TypeFx.is_enum(value, values);

						case "excel load":

							excelId = inputVector.get(2).value;
							String path = inputVector.get(3).value;

							return ExcelManager.load_from_file(env, excelId, path);

						case "excelx load":

							excelId = inputVector.get(2).value;
							path = inputVector.get(3).value;

							return ExcelxManager.load_from_file(env, excelId, path);

						case "excel save":

							excelId = inputVector.get(2).value;
							path = inputVector.get(3).value;

							return ExcelManager.save(env, excelId, path);

						case "excelx save":

							excelId = inputVector.get(2).value;
							path = inputVector.get(3).value;

							return ExcelxManager.save(env, excelId, path);

						case "os execute":

							String json = inputVector.get(2).value;
							String minWait = inputVector.get(3).value;

							return OsManager.execute(json, minWait);

						case "tunnel connect":

							//Get parameters
							sessionId = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return TunnelManager.connect(env, sessionId, json);

						case "tunnel execute":

							//Get parameters
							sessionId = inputVector.get(2).value;
							String mql = inputVector.get(3).value;

							return TunnelManager.execute(env, sessionId, mql);

						case "cifs mkdir":

							//Get parameters
							String smbUrl = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return CifsManager.mkdir(env, smbUrl, json);

						case "cifs rm":

							//Get parameters
							smbUrl = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return CifsManager.rm(env, smbUrl, json);

						case "cifs ls":

							//Get parameters
							smbUrl = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return JsonManager.format_Gson(CifsManager.ls(env, smbUrl, json));

						case "ftp connect":

							//Get key, name and value
							String sid = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return FtpManager.connect(env, sid, json);

						case "ftps connect":

							//Get key, name and value
							sid = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return FtpsManager.connect(env, sid, json);

						case "sftp connect":

							//Get key, name and value
							sid = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return SftpManager.connect(env, sid, json);

						case "ssh connect":

							//Get key, name and value
							sid = inputVector.get(2).value;
							json = inputVector.get(3).value;

							return SshManager.connect(env, sid, json);

						case "compress zip":

							String in = inputVector.get(2).value;
							String out = inputVector.get(3).value;

							CompressManager.zip(in, out);

							return "1";

						case "compress unzip":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.unzip(in, out);

							return "1";

						case "compress tar":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.tar(in, out);

							return "1";

						case "compress untar":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.untar(in, out);

							return "1";

						case "compress tarGz":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.tarGz(in, out);

							return "1";

						case "compress tarBz2":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.tarBz2(in, out);

							return "1";

						case "compress untarGz":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.untarGz(in, out);

							return "1";

						case "compress untarBz2":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.untarBz2(in, out);

							return "1";

						case "compress jar":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.zip(in, out);

							return "1";

						case "compress unjar":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.unzip(in, out);

							return "1";

						case "compress gz":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.gz(in, out);

							return "1";

						case "compress ungz":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.ungz(in, out);

							return "1";

						case "compress bz2":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.bz2(in, out);

							return "1";

						case "compress unbz2":

							in = inputVector.get(2).value;
							out = inputVector.get(3).value;

							CompressManager.unbz2(in, out);

							return "1";

						case "file writer_add_bytes":

							String writerId = inputVector.get(2).value;
							String strBytes = inputVector.get(3).value;

							return FileFx.writer_add_bytes(env, writerId, strBytes);

						case "file writer_add_line":

							writerId = inputVector.get(2).value;
							str = inputVector.get(3).value;

							return FileFx.writer_add_line(env, writerId, str);

						case "file reader_get_bytes":

							String readerId = inputVector.get(2).value;
							String nbBytes = inputVector.get(3).value;

							return FileFx.reader_get_bytes(env, readerId, nbBytes);

						case "file b64_write":

							data = inputVector.get(2).value;
							filePath = inputVector.get(3).value;

							return FileFx.b64_write(data, filePath);

						case "file copy_dir":

							String oldDirPath = inputVector.get(2).value;
							String newDirPath = inputVector.get(3).value;

							return FileFx.copy_dir(oldDirPath, newDirPath);

						case "file copy_file":

							String oldFilePath = inputVector.get(2).value;
							String newFilePath = inputVector.get(3).value;

							return FileFx.copy_file(oldFilePath, newFilePath);

						case "file load":

							filePath = inputVector.get(2).value;
							String encoding = inputVector.get(3).value;

							return FileFx.load(filePath, encoding);

						case "file meta_data":

							filePath = inputVector.get(2).value;
							String attribute = inputVector.get(3).value;

							return FileFx.meta_data(filePath, attribute);

						case "ssh execute_1_cmd":

							sessionId = inputVector.get(2).value;
							String shellCommand = inputVector.get(3).value;

							return SshManager.exec_one_cmd(env, sessionId, shellCommand);

						case "ssh execute_n_cmd":

							sessionId = inputVector.get(2).value;
							shellCommand = inputVector.get(3).value;

							return SshManager.exec(env, sessionId, shellCommand);

						case "sftp rm":

							sessionId = inputVector.get(2).value;
							String file = inputVector.get(3).value;

							return SftpManager.rm(env, sessionId, file);

						case "ftps rm":

							sessionId = inputVector.get(2).value;
							file = inputVector.get(3).value;

							return FtpsManager.rm(env, sessionId, file);

						case "ftps ls":

							sessionId = inputVector.get(2).value;
							String regex = inputVector.get(3).value;

							return JsonManager.format_Gson(FtpsManager.ls_files(env, sessionId, regex).toJSONString());

						case "ftps cd":

							sessionId = inputVector.get(2).value;
							String directory = inputVector.get(3).value;

							return FtpsManager.cd(env, sessionId, directory);

						case "ftp cd":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return FtpManager.cd(env, sessionId, directory);

						case "sftp cd":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return SftpManager.cd(env, sessionId, directory);

						case "sftp lcd":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return SftpManager.lcd(env, sessionId, directory);

						case "ftp mkdir":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return FtpManager.mkdir(env, sessionId, directory);

						case "sftp mkdir":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return SftpManager.mkdir(env, sessionId, directory);

						case "ftps mkdir":

							sessionId = inputVector.get(2).value;
							directory = inputVector.get(3).value;

							return FtpsManager.mkdir(env, sessionId, directory);

						case "ftp ls":

							sessionId = inputVector.get(2).value;
							String fileFilterPath = inputVector.get(3).value;

							return JsonManager.format_Gson(FtpManager.ls(env, sessionId, fileFilterPath).toJSONString());

						case "sftp ls":

							sessionId = inputVector.get(2).value;
							fileFilterPath = inputVector.get(3).value;

							return JsonManager.format_Gson(SftpManager.ls(env, sessionId, fileFilterPath).toJSONString());

						case "xml load":

							String xmlId = inputVector.get(2).value;
							data = inputVector.get(3).value;

							return XmlManager.load(env, xmlId, data);

						case "xml count":

							xmlId = inputVector.get(2).value;
							String xpath = inputVector.get(3).value;

							return XmlManager.count(env, xmlId, xpath);

						case "xml xpath":

							xmlId = inputVector.get(2).value;
							xpath = inputVector.get(3).value;

							return JsonManager.format_Gson(XmlManager.xpath(env, xmlId, xpath).toJSONString());

						case "xml dnode":

							xmlId = inputVector.get(2).value;
							xpath = inputVector.get(3).value;

							return XmlManager.delete_node(env, xmlId, xpath);

						case "xml fields":

							xmlId = inputVector.get(2).value;
							xpath = inputVector.get(3).value;

							return JsonManager.format_Gson(XmlManager.fields(env, xmlId, xpath).toJSONString());

						case "sql value":

							String sqlId = inputVector.get(2).value;
							String selectQuery = inputVector.get(3).value;

							return SQLManager.get_value(session.idConnection, env, sqlId, selectQuery);

						case "sql row":

							sqlId = inputVector.get(2).value;
							selectQuery = inputVector.get(3).value;

							return JsonManager.format_Gson(SQLManager.get_row(session.idConnection, env, sqlId, selectQuery).toJSONString());

						case "sql col_distinct":

							sqlId = inputVector.get(2).value;
							selectQuery = inputVector.get(3).value;

							return JsonManager.format_Gson(SQLManager.get_col_distinct(session.idConnection, env, sqlId, selectQuery).toJSONString());

						case "sql dml":

							sqlId = inputVector.get(2).value;
							String dmlQuery = inputVector.get(3).value;

							return SQLManager.dml(session.idConnection, env, sqlId, dmlQuery);

						case "sql auto_commit":

							sqlId = inputVector.get(2).value;
							bool = inputVector.get(3).value;

							SQLManager.set_auto_commit(env, sqlId, bool);

							return "1";

						case "sql connect":

							//Get id and the config object
							sqlId = inputVector.get(2).value;
							String config = inputVector.get(3).value;

							return SQLManager.connect(env, sqlId, config);

						case "json count":

							//Get key, name and value
							key = inputVector.get(2).value;
							String xPath = inputVector.get(3).value;

							return JsonManager.count(env, key, xPath);

						case "json select":

							//Get key, name and value
							key = inputVector.get(2).value;
							xPath = inputVector.get(3).value;

							return JsonManager.select(env, key, xPath);

						case "json load": 

							//Get parameters
							String docId = inputVector.get(2).value;
							String jsonString = inputVector.get(3).value;

							return JsonManager.load(env, docId, jsonString);

						case "file create":

							filePath = inputVector.get(2).value;
							data = inputVector.get(3).value;

							return FileFx.create(filePath, data);

						case "file append":

							filePath = inputVector.get(2).value;
							data = inputVector.get(3).value;

							return FileFx.append(filePath, data);

						case "string count":

							String string = inputVector.get(2).value;
							String find = inputVector.get(3).value;

							return StringFx.count( string, find);

						case "string levenshtein_distance":

							String word1 = inputVector.get(2).value;
							String word2 = inputVector.get(3).value;

							return StringFx.levenshtein_distance(word1, word2)+"";

						case "string del_char_before_each_line":

							data = inputVector.get(2).value;
							String nbChar = inputVector.get(3).value;

							return StringFx.del_char_before_each_line( data, nbChar);

						case "string ends_with":

							String stringValue = inputVector.get(2).value;
							String stringToEnd = inputVector.get(3).value;

							return StringFx.ends_with( stringValue, stringToEnd);

						case "string indent":

							str = inputVector.get(2).value;
							String nbSpaceBefore = inputVector.get(3).value;

							return StringFx.indent( str, nbSpaceBefore);

						case "string instr":

							String str1 = inputVector.get(2).value;
							String str2 = inputVector.get(3).value;

							return StringFx.instr( str1, str2);

						case "string left":

							str = inputVector.get(2).value;
							String len = inputVector.get(3).value;

							return StringFx.left( str, len);

						case "string like":

							str = inputVector.get(2).value;
							String pat = inputVector.get(3).value;

							return StringFx.like( str, pat);

						case "string locate":

							str1 = inputVector.get(2).value;
							str2 = inputVector.get(3).value;

							return StringFx.locate( str1, str2);

						case "string matches": case "type is_matches_regex":

							str = inputVector.get(2).value;
							pat = inputVector.get(3).value;

							return StringFx.matches( str, pat);

						case "string mid":

							str = inputVector.get(2).value;
							index = inputVector.get(3).value;

							return StringFx.mid( str, index);

						case "string not_like":

							str = inputVector.get(2).value;
							pat = inputVector.get(3).value;

							return StringFx.not_like( str, pat);

						case "string not_regexp":

							str = inputVector.get(2).value;
							pat = inputVector.get(3).value;

							return StringFx.not_regexp( str, pat);

						case "string position":

							str1 = inputVector.get(2).value;
							str2 = inputVector.get(3).value;

							return StringFx.position( str1, str2);

						case "string regexp":

							str = inputVector.get(2).value;
							pat = inputVector.get(3).value;

							return StringFx.regexp( str, pat);

						case "string repeat":

							str = inputVector.get(2).value;
							String count = inputVector.get(3).value;

							return StringFx.repeat( str, count);

						case "string right":

							str = inputVector.get(2).value;
							len = inputVector.get(3).value;

							return StringFx.right( str, len);

						case "string starts_with":

							stringValue = inputVector.get(2).value;
							String stringToStart = inputVector.get(3).value;

							return StringFx.starts_with( stringValue, stringToStart);

						case "string starts_with_or":

							stringValue = inputVector.get(2).value;
							stringToStart = inputVector.get(3).value;

							return StringFx.starts_with_or( stringValue, stringToStart);

						case "string strcmp":

							str1 = inputVector.get(2).value;
							str2 = inputVector.get(3).value;

							return StringFx.strcmp( str1, str2);

						case "string strpos":

							str1 = inputVector.get(2).value;
							str2 = inputVector.get(3).value;

							return StringFx.strpos( str1, str2);

						case "string sublrchar":

							str = inputVector.get(2).value;
							String numberDeleteChar = inputVector.get(3).value;

							return StringFx.sublrchar( str, numberDeleteChar);

						case "string substr":

							str = inputVector.get(2).value;
							index = inputVector.get(3).value;

							return StringFx.substr( str, index);

						case "string substring":

							str = inputVector.get(2).value;
							index = inputVector.get(3).value;

							return StringFx.substring( str, index);

						case "type is_char":

							value = inputVector.get(2).value;
							String size = inputVector.get(3).value;

							return TypeFx.is_char( value, size);

						case "type is_integer":

							value = inputVector.get(2).value;
							size = inputVector.get(3).value;

							return TypeFx.is_integer( value, size);

						case "type is_valid_date":

							String dateToValidate = inputVector.get(2).value;
							String format = inputVector.get(3).value;

							return TypeFx.is_valid_date( dateToValidate, format);

						case "type is_valid_timestamp":

							String timestampToValidate = inputVector.get(2).value;
							format = inputVector.get(3).value;

							return TypeFx.is_valid_timestamp( timestampToValidate, format);

						case "type is_varchar":

							value = inputVector.get(2).value;
							size = inputVector.get(3).value;

							return TypeFx.is_varchar( value, size);

						case "math atan2":

							String number1 = inputVector.get(2).value;
							String number2 = inputVector.get(3).value;

							return MathFx.atan2( number1, number2);

						case "math bit_and":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.bit_and( number1, number2);

						case "math bit_or":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.bit_or( number1, number2);

						case "math bit_xor":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.bit_xor( number1, number2);

						case "math hypot":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.hypot( number1, number2);

						case "math max":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.max( number1, number2);

						case "math min":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.min( number1, number2);

						case "math mod":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.mod( number1, number2);

						case "math pow":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.pow( number1, number2);

						case "math power":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.power( number1, number2);

						case "math round":

							number1 = inputVector.get(2).value;
							number2 = inputVector.get(3).value;

							return MathFx.round( number1, number2);

						case "date is_valid_date":

							dateToValidate = inputVector.get(2).value;
							format = inputVector.get(3).value;

							return DateFx.is_valid_date( dateToValidate, format);

						case "date is_valid_timestamp":

							timestampToValidate = inputVector.get(2).value;
							format = inputVector.get(3).value;

							return DateFx.is_valid_timestamp( timestampToValidate, format);

						case "atom distinct":

							String atomList = inputVector.get(2).value;
							String separator = inputVector.get(3).value;

							return AtomFx.distinct( atomList, separator);

						case "atom distinct_lrtrim":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.distinct_lrtrim( atomList, separator);

						case "atom distinct_lrtrim_1sbefore":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.distinct_lrtrim_1sbefore( atomList, separator);

						case "atom get_first":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.get_first( atomList, separator);

						case "atom get_first_lrtrim":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.get_first_lrtrim( atomList, separator);

						case "atom get_last":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.get_last( atomList, separator);

						case "atom get_last_lrtrim":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.get_last_lrtrim( atomList, separator);

						case "atom lrtrim":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.lrtrim( atomList, separator);

						case "atom size":

							atomList = inputVector.get(2).value;
							separator = inputVector.get(3).value;

							return AtomFx.size( atomList, separator);

						case "not equal":

							//Get parameters
							str1 = inputVector.get(2).value;
							str2 = inputVector.get(3).value;

							return OperatorFx.not_equal(str1, str2);

						case "json fields":

							//Get key, name and value
							key = inputVector.get(2).value;
							String jPath = inputVector.get(3).value;

							return JsonFormatter.format(JsonManager.fields(env, key, jPath));

						default:

							switch (inputVector.get(0).value) {
							case "try":

								//Get parameters
								String mainCommand = inputVector.get(1).value;
								String commandIfError = inputVector.get(2).value;
								String exceptionId = inputVector.get(3).value;

								return Statement.try_mql(session, mainCommand, commandIfError, exceptionId, env, parent_pid, current_pid);

							case "if":

								//Get parameters
								String condition = inputVector.get(1).value;
								String trueAction = inputVector.get(2).value;
								String falseAction = inputVector.get(3).value;

								return Statement.if_mql(session, condition, trueAction, falseAction, env, parent_pid, current_pid);

							default:

								//Script execution
								inputVector.remove(inputVector.size()-1);

								return concatOrUnknow(inputVector);

							}

						}

					}

				}

			case 5:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
				case "metric current used swap mem":

					return Statistic.getUsedSwapSpaceSize()+"";

				case "metric current free swap mem":

					return Statistic.getFreeSwapSpaceSize()+"";

				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
					case "is null or empty":

						//Get parameters
						String str = inputVector.get(4).value;

						return OperatorFx.is_null_or_empty(str);

					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
						case "bot aiml remove":
							
							//Get parameters
							String botName = inputVector.get(3).value;
							String filename = inputVector.get(4).value;
							
							BotManager.remove_aiml_file(env, botName, filename);
							
							return "1";
			
						case "dl n_bayesian load":
							
							//Get parameters
							String bayesianId = inputVector.get(3).value;
							String model_file_path = inputVector.get(4).value;
							
							TextCatManager.load(bayesianId, model_file_path);
							
							return "1";
			
						case "dl n_bayesian predict":
							
							//Get parameters
							bayesianId = inputVector.get(3).value;
							String sentence = inputVector.get(4).value;
							
							return JsonManager.format_Gson(TextCatManager.predict(bayesianId, sentence).toJSONString());
			
						case "dl bayesian predict":
							
							//Get parameters
							bayesianId = inputVector.get(3).value;
							sentence = inputVector.get(4).value;
							
							return JsonManager.format_Gson(BayesianNeuralNetworkManager.predict(bayesianId, sentence).toJSONString());
			
						case "dl bayesian init":
							
							//Get parameters
							bayesianId = inputVector.get(3).value;
							String laplace_int = inputVector.get(4).value;
							
							BayesianNeuralNetworkManager.init(bayesianId, laplace_int);
							
							return "1";
							
						case "dl bayesian create":
							
							//Get parameters
							bayesianId = inputVector.get(3).value;
							String cats = inputVector.get(4).value;
							
							BayesianNeuralNetworkManager.create(bayesianId, cats);
							
							return "1";
			
						case "dl csv load_network":
							
							//Get parameters
							String modelPath = inputVector.get(3).value;
							String helperPath = inputVector.get(4).value;
							
							CSVNeuralNetworkManager.load_model(env, modelPath, helperPath);
							
							return "1";
			
						case "ml h_node close_problem":
							
							//Get params
							String key = inputVector.get(3).value;
							String problemIndex = inputVector.get(4).value;
							
							return MLManager.closeHeuristicProblem(env, key, problemIndex);

						case "ml h_node exist_problem":
							
							//Get params
							key = inputVector.get(3).value;
							problemIndex = inputVector.get(4).value;
							
							return MLManager.existProblem(env, key, problemIndex);

						case "ml cluster nb_point":
							
							//Get parameters
							key = inputVector.get(3).value;
							String clusterIndex = inputVector.get(4).value;
							
							return MLManager.nbPoint(env, key, clusterIndex);

						case "ml cluster points":
							
							//Get params
							key = inputVector.get(3).value;
							clusterIndex = inputVector.get(4).value;
							
							return JsonManager.format_Gson(MLManager.points(env, key, clusterIndex).toJSONString());

						case "pa rl load_from_json":
							
							//Get params
							key = inputVector.get(3).value;
							String json = inputVector.get(4).value;
							
							PALinearRegression.loadFromArray(env, key, json);
							
							return "1";

						case "pa rm set_no_intercept":
							
							//Get params
							key = inputVector.get(3).value;
							String bool = inputVector.get(4).value;
							
							PAMultipleRegressionOLS.setNoIntercept(env, key, bool);
							
							return "1";

						case "pa rl slope_confidence_interval":
							
							//Get params
							key = inputVector.get(3).value;
							String alpha = inputVector.get(4).value;
							
							return PALinearRegression.getSlopeConfidenceInterval(env, key, alpha);
							
						case "pa rm predict":
							
							//Get params
							String regId = inputVector.get(3).value;
							String jsonX = inputVector.get(4).value;
							
							return PAMultipleRegressionOLS.predict(env, regId, jsonX);

						case "pa rl predict":
							
							//Get params
							regId = inputVector.get(3).value;
							String x = inputVector.get(4).value;
							
							return PALinearRegression.get_prediction(env, regId, x);

						case "sql show desc":
							
							String cmId = inputVector.get(3).value;
							String tablename = inputVector.get(4).value;

							return DQManager.db_show_desc(session, cmId, tablename).toJSONString();

						case "pdf from html":

							String html = inputVector.get(3).value;
							String filePath = inputVector.get(4).value;
							
							PDFManager.pdfFromHtml(html, filePath);

							return "1";

						case "excel sheet add":

							String excelId = inputVector.get(3).value;
							String sheetName = inputVector.get(4).value;

							return ExcelManager.sheet_add(env, excelId, sheetName);

						case "excel sheet delete":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;

							return ExcelManager.sheet_delete(env, excelId, sheetName);

						case "excel sheet max_row":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;

							return ExcelManager.sheet_max_row(env, excelId, sheetName);

						case "excelx sheet add":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;

							return ExcelxManager.sheet_add(env, excelId, sheetName);

						case "excelx sheet delete":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;

							return ExcelxManager.sheet_delete(env, excelId, sheetName);

						case "excelx sheet max_row":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;

							return ExcelxManager.sheet_max_row(env, excelId, sheetName);

						case "file count_line dir":

							String dirPath = inputVector.get(3).value;
							String endOfFile = inputVector.get(4).value;

							return FileFx.count_dir(dirPath, endOfFile);

						case "sftp rm dir":

							String sessionId = inputVector.get(3).value;
							String directory = inputVector.get(4).value;

							return SftpManager.rmdir(env, sessionId, directory);

						case "ftps rm dir":

							sessionId = inputVector.get(3).value;
							directory = inputVector.get(4).value;

							return FtpsManager.rmdir(env, sessionId, directory);

						case "ftps set type":

							sessionId = inputVector.get(3).value;
							String transferType = inputVector.get(4).value;

							return FtpsManager.set_file_type(env, sessionId, transferType);

						case "ftps exec prot":

							sessionId = inputVector.get(3).value;
							String prot = inputVector.get(4).value;

							return FtpsManager.exec_prot(env, sessionId, prot);

						case "ftps parse pbsz":

							sessionId = inputVector.get(3).value;
							String pbsz = inputVector.get(4).value;

							return FtpsManager.parse_pbsz(env, sessionId, pbsz);

						case "ftp rm file":

							sessionId = inputVector.get(3).value;
							String remoteFile = inputVector.get(4).value;

							return FtpManager.rm(env, sessionId, remoteFile);

						case "ftp rm dir":

							sessionId = inputVector.get(3).value;
							String remoteDir = inputVector.get(4).value;

							return FtpManager.rmdir(env, sessionId, remoteDir);

						case "ftp active compression":

							sessionId = inputVector.get(3).value;
							bool = inputVector.get(4).value;

							return FtpManager.set_compression(env, sessionId, bool);

						case "ftp set type":

							sessionId = inputVector.get(3).value;
							String type = inputVector.get(4).value;

							return FtpManager.set_type(env, sessionId, type);

						case "xml select node":

							String xmlId = inputVector.get(3).value;
							String xpath = inputVector.get(4).value;

							return XmlManager.select_node(env, xmlId, xpath);

						case "xml select text":

							xmlId = inputVector.get(3).value;
							xpath = inputVector.get(4).value;

							return XmlManager.select_text(env, xmlId, xpath);

						case "xml select attribute":

							xmlId = inputVector.get(3).value;
							xpath = inputVector.get(4).value;

							return XmlManager.select_attribute(env, xmlId, xpath);

						case "env set var":

							String var = inputVector.get(3).value;
							String val = inputVector.get(4).value;

							//Generate an error if the variable name is not valid
							if (!EnvManager.is_valid_varname(var)) {
								throw new Exception("Sorry, the variable name "+var+" is not valid (example: [var1]).");
							}

							env.set(var, val);

							return val;

						case "env incr var":

							var = inputVector.get(3).value;
							val = inputVector.get(4).value;

							return env.incr(var, val);

						case "env decr var":

							var = inputVector.get(3).value;
							val = inputVector.get(4).value;

							return env.decr(var, val);

						case "json is object":

							//Get key, name and value
							key = inputVector.get(3).value;
							String xPath = inputVector.get(4).value;

							return JsonManager.is_obj(env, key, xPath);

						case "json is array":

							//Get key, name and value
							key = inputVector.get(3).value;
							xPath = inputVector.get(4).value;

							return JsonManager.is_array(env, key, xPath);

						default:

							switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
							case "string decode_sign_verify":

								String data = inputVector.get(2).value;
								String signature = inputVector.get(3).value;
								String publicKey = inputVector.get(4).value;

								return StringFx.decode_sign_verify(data, signature, publicKey);

							case "string encode_rsa":

								data = inputVector.get(2).value;
								publicKey = inputVector.get(3).value;
								String privateKey = inputVector.get(4).value;

								return MyRSA.encode(data, publicKey, privateKey);

							case "bot create":
								
								//Get parameters
								botName = inputVector.get(2).value;
								String is_male = inputVector.get(3).value;
								String lang = inputVector.get(4).value;
								
								BotManager.create_bot(botName, is_male, lang);
				
								return "1";
				
							case "bot execute":

								//Get parameters
								botName = inputVector.get(2).value;
								String user = inputVector.get(3).value;
								sentence = inputVector.get(4).value;

								return JsonManager.format_Gson(BotManager.execute(botName, user, sentence).toJSONString());

							case "msword replace":

								//Get parameters
								String source = inputVector.get(2).value;
								String destination = inputVector.get(3).value;
								json = inputVector.get(4).value;

								re.jpayet.mentdb.ext.word.WordManager.replace_tags(source, destination, json);

								return "1";

							case "mswordx replace":

								//Get parameters
								source = inputVector.get(2).value;
								destination = inputVector.get(3).value;
								json = inputVector.get(4).value;

								re.jpayet.mentdb.ext.word.WordxManager.replace_tags(source, destination, json);

								return "1";

							case "html element":

								//Get parameters
								String domId = inputVector.get(2).value;
								String jsonDocId = inputVector.get(3).value;
								String id = inputVector.get(4).value;
								
								HTMLManager.getElementById(env, domId, jsonDocId, id);

								return "1";

							case "tunnel execute_hot":

								//Get parameters
								sessionId = inputVector.get(2).value;
								json = inputVector.get(3).value;
								String mql = inputVector.get(4).value;

								return TunnelManager.execute_hot(env, sessionId, json, mql);

							case "sql select":

								cmId = inputVector.get(2).value;
								String query = inputVector.get(3).value;
								String title = inputVector.get(4).value;
								
								return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.db_show_data(session, cmId, query, title).toJSONString();

							case "rest https_json_post":

								//Get parameters
								String url = inputVector.get(2).value;
								String jsonHeaders = inputVector.get(3).value;
								String jsonData = inputVector.get(4).value;
								
								String s = REST.https_json_post_exe(url, jsonHeaders, jsonData);

								try {
									return JsonManager.format_Gson(s);
								} catch (Exception e) {
									return s;
								}

							case "rest http_json_post":

								//Get parameters
								url = inputVector.get(2).value;
								jsonHeaders = inputVector.get(3).value;
								jsonData = inputVector.get(4).value;
								
								s = REST.http_json_post_exe(url, jsonHeaders, jsonData);

								try {
									return JsonManager.format_Gson(s);
								} catch (Exception e) {
									return s;
								}

							case "cifs put":

								//Get parameters
								String localFile = inputVector.get(2).value;
								remoteFile = inputVector.get(3).value;
								json = inputVector.get(4).value;

								return CifsManager.put(env, localFile, remoteFile, json);

							case "cifs get":

								//Get parameters
								remoteFile = inputVector.get(2).value;
								localFile = inputVector.get(3).value;
								json = inputVector.get(4).value;

								return CifsManager.get(env, remoteFile, localFile, json);

							case "cifs rename":

								//Get parameters
								remoteFile = inputVector.get(2).value;
								String newName = inputVector.get(3).value;
								json = inputVector.get(4).value;

								return CifsManager.rename(env, remoteFile, newName, json);

							case "file ini":

								String path = inputVector.get(2).value;
								String section = inputVector.get(3).value;
								String field = inputVector.get(4).value;

								return FileFx.ini(path, section, field);

							case "file ini_str":

								str = inputVector.get(2).value;
								section = inputVector.get(3).value;
								field = inputVector.get(4).value;

								return FileFx.ini_str(str, section, field);

							case "file create":

								filePath = inputVector.get(2).value;
								data = inputVector.get(3).value;
								String encoding = inputVector.get(4).value;

								return FileFx.create(filePath, data, encoding);

							case "file append":

								filePath = inputVector.get(2).value;
								data = inputVector.get(3).value;
								encoding = inputVector.get(4).value;

								return FileFx.append(filePath, data, encoding);

							case "ftps get":

								//Get parameters
								sessionId = inputVector.get(2).value;
								remoteFile = inputVector.get(3).value;
								localFile = inputVector.get(4).value;

								return FtpsManager.get(env, sessionId, remoteFile, localFile);

							case "sftp get":

								//Get parameters
								sessionId = inputVector.get(2).value;
								remoteFile = inputVector.get(3).value;
								localFile = inputVector.get(4).value;

								return SftpManager.get(env, sessionId, remoteFile, localFile);

							case "ftps rename":

								//Get parameters
								sessionId = inputVector.get(2).value;
								String oldFile = inputVector.get(3).value;
								String newFile = inputVector.get(4).value;

								return FtpsManager.rename(env, sessionId, oldFile, newFile);

							case "ftp rename":

								//Get parameters
								sessionId = inputVector.get(2).value;
								oldFile = inputVector.get(3).value;
								newFile = inputVector.get(4).value;

								return FtpManager.rename(env, sessionId, oldFile, newFile);

							case "sftp rename":

								//Get parameters
								sessionId = inputVector.get(2).value;
								oldFile = inputVector.get(3).value;
								newFile = inputVector.get(4).value;

								return SftpManager.rename(env, sessionId, oldFile, newFile);

							case "ftp put":

								//Get parameters
								sessionId = inputVector.get(2).value;
								localFile = inputVector.get(3).value;
								String mode = inputVector.get(4).value;

								return FtpManager.put(env, sessionId, localFile, mode);

							case "ftp get":

								//Get parameters
								sessionId = inputVector.get(2).value;
								remoteFile = inputVector.get(3).value;
								localFile = inputVector.get(4).value;

								return FtpManager.get(env, sessionId, remoteFile, localFile);

							case "xml dattribute":

								//Get parameters
								xmlId = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String attributeName = inputVector.get(4).value;

								return XmlManager.delete_attribute(env, xmlId, xPath, attributeName);

							case "xml inode":

								//Get parameters
								xmlId = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String node = inputVector.get(4).value;

								return XmlManager.insert_nodes(env, xmlId, xPath, node);

							case "xml utext":

								//Get parameters
								xmlId = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String text = inputVector.get(4).value;

								return XmlManager.update_text(env, xmlId, xPath, text);

							case "json darray":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String ii = inputVector.get(4).value;

								return JsonManager.adelete(env, key, xPath, ii);

							case "json dobject":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String fieldName = inputVector.get(4).value;

								return JsonManager.odelete(env, key, xPath, fieldName);

							case "string split":

								str = inputVector.get(2).value;
								String regex = inputVector.get(3).value;
								String limit = inputVector.get(4).value;

								return StringFx.split(str, regex, limit).toJSONString();

							case "string csv_value":

								str = inputVector.get(2).value;
								String columnSeparator = inputVector.get(3).value;
								String quoteChar = inputVector.get(4).value;

								return StringFx.csv_value(str, columnSeparator, quoteChar);

							case "string instr":

								String str1 = inputVector.get(2).value;
								String str2 = inputVector.get(3).value;
								String fromIndex = inputVector.get(4).value;

								return StringFx.instr( str1, str2, fromIndex);

							case "string locate":

								str1 = inputVector.get(2).value;
								str2 = inputVector.get(3).value;
								fromIndex = inputVector.get(4).value;

								return StringFx.locate( str1, str2, fromIndex);

							case "string lpad":

								str = inputVector.get(2).value;
								String padString = inputVector.get(3).value;
								String paddedLength = inputVector.get(4).value;

								return StringFx.lpad( str, padString, paddedLength);

							case "string mid":

								str = inputVector.get(2).value;
								String beginIndex = inputVector.get(3).value;
								String endIndex = inputVector.get(4).value;

								return StringFx.mid( str, beginIndex, endIndex);

							case "string position":

								str1 = inputVector.get(2).value;
								str2 = inputVector.get(3).value;
								fromIndex = inputVector.get(4).value;

								return StringFx.position( str1, str2, fromIndex);

							case "string repeat_insert_str":

								str = inputVector.get(2).value;
								String strToInsert = inputVector.get(3).value;
								String incr = inputVector.get(4).value;

								return StringFx.repeat_insert_str( str, strToInsert, incr);

							case "string replace":

								str = inputVector.get(2).value;
								String target = inputVector.get(3).value;
								String replacement = inputVector.get(4).value;

								return StringFx.replace( str, target, replacement);

							case "string rpad":

								str = inputVector.get(2).value;
								padString = inputVector.get(3).value;
								paddedLength = inputVector.get(4).value;

								return StringFx.rpad( str, padString, paddedLength);

							case "string strpos":

								str1 = inputVector.get(2).value;
								str2 = inputVector.get(3).value;
								fromIndex = inputVector.get(4).value;

								return StringFx.strpos( str1, str2, fromIndex);

							case "string substr":

								str = inputVector.get(2).value;
								beginIndex = inputVector.get(3).value;
								endIndex = inputVector.get(4).value;

								return StringFx.substr( str, beginIndex, endIndex);

							case "string substring":

								str = inputVector.get(2).value;
								beginIndex = inputVector.get(3).value;
								endIndex = inputVector.get(4).value;

								return StringFx.substring( str, beginIndex, endIndex);

							case "string encode":

								str = inputVector.get(2).value;
								String sourceEnc = inputVector.get(3).value;
								String destinationEnc = inputVector.get(4).value;

								return StringFx.encode( str, sourceEnc, destinationEnc);

							case "type is_bool":

								String value = inputVector.get(2).value;
								String bool1 = inputVector.get(3).value;
								String bool2 = inputVector.get(4).value;

								return TypeFx.is_bool(session, value, bool1, bool2, env);

							case "type is_decimal":

								String stringDecimal = inputVector.get(2).value;
								String digitBeforeTheDecimalPoint = inputVector.get(3).value;
								String digitAfterTheDecimalPoint = inputVector.get(4).value;

								return TypeFx.is_decimal( stringDecimal, digitBeforeTheDecimalPoint, digitAfterTheDecimalPoint);

							case "date add":

								String date = inputVector.get(2).value;
								field = inputVector.get(3).value;
								String number = inputVector.get(4).value;

								return DateFx.add( date, field, number);

							case "date addt":

								String timestamp = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.addt( timestamp, field, number);

							case "date dateadd":

								date = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.dateadd( date, field, number);

							case "date dateaddt":

								timestamp = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.dateaddt( timestamp, field, number);

							case "date datediff":

								date = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.datediff( date, field, number);

							case "date datedifft":

								timestamp = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.datedifft( timestamp, field, number);

							case "date diff":

								date = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.diff( date, field, number);

							case "date difft":

								timestamp = inputVector.get(2).value;
								field = inputVector.get(3).value;
								number = inputVector.get(4).value;

								return DateFx.difft( timestamp, field, number);

							case "date format":

								String timestampToFormat = inputVector.get(2).value;
								String formatIn = inputVector.get(3).value;
								String formatOut = inputVector.get(4).value;

								return DateFx.format( timestampToFormat, formatIn, formatOut);

							case "atom before_exclud":

								String atomList = inputVector.get(2).value;
								String index_str = inputVector.get(3).value;
								String separator = inputVector.get(4).value;

								return AtomFx.before_exclud( atomList, index_str, separator);

							case "atom before_includ":

								atomList = inputVector.get(2).value;
								index_str = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.before_includ( atomList, index_str, separator);

							case "atom count":

								atomList = inputVector.get(2).value;
								String atomToCount = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.count( atomList, atomToCount, separator);

							case "atom count_distinct":

								atomList = inputVector.get(2).value;
								atomToCount = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.count_distinct( atomList, atomToCount, separator);

							case "atom count_lrtrim":

								atomList = inputVector.get(2).value;
								atomToCount = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.count_lrtrim( atomList, atomToCount, separator);

							case "atom count_lrtrim_distinct":

								atomList = inputVector.get(2).value;
								atomToCount = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.count_lrtrim_distinct( atomList, atomToCount, separator);

							case "atom find":

								atomList = inputVector.get(2).value;
								String atomToFind = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.find( atomList, atomToFind, separator);

							case "atom find_lrtrim":

								atomList = inputVector.get(2).value;
								atomToFind = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.find_lrtrim( atomList, atomToFind, separator);

							case "atom get":

								atomList = inputVector.get(2).value;
								index_str = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.get( atomList, index_str, separator);

							case "atom get_lrtrim":

								atomList = inputVector.get(2).value;
								index_str = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.get_lrtrim( atomList, index_str, separator);

							case "atom position":

								atomList = inputVector.get(2).value;
								atomToFind = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.position( atomList, atomToFind, separator);

							case "atom position_lrtrim":

								atomList = inputVector.get(2).value;
								atomToFind = inputVector.get(3).value;
								separator = inputVector.get(4).value;

								return AtomFx.position_lrtrim( atomList, atomToFind, separator);

							case "if force":

								//Get parameters
								String condition = inputVector.get(2).value;
								String trueAction = inputVector.get(3).value;
								String falseAction = inputVector.get(4).value;

								return Statement.if_force_mql(session, condition, trueAction, falseAction, env, parent_pid, current_pid);

							default: 

								switch (inputVector.get(0).value) {

								case "for":

									//Get parameters
									String init = inputVector.get(1).value;
									condition = inputVector.get(2).value;
									String increment = inputVector.get(3).value;
									String action = inputVector.get(4).value;

									Statement.for_mql(session, init, condition, increment, action, env, parent_pid, current_pid);
									
									return "";

								default:

									//Script execution
									inputVector.remove(inputVector.size()-1);

									return concatOrUnknow(inputVector);

								}

							}

						}

					}

				}

			case 6:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
				default :

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
						case "bot aiml set":
							
							//Get parameters
							String botName = inputVector.get(3).value;
							String filename = inputVector.get(4).value;
							String xml = inputVector.get(5).value;
							
							return BotManager.set_aiml_file(env, botName, filename, xml);
			
						case "dl bayesian add_sentence":
							
							//Get parameters
							String bayesianId = inputVector.get(3).value;
							String cat = inputVector.get(4).value;
							String sentence = inputVector.get(5).value;
							
							BayesianNeuralNetworkManager.add_sentence(bayesianId, cat, sentence);
							
							return "1";
			
						case "ml h_node add_problem":
							
							//Get params
							String key = inputVector.get(3).value;
							String searchId = inputVector.get(4).value;
							String from = inputVector.get(5).value;
							
							MLManager.heuristicNode_add_problem(env, key, searchId, from);
							
							return "1";

						case "ml h_node load_from_json":
							
							//Get params
							key = inputVector.get(3).value;
							String isDirect = inputVector.get(4).value;
							String json = inputVector.get(5).value;
							
							MLManager.heuristicNode_load(env, key, isDirect, json);
							
							return "1";

						case "ml cluster point_get":
							
							//Get params
							key = inputVector.get(3).value;
							String clusterIndex = inputVector.get(4).value;
							String pointIndex = inputVector.get(5).value;
							
							return JsonManager.format_Gson(MLManager.getPoint(env, key, clusterIndex, pointIndex).toJSONString());

						case "ml cluster point_delete":
							
							//Get params
							key = inputVector.get(3).value;
							clusterIndex = inputVector.get(4).value;
							pointIndex = inputVector.get(5).value;
							
							MLManager.deletePoint(env, key, clusterIndex, pointIndex);
							
							return "1";

						case "pa rm load_from_json":
							
							//Get params
							key = inputVector.get(3).value;
							String jsonx = inputVector.get(4).value;
							String jsony = inputVector.get(5).value;
							
							PAMultipleRegressionOLS.loadFromJson(env, key, jsonx, jsony);
							
							return "1";

						case "pa rl add_data": 
							
							//Get parameters
							String regId = inputVector.get(3).value;
							String x = inputVector.get(4).value;
							String y = inputVector.get(5).value;
							
							PALinearRegression.add_data(env, regId, x, y);
							
							return "1";
							
						case "sql to html":

							String sqlId = inputVector.get(3).value;
							String tableName = inputVector.get(4).value;
							String selectQuery = inputVector.get(5).value;

							return SQLManager.to_html(session.idConnection, env, sqlId, tableName, selectQuery);

						case "sql show activity":

							String grouptype = inputVector.get(3).value;
							String dtMin = inputVector.get(4).value;
							String dtMax = inputVector.get(5).value;

							return Log.activity(dtMin, dtMax, grouptype);

						case "sql show data":

							String cmId = inputVector.get(3).value;
							String query = inputVector.get(4).value;
							String title = inputVector.get(5).value;

							return DQManager.db_show_data(session, cmId, query, title).toJSONString();

						case "ssh scp from":

							String sessionId = inputVector.get(3).value;
							String remoteFile = inputVector.get(4).value;
							String localFile = inputVector.get(5).value;

							return SshManager.scp_from(env, sessionId, remoteFile, localFile);

						case "ssh scp to":

							sessionId = inputVector.get(3).value;
							localFile = inputVector.get(4).value;
							remoteFile = inputVector.get(5).value;

							return SshManager.scp_to(env, sessionId, localFile, remoteFile);

						case "sql to json":

							sqlId = inputVector.get(3).value;
							tableName = inputVector.get(4).value;
							selectQuery = inputVector.get(5).value;

							return JsonManager.format_Gson(SQLManager.to_json(session.idConnection, env, sqlId, tableName, selectQuery).toJSONString());

						case "sql to xml":

							sqlId = inputVector.get(3).value;
							tableName = inputVector.get(4).value;
							selectQuery = inputVector.get(5).value;

							return SQLManager.to_xml(session.idConnection, env, sqlId, tableName, selectQuery);

						case "json load select": 

							//Get parameters
							String docId = inputVector.get(3).value;
							String jsonString = inputVector.get(4).value;
							String jPath = inputVector.get(5).value;

							return JsonManager.load(env, docId, jsonString, jPath);

						default: 

							switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
							case "string sentence_distance":

								String percent = inputVector.get(2).value;
								String leven = inputVector.get(3).value;
								String sentence1 = inputVector.get(4).value;
								String sentence2 = inputVector.get(5).value;

								return StringFx.sentence_distance(percent, leven, sentence1, sentence2)+"";

							case "string sentences_distance":

								percent = inputVector.get(2).value;
								leven = inputVector.get(3).value;
								sentence1 = inputVector.get(4).value;
								String sentenceArray = inputVector.get(5).value;

								return JsonManager.format_Gson(StringFx.sentences_distance(percent, leven, sentence1, sentenceArray).toJSONString());

							case "json parse_array":

								//Get parameters
								docId = inputVector.get(2).value;
								String jsonPath = inputVector.get(3).value;
								String varValue = inputVector.get(4).value;
								String mqlAction = inputVector.get(5).value;
								
								JsonManager.parse_array(env, session, docId, jsonPath, varValue, mqlAction, parent_pid, current_pid);

								return "";

							case "pa xy_scatter":
								
								//Get parameters
								cmId = inputVector.get(2).value;
								String fieldX = inputVector.get(3).value;
								String fieldY = inputVector.get(4).value;
								String sql = inputVector.get(5).value;
				
								return "j23i88m90m76i39t04r09y35p14a96y09e57t43"+PALinearRegression.xy_scatter(session, cmId, fieldX, fieldY, sql);
				
							case "file reader_open":

								String fileReaderId = inputVector.get(2).value;
								String filePath = inputVector.get(3).value;
								String type = inputVector.get(4).value;
								String encoding = inputVector.get(5).value;

								return FileFx.reader_open(env, fileReaderId, filePath, type, encoding);

							case "rest http":

								String method = inputVector.get(2).value;
								String beginUrl = inputVector.get(3).value;
								String endUrl = inputVector.get(4).value;
								String header = inputVector.get(5).value;

								String s = REST.http_exe(method, beginUrl, endUrl, header);

								try {
									return JsonManager.format_Gson(s);
								} catch (Exception e) {
									return s;
								}

							case "rest https":

								method = inputVector.get(2).value;
								beginUrl = inputVector.get(3).value;
								endUrl = inputVector.get(4).value;
								header = inputVector.get(5).value;

								s = REST.https_exe(method, beginUrl, endUrl, header);

								try {
									return JsonManager.format_Gson(s);
								} catch (Exception e) {
									return s;
								}

							case "log write":

								String msg = inputVector.get(2).value;
								String status = inputVector.get(3).value;
								String c_key = inputVector.get(4).value;
								String c_val = inputVector.get(5).value;

								Log.write(msg, status, c_key, c_val, parent_pid);

								return "1";

							case "file dir_list_regex":

								String dirPath = inputVector.get(2).value;
								String regexFilter = inputVector.get(3).value;
								String getFile = inputVector.get(4).value;
								String getDirectory = inputVector.get(5).value;

								return JsonManager.format_Gson(FileFx.dir_list_regex(dirPath, regexFilter, getFile, getDirectory).toJSONString());

							case "file copy_format":

								String source = inputVector.get(2).value;
								String sourceEncoding = inputVector.get(3).value;
								String target = inputVector.get(4).value;
								String targetEncoding = inputVector.get(5).value;

								return FileFx.copy_format(source, sourceEncoding, target, targetEncoding);

							case "ftps put":

								//Get parameters
								sessionId = inputVector.get(2).value;
								localFile = inputVector.get(3).value;
								remoteFile = inputVector.get(4).value;
								String mode = inputVector.get(5).value;

								return FtpsManager.put(env, sessionId, localFile, remoteFile, mode);

							case "sftp put":

								//Get parameters
								sessionId = inputVector.get(2).value;
								localFile = inputVector.get(3).value;
								remoteFile = inputVector.get(4).value;
								mode = inputVector.get(5).value;

								return SftpManager.put(env, sessionId, localFile, remoteFile, mode);

							case "xml itext":

								//Get parameters
								String xmlId = inputVector.get(2).value;
								String xPath = inputVector.get(3).value;
								String nodeName = inputVector.get(4).value;
								String text = inputVector.get(5).value;

								return XmlManager.insert_text(env, xmlId, xPath, nodeName, text);

							case "xml iattribute":

								//Get parameters
								xmlId = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String attributeName = inputVector.get(4).value;
								text = inputVector.get(5).value;

								return XmlManager.insert_attribute(env, xmlId, xPath, attributeName, text);

							case "sql parse":

								//Get parameters
								sessionId = inputVector.get(2).value;
								String namespace = inputVector.get(3).value;
								selectQuery = inputVector.get(4).value;
								mqlAction = inputVector.get(5).value;

								SQLManager.parse(session.idConnection, env, session, sessionId, selectQuery, namespace, mqlAction, parent_pid, current_pid);
								
								return "";

							case "json iarray":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String value = inputVector.get(4).value;
								type = inputVector.get(5).value;

								return JsonManager.ainsert(env, key, xPath, value, type);

							case "math decimal_format":

								String number = inputVector.get(2).value;
								String pattern = inputVector.get(3).value;
								String decimalSeparator = inputVector.get(4).value;
								String groupingSeparator = inputVector.get(5).value;

								return MathFx.decimal_format( number, pattern, decimalSeparator, groupingSeparator);

							default:

								switch (inputVector.get(0).value) {
								default:

									//Script execution
									inputVector.remove(inputVector.size()-1);

									return concatOrUnknow(inputVector);

								}

							}

						}

					}

				}

			case 7:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
					case "dl img step4 create_or_load_network":
						
						//Get params
						String writerId = inputVector.get(4).value;
						String outputActivation = inputVector.get(5).value;
						String saveNetworkPath = inputVector.get(6).value;
						
						ImageNeuralNetworkManager.config_create_network(env, writerId, outputActivation, saveNetworkPath);
						
						return "1";

					case "dl img step6 predict":
						
						//Get params
						writerId = inputVector.get(4).value;
						String imgPath = inputVector.get(5).value;
						String identity = inputVector.get(6).value;
						
						ImageNeuralNetworkManager.config_predict(env, writerId, imgPath, identity);
						
						return "1";

					case "dl img step2 add_image":
						
						//Get params
						writerId = inputVector.get(4).value;
						imgPath = inputVector.get(5).value;
						identity = inputVector.get(6).value;
						
						ImageNeuralNetworkManager.config_add_image(env, writerId, imgPath, identity);
						
						return "1";

					case "json load by ref": 

						//Get parameters
						String fromDocId = inputVector.get(4).value;
						String jPath = inputVector.get(5).value;
						String newDocId = inputVector.get(6).value;

						return JsonManager.loadByRef(env, fromDocId, newDocId, jPath);

					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
						case "dl n_bayesian create_model":
							
							//Get params
							String lang = inputVector.get(3).value;
							String train_file_path = inputVector.get(4).value;
							String iterations_param = inputVector.get(5).value;
							String model_file_path_to_save = inputVector.get(6).value;
							
							TextCatManager.create_model(lang, train_file_path, iterations_param, model_file_path_to_save);
							
							return "1";

						case "ml cluster point_add":
							
							//Get params
							String key = inputVector.get(3).value;
							String clusterIndex = inputVector.get(4).value;
							String x = inputVector.get(5).value;
							String y = inputVector.get(6).value;
							
							MLManager.addPoint(env, key, clusterIndex, x, y);
							
							return "1";

						case "ml cluster load_from_json":
							
							String clusterId = inputVector.get(3).value;
							String maximumRadius = inputVector.get(4).value;
							String minPoint = inputVector.get(5).value;
							String json = inputVector.get(6).value;
							
							MLManager.loadFromJson(env, clusterId, json, maximumRadius, minPoint);
							
							return "1";
			
						case "sql to excel":

							String sqlId = inputVector.get(3).value;
							String tableName = inputVector.get(4).value;
							String selectQuery = inputVector.get(5).value;
							String excelPath = inputVector.get(6).value;

							SQLManager.to_excel(session.idConnection, env, sqlId, tableName, selectQuery, excelPath);
							
							return "1";

						case "sql to excelx":

							sqlId = inputVector.get(3).value;
							tableName = inputVector.get(4).value;
							selectQuery = inputVector.get(5).value;
							excelPath = inputVector.get(6).value;

							SQLManager.to_excelx(session.idConnection, env, sqlId, tableName, selectQuery, excelPath);
							
							return "1";

						case "sql to pdf":

							sqlId = inputVector.get(3).value;
							tableName = inputVector.get(4).value;
							selectQuery = inputVector.get(5).value;
							excelPath = inputVector.get(6).value;

							SQLManager.to_pdf(session.idConnection, env, sqlId, tableName, selectQuery, excelPath);
							
							return "1";

						case "excel cell get":

							String excelId = inputVector.get(3).value;
							String sheetName = inputVector.get(4).value;
							String rowIndex = inputVector.get(5).value;
							String colIndex = inputVector.get(6).value;

							return ExcelManager.cell_get(env, excelId, sheetName, rowIndex, colIndex);

						case "excel cell eval":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;
							rowIndex = inputVector.get(5).value;
							colIndex = inputVector.get(6).value;

							return ExcelManager.cell_eval(env, excelId, sheetName, rowIndex, colIndex);

						case "excelx cell get":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;
							rowIndex = inputVector.get(5).value;
							colIndex = inputVector.get(6).value;

							return ExcelxManager.cell_get(env, excelId, sheetName, rowIndex, colIndex);

						case "excelx cell eval":

							excelId = inputVector.get(3).value;
							sheetName = inputVector.get(4).value;
							rowIndex = inputVector.get(5).value;
							colIndex = inputVector.get(6).value;

							return ExcelxManager.cell_eval(env, excelId, sheetName, rowIndex, colIndex);

						case "mail download pop3":

							//Get key, name and value
							String output_dir = inputVector.get(3).value;
							String nbMsgToDownload = inputVector.get(4).value;
							String deleteMsgAfterDownload = inputVector.get(5).value;
							json = inputVector.get(6).value;

							return JsonManager.format_Gson(Pop3Manager.parse(output_dir, nbMsgToDownload, deleteMsgAfterDownload, json));

						default:

							switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
							case "soap http":

								String url = inputVector.get(2).value;
								String jsonHeader = inputVector.get(3).value;
								String actionName = inputVector.get(4).value;
								String contentType = inputVector.get(5).value;
								String data = inputVector.get(6).value;

								return SOAPManager.execute_http(url, jsonHeader, actionName, contentType, data);

							case "soap https":

								url = inputVector.get(2).value;
								jsonHeader = inputVector.get(3).value;
								actionName = inputVector.get(4).value;
								contentType = inputVector.get(5).value;
								data = inputVector.get(6).value;

								return SOAPManager.execute_https(url, jsonHeader, actionName, contentType, data);

							case "json parse_obj":

								//Get parameters
								String docId = inputVector.get(2).value;
								String jsonPath = inputVector.get(3).value;
								String varKey = inputVector.get(4).value;
								String varValue = inputVector.get(5).value;
								String mqlAction = inputVector.get(6).value;
								
								JsonManager.parse_obj(env, session, docId, jsonPath, varKey, varValue, mqlAction, parent_pid, current_pid);

								return "";

							case "file writer_open":

								writerId = inputVector.get(2).value;
								String filePath = inputVector.get(3).value;
								String append = inputVector.get(4).value;
								String type = inputVector.get(5).value;
								String encoding = inputVector.get(6).value;

								return FileFx.writer_open(env, writerId, filePath, append, type, encoding);

							case "json uarray":

								//Get key, name and value
								key = inputVector.get(2).value;
								String xPath = inputVector.get(3).value;
								String index = inputVector.get(4).value;
								String value = inputVector.get(5).value;
								type = inputVector.get(6).value;

								return JsonManager.aupdate(env, key, xPath, index, value, type);

							case "json uobject":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								String fieldName = inputVector.get(4).value;
								value = inputVector.get(5).value;
								type = inputVector.get(6).value;

								return JsonManager.oupdate(env, key, xPath, fieldName, value, type);

							case "json iobject":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								fieldName = inputVector.get(4).value;
								value = inputVector.get(5).value;
								type = inputVector.get(6).value;

								return JsonManager.oinsert(env, key, xPath, fieldName, value, type);

							case "json iarray":

								//Get key, name and value
								key = inputVector.get(2).value;
								xPath = inputVector.get(3).value;
								index = inputVector.get(4).value;
								value = inputVector.get(5).value;
								type = inputVector.get(6).value;

								return JsonManager.ainsert(env, key, xPath, index, value, type);

							default:

								//Script execution
								inputVector.remove(inputVector.size()-1);

								return concatOrUnknow(inputVector);

							}

						}

					}

				}

			case 8:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				case "dl img step3 create_hidden_layer":
					
					//Get params
					String writerId = inputVector.get(4).value;
					String nbNeuron = inputVector.get(5).value;
					String activation = inputVector.get(6).value;
					String hasBias = inputVector.get(7).value;
					
					ImageNeuralNetworkManager.config_create_hidden_layers(env, writerId, nbNeuron, activation, hasBias);
					
					return "1";

				case "dl img step1 create_training":
					
					//Get params
					writerId = inputVector.get(4).value;
					String width = inputVector.get(5).value;
					String height = inputVector.get(6).value;
					String isRGB = inputVector.get(7).value;
					
					ImageNeuralNetworkManager.config_training(env, writerId, width, height, isRGB);
					
					return "1";

				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "dl img predict":
						
						//Get params
						String imagePath = inputVector.get(3).value;
						isRGB = inputVector.get(4).value;
						width = inputVector.get(5).value;
						height = inputVector.get(6).value;
						String jsonIdentity = inputVector.get(7).value;
						
						return ImageNeuralNetworkManager.predict(env, imagePath, isRGB, width, height, jsonIdentity);

					case "ml h_node predict":
						
						//Get params
						String key = inputVector.get(3).value;
						String searchId = inputVector.get(4).value;
						String algorithm = inputVector.get(5).value;
						String to = inputVector.get(6).value;
						String param = inputVector.get(7).value;
						
						return JsonManager.format_Gson(MLManager.heuristicNode_compute(env, key, searchId, algorithm, to, param).toJSONString());

					case "ml cluster point_update":
						
						//Get params
						key = inputVector.get(3).value;
						String clusterIndex = inputVector.get(4).value;
						String pointIndex = inputVector.get(5).value;
						String x = inputVector.get(6).value;
						String y = inputVector.get(7).value;
						
						MLManager.updatePoint(env, key, clusterIndex, pointIndex, x, y);
						
						return "1";

					case "ml cluster distance":
						
						String clusterId = inputVector.get(3).value;
						String x1 = inputVector.get(4).value;
						String y1 = inputVector.get(5).value;
						String x2 = inputVector.get(6).value;
						String y2 = inputVector.get(7).value;
						
						return MLManager.getDistanceMeasure(env, clusterId, x1, y1, x2, y2);
						
					case "pa rl load":
						
						String regId = inputVector.get(3).value;
						String cmId = inputVector.get(4).value;
						String fieldX = inputVector.get(5).value;
						String fieldY = inputVector.get(6).value;
						String sqlSource = inputVector.get(7).value;
						
						PALinearRegression.load(env, session, regId, cmId, fieldX, fieldY, sqlSource);
						
						return "1";
						
					case "sql to csv":

						String sqlId = inputVector.get(3).value;
						String tableName = inputVector.get(4).value;
						String selectQuery = inputVector.get(5).value;
						String columnSeparator = inputVector.get(6).value;
						String quoteChar = inputVector.get(7).value;

						return SQLManager.to_csv(session.idConnection, env, sqlId, tableName, selectQuery, columnSeparator, quoteChar);

					case "sql to csv_file":

						sqlId = inputVector.get(3).value;
						String filePath = inputVector.get(4).value;
						selectQuery = inputVector.get(5).value;
						columnSeparator = inputVector.get(6).value;
						quoteChar = inputVector.get(7).value;

						SQLManager.to_csv_file(session.idConnection, env, sqlId, filePath, selectQuery, columnSeparator, quoteChar);

						return "1";
						
					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "scrud csv_to_db":

							cmId = inputVector.get(2).value;
							String tablename = inputVector.get(3).value;
							filePath = inputVector.get(4).value;
							columnSeparator = inputVector.get(5).value;
							quoteChar = inputVector.get(6).value;
							String forceColumnNames = inputVector.get(7).value;

							return ScrudManager.db_csv_to_db(session, cmId, tablename, filePath, columnSeparator, quoteChar, forceColumnNames);

						case "html parse":

							//Get parameters
							String domId = inputVector.get(2).value;
							String jsonDocId = inputVector.get(3).value;
							String typeSearch = inputVector.get(4).value;
							key = inputVector.get(5).value;
							String val = inputVector.get(6).value;
							String mqlAction = inputVector.get(7).value;
							
							HTMLManager.parse(env, session, domId, jsonDocId, typeSearch, key, val, mqlAction, parent_pid, current_pid);

							return "";

						case "csv parse":

							//Get parameters
							String namespace = inputVector.get(2).value;
							filePath = inputVector.get(3).value;
							columnSeparator = inputVector.get(4).value;
							quoteChar = inputVector.get(5).value;
							forceColumnNames = inputVector.get(6).value;
							mqlAction = inputVector.get(7).value;
							
							CSVManager.parse(env, session, filePath, columnSeparator, quoteChar, namespace, forceColumnNames, mqlAction, parent_pid, current_pid);
							
							return "";

						default:

							//Script execution
							inputVector.remove(inputVector.size()-1);

							return concatOrUnknow(inputVector);

						}

					}

				}

			case 9:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "excel cell set":

						String excelId = inputVector.get(3).value;
						String sheetName = inputVector.get(4).value;
						String rowIndex = inputVector.get(5).value;
						String colIndex = inputVector.get(6).value;
						String value = inputVector.get(7).value;
						String type = inputVector.get(8).value;

						ExcelManager.cell_set(env, excelId, sheetName, rowIndex, colIndex, value, type);

						return "1";

					case "excelx cell set":

						excelId = inputVector.get(3).value;
						sheetName = inputVector.get(4).value;
						rowIndex = inputVector.get(5).value;
						colIndex = inputVector.get(6).value;
						value = inputVector.get(7).value;
						type = inputVector.get(8).value;

						ExcelxManager.cell_set(env, excelId, sheetName, rowIndex, colIndex, value, type);

						return "1";

					case "excel cell format":

						excelId = inputVector.get(3).value;
						sheetName = inputVector.get(4).value;
						rowIndex = inputVector.get(5).value;
						colIndex = inputVector.get(6).value;
						String format = inputVector.get(7).value;
						String config = inputVector.get(8).value;

						ExcelManager.cell_format(env, excelId, sheetName, rowIndex, colIndex, config, format);

						return "1";

					case "excelx cell format":

						excelId = inputVector.get(3).value;
						sheetName = inputVector.get(4).value;
						rowIndex = inputVector.get(5).value;
						colIndex = inputVector.get(6).value;
						format = inputVector.get(7).value;
						config = inputVector.get(8).value;

						ExcelxManager.cell_format(env, excelId, sheetName, rowIndex, colIndex, config, format);

						return "1";

					default:

						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "html load_from_url":

							String domId = inputVector.get(2).value;
							String url = inputVector.get(3).value;
							String method = inputVector.get(4).value;
							String timeout = inputVector.get(5).value;
							String jsonHeader = inputVector.get(6).value;
							String jsonCookies = inputVector.get(7).value;
							String jsonData = inputVector.get(8).value;

							HTMLManager.load_from_url(env, domId, url, method, timeout, jsonHeader, jsonCookies, jsonData);
							
							return "1";

						case "mail send":

							String cm = inputVector.get(2).value;
							String to = inputVector.get(3).value;
							String cc = inputVector.get(4).value;
							String bcc = inputVector.get(5).value;
							String subject = inputVector.get(6).value;
							String body = inputVector.get(7).value;
							String json = inputVector.get(8).value;

							SmtpManager.send(cm, to, cc, bcc, subject, body, json);

							return "1";

						default:

							//Script execution
							inputVector.remove(inputVector.size()-1);

							return concatOrUnknow(inputVector);

						}

					}

				}

			case 10:
				
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
				case "dl img step5 train_network":

					String writerId = inputVector.get(4).value;
					String mode = inputVector.get(5).value;
					String minutes = inputVector.get(6).value;
					String strategyError = inputVector.get(7).value;
					String strategyCycles = inputVector.get(8).value;
					String saveNetworkPath = inputVector.get(9).value;

					ImageNeuralNetworkManager.config_train(env, writerId, mode, minutes, strategyError, strategyCycles, saveNetworkPath);

					return "1";

				default:
				
					switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
					case "ml cluster load":
						
						String clusterId = inputVector.get(3).value;
						String cmId = inputVector.get(4).value;
						String fieldX = inputVector.get(5).value;
						String fieldY = inputVector.get(6).value;
						String maxRadius = inputVector.get(7).value;
						String minPoints = inputVector.get(8).value;
						String sqlSource = inputVector.get(9).value;
						
						MLManager.load(env, session, clusterId, cmId, fieldX, fieldY, maxRadius, minPoints, sqlSource);
						
						return "1";
						
					default:
					
						switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
						case "stack search":

							String tableType = inputVector.get(2).value;
							String script = inputVector.get(3).value;
							String dtType = inputVector.get(4).value;
							String dtMin = inputVector.get(5).value;
							String dtMax = inputVector.get(6).value;
							String dtOrder = inputVector.get(7).value;
							String page = inputVector.get(8).value;
							String nbByPage = inputVector.get(9).value;
							
							return StackManager.search(tableType, script, 
									dtType, dtMin, dtMax, dtOrder, page, nbByPage);

						default:

							//Script execution
							inputVector.remove(inputVector.size()-1);

							return concatOrUnknow(inputVector);

						}
						
					}
					
				}

			case 11:
				
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

					return concatOrUnknow(inputVector);
					
				}

			case 12:
				
				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
				case "pa rm load":
					
					String regId = inputVector.get(3).value;
					String cmId = inputVector.get(4).value;
					String fieldX1 = inputVector.get(5).value;
					String fieldX2 = inputVector.get(6).value;
					String fieldX3 = inputVector.get(7).value;
					String fieldX4 = inputVector.get(8).value;
					String fieldX5 = inputVector.get(9).value;
					String fieldY = inputVector.get(10).value;
					String sqlSource = inputVector.get(11).value;
					
					PAMultipleRegressionOLS.load(env, session, regId, cmId, fieldX1, fieldX2, fieldX3, fieldX4, fieldX5, fieldY, sqlSource);
					
					return "1";
					
				default:
				
					switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
					case "log search":

						String status = inputVector.get(2).value;
						String script = inputVector.get(3).value;
						String c_key = inputVector.get(4).value;
						String c_val = inputVector.get(5).value;
						String msgFilter = inputVector.get(6).value;
						String dtMin = inputVector.get(7).value;
						String dtMax = inputVector.get(8).value;
						String dtOrder = inputVector.get(9).value;
						String page = inputVector.get(10).value;
						String nbByPage = inputVector.get(11).value;

						return Log.search(status, script, c_key, c_val, msgFilter, dtMin, dtMax, dtOrder, page, nbByPage);

					default:

						//Script execution
						inputVector.remove(inputVector.size()-1);

						return concatOrUnknow(inputVector);

					}
					
				}

			case 14:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
				case "mail download imap":

					//Get key, name and value
					String output_dir = inputVector.get(3).value;
					String nbMsgToDownload = inputVector.get(4).value;
					String unreadOrAll = inputVector.get(5).value;
					String copyMessageInAnotherFolder = inputVector.get(6).value;
					String deleteMsgAfterDownload = inputVector.get(7).value;
					String markAsRead = inputVector.get(8).value;
					String startDate = inputVector.get(9).value;
					String endDate = inputVector.get(10).value;
					String fromCondition = inputVector.get(11).value;
					String subjectCondition = inputVector.get(12).value;
					String json = inputVector.get(13).value;

					return JsonManager.format_Gson(
							ImapManager.parse(output_dir, nbMsgToDownload, unreadOrAll, 
									copyMessageInAnotherFolder, deleteMsgAfterDownload, 
									markAsRead, startDate, endDate, fromCondition, 
									subjectCondition, json, env, session, 
									parent_pid, current_pid));

				default:

					//Script execution
					inputVector.remove(inputVector.size()-1);

					return concatOrUnknow(inputVector);

				}

			default:

				//Script execution
				inputVector.remove(inputVector.size()-1);

				return concatOrUnknow(inputVector);

			}

		}

	}

	public static String concatOrUnknow(Vector<MQLValue> inputVector) throws Exception {

		String val = inputVector.get(0).value;

		if ((val.startsWith("R")
				&& val.length()>3 && val.charAt(2)=='[') || val.startsWith("TH[")) {

			return StringFx.concat(inputVector);

		} else {

			throw new Exception("Sorry, unknown order: "+Misc.vectorToStringMsg(inputVector));

		}

	}

}