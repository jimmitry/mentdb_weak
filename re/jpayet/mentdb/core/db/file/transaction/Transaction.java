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

package re.jpayet.mentdb.core.db.file.transaction;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.LockObject;
import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.file.data.Block;
import re.jpayet.mentdb.core.db.file.data.DataFilePool;
import re.jpayet.mentdb.core.db.file.data.UnusedFile;
import re.jpayet.mentdb.core.db.file.index.IndexFile;
import re.jpayet.mentdb.core.db.file.index.IndexFilePool;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.tools.Misc;

//Manage transactions
public class Transaction {
	
	//Log file
	public static HashMap<Long, RandomAccessFile> logFiles = new HashMap<Long, RandomAccessFile>();
	
	/*
	 * A : TO_OVERWRITE_COUNTER_INDEX
	 * B : TO_SET_OLD_INDEX_BLOCK
	 * C : TO_REWRITE_BLOCK
	 * D : TO_DELETE_BLOCK
	 * E : TO_REWRITE_UNUSED
	 * F : TO_REWRITE_NEW_UNUSED
	 * G : TO_DELETE_UNUSED
	 */
	
	//Roll back the transaction
	public static void rollback(long sessionId) throws IOException {
		
		//Initialization
		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		Vector<Long> logList = new Vector<Long>();
		
		RandomAccessFile logFilesNewUnused = new RandomAccessFile("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback_new.log", "rw");
		
		//Main rollback (A, B, C, D, E, F, G)
		
		//Move to the start of the file
		logFiles.seek(0);
		//Parse the file
		while (logFiles.getFilePointer()!=logFiles.length()) {
			
			//Save the position of the block
			logList.add(logFiles.getFilePointer());
			
			//Read the record
			logFiles.readUTF();
			
		}
		
		rollback_execute_ABCDEFG(logFiles, logFilesNewUnused, logList);
		
		//H rollback only (save NEW (F) unused block)
		
		logList = new Vector<Long>();
		
		//Move to the start of the file
		logFilesNewUnused.seek(0);
		//Parse the file
		while (logFilesNewUnused.getFilePointer()!=logFilesNewUnused.length()) {
			
			//Save the position of the block
			logList.add(logFilesNewUnused.getFilePointer());

			//Read the record
			logFilesNewUnused.readUTF();
			
		}
		
		rollback_execute_H(logFilesNewUnused, logList);
		
		//Close the file
		logFilesNewUnused.close();
		try {
			FileFx.delete("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback_new.log");
		} catch (Exception e) {}
		
		//Clear lock
		LockObject.clearLocker(sessionId);
		
		Record.cache = new LinkedHashMap<String, JSONObject>();
		
	}
	
