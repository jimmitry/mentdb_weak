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

package re.jpayet.mentdb.ext.dq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.MathFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.sql.SQLQueryManager;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;

//The data quality
public class DQManager {
	
	public static String VALID_DB_LIST = "AS400,SQLServer,MySQL,PostgreSQL,DB2,Oracle,H2,Derby,HSQL"; // , separator
	
	//Create the parameter object
	public static void init() throws Exception {
		
		JSONObject param = new JSONObject();
		
		Record2.add("record", "DQ[]", param.toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static void set(String key, String algo) throws Exception {
		
		synchronized ("DQ[]") {
		
			//Get the node
			JSONObject recNode = Record2.getNode("DQ[]");
	
			recNode.put(key.replace(":", " "), algo);
			
			Record2.update("DQ[]", recNode.toJSONString());
			
		}
		
	}
	
	public static boolean exist(String key) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode("DQ[]");
		
		return recNode.containsKey(key);
		
	}
	
	public static void remove(String key) throws Exception {
		
		synchronized ("DQ[]") {

			//Get the node
			JSONObject recNode = Record2.getNode("DQ[]");
			
			if (!recNode.containsKey(key)) {
				
				throw new Exception("Sorry, the key "+key+" does not exist.");
				
			}
	
			recNode.remove(key);
			
			Record2.update("DQ[]", recNode.toJSONString());
			
		}
		
	}
	
	public static JSONObject show() throws Exception {
		
		return Record2.getNode("DQ[]");
		
	}
	
	public static String generate(SessionThread session, String cmId, String table, String fieldname, String algorithms, String sql) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Initialization
		JSONArray algo = (JSONArray) JsonManager.load(algorithms);
		String result = "json load \"dq\" \"[]\";\n" + 
				"\n" + 
				"json load \"dq_"+fieldname+"\" \"{}\";\n" + 
				"json iobject \"dq_"+fieldname+"\" / field \""+fieldname+"\" STR;\n" + 
				"json iobject \"dq_"+fieldname+"\" / algo \"{}\" OBJ;\n";
		
		for(int i=0;i<algo.size();i++) {

			String key = (String) algo.get(i);
			String mql = DQManager.get_algo(key).replace("[VAR]", "[T_"+fieldname+"]");
			
			result += "json iobject \"dq_"+fieldname+"\" /algo \""+key.replace("\"", "\\\"")+"\" (mql {"+mql+"}) STR;\n";
		
		}
		
		result += "json iarray \"dq\" / (json doc \"dq_"+fieldname+"\") OBJ;\n" + 
				"\n" + 
				"dq analyse \""+cmId.replace("\"", "\\\"")+"\" (json doc \"dq\") \""+table.replace("\"", "\\\"")+"\" \""+sql.replace("\"", "\\\"")+"\";";
		
		return result;
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject analyse_show(EnvManager env, SessionThread session, String cmId, String json, String algoKey, String fieldKey, String title, String query, String parent_pid, String current_pid) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Get a connection
		Object[] obj = get_db_connection(session, cmId);
		
		Statement stm = null;
		ResultSet rs = null;
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "dq_table <"+title+">");
		JSONArray data = new JSONArray();
		table.put("data", data);
		
		JSONArray array = (JSONArray) JsonManager.load(json);
		
		HashMap<String, String> allAlgos = new HashMap<String, String>();
		
		HashMap<String, HashMap<String, Long>> storeValues = new HashMap<String, HashMap<String, Long>>();
		HashMap<String, HashMap<String, String>> storeAlgos = new HashMap<String, HashMap<String, String>>();
		for(int i=0;i<array.size();i++) {
			
			JSONObject o = (JSONObject) array.get(i);
			
			String field = (String) o.get("field");
			
			if (field.equals(fieldKey)) {
			
				HashMap<String, Long> val = new HashMap<String, Long>();
				storeValues.put(field, val);
				
				HashMap<String, String> al = new HashMap<String, String>();
				storeAlgos.put(field, al);
				
				JSONObject algos = (JSONObject) o.get("algo");
				
				val.put(algoKey, 0L);
				
				al.put(algoKey, (String) algos.get(algoKey));
				
				allAlgos.put(algoKey, null);
				
			}
			
		}

