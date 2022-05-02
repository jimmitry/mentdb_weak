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

package re.jpayet.mentdb.core.entity.language;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class LanguageManager {
	
	public static JSONArray language = new JSONArray();
	
	//Create the parameter object
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {
		
		JSONObject lang = new JSONObject();
		lang.put("l", new JSONArray());
		Record2.add("record", "LANG[]", lang.toJSONString());
		
	}
	
	//Load the parameter object
	public static void load() throws Exception {
		
		language = (JSONArray) Record2.getNode("LANG[]").get("l");
		
	}
	
	//Show all languages
	public static JSONArray showAll() throws Exception {
		
		return language;
		
	}
	
	//Check if a language already exist
	public static void exceptionIfNotExist(String lang) throws Exception {
		
		if (!language.contains(lang)) {
			
			throw new Exception("Sorry, the language "+lang+" does not exist."); 
		
		}
		
	}
	
	//Create a new language
	@SuppressWarnings("unchecked")
	public static void create(String language, SessionThread session, EnvManager env) throws Exception {
		
		synchronized ("LANG[]") {
		
			//Check parameters
			if (Misc.lrtrim(language).equals("")) {
				throw new Exception("Sorry, the language id is required char(2)."); 
			}
			
			//Check parameters
			if (language.length()!=2) {
				throw new Exception("Sorry, the language id must be 2 characters."); 
			}
			
			//Get the language node
			JSONObject rec = Record2.getNode("L["+language+"]");
			if (rec!=null) {
				throw new Exception("Sorry, the language id "+language+" already exist."); 
			}
			
			JSONObject languageDirectAccess = new JSONObject();
			Record2.add("record", "L["+language+"]", languageDirectAccess.toJSONString());
			
			JSONObject record = Record2.getNode("LANG[]");
			LanguageManager.language = (JSONArray) record.get("l");
			LanguageManager.language.add(0, language);
			
			Record2.update("LANG[]", record.toJSONString());
			
		}
		
	}

}
