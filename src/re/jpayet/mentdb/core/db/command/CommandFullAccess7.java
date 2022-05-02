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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.SearchEngine;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.ext.azure.AzureManager;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;
import re.jpayet.mentdb.ext.dl.TextCatManager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mail.Pop3Manager;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.rest.REST;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.soap.SOAPManager;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;

//Command full access
public class CommandFullAccess7 {

	//Execute the command
	@SuppressWarnings("unchecked")
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
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
				case "math bin package_3d":

					String nb_max_package = inputVector.get(3).value;
					String timeout_to_stop = inputVector.get(4).value;
					String jsonContainers = inputVector.get(5).value;
					String jsonElements = inputVector.get(6).value;

					return JsonManager.format_Gson(MathFx.bin_package_3d(nb_max_package, timeout_to_stop, jsonContainers, jsonElements).toJSONString());

				case "cluster signal give":

					String cluster_id = inputVector.get(3).value;
					String node_id = inputVector.get(4).value;
					String signal = inputVector.get(5).value;
					String current_time = inputVector.get(6).value;

					return ClusterManager.signal_give(cluster_id, node_id, signal, current_time);

				case "script set delay":
					
					String scriptName = inputVector.get(3).value;
					String delayValue = inputVector.get(4).value;
					String delayType = inputVector.get(5).value;
					String delayCondition = inputVector.get(6).value;
					
					ScriptManager.set_delay(session, env, scriptName, delayCondition, delayValue, delayType);
					
					return "1";
	
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
					case "maintenance set": 
						
						if (!session.user.equals("admin")) {
							throw new Exception("Sorry, only admin can change the status maintenances.");
						}
						
						String m_mql = inputVector.get(2).value;
						String m_ws = inputVector.get(3).value;
						String m_web = inputVector.get(4).value;
						String m_job = inputVector.get(5).value;
						String m_stack = inputVector.get(6).value;

						if (m_mql!=null && m_mql.equals("1")) Start.maintenance_mql = true; else Start.maintenance_mql = false;
						if (m_ws!=null && m_ws.equals("1")) Start.maintenance_ws = true; else Start.maintenance_ws = false;
						if (m_web!=null && m_web.equals("1")) Start.maintenance_web = true; else Start.maintenance_web = false;
						if (m_job!=null && m_job.equals("1")) Start.maintenance_job = true; else Start.maintenance_job = false;
						if (m_stack!=null && m_stack.equals("1")) Start.maintenance_stack = true; else Start.maintenance_stack = false;
						
						Misc.create("data/maintenance.txt", "[MAINTENANCE]\n"
								+ "MQL=" + m_mql + "\n"
								+ "WS=" + m_ws + "\n"
								+ "WEB=" + m_web + "\n"
								+ "JOB=" + m_job + "\n"
								+ "STACK=" + m_stack + "\n");

						JSONObject maintenance = new JSONObject();
						maintenance.put("mql", Start.maintenance_mql);
						maintenance.put("ws", Start.maintenance_ws);
						maintenance.put("web", Start.maintenance_web);
						maintenance.put("job", Start.maintenance_job);
						maintenance.put("stack", Start.maintenance_stack);
						
						return "j23i88m90m76i39t04r09y35p14a96y09e57t48"+maintenance.toJSONString();

					case "relation execute": 

						//Get key
						String text = inputVector.get(2).value;
						String context_obj = inputVector.get(3).value;
						String context_size = inputVector.get(4).value;
						lang = inputVector.get(5).value;
						String searchPunctuation = inputVector.get(6).value;
						
						JSONParser jp = new JSONParser();
						JSONObject context = (JSONObject) jp.parse(context_obj);
						
						LanguageManager.exceptionIfNotExist(lang);
						
						JSONArray l_result = null;
						if (searchPunctuation.equals("1")) 
							l_result = SearchEngine.execute(context, text, lang, true, session, env);
						else 
							l_result = SearchEngine.execute(context, text, lang, false, session, env);
						
