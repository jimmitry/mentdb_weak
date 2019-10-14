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

//Basic doc page
public class Doc_config {
	
	static String p = "config";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Configuration");
		
		Doc.createSection(p, "WARNING", "");

		Doc.createBlockText(p, "<span style='font-weight:bold;color:#F00'>YOU MUST BE AN ADVANCED USER TO CHANGE THE CONFIG FILE.</span>");

		Doc.createSection(p, "Location", "");
		Doc.createBlockText(p, "MentDB_Server/conf/server.conf");
		
	}

}
