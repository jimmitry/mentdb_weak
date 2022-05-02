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

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.symbol.SymbolManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dl.BayesianNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.TextCatManager;
import re.jpayet.mentdb.ext.dl4j.DL4J_CSV_Manager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.fx.MyRSA;
import re.jpayet.mentdb.ext.fx.OperatorFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.fx.TypeFx;
import re.jpayet.mentdb.ext.html.HTMLManager;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.se.IndexAI;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.WebSocketThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stat.Statistic;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess3 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
		case "dq algorithm show":
			
			return JsonManager.format_Gson(DQManager.show().toJSONString());
			
		case "job show activate":
			
			return JsonManager.format_Gson(JobManager.showActivate().toJSONString());
			
		case "script show ghost":
			
			return NodeManager.format(ScriptManager.showGhost().toJSONString());
			
		case "job scheduler start":
			
			JobManager.scheduler_start(session.user);
			
			return "Job scheduler started with successful.";

		case "job scheduler stop":
			
			JobManager.scheduler_stop(session.user);
			
			return "Job scheduler stopped with successful.";

		case "job scheduler restart":
			
			JobManager.scheduler_restart(session.user);
			
			return "Job scheduler restarted with successful.";

		case "job scheduler status":
			
			return JobManager.scheduler_status();
		
		case "sql block close":

			SQLManager.block_close(session.idConnection, env);

			return "1";

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
			
			return AppManager.restart_webserver(session);

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

		case "mongodb disconnect all":
			
			MongoDBManager.client_disconnectall(env);
			
			return "1";

		case "mongodb database_unload all":
			
			MongoDBManager.database_unloadall(env);
			
			return "1";

		case "mongodb collection_unload all":
			
			MongoDBManager.collection_unloadall(env);
			
			return "1";

		default: 

			switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
			case "stack flow_json_get":
				
				//Get key
				String pid = inputVector.get(2).value;
				
				return StackManager.flow_json_get(pid);
			
			case "stack var_show":
				
				//Get key
				pid = inputVector.get(2).value;
				
				return StackManager.var_show(pid);
			
			case "file image_rotate_90":

				String filePath = inputVector.get(2).value;
				
				return FileFx.rotate_image_90(filePath);

			case "version check_group":

				String group = inputVector.get(2).value;
				
				return ScriptManager.commitCheck(session, session.idConnection, group);
				
			case "concentration depth":
				
				String key = inputVector.get(2).value;

				return ""+ConcentrationManager.getConcentrationDepth(key);

			case "relation stimulate":
				
				//Get keys
				String relationId = inputVector.get(2).value;
				
				RelationManager.stimulate(relationId, true);

				return "Relation "+relationId+" stimulated with successful.";

			case "relation delete":
				
				//Get relation id
				relationId = inputVector.get(2).value;
				
				RelationManager.delete(relationId);
				
				return "Relation '"+relationId+"' deleted with successful.";

			case "thought delete":
				
				//Get keys
				String thoughtId = inputVector.get(2).value;
				
				ThoughtManager.delete(thoughtId);

				return "Thought "+thoughtId+" deleted with successful.";

			case "thought stimulate":
				
				//Get keys
				thoughtId = inputVector.get(2).value;
				
				ThoughtManager.stimulate(thoughtId);

				return "Thought "+thoughtId+" stimulated with successful.";

			case "thought show":
				
				//Get key
				String word = inputVector.get(2).value;

				return JsonFormatter.format(ThoughtManager.list(word).toJSONString());

			case "word delete":
				
				//Get key
				word = inputVector.get(2).value;

				WordManager.delete(word);

				return "Word W["+inputVector.get(2).value+"] deleted with successful.";

			case "word show":
				
				word = inputVector.get(2).value;
				
				return JsonFormatter.format(WordManager.showTabLinks(word).toJSONString());
				
			case "symbol stimulate":
				
				String tabLinkId = inputVector.get(2).value;
				
				SymbolManager.stimulate(tabLinkId);
				
				return "Symbol tab link stimulated with successful.";

			case "language insert": case "language create": case "language add": 
				
				//Get parameters
				String lang = inputVector.get(2).value;
				
				LanguageManager.create(lang, session, env);

				return "Language "+lang+" created with successful.";

			case "cluster create":
				
				//Get parameters
				String cluster_id = inputVector.get(2).value;
				
				ClusterManager.add(session.idConnection, cluster_id);

				return "Cluster created with successful.";

			case "cluster exist":
				
				//Get parameters
				cluster_id = inputVector.get(2).value;
				
				if (ClusterManager.exist(cluster_id)) return "1";
				else return "0";

			case "cluster delete":
				
				//Get parameters
				cluster_id = inputVector.get(2).value;
				
				ClusterManager.remove(session.idConnection, cluster_id);

				return "Cluster deleted with successful.";

			case "file_watcher kill": 
				
				//Get key
				key = inputVector.get(2).value;
				
				FileFx.stop_watch_service(key);
				
				return "1";

			case "file_watcher exist": 
				
				//Get key
				key = inputVector.get(2).value;
				
				if (FileFx.exist_watch_service(key)) return "1";
				else return "0";

			case "restricted eval":
				
				String scriptname = inputVector.get(2).value;
				
				return Statement.eval(session, ScriptManager.generateExecute(session, env, scriptname, parent_pid), env, parent_pid, current_pid);
	
			case "restricted execute":
				
				scriptname = inputVector.get(2).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateExecute(session, env, scriptname, parent_pid);
	
			case "restricted stack":
				
				scriptname = inputVector.get(2).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateStack(session, env, scriptname, parent_pid);
	
			case "restricted include":
				
				scriptname = inputVector.get(2).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateInclude(session, env, scriptname, parent_pid);
	
			case "restricted call":
				
				scriptname = inputVector.get(2).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateCall(session, env, scriptname, parent_pid);
			
			case "cm show": case "restricted cm_show":
				
				String type = inputVector.get(2).value;
				
				return JsonFormatter.format(CMManager.show(session.user, type).toJSONString());
	
			case "cm show_obj":
				
				type = inputVector.get(2).value;
				
				return JsonFormatter.format(CMManager.show_obj(type).toJSONString());
	
			case "script export_group":
				
				group = inputVector.get(2).value;
				
				ScriptManager.exportGroup(session, session.idConnection, group);
				
				return "Scripts exported with successfully.";
				
			case "script import_group":

				group = inputVector.get(2).value;
				
				ScriptManager.importGroup(session, env, session.idConnection, group, parent_pid, current_pid);
				
				return "Scripts imported with successfully.";
				
			case "job pause":
				
				String jobid = inputVector.get(2).value;
				
				JobManager.job_pause(session.user, jobid);
				
				return "1";

			case "job resume":
				
				jobid = inputVector.get(2).value;
				
				JobManager.job_resume(session.user, jobid);
				
				return "1";

			case "user secret_key":
				
				String struser = inputVector.get(2).value;
				
				return UserManager.secret_key(session.idConnection, struser);

			case "refresh devel":
				
				String search = inputVector.get(2).value;

				return GroupManager.develToHtml(session, env, session.user, parent_pid, search);

			case "cm get":
				
				//Get key, name and value
				key = inputVector.get(2).value;
				
				return JsonManager.format_Gson(CMManager.get(session.user, key).toJSONString());

			case "cm generate_update":
				
				//Get key, name and value
				key = inputVector.get(2).value;
				
				return CMManager.generate_update(session.user, key);

			case "cm exist":
				
				//Get key, name and value
				key = inputVector.get(2).value;
				
				return CMManager.exist(key);

			case "cm remove":
				
				//Get key, name and value
				key = inputVector.get(2).value;
				
				CMManager.remove(session.user, key);
				
				return "1";

			case "node insert": case "node create": case "node add":
				
				//Get key
				key = inputVector.get(2).value;
				
				if (!GroupManager.isGrantedUser("sys", session.user)) {
					throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
				}

				NodeManager.insertNode(key);

				return "Node "+inputVector.get(2).value+" created with successful.";

			case "node exist":
				
				//Get key
				key = inputVector.get(2).value;
				
				if (!GroupManager.isGrantedUser("sys", session.user)) {
					throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
				}

				if (NodeManager.existNode(key)) return "1";
				else return "0";

			case "node delete":
				
				//Get key
				key = inputVector.get(2).value;
				
				if (!GroupManager.isGrantedUser("sys", session.user)) {
					throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
				}
				
				NodeManager.remove(key);
				return "1";

			case "group add": case "group insert": case "group create":
				
				String groupName = inputVector.get(2).value;
				
				GroupManager.add(session.idConnection, session.user, groupName);
				
				return "Group added with successful.";

			case "group remove": 
				
				groupName = inputVector.get(2).value;
				
				GroupManager.delete(session.idConnection, groupName);
				
				return "Group removed with successful.";

			case "group exist": 
				
				groupName = inputVector.get(2).value;
				
				if (GroupManager.exist(groupName)) return "1";
				else return "0";

			case "group get":
				
				groupName = inputVector.get(2).value;
				
				return NodeManager.format(GroupManager.get(groupName).toJSONString());

			case "sequence remove":
				
				//Get key
				key = inputVector.get(2).value;
			
				SequenceManager.remove(key);
				
				return "Sequence removed with successful.";
			
			case "job remove":
				
				//Get key
				key = inputVector.get(2).value;
			
				JobManager.delete(session.user, key);
				
				return "Job removed with successful.";
			
			case "parameter remove":
				
				//Get key
				key = inputVector.get(2).value;
			
				ParameterManager.remove(key);
				
				return "Parameter removed with successful.";
			
			case "sequence increment":
				
				//Get key
				key = inputVector.get(2).value;
			
				return SequenceManager.incr(key);
			
			case "sequence exist":
				
				//Get key
				key = inputVector.get(2).value;
			
				if (SequenceManager.exist(key)) return "1";
				else return "0";
			
			case "job exist":
				
				//Get key
				key = inputVector.get(2).value;
			
				if (JobManager.exist(key)) return "1";
				else return "0";
			
			case "parameter exist":
				
				//Get key
				key = inputVector.get(2).value;
			
				if (ParameterManager.exist(key)) return "1";
				else return "0";
			
			case "user exist":
				
				//Get key
				String login = inputVector.get(2).value;
			
				if (UserManager.exist(login)) return "1";
				else return "0";
			
			case "user delete":
				
				//Get key
				login = inputVector.get(2).value;
				
				UserManager.delete(session.idConnection, login);
			
				return "1";
			
			case "script delete":
				
				String id = inputVector.get(2).value;
				
				ScriptManager.delete(session.idConnection, id);
				
				return "Script deleted with successful.";
			
			case "script exist":
				
				id = inputVector.get(2).value;
				
				if (ScriptManager.exist(id)) return "1";
				else return "0";
			
			case "script get":
				
				id = inputVector.get(2).value;
				
				return JsonFormatter.format(ScriptManager.get(session, id).toJSONString());
			
			case "node show":
				
				//Get key
				key = inputVector.get(2).value;
				
				if (!GroupManager.isGrantedUser("sys", session.user)) {
					throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
				}
				
				JSONObject rec = Record2.getNode(key);

				//SHOW RECORD
				if (rec==null) throw new Exception("Sorry, the node '"+key+"' does not exist.");
				else return JsonFormatter.format(rec.toJSONString());

			case "mongodb database_show":
				
				//Get parameters
				String clientId = inputVector.get(2).value;
				
				return JsonManager.format_Gson(MongoDBManager.database_show(env, clientId).toJSONString());

			case "mongodb collection_show":
				
				//Get parameters
				String databaseId = inputVector.get(2).value;
				
				return JsonManager.format_Gson(MongoDBManager.collection_show(env, databaseId).toJSONString());

			case "mongodb client_exist":
				
				//Get parameters
				clientId = inputVector.get(2).value;
				
				return MongoDBManager.client_exist(env, clientId);

			case "mongodb database_exist":
				
				//Get parameters
				databaseId = inputVector.get(2).value;
				
				return MongoDBManager.database_exist(env, databaseId);

			case "mongodb collection_exist":
				
				//Get parameters
				String collectionId = inputVector.get(2).value;
				
				return MongoDBManager.collection_exist(env, collectionId);

			case "mongodb database_stat":
				
				//Get parameters
				databaseId = inputVector.get(2).value;
				
				return JsonManager.format_Gson(MongoDBManager.database_stat(env, databaseId).toJSONString());

			case "mongodb disconnect":
				
				//Get parameters
				clientId = inputVector.get(2).value;
				
				MongoDBManager.client_disconnect(env, clientId);
				
				return "1";

			case "mongodb database_unload":
				
				//Get parameters
				databaseId = inputVector.get(2).value;
				
				MongoDBManager.database_unload(env, databaseId);
				
				return "1";

			case "mongodb collection_unload":
				
				//Get parameters
				collectionId = inputVector.get(2).value;
				
				MongoDBManager.collection_unload(env, collectionId);
				
				return "1";

			case "index_ai execute": 
				
				//Get parameters
				String ordersString = inputVector.get(2).value;
				
				return IndexAI.execute(session.idConnection, ordersString).toJSONString();
								
			case "dl4j exist":
				
				//Get parameters
				String dl4jId = inputVector.get(2).value;
				
				if (env.dl4jModel.containsKey(dl4jId)) return "1";
				else return "0";

			case "dl4j delete":
				
				//Get parameters
				dl4jId = inputVector.get(2).value;
				
				if (env.dl4jModel.containsKey(dl4jId)) env.dl4jModel.remove(dl4jId);
				if (env.dl4jNormalizer.containsKey(dl4jId)) env.dl4jNormalizer.remove(dl4jId);
				
				return "1";

			case "dl4j csv_train_and_save_model": 
				
				String json_conf = inputVector.get(2).value;
				
				return DL4J_CSV_Manager.train_and_save_model(env, json_conf);
								
			/*case "dl4j img_train_and_save_model": 
				
				json_conf = inputVector.get(2).value;
				
				DL4J_IMG_Manager.train_and_save_model(env, json_conf);
				
				return "1";*/
								
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
				cluster_id = inputVector.get(2).value;
				
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

			case "stack delete_wait_id":
				
				pid = inputVector.get(2).value;
				
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

			case "log show_time": case "restricted show_pid":

				pid = inputVector.get(2).value;

				return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+Log.show_time(Start.CLUSTER_NODENAME, pid);

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

			case "file reader_close": case "restricted file_reader_close":

				readerId = inputVector.get(2).value;

				return FileFx.reader_close(env, readerId);

			case "file reader_exist":

				readerId = inputVector.get(2).value;

				return FileFx.reader_exist(env, readerId);

			case "file writer_exist":

				String writerId = inputVector.get(2).value;

				return FileFx.writer_exist(env, writerId);

			case "file writer_flush": case "restricted file_writer_flush":

				writerId = inputVector.get(2).value;

				return FileFx.writer_flush(env, writerId);

			case "file writer_close": case "restricted file_writer_close":

				writerId = inputVector.get(2).value;

				return FileFx.writer_close(env, writerId);

			case "file b64_read":

				filePath = inputVector.get(2).value;

				return FileFx.b64_read(session.user, filePath);

			case "file is_directory": case "restricted file_is_directory":

				filePath = inputVector.get(2).value;

				return FileFx.is_directory(filePath);

			case "file mkdir":

				String dirPath = inputVector.get(2).value;

				return FileFx.mkdir(dirPath);

			case "file dir_list": case "restricted dir_list":

				if (session.user.equals("mentdb") || GroupManager.isGrantedUser("api-mql", session.user)) {
					dirPath = inputVector.get(2).value;
				} else {
					dirPath = "tmp/"+session.user;
				}

				return JsonManager.format_Gson(FileFx.dir_list(dirPath).toJSONString());

			case "file delete":

				String path = inputVector.get(2).value;

				return FileFx.delete(path);

			case "file size": case "restricted file_size":

				filePath = inputVector.get(2).value;

				return FileFx.size(filePath);

			case "file last_modified":

				filePath = inputVector.get(2).value;

				return FileFx.last_modified(filePath);

			case "file exist": case "restricted file_exist":

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

				key = inputVector.get(2).value;

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

				return FileFx.load(session.user, filePath);

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
				
			case "math order_int":

				String json = inputVector.get(2).value;

				return MathFx.order_int_asc(json);

			case "math order_long":

				json = inputVector.get(2).value;

				return MathFx.order_long_asc(json);

			case "math order_double":

				json = inputVector.get(2).value;

				return MathFx.order_double_asc(json);

			case "string order":

				json = inputVector.get(2).value;

				return StringFx.order_asc(json);

			case "math order_float":

				json = inputVector.get(2).value;

				return MathFx.order_float_asc(json);

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
				case "login":
					
					String adminUser = inputVector.get(1).value.toLowerCase();
					String adminPassword = inputVector.get(2).value;
					
					if (adminUser.equals("mentdb")) {
						
						return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht5";

					} else if (!adminPassword.equals("") && UserManager.checkPassword(adminUser, adminPassword)) {
						
						session.user = adminUser;
						
						return "Connected:"+session.idConnection;

					} else {
						
						//Bad password
						return "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht3";
						
					}

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

				case "*":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.mul(number1, number2);

				case "/":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.div(number1, number2);

				case "bi-":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.sub_big_int(number1, number2);

				case "bi*":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.mul_big_int(number1, number2);

				case "bi/":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.div_big_int(number1, number2);

				case "bd-":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.sub_big_dec(number1, number2);

				case "bd*":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.mul_big_dec(number1, number2);

				case "bd/":

					//Get parameters
					number1 = inputVector.get(1).value;
					number2 = inputVector.get(2).value;

					return OperatorFx.div_big_dec(number1, number2);

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

				case "synchronized":

					//Get parameters
					key = inputVector.get(1).value;
					String action = inputVector.get(2).value;

					return Statement.synchronized_block(session, key, action, env, parent_pid, current_pid);

				case "while":

					//Get parameters
					condition = inputVector.get(1).value;
					action = inputVector.get(2).value;

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
					id = inputVector.get(1).value;
					String message = inputVector.get(2).value;

					return Statement.exception_mql(session, id, message, env, parent_pid, current_pid);

				default:

					//Script execution
					inputVector.remove(inputVector.size()-1);

					return CommandFullAccess.concatOrUnknow(inputVector);

				}

			}

		}

	}

}