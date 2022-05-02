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
public class Doc_install {
	
	static String p = "install";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Install");
		
		Doc.createSection(p, "MentDB Weak Server", "On OSX/Linux or Windows");

		Doc.createBlockText(p, "1 - BEGIN");
		Doc.createBlockText(p, "2 - Download and Install Java JDK 8");
		Doc.createBlockText(p, "3 - Unzip the file 'MentDB_Weak_Server_v_X.X.X_XXXX-XX-XX.zip'");
		Doc.createBlockText(p, "4 - Install JCE Policy: 'MentDB_Server/tools/local_policy.jar' and 'MentDB_Server/tools/US_export_policy.jar' to your Java system file (jre/lib/security)");
		Doc.createBlockText(p, "5 - edit jre/lib/security/java.security");
		Doc.createBlockText(p, "6 - add security.provider.10=org.bouncycastle.jce.provider.BouncyCastleProvider");
		Doc.createBlockText(p, "7 - copy MentDB_Server/lib/bc*.jar to your Java system file jre/lib/ext");
		Doc.createBlockText(p, "8 - Install MySQL 5.7 and save the root user");
		Doc.createBlockText(p, "9 - Create the database 'mentdb' in MySQL 5.7");
		Doc.createBlockText(p, "10 - cd MentDB_Server/bin/osx-linux (on osx or linux)");
		Doc.createBlockText(p, "10 - cd MentDB_Server/bin/windows (on windows)");
		Doc.createBlockText(p, "11 - Edit the file ./server.sh (or server.bat on Windows)");
		Doc.createBlockText(p, "12 - Change the path to your new Java JDK");
		Doc.createBlockText(p, "13 - Grant 'READ/WRITE' options to your system user on the directory 'MentDB_Server'");
		Doc.createBlockText(p, "14 - Update the file 'MentDB_Server/conf/server.conf' (admin pwd/key, retention day, mysql connection …)");
		Doc.createBlockText(p, "15 - Start the MentDB server ./server.sh (or server.bat on Windows)");
		Doc.createBlockText(p, "16 - END");
		
		Doc.createSection(p, "MentDB Weak Editor", "On OSX/Linux or Windows");

		Doc.createBlockText(p, "1 - BEGIN");
		Doc.createBlockText(p, "2 - Download and Install Java JRE 8");
		Doc.createBlockText(p, "3 - Unzip the file 'MentDB_Weak_Editor_v_X.X.X_XXXX-XX-XX.zip'");
		Doc.createBlockText(p, "4 - cd MentDB_Editor/bin/osx-linux (on osx or linux)");
		Doc.createBlockText(p, "4 - cd MentDB_Editor/bin/windows (on windows)");
		Doc.createBlockText(p, "5 - Edit the file ./editor.sh (or editor.bat on Windows)");
		Doc.createBlockText(p, "6 - Change the path to your new Java JDK");
		Doc.createBlockText(p, "7 - Grant 'READ/WRITE' options to your system user on the directory 'MentDB_Editor'");
		Doc.createBlockText(p, "8 - Start the MentDB editor ./editor.sh (or editor.bat on Windows)");
		Doc.createBlockText(p, "9 - The trigger window open ...");
		Doc.createBlockText(p, "10 - Select and Update the default/admin connection from the trigger window with and set the hostname/port(9998)/admin/password/key like in your the config file ('MentDB_Server/conf/server.conf')");
		Doc.createBlockText(p, "11 - Double click on the default/admin connection");
		Doc.createBlockText(p, "12 - The editor open ...");
		Doc.createBlockText(p, "13 - END");
		
	}

}
