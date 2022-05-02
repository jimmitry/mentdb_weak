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
public class Doc_faq {
	
	static String p = "faq";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "FAQ");
		
		Doc.createSection(p, "This is the first version", "I am waiting for your questions ...");
		
	}

}
