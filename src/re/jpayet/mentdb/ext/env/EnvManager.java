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

package re.jpayet.mentdb.ext.env;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.encog.ml.MLRegression;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.neural.networks.BasicNetwork;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.w3c.dom.Document;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import it.sauronsoftware.ftp4j.FTPClient;
import re.jpayet.mentdb.ext.client.MentDBConnector;

//The environment variable
public class EnvManager {

	//The environment variable
	public HashMap<String, StringBuilder> varEnv = new HashMap<String, StringBuilder>();
	public HashMap<String, Object> jsonObj = new HashMap<String, Object>();
	public HashMap<String, Document> xmlObj = new HashMap<String, Document>();
	public HashMap<String, Connection> sqlObj = new HashMap<String, Connection>();
	public HashMap<String, MongoCollection<org.bson.Document>> mongoCollectionObj = new HashMap<String, MongoCollection<org.bson.Document>>();
	public HashMap<String, MongoClient> mongoClientObj = new HashMap<String, MongoClient>();
	public HashMap<String, MongoDatabase> mongoBddObj = new HashMap<String, MongoDatabase>();
	public HashMap<String, FTPClient> ftpObj = new HashMap<String, FTPClient>();
	public HashMap<String, FTPSClient> ftpsObj = new HashMap<String, FTPSClient>();
	public HashMap<String, Session> sftpObj1 = new HashMap<String, Session>();
	public HashMap<String, Channel> sftpObj2 = new HashMap<String, Channel>();
	public HashMap<String, ChannelSftp> sftpObj3 = new HashMap<String, ChannelSftp>();
	public HashMap<String, Session> sshObj = new HashMap<String, Session>();
	public HashMap<String, Object> fileReaders = new HashMap<String, Object>();
	public HashMap<String, Object> fileWriters = new HashMap<String, Object>();
	public HashMap<String, MentDBConnector> mentdb = new HashMap<String, MentDBConnector>();
	public HashMap<String, HSSFWorkbook> excel = new HashMap<String, HSSFWorkbook>();
	public HashMap<String, XSSFWorkbook> excelx = new HashMap<String, XSSFWorkbook>();
	public HashMap<String, SimpleRegression> linearRegression = new HashMap<String, SimpleRegression>();
	public HashMap<String, OLSMultipleLinearRegression> multipleRegressionOls = new HashMap<String, OLSMultipleLinearRegression>();
	public HashMap<String, org.jsoup.nodes.Document> doms = new HashMap<String, org.jsoup.nodes.Document>();
	public HashMap<String, MultiLayerNetwork> dl4jModel = new HashMap<String, MultiLayerNetwork>();
	public HashMap<String, DataNormalization> dl4jNormalizer = new HashMap<String, DataNormalization>();
	public HashMap<String, HttpURLConnection> http_connections = new HashMap<String, HttpURLConnection>();
	public HashMap<String, HttpsURLConnection> https_connections = new HashMap<String, HttpsURLConnection>();
	
	public HashMap<String, List<Cluster<DoublePoint>>> clusters = new HashMap<String, List<Cluster<DoublePoint>>>();
	@SuppressWarnings("rawtypes")
	public HashMap<String, DBSCANClusterer> dbcluster = new HashMap<String, DBSCANClusterer>();
	public HashMap<String, DoublePoint[]> clusterOriginPoints = new HashMap<String, DoublePoint[]>();

	public HashMap<String, HipsterGraph<String,Double>> heuristicNode = new HashMap<String, HipsterGraph<String,Double>>();
	public HashMap<String, HashMap<String , SearchProblem<Double, String, WeightedNode<Double, String, Double>>>> heuristicNodeSearch = new HashMap<String, HashMap<String , SearchProblem<Double, String, WeightedNode<Double, String, Double>>>>();
	
	public HashMap<String, JSONArray> allSubObjects = new HashMap<String, JSONArray>();
	
	public BasicNetwork bn = null;
	public MLRegression mlr = null;
	public NormalizationHelper helper = null;
	
	//Initialization
	public Statement sql_block_stm = null;
	public ResultSet sql_block_rs = null;
	public int[] sql_block_indexStatement = {0}; //The value 0 is never used
	public String sql_block_tablename = null;
	
	public int sql_query_timeout = 0;
	
	//Show the environment
	@SuppressWarnings("unchecked")
	public JSONObject show() {
		
		JSONObject result = new JSONObject();
		
		for(Object o : varEnv.keySet()) {
			
			String key = (String) o;
			
			if (varEnv.get(key)==null) result.put(key, null);
			else result.put(key, varEnv.get(key).toString());
			
		}
		
		return result;
		
	}

	//Check if a string is a valid variable name
	public static boolean is_valid_varname(String varName) throws Exception {
		
		//Generate an error if the variable name is not valid
		if (!varName.startsWith("[") || !varName.endsWith("]") || varName.length()<3) {
			
			return false;
		
		} else {
			
			return true;
		
		}
		
	}

	//Set variable
	public void set(String varName, String value) throws Exception {
		
		if (value==null) varEnv.put(varName, null);
		else varEnv.put(varName, new StringBuilder(value));
		
	}

	//concat variable
	public void concat(String varName, String value) throws Exception {
		
		if (value!=null) {
			
			varEnv.get(varName).append(value);
			
		}
		
	}

	//Increment variable
	public String incr(String varName, String i) throws Exception {
		
		//Generate an error if the variable does not exist
		if (!varEnv.containsKey(varName)) {
			throw new Exception("Sorry, the variable name "+varName+" does not exist.");
		}
		
		try {
			
			double d = Double.parseDouble(varEnv.get(varName).toString())+Double.parseDouble(i);
			String result = ""+d;
			if (result.endsWith(".0")) result = result.substring(0, result.length()-2);
			
			varEnv.put(varName, new StringBuilder(result));
			return result;
			
		} catch (Exception e) {
			throw new Exception("Sorry, cannot increment the variable "+varName+" ["+e.getMessage()+"].");
		}
		
	}

	//Decrement variable
	public String decr(String varName, String i) throws Exception {
		
		//Generate an error if the variable does not exist
		if (!varEnv.containsKey(varName)) {
			throw new Exception("Sorry, the variable name "+varName+" does not exist.");
		}
		
		try {
			
			double d = Double.parseDouble(varEnv.get(varName).toString())-Double.parseDouble(i);
			String result = ""+d;
			if (result.endsWith(".0")) result = result.substring(0, result.length()-2);
			
			varEnv.put(varName, new StringBuilder(result));
			return result;
			
		} catch (Exception e) {
			throw new Exception("Sorry, cannot decrement the variable "+varName+" ["+e.getMessage()+"].");
		}
		
	}

	//remove variable
	public void remove(String varName) throws Exception {
		
		//Generate an error if the variable does not exist
		if (!varEnv.containsKey(varName)) {
			throw new Exception("Sorry, the variable name "+varName+" does not exist.");
		}
		
		varEnv.remove(varName);
		
	}

	//exist variable
	public boolean exist(String varName) {
		
		return varEnv.containsKey(varName);
		
	}

	//get variable
	public String get(String varName) throws Exception {
		
		//Generate an error if the variable does not exist
		if (!varEnv.containsKey(varName)) {
			throw new Exception("Sorry, the variable name "+varName+" does not exist.");
		}
		
		Object o = varEnv.get(varName);
		
		if (o==null) return null;
		else return ((StringBuilder) o).toString();
		
	}

}