						if (l_result.size()==0) {
							return null;
						} else {
							
							//-> "[INTENT]" [CURRENT_INTENT];
							//-> "[PATTERN]" (json select "row" /intent);
							//json load "VARS" (string get_variable [INTENT] [PATTERN]);
							
							String r = AtomFx.get((String) l_result.get(0), "1", " ");
							
							JSONObject rec = Record2.getNode(r);
							String mql = (String) rec.get("mql");
							String ct = (String) rec.get("ct");
							String[] rlth = ((String) rec.get("rlth")).split(" ");
							String pattern = "";
							Random ran = new Random();
							boolean has_variable = false;
							for(int ith = 0;ith<rlth.length;ith++) {
								
								JSONArray ws = ThoughtManager.getWords(rlth[ith], lang, new Vector<MQLValue>());
								String str = ((String) ((JSONObject) ws.get(ran.nextInt(ws.size()))).get("word"));
								str = str.substring(2, str.length()-1);
								if (str.equals("[1]")) {
									has_variable = true;
								}
								pattern+= " "+str;
								
							}
							pattern = pattern.substring(1);
							
							if (has_variable) {
								env.varEnv.put("[INTENT]", new StringBuilder(text));
								env.varEnv.put("[PATTERN]", new StringBuilder(pattern));
								env.jsonObj.put("VARS", StringFx.get_variable(text, pattern));
							}
							
							HashMap<Integer, String> rrr = new HashMap<Integer, String>();
							
							for(Object o2 : context.keySet()) {
								
								String k = (String) o2;
								int cur = Integer.parseInt(""+context.get(k));
								
								rrr.put(cur, k);
								
							}
							
							ArrayList<String> rr = new ArrayList<String>();
							for(int i = 1;i<=rrr.size();i++) {
								
								rr.add(rrr.get(i));
								
							}
							
							if (rr.size()==0) {
								rr.add(0, ct);
							} else if (!rr.get(0).equals(ct)) {
								rr.add(0, ct);
							}
							
							JSONObject new_context = new JSONObject();
							for(int i = 0;i<rr.size();i++) {
								if (rr.size()<Integer.parseInt(context_size) && !new_context.containsKey(rr.get(i))) {
									new_context.put(rr.get(i), i+1);
								}
							}
							
							JSONObject o_r = new JSONObject();
							o_r.put("msg", Statement.eval(session, mql, env, parent_pid, current_pid));
							o_r.put("ct_obj", new_context);
							o_r.put("search", l_result);
							
							return JsonFormatter.format(o_r.toJSONString());
							
						}
						
					case "dq generate":
						
						//Get key, name and value
						String cmId = inputVector.get(2).value;
						String tablename = inputVector.get(3).value;
						String fieldname = inputVector.get(4).value;
						String jsonArrayAlgoId = inputVector.get(5).value;
						String sql = inputVector.get(6).value;
						
						return DQManager.generate(session, cmId, tablename, fieldname, jsonArrayAlgoId, sql);
		
					case "node uarray":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						String xPath = inputVector.get(3).value;
						String index = inputVector.get(4).value;
						String value = inputVector.get(5).value;
						String type = inputVector.get(6).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
						
						NodeManager.aupdate(key, xPath, index, value, type);
		
						return "Updated with successful.";
		
					case "node uobject":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						String fieldName = inputVector.get(4).value;
						value = inputVector.get(5).value;
						type = inputVector.get(6).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
		
						NodeManager.oupdate(key, xPath, fieldName, value, type);
		
						return "Updated with successful.";
		
					case "node iobject":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						fieldName = inputVector.get(4).value;
						value = inputVector.get(5).value;
						type = inputVector.get(6).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
		
						NodeManager.oinsert(key, xPath, fieldName, value, type);
		
						return "Inserted with successful.";
		
					case "node iarray":
						
						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						index = inputVector.get(4).value;
						value = inputVector.get(5).value;
						type = inputVector.get(6).value;
						
						if (!GroupManager.isGrantedUser("sys", session.user)) {
							throw new Exception("Sorry, to use node manager, "+session.user+" must be granted with 'sys'.");
						}
			
						NodeManager.ainsert(key, xPath, index, value, type);
						
						return "Inserted with successful.";
			
					case "azure get_token":

						String email = inputVector.get(2).value;
						String password = inputVector.get(3).value;
						String clientId = inputVector.get(4).value;
						String tenant = inputVector.get(5).value;
						String scope = inputVector.get(6).value;

						return AzureManager.getTokenAzure(email, password, clientId, tenant, scope);

					case "rest http":

						String method = inputVector.get(2).value;
						String beginUrl = inputVector.get(3).value;
						String endUrl = inputVector.get(4).value;
						String header = inputVector.get(5).value;
						String jsonCoolied = inputVector.get(6).value;

						String s = REST.http_exe(method, beginUrl, endUrl, header, jsonCoolied);

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
						jsonCoolied = inputVector.get(6).value;

						s = REST.https_exe(method, beginUrl, endUrl, header, jsonCoolied);

						try {
							return JsonManager.format_Gson(s);
						} catch (Exception e) {
							return s;
						}

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

					case "file writer_open": case "restricted file_writer_open":

						writerId = inputVector.get(2).value;
						String filePath = inputVector.get(3).value;
						
						if (!GroupManager.isGrantedUser("api-mql", session.user) && (!filePath.startsWith("tmp/"+session.user+"/") || filePath.indexOf("..")>-1)) {
							throw new Exception("Sorry, you are in a restricted MQL session. Only files in 'tmp/"+session.user+"' are allowed.");
						}
						
						String append = inputVector.get(4).value;
						type = inputVector.get(5).value;
						String encoding = inputVector.get(6).value;

						return FileFx.writer_open(session.user, env, writerId, filePath, append, type, encoding);

					case "json uarray":

						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						index = inputVector.get(4).value;
						value = inputVector.get(5).value;
						type = inputVector.get(6).value;

						return JsonManager.aupdate(env, key, xPath, index, value, type);

					case "json uobject":

						//Get key, name and value
						key = inputVector.get(2).value;
						xPath = inputVector.get(3).value;
						fieldName = inputVector.get(4).value;
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

						return CommandFullAccess.concatOrUnknow(inputVector);

					}

				}

			}

		}

	}

}