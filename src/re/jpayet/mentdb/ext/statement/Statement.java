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

package re.jpayet.mentdb.ext.statement;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.parameter.ParamCache;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

//The statement class
public class Statement {
	
	//EVAL statement
	public static String eval(SessionThread session, Vector<Vector<MQLValue>> mql, EnvManager env, String parent_pid, String current_pid) throws Exception {

		return CommandManager.executeAllCommands(session, mql, env, parent_pid, current_pid);
		
	}
	
	//EVAL statement
	public static String eval(SessionThread session, String mql, EnvManager env, String parent_pid, String current_pid) throws Exception {

		return CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, parent_pid, current_pid);
		
	}
	
	//EXCEPTION statement
	public static String exception_mql(SessionThread session, String id, String message, EnvManager env, String parent_pid, String current_pid) throws Exception {

		String id_str = CommandManager.executeAllCommands(session, Misc.splitCommand(id), env, parent_pid, current_pid);
		String message_str = CommandManager.executeAllCommands(session, Misc.splitCommand(message), env, parent_pid, current_pid);
		
		//Set the error message
		env.set("[ERROR]", id_str+": "+message_str);
		
		//Generate an exception
		throw new Exception(id_str+": "+message_str);
		
	}
	
	//TRY statement
	public static String try_mql(SessionThread session, String mainCommand, String commandIfError, String exceptionId, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the variable name is not valid
		if (!EnvManager.is_valid_varname(exceptionId)) {
			throw new Exception("Sorry, the variable name "+exceptionId+" is not valid (example: [var1]).");
		}

		//Try
		try {
			
			//Try to execute the first expression
			result=CommandManager.executeAllCommands(session, Misc.splitCommand(mainCommand), env, parent_pid, current_pid);
			
			//Clear the error message if exist
			if (env.exist(exceptionId)) {
				env.remove(exceptionId);
			}
			
		} catch (Exception e) {
			
			//Set the error message in the environment variable
			env.set(exceptionId, ""+e.getMessage());
			
			//An error appears then execute the next expression
			result=CommandManager.executeAllCommands(session, Misc.splitCommand(commandIfError), env, parent_pid, current_pid);
			
		}

		//Return the result of the function
		return result;
		
	}
	
	//REPEAT statement
	public static void repeat_mql(SessionThread session, String condition, String action, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		String conditionResult = null;

		Vector<Vector<MQLValue>> repeat_condition = Misc.splitCommand(condition);
		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(action);
		
		try {
		
			//While loop
			do {
	
				try {
					
					//Execute the action
					CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);

				} catch (Exception e) {
					
					if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw e;
					}
					
				};
	
				//Retest the condition
				session.sessionThreadAgent.current_function = "repeat > "+condition;
				conditionResult = CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_condition), env, parent_pid, current_pid);

				session.sessionThreadAgent.current_function = "repeat > next";
				
			} while (conditionResult!=null && conditionResult.equals("1"));
			
		}  catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}
	
	//WHILE statement
	public static void while_mql(SessionThread session, String condition, String action, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		Vector<Vector<MQLValue>> repeat_condition = Misc.splitCommand(condition);
		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(action);
		
		//Initialization
		String conditionResult = CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_condition), env, parent_pid, current_pid);
		
		try {
		
			//While loop
			while (conditionResult!=null && conditionResult.equals("1")) {
	
				try {
					
					//Execute the action
					CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);

				} catch (Exception e) {
					
					if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw e;
					}
					
				};
	
				//Retest the condition
				session.sessionThreadAgent.current_function = "while > "+condition;
				conditionResult = CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_condition), env, parent_pid, current_pid);

				session.sessionThreadAgent.current_function = "while > next";
			
			}
			
		}  catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}
	
	//FOR statement
	public static void for_mql(SessionThread session, String init, String condition, String increment, String action, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the initialization
		CommandManager.executeAllCommands(session, Misc.splitCommand(init), env, parent_pid, current_pid);

		Vector<Vector<MQLValue>> repeat_condition = Misc.splitCommand(condition);
		Vector<Vector<MQLValue>> repeat_increment = Misc.splitCommand(increment);
		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(action);
		
		//Initialization
		String conditionResult = CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_condition), env, parent_pid, current_pid);
		
		try {
		
			//For loop
			while (conditionResult!=null && conditionResult.equals("1")) {
	
				try {
					
					//Execute the action
					CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);

				} catch (Exception e) {
					
					if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw e;
					}
					
				};
				
				//Increment
				session.sessionThreadAgent.current_function = "for > "+increment;
				CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_increment), env, parent_pid, current_pid);
				
				//Reset the condition result
				session.sessionThreadAgent.current_function = "for > "+condition;
				conditionResult = CommandManager.executeAllCommands(session, MQLValue.deepCopyValue(repeat_condition), env, parent_pid, current_pid);

				session.sessionThreadAgent.current_function = "for > next";
				
			}
			
		} catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}
	
	public static ConcurrentHashMap<String, ParamCache> all_synchronized_key = new ConcurrentHashMap<String, ParamCache>();
	public static String synchronized_block(SessionThread session, String key, String mql, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		if (!all_synchronized_key.containsKey(key)) {
			all_synchronized_key.put(key, new ParamCache(key));
		}
		
		synchronized (all_synchronized_key.get(key)) {
			return CommandManager.executeAllCommands(session, Misc.splitCommand(mql), env, parent_pid, current_pid);
		}
		
	}
	
	//IF statement
	public static String if_mql(SessionThread session, String condition, String trueAction, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the condition
		String condition_result = CommandManager.executeAllCommands(session, Misc.splitCommand(condition), env, parent_pid, current_pid);
		
		if (condition_result!=null && condition_result.equals("1")) {
			return CommandManager.executeAllCommands(session, Misc.splitCommand(trueAction), env, parent_pid, current_pid);
		} else {
			return "";
		}
		
	}
	
	//IF statement
	public static String if_mql(SessionThread session, String condition, String trueAction, String falseAction, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the condition
		String condition_result = CommandManager.executeAllCommands(session, Misc.splitCommand(condition), env, parent_pid, current_pid);
		
		if (condition_result!=null && condition_result.equals("1")) {
			return CommandManager.executeAllCommands(session, Misc.splitCommand(trueAction), env, parent_pid, current_pid);
		} else {
			return CommandManager.executeAllCommands(session, Misc.splitCommand(falseAction), env, parent_pid, current_pid);
		}
		
	}
	
	//IF statement (force)
	public static String if_force_mql(SessionThread session, String condition, String trueAction, String falseAction, EnvManager env, String parent_pid, String current_pid) throws Exception {
		
		//Execute the condition
		String condition_result = CommandManager.executeAllCommands(session, Misc.splitCommand(condition), env, parent_pid, current_pid);

		String rt = CommandManager.executeAllCommands(session, Misc.splitCommand(trueAction), env, parent_pid, current_pid);
		String rf = CommandManager.executeAllCommands(session, Misc.splitCommand(falseAction), env, parent_pid, current_pid);
		
		if (condition_result!=null && condition_result.equals("1")) {
			return rt;
		} else {
			return rf;
		}
		
	}
	
	//Case statement
	public static String case_statement(SessionThread session, Vector<MQLValue> parameters, EnvManager env, String parent_pid, String current_pid) throws Exception {

		//Initialization
		String result = null, resultCondition = null;;
		boolean alreadyExecuted = false;
		
		//If the number of parameter = 1
		if (parameters.size()==1) {
			
			//Execute the else case
			return CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(0).value), env, parent_pid, current_pid);
			
		} else {
			
			//Get the number of pairs
			int nbParamDiviseBy2 = Math.abs(parameters.size()/2);
			
			//Parse all pairs
			for(int i=0;i<nbParamDiviseBy2;i++) {
				
				//Evaluate the condition
				resultCondition = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(i*2).value), env, parent_pid, current_pid);
				
				//Execute if test case is true
				if (resultCondition!=null && resultCondition.equals("1")) {
					
					result = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(i*2+1).value), env, parent_pid, current_pid);
					alreadyExecuted = true;
					break;
					
				}
				
			}
			
			//The last parameter
			if (!alreadyExecuted && (nbParamDiviseBy2*2)!=parameters.size()) {

				result = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(parameters.size()-1).value), env, parent_pid, current_pid);
				
			}
			
		}

		//Return the result of the function
		return result;

	}
	
	//Case statement
	public static String mode_statement(SessionThread session, Vector<MQLValue> parameters, EnvManager env, String parent_pid, String current_pid) throws Exception {

		//Initialization
		String result = null, resultCase = null;;
		boolean alreadyExecuted = false;
		
		HashMap<String, String> conditions = new HashMap<String, String>();
		
		//If the number of parameter = 1
		if (parameters.size()<=2) {
			
			//Execute the else case
			throw new Exception("Sorry, 'env' statement must have 2 or more parameters.");
			
		} else {
			
			String valueToSearch = CommandManager.executeAllCommands(session, Misc.splitCommand("parameter get value \""+(parameters.get(0).value.replace("\"", ""))+"\";"), env, parent_pid, current_pid);
			parameters.remove(0);
			
			//Get the number of pairs
			int nbParamDiviseBy2 = Math.abs(parameters.size()/2);
			
			//Parse all pairs
			for(int i=0;i<nbParamDiviseBy2;i++) {
				
				//Evaluate the condition
				resultCase = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(i*2).value), env, parent_pid, current_pid);
				
				conditions.put(resultCase, parameters.get(i*2+1).value);
				
			}
			
			//Search
			if (conditions.containsKey(valueToSearch)) {
				
				result = CommandManager.executeAllCommands(session, Misc.splitCommand(conditions.get(valueToSearch)), env, parent_pid, current_pid);
				alreadyExecuted = true;
				
			}
			
			//The last parameter
			if (!alreadyExecuted && (nbParamDiviseBy2*2)!=parameters.size()) {

				result = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(parameters.size()-1).value), env, parent_pid, current_pid);
				
			}
			
		}

		//Return the result of the function
		return result;

	}
	
	//Case statement
	public static String switch_statement(SessionThread session, Vector<MQLValue> parameters, EnvManager env, String parent_pid, String current_pid) throws Exception {

		//Initialization
		String result = null, resultCase = null;;
		boolean alreadyExecuted = false;
		
		HashMap<String, String> conditions = new HashMap<String, String>();
		
		//If the number of parameter = 1
		if (parameters.size()<=2) {
			
			//Execute the else case
			throw new Exception("Sorry, 'switch' statement must have 2 or more parameters.");
			
		} else {
			
			String valueToSearch = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(0).value), env, parent_pid, current_pid);
			parameters.remove(0);
			
			//Get the number of pairs
			int nbParamDiviseBy2 = Math.abs(parameters.size()/2);
			
			//Parse all pairs
			for(int i=0;i<nbParamDiviseBy2;i++) {
				
				//Evaluate the condition
				resultCase = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(i*2).value), env, parent_pid, current_pid);
				
				conditions.put(resultCase, parameters.get(i*2+1).value);
				
			}
			
			//Search
			if (conditions.containsKey(valueToSearch)) {
				
				result = CommandManager.executeAllCommands(session, Misc.splitCommand(conditions.get(valueToSearch)), env, parent_pid, current_pid);
				alreadyExecuted = true;
				
			}
			
			//The last parameter
			if (!alreadyExecuted && (nbParamDiviseBy2*2)!=parameters.size()) {

				result = CommandManager.executeAllCommands(session, Misc.splitCommand(parameters.get(parameters.size()-1).value), env, parent_pid, current_pid);
				
			}
			
		}

		//Return the result of the function
		return result;

	}

}
