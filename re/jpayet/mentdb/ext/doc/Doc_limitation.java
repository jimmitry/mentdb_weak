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
public class Doc_limitation {
	
	static String p = "limitation";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Limitations");

		Doc.createSection(p, "Max node", "(2^31-1)x(2^63-1)x0.75");
		Doc.createSection(p, "High availability", "This server, by its nature can not be under high availability.<br>This brain works like me, I can not chat instantly with 2 people at the same time, I put on hold ...");
		Doc.createSection(p, "Scalability", "This server, by its nature can not be scalable.<br>This brain works like me, I can not chat instantly with 2 people at the same time, I put on hold ...");
		Doc.createSection(p, "ODBC", "No. I have provided you a REST API, and a Java connector for low level calls.<br><a href='clients.html'>Go to clients ...</a>");
		
	}

}
