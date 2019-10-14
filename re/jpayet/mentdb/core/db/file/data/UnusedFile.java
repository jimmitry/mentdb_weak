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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.json.simple.JSONArray;

import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.ext.json.JsonFormatter;

//Manage the unused file
public class UnusedFile {
	
	public RandomAccessFile unusedFile = null;
	public int id = 0;

	//Contructor
	public UnusedFile(int id) throws FileNotFoundException {
		
		this.id = id;
		
	}
	
	//Get a list of unused block
	@SuppressWarnings("unchecked")
	public static String unused_list(DataFile dataFile, int limit) throws IOException {
		
		//Initialization
		long position = dataFile.unusedFile.unusedFile.length();
		
		JSONArray array = new JSONArray();
		boolean continu = true;
		
		//Parse all values
		while (continu) {
			
			if (position>0 && dataFile.unusedFile.unusedFile.length()>0) {
				
				position-=8;
				
				//Move to the end
				dataFile.unusedFile.unusedFile.seek(position);
				
				array.add(dataFile.unusedFile.unusedFile.readLong());
				
			}
			
			if (limit==0 || dataFile.unusedFile.unusedFile.length()==0) {
				
				continu = false;
				
			}
			
			limit--;
			
		}
		
		return JsonFormatter.format(array.toJSONString());
		
	}
	
	//Get a position to write
	public static long getPosition(long sessionId, DataFile dataFile) throws IOException {

		//Initialization
		RandomAccessFile unusedFile = dataFile.unusedFile.unusedFile;
		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		long position = 0;

		//Check if there are unused blocks
		if (unusedFile.length()==0) {
			
			//Move to the end of the data file
			position = dataFile.dataFile.length();

			//Initialization
			long logPosition = logFiles.length();

			//Move to the end position into the log file
			logFiles.seek(logPosition);

			//Write the log type
			logFiles.writeUTF("0;F;"+dataFile.id+";"+position);
			//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;F;"+dataFile.id+";"+position+"\n", true);

			//Clear the block
			Block.delete(dataFile.id, position);
			
		} else {

			//Get the last position
			unusedFile.seek(unusedFile.length()-8);
			long lastPosition = unusedFile.readLong();

			//Initialization
			long logPosition = logFiles.length();

			//Move to the end position into the log file
			logFiles.seek(logPosition);

			//Write the log type
			logFiles.writeUTF("0;E;"+dataFile.id+";"+lastPosition);
			//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;E;"+dataFile.id+";"+lastPosition+"\n", true);

			//Delete the last element
			unusedFile.setLength(unusedFile.length()-8);

			//Misc.append("/Users/jimmitry/Desktop/exe.log", "DELETE>"+(unusedFile.length()/8)+"\n", true);

			position = lastPosition;
			
		}
		
		return position;
		
	}
	
	//Get the last unused block
	public static long last_unused_block(DataFile df) throws IOException {
		
		//Initialization
		if (nb_unused_block(df)==0) return -1;
		else {
			df.unusedFile.unusedFile.seek(df.unusedFile.unusedFile.length()-8);
			return df.unusedFile.unusedFile.readLong();
		}
		
	}
	
	//Get the number of unused block
	public static long nb_unused_block(DataFile df) throws IOException {
		
		//Initialization
		return df.unusedFile.unusedFile.length()/8;
		
	}
	
	//Open the unused file
	public void open() throws FileNotFoundException {

		//Open the unused file
		unusedFile = new RandomAccessFile("data"+File.separator+"data"+File.separator+id+File.separator+"brain"+id+".uub", "rw");
		
	}
	
	//Close the unused file
	public void close() throws IOException {

		//Close the unused file
		unusedFile.close();
		
	}

}