	//Roll back the transaction
	public static void rollback_execute_ABCDEFG(RandomAccessFile logFiles, RandomAccessFile logFilesNewUnused, Vector<Long> logList) throws IOException {
		
		//Parse all blocks into the log file by the end
		for(int i=(logList.size()-1);i!=-1;i--) {
			
			//Get the position of the block
			long position = logList.get(i);
			
			//Move to the position into the log file
			logFiles.seek(position);
			
			String row = logFiles.readUTF();
			//Misc.append("/Users/jimmitry/Desktop/exe.log", row+"\n", true);
			
			//Read the record
			String[] rec = Misc.atom_tab(row);
			
			//Initialization
			int dataFileId = -1, indexFileId = -1; //Data and Index file id
			long counter = -1; //Block position
			long blockPosition = -1; //Block position
			long unusedPosition = -1; //Unused file length
			long nextBlockAddress = -1; //Address to the next block in the record
			String dataStr = ""; //The data
			int dataStrSize = 0; //The size of the data in the block
			UnusedFile uf = null;
			String blockType = "";
			long savePosition = -1;
			boolean writeBlock = false;
			IndexFile indexFile = null;

			//Try to read the type of the block (else delete the block)
			try {
				
				//Read the type of the block
				blockType = rec[1];

				switch (blockType) {

				case "A": 
					
					//Read all data
					indexFileId = Integer.parseInt(rec[2]); //Index file id
					counter = Long.parseLong(rec[3]); //Counter
					
					//Move to the position
					indexFile = IndexFilePool.if_get(indexFileId);
					indexFile.indexFile.seek(0);
					
					//Write the counter
					indexFile.indexFile.writeLong(counter);

					//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;TRANSACTION_A;"+indexFileId+";"+counter+"\n", false);
				
				break;

				case "B": 
					
					//Read all data
					indexFileId = Integer.parseInt(rec[2]); //Index file id
					long indexPosition = Long.parseLong(rec[3]); //Index position
					dataFileId = Integer.parseInt(rec[4]); //Data file id
					blockPosition = Long.parseLong(rec[5]); //Block position
					
					//Move to the position
					indexFile = IndexFilePool.if_get(indexFileId);
					indexFile.indexFile.seek(indexPosition);
					
					//Write the file id
					indexFile.indexFile.writeInt(dataFileId);
					
					//Write the data file position
					indexFile.indexFile.writeLong(blockPosition);
					

					//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;TRANSACTION_B;"+indexFileId+";"+indexPosition+";"+dataFileId+";"+blockPosition+"\n", false);
					
				break;

				case "C": 
					
					//Read all data into the block
					dataFileId = Integer.parseInt(rec[2]); //Data file id
					blockPosition = Long.parseLong(rec[3]); //Block position
					nextBlockAddress = Long.parseLong(rec[4]); //Next block address
					dataStr = rec[5]; //Data
					dataStrSize = Integer.parseInt(rec[6]); //Data size
					
					//Rewrite the block
					Block.rewrite(dataFileId, blockPosition, nextBlockAddress, dataStr, dataStrSize);
					
					//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;TRANSACTION_C;"+dataFileId+";"+blockPosition+";"+nextBlockAddress+";"+dataStr+";"+dataStrSize+"\n", false);
					
				break;

				case "D":
					
					//Read all data
					dataFileId = Integer.parseInt(rec[2]); //Data file id
					blockPosition = Long.parseLong(rec[3]); //Block position
					
					//Delete the block
					Block.delete(dataFileId, blockPosition);

					//Misc.append("/Users/jimmitry/Desktop/rollbak.log", uf.unusedFile.length()+"____0;D;"+dataFileId+";"+blockPosition+"\n", false);
				
				break;

				case "E": 
					
					//Read all data
					dataFileId = Integer.parseInt(rec[2]); //Data file id
					blockPosition = Long.parseLong(rec[3]); //Block position
					
					uf = DataFilePool.dfp_get(dataFileId).unusedFile;

					savePosition = uf.unusedFile.length()-8;
					writeBlock = false;
					if (savePosition>=0) {
						
						//Check if the last is different
						uf.unusedFile.seek(savePosition);
						if (uf.unusedFile.readLong()!=blockPosition) {
							
							writeBlock = true;
							
						}
						
					} else writeBlock = true;
					
					if (writeBlock) {

						uf.unusedFile.seek(uf.unusedFile.length());
						uf.unusedFile.writeLong(blockPosition);
						//Misc.append("/Users/jimmitry/Desktop/exe.log", "TRANSACTION_ADD<E<"+(uf.unusedFile.length()/8)+":"+blockPosition+"\n", false);
						
						Block.delete(dataFileId, blockPosition);
						
					}
					
					//Misc.append("/Users/jimmitry/Desktop/rollbak.log", "0;E;"+dataFileId+";"+blockPosition+"\n", false);
				
				break;

				case "F": 
					
					//Read all data
					dataFileId = Integer.parseInt(rec[2]); //Data file id
					blockPosition = Long.parseLong(rec[3]); //Block position
					
					logFilesNewUnused.seek(logFilesNewUnused.length());
					
					logFilesNewUnused.writeUTF("0;H;"+dataFileId+";"+blockPosition);
					
					//Misc.append("/Users/jimmitry/Desktop/rollbak.log", "0;F;"+dataFileId+";"+blockPosition+"\n", false);
				
				break;

				case "G": 
					
					//Read all data
					dataFileId = Integer.parseInt(rec[2]); //Data file id
					unusedPosition = Long.parseLong(rec[3]); //Unused file length

					//Delete the unused block
					DataFilePool.dfp_get(dataFileId).unusedFile.unusedFile.setLength(unusedPosition);

					//Misc.append("/Users/jimmitry/Desktop/exe.log", "TRANSACTION_DELETE>G>"+(DataFilePool.dfp_get(dataFileId).unusedFile.unusedFile.length()/8)+"\n", false);
					
					//Misc.append("/Users/jimmitry/Desktop/rollbak.log", unusedPosition+"===unusedFile.length="+DataFilePool.dfp_get(dataFileId).unusedFile.unusedFile.length()+"\n", false);
				
				break;
				
				}
				
			} catch (Exception e) {
				
				System.out.println("Rollback message ["+i+":"+blockType+"] > "+e.getMessage());
				
			} finally {
				
				//Delete the block
				logFiles.setLength(position);
				
			}
			
		}
		
	}
	
