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
public class Doc_user {
	
	static String p = "user";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "User &amp; Right");

		Doc.createSection(p, "Show users", "");
		Doc.createBlockMql(p, "User", "user show");

		Doc.createSection(p, "Create a new user", "");
		Doc.createBlockMql(p, "User", "user create");
		
		Doc.createSection(p, "Update a user password", "");
		Doc.createBlockMql(p, "User", "user set password");
		
	}

}
