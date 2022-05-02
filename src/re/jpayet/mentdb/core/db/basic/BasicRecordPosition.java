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
public class BasicRecordPosition {
	
	//Properties
	public BasicPosition index = new BasicPosition();
	public BasicPosition collision = new BasicPosition();
	public BasicPosition data = new BasicPosition();
	
	//Constructor
	public BasicRecordPosition(int indexFileId, long indexPosition, int collisionFileId, long collisionPosition, int dataFileId, long dataPosition) {
		
		//Initialization
		this.index.fileId = indexFileId;
		this.index.position = indexPosition;
		this.collision.fileId = collisionFileId;
		this.collision.position = collisionPosition;
		this.data.fileId = dataFileId;
		this.data.position = dataPosition;
		
	}
	
	//Constructor
	public BasicRecordPosition() {
		
	}
	
	public String toString() {
		
		String result = "index.fileId="+this.index.fileId+"\n";
		result += "index.position="+this.index.position+"\n";
		result += "collision.fileId="+this.collision.fileId+"\n";
		result += "collision.position="+this.collision.position+"\n";
		result += "data.fileId="+this.data.fileId+"\n";
		result += "data.position="+this.data.position+"\n";
		
		return result;
		
	}

}