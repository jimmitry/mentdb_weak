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

package re.jpayet.mentdb.core.db;

import java.sql.Connection;

//Manage records
public class ConnectorObj {
	
	public int pos = -1;
	public Connection cnt = null;
	
	public ConnectorObj() {
		
	}

}