	//Roll back the transaction
	public static void rollback_execute_H(RandomAccessFile logFilesNewUnused, Vector<Long> logList) throws IOException {
		
		//Parse all blocks into the log file by the end
		for(int i=(logList.size()-1);i!=-1;i--) {
			
			//Get the position of the block
			long position = logList.get(i);
			
			//Move to the position into the log file
			logFilesNewUnused.seek(position);
			
			String row = logFilesNewUnused.readUTF();
			//Misc.append("/Users/jimmitry/Desktop/exe.log", row+"\n", true);
			
			//Read the record
			String[] rec = Misc.atom_tab(row);
			
			//Initialization
			int dataFileId = -1; //Data file id
			long blockPosition = -1; //Block position
			UnusedFile uf = null;
			String blockType = "";
			long savePosition = -1;
			boolean writeBlock = false;

			//Try to read the type of the block (else delete the block)
			try {
				
				//Read the type of the block
				blockType = rec[1];

				//Read all data
				dataFileId = Integer.parseInt(rec[2]); //Data file id
				blockPosition = Long.parseLong(rec[3]); //Block position
				
				uf = DataFilePool.dfp_get(dataFileId).unusedFile;

				savePosition = uf.unusedFile.length()-8;
				writeBlock = false;
				if (savePosition>=0) {
					
					//Check if the last is different
					uf.unusedFile.seek(savePosition);
					if (uf.unusedFile.readLong()!=blockPosition) {
						
						writeBlock = true;
						
					}
					
				} else writeBlock = true;
				
				if (writeBlock) {

					uf.unusedFile.seek(uf.unusedFile.length());
					uf.unusedFile.writeLong(blockPosition);
					//Misc.append("/Users/jimmitry/Desktop/exe.log", "TRANSACTION_ADD<E<"+(uf.unusedFile.length()/8)+":"+blockPosition+"\n", false);
					
					Block.delete(dataFileId, blockPosition);
					
				}
				
				//Misc.append("/Users/jimmitry/Desktop/rollbak.log", "0;F;"+dataFileId+";"+blockPosition+"\n", false);
				
			} catch (Exception e) {
				
				System.out.println("Rollback message ["+i+":"+blockType+"] > "+e.getMessage());
				
			} finally {
				
				//Delete the block
				logFilesNewUnused.setLength(position);
				
			}
			
		}
		
	}
	
