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

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.entity.language.LanguageManager;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.OperatorFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.parallel.ParallelManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.statement.Statement;
import re.jpayet.mentdb.ext.tools.Misc;

//Command full access
public class CommandFullAccess {

	//Execute the command
	public static String execute(SessionThread session, Vector<MQLValue> inputVector, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Stop the execution if the session was interrupted
		if (session!=null && session.isInterrupted && inputVector!=null) {

			throw new Exception("Sorry, the action has been canceled.");
			
		}
		
		//Check if this is a comment
		if (inputVector.get(0).value.startsWith("#")) {

			if (inputVector.get(0).value.startsWith("#:")) System.out.println(inputVector.get(0).value);

			return "This is a comment ...";
		}

		//Add ; at the end
		if (!(inputVector.get(inputVector.size()-1).value).equals(";")) {
			inputVector.add(new MQLValue(";", 0, 0));
		}

		//Return the variable value
		if (inputVector.size()==2 && inputVector.get(0).cmdBlockType==1) {
			if (env.exist(inputVector.get(0).value)) 
				return env.get(inputVector.get(0).value);
			else if (inputVector.get(0).value.equals("[_n_]")) 
				return "\n";
			else if (inputVector.get(0).value.equals("[_r_]")) 
				return "\r";
			else throw new Exception("Sorry, the variable name "+inputVector.get(0).value+" does not exist.");
		}

		//Load variables
		for(int i=0;i<inputVector.size()-1;i++) {

			switch (inputVector.get(i).cmdBlockType) {
			case 1:

				//Get a variable name
				if (env.exist(inputVector.get(i).value)) 
					inputVector.get(i).value = env.get(inputVector.get(i).value);
				else if (inputVector.get(i).value.equals("[_n_]")) 
					inputVector.get(i).value = "\n";
				else if (inputVector.get(i).value.equals("[_r_]")) 
					inputVector.get(i).value = "\r";
				else {
					throw new Exception("Sorry, the variable name "+inputVector.get(i).value+" does not exist.");
				}

				break;

			case 3:
				
				switch (inputVector.get(i).value) {
				case "false":
					
					inputVector.get(i).value = "0";
					
					break;
					
				case "true":
					
					inputVector.get(i).value = "1";
					
					break;
				case "null":
					
					inputVector.get(i).value = null;

					if (i==0 && inputVector.size()==2) {
						return null;
					}
					
					break;
					
				}

				break;

			case -1:

				inputVector.get(i).value = CommandManager.executeAllCommands(session, Misc.splitCommand("link get value \""+inputVector.get(i).value.replace("\"", "\\\"")+"\""), env, parent_pid, current_pid);

				break;

			case 2: 

				inputVector.get(i).value = CommandManager.executeAllCommands(session, Misc.splitCommand(inputVector.get(i).value), env, parent_pid, current_pid);

				break;

			}

		}
		
		if ((inputVector.get(0).value.equals("stack") || inputVector.get(0).value.equals("stack_execute")) && inputVector.size()>2 && DateFx.is_valid_timestamp(inputVector.get(1).value).equals("1")) {
			
			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return StackManager.insert_stack(session, inputVector, env);
			
		}

		switch (inputVector.get(0).value) {
		case "concat_var":

			//Script concat
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return StringFx.concat_var(inputVector, env);

		case "include":
			
			//No control on log manager

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			String scriptName = inputVector.get(0).value;
			
			int nbParamDiviseBy2 = (inputVector.size()-1)/2;
			if ((nbParamDiviseBy2*2)!=(inputVector.size()-1)) {

				throw new Exception("Sorry, the script '"+scriptName+"' does not has a valid number of parameters.");

			}
			
			//Set all parameters into the variable environment
			for(int i=1;i<inputVector.size();i=i+2) {
				
				env.set(inputVector.get(i).value, inputVector.get(i+1).value);
				
			}

			session.sessionThreadAgent.current_function = "script include start > "+scriptName;
			JSONObject o = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "script get \""+scriptName.replace("\"", "\\\"")+"\";"));
			env.set("[PID]", current_pid);
			env.set("[PPID]", parent_pid);
			String res = Statement.eval(session, Misc.splitCommand((String) o.get("mql")), env, parent_pid, current_pid);
			session.sessionThreadAgent.current_function = "script include end > "+scriptName;
			return res;

		case "call":
			
			//Control on log manager + parent env

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			env.set("[PID]", current_pid);
			env.set("[PPID]", parent_pid);
			scriptName = inputVector.get(0).value;
			session.sessionThreadAgent.current_function = "script call start > "+scriptName;
			res = ScriptManager.execute(session, inputVector, env, false, parent_pid, current_pid);
			session.sessionThreadAgent.current_function = "script call end > "+scriptName;
			return res;

