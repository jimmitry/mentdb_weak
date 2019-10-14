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

package re.jpayet.mentdb.ext.pa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;

//Predictive analysis - Linear regression
public class PALinearRegression {
	
	public static String exist(EnvManager env, String regId) throws Exception {

		if (env.linearRegression.containsKey(regId)) {

			return "1";

		} else {
			
			return "0";
			
		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {

		JSONArray result = new JSONArray();
		
		for (Object entry : env.linearRegression.keySet()) {
			result.add(entry.toString());
	    }

		return result;

	}

	@SuppressWarnings("unchecked")
	public static void load(EnvManager env, SessionThread session, String regId, String cmId, String fieldX, String fieldY, String query) throws Exception {
		
		//Generate an error if the regression id already exist
		if (exist(env, regId).equals("1")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' already exist.");
			
		}
		
		//Initialization
		SimpleRegression sr = new SimpleRegression();
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONArray columns = new JSONArray();
		
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

			}

			//Parse the resultset
			while (rs.next()) {
				
				double x = 0, y = 0;
				
				boolean isValidValue = true;

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					String field = (String) columns.get(i);
					String val = rs.getString(i+1);
					
					if (field.equals(fieldX)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x = Double.parseDouble(val);
						
					}
					if (field.equals(fieldY)) {
						
						if (val==null || val.equals("")) isValidValue = false;
						else y = Double.parseDouble(val);
						
					}
					
				}
				
				if (isValidValue) {
					
					sr.addData(x, y);
					
				}

			}
			
			env.linearRegression.put(regId, sr);
			
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

	public static void loadFromArray(EnvManager env, String regId, String json) throws Exception {
		
		//Generate an error if the regression id already exist
		if (exist(env, regId).equals("1")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' already exist.");
			
		}
		
		//Initialization
		SimpleRegression sr = new SimpleRegression();
		
		JSONArray array = (JSONArray) JsonManager.load(json);
		
		for(int i=0;i<array.size();i++) {
			
			JSONArray line = (JSONArray) array.get(i);
			
			sr.addData(Double.parseDouble(""+line.get(0)), Double.parseDouble(""+line.get(1)));
			
		}
		
		env.linearRegression.put(regId, sr);
		
	}

	public static void load_empty(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id already exist
		if (exist(env, regId).equals("1")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' already exist.");
			
		}
		
		//Initialization
		SimpleRegression sr = new SimpleRegression();
		
		env.linearRegression.put(regId, sr);
		
	}

	public static String get_slope(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSlope();
		
	}

	public static String get_intercept(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getIntercept();
		
	}

	public static String getInterceptStdErr(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getInterceptStdErr();
		
	}

	public static String getMeanSquareError(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getMeanSquareError();
		
	}

	public static String getN(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getN();
		
	}

	public static String getR(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getR();
		
	}

	public static String getRegressionSumSquares(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getRegressionSumSquares();
		
	}

	public static String getRSquare(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getRSquare();
		
	}

	public static String getSignificance(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSignificance();
		
	}

	public static String getSlopeConfidenceInterval(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSlopeConfidenceInterval();
		
	}

	public static String getSlopeStdErr(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSlopeStdErr();
		
	}

	public static String getSumSquaredErrors(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSumSquaredErrors();
		
	}

	public static String getTotalSumSquares(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getTotalSumSquares();
		
	}

	public static String getXSumSquares(EnvManager env, String regId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getXSumSquares();
		
	}

	public static String getSlopeConfidenceInterval(EnvManager env, String regId, String alpha) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).getSlopeConfidenceInterval(Double.parseDouble(alpha));
		
	}

	public static String get_prediction(EnvManager env, String regId, String x) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		return ""+env.linearRegression.get(regId).predict(Double.parseDouble(x));
		
	}

	public static void add_data(EnvManager env, String regId, String x, String y) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		env.linearRegression.get(regId).addData(Double.parseDouble(x), Double.parseDouble(y));
		
	}

	public static String close(EnvManager env, String regId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {
			
			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");
			
		}
		
		env.linearRegression.remove(regId);
		
		return result;
		
	}

	public static String closeall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, SimpleRegression> e : env.linearRegression.entrySet()) {
			
			allKeysToDelete.add(e.getKey());
			
		}
		
		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {
			
			try {
			
				//Close the document
				close(env, allKeysToDelete.get(i));
				nbClosed++;
				
			} catch (Exception e) {
				
				//Nothing to do
				
			}
			
		}
		
		return ""+nbClosed;
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject xy_scatter(SessionThread session, String cmId, String fieldX, String fieldY, String query) throws Exception {
		
		//Get a connection
		Object[] obj = DQManager.get_db_connection(session, cmId);

		Statement stm = null;
		ResultSet rs = null;
		
		JSONObject table = new JSONObject();
		JSONArray columns = new JSONArray();
		table.put("title", fieldX+", "+fieldY);
		JSONArray data = new JSONArray();
		table.put("data", data);
		JSONArray serie1 = new JSONArray();
		data.add(serie1);
		
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

				String name = "";

				try {if (name==null || name.equals("")) name = meta.getColumnLabel(i+1);} catch (Exception e) {}
				try {if (name==null || name.equals("")) name = meta.getColumnName(i+1);} catch (Exception e) {}
				if (name==null || name.equals("")) name = "EXPR"+(i+1);
				
				columns.add(name);

			}

			//Parse the resultset
			while (rs.next()) {
				
				JSONArray line = new JSONArray();
				line.add(0D);
				line.add(0D);
				
				boolean isValidValue = true;

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					String field = (String) columns.get(i);
					String val = rs.getString(i+1);
					
					if (field.equals(fieldX)) {

						if (val==null) isValidValue = false;
						else line.set(0, Double.parseDouble(val));
						
					}
					if (field.equals(fieldY)) {
						
						if (val==null) isValidValue = false;
						else line.set(1, Double.parseDouble(val));
						
					}
					
				}
				
				if (isValidValue) {
					
					serie1.add(line);
					
				}

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
	public static JSONObject xy_scatter(String json) throws Exception {
		
		JSONObject table = new JSONObject();
		table.put("title", "x, y");
		JSONArray data = new JSONArray();
		table.put("data", data);
		JSONArray serie1 = (JSONArray) JsonManager.load(json);
		data.add(serie1);
		
		return table;
		
	}

}