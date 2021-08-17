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

package re.jpayet.mentdb.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import difflib.Chunk;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.InsertDelta;
import re.jpayet.mentdb.editor.Mentalese_Editor;

//The miscellaneous class
public class Misc {
	
	public static long lastTime = 0;
	
	public static boolean is_email(String emailAddress) {

		//Check if is an email address
		if (size(emailAddress, "@")==2) {
			if (size(atom(emailAddress, 2, "@"), ".")==2)
				return true;
			else return false;
		} else return false;
		
	}
	
	public static String systimestamp() {
		
		//Prepare format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//Get timestamp
		return dateFormat.format(System.currentTimeMillis());
		
	}

	public static String ini(String path, String section, String field) throws Exception {
		
		//Initialization
		String line="", value=null;
		int found = 0;
		BufferedReader in =null;

		//All parameters cannot be null or empty
		if (path==null || section==null || field==null || path.equals("") || section.equals("") || field.equals("")) {

			throw new Exception("Sorry, the path, section and field cannot be null or empty.");

		} else {

			//Try to get the value
			try {

				//In stream
				in =  new BufferedReader( new InputStreamReader( new FileInputStream(path)));
				//parse to find the section
				while ((line=in.readLine())!=null)
				{
					try {

						if (substr(line, 0, section.length()+2).equals("[" + section + "]"))
						{
							//The section found
							break;
						}

					} catch (Exception e) {};

				}

				//parse to find the field
				while (((line=in.readLine())!=null))
				{
					try {

						if (!(line.substring(0,1).equals("["))) {

							if (atom(line, 1, "=").equals(field))
							{
								//The field found
								found=1;
								break;
							}

						} else break;

					} catch (Exception e) {};
				}

				try {

					//Manage the last line
					if (found==0 && line!=null) {
						if (atom(line, 1, "=").equals(field))	found=1;
					}

				} catch (Exception e) {};

				//Get the value of found
				if (found==1) value=line.substring(field.length()+1).replace("\r\n", "").replace("\n", "").replace("\r", "");

				//return the value
				return value;

			} catch (Exception e) {

				return null;

			} finally {

				//Close the stream
				try {in.close();} catch (Exception e) {};

			}

		}

	}

