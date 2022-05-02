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
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.excel.ExcelManager;
import re.jpayet.mentdb.ext.excel.ExcelxManager;
import re.jpayet.mentdb.ext.html.HTMLManager;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess9 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value+" "+inputVector.get(3).value) {
		default:

			switch (inputVector.get(0).value+" "+inputVector.get(1).value+" "+inputVector.get(2).value) {
			case "dq analyse show":
				
				//Get key, name and value
				String cmId = inputVector.get(3).value;
				String json = inputVector.get(4).value;
				String algokey = inputVector.get(5).value;
				String fieldkey = inputVector.get(6).value;
				String title = inputVector.get(7).value;
				String query = inputVector.get(8).value;
				
				return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+DQManager.analyse_show(env, session, cmId, json, algokey, fieldkey, title, query, parent_pid, current_pid).toJSONString();

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
				case "script update":
					
					String scriptName = inputVector.get(2).value;
					String activateLog = inputVector.get(3).value;
					String nbAttempt = inputVector.get(4).value;
					String variables = inputVector.get(5).value;
					String desc = inputVector.get(6).value;
					String mql = inputVector.get(7).value;
					String example = inputVector.get(8).value;
					
					ScriptManager.update(env, session, scriptName, variables, mql, desc, example, activateLog, nbAttempt, parent_pid);
					
					return "Script updated with successful.";
				
				case "script merge":
					
					scriptName = inputVector.get(2).value;
					activateLog = inputVector.get(3).value;
					nbAttempt = inputVector.get(4).value;
					variables = inputVector.get(5).value;
					desc = inputVector.get(6).value;
					mql = inputVector.get(7).value;
					example = inputVector.get(8).value;
					
					ScriptManager.merge(env, session, scriptName, variables, mql, desc, example, activateLog, nbAttempt, parent_pid);
					
					return "Script merged with successful.";
				
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
					json = inputVector.get(8).value;

					SmtpManager.send(cm, to, cc, bcc, subject, body, json);

					return "1";

				default:

					//Script execution
					inputVector.remove(inputVector.size()-1);

					return CommandFullAccess.concatOrUnknow(inputVector);

				}

			}

		}

	}

}