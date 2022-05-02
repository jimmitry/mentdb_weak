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

package re.jpayet.mentdb.ext.job;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import static org.quartz.JobBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.cluster.ClusterManager;
import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.ext.mail.SmtpManager;
import re.jpayet.mentdb.ext.script.ScriptManager;
import re.jpayet.mentdb.ext.script.StackManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;

//The job class
public class JobManager implements Job {
	
	//Initialization
	public static Scheduler scheduler = null;
	
	//The constructor
	public JobManager() {
		
	}
	
	//Initialize the job manager
	public static void init() throws Exception {

	}
	
	@SuppressWarnings("unchecked")
	public static void add(String key, String scriptName, String pattern, String activate, String login) throws Exception {
		
		synchronized ("JOB["+key+"]") {
		
			if (Record2.countRows("mql_job", "JOB["+key+"]")>0) {
				
				throw new Exception("Sorry, the job "+key+" already exist.");
				
			}
			
			//Generate an error if the script does not exist
			if (!ScriptManager.exist(scriptName)) {
	
				throw new Exception("Sorry, the script "+scriptName+" does not exist.");
	
			}
			
			//Check the pattern
			try {
				
				new CronExpression(pattern);
				
			} catch (Exception e) {
				
				throw new Exception("Sorry, the job pattern "+pattern+" is not valid.");
				
			}
			
			if (!ScriptManager.isGrantedToUser(login, scriptName)) {
				throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
			}
			
			if (login.equals("mentdb")) login = "admin";
			
			JSONObject node = new JSONObject();
			node.put("id", key);
			node.put("scriptName", scriptName);
			node.put("pattern", pattern);
			node.put("activate", activate);
			node.put("login", login);
			Record2.add("mql_job","JOB["+key+"]", node.toJSONString());
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void update(String login, String key, String scriptName, String pattern, String activate) throws Exception {
		
		synchronized ("JOB["+key+"]") {

			if (Record2.countRows("mql_job", "JOB["+key+"]")==0) {
	
				throw new Exception("Sorry, the job "+key+" does not exist.");
				
			}
			
			//Generate an error if the script does not exist
			if (!ScriptManager.exist(scriptName)) {
	
				throw new Exception("Sorry, the script "+scriptName+" does not exist.");
	
			}
			
			//Check the pattern
			try {
				
				new CronExpression(pattern);
				
			} catch (Exception e) {
				
				throw new Exception("Sorry, the job pattern "+pattern+" is not valid.");
				
			}
			
			if (!ScriptManager.isGrantedToUser(login, scriptName)) {
				throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
			}
			
			JSONObject job = Record2.getNode("JOB["+key+"]");
			
			JSONObject node = new JSONObject();
			node.put("id", key);
			node.put("scriptName", scriptName);
			node.put("pattern", pattern);
			node.put("activate", activate);
			node.put("login", job.get("login"));
			Record2.update("JOB["+key+"]", node.toJSONString());
		}
		
	}
	
	public static void delete(String login, String key) throws Exception {
		
		synchronized ("JOB["+key+"]") {

			if (Record2.countRows("mql_job", "JOB["+key+"]")==0) {
	
				throw new Exception("Sorry, the job "+key+" does not exist.");
				
			}
			
			JSONObject job = Record2.getNode("JOB["+key+"]");
			
			String scriptName = (String) job.get("scriptName");
			if (!ScriptManager.isGrantedToUser(login, scriptName)) {
				throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
			}
			
			Record2.remove("JOB["+key+"]");
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject show() throws Exception {
		
		JSONObject result = new JSONObject();
		
		ArrayList<JSONObject> list = Record2.getRows("mql_job");
		
		for(JSONObject job : list) {
			
			result.put((String) job.get("id"), job);
			
		}
		
		return result;
		
	}
	
	public static boolean exist(String key) throws Exception {
		
		if (Record2.getNode("JOB["+key+"]")==null) {
			
			return false;
			
		} else {
			
			return true;
			
		}
		
	}
	
	//Generate update
	public static String generateUpdate(String login, String key) throws Exception {
		
		//Get the JOB object
		JSONObject job = Record2.getNode("JOB["+key+"]");
		
		//Generate an error if the parameter does not exist
		if (job==null) {

			throw new Exception("Sorry, the job "+key+" does not exist.");

		}
		
		String scriptName = (String) job.get("scriptName");
		if (!ScriptManager.isGrantedToUser(login, scriptName)) {
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
		}
		
		return "job update \""+key+"\" \""+job.get("scriptName")+"\" \""+job.get("pattern")+"\" "+((job.get("activate")+"").equals("1")?"true":"false")+";\n"
				+ "job pause \""+key+"\";\n"
				+ "job resume \""+key+"\";";
		
	}

	//Show all jobs
	@SuppressWarnings("unchecked")
	public static JSONArray showActivate() throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
			
		//List all jobs into the scheduler
		if (scheduler!=null) for(String group: scheduler.getJobGroupNames()) {
			
		   for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
	
				JSONArray loc = new JSONArray();
	
				loc.add((jobKey+"").substring(6));
				loc.add(scheduler.getJobDetail(jobKey).getJobDataMap().getString("scriptName")+"");
				loc.add(scheduler.getJobDetail(jobKey).getJobDataMap().getString("pattern")+"");
				loc.add(scheduler.getJobDetail(jobKey).getJobDataMap().getString("activate")+"");
	
				result.add(loc);
			   
		   }
		   
		}
		
		return result;
		
	}

	//Show all jobs
	@SuppressWarnings("unchecked")
	public static JSONObject showActivateKeys() throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		
		//List all jobs into the scheduler
		if (scheduler!=null) for(String group: scheduler.getJobGroupNames()) {
			
		   for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
	
				result.put((jobKey+"").substring(6), scheduler.getJobDetail(jobKey).getJobDataMap().getString("pattern")+"");
			   
		   }
		   
		}
		
		return result;
		
	}

