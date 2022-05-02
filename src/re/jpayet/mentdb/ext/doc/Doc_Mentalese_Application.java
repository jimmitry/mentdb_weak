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

public class Doc_Mentalese_Application {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {
		
		page_group.put("App", "Application Manager");

		functions.put("App", new Vector<MQLDocumentation>());
		page_description.put("App", "<img src='images/p.png' style='vertical-align: middle;'>Here you can create new contexts for your web applications.");
		mql = new MQLDocumentation(true, "app create", "To create a new application", "app create \"http\" \"demo\" \"default\" \"100\";", "Application created with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The context type (http|https)", "string", true));
		mql.addParam(new MQLParam("contextName", "The context name", "string", true));
		mql.addParam(new MQLParam("template", "The template id", "string", true));
		mql.addParam(new MQLParam("version", "The version app to use (ex: 100)", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app show", "To show all applications into a web port", "app show \"http\";", "[<br>  \"demo\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The context type (http|https)", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app is_granted_a", "To check if an object is granted (administrator)", "app is_granted_a \"demo_cm_mysql.products.list.show\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("tag", "The referenced tag into the object", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app is_granted_sa", "To check if an object is granted (super administrator)", "app is_granted_sa \"demo_cm_mysql.products.list.show\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("tag", "The referenced tag into the object", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app exist", "Check if an application already exist", "app exist \"http\" \"demo\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The context type (http|https)", "string", true));
		mql.addParam(new MQLParam("contextName", "The context name", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app delete", "To delete an application", "app delete \"http\" \"demo\";", "Application deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The context type (http|https)", "string", true));
		mql.addParam(new MQLParam("contextName", "The context name", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app webserver restart", "To restart the web server", "app webserver restart;", "1", null, null, null, null, false, "");
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app menu", "To add a new menu", "app menu \"/menu\" \"home\" \"Home\" \"fa-home\" \"app_page=home\" post \"home\" \"menu_home\" \"*+\"", "Use only into a WEB application ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jPath", "The jPath", "string", true));
		mql.addParam(new MQLParam("id", "The menu id", "string", true));
		mql.addParam(new MQLParam("title", "The title", "string", true));
		mql.addParam(new MQLParam("icon", "The fa icon", "string", true));
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("method", "The method (get|post)", "string", true));
		mql.addParam(new MQLParam("topMenu", "The top menu id", "string", true));
		mql.addParam(new MQLParam("groups", "The groups", "string", true));
		mql.addParam(new MQLParam("adminType", "The admin type (*-|*+)", "string", true));
		functions.get("App").add(mql);
		mql = new MQLDocumentation(true, "app menu show", "To show the menu", "app menu show;", "Use only into a WEB application ...", null, null, null, null, false, "");
		functions.get("App").add(mql);
		
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_2_script___create_remote", "To create a remote valid script", "script create exe \"script.var.show\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[OBJ_OVERWRITE_B64]\" {true} \"Parameters in base 64\" is_null:false is_empty:false \"{}\")\n" + 
				"  )\n" + 
				"  \"Show all variables\"\n" + 
				"{\n" + 
				"\n" + 
				"	json load \"param\" (string decode_b64 [OBJ_OVERWRITE_B64]);\n" + 
				"	\n" + 
				"	include \"app.100.obj.sajax.alert.exe\"\n" + 
				"		\"[type]\" \"ALERT_PRIMARY\"\n" + 
				"		\"[strong]\" \"OK !\"\n" + 
				"		\"[msg]\" (json doc \"param\")\n" + 
				"	;\n" + 
				"	\n" + 
				"} \"Return ...\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___html_div", "To add custom HTML", "concat_var \"[page]\" \"<div></div>\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___css_container", "To show a Bootstrap container object", "include \"app.100.obj.sajax.skeleton.container.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"		\n" + 
				"	include \"app.100.obj.sajax.skeleton.div.end.exe\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___css_row", "To show a Bootstrap row object", "include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___css_col", "To show a Bootstrap col object", "include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"container_id_1\" \"[name]\" \"\" \"[class]\" \"col-12 col-xs-12 col-sm-12 col-md-12 col-lg-12\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___onload", "To execute a MQL source code before loading the page", "include \"app.100.obj.sajax.skeleton.onload.exe\"\n" + 
				"		\"[scriptname]\" \"script.var.show.exe\"\n" + 
				"		\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{\\\"v1\\\": 1, \\\"v2\\\": 2}\")\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___on_click_button", "To get a javascript source code to execute a MQL remote script in a button", "concat_var \"[page]\" \"<div><button onclick=\" (include \"app.100.obj.sajax.client.exe\" \n" + 
				"		\"[scriptname]\" \"script.var.show.exe\" \n" + 
				"		\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{\\\"v1\\\": 1, \\\"v2\\\": 2}\") \n" + 
				"		\"[getElementById]\" \"|param1=elementById,param2=elementById,param3=elementById\" \n" + 
				"		\"[data]\" \"|$('#form1').serializeObject()|javascript which return json object...\"\n" + 
				"	) \">execute me</button></div>\";", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___on_click_event", "To get a javascript source code to execute a MQL remote script", "onclick=\" (include \"app.100.obj.sajax.client.exe\" \n" + 
				"			\"[scriptname]\" \"script.var.show.exe\" \n" + 
				"			\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{\\\"v1\\\": 1, \\\"v2\\\": 2}\") \n" + 
				"			\"[getElementById]\" \"|param1=elementById,param2=elementById,param3=elementById\" \n" + 
				"			\"[data]\" \"|$('#form1').serializeObject()|javascript which return json object...\"\n" + 
				"		) \"", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_1_skeleton___import_all_charts", "To get skeleton for all charts", "include \"app.100.obj.sajax.skeleton.container.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"kpi\" \"[name]\" \"\" \"[class]\" \"col-6 col-xs-6 col-sm-6 col-md-6 col-lg-6\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"progress_bar\" \"[name]\" \"\" \"[class]\" \"col-6 col-xs-6 col-sm-6 col-md-6 col-lg-6\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_line\" \"[name]\" \"\" \"[class]\" \"col-4 col-xs-4 col-sm-4 col-md-4 col-lg-4\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_bar\" \"[name]\" \"\" \"[class]\" \"col-4 col-xs-4 col-sm-4 col-md-4 col-lg-4\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_box\" \"[name]\" \"\" \"[class]\" \"col-4 col-xs-4 col-sm-4 col-md-4 col-lg-4\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_tristate\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_discrete\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_bullet\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"sparkline_pie\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"bar_vertical\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"bar_horizontal\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"bar_vertical_stacked\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"bar_horizontal_stacked\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"doughnut\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"pie\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"polar\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"radar\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_basic\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_basic_fill\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_stepped_before\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_stepped_after\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"time_basic\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"time_basic_fill\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"time_courbe\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"time_courbe_fill\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.row.exe\" \"[id]\" \"\" \"[name]\" \"\" \"[class]\" \"\" \"[style]\" \"\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_courbe\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_courbe_fill\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"line_point\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"			include \"app.100.obj.sajax.skeleton.col.exe\" \"[id]\" \"gauge\" \"[name]\" \"\" \"[class]\" \"col-3 col-xs-3 col-sm-3 col-md-3 col-lg-3\" \"[style]\" \"\"; include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"		include \"app.100.obj.sajax.skeleton.div.end.exe\";\n" + 
				"	include \"app.100.obj.sajax.skeleton.div.end.exe\";", "html", null, null, null, null, false, ""));
		
		
		
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___button_in_container", "To add a button into a container", "include \"app.100.obj.sajax.button.exe\"\n" + 
				"		\"[container_id]\" \"container_id_1\"\n" + 
				"		\"[submitTitle]\" \"Submit\"\n" + 
				"		\"[submitType]\" \"primary\"\n" + 
				"		\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{}\")\n" + 
				"		\"[mql_script]\" \"app.100.template.default.actions.demo.test.exe\"\n" + 
				"		\"[data_eval]\" \"\"\n" + 
				"		\"[html]\" \"style='color:#000'\"\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___refresh_container_with_data", "To refresh a container with a HTML source code", "include \"app.100.obj.sajax.refresh.exe\"\n" + 
				"		\"[target]\" \"container_id_1\"\n" + 
				"		\"[innerHtml]\" \"<div style='background-color:#313131;color:#fff'>Hello!</div>\"\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___show_alert", "To show an alert", "include \"app.100.obj.sajax.alert.exe\"\n" + 
				"		\"[type]\" \"ALERT_PRIMARY|ALERT_SECONDARY|ALERT_SUCCESS|ALERT_DANGER|ALERT_WARNING|ALERT_INFO|ALERT_LIGHT|ALERT_DARK\"\n" + 
				"		\"[strong]\" \"OK !\"\n" + 
				"		\"[msg]\" \"Message\"\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___execute_javascript", "To execute a javascript source code", "include \"app.100.obj.sajax.javascript.exe\"\n" + 
				"		\"[javascript]\" \"alert('test');\"\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___on_click_button", "To get a javascript source code to execute a MQL remote script in a button", "include \"app.100.obj.sajax.refresh.exe\"\n" + 
				"		\"[target]\" \"container_id_1\"\n" + 
				"		\"[innerHtml]\" (concat \"<button onclick=\" (include \"app.100.obj.sajax.client.exe\" \n" + 
				"			\"[scriptname]\" \"script.var.show.exe\" \n" + 
				"			\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{\\\"v1\\\": 1, \\\"v2\\\": 2}\") \n" + 
				"			\"[getElementById]\" \"|param1=elementById,param2=elementById,param3=elementById\" \n" + 
				"			\"[data]\" \"|$('#form1').serializeObject()|javascript which return json object...\"\n" + 
				"		) \">execute me</button>\")\n" + 
				"	;", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___on_click_event", "To get a javascript source code to execute a MQL remote script", "onclick=\" (include \"app.100.obj.sajax.client.exe\" \n" + 
				"			\"[scriptname]\" \"script.var.show.exe\" \n" + 
				"			\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{\\\"v1\\\": 1, \\\"v2\\\": 2}\") \n" + 
				"			\"[getElementById]\" \"|param1=elementById,param2=elementById,param3=elementById\" \n" + 
				"			\"[data]\" \"|$('#form1').serializeObject()|javascript which return json object...\"\n" + 
				"		) \"", "html", null, null, null, null, false, ""));
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___import_all_chart", "To get all charts", "include \"app.100.obj.kpi.exe\" \"[container_id]\" \"kpi\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_bar\" \"[type]\" \"bar\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_line\" \"[type]\" \"line\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_box\" \"[type]\" \"box\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_tristate\" \"[type]\" \"tristate\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_discrete\" \"[type]\" \"discrete\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_bullet\" \"[type]\" \"bullet\";\n" + 
				"	include \"app.100.obj.sparkline.exe\" \"[container_id]\" \"sparkline_pie\" \"[type]\" \"pie\";\n" + 
				"	include \"app.100.obj.progress_bar.exe\" \"[container_id]\" \"progress_bar\";\n" + 
				"	include \"app.100.obj.chartjs.bar_vertical.exe\" \"[container_id]\" \"bar_vertical\";\n" + 
				"	include \"app.100.obj.chartjs.bar_horizontal.exe\" \"[container_id]\" \"bar_horizontal\";\n" + 
				"	include \"app.100.obj.chartjs.bar_vertical_stacked.exe\" \"[container_id]\" \"bar_vertical_stacked\";\n" + 
				"	include \"app.100.obj.chartjs.bar_horizontal_stacked.exe\" \"[container_id]\" \"bar_horizontal_stacked\";\n" + 
				"	include \"app.100.obj.chartjs.doughnut.exe\" \"[container_id]\" \"doughnut\";\n" + 
				"	include \"app.100.obj.chartjs.pie.exe\" \"[container_id]\" \"pie\";\n" + 
				"	include \"app.100.obj.chartjs.polar.exe\" \"[container_id]\" \"polar\";\n" + 
				"	include \"app.100.obj.chartjs.radar.exe\" \"[container_id]\" \"radar\";\n" + 
				"	include \"app.100.obj.chartjs.line_basic.exe\" \"[container_id]\" \"line_basic\";\n" + 
				"	include \"app.100.obj.chartjs.line_basic_fill.exe\" \"[container_id]\" \"line_basic_fill\";\n" + 
				"	include \"app.100.obj.chartjs.line_stepped_before.exe\" \"[container_id]\" \"line_stepped_before\";\n" + 
				"	include \"app.100.obj.chartjs.line_stepped_after.exe\" \"[container_id]\" \"line_stepped_after\";\n" + 
				"	include \"app.100.obj.chartjs.line_courbe.exe\" \"[container_id]\" \"line_courbe\";\n" + 
				"	include \"app.100.obj.chartjs.line_courbe_fill.exe\" \"[container_id]\" \"line_courbe_fill\";\n" + 
				"	include \"app.100.obj.chartjs.line_point.exe\" \"[container_id]\" \"line_point\";\n" + 
				"	include \"app.100.obj.chartjs.time_basic.exe\" \"[container_id]\" \"time_basic\";\n" + 
				"	include \"app.100.obj.chartjs.time_courbe.exe\" \"[container_id]\" \"time_courbe\";\n" + 
				"	include \"app.100.obj.chartjs.time_courbe_fill.exe\" \"[container_id]\" \"time_courbe_fill\";\n" + 
				"	include \"app.100.obj.chartjs.time_basic_fill.exe\" \"[container_id]\" \"time_basic_fill\";\n" + 
				"	include \"app.100.obj.gauge_meter.exe\" \"[container_id]\" \"gauge\";", "html", null, null, null, null, false, ""));
		
		
		
		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form", "To show a form", "-> \"[object]\" \"\";\n" + 
				"	include \"app.100.obj.form.begin.exe\" \"[form_id]\" \"form_1\" \"[modal]\" true\n" + 
				"		\"[action]\" \"index.jsp\"\n" + 
				"		\"[enctype]\" false\n" + 
				"		\"[title]\" \"New book\"\n" + 
				"		\"[subTitle]\" \"\"\n" + 
				"		\"[method]\" \"post\"\n" + 
				"		\"[widthIfModal]\" \"modal-lg\"\n" + 
				"		\"[html]\" \"\";\n" + 
				"		\n" + 
				"		\n" + 
				"		\n" + 
				"	include \"app.100.obj.form.end.exe\" \"[form_id]\" \"form_1\" \"[modal]\" true\n" + 
				"		\"[OBJ_OVERWRITE_B64]\" (string encode_b64 \"{}\")\n" + 
				"		\"[container_id]\" \"container_id_1\"\n" + 
				"		\"[mql_script]\" \"script.var.show.exe\"\n" + 
				"		\"[data_eval]\" \"$('#form_1').serializeObject()\"\n" + 
				"		\"[closeTitle]\" \"Fermer\"\n" + 
				"		\"[submitTitle]\" \"Ajouter\"\n" + 
				"		\"[submitType]\" \"primary\";", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_text", "To show a textbox (text type)", "	include \"app.100.obj.form.control.textbox.text.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Text\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_range", "To show a textbox (range type)", "	include \"app.100.obj.form.control.textbox.range.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Range\" \"[description]\" \"\"\n" + 
				"		\"[min]\" 0 \"[max]\" 100 \"[step]\" 5 \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" 10\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_number", "To show a textbox (number type)", "	include \"app.100.obj.form.control.textbox.number.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Number\" \"[description]\" \"\" \"[maxlength]\" \"\"\n" + 
				"		\"[min]\" 0 \"[max]\" 100 \"[step]\" 5 \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" 10\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_mail", "To show a textbox (mail type)", "	include \"app.100.obj.form.control.textbox.mail.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Mail\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_tel", "To show a textbox (tel type)", "	include \"app.100.obj.form.control.textbox.tel.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Tel\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_time", "To show a textbox (time type)", "	include \"app.100.obj.form.control.textbox.time.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Time\" \"[description]\" \"\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_date_fr", "To show a textbox (date_fr type)", "	include \"app.100.obj.form.control.textbox.date_fr.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Date fr\" \"[description]\" \"\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_date_en", "To show a textbox (date_en type)", "	include \"app.100.obj.form.control.textbox.date_en.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Date en\" \"[description]\" \"\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_datetime_fr", "To show a textbox (datetime_fr type)", "	include \"app.100.obj.form.control.textbox.datetime_fr.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Datetime fr\" \"[description]\" \"\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_datetime_en", "To show a textbox (datetime_en type)", "	include \"app.100.obj.form.control.textbox.datetime_en.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Datetime en\" \"[description]\" \"\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_color", "To show a textbox (color type)", "	include \"app.100.obj.form.control.textbox.color.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Color\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_password", "To show a textbox (password type)", "	include \"app.100.obj.form.control.textbox.password.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Password\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textbox_file", "To show a textbox (file type)", "	include \"app.100.obj.form.control.textbox.file.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"File\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textarea", "To show a textarea", "	include \"app.100.obj.form.control.textarea.exe\" \"[control_id]\" \"control_14\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Textarea\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_textarea_ckeditor", "To show a textarea", "	include \"app.100.obj.form.control.textarea_cke.exe\" \"[control_id]\" \"control_14\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Textarea\" \"[description]\" \"\" \"[maxlength]\" \"255\" \"[placeholder]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_select_mono", "To show a select mono", "	include \"app.100.obj.form.control.select_mono.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Select mono\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_select_multiple", "To show a select multiple", "	include \"app.100.obj.form.control.select_multiple.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Select multiple\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[values]\" \"{}\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_radio_line", "To show a radio line", "	include \"app.100.obj.form.control.radio_line.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Radio line\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_radio_inline", "To show a radio inline", "	include \"app.100.obj.form.control.radio_inline.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Radio inline\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_checkbox_line", "To show a checkbox line", "	include \"app.100.obj.form.control.checkbox_line.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Checkbox line\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[values]\" \"{}\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_checkbox_inline", "To show a checkbox inline", "	include \"app.100.obj.form.control.checkbox_inline.exe\" \"[control_id]\" \"control_1\" \"[class]\" \"col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12\"\n" + 
				"		\"[label]\" \"Checkbox inline\" \"[description]\" \"\"\n" + 
				"		\"[readonly]\" false \"[required]\" false \"[disabled]\" false \"[html]\" \"\"\n" + 
				"		\"[inValues]\" \"[]\"\n" + 
				"		\"[optionValues]\" \"[]\"\n" + 
				"		\"[values]\" \"{}\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.get("App").add(new MQLDocumentation(true, "_app_sajax_3_in_loading___form_control_hidden", "To add a hidden", "	include \"app.100.obj.form.control.hidden.exe\"\n" + 
				"		\"[control_id]\" \"control_1\"\n" + 
				"		\"[value]\" \"\"\n" + 
				"	;", "html", null, null, null, null, false, ""));

		functions.put("Virtual host", new Vector<MQLDocumentation>());
		page_description.put("Virtual host", "<img src='images/p.png' style='vertical-align: middle;'>Here you can manage your virtual host.");
		mql = new MQLDocumentation(true, "app vhost add", "To add a new virtual host", "app vhost add \"http\" \"demo\" \"www.jpayet.re\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("protocol", "The protocol (http|https)", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		functions.get("Virtual host").add(mql);
		mql = new MQLDocumentation(true, "app vhost show", "To show virtual hosts", "app vhost show \"http\" \"demo\";", "{\"www.jpayet.re\": \"demo\"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("protocol", "The protocol (http|https)", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		functions.get("Virtual host").add(mql);
		mql = new MQLDocumentation(true, "app vhost exist", "To check if a virtual host already exist", "app vhost exist \"http\" \"demo\" \"www.jpayet.re\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("protocol", "The protocol (http|https)", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		functions.get("Virtual host").add(mql);
		mql = new MQLDocumentation(true, "app vhost delete", "To delete a virtual host", "app vhost delete \"http\" \"demo\" \"www.jpayet.re\";", "1", "app delete \"http\" \"demo\";", "Application deleted with successful.", null, null, false, "");
		mql.addParam(new MQLParam("protocol", "The protocol (http|https)", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		mql.addParam(new MQLParam("hostname", "The hostname", "string", true));
		functions.get("Virtual host").add(mql);
		
	}

}