	public static String substr(String str, int beginIndex, int endIndex) {

		//Try to get the sub string
		try {

			//Return null if one parameter is null
			if (str==null) {

				return null;

			} else {

				//Return null if beginIndex and endIndex are not valid
				if (beginIndex>=str.length() || endIndex<beginIndex) return "";
				else {

					//Get the sub string
					if (endIndex>str.length()) return ""+str.substring(beginIndex, str.length());
					else return ""+str.substring(beginIndex, endIndex);

				}

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	@SuppressWarnings("unchecked")
	public static String[] text_diff(String text1, String text2) {

		StringBuilder right = new StringBuilder("REVISED\n");
		StringBuilder left = new StringBuilder("ORIGINAL\n");

		List<String> original = Arrays.asList(text1.split("\n"));
		List<String> revised = Arrays.asList(text2.split("\n"));
		
		// Compute diff. Get the Patch object. Patch is the container for computed deltas.
		List<Delta> inlineDeltas = DiffUtils.diff(original, revised).getDeltas();
		
		Collections.reverse(inlineDeltas);
		
		//print out a detailed list of changes
		try {
			String tmp = "";
			
			for (Delta delta: inlineDeltas) {
				if(delta instanceof InsertDelta){
					
					tmp = "Insert at line: "+(delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
					Chunk ck = delta.getRevised();
					int nbRight = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "   size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						right.append(tmp+"\n");
						nbRight++;
					}
					
					for(int i=0;i<nbRight;i++) {
						left.append("\n");
					}

				} else if(delta instanceof DeleteDelta){
					
					tmp = "Delete at line: "+(delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
				} else {
					tmp = "Change at line: " + (delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
					String tx1="", tx2="";
					
					Chunk ck = delta.getRevised();
					int nbRight = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "   size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						right.append(tmp+"\n");
						nbRight++;
						tx2+=sl;
					}
					
					ck = delta.getOriginal();
					int nbLeft = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "   size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						left.append(tmp+"\n");
						nbLeft++;
						tx1+=sl;
					}
					
					if (nbLeft<nbRight) {
						
						for(int i=0;i<(nbRight-nbLeft);i++) {
							left.append("\n");
						}
						
					}
					
					if (nbRight<nbLeft) {
						
						for(int i=0;i<(nbLeft-nbRight);i++) {
							right.append("\n");
						}
						
					}
					
					String[] v = text_diff_char(tx1, tx2);
					left.append(v[0]);
					right.append(v[1]);
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] r = {left.toString(), right.toString()};

		return r;

	}

	@SuppressWarnings("unchecked")
	public static String[] text_diff_char(String text1, String text2) {

		StringBuilder right = new StringBuilder("");
		StringBuilder left = new StringBuilder("");

		List<String> original = new ArrayList<String>();
		for(char c : text1.toCharArray()) {
			original.add(""+c);
		}
		List<String> revised = new ArrayList<String>();
		for(char c : text2.toCharArray()) {
			revised.add(""+c);
		}
		
		// Compute diff. Get the Patch object. Patch is the container for computed deltas.
		List<Delta> inlineDeltas = DiffUtils.diff(original, revised).getDeltas();
		
		Collections.reverse(inlineDeltas);
		
		//print out a detailed list of changes
		try {
			String tmp = "";
			
			for (Delta delta: inlineDeltas) {
				if(delta instanceof InsertDelta){
					
					tmp = "   Insert at char: "+(delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
					Chunk ck = delta.getRevised();
					int nbRight = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "      size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						right.append(tmp+"\n");
						nbRight++;
					}
					
					for(int i=0;i<nbRight;i++) {
						left.append("\n");
					}

				} else if(delta instanceof DeleteDelta){
					
					tmp = "   Delete at char: "+(delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
				} else {
					tmp = "   Change at char: " + (delta.getOriginal().getPosition()+1);
					right.append(tmp + "\n");
					left.append(tmp + "\n");
					
					Chunk ck = delta.getRevised();
					int nbRight = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "      size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						right.append(tmp+"\n");
						nbRight++;
					}
					
					ck = delta.getOriginal();
					int nbLeft = 0;
					for(String sl: (List<String>)ck.getLines()){
						tmp = "      size["+sl.length()+"]: \""+sl.replace("\"", "\\\"")+"\"";
						left.append(tmp+"\n");
						nbLeft++;
					}
					
					if (nbLeft<nbRight) {
						
						for(int i=0;i<(nbRight-nbLeft);i++) {
							left.append("\n");
						}
						
					}
					
					if (nbRight<nbLeft) {
						
						for(int i=0;i<(nbLeft-nbRight);i++) {
							right.append("\n");
						}
						
					}
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] r = {left.toString(), right.toString()};

		return r;

	}
	
	public static String deleteFile(String fileOrDirectoryPath) throws Exception {

		//Initialization
		String b = "1";
		File fileOrdir = new File(fileOrDirectoryPath);
		String[] dirList;
		String subFileOrDir = "";

		//Check if directory
		if (!fileOrdir.isDirectory()) {

			//It's a file, delete it
			fileOrdir.delete();

		} else {

			//It's a directory

			//Get all files or directories in the current directory
			dirList=fileOrdir.list();

			//Parse all files or directories
			for (int i=0; i<dirList.length; i++) {

				//Get the sub file or directory
				subFileOrDir = dirList[i];

				//Delete the sub file or directory
				if (b.equals("1")) {

					b = deleteFile(fileOrDirectoryPath+File.separator+subFileOrDir);

				}
			}

			//Delete the current directory
			fileOrdir.delete();
		}

		return b;

	}
	
	//Get MD5 of a string
	public static String md5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		byte[] bytesOfPassword = str.getBytes("UTF-8");

		MessageDigest md = MessageDigest.getInstance("MD5");
		return new String(md.digest(bytesOfPassword), "UTF-8");
		
	}
	
	//Get all objects in string tab format
	public static String[] JSONArray2StringTab(JSONArray in) throws Exception {
		
		//Initialization
		String[] result = new String[in.size()];
		
		//Parse all objects
		for(int i=0;i<in.size();i++) {
			
			//Get the current object
			result[i] = in.get(i)+"";
			
		}
		
		return result;
		
	}
	
	//Diff date
	public static String dateDiffOneDay(String date) {
		
		//Initialization
		Calendar cal = Calendar.getInstance();    
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7))-1);
		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		return sdf.format(cal.getTime());
		
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	//Load a JSON string
	public static JSONArray loadArray(String json) {
		
		return (JSONArray) JSONValue.parse(json);
		
	}
	
	public static int size(String atomList, String separator) {

		//Prepare list
		String copyAtomList=atomList;
		Pattern motif = Pattern.compile("["+separator+"]");

		//Split the list
		String[] ch = motif.split(copyAtomList, -1);

		//Return the size
		return ch.length;

	}
	
	//Split words
	public static String[] splitWords(String words, String separator) {
		
		return words.split(Pattern.quote(separator));
		
	}
	
	//Read an ini file
	public static String conf_value(String path, String section, String field) {

		//Initialization
		String line="", value="";
		int found = 0;
		BufferedReader in =null;

		//Try to get the value
		try {

			//In stream
			in =  new BufferedReader( new FileReader(path));

			//parse to find the section
			while ((line=in.readLine())!=null)
			{
				
				if (!line.equals("") && line.equals("[" + section + "]"))
				{
					//The section found
					break;
				}
				
			}

			//parse to find the field
			while (((line=in.readLine())!=null))
			{
				if (!line.equals("") && !(line.substring(0,1).equals("["))) {
					if (atom(line, 1, "=").equals(field))
					{
						//The field found
						found=1;
						break;
					}
					
				}
			}
			
			//Manage the last line
			if (found==0 && line!=null &&  !line.equals("")) {
				if (atom(line, 1, "=").equals(field))	{
					found=1;
				}
			}
			
			//Get the value of found
			if (found==1) value=line.substring(field.length()+1).replace("\r\n", "").replace("\n", "").replace("\r", "");

			//return the value
			return value;

		} catch (Exception e) {

			return null;

		} finally {

			//Close the stream
			try {in.close();} catch (Exception e) {};

		}

	}
	
	//Basic rpad function
	public static String rpad(String str, String padString, String paddedLength) {

		//Initialization
		String strTmp=str;

		//Try to complete the string
		try {

			//Return null if one parameter is null
			if (str==null || padString==null || paddedLength==null) {

				return null;

			} else {

				//Complete the string
				for(int i=str.length();i<Integer.parseInt(paddedLength);i++) {
					strTmp+=padString;
				}

				//Return the right pad string
				return strTmp;

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}
	
	static String current_system_out_print = "";
	
	//Add space rpad
	public static void system_out_print(String str, boolean ln, String color, String strAdd) {
		
		str += strAdd;
		
		if (ln) {
			
			System.out.println(color+Misc.rpad(current_system_out_print+str, " ", "65").substring((current_system_out_print+str).length())+str.substring(0, str.length()-strAdd.length()));
			
		} else {
			current_system_out_print = str;
			System.out.print(color+str);
		}

	}
	
	//Get the os
	public static String os() {
		
		String OS = System.getProperty("os.name").toLowerCase();
		
		if (OS.indexOf("win") >= 0) {
			
			return "win";
			
		} else if (OS.indexOf("mac") >= 0) {
			
			return "mac";
			
		} else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) {
			
			return "unix";
			
		} else return "unknow";
		
	}
	
	//Get a specific string in a list
	public static String atom(String atomList, int index, String separator) {
		
		//Prepare the list
		String copyAtomList=atomList;
		Pattern motif = Pattern.compile("["+separator+"]");
		
		//Split the list
		String[] ch = motif.split(copyAtomList, -1);
		
		//Return the atom
		return ch[index-1];
		
	}
	
	//Basic lrtrim function
	public static String lrtrim(String str) {
		
		return str.replaceAll("\\s+$", "").replaceAll("^\\s+", "");

	}
	
	//Basic trim function
	public static String trim(String str) {
		
		return str.replaceAll("\\s+$", "").replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");

	}
	
	public static String load(String filePath) throws Exception
	{

		//Initialization
		String line="";
		BufferedReader in = null;
		StringBuilder data = new StringBuilder();

		//Try to get data
		try {

			//Load the stream
			in =  new BufferedReader( new InputStreamReader( new FileInputStream(filePath)));

			//Parse the stream
			while ((line=in.readLine())!=null)
			{
				//Get line by line
				data.append(line);
				data.append("\n");
			}

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close input stream
			try {in.close();} catch (Exception e) {};

		}

		//Return the data
		return data.toString();
	}
	
	public static String create(String filePath, String str) throws Exception {

		//Initialization
		FileWriter fw = null;

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file);

			//Write str
			fw.write(str);

			//Flush the file
			fw.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}
	
	public static void log(String msg) {
		
		try {
			
			String dt = "";
			//Prepare format
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			long curTime = System.currentTimeMillis();
			
			//Get timestamp
			dt = dateFormat.format(curTime);
			
			Mentalese_Editor.writeToScreen("mentdb", Mentalese_Editor.mentdbColorBold, Mentalese_Editor.errorColor, dt+": "+msg+" >>> "+(lastTime-curTime), true);
			
			lastTime = curTime;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String appendFile(String filePath, String str) throws Exception {

		//Initialization
		FileWriter fw = null;

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file, true);

			//Write str
			fw.write(str);

			//Flush the file
			fw.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw e;

		} finally {

			//Close the writer
			try {fw.close();} catch (Exception e) {}

		}

	}

}