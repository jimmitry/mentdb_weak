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
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mongodb.MongoDBManager;
import re.jpayet.mentdb.ext.pa.PAMultipleRegressionOLS;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess12 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
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
			case "mongodb collection_select":
				
				//Get parameters
				String collectionId = inputVector.get(2).value;
				String jsonFilter = inputVector.get(3).value;
				String jsonSort = inputVector.get(4).value;
				String jsonProjection = inputVector.get(5).value;
				String jsonHint = inputVector.get(6).value;
				String jsonMin = inputVector.get(7).value;
				String jsonMax = inputVector.get(8).value;
				String batchSize = inputVector.get(9).value;
				String skip = inputVector.get(10).value;
				String limit = inputVector.get(11).value;
				
				return JsonManager.format_Gson(MongoDBManager.collection_select(env, 
						collectionId, 
						jsonFilter, 
						jsonSort, 
						batchSize, 
						skip, 
						limit, 
						jsonProjection, 
						jsonHint, 
						jsonMin, 
						jsonMax).toJSONString());

			case "log search": case "restricted log_search":

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

				return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+Log.search(status, script, c_key, c_val, msgFilter, dtMin, dtMax, dtOrder, page, nbByPage);

			default:

				//Script execution
				inputVector.remove(inputVector.size()-1);

				return CommandFullAccess.concatOrUnknow(inputVector);

			}
			
		}

	}

}