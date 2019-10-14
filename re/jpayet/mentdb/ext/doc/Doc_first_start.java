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
public class Doc_first_start {
	
	static String p = "first_start";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "First start and database creation");

		Doc.createSection(p, "Start MentDB Server.", "On OSX/Linux or Windows");

		Doc.createBlockText(p, "1 - cd MentDB_Server/bin");
		Doc.createBlockText(p, "2 - cd osx-linux (on osx or linux)");
		Doc.createBlockText(p, "2 - cd windows (on windows)");
		Doc.createBlockText(p, "3 - ./server.sh (or server.bat under Windows)");
		Doc.createBlockText(p, "4 - Wait please, the server starts and your single<b>*</b> database will be created ...");

		Doc.createBlockText(p, "<b>* There is a single database into MentDB, as a human has an unique brain.</b><br><br><img style='width:790px' src='images/cap00.png'>");
		
	}

}
