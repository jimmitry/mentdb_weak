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

package re.jpayet.mentdb.ext.mysql;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.compress.CompressManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.Misc;

public class MYSQLManager {

	static public Connection mysqlConnection = null;

	public static void connect() throws Exception {
		
		Properties p = new Properties();
		p.put("allowMultiQueries", "true");
		p.put("user", Start.MySQL_USER);
		p.put("password", Start.MySQL_PWD);
		mysqlConnection = DriverManager.getConnection("jdbc:mysql://"+Start.MySQL_HOST+":"+Start.MySQL_PORT+"/"+Start.MySQL_DB, p);  
		
	}

	public static void open(boolean newDatabase) throws Exception {
		
		Class.forName("com.mysql.jdbc.Driver");
		connect();
		
		if (newDatabase) {
			
			mysqlConnection.createStatement().execute(FileFx.load("tools"+File.separator+"mentdb.sql"));
			
		}
		
	}

	public static void resetLog() throws Exception {
		
		executeUpdate("DELETE FROM `logs`;", true);
		executeUpdate("ALTER TABLE `logs` AUTO_INCREMENT = 1;", true);
		
	}

	public static void resetMail() throws Exception {
		
		executeUpdate("DELETE FROM `mails`;", true);
		executeUpdate("ALTER TABLE `mails` AUTO_INCREMENT = 1;", true);
		
	}

	public static void addLog(String script, String parent_pid, String pid, String msg, String status, String c_key, String c_val) throws Exception {
		
		executeUpdate("INSERT INTO `logs`\n" + 
				"(`script`,\n" + 
				"`parent_pid`,\n" + 
				"`pid`,\n" + 
				"`msg`,\n" + 
				"`status`,\n" + 
				"`c_key`,\n" + 
				"`c_val`)\n" + 
				"VALUES\n" + 
				"("+SQLManager.encode(script)+",\n" + 
				SQLManager.encode(parent_pid)+",\n" + 
				SQLManager.encode(pid)+",\n" + 
				SQLManager.encode(msg)+",\n" + 
				SQLManager.encode(status)+",\n" + 
				SQLManager.encode(c_key)+",\n" + 
				SQLManager.encode(c_val)+");", false);
		
	}

	public static void removeLog() throws Exception {
		
		Statement stm = null;
		ResultSet rs = null;
		FileWriter fw = null;
		
		File currentLogFile = new File(Start.LOG_ARCHIVE_PATH+File.separator+"cur_log.sql");
		
		try {
			
			//Create the archive file if does not exist
			if (!currentLogFile.exists()) {
				
				Misc.create(Start.LOG_ARCHIVE_PATH+File.separator+"cur_log.sql", "");
				
			}
			
			fw = new FileWriter(currentLogFile, true);
			
			//Get data
			stm = mysqlConnection.createStatement();
			rs = stm.executeQuery("SELECT `logs`.`id`,\n" + 
					"    `logs`.`dtInsert`,\n" + 
					"    `logs`.`script`,\n" + 
					"    `logs`.`parent_pid`,\n" + 
					"    `logs`.`pid`,\n" + 
					"    `logs`.`msg`,\n" + 
					"    `logs`.`status`,\n" + 
					"    `logs`.`c_key`,\n" + 
					"    `logs`.`c_val`\n" + 
					"FROM `logs` WHERE DATEDIFF(CURRENT_TIMESTAMP, dtInsert)>"+Start.LOG_RETENTION_DAYS+" ORDER BY dtInsert");
			StringBuilder sql = new StringBuilder("");
			int nb = 0;
			
			//Parse the resultset
			while (rs.next()) {
				
				nb++;
				
				//Build the string
				sql.append("INSERT INTO `logs` " + 
						"(`id`, " + 
						"`dtInsert`, " + 
						"`script`, " + 
						"`parent_pid`, " + 
						"`pid`, " + 
						"`msg`, " + 
						"`status`, " + 
						"`c_key`, " + 
						"`c_val`) " + 
						"VALUES " + 
						"("+SQLManager.encode(rs.getString(1))+", " + 
						SQLManager.encode(rs.getString(2))+", " + 
						SQLManager.encode(rs.getString(3))+", " + 
						SQLManager.encode(rs.getString(4))+", " + 
						SQLManager.encode(rs.getString(5))+", " + 
						SQLManager.encode(rs.getString(6))+", " + 
						SQLManager.encode(rs.getString(7))+", " + 
						SQLManager.encode(rs.getString(8))+", " + 
						SQLManager.encode(rs.getString(9))+");\n");
				
				if (nb==20) {
					
					//Write data
					fw.write(sql.toString());

					//Flush the file
					fw.flush();
					
					sql = new StringBuilder("");
					
					nb = 0;
				
				}
				
			}
			
			if (sql.length()>0) {
				
				//Write str
				fw.write(sql.toString());

				//Flush the file
				fw.flush();
			
			}
			
			fw.close();
			
			if (currentLogFile.length()>=Long.parseLong(Start.LOG_ARCHIVE_SIZE)*1000000) {
			
				CompressManager.zip(Start.LOG_ARCHIVE_PATH+File.separator+"cur_log.sql", Start.LOG_ARCHIVE_PATH+File.separator+"log_"+DateFx.systimestamp_min()+".sql.zip");
				
				currentLogFile.delete();
				
			}
			
			executeUpdate("DELETE FROM `logs` WHERE DATEDIFF(CURRENT_TIMESTAMP, dtInsert)>"+Start.LOG_RETENTION_DAYS, true);
			
		} catch (Exception e) {
			
			throw new Exception(""+e.getMessage());
			
		} finally {
			
			//Close the writer
			try {if (fw!=null) fw.close();} catch (Exception F) {}
			
			try {
				if (stm!=null) stm.close();
			} catch (Exception F) {}
			
			try {
				if (rs!=null) rs.close();
			} catch (Exception F) {}
			
		}
		
	}
	
