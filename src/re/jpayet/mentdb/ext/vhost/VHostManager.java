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

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.ext.app.AppManager;

public class VHostManager {
	
	//Create the parameter object
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {
		
		JSONObject vhosts = new JSONObject();
		vhosts.put("http", new JSONObject());
		vhosts.put("https", new JSONObject());
		
		Record2.add("record", "VHOST[]", vhosts.toJSONString());
		
	}
	
	public static JSONObject show(String protocol, String context) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}
		
		if (AppManager.exist_context(protocol, context).equals("0")) {
			
			throw new Exception("Sorry, the context '"+context+"' does not exist.");
			
		}
		
		JSONObject r = Record2.getNode("APP["+protocol+"_"+context+"]");
		if (r==null) return new JSONObject();
		else return (JSONObject) r.get("vhost");
		
	}
	
	public static JSONObject show_all(String protocol) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}
		
		return (JSONObject) Record2.getNode("VHOST[]").get(protocol);
		
	}
	
	public static boolean exist(String protocol, String hostname, String context) throws Exception {
		
		//Generate an error if the protocol is not http or https
		if (protocol==null || (!protocol.equals("http") && !protocol.equals("https"))) {

			throw new Exception("Sorry, the protocol must be 'http' or 'https'.");

		}

		if (AppManager.exist_context(protocol, context).equals("0")) {
			
			throw new Exception("Sorry, the context '"+context+"' does not exist.");
			
		}
		
		JSONObject obj1 = (JSONObject) Record2.getNode("VHOST[]").get(protocol);
		
		return obj1.containsKey(hostname);
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add(String protocol, String hostname, String context) throws Exception {
		
		synchronized ("VHOST[]") {
		
			if (exist(protocol, hostname, context)) {
				
				throw new Exception("Sorry, the virtual host '"+protocol+":"+hostname+"' already exist.");
				
			}
			
			//Get the node
			JSONObject rec = Record2.getNode("VHOST[]");
			JSONObject obj = (JSONObject) rec.get(protocol);
			obj.put(hostname, context);
			
			Record2.update("VHOST[]", rec.toJSONString());
			
		}
		
		synchronized ("APP["+protocol+"_"+context+"]") {
		
			JSONObject rec = Record2.getNode("APP["+protocol+"_"+context+"]");
			JSONObject obj = (JSONObject) rec.get("vhost");
			obj.put(hostname, context);
			
			Record2.update("APP["+protocol+"_"+context+"]", rec.toJSONString());
			
		}
		
	}
	
	public static void remove(String protocol, String hostname, String context) throws Exception {
		
		synchronized ("VHOST[]") {
		
			if (!exist(protocol, hostname, context)) {
				
				throw new Exception("Sorry, the virtual host '"+protocol+":"+hostname+"' does not exist.");
				
			}
			
			//Get the node
			JSONObject rec = Record2.getNode("VHOST[]");
			JSONObject obj = (JSONObject) rec.get(protocol);
			obj.remove(hostname);
			
			Record2.update("VHOST[]", rec.toJSONString());
			
		}
		
		synchronized ("APP["+protocol+"_"+context+"]") {
		
			JSONObject rec = Record2.getNode("APP["+protocol+"_"+context+"]");
			JSONObject obj = (JSONObject) rec.get("vhost");
			obj.remove(hostname);
			
			Record2.update("APP["+protocol+"_"+context+"]", rec.toJSONString());
			
		}
		
	}

}
