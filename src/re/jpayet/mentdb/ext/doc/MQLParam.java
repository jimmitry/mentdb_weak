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

package re.jpayet.mentdb.ext.doc;

//The MQL documentation object
public class MQLParam {
	//Properties
	public String name = "";
	public String description = "";
	public String type = "";
	public boolean required = true;
	
	//Constructor
	public MQLParam(String name, String description, String type, boolean required) {

		this.name = name;
		this.description = description;
		this.type = type;
		this.required = required;
		
	}

}