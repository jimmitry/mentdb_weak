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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.BasicData;
import re.jpayet.mentdb.core.db.basic.BasicIndex;
import re.jpayet.mentdb.core.db.basic.BasicPosition;
import re.jpayet.mentdb.core.db.basic.BasicRecordPosition;
import re.jpayet.mentdb.core.db.file.data.Block;
import re.jpayet.mentdb.core.db.file.data.DataFile;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.data.UnusedFile;
import re.jpayet.mentdb.core.db.file.index.IndexFile;
import re.jpayet.mentdb.core.db.file.index.IndexFilePool;
import re.jpayet.mentdb.core.db.file.transaction.Transaction;

//Manage records
public class Record {

	public static LinkedHashMap<String, JSONObject> cache = new LinkedHashMap<String, JSONObject>();
	static public int RECORD_CACHE_SIZE = 0;
	
	public long dataFilePosition = -1;
	public String data = "";
	public int dataFileId = -1;
	
	//Constructor
	public Record(int dataFileId, long dataFilePosition, String data) {

		this.dataFileId = dataFileId;
		this.dataFilePosition = dataFilePosition;
		this.data = data;
		
	}
	
	public static JSONObject getNode(long sessionId, String key) throws Exception {
		
		JSONObject result = null;
		
		result = LockObject.read(key, sessionId);
		
		if (result!=null) {
			return result;
		}
		
		//Try to get the node
		result = cache.get(key);
		
		if (result==null) {
			
			//The node does not exist
			Record rec = Record.get_record(key);
			
			if (rec==null) {
				
				return null;
				
			} else {
			
				BasicData bd = new BasicData();
				bd.load(rec.data);
				result = bd.dataNode;
				
				//Save the node
				cache.put(key, result);
				
				//Delete the first if the size is too big
				if (cache.size()>RECORD_CACHE_SIZE) {
					
					Entry<String, JSONObject> e = cache.entrySet().iterator().next();
					
					cache.remove(e.getKey());
					
				}
			
			}
			
		} else {
			
			result = (JSONObject) new org.json.simple.parser.JSONParser().parse(result.toJSONString());
			
		}
		
		return result;
		
	}
	
	public static JSONObject getNodeWithoutLockEngine(long sessionId, String key) throws Exception {
		
		//Try to get the node
		JSONObject result = cache.get(key);
		
		if (result==null) {
			
			//The node does not exist
			Record rec = Record.get_record(key);
			
			if (rec==null) {
				
				return null;
				
			} else {
			
				BasicData bd = new BasicData();
				bd.load(rec.data);
				result = bd.dataNode;
				
				//Save the node
				cache.put(key, result);
				
				//Delete the first if the size is too big
				if (cache.size()>RECORD_CACHE_SIZE) {
					
					Entry<String, JSONObject> e = cache.entrySet().iterator().next();
					
					cache.remove(e.getKey());
					
				}
			
			}
			
		}
		
		return result;
		
	}
	
	//Add a new record
	public static BasicRecordPosition add(long sessionId, String key, String value) throws Exception {
		
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "ADD_"+key+"="+value+"\n", true);
		
		LockObject.generateException(key, sessionId);
		
		//Get the index file position to write
		int indexFileId = IndexFile.indexFileId(key);
		
