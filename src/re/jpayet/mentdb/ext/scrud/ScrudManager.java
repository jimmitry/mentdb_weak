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

package re.jpayet.mentdb.ext.scrud;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.opencsv.CSVReader;

import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.sql.SQLQueryManager;

//SCRUD Operations
public class ScrudManager {

	public static String db_create(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "CREATE TABLE "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" (", column = "", pri = "";

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				column += SQLQueryManager.scrud_create_column(subType, rs);
				pri += SQLQueryManager.scrud_create_column_pri(subType, rs);

			}

			column = column.substring(0, column.length()-1);
			if (!pri.equals("")) {
				
				pri = pri.substring(0, pri.length()-1);
			
			}
			
			if (!pri.equals("")) {
				
				if (subType.equals("Oracle")) {
					query += column+ ", \n	CONSTRAINT "+tablename+"_pk PRIMARY KEY ("+pri+"\n	)\n"+
							");";
				} else {
					query += column+ ", \n	PRIMARY KEY ("+pri+"\n	)\n"+
						");";
				}
				
			} else {
				
				query += column+ "\n"+
						");";
				
			}
			
			
			return query;
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_export(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String query = "SELECT ", expression = "", where = "", script_param = "";
		
		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				script_param += SQLQueryManager.scrud_select_script_param(subType, rs);

			}

			expression = expression.substring(0, expression.length()-1);
			if (!where.equals("")) {
				
				where = where.substring(0, where.length()-5);
			
			}
			
			query += SQLQueryManager.scrud_select_limit2(subType, where.equals(""))+" "+expression+
					"\n			 FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+
					(where.equals("")?"":" \n			 WHERE "+where)+"\n"+SQLQueryManager.scrud_select_limit(subType, where.equals(""));
			
