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

package re.jpayet.mentdb.core.entity.stimulation;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class StimulationManager {
	
	public static JSONObject ai_tasks = new JSONObject();
	
	public static Vector<StimulationManager> userStimulationStack = new Vector<StimulationManager>();
	
	@SuppressWarnings("unchecked")
	public static void init(long sessionId) throws Exception {
		
		JSONObject stim_wait = new JSONObject();
		stim_wait.put("sw", new JSONArray());

		Record.add(sessionId, "STIM_WAIT[]", stim_wait.toJSONString());
		Record.add(sessionId, "TASKS[]", new JSONObject().toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static void waiting_push(long sessionId, String targetUser, String sentence) throws Exception {
		
		JSONObject o = Record.getNode(sessionId, "STIM_WAIT[]");
		JSONArray stim_wait_tab = (JSONArray) o.get("sw");
		
		JSONObject stim = new JSONObject();
		stim.put("u", targetUser);
		stim.put("s", sentence);
		
		stim_wait_tab.add(stim);
		
		Record.update(sessionId, "STIM_WAIT[]", o.toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray waiting_pull(long sessionId) throws Exception {
		
		JSONObject obj = Record.getNode(sessionId, "STIM_WAIT[]");
		
		JSONArray stim_wait_tab = (JSONArray) obj.get("sw");
		
		if (stim_wait_tab.size()==0) {
			
			return new JSONArray();
			
		}
		
		//Get the first user
		String targetUser = (String) ((JSONObject) stim_wait_tab.get(0)).get("u");
		
		JSONArray result = new JSONArray();
		Vector<Integer> to_delete = new Vector<Integer>();
		
		//Get the stimulation for the user
		for(int i=0;i<stim_wait_tab.size();i++) {
			
			JSONObject o = (JSONObject) stim_wait_tab.get(i);
			String localUser = (String) (o).get("u");
			
			if (localUser.equals(targetUser)) {
				
				result.add(o);
				to_delete.add(i);
				
			}
			
		}
		
		//Delete all stimulations
		for(int i=to_delete.size()-1;i>=0;i--) {
			
			int ii = to_delete.get(i);
			stim_wait_tab.remove(ii);
			
		}
		
		obj.put("sw", stim_wait_tab);
		
		Record.update(sessionId, "STIM_WAIT[]", obj.toJSONString());
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static void reset(long sessionId, String user, EnvManager env) throws Exception {
		
		StimulationManager.ai_tasks.put(user, new JSONArray());
		
		env.jsonObj.put("ai_tasks", StimulationManager.ai_tasks);
		
	}
	
	public static JSONObject show(long sessionId) throws Exception {
		
		return StimulationManager.ai_tasks;
		
	}
	
	public static void saveTasks(long sessionId) throws Exception {
		
		Record.update(sessionId, "TASKS[]", StimulationManager.ai_tasks.toJSONString());
		
	}
	
	//Properties
	public String targetUser = "", toUser = "", sentence = "";
	public long timestamp = 0;
	
	//Constructor
	public StimulationManager(long timestamp, String targetUser, String toUser, String sentence) {
		
		this.timestamp=timestamp;
		this.targetUser=targetUser;
		this.toUser=toUser;
		this.sentence=sentence;
		
	}
	
	//Add the stimulation into the user stack
	public static void addIntoTheUserStack(long timestamp, String targetUser, String toUser, String sentence) throws Exception {

		//Create the stimulation object
		StimulationManager stm = new StimulationManager(timestamp, targetUser, toUser, sentence);
		
		//Add into the stack
		userStimulationStack.addElement(stm);
		
	}
	
	public static String call(boolean initialization, String targetUser, String str, EnvManager env, SessionThread session) throws Exception {
		
		if (initialization) {
			
			return CommandManager.executeAllCommands(false, session, Misc.splitCommand("include \"brain.internal.stimulate.user.post\" \"[ai_str]\" \""+str.replace("\"", "\\\"")+"\"  \"[ai_targetUser]\" \""+targetUser.replace("\"", "\\\"")+"\";"), env, null, null);
			
		} else {
			
			return CommandManager.executeAllCommands(false, session, Misc.splitCommand("include \"brain.internal.click.post\";"), env, null, null);
			
		}
		
	}
	
	//redirection
	@SuppressWarnings("unchecked")
	public static void redirect(String data, JSONArray stimulation) throws Exception {
		
		//Checker
		if (stimulation.size()==0) {
			
			throw new Exception("Sorry, you must initialize the stimulation tab before.");
			
		}
		
		JSONArray array = ((JSONArray) stimulation.get(stimulation.size()-1));

		JSONObject stm = new JSONObject();
		stm.put("_current_action_", "REDIRECTION");
		stm.put("_next_action_", "redirection_relation");
		stm.put("redirection_data", data);
		
		array.add(stm);
				
	}
	
	//Build stimulation block
	@SuppressWarnings("unchecked")
	public static JSONObject StimulationValue(long sessionId, SessionThread session, EnvManager env, String value, int strType, boolean showTypes) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		
		result.put("val", value);
		result.put("strType", strType);
		result.put("thoughtFilter", "");
		
		return result;
		
	}
	
	//Split without empty
	@SuppressWarnings("unchecked")
	public static JSONArray splitStimulation(long sessionId, SessionThread session, EnvManager env, String str, boolean showTypes) throws Exception {
		
		//Initialization
		StringBuilder tmpstr = new StringBuilder(str);
		JSONArray commands = new JSONArray();
		
		//New sentence
		JSONObject newSentence = new JSONObject();
		newSentence.put("s", new JSONArray());
		commands.add(newSentence);
		
		int endIndex = 1;

		//Parse the command string
		while (!tmpstr.toString().equals("")) {
			
			String c_tmpstr = tmpstr.substring(0, 1);
			switch (c_tmpstr) {
			case "?": case ",": case ".": case ";": case "!": case ":": 
				
				((JSONArray) ((JSONObject) commands.get(commands.size()-1)).get("s")).add(StimulationValue(sessionId, session, env, tmpstr.substring(0, 1), 9, showTypes));
				
				//New sentence
				newSentence = new JSONObject();
				newSentence.put("s", new JSONArray());
				commands.add(newSentence);
				
				tmpstr = new StringBuilder(tmpstr.substring(1));
				break;
				
			case "\n": case "\t": case " ": 
				
				tmpstr = new StringBuilder(tmpstr.substring(1));
				break;
				
			case "\"": 
				
				//It is a string
				endIndex = tmpstr.indexOf("\"", 1);
				while (endIndex!=-1 && tmpstr.substring(endIndex-1, endIndex).equals("\\")) {
					endIndex = tmpstr.indexOf("\"", endIndex+1);
				}
				
				//Generate an error if the string is never close
				if (endIndex==-1) {
					
					throw new Exception("Sorry, Invalid command (string must be close)");
					
				} else {
					
					//The string is close
					String tmptmpstr = tmpstr.substring(1, endIndex);
					((JSONArray) ((JSONObject) commands.get(commands.size()-1)).get("s")).add(StimulationValue(sessionId, session, env, tmptmpstr.replace("\\\"", "\""), 1, showTypes));
					
					tmpstr = new StringBuilder(tmpstr.substring(endIndex+1));
					
				}
				break;
				
			default:
				
				//It is not a string
				//Find the space
				HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
				int i0 = tmpstr.indexOf("\t");hm.put(i0, 0);
				int i1 = tmpstr.indexOf(" ");hm.put(i1, 0);
				int i2 = tmpstr.indexOf("\n");hm.put(i2, 0);
				int i3 = tmpstr.indexOf("?");hm.put(i3, 0);
				int i4 = tmpstr.indexOf(",");hm.put(i4, 0);
				int i5 = tmpstr.indexOf(".");hm.put(i5, 0);
				int i6 = tmpstr.indexOf(";");hm.put(i6, 0);
				int i7 = tmpstr.indexOf("!");hm.put(i7, 0);
				int i8 = tmpstr.indexOf(":");hm.put(i8, 0);
				
				if (hm.size()==1) {
					endIndex = -1;
				} else {
					SortedSet<Integer> keys = new TreeSet<Integer>(hm.keySet());
					Object[] iKeys = keys.toArray();
					if (((Integer) iKeys[0])!=-1) {
						endIndex = (Integer) iKeys[0];
					} else {
						endIndex = (Integer) iKeys[1];
					}
					
				}
								
				//Go to the next key
				if (endIndex==-1) {
					String tmptmpstr = tmpstr.substring(0, tmpstr.length());
					if (!tmptmpstr.equals("")) {
						
						JSONArray split_word = StringFx.split_word(tmptmpstr, "'-");
						for(int sp = 0;sp<split_word.size();sp++) {
							((JSONArray) ((JSONObject) commands.get(commands.size()-1)).get("s")).add(StimulationValue(sessionId, session, env, ""+split_word.get(sp), 0, showTypes));
						}
					}

					tmpstr = new StringBuilder("");
					
				} else {

					String tmptmpstr = tmpstr.substring(0, endIndex);
					if (!tmptmpstr.equals("")) {
						JSONArray split_word = StringFx.split_word(tmptmpstr, "'-");
						for(int sp = 0;sp<split_word.size();sp++) {
							((JSONArray) ((JSONObject) commands.get(commands.size()-1)).get("s")).add(StimulationValue(sessionId, session, env, ""+split_word.get(sp), 0, showTypes));
						}
					}

					tmpstr = new StringBuilder(tmpstr.substring(endIndex));
					
				}
			
			}
			
		}
		
		//Remove the last if empty
		int nbDeleted = 0, nbTab = commands.size();
		for(int i=0;i<nbTab;i++) {
			if (((JSONArray) ((JSONObject) commands.get(i-nbDeleted)).get("s")).size()==0) {
				commands.remove(i-nbDeleted);
				nbDeleted++;
			}
		}
		
		//Return all command lines
		return commands;
		
	}

}
