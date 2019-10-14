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

package re.jpayet.mentdb.ext.vhost;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.ext.app.AppManager;

public class VHostManager {
	
	//Create the parameter object
	@SuppressWarnings("unchecked")
	public static void init(long sessionId) throws Exception {
		
		JSONObject vhosts = new JSONObject();
		vhosts.put("http", new JSONObject());
		vhosts.put("https", new JSONObject());
		
		Record.add(sessionId, "VHOST[]", vhosts.toJSONString());
		
	}
	
	public static JSONObject show(long sessionId, String protocol, String context) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}
		
		if (AppManager.exist_context(protocol, context).equals("0")) {
			
			throw new Exception("Sorry, the context '"+context+"' does not exist.");
			
		}
		
		JSONObject r = Record.getNode(sessionId, "APP["+protocol+"_"+context+"]");
		if (r==null) return new JSONObject();
		else return (JSONObject) r.get("vhost");
		
	}
	
	public static JSONObject show_all(long sessionId, String protocol) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}
		
		return (JSONObject) Record.getNode(sessionId, "VHOST[]").get(protocol);
		
	}
	
	public static boolean exist(long sessionId, String protocol, String hostname, String context) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}

		if (AppManager.exist_context(protocol, context).equals("0")) {
			
			throw new Exception("Sorry, the context '"+context+"' does not exist.");
			
		}
		
		JSONObject obj1 = (JSONObject) Record.getNode(sessionId, "VHOST[]").get(protocol);
		
		return obj1.containsKey(hostname);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add(long sessionId, String protocol, String hostname, String context) throws Exception {
		
		if (exist(sessionId, protocol, hostname, context)) {
			
			throw new Exception("Sorry, the virtual host '"+protocol+":"+hostname+"' already exist.");
			
		}
		
		//Get the node
		JSONObject rec = Record.getNode(sessionId, "VHOST[]");
		JSONObject obj = (JSONObject) rec.get(protocol);
		obj.put(hostname, context);
		
		Record.update(sessionId, "VHOST[]", rec.toJSONString());
		
		rec = Record.getNode(sessionId, "APP["+protocol+"_"+context+"]");
		obj = (JSONObject) rec.get("vhost");
		obj.put(hostname, context);
		
		Record.update(sessionId, "APP["+protocol+"_"+context+"]", rec.toJSONString());
		
	}
	
	public static void remove(long sessionId, String protocol, String hostname, String context) throws Exception {
		
		if (!exist(sessionId, protocol, hostname, context)) {
			
			throw new Exception("Sorry, the virtual host '"+protocol+":"+hostname+"' does not exist.");
			
		}
		
		//Get the node
		JSONObject rec = Record.getNode(sessionId, "VHOST[]");
		JSONObject obj = (JSONObject) rec.get(protocol);
		obj.remove(hostname);
		
		Record.update(sessionId, "VHOST[]", rec.toJSONString());
		
		rec = Record.getNode(sessionId, "APP["+protocol+"_"+context+"]");
		obj = (JSONObject) rec.get("vhost");
		obj.remove(hostname);
		
		Record.update(sessionId, "APP["+protocol+"_"+context+"]", rec.toJSONString());
		
	}

}