			return "script create exe \""+cmId+"."+tablename+".export\" false 1\n" + 
					"  (param\n" + 
					"  	(var \"[directory]\" {true} \"The directory\" is_null:false is_empty:false \"/Users/jimmitry/Desktop\")\n" + 
					"  	(var \"[filename]\" {true} \"The file name\" is_null:false is_empty:false \"test\")\n" + 
					"	(var \"[format]\" {type is_enum [format] \"json,xml,csv,excel,excelx,html,pdf\"} \"the format (json|xml|csv|excel|excelx|html|pdf)\" is_null:false is_empty:false \"json\")\n" + 
					script_param + 
					"  )\n" + 
					"  \"Export the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[query]\" (concat \""+query+"\");\n" + 
					"		\n" + 
					"		switch ([format])\n" + 
					"			(\"json\") {\n" + 
					"				file create (concat [directory] \"/\" [filename] \".json\") (sql to json \"session1\" \"products\" (concat [query]));\n" + 
					"			}\n" + 
					"			(\"xml\") {\n" + 
					"				file create (concat [directory] \"/\" [filename] \".xml\") (sql to xml \"session1\" \"products\" (concat [query]));\n" + 
					"			}\n" + 
					"			(\"csv\") {\n" + 
					"				file create (concat [directory] \"/\" [filename] \".csv\") (sql to csv \"session1\" \"products\" (concat [query]) \",\" \"'\");\n" + 
					"			}\n" + 
					"			(\"excel\") {\n" + 
					"				sql to excel \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".xls\");\n" + 
					"			}\n" + 
					"			(\"excelx\") {\n" + 
					"				sql to excelx \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".xlsx\");\n" + 
					"			}\n" + 
					"			(\"html\") {\n" + 
					"				file create (concat [directory] \"/\" [filename] \".html\") (sql to html \"session1\" \"products\" (concat [query]));\n" + 
					"			}\n" + 
					"			(\"pdf\") {\n" + 
					"				sql to pdf \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".pdf\");\n" + 
					"			}\n" + 
					"			{exception (1) (\"Sorry, the export format must be 'json|xml|csv|excel|excelx|html|pdf'.\");}\n" + 
					"		;\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return nothing\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_select(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String query = "SELECT ", expression = "", where = "", script_param = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				script_param += SQLQueryManager.scrud_select_script_param(subType, rs);

			}

			expression = expression.substring(0, expression.length()-1);
			if (!where.equals("")) {
				
				where = where.substring(0, where.length()-5);
			
			}
			
			query += SQLQueryManager.scrud_select_limit2(subType, where.equals(""))+" "+expression+
					"\n			 FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+
					(where.equals("")?"":" \n			 WHERE "+where)+"\n"+SQLQueryManager.scrud_select_limit(subType, where.equals(""));
			
			return "script create get \""+cmId+"."+tablename+".select\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Return rows from the table '"+tablename+"' in JSON.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[json_result]\" (sql to json \"session1\" \"products\" (concat \n" + 
					"			\""+query+"\"\n" + 
					"		));\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[json_result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return a JSON Object\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_parse(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String query = "SELECT ", expression = "", where = "", script_param = "", load_variables = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				script_param += SQLQueryManager.scrud_select_script_param(subType, rs);
				load_variables += SQLQueryManager.scrud_select_load_variables(subType, rs);

			}

			expression = expression.substring(0, expression.length()-1);
			if (!where.equals("")) {
				
				where = where.substring(0, where.length()-5);
			
			}
			
			query += SQLQueryManager.scrud_select_limit2(subType, where.equals(""))+" "+expression+
					"\n			 FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+
					(where.equals("")?"":" \n			 WHERE "+where)+"\n"+SQLQueryManager.scrud_select_limit(subType, where.equals(""));
			
			return "script create exe \""+cmId+"."+tablename+".parse_and_action\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Execute MQL action on the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[json_result]\" (sql parse \"session1\" \"T\" (concat \n"+
					"			\""+query+"\") {\n" + 
					"			\n" + 
					"			#Here the fields ...;\n" + 
					load_variables+ 
					"			\n" + 
					"			#Here your MQL code ...;\n" + 
					"			\n" + 
					"		\n" + 
					"		});\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[json_result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return a JSON result with all return lines.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_db_to_db(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "SELECT ", expression = "", where = "", whereSub = "", script_param = "", script_param_init = "", values = "", expression_update = "";
		String insert = "INSERT INTO "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" (";
		String update = "UPDATE "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" SET";
		String delete = "DELETE FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" WHERE ";

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				whereSub += SQLQueryManager.scrud_select_where_pk(subType, rs, "T_");
				script_param += SQLQueryManager.scrud_select_script_param(subType, rs);
				script_param_init += SQLQueryManager.scrud_select_script_param_init(subType, rs, cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.where.");
				values += SQLQueryManager.scrud_insert_values(subType, rs, "T_");
				

				expression_update += SQLQueryManager.scrud_update_expression(subType, rs, "T_");

			}

			expression = expression.substring(0, expression.length()-1);
			expression_update = expression_update.substring(0, expression_update.length()-1);
			values = values.substring(0, values.length()-2);
			if (!where.equals("")) {
				
				where = where.substring(0, where.length()-5);
			
			}
			if (!whereSub.equals("")) {
				
				whereSub = whereSub.substring(0, whereSub.length()-5);
			
			}

			update += expression_update+"\n			WHERE "+whereSub;
			delete += whereSub;
			
			insert += expression+
					"\n			) VALUES ("+
					values+"\n			);";
			
			query += SQLQueryManager.scrud_select_limit2(subType, where.equals(""))+" "+expression+
					"\n			 FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+
					(where.equals("")?"":" \n			 WHERE "+where)+"\n"+SQLQueryManager.scrud_select_limit(subType, where.equals(""));
			
			return "script create exe \""+cmId+"."+tablename+".db_to_db\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Move data from a table to another table.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	#To create parameters;\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.lock\" null;\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.err\" \"\";\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.ts\" \"\";\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.pk\" \"0\";\n" + 
					"\n" + script_param_init+
					"	\n" + 
					"	#Execute only if not use;\n" + 
					"	if (parameter lock_if_null \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.lock\" (concat \"Locked by: \" (who) \"/\" (sid))) {\n" + 
					"	\n" + 
					"		try {\n" + 
					"			\n" + 
					"			#Connection ...;\n" + 
					"			sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"			sql connect \"session2\" {cm get \""+cmId+"\"};\n" + 
					"			sql auto_commit \"session2\" false;\n" + 
					"			-> \"[nbToCommit]\" 0;\n" + 
					"			\n" + 
					"			-> \"[json_result]\" (sql parse \"session1\" \"T\" (concat \n"+
					"				\""+query+"\") {\n" + 
					"				\n" + 
					"				#Here your MQL code ...;\n\n" + 
					"				#INSERT REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session2\" (concat \n"+
					"					\""+insert+"\"\n"+
					"				));\n\n" + 
					"				#UPDATE REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session2\" (concat \n"+
					"					\""+update+"\"\n"+
					"				));\n\n" + 
					"				#DELETE REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session2\" (concat \n"+
					"					\""+delete+"\"\n"+
					"				));\n" + 
					"				\n" + 
					"				++ \"[nbToCommit]\";\n" + 
					"				\n" + 
					"				if (== [nbToCommit] 100) {\n" + 
					"					\n" + 
					"					sql commit \"session2\";\n" + 
					"					\n" + 
					"					-> \"[nbToCommit]\" 0;\n" + 
					"					\n" + 
					"				};\n" + 
					"			\n" + 
					"			});\n" + 
					"			\n" + 
					"			#End by commit;\n" + 
					"			sql commit \"session2\";\n" + 
					"			\n" + 
					"			#Disconnection ...;\n" + 
					"			sql disconnect \"session1\";\n" + 
					"			sql disconnect \"session2\";\n" + 
					"			\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.lock\" null;\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.err\" \"\";\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.ts\" \"\";\n" + 
					"			\n" + 
					"			\"OK\"\n" + 
					"			\n" + 
					"		} {\n" + 
					"			\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.lock\" null;\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.err\" [err];\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.ts\" (date systimestamp);\n" + 
					"		\n" + 
					"			#Close the connection;\n" + 
					"			try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"			try {sql disconnect \"session2\"} {} \"[sub_err]\";\n" + 
					"			\n" + 
					"			#Generate an error;\n" + 
					"			exception (1) ([err]);\n" + 
					"			\n" + 
					"		} \"[err]\";\n" + 
					"	\n" + 
					"	} {\n" + 
					"		\n" + 
					"		parameter get value \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".DB_TO_DB.lock\";\n" + 
					"		\n" + 
					"	}\n" + 
					"	\n" + 
					"} \"Return nothing.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_csv_to_db(SessionThread session, String cmId, String tablename, String filePath, String columnSeparator, String quoteChar, String forceColumnNames) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String expression = "", where = "", whereSub = "", values = "", expression_update = "", csv_vars = "";
		String insert = "INSERT INTO "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" (";
		String update = "UPDATE "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" SET";
		String delete = "DELETE FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" WHERE ";

		int[] indexStatement = {0}; //The value 0 is never used
		CSVReader reader = null;
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				whereSub += SQLQueryManager.scrud_select_where_pk(subType, rs, "T_");
				values += SQLQueryManager.scrud_insert_values(subType, rs, "T_");
				

				expression_update += SQLQueryManager.scrud_update_expression(subType, rs, "T_");

			}

			expression = expression.substring(0, expression.length()-1);
			expression_update = expression_update.substring(0, expression_update.length()-1);
			values = values.substring(0, values.length()-2);
			if (!where.equals("")) {
				
				where = where.substring(0, where.length()-5);
			
			}
			if (!whereSub.equals("")) {
				
				whereSub = whereSub.substring(0, whereSub.length()-5);
			
			}

			update += expression_update+"\n			WHERE "+whereSub;
			delete += whereSub;
			
			insert += expression+
					"\n			) VALUES ("+
					values+"\n			);";
			
			reader = new CSVReader(new FileReader(filePath), columnSeparator.charAt(0), quoteChar.charAt(0));
			
			String[] values_tab;
			
			if (forceColumnNames!=null && !forceColumnNames.equals("")) {
				
				//Get the first column
				values_tab = reader.readNext();
				
				int nb = Integer.parseInt(AtomFx.size(forceColumnNames, columnSeparator));
				for(int i=1;i<=nb;i++) {
					csv_vars+= "				[T_"+AtomFx.get(forceColumnNames, ""+i, columnSeparator)+"]\n";
				}
				
			} else {
				
				//Get the first column
				values_tab = reader.readNext();
				
				for(int i=0;i<values_tab.length;i++) {
					
					csv_vars+= "				[T_"+values_tab[i]+"]\n";
							
				}
				
			}
			
			return "script create exe \""+cmId+"."+tablename+".csv_to_db\" false 1\n" + 
					"  (param\n" + 
					"  	(var \"[csv]\" {true} \"The CSV file path\" is_null:false is_empty:false \"\")\n" + 
					"  )\n" + 
					"  \"Move data from a CSV to a table.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	#To create parameters;\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.lock\" null;\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.err\" \"\";\n" + 
					"	parameter add \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.ts\" \"\";\n" + 
					"	\n" + 
					"	#Execute only if not use;\n" + 
					"	if (parameter lock_if_null \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.lock\" (concat \"Locked by: \" (who) \"/\" (sid))) {\n" + 
					"	\n" + 
					"		try {\n" + 
					"			\n" + 
					"			#Connection ...;\n" + 
					"			sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"			sql auto_commit \"session1\" false;\n" + 
					"			-> \"[nbToCommit]\" 0;\n" + 
					"			\n" + 
					"			csv parse \"T\" [csv] \""+columnSeparator.replace("\"", "\\\"")+"\" \""+quoteChar.replace("\"", "\\\"")+"\" \""+forceColumnNames.replace("\"", "\\\"")+"\" {\n" + 
					"				\n" + 
					"				#CSV VARIABLE TO MATCH;\n\n" + 
					""+csv_vars+"\n" + 
					"				#INSERT REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"					\""+insert+"\"\n"+
					"				));\n\n" + 
					"				#UPDATE REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"					\""+update+"\"\n"+
					"				));\n\n" + 
					"				#DELETE REQUEST;\n" + 
					"				-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"					\""+delete+"\"\n"+
					"				));\n" + 
					"				\n" + 
					"				++ \"[nbToCommit]\";\n" + 
					"				\n" + 
					"				if (== [nbToCommit] 100) {\n" + 
					"					\n" + 
					"					sql commit \"session1\";\n" + 
					"					\n" + 
					"					-> \"[nbToCommit]\" 0;\n" + 
					"					\n" + 
					"				};\n" + 
					"			\n" + 
					"			};\n" + 
					"			\n" + 
					"			#End by commit;\n" + 
					"			sql commit \"session1\";\n" + 
					"			\n" + 
					"			#Disconnection ...;\n" + 
					"			sql disconnect \"session1\";\n" + 
					"			\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.lock\" null;\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.err\" \"\";\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.ts\" \"\";\n" + 
					"			\n" + 
					"			\"OK\"\n" + 
					"			\n" + 
					"		} {\n" + 
					"			\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.lock\" null;\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.err\" [err];\n" + 
					"			parameter update \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.ts\" (date systimestamp);\n" + 
					"		\n" + 
					"			#Close the connection;\n" + 
					"			try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"			\n" + 
					"			#Generate an error;\n" + 
					"			exception (1) ([err]);\n" + 
					"			\n" + 
					"		} \"[err]\";\n" + 
					"	\n" + 
					"	} {\n" + 
					"		\n" + 
					"		parameter get value \""+cmId.toUpperCase()+"."+tablename.toUpperCase()+".CSV_TO_DB.lock\";\n" + 
					"		\n" + 
					"	}\n" + 
					"	\n" + 
					"} \"Return nothing.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {reader.close();} catch (Exception f) {};
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_insert(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String expression = "", values = "", script_param = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "INSERT INTO "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" (";

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				values += SQLQueryManager.scrud_insert_values(subType, rs, "");
				script_param += SQLQueryManager.scrud_insert_script_param(subType, rs);

			}

			expression = expression.substring(0, expression.length()-1);
			values = values.substring(0, values.length()-2);
			
			query += expression+
					"\n			) VALUES ("+
					values+"\n			);";
			
			return "script create post \""+cmId+"."+tablename+".insert\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Insert a new element into the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"			\""+query+"\"\n"+
					"		));\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return the number of impacted lines.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_merge(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String expression = "", wexpression = "", values = "", script_param = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "INSERT INTO "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" (";

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				wexpression += SQLQueryManager.scrud_update_expression(subType, rs);
				expression += SQLQueryManager.scrud_select_expression(subType, rs);
				values += SQLQueryManager.scrud_insert_values(subType, rs, "");
				script_param += SQLQueryManager.scrud_insert_script_param(subType, rs);

			}

			wexpression = wexpression.substring(0, wexpression.length()-2);
			expression = expression.substring(0, expression.length()-1);
			values = values.substring(0, values.length()-2);
			
			query += expression+
					"\n			) VALUES ("+
					values+"\n			) ON DUPLICATE KEY UPDATE "+
					wexpression+";";
			
			return "script create post \""+cmId+"."+tablename+".merge\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Insert a new element into the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"			\""+query+"\"\n"+
					"		));\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return the number of impacted lines.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_update(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String expression = "", where = "", script_param = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "UPDATE "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename)+" SET ";

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				expression += SQLQueryManager.scrud_update_expression(subType, rs);
				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				script_param += SQLQueryManager.scrud_insert_script_param(subType, rs);

			}

			expression = expression.substring(0, expression.length()-2);
			if (!where.equals("")) {
				where = where.substring(0, where.length()-5);
			}
			
			query += expression+
					"\n			WHERE "+
					where+"\n			;";
			
			return "script create put \""+cmId+"."+tablename+".update\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Update an element into the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"			\""+query+"\"\n"+
					"		));\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return the number of impacted lines.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

	public static String db_delete(SessionThread session, String cmId, String tablename) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		String where = "", script_param = "";

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		
		String query = "DELETE FROM "+SQLQueryManager.scrud_select_tablename(subType, defaultSchema, tablename);

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.desc_tables(subType, defaultSchema, tablename));
			
			//Parse the resultset
			while (rs.next()) {

				where += SQLQueryManager.scrud_select_where_pk(subType, rs);
				script_param += SQLQueryManager.scrud_select_script_param(subType, rs);

			}

			if (!where.equals("")) {
				where = where.substring(0, where.length()-5);
			}
			
			query += "\n			WHERE "+
					where+"\n			;";
			
			return "script create delete \""+cmId+"."+tablename+".delete\" false 1\n" + 
					"  (param\n" + 
					script_param + 
					"  )\n" + 
					"  \"Delete an element from the table '"+tablename+"'.\"\n" + 
					"{\n" + 
					"	\n" + 
					"	try {\n" + 
					"		\n" + 
					"		#Connection ...;\n" + 
					"		sql connect \"session1\" {cm get \""+cmId+"\"};\n" + 
					"		\n" + 
					"		-> \"[result]\" (sql dml \"session1\" (concat \n"+
					"			\""+query+"\"\n"+
					"		));\n" + 
					"		\n" + 
					"		#Disconnection ...;\n" + 
					"		sql disconnect \"session1\";\n" + 
					"		\n" + 
					"		# Return the json;\n" + 
					"		[result]\n" + 
					"		\n" + 
					"	} {\n" + 
					"\n" + 
					"		#Close the connection;\n" + 
					"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
					"\n" + 
					"		#Generate an error;\n" + 
					"		exception (1) ([err]);\n" + 
					"		\n" + 
					"	} \"[err]\";\n" + 
					"	\n" + 
					"} \"Return the number of impacted lines.\";";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage());
			
		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(session.idConnection, indexStatement[0]);
				
			}
			
			//Close the connection and send an error message
			try {rs.close();} catch (Exception f) {};
			try {stm.close();} catch (Exception f) {};
			try {cnx.close();} catch (Exception f) {};
			
		}
		
	}

}