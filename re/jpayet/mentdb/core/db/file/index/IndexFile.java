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
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.BasicIndex;
import re.jpayet.mentdb.core.db.basic.BasicPosition;
import re.jpayet.mentdb.core.db.file.data.Block;
import re.jpayet.mentdb.core.db.file.data.DataFile;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.server.Start;

//Manage the index file
public class IndexFile {
	
	//Local properties
	public String fileName = "";
	public int id = 0;
	public RandomAccessFile indexFile = null;
	public DataFile dataFile = null;

	public static long MAX_INDEX_BLOCK_SIZE = 0;
	public static int EXTEND_FS_FORCE = 0;
	
	//Constructor
	public IndexFile(int id, String fileName) {

		this.id = id;
		this.fileName = fileName;
		
	}
	
	//Open the data file
	public void open() throws IOException {
		
		//Open the data file
		indexFile = new RandomAccessFile(fileName, "rw");
				
	}
	
	//Add nbEntries extra entries
	public void addExtraEntries(long nbEntries) throws IOException {
		
		//Write nbEntries empty entries
		for(long i=0;i<nbEntries;i++) {
			
			writeDefaultEntry(indexFile.length(), -1, -1);
			
		}
		
	}
	
	//Get the current block size of the index file
	public static long if_current_block_size(RandomAccessFile indexFile) throws IOException {
		
		//Move to the start
		indexFile.seek(0);
		
		//return the current size
		return indexFile.readLong();
		
	}
	
	//Check if the index file is used > 75%
	public boolean isG75Percent() throws Exception {
		
		if (IndexFile.if_current_block_size(indexFile)*100/IndexFile.if_current_max_block_size(indexFile)>75) return true;
		else return false;
		
	}
	
