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

package re.jpayet.mentdb.ext.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.pdf.PDFManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class SQLManager {
	
	public static ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Statement>> allStatements = new ConcurrentHashMap<Long, ConcurrentHashMap<Integer, Statement>>();
	private static int indexStatement = 0;
	
	public static synchronized int addStatement(long sessionId, Statement stm) {
		
		indexStatement++;
		if (indexStatement==0) indexStatement++;
		
		if (!allStatements.containsKey(sessionId)) allStatements.put(sessionId, new ConcurrentHashMap<Integer, Statement>());
		
		allStatements.get(sessionId).put(indexStatement, stm);
		
		return indexStatement;
		 
	}

	public static void deleteStatement(long sessionId, int index) {
		
		if (allStatements.containsKey(sessionId)
				&& allStatements.get(sessionId).containsKey(index)) {
			
			try {
				allStatements.get(sessionId).remove(index);
			} catch (Exception e) {}
		
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {

		//Initialization
		JSONArray loaded = new JSONArray();

		for(String key : env.sqlObj.keySet()) {

			loaded.add(key);

		}

		return loaded;

	}

	public static String connect(EnvManager env, String sqlId, String configJson) throws Exception {

		//Generate an error if the SQL id already exist
		if (env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' already exist.");

		}

		try {

			//Load the configuration
			JSONObject conf = (JSONObject) JsonManager.load(configJson);

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

			//Save the connection
			env.sqlObj.put(sqlId, connection);

			return "1";

		} catch (Exception e) {

			throw new Exception("Sorry, there is a database connection error ("+e.getMessage()+").");

		}

	}
	
	public static void set_timeout(EnvManager env, String sqlQueryTimeout) throws Exception {
		
		try {
			
			if (Integer.parseInt(sqlQueryTimeout)<0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the timeout must a valid integer >= 0");
			
		}
	
		env.sql_query_timeout = Integer.parseInt(sqlQueryTimeout);
	
	}

	public static String get_value(long sessionId, EnvManager env, String sqlId, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		String result = null;
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {

			Connection c = env.sqlObj.get(sqlId);
			
			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			int nb = 0;

			//Parse the resultset
			while (rs.next()) {

				nb++;

				result = rs.getString(1);

				break;

			}
			
			if (nb==0) {

				throw new Exception("Zero line returned.");

			}
			
			return result;

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONObject get_row(long sessionId, EnvManager env, String sqlId, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		JSONObject result = new JSONObject();
		JSONArray columns = new JSONArray();
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name);

			}
			
			//Parse the resultset
			while (rs.next()) {

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					result.put((String) columns.get(i), rs.getString(i+1));

				}

				break;

			}

			return result;

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONObject get_col_distinct(long sessionId, EnvManager env, String sqlId, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		JSONObject result = new JSONObject();
		JSONArray columns = new JSONArray();
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name);

			}
			
			//Parse the resultset
			while (rs.next()) {

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					result.put(rs.getString(i+1), (String) columns.get(i));

				}

			}

			return result;

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	public static void to_excel(long sessionId, EnvManager env, String sqlId, String tablename, String selectQuery, String filePath) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;

		HSSFWorkbook excel = new HSSFWorkbook();
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();
			excel.createSheet(tablename);
			HSSFSheet sh = excel.getSheet(tablename);
			HSSFRow row = sh.createRow(0);

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				HSSFCell cell = row.getCell(i, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				cell.setCellValue(name);

			}

			int j = 0;

			//Parse the resultset
			while (rs.next()) {

				j++;

				//Reset the line
				row = sh.createRow(j);

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					HSSFCell cell = row.getCell(i, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellValue(rs.getString(i+1));

				}

			}

			excel.write(new FileOutputStream(filePath));

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

			try {

				//Close the excel document
				excel.close();

			} catch (Exception f) {}

		}

	}

	public static void to_excelx(long sessionId, EnvManager env, String sqlId, String tablename, String selectQuery, String filePath) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;

		XSSFWorkbook excel = new XSSFWorkbook();
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();
			excel.createSheet(tablename);
			XSSFSheet sh = excel.getSheet(tablename);
			XSSFRow row = sh.createRow(0);

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				XSSFCell cell = row.getCell(i, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				cell.setCellValue(name);

			}

			int j = 0;

			//Parse the resultset
			while (rs.next()) {

				j++;

				//Reset the line
				row = sh.createRow(j);

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					XSSFCell cell = row.getCell(i, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellValue(rs.getString(i+1));

				}

			}

			excel.write(new FileOutputStream(filePath));

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

			try {

				//Close the excel document
				excel.close();

			} catch (Exception f) {}

		}

	}

	public static String to_html(long sessionId, EnvManager env, String sqlId, String tablename, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;

		StringBuilder result = new StringBuilder("<html><head>\n" + 
				"<style>\n" + 
				"table, td, th {\n" + 
				"    border: 1px solid black;\n" + 
				"    padding: 8px;\n" + 
				"}\n" + 
				"#table1 {\n" + 
				"    border-collapse: collapse;\n" + 
				"}\n" + 
				"</style>\n" + 
				"</head><body><h3>Table: <b>"+tablename+"</b></h3><br/>\n");

		int[] indexStatement = {0}; //The value 0 is never used
		
		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();
			
			result.append("<table id='table1'><tr>");
			
			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				result.append("<th>"+name+"</th>");

			}

			result.append("</tr>");

			//Parse the resultset
			while (rs.next()) {

				result.append("<tr>");

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					if (rs.getString(i+1)==null) result.append("<td style='color:#FF0000'>[NULL]</td>");
					else result.append("<td>"+rs.getString(i+1).replace("<", "&lt;")+"</td>");

				}
				
				result.append("</tr>");

			}

			result.append("</table></body></html>");
			
			return result.toString();

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	public static void to_pdf(long sessionId, EnvManager env, String sqlId, String tablename, String selectQuery, String filePath) throws Exception {

		PDFManager.pdfFromHtml(SQLManager.to_html(sessionId, env, sqlId, tablename, selectQuery), filePath);

	}

	@SuppressWarnings("unchecked")
	public static JSONObject to_json(long sessionId, EnvManager env, String sqlId, String tableName, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		JSONObject table = new JSONObject();
		table.put("table", tableName);
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray data = new JSONArray();
		table.put("data", data);
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columns.add(name);

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

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	//Convert a node to string representation
	public static String nodeToString(Node node) throws Exception {

		//Initialization
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(node);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;

	}

	public static String to_xml(long sessionId, EnvManager env, String sqlId, String tableName, String selectQuery) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("table");
		doc.appendChild(rootElement);

		// table name elements
		Element table = doc.createElement("table");
		table.appendChild(doc.createTextNode(tableName));
		rootElement.appendChild(table);
		
		// columns elements
		Element columns = doc.createElement("columns");
		rootElement.appendChild(columns);

		// data elements
		Element data = doc.createElement("data");
		rootElement.appendChild(data);
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);
			
			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();
			Vector<String> colnames = new Vector<String>();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";
				
				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				Element col = doc.createElement("item");
				col.appendChild(doc.createTextNode(name));
				columns.appendChild(col);

				colnames.add(name);

			}
			
			//Parse the resultset
			while (rs.next()) {

				//Reset the line
				Element line = doc.createElement("item");				

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					Element e = doc.createElement(colnames.get(i));
					if (rs.getString(i+1)==null) {
						e.appendChild(doc.createTextNode(""));
						e.setAttribute("nil", "true");
					}
					else e.appendChild(doc.createTextNode(rs.getString(i+1)));
					line.appendChild(e);

				}

				data.appendChild(line);

			}

			return nodeToString(doc);

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	public static String to_csv(long sessionId, EnvManager env, String sqlId, String tableName, String selectQuery, String columnSeparator, String quoteChar) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Generate an error if the columnSeparator length > 1
		if (columnSeparator.length()>1 || columnSeparator.length()==0) {

			throw new Exception("Sorry, the column separator must be a char(1).");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		StringBuilder tableBuilder = new StringBuilder();
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);
			String table = "";
			String columnNames = "";

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columnNames += columnSeparator+name;

			}

			columnNames = columnNames.substring(1);
			String line = "";

			//Parse the resultset
			while (rs.next()) {

				line = "";

				//Reset the line
				StringBuilder lineBuilder = new StringBuilder();

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					lineBuilder.append(columnSeparator + StringFx.csv_value(rs.getString(i+1), columnSeparator, quoteChar));

				}

				line = lineBuilder.toString();

				//Clear the first char
				if (!line.equals("")) {
					line = line.substring(1);
				}

				tableBuilder.append("\n" + line);

			}

			table = tableBuilder.toString();

			if (table.length()==0) return columnNames;
			else return columnNames+table;

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	public static void to_csv_file(long sessionId, EnvManager env, String sqlId, String filePath, String selectQuery, String columnSeparator, String quoteChar) throws Exception {
		
		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Generate an error if the columnSeparator length > 1
		if (columnSeparator.length()>1 || columnSeparator.length()==0) {

			throw new Exception("Sorry, the column separator must be a char(1).");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		int[] indexStatement = {0}; //The value 0 is never used
		FileWriter fw = null;

		try {

			//Initialization
			File file = new File(filePath);
			
			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file);
			
			Connection c = env.sqlObj.get(sqlId);
			String columnNames = "";

			//Get a statement and a resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				columnNames += columnSeparator+name;

			}

			columnNames = columnNames.substring(1);
			String line = "";
			
			//Write str
			fw.write(columnNames);

			//Flush the file
			fw.flush();

			//Parse the resultset
			while (rs.next()) {

				line = "";

				//Reset the line
				StringBuilder lineBuilder = new StringBuilder();

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					lineBuilder.append(columnSeparator + StringFx.csv_value(rs.getString(i+1), columnSeparator, quoteChar));

				}

				line = lineBuilder.toString();

				//Clear the first char
				if (!line.equals("")) {
					line = line.substring(1);
				}

				//Write str
				fw.write("\n" + line);

				//Flush the file
				fw.flush();

			}

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}
			
			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}

	public static String dml(long sessionId, EnvManager env, String sqlId, String query) throws Exception {
		
		//Initialization
		Statement stm = null;
		int[] indexStatement = {0}; //The value 0 is never used
		
		try {

			//Generate an error if the SQL id does not exist
			if (!env.sqlObj.containsKey(sqlId)) {
	
				throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");
	
			}
			
			Connection c = env.sqlObj.get(sqlId);
			
			//Get a statement and resultset
			stm = c.createStatement();
			
			stm.setQueryTimeout(env.sql_query_timeout);
			
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			
			return stm.executeUpdate(query)+"";

		} catch (Exception e) {
			
			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}
			
			try {
				//Close the statement
				stm.close();
			} catch (Exception f) {}

		}

	}

	public static void parse(long sessionId, EnvManager env, SessionThread session, String sqlId, String selectQuery, String namespace, String mqlAction, String parent_pid, String current_pid) throws Exception {

		//Initialization
		sqlId = re.jpayet.mentdb.ext.statement.Statement.eval(session, sqlId, env, parent_pid, current_pid);
		selectQuery = re.jpayet.mentdb.ext.statement.Statement.eval(session, selectQuery, env, parent_pid, current_pid);
		namespace = re.jpayet.mentdb.ext.statement.Statement.eval(session, namespace, env, parent_pid, current_pid);

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		//Initialization
		Statement stm = null;
		ResultSet rs = null;
		int[] indexStatement = {0}; //The value 0 is never used

		try {

			Connection c = env.sqlObj.get(sqlId);

			//Get a statement and resultset
			stm = c.createStatement();
			stm.setQueryTimeout(env.sql_query_timeout);
			indexStatement[0] = SQLManager.addStatement(sessionId, stm);
			rs = stm.executeQuery(selectQuery);
			ResultSetMetaData meta = rs.getMetaData();
			Vector<String> colnames = new Vector<String>();

			//Get column names
			for(int i=0;i<meta.getColumnCount();i++) {

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);

				colnames.add(name);

			}
			
			Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(mqlAction);
			
			try {

				//Parse the resultset
				while (rs.next()) {
	
					//Load variable
					for(int i=0;i<meta.getColumnCount();i++) {
	
						env.set("["+namespace+"_"+colnames.get(i)+"]", rs.getString(i+1));
	
					}
					
					try {
	
						//Execute action
						re.jpayet.mentdb.ext.statement.Statement.eval(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);
						
					} catch (Exception e) {
						if (e.getMessage()==null || !e.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
							throw e;
						}
					};
	
				}
				
			}  catch (Exception m) {
				if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
					throw m;
				}
			}

		} catch (Exception e) {

			throw new Exception("Sorry, there is a SQL error ("+e.getMessage()+").");

		} finally {
			
			if (indexStatement[0]!=0) {
				
				SQLManager.deleteStatement(sessionId, indexStatement[0]);
				
			}

			try {

				//Close the resultset
				rs.close();

			} catch (Exception f) {}

			try {

				//Close the statement
				stm.close();

			} catch (Exception f) {}

		}

	}

	public static String encode(String data) {

		//Return null if data is null
		if (data==null) {

			return "null";

		} else {

			//Replace all quotes in string and complete the string with '
			return "'"+data.replace("'", "''").replace("\\", "\\\\")+"'";

		}

	}

	public static String encode_like(String data) {

		//Return null if data is null
		if (data==null) {

			return "null";

		} else {

			//Replace all quotes in string and complete the string with '
			return "'%"+data.replace("'", "''").replace("\\", "\\\\")+"%'";

		}

	}

	public static void set_auto_commit(EnvManager env, String sqlId, String autoCommit) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		Connection c = env.sqlObj.get(sqlId);

		if (autoCommit.equals("1")) c.setAutoCommit(true);
		else c.setAutoCommit(false);

	}

	public static void commit(EnvManager env, String sqlId) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		Connection c = env.sqlObj.get(sqlId);

		c.commit();

	}

	public static void rollback(EnvManager env, String sqlId) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		Connection c = env.sqlObj.get(sqlId);

		c.rollback();

	}

	public static void disconnect(EnvManager env, String sqlId) throws Exception {

		//Generate an error if the SQL id does not exist
		if (!env.sqlObj.containsKey(sqlId)) {

			throw new Exception("Sorry, the SQL id '"+sqlId+"' does not exist.");

		}

		Connection c = env.sqlObj.get(sqlId);

		c.close();

		env.sqlObj.remove(sqlId);

	}

	@SuppressWarnings("unchecked")
	public static JSONArray disconnectAll(EnvManager env) throws Exception {

		JSONArray result = new JSONArray();

		//Parse all objects
		for(String sqlId : env.sqlObj.keySet()) {

			Connection c = env.sqlObj.get(sqlId);

			try {

				c.close();

				result.add(sqlId+": 1");

				env.sqlObj.remove(sqlId);

			} catch (Exception e) {

				result.add(sqlId+": ERR("+e.getMessage()+")");

			}

		}

		return result;

	}

}