		case "execute":
			
			//Control on log manager + new env

			//Script execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			EnvManager newEnv = new EnvManager();
			newEnv.set("[PPID]", parent_pid);
			scriptName = inputVector.get(0).value;
			session.sessionThreadAgent.current_function = "script execute start > "+scriptName;
			res = ScriptManager.execute(session, inputVector, newEnv, true, parent_pid, null);
			session.sessionThreadAgent.current_function = "script execute end > "+scriptName;
			return res;

		case "concat":

			//Script concat
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return StringFx.concat(inputVector);

		case "parallel":

			//Script parallel execution
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return JsonManager.format_Gson(ParallelManager.exe(env, inputVector, session, parent_pid, current_pid).toJSONString());

		case "case":

			//Case
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);

			return Statement.case_statement(session, inputVector, env, parent_pid, current_pid);

		case "switch":

			//Case
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			
			return Statement.switch_statement(session, inputVector, env, parent_pid, current_pid);

		case "mode":

			//Case
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			
			return Statement.mode_statement(session, inputVector, env, parent_pid, current_pid);

		case "+":

			//Addition
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return OperatorFx.add(inputVector, env);

		case "bi+":

			//Addition
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return OperatorFx.add_big_int(inputVector, env);

		case "bd+":

			//Addition
			inputVector.remove(0);
			inputVector.remove(inputVector.size()-1);
			return OperatorFx.add_big_dec(inputVector, env);

		default :
			
			//Translations ...
			if (inputVector.size()>2) {
				
				if (inputVector.get(0).value.equals("relation") && inputVector.get(1).value.equals("translate")) {
					
					if (inputVector.size()<7) {
						
						throw new Exception("Sorry, the translate function must have 4 arguments min.");
						
					}
					
					String relationId = inputVector.get(2).value;
					String cooperation = inputVector.get(3).value;
					String lang = inputVector.get(4).value;
					String level = inputVector.get(5).value;

					inputVector.remove(0);
					inputVector.remove(0);
					inputVector.remove(0);
					inputVector.remove(0);
					inputVector.remove(0);
					inputVector.remove(0);
					inputVector.remove(inputVector.size()-1);
					
					LanguageManager.exceptionIfNotExist(lang);
					
					return RelationManager.translate(relationId, lang, true, inputVector, env, session, Integer.parseInt(cooperation), level);
					
				}
				
			}
			
			switch (inputVector.size()-1) {
			case 1:
				
				return CommandFullAccess1.execute(session, inputVector, env, parent_pid, current_pid);

			case 2:

				return CommandFullAccess2.execute(session, inputVector, env, parent_pid, current_pid);

			case 3:

				return CommandFullAccess3.execute(session, inputVector, env, parent_pid, current_pid);

			case 4:

				return CommandFullAccess4.execute(session, inputVector, env, parent_pid, current_pid);

			case 5:

				return CommandFullAccess5.execute(session, inputVector, env, parent_pid, current_pid);

			case 6:

				return CommandFullAccess6.execute(session, inputVector, env, parent_pid, current_pid);

			case 7:

				return CommandFullAccess7.execute(session, inputVector, env, parent_pid, current_pid);

			case 8:
				
				return CommandFullAccess8.execute(session, inputVector, env, parent_pid, current_pid);

			case 9:

				return CommandFullAccess9.execute(session, inputVector, env, parent_pid, current_pid);

			case 10:
				
				return CommandFullAccess10.execute(session, inputVector, env, parent_pid, current_pid);

			case 11:
				
				return CommandFullAccess11.execute(session, inputVector, env, parent_pid, current_pid);

			case 12:
				
				return CommandFullAccess12.execute(session, inputVector, env, parent_pid, current_pid);

			case 13:
				
				return CommandFullAccess13.execute(session, inputVector, env, parent_pid, current_pid);
			
			case 14:

				return CommandFullAccess14.execute(session, inputVector, env, parent_pid, current_pid);

			default:

				//Script execution
				inputVector.remove(inputVector.size()-1);

				return concatOrUnknow(inputVector);

			}

		}

	}

	public static String concatOrUnknow(Vector<MQLValue> inputVector) throws Exception {

		String val = inputVector.get(0).value;

		if ((val.startsWith("R")
				&& val.length()>3 && val.charAt(2)=='[') || val.startsWith("TH[")) {

			return StringFx.concat(inputVector);

		} else {

			throw new Exception("Sorry, unknown order: "+Misc.vectorToStringMsg(inputVector));

		}

	}

}