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

package re.jpayet.mentdb.ext.doc;

import java.util.LinkedHashMap;
import java.util.Vector;

public class Doc_Mentalese_ESB {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {

		page_group.put("Stack", "Enterprise Service Bus");

		functions.put("Stack", new Vector<MQLDocumentation>());
		page_description.put("Stack", "<img src='images/p.png' style='vertical-align: middle;'>Here you can execute a script with delay.");
		mql = new MQLDocumentation(true, "stack", "To execute a script into the stack", "stack (date now) \"division.post\" \"[v1]\" 10 \"[v2]\" 5;", "2a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("exeDate", "The execution date", "string", true));
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("varName1", "The variable name 1", "string", false));
		mql.addParam(new MQLParam("varlue1", "The value 1", "string", false));
		mql.addParam(new MQLParam("varNameN", "The variable name N", "string", false));
		mql.addParam(new MQLParam("varlueN", "The value N", "string", false));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack process", "Process to execute the stack", "stack process;", "1", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack status", "Get the status of the stack", "stack status;", "STACK STATUS\n"
				+ "PROCESS_LIMIT=10\n"
				+ "lock=false\n"
				+ "lock_count=false\n"
				+ "\n"
				+ "STACK SCRIPT\n"
				+ "STACK PID", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack reset", "Reset lockers of the stack", "stack reset;", "1", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack process_limit", "Show the stack config process limit.", "stack process_limit;", "4", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack count_wait", "Count all waiting execution in the stack.", "stack count_wait;", "2", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack count_running", "Count all running execution in the stack.", "stack count_running;", "4", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack count_closed", "Count all closed execution in the stack.", "stack count_closed;", "406", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack count_error", "Count all error execution in the stack.", "stack count_error;", "122", null, null, null, null, false, "");
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack flow_init", "Initialize the name of a flow process", "stack flow_init [PID] \"name_of_the_flow\" \"{}\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The pid of the flow", "string", true));
		mql.addParam(new MQLParam("nameFlow", "The name of the flow", "string", true));
		mql.addParam(new MQLParam("json", "The JSON object to save", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "stack flow_step", "Mark a new step for the specified flow", "stack flow_step [PID] 1 \"Begin_of_the_flow\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The pid of the flow", "string", true));
		mql.addParam(new MQLParam("position", "The step position", "number", true));
		mql.addParam(new MQLParam("posName", "The position name", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "stack flow_json_get", "Put a couple of key/value into a specified flow (stack process)", "json load \"flow_json\" (stack flow_json_get [PID]);", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The pid of the flow", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "stack flow_json_set", "Put a couple of key/value into a specified flow (stack process)", "stack flow_json_set [PID] (json doc \"flow_json\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The pid of the flow", "string", true));
		mql.addParam(new MQLParam("json", "The JSON object to save", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "stack var_show", "Get variables for a specific process", "stack var_show 1", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The pid of the process", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "stack delete_wait_id", "Delete a specific execution (wait).", "stack delete_wait_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack delete_wait_script", "Delete all executions for a specific script (wait).", "stack delete_wait_script \"division.post\";", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack delete_closed_id", "Delete a specific execution (closed).", "stack delete_closed_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack delete_closed_script", "Delete all executions for a specific script (closed).", "stack delete_closed_script \"division.post\";", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack delete_error_id", "Delete a specific execution (error).", "stack delete_error_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack delete_error_script", "Delete all executions for a specific script (error).", "stack delete_error_script \"division.post\";", "10", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack reset_error_nbattempt_id", "Reset the number of attempt (error).", "stack reset_error_nbattempt_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack reset_error_nbattempt_script", "Reset the number of attempt (error).", "stack reset_error_nbattempt_script \"division.post\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack replay_error_id", "Replay a specific execution (error).", "stack replay_error_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack replay_error_script", "Replay all executions for a specific script (error).", "stack replay_error_script \"division.post\";", "7", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack get", "To show a specific execution (with variables).", "stack get 4;", "{\n" + 
				"  \"lastattempt\": \"2018-01-04 10:39:17.0\",\n" + 
				"  \"dtcreate\": \"2018-01-04 10:32:07.0\",\n" + 
				"  \"nbattempt\": \"1\",\n" + 
				"  \"dtclosed\": null,\n" + 
				"  \"dtexe\": \"2018-01-04 10:32:07.0\",\n" + 
				"  \"pid\": \"17\",\n" + 
				"  \"login\": \"admin\",\n" + 
				"  \"script\": \"addition.post\",\n" + 
				"  \"dterror\": \"2018-01-04 10:39:17.0\",\n" + 
				"  \"nbmaxattempt\": \"1\",\n" + 
				"  \"lasterrormsg\": \"\\nline 1 \\u003e\\u003e\\u003e addition.post [v1] 12 [v2] 19 \\nline 2 \\u003e\\u003e\\u003e exception 1; \\\"\\\\\\\"your message ...\\\\\\\";\\\" ; 1: your message ...\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "number", true));
		functions.get("Stack").add(mql);
		mql = new MQLDocumentation(true, "stack search", "To search an execution process", "stack search \"error\" \n" + 
				"	null \n" + 
				"	\"exe\" \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 10)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 5;", "{\"column_types\":[\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"LONG\",\"LONG\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\"],\"data\":[],\"columns\":[\"pid\",\"script\",\"dtcreate\",\"lastattempt\",\"nbattempt\",\"nbmaxattempt\",\"dtexe\",\"login\",\"lasterrormsg\",\"dtclosed\",\"dterror\"],\"title\":\"stack\"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("tableType", "The table type (wait|running|closed|error)", "string", true));
		mql.addParam(new MQLParam("script", "The script (can be null)", "string", true));
		mql.addParam(new MQLParam("dtType", "The date type (exe|closed|error|create|lastattempt)", "string", true));
		mql.addParam(new MQLParam("dtMin", "The min date", "string", true));
		mql.addParam(new MQLParam("dtMax", "The max date", "string", true));
		mql.addParam(new MQLParam("dtOrder", "The order (ASC|DESC)", "string", true));
		mql.addParam(new MQLParam("page", "The page", "number", true));
		mql.addParam(new MQLParam("nbByPage", "The number of line by page", "number", true));
		functions.get("Stack").add(mql);
		
		functions.put("Job", new Vector<MQLDocumentation>());
		page_description.put("Job", "<img src='images/p.png' style='vertical-align: middle;'>You can run MQL scripts automatically at specific times.");
		mql = new MQLDocumentation(true, "job help", "To show the job help", "job help;", "Cron is a UNIX tool that has been around for a long time, so its scheduling capabilities are powerful<br>and proven. The CronTrigger class is based on the scheduling capabilities of cron.<br><br><br>CronTrigger uses \"cron expressions\", which are able to create firing schedules such as: \"At 8:00am every<br>Monday through Friday\" or \"At 1:30am every last Friday of the month\".<br><br><br>Cron expressions are powerful, but can be pretty confusing. This tutorial aims to take some of the mystery out of<br>creating a cron expression, giving users a resource which they can visit before having to ask in a forum or mailing<br>list.<br><br><br>Format<br><br><br>A cron expression is a string comprised of 6 or 7 fields separated by white space. Fields can contain any of the<br>allowed values, along with various combinations of the allowed special characters for that field. The fields are as<br>follows:<br><br><br>Field Name  Mandatory  Allowed Values  Allowed Special Characters<br>Seconds  YES  0-59  , - * /<br>Minutes  YES  0-59  , - * /<br>Hours  YES  0-23  , - * /<br>Day of month  YES  1-31  , - * ? / L W<br>Month  YES  1-12 or JAN-DEC  , - * /<br>Day of week  YES  1-7 or SUN-SAT  , - * ? / L #<br>Year  NO  empty, 1970-2099  , - * /<br><br>So cron expressions can be as simple as this: * * * * ? *<br><br><br>or more complex, like this: 0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2002-2010<br><br><br>Special characters<br><br><br><br>* (\"all values\") - used to select all values within a field. For example, \"\"<br>in the minute field means *\"every minute\".<br><br><br>? (\"no specific value\") - useful when you need to specify something in one of the<br>two fields in which the character is allowed, but not the other. For example, if I want my trigger to fire on a<br>particular day of the month (say, the 10th), but don't care what day of the week that happens to be, I would put<br>\"10\" in the day-of-month field, and \"?\" in the day-of-week field. See the examples below for clarification.<br><br><br>- - used to specify ranges. For example, \"10-12\" in the hour field means \"the<br>hours 10, 11 and 12\".<br><br><br>, - used to specify additional values. For example, \"MON,WED,FRI\" in the day-of-week<br>field means \"the days Monday, Wednesday, and Friday\".<br><br><br>/ - used to specify increments. For example, \"0/15\" in the seconds field means \"the<br>seconds 0, 15, 30, and 45\". And \"5/15\" in the seconds field means \"the seconds 5, 20, 35, and 50\". You can<br>also specify '/' after the '' character - in this case '' is equivalent to having '0' before the '/'. '1/3'<br>in the day-of-month field means \"fire every 3 days starting on the first day of the month\".<br><br><br>L (\"last\") - has different meaning in each of the two fields in which it is<br>allowed. For example, the value \"L\" in the day-of-month field means \"the last day of the month\" - day<br>31 for January, day 28 for February on non-leap years. If used in the day-of-week field by itself, it simply means<br>\"7\" or \"SAT\". But if used in the day-of-week field after another value, it means \"the last xxx day of the<br>month\" - for example \"6L\" means \"the last friday of the month\". You can also specify an offset<br>from the last day of the month, such as \"L-3\" which would mean the third-to-last day of the calendar month.<br>When using the 'L' option, it is important not to specify lists, or ranges of values, as you'll get<br>confusing/unexpected results.<br><br><br>W (\"weekday\") - used to specify the weekday (Monday-Friday) nearest the given day.<br>As an example, if you were to specify \"15W\" as the value for the day-of-month field, the meaning is: \"the<br>nearest weekday to the 15th of the month\". So if the 15th is a Saturday, the trigger will fire on Friday the 14th.<br>If the 15th is a Sunday, the trigger will fire on Monday the 16th. If the 15th is a Tuesday, then it will fire on<br>Tuesday the 15th. However if you specify \"1W\" as the value for day-of-month, and the 1st is a Saturday, the trigger<br>will fire on Monday the 3rd, as it will not 'jump' over the boundary of a month's days. The 'W' character can only<br>be specified when the day-of-month is a single day, not a range or list of days.<br><br><br><br><br>The 'L' and 'W' characters can also be combined in the day-of-month field to yield 'LW', which<br>translates to *\"last weekday of the month\"*.<br><br><br># - used to specify \"the nth\" XXX day of the month. For example, the value of \"6#3\"<br>in the day-of-week field means \"the third Friday of the month\" (day 6 = Friday and \"#3\" = the 3rd one in<br>the month). Other examples: \"2#1\" = the first Monday of the month and \"4#5\" = the fifth Wednesday of the month. Note<br>that if you specify \"#5\" and there is not 5 of the given day-of-week in the month, then no firing will occur that<br>month.<br><br><br><br>The legal characters and the names of months and days of the week are not case sensitive. MON<br>is the same as mon.<br><br>Examples<br><br><br>Here are some full examples:<br><br><br>**Expression**  **Meaning**<br>0 0 12 * * ?  Fire at 12pm (noon) every day<br>0 15 10 ? * *  Fire at 10:15am every day<br>0 15 10 * * ?  Fire at 10:15am every day<br>0 15 10 * * ? *  Fire at 10:15am every day<br>0 15 10 * * ? 2005  Fire at 10:15am every day during the year 2005<br>0 * 14 * * ?  Fire every minute starting at 2pm and ending at 2:59pm, every day<br>0 0/5 14 * * ?  Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day<br>0 0/5 14,18 * * ?  Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5minutes starting at 6pm and ending at 6:55pm, every day<br>0 0-5 14 * * ?  Fire every minute starting at 2pm and ending at 2:05pm, every day<br>0 10,44 14 ? 3 WED  Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.<br>0 15 10 ? * MON-FRI  Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday<br>0 15 10 15 * ?  Fire at 10:15am on the 15th day of every month<br>0 15 10 L * ?  Fire at 10:15am on the last day of every month<br>0 15 10 L-2 * ?  Fire at 10:15am on the 2nd-to-last last day of every month<br>0 15 10 ? * 6L  Fire at 10:15am on the last Friday of every month<br>0 15 10 ? * 6L  Fire at 10:15am on the last Friday of every month<br>0 15 10 ? * 6L 2002-2005  Fire at 10:15am on every last friday of every month during the years 2002,2003, 2004 and 2005<br>0 15 10 ? * 6#3  Fire at 10:15am on the third Friday of every month<br>0 0 12 1/5 * ?  Fire at 12pm (noon) every 5 days every month, starting on the first day of themonth.<br>0 11 11 11 11 ?  Fire every November 11th at 11:11am.<br><br><br>Pay attention to the effects of '?' and '*' in the day-of-week and day-of-month fields!<br>", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job add", "To add a new job", "job add \"myJobId\" \"myScript.post\" \"0/5 * * * * ?\" true;", "Job added with successful.", null, null, null, null, false, "job create|insert|add");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("pattern", "The pattern", "string", true));
		mql.addParam(new MQLParam("activate", "Is activate ? (true | false)", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job show", "To show all jobs", "job show;", "{<br>  \"server.reset_id\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"server.reset_id.post\",<br>    \"id\": \"server.reset_id\"<br>  },<br>  \"server.process_mail\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"mail.process_mail.post\",<br>    \"id\": \"server.process_mail\"<br>  },<br>  \"server.remove_logs\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"log.remove_with_retention.post\",<br>    \"id\": \"server.remove_logs\"<br>  },<br>  \"server.process_stack\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"stack.process_stack.post\",<br>    \"id\": \"server.process_stack\"<br>  }<br>}", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job show activate", "To show all activate jobs", "job show activate;", "[<br>  [<br>    \"server.process_stack\",<br>    \"stack.process_stack.post\",<br>    \"0/1 * * * * ?\",<br>    \"1\"<br>  ],<br>  [<br>    \"server.reset_id\",<br>    \"server.reset_id.post\",<br>    \"0 0 0 * * ?\",<br>    \"1\"<br>  ],<br>  [<br>    \"server.process_mail\",<br>    \"mail.process_mail.post\",<br>    \"0/1 * * * * ?\",<br>    \"1\"<br>  ],<br>  [<br>    \"server.remove_logs\",<br>    \"log.remove_with_retention.post\",<br>    \"0 0 0 * * ?\",<br>    \"1\"<br>  ]<br>]", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job exist", "To check if a job already exist", "job exist \"myJobId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job pause", "To pause a job", "job pause \"myJobId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job resume", "To resume a job", "job resume \"myJobId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job generate update", "To generate an update job update request", "job generate update \"myJobId\";", "job update \"myJobId\" \"addition\" \"0 0 * * * ?\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job update", "To update a job", "job update \"myJobId\" \"addition\" \"0 1 * * * ?\" true;", "Job updated with successful.", "job show", "{<br>  \"server.reset_id\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"server.reset_id.post\",<br>    \"id\": \"server.reset_id\"<br>  },<br>  \"server.process_mail\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"mail.process_mail.post\",<br>    \"id\": \"server.process_mail\"<br>  },<br>  \"server.remove_logs\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"log.remove_with_retention.post\",<br>    \"id\": \"server.remove_logs\"<br>  },<br>  \"server.process_stack\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"stack.process_stack.post\",<br>    \"id\": \"server.process_stack\"<br>  }<br>}", null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("pattern", "The pattern", "string", true));
		mql.addParam(new MQLParam("activate", "Is activate ? (true | false)", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job set cluster_script", "Set the cluster script execution", "job set cluster_script \"myJobId\" \"cluster.1n.job_execution.exe\";", "Job updated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		mql.addParam(new MQLParam("cluster_script", "The cluster script (can be null)", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job remove", "To remove a job", "job remove \"myJobId\";", "Job removed with successful.", "job show", "{<br>  \"server.reset_id\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"server.reset_id.post\",<br>    \"id\": \"server.reset_id\"<br>  },<br>  \"server.process_mail\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"mail.process_mail.post\",<br>    \"id\": \"server.process_mail\"<br>  },<br>  \"server.remove_logs\": {<br>    \"pattern\": \"0 0 0 * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"log.remove_with_retention.post\",<br>    \"id\": \"server.remove_logs\"<br>  },<br>  \"server.process_stack\": {<br>    \"pattern\": \"0/1 * * * * ?\",<br>    \"activate\": \"1\",<br>    \"scriptName\": \"stack.process_stack.post\",<br>    \"id\": \"server.process_stack\"<br>  }<br>}", null, null, false, "");
		mql.addParam(new MQLParam("jobId", "The job id", "string", true));
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job scheduler stop", "To stop the job scheduler", "job scheduler stop;", "Job scheduler stoped with successful.", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job scheduler start", "To start the job scheduler", "job scheduler start;", "Job scheduler started with successful.", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job scheduler restart", "To restart the job scheduler", "job scheduler restart;", "Job scheduler restarted with successful.", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		mql = new MQLDocumentation(true, "job scheduler status", "To show the status of the job scheduler", "job scheduler status;", "Running...", null, null, null, null, false, "");
		functions.get("Job").add(mql);
		
	}

}