	//Resize the index file
	public static void resize(long sessionId, IndexFile indexFile) throws Exception {
		
		//Initialization
		RandomAccessFile savedResizeInfo = null;
		int indexFileId = 0;
		long maxIndexSize = 0;
		long lastIndexSize = 0;
		long currentBlockPosition = 0;
		long maxBlockPosition = 0;
		
		if (indexFile==null) {
			
			//Try to read the file
			try {
				
				if (!(new File("data"+File.separator+"transaction"+File.separator+"resize.idx")).exists()) {
					
					return;
				
				}
				
				//Get data from the file
				savedResizeInfo = new RandomAccessFile("data"+File.separator+"transaction"+File.separator+"resize.idx", "rw");
				
				//Move to the start
				savedResizeInfo.seek(0);
				
				//Read data
				indexFileId = savedResizeInfo.readInt(); //Index file id
				lastIndexSize = savedResizeInfo.readLong(); //The last index file length (before resize)
				maxIndexSize = savedResizeInfo.readLong(); //The counter of entry to add
				currentBlockPosition = savedResizeInfo.readLong(); //The current block position finished
				maxBlockPosition = savedResizeInfo.readLong(); //The max block to move
				
				//Load the indexFile
				indexFile = IndexFilePool.if_get(indexFileId);
				
			} catch (Exception e) {
				
				//Nothing to do ... the crash appear before writing into the file
				
				//Close and delete the file
				try {savedResizeInfo.close();} catch (Exception f) {};
				try {(new File("data"+File.separator+"transaction"+File.separator+"resize.idx")).delete();} catch (Exception f) {};
				
				return;
				
			}
			
		} else {
			
			indexFileId = indexFile.id;
			lastIndexSize = indexFile.indexFile.length();
			maxIndexSize = (indexFile.indexFile.length()-8)*2;
			currentBlockPosition = 0;
			maxBlockPosition = (indexFile.indexFile.length()-8)/12;
			
			//Write the resize.idx file
			savedResizeInfo = new RandomAccessFile("data"+File.separator+"transaction"+File.separator+"resize.idx", "rw");
			
			//Move to the start
			savedResizeInfo.seek(0);
			
			//Write data
			savedResizeInfo.writeInt(indexFileId); //Index file id
			savedResizeInfo.writeLong(lastIndexSize); //The last index file length (before resize)
			savedResizeInfo.writeLong(maxIndexSize); //The counter of entry to add
			savedResizeInfo.writeLong(currentBlockPosition); //The current block position (in the table not in the file ...)
			savedResizeInfo.writeLong(maxBlockPosition); //The max block position (in the table not in the file ...)
			
		}
		
		//Extend the number of entry in the index file
		
		//Write empty entries
		while(lastIndexSize<=maxIndexSize) {
			
			//Write and clear the block
			indexFile.writeDefaultEntry(lastIndexSize, -1, -1);
			
			//Mark the block as created and cleared with successful
			savedResizeInfo.seek(4);
			lastIndexSize+=12;
			savedResizeInfo.writeLong(lastIndexSize);
			
		}
		
		//Move data
		while (currentBlockPosition!=maxBlockPosition) {
			
			//Move to the current block
			indexFile.indexFile.seek(currentBlockPosition*12+8);
		
			//Read the file id
			int collisionDataFileId = indexFile.indexFile.readInt();
			
			//Read the data file position
			long collisionDataFilePosition = indexFile.indexFile.readLong();
			
			//Check if there are collision
			if (collisionDataFileId!=-1) {
				
				//Clear the index entry
				IndexFilePool.if_get(indexFileId).writeEntry(sessionId, indexFileId,
						currentBlockPosition*12+8, 
						-1, 
						-1);
				
				//Get all collisions
				String bnIndex = Block.decodeb64(Record.getData(DataFilePool.dfp_get(collisionDataFileId), collisionDataFilePosition));
				BasicIndex bn = new BasicIndex();
				bn.load(bnIndex);
				
				//Delete the collision node
				Record.delete("resize------", sessionId, DataFilePool.dfp_get(collisionDataFileId) , collisionDataFilePosition);
				
				//Get all nodes
				HashMap<String, String> allNodes = new HashMap<String, String>();
				for(int i=0;i<bn.countCollision();i++) {
					
					allNodes.put(bn.getCollisionKey(i), Block.decodeb64(Record.getData(DataFilePool.dfp_get(bn.getCollisionFileId(i)), bn.getCollisionPosition(i))));
					
				}
				
				//Delete all nodes
				for(int i=0;i<bn.countCollision();i++) {
					
					Record.delete("resize------", sessionId, DataFilePool.dfp_get(bn.getCollisionFileId(i)), bn.getCollisionPosition(i));
					
				}
				
				//Recreate all nodes
				for (Entry<String, String> e : allNodes.entrySet()) {
				    
					Record.recreate(sessionId, e.getKey(), e.getValue());
				    
				}
				
			}
			
			//Commit the transaction
			Transaction.commit(sessionId);
			
			//Mark the block moved
			savedResizeInfo.seek(20);
			currentBlockPosition++;
			savedResizeInfo.writeLong(currentBlockPosition);
			
		}
		
		//Close and delete the file
		try {savedResizeInfo.close();} catch (Exception f) {};
		try {(new File("data"+File.separator+"transaction"+File.separator+"resize.idx")).delete();} catch (Exception f) {};
		
	}
	
	//Get the max block size of the index file
	public static long if_current_max_block_size(RandomAccessFile indexFile) throws IOException {
		
		//return the current max size
		return (indexFile.length()-8)/12;
		
	}
	
	//Get the size of the index file
	public static long if_get_size(int id) throws IOException {
		
		return IndexFilePool.if_get(id).indexFile.length();
		
	}
	
	//Get the percent of used of the index file
	public static int if_percent_used(RandomAccessFile indexFile) throws Exception {
		
		//return the current max size
		return (int) (((IndexFile.if_current_block_size(indexFile))*100/IndexFile.if_current_max_block_size(indexFile)));
		
	}
	