		//Get the position to write into the index file
		long indexFileTabIndex = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileId).indexFile));
		
		//Try to get the record (data)
		Record record = IndexFile.existEntry(key, indexFileId, indexFileTabIndex);
		
		//Check if the key already exist
		if (record==null) {
			
			//Resize if > 75%
			if (IndexFilePool.if_get(indexFileId).isG75Percent()) {
				
				throw new Exception("Sorry, the index file '"+indexFileId+"' is full. Stop the server, update parameters in server.conf: 'EXTEND_FS_FORCE' to 1. Restart the server. The server save data... At the end the server stop, update 'EXTEND_FS_FORCE' to 2 and change 'MAX_INDEX_BLOCK_SIZE' x2 (for example 16384 to 32768). Restart the server. The server rebuild data from archive... At the end the server stop, set parameter 'EXTEND_FS_FORCE' to 0.");
			
			}

			//Get the data file to write the new record (data)
			DataFile dataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(value));

			//Write and get the position in the data file
			long recordPosition = Record.write(sessionId, key, dataFile, value);
			
			DataFile newCollisionDataFile = null;
			long newCollisionPosition = -1;

			//Check if the basic collision node does not exist
			if (!IndexFile.existCollisionNode(indexFileId, indexFileTabIndex)) {
				
				//Create the basic collision node and add the collision
				BasicIndex collisionNode = new BasicIndex();
				collisionNode.addCollision(key, dataFile.id, recordPosition);
				
				//Get the data file to write the collision node
				newCollisionDataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(collisionNode.collisionNode.toJSONString()));
				
				//Create the collision record
				newCollisionPosition = Record.write(sessionId, key, newCollisionDataFile, collisionNode.collisionNode.toJSONString());
				
			} else {
				
				//Load the JSON collision
				BasicIndex collisionNode = new BasicIndex();
				String jsonCollisionNode = IndexFile.getCollisionNode(indexFileId, indexFileTabIndex);
				
				collisionNode.load(jsonCollisionNode);

				//Get old collision
				BasicPosition oldCollision = IndexFile.getCollisionCoord(indexFileId, indexFileTabIndex);
				
				//Delete the old collision
				Record.delete(key, sessionId, DataFilePool.dfp_get(oldCollision.fileId) , oldCollision.position);
				
				//Add the new collision
				collisionNode.addCollision(key, dataFile.id, recordPosition);
				
				//Get the data file to write the collision
				newCollisionDataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(collisionNode.collisionNode.toJSONString()));
				
				//Create the collision record
				newCollisionPosition = Record.write(sessionId, key, newCollisionDataFile, collisionNode.collisionNode.toJSONString());
				
			}
			
			//Write an entry in the index file
			IndexFilePool.if_get(indexFileId).writeEntry(sessionId, indexFileId,
					indexFileTabIndex*12+8, 
					newCollisionDataFile.id, 
					newCollisionPosition);

			BasicRecordPosition result = new BasicRecordPosition(indexFileId, indexFileTabIndex*12+8, newCollisionDataFile.id, newCollisionPosition, dataFile.id, recordPosition);

			//Initialization
			RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
			long logPosition = logFiles.length();
			
			//Move to the end position into the log file
			logFiles.seek(logPosition);
			
			//Write the begin of the block into the log file
			logFiles.writeUTF("0;A;"+indexFileId+";"+IndexFile.if_current_block_size(IndexFilePool.if_get(indexFileId).indexFile));
			//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;A;"+indexFileId+";"+IndexFile.if_current_block_size(IndexFilePool.if_get(indexFileId).indexFile)+"\n", true);
			
			//Increment the file index
			IndexFilePool.if_get(indexFileId).increment();
			
			//Lock the record
			LockObject.put(key, sessionId, null);
			
			return result;
			
		} else return null;
		
	}
	
	//Remove a record
	public static BasicRecordPosition remove(long sessionId, String key) throws Exception {
		
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "REMOVE_"+key+"\n", true);
		
		JSONObject lastValue = null;
		
		if (LockObject.generateException(key, sessionId)==null) {
			
			lastValue = getNodeWithoutLockEngine(sessionId, key);
		
		}
		
		//Remove the key from the cache
		cache.remove(key);
		
		//Get the index file position
		int indexFileId = IndexFile.indexFileId(key);
		
		//Get the position into the index file tab
		long indexFileTabIndex = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileId).indexFile));
		
		//Try to get the record
		Record record = IndexFile.existEntry(key, indexFileId, indexFileTabIndex);
		
		if (record!=null) {
			
			//Remove the data node
			Record.delete(key, sessionId, DataFilePool.dfp_get(record.dataFileId), record.dataFilePosition);
			
			//Load the JSON collision
			BasicIndex biNode = new BasicIndex();
			biNode.load(IndexFile.getCollisionNode(indexFileId, indexFileTabIndex));
			
			//Get old collision
			BasicPosition oldCollision = IndexFile.getCollisionCoord(indexFileId, indexFileTabIndex);
			
			//Delete the old collision record
			Record.delete(key, sessionId, DataFilePool.dfp_get(oldCollision.fileId) , oldCollision.position);
			
			//Delete the collision
			biNode.deleteCollision(key);
			
			BasicRecordPosition result = null;
				
			if (biNode.countCollision()!=0) {
				
				//Get the data file to write the collision node
				DataFile newCollisionDataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(biNode.collisionNode.toJSONString()));
				
				//Create the collision record
				long newCollisionPosition = Record.write(sessionId, key, newCollisionDataFile, biNode.collisionNode.toJSONString());
				
				//Write an entry in the index file
				IndexFilePool.if_get(indexFileId).writeEntry(sessionId, indexFileId,
						indexFileTabIndex*12+8, 
						newCollisionDataFile.id, 
						newCollisionPosition);
				
				result = new BasicRecordPosition(indexFileId, indexFileTabIndex*12+8, newCollisionDataFile.id, newCollisionPosition, record.dataFileId, record.dataFilePosition);
				
			} else {
				
				//Clear the index entry
				IndexFilePool.if_get(indexFileId).writeEntry(sessionId, indexFileId,
						indexFileTabIndex*12+8, 
						-1, 
						-1);
				
				result = new BasicRecordPosition(indexFileId, indexFileTabIndex*12+8, -1, -1, record.dataFileId, record.dataFilePosition);
				
			}
			
			//Initialization
			RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
			long logPosition = logFiles.length();
			
			//Move to the end position into the log file
			logFiles.seek(logPosition);
			
			//Write the begin of the block into the log file
			logFiles.writeUTF("0;A;"+indexFileId+";"+IndexFile.if_current_block_size(IndexFilePool.if_get(indexFileId).indexFile));
			//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;A;"+indexFileId+";"+IndexFile.if_current_block_size(IndexFilePool.if_get(indexFileId).indexFile)+"\n", true);
			
			//Increment the file index
			IndexFilePool.if_get(indexFileId).decrement();
			
			//Lock the record
			if (lastValue!=null) {
				
				LockObject.put(key, sessionId, lastValue);
			
			}
			
			return result;
			
		} else return null;
		
	}
	
	//Update a record
	public static BasicRecordPosition update(long sessionId, String key, String value) throws Exception {
		
		//Remove the record
		Record.remove(sessionId, key);
		
		//Create the updated record
		BasicRecordPosition r = Record.add(sessionId, key, value);
		
		return r;
		
	}
	
	//Get a record
	public static Record get_record(String key) throws Exception {
		
		//Get the index file position
		int indexFileToWrite = IndexFile.indexFileId(key);
		
		//Get the position into the index file tab
		long indexFileTabPosition = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileToWrite).indexFile));

		//Try to get the data
		Record r = IndexFile.existEntry(key, indexFileToWrite, indexFileTabPosition);

		return r;
		
	}
	
	//Recreate a new record
	public static void recreate(long sessionId, String key, String value) throws Exception {
				
		//Get the index file position to write
		int indexFileId = IndexFile.indexFileId(key);
			
		//Get the position to write into the index file
		long indexFileTabIndex = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileId).indexFile));

		//Try to get the record (data)
		Record record = IndexFile.existEntry(key, indexFileId, indexFileTabIndex);

		//Check if the key already exist
		if (record==null) {

			//Get the data file to write the new record (data)
			DataFile dataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(value));

			//Write and get the position in the data file
			long recordPosition = Record.write(sessionId, key, dataFile, value);
			
			DataFile newCollisionDataFile = null;
			long newCollisionPosition = -1;

			//Check if the basic collision node does not exist
			if (!IndexFile.existCollisionNode(indexFileId, indexFileTabIndex)) {

				//Create the basic collision node and add the collision
				BasicIndex collisionNode = new BasicIndex();
				collisionNode.addCollision(key, dataFile.id, recordPosition);

				//Get the data file to write the collision node
				newCollisionDataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(collisionNode.collisionNode.toJSONString()));
				
				//Create the collision record
				newCollisionPosition = Record.write(sessionId, key, newCollisionDataFile, collisionNode.collisionNode.toJSONString());
				
			} else {

				//Load the JSON collision
				BasicIndex collisionNode = new BasicIndex();
				collisionNode.load(IndexFile.getCollisionNode(indexFileId, indexFileTabIndex));
				
				//Get old collision
				BasicPosition oldCollision = IndexFile.getCollisionCoord(indexFileId, indexFileTabIndex);
				
				//Delete the old collision
				Record.delete(key, sessionId, DataFilePool.dfp_get(oldCollision.fileId) , oldCollision.position);
				
				//Add the new collision
				collisionNode.addCollision(key, dataFile.id, recordPosition);

				//Get the data file to write the collision
				newCollisionDataFile = DataFilePool.getDataFileToWrite(Record.nbBlockToWrite(collisionNode.collisionNode.toJSONString()));
				
				//Create the collision record
				newCollisionPosition = Record.write(sessionId, key, newCollisionDataFile, collisionNode.collisionNode.toJSONString());
				
			}
			
			//Write an entry in the index file
			IndexFilePool.if_get(indexFileId).writeEntry(sessionId, indexFileId,
					indexFileTabIndex*12+8, 
					newCollisionDataFile.id, 
					newCollisionPosition);
			
		};
		
	}
	
	//Get a record details
	@SuppressWarnings("unchecked")
	public static JSONObject getDetail(String key) throws Exception {
				
		//Initialization
		JSONObject coord = new JSONObject();
		
		//Get the index file position
		int indexFileToWrite = IndexFile.indexFileId(key);
		coord.put("key", key);
		coord.put("indexFileToWrite", indexFileToWrite);
		
		//Get the position into the index file tab
		long indexFileTabPosition = IndexFile.indexFileTabIndex(key, IndexFile.if_current_max_block_size(IndexFilePool.if_get(indexFileToWrite).indexFile));
		coord.put("indexFileTabPosition", indexFileTabPosition);
		
		Record rec = IndexFile.existEntry(key, indexFileToWrite, indexFileTabPosition);
		
		if (rec == null) {
			
			throw new Exception("Sorry, the key "+key+" does not exist.");
			
		}
		
		coord.put("dataFileId", rec.dataFileId);
		coord.put("dataFilePosition", rec.dataFilePosition);
		coord.put("data", rec.data);
		coord.put("size", rec.data.length());
		
		return coord;
		
	}
	
	//Delete a record
	public static void delete(String key, long sessionId, DataFile dataFile, long position) throws Exception {
		
		//Get the block
		Block block = Block.read_block(dataFile, position);
		
		//Check if there is another block and delete it
		if (block.nextBlockAddress!=-1) {
			
			delete(key, sessionId, dataFile, block.nextBlockAddress);
			
		}
		
		//Delete the block
		Block.do_delete(key, sessionId, dataFile, position);
		
	}
	
	//Count the number of block to write
	public static int nbBlockToWrite(String recValue) throws IOException {
		
	    return (int) Math.ceil(recValue.length()/DataFile.BLOCK_SIZE)+1;
	    
	}
	
	//Write a record
	public static long write(long sessionId, String key, DataFile dataFile, String recValue) throws IOException {
		
		return writeB64(sessionId, key, dataFile, Block.encodeb64(recValue));
		
	}
	
	//Write a record
	public static long writeB64(long sessionId, String key, DataFile dataFile, String recValue) throws IOException {
		
		//Initialization
		int nb = recValue.length();
		double nb_block = Math.ceil((nb+0.0)/DataFile.BLOCK_SIZE);
		
		long fisrt_block = UnusedFile.getPosition(sessionId, dataFile);
		long position = fisrt_block;
		
		int j = 1;
		
		//Create all blocks for the record
	    for (int i = 0; i < nb; i += DataFile.BLOCK_SIZE) {
	    	
		    	//Check if this is the last block
		    	if (j==nb_block) {
		    		
		    		Block.do_write(sessionId, key, dataFile, position, new Block(recValue.substring(i, Math.min(nb, i + DataFile.BLOCK_SIZE))), -1);

		    	} else {
		    		
		    		long next_position = UnusedFile.getPosition(sessionId, dataFile);

		    		Block.do_write(sessionId, key, dataFile, position, new Block(recValue.substring(i, Math.min(nb, i + DataFile.BLOCK_SIZE))), next_position);
			    	
			    	position = next_position;
			    	
			    	j++;

		    	}
				
	    }
	    
	    return fisrt_block;
		
	}
	
	//Write a record
	public static long writeB64_old(long sessionId, String key, DataFile dataFile, String recValue) throws IOException {
		
		//Initialization
		List<Block> blocks = new ArrayList<Block>();
		List<Long> blocksPosition = new ArrayList<Long>();
		int nb = recValue.length();
		
		//Create all blocks for the record
	    for (int i = 0; i < nb; i += DataFile.BLOCK_SIZE) {
	    	
		    	//Create the block
		    	Block block = new Block(recValue.substring(i, Math.min(nb, i + DataFile.BLOCK_SIZE)));
		    
		    	//Save the block
		    	blocks.add(block);
		   
		    	//Get and save a position
		    	blocksPosition.add(UnusedFile.getPosition(sessionId, dataFile));
		    
	    }
		
	    //Write all blocks in the record
	    for (int i = 0; i < blocks.size(); i ++) {
	    	
		    	long position = blocksPosition.get(i);
			
		    	//Check if this is the last block
		    	if (i+1 == blocks.size()) {
		    		
		    		Block.do_write(sessionId, key, dataFile, position, blocks.get(i), -1);
		    		
		    	} else {
	
			    	long positionP1 = blocksPosition.get(i+1);
	
		    		Block.do_write(sessionId, key, dataFile, position, blocks.get(i), positionP1);
		    		
		    	}
	    	
	    }
		
	    return blocksPosition.get(0);
		
	}
	
	//Get the data into the string format
	public static String getData(DataFile dataFile, long position) throws Exception {
		
		//Initialization
		
		Block block = Block.read_block(dataFile, position);
		
		StringBuilder result = new StringBuilder();
		result.append(block.getData());
		
		if (block.nextBlockAddress!=-1) {
			
			getSubData(dataFile, block.nextBlockAddress, result);
			
		}
		
	    return result.toString();
		
	}
	
	//Get the data into the string format
	public static void getSubData(DataFile dataFile, long position, StringBuilder result) throws Exception {
		
		//Initialization
		
		Block block = Block.read_block(dataFile, position);
		
		result.append(block.getData());
		
		if (block.nextBlockAddress!=-1) {
			
			getSubData(dataFile, block.nextBlockAddress, result);
			
		}
		
	}
	
	//Read a record
	@SuppressWarnings("unchecked")
	public static String read(DataFile dataFile, long nb, long position, JSONArray array) throws Exception {
		
		//Initialization
		Block block = Block.read_block(dataFile, position);
		String recString = block.dataStr.substring(0, block.dataStrSize);
		
		JSONObject json = new JSONObject();
		json.put("blockId", nb);
		json.put("position", position);
		BasicData bd = new BasicData();
		bd.load(block.toString());
		json.put("block", bd.dataNode);
		array.add(json);
		
		
		if (block.nextBlockAddress!=-1) {
			
			recString += read(dataFile, nb+1, block.nextBlockAddress, array);
			
		}
		
		return recString;
		
	}

}
