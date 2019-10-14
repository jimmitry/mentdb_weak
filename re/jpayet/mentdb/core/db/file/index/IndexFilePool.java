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

package re.jpayet.mentdb.core.db.file.index;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

//Manage the index file pool
public class IndexFilePool {

	//Initialization
	public static LinkedHashMap<Integer, IndexFile> allIndexFiles = new LinkedHashMap<Integer, IndexFile>();
	public static int NB_INDEX_FILE_OPEN = 0;
	
	//Close all index files
	public static void close() throws IOException {
		
		//Parse the map
		Set<Entry<Integer, IndexFile>> entries = allIndexFiles.entrySet();
		Iterator<Entry<Integer, IndexFile>> it = entries.iterator();
		while (it.hasNext()) {
			
			//Get the key
			Entry<Integer, IndexFile> e = it.next();
			
			//Close the file
			e.getValue().close();
			
		}
		
	}
	
	//Get an index file
	public static IndexFile if_get(int index) throws IOException {
		
		//Check if the file is already loaded
		if (allIndexFiles.containsKey(index)) {
			
			return allIndexFiles.get(index);
			
		} else {
			
			//Check if the number of file > NB_INDEX_FILE_OPEN
			if (allIndexFiles.size()>=NB_INDEX_FILE_OPEN) {
				
				//Delete the first element
				Set<Entry<Integer, IndexFile>> entries = allIndexFiles.entrySet();
				// parse the set
				Iterator<Entry<Integer, IndexFile>> it = entries.iterator();
				if (it.hasNext()) {
					
					//Get the key
					Entry<Integer, IndexFile> e = it.next();
					
					//Close the file and remove entry
					e.getValue().close();
					allIndexFiles.remove(e.getKey());
					
				}
				
			}
			
			//Load the index file
			IndexFile indexFile = new IndexFile(index, "data"+File.separator+"index"+File.separator+index+File.separator+"brain"+index+".idx");
			indexFile.open();
			
			allIndexFiles.put(index, indexFile);
			
			return allIndexFiles.get(index);
			
		}
		
	}

}
