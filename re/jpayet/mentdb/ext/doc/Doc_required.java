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
public class Doc_required {
	
	static String p = "required";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Requirement");

		Doc.createSection(p, "CPU", "2 GHz");
		Doc.createSection(p, "RAM", "1 Go");
		Doc.createSection(p, "DISK", "1 Go");
		
	}

}
