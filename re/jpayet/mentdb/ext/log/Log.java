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

package re.jpayet.mentdb.ext.log;

import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.stat.Statistic;

//The log class
public class Log {

	//The current ID of file log writer (mentdb.logX); X=0 or 1 or 2 or ... or 9
	public static int currentLogFileId = 0;

	//Default max log file size = 30 Mb
	public static long maxLogSize = 30;

	//Default number of line to show
	public static int currentNbLine = 10;
	
	//Show the last log text added into the log system
	public static String show(int nbLine) {
		
		//Set the current number of line if 0
		if (nbLine == 0) {
			
			nbLine = currentNbLine; 
		
		} else {
			
			currentNbLine = nbLine;
			
		}
		
		if (Statistic.systemName().toLowerCase().indexOf("windows")>-1) {
			nbLine = nbLine*2;
		}
		
		//Initialization
		int fileId = currentLogFileId;
		String lastLine = "";

		//Initialization
		java.io.RandomAccessFile fileHandler = null;
		
		try {
			
			//Initialization
			fileHandler = new java.io.RandomAccessFile( new File("logs"+File.separator+"mentdb"+fileId+".log"), "r" );
			
			long fileLength = fileHandler.length() - 1;
			StringBuilder sb = new StringBuilder();
			int line = 0;

			//Parse the file
			for(long filePointer = fileLength; filePointer != -1; filePointer--){
				fileHandler.seek( filePointer );
				int readByte = fileHandler.readByte();

				//Break if end of line
				if( readByte == 0xA ) {
					line = line + 1;
					if (line == nbLine) {
						if (filePointer == fileLength) {
							continue;
						}
						break;
					}
				} else if( readByte == 0xD ) {
					line = line + 1;
					if (line == nbLine) {
						if (filePointer == fileLength - 1) {
							continue;
						}
						break;
					}
				}
				
				//Append data
				sb.append( ( char ) readByte );
				
			}

			//Reverse the string
			lastLine = sb.reverse().toString();
			
		} catch( java.io.IOException e ) {
			
		} finally {
			
			if (fileHandler != null )
				
				try {
					
					fileHandler.close();
					
				} catch (Exception e) {
			}
			
		}
		
		//Return the line
		return lastLine;
		
	}
	
	/**
	 * @throws Exception 
	 * @name trace
	 * @description Trace in the log file system
	 */
	public static void write(String message, String status, String c_key, String c_val, String parent_pid) throws Exception {
		
		//Log only if the parent_pid is not null
		if (parent_pid!=null) {
			
			MYSQLManager.addLog(null, parent_pid, null, message, status, c_key, c_val);
		
		}
		
	}
	
	/**
	 * @name trace
	 * @description Trace in the log file system
	 */
	public static void trace(String message) {

		//Initialization
		FileWriter fileWriterCurrentLogFile = null;

		//Prepare the current log file
		File currentLogFile = new File("logs"+File.separator+"mentdb"+currentLogFileId+".log");

		try {

			//Check if current log file size is too large
			if (currentLogFile.length()>=(maxLogSize)*1000000)
			{

				//Too large then write in next log file
				currentLogFileId+=1;
				if (currentLogFileId>9) currentLogFileId=0;
				currentLogFile = new File("logs"+File.separator+"mentdb"+currentLogFileId+".log");

				//Clear the next log file
				currentLogFile.delete();
				currentLogFile.createNewFile();

			}

			//Write your message
			fileWriterCurrentLogFile = new FileWriter(currentLogFile, true);
			fileWriterCurrentLogFile.write((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SZ")).format(System.currentTimeMillis())+": "+message+System.getProperty("line.separator"));

		}
		catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, (new File(".")).getAbsolutePath()+"Err: "+e.getMessage());
			
		} finally {

			try {fileWriterCurrentLogFile.flush();} catch (Exception e) {};
			try {fileWriterCurrentLogFile.close();} catch (Exception e) {};

		};
		
	}

