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

public class Doc_Mentalese_SOA {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {
		
		page_group.put("Script", "Service Oriented Architecture");
		
		functions.put("Script", new Vector<MQLDocumentation>());
		page_description.put("Script", "<img src='images/p.png' style='vertical-align: middle;'>MQL functions can be saved in a script."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>You can call these scripts inside processes."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>Or directly in web-service.");
		mql = new MQLDocumentation(true, "script export_group", "Export all scripts from a group", "script export_group \"group\";", "Scripts exported with successfully.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script import_group", "Import all scripts", "script import_group \"group\";", "Scripts imported with successfully.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script create", "To create a new script", "script create post \"addition\" false 1\n  (param\n  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n  )\n  \"description ...\"\n{\n	\n	+ [v1] [v2];\n	\n} \"Return ...\";", "Script added with successful.", null, null, null, null, false, "script create|insert|add");
		mql.addParam(new MQLParam("method", "The method (post|get|put|delete|exe|conf|app)", "string", true));
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("activateLog", "Activate the log activity (true|false)", "boolean", true));
		mql.addParam(new MQLParam("nbAttempt", "Nb attempt to execute into the stack", "number", true));
		mql.addParam(new MQLParam("variables", "The variables object", "string", true));
		mql.addParam(new MQLParam("desc", "The description of the script action", "string", true));
		mql.addParam(new MQLParam("script", "The script", "string", true));
		mql.addParam(new MQLParam("return", "The description of the return value", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script update", "To update a script", "script update \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";", "Script updated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("activateLog", "Activate the log activity (true|false)", "boolean", true));
		mql.addParam(new MQLParam("nbAttempt", "Nb attempt to execute into the stack", "number", true));
		mql.addParam(new MQLParam("variables", "The variables separate by ','", "string", true));
		mql.addParam(new MQLParam("desc", "The description of the script action", "string", true));
		mql.addParam(new MQLParam("script", "The script", "string", true));
		mql.addParam(new MQLParam("return", "The description of the return value", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script merge", "To merge a script", "script merge \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";", "Script merged with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("activateLog", "Activate the log activity (true|false)", "boolean", true));
		mql.addParam(new MQLParam("nbAttempt", "Nb attempt to execute into the stack", "number", true));
		mql.addParam(new MQLParam("variables", "The variables separate by ','", "string", true));
		mql.addParam(new MQLParam("desc", "The description of the script action", "string", true));
		mql.addParam(new MQLParam("script", "The script", "string", true));
		mql.addParam(new MQLParam("return", "The description of the return value", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script set delay", "To the delay options", "script set delay \"addition.post\" 10 day {\n	true\n};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("delayValue", "The delay value", "string", true));
		mql.addParam(new MQLParam("delayType", "The delay type (sec|min|hour|day|month|year)", "string", true));
		mql.addParam(new MQLParam("delayCondition", "The delay condition", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script get", "To get a script object", "script get \"addition.post\";", "{<br>  \"delay_condition\": \"\\n\\ttrue\\n;\",<br>  \"delay_type\": \"day\",<br>  \"activateLog\": \"0\",<br>  \"mql\": \"\\n\\t\\n\\t+ [v1] [v2];\\n\\t\\n\",<br>  \"groups\": {<br>    \"mentdb\": 0<br>  },<br>  \"k\": \"addition.post\",<br>  \"vars\": \"param\\n  \\t(var \\\"[v1]\\\" {type is_double [v1]} \\\"description ...\\\" is_null:true is_empty:true \\\"example ...\\\")\\n  \\t(var \\\"[v2]\\\" {type is_double [v2]} \\\"description ...\\\" is_null:true is_empty:true \\\"example ...\\\")\\n  ;\",<br>  \"nbAttempt\": \"1\",<br>  \"return\": \"Return ...\",<br>  \"delay_value\": \"10\",<br>  \"desc\": \"description ...\"<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate create", "To generate a create script request", "script generate create \"addition.post\";", "script create post \"addition\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate update", "To generate an update script request", "script generate update \"addition.post\";", "script update \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate merge", "To generate a merge script request", "script generate merge \"addition.post\";", "script merge \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script copy", "To copy a script", "script copy \"addition.post\" post \"addition2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("oldScriptName", "The old script name", "string", true));
		mql.addParam(new MQLParam("method", "The method (post|get|put|delete)", "string", true));
		mql.addParam(new MQLParam("newScriptName", "The new script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script copy all", "To copy all scripts", "script copy all \"addition.\" \"addition3.\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("startsWithScriptName", "The starts with script name", "string", true));
		mql.addParam(new MQLParam("replacement", "The replacement path", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script rename", "To rename a script", "script rename \"addition2.post\" post \"addition4\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("oldScriptName", "The old script name", "string", true));
		mql.addParam(new MQLParam("method", "The method (post|get|put|delete)", "string", true));
		mql.addParam(new MQLParam("newScriptName", "The new script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script rename all", "To rename all scripts", "script rename all \"addition4.\" \"addition5.\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("startsWithScriptName", "The starts with script name", "string", true));
		mql.addParam(new MQLParam("replacement", "The replacement path", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script exist", "To check if a script already exist", "script exist \"addition.post\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script show", "To show all scripts", "script show", "{<br>  \"app.100.obj.chartjs.time_courbe.exe\": 0,<br>  \"app.100.template.default.page.login.exe\": 0,<br>  \"file.remote.sftp.config.get\": 0,<br>  \"file.remote.ftp.config.get\": 0,<br>  \"app.100.obj.form.control.checkbox_line.exe\": 0,<br>  \"app.100.obj.form.control.textbox.mail.exe\": 0,<br>  \"app.100.obj.chartjs.time_basic.exe\": 0,<br>  \"file.remote.cifs.config.get\": 0,<br>  \"app.100.obj.list.begin.exe\": 0,<br>  \"app.100.obj.chartjs.line_courbe_fill.exe\": 0,<br>  \"demo.file.pop3.download.post\": 0,<br>  \"app.100.obj.bootstrap.col.begin.exe\": 0,<br>  \"app.100.obj.form.control.textbox.date_en.exe\": 0,<br>  \"brain.mental.action.parser.cut_msg.post\": 0,<br>  \"app.100.obj.form.control.radio_line.exe\": 0,<br>  \"app.100.obj.chartjs.line_stepped_before.exe\": 0,<br>  \"demo.file.sftp.download.post\": 0,<br>  \"app.100.template.default.html.body_top.exe\": 0,<br>  \"stimulation.msg.load_last.post\": 0,<br>  \"app.100.obj.form.control.textbox.tel.exe\": 0,<br>  \"brain.mental.action.db.search_strategy.post\": 0,<br>  \"app.100.obj.form.control.textbox.datetime_fr.exe\": 0,<br>  \"db.sqlserver.config.get\": 0,<br>  \"app.100.template.jimdev.conf\": 0,<br>  \"action.execute_relation_rs.post\": 0,<br>  \"db.postgresql.config.get\": 0,<br>  \"app.100.template.jimdev.page.user.exe\": 0,<br>  \"demo.file.ftp.download.post\": 0,<br>  \"brain.mental.action.strategy.get_last.post\": 0,<br>  \"app.100.template.default.html.menu_left.exe\": 0,<br>  \"demo.file.ftps.upload.post\": 0,<br>  \"brain.mental.process.test.post\": 0,<br>  \"app.100.template.default.html.bar_top.exe\": 0,<br>  \"db.hsql.server.config.get\": 0,<br>  \"mentdb.remote.config.get\": 0,<br>  \"app.100.template.jimdev.page.login.exe\": 0,<br>  \"app.100.obj.progress_bar.exe\": 0,<br>  \"app.100.start.exe\": 0,<br>  \"app.100.template.jimdev.html.body_bottom.exe\": 0,<br>  \"action.redirection_relation.post\": 0,<br>  \"app.100.obj.chartjs.bar_stacked.exe\": 0,<br>  \"app.100.template.jimdev.page.home.exe\": 0,<br>  \"app.100.obj.form.begin.exe\": 0,<br>  \"app.100.obj.form.control.textbox.date_fr.exe\": 0,<br>  \"app.100.template.jimdev.page.help.exe\": 0,<br>  \"app.100.obj.form.control.textbox.password.exe\": 0,<br>  \"db.h2.embedded.config.get\": 0,<br>  \"app.100.template.default.conf\": 0,<br>  \"perf.symbol.post\": 0,<br>  \"db.mysql.config.get\": 0,<br>  \"demo.file.csv.parse.post\": 0,<br>  \"mail.imap.config.get\": 0,<br>  \"app.100.obj.form.control.textbox.text.exe\": 0,<br>  \"demo.sql.get\": 0,<br>  \"app.100.obj.chartjs.pie.exe\": 0,<br>  \"demo.file.copy_text.post\": 0,<br>  \"db.db2.config.get\": 0,<br>  \"demo.file.excelx.parse.post\": 0,<br>  \"app.100.template.jimdev.app\": 0,<br>  \"db.hsql.embedded.config.get\": 0,<br>  \"addition.post\": 0,<br>  \"perf.node.post\": 0,<br>  \"app.100.obj.onload.exe\": 0,<br>  \"boot.execute.on.start.post\": 0,<br>  \"app.100.obj.sparkline.exe\": 0,<br>  \"app.100.template.default.page.user.exe\": 0,<br>  \"app.100.obj.bootstrap.col.end.exe\": 0,<br>  \"data_type.create.post\": 0,<br>  \"perf.relation.post\": 0,<br>  \"app.100.template.default.page.home.exe\": 0,<br>  \"mail.pop3.config.get\": 0,<br>  \"db.as400.config.get\": 0,<br>  \"brain.mental.action.exe.check_strategy.post\": 0,<br>  \"app.100.obj.form.control.select_mono.exe\": 0,<br>  \"app.100.obj.form.control.hidden.exe\": 0,<br>  \"file.remote.ftps.config.get\": 0,<br>  \"db.firebird.config.get\": 0,<br>  \"app.100.template.jimdev.html.body_top.exe\": 0,<br>  \"app.100.obj.form.control.select_multiple.exe\": 0,<br>  \"demo.file.sftp.upload.post\": 0,<br>  \"app.100.obj.form.control.textbox.range.exe\": 0,<br>  \"brain.mental.process.click.post\": 0,<br>  \"db.derby.embedded.config.get\": 0,<br>  \"demo.file.ftps.download.post\": 0,<br>  \"app.100.obj.form.control.textbox.file.exe\": 0,<br>  \"app.100.obj.chartjs.doughnut.exe\": 0,<br>  \"file.remote.ssh.config.get\": 0,<br>  \"app.100.obj.form.control.textbox.datetime_en.exe\": 0,<br>  \"app.100.obj.chartjs.line_point.exe\": 0,<br>  \"app.100.obj.form.control.textarea.exe\": 0,<br>  \"app.100.obj.chartjs.bar_horizontal_stacked.exe\": 0,<br>  \"app.100.obj.chartjs.time_courbe_fill.exe\": 0,<br>  \"demo.file.copy_bytes.post\": 0,<br>  \"demo.mentdb.execute.post\": 0,<br>  \"app.100.obj.chartjs.polar.exe\": 0,<br>  \"boot.execute.on.shutdown.post\": 0,<br>  \"thought.create.post\": 0,<br>  \"app.100.template.default.page.help.exe\": 0,<br>  \"action.execute_relation_ra.post\": 0,<br>  \"app.100.template.default.app\": 0,<br>  \"db.derby.server.config.get\": 0,<br>  \"db.oracle.config.get\": 0,<br>  \"stack.process_stack.post\": 0,<br>  \"brain.mental.action.parser.parse_sentences.post\": 0,<br>  \"demo.file.json.array.parse.post\": 0,<br>  \"demo.file.parse_text.post\": 0,<br>  \"brain.internal.stimulate.post\": 0,<br>  \"app.100.template.jimdev.html.bar_top.exe\": 0,<br>  \"demo.file.ftp.upload.post\": 0,<br>  \"perf.word.post\": 0,<br>  \"brain.mental.action.exe.start_strategy.post\": 0,<br>  \"demo.file.json.obj.parse.post\": 0,<br>  \"word.create.post\": 0,<br>  \"action.closed.post\": 0,<br>  \"app.100.obj.kpi.exe\": 0,<br>  \"app.100.obj.form.control.textbox.time.exe\": 0,<br>  \"mail.process_mail.post\": 0,<br>  \"server.version.post\": 0,<br>  \"app.100.obj.chartjs.line_stepped_after.exe\": 0,<br>  \"app.100.obj.chartjs.line_courbe.exe\": 0,<br>  \"mail.smtp.config.get\": 0,<br>  \"app.100.obj.chartjs.time_basic_fill.exe\": 0,<br>  \"demo.file.excel.parse.post\": 0,<br>  \"action.relation_redirection.post\": 0,<br>  \"app.100.obj.form.control.textbox.number.exe\": 0,<br>  \"brain.mental.action.db.basic_search.post\": 0,<br>  \"brain.mental.process.initialize.post\": 0,<br>  \"file.local.config.get\": 0,<br>  \"app.100.obj.chartjs.radar.exe\": 0,<br>  \"demo.file.xml.parse.post\": 0,<br>  \"app.100.obj.chartjs.dataset.init.exe\": 0,<br>  \"action.execute_relation_rl.post\": 0,<br>  \"app.100.obj.chartjs.line_basic_fill.exe\": 0,<br>  \"action.sentences_closed.post\": 0,<br>  \"app.100.obj.form.control.textbox.color.exe\": 0,<br>  \"addition4.post\": 0,<br>  \"app.100.obj.gauge_meter.exe\": 0,<br>  \"server.info.post\": 0,<br>  \"app.100.obj.chartjs.bar_horizontal.exe\": 0,<br>  \"app.100.template.default.html.body_bottom.exe\": 0,<br>  \"app.100.obj.form.end.exe\": 0,<br>  \"perf.thought.post\": 0,<br>  \"word.create_fr_en.post\": 0,<br>  \"app.100.obj.chartjs.bar.exe\": 0,<br>  \"server.reset_id.post\": 0,<br>  \"demo.file.imap.download.post\": 0,<br>  \"app.100.obj.chartjs.line_basic.exe\": 0,<br>  \"brain.ws.stimulate.post\": 0,<br>  \"app.100.obj.form.control.checkbox_inline.exe\": 0,<br>  \"w_th.post\": 0,<br>  \"app.100.template.jimdev.html.menu_left.exe\": 0,<br>  \"lib.value.check.v1.exe\": 0,<br>  \"action.relation_response.post\": 0,<br>  \"brain.mental.action.db.deep_search.post\": 0,<br>  \"db.h2.server.config.get\": 0,<br>  \"demo.file.parse_dir.post\": 0,<br>  \"log.remove_with_retention.post\": 0,<br>  \"app.100.obj.form.control.radio_inline.exe\": 0<br>}", null, null, null, null, false, "");
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script show ghost", "To show all ghost scripts", "script show ghost", "{}", null, null, null, null, false, "");
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script export_all", "To export all scripts", "script export_all;", "Scripts exported with successfully.", null, null, null, null, false, "");
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script get_all", "To get all scripts", "script get_all;", "Warning, The scripts are loaded into the editor ...", null, null, null, null, false, "");
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "execute", "To execute a script in a new environment", "execute \"addition.post\" \"[v1]\" 10 \"[v2]\" 5;", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("varName1", "The variable name 1", "string", false));
		mql.addParam(new MQLParam("varlue1", "The value 1", "string", false));
		mql.addParam(new MQLParam("varNameN", "The variable name N", "string", false));
		mql.addParam(new MQLParam("varlueN", "The value N", "string", false));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "call", "To execute a script in the parent environment", "call \"addition.post\" \"[v1]\" 10 \"[v2]\" 5;", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("varName1", "The variable name 1", "string", false));
		mql.addParam(new MQLParam("varlue1", "The value 1", "string", false));
		mql.addParam(new MQLParam("varNameN", "The variable name N", "string", false));
		mql.addParam(new MQLParam("varlueN", "The value N", "string", false));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "include", "To execute a script in the parent environment", "include \"addition.post\" \"[v1]\" 10 \"[v2]\" 5;", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script is granted", "To check if a script is granted to a specific user", "script is granted \"addition.post\" \"admin\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate url", "To generate an URL to execute a mental script", "script generate url \"addition.post\";", "Method\n" + 
				"\n" + 
				"Header\n" + 
				"  x-token = (empty or not define the first time)\n" + 
				"  x-user = mentdb (required the first time)\n" + 
				"  x-password = ***** (required the first time)\n" + 
				"URL\n" + 
				"  https://localhost:9999/api/?x-token=&x-user=&x-password=&v1=&v2=", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate delay", "To generate delay options", "script generate delay \"addition.post\";", "j23i88m90m76i39t04r09y35p14a96y09e57t36script set delay \"addition.post\" 10 day {\n" + 
				"	true\n" + 
				";};", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate execute", "To generate a MQL execute script", "script generate execute \"addition.post\";", "execute \"addition.post\"\n" + 
				"	\"[v1]\" \"example ...\"\n" + 
				"	\"[v2]\" \"example ...\"\n" + 
				";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate stack", "To generate a MQL stack script", "script generate stack \"addition.post\";", "stack (date now) \"addition.post\"\n" + 
				"	\"[v1]\" \"example ...\"\n" + 
				"	\"[v2]\" \"example ...\"\n" + 
				";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate call", "To generate a MQL call script", "script generate call \"addition.post\";", "call \"addition.post\"\n" + 
				"	\"[v1]\" \"example ...\"\n" + 
				"	\"[v2]\" \"example ...\"\n" + 
				";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate include", "To generate a MQL call script", "script generate include \"addition.post\";", "include \"addition.post\"\n" + 
				"	\"[v1]\" \"example ...\"\n" + 
				"	\"[v2]\" \"example ...\"\n" + 
				";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script generate sub_include", "To generate a MQL sub include script", "script generate sub_include \"addition.post\";", "concat \"include \\\"addition.post\\\"\n" + 
				"	\\\"[v1]\\\" \\\"\" (string mql_encode [v1]) \"\\\"\n" + 
				"	\\\"[v2]\\\" \\\"\" (string mql_encode [v2]) \"\\\"\n" + 
				";\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script show groups", "To show all groups for a specific script", "script show groups \"addition.post\"", "{\n" + 
				"  \"sys\": 0,\n" + 
				"  \"mentdb\": 0\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script show users", "To show all granted users for a specific script", "script show users \"addition.post\"", "{\n" + 
				"  \"ai\": 0,\n" + 
				"  \"admin\": 0,\n" + 
				"  \"mentdb\": 0\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script delete", "To delete a script", "script delete \"addition.post\";", "Script deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		functions.get("Script").add(mql);
		mql = new MQLDocumentation(true, "script delete all", "To delete all scripts", "script delete all \"addition\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("startsWithScriptName", "The starts with script name", "string", true));
		functions.get("Script").add(mql);
		
		functions.put("Versioning", new Vector<MQLDocumentation>());
		page_description.put("Versioning", "<img src='images/p.png' style='vertical-align: middle;'>Here you can version your MQL scripts.");
		mql = new MQLDocumentation(true, "version check_group", "Check version difference", "version check_group \"admin\";", "STATE : SCRIPT\n" + 
				"I : addition.post\n" + 
				"I : app.100.template.index_ai.actions.MENTDB.se_city.delete.exe\n" + 
				"I : app.100.template.index_ai.actions.MENTDB.se_city.delete_confirm.exe", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version commit_group", "Commit a group", "version commit_group \"admin\" \"first commit\";", "20201223144918_sFIEJm_1Qxb5x", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		mql.addParam(new MQLParam("msg", "The message", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version commit_script", "Commit a script", "version commit_script \"admin\" \"addition.post\" \"first commit\";", "20201223144918_sFIEJm_1Qxb5x", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		mql.addParam(new MQLParam("filterScritname", "The filter script name", "string", true));
		mql.addParam(new MQLParam("msg", "The message", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version show_commits", "Show commits from a group", "version show_commits \"admin\";", "COMMITS\n" + 
				"<20201223163207_6XCUl3_rgqaUo_CMT>\n" + 
				"      admin - first message\n" + 
				"      version show_scriptnames admin 20201223163207_6XCUl3_rgqaUo_CMT;\n" + 
				"      version show_scripts admin 20201223163207_6XCUl3_rgqaUo_CMT;\n" + 
				"<20201223163254_UvZtRg_EKuRlR_CMT>\n" + 
				"      admin - addition a été changer\n" + 
				"      version show_scriptnames admin 20201223163254_UvZtRg_EKuRlR_CMT;\n" + 
				"      version show_scripts admin 20201223163254_UvZtRg_EKuRlR_CMT;", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version show_scriptnames", "Show all scriptnames from a commit", "version show_scriptnames \"admin\" \"20201223144918_sFIEJm_1Qxb5x\";", "<20201223163254_UvZtRg_EKuRlR_CMT>\n" + 
				"version show_script admin 20201223163254_UvZtRg_EKuRlR_CMT addition.post.mql;", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		mql.addParam(new MQLParam("commit_id", "The commit id", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version show_scripts", "Show all scriptnames from a commit", "version show_scripts \"admin\" \"20201223144918_sFIEJm_1Qxb5x\";", "script merge \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description2 ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";\n"
				+ "script set delay \"addition.post\" 0 day {1};", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		mql.addParam(new MQLParam("commit_id", "The commit id", "string", true));
		functions.get("Versioning").add(mql);
		mql = new MQLDocumentation(true, "version show_script", "Show a specific script from a commit", "version show_script \"admin\" \"20201223144918_sFIEJm_1Qxb5x\" \"addition.post\";", "script merge \"addition.post\" false 1 \n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  ;) \n" + 
				"  \"description2 ...\" \n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";\n" + 
				"script set delay \"addition.post\" 0 day {1};", null, null, null, null, false, "");
		mql.addParam(new MQLParam("group", "The group name", "string", true));
		mql.addParam(new MQLParam("commit_id", "The commit id", "string", true));
		mql.addParam(new MQLParam("scriptname", "The scriptname", "string", true));
		functions.get("Versioning").add(mql);
		
		functions.put("User", new Vector<MQLDocumentation>());
		page_description.put("User", "<img src='images/p.png' style='vertical-align: middle;'>A user can connect to the AI, or directly to the storage structure."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A user acts directly on the stimulation table."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A user can not be deleted. It can be disabled.");
		mql = new MQLDocumentation(true, "user create", "To create a new user", "user create \"bob\" \"pwd\";", "User bob created with successful.", "user create \"luc-yann\" \"pwd\" \"-\"", "User luc-yann created with successful.", null, null, false, "user create|insert|add");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user check password", "To check a user password", "user check password \"bob\" \"pwd\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user exist", "To a user already exist", "user exist \"admin\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user secret_key", "Get the user secret key", "user secret_key \"admin\"", "sh6gggsshdkjh834d", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user set password", "To set a user password", "user set password \"bob\" \"pwd\";", "Password updated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user show", "To show all users", "user show", "[<br>  \"bob2\",<br>  \"luc-yann\",<br>  \"bob\",<br>  \"admin\",<br>  \"ai\",<br>  \"mentdb\"<br>]", null, null, null, null, false, "");
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user show groups", "To show all groups for a specific user", "user show groups \"admin\"", "{<br>  \"app\": 0,<br>  \"luc-yann\": 0,<br>  \"lib\": 0,<br>  \"luc-yann2\": 0,<br>  \"ai\": 0,<br>  \"admin\": 0,<br>  \"cm\": 0,<br>  \"sys\": 0,<br>  \"sample\": 0,<br>  \"api-mql\": 0,<br>  \"api-rest\": 0,<br>  \"bob2\": 0,<br>  \"public\": 0,<br>  \"bob\": 0,<br>  \"api-ai\": 0,<br>  \"mentdb\": 0<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user show scripts", "To show all granted scripts for a specific user", "user show scripts \"admin\"", "{<br>  \"app.100.obj.chartjs.time_courbe.exe\": 0,<br>  \"app.100.template.default.page.login.exe\": 0,<br>  \"file.remote.sftp.config.get\": 0,<br>  \"file.remote.ftp.config.get\": 0,<br>  \"app.100.obj.form.control.checkbox_line.exe\": 0,<br>  \"app.100.obj.form.control.textbox.mail.exe\": 0,<br>  \"app.100.obj.chartjs.time_basic.exe\": 0,<br>  \"file.remote.cifs.config.get\": 0,<br>  \"app.100.obj.list.begin.exe\": 0,<br>  \"app.100.obj.chartjs.line_courbe_fill.exe\": 0,<br>  \"demo.file.pop3.download.post\": 0,<br>  \"app.100.obj.bootstrap.col.begin.exe\": 0,<br>  \"app.100.obj.form.control.textbox.date_en.exe\": 0,<br>  \"brain.mental.action.parser.cut_msg.post\": 0,<br>  \"app.100.obj.form.control.radio_line.exe\": 0,<br>  \"app.100.obj.chartjs.line_stepped_before.exe\": 0,<br>  \"demo.file.sftp.download.post\": 0,<br>  \"app.100.template.default.html.body_top.exe\": 0,<br>  \"stimulation.msg.load_last.post\": 0,<br>  \"app.100.obj.form.control.textbox.tel.exe\": 0,<br>  \"brain.mental.action.db.search_strategy.post\": 0,<br>  \"app.100.obj.form.control.textbox.datetime_fr.exe\": 0,<br>  \"db.sqlserver.config.get\": 0,<br>  \"app.100.template.jimdev.conf\": 0,<br>  \"db.postgresql.config.get\": 0,<br>  \"action.execute_relation_rs.post\": 0,<br>  \"app.100.template.jimdev.page.user.exe\": 0,<br>  \"demo.file.ftp.download.post\": 0,<br>  \"brain.mental.action.strategy.get_last.post\": 0,<br>  \"app.100.template.default.html.menu_left.exe\": 0,<br>  \"demo.file.ftps.upload.post\": 0,<br>  \"brain.mental.process.test.post\": 0,<br>  \"app.100.template.default.html.bar_top.exe\": 0,<br>  \"db.hsql.server.config.get\": 0,<br>  \"mentdb.remote.config.get\": 0,<br>  \"app.100.template.jimdev.page.login.exe\": 0,<br>  \"app.100.start.exe\": 0,<br>  \"app.100.obj.progress_bar.exe\": 0,<br>  \"app.100.template.jimdev.html.body_bottom.exe\": 0,<br>  \"action.redirection_relation.post\": 0,<br>  \"app.100.obj.chartjs.bar_stacked.exe\": 0,<br>  \"app.100.template.jimdev.page.home.exe\": 0,<br>  \"app.100.obj.form.begin.exe\": 0,<br>  \"app.100.obj.form.control.textbox.date_fr.exe\": 0,<br>  \"app.100.template.jimdev.page.help.exe\": 0,<br>  \"app.100.obj.form.control.textbox.password.exe\": 0,<br>  \"db.h2.embedded.config.get\": 0,<br>  \"app.100.template.default.conf\": 0,<br>  \"perf.symbol.post\": 0,<br>  \"db.mysql.config.get\": 0,<br>  \"demo.file.csv.parse.post\": 0,<br>  \"mail.imap.config.get\": 0,<br>  \"app.100.obj.form.control.textbox.text.exe\": 0,<br>  \"demo.sql.get\": 0,<br>  \"app.100.obj.chartjs.pie.exe\": 0,<br>  \"demo.file.copy_text.post\": 0,<br>  \"db.db2.config.get\": 0,<br>  \"demo.file.excelx.parse.post\": 0,<br>  \"app.100.template.jimdev.app\": 0,<br>  \"db.hsql.embedded.config.get\": 0,<br>  \"perf.node.post\": 0,<br>  \"app.100.obj.onload.exe\": 0,<br>  \"boot.execute.on.start.post\": 0,<br>  \"app.100.obj.sparkline.exe\": 0,<br>  \"app.100.template.default.page.user.exe\": 0,<br>  \"app.100.obj.bootstrap.col.end.exe\": 0,<br>  \"data_type.create.post\": 0,<br>  \"perf.relation.post\": 0,<br>  \"app.100.template.default.page.home.exe\": 0,<br>  \"mail.pop3.config.get\": 0,<br>  \"db.as400.config.get\": 0,<br>  \"brain.mental.action.exe.check_strategy.post\": 0,<br>  \"app.100.obj.form.control.select_mono.exe\": 0,<br>  \"app.100.obj.form.control.hidden.exe\": 0,<br>  \"file.remote.ftps.config.get\": 0,<br>  \"db.firebird.config.get\": 0,<br>  \"app.100.template.jimdev.html.body_top.exe\": 0,<br>  \"app.100.obj.form.control.select_multiple.exe\": 0,<br>  \"demo.file.sftp.upload.post\": 0,<br>  \"app.100.obj.form.control.textbox.range.exe\": 0,<br>  \"db.derby.embedded.config.get\": 0,<br>  \"brain.mental.process.click.post\": 0,<br>  \"demo.file.ftps.download.post\": 0,<br>  \"app.100.obj.form.control.textbox.file.exe\": 0,<br>  \"app.100.obj.chartjs.doughnut.exe\": 0,<br>  \"file.remote.ssh.config.get\": 0,<br>  \"app.100.obj.form.control.textbox.datetime_en.exe\": 0,<br>  \"app.100.obj.chartjs.line_point.exe\": 0,<br>  \"app.100.obj.form.control.textarea.exe\": 0,<br>  \"app.100.obj.chartjs.bar_horizontal_stacked.exe\": 0,<br>  \"app.100.obj.chartjs.time_courbe_fill.exe\": 0,<br>  \"demo.file.copy_bytes.post\": 0,<br>  \"demo.mentdb.execute.post\": 0,<br>  \"app.100.obj.chartjs.polar.exe\": 0,<br>  \"thought.create.post\": 0,<br>  \"boot.execute.on.shutdown.post\": 0,<br>  \"app.100.template.default.page.help.exe\": 0,<br>  \"app.100.template.default.app\": 0,<br>  \"db.derby.server.config.get\": 0,<br>  \"action.execute_relation_ra.post\": 0,<br>  \"db.oracle.config.get\": 0,<br>  \"stack.process_stack.post\": 0,<br>  \"brain.mental.action.parser.parse_sentences.post\": 0,<br>  \"demo.file.json.array.parse.post\": 0,<br>  \"demo.file.parse_text.post\": 0,<br>  \"brain.internal.stimulate.post\": 0,<br>  \"app.100.template.jimdev.html.bar_top.exe\": 0,<br>  \"demo.file.ftp.upload.post\": 0,<br>  \"perf.word.post\": 0,<br>  \"brain.mental.action.exe.start_strategy.post\": 0,<br>  \"demo.file.json.obj.parse.post\": 0,<br>  \"word.create.post\": 0,<br>  \"app.100.obj.kpi.exe\": 0,<br>  \"action.closed.post\": 0,<br>  \"app.100.obj.form.control.textbox.time.exe\": 0,<br>  \"mail.process_mail.post\": 0,<br>  \"server.version.post\": 0,<br>  \"app.100.obj.chartjs.line_stepped_after.exe\": 0,<br>  \"app.100.obj.chartjs.line_courbe.exe\": 0,<br>  \"mail.smtp.config.get\": 0,<br>  \"app.100.obj.chartjs.time_basic_fill.exe\": 0,<br>  \"demo.file.excel.parse.post\": 0,<br>  \"action.relation_redirection.post\": 0,<br>  \"app.100.obj.form.control.textbox.number.exe\": 0,<br>  \"brain.mental.action.db.basic_search.post\": 0,<br>  \"brain.mental.process.initialize.post\": 0,<br>  \"file.local.config.get\": 0,<br>  \"app.100.obj.chartjs.radar.exe\": 0,<br>  \"demo.file.xml.parse.post\": 0,<br>  \"app.100.obj.chartjs.dataset.init.exe\": 0,<br>  \"app.100.obj.chartjs.line_basic_fill.exe\": 0,<br>  \"action.execute_relation_rl.post\": 0,<br>  \"app.100.obj.form.control.textbox.color.exe\": 0,<br>  \"action.sentences_closed.post\": 0,<br>  \"app.100.obj.gauge_meter.exe\": 0,<br>  \"server.info.post\": 0,<br>  \"app.100.obj.chartjs.bar_horizontal.exe\": 0,<br>  \"app.100.template.default.html.body_bottom.exe\": 0,<br>  \"app.100.obj.form.end.exe\": 0,<br>  \"word.create_fr_en.post\": 0,<br>  \"perf.thought.post\": 0,<br>  \"app.100.obj.chartjs.bar.exe\": 0,<br>  \"server.reset_id.post\": 0,<br>  \"demo.file.imap.download.post\": 0,<br>  \"app.100.obj.chartjs.line_basic.exe\": 0,<br>  \"brain.ws.stimulate.post\": 0,<br>  \"app.100.obj.form.control.checkbox_inline.exe\": 0,<br>  \"w_th.post\": 0,<br>  \"lib.value.check.v1.exe\": 0,<br>  \"app.100.template.jimdev.html.menu_left.exe\": 0,<br>  \"action.relation_response.post\": 0,<br>  \"brain.mental.action.db.deep_search.post\": 0,<br>  \"db.h2.server.config.get\": 0,<br>  \"demo.file.parse_dir.post\": 0,<br>  \"log.remove_with_retention.post\": 0,<br>  \"app.100.obj.form.control.radio_inline.exe\": 0<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("User").add(mql);
		mql = new MQLDocumentation(true, "user delete", "To delete a user", "user delete \"bob\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		functions.get("User").add(mql);
		
		functions.put("Group", new Vector<MQLDocumentation>());
		page_description.put("Group", "<img src='images/p.png' style='vertical-align: middle;'>A user or script can belong to groups."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A group has rights.");
		mql = new MQLDocumentation(true, "group add", "To add a new group", "group add \"test\";", "Group added with successful.", null, null, null, null, false, "group create|insert|add");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group exist", "To check if a group exist", "group exist \"test\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group grant user", "To grant a user to a group", "group grant user \"admin\" \"test\";", "User granted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group grant script", "To grant a script to a group", "script create post \"addition\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[v1]\" {type is_double [v1]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"example ...\")\n" + 
				"  )\n" + 
				"  \"description ...\"\n" + 
				"{\n" + 
				"	\n" + 
				"	+ [v1] [v2];\n" + 
				"	\n" + 
				"} \"Return ...\";<br>group grant script \"addition.post\" \"test\"", "Script granted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group grant_all script", "To grant all script whitch starts with a string to a group", "group grant_all script \"addition.\" \"test\";", "Scripts granted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("script_starts_with", "The script name starts with", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group ungrant_all script", "To ungrant all script whitch starts with a string to a group", "group ungrant_all script \"addition.\" \"test\";", "Scripts ungranted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("script_starts_with", "The script name starts with", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group is granted user", "To check if a user granted to a group", "group is granted user \"admin\" \"test\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group is granted script", "To check if a script granted to a group", "group is granted script \"addition.post\" \"test\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group get", "To get the group", "group get \"test\"", "{<br>  \"c\": \"admin\",<br>  \"k\": \"test\",<br>  \"scripts\": {<br>    \"addition.post\": 0<br>  },<br>  \"users\": {<br>    \"admin\": 0<br>  }<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group get user", "To get users from a group", "group get user \"test\"", "{<br>  \"admin\": 0<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group get script", "To get scripts from a group", "group get script \"test\"", "{<br>  \"addition.post\": 0<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group show", "To show all groups", "group show", "{<br>  \"app\": 0,<br>  \"luc-yann\": 0,<br>  \"lib\": 0,<br>  \"test\": 0,<br>  \"luc-yann2\": 0,<br>  \"ai\": 0,<br>  \"admin\": 0,<br>  \"cm\": 0,<br>  \"sys\": 0,<br>  \"sample\": 0,<br>  \"api-mql\": 0,<br>  \"api-rest\": 0,<br>  \"bob2\": 0,<br>  \"public\": 0,<br>  \"bob\": 0,<br>  \"api-ai\": 0,<br>  \"mentdb\": 0<br>}", null, null, null, null, false, "");
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group ungrant user", "To ungrant a user to a group", "group ungrant user \"admin\" \"test\";", "User ungranted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("login", "The login", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group ungrant script", "To ungrant a script to a group", "group ungrant script \"addition.post\" \"test\";", "Script ungranted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("scriptName", "The script name", "string", true));
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		mql = new MQLDocumentation(true, "group remove", "To remove a group", "group remove \"test\";", "Group removed with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupName", "The group name", "string", true));
		functions.get("Group").add(mql);
		
		functions.put("Cluster", new Vector<MQLDocumentation>());
		page_description.put("Cluster", "<img src='images/p.png' style='vertical-align: middle;'>Execute MQL scripts throught a cluster of MentDB server.");
		mql = new MQLDocumentation(true, "cluster show", "To show all clusters", "cluster show;", "[\n" + 
				"  \"cluster_id_2\",\n" + 
				"  \"cluster_id_1\"\n" + 
				"]", null, null, null, null, false, "");
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster create", "To create a new cluster", "cluster create \"cluster_id_1\";", "Cluster created with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster exist", "To check if a cluster already exist", "cluster exist \"cluster_id_1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster delete", "To delete a cluster", "cluster delete \"cluster_id_2\";", "Cluster deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal process", "To send signals to agents", "signal process;", "1", null, null, null, null, false, "");
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal deploy", "To deploy all signals to all nodes", "signal deploy \"cluster_id_1\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 \"metric current cpu jvm;\"", "ok/set/cluster_id_1/node_id_3\n" + 
				"ok/set/cluster_id_1/node_id_4\n" + 
				"ok/set/cluster_id_1/node_id_1\n" + 
				"ok/set/cluster_id_1/node_id_2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		mql.addParam(new MQLParam("port", "The port", "string", true));
		mql.addParam(new MQLParam("user", "The user", "string", true));
		mql.addParam(new MQLParam("user_key", "The user key", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		mql.addParam(new MQLParam("connectTimeout", "Connect timeout", "string", true));
		mql.addParam(new MQLParam("readTimeout", "Read timeout", "string", true));
		mql.addParam(new MQLParam("mql_signal", "MQL signal (return a number)", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal set", "To set a new signal into a node", "cluster signal set \"cluster_id_1\" \"node_id_1\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 \"metric current cpu jvm;\"", "Signal added/updated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		mql.addParam(new MQLParam("port", "The port", "string", true));
		mql.addParam(new MQLParam("user", "The user", "string", true));
		mql.addParam(new MQLParam("user_key", "The user key", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		mql.addParam(new MQLParam("connectTimeout", "Connect timeout", "string", true));
		mql.addParam(new MQLParam("readTimeout", "Read timeout", "string", true));
		mql.addParam(new MQLParam("mql_signal", "MQL signal (return a number)", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal delete", "To delete a signal from a node", "cluster signal delete \"cluster_id_1\" \"node_id_1\"", "Signal deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal show", "To show all signals from a node for a specific cluster", "cluster signal show \"cluster_id_1\"", "CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n" + 
				"cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:58:07 : \n" + 
				"cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:58:07 : \n" + 
				"cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:58:07 : \n" + 
				"cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:58:07 : ", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal remote_show", "To show all remote signals from a node for a specific cluster", "signal remote_show \"cluster_id_1\"", "ok/CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n" + 
				"cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; /  : \n" + 
				"cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; /  : \n" + 
				"ok/CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n" + 
				"cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; /  : \n" + 
				"ok/CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n" + 
				"cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"ok/CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n" + 
				"cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : \n" + 
				"cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / metric current cpu jvm; / 2019-07-01 09:57:47 : ", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster signal give", "To give a signal to an agent", "cluster signal give \"cluster_id_1\" \"node_id_1\" 52 \"2019-06-18 10:21:56\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		mql.addParam(new MQLParam("signal", "The signal (a number)", "number", true));
		mql.addParam(new MQLParam("current_time", "The current time", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node set", "To set a new node into a cluster", "cluster node set \"cluster_id_1\" \"node_id_1\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_2\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_3\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_4\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;", "Node added/updated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		mql.addParam(new MQLParam("port", "The port", "string", true));
		mql.addParam(new MQLParam("user", "The user", "string", true));
		mql.addParam(new MQLParam("user_key", "The user key", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		mql.addParam(new MQLParam("connectTimeout", "Connect timeout", "string", true));
		mql.addParam(new MQLParam("readTimeout", "Read timeout", "string", true));
		mql.addParam(new MQLParam("active_signal", "Is an auto active signal node", "boolean", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster nodes show_obj", "To show all nodes from a cluster in an array format", "cluster nodes show_obj \"cluster_id_1\"", "{\n" + 
				"  \"node_id_3\": {\n" + 
				"    \"in_the_cluster\": \"1\",\n" + 
				"    \"active_signal\": \"1\",\n" + 
				"    \"signal_last_time\": \"2019-06-30 09:35:45\",\n" + 
				"    \"user_key\": \"pwd\",\n" + 
				"    \"error\": \"\",\n" + 
				"    \"cluster_id\": \"cluster_id_1\",\n" + 
				"    \"hostname\": \"localhost\",\n" + 
				"    \"password\": \"pwd\",\n" + 
				"    \"port\": \"9998\",\n" + 
				"    \"readTimeout\": \"30000\",\n" + 
				"    \"connectTimeout\": \"10000\",\n" + 
				"    \"signal\": \"0.2\",\n" + 
				"    \"user\": \"admin\",\n" + 
				"    \"node_id\": \"node_id_3\"\n" + 
				"  },\n" + 
				"  \"node_id_4\": {\n" + 
				"    \"in_the_cluster\": \"1\",\n" + 
				"    \"active_signal\": \"1\",\n" + 
				"    \"signal_last_time\": \"2019-06-30 09:35:45\",\n" + 
				"    \"user_key\": \"pwd\",\n" + 
				"    \"error\": \"\",\n" + 
				"    \"cluster_id\": \"cluster_id_1\",\n" + 
				"    \"hostname\": \"localhost\",\n" + 
				"    \"password\": \"pwd\",\n" + 
				"    \"port\": \"9998\",\n" + 
				"    \"readTimeout\": \"30000\",\n" + 
				"    \"connectTimeout\": \"10000\",\n" + 
				"    \"signal\": \"35.1\",\n" + 
				"    \"user\": \"admin\",\n" + 
				"    \"node_id\": \"node_id_4\"\n" + 
				"  },\n" + 
				"  \"node_id_1\": {\n" + 
				"    \"in_the_cluster\": \"1\",\n" + 
				"    \"active_signal\": \"1\",\n" + 
				"    \"signal_last_time\": \"2019-06-30 09:35:45\",\n" + 
				"    \"user_key\": \"pwd\",\n" + 
				"    \"error\": \"\",\n" + 
				"    \"cluster_id\": \"cluster_id_1\",\n" + 
				"    \"hostname\": \"localhost\",\n" + 
				"    \"password\": \"pwd\",\n" + 
				"    \"port\": \"9998\",\n" + 
				"    \"readTimeout\": \"30000\",\n" + 
				"    \"connectTimeout\": \"10000\",\n" + 
				"    \"signal\": \"57.0\",\n" + 
				"    \"user\": \"admin\",\n" + 
				"    \"node_id\": \"node_id_1\"\n" + 
				"  },\n" + 
				"  \"node_id_2\": {\n" + 
				"    \"in_the_cluster\": \"1\",\n" + 
				"    \"active_signal\": \"1\",\n" + 
				"    \"signal_last_time\": \"2019-06-30 09:35:45\",\n" + 
				"    \"user_key\": \"pwd\",\n" + 
				"    \"error\": \"\",\n" + 
				"    \"cluster_id\": \"cluster_id_1\",\n" + 
				"    \"hostname\": \"localhost\",\n" + 
				"    \"password\": \"pwd\",\n" + 
				"    \"port\": \"9998\",\n" + 
				"    \"readTimeout\": \"30000\",\n" + 
				"    \"connectTimeout\": \"10000\",\n" + 
				"    \"signal\": \"57.5\",\n" + 
				"    \"user\": \"admin\",\n" + 
				"    \"node_id\": \"node_id_2\"\n" + 
				"  }\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster nodes show_txt", "To show all nodes from a cluster in text format", "cluster nodes show_txt \"cluster_id_1\"", "IN_THE_CLUSTER / ACTIVE_SIGNAL / CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / SIGNAL / LAST_TIME / NB_CMD : ERROR\n" + 
				"1 / 0 / cluster_id_1 / node_id_3 / admin@localhost:9998 / 10000 / 30000 / 0.2 / 2019-07-01 09:41:07 / 11 : \n" + 
				"1 / 0 / cluster_id_1 / node_id_4 / admin@localhost:9998 / 10000 / 30000 / 35.0 / 2019-07-01 09:41:07 / 0 : \n" + 
				"1 / 0 / cluster_id_1 / node_id_1 / admin@localhost:9998 / 10000 / 30000 / 58.8 / 2019-07-01 09:41:07 / 0 : \n" + 
				"1 / 0 / cluster_id_1 / node_id_2 / admin@localhost:9998 / 10000 / 30000 / 57.0 / 2019-07-01 09:41:07 / 0 : ", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node delete", "To delete a node from a cluster", "cluster node delete \"cluster_id_1\" \"node_id_1\";", "Node deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node expels", "To expels a node from a cluster", "cluster node expels \"cluster_id_1\" \"node_id_1\" \"error message ...\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		mql.addParam(new MQLParam("error", "The error message", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster execute_hot", "Execute a MQL command in hot throught a cluster.", "-> \"[local_var1]\" \"data1\";\n" + 
				"-> \"[local_var2]\" \"data2\";\n" + 
				"include \"cluster.1n.hot.exe\"\n" + 
				"	\"[cluster_id]\" \"cluster_id_1\"\n" + 
				"	\"[method]\" \"LB_50_50|SIGNAL\"\n" + 
				"	\"[tunnel_id]\" \"tunnel_id_1\"\n" + 
				"	\"[request]\" (concat \n" + 
				"		\"-> \\\"[remote_var1]\\\" \" (mql encode [local_var1]) \";\"\n" + 
				"		\"-> \\\"[remote_var2]\\\" \" (mql encode [local_var2]) \";\"\n" + 
				"		(mql {\n" + 
				"			concat [remote_var1] \":\" [remote_var2]\n" + 
				"		})\n" + 
				"	)\n" + 
				";", "data1:data2", null, null, null, null, false, "");
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster execute", "Execute a MQL command throught a cluster.", "-> \"[local_var1]\" \"data1\";\n" + 
				"-> \"[local_var2]\" \"data2\";\n" + 
				"execute \"cluster.1n.exe\"\n" + 
				"	\"[cluster_id]\" \"cluster_id_1\"\n" + 
				"	\"[method]\" \"LB_50_50|SIGNAL\"\n" + 
				"	\"[request]\" (concat \n" + 
				"		\"-> \\\"[remote_var1]\\\" \" (mql encode [local_var1]) \";\"\n" + 
				"		\"-> \\\"[remote_var2]\\\" \" (mql encode [local_var2]) \";\"\n" + 
				"		(mql {\n" + 
				"			concat [remote_var1] \":\" [remote_var2]\n" + 
				"		})\n" + 
				"	)\n" + 
				";", "data1:data2", null, null, null, null, false, "");
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node reinstate", "To reinstate a node from a cluster", "cluster node reinstate \"cluster_id_1\" \"node_id_1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("nodeId", "The node id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node generate_set", "To generate a set MQL code for set a node into a cluster", "in editor {cluster node generate_set \"cluster_id_1\"};", "cluster node set \"cluster_id_1\" \"node_id_3\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_4\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_1\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;\n" + 
				"cluster node set \"cluster_id_1\" \"node_id_2\" \"localhost\" \"9998\" \"admin\" \"pwd\" \"pwd\" 10000 30000 true;", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Cluster").add(mql);
		mql = new MQLDocumentation(true, "cluster node", "To get a cluster node", "cluster node \"cluster_id_1\" \"LB_50_50\"", "{\n" + 
				"  \"subTunnels\": \"[MQL_TO_REPLACE]\",\n" + 
				"  \"hostname\": \"localhost\",\n" + 
				"  \"password\": \"pwd\",\n" + 
				"  \"cluster_id\": \"cluster_id_1\",\n" + 
				"  \"port\": \"9998\",\n" + 
				"  \"readTimeout\": \"30000\",\n" + 
				"  \"connectTimeout\": \"10000\",\n" + 
				"  \"type\": \"mentdb\",\n" + 
				"  \"user\": \"admin\",\n" + 
				"  \"cluster_method\": \"lb_50_50\",\n" + 
				"  \"key\": \"pwd\",\n" + 
				"  \"node_id\": \"node_id_3\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("method", "The method (LB_50_50|SIGNAL)", "string", true));
		functions.get("Cluster").add(mql);
		
	}

}