		Connection cnx = (Connection) obj[0];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(query);
			
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);
				
				columns.add(name);
				
				switch (meta.getColumnType(i+1)) {
				case Types.BIGINT: case Types.INTEGER: case Types.SMALLINT: case Types.TINYINT: 
					column_types.add("LONG");
					break;
				case Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT:  case Types.NUMERIC: case Types.REAL: 
					column_types.add("DOUBLE");
					break;
				default : 
					column_types.add("STRING");
				}
				

			}
			
			HashMap<String, String> algos = storeAlgos.get(fieldKey);
			String mql = algos.get(algoKey);
			
			//Parse the resultset
			while (rs.next()) {
				
				JSONObject line = new JSONObject();
				
				boolean saveLine = false;

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					String field = (String) columns.get(i);
					String val = rs.getString(i+1);
					
					env.set("[T_"+field+"]", val);
					
					line.put(field, val);
					
					if (storeAlgos.get(field)!=null) {
					
						try {
							
							String r = re.jpayet.mentdb.ext.statement.Statement.eval(session, mql, env, parent_pid, current_pid);
							
							if (r!=null && r.equals("1")) {
								
								saveLine = true;
								
							}
							
						} catch (Exception f) {}
						
					}
					
				}
				
				if (saveLine) data.add(line);

			}
			
			return table;
			
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

	@SuppressWarnings("unchecked")
	public static JSONObject analyse(EnvManager env, SessionThread session, String cmId, String json, String title, String query, String parent_pid, String current_pid) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Get a connection
		Object[] obj = get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		table.put("algo", json);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "dq <"+title+">");
		JSONArray data = new JSONArray();
		table.put("data", data);
		table.put("i_cmId", cmId);
		table.put("i_json", json);
		table.put("i_title", title);
		table.put("i_query", query);
		
		JSONArray array = (JSONArray) JsonManager.load(json);
		
		HashMap<String, String> allAlgos = new HashMap<String, String>();
		
		HashMap<String, HashMap<String, Long>> storeValues = new HashMap<String, HashMap<String, Long>>();
		HashMap<String, HashMap<String, String>> storeAlgos = new HashMap<String, HashMap<String, String>>();
		for(int i=0;i<array.size();i++) {
			
			JSONObject o = (JSONObject) array.get(i);
			
			String field = (String) o.get("field");
			
			HashMap<String, Long> val = new HashMap<String, Long>();
			storeValues.put(field, val);
			
			HashMap<String, String> al = new HashMap<String, String>();
			storeAlgos.put(field, al);
			
			JSONObject algos = (JSONObject) o.get("algo");
			
			for(Object algo : algos.keySet()) {
				
				String key = (String) algo;
				
				val.put(key, 0L);
				
				al.put(key, (String) algos.get(key));
				
				allAlgos.put(key, null);
				
			}
			
		}

		Connection cnx = (Connection) obj[0];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(query);
			
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);
				
				columns.add(name);
				
				switch (meta.getColumnType(i+1)) {
				case Types.BIGINT: case Types.INTEGER: case Types.SMALLINT: case Types.TINYINT: 
					column_types.add("LONG");
					break;
				case Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT:  case Types.NUMERIC: case Types.REAL: 
					column_types.add("DOUBLE");
					break;
				default : 
					column_types.add("STRING");
				}
				

			}
			
			long nbLine = 0;

			//Parse the resultset
			while (rs.next()) {
				
				nbLine++;

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					String field = (String) columns.get(i);
					String val = rs.getString(i+1);
					
					env.set("[T_"+field+"]", val);
					
					HashMap<String, Long> values = storeValues.get(field);
					HashMap<String, String> algos = storeAlgos.get(field);
					if (algos!=null) {
						
						for(Entry<String, String> e : algos.entrySet()) {
							
							try {
								
								String r = re.jpayet.mentdb.ext.statement.Statement.eval(session, e.getValue(), env, parent_pid, current_pid);

								if (r!=null && r.equals("1")) {

									values.put(e.getKey(), values.get(e.getKey())+1);
									
								}
								
							} catch (Exception f) {}
						
						}
						
					}
					
				}

			}
			
			columns = new JSONArray();
			table.put("columns", columns);

			column_types = new JSONArray();
			table.put("column_types", column_types);
			
			columns.add("field");
			column_types.add("STRING");
			
			for(Entry<String, String> a : allAlgos.entrySet()) {
				
				String algoId = a.getKey();

				columns.add(algoId);
				column_types.add("STRING");
				
			}

			for(Entry<String, HashMap<String, Long>> e : storeValues.entrySet()) {

				String field = e.getKey();
				
				//Reset the line
				JSONObject line = new JSONObject();
				line.put("field", field);
				
				HashMap<String, Long> values = e.getValue();
				
				for(Entry<String, String> a : allAlgos.entrySet()) {
					
					String algoId = a.getKey();
					
					if (values.get(algoId)!=null) {
						long v = values.get(algoId);
						double d = 100.0*v/nbLine;
						
						line.put(algoId, MathFx.round(""+d, "2")+"% ("+values.get(algoId)+"/"+nbLine+")");
					} else line.put(algoId, null);
					
				}
				
				data.add(line);
				
			}
			
			return table;
			
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
	
	public static String get_algo(String key) throws Exception {
		
		//Get the node
		JSONObject recNode = Record2.getNode("DQ[]");
		
		if (!recNode.containsKey(key)) {
			
			throw new Exception("Sorry, the key "+key+" does not exist.");
			
		}
		
		return ((String) recNode.get(key));
		
	}
	
	public static Object[] get_db_connection(SessionThread session, String cmId) throws Exception {
		
		//Load the configuration
		JSONObject conf = (JSONObject) JsonManager.load(Database.execute_admin_mql(session, "cm get \""+cmId.replace("\"", "\\\"")+"\";"));

		String subType = (String) conf.get("subType");
		String defaultSchema = (String) conf.get("defaultSchema");
		
		if (subType==null || AtomFx.find(VALID_DB_LIST, subType, ",").equals("0")) {
			
			throw new Exception("Sorry, only database in ["+VALID_DB_LIST+"] can use the data quality algorithms ...");
			
		}

		//Get config values
		JSONObject properties = (JSONObject) conf.get("properties");
		String driver = (String) conf.get("driver");
		String jdbc = (String) conf.get("jdbc");
		int loginTimeout = Integer.parseInt((String) conf.get("loginTimeout"));

		//Load the driver
		Class.forName(driver);

		//Set timeout for database connection
		DriverManager.setLoginTimeout(loginTimeout);

		//Parse and load all properties
		Properties p = new Properties();
		for(Object o : properties.keySet()) {

			//Get the current property
			String key = (String) o;
			String value = (String) properties.get(key);

			p.setProperty(key, value);

		}

		//Get the connection
		Connection connection = null;

		if (p.size()==0) connection = DriverManager.getConnection(jdbc);
		else connection = DriverManager.getConnection(jdbc, p);
		
		Object[] result = new Object[3];

		result[0] = connection;
		result[1] = subType;
		result[2] = defaultSchema;

		return result;
		
	}

	@SuppressWarnings("unchecked")
	public static JSONArray db_show_tables(SessionThread session, String cmId) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Get a connection
		Object[] obj = get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONArray result = new JSONArray();

		Connection cnx = (Connection) obj[0];
		String subType = (String) obj[1];
		String defaultSchema = (String) obj[2];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(SQLQueryManager.show_tables(subType, defaultSchema));
			
			//Parse the resultset
			while (rs.next()) {
				
				result.add(rs.getString(1));
				
			}
			
			session.env.allSubObjects.put(cmId, result);
			
			return result;
			
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

	@SuppressWarnings("unchecked")
	public static JSONObject db_show_data(SessionThread session, String cmId, String query, String sizes, String title) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Get a connection
		Object[] obj = get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		JSONArray column_sizes = new JSONArray();
		table.put("column_sizes", column_sizes);
		table.put("cmId", cmId);
		table.put("query", query);
		table.put("title", "table <"+title+">");
		if (sizes==null || sizes.equals("")) {
			table.put("mql", "sql select \""+cmId.replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\" \""+title.replace("\"", "\\\"")+"\";");
		} else {
			table.put("mql", "sql select \""+cmId.replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\" \""+sizes+"\" \""+title.replace("\"", "\\\"")+"\";");
		}
		JSONArray data = new JSONArray();
		table.put("data", data);

		Connection cnx = (Connection) obj[0];
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {
			
			//Get a statement and a resultset
			stm = cnx.createStatement();
			stm.setQueryTimeout(session.env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(session.idConnection, stm);
			rs = stm.executeQuery(query);
			
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				if (sizes!=null && !sizes.equals("")) {
					if (i<Integer.parseInt(AtomFx.size(sizes, ","))) {
						column_sizes.add(AtomFx.get(sizes, ""+(i+1), ","));
					} else {
						column_sizes.add("150");
					}
				}

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name);
				
				switch (meta.getColumnType(i+1)) {
				case Types.BIGINT: case Types.INTEGER: case Types.SMALLINT: case Types.TINYINT: 
					column_types.add("LONG");
					break;
				case Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT:  case Types.NUMERIC: case Types.REAL: 
					column_types.add("DOUBLE");
					break;
				default : 
					column_types.add("STRING");
				}
				

			}

			//Parse the resultset
			while (rs.next()) {

				//Reset the line
				JSONObject line = new JSONObject();

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					line.put((String) columns.get(i), rs.getString(i+1));
					
				}

				data.add(line);

			}
			
			return table;
			
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

	@SuppressWarnings("unchecked")
	public static JSONObject db_show_desc(SessionThread session, String cmId, String tablename) throws Exception {
		
		if (Misc.lrtrim(cmId).equals("MENTDB") && !GroupManager.isGrantedUser("sys", session.user)) {
			throw new Exception("Sorry, the user '"+session.user+"' is not granted with 'sys'.");
		}
		
		//Get a connection
		Object[] obj = get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "desc <"+tablename+">");
		JSONArray data = new JSONArray();
		table.put("data", data);

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
			
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name.toUpperCase());
				
				switch (meta.getColumnType(i+1)) {
				case Types.BIGINT: case Types.INTEGER: case Types.SMALLINT: case Types.TINYINT: 
					column_types.add("LONG");
					break;
				case Types.DECIMAL: case Types.DOUBLE: case Types.FLOAT:  case Types.NUMERIC: case Types.REAL: 
					column_types.add("DOUBLE");
					break;
				default : 
					column_types.add("STRING");
				}
				

			}

			//Parse the resultset
			while (rs.next()) {

				//Reset the line
				JSONObject line = new JSONObject();

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					line.put((String) columns.get(i), rs.getString(i+1));
					
				}

				data.add(line);

			}
			
			return table;
			
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