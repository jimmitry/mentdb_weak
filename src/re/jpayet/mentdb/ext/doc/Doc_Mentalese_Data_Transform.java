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

public class Doc_Mentalese_Data_Transform {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {
		
		page_group.put("MQL Syntax", "Data transformation");
		
		functions.put("MQL Syntax", new Vector<MQLDocumentation>());
		page_description.put("MQL Syntax", "<img src='images/p.png' style='vertical-align: middle;'>You can can see the basic syntax of the MQL language.");
		//ghost_functions.put("MQL Syntax", null);
		mql = new MQLDocumentation(true, "category1 categoryN action param1 paramN", "All MQL function respect the format \"category1 categoryN action param1 paramN\"", "who", "mentdb", "concat \"Hello \" \"Adam!\"", "Hello Adam!", null, null, false, "");
		mql.addParam(new MQLParam("category1", "The category 1", "string", false));
		mql.addParam(new MQLParam("categoryN", "The category N", "string", false));
		mql.addParam(new MQLParam("action", "The category 1", "string", true));
		mql.addParam(new MQLParam("param1", "The parameter 1", "string", false));
		mql.addParam(new MQLParam("paramN", "The parameter 1", "string", false));
		functions.get("MQL Syntax").add(mql);
		
		functions.put("Data Type", new Vector<MQLDocumentation>());
		page_description.put("Data Type", "<img src='images/p.png' style='vertical-align: middle;'>Here you can see all data types in the MQL language.");
		//ghost_functions.put("Data Type", null);
		mql = new MQLDocumentation(true, "numeric", "Numeric type", "123", "123", "+ 3 4", "7", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "string", "String type", "\"Hello!\"", "Hello!", "concat \"Hello \" \"Adam\" \"!\"", "Hello Adam!", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "null", "Null type", "null", "null", "is null null;", "1", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "[_n_]", "LF", "[_n_]", "\n", null, null, null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "[_r_]", "CR", "[_r_]", "\r", null, null, null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "variable", "Variable type", "-> \"[var1]\" 16", "16", "env show;", "{\n  \"[var2]\": \"16\"\n}", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "boolean", "Boolean type", "true", "1", "false", "0", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "json", "JSON type", "json load \"keyId\" \"{}\";", "1", "json doc \"keyId\";", "{}", null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "comment", "Comment type", "#This is a comment;", "This is a comment ...", null, null, null, null, false, "");
		functions.get("Data Type").add(mql);
		mql = new MQLDocumentation(true, "error", "Error type", "exception (1) (\"your message ...\");", "1: your message ...", null, null, null, null, false, "");
		functions.get("Data Type").add(mql);

