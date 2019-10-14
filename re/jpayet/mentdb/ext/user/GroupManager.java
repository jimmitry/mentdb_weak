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

package re.jpayet.mentdb.ext.user;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.BasicData;
import re.jpayet.mentdb.ext.app.AppManager;
import re.jpayet.mentdb.ext.cm.CMManager;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.job.JobManager;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.sql.SQLQueryManager;
import re.jpayet.mentdb.ext.vhost.VHostManager;

//Grant management
public class GroupManager {
	
	//Create the group object
	public static void init(long sessionId) throws Exception {
		
		JSONObject seq = new JSONObject();
		
		Record.add(sessionId, "G[]", seq.toJSONString());
		
	}
	
	//Check if a group already exist
	public static boolean exist(long sessionId, String groupName) throws Exception {
		
		if (Record.getNode(sessionId, "G["+groupName+"]")==null) {
			return false;
		} else {
			return true;
		}
		
	}
	
	//Show all groups
	public static JSONObject showAllGroups(long sessionId) throws Exception {
		
		return Record.getNode(sessionId, "G[]");
		
	}
	
	//Get group
	public static JSONObject userGetGroup(long sessionId, String login) throws Exception {
		
		if (GroupManager.isGrantedUser(sessionId, "sys", login)) 
			return showAllGroups(sessionId);
		else 
			return (JSONObject) Record.getNode(sessionId, "U["+login.toLowerCase()+"]").get("groups");
		
	}
	
	//Get group
	@SuppressWarnings("unchecked")
	public static JSONObject scriptGetGroup(long sessionId, String scriptName) throws Exception {
		
		//Generate an error if the script does not exist
		if (!ScriptManager.exist(sessionId, scriptName)) {

			throw new Exception("Sorry, the script "+scriptName+" does not exist.");

		}
		
		JSONObject result = (JSONObject) ((JSONObject) Record.getNode(sessionId, "script["+scriptName+"]")).get("groups");
		
		result.put("sys", 0);
		
		return result;
		
	}
	
	//Get script
	@SuppressWarnings("unchecked")
	public static JSONObject userGetScript(long sessionId, String login) throws Exception {
		
		JSONObject result = new JSONObject();
		
		JSONObject groups = GroupManager.userGetGroup(sessionId, login);
		for(Object group : groups.keySet()) {
			
			String g = (String) group;
			
			//Get all scripts
			JSONObject scripts = GroupManager.getScript(sessionId, g);
			for(Object script : scripts.keySet()) {
				
				String s = (String) script;
				
				result.put(s, 0);
				
			}
			
		}
		
		return result;
		
	}
	
	//Get user
	@SuppressWarnings("unchecked")
	public static JSONObject scriptGetUser(long sessionId, String scriptName) throws Exception {
		
		JSONObject result = new JSONObject();
		
		JSONObject groups = GroupManager.scriptGetGroup(sessionId, scriptName);
		for(Object group : groups.keySet()) {
			
			String g = (String) group;
			
			//Get all users
			JSONObject users = GroupManager.getUser(sessionId, g);
			for(Object user : users.keySet()) {
				
				String u = (String) user;
				
				result.put(u, 0);
				
			}
			
		}
		
		JSONObject sysUsers = GroupManager.getUser(sessionId, "sys");
		for(Object sysUser : sysUsers.keySet()) {
			
			result.put((String) sysUser, 0);
			
		}
		
		return result;
		
	}
	
