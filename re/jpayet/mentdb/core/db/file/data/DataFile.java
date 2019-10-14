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

//Manage the data file
public class DataFile {
	
	public static int BLOCK_SIZE = 0;
	public static String EMPTY_BLOCK = "";
	public static int ADD_TO_BLOCK = 24;//+2;+2bytes+length(65500)+length(9,223,372,036,854,775,807)=26
	public static long MAX_BLOCK = 0;
	public static int BLOCK_SIZE_ADD_TO_BLOCK = 0;
	
	//Local properties
	public String fileName = "";
	public int id = 0;
	public RandomAccessFile dataFile = null;
	public UnusedFile unusedFile = null;
	
	//Constructor
	public DataFile(int id, String fileName) {

		this.id = id;
		this.fileName = fileName;
		
	}
	
	//Get the max block size of the data file
	public static long df_current_max_block_size(RandomAccessFile dataFile) throws IOException {
		
		//Initialization
		long nb = (dataFile.length()/(BLOCK_SIZE_ADD_TO_BLOCK));
		
		//return the current max size
		if (nb<DataFile.MAX_BLOCK) return DataFile.MAX_BLOCK;
		else return nb;
		
	}
	
	//Get the size of the data file
	public static long df_get_size(RandomAccessFile dataFile) throws IOException {
		
		return dataFile.length();
		
	}
	
	//Get the current block size of the data file
	public static long df_current_block_size(RandomAccessFile dataFile) throws IOException {
		
		//return the current size
		return (dataFile.length()/(BLOCK_SIZE_ADD_TO_BLOCK));
		
	}
	
	//Get the percent of used of the data file
	public static int df_percent_used(RandomAccessFile dataFile) throws Exception {
		
		//return the current max size
		return (int) (((DataFile.df_current_block_size(dataFile))*100/DataFile.df_current_max_block_size(dataFile)));
		
	}
	
	//Get the per30 of used of the data file
	public static String df_per30_used(RandomAccessFile dataFile) throws Exception {
		
		//Initialization
		int per30 = (int) (((DataFile.df_current_block_size(dataFile)*30)/DataFile.df_current_max_block_size(dataFile)));
		
		//return the per20 in string format
		return new String(new char[per30]).replace('\0', '|')+
				new String(new char[30-per30]).replace('\0', '-');
		
	}
	
	//Open the data file
	public void open() throws FileNotFoundException {
		
		//Create the data/data/id folder if does not exist
		if (!(new File("data"+File.separator+"data"+File.separator+id).exists())) {
			
			(new File("data"+File.separator+"data"+File.separator+id)).mkdir();
			
		}

		//Open the data file
		dataFile = new RandomAccessFile(fileName, "rw");
		unusedFile = new UnusedFile(id);
		unusedFile.open();
		
	}
	
	//Close the data file
	public void close() throws IOException {

		//Close the data file
		dataFile.close();
		unusedFile.close();
		
	}
	
	public static void search_in_file(int dataFileId, String text_to_find, int depth) throws IOException {
		
		DataFile df = DataFilePool.dfp_get(dataFileId);
		
		long z = 0;
		long position = 0;
		System.out.println("\n#########################################START SEARCH");
		long nbBlock = (df.dataFile.length()/(BLOCK_SIZE_ADD_TO_BLOCK));
		int[] found_depth = {0};
		
		while (df.dataFile.getFilePointer()<df.dataFile.length()) {
			
			Block bl = Block.read_block(df, position);
			long TMP_position = df.dataFile.getFilePointer();
			
			String str = Block.decodeb64(bl.dataStr.substring(0, bl.dataStrSize));
			
			if (str.indexOf(text_to_find)>-1) {
				
				System.out.println("\n\n##########NextBlock pos = "+bl.nextBlockAddress);
				System.out.println("##CurrBlock pos = "+position+"; b="+(z+1)+"; data = "+str);

				found_depth[0]++;
				if (bl.nextBlockAddress!=-1 && found_depth[0]<=depth) {
					
					read_next_blocks(df, bl.nextBlockAddress, found_depth, depth);
					
				}
				
			};
			
			position = TMP_position;
			
			z++;
			System.out.print("> current block = "+z+"/"+nbBlock+"                                \r");
			
			
		}
		
		System.out.println("\n#########################################END SEARCH");
		
	}
	
	public static void read_next_blocks(DataFile df, long position, int[] found_depth, int depth) throws IOException {
		
		Block bl = Block.read_block(df, position);
		
		String str = Block.decodeb64(bl.dataStr.substring(0, bl.dataStrSize));
		
		System.out.println("\n\nSUB##NextBlock pos = "+bl.nextBlockAddress);
		System.out.println("SUB##CurrBlock pos = "+position+"; data = "+str);
		
		found_depth[0]++;
		if (bl.nextBlockAddress!=-1 && found_depth[0]<=depth) {
			
			read_next_blocks(df, bl.nextBlockAddress, found_depth, depth);
			
		}
		
	}

}