	@SuppressWarnings("unchecked")
	public static String search(String status, String script, String c_key, String c_val, String msgFilter,
			String dtMin, String dtMax, String dtOrder, String page, String nbByPage) throws Exception {
		
		//Initialization
		String condition = "";
		String orderby = "";
		String limit = "";
		
		if (script!=null && !script.equals("")) {
			
			condition += " and script like "+SQLManager.encode_like(script);
			
		}
		
		if (c_key!=null && !c_key.equals("")) {
			
			condition += " and c_key like "+SQLManager.encode_like(c_key);
			
		}
		
		if (c_val!=null && !c_val.equals("")) {
			
			condition += " and c_val like "+SQLManager.encode_like(c_val);
			
		}
		
		if (msgFilter!=null && !msgFilter.equals("")) {
			
			condition += " and msg like "+SQLManager.encode_like(msgFilter);
			
		}
		
		//Generate an error if the status is null or empty
		if (status==null || status.equals("")) {
			
			throw new Exception("Sorry, the status cannot be null or empty.");
			
		}
		
		status = status.toLowerCase();
		
		//Generate an error if the status is not valid
		if (!status.equals("ok") && !status.equals("ko")) {
			
			throw new Exception("Sorry, the status must be 'ok|ko'.");
			
		}
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "log <"+status+">");
		table.put("mql", "in out_editor {\n" + 
				"	log search \""+status+"\" \n" + 
				"	"+(script==null?"null":"\""+script+"\"")+" \n" + 
				"	\""+c_key.replace("\"", "\\\"")+"\" \n" + 
				"	\""+c_val.replace("\"", "\\\"")+"\" \n" + 
				"	\""+msgFilter.replace("\"", "\\\"")+"\" \n" + 
				"	\""+dtMin+"\"\n" + 
				"	\""+dtMax+"\"\n" + 
				"	"+dtOrder+" "+page+" "+nbByPage+";\n" + 
				"}");
		JSONArray data = new JSONArray();
		table.put("data", data);
		
		condition += " and status = "+SQLManager.encode(status.toUpperCase());
		
		//Generate an error if the min date
		if (DateFx.is_valid_timestamp(dtMin).equals("0")) {
			
			throw new Exception("Sorry, the min date is not a valid datetime.");
			
		}
		
		//Generate an error if the max date
		if (DateFx.is_valid_timestamp(dtMax).equals("0")) {
			
			throw new Exception("Sorry, the max date is not a valid datetime.");
			
		}
		
		//Generate an error if the ordered date is null or empty
		if (dtOrder==null || dtOrder.equals("")) {
			
			throw new Exception("Sorry, the ordered date cannot be null or empty.");
			
		}
		
		dtOrder = dtOrder.toLowerCase();
		
		//Generate an error if the ordered date is not valid
		if (!dtOrder.equals("asc") && !dtOrder.equals("desc")) {
			
			throw new Exception("Sorry, the ordered date must be 'asc|desc'.");
			
		}
		
		//Generate an error if the page is null or empty
		if (page==null || page.equals("")) {
			
			throw new Exception("Sorry, the page cannot be null or empty.");
			
		}
		
		//Generate an error if the number by page is null or empty
		if (nbByPage==null || nbByPage.equals("")) {
			
			throw new Exception("Sorry, the number by page cannot be null or empty.");
			
		}
		
