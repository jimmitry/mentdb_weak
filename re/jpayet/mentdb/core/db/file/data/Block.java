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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.file.transaction.Transaction;
import re.jpayet.mentdb.ext.json.JsonFormatter;
import re.jpayet.mentdb.ext.tools.Misc;

//Block management
public class Block {
	
	//Initialization
	public long nextBlockAddress = -1; //Address to the next block in the record
	public String dataStr = ""; //The data
	public int dataStrSize = 0; //The size of the data in the block

	static Encoder b64Encoder = Base64.getEncoder();
	static Decoder b64Decoder = Base64.getDecoder();
	
	//Constructor
	public Block() {
		
	}
	
	//Constructor
	public Block(String dataStr) {

		int nb = dataStr.length();
		this.dataStr = dataStr + DataFile.EMPTY_BLOCK.substring(nb);
		this.dataStrSize = nb;
		
	}
	
	//Delete a block (without log file)
	public static void delete(int dataFileId, long position) throws IOException {
		
		//Initialization
		RandomAccessFile dataFile = DataFilePool.dfp_get(dataFileId).dataFile;
		
		//Move to the position into the data file
		dataFile.seek(position);
		
		//Reset all values
		dataFile.write((Misc.rpad_long("-1")+DataFile.EMPTY_BLOCK+Misc.rpad_smallint("0")).getBytes());
		
	}
	
	//Rewrite a block (without log file)
	public static void rewrite(int dataFileId, long position, long nextBlockAddress, 
			String dataStr, int dataStrSize) throws IOException {
		
		//Initialization
		RandomAccessFile dataFile = DataFilePool.dfp_get(dataFileId).dataFile;
		
		//Move to the position into the data file
		dataFile.seek(position);
		
		//Reset all values
		dataFile.write((Misc.rpad_long(nextBlockAddress+"")+dataStr+Misc.rpad_smallint(dataStrSize+"")).getBytes());
		
	}
	
	//Delete a block (with log file)
	public static void do_delete(String key, long sessionId, DataFile dataFile, long position) throws IOException {

		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		RandomAccessFile df = dataFile.dataFile;
		RandomAccessFile uf = dataFile.unusedFile.unusedFile;
		
		//Initialization
		long logPosition = logFiles.length();
		
		//Move to the end position into the log file
		logFiles.seek(logPosition);
		
		//Move to the position into the data file
		df.seek(position);
		
		//Read and save all values into the log file
		byte[] b = new byte[DataFile.BLOCK_SIZE_ADD_TO_BLOCK];
		df.read(b);
		String data = new String(b);
		String rec0 = data.substring(0, 19);
		String rec1 = data.substring(19, 19+DataFile.BLOCK_SIZE);
		String rec2 = data.substring(19+DataFile.BLOCK_SIZE, 19+DataFile.BLOCK_SIZE+5);
		logFiles.writeUTF("0;C;"+dataFile.id+";"+position+";"+rec0.replace(" ", "")+";"+rec1+";"+rec2.replace(" ", ""));
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;C;"+dataFile.id+";"+position+";"+rec0.replace(" ", "")+";"+StringFx.decode_b64(rec1.replace(" ", ""))+";"+rec2.replace(" ", "")+"\n", false);
		
		//Move to the position into the data file
		//df.seek(position);
		
		//Reset all values
		//df.write((Misc.rpad_long("-1")+DataFile.EMPTY_BLOCK+Misc.rpad_smallint("0")).getBytes());
		
		//Initialization
		//logPosition = logFiles.length();
		
		//Move to the end position into the log file
		//logFiles.seek(logPosition);
		
		//Write the log type
		logFiles.writeUTF("0;G;"+dataFile.id+";"+uf.length());
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;G;"+dataFile.id+";"+uf.length()+"\n", true);
		
		//Mark as unused block
		uf.seek(uf.length());
		uf.writeLong(position);
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "ADD<"+key+">"+(uf.length()/8)+":"+position+"\n", true);
		
	}
	
	//Write a block (with log file)
	public static void do_write(long sessionId, String key, DataFile dataFile, long position, Block block, long nextBlockAddress) throws IOException {
		
		//Initialization
		RandomAccessFile logFiles = Transaction.logFiles.get(sessionId);
		RandomAccessFile df = dataFile.dataFile;
		
		long logPosition = logFiles.length();
		
		//Move to the end position into the log file
		logFiles.seek(logPosition);
		
		//Write the log type
		logFiles.writeUTF("0;D;"+dataFile.id+";"+position);
		//Misc.append("/Users/jimmitry/Desktop/exe.log", "0;D;"+dataFile.id+";"+position+"\n", true);
		
		//Move to the position
		df.seek(position);
		
		df.write((Misc.rpad_long(nextBlockAddress+"")+block.dataStr+Misc.rpad_smallint(block.dataStrSize+"")).getBytes());
		
	}
	
	//Encode a string into base 64 format
	public static String encodeb64(String value) {
		
		return new String(b64Encoder.encode(value.getBytes()));
		
	}
	
	//Decode a string from base 64 format
	public static String decodeb64(String value) {
		
		return new String(b64Decoder.decode(value.getBytes()));
		
	}
	
	//Read a block
	public static Block read_block(DataFile dataFile, long position) throws IOException {
		
		//Initialization
		Block block = new Block();
		RandomAccessFile df = dataFile.dataFile;
		
		//Move to the position
		df.seek(position);
		byte[] b = new byte[DataFile.BLOCK_SIZE_ADD_TO_BLOCK];
		df.read(b);
		String data = new String(b);
		String rec0 = data.substring(0, 19);
		String rec1 = data.substring(19, 19+DataFile.BLOCK_SIZE);
		String rec2 = data.substring(19+DataFile.BLOCK_SIZE, 19+DataFile.BLOCK_SIZE+5);
		
		block.nextBlockAddress = Long.parseLong(rec0.replace(" ", ""));
		
		block.dataStr = rec1;
		
		block.dataStrSize = Integer.parseInt(rec2.replace(" ", ""));
		
		return block;
		
	}
	
	//Get the block into the string format
	@SuppressWarnings("unchecked")
	public String toString() {
		
		//Initialization
		JSONObject json = new JSONObject();
		
		//Get info
		json.put("nextBlockAddress", nextBlockAddress);
		json.put("data", dataStr.substring(0, dataStrSize));
		
	    return JsonFormatter.format(json.toJSONString());
		
	}
	
	//Get the data into the string format
	public String getData() {
		
	    return dataStr.substring(0, dataStrSize);
		
	}
	
}
