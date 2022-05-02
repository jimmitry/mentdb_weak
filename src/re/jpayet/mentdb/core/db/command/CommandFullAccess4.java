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

import org.jfree.data.json.impl.JSONObject;
import org.json.simple.JSONArray;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.symbol.SymbolManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.compress.CompressManager;
import re.jpayet.mentdb.ext.dl.BayesianNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.CSVNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.TextCatManager;
import re.jpayet.mentdb.ext.dl4j.DL4J_CSV_Manager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
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
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.os.OsManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.remote.CifsManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.rest.REST_DOCManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.scrud.ScrudManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stat.Statistic;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess4 {

	//Execute the command
	@SuppressWarnings("unchecked")
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
		case "script public api doc": 
			
			return REST_DOCManager.public_api_doc(env);

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
			case "user set password":
				
				//Get user info
				String pwd = inputVector.get(3).value;
				
				UserManager.updatePassword(session.idConnection, session.user, pwd);
				
				return "Password updated with successful.";
			
			case "relation show thoughts":
				
				String relationId = inputVector.get(3).value;
				
				return NodeManager.format(RelationManager.showThoughtsRecursivelyRLTH(relationId).toJSONString());
			
			case "thought show words":
				
				String thoughtId = inputVector.get(3).value;
				
				return NodeManager.format(ThoughtManager.getWords(thoughtId).toJSONString());
			
			case "word show languages":
				
				//Get word
				String word = inputVector.get(3).value;
	
				return NodeManager.format(WordManager.showLanguages(word).toJSONString());
	
			case "word lang probability":
				
				//Get sentence
				String sentence = inputVector.get(3).value;
				
				Vector<Vector<MQLValue>> s = Misc.splitCommand(sentence);
				
				if (s.size()==0) {
					
					return new JSONArray().toJSONString();
					
				}
	
				return NodeManager.format(WordManager.getLangProbability(s.get(0)).toJSONString());
	
			case "symbol show languages":
				
				String symbolName = inputVector.get(3).value;
				
				return JsonFormatter.format(SymbolManager.showLanguages(symbolName).toJSONString());

			case "cluster signal show":
				
				//Get params
				String cluster_id = inputVector.get(3).value;
				
				return ClusterManager.signal_show(session.idConnection, cluster_id);
				
			case "cluster nodes show_obj":
				
				//Get params
				cluster_id = inputVector.get(3).value;
				
				return JsonManager.format_Gson(ClusterManager.node_show_obj(session.idConnection, cluster_id).toJSONString());

			case "cluster node generate_set":
				
				//Get params
				cluster_id = inputVector.get(3).value;
				
				return ClusterManager.node_generate_update(session.idConnection, cluster_id);

			case "cluster nodes show_txt":
				
				//Get params
				cluster_id = inputVector.get(3).value;
				
				return ClusterManager.node_show_text(session.idConnection, cluster_id);

			case "app vhost show":
				
				//Get params
				String protocol = inputVector.get(3).value;
				
				return JsonManager.format_Gson(VHostManager.show_all(protocol).toJSONString());

			case "dq algorithm exist":
				
				//Get params
				String key = inputVector.get(3).value;
				
				if (DQManager.exist(key)) return "1";
				else return "0";

			case "dq algorithm get":
				
				//Get params
				key = inputVector.get(3).value;
				
				return DQManager.get_algo(key);

			case "dq algorithm remove":
				
				//Get params
				key = inputVector.get(3).value;
				
				DQManager.remove(key);
				
				return "1";

			case "script delete all":
				
				//Get params
				String startsWithScriptName = inputVector.get(3).value;

				ScriptManager.delete_all(session, env, session.idConnection, startsWithScriptName);
				
				return "1";

			case "sequence generate update":
				
				key = inputVector.get(3).value;
				
				return SequenceManager.generateUpdate(key);

			case "parameter generate update":
				
				key = inputVector.get(3).value;
				
				return ParameterManager.generateUpdate(key);

			case "parameter generate merge":
				
				key = inputVector.get(3).value;
				
				return ParameterManager.generateMerge(key);

			case "job generate update":
				
				String jobId = inputVector.get(3).value;
				
				return JobManager.generateUpdate(session.user, jobId);

			case "script generate url":
				
				String scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateUrl(session, env, scriptName, session.user, parent_pid);

			case "script generate delay":
				
				scriptName = inputVector.get(3).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.generateUpdateDelay(session, scriptName);

			case "script generate execute":
				
				scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateExecute(session, env, scriptName, parent_pid);

			case "script generate stack":
				
				scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateStack(session, env, scriptName, parent_pid);

			case "script generate call":
				
				scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateCall(session, env, scriptName, parent_pid);

			case "script generate sub_include":
				
				scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateSubInclude(session, env, scriptName, parent_pid);

			case "script generate include":
				
				scriptName = inputVector.get(3).value;
				
				return ScriptManager.generateInclude(session, env, scriptName, parent_pid);

			case "user show groups":
				
				String login = inputVector.get(3).value;
				
				return NodeManager.format(GroupManager.userGetGroup(login).toJSONString());

			case "user show scripts":
				
				login = inputVector.get(3).value;
				
				return NodeManager.format(GroupManager.userGetScript(session.idConnection, login).toJSONString());

			case "script show groups":
				
				scriptName = inputVector.get(3).value;
				
				return NodeManager.format(GroupManager.scriptGetGroup(scriptName).toJSONString());

			case "script show users":
				
				scriptName = inputVector.get(3).value;
				
				return NodeManager.format(GroupManager.scriptGetUser(scriptName).toJSONString());

			case "group get user":
				
				String groupName = inputVector.get(3).value;
				
				return NodeManager.format(GroupManager.getUser(groupName).toJSONString());

			case "group get script":

				groupName = inputVector.get(3).value;

				return NodeManager.format(GroupManager.getScript(session.idConnection, groupName).toJSONString());

			case "script generate update":
				
				String id = inputVector.get(3).value;
				
				return ScriptManager.generateUpdate(session, id);
			
			case "script generate merge":
				
				id = inputVector.get(3).value;
				
				return ScriptManager.generateMerge(session, id);
			
			case "script generate create":
				
				id = inputVector.get(3).value;
				
				return ScriptManager.generateCreate(session, id);
			
			case "parameter get value":
				
				//Get key
				key = inputVector.get(3).value;

				return ParameterManager.get_value(key);

			case "sequence get current":
				
				//Get key
				key = inputVector.get(3).value;

				return SequenceManager.get_current(key);

			case "relation show sentence":
				
				relationId = inputVector.get(3).value;
				
				return RelationManager.showSentence(relationId, env, session);

			case "sql block to_json":

				String nbLine = inputVector.get(3).value;

				return SQLManager.block_to_json(session.idConnection, env, nbLine).toJSONString();

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
				key = inputVector.get(3).value;

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
				case "stack flow_json_set":
					
					//Get key
					String pid = inputVector.get(2).value;
					String json = inputVector.get(3).value;
					
					return StackManager.flow_json_set(pid, json);
				
				case "log show_time": case "restricted show_pid":

					String nodename = inputVector.get(2).value;
					pid = inputVector.get(3).value;

					return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+Log.show_time(nodename, pid);

				case "parameter add": case "parameter create": case "parameter insert":
					
					//Get key
					key = inputVector.get(2).value;
					String value = inputVector.get(3).value;
					
					ParameterManager.add(key, value);
					
					return "Parameter added with successful.";
				
				case "parameter merge":
					
					//Get key
					key = inputVector.get(2).value;
					value = inputVector.get(3).value;
					
					ParameterManager.merge(key, value);
					
					return "Parameter merged with successful.";
				
				case "version show_scriptnames":

					String group = inputVector.get(2).value;
					String commit_id = inputVector.get(3).value;
					
					return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.show_commit_scripts(session.idConnection, group, commit_id);
					
				case "version show_scripts":

					group = inputVector.get(2).value;
					commit_id = inputVector.get(3).value;
					
					return ScriptManager.reload_commit_scripts(session.user, session.idConnection, group, commit_id);
					
				case "version commit_group":

					group = inputVector.get(2).value;
					String message = inputVector.get(3).value;
					
					return ScriptManager.commitGroup(session, session.user, session.idConnection, group, message);
					
				case "circle delete":

					String level = inputVector.get(2).value;
					String r_th = inputVector.get(3).value;
					
					CircleManager.deleteCircle(level, r_th);
					
					return "The relation/thought is out of the circle.";
	
				case "circle show":

					level = inputVector.get(2).value;
					r_th = inputVector.get(3).value;
					
					return JsonFormatter.format(CircleManager.showCircle(level, r_th).toJSONString());
	
				case "circle ids":

					level = inputVector.get(2).value;
					r_th = inputVector.get(3).value;
					
					return JsonFormatter.format(CircleManager.getIds(level, r_th).toJSONString());
	
				case "circle exist":

					level = inputVector.get(2).value;
					r_th = inputVector.get(3).value;
					
					return CircleManager.existCircle(level, r_th);
	
				case "thought prob_in_words":
					
					//Get key
					String ths = inputVector.get(2).value;
					String words = inputVector.get(3).value;
					
					return ThoughtManager.probability_in_words(ths, words)+"";

				case "word stimulate":
					
					//Get word
					word = inputVector.get(2).value;
					String lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);

					WordManager.stimulate(word, lang);

					return "Word W["+inputVector.get(2).value+"] stimulated with successful in the language '"+lang+"'.";

				case "word levenshtein":
					
					//Get string
					String symbolChars = inputVector.get(2).value;
					String order = inputVector.get(3).value;
					int iorder = 0;
					
					if (order.toLowerCase().equals("desc")) {
						
						iorder = 2;
						
					} else if (order.toLowerCase().equals("asc")) {
						
						iorder = 1;
						
					} else if (order.toLowerCase().equals("top")) {
						
						iorder = 0;
						
					} else {
						
						throw new Exception("Sorry, the order field is not valid (asc|desc|top).");
						
					}

					return WordManager.searchLevenshteinDistance(symbolChars, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder).toJSONString();

				case "word exist":
					
					//Get key
					key = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					if(WordManager.exist(key, lang)) return "1";
					else return "0";

				case "thought show":
					
					//Get key
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);

					return JsonFormatter.format(ThoughtManager.list(word, lang).toJSONString());

				case "thought first":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return WordManager.firstTabLink(word, lang)+"";

				case "thought last":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return WordManager.lastTabLink(word, lang)+"";

				case "word delete":
					
					//Get key
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
	
					WordManager.deleteLang(word, lang);
	
					return "Word W["+inputVector.get(2).value+"] deleted with successful in the language '"+lang+"'.";
	
				case "word first":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return WordManager.firstTabLink(word, lang)+"";
	
				case "word last":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return WordManager.lastTabLink(word, lang)+"";
	
				case "word show":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(WordManager.showTabLinks(word, lang).toJSONString());
	
				case "word perception":
					
					word = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(WordManager.showTabLinkPerceptions(word, lang).toJSONString());
				
				case "symbol perception":
					
					String symbol = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(SymbolManager.showPerceptionTabLinks(symbol, lang).toJSONString());
				
				case "symbol insert": case "symbol create": case "symbol add": 
					
					//Get key
					symbol = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);

					SymbolManager.create(symbol, lang);

					return "Symbol S["+inputVector.get(2).value+"] created with successful in the language '"+lang+"'.";

				case "symbol first":
					
					symbol = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return SymbolManager.firstTabLink(symbol, lang);
	
				case "symbol last":
					
					symbol = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return SymbolManager.lastTabLink(symbol, lang);
	
				case "symbol show":
					
					symbol = inputVector.get(2).value;
					lang = inputVector.get(3).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(SymbolManager.showTabLinks(symbol, lang, ConcentrationManager.getConcentrationDepth("C[symbol]")).toJSONString());
	
				case "cluster node":

					cluster_id = inputVector.get(2).value;
					String method = inputVector.get(3).value;

					return JsonFormatter.format(ClusterManager.get_node(session.idConnection, cluster_id, method).toJSONString());

				case "cm exist":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					String type = inputVector.get(3).value;
					
					return CMManager.exist_type(key, type);

				case "app delete":
					
					//Get parameters
					type = inputVector.get(2).value;
					String context = inputVector.get(3).value;
	
					return AppManager.delete_context(session, type, context);
	
				case "cm set":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					json = inputVector.get(3).value;
	
					CMManager.set(session.user, key, json);
					
					return "1";

				case "node select":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					String xPath = inputVector.get(3).value;
					
					if (!GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
					}
	
					return NodeManager.select(key, xPath);

				case "node count":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					xPath = inputVector.get(3).value;
					
					if (!GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
					}
	
					return NodeManager.count(key, xPath);

				case "user insert": case "user create": case "user add": 
					
					//Get parameters
					login = inputVector.get(2).value;
					String password = inputVector.get(3).value;
					
					if (!session.user.equals("mentdb") && !session.user.equals("admin") && !GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to create a user, "+session.user+" must be granted with 'sys'.");
					}
					
					return "User "+login+" created with successful ("+UserManager.create(session.idConnection, login, password)+").";

				case "scrud load":
					
					//Get key, name and value
					String cm = inputVector.get(2).value;
					String table = inputVector.get(3).value;
					
					org.json.simple.JSONObject cnx = CMManager.get(session.user, cm);
					String cmtype = (String) cnx.get("subType");
					if (cmtype==null || !AtomFx.find("SQLServer,PostgreSQL,H2,MySQL,Derby,Oracle,HSQL", cmtype, ",").equals("0")) {
						throw new Exception("Sorry, the scrud works only with SQLServer|PostgreSQL|H2|MySQL|Derby|Oracle|HSQL.");
					}
					
					JSONObject res = new JSONObject();
					res.put("cm", cm);
					res.put("ta", table);
					
					return "j23i88m90m76i39t04r09y35p14a96y09e57t11"+res.toJSONString();

				case "node fields":
					
					//Get key, name and value
					key = inputVector.get(2).value;
					String jPath = inputVector.get(3).value;
					
					if (!GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
					}

					return JsonFormatter.format(NodeManager.fields(key, jPath));

				case "sequence add": case "sequence create": case "sequence insert":
					
					//Get key
					key = inputVector.get(2).value;
					value = inputVector.get(3).value;

					SequenceManager.add(key, value);
					
					return "Sequence added with successful.";
				
				case "sequence update":
					
					//Get key
					key = inputVector.get(2).value;
					value = inputVector.get(3).value;
				
					SequenceManager.update(key, value);
					
					return "Sequence updated with successful.";
				
				case "parameter update":
					
					//Get key
					key = inputVector.get(2).value;
					value = inputVector.get(3).value;
				
					ParameterManager.update(key, value);
					
					return "Parameter updated with successful.";
				
				case "parameter lock_if_null":
					
					//Get key
					key = inputVector.get(2).value;
					value = inputVector.get(3).value;
				
					return ParameterManager.lock_if_null(key, value);
				
				case "string get_variable":

					str = inputVector.get(2).value;
					String pattern = inputVector.get(3).value;

					return JsonManager.format_Gson(StringFx.get_variable(str, pattern).toJSONString());

				case "mongodb collection_delete":
					
					//Get parameters
					String collectionId = inputVector.get(2).value;
					String jsonFilter = inputVector.get(3).value;
					
					return ""+MongoDBManager.collection_delete(env, collectionId, jsonFilter);
	
				case "mongodb collection_insert":
					
					//Get parameters
					collectionId = inputVector.get(2).value;
					json = inputVector.get(3).value;
					
					MongoDBManager.collection_insert(env, collectionId, json);
					
					return "1";
	
				case "mongodb connect":
					
					//Get parameters
					String url = inputVector.get(2).value;
					String clientId = inputVector.get(3).value;
					
					MongoDBManager.client_connect(env, url, clientId);
	
					return "1";
	
				case "pa polynomial_curve_fit_get_coeff":
					
					//Get parameters
					String data = inputVector.get(2).value;
					String degree = inputVector.get(3).value;
	
					return PALinearRegression.polynomial_curve_fit_get_coeff(data, degree).toJSONString();
	
				case "pa polynomial_curve_fit_eval":
					
					//Get parameters
					String coeff = inputVector.get(2).value;
					String x = inputVector.get(3).value;
	
					return PALinearRegression.polynomial_curve_fit_eval(coeff, x);
	
				case "pa xy_scatter":
					
					//Get parameters
					json = inputVector.get(2).value;
					String title = inputVector.get(3).value;
	
					return "j23i88m90m76i39t04r09y35p14a96y09e57t43"+PALinearRegression.xy_scatter(json, title);
	
				case "dl4j csv_load_model": 

					String dl4jId = inputVector.get(2).value;
					String json_conf = inputVector.get(3).value;
					
					DL4J_CSV_Manager.load_model(env, dl4jId, json_conf);
					
					return "1";
									
				case "string encode_des":

					data = inputVector.get(2).value;
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
					type = inputVector.get(2).value;
					context = inputVector.get(3).value;
	
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

					value = inputVector.get(2).value;
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

					json = inputVector.get(2).value;
					String minWait = inputVector.get(3).value;

					return OsManager.execute(session.user, json, minWait);

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

				case "file writer_add_bytes": case "restricted file_writer_add_bytes":

					String writerId = inputVector.get(2).value;
					String strBytes = inputVector.get(3).value;
					
					return FileFx.writer_add_bytes(env, writerId, strBytes);

				case "file writer_add_line":

					writerId = inputVector.get(2).value;
					str = inputVector.get(3).value;

					return FileFx.writer_add_line(env, writerId, str);

				case "file reader_get_bytes": case "restricted file_reader_get_bytes":

					String readerId = inputVector.get(2).value;
					String nbBytes = inputVector.get(3).value;

					return FileFx.reader_get_bytes(env, readerId, nbBytes);

				case "file b64_write":

					data = inputVector.get(2).value;
					filePath = inputVector.get(3).value;

					return FileFx.b64_write(session.user, data, filePath);

				case "file copy_dir":

					String oldDirPath = inputVector.get(2).value;
					String newDirPath = inputVector.get(3).value;

					return FileFx.copy_dir(session.user, oldDirPath, newDirPath);

				case "file copy_file":

					String oldFilePath = inputVector.get(2).value;
					String newFilePath = inputVector.get(3).value;

					return FileFx.copy_file(session.user, oldFilePath, newFilePath);

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
					String bool = inputVector.get(3).value;

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
					xPath = inputVector.get(3).value;

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

					return FileFx.create(session.user, filePath, data);

				case "file append":

					filePath = inputVector.get(2).value;
					data = inputVector.get(3).value;

					return FileFx.append(session.user, filePath, data);

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
					jPath = inputVector.get(3).value;

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

						return CommandFullAccess.concatOrUnknow(inputVector);

					}

				}

			}

		}

	}

}