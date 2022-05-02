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

package re.jpayet.mentdb.ext.cluster;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.remote.TunnelManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.user.GroupManager;

public class ClusterManager {
	
	public static JSONObject allsignals_obj = null;
	public static JSONObject allclusters_array = null;
	public static JSONObject allclusters_obj = null;
	
	//Create the cluster object
	public static void init() throws Exception {
		
		Record2.add("record", "CLUSTER[]", (new JSONObject()).toJSONString());
		Record2.add("record", "SIGNAL[]", (new JSONObject()).toJSONString());

		allclusters_obj = (JSONObject) JsonManager.load(Record2.getNode("CLUSTER[]").toJSONString());
		allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
		
		allsignals_obj = Record2.getNode("SIGNAL[]");
		
	}
	
	//Load the cluster object
	public static void load() throws Exception {

		allclusters_obj = (JSONObject) JsonManager.load(Record2.getNode("CLUSTER[]").toJSONString());
		allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
		
		allsignals_obj = Record2.getNode("SIGNAL[]");
		
	}
	
	//Add a new cluster
	@SuppressWarnings("unchecked")
	public static void add(long sessionId, String cluster_id) throws Exception {
		
		synchronized ("CLUSTER[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			if (allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" already exist.");
				
			}
			
			allclusters_obj.put(cluster_id, new JSONObject());
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject transform_cluster_obj_to_array(JSONObject cluster) throws Exception {
		
		JSONObject result = new JSONObject();
		
		for(Object o : cluster.keySet()) {
			
			String cluster_id = (String) o;
			
			JSONArray nodes = new JSONArray();
			
			JSONObject cluster_nodes = (JSONObject) cluster.get(cluster_id);
			for(Object z : cluster_nodes.keySet()) {
			
				String node_id = (String) z;
				
				nodes.add((JSONObject) cluster_nodes.get(node_id));
			
			}
				
			result.put(cluster_id, nodes);
			
		}
		
		return result;
		
	}
	
	
	public static void remove(long sessionId, String cluster_id) throws Exception {
		
		synchronized ("CLUSTER[]") {

			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			if (!allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
				
			}
	
			allclusters_obj.remove(cluster_id);
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	public static void remove_all(long sessionId) throws Exception {
		
		synchronized ("CLUSTER[]") {

			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			allclusters_obj = new JSONObject();
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	public static boolean exist(String cluster_id) throws Exception {

		return allclusters_obj.containsKey(cluster_id);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show_ids(long sessionId) throws Exception {
		
		JSONArray result = new JSONArray();
		
		for(Object o : allclusters_obj.keySet()) {
			
			String cluster_id = (String) o;
			result.add(cluster_id);
			
		}
		
		return result;
		
	}
	
	static HashMap<String, Integer> current_pos_lb_50_50 = new HashMap<String, Integer>();
	
	@SuppressWarnings("unchecked")
	public synchronized static JSONObject get_connection_from_cluster_lb_50_50(String cluster_id) throws Exception {
		
		JSONArray nodes = (JSONArray) allclusters_array.get(cluster_id);
		
		if (!current_pos_lb_50_50.containsKey(cluster_id)) {
			current_pos_lb_50_50.put(cluster_id, -1);
		}

		JSONObject n = null;
		int index_to_find = 0;
		do {
			
			int current_pos = current_pos_lb_50_50.get(cluster_id)+1;
			if (current_pos==nodes.size()) {
				current_pos = 0;
			}
			current_pos_lb_50_50.put(cluster_id, current_pos);
			
			n = (JSONObject) nodes.get(current_pos);
			
			index_to_find++;
        } while (index_to_find <= nodes.size() && ((String) n.get("in_the_cluster")).equals("0"));
		
		if (((String) n.get("in_the_cluster")).equals("0")) {
			
			throw new Exception("Sorry, all nodes from the cluster '"+cluster_id+"' are evicted.");
			
		}
		
		JSONObject cnt = new JSONObject();
		cnt.put("subTunnels", "[MQL_TO_REPLACE]");
		cnt.put("hostname", n.get("hostname"));
		cnt.put("port", n.get("port"));
		cnt.put("user", n.get("user"));
		cnt.put("key", n.get("user_key"));
		cnt.put("password", n.get("password"));
		cnt.put("connectTimeout", n.get("connectTimeout"));
		cnt.put("readTimeout", n.get("readTimeout"));
		cnt.put("type", "mentdb");
		cnt.put("node_id", n.get("node_id"));

		n = (JSONObject) ((JSONObject) allclusters_obj.get(cluster_id)).get(""+n.get("node_id"));
		
		long nb_cmd = Long.parseLong(n.get("nb_cmd")+"");
		nb_cmd++;
		
		n.put("nb_cmd", nb_cmd);
		
		return cnt;
		
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static JSONObject get_connection_from_cluster_signal(long sessionId, String cluster_id) throws Exception {
		
		JSONArray nodes = (JSONArray) allclusters_array.get(cluster_id);
		
		JSONObject proba = new JSONObject();
		
		JSONObject n = null;
		for(int i=0;i<nodes.size();i++) {
			
			n = (JSONObject) nodes.get(i);
			
			if (((String) n.get("in_the_cluster")).equals("1")) {

				long node_l = Long.parseLong(DateFx.ts_to_long(""+n.get("signal_last_time")));
				long now_l = Long.parseLong(DateFx.ts_to_long(DateFx.systimestamp()));
				
				if (now_l>node_l+Start.CLUSTER_LIFE_BEFORE_EXPULSION) {
					
					ClusterManager.node_expels(cluster_id, ""+n.get("node_id"), "Sorry, the node '"+n.get("node_id")+"' took too long to give sign of life.");
					
				} else {
					
					proba.put(n.get("node_id"), Double.parseDouble(n.get("signal")+""));
					
				}
				
			}
			
		}
		
		String seleted_node = "";

		List<Map.Entry<String, Double>> list = new LinkedList<>( proba.entrySet() );
		if (list.size()==0) {
			
			throw new Exception("Sorry, all nodes from the cluster '"+cluster_id+"' are evicted.");
			
		} else if (list.size()==1) {
			
			seleted_node = list.get(0).getKey();
			
		} else {
			
			Collections.sort( list, new Comparator<Map.Entry<String, Double>>() {
				@Override
				public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
				{
					return ( o1.getValue() ).compareTo( o2.getValue() );
				}
			} );
			
			seleted_node = list.get(0).getKey();
			
		}

		n = (JSONObject) ((JSONObject) allclusters_obj.get(cluster_id)).get(seleted_node);
		
		JSONObject cnt = new JSONObject();
		cnt.put("subTunnels", "[MQL_TO_REPLACE]");
		cnt.put("hostname", n.get("hostname"));
		cnt.put("port", n.get("port"));
		cnt.put("user", n.get("user"));
		cnt.put("key", n.get("user_key"));
		cnt.put("password", n.get("password"));
		cnt.put("connectTimeout", n.get("connectTimeout"));
		cnt.put("readTimeout", n.get("readTimeout"));
		cnt.put("type", "mentdb");
		cnt.put("node_id", n.get("node_id"));
		
		long nb_cmd = Long.parseLong(n.get("nb_cmd")+"");
		nb_cmd++;
		
		n.put("nb_cmd", nb_cmd);
		
		return cnt;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject get_node(long sessionId, String cluster_id, String method) throws Exception {
		
		if (method!=null) {
			method = method.toLowerCase();
		}
		
		if (method==null || (!method.equals("lb_50_50") && !method.equals("signal"))) {
			
			throw new Exception("Sorry, the method "+method+" is not valid (must be LB_50_50|SIGNAL).");
			
		}
		
		if (!exist(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id '"+cluster_id+"' does not exist.");
			
		}
		
		if (method.equals("lb_50_50")) {
			
			JSONObject cnt = get_connection_from_cluster_lb_50_50(cluster_id);

			cnt.put("cluster_id", cluster_id);
			cnt.put("cluster_method", method);
			
			return cnt;
			
		} else {
			
			JSONObject cnt = get_connection_from_cluster_signal(sessionId, cluster_id);

			cnt.put("cluster_id", cluster_id);
			cnt.put("cluster_method", method);
			
			return cnt;
			
		}
		
	}
	
	//Set a new node into a cluster
	@SuppressWarnings("unchecked")
	public static void node_set(long sessionId, String cluster_id, String node_id, String hostname, String port, String user, String user_key, String password, String connectTimeout, String readTimeout, String active_signal) throws Exception {
		
		synchronized ("CLUSTER[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			try {
				Integer.parseInt(port);
			} catch (Exception e) {
				throw new Exception("Sorry, the port "+port+" is not valid (must be a number).");
			}
			
			try {
				Integer.parseInt(connectTimeout);
			} catch (Exception e) {
				throw new Exception("Sorry, the connectTimeout "+connectTimeout+" is not valid (must be a number).");
			}
			
			try {
				Integer.parseInt(readTimeout);
			} catch (Exception e) {
				throw new Exception("Sorry, the readTimeout "+readTimeout+" is not valid (must be a number).");
			}
			
			if (active_signal==null || (!active_signal.equals("0") && !active_signal.equals("1"))) {
				
				throw new Exception("Sorry, the active_signal "+active_signal+" is not valid (must be a boolean).");
				
			}
			
			if (!allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
				
			}
			
			JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
			
			JSONObject node = null;
			
			if (current_cluster.containsKey(node_id)) {
				node = (JSONObject) current_cluster.get(node_id);
			} else {
				node = new JSONObject();
				node.put("in_the_cluster", "1");
				node.put("error", "");
				node.put("signal_last_time", DateFx.systimestamp());
				node.put("signal", "0");
				node.put("nb_cmd", "0");
			}
	
			node.put("cluster_id", cluster_id);
			node.put("node_id", node_id);
			node.put("hostname", hostname);
			node.put("port", port);
			node.put("user", user);
			node.put("user_key", user_key);
			node.put("password", password);
			node.put("connectTimeout", connectTimeout);
			node.put("readTimeout", readTimeout);
			node.put("active_signal", active_signal);
			
			current_cluster.put(node_id, node);
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	//Remove a node from a cluster
	public static void node_remove(long sessionId, String cluster_id, String node_id) throws Exception {
		
		synchronized ("CLUSTER[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			if (!allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
				
			}
			
			JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
			
			if (!current_cluster.containsKey(node_id)) {
				
				throw new Exception("Sorry, the node id "+node_id+" does not exist.");
				
			}
			
			current_cluster.remove(node_id);
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	//Show all nodes from a cluster
	public static String node_generate_update(long sessionId, String cluster_id) throws Exception {
		
		String result = "";
		
		if (!allclusters_obj.containsKey(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
			
		}
		
		JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
		
		for(Object o : current_cluster.keySet()) {
			
			String node_id = (String) o;
			JSONObject node = (JSONObject) current_cluster.get(node_id);
			
			result += "cluster node set \""+cluster_id+"\" \""+node_id+"\" \""+node.get("hostname")+"\" \""+node.get("port")+"\" \""+node.get("user")+"\" \""+node.get("user_key")+"\" \""+node.get("password")+"\" "+node.get("connectTimeout")+" "+node.get("readTimeout")+" "+((node.get("active_signal")+"").equals("1")?"true":"false")+";\n";
			
		}
		
		return result;
		
	}
	
	//Show all nodes from a cluster
	public static String node_show_text(long sessionId, String cluster_id) throws Exception {
		
		String result = "IN_THE_CLUSTER / ACTIVE_SIGNAL / CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / SIGNAL / LAST_TIME / NB_CMD : ERROR\n";
		
		if (!allclusters_obj.containsKey(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
			
		}
		
		JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
		
		for(Object o : current_cluster.keySet()) {
			
			String node_id = (String) o;
			JSONObject node = (JSONObject) current_cluster.get(node_id);
			
			result+= node.get("in_the_cluster")+" / "+node.get("active_signal")+" / "+node.get("cluster_id")+" / "+node.get("node_id")+" / "+node.get("user")+"@"+node.get("hostname")+":"+node.get("port")+" / "+node.get("connectTimeout")+" / "+node.get("readTimeout")+" / "+node.get("signal")+" / "+node.get("signal_last_time")+" / "+node.get("nb_cmd")+" : "+node.get("error")+"\n";
			
		}
		
		return result;
		
	}
	
	//Show all nodes from a cluster
	public static JSONObject node_show_obj(long sessionId, String cluster_id) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
		
		if (!allclusters_obj.containsKey(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
			
		}
		
		JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
		
		return current_cluster;
		
	}
	
	//Expels a node from a cluster
	@SuppressWarnings("unchecked")
	public static void node_expels(String cluster_id, String node_id, String error) throws Exception {
		
		synchronized ("CLUSTER[]") {
		
			if (!allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
				
			}
			
			JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
			
			if (!current_cluster.containsKey(node_id)) {
				
				throw new Exception("Sorry, the node id "+node_id+" does not exist.");
				
			}
			
			JSONObject node = (JSONObject) current_cluster.get(node_id);
	
			node.put("in_the_cluster", "0");
			node.put("error", error.replace("\n", " ").replace("\r", " "));
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	//Reinstall a node from a cluster
	@SuppressWarnings("unchecked")
	public static void node_reinstate(long sessionId, String cluster_id, String node_id) throws Exception {
		
		synchronized ("CLUSTER[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			if (!allclusters_obj.containsKey(cluster_id)) {
				
				throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
				
			}
			
			JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
			
			if (!current_cluster.containsKey(node_id)) {
				
				throw new Exception("Sorry, the node id "+node_id+" does not exist.");
				
			}
			
			JSONObject node = (JSONObject) current_cluster.get(node_id);
	
			node.put("nb_cmd", "0");
			node.put("in_the_cluster", "1");
			node.put("error", "");
			
			Record2.update("CLUSTER[]", allclusters_obj.toJSONString());
			
			allclusters_obj = (JSONObject) JsonManager.load(allclusters_obj.toJSONString());
			allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
			
		}
		
	}
	
	//Deploy all signals to all nodes
	@SuppressWarnings("unchecked")
	public static String signals_deploy(EnvManager env, long sessionId, String cluster_id, String hostname, String port, String user, String user_key, String password, String connectTimeout, String readTimeout, String mql_signal) throws Exception {
		
		String result = "";
		
		if (!allclusters_obj.containsKey(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
			
		}
		
		JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
		
		for(Object o : current_cluster.keySet()) {
			
			String node_id = (String) o;
			JSONObject n = (JSONObject) current_cluster.get(node_id);
			
			JSONObject cnt = new JSONObject();
			cnt.put("subTunnels", "[MQL_TO_REPLACE]");
			cnt.put("hostname", n.get("hostname"));
			cnt.put("port", n.get("port"));
			cnt.put("user", n.get("user"));
			cnt.put("key", n.get("user_key"));
			cnt.put("password", n.get("password"));
			cnt.put("connectTimeout", n.get("connectTimeout"));
			cnt.put("readTimeout", n.get("readTimeout"));
			cnt.put("type", "mentdb");
			cnt.put("node_id", n.get("node_id"));
			
			try {

				TunnelManager.connect(env, "cluster_signal_deployment_tunnel_id", cnt.toJSONString());
				
				if ((n.get("active_signal")+"").equals("1")) {
					TunnelManager.execute(env, "cluster_signal_deployment_tunnel_id", "cluster signal set \""+cluster_id.replace("\"", "\\\"")+"\" \""+(((String) n.get("node_id")).replace("\"", "\\\""))+"\" \""+(hostname.replace("\"", "\\\""))+"\" \""+(port.replace("\"", "\\\""))+"\" \""+(user.replace("\"", "\\\""))+"\" \""+(user_key.replace("\"", "\\\""))+"\" \""+(password.replace("\"", "\\\""))+"\" "+(connectTimeout.replace("\"", "\\\""))+" "+(readTimeout.replace("\"", "\\\""))+" \""+(mql_signal.replace("\"", "\\\""))+"\";");
				} else {
					TunnelManager.execute(env, "cluster_signal_deployment_tunnel_id", "cluster signal delete \""+cluster_id.replace("\"", "\\\"")+"\" \""+(((String) n.get("node_id")).replace("\"", "\\\""))+"\";");
				}
				
				TunnelManager.close(env, "cluster_signal_deployment_tunnel_id");
				
				if ((n.get("active_signal")+"").equals("1")) {
					result+= "ok/set/"+cluster_id+"/"+n.get("node_id")+"\n";
				} else {
					result+= "ok/remove/"+cluster_id+"/"+n.get("node_id")+"\n";
				}
						
			} catch (Exception e) {
				
				String err = ""+e.getMessage();
				if ((n.get("active_signal")+"").equals("1")) {
					result+= "KO/set/"+cluster_id+"/"+n.get("node_id")+"/"+err.replace("\n", " ")+"\n";
				} else {
					result+= "KO/remove/"+cluster_id+"/"+n.get("node_id")+"/"+err.replace("\n", " ")+"\n";
				}
				
				try {TunnelManager.close(env, "cluster_signal_deployment_tunnel_id");} catch (Exception f) {};
				
			}
			
		}
		
		return result;
		
	}
	
	//Show all remote signals to all nodes
	@SuppressWarnings("unchecked")
	public static String signals_remote_show(EnvManager env, long sessionId, String cluster_id) throws Exception {
		
		String result = "";
		
		if (!allclusters_obj.containsKey(cluster_id)) {
			
			throw new Exception("Sorry, the cluster id "+cluster_id+" does not exist.");
			
		}
		
		JSONObject current_cluster = (JSONObject) allclusters_obj.get(cluster_id);
		
		for(Object o : current_cluster.keySet()) {
			
			String node_id = (String) o;
			JSONObject n = (JSONObject) current_cluster.get(node_id);
			
			JSONObject cnt = new JSONObject();
			cnt.put("subTunnels", "[MQL_TO_REPLACE]");
			cnt.put("hostname", n.get("hostname"));
			cnt.put("port", n.get("port"));
			cnt.put("user", n.get("user"));
			cnt.put("key", n.get("user_key"));
			cnt.put("password", n.get("password"));
			cnt.put("connectTimeout", n.get("connectTimeout"));
			cnt.put("readTimeout", n.get("readTimeout"));
			cnt.put("type", "mentdb");
			cnt.put("node_id", n.get("node_id"));
			
			try {

				TunnelManager.connect(env, "cluster_signal_deployment_tunnel_id", cnt.toJSONString());
				
				result+= "ok/"+TunnelManager.execute(env, "cluster_signal_deployment_tunnel_id", "cluster signal show \""+cluster_id.replace("\"", "\\\"")+"\";");
				
				TunnelManager.close(env, "cluster_signal_deployment_tunnel_id");
				
			} catch (Exception e) {
				
				String err = ""+e.getMessage();
				result+= "KO/"+cluster_id+"/"+n.get("node_id")+"/"+err.replace("\n", " ")+"\n";
				
				try {TunnelManager.close(env, "cluster_signal_deployment_tunnel_id");} catch (Exception f) {};
				
			}
			
		}
		
		return result;
		
	}
	
	//Show all remote signals to all nodes
	@SuppressWarnings("unchecked")
	public static String signal_give(String cluster_id, String node_id, String signal, String current_time) throws Exception {
		
		
		if (allclusters_obj.containsKey(cluster_id)) {
			
			JSONObject cluster = (JSONObject) allclusters_obj.get(cluster_id);
			
			if (cluster.containsKey(node_id)) {
				
				JSONObject node = (JSONObject) cluster.get(node_id);
				
				node.put("signal_last_time", current_time);
				node.put("signal", signal);
				
				allclusters_array = transform_cluster_obj_to_array(allclusters_obj);
				
				return "1";
				
			} else {
				
				throw new Exception("Sorry, the node id '"+node_id+"' does not exist.");
				
			}
			
		} else {
			
			throw new Exception("Sorry, the cluster id '"+cluster_id+"' does not exist.");
			
		}
		
	}

	public static AtomicBoolean lock = new AtomicBoolean(false);
	
	@SuppressWarnings("unchecked")
	public static void process_signal() {
	    
		try {
			
			if (!lock.compareAndSet(false, true)) {
				
				return;
	
			}
			
			//Get time
			String current_time = DateFx.systimestamp();
			
			EnvManager env = new EnvManager();
			
			//Parse all clusters
			for(Object c : allsignals_obj.keySet()) {
				
				String cluster_id = (String) c;
				JSONObject signals = (JSONObject) allsignals_obj.get(cluster_id);
				
				//Parse all signals
				for(Object s : signals.keySet()) {
					
					String node_id = (String) s;
					JSONObject n = (JSONObject) signals.get(node_id);
					
    				//Get signal
    				String sig = ""+Database.execute_admin_mql(null, (String) n.get("mql_signal"));
    				
					JSONObject cnt = new JSONObject();
					cnt.put("subTunnels", "[MQL_TO_REPLACE]");
					cnt.put("hostname", n.get("hostname"));
					cnt.put("port", n.get("port"));
					cnt.put("user", n.get("user"));
					cnt.put("key", n.get("user_key"));
					cnt.put("password", n.get("password"));
					cnt.put("connectTimeout", n.get("connectTimeout"));
					cnt.put("readTimeout", n.get("readTimeout"));
					cnt.put("type", "mentdb");
					cnt.put("node_id", n.get("node_id"));
					
					try {

						TunnelManager.connect(env, "cluster_signal_give_tunnel_id", cnt.toJSONString());
						
						TunnelManager.execute(env, "cluster_signal_give_tunnel_id", "cluster signal give \""+cluster_id.replace("\"", "\\\"")+"\" \""+node_id.replace("\"", "\\\"")+"\" "+sig+" \""+current_time+"\";");
						
						TunnelManager.close(env, "cluster_signal_give_tunnel_id");
						
						n.put("error", "");
						n.put("signal_last_time", current_time);
						
					} catch (Exception e) {
						
						try {TunnelManager.close(env, "cluster_signal_give_tunnel_id");} catch (Exception f) {};
						
						n.put("error", (""+e.getMessage()).replace("\n", " "));
						n.put("signal_last_time", current_time);
						
					}
					
				}
				
			}
				
		} catch (Exception e) {
		}
		
		lock.set(false);
		
	}
	
	//Set a new signal into a node
	@SuppressWarnings("unchecked")
	public static void signal_set(long sessionId, String cluster_id, String node_id, String hostname, String port, String user, String user_key, String password, String connectTimeout, String readTimeout, String mql_signal) throws Exception {
		
		synchronized ("SIGNAL[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			try {
				Integer.parseInt(port);
			} catch (Exception e) {
				throw new Exception("Sorry, the port "+port+" is not valid (must be a number).");
			}
			
			try {
				Integer.parseInt(connectTimeout);
			} catch (Exception e) {
				throw new Exception("Sorry, the connectTimeout "+connectTimeout+" is not valid (must be a number).");
			}
			
			try {
				Integer.parseInt(readTimeout);
			} catch (Exception e) {
				throw new Exception("Sorry, the readTimeout "+readTimeout+" is not valid (must be a number).");
			}
			
			if (!allsignals_obj.containsKey(cluster_id)) {
				
				allsignals_obj.put(cluster_id, new JSONObject());
				
			}
			
			JSONObject current_cluster = (JSONObject) allsignals_obj.get(cluster_id);
			
			JSONObject node = new JSONObject();
	
			node.put("cluster_id", cluster_id);
			node.put("node_id", node_id);
			node.put("hostname", hostname);
			node.put("port", port);
			node.put("user", user);
			node.put("user_key", user_key);
			node.put("password", password);
			node.put("connectTimeout", connectTimeout);
			node.put("readTimeout", readTimeout);
			node.put("error", "");
			node.put("signal_last_time", "");
			node.put("mql_signal", mql_signal);
			
			current_cluster.put(node_id, node);
			
			Record2.update("SIGNAL[]", allsignals_obj.toJSONString());
			
		}
		
	}
	
	//Set delete signal into a node
	public static void signal_delete(long sessionId, String cluster_id, String node_id) throws Exception {
		
		synchronized ("SIGNAL[]") {
		
			GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
			
			if (allsignals_obj.containsKey(cluster_id)) {
				
				JSONObject current_cluster = (JSONObject) allsignals_obj.get(cluster_id);
				
				current_cluster.remove(node_id);
				
				Record2.update("SIGNAL[]", allsignals_obj.toJSONString());
				
			}
			
		}
		
	}
	
	//Show signal into a node
	public static String signal_show(long sessionId, String cluster_id) throws Exception {
		
		GroupManager.generateErrorIfNotGranted(sessionId, "cluster", "Cluster");
		
		String result = "CLUSTER_ID / NODE_ID / USER@HOSTNAME:PORT / CONNECT_TIMEOUT / READ_TIMEOUT / MQL_SIGNAL / LAST_TIME : ERROR\n";
		
		if (allsignals_obj.containsKey(cluster_id)) {
			
			JSONObject current_cluster = (JSONObject) allsignals_obj.get(cluster_id);
			
			for(Object o : current_cluster.keySet()) {
				
				String node_id = (String) o;
				JSONObject node = (JSONObject) current_cluster.get(node_id);
				
				result+= node.get("cluster_id")+" / "+node.get("node_id")+" / "+node.get("user")+"@"+node.get("hostname")+":"+node.get("port")+" / "+node.get("connectTimeout")+" / "+node.get("readTimeout")+(" / "+node.get("mql_signal")).replace("\n", " ")+" / "+node.get("signal_last_time")+" : "+node.get("error")+"\n";
				
			}
			
			return result;
			
		} else return result;
		
	}
	
}