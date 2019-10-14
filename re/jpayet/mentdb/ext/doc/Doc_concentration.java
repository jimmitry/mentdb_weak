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
public class Doc_concentration {
	
	static String p = "concentration";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Concentration");

		Doc.createSection(p, "Show all the concentrations depth", "");
		Doc.createBlockMql(p, "Concentration", "concentration show");

		Doc.createSection(p, "Show a specific concentration depth", "");
		Doc.createBlockMql(p, "Concentration", "concentration depth");
		
		Doc.createSection(p, "Set the concentration depth", "");
		Doc.createBlockMql(p, "Concentration", "concentration set depth");
		
	}

}
