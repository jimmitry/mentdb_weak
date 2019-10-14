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

package re.jpayet.mentdb.core.db.file.data;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

//Manage the data file pool
public class DataFilePool {

	//Initialization
	public static LinkedHashMap<Integer, DataFile> allDataFiles = new LinkedHashMap<Integer, DataFile>();
	public static int NB_DATA_FILE_OPEN = 0;
	
	//Close all data files
	public static void close() throws IOException {
		
		//Parse the map
		Set<Entry<Integer, DataFile>> entries = allDataFiles.entrySet();
		Iterator<Entry<Integer, DataFile>> it = entries.iterator();
		while (it.hasNext()) {
			
			//Get the key
			Entry<Integer, DataFile> e = it.next();
			
			//Close the file
			e.getValue().close();
			
		}
		
	}
	
	//Get a data file to write
	public static DataFile getDataFileToWrite(int nbBlockToWrite) throws Exception {
		
		//Initialization
		int id = 0;
		DataFile dataFile = null;

		//Parse all data files
		while(true) {
			
			//Get the first data file
			dataFile = DataFilePool.dfp_get(id);
			
			//Check if there are unused blocks
			if (dataFile.unusedFile.unusedFile.length()/8>=nbBlockToWrite) {
				
				break; 
			
			}
			
			//Check if the file is not full
			if ((DataFile.df_current_block_size(dataFile.dataFile)+nbBlockToWrite)<=DataFile.MAX_BLOCK) {
								
				break;
			
			}
			
			id++;
			
		}
		
		return dataFile;
		
	}
	
	//Get a data file
	public static DataFile dfp_get(int index) throws IOException {
		
		//Check if the file is already loaded
		if (allDataFiles.containsKey(index)) {
			
			return allDataFiles.get(index);
			
		} else {
			
			//Check if the number of file > NB_DATA_FILE_OPEN
			if (allDataFiles.size()>=NB_DATA_FILE_OPEN) {
				
				//Delete the first element
				Set<Entry<Integer, DataFile>> entries = allDataFiles.entrySet();
				// parse the set
				Iterator<Entry<Integer, DataFile>> it = entries.iterator();
				if (it.hasNext()) {
					
					//Get the key
					Entry<Integer, DataFile> e = it.next();
					
					//Close the file and remove entry
					e.getValue().close();
					allDataFiles.remove(e.getKey());
					
				}
				
			}
			
			//Load the data file
			DataFile dataFile = new DataFile(index, "data"+File.separator+"data"+File.separator+index+File.separator+"brain"+index+".dat");
			dataFile.open();
			
			allDataFiles.put(index, dataFile);
			
			return allDataFiles.get(index);
			
		}
		
	}

}