	public synchronized static String syncExecuteUpdate(String query, boolean throwException, boolean getLastUpdate) throws Exception {

		return execute(query, throwException, getLastUpdate);
		
	}
	
	public synchronized static String executeUpdate(String query, boolean throwException) throws Exception {

		return execute(query, throwException, false);
		
	}
	
	public static String execute(String query, boolean throwException, boolean getLastUpdate) throws Exception {

		Statement stm1 = null;
		ResultSet rs1 = null;
		String result = null;
		
		try {
			
			//Execute query
			stm1 = mysqlConnection.createStatement();
			if (getLastUpdate) {
				
				stm1.executeUpdate(query);
				rs1 = stm1.executeQuery("select LAST_INSERT_ID();");
				rs1.next();
				result = rs1.getString(1);
				
			} else {
				
				result = ""+ stm1.executeUpdate(query);
				
			}
			
			return result;
			
		} catch (Exception e) {

			Statement stm2 = null;
			ResultSet rs2 = null;
			
			try {
				
				//Try to reconnect
				connect();
				
				//Execute query
				stm2 = mysqlConnection.createStatement();
				if (getLastUpdate) {
					
					stm2.executeUpdate(query);
					rs2 = stm2.executeQuery("select LAST_INSERT_ID();");
					rs2.next();
					result = rs2.getString(1);
					
				} else {
					
					stm2.executeUpdate(query);
					
				}
				
				return result;
				
			} catch (Exception f) {
				
				//Nothing
				System.out.println("Error 'log write': "+f.getMessage());
				
				if (throwException) {
					
					throw new Exception(""+f.getMessage());
					
				}
				
				return result;
				
			} finally {
				
				try {
					if (rs2!=null) rs2.close();
				} catch (Exception g) {}
				
				try {
					if (stm2!=null) stm2.close();
				} catch (Exception g) {}
				
			}
			
		} finally {
			
			try {
				if (rs1!=null) rs1.close();
			} catch (Exception e) {}
			
			try {
				if (stm1!=null) stm1.close();
			} catch (Exception e) {}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject select_row(String sql) throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select(sql);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			ResultSetMetaData meta = rs.getMetaData();
			
			JSONObject result = new JSONObject();
			
			while (rs.next()) {
				
				for(int i=0;i<meta.getColumnCount();i++) {
					
					String name = "";
					
					try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
					try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
					if (name==null || name.equals("")) name = "EXPR"+(i+1);

					result.put(name, rs.getString(i+1));

				}
				
				break;
				
			}
			
			return result;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray select_rows(String sql) throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select(sql);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			ResultSetMetaData meta = rs.getMetaData();
			
			JSONArray result = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject line = new JSONObject();
				for(int i=0;i<meta.getColumnCount();i++) {
					
					String name = "";
					
					try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
					try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
					if (name==null || name.equals("")) name = "EXPR"+(i+1);

					line.put(name, rs.getString(i+1));

				}
				
				result.add(line);
				
			}
			
			return result;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject select_param(String pid) throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT \n" + 
					"    `stack_var`.`param`,\n" + 
					"    `stack_var`.`value`\n" + 
					"FROM `stack_var` where pid="+SQLManager.encode(pid));

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			JSONObject result = new JSONObject();
			
			while (rs.next()) {
				
				result.put(rs.getString(1), rs.getString(2));
				
			}
			
			return result;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}
	
	public static Object[] select(String query) throws Exception {

		Statement stm1 = null;
		ResultSet rs1 = null;
				
		try {
			
			//Execute query
			stm1 = mysqlConnection.createStatement();
			rs1 = stm1.executeQuery(query);
			
			Object[] result = {stm1, rs1};
			
			return result;
			
		} catch (Exception e) {
			
			try {
				if (rs1!=null) rs1.close();
			} catch (Exception f) {}
			
			try {
				if (stm1!=null) stm1.close();
			} catch (Exception f) {}
			

			Statement stm2 = null;
			ResultSet rs2 = null;
			
			try {
				
				//Try to reconnect
				connect();
				
				//Execute query
				stm2 = mysqlConnection.createStatement();
				rs2 = stm2.executeQuery(query);
				
				Object[] result = {stm2, rs2};
				
				return result;
				
			} catch (Exception f) {
				
				try {
					if (rs2!=null) rs2.close();
				} catch (Exception g) {}
				
				try {
					if (stm2!=null) stm2.close();
				} catch (Exception g) {}
				
				//Nothing
				System.out.println("Error 'log write': "+f.getMessage());
				
				throw new Exception(""+f.getMessage());
				
			}
			
		}
		
	}

	public static void close() {
		
		try {
			
			mysqlConnection.close();
			
		} catch (Exception e) {}
		
	}

}
