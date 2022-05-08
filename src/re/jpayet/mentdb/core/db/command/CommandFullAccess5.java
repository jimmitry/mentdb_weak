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
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.circle.CircleManager;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.relation.SearchEngine;
import re.jpayet.mentdb.core.entity.symbol.SymbolManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.dl.BayesianNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.CSVNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.TextCatManager;
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
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.pdf.PDFManager;
import re.jpayet.mentdb.ext.remote.CifsManager;
import re.jpayet.mentdb.ext.remote.FtpManager;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.rest.REST_DOCManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.stat.Statistic;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.user.UserManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess5 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
		case "metric current used swap mem":

			return Statistic.getUsedSwapSpaceSize()+"";

		case "metric current free swap mem":

			return Statistic.getFreeSwapSpaceSize()+"";

		default:

			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
			case "relation show thought nodes":
				
				String relationId = inputVector.get(4).value;
				
				return NodeManager.format(RelationManager.showThoughtNodes(relationId).toJSONString());
			
			case "is null or empty":

				//Get parameters
				String str = inputVector.get(4).value;

				return OperatorFx.is_null_or_empty(str);

			default:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {

				
				case "job set cluster_script":
					
					//Get parameters
					String jobId = inputVector.get(3).value;
					String cluster_script = inputVector.get(4).value;
					
					JobManager.set_cluster_script(session.user, jobId, cluster_script);
					
					return "Job updated with successful.";
				case "concentration set depth":
					
					String key = inputVector.get(3).value;
					int depth = 0;
				
					//Check if the depth is valid
					try {
				
						depth = Integer.parseInt(inputVector.get(4).value);
				
						if (depth<=0) {
				
							throw new Exception("err");
				
						}
				
					} catch (Exception e) {
				
						throw new Exception("Sorry, the depth value is not valid.");
				
					}
					
					ConcentrationManager.setConcentrationDepth(key, depth);
					
					return "Concentration depth saved with successful.";
				
				case "thought show words":
					
					String thoughtId = inputVector.get(3).value;
					String lang = inputVector.get(4).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return NodeManager.format(ThoughtManager.getWords(thoughtId, lang, new Vector<MQLValue>()).toJSONString());
				
				case "symbol show words":
					
					String symbol = inputVector.get(3).value;
					lang = inputVector.get(4).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(SymbolManager.showWords(symbol, lang));

				case "cluster signal delete":
					
					//Get params
					String cluster_id = inputVector.get(3).value;
					String node_id = inputVector.get(4).value;
					
					ClusterManager.signal_delete(session.idConnection, cluster_id, node_id);
					
					return "Signal deleted with successful.";

				case "cluster node delete":
					
					//Get params
					cluster_id = inputVector.get(3).value;
					node_id = inputVector.get(4).value;
					
					ClusterManager.node_remove(session.idConnection, cluster_id, node_id);
					
					return "Node deleted with successful.";

				case "cluster node reinstate":
					
					//Get params
					cluster_id = inputVector.get(3).value;
					node_id = inputVector.get(4).value;
					
					ClusterManager.node_reinstate(session.idConnection, cluster_id, node_id);
					
					return "1";

				case "app vhost show":
					
					//Get params
					String protocol = inputVector.get(3).value;
					String context = inputVector.get(4).value;
					
					return JsonManager.format_Gson(VHostManager.show(protocol, context).toJSONString());

				case "dq algorithm set":
					
					//Get params
					key = inputVector.get(3).value;
					String algo = inputVector.get(4).value;
					
					DQManager.set(key, algo);
					
					return "1";

				case "user check password": 
					
					//Get parameters
					String login = inputVector.get(3).value;
					String password = inputVector.get(4).value;
					
					if (UserManager.checkPassword(login, password)) return "1";
					else return "0";

				case "script api doc": 
					
					//Get parameters
					login = inputVector.get(3).value;
					password = inputVector.get(4).value;
					
					return REST_DOCManager.private_api_doc(env, login, password);

				case "group grant user":
					
					login = inputVector.get(3).value;
					String groupName = inputVector.get(4).value;
					
					GroupManager.grantUser(session.idConnection, groupName, login);
	
					return "User granted with successful.";
	
				case "group grant script":
					
					String scriptId = inputVector.get(3).value;
					groupName = inputVector.get(4).value;
					
					GroupManager.grantScript(session.idConnection, groupName, scriptId, false);
	
					return "Script granted with successful.";
	
				case "group ungrant user":
					
					login = inputVector.get(3).value;
					groupName = inputVector.get(4).value;
					
					GroupManager.ungrantUser(session.idConnection, groupName, login);
	
					return "User ungranted with successful.";
	
				case "group grant_all script":
					
					scriptId = inputVector.get(3).value;
					groupName = inputVector.get(4).value;
					
					GroupManager.grantAllScript(session.idConnection, groupName, scriptId, false);
	
					return "Scripts granted with successful.";
	
				case "group ungrant_all script":
					
					scriptId = inputVector.get(3).value;
					groupName = inputVector.get(4).value;
					
					GroupManager.ungrantAllScript(session.idConnection, groupName, scriptId);
	
					return "Scripts ungranted with successful.";
	
				case "group ungrant script":
					
					scriptId = inputVector.get(3).value;
					groupName = inputVector.get(4).value;
					
					GroupManager.ungrantScript(session.idConnection, groupName, scriptId);
	
					return "Script ungranted with successful.";
	
				case "script is granted":

					String scriptName = inputVector.get(3).value;
					login = inputVector.get(4).value;
					
					if (ScriptManager.isGrantedToUser(login, scriptName)) return "1";
					else return "0";
					
				case "node is object":
						
					//Get key, name and value
					key = inputVector.get(3).value;
					String xPath = inputVector.get(4).value;
					
					if (!GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
					}
	
					return NodeManager.is_obj(key, xPath);

				case "node is array":
						
					//Get key, name and value
					key = inputVector.get(3).value;
					xPath = inputVector.get(4).value;
					
					if (!GroupManager.isGrantedUser("sys", session.user)) {
						throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
					}
	
					return NodeManager.is_array(key, xPath);
	
				case "user set password":
					
					//Get user info
					String user = inputVector.get(3).value;
					String pwd = inputVector.get(4).value;
					
					GroupManager.generateErrorIfNotGranted(session.idConnection, "sys", "User");
					
					UserManager.updatePassword(session.idConnection, user, pwd);
					
					return "Password updated with successful.";
				
				case "script copy all":
					
					//Get params
					String startsWithScriptName = inputVector.get(3).value;
					String replacement = inputVector.get(4).value;
					
					ScriptManager.copy_all(session, env, session.idConnection, startsWithScriptName, replacement, parent_pid);
					
					return "1";

				case "script rename all":
					
					//Get params
					startsWithScriptName = inputVector.get(3).value;
					replacement = inputVector.get(4).value;
					
					ScriptManager.rename_all(session, env, session.idConnection, startsWithScriptName, replacement, parent_pid);
					
					return "1";

				case "word perception thought":
					
					String word = inputVector.get(3).value;
					lang = inputVector.get(4).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(WordManager.showThoughtPerceptions(word, lang).toJSONString());
				
				case "word perception symbol":
					
					word = inputVector.get(3).value;
					lang = inputVector.get(4).value;
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return JsonFormatter.format(WordManager.showSymbolPerception(word, lang).toJSONString());
				
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
					key = inputVector.get(3).value;
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
					xPath = inputVector.get(4).value;

					return JsonManager.is_obj(env, key, xPath);

				case "json is array":

					//Get key, name and value
					key = inputVector.get(3).value;
					xPath = inputVector.get(4).value;

					return JsonManager.is_array(env, key, xPath);

				default:

					switch (inputVector.get(0).value+" "+inputVector.get(1).value) {
					case "cifs ls":

						//Get parameters
						String smbUrl = inputVector.get(2).value;
						String filter = inputVector.get(3).value;
						json = inputVector.get(4).value;

						return JsonManager.format_Gson(CifsManager.ls(env, smbUrl, filter, json));

					case "stack flow_init":
						
						//Get key
						String pid = inputVector.get(2).value;
						String nameFlow = inputVector.get(3).value;
						json = inputVector.get(4).value;
						
						return StackManager.flow_init(pid, nameFlow, json);
					
					case "stack flow_step":
						
						//Get key
						pid = inputVector.get(2).value;
						String pos = inputVector.get(3).value;
						String posname = inputVector.get(4).value;
						
						return StackManager.flow_step(pid, pos, posname);
					
					case "parameter add": case "parameter create": case "parameter insert":
						
						//Get key
						key = inputVector.get(2).value;
						String value = inputVector.get(3).value;
						@SuppressWarnings("unused") String locked = inputVector.get(4).value;
						
						ParameterManager.add(key, value);
						
						return "Parameter added with successful.";
					
					case "parameter merge":
						
						//Get key
						key = inputVector.get(2).value;
						value = inputVector.get(3).value;
						locked = inputVector.get(4).value;
						
						ParameterManager.merge(key, value);
						
						return "Parameter merged with successful.";
					
					case "version commit_script":

						String group = inputVector.get(2).value;
						String filterScript = inputVector.get(3).value;
						String message = inputVector.get(4).value;
						
						return ScriptManager.commitScript(session, session.user, session.idConnection, group, filterScript, message);
						
					case "version show_commits":

						group = inputVector.get(2).value;
						filter = inputVector.get(3).value;
						String nb_row = inputVector.get(4).value;
						
						return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+ScriptManager.show_all_commits(session.user, session.idConnection, group, filter, nb_row);
						
					case "version show_script":

						group = inputVector.get(2).value;
						String commit_id = inputVector.get(3).value;
						String scriptname = inputVector.get(4).value;
						
						return ScriptManager.show_mql(session.user, session.idConnection, group, commit_id, scriptname);
						
					case "circle ids":

						String level = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						String r_th = inputVector.get(4).value;
						
						return JsonFormatter.format(CircleManager.getIds(level, lang, r_th).toJSONString());
		
					case "circle id":

						level = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						r_th = inputVector.get(4).value;
						
						return CircleManager.getId(level, lang, r_th);
		
					case "circle contains":

						level = inputVector.get(2).value;
						String r_th_1 = inputVector.get(3).value;
						String r_th_2 = inputVector.get(4).value;
						
						if (CircleManager.contains(level, r_th_1, r_th_2)) {
							
							return "1";
							
						} else {
							
							return "0";
							
						}

					case "circle merge": 

						level = inputVector.get(2).value;
						String th_1 = inputVector.get(3).value;
						String th_2 = inputVector.get(4).value;
						
						return CircleManager.mergeCircle(level, th_1, th_2, env, session);
					
					case "relation search":
						
						//Get key
						String text = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						String searchPunctuation = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						if (searchPunctuation.equals("1")) 
							return JsonFormatter.format(SearchEngine.search(text, lang, true, session, env).toJSONString());
						else 
							return JsonFormatter.format(SearchEngine.search(text, lang, false, session, env).toJSONString());
		
					case "thought merge":
						
						//Get keys
						level = inputVector.get(2).value;
						String thoughtId1 = inputVector.get(3).value;
						String thoughtId2 = inputVector.get(4).value;
						
						ThoughtManager.merge(level, thoughtId1, thoughtId2, session, env);
	
						return "Thoughts merged with successful.";
	
					case "thought get":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(4).value;
						int positionInt = 0;
						
						LanguageManager.exceptionIfNotExist(lang);

						//Check if the file id is valid
						try {

							positionInt = Integer.parseInt(inputVector.get(3).value);

							if (positionInt<0) {

								throw new Exception("err");

							}

						} catch (Exception e) {

							throw new Exception("Sorry, the position is not valid.");

						}
						
						return WordManager.getTabLink(word, positionInt, lang);

					case "word levenshtein":
						
						//Get string
						String symbolChars = inputVector.get(2).value;
						String order = inputVector.get(3).value;
						lang = inputVector.get(4).value;
						int iorder = 0;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						if (order.toLowerCase().equals("desc")) {
							
							iorder = 2;
							
						} else if (order.toLowerCase().equals("asc")) {
							
							iorder = 1;
							
						} else if (order.toLowerCase().equals("top")) {
							
							iorder = 0;
							
						} else {
							
							throw new Exception("Sorry, the order field is not valid (asc|desc|top).");
							
						}
	
						return WordManager.searchLevenshteinDistance(symbolChars, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder, lang).toJSONString();
	
					case "word get":
						
						word = inputVector.get(2).value;
						lang = inputVector.get(4).value;
						int position = 0;
						
						LanguageManager.exceptionIfNotExist(lang);
		
						//Check if the file id is valid
						try {
		
							position = Integer.parseInt(inputVector.get(3).value);
		
							if (position<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the position is not valid.");
		
						}
						
						return WordManager.getTabLink(word, position, lang);
		
					case "word insert": case "word create": case "word add": 
						
						//Get key
						key = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						String lock_translation = inputVector.get(4).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.create(key, "", lang, lock_translation, session, env);
	
					case "symbol get":
						
						symbol = inputVector.get(2).value;
						lang = inputVector.get(4).value;
						position = 0;
						
						LanguageManager.exceptionIfNotExist(lang);
		
						//Check if the file id is valid
						try {
		
							position = Integer.parseInt(inputVector.get(3).value);
		
							if (position<0) {
		
								throw new Exception("err");
		
							}
		
						} catch (Exception e) {
		
							throw new Exception("Sorry, the position is not valid.");
		
						}
						
						return SymbolManager.getTabLink(symbol, position, lang);
		
					case "script copy":
						
						//Get params
						String oldScriptName = inputVector.get(2).value;
						String method = inputVector.get(3).value;
						String newScriptName = inputVector.get(4).value;
		
						ScriptManager.copy(session, env, session.idConnection, oldScriptName, method, newScriptName, parent_pid);
						
						return "1";
	
					case "script rename":
						
						//Get params
						oldScriptName = inputVector.get(2).value;
						method = inputVector.get(3).value;
						newScriptName = inputVector.get(4).value;
		
						ScriptManager.rename(session, env, session.idConnection, oldScriptName, method, newScriptName, parent_pid);
						
						return "1";
	
					case "node dobject":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						String fieldName = inputVector.get(4).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
		
						NodeManager.odelete(key, xPath, fieldName);
		
						return "Deleted with successful.";
		
					case "node darray":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						String i = inputVector.get(4).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
		
						NodeManager.adelete(key, xPath, i);
		
						return "Deleted with successful.";
		
					case "mongodb collection_update":
						
						//Get parameters
						String collectionId = inputVector.get(2).value;
						String jsonTarget = inputVector.get(3).value;
						String jsonAction = inputVector.get(4).value;
						
						return ""+MongoDBManager.collection_update(env, collectionId, jsonTarget, jsonAction);
		
					case "mongodb database_load":
						
						//Get parameters
						String clientId = inputVector.get(2).value;
						String databaseId = inputVector.get(3).value;
						String databaseName = inputVector.get(4).value;
						
						MongoDBManager.database_load(env, clientId, databaseId, databaseName);
						
						return "1";
		
					case "mongodb collection_load":
						
						//Get parameters
						databaseId = inputVector.get(2).value;
						collectionId = inputVector.get(3).value;
						String collectionName = inputVector.get(4).value;
						
						MongoDBManager.collection_load(env, databaseId, collectionId, collectionName);
						
						return "1";
		
					case "math base_to_base":

						str = inputVector.get(2).value;
						String fromBase = inputVector.get(3).value;
						String toBase = inputVector.get(4).value;

						return MathFx.convertFromBaseToBase(str, fromBase, toBase);

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
						lang = inputVector.get(4).value;
						
						BotManager.create_bot(botName, is_male, lang);
		
						return "1";
		
					case "bot execute":

						//Get parameters
						botName = inputVector.get(2).value;
						user = inputVector.get(3).value;
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
						
						return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.db_show_data(session, cmId, query, null, title).toJSONString();

					case "file image_resize":

						filePath = inputVector.get(2).value;
						String width = inputVector.get(3).value;
						String height = inputVector.get(4).value;
						
						return FileFx.resize_image(filePath, width, height);

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

						return FileFx.ini(session.user, path, section, field);

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
						text = inputVector.get(4).value;

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
						fieldName = inputVector.get(4).value;

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
						replacement = inputVector.get(4).value;

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

						value = inputVector.get(2).value;
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

							return CommandFullAccess.concatOrUnknow(inputVector);

						}

					}

				}

			}

		}

	}

}