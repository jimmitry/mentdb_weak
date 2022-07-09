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

package re.jpayet.mentdb.core.entity.concentration;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.parameter.ParameterManager;

public class ConcentrationManager {
	
	public static JSONObject allConcentrations = new JSONObject();
	
	//Init the concentration depth numbers
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {
		
		ParameterManager.add("C[symbol]", "200");
		ParameterManager.add("C[relation]", "200");
		ParameterManager.add("C[relation-env]", "40");
		
		ParameterManager.add("C[graph-symbol]", "5");
		ParameterManager.add("C[graph-relation]", "5");

		ParameterManager.add("C[process-tab]", "100");
		ParameterManager.add("C[brain-env]", "40");
		ParameterManager.add("C[brain-target]", "200");
		ParameterManager.add("C[last-user-r]", "40");
		
		ParameterManager.add("C[user]", "25");
		ParameterManager.add("C[language]", "25");

		ParameterManager.add("C[deep-search]", "30");
		ParameterManager.add("C[deep-activation-level]", "2");

		ParameterManager.add("C[last-strategy]", "10");
		
		ParameterManager.add("C[elevation_max_acceptance]", "30");

		//To do ...
		ParameterManager.add("C[emotion]", "25");

		allConcentrations.put("C[symbol]", ParameterManager.get_value("C[symbol]"));
		allConcentrations.put("C[relation]", ParameterManager.get_value("C[relation]"));
		allConcentrations.put("C[relation-env]", ParameterManager.get_value("C[relation-env]"));
		allConcentrations.put("C[graph-symbol]", ParameterManager.get_value("C[graph-symbol]"));
		allConcentrations.put("C[graph-relation]", ParameterManager.get_value("C[graph-relation]"));
		allConcentrations.put("C[process-tab]", ParameterManager.get_value("C[process-tab]"));
		allConcentrations.put("C[brain-env]", ParameterManager.get_value("C[brain-env]"));
		allConcentrations.put("C[brain-target]", ParameterManager.get_value("C[brain-target]"));
		allConcentrations.put("C[last-user-r]", ParameterManager.get_value("C[last-user-r]"));
		allConcentrations.put("C[user]", ParameterManager.get_value("C[user]"));
		allConcentrations.put("C[language]", ParameterManager.get_value("C[language]"));
		allConcentrations.put("C[deep-search]", ParameterManager.get_value("C[deep-search]"));
		allConcentrations.put("C[deep-activation-level]", ParameterManager.get_value("C[deep-activation-level]"));
		allConcentrations.put("C[last-strategy]", ParameterManager.get_value("C[last-strategy]"));
		allConcentrations.put("C[elevation_max_acceptance]", ParameterManager.get_value("C[elevation_max_acceptance]"));
		allConcentrations.put("C[emotion]", ParameterManager.get_value("C[emotion]"));

	}
	
	//Load the concentration depth numbers
	@SuppressWarnings("unchecked")
	public static void load() throws Exception {

		allConcentrations.put("C[symbol]", ParameterManager.get_value("C[symbol]"));
		allConcentrations.put("C[relation]", ParameterManager.get_value("C[relation]"));
		allConcentrations.put("C[relation-env]", ParameterManager.get_value("C[relation-env]"));
		allConcentrations.put("C[graph-symbol]", ParameterManager.get_value("C[graph-symbol]"));
		allConcentrations.put("C[graph-relation]", ParameterManager.get_value("C[graph-relation]"));
		allConcentrations.put("C[process-tab]", ParameterManager.get_value("C[process-tab]"));
		allConcentrations.put("C[brain-env]", ParameterManager.get_value("C[brain-env]"));
		allConcentrations.put("C[brain-target]", ParameterManager.get_value("C[brain-target]"));
		allConcentrations.put("C[last-user-r]", ParameterManager.get_value("C[last-user-r]"));
		allConcentrations.put("C[user]", ParameterManager.get_value("C[user]"));
		allConcentrations.put("C[language]", ParameterManager.get_value("C[language]"));
		allConcentrations.put("C[deep-search]", ParameterManager.get_value("C[deep-search]"));
		allConcentrations.put("C[deep-activation-level]", ParameterManager.get_value("C[deep-activation-level]"));
		allConcentrations.put("C[last-strategy]", ParameterManager.get_value("C[last-strategy]"));
		allConcentrations.put("C[elevation_max_acceptance]", ParameterManager.get_value("C[elevation_max_acceptance]"));
		allConcentrations.put("C[emotion]", ParameterManager.get_value("C[emotion]"));

	}
	
	//Show the concentration object
	public static JSONObject show()  {

		return allConcentrations;

	}
	
	//Get the concentration depth number
	public static int getConcentrationDepth(String key) throws NumberFormatException, Exception {

		return Integer.parseInt(""+allConcentrations.get(key));

	}
	
	//Set the concentration depth number
	@SuppressWarnings("unchecked")
	public static void setConcentrationDepth(String key, int depth) throws Exception {

		ParameterManager.update(key, ""+depth);
		
		allConcentrations.put(key, ""+depth);

	}

}