		//The page must be a number
		try {
			
			if (Integer.parseInt(page)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the page must be a valid number (>=1).");
			
		}
		
		//The number by page must be a number
		try {
			
			if (Integer.parseInt(nbByPage)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the number by page must be a valid number (>=1).");
			
		}
		
		condition += " and (dtInsert BETWEEN "+SQLManager.encode(dtMin)+" AND "+SQLManager.encode(dtMax)+")";
		orderby = " order by dtInsert "+dtOrder;
		limit = " limit "+((Integer.parseInt(page)-1)*Integer.parseInt(nbByPage))+", "+nbByPage;
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT dtInsert, "
					+ "status, "
					+ "script, "
					+ "parent_pid, "
					+ "pid, " +
					"	 `c_key`,\n" + 
					"    `c_val`,\n" + 
					"    `msg`, id\n" + 
					"FROM `logs` WHERE 1=1"+condition+orderby+limit);
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
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
			
			while (rs.next()) {

				String l_dtInsert = rs.getString(1).substring(0, 19);
				String l_status = (rs.getString(2).equals("OK")?"ok":"KO");
				String l_script = rs.getString(3);
				
				String l_parent_pid = rs.getString(4);
				if (l_parent_pid==null) l_parent_pid = "";

				String l_pid = rs.getString(5);
				
				String l_c_key = rs.getString(6);
				if (l_c_key==null) l_c_key = "";
				
				String l_c_val = rs.getString(7);
				if (l_c_val==null) l_c_val = "";
				
				String l_msg = rs.getString(8);
				if (l_msg==null) l_msg = "";

				String l_id = rs.getString(9);
				
				JSONObject line = new JSONObject();
				line.put("dtInsert", l_dtInsert);
				line.put("status", l_status);
				line.put("script", l_script);
				line.put("parent_pid", l_parent_pid);
				line.put("pid", l_pid);
				line.put("c_key", l_c_key);
				line.put("c_val", l_c_val);
				line.put("msg", l_msg);
				line.put("id", l_id);
				
				data.add(line);
				
			}
			
			return table.toJSONString();

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
	public static String activity(String dtMin, String dtMax, String grouptype) throws Exception {
		
		if (grouptype==null) {
			grouptype = "";
		}
		
		grouptype = grouptype.toUpperCase();
		
		if (!(grouptype.equals("DAY") || grouptype.equals("MONTH") || grouptype.equals("YEAR") || grouptype.equals("SEC") || grouptype.equals("MIN") || grouptype.equals("HOUR"))) {
			
			throw new Exception("Sorry, the field must be SEC|MIN|HOUR|DAY|MONTH|YEAR.");
		
		}
		
		//Initialization
		String condition = "";
		
		JSONObject table = new JSONObject();
		JSONArray closed = new JSONArray();
		table.put("closed", closed);
		JSONArray error = new JSONArray();
		table.put("error", error);
		
		//Generate an error if the min date
		if (DateFx.is_valid_timestamp(dtMin).equals("0")) {
			
			throw new Exception("Sorry, the min date is not a valid datetime.");
			
		}
		
		//Generate an error if the max date
		if (DateFx.is_valid_timestamp(dtMax).equals("0")) {
			
			throw new Exception("Sorry, the max date is not a valid datetime.");
			
		}
		
		condition += " (dtexe BETWEEN "+SQLManager.encode(dtMin)+" AND "+SQLManager.encode(dtMax)+")";
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		String selectExpression = "";
		
		switch (grouptype) {
		case "YEAR":
			selectExpression = "SUBSTRING(dtexe,1,4)";
			break;
		case "MONTH":
			selectExpression = "SUBSTRING(dtexe,1,7)";
			break;
		case "DAY":
			selectExpression = "SUBSTRING(dtexe,1,10)";
			break;
		case "HOUR":
			selectExpression = "SUBSTRING(dtexe,1,13)";
			break;
		case "MIN":
			selectExpression = "SUBSTRING(dtexe,1,16)";
			break;
		case "SEC":
			selectExpression = "dtexe";
			break;
		}
		
		try {
			
			table.put("groupType", grouptype);
			table.put("mql", "in activity {\n" + 
					"	sql show activity "+grouptype+"\n" + 
					"	\""+dtMin+"\"\n" + 
					"	\""+dtMax+"\"\n" + 
					"}");
			
			cmo = MYSQLManager.select("SELECT "+selectExpression+", count(*)"
					+ "FROM `stack_closed` WHERE "+condition+
					"GROUP BY "+selectExpression);
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {
				
				JSONObject line = new JSONObject();
				line.put("t", rs.getString(1));
				line.put("v", rs.getString(2));
				
				closed.add(line);
				
			}
			
			rs.close();
			stm.close();
			
			cmo = MYSQLManager.select("SELECT "+selectExpression+", count(*)"
					+ "FROM `stack_error` WHERE "+condition+
					"GROUP BY "+selectExpression);
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {
				
				JSONObject line = new JSONObject();
				line.put("t", rs.getString(1));
				line.put("v", rs.getString(2));
				
				error.add(line);
				
			}
			
			return table.toJSONString();

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

	public static void get_pid_linked(String pid, HashMap<String, Integer> alreadySearch) throws Exception {
		
		//Initialization
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		Vector<String> toSearch = new Vector<String>();
		
		try {
			
			alreadySearch.put(pid, 0);
			
			cmo = MYSQLManager.select("SELECT parent_pid, pid\n" + 
					"FROM `logs` WHERE pid="+SQLManager.encode(pid)+" or parent_pid="+SQLManager.encode(pid)+"");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];

			while (rs.next()) {

				String found_parent_pid = rs.getString(1);
				String found_pid = rs.getString(2);
				
				if (found_pid!=null && !found_pid.equals("") && !alreadySearch.containsKey(found_pid)) {
				
					toSearch.add(found_pid);
					
				}
				
				if (found_parent_pid!=null && !found_parent_pid.equals("") && !alreadySearch.containsKey(found_parent_pid)) {
				
					toSearch.add(found_parent_pid);
					
				}
				
			}

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
		
		//Search sub
		for(int i = 0; i<toSearch.size();i++) {
			
			get_pid_linked(toSearch.get(i), alreadySearch);
			
		}
		
	}

	@SuppressWarnings("unchecked")
	public static String show_time(String pid) throws Exception {
		
		//Initialization
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		String sql_pid = "";
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray column_types = new JSONArray();
		table.put("column_types", column_types);
		table.put("title", "PID <"+pid+">");
		table.put("mql", "in out_editor {\n" + 
				"	log show_time \""+pid+"\"\n" + 
				"};");
		JSONArray data = new JSONArray();
		table.put("data", data);
		
		HashMap<String, Integer> pid_list = new HashMap<String, Integer>();
		get_pid_linked(pid, pid_list);
		
		for (Entry<String, Integer> e : pid_list.entrySet()) {
		    String cur_pid = e.getKey();
		    
		    sql_pid += ","+SQLManager.encode(cur_pid);
		    
		}
		
		sql_pid = sql_pid.substring(1);
		
		try {
			
			cmo = MYSQLManager.select("SELECT dtInsert, "
					+ "status, "
					+ "script, "
					+ "parent_pid, "
					+ "pid, " +
					"	 `c_key`,\n" + 
					"    `c_val`,\n" + 
					"    `msg`,\n" + 
					"    `id`\n" + 
					"FROM `logs` WHERE (pid in ("+sql_pid+") or parent_pid in ("+sql_pid+")) ORDER BY id;");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
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

			while (rs.next()) {

				String l_dtInsert = rs.getString(1).substring(0, 19);
				String l_status = (rs.getString(2).equals("OK")?"ok":"KO");
				String l_script = rs.getString(3);
				
				String l_parent_pid = rs.getString(4);
				if (l_parent_pid==null) l_parent_pid = "";

				String l_pid = rs.getString(5);
				
				String l_c_key = rs.getString(6);
				if (l_c_key==null) l_c_key = "";
				
				String l_c_val = rs.getString(7);
				if (l_c_val==null) l_c_val = "";
				
				String l_msg = rs.getString(8);
				if (l_msg==null) l_msg = "";

				String l_id = rs.getString(9);
				
				JSONObject line = new JSONObject();
				line.put("dtInsert", l_dtInsert);
				line.put("status", l_status);
				line.put("script", l_script);
				line.put("parent_pid", l_parent_pid);
				line.put("pid", l_pid);
				line.put("c_key", l_c_key);
				line.put("c_val", l_c_val);
				line.put("msg", l_msg);
				line.put("id", l_id);
				
				data.add(line);
				
			}
			
			return table.toJSONString();

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

}
