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

package re.jpayet.mentdb.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.Misc;

//Manage records
public class Record2 {
	
	public static Vector<ConnectorObj> connection_pool = new Vector<ConnectorObj>();
	
	public static void init() throws Exception {
		
		for(int i=0;i<Start.MAX_POOL_SIZE;i++) {
			
			ConnectorObj obj = new ConnectorObj();
			obj.pos = i;
			obj.cnt = load_one_connection();
			connection_pool.add(obj);
			
		}
		
	}
	
	public static void close() throws Exception {
		
		for(int i=0;i<Start.MAX_POOL_SIZE;i++) {
			
			try {
				
				connection_pool.get(i).cnt.close();
				
			} catch (Exception e) {}
			
		}
		
	}
	
	public static JSONObject getNode(String key) throws Exception {
		
		Connection recordConnection = connect();

		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			stm = recordConnection.createStatement();
			rs = stm.executeQuery("SELECT `val` FROM `record` WHERE `key`="+SQLManager.encode(key)+";");
			
			if (rs.next()) {
				
				String json = rs.getString(1);
				rs.close();
				stm.close();
				
				JSONParser jp = new JSONParser();
				if (Start.MODE_NO_LIGHT) return (JSONObject) jp.parse(json);
				else return (JSONObject) jp.parse(StringEscapeUtils.unescapeJava(json));
				
			} else {
				
				rs.close();
				stm.close();
				
				return null;
				
			}
			
		} catch (Exception f) {
			
			try {rs.close();} catch (Exception e) {}
			try {stm.close();} catch (Exception e) {}
			
			return null;
		}
		
	}

	public static Statement scriptSearch0() throws Exception {
		Connection recordConnection = connect();
		return recordConnection.createStatement();
	}
	public static boolean scriptSearch1(Statement stm, ResultSet rs, String key, String search) throws Exception {
		
		if (Start.MODE_NO_LIGHT) {
			
			rs = stm.executeQuery("SELECT count(*) FROM `record` WHERE `key` = "+SQLManager.encode("script["+key+"]")+" and JSON_EXTRACT(val, \"$.mql\") like "+(SQLManager.encode_like(search))+";");
			rs.next();
			if (Integer.parseInt(rs.getString(1))>0) return true;
			else return false;
			
		} else {

			rs = stm.executeQuery("SELECT val FROM `record` WHERE `key` = "+SQLManager.encode("script["+key+"]")+";");
			boolean b = false;
			while (rs.next()) {

				JSONObject val = (JSONObject) (new JSONParser().parse(StringEscapeUtils.unescapeJava(rs.getString(1))));

				String mql = (String) val.get("mql");
				if (mql.toLowerCase().indexOf(search.toLowerCase())>-1) {
					b = true;
					break;
				}

			}

			return b;
			
		}
		
	}
	public static void scriptSearch2(Statement stm, ResultSet rs) throws Exception {

		try {rs.close();} catch (Exception e) {}
		try {stm.close();} catch (Exception e) {}
		
	}
	
	public static long countRows(String table, String key) throws Exception {
		
		Connection recordConnection = connect();

		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			long nb = 0;
			
			stm = recordConnection.createStatement();
			if (key!=null && !key.equals("")) rs = stm.executeQuery("SELECT count(*) FROM `record` WHERE `key`="+SQLManager.encode(key)+";");
			else rs = stm.executeQuery("SELECT count(*) FROM `record` WHERE `type`="+SQLManager.encode(table)+";");
			
			if (rs.next()) {
				
				nb = Long.parseLong(rs.getString(1));
				
				rs.close();
				stm.close();
				
				return nb;
				
			} else {
				
				rs.close();
				stm.close();
				
				return nb;
				
			}
			
		} catch (Exception f) {
			
			try {rs.close();} catch (Exception e) {}
			try {stm.close();} catch (Exception e) {}
			
			throw f;
			
		}
		
	}
	
	public static ArrayList<JSONObject> getRows(String table) throws Exception {
		
		Connection recordConnection = connect();

		Statement stm = null;
		ResultSet rs = null;
		JSONParser jp = new JSONParser();
		
		try {
			
			ArrayList<JSONObject> result = new ArrayList<JSONObject>();
			
			stm = recordConnection.createStatement();
			rs = stm.executeQuery("SELECT `val` FROM `record` WHERE `type`="+SQLManager.encode(table)+" ORDER BY `key`;");
			
			while (rs.next()) {
				
				if (Start.MODE_NO_LIGHT) result.add((JSONObject) jp.parse(rs.getString(1)));
				else result.add((JSONObject) jp.parse(StringEscapeUtils.unescapeJava(rs.getString(1))));
				
			}
			
			rs.close();
			stm.close();
			
			return result;
			
		} catch (Exception f) {
			
			try {rs.close();} catch (Exception e) {}
			try {stm.close();} catch (Exception e) {}
			
			throw f;
			
		}
		
	}
	
	public static void add(String table, String key, String val) throws Exception {
		
		Connection recordConnection = connect();
		
		Statement stm = null;
		
		try {
			
			stm = recordConnection.createStatement();
			stm.executeUpdate("INSERT INTO `record` (`key`, `val`, `type`) VALUES ("+SQLManager.encode(key)+", "+SQLManager.encode(val)+", "+SQLManager.encode(table)+");");
			stm.close();
			
		} catch (Exception e) {
			
			try {stm.close();} catch (Exception f) {};
			throw e;
			
		}
		
	}
	
	public static void update(String key, String val) throws Exception {
		
		Connection recordConnection = connect();

		Statement stm = null;
		
		try {
			
			stm = recordConnection.createStatement();
			
			stm.executeUpdate("UPDATE `record` SET `val`="+SQLManager.encode(val)+" WHERE `key`="+SQLManager.encode(key)+";");
			
			stm.close();
			
		} catch (Exception e) {
			
			try {stm.close();} catch (Exception f) {};
			throw e;
			
		}
		
	}
	
	public static void remove(String key) throws Exception {
		
		Connection recordConnection = connect();

		Statement stm = null;
		
		try {
			
			stm = recordConnection.createStatement();
			
			stm.executeUpdate("DELETE FROM `record` WHERE `key`="+SQLManager.encode(key)+";");
			
			stm.close();
			
		} catch (Exception e) {
			
			try {stm.close();} catch (Exception f) {};
			throw e;
			
		}
		
	}

	public static Connection load_one_connection() throws Exception {
		
		Connection recordConnection = null;
		
		//System.out.println("New Connection ...");
		if (Start.MODE_NO_LIGHT) {
			
			Properties p = new Properties();
			p.put("user", Start.MySQL_USER);
			p.put("password", Start.MySQL_PWD);
			recordConnection = DriverManager.getConnection("jdbc:mysql://"+Start.MySQL_HOST+":"+Start.MySQL_PORT+"/"+Start.MySQL_DB+(!Misc.lrtrim(Start.MySQL_OPTION).equals("")?"?"+Misc.lrtrim(Start.MySQL_OPTION):""), p);  
		
		} else {
			
			Properties p = new Properties();
			p.put("user", Start.MySQL_USER);
			p.put("password", Start.MySQL_PWD);
			recordConnection = DriverManager.getConnection("jdbc:h2:./data/mentdb_h2;MODE=MYSQL;DATABASE_TO_LOWER=TRUE", p);  
		
		}
		
		return recordConnection;
		
	}
	
	public static AtomicInteger position = new AtomicInteger(0);

	public static Connection connect() throws Exception {
		
		int pos = position.incrementAndGet()%connection_pool.size();
		
		ConnectorObj obj = connection_pool.get(pos);
		
		synchronized (obj) {
			
			Connection recordConnection = obj.cnt;
			
			if (ping(recordConnection, pos)) {
				return recordConnection;
			}
			//System.out.println("New Connection ...");
			if (Start.MODE_NO_LIGHT) {
				
				Properties p = new Properties();
				p.put("user", Start.MySQL_USER);
				p.put("password", Start.MySQL_PWD);
				recordConnection = DriverManager.getConnection("jdbc:mysql://"+Start.MySQL_HOST+":"+Start.MySQL_PORT+"/"+Start.MySQL_DB+(!Misc.lrtrim(Start.MySQL_OPTION).equals("")?"?"+Misc.lrtrim(Start.MySQL_OPTION):""), p);  
			
			} else {
				
				Properties p = new Properties();
				p.put("user", Start.MySQL_USER);
				p.put("password", Start.MySQL_PWD);
				recordConnection = DriverManager.getConnection("jdbc:h2:./data/mentdb_h2;MODE=MYSQL;DATABASE_TO_LOWER=TRUE", p);  
			
			}
			
			obj.cnt = recordConnection;
			
			return recordConnection;
			
		}
		
	}
	
	public static boolean ping(Connection recordConnection, int pos) throws Exception {

		Statement stm = null;
		ResultSet rs = null;
		
		try {
			stm = recordConnection.createStatement();
			rs = stm.executeQuery("SELECT 1");
			
			if (rs.next()) {
				
				int i = Integer.parseInt(rs.getString(1)+"");
				if (i==1) {
					rs.close();
					stm.close();
					//System.out.println("CNX+++"+pos);
					return true;
				} else {
					
					rs.close();
					stm.close();
					recordConnection.close();
					//System.out.println("CNX---"+pos);
					return false;
				}
				
			} else {
				
				rs.close();
				stm.close();
				recordConnection.close();
				//System.out.println("CNX---"+pos);
				return false;
				
			}
			
		} catch (Exception e) {
			try {
				rs.close();
			} catch (Exception f) {}
			
			try {
				stm.close();
			} catch (Exception f) {}
			
			try {
				recordConnection.close();
			} catch (Exception f) {}
			System.out.println("CNX---"+pos+": "+e.getMessage());
			re.jpayet.mentdb.ext.log.Log.trace("MYSQL PING: "+e.getMessage());
			
			return false;
		}
		
	}

}
