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

package re.jpayet.mentdb.ext.html;

import java.util.Vector;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class HTMLManager {
	
	public static void getElementById(EnvManager env, String domId, String jsonDocId, String id) throws Exception {
		
		//Generate an error if the domId is null or empty
		if (domId==null || domId.equals("")) {
			
			throw new Exception("Sorry, the domId cannot be null or empty.");
			
		}
		
		//Generate an error if the domId already exist
		if (exist(env, domId).equals("0")) {
			
			throw new Exception("Sorry, the domId '"+domId+"' does not exist.");
			
		}
		
		//Generate an error if the jsonDocId is null or empty
		if (jsonDocId==null || jsonDocId.equals("")) {
			
			throw new Exception("Sorry, the jsonDocId cannot be null or empty.");
			
		}
		
		//Generate an error if the id is null or empty
		if (id==null || id.equals("")) {
			
			throw new Exception("Sorry, the id cannot be null or empty.");
			
		}
		
		Document doc = env.doms.get(domId);
		
		Element e = doc.getElementById(id);
		
		//Save the document
		env.jsonObj.put(jsonDocId, buildElement(e));
		
	}
	
	public static void parse(EnvManager env, SessionThread session, String domId, String jsonDocId, String typeSearch, String key, String val, String mqlAction, String parent_pid, String current_pid) throws Exception {
		
		//Initialization
		domId = re.jpayet.mentdb.ext.statement.Statement.eval(session, domId, env, parent_pid, current_pid);
		typeSearch = re.jpayet.mentdb.ext.statement.Statement.eval(session, typeSearch, env, parent_pid, current_pid);
		key = re.jpayet.mentdb.ext.statement.Statement.eval(session, key, env, parent_pid, current_pid);
		val = re.jpayet.mentdb.ext.statement.Statement.eval(session, val, env, parent_pid, current_pid);
		jsonDocId = re.jpayet.mentdb.ext.statement.Statement.eval(session, jsonDocId, env, parent_pid, current_pid);
		
		//Generate an error if the domId is null or empty
		if (domId==null || domId.equals("")) {
			
			throw new Exception("Sorry, the domId cannot be null or empty.");
			
		}
		
		//Generate an error if the domId already exist
		if (exist(env, domId).equals("0")) {
			
			throw new Exception("Sorry, the domId '"+domId+"' does not exist.");
			
		}
		
		//Generate an error if the jsonDocId is null or empty
		if (jsonDocId==null || jsonDocId.equals("")) {
			
			throw new Exception("Sorry, the jsonDocId cannot be null or empty.");
			
		}
		
		//Generate an error if the typeSearch is null or empty
		if (typeSearch==null || typeSearch.equals("")) {
			
			throw new Exception("Sorry, the tagName cannot be null or empty.");
			
		}
		
		typeSearch = typeSearch.toUpperCase();
		
		//Generate an error if the typeSearch is not valid
		if (!typeSearch.equals("TAG")
				 && !typeSearch.equals("ATTRIBUTE")
				 && !typeSearch.equals("SELECT")
				 && !typeSearch.equals("CLASS")
				 && !typeSearch.equals("ATTRIBUTE_VALUE")
				 && !typeSearch.equals("CONTAINING_OWN_TEXT")
				 && !typeSearch.equals("CONTAINING_TEXT")
				 && !typeSearch.equals("OWN_TEXT_REGEX")
				 && !typeSearch.equals("TEXT_REGEX")
				 && !typeSearch.equals("ATTRIBUTE_STARTING")
				 && !typeSearch.equals("ATTRIBUTE_VALUE_CONTAINING")
				 && !typeSearch.equals("ATTRIBUTE_VALUE_ENDING")
				 && !typeSearch.equals("ATTRIBUTE_VALUE_REGEX")
				 && !typeSearch.equals("ATTRIBUTE_VALUE_NOT")
				 && !typeSearch.equals("ATTRIBUTE_VALUE_STARTING")) {
			
			throw new Exception("Sorry, the typeSearch is not valid (TAG | ATTRIBUTE | SELECT | CLASS | ATTRIBUTE_VALUE | CONTAINING_OWN_TEXT | CONTAINING_TEXT | OWN_TEXT_REGEX | TEXT_REGEX | ATTRIBUTE_STARTING | ATTRIBUTE_VALUE_CONTAINING | ATTRIBUTE_VALUE_ENDING | ATTRIBUTE_VALUE_REGEX | ATTRIBUTE_VALUE_NOT | ATTRIBUTE_VALUE_STARTING).");
			
		}
		
		Document doc = env.doms.get(domId);
		
		Elements es = null;
		
		switch (typeSearch) {
		case "TAG":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (tag) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByTag(key);
			
			break;
		case "ATTRIBUTE":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttribute(key);
			
			break;
		case "ATTRIBUTE_STARTING":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute prefix) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttributeStarting(key);
			
			break;
		case "ATTRIBUTE_VALUE":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null
			if (val==null) {
				
				throw new Exception("Sorry, the value cannot be null.");
				
			}
			
			es = doc.getElementsByAttributeValue(key, val);
			
			break;
		case "ATTRIBUTE_VALUE_CONTAINING":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null or empty
			if (val==null || val.equals("")) {
				
				throw new Exception("Sorry, the value (match) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttributeValueContaining(key, val);
			
			break;
		case "ATTRIBUTE_VALUE_ENDING":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null or empty
			if (val==null || val.equals("")) {
				
				throw new Exception("Sorry, the value (valueSuffix) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttributeValueEnding(key, val);
			
			break;
		case "ATTRIBUTE_VALUE_REGEX":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null or empty
			if (val==null || val.equals("")) {
				
				throw new Exception("Sorry, the value (pattern) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttributeValueMatching(key, val);
			
			break;
		case "ATTRIBUTE_VALUE_NOT":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null
			if (val==null) {
				
				throw new Exception("Sorry, the value cannot be null.");
				
			}
			
			es = doc.getElementsByAttributeValueNot(key, val);
			
			break;
		case "ATTRIBUTE_VALUE_STARTING":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (attribute) cannot be null or empty.");
				
			}
			
			//Generate an error if the value is null or empty
			if (val==null || val.equals("")) {
				
				throw new Exception("Sorry, the value (valuePrefix) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByAttributeValueStarting(key, val);
			
			break;
		case "CLASS":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (className) cannot be null or empty.");
				
			}
			
			es = doc.getElementsByClass(key);
			
			break;
		case "CONTAINING_OWN_TEXT":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (searchText) cannot be null or empty.");
				
			}
			
			es = doc.getElementsContainingOwnText(key);
			
			break;
		case "CONTAINING_TEXT":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (searchText) cannot be null or empty.");
				
			}
			
			es = doc.getElementsContainingText(key);
			
			break;
		case "OWN_TEXT_REGEX":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (pattern) cannot be null or empty.");
				
			}
			
			es = doc.getElementsMatchingOwnText(key);
			
			break;
		case "TEXT_REGEX":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (pattern) cannot be null or empty.");
				
			}
			
			es = doc.getElementsMatchingText(key);
			
			break;
		case "SELECT":
			
			//Generate an error if the key is null or empty
			if (key==null || key.equals("")) {
				
				throw new Exception("Sorry, the key (cssQuery) cannot be null or empty.");
				
			}
			
			es = doc.select(key);
			
			break;
		
		}

		Vector<Vector<MQLValue>> repeat_action = Misc.splitCommand(mqlAction);
		
		try {
		
			for (Element e : es) {
				
				//Save the document
				env.jsonObj.put(jsonDocId, buildElement(e));
				
				try {
				
					//Execute action
					re.jpayet.mentdb.ext.statement.Statement.eval(session, MQLValue.deepCopyValue(repeat_action), env, parent_pid, current_pid);
					
				} catch (Exception ee) {
					if (ee.getMessage()==null || !ee.getMessage().endsWith("j28hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
						throw ee;
					}
				};
				
			}
			
		}  catch (Exception m) {
			if (m.getMessage()==null || !m.getMessage().endsWith("j18hki95orm34hrm62vni93tkmr33sdy9hj24p0op7aHvcxy785se566at")) {
				throw m;
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildElement(Element e) {
		
		JSONObject result = new JSONObject();
		JSONObject attrib = new JSONObject();
		result.put("attributes", attrib);
		JSONArray clas = new JSONArray();
		result.put("classNames", clas);

		Attributes attr = e.attributes();
		List<Attribute> lattr = attr.asList();
		for(int i=0;i<lattr.size();i++) {
			
			Attribute a = lattr.get(i);
			attrib.put(a.getKey(), a.getValue());
			
		}
		
		Set<String> c = e.classNames();
		for(String o : c) {
			
			clas.add(o);
			
		}

		result.put("text", e.text());
		result.put("html", e.html());
		result.put("formVal", e.val());
		result.put("tagName", e.tagName());
		result.put("id", e.id());
		result.put("nodeName", e.nodeName());
		result.put("outerHtml", e.outerHtml());
		result.put("ownText", e.ownText());
		result.put("wholeText", e.wholeText());
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		for(String k : env.doms.keySet()) {
			
			result.add(k);
			
		}
		
		return result;
		
	}

	public static String exist(EnvManager env, String domId) {
		
		if (env.doms.containsKey(domId)) 
			return "1"; 
		else return "0";
		
	}

	public static String close(EnvManager env, String domId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the domId does not exist
		if (exist(env, domId).equals("0")) {
			
			throw new Exception("Sorry, the domId '"+domId+"' does not exist.");
			
		}
		
		env.doms.remove(domId);
		
		return result;
		
	}

	public static String closeall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, Document> e : env.doms.entrySet()) {
			
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
	
	public static void load_from_string(EnvManager env, String domId, String html) throws Exception {
		
		//Generate an error if the domId is null or empty
		if (domId==null || domId.equals("")) {
			
			throw new Exception("Sorry, the domId cannot be null or empty.");
			
		}
		
		//Generate an error if the domId already exist
		if (exist(env, domId).equals("1")) {
			
			throw new Exception("Sorry, the domId '"+domId+"' already exist.");
			
		}
		
		Document doc = Jsoup.parse(html);
		
		env.doms.put(domId, doc);
		
	}
	
	public static void load_from_url(EnvManager env, String domId, String url, String method, String timeout, String jsonHeader, String jsonCookies, String jsonData) throws Exception {
		
		//Generate an error if the domId is null or empty
		if (domId==null || domId.equals("")) {
			
			throw new Exception("Sorry, the domId cannot be null or empty.");
			
		}
		int t_o = 0;
		try {
			
			t_o = Integer.parseInt(timeout);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the timeout is not a valid number (ex: 5000).");
			
		}
		
		//Generate an error if the method is null or empty
		if (method==null || method.equals("")) {
			
			throw new Exception("Sorry, the method cannot be null or empty.");
			
		}
		
		method = method.toUpperCase();
		
		//Generate an error if the method is not GET|POST
		if (!method.equals("GET") && !method.equals("POST")) {
			
			throw new Exception("Sorry, the method must be GET|POST.");
			
		}
		
		//Generate an error if the domId already exist
		if (exist(env, domId).equals("1")) {
			
			throw new Exception("Sorry, the domId '"+domId+"' already exist.");
			
		}

		JSONObject header = (JSONObject) JsonManager.load(jsonHeader);
		JSONObject cookies = (JSONObject) JsonManager.load(jsonCookies);
		JSONObject dta = (JSONObject) JsonManager.load(jsonData);
		
		Document doc = null;
		
		Connection cnt = Jsoup.connect(url);
		
		for(Object o : header.keySet()) {
			
			String key = (String) o;
			String value = (String) header.get(key);
			cnt.header(key, value);
			
		}
		
		for(Object o : cookies.keySet()) {
			
			String key = (String) o;
			String value = (String) cookies.get(key);
			cnt.cookie(key, value);
			
		}
		
		if (method.equals("GET")) {
			doc = cnt.timeout(t_o).userAgent("MentDB/mentdb.org").get();
		} else if (method.equals("POST")) {
			cnt.timeout(t_o);
			
			for(Object o : dta.keySet()) {
				
				String key = (String) o;
				String value = (String) dta.get(key);
				cnt.data(key, value);
				
			}
			
			doc = cnt.userAgent("MentDB/mentdb.org").post();
		}
		
		env.doms.put(domId, doc);
		
	}

}
