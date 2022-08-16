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
import re.jpayet.mentdb.ext.bot.BotManager;
import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.ml.MLManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.session.SessionThread;

//Command full access
public class CommandFullAccess10 {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
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
				case "bot training_merge":
					
					String bot = inputVector.get(2).value;
					String key = inputVector.get(3).value;
					String context = inputVector.get(4).value;
					String rights = inputVector.get(5).value;
					String description = inputVector.get(6).value;
					String in_trigger_json = inputVector.get(7).value;
					String out_mql_output_json = inputVector.get(8).value;
					String consciousness_json = inputVector.get(9).value;
					
					BotManager.training_merge(bot, key, context, rights, description, in_trigger_json, out_mql_output_json, consciousness_json);
					
					return "1";
				
				case "script add": case "script create": case "script insert":

					String method = inputVector.get(2).value;
					String scriptName = inputVector.get(3).value;
					String activateLog = inputVector.get(4).value;
					String nbAttempt = inputVector.get(5).value;
					String variables = inputVector.get(6).value;
					String desc = inputVector.get(7).value;
					String mql = inputVector.get(8).value;
					String example = inputVector.get(9).value;
					
					ScriptManager.add(env, session, method, scriptName, variables, mql, desc, example, activateLog, nbAttempt, parent_pid);
					
					return "Script added with successful.";

				case "stack search": case "restricted stack_search":

					String tableType = inputVector.get(2).value;
					String script = inputVector.get(3).value;
					String dtType = inputVector.get(4).value;
					String dtMin = inputVector.get(5).value;
					String dtMax = inputVector.get(6).value;
					String dtOrder = inputVector.get(7).value;
					String page = inputVector.get(8).value;
					String nbByPage = inputVector.get(9).value;
					
					return "j23i88m90m76i39t04r09y35p14a96y09e57t41"+StackManager.search(tableType, script, 
							dtType, dtMin, dtMax, dtOrder, page, nbByPage);

				default:

					//Script execution
					inputVector.remove(inputVector.size()-1);

					return CommandFullAccess.concatOrUnknow(inputVector);

				}
				
			}
			
		}

	}

}