	//Get the per30 of used of the index file
	public static String if_per30_used(RandomAccessFile indexFile) throws Exception {
		
		//Initialization
		int per30 = (int) (((IndexFile.if_current_block_size(indexFile)*30)/IndexFile.if_current_max_block_size(indexFile)));
		
		//return the per20 in string format
		return new String(new char[per30]).replace('\0', '|')+
				new String(new char[30-per30]).replace('\0', '-');
		
	}
	
	//Close the data file
	public void close() throws IOException {

		//Open the data file
		indexFile.close();
		
	}
	
	//Check if an entry already exist
	public static Record existEntry(String key, int indexFileId, long indexFileTabIndex) throws Exception {
		
		//Initialization
		String data = "";
		long collisionDataFilePosition = -1;
		int collisionDataFileId = -1;
		
		//Move to the position
		IndexFilePool.if_get(indexFileId).indexFile.seek(indexFileTabIndex*12+8);
		
		//Read the collision file id
		collisionDataFileId = IndexFilePool.if_get(indexFileId).indexFile.readInt();
		
		//Read the collision data file position
		collisionDataFilePosition = IndexFilePool.if_get(indexFileId).indexFile.readLong();
		
		if (collisionDataFileId!=-1) {
		
			//Get the basic index record
			String bnIndex = Block.decodeb64(Record.getData(DataFilePool.dfp_get(collisionDataFileId), collisionDataFilePosition));
			BasicIndex bn = new BasicIndex();
			bn.load(bnIndex);
			
			int dataFileId = bn.getCollisionFileId(key);
			long dataFilePosition = bn.getCollisionPosition(key);
			
			//Check if exist a collision
			if (dataFileId!=-1) {
				
				//A collision exist
				data = Block.decodeb64(Record.getData(DataFilePool.dfp_get(dataFileId), dataFilePosition));
				
				//Return the record
				return new Record(dataFileId, dataFilePosition, data);
				
			} else return null;
			
			
		} else return null;
		
	}
	
	//Check if a collision node already exist
	public static boolean existCollisionNode(int indexFileToWrite, long indexFileTabPosition) throws IOException {
		
		//Initialization
		int collisionDataFileId = -1;
		
		//Move to the position
		IndexFilePool.if_get(indexFileToWrite).indexFile.seek(indexFileTabPosition*12+8);
		
		//Read the collision file id
		collisionDataFileId = IndexFilePool.if_get(indexFileToWrite).indexFile.readInt();
		
		if (collisionDataFileId!=-1) {
			
			return true;
			
		} else return false;
		
	}
	
	//Get the collision node
	public static String getCollisionNode(int indexFileToWrite, long indexFileTabPosition) throws Exception {
		
		//Initialization
		int collisionDataFileId = -1;
		long collisionDataFilePosition = -1;
		
		//Move to the position
		IndexFilePool.if_get(indexFileToWrite).indexFile.seek(indexFileTabPosition*12+8);
		
		//Read the collision file id
		collisionDataFileId = IndexFilePool.if_get(indexFileToWrite).indexFile.readInt();
		
		//Read the collision position
		collisionDataFilePosition = IndexFilePool.if_get(indexFileToWrite).indexFile.readLong();
		
		if (collisionDataFileId!=-1) {
			
			return Block.decodeb64(Record.getData(DataFilePool.dfp_get(collisionDataFileId), collisionDataFilePosition));
			
		} else return null;
		
	}
	
	//Get the collision coordinate
	public static BasicPosition getCollisionCoord(int indexFileToWrite, long indexFileTabPosition) throws IOException {
		
		//Initialization
		int collisionDataFileId = -1;
		long collisionDataFilePosition = -1;
		
		//Move to the position
		IndexFilePool.if_get(indexFileToWrite).indexFile.seek(indexFileTabPosition*12+8);
		
		//Read the collision file id
		collisionDataFileId = IndexFilePool.if_get(indexFileToWrite).indexFile.readInt();
		
		//Read the collision position
		collisionDataFilePosition = IndexFilePool.if_get(indexFileToWrite).indexFile.readLong();
		
		return new BasicPosition(collisionDataFileId, collisionDataFilePosition);
		
	}
	
