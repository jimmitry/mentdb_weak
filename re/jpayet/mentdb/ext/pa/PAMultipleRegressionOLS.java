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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.json.simple.JSONArray;

import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;

//Predictive analysis - Linear regression
public class PAMultipleRegressionOLS {

	public static String exist(EnvManager env, String regId) throws Exception {

		if (env.multipleRegressionOls.containsKey(regId)) {

			return "1";

		} else {

			return "0";

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {

		JSONArray result = new JSONArray();

		for (Object entry : env.multipleRegressionOls.keySet()) {
			result.add(entry.toString());
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static void load(EnvManager env, SessionThread session, String regId, String cmId, String fieldX1, String fieldX2, String fieldX3, String fieldX4, String fieldX5, String fieldY, String query) throws Exception {

		//Generate an error if the regression id already exist
		if (exist(env, regId).equals("1")) {

			throw new Exception("Sorry, the regression id '"+regId+"' already exist.");

		}

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

			List<Double> yy = new ArrayList<Double>();
			HashMap<String, List<Double>> xxx = new HashMap<String, List<Double>>();

			Vector<String> activateFields = new Vector<String>();

			if (!fieldX1.equals("")) {activateFields.add("x1");}
			if (!fieldX2.equals("")) {activateFields.add("x2");}
			if (!fieldX3.equals("")) {activateFields.add("x3");}
			if (!fieldX4.equals("")) {activateFields.add("x4");}
			if (!fieldX5.equals("")) {activateFields.add("x5");}

			//Parse the resultset
			while (rs.next()) {

				double x1 = 0, x2 = 0, x3 = 0, x4 = 0, x5 = 0, y = 0;

				boolean isValidValue = true;

				//Get the data
				for(int i=0;i<meta.getColumnCount();i++) {

					String field = (String) columns.get(i);
					String val = rs.getString(i+1);

					if (!fieldX1.equals("") && field.equals(fieldX1)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x1 = Double.parseDouble(val);

					}
					if (!fieldX2.equals("") && field.equals(fieldX2)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x2 = Double.parseDouble(val);

					}
					if (!fieldX3.equals("") && field.equals(fieldX3)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x3 = Double.parseDouble(val);

					}
					if (!fieldX4.equals("") && field.equals(fieldX4)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x4 = Double.parseDouble(val);

					}
					if (!fieldX5.equals("") && field.equals(fieldX5)) {

						if (val==null || val.equals("")) isValidValue = false;
						else x5 = Double.parseDouble(val);

					}
					if (field.equals(fieldY)) {

						if (val==null || val.equals("")) isValidValue = false;
						else y = Double.parseDouble(val);

					}

				}

				if (isValidValue) {

					yy.add(y);

					if (!fieldX1.equals("")) {

						if (!xxx.containsKey("x1")) xxx.put("x1", new Vector<Double>());

						xxx.get("x1").add(x1);

					}

					if (!fieldX2.equals("")) {

						if (!xxx.containsKey("x2")) xxx.put("x2", new Vector<Double>());

						xxx.get("x2").add(x2);

					}

					if (!fieldX3.equals("")) {

						if (!xxx.containsKey("x3")) xxx.put("x3", new Vector<Double>());

						xxx.get("x3").add(x3);

					}

					if (!fieldX4.equals("")) {

						if (!xxx.containsKey("x4")) xxx.put("x4", new Vector<Double>());

						xxx.get("x4").add(x4);

					}

					if (!fieldX5.equals("")) {

						if (!xxx.containsKey("x5")) xxx.put("x5", new Vector<Double>());

						xxx.get("x5").add(x5);

					}

				}

			}

			//Initialization
			OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

			double[] yyy = new double[yy.size()];
			for (int z = 0; z < yy.size(); z++) {
				yyy[z] = yy.get(z);
			}

			int nb = xxx.get(activateFields.get(0)).size();
			double[][] xxxx = new double[nb][];

			for(int i=0;i<nb;i++) {

				double[] xx = new double[activateFields.size()];
				for(int j=0;j<activateFields.size();j++) {

					xx[j] = xxx.get(activateFields.get(j)).get(i);

				}
				xxxx[i] = xx;

			}

			regression.newSampleData(yyy, xxxx);

			env.multipleRegressionOls.put(regId, regression);

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

	public static void loadFromJson(EnvManager env, String regId, String jsonx, String jsony) throws Exception {

		//Generate an error if the regression id already exist
		if (exist(env, regId).equals("1")) {

			throw new Exception("Sorry, the regression id '"+regId+"' already exist.");

		}

		//Initialization
		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		
		JSONArray yy = (JSONArray) JsonManager.load(jsony);

		double[] yyy = new double[yy.size()];
		for (int z = 0; z < yy.size(); z++) {
			yyy[z] = Double.parseDouble(""+yy.get(z));
		}
		
		JSONArray xx = (JSONArray) JsonManager.load(jsonx);

		double[][] xxxx = new double[xx.size()][];

		for(int i=0;i<xx.size();i++) {
			
			JSONArray row = (JSONArray) xx.get(i);

			double[] xxx = new double[row.size()];
			for(int j=0;j<row.size();j++) {

				xxx[j] = Double.parseDouble(""+row.get(j));

			}
			xxxx[i] = xxx;

		}

		regression.newSampleData(yyy, xxxx);

		env.multipleRegressionOls.put(regId, regression);

	}

	@SuppressWarnings("unchecked")
	public static JSONArray estimateRegressionParameters(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		JSONArray result = new JSONArray();

		double[] v = env.multipleRegressionOls.get(regId).estimateRegressionParameters();

		for(int i=0;i<v.length;i++) {


			result.add(v[i]);
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray estimateRegressionParametersStandardErrors(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		JSONArray result = new JSONArray();

		double[] v = env.multipleRegressionOls.get(regId).estimateRegressionParametersStandardErrors();

		for(int i=0;i<v.length;i++) {


			result.add(v[i]);
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray estimateResiduals(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		JSONArray result = new JSONArray();

		double[] v = env.multipleRegressionOls.get(regId).estimateResiduals();

		for(int i=0;i<v.length;i++) {
			result.add(v[i]);
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray estimateRegressionParametersVariance(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		JSONArray result = new JSONArray();

		double[][] v = env.multipleRegressionOls.get(regId).estimateRegressionParametersVariance();

		for(int i=0;i<v.length;i++) {

			JSONArray row = new JSONArray();

			for(int j=0;j<v[i].length;j++) {

				row.add(v[i][j]);

			}

			result.add(row);

		}

		return result;

	}

	public static String estimateRegressionStandardError(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).estimateRegressionStandardError();

	}

	public static String estimateRegressandVariance(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).estimateRegressandVariance();

	}

	public static String estimateErrorVariance(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).estimateErrorVariance();

	}

	public static String calculateTotalSumOfSquares(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).calculateTotalSumOfSquares();

	}

	public static String calculateRSquared(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).calculateRSquared();

	}

	public static String calculateResidualSumOfSquares(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).calculateResidualSumOfSquares();

	}

	public static String calculateAdjustedRSquared(EnvManager env, String regId) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		return ""+env.multipleRegressionOls.get(regId).calculateAdjustedRSquared();

	}

	public static void setNoIntercept(EnvManager env, String regId, String bool) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		if (bool!=null && bool.equals("1")) env.multipleRegressionOls.get(regId).setNoIntercept(true);
		else env.multipleRegressionOls.get(regId).setNoIntercept(false);

	}

	public static String predict(EnvManager env, String regId, String json) throws Exception {

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}
		
		JSONArray a = (JSONArray) JsonManager.load(json);
		double[] x = new double[a.size()];
		for(int z = 0;z<a.size();z++) {
			
			x[z] = Double.parseDouble(""+a.get(z));
			
		}

		double[] beta = env.multipleRegressionOls.get(regId).estimateRegressionParameters();

		// intercept at beta[0]
		double prediction = beta[0];
		for (int i = 1; i < beta.length; i++) {
			prediction += beta[i] * x[i - 1];
		}
		
		return ""+prediction;
	}

	public static String close(EnvManager env, String regId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the regression id does not exist
		if (exist(env, regId).equals("0")) {

			throw new Exception("Sorry, the regression id '"+regId+"' does not exist.");

		}

		env.multipleRegressionOls.remove(regId);

		return result;

	}

	public static String closeall(EnvManager env) throws Exception {

		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to close
		for (Entry<String, OLSMultipleLinearRegression> e : env.multipleRegressionOls.entrySet()) {

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

}