		functions.put("Statement", new Vector<MQLDocumentation>());
		page_description.put("Statement", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have the classic statements functions.");
		mql = new MQLDocumentation(true, "eval", "Eval a MQL expression", "eval \"log trace OK!;\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("mql", "The MQL source code", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "parallel", "Execute many commands in the same time", "parallel {\n" + 
				"	log trace \"1\";\n" + 
				"} {\n" + 
				"	log trace \"2\";\n" + 
				"} {\n" + 
				"	log trace \"3\";\n" + 
				"};", "[\n  {\n    \"result\": \"1\",\n    \"status\": \"OK\"\n  },\n  {\n    \"result\": \"1\",\n    \"status\": \"OK\"\n  },\n  {\n    \"result\": \"1\",\n    \"status\": \"OK\"\n  }\n]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("mql1", "The first MQL command", "String", true));
		mql.addParam(new MQLParam("mqlN", "The N MQL command", "String", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "if", "IF statement", "if (true) {\n\n	log trace OK!\n\n} {\n\n	log trace KO!\n\n};", "1", "if (true) {\n\n	log trace hello!\n\n};", "1", null, null, false, "");
		mql.addParam(new MQLParam("condition", "The condition", "string", true));
		mql.addParam(new MQLParam("trueAction", "The true action", "boolean", true));
		mql.addParam(new MQLParam("falseAction", "The false action", "boolean", false));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "if force", "IF statement (force true and false execution)", "if force (true) {\n\n	log trace OK!\n\n} {\n\n	log trace KO!\n\n};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("condition", "The condition", "string", true));
		mql.addParam(new MQLParam("trueAction", "The true action", "boolean", true));
		mql.addParam(new MQLParam("falseAction", "The false action", "boolean", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "case", "CASE statement", "case\n	(true) {A}\n	(false) {B}\n	{C}\n;", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("condition1", "The condition 1", "boolean", true));
		mql.addParam(new MQLParam("action1", "The action 1", "string", true));
		mql.addParam(new MQLParam("conditionN", "The condition N", "boolean", true));
		mql.addParam(new MQLParam("actionN", "The action N", "string", true));
		mql.addParam(new MQLParam("elseAction", "The else action", "string", false));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "switch", "SWITCH statement", "switch (A)\n	(A) {A}\n	(B) {B}\n	{C}\n;", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("valueToSearch", "The value to search", "boolean", true));
		mql.addParam(new MQLParam("condition1", "The condition 1", "boolean", true));
		mql.addParam(new MQLParam("action1", "The action 1", "string", true));
		mql.addParam(new MQLParam("conditionN", "The condition N", "boolean", true));
		mql.addParam(new MQLParam("actionN", "The action N", "string", true));
		mql.addParam(new MQLParam("elseAction", "The else action", "string", false));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "mode", "SWITCH statement", "mode SERVER_MODE\n	(PROD) {A}\n	(DEV) {B}\n	{C}\n;", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("paramMode", "The parameter mode", "string", true));
		mql.addParam(new MQLParam("condition1", "The condition 1", "boolean", true));
		mql.addParam(new MQLParam("action1", "The action 1", "string", true));
		mql.addParam(new MQLParam("conditionN", "The condition N", "boolean", true));
		mql.addParam(new MQLParam("actionN", "The action N", "string", true));
		mql.addParam(new MQLParam("elseAction", "The else action", "string", false));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "for", "FOR statement", "for (-> \"[i]\" 0) (< [i] 45) (++ \"[i]\") {\n\n	log trace [i];\n\n};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("init", "The init mql source code", "string", true));
		mql.addParam(new MQLParam("condition", "The condition", "boolean", true));
		mql.addParam(new MQLParam("increment", "The increment", "string", true));
		mql.addParam(new MQLParam("action", "The action", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "while", "WHILE statement", "-> \"[i]\" 0;\nwhile (< [i] 45) {\n\n	log trace [i];\n	++ \"[i]\";\n\n};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("condition", "The condition", "boolean", true));
		mql.addParam(new MQLParam("action", "The action", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "repeat", "REPEAT statement", "-> \"[i]\" 0;\nrepeat (< [i] 45) {\n\n	log trace [i];\n	++ \"[i]\";\n\n};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("condition", "The condition", "boolean", true));
		mql.addParam(new MQLParam("action", "The action", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "synchronized", "Synchronized MQL block", "synchronized (\"KEY\") {\n	log trace \"OK\";\n}", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The synchronized key", "string", true));
		mql.addParam(new MQLParam("action", "The action", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "break", "Break a FOR/WHILE/REPEAT/SQL_PARSE/CSV_PARSE/HTML_PARSE/JSON_PARSE_OBJ/JSON_PARSE_ARRAY statement", "break", "BREAK", null, null, null, null, false, "");
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "continue", "Stop the sub action and continue the loop : FOR/WHILE/REPEAT/SQL_PARSE/CSV_PARSE/HTML_PARSE/JSON_PARSE_OBJ/JSON_PARSE_ARRAY statement", "continue", "CONTINUE", null, null, null, null, false, "");
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "exception", "EXCEPTION statement", "exception (1) (\"your message ...\");", "1: your message ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("id", "The error message id", "string", true));
		mql.addParam(new MQLParam("message", "The error message", "string", true));
		functions.get("Statement").add(mql);
		mql = new MQLDocumentation(true, "try", "TRY statement", "try {\n\n	exception (1) (\"your messffage ...\");\n\n} {\n\n	log trace [err]; \n\n} \"[err]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("action", "The main action", "string", true));
		mql.addParam(new MQLParam("errorAction", "The error action if an error was generated", "string", true));
		mql.addParam(new MQLParam("idException", "The variable to save the error message", "string", true));
		functions.get("Statement").add(mql);
		
		functions.put("Operator", new Vector<MQLDocumentation>());
		page_description.put("Operator", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have the basic operators.");
		mql = new MQLDocumentation(true, "+", "addition", "+ 23 42", "65", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "-", "substration", "- 50 26", "24", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "*", "multiplication", "* 2 5", "10", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "/", "division", "/ 30 2", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bi+", "addition", "bi+ 23 42", "65", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bi-", "substration", "bi- 50 26", "24", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bi*", "multiplication", "bi* 2 5", "10", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bi/", "division", "bi/ 30 2", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bd+", "addition", "bd+ 23 42", "65", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bd-", "substration", "bd- 50 26", "24", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bd*", "multiplication", "bd* 2 5", "10", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "bd/", "division", "bd/ 30 2", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "and", "and boolean", "and 1 1", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("boolean1", "The boolean 1", "boolean", true));
		mql.addParam(new MQLParam("boolean2", "The boolean 2", "boolean", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "or", "or boolean", "or 1 0", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("boolean1", "The boolean 1", "boolean", true));
		mql.addParam(new MQLParam("boolean2", "The boolean 2", "boolean", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "xor", "xor boolean", "xor 1 0", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("boolean1", "The boolean 1", "boolean", true));
		mql.addParam(new MQLParam("boolean2", "The boolean 2", "boolean", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "not", "not boolean", "not 0", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("boolean", "The boolean", "boolean", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "is null", "Check if the value is null", "is null null", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "is not null", "Check if the value is not null", "is not null null", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "is null or empty", "Check if the value is null or empty", "is null or empty null", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "is empty", "Check if the string is empty", "is empty \"\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "is not empty", "Check if the string is not empty", "is not empty \"\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "equal", "Check if 2 strings are equal", "equal \"str1\" \"str1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string 1", "string", true));
		mql.addParam(new MQLParam("str2", "The string 2", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "not equal", "Check if 2 strings are not equal", "not equal \"str1\" \"str1\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string 1", "string", true));
		mql.addParam(new MQLParam("str2", "The string 2", "string", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "==", "Check if 2 numbers are equal", "== \"25\" \"25.0\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "!=", "Check if 2 numbers are not equal", "!= \"25\" \"25.0\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, ">", "Check if a number is greater than another", "> \"27\" \"26.0\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, ">=", "Check if a number is greater or equal than another", ">= \"26\" \"26.0\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "<", "Check if a number is less than another", "< \"25\" \"26.0\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		mql = new MQLDocumentation(true, "<=", "Check if a number is less or equal than another", "<= \"26\" \"26.0\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "The number 1", "number", true));
		mql.addParam(new MQLParam("number2", "The number 2", "number", true));
		functions.get("Operator").add(mql);
		
		functions.put("Date", new Vector<MQLDocumentation>());
		page_description.put("Date", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "date add", "add a number to a date.\n", "date add \"1980-06-18\" \"DAY\" 1", "1980-06-19", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date addt", "add a number to a time stamp.\n", "date addt \"1980-06-18 00:00:00\" \"DAY\" 1", "1980-06-19 00:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (SEC|MIN|HOUR|DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date current_ms", "returns the current time in milli second.\n", "date current_ms", "1496164604097", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date current_ns", "returns the current time in nano second.\n", "date current_ns", "823603146305304", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date curdate", "returns the current date.\n", "date curdate", "2015-09-13", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date current_date", "returns the current date.\n", "date current_date", "2015-09-13", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date current_time", "returns the current time.\n", "date current_time", "14:01:22", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date current_timestamp", "returns the current timestamp.\n", "date current_timestamp", "2015-09-13 14:01:42", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date curtime", "returns the current time.\n", "date curtime", "14:01:57", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date curtimestamp", "returns the current timestamp.\n", "date curtimestamp", "2015-09-13 14:02:13", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date dateadd", "add a number to a date.\n", "date dateadd \"1980-06-18\" \"DAY\" 1", "1980-06-19", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date dateaddt", "add a number to a timestamp.\n", "date dateaddt \"1980-06-18 00:00:00\" \"DAY\" 1", "1980-06-19 00:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (SEC|MIN|HOUR|DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date datediff", "substract a number to a date.\n", "date datediff \"1980-06-18\" \"DAY\" 1", "1980-06-17", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date datedifft", "substract a number to a date.\n", "date datedifft \"1980-06-18 00:00:00\" \"DAY\" 1", "1980-06-17 00:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (SEC|MIN|HOUR|DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date ts_to_long", "Converts a timestamp to a long int.\n", "date ts_to_long \"1980-06-18 00:00:00\"", "330120000000", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date dt_to_long", "Converts the date parameter to a long int.\n", "date dt_to_long \"1980-06-18\"", "330120000000", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date long_to_dt", "Converts the date parameter sent by the user to a long int.\n", "date long_to_dt \"330120000000\"", "1980-06-18", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date long_to_ts", "Converts a long int to a timestamp.\n", "date long_to_ts \"330120000000\"", "1980-06-18 00:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date day_of_month", "Returns the day specified in the date sent in parameter.\n", "date day_of_month \"1980-06-18\"", "18", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date day_of_week", "Returns the week value from the date that was sent as parameter.\n", "date day_of_week \"1980-06-18\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date day_of_year", "Returns the year value from the date that was sent as parameter.\n", "date day_of_year \"1980-06-18\"", "170", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date dayname", "Returns the name of the day value from the date sent as parameter.\n", "date dayname \"1980-06-18\"", "wednesday", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date diff", "substract a number to a date.\n", "date diff \"1980-06-18\" \"DAY\" 1", "1980-06-17", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date difft", "substract a number to a time stamp.\n", "date difft \"1980-06-18 00:00:00\" \"DAY\" 1", "1980-06-17 00:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		mql.addParam(new MQLParam("field", "The field to add (SEC|MIN|HOUR|DAY|MONTH|YEAR)", "String", true));
		mql.addParam(new MQLParam("number", "The number to add", "Number", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date full_systimestamp", "returns the current full timestamp.\n", "date full_systimestamp", "2015-09-13 14:03:20.395+0400", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date hour", "Returns the hour value of the time that was sent as parameter.\n", "date hour \"12:15:56\"", "12", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The time", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date is_valid_date", "check if a date is valid.\n", "date is_valid_date \"1980-06-18\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dateToValidate", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date is_valid_date", "check if a date is valid.\n", "date is_valid_date \"1980-06-18\" \"yyyy-MM-dd\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dateToValidate", "The date", "String", true));
		mql.addParam(new MQLParam("format", "The date format", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date is_valid_time", "check if a time is valid.\n", "date is_valid_time \"12:00:01\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timeToValidate", "The time (12:00:01)", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date is_valid_timestamp", "check if a timestamp is valid.\n", "date is_valid_timestamp \"1980-06-18 12:00:01\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestampToValidate", "The timestamp", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date is_valid_timestamp", "check if a timestamp is valid.\n", "date is_valid_timestamp \"1980-06-18 12:00:01\" \"yyyy-MM-dd HH:mm:ss\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestampToValidate", "The timestamp", "String", true));
		mql.addParam(new MQLParam("format", "The format (ex: yyyy MM dd HH:mm:ss)", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date format", "format a timestamp to another format.\n", "date format \"19800618 12:00\" \"yyyyMMdd HH:mm\" \"yyyy-MM-dd HH:mm:ss\"", "1980-06-18 12:00:01", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestampToFormat", "The timestamp", "String", true));
		mql.addParam(new MQLParam("formatIn", "The in format (ex: yyyyMMdd HH:mm)", "String", true));
		mql.addParam(new MQLParam("formatOut", "The out format (ex: yyyy MM dd HH:mm:ss)", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date minute", "Returns the minute value of the time that was sent as parameter.\n", "date minute \"12:15:56\"", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The time", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date month", "Returns the month value of a date sent as parameter.\n", "date month \"1980-06-18\"", "6", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date monthname", "Returns the name of the month of the date sent as parameter.\n", "date monthname \"1980-06-18\"", "juin", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date nb_day", "get the number of days between two dates", "date nb_day \"1980-06-18\" \"1980-06-20\"", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date1", "The date 1", "String", true));
		mql.addParam(new MQLParam("date2", "The date 2", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date seconde", "Returns the second value from the time sent as parameter.\n", "date seconde \"12:15:56\"", "56", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The time", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date sysdate", "returns the current date.\n", "date sysdate", "2015-09-13", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date systime", "returns the current time.\n", "date systime", "14:04:18", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date systimestamp", "returns the current timestamp.\n", "date systimestamp", "2015-09-13 14:04:32", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date now", "returns the current timestamp.\n", "date now", "2015-09-13 14:04:32", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date systimestamp_min", "returns the current timestamp in minimum format.\n", "date systimestamp_min", "20150913140446", null, null, null, null, false, "");
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date time", "get the time of a timestamp.\n", "date time \"1980-06-18 12:25:56\"", "12:25:56", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestamp", "The timestamp", "String", true));
		functions.get("Date").add(mql);
		mql = new MQLDocumentation(true, "date year", "Returns the year value from the date sent as parameter.\n", "date year \"1980-06-18\"", "1980", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Date").add(mql);

		functions.put("Math", new Vector<MQLDocumentation>());
		page_description.put("Math", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "math abs", "get the absolute value of a number\n", "math abs -56", "56", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math base_to_base", "Get a number in another base", "math base_to_base 3 10 2", "11", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		mql.addParam(new MQLParam("fromBase", "A number", "Number", true));
		mql.addParam(new MQLParam("toBase", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math bin package_3d", "Get a bin package 3d", "json load \"containers\" \"[]\";\n"
				+ "json load \"container\" \"{}\";\n"
				+ "json iobject \"container\" / \"id\" \"package_1\" STR;\n"
				+ "json iobject \"container\" / \"x\" 5 STR;\n"
				+ "json iobject \"container\" / \"y\" 10 STR;\n"
				+ "json iobject \"container\" / \"z\" 1 STR;\n"
				+ "json iobject \"container\" / \"w\" 5 STR;\n"
				+ "json iarray \"containers\" / (json doc \"container\") OBJ;\n"
				+ "json load \"container\" \"{}\";\n"
				+ "json iobject \"container\" / \"id\" \"package_2\" STR;\n"
				+ "json iobject \"container\" / \"x\" 10 STR;\n"
				+ "json iobject \"container\" / \"y\" 15 STR;\n"
				+ "json iobject \"container\" / \"z\" 10 STR;\n"
				+ "json iobject \"container\" / \"w\" 5 STR;\n"
				+ "json iarray \"containers\" / (json doc \"container\") OBJ;\n"
				+ "json load \"container\" \"{}\";\n"
				+ "json iobject \"container\" / \"id\" \"package_3\" STR;\n"
				+ "json iobject \"container\" / \"x\" 20 STR;\n"
				+ "json iobject \"container\" / \"y\" 10 STR;\n"
				+ "json iobject \"container\" / \"z\" 5 STR;\n"
				+ "json iobject \"container\" / \"w\" 8 STR;\n"
				+ "json iarray \"containers\" / (json doc \"container\") OBJ;\n"
				+ "\n"
				+ "json load \"elements\" \"[]\";\n"
				+ "json load \"element\" \"{}\";\n"
				+ "json iobject \"element\" / \"id\" \"Element 1\" STR;\n"
				+ "json iobject \"element\" / \"x\" 2 STR;\n"
				+ "json iobject \"element\" / \"y\" 5 STR;\n"
				+ "json iobject \"element\" / \"z\" 1 STR;\n"
				+ "json iobject \"element\" / \"w\" 2 STR;\n"
				+ "json iobject \"element\" / \"q\" 10 STR;\n"
				+ "json iarray \"elements\" / (json doc \"element\") OBJ;\n"
				+ "json load \"element\" \"{}\";\n"
				+ "json iobject \"element\" / \"id\" \"Element 1\" STR;\n"
				+ "json iobject \"element\" / \"x\" 5 STR;\n"
				+ "json iobject \"element\" / \"y\" 9 STR;\n"
				+ "json iobject \"element\" / \"z\" 1 STR;\n"
				+ "json iobject \"element\" / \"w\" 3 STR;\n"
				+ "json iobject \"element\" / \"q\" 6 STR;\n"
				+ "json iarray \"elements\" / (json doc \"element\") OBJ;\n"
				+ "json load \"element\" \"{}\";\n"
				+ "json iobject \"element\" / \"id\" \"Element 1\" STR;\n"
				+ "json iobject \"element\" / \"x\" 3 STR;\n"
				+ "json iobject \"element\" / \"y\" 5 STR;\n"
				+ "json iobject \"element\" / \"z\" 1 STR;\n"
				+ "json iobject \"element\" / \"w\" 4 STR;\n"
				+ "json iobject \"element\" / \"q\" 1 STR;\n"
				+ "json iarray \"elements\" / (json doc \"element\") OBJ;\n"
				+ "\n"
				+ "math bin package_3d 10 5000 (json doc \"containers\") (json doc \"elements\");", "[\n"
						+ "  \"package_3\",\n"
						+ "  \"package_3\",\n"
						+ "  \"package_3\",\n"
						+ "  \"package_3\",\n"
						+ "  \"package_3\",\n"
						+ "  \"package_1\"\n"
						+ "]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("nb_max_package", "A number", "Number", true));
		mql.addParam(new MQLParam("timeout_to_stop", "A number", "Number", true));
		mql.addParam(new MQLParam("jsonContainers", "A json string that contains containers", "Number", true));
		mql.addParam(new MQLParam("jsonElements", "A json string that contains elements", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math acos", "get the arc cosin of a number\n", "math acos 42", "NaN", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math asin", "get the arc sin of a number\n", "math asin 0.5", "0.5235987755982989", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math atan", "get the arc tangent of a number\n", "math atan 42", "1.5469913006098266", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math atan2", "get the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta)\n", "math atan2 4 2", "1.1071487177940904", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math avg", "get the avg of a json array of vales\n", "math avg \"[10 20]\"", "15.0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("json_array", "A json array (of double values)", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math bit_and", "the bitwise & operation\n", "math bit_and 4456 234", "104", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math bit_or", "the bitwise or operation\n", "math bit_or 4456 234", "4586", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math bit_xor", "the bitwise xor operation\n", "math bit_xor 4456 234", "4482", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math cbrt", "returns the cube root of a double value\n", "math cbrt 42", "3.4760266448864496", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math ceil", "get the smallest integer value not less than the number specified as an argument\n", "math ceil 1.2", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math ceiling", "get the smallest integer value not less than the number specified as an argument\n", "math ceiling 1.2", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math cos", "get the cosine of a number\n", "math cos 42", "-0.39998531498835127", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math cosh", "get the hyperbolic cosine of a number\n", "math cosh 42", "8.696374707602505E17", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math cot", "get the cotangent of a number\n", "math cot 42", "0.4364167060752729", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math decimal_format", "convert a number to another formated decimal number\n", "math decimal_format 42.456 \"##.##\" \".\" \" \"", "42.46", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		mql.addParam(new MQLParam("pattern", "The pattern", "String", true));
		mql.addParam(new MQLParam("decimalSeparator", "The decimal separator", "String", true));
		mql.addParam(new MQLParam("groupingSeparator", "The grouping separator", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math deg_to_rad", "converts an angle measured in degrees to an approximately equivalent angle measured in radians\n", "math deg_to_rad 42", "0.7330382858376184", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math e", "get the Euler's number value approximately.\n", "math e", "2.718281828459045", null, null, null, null, false, "");
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math exp", "returns euler's number e raised to the power of a double value\n", "math exp 42", "1.73927494152050099E18", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math expm1", "returns e^x -1\n", "math expm1 42", "1.73927494152050099E18", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math floor", "get the largest integer value not greater than a number specified as argument\n", "math floor 1.2", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math hypot", "returns sqrt(x^2 +y^2) without intermediate overflow or underflow\n", "math hypot 4456 234", "4462.139845410496", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math log", "get the natural logarithm of the argument\n", "math log 42", "3.7376696182833684", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math log10", "get the base 10 logarithm of the argument\n", "math log10 42", "1.6232492903979006", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math log1p", "returns the natural logarithm of the sum of the argument and 1\n", "math log1p 42", "3.7612001156935624", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math max", "returns the greater number between the two parameters sent.\n", "math max 4456 234", "4456", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math min", "returns the smaller number between the parameters sent.\n", "math min 4456 234", "234", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math mod", "returns the result of the modulo of the 2 numbers sent as parameters.\n", "math mod 4456 234", "10", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math order_int", "Return an ordered array of int", "math order_int \"[8, 6, 5]\"", "\"[5, 6, 8]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "A JSON Array", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math order_long", "Return an ordered array of long", "math order_long \"[8, 6, 5]\"", "\"[5, 6, 8]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "A JSON Array", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math order_double", "Return an ordered array of double", "math order_double \"[8, 6, 5]\"", "\"[5, 6, 8]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "A JSON Array", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math order_float", "Return an ordered array of float", "math order_float \"[8, 6, 5]\"", "\"[5, 6, 8]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "A JSON Array", "String", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math pi", "get pi value\n", "math pi", "3.141592653589793", null, null, null, null, false, "");
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math pow", "get the value 'number1' and returns 'number 1' to the power of 'number'2'.\n", "math pow 0.5 7", "0.0078125", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math power", "get the value 'number1' and returns 'number 1' to the power of 'number'2'.<br>", "math power 0.5 7", "0.0078125", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math rad_to_deg", "converts an angle measured in radians to an approximately equivalent angle measured in degrees<br>", "math rad_to_deg 42", "2406.4227395494577", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math random", "get the random number between 0 and the given argument 'number'.<br>", "math random 42", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math rint", "returns the double value that is closest in value to the argument and is equal to a mathematical integer<br>", "math rint 42", "42", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math round", "rounds 'number 1' up to a precision specified by 'number 2'.<br>", "math round 1.23654 2", "1.24", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number1", "A number", "Number", true));
		mql.addParam(new MQLParam("number2", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math sign", "get the sign of a number<br>", "math sign 42", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math signum", "get the sign of a number and returns 1 if the number is positive and -1 if the number is negative.<br>", "math signum 42", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math sin", "get the sine of a number<br>", "math sin 42", "-0.9165215479156338", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math sinh", "get the hyperbolic sine of a number<br>", "math sinh 42", "8.696374707602505E17", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math sqrt", "get the square root of a non-negative number of the argument<br>", "math sqrt 42", "6.48074069840786", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math tan", "get the tangent of a number<br>", "math tan 42", "2.2913879924374863", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math tanh", "get the hyperbolic tangent of a number<br>", "math tanh 42", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);
		mql = new MQLDocumentation(true, "math ulp", "returns the size of an ulp of the argument. an ulp of a double value is the positive distance between this floating-point value and the double value next larger in magnitude<br>", "math ulp 42", "7.105427357601002E-15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("number", "A number", "Number", true));
		functions.get("Math").add(mql);

		functions.put("String", new Vector<MQLDocumentation>());
		page_description.put("String", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "string ascii", "convert a character to integer<br>", "string ascii \"a\"", "97", null, null, null, null, false, "");
		mql.addParam(new MQLParam("chr", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string bin", "returns a string representation of the binary value of n, where n is a long (bigint) number<br>", "string bin 97", "1100001", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string bit_length", "returns the length of the string str in bits<br>", "string bit_length 61", "16", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string char", "convert an integer to a character<br>", "string char 97", "a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string char_length", "returns the length of the string. the length is equal to the number of unicode code units in the string.<br>", "string char_length \"azerty\"", "6", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string char_to_int", "convert a character to an integer using the ASCII classification<br>", "string char_to_int \"a\"", "97", null, null, null, null, false, "");
		mql.addParam(new MQLParam("chr", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "concat", "concat all parameters (do not use this function with string before)<br>", "concat \"str1\" \"str2\" \"str3\"", "str1str2str3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("strN", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string csv_value", "convert a string into a valid csv value", "string csv_value \"a\" \",\" \"'\"", "a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The string data", "String", true));
		mql.addParam(new MQLParam("columnSeparator", "The column separator", "String", true));
		mql.addParam(new MQLParam("quoteChar", "Quote char", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string sentence_distance", "To get the sentence distance between two sentences", "string sentence_distance 40 70 \"word1 word2 word3\" \"word1 word2\"", "267.0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("activationPercent", "The activation percent", "Number", true));
		mql.addParam(new MQLParam("levenshteinPercent", "The levenshtein percent", "Number", true));
		mql.addParam(new MQLParam("sentence1", "The sentence 1", "String", true));
		mql.addParam(new MQLParam("sentence2", "The sentence 2", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string sentences_distance", "To get the sentence distance between several sentences", "string sentences_distance 40 70 \"vrai passionné\" \"{\n" + 
				"	\\\"1\\\": {\n" + 
				"		\\\"i\\\": \\\"des saveurs qui explosent en bouche\\\",\n" + 
				"		\\\"o\\\": \\\"1\\\"\n" + 
				"	},\n" + 
				"	\\\"2\\\": {\n" + 
				"		\\\"i\\\": \\\"un vrai passionné de cuisine\\\",\n" + 
				"		\\\"o\\\": \\\"-1\\\"\n" + 
				"	},\n" + 
				"	\\\"3\\\": {\n" + 
				"		\\\"i\\\": \\\"agréablement surpenant\\\",\n" + 
				"		\\\"o\\\": \\\"0\\\"\n" + 
				"	}\n" + 
				"}\"", "{\n" + 
						"  \\\"best_id\\\": \\\"2\\\",\n" + 
						"  \\\"best_predict\\\": \\\"-1\\\",\n" + 
						"  \\\"best_sentence\\\": \\\"un vrai passionné de cuisine\\\",\n" + 
						"  \\\"best_value\\\": 240.0,\n" + 
						"  \\\"handle\\\": {\n" + 
						"    \\\"1\\\": {\n" + 
						"      \\\"v\\\": 0.0,\n" + 
						"      \\\"i\\\": \\\"des saveurs qui explosent en bouche\\\",\n" + 
						"      \\\"o\\\": \\\"1\\\"\n" + 
						"    },\n" + 
						"    \\\"2\\\": {\n" + 
						"      \\\"v\\\": 240.0,\n" + 
						"      \\\"i\\\": \\\"un vrai passionné de cuisine\\\",\n" + 
						"      \\\"o\\\": \\\"-1\\\"\n" + 
						"    },\n" + 
						"    \\\"3\\\": {\n" + 
						"      \\\"v\\\": 0.0,\n" + 
						"      \\\"i\\\": \\\"agréablement surpenant\\\",\n" + 
						"      \\\"o\\\": \\\"0\\\"\n" + 
						"    }\n" + 
						"  }\n" + 
						"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("activationPercent", "The activation percent", "Number", true));
		mql.addParam(new MQLParam("levenshteinPercent", "The levenshtein percent", "Number", true));
		mql.addParam(new MQLParam("sentence1", "The sentence 1", "String", true));
		mql.addParam(new MQLParam("sentenceObj", "The sentence object (key=sentence)", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string order", "Return an ordered array of string", "string order \"[\\\"A\\\", \\\"C\\\", \\\"B\\\"]\"", "\"[\\\"A\\\", \\\"B\\\", \\\"C\\\"]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "A JSON Array", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string get_variable", "To get variables from a string", "string get_variable \"Paris is a beautifull city\" \"[1] is a beautifull city\"", "[\"Paris\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("input", "The input", "String", true));
		mql.addParam(new MQLParam("pattern", "The pattern ([1] for the first variable)", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string levenshtein_distance", "To get the levenshtein distance (length difference) between two strings", "string levenshtein_distance \"admn\" \"admin\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word1", "The word 1", "String", true));
		mql.addParam(new MQLParam("word2", "The word 2", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string md5", "get the md5", "string md5 \"admin\"", "21232f297a57a5a743894a0e4a801fc3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string sha", "get the sha", "string sha \"admin\"", "d033e22ae348aeb5660fc2140aec35850c4da997", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string count", "count the number of occurences of a string in another string<br>", "string count \"azertyaze\" \"a\"", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("string", "The string", "String", true));
		mql.addParam(new MQLParam("find", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_sign_generate_keypair", "Generate a key pair for sign encryption<br>", "string encode_sign_generate_keypair \"2048\"", "{\n" + 
				"  \"privateKey\": \"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnwvK73uoux+7wwULXE1OcTJO/KYuClwbX3B15jaE2KSW7PVIVkLehJ6gz0PnIXddpFaKRgoX1fjnovaUtA2n3Y3VzaHR0OEaYG9StkHGisXndDNuazzJ6BeUvvKA83OVB5JMn7skee3Fq29UPjRLhYNGnms/Ry7BpUYSO2KdjNGDgE/qDdc7PUSzDivM76BXdcDQHrrP6RGeqBL4a/3G/gq3jSjMoDLO1vMzdVzFRAX2fROCSBI641Y/UBryd3CQHXssUBBI9Kg3hEO60tr2X/oFDExIONP48y5NmsPsNRKw/hJ8MYaMDAxopuaMq5XUQ3EuOQf63HWOp75/RJNjXAgMBAAECggEBAJWBSdsd1KuuKAXM5alQovGm2QDCoH7X4xRaKLZb/rCKyQJtqopndw6aje7nrMLgxojy6/crUACw5iDBacOMyFHdBB2+mBG+2ULTgpzPHkY0Tfsua0MypbRTLqV1P4QeA/3OG0q6JegRcMkfcmgyfAH4y77HW6+HDAc/y5eWAs6HvOv69KjgVWwk7M+qePJAfqSA1mVfvtZp5woD3xzQY0hIb/lnG/tDCQp+p1v3rFrFpufdkgFX5JTx37oe4mnc4Zrpvr+qxekT1HlJVgo3V9ooN/oVAyuQXg58y3ZmYH6mCXlgNe+QYH/TknHWlqAnK0eu95dgHX6GtNA6Dz9TjsECgYEA0ZvSYs/cXHre4KdWV7h9higezi0JKfygEOBYVEeUiJ1cC/8a43rTns4oobnOczcs7UJUpR3iV+xNmw22rOwLXG8wjXOAmrjvC9iHhMNiNVox/hxZ75755FcjzFz8AOLVCUHeHMiIxrwm1lRL+92ZdYxgXh2IcXy0T+X3xicj5aECgYEAzOQdrsZ/7kaIYCgCF8ugl+aEYi83GE7J/RT0gemvt68JWOGdq18MfFzuYSeLbHZMSaWWbzlNj0mU1dwtgryfiXI3YHQ5Oe70Fef88cZ04lpnOrw+E1B539sG3Ldftif4bBgPfTrP3smXz8LAXdmB030yuiI1x8+fO//CgBrpO3cCgYEApOLw8y4UaON3JD4+m67Tf49b/FBNHyxNNjllApd0bqtq1z0Mh9n9UrVWHTKvBs9mlhVRxLnnbcbDna+B/jGJFmOZTAyKJCxKT4U7xeJ1BQc4wG7JCMiER22NCZwz2PaXIzsfYIEXp04nNv8E7tJ5f5C31hFhXfT9WsTKzqPHh0ECgYBOUWgTNacTEnzwnHpoVBq31ZKG4Vti/ELzbX4k5omXYd3lzp/xMKzaIL+x/Waq9/EjOZtuOm4uNkchFD+FOMqWxETspqB1R6SfdwTV+jEVkM7iwa/MvDdJ3TQbTDDWtSKdVvkcuk1Y8KAJ93yGoyygBbxNL+R00yUfzxyu0RqT8wKBgHinNaHsk3HY5rq7SR708dtj9dqc0DDQ+M+1M979XL8tfqCtZ17xluTi7QAdcq8xIX8ev1hkkkv6elxhjvcqFww9w45cpB76MFuW0dzfi7/32wFkwpwc5ih0+Qcp1qqJspiQKG3/2ur9sNndBejYmtNY4i5mhlllXCiF/87PKRTZ\",\n" + 
				"  \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp8Lyu97qLsfu8MFC1xNTnEyTvymLgpcG19wdeY2hNikluz1SFZC3oSeoM9D5yF3XaRWikYKF9X456L2lLQNp92N1c2h0dDhGmBvUrZBxorF53Qzbms8yegXlL7ygPNzlQeSTJ+7JHntxatvVD40S4WDRp5rP0cuwaVGEjtinYzRg4BP6g3XOz1Esw4rzO+gV3XA0B66z+kRnqgS+Gv9xv4Kt40ozKAyztbzM3VcxUQF9n0TgkgSOuNWP1Aa8ndwkB17LFAQSPSoN4RDutLa9l/6BQxMSDjT+PMuTZrD7DUSsP4SfDGGjAwMaKbmjKuV1ENxLjkH+tx1jqe+f0STY1wIDAQAB\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("keysize", "The key size", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_sign", "Get a signature from a string<br>", "string encode_sign \"azerty\" (json select \"keyId\" \"/privateKey\")", "MD0CHCGD3SEzr5rIZnSdnyT+DGLPqHbI9VFBjwY3yv0CHQCfA56aUbe0y/ixaXgx7GWXM/JFAYAXnU3uFP/l", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("privateKey", "The private key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_sign_verify", "Check a text with a signature and a public key<br>", "string decode_sign_verify \"azerty\" \"MD0CHCGD3SEzr5rIZnSdnyT+DGLPqHbI9VFBjwY3yv0CHQCfA56aUbe0y/ixaXgx7GWXM/JFAYAXnU3uFP/l\" (json select \"keyId\" \"/publicKey\")", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("signature", "The signature", "String", true));
		mql.addParam(new MQLParam("puvlicKey", "The public key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_rsa_generate_keypair", "Generate a key pair for RSA encryption<br>", "string encode_rsa_generate_keypair \"2048\"", "{\n" + 
				"  \"privateKey\": \"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnwvK73uoux+7wwULXE1OcTJO/KYuClwbX3B15jaE2KSW7PVIVkLehJ6gz0PnIXddpFaKRgoX1fjnovaUtA2n3Y3VzaHR0OEaYG9StkHGisXndDNuazzJ6BeUvvKA83OVB5JMn7skee3Fq29UPjRLhYNGnms/Ry7BpUYSO2KdjNGDgE/qDdc7PUSzDivM76BXdcDQHrrP6RGeqBL4a/3G/gq3jSjMoDLO1vMzdVzFRAX2fROCSBI641Y/UBryd3CQHXssUBBI9Kg3hEO60tr2X/oFDExIONP48y5NmsPsNRKw/hJ8MYaMDAxopuaMq5XUQ3EuOQf63HWOp75/RJNjXAgMBAAECggEBAJWBSdsd1KuuKAXM5alQovGm2QDCoH7X4xRaKLZb/rCKyQJtqopndw6aje7nrMLgxojy6/crUACw5iDBacOMyFHdBB2+mBG+2ULTgpzPHkY0Tfsua0MypbRTLqV1P4QeA/3OG0q6JegRcMkfcmgyfAH4y77HW6+HDAc/y5eWAs6HvOv69KjgVWwk7M+qePJAfqSA1mVfvtZp5woD3xzQY0hIb/lnG/tDCQp+p1v3rFrFpufdkgFX5JTx37oe4mnc4Zrpvr+qxekT1HlJVgo3V9ooN/oVAyuQXg58y3ZmYH6mCXlgNe+QYH/TknHWlqAnK0eu95dgHX6GtNA6Dz9TjsECgYEA0ZvSYs/cXHre4KdWV7h9higezi0JKfygEOBYVEeUiJ1cC/8a43rTns4oobnOczcs7UJUpR3iV+xNmw22rOwLXG8wjXOAmrjvC9iHhMNiNVox/hxZ75755FcjzFz8AOLVCUHeHMiIxrwm1lRL+92ZdYxgXh2IcXy0T+X3xicj5aECgYEAzOQdrsZ/7kaIYCgCF8ugl+aEYi83GE7J/RT0gemvt68JWOGdq18MfFzuYSeLbHZMSaWWbzlNj0mU1dwtgryfiXI3YHQ5Oe70Fef88cZ04lpnOrw+E1B539sG3Ldftif4bBgPfTrP3smXz8LAXdmB030yuiI1x8+fO//CgBrpO3cCgYEApOLw8y4UaON3JD4+m67Tf49b/FBNHyxNNjllApd0bqtq1z0Mh9n9UrVWHTKvBs9mlhVRxLnnbcbDna+B/jGJFmOZTAyKJCxKT4U7xeJ1BQc4wG7JCMiER22NCZwz2PaXIzsfYIEXp04nNv8E7tJ5f5C31hFhXfT9WsTKzqPHh0ECgYBOUWgTNacTEnzwnHpoVBq31ZKG4Vti/ELzbX4k5omXYd3lzp/xMKzaIL+x/Waq9/EjOZtuOm4uNkchFD+FOMqWxETspqB1R6SfdwTV+jEVkM7iwa/MvDdJ3TQbTDDWtSKdVvkcuk1Y8KAJ93yGoyygBbxNL+R00yUfzxyu0RqT8wKBgHinNaHsk3HY5rq7SR708dtj9dqc0DDQ+M+1M979XL8tfqCtZ17xluTi7QAdcq8xIX8ev1hkkkv6elxhjvcqFww9w45cpB76MFuW0dzfi7/32wFkwpwc5ih0+Qcp1qqJspiQKG3/2ur9sNndBejYmtNY4i5mhlllXCiF/87PKRTZ\",\n" + 
				"  \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp8Lyu97qLsfu8MFC1xNTnEyTvymLgpcG19wdeY2hNikluz1SFZC3oSeoM9D5yF3XaRWikYKF9X456L2lLQNp92N1c2h0dDhGmBvUrZBxorF53Qzbms8yegXlL7ygPNzlQeSTJ+7JHntxatvVD40S4WDRp5rP0cuwaVGEjtinYzRg4BP6g3XOz1Esw4rzO+gV3XA0B66z+kRnqgS+Gv9xv4Kt40ozKAyztbzM3VcxUQF9n0TgkgSOuNWP1Aa8ndwkB17LFAQSPSoN4RDutLa9l/6BQxMSDjT+PMuTZrD7DUSsP4SfDGGjAwMaKbmjKuV1ENxLjkH+tx1jqe+f0STY1wIDAQAB\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("keysize", "The key size", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_rsa", "Encode a string into DES algorithm<br>", "string encode_rsa \"azerty\" (json select \"keyId\" \"/publicKey\") (json select \"keyId\" \"/privateKey\")", "AI1yh7NIDFHjWeO3PKOTdC0sJC68MyZtCJvjG3fpkTFenS+8jSuS4qtq6XNB9X5fzBuyKKD0Cpcn4k8ePmoYJxuPdjnwLrE807QR/hl20lOUn773ldViV91LH0/nlfkxBeiEGjrUwN8he8nqeF8pVSqZiJpz5+YxnnG3nxcLQB1YABSlAtC9hx41+BLUbJx5AMFgtGZ72q/XxeTCKs8vXUc6TtAtF9TPvNznN506/TsS+CeaPAkLdwC3yLeI+u6Wlf9EwPDyfUtcpIvDxcRgWJYy1lwjgONCpRbgQTyX01Uk+dXAgctozYZnlVoyllwe5pevMemGG8y87llBJImCJCA=", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("publicKey", "The public key", "String", true));
		mql.addParam(new MQLParam("privateKey", "The private key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_rsa", "Decode a string from DES algorithm<br>", "string decode_rsa \"AI1yh7NIDFHjWeO3PKOTdC0sJC68MyZtCJvjG3fpkTFenS+8jSuS4qtq6XNB9X5fzBuyKKD0Cpcn4k8ePmoYJxuPdjnwLrE807QR/hl20lOUn773ldViV91LH0/nlfkxBeiEGjrUwN8he8nqeF8pVSqZiJpz5+YxnnG3nxcLQB1YABSlAtC9hx41+BLUbJx5AMFgtGZ72q/XxeTCKs8vXUc6TtAtF9TPvNznN506/TsS+CeaPAkLdwC3yLeI+u6Wlf9EwPDyfUtcpIvDxcRgWJYy1lwjgONCpRbgQTyX01Uk+dXAgctozYZnlVoyllwe5pevMemGG8y87llBJImCJCA=\" (json select \"keyId\" \"/privateKey\")", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("privateKey", "The private key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_des_generate_key", "Generate a key for DES encryption<br>", "string encode_des_generate_key \"56\"", "eVHgJt9uHAE=", null, null, null, null, false, "");
		mql.addParam(new MQLParam("keysize", "The key size", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_des", "Encode a string into DES algorithm<br>", "string encode_des \"azerty\" \"eVHgJt9uHAE=\"", "uMIF5QaE3zc=", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("secretKey", "The secret key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_des", "Decode a string from DES algorithm<br>", "string decode_des \"uMIF5QaE3zc=\" \"eVHgJt9uHAE=\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("secretKey", "The secret key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_blowfish", "Encode a string into blowfish algorithm<br>", "string encode_blowfish \"azerty\" \"mySecretKey\"", "HFnj8tkXUyY=", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("secretKey", "The secret key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_blowfish", "Decode a string from blowfish algorithm<br>", "string decode_blowfish \"HFnj8tkXUyY=\" \"mySecretKey\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("secretKey", "The secret key", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_pbe", "Encode a string into PBE algorithm<br>", "string encode_pbe \"azerty\" \"myPassword\"", "{\"encrypted\":\"gREfhYMVFjs=\",\"params\":\"MA0ECG5GZ4n6ieTAAgEK\"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data string", "String", true));
		mql.addParam(new MQLParam("password", "The password", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_pbe", "Decode a string from PBE algorithm<br>", "string decode_pbe \"{\"encrypted\":\"gREfhYMVFjs=\",\"params\":\"MA0ECG5GZ4n6ieTAAgEK\"}\" \"myPassword\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("json_data", "The json data and parameters string", "String", true));
		mql.addParam(new MQLParam("password", "The password", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode_b64", "encode a string into b64<br>", "string encode_b64 \"azerty\"", "YXplcnR5", null, null, null, null, false, "");
		mql.addParam(new MQLParam("string", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string decode_b64", "decode a string from b64<br>", "string decode_b64 \"YXplcnR5\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("string", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string del_char_before_each_line", "delete a number of char on each lines<br>", "string del_char_before_each_line \"sdlfkjdf<br>dfgdfgdfgdfgd\" 4", "kjdf<br>fgdfgdfgd<br>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "String", true));
		mql.addParam(new MQLParam("nbChar", "The number of chars", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string mql_to_html", "Convert a MQL source code to HTML", "in editor {string mql_to_html (mql {\n" + 
				"\n" + 
				"	concat \"r=\" [R];\n" + 
				"\n" + 
				"})};", "\n" + 
						"\n" + 
						"	<span style='color:#003cc8'>concat</span> <span style='color:#007700'>\"r=\"</span> <span style='color:#ba1c00'>[R]</span>;\n" + 
						"\n" + 
						"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("mql", "The mql source code", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string ends_with", "check if a value ends with another string<br>", "string ends_with \"azertyaze\" t", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringValue", "The string", "String", true));
		mql.addParam(new MQLParam("stringToEnd", "The end string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string first_letter_upper", "returns the string with the first letter in uppercase and the rest of the string in lowercase.<br>", "string first_letter_upper \"azerty\"", "Azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string first_letter", "returns the first letter from a string.<br>", "string first_letter \"azerty\"", "a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string generate_random_str", "generate a random string with the length of the random string as parameter.<br>", "string generate_random_str 1", "D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("size", "The size of the string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string hex", "returns a string representation of the hexadecimal value of num, where num is a long int (bigint) number<br>", "string hex 97", "61", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string hex_to_int", "convert an hexadecimal number to long int (bigint) number<br>", "string hex_to_int 97", "151", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hex", "The hex number", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string hex_to_str", "get the ASCII character associated to an hexadecimal number.<br>", "string hex_to_str 61", "a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hex", "The hexadecimal number", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string indent", "returns the indented string<br>", "string indent \"azerty<br>a<br>b<br>c\" 3", "azerty<br>   a<br>   b<br>   c", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("nbSpaceBefore", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string instr", "returns the index within this string of the first occurrence of the specified substring<br>", "string instr \"azerty\" \"r\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string instr", "returns the index within this string of the first occurrence of the specified substring, starting at the specified index<br>", "string instr \"azerty\" \"r\" 4", "-1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		mql.addParam(new MQLParam("fromIndex", "starting to index", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string int_to_char", "convert an integer to character<br>", "string int_to_char 97", "a", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string int_to_hex", "returns a string representation of the hexadecimal value of n, where n is a long int (bigint) number<br>", "string int_to_hex 97", "61", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string int_to_oct", "returns a string representation of the octal value of n, where n is a long int (bigint) number<br>", "string int_to_oct 97", "141", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string is_letter", "check if a value contains only letters<br>", "string is_letter \"abcd\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string is_alpha_num_uds", "check if a value contains only letters, underscore char or numbers<br>", "string is_alpha_num_uds \"abc_12d\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string is_alpha_num", "check if a value contains only letters or numbers<br>", "string is_alpha_num \"abc12d\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string is_number_char", "check if a value contains only number char<br>", "string is_number_char \"-456.45\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string itrim", "transform multiple spaces by only one space between strings<br>", "string itrim \"aze   rty\"", "aze rty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string lcase", "converts all of the characters in this string to lower case using the rules of the default locale<br>", "string lcase \"AZERTY\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string left", "returns every indexes of a string until the number of character returned is equal to the n paramater sent.<br>", "string left \"AZERTY\" 3", "AZE", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("len", "The length", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string length", "returns the length of this string. the length is equal to the number of unicode code units in the string.<br>", "string length \"azerty\"", "6", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string like", "performs a pattern match of a string expression expr against a pattern pat<br>", "string like \"azerty\" \".*ze.*\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string locate", "returns the index within this string of the first occurrence of the specified substring<br>", "string locate \"azerty\" \"r\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string locate", "returns the index within this string of the first occurrence of the specified substring, starting at the specified index<br>", "string locate \"azerty\" \"r\" 4", "-1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		mql.addParam(new MQLParam("fromIndex", "starting to index", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string lower", "converts all of the characters in this string to lower case using the rules of the default locale<br>", "string lower \"AZERTY\"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string lpad", "pads the left-side of a string with a specific set of characters<br>", "string lpad \"azertyaze\" \"#\" 10", "#azertyaze", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("padString", "The pad string", "String", true));
		mql.addParam(new MQLParam("paddedLength", "The end of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string lrtrim", "returns the string without space character on the left and on the right<br>", "string lrtrim \"  azerty   \"", "azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string lrtrim0", "returns the string without 0 character on the left and on the right<br>", "string lrtrim0 \"  123   \"", "  123   ", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string ltrim", "removes the space characters at the beginning of a string until a different character if found.<br>", "string ltrim \"  azerty   \"", "azerty&nbsp;&nbsp;&nbsp;", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string matches", "Verify if two strings are matching, the use of '.*' for autocompletion is handled.<br>", "string matches \"azerty\" \".*ze.*\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string mid", "returns a new string starting at index.<br>", "string mid \"azertyaze\" 3", "rtyaze", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("index", "The begin of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string mid", "returns a new string that starts at beginIndex of the original string and end at endIndex of the original string.<br>", "string mid \"azertyaze\" 3 5", "rt", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("beginIndex", "The begin of the sub string", "Number", true));
		mql.addParam(new MQLParam("endIndex", "The end of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string not_like", "verify if two strings are different.<br>", "string not_like \"azerty\" \".*ze.*\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string not_regexp", "verify if two strings are different.<br>", "string not_regexp \"azerty\" \".*ze.*\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string oct", "convert num which is an int to an octal form.<br>", "string oct 97", "141", null, null, null, null, false, "");
		mql.addParam(new MQLParam("num", "The number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string oct_to_int", "convert oct which is an octal number to long int (bigint).<br>", "string oct_to_int \"15\"", "13", null, null, null, null, false, "");
		mql.addParam(new MQLParam("oct", "The octal number", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string position", "returns the index at which the first occurence has been found.<br>", "string position \"azerty\" \"r\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string position", "returns the index within this string of the first occurrence of the specified substring, starting at the specified index<br>", "string position \"azerty\" \"r\" 4", "-1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		mql.addParam(new MQLParam("fromIndex", "starting to index", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string regexp", "verify if two strings are identical.<br>", "string regexp \"azerty\" \".*ze.*\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string repeat", "returns count number of times the string that was passed as parameter.<br>", "string repeat \"AZERTY\" 3", "AZERTYAZERTYAZERTY", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("count", "The count number", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string repeat_insert_str", "returns str with strToInsert inserted all n characters<br>", "string repeat_insert_str \"azertyuiop\" \"-\" 3", "aze-rty-uio-p-", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("strToInsert", "The string to insert", "String", true));
		mql.addParam(new MQLParam("incr", "The increment", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string replace", "replaces every occurences 'target' by the 'replacement' parameter in the string that was sent to this function.<br>", "string replace \"azerty\" \"z\" 9", "a9erty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("target", "The string target", "String", true));
		mql.addParam(new MQLParam("replacement", "The replacement", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string reverse", "returns the inverted form of 'str'.<br>", "string reverse \"AZERTY\"", "YTREZA", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string right", "get every charcater starting from the end of the string to len.<br>", "string right \"AZERTY\" 3", "RTY", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("len", "The length", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string rpad", "add characters at the end of the string only if specified 'paddedLength' is larger than the size of the original array.<br>", "string rpad \"azertyaze\" \"#\" 10", "azertyaze#", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("padString", "The pad string", "String", true));
		mql.addParam(new MQLParam("paddedLength", "The end of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string rtrim", "removes space characters starting from the end of the string until another character than space is found.<br>", "string rtrim \"  azerty   \"", "&nbsp;&nbsp;azerty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string space", "returns a string composed of n space characters<br>", "string space \"5\"", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", null, null, null, null, false, "");
		mql.addParam(new MQLParam("count", "The number of space", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string split", "Split a string with spaces used as separators.", "string split \"a b c\" \" \" -1", "[\"a\",\"b\",\"c\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("regex", "The regex", "String", true));
		mql.addParam(new MQLParam("limit", "The limit", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string split_mql", "Split an MQL source code", "string split_mql \"a b c\" 1", "b", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("index", "The index position", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string split_sentence", "Split a sentence using every non-alphanumerical characters as separators. If multiple non-alphanumerical characters are found, this function will still split the sentence without putting any non-alphanumerical characters in the words.", "string split_sentence \"Hello, how are you ? Bye.\" \",?.\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("chars", "The chars separator", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string starts_with", "verify if the first character of a string is equal to the first character of another string.<br>", "string starts_with \"azertyaze\" v", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringValue", "The string", "String", true));
		mql.addParam(new MQLParam("stringToStart", "The start string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string starts_with_or", "verify if the first character of a string is equal to the first character of another string.<br>", "string starts_with_or \"azertyaze\" aeiou", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringValue", "The string", "String", true));
		mql.addParam(new MQLParam("stringToStart", "The start string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string str_to_hex", "converts each character's ASCII decimal form to an hexadecimal form.<br>", "string str_to_hex 97", "3937", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string strcmp", "Compare two strings and returns the difference from their ASCII decimal form. If it returns 0, it means that the function found no differences, in any other cases it means that the strings are different.<br>", "string strcmp \"AZERTY\" \"iop\"", "-40", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string 1", "String", true));
		mql.addParam(new MQLParam("str2", "The string 2", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string strpos", "returns the index at which the first occurence between the two sent strings has been found. It will always return the lowest index possible. Returns -1 if no ocucrences are found.<br>", "string strpos \"azerty\" \"r\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string strpos", "returns the index at which the first occurence between the two sent strings has been found. It will always return the lowest index possible. Returns -1 if no ocucrences are found<br>", "string strpos \"azerty\" \"r\" 4", "-1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("str2", "The string", "String", true));
		mql.addParam(new MQLParam("fromIndex", "starting to index", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string sublrchar", "delete a number of char at the start and at the end of the string<br>", "string sublrchar \"azerty\" 1", "zert", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("numberDeleteChar", "The number of char to delete", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string substr", "returns the original string starting at the sent index.<br>", "string substr \"azertyaze\" 3", "rtyaze", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("index", "The begin of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string substr", "returns the original string, starting at 'beginIndex' and ending at 'endIndex'.<br>", "string substr \"azertyaze\" 3 5", "rt", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("beginIndex", "The begin of the sub string", "Number", true));
		mql.addParam(new MQLParam("endIndex", "The end of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string substring", "returns the original string starting at the sent index.<br>", "string substring \"azertyaze\" 3", "rtyaze", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("index", "The begin of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string substring", "returns the original string, starting at 'beginIndex' and ending at 'endIndex'.<br>", "string substring \"azertyaze\" 3 5", "rt", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("beginIndex", "The begin of the sub string", "Number", true));
		mql.addParam(new MQLParam("endIndex", "The end of the sub string", "Number", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string to_string", "return a valid sql string data (example: 'i\'am a man')<br>", "string to_string \"az\\\"erty\"", "'az\"erty'", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string encode", "encode a string to another encoding format<br>", "string encode \"az\\\"erty\" \"ISO-8859-1\" \"UTF-8\"", "az\"erty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		mql.addParam(new MQLParam("sourceEnc", "The source encoding format", "String", true));
		mql.addParam(new MQLParam("destinationEnc", "The destination encoding format", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string empty_if_null", "set the state of the string to empty if the string is null (ex : str = NULL, empty_if_null(str)-> str = '')<br>", "string empty_if_null \"a b@mail.com\"", "a b@mail.com", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string null_if_empty", "set the string to null if the string is empty (ex : str = '', null_if_empty(str)-> str = NULL)<br>", "string null_if_empty \"a b@mail.com\"", "a b@mail.com", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string trim", "returns the string without space character on the right, left and will put only one space if multiple spaces are found between non-space characters.<br>", "string trim \" aze   rty \"", "aze rty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string txt", "return a valid sql string data (example: 'i\'am a man')<br>", "string txt \"az'erty\"", "'az\\'erty'", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string txt2", "return a valid sql string data (example: 'i''am a man')<br>", "string txt2 \"az'erty\"", "'az''erty'", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string ucase", "converts all of the characters in this string to upper case using the rules of the default locale<br>", "string ucase \"azerty\"", "AZERTY", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string unhex", "convert an hexadecimal number to a long int(bigint).<br>", "string unhex 97", "151", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hex", "The hex number", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string upper", "converts of all the characters in this string to upper case using the rules of the default locale<br>", "string upper \"azerty\"", "AZERTY", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "string zero", "return 0 if 'str' is an empty string else the function will return the string<br>", "string zero \"az'erty\"", "az'erty", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "String", true));
		functions.get("String").add(mql);

		functions.put("Type", new Vector<MQLDocumentation>());
		page_description.put("Type", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "type is_bool", "This function will check if a value is equal to 'bool1' or 'bool2' and return a boolean as a result.<br>", "type is_bool 1 1 0", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		mql.addParam(new MQLParam("bool1", "The first boolean", "String", true));
		mql.addParam(new MQLParam("bool2", "The second boolean", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_enum", "This function will check if a value corresponds to one of the value from the enum and return a boolean as a result.", "type is_enum 1 1,2,3", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		mql.addParam(new MQLParam("values", "The values", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_char", "This function will check if the size of the string or char sent by the user as parameter is correct.<br>", "type is_char 1 1", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		mql.addParam(new MQLParam("size", "The char size authorized", "Number", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_date", "check if a value is a valid date (example: 1980-06-18) in english format, returning a boolean with 0 as false and 1 as true.<br>", "type is_date \"1980-06-18\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("date", "The date", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_decimal", "check if a value is a decimal number and returns a boolean as a result.<br>", "type is_decimal 12.23 4 5", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringDecimal", "The string in decimal format", "String", true));
		mql.addParam(new MQLParam("digitBeforeTheDecimalPoint", "The number of digit before the decimal point", "Number", true));
		mql.addParam(new MQLParam("digitAfterTheDecimalPoint", "The number of digit after the decimal point", "Number", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_email", "This function will check if a value is an email address and will return a boolean<br>", "type is_email \"jim@mentdb.org\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("emailAddress", "The email", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_hour", "check if a value is in hour format (example: 12:35:56)<br>", "type is_hour \"12:35:56\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringHour", "The hour", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_time", "check if a value is in hour format (example: 12:35:56)<br>", "type is_time \"12:35:56\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringHour", "The hour", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_hour_without_sec", "check if a value is in hour format (example: 12:35). If the value is not in hour format or if seconds are in the string sent by the user this function will return false.<br>", "type is_hour_without_sec \"12:35:56\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("stringHour", "The hour", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_integer", "This function will return true if the 'size' variable is equal to the number of characters given by the variable 'value'.(with size)<br>", "type is_integer 1456 5", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		mql.addParam(new MQLParam("size", "The char size authorized", "Number", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_number", "This function will return true if 'value' is a number else it will return 0.<br>", "type is_number 1456", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_matches_regex", "This fucntion compares two strings to see if both of the strings are identical. With '.*' the function will try to autocomplete a word to see if a match occurs (ex : type is_matches_regex(azerty, .*er.*) Here .*er.* will be replaced by azerty)., in this case, it will still return true if it matches after autocompletion.", "type is_matches_regex \"azerty\" \".*ze.*\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "the string", "String", true));
		mql.addParam(new MQLParam("pat", "the paterne", "String", true));
		functions.get("String").add(mql);
		mql = new MQLDocumentation(true, "type is_byte", "This function will check if a value is a byte and will return a boolean.<br>", "type is_byte 13", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_small_int", "This function will check if a value is a small integer and will return a boolean.<br>", "type is_small_int 13000", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_medium_int", "This function will check if a value is a medium integer and will return a boolean.<br>", "type is_medium_int 130000", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_int", "This function will check if a value is an integer and will return a boolean.<br>", "type is_int 1300000", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_big_int", "This function will check if a value is a big integer and will return a boolean.<br>", "type is_big_int 1300000", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_float", "This function will check if a value is a float number and will return a boolean.<br>", "type is_float 1300000.4", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_double", "This function will check if a value is a double number and will return a boolean.<br>", "type is_double 1300000.4", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_timestamp", "This function will check if a value is a timestamp format (example: '2009-06-18 12:00:00') and will return a boolean.<br>", "type is_timestamp \"2009-06-18 12:00:00\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_valid_date", "This function will check if a date is valid and will return a boolean.<br>", "type is_valid_date \"1980-06-18\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dateToValidate", "The date", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_valid_date", "This fucntion will check if a date is valid a boolean..<br>", "type is_valid_date \"1980-06-18\" \"yyyy-MM-dd\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dateToValidate", "The date", "String", true));
		mql.addParam(new MQLParam("format", "The date format", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_valid_time", "This function will check if a time is valid and will return a boolean.<br>", "type is_valid_time \"12:00:01\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timeToValidate", "The time (12:00:01)", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_valid_timestamp", "This function will check if a timestamp is valid and will return a boolean.<br>", "type is_valid_timestamp \"1980-06-18 12:00:01\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestampToValidate", "The timestamp", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_valid_timestamp", "This function will check if a timestamp is valid and will return a boolean..<br>", "type is_valid_timestamp \"1980-06-18 12:00:01\" \"yyyy-MM-dd HH:mm:ss\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timestampToValidate", "The timestamp", "String", true));
		mql.addParam(new MQLParam("format", "The format (ex: yyyy MM dd HH:mm:ss)", "String", true));
		functions.get("Type").add(mql);
		mql = new MQLDocumentation(true, "type is_varchar", "This function will check if the size of the string or char sent by the user as parameter is correct.<br>", "type is_varchar 1 1", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("value", "The value", "String", true));
		mql.addParam(new MQLParam("size", "The char size authorized", "Number", true));
		functions.get("Type").add(mql);

		functions.put("Constant", new Vector<MQLDocumentation>());
		page_description.put("Constant", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "constant math_four_pi", "Holds 4*pi.", "constant math_four_pi", "(12.5663706143591725 ± 8.9E-16)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant math_half_pi", "Holds pi/2.", "constant math_half_pi", "(1.5707963267948966 ± 1.1E-16)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant math_pi", "Holds the ratio of the circumference of a circle to its diameter", "constant math_pi", "(3.1415926535897931 ± 2.2E-16)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant math_pi_square", "Holds pi^2", "constant math_pi_square", "(9.869604401089358 ± 8.9E-16)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant math_two_pi", "Holds 2*pi", "constant math_two_pi", "(6.2831853071795862 ± 4.4E-16)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_a0", "Holds the Bohr radius (α/(4π·Rinf))", "constant physics_a0", "(5.291772E-11 ± 1.8E-17) m", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_amu", "Holds the unified atomic mass unit (0.001 kg/mol)/N", "constant physics_amu", "(1.6605389E-27 ± 2.8E-34) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_bohr_magneton", "Holds the Bohr magneton (ℏ·e/2me)", "constant physics_bohr_magneton", "(9.274009E-24 ± 4.0E-30) C·J·s/kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_c", "Holds the speed of light in vacuum (exact).", "constant physics_c", "299792458 m/s", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_c_square", "Holds c^2.", "constant physics_c_square", "89875517873681764 m²/s²", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_e", "Holds the elementary charge (positron charge).", "constant physics_e", "(1.6021765E-19 ± 1.4E-26) C", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_eh", "Holds the Hartree energy (2Rinf·h·c)", "constant physics_eh", "(4.3597442E-18 ± 7.2E-25) J", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_f", "Holds the Faraday constant (N·e)", "constant physics_f", "(96485.34 ± 0.024) C/mol", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_fine_structure", "Holds the fine structure constant (e²/(2·ε0·c·h))", "constant physics_fine_structure", "(0.007297353 ± 2.5E-9)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_g", "Holds the standard acceleration due to gravity (approximately equal to the acceleration due to gravity on the Earth's surface).", "constant physics_g", "(9.8066499999999976 ± 8.9E-16) m/s²", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_g0", "Holds the conductance quantum (2e²/h)", "constant physics_g0", "(0.00007748092 ± 2.6E-11) S", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_k", "Holds the Boltzmann constant.", "constant physics_k", "(1.380651E-23 ± 2.4E-29) J/K", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_lp", "Holds the Planck length (ℏ/(mP·c))", "constant physics_lp", "(1.6162E-35 ± 1.2E-39) m", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_m_deuteron", "Holds the deuteron rest mass.", "constant physics_m_deuteron", "(3.3435833E-27 ± 5.7E-34) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_m_electron", "Holds the electron rest mass.", "constant physics_m_electron", "(9.109383E-31 ± 1.6E-37) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_m_muon", "Holds the muon rest mass.", "constant physics_m_muon", "(1.8835314E-28 ± 3.3E-35) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_m_neutron", "Holds the neutron rest mass.", "constant physics_m_neutron", "(1.6749273E-27 ± 2.9E-34) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_m_proton", "Holds the proton rest mass.", "constant physics_m_proton", "(1.6726217E-27 ± 2.9E-34) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_magn_flux_quantum", "Holds the magnetic flux quantum (h/2e)", "constant physics_magn_flux_quantum", "(2.0678337E-15 ± 5.2E-22) Wb", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_mp", "Holds the Planck mass (ℏ·c/G)1/2", "constant physics_mp", "(2.1765E-8 ± 1.6E-12) kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_n", "Holds the Avogadro constant.", "constant physics_n", "(6.022142E23 ± 1.0E17) 1/mol", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_newtonian_g", "Holds the Newtonian constant of gravitation.", "constant physics_newtonian_g", "(6.674E-11 ± 1.0E-14) m³/(kg·s²)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_nuclear_magneton", "Holds the nuclear magneton (ℏ·e/2mp)", "constant physics_nuclear_magneton", "(5.050783E-27 ± 2.2E-33) C·J·s/kg", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_perm_vac_electric", "Holds the permeability of vacuum or electric constant.", "constant physics_perm_vac_electric", "(8.854187817620389E-12 ± 2.4E-27) A²·s²/(N·m²)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_perm_vac_magnetic", "Holds the permeability of vacuum or magnetic constant.", "constant physics_perm_vac_magnetic", "(0.0000012566370614359171 ± 1.1E-22) N/A²", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_planck", "Holds the Planck constant.", "constant physics_planck", "(6.626069E-34 ± 1.1E-40) J·s", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_planck_over_two_pi", "Holds the Planck constant over 2*pi.", "constant physics_planck_over_two_pi", "(1.0545717E-34 ± 1.8E-41) J·s", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_r", "Holds the molar gas constant (N·k)", "constant physics_r", "(8.31447 ± 0.000016) J/(mol·K)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_rinf", "Holds the Rydberg constant (α²·me·c/2h).", "constant physics_rinf", "(1.097373156852E7 ± 0.000073) 1/m", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_stefan_boltzmann", "Holds the Stefan-Boltzmann constant ((π²/60)·k4/(ℏ³·c²))", "constant physics_stefan_boltzmann", "(5.6704E-8 ± 4.2E-13) J/(K^4·s·m²)", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_tp", "Holds the Planck time (lP/c)", "constant physics_tp", "(5.3912E-44 ± 4.1E-48) s", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		mql = new MQLDocumentation(true, "constant physics_z0", "Holds the characteristic impedance of vacuum (µ0·c).", "constant physics_z0", "(376.73031346177061 ± 2.8E-14) Ω", null, null, null, null, false, "");
		functions.get("Constant").add(mql);
		
	}

}
