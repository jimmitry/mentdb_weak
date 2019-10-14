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

package re.jpayet.mentdb.ext.ml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import es.usc.citius.hipster.algorithm.Algorithm.SearchResult;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import re.jpayet.mentdb.ext.dq.DQManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;

//Machine learning
public class MLManager {

	public static String existClusters(EnvManager env, String clusterId) throws Exception {

		if (env.clusters.containsKey(clusterId)) {

			return "1";

		} else {

			return "0";

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray showClusters(EnvManager env) throws Exception {

		JSONArray result = new JSONArray();

		for (Object entry : env.clusters.keySet()) {
			result.add(entry.toString());
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static void load(EnvManager env, SessionThread session, String clusterId, String cmId, String fieldX, String fieldY, String maximumRadius, String minPoint, String query) throws Exception {
		
		//Generate an error if the regression id already exist
		if (existClusters(env, clusterId).equals("1")) {
			
			throw new Exception("Sorry, the cluster id '"+clusterId+"' already exist.");
			
		}
		
		List<DoublePoint> points = new ArrayList<DoublePoint>();
		
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
					
					points.add(new DoublePoint(new double[] { x, y }));
					
				}

			}
			
			DoublePoint[] ps = new DoublePoint[points.size()];
			for(int i=0;i<points.size();i++) {
				
				ps[i] = points.get(i);
				
			}
			
			DBSCANClusterer<DoublePoint> transformer = new DBSCANClusterer<DoublePoint>(Double.parseDouble(maximumRadius), Integer.parseInt(minPoint));
	        List<Cluster<DoublePoint>> clusters = transformer.cluster(Arrays.asList(ps));

	        env.clusters.put(clusterId, clusters);
	        env.dbcluster.put(clusterId, transformer);
	        env.clusterOriginPoints.put(clusterId, ps);
			
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

	public static void loadFromJson(EnvManager env, String clusterId, String json, String maximumRadius, String minPoint) throws Exception {
		
		//Generate an error if the regression id already exist
		if (existClusters(env, clusterId).equals("1")) {
			
			throw new Exception("Sorry, the cluster id '"+clusterId+"' already exist.");
			
		}
		
		JSONArray array = (JSONArray) JsonManager.load(json);
		DoublePoint[] ps = new DoublePoint[array.size()];
		
		for(int i=0;i<array.size();i++) {
			
			JSONArray line = (JSONArray) array.get(i);
			
			ps[i] = new DoublePoint(new double[] { Double.parseDouble(""+line.get(0)), Double.parseDouble(""+line.get(1)) });
			
		}
		
		DBSCANClusterer<DoublePoint> transformer = new DBSCANClusterer<DoublePoint>(Double.parseDouble(maximumRadius), Integer.parseInt(minPoint));
        List<Cluster<DoublePoint>> clusters = transformer.cluster(Arrays.asList(ps));

        env.clusters.put(clusterId, clusters);
        env.dbcluster.put(clusterId, transformer);
        env.clusterOriginPoints.put(clusterId, ps);
		
	}

	public static String nbCluster(EnvManager env, String clusterId) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
		
		return ""+clusters.size();

	}

	public static String nbPoint(EnvManager env, String clusterId, String clusterIndex) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
		
		return ""+clusters.get(Integer.parseInt(clusterIndex)).getPoints().size();

	}

	@SuppressWarnings("unchecked")
	public static JSONArray points(EnvManager env, String clusterId, String clusterIndex) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
		
		JSONArray result = new JSONArray();
		
		List<DoublePoint> l = clusters.get(Integer.parseInt(clusterIndex)).getPoints();
		
		for(int i=0;i<l.size();i++) {
			
			JSONArray row = new JSONArray();

			row.add(l.get(i).getPoint()[0]);
			row.add(l.get(i).getPoint()[1]);
			
			result.add(row);
			
		}
		
		return result;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray getPoint(EnvManager env, String clusterId, String clusterIndex, String pointIndex) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
		
		JSONArray result = new JSONArray();
		
		List<DoublePoint> l = clusters.get(Integer.parseInt(clusterIndex)).getPoints();
		
		result.add(l.get(Integer.parseInt(pointIndex)).getPoint()[0]);
		result.add(l.get(Integer.parseInt(pointIndex)).getPoint()[1]);
		
		return result;

	}

	public static void deletePoint(EnvManager env, String clusterId, String clusterIndex, String pointIndex) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
				
		List<DoublePoint> l = clusters.get(Integer.parseInt(clusterIndex)).getPoints();
		
