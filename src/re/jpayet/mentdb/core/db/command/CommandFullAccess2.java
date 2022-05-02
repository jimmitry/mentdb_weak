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
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dl4j.DL4J_CSV_Manager;
import re.jpayet.mentdb.ext.doc.MQLDocumentation;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.ConstantFx;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.fx.OperatorFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.html.HTMLManager;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.os.OsManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.se.IndexAI;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stat.Statistic;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess2 {

	//Execute the command
	@SuppressWarnings("unchecked")
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
		case "restricted is": 

			if (GroupManager.isGrantedUser("api-mql", session.user)) return "0";
			else return "1";

		case "maintenance get": case "restricted maintenance_get": 
			
			JSONObject maintenance = new JSONObject();
			maintenance.put("mql", Start.maintenance_mql);
			maintenance.put("ws", Start.maintenance_ws);
			maintenance.put("web", Start.maintenance_web);
			maintenance.put("job", Start.maintenance_job);
			maintenance.put("stack", Start.maintenance_stack);
			
			return "j23i88m90m76i39t04r09y35p14a96y09e57t48"+maintenance.toJSONString();

		case "restricted server_mode": 

			return ParameterManager.get_value("SERVER_MODE");

		case "restricted server_name": 

			return ParameterManager.get_value("SERVER_NAME");

		case "concentration show":
			
			return NodeManager.format(ConcentrationManager.show().toJSONString());

		case "language show":
			
			return JsonFormatter.format(LanguageManager.showAll().toJSONString());

		case "cluster show":
			
			return JsonManager.format_Gson(ClusterManager.show_ids(session.idConnection).toJSONString());

		case "file_watcher show": 
			
			return JsonManager.format_Gson(FileFx.show_watch_service().toJSONString());

		case "refresh admin":
			
			return GroupManager.adminToHtml(session, env, session.user, parent_pid);

		case "refresh config":
			
			return GroupManager.configToHtml(session, env, session.user, parent_pid);

		case "job help":
			
			return JobManager.help();
		
		case "metric sessions":
			
			if (!GroupManager.isGrantedUser("sys", session.user)) {
				throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
			}
			
			return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+Start.metric_sessions(session.user).toJSONString();
		
		case "group show":

			return NodeManager.format(GroupManager.showAllGroups().toJSONString());

		case "script show":
			
			return NodeManager.format(ScriptManager.show(session.idConnection).toJSONString());
			
		case "script export_all":
			
			ScriptManager.exportAll(session, session.idConnection);
			
			return "Scripts exported with successfully.";
			
		case "script get_all":
			
			return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.getAllScript(session, session.idConnection);
			
		case "user show":
			
			return JsonFormatter.format(UserManager.showAll().toJSONString());

		case "sequence show":
			
			return JsonFormatter.format(SequenceManager.showAllSeqs().toJSONString());

		case "job show":
			
			return JsonFormatter.format(JobManager.show().toJSONString());

		case "parameter show":
			
			return JsonFormatter.format(ParameterManager.showAllParams().toJSONString());

		case "cm show":
			
			return JsonFormatter.format(CMManager.show(session.user, null).toJSONString());

		case "cm show_scrud":
			
			return JsonFormatter.format(CMManager.show_scrud(session.user).toJSONString());

		case "cm show_obj":
			
			return JsonFormatter.format(CMManager.show_obj(null).toJSONString());

		case "count sessions":
			
			return ""+(SessionThreadAgent.allServerThread.size());

		case "mongodb database_show_loaded":
			
			return JsonManager.format_Gson(MongoDBManager.database_show(env).toJSONString());

		case "mongodb collection_show_loaded":
			
			return JsonManager.format_Gson(MongoDBManager.collection_show(env).toJSONString());

		case "mongodb client_show_loaded":
			
			return JsonManager.format_Gson(MongoDBManager.client_show(env).toJSONString());

		case "index_ai process": 
			
			return IndexAI.process()+"";
							
		case "stack process": 
			
			StackManager.process();
			
			return "1";
							
		case "stack status": 
			
			return StackManager.status();
											
		case "stack reset": 
			
			StackManager.reset();
			
			return "1";
							
		case "signal process": 
			
			ClusterManager.process_signal();
			return "1";

		case "bot show":
			
			return JsonManager.format_Gson(BotManager.show().toJSONString());

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
			int i4 = Integer.parseInt(FileFx.count_dir("MentDB_Editor_3/src", ".java"));
			int i5 = Integer.parseInt(FileFx.count_dir("MentDB_Editor_3/mql", ".mql"));

			String[] files = (new File("mql"+File.separator+"scripts")).list();
			for(int z=0;z<files.length;z++) {

				i3+= FileFx.count_lines("mql"+File.separator+"scripts"+File.separator+files[z]);

			}

			return (i1+i2+i3+i4+i5)+"";

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

		case "dl4j show": 

			return JsonManager.format_Gson(DL4J_CSV_Manager.show(env).toJSONString());

		default:

			switch (inputVector.get(0).value) {
			case "get_param":
				
				//Get key
				String key = inputVector.get(1).value;

				return ParameterManager.get_value(key);

			case "stop": case "shutdown":
				
				String idCode = inputVector.get(1).value;
				
				if (!idCode.equals(Misc.load("data"+File.separator+".id").replace("\r", "").replace("\n", ""))) {
					
					throw new Exception("Sorry, the identification code is invalid."); 
					
				}
				
				GroupManager.generateErrorIfNotGranted(session.idConnection, "sys", "Stop");
				
				Start.listening = false;
				Start.serverSocket.close();
				
				return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht0";

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

			case "$$":

				str = inputVector.get(1).value;

				System.out.println(str);

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

				return CommandFullAccess.concatOrUnknow(inputVector);

			}

		}

	}

}