	//Log list
	@SuppressWarnings("unchecked")
	public static JSONArray logList(long sessionId, int limit) throws IOException {
		
		//Initialization
		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		Vector<Long> logList = new Vector<Long>();
		JSONArray resultJson = new JSONArray();
		String result = "";
		int ii = 0;
		
		//Move to the start of the file
		logFiles.seek(0);
		
		//Parse the file
		while (logFiles.getFilePointer()!=logFiles.length()) {
			
			//Save the position of the block
			logList.add(logFiles.getFilePointer());
			
			//Read the record
			logFiles.readUTF();
			
		}
		
		//Parse all blocks into the log file by the end
		for(int i=(logList.size()-1);i!=-1 && limit>0;i--) {
			
			//Get the position of the block
			long position = logList.get(i);
			
			limit--;
			ii++;
			
			//Move to the position into the log file
			logFiles.seek(position);
			
			//Read the record
			String[] rec = Misc.atom_tab(logFiles.readUTF());
			
			//Try to read the type of the block
			try {
				
				//Read the type of the block
				String blockType = rec[1];
				
				/*
				 * A : TO_OVERWRITE_COUNTER_INDEX
				 * B : TO_SET_OLD_INDEX_BLOCK
				 * C : TO_REWRITE_BLOCK
				 * D : TO_DELETE_BLOCK
				 * E : TO_REWRITE_UNUSED
				 * F : TO_REWRITE_NEW_UNUSED
				 * G : TO_DELETE_UNUSED
				 */
				
				switch (blockType) {

				case "A": 

					//Read all data
					result = "";
					result += ii+"\tTO_OVERWRITE_COUNTER_INDEX";
					result += "\tIndexFileId:"+Integer.parseInt(rec[2]); //Index file id
					result += "\tCounter:"+Long.parseLong(rec[3]); //Counter
					resultJson.add(result);
				
				break;

				case "B": 

					//Read all data
					result = "";
					result += ii+"\tTO_SET_OLD_INDEX_BLOCK";
					result += "\tIndexFileId:"+Integer.parseInt(rec[2]); //Index file id
					result += "\tIndexPosition:"+Long.parseLong(rec[3]); //Index position
					result += "\tDataFileId:"+Integer.parseInt(rec[4]); //Data file id
					result += "\tDataPosition:"+Long.parseLong(rec[5]); //Data position
					resultJson.add(result);
				
				break;

				case "C": 

					//Read all data
					result = "";
					result += ii+"\tTO_REWRITE_BLOCK";
					result += "\tDataFileId:"+Integer.parseInt(rec[2]); //Data file id
					result += "\tPosition:"+Long.parseLong(rec[3]); //Block position
					result += "\tNextPosition:"+Long.parseLong(rec[4]); //Next block address
					result += "\tData:"+rec[5]; //Data
					result += "\tSize:"+Integer.parseInt(rec[6]); //Data size
					resultJson.add(result);
				
				break;

				case "D": 
				
					//Read all data
					result = "";
					result += ii+"\tTO_DELETE_BLOCK";
					result += "\tDataFileId:"+Integer.parseInt(rec[2]); //Data file id
					result += "\tPosition:"+Long.parseLong(rec[3]); //Block position
					resultJson.add(result);
				
				break;

				case "E": 
				
					//Read all data
					result = "";
					result += ii+"\tTO_REWRITE_UNUSED";
					result += "\tDataFileId:"+Integer.parseInt(rec[2]); //Data file id
					result += "\tPosition:"+Long.parseLong(rec[3]); //Block position
					resultJson.add(result);
				
				break;

				case "F": 

					//Read all data
					result = "";
					result += ii+"\tTO_REWRITE_NEW_UNUSED";
					result += "\tDataFileId:"+Integer.parseInt(rec[2]); //Data file id
					result += "\tPosition:"+Long.parseLong(rec[3]); //Block position
					resultJson.add(result);
				
				break;

				case "G": 
				
					//Read all data
					result = "";
					result += ii+"\tTO_DELETE_UNUSED";
					result += "\tDataFileId:"+Integer.parseInt(rec[2]); //Data file id
					result += "\tUnusedLength:"+Long.parseLong(rec[3]); //Unused file length
					resultJson.add(result);
				
				break;
				
				}
				
			} catch (Exception e) {
				
				
			}
			
		}
		
		if (!result.equals("")) {
			
			result = result.substring(1);
			
		}
		
		return resultJson;
		
	}
	
	//Commit the transaction
	public static void commit(long sessionId) throws IOException {
		
		//Set the length of the file
		Transaction.logFiles.get(sessionId).setLength(0);
		
		try {
			FileFx.delete("data"+File.separator+"transaction"+File.separator+sessionId+File.separator+"rollback_new.log");
		} catch (Exception e) {}
		
		//Clear lock
		LockObject.clearLocker(sessionId);
		
	}

}