	//Group to HTML
	@SuppressWarnings("unchecked")
	public static String adminToHtml(long sessionId, SessionThread session, EnvManager env, String login, String parent_pid) throws Exception {
		
		//Initialization
		JSONObject json = new JSONObject();
		JSONObject core = new JSONObject();
		json.put("core", core);
		JSONArray data = new JSONArray();
		core.put("data", data);

		JSONObject activity = new JSONObject();
		activity.put("text", "Activity");
		activity.put("icon", "images/activity.png");
		data.add(activity);
		JSONArray activityChildrenAction = new JSONArray();
		activity.put("children", activityChildrenAction);
		
		JSONObject stat = new JSONObject();
		stat.put("text", "Stack lines");
		stat.put("icon", "images/stat.png");
		activityChildrenAction.add(stat);
		JSONArray statChildrenAction = new JSONArray();
		stat.put("children", statChildrenAction);
		stat.put("mql", "editor open form stack lines");
		stat.put("direct", "0");

		JSONObject stack = new JSONObject();
		stack.put("text", "Stack process");
		stack.put("icon", "images/stack.png");
		activityChildrenAction.add(stack);
		JSONArray stackChildrenAction = new JSONArray();
		stack.put("children", stackChildrenAction);
		stack.put("mql", "editor open form stack process");
		stack.put("direct", "0");
		
		stack.put("title1", "Stack search error");
		stack.put("action1", "in out_editor {\n	stack search \"error\" \n" + 
				"	\"\" \n" + 
				"	\"exe\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		stack.put("direct1", "0");
		
		stack.put("title2", "Stack search closed");
		stack.put("action2", "in out_editor {\n	stack search \"closed\" \n" + 
				"	\"\" \n" + 
				"	\"exe\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		stack.put("direct2", "0");
		
		stack.put("title3", "Stack search running");
		stack.put("action3", "in out_editor {\n	stack search \"running\" \n" + 
				"	\"\" \n" + 
				"	\"exe\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		stack.put("direct3", "0");
		
		stack.put("title4", "Stack search wait");
		stack.put("action4", "in out_editor {\n	stack search \"wait\" \n" + 
				"	\"\" \n" + 
				"	\"exe\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		stack.put("direct4", "0");
		
		stack.put("title5", "Stack get process");
		stack.put("action5", "stack get 1");
		stack.put("direct5", "0");
		
		stack.put("title6", "Stack count error");
		stack.put("action6", "stack count_error");
		stack.put("direct6", "1");
		
		stack.put("title7", "Stack count closed");
		stack.put("action7", "stack count_closed");
		stack.put("direct7", "1");
		
		stack.put("title8", "Stack count running");
		stack.put("action8", "stack count_running");
		stack.put("direct8", "1");
		
		stack.put("title9", "Stack count wait");
		stack.put("action9", "stack count_wait");
		stack.put("direct9", "1");

		JSONObject plog = new JSONObject();
		plog.put("text", "Logs before archive");
		plog.put("icon", "images/plog.png");
		activityChildrenAction.add(plog);
		JSONArray plogChildrenAction = new JSONArray();
		plog.put("children", plogChildrenAction);
		plog.put("mql", "editor open form logs before archive");
		plog.put("direct", "0");
		
		plog.put("title1", "Log search ko");
		plog.put("action1", "in out_editor {\n	log search \"ko\" \n" + 
				"	\"\" \n" + 
				"	\"\" \n" + 
				"	\"\" \n" + 
				"	\"\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		plog.put("direct1", "0");
		
		plog.put("title2", "Log search ko (filter)");
		plog.put("action2", "in out_editor {\n	log search \"ko\" \n" + 
				"	\"scriptNameFilter\" \n" + 
				"	\"keyFilter\" \n" + 
				"	\"valFilter\" \n" + 
				"	\"msgerrorFilter\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 30)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 500;\n}");
		plog.put("direct2", "0");
		
		plog.put("title3", "Log show time");
		plog.put("action3", "in out_editor {\n	log show_time 1\n}");
		plog.put("direct3", "0");
		
  		JSONObject addUser = new JSONObject();
  		addUser.put("text", "User");
  		addUser.put("icon", "images/user1.png");
		data.add(addUser);
		JSONArray userChildren = new JSONArray();
		addUser.put("children", userChildren);
		
		addUser.put("title1", "Add");
		addUser.put("action1", "user create \"<<<login>>>\" \"<<<password>>>\";");
		addUser.put("direct1", "0");
		
		addUser.put("title2", "DB Sessions");
		addUser.put("action2", "sessions;");
		addUser.put("direct2", "1");
		
		addUser.put("title3", "Current DB session id");
		addUser.put("action3", "sid;");
		addUser.put("direct3", "1");
		
		addUser.put("title4", "WS Sessions");
		addUser.put("action4", "@sessions;");
		addUser.put("direct4", "1");
		
		addUser.put("title5", "Current WS session id");
		addUser.put("action5", "@sid;");
		addUser.put("direct5", "1");

  		JSONArray userList = UserManager.showAll(sessionId);
  		
  		for(int i=0;i<userList.size();i++) {
  			
  			String u = userList.get(i)+"";
  			
  			if (!u.equals("ai") && !u.equals("mentdb")) {
  			
	  			JSONObject user = new JSONObject();

	  			user.put("text", ""+u);
	  			user.put("icon", "images/user1.png");
	  			userChildren.add(user);
				JSONArray userChildrenAction = new JSONArray();
				user.put("children", userChildrenAction);
				user.put("mql", "node show U["+u+"];");
				user.put("direct", "1");
				
				user.put("title1", "Show node");
				user.put("action1", "node show U["+u+"];");
				user.put("direct1", "1");
				
				user.put("title2", "Show thought");
				user.put("action2", "node show {\n	node select U["+u+"] \"/th\"; \n};");
				user.put("direct2", "1");
				
				user.put("title3", "Show user groups");
				user.put("action3", "user show groups \""+(u+"").replace("\"", "\\\"")+"\";");
				user.put("direct3", "1");
				
				user.put("title4", "Show user scripts");
				user.put("action4", "user show scripts \""+(u+"").replace("\"", "\\\"")+"\";");
				user.put("direct4", "1");
				
				user.put("title5", "Grant group");
				user.put("action5", "group grant user \""+(u+"").replace("\"", "\\\"")+"\" \"<<<group>>>\";");
				user.put("direct5", "0");
				
				user.put("title6", "Ungrant group");
				user.put("action6", "group ungrant user \""+(u+"").replace("\"", "\\\"")+"\" \"<<<group>>>\";");
				user.put("direct6", "0");
				
				user.put("title7", "Update password");
				user.put("action7", "user set password \""+(u+"").replace("\"", "\\\"")+"\" \"<<<password>>>\";");
				user.put("direct7", "0");
				
				user.put("title8", "Disable (Cannot be deleted)");
				user.put("action8", "user disable \""+(u+"").replace("\"", "\\\"")+"\";");
				user.put("direct8", "0");
				
				user.put("title9", "Grant group 'api-mql'");
				user.put("action9", "group grant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-mql\";");
				user.put("direct9", "0");
				
				user.put("title10", "Grant group 'api-ai'");
				user.put("action10", "group grant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-ai\";");
				user.put("direct10", "0");
				
				user.put("title11", "Grant group 'api-rest'");
				user.put("action11", "group grant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-rest\";");
				user.put("direct11", "0");
				
				user.put("title12", "Ungrant group 'api-mql'");
				user.put("action12", "group ungrant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-mql\";");
				user.put("direct12", "0");
				
				user.put("title13", "Ungrant group 'api-ai'");
				user.put("action13", "group ungrant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-ai\";");
				user.put("direct13", "0");
				
				user.put("title14", "Ungrant group 'api-rest'");
				user.put("action14", "group ungrant user \""+(u+"").replace("\"", "\\\"")+"\" \"api-rest\";");
				user.put("direct14", "0");

  			}
  			
  		}

  		JSONObject addGroup = new JSONObject();
  		addGroup.put("text", "Group");
  		addGroup.put("icon", "images/group.png");
		data.add(addGroup);
		JSONArray groupChildren = new JSONArray();
		addGroup.put("children", groupChildren);
		
		addGroup.put("title1", "Add");
		addGroup.put("action1", "group add \"<<<groupName>>>\";");
		addGroup.put("direct1", "0");
		
		JSONObject userGroups = GroupManager.userGetGroup(sessionId, login);
		
		List<Map.Entry<String, Object>> listObj = new LinkedList<>( userGroups.entrySet() );
		Collections.sort( listObj, new Comparator<Map.Entry<String, Object>>() {
			@Override
			public int compare( Map.Entry<String, Object> o1, Map.Entry<String, Object> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(Map.Entry<String, Object> o : listObj) {
			
			String k = o.getKey();

			JSONObject group = new JSONObject();
			group.put("text", k);
			group.put("icon", "images/group.png");
			groupChildren.add(group);
			JSONArray groupChildrenAction = new JSONArray();
			group.put("children", groupChildrenAction);
			group.put("mql", "group get \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct", "1");
			
			group.put("title1", "Show");
			group.put("action1", "group get \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct1", "1");
			
			group.put("title2", "Show user");
			group.put("action2", "group get user \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct2", "1");
			
			group.put("title3", "Show script");
			group.put("action3", "group get script \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct3", "1");
			
			group.put("title4", "Grant user");
			group.put("action4", "group grant user \"<<<login>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct4", "0");
			
			group.put("title5", "Grant script");
			group.put("action5", "group grant script \"<<<scriptName>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct5", "0");
			
			group.put("title6", "Grant script (starts with)");
			group.put("action6", "group grant_all script \"<<<scriptName>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct6", "0");
			
			group.put("title7", "Ungrant user");
			group.put("action7", "group ungrant user \"<<<login>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct7", "0");
			
			group.put("title8", "Ungrant script");
			group.put("action8", "group ungrant script \"<<<scriptName>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct8", "0");
			
			group.put("title9", "Ungrant script (starts with)");
			group.put("action9", "group ungrant_all script \"<<<scriptName>>>\" \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct9", "0");
			
			group.put("title10", "Delete");
			group.put("action10", "group remove \""+k.replace("\"", "\\\"")+"\";");
			group.put("direct10", "0");

		}

  		JSONObject job = new JSONObject();
  		job.put("text", "Job");
  		job.put("icon", "images/timerrun.png");
		data.add(job);
		JSONArray jobChildrenAction = new JSONArray();
		job.put("children", jobChildrenAction);
		
		job.put("title1", "Add");
		job.put("action1", "job add \"<<<jobId>>>\" \"<<<scriptName>>>\" \"0 30 * * * ?\" true;");
		job.put("direct1", "0");

		JSONObject jobs = JobManager.show(sessionId);
		JSONObject activatedJobs = JobManager.showActivateKeys();
		JSONObject runningJobs = JobManager.showRunningKeys();
		
		listObj = new LinkedList<>( jobs.entrySet() );
		Collections.sort( listObj, new Comparator<Map.Entry<String, Object>>() {
			@Override
			public int compare( Map.Entry<String, Object> o1, Map.Entry<String, Object> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(Map.Entry<String, Object> o : listObj) {
			
			String currentJob = o.getKey();
			
			if (ScriptManager.isGrantedToUser(sessionId, login, ""+((JSONObject) jobs.get(currentJob)).get("scriptName"))) {
			
				String imgTimer = "timer";
				
				if (activatedJobs.containsKey(currentJob)) {
					
					imgTimer = "timerrun";
					
					if (runningJobs.containsKey(currentJob)) {
						
						imgTimer = "timerrunning";
						
					}
					
				}
				
				JSONObject curJob = new JSONObject();
				curJob.put("text", currentJob);
				curJob.put("icon", "images/"+imgTimer+".png");
				jobChildrenAction.add(curJob);
				curJob.put("mql", "in editor {\n	job generate update \""+currentJob.replace("\"", "\\\"")+"\";\n};");
				curJob.put("direct", "1");
				JSONArray jobInfoChildrenAction = new JSONArray();
				curJob.put("children", jobInfoChildrenAction);
				
				curJob.put("title1", "Edit");
				curJob.put("action1", "in editor {\n	job generate update \""+currentJob.replace("\"", "\\\"")+"\";\n};");
				curJob.put("direct1", "1");
				
				curJob.put("title2", "Desactivate");
				curJob.put("action2", "job pause \""+currentJob.replace("\"", "\\\"")+"\";");
				curJob.put("direct2", "1");
				
				curJob.put("title3", "Activate");
				curJob.put("action3", "job resume \""+currentJob.replace("\"", "\\\"")+"\";");
				curJob.put("direct3", "1");
				
				curJob.put("title4", "Delete");
				curJob.put("action4", "job remove \""+currentJob.replace("\"", "\\\"")+"\";");
				curJob.put("direct4", "0");
				
				JSONObject curJobInfo1 = new JSONObject();
				String scriptName = ""+((JSONObject) jobs.get(currentJob)).get("scriptName");
				curJobInfo1.put("text", scriptName);
				curJobInfo1.put("icon", "images/mental_job.png");
				jobInfoChildrenAction.add(curJobInfo1);
				
				JSONArray mentalChildren = new JSONArray();
				curJobInfo1.put("children", mentalChildren);
				curJobInfo1.put("mql", "in editor {\n	script generate update \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct", "1");
				
				curJobInfo1.put("title1", "Edit");
				curJobInfo1.put("action1", "in editor {\n	script generate update \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct1", "1");
				
				curJobInfo1.put("title2", "Execute (clipboard)");
				curJobInfo1.put("action2", "in clipboard {\n	script generate execute \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct2", "1");
				
				curJobInfo1.put("title3", "Call (clipboard)");
				curJobInfo1.put("action3", "in clipboard {\n	script generate call \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct3", "1");
				
				curJobInfo1.put("title4", "Include (clipboard)");
				curJobInfo1.put("action4", "in clipboard {\n	script generate include \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct4", "1");
				
				curJobInfo1.put("title5", "Concat (clipboard)");
				curJobInfo1.put("action5", "in clipboard {\n	script generate sub_include \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct5", "1");

				curJobInfo1.put("title6", "Stack (clipboard)");
				curJobInfo1.put("action6", "in clipboard {\n	script generate stack \""+scriptName.replace("\"", "\\\"")+"\";\n};");
				curJobInfo1.put("direct6", "1");
				
				curJobInfo1.put("title7", "Execute immediate");
				curJobInfo1.put("action7", "eval (script generate execute \""+scriptName.replace("\"", "\\\"")+"\";)");
				curJobInfo1.put("direct7", "1");
				
				JSONObject curJobInfo2 = new JSONObject();
				curJobInfo2.put("text", ""+((JSONObject) jobs.get(currentJob)).get("pattern"));
				curJobInfo2.put("icon", "images/info.png");
				jobInfoChildrenAction.add(curJobInfo2);
				
				JSONObject curJobInfo3 = new JSONObject();
				curJobInfo3.put("text", ((""+((JSONObject) jobs.get(currentJob)).get("activate")).equals("1")?"Activate":"Desactivate"));
				curJobInfo3.put("icon", "images/info.png");
				jobInfoChildrenAction.add(curJobInfo3);
				
				JSONObject curJobInfo4 = new JSONObject();
				curJobInfo4.put("text", ""+((JSONObject) jobs.get(currentJob)).get("login"));
				curJobInfo4.put("icon", "images/info.png");
				jobInfoChildrenAction.add(curJobInfo4);
				
			}
			
		}

		job.put("title2", "Status");
		job.put("action2", "job scheduler status;");
		job.put("direct2", "1");
		
		job.put("title3", "Start");
		job.put("action3", "job scheduler start;");
		job.put("direct3", "0");
		
		job.put("title4", "Stop");
		job.put("action4", "job scheduler stop;");
		job.put("direct4", "0");
		
		job.put("title5", "Restart");
		job.put("action5", "job scheduler restart;");
		job.put("direct5", "0");
		
		JSONObject addSequence = new JSONObject();
  		addSequence.put("text", "Sequence");
  		addSequence.put("icon", "images/seq.png");
		data.add(addSequence);
		JSONArray sequenceChildren = new JSONArray();
		addSequence.put("children", sequenceChildren);
		
		addSequence.put("title1", "Add");
		addSequence.put("action1", "sequence add \"<<<key>>>\" \"<<<value>>>\";");
		addSequence.put("direct1", "0");
		
		BasicData bd = new BasicData();  bd.load(SequenceManager.showAllSeqs(sessionId));
		JSONObject seqs = bd.dataNode;

		List<Map.Entry<String, Long>> list = new LinkedList<>( seqs.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(Map.Entry<String, Long> o : list) {
			
			String currentSeq = o.getKey();
			
			JSONObject curSeq = new JSONObject();
			curSeq.put("text", currentSeq);
			curSeq.put("icon", "images/seq.png");
			sequenceChildren.add(curSeq);
			curSeq.put("mql", "in editor {\n	sequence generate update \""+currentSeq.replace("\"", "\\\"")+"\";\n};");
			curSeq.put("direct", "1");
			
			curSeq.put("title1", "Edit");
			curSeq.put("action1", "in editor {\n	sequence generate update \""+currentSeq.replace("\"", "\\\"")+"\";\n};");
			curSeq.put("direct1", "1");
			
			curSeq.put("title2", "Delete");
			curSeq.put("action2", "sequence remove \""+currentSeq.replace("\"", "\\\"")+"\";");
			curSeq.put("direct2", "0");
			
		}
		
  		JSONObject addParam = new JSONObject();
  		addParam.put("text", "Parameter");
  		addParam.put("icon", "images/conf.png");
		data.add(addParam);
		JSONArray paramChildrenAction = new JSONArray();
		addParam.put("children", paramChildrenAction);
  		
		addParam.put("title1", "Add");
		addParam.put("action1", "parameter add \"<<<param>>>\" \"<<<value>>>\" 0;");
		addParam.put("direct1", "0");

		JSONObject params = ParameterManager.showAllParams();
		list = new LinkedList<>( params.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		for(Map.Entry<String, Long> o : list) {
			
			String currentParam = o.getKey();
			
			JSONObject curParam = new JSONObject();
			curParam.put("text", currentParam);
			curParam.put("icon", "images/conf.png");
			paramChildrenAction.add(curParam);
			curParam.put("mql", "in editor {\n	parameter generate merge \""+currentParam.replace("\"", "\\\"")+"\";\n};");
			curParam.put("direct", "1");
			
			curParam.put("title1", "Edit");
			curParam.put("action1", "in editor {\n	parameter generate merge \""+currentParam.replace("\"", "\\\"")+"\";\n};");
			curParam.put("direct1", "1");
			
			curParam.put("title2", "Delete");
			curParam.put("action2", "parameter remove \""+currentParam.replace("\"", "\\\"")+"\";");
			curParam.put("direct2", "0");
			
		}

  		JSONObject fs = new JSONObject();
  		fs.put("text", "File System");
  		fs.put("icon", "images/fs.png");
		data.add(fs);
		JSONArray fsChildrenAction = new JSONArray();
		fs.put("children", fsChildrenAction);
		fs.put("mql", "metric index files 1 10;");
		fs.put("direct", "1");
		
		fs.put("title1", "Index file");
		fs.put("action1", "metric index files 1 10;");
		fs.put("direct1", "1");
		
		fs.put("title2", "Data file");
		fs.put("action2", "metric data files 1 10;");
		fs.put("direct2", "1");
		
		fs.put("title3", "Transaction logs");
		fs.put("action3", "transaction logs 10;");
		fs.put("direct3", "1");
		
		fs.put("title4", "Show log file");
		fs.put("action4", "log show;");
		fs.put("direct4", "1");

	  	return "j23i88m90m76i39t04r09y35p14a96y09e57t35"+json.toJSONString();
		
	}
	
	//Group to HTML
	@SuppressWarnings("unchecked")
	public static String develToHtml(long sessionId, SessionThread session, EnvManager env, String login, String parent_pid, String search) throws Exception {
		
		//Initialization
		JSONObject json = new JSONObject();
		JSONObject core = new JSONObject();
		json.put("core", core);
		JSONArray data = new JSONArray();
		core.put("data", data);
		boolean noSearch = false;
		
		if (search==null) search = "";
		
		search = StringFx.lrtrim(search);
		
		//Reset
		if (search.equals("") || search.equals("Search ...")) {
			
			search = "";
			noSearch = true;
			
		}
		
		JSONObject mentalScript = new JSONObject();
		mentalScript.put("text", "Script");
		mentalScript.put("icon", "images/mental.png");
		JSONObject opened = new JSONObject();
		opened.put("opened", true);
		mentalScript.put("state", opened);
		data.add(mentalScript);
		
		mentalScript.put("title1", "Add");
		mentalScript.put("action1", "script create post|get|put|delete \"<<<folder.subfolder.scriptName>>>\" false 1\n  (param\n  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n  )\n  \"description ...\"\n{\n	\n	#Your code here...;\n	\n} \"Return ...\";");
		mentalScript.put("direct1", "0");
		
		mentalScript.put("title2", "Get all scripts");
		mentalScript.put("action2", "script get_all;");
		mentalScript.put("direct2", "1");
		
		mentalScript.put("title3", "Export");
		mentalScript.put("action3", "script export_all;");
		mentalScript.put("direct3", "0");
		
		JSONArray children = null;

		children = new JSONArray();
		mentalScript.put("children", children);
		
		//Get all groups
		JSONObject userGroups = GroupManager.userGetGroup(sessionId, login);

		List<Map.Entry<String, Long>> list_group = new LinkedList<>( userGroups.entrySet() );
		Collections.sort( list_group, new Comparator<Map.Entry<String, Long>>() {
			@Override
			public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );

		for (Map.Entry<String, Long> e : list_group) {
			
			String k = e.getKey();
			
			JSONObject scripts = GroupManager.getScript(sessionId, k);

			List<Map.Entry<String, Long>> list = new LinkedList<>( scripts.entrySet() );
			Collections.sort( list, new Comparator<Map.Entry<String, Long>>() {
				@Override
				public int compare( Map.Entry<String, Long> o1, Map.Entry<String, Long> o2 )
				{
					return ( o1.getKey() ).compareTo( o2.getKey() );
				}
			} );

			if (scripts.size()>0 && !k.equals("mentdb")) {
			
				JSONObject group = new JSONObject();
				group.put("text", k);
				group.put("icon", "images/group.png");
				children.add(group);
				JSONArray groupChildren = new JSONArray();
				group.put("children", groupChildren);
				
				group.put("title1", "WARNING: Export scripts from MentDB");
				group.put("action1", "script export_group \""+k+"\";");
				group.put("direct1", "0");
				
				group.put("title2", "WARNING: Import script from File");
				group.put("action2", "script import_group \""+k+"\";");
				group.put("direct2", "0");
				
				for (Map.Entry<String, Long> ee : list) {
					
					String s = ee.getKey();
				
					boolean searchOrNot = false;
					if (!noSearch && s.toLowerCase().indexOf(search.toLowerCase())>-1) {
						searchOrNot = true;
					}
					
					if (!noSearch && !searchOrNot) {
						
						String code = (String) ScriptManager.get(sessionId, s).get("mql");
						if (code.toLowerCase().indexOf(search.toLowerCase())>-1) {
							searchOrNot = true;
						}
						
					}
					
					if (noSearch || searchOrNot) {

						JSONObject mental = new JSONObject();
						mental.put("text", s);
						
						mental.put("icon", "images/mental.png");
						groupChildren.add(mental);
						JSONArray mentalChildren = new JSONArray();
						mental.put("children", mentalChildren);
						mental.put("mql", "in editor {\n	script generate update \""+s.replace("\"", "\\\"")+"\";\n};");
						mental.put("direct", "1");
	
					}
					
				}
				
			}

		}

		JSONObject app = new JSONObject();
		app.put("text", "Application");
		app.put("icon", "images/app.png");
		data.add(app);
		JSONArray appChildrenAction = new JSONArray();
		app.put("children", appChildrenAction);

		app.put("title1", "Restart the web server");
		app.put("action1", "app webserver restart;");
		app.put("direct1", "0");
		
		JSONObject http = new JSONObject();
		http.put("text", "http");
		http.put("icon", "images/folder.png");
		appChildrenAction.add(http);
		JSONArray httpChildrenAction = new JSONArray();
		http.put("children", httpChildrenAction);

		http.put("title1", "Create application");
		http.put("action1", "app create \"http\" \"demo\" \"default\" \"100\";");
		http.put("direct1", "0");
		
		JSONObject https = new JSONObject();
		https.put("text", "https");
		https.put("icon", "images/folder.png");
		appChildrenAction.add(https);
		JSONArray httpsChildrenAction = new JSONArray();
		https.put("children", httpsChildrenAction);

		https.put("title1", "Create application");
		https.put("action1", "app create \"https\" \"demo\" \"default\" \"100\";");
		https.put("direct1", "0");
		
		JSONArray contexts = AppManager.show_context("http");
  		
  		for(int i=0;i<contexts.size();i++) {
  			
  			String u = contexts.get(i)+"";
  			
  			JSONObject context = new JSONObject();

  			context.put("text", ""+u);
  			context.put("icon", "images/app.png");
  			httpChildrenAction.add(context);
			JSONArray contextChildrenAction = new JSONArray();
			
			context.put("children", contextChildrenAction);
			context.put("mql", "app vhost show \"http\" \""+u+"\";");
			context.put("direct", "1");
			
			context.put("title1", "Show config");
			context.put("action1", "app vhost show \"http\" \""+u+"\";");
			context.put("direct1", "1");
			
			context.put("title2", "Add virtual host");
			context.put("action2", "app vhost add \"http\" \""+u+"\" \"<<<hostname>>>\";");
			context.put("direct2", "0");
			
			context.put("title3", "Delete");
			context.put("action3", "app delete \"http\" \""+u+"\";");
			context.put("direct3", "0");
			
			JSONObject vhosts = VHostManager.show(sessionId, "http", u);
	  		
	  		for(Object o : vhosts.keySet()) {
	  			
	  			String v = (String) o;

	  			JSONObject vhost = new JSONObject();
	  			vhost.put("text", v);
	  			vhost.put("icon", "images/vhost.png");
	  			contextChildrenAction.add(vhost);
				JSONArray vhostChildrenAction = new JSONArray();
				vhost.put("children", vhostChildrenAction);
				
				vhost.put("title1", "Delete");
				vhost.put("action1", "app vhost delete \"http\" \""+u+"\" \""+v+"\";");
				vhost.put("direct1", "0");
				
	  		}
  			
  		}
  		
		contexts = AppManager.show_context("https");

  		for(int i=0;i<contexts.size();i++) {
  			
  			String u = contexts.get(i)+"";
  			
  			JSONObject context = new JSONObject();

  			context.put("text", ""+u);
  			context.put("icon", "images/app.png");
  			httpsChildrenAction.add(context);
			JSONArray contextChildrenAction = new JSONArray();
			
			context.put("children", contextChildrenAction);
			context.put("mql", "app vhost show \"https\" \""+u+"\";");
			context.put("direct", "1");
			
			context.put("title1", "Show config");
			context.put("action1", "app vhost show \"https\" \""+u+"\";");
			context.put("direct1", "1");
			
			context.put("title2", "Add virtual host");
			context.put("action2", "app vhost add \"https\" \""+u+"\" \"<<<hostname>>>\";");
			context.put("direct2", "0");
			
			context.put("title3", "Delete");
			context.put("action3", "app delete \"https\" \""+u+"\";");
			context.put("direct3", "0");
			
			JSONObject vhosts = VHostManager.show(sessionId, "https", u);
	  		
	  		for(Object o : vhosts.keySet()) {
	  			
	  			String v = (String) o;

	  			JSONObject vhost = new JSONObject();
	  			vhost.put("text", v);
	  			vhost.put("icon", "images/vhost.png");
	  			contextChildrenAction.add(vhost);
				JSONArray vhostChildrenAction = new JSONArray();
				vhost.put("children", vhostChildrenAction);
				
				vhost.put("title1", "Delete");
				vhost.put("action1", "app vhost delete \"https\" \""+u+"\" \""+v+"\";");
				vhost.put("direct1", "0");

	  			
	  		}
  			
  		}
  		
  		JSONObject help = new JSONObject();
  		help.put("text", "Help");
  		help.put("icon", "images/help.png");
  		help.put("mql", "help;");
  		help.put("direct", "1");
		data.add(help);

	  	return "j23i88m90m76i39t04r09y35p14a96y09e57t65"+json.toJSONString();
		
	}
	
	//Group to HTML
	@SuppressWarnings("unchecked")
	public static String configToHtml(long sessionId, SessionThread session, EnvManager env, String login, String parent_pid) throws Exception {
		
		//Initialization
		JSONObject json = new JSONObject();
		JSONObject core = new JSONObject();
		json.put("core", core);
		JSONArray data = new JSONArray();
		core.put("data", data);
		
  		JSONObject db = new JSONObject();
  		db.put("text", "Database");
  		db.put("icon", "images/connection_db.png");
		data.add(db);
		JSONArray dbChildrenAction = new JSONArray();
		db.put("children", dbChildrenAction);
		
		db.put("title1", "Create as400 connection");
		db.put("action1", "cm set \"demo_cm_as400\" {execute \"db.as400.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"8471\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct1", "0");
		
		db.put("title2", "Create db2 connection");
		db.put("action2", "cm set \"demo_cm_db2\" {execute \"db.db2.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"50000\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"db2admin\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct2", "0");
		
		db.put("title3", "Create derby server connection");
		db.put("action3", "cm set \"demo_cm_derby_server\" {execute \"db.derby.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"1527\"\n" + 
				"	\"[database]\" \"demo/db/derby/test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};");
		db.put("direct3", "0");
		
		db.put("title4", "Create derby embedded connection");
		db.put("action4", "cm set \"demo_cm_derby_embedded\" {execute \"db.derby.embedded.config.get\"\n" + 
				"	\"[database]\" \"demo/db/derby/test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};");
		db.put("direct4", "0");
		
		db.put("title5", "Create firebird connection");
		db.put("action5", "cm set \"demo_cm_firebird\" {execute \"db.firebird.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"3050\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"SYSDBA\"\n" + 
				"	\"[password]\" \"masterkey\"\n" + 
				";};");
		db.put("direct5", "0");
		
		db.put("title6", "Create h2 server connection");
		db.put("action6", "cm set \"demo_cm_h2_server\" {execute \"db.h2.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"9091\"\n" + 
				"	\"[database]\" \"demo/db/h2/test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};");
		db.put("direct6", "0");
		
		db.put("title7", "Create h2 embedded connection");
		db.put("action7", "cm set \"demo_cm_h2_embedded\" {execute \"db.h2.embedded.config.get\"\n" + 
				"	\"[database]\" \"demo/db/h2/test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};");
		db.put("direct7", "0");
		
		db.put("title8", "Create hsql server connection");
		db.put("action8", "cm set \"demo_cm_h2_server\" {execute \"db.hsql.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"9001\"\n" + 
				"	\"[database]\" \"demo/db/hsql/test_db\"\n" + 
				"	\"[user]\" \"SA\"\n" + 
				"	\"[password]\" \"SA\"\n" + 
				";};");
		db.put("direct8", "0");
		
		db.put("title9", "Create hsql embedded connection");
		db.put("action9", "cm set \"demo_cm_h2_embedded\" {execute \"db.hsql.embedded.config.get\"\n" + 
				"	\"[database]\" \"demo/db/hsql/test_db\"\n" + 
				"	\"[user]\" \"SA\"\n" + 
				"	\"[password]\" \"SA\"\n" + 
				";};");
		db.put("direct9", "0");
		
		db.put("title10", "Create mysql connection");
		db.put("action10", "cm set \"demo_cm_mysql\" {execute \"db.mysql.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"3306\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct10", "0");
		
		db.put("title11", "Create oracle connection");
		db.put("action11", "cm set \"demo_cm_oracle\" {execute \"db.oracle.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"1522\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"sys\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct11", "0");
		
		db.put("title12", "Create postgresql connection");
		db.put("action12", "cm set \"demo_cm_postgresql\" {execute \"db.postgresql.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"5432\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"postgres\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct12", "0");
		
		db.put("title13", "Create sqlserver connection");
		db.put("action13", "cm set \"demo_cm_sqlserver\" {execute \"db.sqlserver.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"1195\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		db.put("direct13", "0");

		JSONArray cms = CMManager.show(sessionId, null);
		
		for(int i=0;i<cms.size();i++) {
			
			String currentCm = (String) cms.get(i);
			
			JSONObject cmobj = CMManager.get(sessionId, currentCm);
			String type = (String) cmobj.get("type");
			
			switch (type) {
			case "db": 

				String subType = (String) cmobj.get("subType");
				String defaultSchema = (String) cmobj.get("defaultSchema");
				
				if (subType!=null && !AtomFx.find(DQManager.VALID_DB_LIST, subType, ",").equals("0")) {
					
					JSONObject curCm = new JSONObject();
					curCm.put("text", currentCm);
					curCm.put("icon", "images/connection_db.png");
					dbChildrenAction.add(curCm);
					curCm.put("mql", "sql show tables \""+currentCm.replace("\"", "\\\"")+"\";");
					curCm.put("direct", "1");
					JSONArray tablesChildrenAction = new JSONArray();
					curCm.put("children", tablesChildrenAction);
					
					curCm.put("title1", "Ping ...");
					curCm.put("action1", "sql connect \"pingSession\" {cm get \""+currentCm.replace("\"", "\\\"")+"\"};\nsql disconnect \"pingSession\";");
					curCm.put("direct1", "1");
					
					curCm.put("title2", "Select ...");
					curCm.put("action2", "sql select \""+currentCm.replace("\"", "\\\"")+"\" \"select * from table\" \"table\";");
					curCm.put("direct2", "0");
					
					curCm.put("title3", "show table");
					curCm.put("action3", "sql show tables \""+currentCm.replace("\"", "\\\"")+"\";");
					curCm.put("direct3", "1");
					
					curCm.put("title4", "Get config");
					curCm.put("action4", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
					curCm.put("direct4", "1");
					
					curCm.put("title5", "Generate update");
					curCm.put("action5", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
					curCm.put("direct5", "1");
					
					curCm.put("title6", "Delete");
					curCm.put("action6", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
					curCm.put("direct6", "0");
					
					if (session.env.allSubObjects.containsKey(currentCm)) {
						
						JSONArray a = session.env.allSubObjects.get(currentCm);
						
						for(int iTable = 0;iTable<a.size();iTable++) {
							
							String tablename = (String) a.get(iTable); 
							
							JSONObject curtable = new JSONObject();
							curtable.put("text", tablename);
							curtable.put("icon", "images/connection_table.png");
							tablesChildrenAction.add(curtable);
							curtable.put("mql", "sql select \""+currentCm.replace("\"", "\\\"")+"\" \""+SQLQueryManager.show_data(subType, defaultSchema, tablename)+"\" \""+tablename.replace("\"", "\\\"")+"\";");
							curtable.put("direct", "1");
							
							curtable.put("title1", "Show 500 rows");
							curtable.put("action1", "sql select \""+currentCm.replace("\"", "\\\"")+"\" \""+SQLQueryManager.show_data(subType, defaultSchema, tablename)+"\" \""+tablename.replace("\"", "\\\"")+"\";");
							curtable.put("direct1", "0");
							
							curtable.put("title2", "Show desc");
							curtable.put("action2", "in out_editor {\n	sql show desc \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct2", "1");
							
							curtable.put("title3", "GEN HANDLE > PARSE AND ACTION");
							curtable.put("action3", "in editor {\n	scrud parse \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct3", "1");
							
							curtable.put("title4", "GEN HANDLE > DB TO DB");
							curtable.put("action4", "in editor {\n	scrud db_to_db \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct4", "1");
							
							curtable.put("title5", "GEN HANDLE > CSV TO DB");
							curtable.put("action5", "in editor {\n	scrud csv_to_db \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\" \"/Users/jimmitry/Desktop/file.csv\" \",\" \"'\" \"A,B,C\";\n}");
							curtable.put("direct5", "0");
							
							curtable.put("title6", "GEN WS > SELECT");
							curtable.put("action6", "in editor {\n	scrud select \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct6", "1");
							
							curtable.put("title7", "GEN WS > INSERT");
							curtable.put("action7", "in editor {\n	scrud insert \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct7", "1");
							
							curtable.put("title8", "GEN WS > UPDATE");
							curtable.put("action8", "in editor {\n	scrud update \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct8", "1");
							
							curtable.put("title9", "GEN WS > DELETE");
							curtable.put("action9", "in editor {\n	scrud delete \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct9", "1");
							
							curtable.put("title10", "GEN HANDLE > EXPORT FILE");
							curtable.put("action10", "in editor {\n	scrud export \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct10", "1");
							
							curtable.put("title11", "GEN SQL > CREATE TABLE");
							curtable.put("action11", "in editor {\n	scrud create \""+currentCm.replace("\"", "\\\"")+"\" \""+tablename.replace("\"", "\\\"")+"\";\n}");
							curtable.put("direct11", "1");
							
							curtable.put("title12", "GEN APP > GENERATE SCRUD OPERATIONS");
							curtable.put("action12", "in editor {\n	execute \"app.100.scrud.generate.exe\" \n		\"[app_name]\" \"APP_NAME\"\n		\"[prefix]\" \"app.100.template.default.actions.demo.\" \n		\"[database]\" \""+currentCm.replace("\"", "\\\"")+"\" \n		\"[table]\" \""+tablename.replace("\"", "\\\"")+"\"\n	;\n};");
							curtable.put("direct12", "0");
							
							
						}
						
					}
					
				} else {
				
					JSONObject curCm = new JSONObject();
					curCm.put("text", currentCm);
					curCm.put("icon", "images/connection_db_w.png");
					dbChildrenAction.add(curCm);
					curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
					curCm.put("direct", "1");
					
					curCm.put("title1", "Ping ...");
					curCm.put("action1", "sql connect \"pingSession\" {cm get \""+currentCm.replace("\"", "\\\"")+"\"};\nsql disconnect \"pingSession\";");
					curCm.put("direct1", "1");
					
					curCm.put("title2", "Select ...");
					curCm.put("action2", "in out_editor {\n	sql show data \""+currentCm.replace("\"", "\\\"")+"\" \"select * from table\" \"table\";\n}");
					curCm.put("direct2", "0");
					
					curCm.put("title3", "Get config");
					curCm.put("action3", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
					curCm.put("direct3", "1");
					
					curCm.put("title4", "Generate update");
					curCm.put("action4", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
					curCm.put("direct4", "1");
					
					curCm.put("title5", "Delete");
					curCm.put("action5", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
					curCm.put("direct5", "0");
					
				}
			break;
			}
			
		}
		
  		JSONObject cmFile = new JSONObject();
  		cmFile.put("text", "Remote file");
  		cmFile.put("icon", "images/connection_file.png");
		data.add(cmFile);
		JSONArray cmFileChildrenAction = new JSONArray();
		cmFile.put("children", cmFileChildrenAction);
		
		cmFile.put("title1", "Create cifs connection");
		cmFile.put("action1", "cm set \"demo_cm_cifs\" {execute \"file.remote.cifs.config.get\"\n" + 
				"	\"[hostname]\" \"192.168.220.130\"\n" + 
				"	\"[port]\" \"445\"\n" + 
				"	\"[domain]\" \"domain\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[readTimeout]\" \"30000\"\n" + 
				";};");
		cmFile.put("direct1", "0");
		
		cmFile.put("title2", "Create file connection");
		cmFile.put("action2", "cm set \"demo_cm_file\" {execute \"file.local.config.get\"\n" + 
				"	\"[dir]\" \"/Users/jimmitry/Desktop\"\n" + 
				";};");
		cmFile.put("direct2", "0");
		
		cmFile.put("title3", "Create ftp connection");
		cmFile.put("action3", "cm set \"demo_cm_ftp\" {execute \"file.remote.ftp.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"21\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};");
		cmFile.put("direct3", "0");
		
		cmFile.put("title4", "Create ftps connection");
		cmFile.put("action4", "cm set \"demo_cm_ftps\" {execute \"file.remote.ftps.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"21\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[protocol]\" \"TLS\"\n" + 
				"	\"[isImplicit]\" false\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				"	\"[dataTimeout]\" 30000\n" + 
				"	\"[keepAliveTimeout]\" 300\n" + 
				";};");
		cmFile.put("direct4", "0");
		
		cmFile.put("title5", "Create sftp connection");
		cmFile.put("action5", "cm set \"demo_cm_sftp\" {execute \"file.remote.sftp.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"22\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				"	\"[dataTimeout]\" 30000\n" + 
				";};");
		cmFile.put("direct5", "0");
		
		cmFile.put("title6", "Create ssh connection");
		cmFile.put("action6", "cm set \"demo_cm_ssh\" {execute \"file.remote.ssh.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"22\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				";};");
		cmFile.put("direct6", "0");
		
		for(int i=0;i<cms.size();i++) {
			
			String currentCm = (String) cms.get(i);
			
			JSONObject cmobj = CMManager.get(sessionId, currentCm);
			String type = (String) cmobj.get("type");
			
			switch (type) {
			case "ftp": 
				
				JSONObject curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_ftp.gif");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > DOWNLOAD FILES");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.ftp.download.post\") \"demo_cm_ftp\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "GEN > UPLOAD FILES");
				curCm.put("action3", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.ftp.upload.post\") \"demo_cm_ftp\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "Generate update");
				curCm.put("action4", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct4", "1");
				
				curCm.put("title5", "Delete");
				curCm.put("action5", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct5", "0");
				
			break;
			case "ftps": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_ftps.gif");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > DOWNLOAD FILES");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.ftps.download.post\") \"demo_cm_ftps\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "GEN > UPLOAD FILES");
				curCm.put("action3", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.ftps.upload.post\") \"demo_cm_ftps\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "Generate update");
				curCm.put("action4", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct4", "1");
				
				curCm.put("title5", "Delete");
				curCm.put("action5", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct5", "0");
				
			break;
			case "sftp": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_sftp.gif");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > DOWNLOAD FILES");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.sftp.download.post\") \"demo_cm_sftp\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "GEN > UPLOAD FILES");
				curCm.put("action3", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.sftp.upload.post\") \"demo_cm_sftp\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "Generate update");
				curCm.put("action4", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct4", "1");
				
				curCm.put("title5", "Delete");
				curCm.put("action5", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct5", "0");
				
			break;
			case "ssh": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_ssh.png");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "Generate update");
				curCm.put("action2", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "Delete");
				curCm.put("action3", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct3", "0");
				
			break;
			case "cifs": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_cifs.png");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "Generate update");
				curCm.put("action2", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "Delete");
				curCm.put("action3", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct3", "0");
				
			break;
			case "file": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_file.png");
				cmFileChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > PARSE DIRECTORY");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.parse_dir.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "GEN > PARSE TEXT FILE");
				curCm.put("action3", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.parse_text.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "GEN > PARSE JSON OBJ");
				curCm.put("action4", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.json.obj.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct4", "1");
				
				curCm.put("title5", "GEN > PARSE JSON ARRAY");
				curCm.put("action5", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.json.array.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct5", "1");
				
				curCm.put("title6", "GEN > PARSE XML ITEM");
				curCm.put("action6", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.xml.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct6", "1");
				
				curCm.put("title7", "GEN > PARSE CSV FILE");
				curCm.put("action7", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.csv.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct7", "1");
				
				curCm.put("title8", "GEN > PARSE Excel FILE");
				curCm.put("action8", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.excel.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct8", "1");
				
				curCm.put("title9", "GEN > PARSE ExcelX FILE");
				curCm.put("action9", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.excelx.parse.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct9", "1");
				
				curCm.put("title10", "GEN > COPY TEXT FILE");
				curCm.put("action10", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.copy_text.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct10", "1");
				
				curCm.put("title11", "GEN > COPY BINARY FILE");
				curCm.put("action11", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.copy_bytes.post\") \"demo_cm_file\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct11", "1");
				
				curCm.put("title12", "Generate update");
				curCm.put("action12", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct12", "1");
				
				curCm.put("title13", "Delete");
				curCm.put("action13", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct13", "0");
				
			break;
			}
			
		}
		
  		JSONObject cmMail = new JSONObject();
  		cmMail.put("text", "Mail");
  		cmMail.put("icon", "images/connection_smtp.png");
		data.add(cmMail);
		JSONArray cmMailChildrenAction = new JSONArray();
		cmMail.put("children", cmMailChildrenAction);
		
		cmMail.put("title1", "Create imap connection");
		cmMail.put("action1", "cm set \"demo_cm_imap\" {execute \"mail.imap.config.get\"\n" + 
				"	\"[hostname]\" \"imap.gmail.com\"\n" + 
				"	\"[port]\" \"993\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[auth]\" false\n" + 
				"	\"[tls]\" false\n" + 
				"	\"[ssl]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};");
		cmMail.put("direct1", "0");
		
		cmMail.put("title2", "Create pop3 connection");
		cmMail.put("action2", "cm set \"demo_cm_pop3\" {execute \"mail.pop3.config.get\"\n" + 
				"	\"[hostname]\" \"pop.gmail.com\"\n" + 
				"	\"[port]\" \"995\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[tls]\" false\n" + 
				"	\"[ssl]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};");
		cmMail.put("direct2", "0");
		
		cmMail.put("title3", "Create smtp connection");
		cmMail.put("action3", "cm set \"demo_cm_smtp\" {execute \"mail.smtp.config.get\"\n" + 
				"	\"[hostname]\" \"smtp.gmail.com\"\n" + 
				"	\"[port]\" \"465\"\n" + 
				"	\"[sender]\" \"your-account@gmail.com\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[authentication]\" true\n" + 
				"	\"[tls]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};");
		cmMail.put("direct3", "0");
		
		for(int i=0;i<cms.size();i++) {
			
			String currentCm = (String) cms.get(i);
			
			JSONObject cmobj = CMManager.get(sessionId, currentCm);
			String type = (String) cmobj.get("type");
			
			switch (type) {
			case "imap": 
				
				JSONObject curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_imap.png");
				cmMailChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > DOWNLOAD MAILS");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.imap.download.post\") \"demo_cm_imap\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "Generate update");
				curCm.put("action3", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "Delete");
				curCm.put("action4", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct4", "0");
				
			break;
			case "pop3": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_pop3.png");
				cmMailChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > DOWNLOAD MAILS");
				curCm.put("action2", "in editor {\n" + 
						"	string replace (script generate create \"demo.file.pop3.download.post\") \"demo_cm_pop3\" \""+currentCm.replace("\"", "\\\"")+"\";\n" + 
						"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "Generate update");
				curCm.put("action3", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct3", "1");
				
				curCm.put("title4", "Delete");
				curCm.put("action4", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct4", "0");
				
			break;
			case "smtp": 
				
				curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_smtp.png");
				cmMailChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "Generate update");
				curCm.put("action2", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct2", "1");
				
				curCm.put("title3", "Delete");
				curCm.put("action3", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct3", "0");
				
			break;
			}
			
		}
		
  		JSONObject cm = new JSONObject();
  		cm.put("text", "MentDB");
  		cm.put("icon", "images/connection_mentdb.png");
		data.add(cm);
		JSONArray cmChildrenAction = new JSONArray();
		cm.put("children", cmChildrenAction);
		
		cm.put("title1", "Create mentdb connection");
		cm.put("action1", "cm set \"demo_cm_mentdb\" {execute \"mentdb.remote.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"9998\"\n" + 
				"	\"[key]\" \"pwd\"\n" + 
				"	\"[user]\" \"admin\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[readTimeout]\" \"30000\"\n" + 
				"	\"[subTunnels]\" (mql {\n" + 
				"\n" + 
				"tunnel execute_hot \"tunnelId1\" {cm get \"demo_cm_mentdb\";} (mql {\n" + 
				"	tunnel execute_hot \"tunnelId2\" {cm get \"demo_cm_mentdb\";} (mql {\n" + 
				"	\n" + 
				"		[MQL_TO_REPLACE]\n" + 
				"		\n" + 
				"	});\n" + 
				"});\n" + 
				"\n" + 
				"})\n" + 
				";};");
		cm.put("direct1", "0");
		
		for(int i=0;i<cms.size();i++) {
			
			String currentCm = (String) cms.get(i);
			
			JSONObject cmobj = CMManager.get(sessionId, currentCm);
			String type = (String) cmobj.get("type");
			
			switch (type) {
			case "mentdb": 
				
				JSONObject curCm = new JSONObject();
				curCm.put("text", currentCm);
				curCm.put("icon", "images/connection_mentdb.png");
				cmChildrenAction.add(curCm);
				curCm.put("mql", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct", "1");
				
				curCm.put("title1", "Get");
				curCm.put("action1", "cm get \""+currentCm.replace("\"", "\\\"")+"\"");
				curCm.put("direct1", "1");
				
				curCm.put("title2", "GEN > EXECUTE HOT MQL");
				curCm.put("action2", "-> \"[local_var1]\" \"data1\";\n" + 
						"-> \"[local_var2]\" \"data2\";\n" + 
						"-> \"[result1]\" (tunnel execute_hot \"session1\" {cm get \""+currentCm.replace("\"", "\\\"")+"\";} (concat \n" + 
						"	\"-> \\\"[remote_var1]\\\" \" (mql encode [local_var1]) \";\"\n" + 
						"	\"-> \\\"[remote_var2]\\\" \" (mql encode [local_var2]) \";\"\n" + 
						"	(mql {\n" + 
						"		concat [remote_var1] \":\" [remote_var2]\n" + 
						"	})\n" + 
						"));");
				curCm.put("direct2", "0");
				
				curCm.put("title3", "GEN > EXECUTE MQL");
				curCm.put("action3", "tunnel connect \"session1\" {cm get \""+currentCm.replace("\"", "\\\"")+"\";};\n" + 
						"-> \"[result1]\" (tunnel execute \"session1\" (mql {\n" + 
						"	name\n" + 
						"}));\n" + 
						"-> \"[local_var1]\" \"data1\";\n" + 
						"-> \"[local_var2]\" \"data2\";\n" + 
						"-> \"[result2]\" (tunnel execute \"session1\" (concat \n" + 
						"	\"-> \\\"[remote_var1]\\\" \" (mql encode [local_var1]) \";\"\n" + 
						"	\"-> \\\"[remote_var2]\\\" \" (mql encode [local_var2]) \";\"\n" + 
						"	(mql {\n" + 
						"		concat [remote_var1] \":\" [remote_var2]\n" + 
						"	})\n" + 
						"));\n" + 
						"tunnel disconnect \"session1\";\n" + 
						"concat [result1] \">\" [result2]");
				curCm.put("direct3", "0");
				
				curCm.put("title4", "Generate update");
				curCm.put("action4", "in editor {cm generate_update \""+currentCm.replace("\"", "\\\"")+"\"};");
				curCm.put("direct4", "1");
				
				curCm.put("title5", "Delete");
				curCm.put("action5", "cm remove \""+currentCm.replace("\"", "\\\"")+"\";");
				curCm.put("direct5", "0");
				
			break;
			}
			
		}

	  	return "j23i88m90m76i39t04r09y35p14a96y09e57t66"+json.toJSONString();
		
	}
	
	//Show a group
	public static JSONObject get(long sessionId, String groupName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		}
		
		return Record.getNode(sessionId, "G["+groupName+"]");
		
	}
	
	//Get user
	public static JSONObject getUser(long sessionId, String groupName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		}
		
		JSONObject bd = Record.getNode(sessionId, "G["+groupName+"]");
		
		return (JSONObject) bd.get("users");
		
	}
	
	//Get script
	public static JSONObject getScript(long sessionId, String groupName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		}
		
		if (groupName.equals("sys")) 
			return ScriptManager.show(sessionId);
		else {
			
			JSONObject bd = Record.getNode(sessionId, "G["+groupName+"]");
			return (JSONObject) bd.get("scripts");
			
		}
		
		
	}
	
	//Generate an error if the user does not have rights
	public static void generateErrorIfNotGranted(long sessionId, String group, String msg) throws Exception {
		
		//Initialization
		String user = "mentdb";
		
		if (sessionId>0) {
			user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
		}
		
		if (!user.equals("mentdb") && 
				!GroupManager.isGrantedUser(sessionId, group, user) && 
				!GroupManager.isGrantedUser(sessionId, "sys", user)) {
			
			throw new Exception("Sorry, '"+user+"' is not in '"+group+"' group ("+msg+")."); 
			
		}
		
	}
	
	//Add a group
	@SuppressWarnings("unchecked")
	public static void add(long sessionId, String user, String groupName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> already exist.");
			
		} else {
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G[]");
			
			recNode.put(groupName, 0);
			
			Record.update(sessionId, "G[]", recNode.toJSONString());
		
			//Build the value
			JSONObject group = new JSONObject();
			group.put("k", groupName);
			if (user.equals("mentdb")) group.put("c", "admin");
			else group.put("c", user);
			group.put("users", new JSONObject());
			group.put("scripts", new JSONObject());
			
			//Add the record
			Record.add(sessionId, "G["+groupName+"]", group.toJSONString());
			
			if (!user.equals("mentdb")) {
			
				//Grant the user to his group
				if (!GroupManager.isGrantedUser(sessionId, groupName, user)) {
					
					GroupManager.grantUser(sessionId, groupName, user);
				
				}
				
			}
		
		}
		
	}
	
	//Delete a group
	public static void delete(long sessionId, String groupName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else {
			
			if (groupName.equals("sys") 
					|| groupName.equals("lib")
					|| groupName.equals("mentdb")
					|| groupName.equals("ai")
					|| groupName.equals("admin")
					|| groupName.equals("brain")
					|| groupName.equals("sequence")
					|| groupName.equals("parameter")
					|| groupName.equals("job")
					|| groupName.equals("concentration")
					|| groupName.equals("node_w") 
					|| groupName.equals("public")
					|| groupName.equals("api-mql")
					|| groupName.equals("api-ai") 
					|| groupName.equals("api-rest")) {
				
				throw new Exception("Sorry, the group <"+groupName+"> is a system group.");
				
			}
			
			//Get the node
			JSONObject rec = Record.getNode(sessionId, "G["+groupName+"]");
			
			String creator = rec.get("c")+"";
			
			String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
			
			if (!user.equals("mentdb") && !creator.equals(user) && !isGrantedUser(sessionId, "sys", user)) {
				
				throw new Exception("Sorry, only '"+creator+"' can delete the group '"+groupName+"'."); 
			
			}
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G[]");
			
			recNode.remove(groupName);
			
			Record.update(sessionId, "G[]", recNode.toJSONString());
		
			//Add the record
			Record.remove(sessionId, "G["+groupName+"]");
		
		}
		
	}
	
	//Grant user
	@SuppressWarnings("unchecked")
	public static void grantUser(long sessionId, String groupName, String login) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (login.length()==0) {
			
			throw new Exception("Sorry, the login is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!UserManager.exist(sessionId, login)) {
			
			throw new Exception("Sorry, the user <"+login+"> does not exist.");
			
		} else {
			
			if (isGrantedUser(sessionId, groupName, login)) {
				
				throw new Exception("Sorry, the user is already granted.");
				
			}
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			
			String creator = recNode.get("c")+"";
			
			String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
			
			if (!user.equals("mentdb") && !creator.equals(user) && !isGrantedUser(sessionId, "sys", user)) {
				
				throw new Exception("Sorry, only '"+creator+"' can grant the group '"+groupName+"'."); 
			
			}
			
			((JSONObject) recNode.get("users")).put(login, 0);
			
			Record.update(sessionId, "G["+groupName+"]", recNode.toJSONString());
			
			//Add into the user object
			//Get the node
			JSONObject userRecNode = Record.getNode(sessionId, "U["+login.toLowerCase()+"]");
			((JSONObject) userRecNode.get("groups")).put(groupName, 0);
			Record.update(sessionId, "U["+login.toLowerCase()+"]", userRecNode.toJSONString());
		
		}
		
	}
	
	//Ungrant user
	public static void ungrantUser(long sessionId, String groupName, String login) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (login.length()==0) {
			
			throw new Exception("Sorry, the login is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!UserManager.exist(sessionId, login)) {
			
			throw new Exception("Sorry, the user <"+login+"> does not exist.");
			
		} else {
			
			if (!isGrantedUser(sessionId, groupName, login)) {
				
				throw new Exception("Sorry, the user is not granted to the group '"+groupName+"'.");
				
			}
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			
			String creator = recNode.get("c")+"";
			
			String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
			
			if (!user.equals("mentdb") && !creator.equals(user) && !isGrantedUser(sessionId, "sys", user)) {
				
				throw new Exception("Sorry, only '"+creator+"' can ungrant the group '"+groupName+"'."); 
			
			}
			
			((JSONObject) recNode.get("users")).remove(login);
			
			Record.update(sessionId, "G["+groupName+"]", recNode.toJSONString());
			
			//Delete from the user object
			//Get the node
			JSONObject userRecNode = Record.getNode(sessionId, "U["+login.toLowerCase()+"]");
			((JSONObject) userRecNode.get("groups")).remove(groupName);
			Record.update(sessionId, "U["+login.toLowerCase()+"]", userRecNode.toJSONString());
		
		}
		
	}
	
	//Check if a user is granted
	public static boolean isGrantedUser(long sessionId, String groupName, String login) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (login.length()==0) {
			
			throw new Exception("Sorry, the login is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!UserManager.exist(sessionId, login)) {
			
			throw new Exception("Sorry, the user <"+login+"> does not exist.");
			
		} else {
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			
			if (((JSONObject) recNode.get("users")).containsKey(login)) return true;
			else return false;
		
		}
		
	}
	
	//Grant script
	public static void grantAllScript(long sessionId, String groupName, String scriptNameStartsWith, boolean force) throws Exception {
		
		JSONObject scripts = ScriptManager.show(sessionId);
		
		for(Object o: scripts.keySet()) {
			
			String scriptName = (String) o;
			
			if (scriptName.startsWith(scriptNameStartsWith)) {
				
				if (!GroupManager.isGrantedScript(sessionId, groupName, scriptName)) {
					GroupManager.grantScript(sessionId, groupName, scriptName, force);
				}
				
			}
			
		}
		
	}
	
	//Ungrant script
	public static void ungrantAllScript(long sessionId, String groupName, String scriptNameStartsWith) throws Exception {
		
		JSONObject scripts = ScriptManager.show(sessionId);
		
		for(Object o: scripts.keySet()) {
			
			String scriptName = (String) o;
			
			if (scriptName.startsWith(scriptNameStartsWith)) {
				
				if (GroupManager.isGrantedScript(sessionId, groupName, scriptName)) {
					GroupManager.ungrantScript(sessionId, groupName, scriptName);
				}
				
			}
			
		}
		
	}
	
	//Grant script
	@SuppressWarnings("unchecked")
	public static void grantScript(long sessionId, String groupName, String scriptName, boolean force) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (scriptName.length()==0) {
			
			throw new Exception("Sorry, the script name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!ScriptManager.exist(sessionId, scriptName)) {
			
			throw new Exception("Sorry, the script <"+scriptName+"> does not exist.");
			
		} else {
			
			if (isGrantedScript(sessionId, groupName, scriptName)) {
				
				throw new Exception("Sorry, the script is already granted.");
				
			}
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			
			if (!force) {
				
				String user = SessionThreadAgent.allServerThread.get(sessionId).serverThread.user;
			
				if (!user.equals("mentdb") && !isGrantedUser(sessionId, "sys", user) && !ScriptManager.isGrantedToUser(sessionId, user, scriptName)) {
					
					throw new Exception("Sorry, the script '"+scriptName+"' is not granted to the user '"+user+"'."); 
				
				}
				
			}
			
			((JSONObject) recNode.get("scripts")).put(scriptName, 0);
			
			Record.update(sessionId, "G["+groupName+"]", recNode.toJSONString());
			
			//Add into the script object
			//Get the node
			JSONObject scriptRecNode = Record.getNode(sessionId, "script["+scriptName+"]");
			((JSONObject) scriptRecNode.get("groups")).put(groupName, 0);
			Record.update(sessionId, "script["+scriptName+"]", scriptRecNode.toJSONString());
		
		}
		
	}
	
	//Ungrant script
	public static void ungrantScript(long sessionId, String groupName, String scriptName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}

		if (scriptName.length()==0) {
			
			throw new Exception("Sorry, the script name is required.");
			
		}

		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!ScriptManager.exist(sessionId, scriptName)) {
			
			throw new Exception("Sorry, the script <"+scriptName+"> does not exist.");
			
		} else {

			if (!isGrantedScript(sessionId, groupName, scriptName)) {

				throw new Exception("Sorry, the script is not granted.");
				
			}

			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			((JSONObject) recNode.get("scripts")).remove(scriptName);

			Record.update(sessionId, "G["+groupName+"]", recNode.toJSONString());

			//Delete from the script object
			//Get the node
			JSONObject scriptRecNode = Record.getNode(sessionId, "script["+scriptName+"]");
			((JSONObject) scriptRecNode.get("groups")).remove(groupName);
			Record.update(sessionId, "script["+scriptName+"]", scriptRecNode.toJSONString());

		}
		
	}
	
	//Check if a script is granted
	public static boolean isGrantedScript(long sessionId, String groupName, String scriptName) throws Exception {
		
		if (groupName.length()==0) {
			
			throw new Exception("Sorry, the group name is required.");
			
		}
		
		if (scriptName.length()==0) {
			
			throw new Exception("Sorry, the script name is required.");
			
		}
		
		if (!exist(sessionId, groupName)) {
			
			throw new Exception("Sorry, the group <"+groupName+"> does not exist.");
			
		} else if (!ScriptManager.exist(sessionId, scriptName)) {
			
			throw new Exception("Sorry, the script <"+scriptName+"> does not exist.");
			
		} else {
			
			//Get the node
			JSONObject recNode = Record.getNode(sessionId, "G["+groupName+"]");
			
			if (((JSONObject) recNode.get("scripts")).containsKey(scriptName)) return true;
			else return false;
			
		}
		
	}
	
}
