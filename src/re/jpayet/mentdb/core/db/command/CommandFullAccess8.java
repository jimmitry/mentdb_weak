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
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.thought.ThoughtManager;
import re.jpayet.mentdb.ext.csv.CSVManager;
import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.html.HTMLManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.pa.PALinearRegression;
import re.jpayet.mentdb.ext.scrud.ScrudManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.soap.SOAPManager;
import re.jpayet.mentdb.ext.sql.SQLManager;

//Command full access
public class CommandFullAccess8 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value+" "+inputVector.get(4).value) {
		case "thought delete by word lang":
			
			//Get key
			String word = inputVector.get(5).value;
			String lang = inputVector.get(6).value;
			String thoughtId = inputVector.get(7).value;
			
			LanguageManager.exceptionIfNotExist(lang);

			ThoughtManager.delete(word, thoughtId, lang);

			return "Thought "+thoughtId+" deleted with successful for the word '"+word+"' and the language '"+lang+"'.";

		default:
			
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
					case "soap http":

						String url = inputVector.get(2).value;
						String jsonHeader = inputVector.get(3).value;
						String actionName = inputVector.get(4).value;
						String contentType = inputVector.get(5).value;
						String data = inputVector.get(6).value;
						String proxy_config = inputVector.get(7).value;

						return SOAPManager.execute_http_proxy(url, jsonHeader, actionName, contentType, data, proxy_config);

					case "soap https":

						url = inputVector.get(2).value;
						jsonHeader = inputVector.get(3).value;
						actionName = inputVector.get(4).value;
						contentType = inputVector.get(5).value;
						data = inputVector.get(6).value;
						proxy_config = inputVector.get(7).value;

						return SOAPManager.execute_https_proxy(url, jsonHeader, actionName, contentType, data, proxy_config);

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

						return CommandFullAccess.concatOrUnknow(inputVector);

					}

				}

			}
			
		}

	}

}