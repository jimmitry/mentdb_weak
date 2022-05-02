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
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.core.entity.word.WordManager;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.dl.BayesianNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl4j.DL4J_CSV_Manager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.remote.FtpsManager;
import re.jpayet.mentdb.ext.remote.SftpManager;
import re.jpayet.mentdb.ext.remote.SshManager;
import re.jpayet.mentdb.ext.rest.REST;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stripe.StripeManager;
import re.jpayet.mentdb.ext.user.GroupManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

//Command full access
public class CommandFullAccess6 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
		default :

			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
			case "thought delete by word":
				
				//Get key
				String word = inputVector.get(4).value;
				String thoughtId = inputVector.get(5).value;

				ThoughtManager.deleteWord(thoughtId, word);

				return "Thought "+thoughtId+" deleted with successful for the word '"+word+"'.";

			case "thought delete by lang":
				
				//Get key
				String lang = inputVector.get(4).value;
				thoughtId = inputVector.get(5).value;
				
				LanguageManager.exceptionIfNotExist(lang);

				ThoughtManager.deleteLang(thoughtId, lang);

				return "Thought "+thoughtId+" deleted with successful in the language '"+lang+"'.";

			case "group is granted user":
				
				String login = inputVector.get(4).value;
				String groupName = inputVector.get(5).value;
				
				if (GroupManager.isGrantedUser(groupName, login)) return "1";
				return "0";

			case "group is granted script":
				
				String scriptId = inputVector.get(4).value;
				groupName = inputVector.get(5).value;
				
				if (GroupManager.isGrantedScript(groupName, scriptId)) return "1";
				else return "0";

			default:

				switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
				case "cluster node expels":
					
					//Get params
					String cluster_id = inputVector.get(3).value;
					String node_id = inputVector.get(4).value;
					String error = inputVector.get(5).value;
					
					ClusterManager.node_expels(cluster_id, node_id, error);
					
					return "1";

				case "app vhost exist":
					
					//Get params
					String protocol = inputVector.get(3).value;
					String context = inputVector.get(4).value;
					String hostname = inputVector.get(5).value;
					
					if (VHostManager.exist(protocol, hostname, context)) return "1";
					else return "0";

				case "app vhost add":
					
					//Get params
					protocol = inputVector.get(3).value;
					context = inputVector.get(4).value;
					hostname = inputVector.get(5).value;
					
					VHostManager.add(protocol, hostname, context);
					
					return "1";
					
				case "app vhost delete":
					
					//Get params
					protocol = inputVector.get(3).value;
					context = inputVector.get(4).value;
					hostname = inputVector.get(5).value;
					
					VHostManager.remove(protocol, hostname, context);
					
					return "1";

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

				case "sql show activity": case "restricted show activity":

					String grouptype = inputVector.get(3).value;
					String dtMin = inputVector.get(4).value;
					String dtMax = inputVector.get(5).value;

					return "j23i88m90m76i39t04r09y35p14a96y09e57t42"+Log.activity(dtMin, dtMax, grouptype);

				case "sql show data":

					String cmId = inputVector.get(3).value;
					String query = inputVector.get(4).value;
					String title = inputVector.get(5).value;

					return DQManager.db_show_data(session, cmId, query, null, title).toJSONString();

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

				case "sql block open":

					sqlId = inputVector.get(3).value;
					tableName = inputVector.get(4).value;
					selectQuery = inputVector.get(5).value;
					
					SQLManager.block_open(session.idConnection, env, sqlId, tableName, selectQuery);

					return "1";

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
					case "sql select":

						cmId = inputVector.get(2).value;
						query = inputVector.get(3).value;
						String size = inputVector.get(4).value;
						title = inputVector.get(5).value;
						
						return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.db_show_data(session, cmId, query, size, title).toJSONString();

					case "stripe session":
						
						//Get key
						String jsonItems = inputVector.get(2).value;
						String successUrl = inputVector.get(3).value;
						String cancelUrl = inputVector.get(4).value;
						String secretKey = inputVector.get(5).value;
						
						return StripeManager.get_paiement_session(jsonItems, successUrl, cancelUrl, secretKey);
					
					case "relation insert": case "relation create": case "relation add":
						
						//Get thoughts
						String thoughts = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						context = inputVector.get(4).value;
						String mql = inputVector.get(5).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return RelationManager.create(thoughts, lang, context, mql);
		
					case "word search":
						
						//Get string
						String symbolChars = inputVector.get(2).value;
						String regex = inputVector.get(3).value;
						String order = inputVector.get(4).value;
						lang = inputVector.get(5).value;
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

						return WordManager.search(symbolChars, regex, ConcentrationManager.getConcentrationDepth("C[symbol]"), iorder, lang).toJSONString();

					case "word insert": case "word create": case "word add": 
						
						//Get key
						key = inputVector.get(2).value;
						lang = inputVector.get(3).value;
						String separator = inputVector.get(4).value;
						String lock_translation = inputVector.get(5).value;
						
						LanguageManager.exceptionIfNotExist(lang);
						
						return WordManager.create(key, separator, lang, lock_translation, session, env);
	
					case "thought insert": case "thought create":  case "thought add": 
						
						//Get key
						word = inputVector.get(2).value;
						separator = inputVector.get(3).value;
						lang = inputVector.get(4).value;
						lock_translation = inputVector.get(5).value;
						
						LanguageManager.exceptionIfNotExist(lang);
	
						return ThoughtManager.create(word, separator, lang, lock_translation, session, env);
	
					case "file_watcher start": 
						
						//Get key
						key = inputVector.get(2).value;
						String user = inputVector.get(3).value;
						String directory = inputVector.get(4).value;
						String scriptName = inputVector.get(5).value;
						
						FileFx.start_watch_service(session, key, user, directory, scriptName);
						
						return "1";
	
					case "app create":
						
						//Get parameters
						String type = inputVector.get(2).value;
						context = inputVector.get(3).value;
						String template = inputVector.get(4).value;
						String version = inputVector.get(5).value;
		
						return AppManager.create_context(session, type, context, template, version);
		
					case "dq analyse":
						
						//Get key, name and value
						cmId = inputVector.get(2).value;
						json = inputVector.get(3).value;
						title = inputVector.get(4).value;
						query = inputVector.get(5).value;
						
						return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.analyse(env, session, cmId, json, title, query, parent_pid, current_pid).toJSONString();
		
					case "job add": case "job create": case "job insert":
						
						//Get parameters
						String jobId = inputVector.get(2).value;
						scriptName = inputVector.get(3).value;
						String pattern = inputVector.get(4).value;
						String activate = inputVector.get(5).value;
	
						JobManager.add(jobId, scriptName, pattern, activate, session.user);
						
						return "Job added with successful.";
					
					case "job update":
						
						//Get parameters
						jobId = inputVector.get(2).value;
						scriptName = inputVector.get(3).value;
						pattern = inputVector.get(4).value;
						activate = inputVector.get(5).value;
	
						JobManager.update(session.user, jobId, scriptName, pattern, activate);
						
						return "Job updated with successful.";
					
					case "node iarray":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String xPath = inputVector.get(3).value;
						String value = inputVector.get(4).value;
						type = inputVector.get(5).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
			
						NodeManager.ainsert(key, xPath, value, type);
			
						return "Inserted with successful.";
			
					case "pa polynomial_curve_fit_eval_incr":
						
						//Get parameters
						String coeff = inputVector.get(2).value;
						String x_min = inputVector.get(3).value;
						String x_max = inputVector.get(4).value;
						String x_incr = inputVector.get(5).value;
		
						return PALinearRegression.polynomial_curve_fit_eval_incr(coeff, x_min, x_max, x_incr).toJSONString();
		
					case "rest https_json_post":

						//Get parameters
						String url = inputVector.get(2).value;
						String jsonHeaders = inputVector.get(3).value;
						String jsonCookies = inputVector.get(4).value;
						String jsonData = inputVector.get(5).value;
						
						String s = REST.https_json_post_exe(url, jsonHeaders, jsonCookies, jsonData);

						try {
							return JsonManager.format_Gson(s);
						} catch (Exception e) {
							return s;
						}

					case "rest http_json_post":

						//Get parameters
						url = inputVector.get(2).value;
						jsonHeaders = inputVector.get(3).value;
						jsonCookies = inputVector.get(4).value;
						jsonData = inputVector.get(5).value;
						
						s = REST.http_json_post_exe(url, jsonHeaders, jsonCookies, jsonData);

						try {
							return JsonManager.format_Gson(s);
						} catch (Exception e) {
							return s;
						}

					case "dl4j csv_predict": 
						
						String dl4jId = inputVector.get(2).value;
						String json_conf = inputVector.get(3).value;
						String csv_to_test = inputVector.get(4).value;
						String text_batch_size = inputVector.get(5).value;
						
						return JsonManager.format_Gson(DL4J_CSV_Manager.predict_row(env, dl4jId, json_conf, csv_to_test, text_batch_size));
										
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
		
					case "file reader_open": case "restricted file_reader_open":

						String fileReaderId = inputVector.get(2).value;
						String filePath = inputVector.get(3).value;
						
						if (!GroupManager.isGrantedUser("api-mql", session.user) && (!filePath.startsWith("tmp/"+session.user+"/") || filePath.indexOf("..")>-1)) {
							throw new Exception("Sorry, you are in a restricted MQL session. Only files in 'tmp/"+session.user+"' are allowed.");
						}
						
						type = inputVector.get(4).value;
						String encoding = inputVector.get(5).value;

						return FileFx.reader_open(session.user, env, fileReaderId, filePath, type, encoding);

					case "log write":

						String msg = inputVector.get(2).value;
						String status = inputVector.get(3).value;
						String c_key = inputVector.get(4).value;
						String c_val = inputVector.get(5).value;

						Log.write(session.current_scriptname, msg, status, c_key, c_val, parent_pid);

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

						return FileFx.copy_format(session.user, source, sourceEncoding, target, targetEncoding);

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
						xPath = inputVector.get(3).value;
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
						value = inputVector.get(4).value;
						type = inputVector.get(5).value;

						return JsonManager.ainsert(env, key, xPath, value, type);

					case "math decimal_format":

						String number = inputVector.get(2).value;
						pattern = inputVector.get(3).value;
						String decimalSeparator = inputVector.get(4).value;
						String groupingSeparator = inputVector.get(5).value;

						return MathFx.decimal_format( number, pattern, decimalSeparator, groupingSeparator);

					default:

						switch (inputVector.get(0).value) {
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