	//Show all running jobs
	@SuppressWarnings("unchecked")
	public static JSONObject showRunningKeys() throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
			
		//List all jobs into the scheduler
		if (scheduler!=null) for(JobExecutionContext j: scheduler.getCurrentlyExecutingJobs()) {
			
		   JobKey jobKey = j.getJobDetail().getKey();
		   
		   result.put((jobKey+"").substring(6), scheduler.getJobDetail(jobKey).getJobDataMap().getString("pattern")+"");
		   
		}
		
		return result;
		
	}
	
	public static boolean show_msg_maintenance = true;

	//Job execution
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		//initialization
	    JobDataMap dataMap = context.getJobDetail().getJobDataMap();

	    String scriptName = dataMap.getString("scriptName");
	    String user = dataMap.getString("login");
		
		if (!Start.maintenance_job) {
			
			show_msg_maintenance = true;
		    
		    SessionThread thread = null;
		    
		    //Try to execute the command
			try {
				
				boolean execute = true;
				
				switch (scriptName) {
				case "server.stack.post":
					
					if (StackManager.loadedProcess_counter()>=StackManager.PROCESS_LIMIT) {
						
						execute = false;
						
					} else if (StackManager.count_to_process()==0) {
						
						execute = false;
						
					}
					
					break;
					
				case "server.mail.post":
					
					if (SmtpManager.loadedProcess.size()>=SmtpManager.PROCESS_LIMIT) {
						
						execute = false;
						
					} else if (SmtpManager.count_to_process()==0) {
						
						execute = false;
						
					}
					
					break;
				case "server.cluster.signal.post":
					
					if (ClusterManager.allsignals_obj.size()==0) {
						
						execute = false;
						
					}
					
					break;
				}
				
				if (execute) {
					
					thread = new SessionThread(null);
					thread.user = user;
					
					CommandSyncAccess.execute(0, thread, null, null, 6, null, null, null, scriptName, null, null);
					
					SessionThreadAgent agent = new SessionThreadAgent(thread, "JOB", "execute \""+scriptName.replace("\"", "\\\"")+"\"", "");
					agent.current_function = "JOB start";
					
					SessionThreadAgent.allServerThread.put(thread.idConnection, agent);
					
					String result = CommandManager.executeAllCommands(thread, Misc.splitCommand("execute \""+scriptName.replace("\"", "\\\"")+"\""), thread.env, null, null);
					agent.current_function = "JOB end";
					
					try {SessionThreadAgent.allServerThread.get(thread.idConnection).reset_origin("JOB", "", "", result);} catch (Exception e1) {Log.trace("JOB ERROR1: "+e1.getMessage());}
					try {SessionThreadAgent.allServerThread.get(thread.idConnection).close_transaction();} catch (Exception e1) {Log.trace("JOB ERROR2: "+e1.getMessage());}
					
				}
				
			} catch (Exception e) {

				try {thread.sessionThreadAgent.current_function = "JOB error > "+e.getMessage();} catch (Exception e1) {}
				try {SessionThreadAgent.allServerThread.get(thread.idConnection).reset_origin("JOB", "", "", ""+e.getMessage());} catch (Exception e1) {Log.trace("JOB ERROR3: "+e1.getMessage());}
				try {SessionThreadAgent.allServerThread.get(thread.idConnection).close_transaction();} catch (Exception e1) {Log.trace("JOB ERROR4: "+e1.getMessage());};
				
				//Write the error message
				Log.trace("JOB ERROR: "+e.getMessage()+" [execute \""+scriptName.replace("\"", "\\\"")+"\"]");
				try {
					Log.write(scriptName, "JOB ERROR: "+e.getMessage()+" [execute \""+scriptName.replace("\"", "\\\"")+"\"]", "KO", null, null, null);
				} catch (Exception e1) {}
				
			} finally {
				
				if (thread!=null && thread.idConnection>0) {
					
					try {
						SessionThread.closeSession(thread.env, thread.idConnection);
					} catch (Exception e) {Log.trace("JOB ERROR5: "+e.getMessage());}
					try {
						SessionThreadAgent.allServerThread.remove(thread.idConnection);
					} catch (Exception e) {Log.trace("JOB ERROR5: "+e.getMessage());}
					
				}
				
			}
			
		} else {
			if (show_msg_maintenance) {
				re.jpayet.mentdb.ext.log.Log.trace("Call JOB stopped by maintenance ("+user+": "+scriptName+").");
				show_msg_maintenance = false;
			}
		}
		
	}

	public static String help() throws Exception {
		
		return "cron is a UNIX tool that has been around for a long time, so its scheduling capabilities are powerful\n"+
				"and proven. The CronTrigger class is based on the scheduling capabilities of cron.\n"+
				"\n"+
				"\n"+
				"CronTrigger uses \"cron expressions\", which are able to create firing schedules such as: \"At 8:00am every\n"+
				"Monday through Friday\" or \"At 1:30am every last Friday of the month\".\n"+
				"\n"+
				"\n"+
				"Cron expressions are powerful, but can be pretty confusing. This tutorial aims to take some of the mystery out of\n"+
				"creating a cron expression, giving users a resource which they can visit before having to ask in a forum or mailing\n"+
				"list.\n"+
				"\n"+
				"\n"+
				"Format\n"+
				"\n"+
				"\n"+
				"A cron expression is a string comprised of 6 or 7 fields separated by white space. Fields can contain any of the\n"+
				"allowed values, along with various combinations of the allowed special characters for that field. The fields are as\n"+
				"follows:\n"+
				"\n"+
				"\n"+
				"Field Name  Mandatory  Allowed Values  Allowed Special Characters\n"+
				"Seconds  YES  0-59  , - * /\n"+
				"Minutes  YES  0-59  , - * /\n"+
				"Hours  YES  0-23  , - * /\n"+
				"Day of month  YES  1-31  , - * ? / L W\n"+
				"Month  YES  1-12 or JAN-DEC  , - * /\n"+
				"Day of week  YES  1-7 or SUN-SAT  , - * ? / L #\n"+
				"Year  NO  empty, 1970-2099  , - * /\n"+
				"\n"+
				"So cron expressions can be as simple as this: * * * * ? *\n"+
				"\n"+
				"\n"+
				"or more complex, like this: 0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010\n"+
				"\n"+
				"\n"+
				"Special characters\n"+
				"\n"+
				"\n"+
				"\n"+
				"* (\"all values\") - used to select all values within a field. For example, \"\"\n"+
				"in the minute field means *\"every minute\".\n"+
				"\n"+
				"\n"+
				"? (\"no specific value\") - useful when you need to specify something in one of the\n"+
				"two fields in which the character is allowed, but not the other. For example, if I want my trigger to fire on a\n"+
				"particular day of the month (say, the 10th), but don't care what day of the week that happens to be, I would put\n"+
				"\"10\" in the day-of-month field, and \"?\" in the day-of-week field. See the examples below for clarification.\n"+
				"\n"+
				"\n"+
				"- - used to specify ranges. For example, \"10-12\" in the hour field means \"the\n"+
				"hours 10, 11 and 12\".\n"+
				"\n"+
				"\n"+
				", - used to specify additional values. For example, \"MON,WED,FRI\" in the day-of-week\n"+
				"field means \"the days Monday, Wednesday, and Friday\".\n"+
				"\n"+
				"\n"+
				"/ - used to specify increments. For example, \"0/15\" in the seconds field means \"the\n"+
				"seconds 0, 15, 30, and 45\". And \"5/15\" in the seconds field means \"the seconds 5, 20, 35, and 50\". You can\n"+
				"also specify '/' after the '' character - in this case '' is equivalent to having '0' before the '/'. '1/3'\n"+
				"in the day-of-month field means \"fire every 3 days starting on the first day of the month\".\n"+
				"\n"+
				"\n"+
				"L (\"last\") - has different meaning in each of the two fields in which it is\n"+
				"allowed. For example, the value \"L\" in the day-of-month field means \"the last day of the month\" - day\n"+
				"31 for January, day 28 for February on non-leap years. If used in the day-of-week field by itself, it simply means\n"+
				"\"7\" or \"SAT\". But if used in the day-of-week field after another value, it means \"the last xxx day of the\n"+
				"month\" - for example \"6L\" means \"the last friday of the month\". You can also specify an offset\n"+
				"from the last day of the month, such as \"L-3\" which would mean the third-to-last day of the calendar month.\n"+
				"When using the 'L' option, it is important not to specify lists, or ranges of values, as you'll get\n"+
				"confusing/unexpected results.\n"+
				"\n"+
				"\n"+
				"W (\"weekday\") - used to specify the weekday (Monday-Friday) nearest the given day.\n"+
				"As an example, if you were to specify \"15W\" as the value for the day-of-month field, the meaning is: \"the\n"+
				"nearest weekday to the 15th of the month\". So if the 15th is a Saturday, the trigger will fire on Friday the 14th.\n"+
				"If the 15th is a Sunday, the trigger will fire on Monday the 16th. If the 15th is a Tuesday, then it will fire on\n"+
				"Tuesday the 15th. However if you specify \"1W\" as the value for day-of-month, and the 1st is a Saturday, the trigger\n"+
				"will fire on Monday the 3rd, as it will not 'jump' over the boundary of a month's days. The 'W' character can only\n"+
				"be specified when the day-of-month is a single day, not a range or list of days.\n"+
				"\n"+
				"\n"+
				"\n"+
				"\n"+
				"The 'L' and 'W' characters can also be combined in the day-of-month field to yield 'LW', which\n"+
				"translates to *\"last weekday of the month\"*.\n"+
				"\n"+
				"\n"+
				"# - used to specify \"the nth\" XXX day of the month. For example, the value of \"6#3\"\n"+
				"in the day-of-week field means \"the third Friday of the month\" (day 6 = Friday and \"#3\" = the 3rd one in\n"+
				"the month). Other examples: \"2#1\" = the first Monday of the month and \"4#5\" = the fifth Wednesday of the month. Note\n"+
				"that if you specify \"#5\" and there is not 5 of the given day-of-week in the month, then no firing will occur that\n"+
				"month.\n"+
				"\n"+
				"\n"+
				"\n"+
				"The legal characters and the names of months and days of the week are not case sensitive. MON\n"+
				"is the same as mon.\n"+
				"\n"+
				"Examples\n"+
				"\n"+
				"\n"+
				"Here are some full examples:\n"+
				"\n"+
				"\n"+
				"**Expression**  **Meaning**\n"+
				"0 0 12 * * ?  Fire at 12pm (noon) every day\n"+
				"0 15 10 ? * *  Fire at 10:15am every day\n"+
				"0 15 10 * * ?  Fire at 10:15am every day\n"+
				"0 15 10 * * ? *  Fire at 10:15am every day\n"+
				"0 15 10 * * ? 2005  Fire at 10:15am every day during the year 2005\n"+
				"0 * 14 * * ?  Fire every minute starting at 2pm and ending at 2:59pm, every day\n"+
				"0 0/5 14 * * ?  Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day\n"+
				"0 0/5 14,18 * * ?  Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5minutes starting at 6pm and ending at 6:55pm, every day\n"+
				"0 0-5 14 * * ?  Fire every minute starting at 2pm and ending at 2:05pm, every day\n"+
				"0 10,44 14 ? 3 WED  Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.\n"+
				"0 15 10 ? * MON-FRI  Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday\n"+
				"0 15 10 15 * ?  Fire at 10:15am on the 15th day of every month\n"+
				"0 15 10 L * ?  Fire at 10:15am on the last day of every month\n"+
				"0 15 10 L-2 * ?  Fire at 10:15am on the 2nd-to-last last day of every month\n"+
				"0 15 10 ? * 6L  Fire at 10:15am on the last Friday of every month\n"+
				"0 15 10 ? * 6L  Fire at 10:15am on the last Friday of every month\n"+
				"0 15 10 ? * 6L 2002-2005  Fire at 10:15am on every last friday of every month during the years 2002,2003, 2004 and 2005\n"+
				"0 15 10 ? * 6#3  Fire at 10:15am on the third Friday of every month\n"+
				"0 0 12 1/5 * ?  Fire at 12pm (noon) every 5 days every month, starting on the first day of themonth.\n"+
				"0 11 11 11 11 ?  Fire every November 11th at 11:11am.\n"+
				"\n"+
				"\n"+
				"Pay attention to the effects of '?' and '*' in the day-of-week and day-of-month fields!\n";
		
	}

	//Stop all jobs
	public static void scheduler_stop(String login) throws Exception {
		
		if (!GroupManager.isGrantedUser("sys", login)) {
			throw new Exception("Sorry, '"+login+"' is not granted to 'sys'.");
		}
		
		//Generate an error if the job scheduler is already stopped
		if (scheduler == null) {

			throw new Exception("Sorry, the job scheduler is already stopped.");

		}
		
		//Delete all jobs from the scheduler
		for(String group: scheduler.getJobGroupNames()) {
		   for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
			   scheduler.deleteJob(jobKey);
		   }
		}
		
		scheduler.shutdown();
		scheduler = null;
		
	}

	//Start all jobs
	public static void scheduler_start(String login) throws Exception {
		
		if (!GroupManager.isGrantedUser("sys", login)) {
			throw new Exception("Sorry, '"+login+"' is not granted to 'sys'.");
		}
		
		//Generate an error if the job scheduler is already started
		if (scheduler!=null) {

			throw new Exception("Sorry, the job scheduler is already started.");

		}

		ClusterManager.lock = new AtomicBoolean(false);
		SmtpManager.lock = new AtomicBoolean(false);
		SmtpManager.lock_count = new AtomicBoolean(false);
		StackManager.lock = new AtomicBoolean(false);
		StackManager.lock_count = new AtomicBoolean(false);
		StackManager.loadedProcessCounter.clear();
		StackManager.loadedProcessThread.clear();
		
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		JSONObject jobs = show();
		
		//parse all jobs
		for (Object entry : jobs.keySet()) {
			
			String key = entry.toString();

			String pattern = ((JSONObject) jobs.get(key)).get("pattern")+"";
			String scriptName = ((JSONObject) jobs.get(key)).get("scriptName")+"";
			String activate = ((JSONObject) jobs.get(key)).get("activate")+"";
			login = ((JSONObject) jobs.get(key)).get("login")+"";
			
			if (activate.equals("1")) {
			
				//Create the job
				JobDetail job = newJob(JobManager.class)
					      .withIdentity("J"+key, "Jobs")
					      .usingJobData("scriptName", scriptName)
					      .usingJobData("pattern", pattern)
					      .usingJobData("activate", activate)
					      .usingJobData("login", login)
					      .build();
			     
				//Create the trigger
		         Trigger trigger = newTrigger()
		        		    .withIdentity("T"+key, "Jobs")
		        		    .startNow()
		        		    .withSchedule(cronSchedule(pattern))
		        		    .build();
			    
		        //Schedule the job
			    scheduler.scheduleJob(job, trigger);
			    
			}
			
	    }
		
		scheduler.start();
		
	}

	//Pause a job
	@SuppressWarnings("unchecked")
	public static void job_pause(String login, String id) throws Exception {
		
		//Generate an error if the job scheduler is already stopped
		if (scheduler == null) {

			throw new Exception("Sorry, the job scheduler is already stopped.");

		}
		
		if (JobManager.showRunningKeys().containsKey(id)) {
			
			throw new Exception("Sorry, the job '"+id+"' is running.");
			
		}
		
		JSONObject job = Record2.getNode("JOB["+id+"]");
		
		String scriptName = (String) job.get("scriptName");
		if (!ScriptManager.isGrantedToUser(login, scriptName)) {
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
		}
		
		//Delete all jobs from the scheduler
		for(String group: scheduler.getJobGroupNames()) {
		   for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
			   if ((jobKey+"").equals("Jobs.J"+id)) {
				   scheduler.deleteJob(jobKey);
				   break;
			   }
		   }
		}
		
		//Get the JOB object
		JSONObject j = Record2.getNode("JOB["+id+"]");
		
		j.put("activate", "0");

		Record2.update("JOB["+id+"]", j.toJSONString());
		
	}

	//Resume a job
	@SuppressWarnings("unchecked")
	public static void job_resume(String login, String id) throws Exception {
		
		JSONObject job = Record2.getNode("JOB["+id+"]");
		
		String scriptName = (String) job.get("scriptName");
		if (!ScriptManager.isGrantedToUser(login, scriptName)) {
			throw new Exception("Sorry, the script '"+scriptName+"' is not granted to '"+login+"'.");
		}
		
		String pattern = job.get("pattern")+"";
		String activate = job.get("activate")+"";
		String sublogin = job.get("login")+"";
		
		//Create the job
		JobDetail job_obj = newJob(JobManager.class)
			      .withIdentity("J"+id, "Jobs")
			      .usingJobData("scriptName", scriptName)
			      .usingJobData("pattern", pattern)
			      .usingJobData("activate", activate)
			      .usingJobData("login", sublogin)
			      .build();
	     
		//Create the trigger
         Trigger trigger = newTrigger()
        		    .withIdentity("T"+id, "Jobs")
        		    .startNow()
        		    .withSchedule(cronSchedule(pattern))
        		    .build();
	    
        //Schedule the job
	    scheduler.scheduleJob(job_obj, trigger);
		

		//Get the JOB object
		JSONObject j = Record2.getNode("JOB["+id+"]");
		
		j.put("activate", "1");

		Record2.update("JOB["+id+"]", j.toJSONString());
		
		
	}

	//Reload all jobs
	public static void scheduler_restart(String login) throws Exception {
		
		//Stop all jobs
		scheduler_stop(login);
		
		//Start all jobs
		scheduler_start(login);
		
	}

	//Get the status of the scheduler
	public static String scheduler_status() throws Exception {
		
		if (scheduler==null) return "Stopped.";
		else return "Running...";
		
	}

}