	//Write a default entry
	public void writeDefaultEntry(long indexPosition, int fileId, long dataFilePosition) throws IOException {
		
		//Move to the position
		indexFile.seek(indexPosition);
		
		//Write the file id
		indexFile.writeInt(fileId);
		
		//Write the data file position
		indexFile.writeLong(dataFilePosition);
		
	}
	
	//Write an entry
	public void writeEntry(long sessionId, int indexFileId, long indexPosition, int collisionFileId, long collisionFilePosition) throws IOException {
		
		//Move to the position
		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		indexFile.seek(indexPosition);

		//Get old collision
		int oldCollisionFileId = indexFile.readInt();
		long oldCollisionPosition = indexFile.readLong();
		
		//Initialization
		long logPosition = logFiles.length();
		
		//Move to the end position into the log file
		logFiles.seek(logPosition);
		
		//Write the begin of the block into the log file
		logFiles.writeUTF("0;B;"+indexFileId+";"+indexPosition+";"+oldCollisionFileId+";"+oldCollisionPosition);
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;B;"+indexFileId+";"+indexPosition+";"+oldCollisionFileId+";"+oldCollisionPosition+"\n", true);

		//Move to the position
		indexFile.seek(indexPosition);
		
		//Write the file id
		indexFile.writeInt(collisionFileId);
		
		//Write the data file position
		indexFile.writeLong(collisionFilePosition);
		
	}
	
	//Increment the number of element in the index file
	public void increment() throws IOException {
		
		//Move to the begin
		indexFile.seek(0);
		
		//Get the number of nodes and add 1
		long nbNode = indexFile.readLong();
		nbNode++;

		//Move to the begin
		indexFile.seek(0);
		
		//Write the new number
		indexFile.writeLong(nbNode);
		
	}
	
	//Decrement the number of element in the index file
	public void decrement() throws IOException {
		
		//Move to the begin
		indexFile.seek(0);
		
		//Get the number of nodes and add 1
		long nbNode = indexFile.readLong();
		nbNode--;

		//Move to the begin
		indexFile.seek(0);
		
		//Write the new number
		indexFile.writeLong(nbNode);
		
	}
	
	//Get the index file id of the key
	public static int indexFileId(String key) {
		
		   return (key.hashCode() & 0x7fffffff) % Start.NB_INDEX_FILE;
		   
	}
	
	//Get the index number position of the key in a table with tableSize number of element
	public static long indexFileTabIndex(String key, long tableSize) {
		
		   return (hash2(key) & 0x7fffffff) % tableSize;
		   
	}
	
	//The hash function
	public static long hash2(String string) {
		
		long h = 1125899906842597L; // prime
		int len = string.length();

		for (int i = 0; i < len; i++) {
			h = 31*h + string.charAt(i);
		}
		
		return h;
		
	}
	
	//Get the block into the string format
	@SuppressWarnings("unchecked")
	public static String if_toString(int indexFileId, long position) throws IOException {
		
		//Move to the position
		IndexFile indexFile = IndexFilePool.if_get(indexFileId);
		indexFile.indexFile.seek(position);
		
		//Initialization
		JSONObject json = new JSONObject();
		
		//Get info
		json.put("DataFileId", indexFile.indexFile.readInt());
		json.put("DataPosition", indexFile.indexFile.readLong());
		
	    return JsonFormatter.format(json.toJSONString());
		
	}
	
	//Get the block
	public static Long[] if_toString_obj(int indexFileId, long position) throws IOException {
		
		//Move to the position
		IndexFile indexFile = IndexFilePool.if_get(indexFileId);
		indexFile.indexFile.seek(position);
		
		//Initialization
		Long[] result = {-1L, -1L};

		result[0] = indexFile.indexFile.readInt()+0L;
		result[1] = indexFile.indexFile.readLong();
		
	    return result;
		
	}

}