		l.remove(Integer.parseInt(pointIndex));

	}

	public static void addPoint(EnvManager env, String clusterId, String clusterIndex, String x, String y) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
				
		List<DoublePoint> l = clusters.get(Integer.parseInt(clusterIndex)).getPoints();
		
		l.add(new DoublePoint(new double[] { Double.parseDouble(x), Double.parseDouble(y) }));

	}

	public static void updatePoint(EnvManager env, String clusterId, String clusterIndex, String pointIndex, String x, String y) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
				
		List<DoublePoint> l = clusters.get(Integer.parseInt(clusterIndex)).getPoints();
		
		l.set(Integer.parseInt(pointIndex), new DoublePoint(new double[] { Double.parseDouble(x), Double.parseDouble(y) }));

	}
	
	public static String getDistanceMeasure(EnvManager env, String clusterId, String x1, String y1, String x2, String y2) throws Exception {

		//Generate an error if the cluster id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		return ""+env.dbcluster.get(clusterId).getDistanceMeasure().compute(
				new double[] { Double.parseDouble(x1), Double.parseDouble(y1) }, 
				new double[] { Double.parseDouble(x2), Double.parseDouble(y2) }
			);

	}

	public static String close(EnvManager env, String clusterId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the regression id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}

		env.clusters.remove(clusterId);
        env.dbcluster.remove(clusterId);
        env.clusterOriginPoints.remove(clusterId);

		return result;

	}

	public static String closeall(EnvManager env) throws Exception {

		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to close
		for (Entry<String, List<Cluster<DoublePoint>>> e : env.clusters.entrySet()) {

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
	public static JSONObject xy_scatter(EnvManager env, String clusterId) throws Exception {
		
		//Generate an error if the regression id does not exist
		if (existClusters(env, clusterId).equals("0")) {

			throw new Exception("Sorry, the cluster id '"+clusterId+"' does not exist.");

		}
		
		JSONObject table = new JSONObject();
		table.put("title", clusterId);
		JSONArray data = new JSONArray();
		table.put("data", data);
		
		List<Cluster<DoublePoint>> clusters = env.clusters.get(clusterId);
		for(Cluster<DoublePoint> c : clusters) {
			
			JSONArray serie = new JSONArray();
			
			for(int j = 0;j<c.getPoints().size();j++) {
				
				JSONArray p = new JSONArray();
				
				double[] d = c.getPoints().get(j).getPoint();

				p.add(d[0]);
				p.add(d[1]);
				
				serie.add(p);
				
			}
			
			data.add(serie);
			
		}
		
		return table;
		
	}
	
	public static void heuristicNode_load(EnvManager env, String hid, String isDirect, String json) throws Exception {
		
		//Generate an error if the heuristic id already exist
		if (existHeuristicNode(env, hid).equals("1")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' already exist.");

		}
		
		GraphBuilder<String,Double> graph_builder = GraphBuilder.<String,Double>create();
		
		JSONArray data = (JSONArray) JsonManager.load(json);
		
		for(int i=0;i<data.size();i++) {
			
			JSONArray row = (JSONArray) data.get(i);

			String n1 = (String) row.get(0);
			String n2 = (String) row.get(1);
			double d = Double.parseDouble(""+row.get(2));
			
			graph_builder.connect(n1).to(n2).withEdge(d);
			
		}
		
		if (isDirect!=null && isDirect.equals("1")) {
			
			env.heuristicNode.put(hid, graph_builder.createDirectedGraph());
			env.heuristicNodeSearch.put(hid, new HashMap<String , SearchProblem<Double, String, WeightedNode<Double, String, Double>>>());
			
		} else {
			
			env.heuristicNode.put(hid, graph_builder.createUndirectedGraph());
			env.heuristicNodeSearch.put(hid, new HashMap<String , SearchProblem<Double, String, WeightedNode<Double, String, Double>>>());
			
		}
		
	}
	
	public static void heuristicNode_add_problem(EnvManager env, String hid, String searchId, String from) throws Exception {//Generate an error if the heuristic id already exist
		
		//Generate an error if the heuristic id does not exist
		if (existHeuristicNode(env, hid).equals("0")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' does not exist.");

		}
		
		//Generate an error if the search problem id already exist
		if (existProblem(env, hid, searchId).equals("1")) {

			throw new Exception("Sorry, the problem id '"+searchId+"' already exist.");

		}
		
		SearchProblem<Double, String, WeightedNode<Double, String, Double>> p = GraphSearchProblem
                .startingFrom(from)
                .in(env.heuristicNode.get(hid))
                .takeCostsFromEdges()
                .build();
		
		env.heuristicNodeSearch.get(hid).put(searchId, p);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject heuristicNode_compute(EnvManager env, String hid, String searchId, String algorithm, String to, String param) throws Exception {
		
		//Generate an error if the heuristic id does not exist
		if (existHeuristicNode(env, hid).equals("0")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' does not exist.");

		}
		
		//Generate an error if the search problem id does not exist
		if (existProblem(env, hid, searchId).equals("0")) {

			throw new Exception("Sorry, the problem id '"+searchId+"' does not exist.");

		}
		
		if (algorithm==null) {
			
			algorithm = "";
			
		}
		
		algorithm = algorithm.toLowerCase();
		
		@SuppressWarnings("rawtypes")
		SearchResult result = null;
		
		switch (algorithm) {
		case "dijkstra": 
			
			result = Hipster.createDijkstra(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "a_star": 
			
			result = Hipster.createAStar(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "bellman_ford": 
			
			result = Hipster.createBellmanFord(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "breadth_first_search": 
			
			result = Hipster.createBreadthFirstSearch(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "depth_first_search": 
			
			result = Hipster.createDepthFirstSearch(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "i_d_a_star": 
			
			result = Hipster.createIDAStar(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
		
		case "multi_objective_l_s": 
			
			result = Hipster.createMultiobjectiveLS(env.heuristicNodeSearch.get(hid).get(searchId)).search(to);
			
			break;
			
		case "depth_limited_search": 
			
			try {
				
				Integer.parseInt(param);
				
			} catch (Exception e) {
				
				throw new Exception("Sorry, the param must be an integer.");
				
			}
			
			result = Hipster.createDepthLimitedSearch(env.heuristicNodeSearch.get(hid).get(searchId), Integer.parseInt(param)).search(to);
			
			break;
			
		case "hill_climbing": 
			
			if (param==null || (!param.equals("1") && !param.equals("0"))) {
				
				throw new Exception("Sorry, the param must be a boolean (true|false).");
				
			}
			
			if (param!=null && param.equals("1")) {
				
				result = Hipster.createHillClimbing(env.heuristicNodeSearch.get(hid).get(searchId), true).search(to);
				
			} else {
				
				result = Hipster.createHillClimbing(env.heuristicNodeSearch.get(hid).get(searchId), false).search(to);
				
			}
			
			break;
		
		default:
			throw new Exception("Sorry, the algorithm '"+algorithm+"' does not exist (dijkstra|a_star|bellman_ford|breadth_first_search|depth_first_search|depth_limited_search|i_d_a_star|multi_objective_l_s|hill_climbing).");
		}
		
		JSONObject r = new JSONObject();
		r.put("elapsed", result.getElapsed());
		
		@SuppressWarnings("rawtypes")
		WeightedNode node = (WeightedNode) result.getGoalNode();
		
		r.put("state", node.state());
		r.put("cost", node.getCost());
		r.put("estimation", node.getEstimation());
		r.put("score", node.getScore());
		JSONArray op = new JSONArray();
		r.put("optimalPaths", op);

		for(Object o : result.getOptimalPaths()) {

			List<String> ls = (List<String>) o;
			JSONArray row = new JSONArray();
			
			for(String oo : ls) {
				
				row.add(oo);
				
			}
			
			op.add(row);
			
		}

		return r;
		
	}

	public static String existHeuristicNode(EnvManager env, String hid) throws Exception {

		if (env.heuristicNode.containsKey(hid)) {

			return "1";

		} else {

			return "0";

		}

	}

	public static String existProblem(EnvManager env, String hid, String searchId) throws Exception {

		if (env.heuristicNodeSearch.containsKey(hid)) {

			if (env.heuristicNodeSearch.get(hid).containsKey(searchId)) {

				return "1";

			} else {

				return "0";

			}

		} else {

			return "0";

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray showHeuristicNode(EnvManager env) throws Exception {

		JSONArray result = new JSONArray();

		for (Object entry : env.heuristicNode.keySet()) {
			result.add(entry.toString());
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public static JSONArray showHeuristicProblem(EnvManager env, String hid) throws Exception {

		JSONArray result = new JSONArray();

		//Generate an error if the heuristic id does not exist
		if (existHeuristicNode(env, hid).equals("0")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' does not exist.");

		}

		for (Object entry : env.heuristicNodeSearch.get(hid).keySet()) {
			result.add(entry.toString());
		}

		return result;

	}

	public static String closeHeuristicProblem(EnvManager env, String hid, String searchId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the heuristic id does not exist
		if (existHeuristicNode(env, hid).equals("0")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' does not exist.");

		}
		
		//Generate an error if the search problem id does not exist
		if (existProblem(env, hid, searchId).equals("0")) {

			throw new Exception("Sorry, the problem id '"+searchId+"' does not exist.");

		}

		env.heuristicNodeSearch.get(hid).remove(searchId);

		return result;

	}

	public static String closeHeuristicNode(EnvManager env, String hid) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the heuristic id does not exist
		if (existHeuristicNode(env, hid).equals("0")) {

			throw new Exception("Sorry, the heuristic id '"+hid+"' does not exist.");

		}

		env.heuristicNode.remove(hid);
		env.heuristicNodeSearch.remove(hid);

		return result;

	}

	public static String closeallHeuristicNode(EnvManager env) throws Exception {

		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to close
		for (Entry<String, HipsterGraph<String,Double>> e : env.heuristicNode.entrySet()) {

			allKeysToDelete.add(e.getKey());

		}

		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {

			try {

				//Close the document
				closeHeuristicNode(env, allKeysToDelete.get(i));
				nbClosed++;

			} catch (Exception e) {

				//Nothing to do

			}

		}

		return ""+nbClosed;

	}

}