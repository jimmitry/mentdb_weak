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

package re.jpayet.mentdb.core.db.basic;

//The basic position object (used for data file and index file)
public class BasicPosition {
	
	//Properties
	public int fileId = -1;
	public long position = -1;
	
	//Constructor
	public BasicPosition(int fileId, long position) {
		
		//Initialization
		this.fileId = fileId;
		this.position = position;
		
	}
	
	//Constructor
	public BasicPosition() {
		
	}

}