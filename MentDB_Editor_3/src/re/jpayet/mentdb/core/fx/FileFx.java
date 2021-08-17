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

package re.jpayet.mentdb.core.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.apache.commons.net.util.Base64;

//The type class
public class FileFx {

	public static void reader_close(FileInputStream fi) throws Exception {

		//Try to close
		try {
			
			//Close the binary file reader
			fi.close();

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static FileInputStream reader_open(String filePath) throws Exception {
		
		try {
			
			//Create the file input stream
			return new FileInputStream(filePath);

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String reader_get_bytes(FileInputStream fi, String nbBytes) throws Exception {

		//Try to get data
		try {

			//Get data from file
			byte[] bytes = new byte[Integer.parseInt(nbBytes)];
			int state = fi.read(bytes);

			//Return the hex code ...
			if (state!=-1) {

				if (Integer.parseInt(nbBytes)==state) return new String(Base64.encodeBase64(bytes));
				else {

					return new String(Base64.encodeBase64(Arrays.copyOf(bytes, state)));

				}

			} else return null;

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static void writer_close(FileOutputStream fo) throws Exception {

		//Try to close
		try {
			
			//Close the binary file writer
			fo.close();

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static FileOutputStream writer_open(String filePath) throws Exception {
		
		//Initialization
		File file = new File(filePath);

		try {
			
			//Overwrite the file
			file.createNewFile();
			return new FileOutputStream(file, false);

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static void writer_flush(FileOutputStream fo) throws Exception {

		//Try to flush
		try {
			
			fo.flush();

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static void writer_add_bytes(FileOutputStream fo, String data) throws Exception {
		
		//Get byte file from hex string flow
		byte[] b = Base64.decodeBase64(data);
		
		//Add data in file
		fo.write(b);
	};
	
	
}
