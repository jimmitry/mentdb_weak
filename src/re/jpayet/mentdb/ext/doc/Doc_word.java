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
public class Doc_word {
	
	static String p = "word";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Create word");

		Doc.createSection(p, "Create words", "");
		Doc.createBlockMql(p, "Word", "word create");
		
	}

}
