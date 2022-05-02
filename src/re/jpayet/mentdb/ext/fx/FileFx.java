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

package re.jpayet.mentdb.ext.fx;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.net.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.log.Log;
import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;
import re.jpayet.mentdb.ext.tools.MentDBFilenameFilter;
import re.jpayet.mentdb.ext.tools.Misc;
import re.jpayet.mentdb.ext.user.GroupManager;

//The type class
public class FileFx {

	public static HashMap<String, WatchService> allWatchServices = new HashMap<String, WatchService>();
	public static HashMap<String, String> allWatchServiceDirectories = new HashMap<String, String>();
	public static HashMap<String, String> allWatchServiceScriptName = new HashMap<String, String>();
	public static HashMap<String, String> allWatchServiceUsers = new HashMap<String, String>();
	public static HashMap<String, String> allWatchServiceLastExecutionDate = new HashMap<String, String>();
	public static HashMap<String, String> allWatchServiceErrorMsg = new HashMap<String, String>();
	
	public static String rotate_image_90(String imagePath) throws Exception {
		
	    BufferedImage src = ImageIO.read(new File(imagePath));
	    
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage dest = new BufferedImage(height, width, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    String type = "";
	    if (imagePath.toLowerCase().endsWith(".jpg") || imagePath.toLowerCase().endsWith(".jpeg")) {
	    	type = "jpg";
	    } else if (imagePath.toLowerCase().endsWith(".png")) {
	    	type = "png";
	    }
	    ImageIO.write(dest, type, bos);
	    
	    return new String(Base64.encodeBase64Chunked(bos.toByteArray()));
		
	}
	
	public static String resize_image(String imagePath, String width, String height) throws Exception {
		
		int targetWidth = Integer.parseInt(width);
		int targetHeight = Integer.parseInt(height);
	    
	    BufferedImage originalImage = ImageIO.read(new File(imagePath));
		
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    String type = "";
	    if (imagePath.toLowerCase().endsWith(".jpg") || imagePath.toLowerCase().endsWith(".jpeg")) {
	    	type = "jpg";
	    } else if (imagePath.toLowerCase().endsWith(".png")) {
	    	type = "png";
	    }
	    ImageIO.write(outputImage, type, bos);
	    
	    return new String(Base64.encodeBase64Chunked(bos.toByteArray()));
		
	}
	
	public static void start_watch_service(SessionThread session, String skey, String user, String directory, String scriptName) throws Exception {
		
		if (exist_watch_service(skey)) {
			
			throw new Exception("Sorry, watch service key '"+skey+"' already exist.");
			
		}
		
		WatchService watchService = FileSystems.getDefault().newWatchService();
		
		Path path = Paths.get(directory);
		path.register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		
		allWatchServices.put(skey, watchService);
		allWatchServiceDirectories.put(skey, directory);
		allWatchServiceScriptName.put(skey, scriptName);
		allWatchServiceUsers.put(skey, user);
		allWatchServiceLastExecutionDate.put(skey, DateFx.current_timestamp());
		allWatchServiceErrorMsg.put(skey, "");
		
		Thread t1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	try {
		    		
		    			Log.trace("Begin FileWatcher: "+skey);
		    		
					WatchKey key;
					while ((key = watchService.take()) != null) {
						
						String dtExe = DateFx.current_timestamp();
						
						allWatchServiceLastExecutionDate.put(skey, dtExe);
						
						for (WatchEvent<?> event : key.pollEvents()) {
							
							String pid = Database.execute_admin_mql(session, "sequence increment \"pid\";");
							String nbmaxattempt = "1";

							String parent_id = null;
							if (session.env.exist("[PID]")) {
								parent_id = session.env.get("[PID]");
							}
							
							MYSQLManager.executeUpdate("INSERT INTO `stack_wait`" + 
									"(`pid`," + 
									"`parent_id`," + 
									"`script`," + 
									"`priority`," + 
									"`nb_in_thread`," + 
									"`lastattempt`," + 
									"`nbattempt`," + 
									"`nbmaxattempt`," + 
									"`dtexe`," + 
									"`login`," + 
									"`lasterrormsg`," + 
									"`dtclosed`," + 
									"`dterror`,"
									+ "`state`)" + 
									"VALUES" + 
									"("+SQLManager.encode(pid)+"," + 
									SQLManager.encode(parent_id)+"," + 
									SQLManager.encode(scriptName)+"," + 
									SQLManager.encode("0")+"," + 
									SQLManager.encode("5")+"," + 
									"null," + 
									"0," + 
									SQLManager.encode(nbmaxattempt)+"," + 
									SQLManager.encode(dtExe)+"," + 
									SQLManager.encode(user)+"," + 
									"null," + 
									"null," + 
									"null, 'E');", true);
							
							try {
								
								MYSQLManager.executeUpdate("INSERT INTO `stack_var`" + 
										"(`pid`," + 
										"`param`," + 
										"`value`)" + 
										"VALUES" + 
										"("+SQLManager.encode(pid)+"," + 
										SQLManager.encode("[kind]")+"," + 
										SQLManager.encode(""+event.kind())+");", true);
								
								MYSQLManager.executeUpdate("INSERT INTO `stack_var`" + 
										"(`pid`," + 
										"`param`," + 
										"`value`)" + 
										"VALUES" + 
										"("+SQLManager.encode(pid)+"," + 
										SQLManager.encode("[file]")+"," + 
										SQLManager.encode(""+event.context())+");", true);
								
								MYSQLManager.executeUpdate("UPDATE stack_wait SET `state`='W' where `pid`="+SQLManager.encode(pid)+";", false);
								
							} catch (Exception e) {
								
								MYSQLManager.executeUpdate("delete from `stack_var` where `pid`="+SQLManager.encode(pid)+";", false);
								MYSQLManager.executeUpdate("delete from `stack_wait` where `pid`="+SQLManager.encode(pid)+";", false);
								
								throw e;
								
							}

						}
						
						key.reset();
						
					}
					
					Log.trace("End FileWatcher: "+skey);
					
				} catch (Exception e) {
					
					Log.trace("End FileWatcher: "+skey);
					
					try {
						if (e.getMessage()!=null) allWatchServiceErrorMsg.put(skey, ""+e.getMessage());
					} catch (Exception f) {}
					
				}
		        
		    }
		});  
		t1.start();
		
	}
	
	public static void stop_watch_service(String skey) throws Exception {
		
		if (!exist_watch_service(skey)) {
			
			throw new Exception("Sorry, watch service key '"+skey+"' does not exist.");
			
		}
		
		try {
			
			allWatchServices.get(skey).close();
			
		} catch (Exception e) {}
		
		allWatchServices.remove(skey);
		allWatchServiceDirectories.remove(skey);
		allWatchServiceScriptName.remove(skey);
		allWatchServiceUsers.remove(skey);
		allWatchServiceLastExecutionDate.remove(skey);
		allWatchServiceErrorMsg.remove(skey);
		
	}
	
	public static boolean exist_watch_service(String skey) throws Exception {
		
		return allWatchServices.containsKey(skey);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show_watch_service() throws Exception {
		
		JSONArray result = new JSONArray();
		
		for(Object o : allWatchServices.keySet()) {
			
			String skey = (String) o;
			
			JSONObject obj = new JSONObject();
			obj.put("Key", skey);
			obj.put("Dir", allWatchServiceDirectories.get(skey));
			obj.put("Script", allWatchServiceScriptName.get(skey));
			obj.put("User", allWatchServiceUsers.get(skey));
			obj.put("LastTS", allWatchServiceLastExecutionDate.get(skey));
			obj.put("Err", allWatchServiceErrorMsg.get(skey));
			obj.put("State", (allWatchServiceErrorMsg.get(skey)==null || !allWatchServiceErrorMsg.get(skey).equals("")?"ERROR":"RUNNING..."));
			
			result.add(obj);
			
		}
		
		return result;
		
	}
	
	//Count the number of line in a directory
	public static String count_dir(String dirPath, String endOfFile) throws Exception {
		
		//Initialization
		int cnt = 0;
		File dir = new File(dirPath);
		String[] allFiles = dir.list();

		//Parse all files
		for(int i=0;i<allFiles.length;i++) {

			//get the current file
			String curFile = allFiles[i];

			//Check if a directory or a file
			if ((new File(dirPath+File.separator+curFile)).isDirectory()) {

				cnt += Long.parseLong(count_dir(dirPath+File.separator+curFile, endOfFile));

			} else {

				if (curFile.endsWith(endOfFile)) cnt += Long.parseLong(""+count_lines(dirPath+File.separator+curFile));

			}

		}

		//Return the number of line
		return ""+cnt;

	}
	
	//Count lines in a file
	public static int count_lines(String filePath) throws Exception {
		
		//Initialization
		LineNumberReader reader  = new LineNumberReader(new FileReader(filePath));
		int cnt = 0;
		@SuppressWarnings("unused")
		String lineRead = "";

		try {

			//Parse the file
			while ((lineRead = reader.readLine()) != null) {}

			//Get the number of line
			cnt = reader.getLineNumber(); 

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, an error appears: "+e.getMessage()+".");

		} finally {

			//Close the reader
			try {reader.close();} catch (Exception e) {}

		}

		//Return the number of line
		return cnt;

	}

	//Create a new file
	public static String create(String user, String filePath, String data) throws Exception {

		//filePath cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//The string to add cannot be null
		if (data==null) {

			throw new Exception("Sorry, the string to add cannot be null.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}
		
		Misc.create(filePath, data);
		
		return "1";

	}
	
	public static String create(String user, String filePath, String str, String encoding) throws Exception {

		//Initialization
		Writer out = null;

		//filePath cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//encoding cannot be null or empty
		if (encoding==null || encoding.equals("")) {

			throw new Exception("Sorry, the file encoding cannot be null or empty.");

		}

		//The string to add cannot be null
		if (str==null) {

			throw new Exception("Sorry, the string to add cannot be null.");

		}

		//Check if the file is a directory
		if (exist(filePath).equals("1") && is_directory(filePath).equals("1")) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file, false), encoding));

			//Write str
			out.write(str);

			//Flush the file
			out.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Close the writer
			try {out.close();} catch (Exception e) {}

		}

	}

	public static String append(String user, String filePath, String str) throws Exception {
		
		//Initialization
		FileWriter fw = null;

		//filePath cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//The string to add cannot be null
		if (str==null) {

			throw new Exception("Sorry, the string to add cannot be null.");

		}

		//Check if the file is a directory
		if (exist(filePath).equals("1") && is_directory(filePath).equals("1")) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			file.createNewFile();
			fw = new FileWriter(file, true);

			//Write the string
			fw.write(str);

			//Flush the file
			fw.flush();
			fw.close();

			//Return true
			return "1";

		} catch (Exception e) {

			//Return the error
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Try to close the file
			try {fw.close();} catch (Exception f) {}

		}

	}
	
	public static String append(String user, String filePath, String str, String encoding) throws Exception {
		
		//Initialization
		Writer out = null;

		//filePath cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//encoding cannot be null or empty
		if (encoding==null || encoding.equals("")) {

			throw new Exception("Sorry, the file encoding cannot be null or empty.");

		}

		//The string to add cannot be null
		if (str==null) {

			throw new Exception("Sorry, the string to add cannot be null.");

		}

		//Check if the file is a directory
		if (exist(filePath).equals("1") && is_directory(filePath).equals("1")) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Initialization
		File file = new File(filePath);

		try {

			//Create the new file
			out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file, true), encoding));

			//Write str
			out.write(str);

			//Flush the file
			out.flush();

			//Return true
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Close the writer
			try {out.close();} catch (Exception e) {}

		}

	}

	//Check if a file already exist
	public static String exist(String fileOrDirectoryPath) throws Exception {

		//The path cannot be null or empty
		if (fileOrDirectoryPath==null || fileOrDirectoryPath.equals("")) {

			throw new Exception("Sorry, the file or directory cannot be null or empty.");

		} else {

			//Initialization
			File fileOrdir = new File(fileOrDirectoryPath);

			if (fileOrdir.exists()) return "1";
			else return "0";

		}

	}
	
	//Load a file
	public static String load(String user, String filePath, String encoding) throws IOException, Exception {
		
		//Initialization
		String line="";
		BufferedReader in = null;
		StringBuilder data = new StringBuilder();

		//The file path cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//The file encoding cannot be null or empty
		if (encoding==null || encoding.equals("")) {

			throw new Exception("Sorry, the file encoding cannot be null or empty.");

		}

		//Check if the file exist
		if (exist(filePath).equals("0")) {

			throw new Exception("Sorry, the file '"+filePath+"' does not exist.");

		}

		//File cannot be a directory
		if ((new File(filePath)).isDirectory()) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Try to get data
		try {

			//Load the stream
			in =  new BufferedReader( new InputStreamReader( new FileInputStream(filePath), encoding));

			//Parse the stream
			while ((line=in.readLine())!=null)
			{
				//Get line by line
				data.append(line);
				data.append("\n");
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Close input stream
			try {in.close();} catch (Exception e) {};

		}

		//Return the data
		return data.toString();
		
	}
	
	public static boolean is_server_conf(String filePath) throws Exception {
		
		File file = new File(filePath);
		file = file.getCanonicalFile();
		
		if (file.getCanonicalPath().replace("\\", "/").endsWith("MentDB_Server_3/conf/server.conf")) 
			return true;
		else return false;
		
	}
	
	public static String load(String user, String filePath) throws Exception {
		
		//Initialization
		String line="";
		BufferedReader in = null;
		StringBuilder data = new StringBuilder();

		//The file path cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//Check if the file exist
		if (exist(filePath).equals("0")) {

			throw new Exception("Sorry, the file '"+filePath+"' does not exist.");

		}

		//File cannot be a directory
		if ((new File(filePath)).isDirectory()) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
		}

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

	//Check if a file is a directory
	public static String is_directory(String fileOrDirectoryPath) throws Exception {

		//The path cannot be null or empty
		if (fileOrDirectoryPath==null || fileOrDirectoryPath.equals("")) {

			throw new Exception("Sorry, the file or directory path cannot be null or empty.");

		} else {

			//Check if the file exist
			if (exist(fileOrDirectoryPath).equals("0")) {

				throw new Exception("Sorry, the file or directory '"+fileOrDirectoryPath+"' does not exist.");

			}

			//Initialization
			File fileOrdir = new File(fileOrDirectoryPath);

			if (fileOrdir.isDirectory()) return "1";
			else return "0";

		}

	}
	
	//Get basic meta data
	public static String meta_data(String filePath, String attribute) throws Exception {
		
		//Initialization
		String result = "";
		
		File f = new File(filePath);
		
		if (!(f).exists()) {

			throw new Exception("Sorry, the file '"+filePath+"' does not exist.");

		}
		
		BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
		
		if (attribute==null || attribute.equals("")) {
			throw new Exception("Sorry, the attribute cannot be null or empty.");
		}

		if (attribute.equals("creationTime")) {
			result = ""+attr.creationTime();
		} else if (attribute.equals("lastAccessTime")) {
			result = ""+attr.lastAccessTime();
		} else if (attribute.equals("lastModifiedTime")) {
			result = ""+attr.lastModifiedTime();
		} else if (attribute.equals("author")) {
			FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(f.toPath(), FileOwnerAttributeView.class);
	        UserPrincipal owner = ownerAttributeView.getOwner();
	        result = ""+owner.getName();
		} else {
			throw new Exception("Sorry, the attribute does not exist.");
		}
		
		return result;
		
	}

	public static String cur_abs_dir() throws Exception {

		return (new File(".")).getAbsolutePath();

	}

	public static String cur_canonical_dir() throws Exception {

		return (new File(".")).getCanonicalPath();

	}

	public static String pwd() throws Exception {

		return cur_abs_dir();

	}

	public static String size(String fileOrDirectoryPath) throws Exception {

		//Initialization
		long size = 0;
		File dirOrFile = new File(fileOrDirectoryPath);

		//Check if the file or directory exist or not
		if (!dirOrFile.exists()) {

			throw new Exception("Sorry, the file or directory does not exist.");

		}

		//Check if file or directory
		if (dirOrFile.isDirectory()) {

			//It's a directory
			size = getFolderSize(dirOrFile);

		} else {

			//Set the size of the file
			size = dirOrFile.length();

		}

		//Return the size
		return ""+size;

	}

	public static String last_modified(String fileOrDirectoryPath) throws Exception {

		//Initialization
		File dirOrFile = new File(fileOrDirectoryPath);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//Return the size
		return ""+sdf.format(dirOrFile.lastModified());

	}
	
	public static String mkdir(String directoryPath) throws Exception {
		
		//The path cannot be null or empty
		if (directoryPath==null || directoryPath.equals("")) {

			throw new Exception("Sorry, the directory path cannot be null or empty.");

		} else {

			//Check if the file or directory already exist
			if (exist(directoryPath).equals("1")) {

				throw new Exception("Sorry, the file or directory '"+directoryPath+"' already exist.");

			}

			//Initialization
			String b = "1";

			//Try to create directory
			try {

				//Make the directory
				(new File(directoryPath)).mkdir();

			} catch (Exception e) {

				//Generate an error
				throw new Exception("Sorry, "+e.getMessage()+".");

			}

			return b;

		}

	}

	public static String delete(String fileOrDirectoryPath) throws Exception {
		
		//All parameters can not be null
		if (fileOrDirectoryPath==null || fileOrDirectoryPath.equals("")) {

			throw new Exception("Sorry, the file or directory cannot be null or empty.");

		} else {

			//Initialization
			String b = "1";
			File fileOrdir = new File(fileOrDirectoryPath);
			String[] dirList;
			String subFileOrDir = "";

			//Check if the file exist
			if (exist(fileOrDirectoryPath).equals("0")) {

				throw new Exception("Sorry, the file or directory '"+fileOrDirectoryPath+"' does not exist.");

			}
			
			File file = new File(fileOrDirectoryPath);
			if (file.getCanonicalPath().replace("\\", "/").endsWith("MentDB_Server_3/conf")) {
				
				throw new Exception("Sorry, the directory 'MentDB_Server_3/conf' cannot be deleted.");
				
			}
			if (file.getCanonicalPath().replace("\\", "/").endsWith("MentDB_Server_3/conf/server.conf")) {
				
				throw new Exception("Sorry, the file 'MentDB_Server_3/conf/server.conf' cannot be deleted.");
				
			}

			//Try to delete
			try {

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

							b = delete(fileOrDirectoryPath+"/"+subFileOrDir);

						}
					}

					//Delete the current directory
					fileOrdir.delete();
				}

			} catch (Exception e) { 

				throw new Exception("Sorry, "+e.getMessage()+".");

			};

			return b;

		}

	}

	public static String copy_dir(String user, String oldDirPath, String newDirPath) throws Exception {
		
		//Initialization
		String b = "1";

		//All path cannot be null or emty
		if (oldDirPath==null || newDirPath==null || oldDirPath.equals("") || newDirPath.equals("")) {

			throw new Exception("Sorry, old and new paths cannot be null or empty.");

		}

		//Get the old and new file
		File oldDir = new File(oldDirPath);
		File newDir = new File(newDirPath);

		//Get the old directory list
		String[] dirList=oldDir.list();

		//Generate an error if the old directory does not exist
		if (!oldDir.exists()) {

			throw new Exception("Sorry, the directory '"+oldDirPath+"' does not exist.");

		}

		//Generate an error if the old directory is a file
		if (!oldDir.isDirectory()) {

			throw new Exception("Sorry, '"+oldDirPath+"' must be a directory.");

		}

		//Check if the new directory exist or not
		if (!newDir.exists()) {

			//Create if not exist
			newDir.mkdir();

		} else {

			if (!newDir.isDirectory()) {

				throw new Exception("Sorry, '"+newDirPath+"' must be a directory.");

			}

		}

		//Parse the directory list if not null
		if (dirList!=null) {

			for (int i=0; i<dirList.length; i++) {

				//Get the current file or directory
				String currentFileOrDirectory = dirList[i];

				//get the current file object
				File currentFileObject = new File(oldDirPath+"/"+currentFileOrDirectory);

				//Check if a directory or not
				if (!currentFileObject.isDirectory()) {

					//Copy the file
					copy_file(user, oldDirPath+"/"+currentFileOrDirectory, newDirPath+"/"+currentFileOrDirectory);

				} else {

					//Copy the directory
					copy_dir(user, oldDirPath+"/"+currentFileOrDirectory, newDirPath+"/"+currentFileOrDirectory);

				}

			}

		}

		//Return a boolean
		return b;
	}

	public static String copy_file(String user, String oldFilePath, String newFilePath) throws Exception {
		
		//All path cannot be null or empty
		if (oldFilePath==null || newFilePath==null || oldFilePath.equals("") || newFilePath.equals("")) {

			throw new Exception("Sorry, old and new paths cannot be null or empty.");

		}

		//Initialization
		String b ="1";
		InputStream in = null;
		OutputStream out = null;
		File file_in = new File(oldFilePath);

		//Generate an error if the old file does not exist
		if (!file_in.exists()) {

			throw new Exception("Sorry, the file '"+oldFilePath+"' does not exist.");

		}

		//Generate an error if the old file is a directory
		if (file_in.isDirectory()) {

			throw new Exception("Sorry, '"+oldFilePath+"' must be a file.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(oldFilePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
		}
		
		if (user!=null && !user.equals("") && is_server_conf(newFilePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		try {

			//Copy the stream
			Files.copy((new File(oldFilePath).toPath()), (new File(newFilePath).toPath()), StandardCopyOption.REPLACE_EXISTING);

		} catch (Exception e) {

			//generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Try to close all streams
			try {in.close();} catch (Exception e) {};
			try {out.close();} catch (Exception e) {};

		}

		return b;
	}

	public static String copy_format(String user, String source, String srcEncoding, String target, String tgtEncoding) throws Exception {
		
		//Initialization
		BufferedReader br = null;
		BufferedWriter bw = null;

		//source cannot be null or empty
		if (source==null || source.equals("")) {

			throw new Exception("Sorry, the source file path cannot be null or empty.");

		}

		//srcEncoding cannot be null or empty
		if (srcEncoding==null || srcEncoding.equals("")) {

			throw new Exception("Sorry, the source encoding cannot be null or empty.");

		}

		//target cannot be null or empty
		if (target==null || target.equals("")) {

			throw new Exception("Sorry, the target file path cannot be null or empty.");

		}

		//tgtEncoding cannot be null or empty
		if (tgtEncoding==null || tgtEncoding.equals("")) {

			throw new Exception("Sorry, the target encoding cannot be null or empty.");

		}

		//Check if the source file does not exist
		if (exist(source).equals("0")) {

			throw new Exception("Sorry, '"+source+"' does not exist.");

		}

		//Check if the source file is a directory
		if (is_directory(source).equals("1")) {

			throw new Exception("Sorry, '"+source+"' is a directory.");

		}

		//Check if the target file already exist
		if (exist(target).equals("1")) {

			throw new Exception("Sorry, '"+source+"' already exist.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(source) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
		}
		
		if (user!=null && !user.equals("") && is_server_conf(target) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		try{
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(source),srcEncoding));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));
			
			char[] buffer = new char[16384];
			int read;
			
			while ((read = br.read(buffer)) != -1)
				bw.write(buffer, 0, read);
			
			return "1";
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		} finally {
			
			try {br.close();} catch (Exception e) {};
			try {bw.close();} catch (Exception e) {};
			
		}

	}

	public static String ini(String user, String path, String section, String field) throws Exception {
		
		//Initialization
		String line="", value=null;
		int found = 0;
		BufferedReader in =null;

		//All parameters cannot be null or empty
		if (path==null || section==null || field==null || path.equals("") || section.equals("") || field.equals("")) {

			throw new Exception("Sorry, the path, section and field cannot be null or empty.");

		} else {

			//Check if the file exist
			if (exist(path).equals("0")) {

				throw new Exception("Sorry, the file '"+path+"' does not exist.");

			}

			//Check if is directory
			if (is_directory(path).equals("1")) {

				throw new Exception("Sorry, '"+path+"' must be a file.");

			}
			
			if (user!=null && !user.equals("") && is_server_conf(path) && !GroupManager.isGrantedUser("sys", user) && (
					(section.equals("ADMIN") && field.equals("PWD"))
					|| (section.equals("ADMIN") && field.equals("SERVER_ENCRYPT_KEY"))
					|| (section.equals("SQL") && field.equals("pwd"))
					|| (section.equals("SECURITY") && field.equals("CU_KEYSTORE_PASSWORD"))
					)) {
				throw new Exception("Sorry, to get info '"+section+"/"+field+"' from 'server.conf', "+user+" must be granted with 'sys'.");
			}

			//Try to get the value
			try {

				//In stream
				in =  new BufferedReader( new InputStreamReader( new FileInputStream(path)));
				//parse to find the section
				while ((line=in.readLine())!=null)
				{
					try {

						if (StringFx.substr(line, "0", ""+section.length()+2).equals("[" + section + "]"))
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

							if (AtomFx.get(line, "1", "=").equals(field))
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
						if (AtomFx.get(line, "1", "=").equals(field))	found=1;
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

	public static String ini_str(String data, String section, String field) throws Exception {

		//Initialization
		String line="", value=null;
		int found = 0;
		BufferedReader in =null;

		//All parameters cannot be null or empty
		if (data==null || section==null || field==null || data.equals("") || section.equals("") || field.equals("")) {

			throw new Exception("Sorry, the data, section and field cannot be null or empty.");

		} else {

			//Try to get the value
			try {
				
				//In stream
				line = data.substring(0, data.indexOf("\n"));
				data = data.substring(line.length()+1);

				//parse to find the section
				while (line!=null)
				{

					try {

						if (StringFx.substr(line, "0", ""+section.length()+2).equals("[" + section + "]"))
						{
							//The section found
							break;
						}

					} catch (Exception e) {};

					line = data.substring(0, data.indexOf("\n"));
					data = data.substring(line.length()+1);

				}

				line = data.substring(0, data.indexOf("\n"));
				data = data.substring(line.length()+1);

				//parse to find the field
				while ((line!=null))
				{
					try {

						if (!(line.substring(0,1).equals("["))) {

							if (AtomFx.get(line, "1", "=").equals(field))
							{
								//The field found
								found=1;
								break;
							}

						} else break;

					} catch (Exception e) {};

					line = data.substring(0, data.indexOf("\n"));
					data = data.substring(line.length()+1);

				}

				try {

					//Manage the last line
					if (found==0 && line!=null) {
						if (AtomFx.get(line, "1", "=").equals(field))	found=1;
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

	@SuppressWarnings("unchecked")
	public static JSONArray dir_list(String directoryPath) throws Exception {
		
		//All parameters can not be null
		if (directoryPath==null || directoryPath.equals("")) {

			throw new Exception("Sorry, the path cannot be null or empty.");

		} else {

			//Generate an error if the directory does not exist
			if (!(new File(directoryPath)).exists()) {

				throw new Exception("Sorry, the directory '"+directoryPath+"' does not exist.");

			}

			//Generate an error if not a directory
			if (!(new File(directoryPath)).isDirectory()) {

				throw new Exception("Sorry, '"+directoryPath+"' must be a directory.");

			}

			//Initialization
			JSONArray list = new JSONArray();

			//List the directory
			String[] listTab = (new File(directoryPath)).list();

			//Parse the list
			for(int i=0;i<listTab.length;i++) {

				//Get the current file or directory name
				list.add(listTab[i]);

			}

			return list;

		}

	} 

	@SuppressWarnings("unchecked")
	public static JSONArray dir_list_regex(String directoryPath, String regexFilter, String getFile, String getDirectory) throws Exception {
		
		//Generate an error if the regex is null or empty
		if (regexFilter==null || regexFilter.equals("")) {

			throw new Exception("Sorry, the regex filter cannot be null or empty.");

		}
		
		//Generate an error if the boolean getFile is not valid
		if (getFile==null || getFile.equals("") || (!getFile.equals("1") && !getFile.equals("0"))) {

			throw new Exception("Sorry, the getFile boolean must be 1 or 0.");

		}
		
		//Generate an error if the boolean getDirectory is not valid
		if (getDirectory==null || getDirectory.equals("") || (!getDirectory.equals("1") && !getDirectory.equals("0"))) {

			throw new Exception("Sorry, the getDirectory boolean must be 1 or 0.");

		}
		
		//All parameters can not be null
		if (directoryPath==null || directoryPath.equals("")) {

			throw new Exception("Sorry, the path cannot be null or empty.");

		} else {

			//Generate an error if the directory does not exist
			if (!(new File(directoryPath)).exists()) {

				throw new Exception("Sorry, the directory '"+directoryPath+"' does not exist.");

			}

			//Generate an error if not a directory
			if (!(new File(directoryPath)).isDirectory()) {

				throw new Exception("Sorry, '"+directoryPath+"' must be a directory.");

			}

			//Initialization
			JSONArray list = new JSONArray();
			
			// create new filename filter
	         FilenameFilter fileNameFilter = new MentDBFilenameFilter(regexFilter, (getFile.equals("1")?true:false) , (getDirectory.equals("1")?true:false));

			//List the directory
			String[] listTab = (new File(directoryPath)).list(fileNameFilter);

			//Parse the list
			for(int i=0;i<listTab.length;i++) {

				//Get the current file or directory name
				list.add(listTab[i]);

			}

			//Return the list
			return list;

		}

	}

	public static String b64_read(String user, String filePath) throws Exception {
		
		//The path cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		} else {

			//Check if the file exist
			if (exist(filePath).equals("0")) {

				throw new Exception("Sorry, the file '"+filePath+"' does not exist.");

			}

			//Generate an error if the file is a directory
			if ((new File(filePath)).isDirectory()) {

				throw new Exception("Sorry, '"+filePath+"' must be a file.");

			}
			
			if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
				throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
			}

			//Get the binary data
			return b64Read(filePath);

		}

	}

	public static String b64_write(String user, String dataInB64Format, String filePath) throws Exception {
		
		//The file path cannot be null or empty
		if (filePath==null || filePath.equals("")) {

			throw new Exception("Sorry, the file path cannot be null or empty.");

		}

		//Data cannot be null
		if (dataInB64Format==null) {

			throw new Exception("Sorry, data cannot be null.");

		}

		//A directory cannot be overwrite
		if ((new File(filePath)).exists() && (new File(filePath)).isDirectory()) {

			throw new Exception("Sorry, a directory cannot be overwrite.");

		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Write the file
		if (b64Write(dataInB64Format, filePath)) return "1";
		else return "0";

	}

	public static String reader_exist(EnvManager env, String fileReaderId) throws Exception {

		//The file reader id cannot be null or empty
		if (fileReaderId==null || fileReaderId.equals("")) {

			throw new Exception("Sorry, the file reader id cannot be null or empty.");

		}

		//Check if contains key
		if (env.fileReaders.containsKey(fileReaderId)) {

			//Return true
			return "1";

		} else {

			//Return false
			return "0";

		}

	}

	public static String reader_close(EnvManager env, String fileReaderId) throws Exception {

		//The file reader id cannot be null or empty
		if (reader_exist(env, fileReaderId).equals("0")) {

			throw new Exception("Sorry, the file reader does not exist.");

		}
		
		Object o = env.fileReaders.get(fileReaderId);
		
		boolean is_text = false;
		if (o instanceof BufferedReader) {
			
			is_text = true;
			
		}

		//Try to close
		try {
			
			if (is_text) {

				//Close the file reader
				((BufferedReader) env.fileReaders.get(fileReaderId)).close();
	
				//Delete the reader from the environment variable
				env.fileReaders.remove(fileReaderId);
	
				return "1";
				
			} else {
				
				//Close the binary file reader
				((FileInputStream) env.fileReaders.get(fileReaderId)).close();

				//Delete the binary reader from the environment variable
				env.fileReaders.remove(fileReaderId);

				return "1";
				
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String reader_open(String user, EnvManager env, String fileReaderId, String filePath, String type, String encoding) throws Exception {
		
		//All parameters can not be null
		if (fileReaderId==null || fileReaderId.equals("") || filePath==null || filePath.equals("") || type==null || type.equals("")) {

			throw new Exception("Sorry, all arguments cannot be null or empty.");

		}

		type = type.toLowerCase();
		
		//Check if the type is TEXT | BINARY
		if (!type.toLowerCase().equals("text") && !type.toLowerCase().equals("binary")) {

			throw new Exception("Sorry, the type must be 'TEXT|BINARY'.");

		}

		//Check if the file is a directory
		if (exist(filePath).equals("1") && is_directory(filePath).equals("1")) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}

		//Close the file reader if already exist
		if (reader_exist(env, fileReaderId).equals("1")) {

			reader_close(env, fileReaderId);

		}
		
		boolean is_text = false;
		if (type.equals("text")) {
			
			is_text = true;
			
		}

		try {
			
			if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
				throw new Exception("Sorry, to read 'server.conf', "+user+" must be granted with 'sys'.");
			}
			
			if (is_text) {

				//Create the beffered reader
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
	
				//Put the file reader into the environment
				env.fileReaders.put(fileReaderId, br);
	
				//Return true
				return "1";
				
			} else {
				
				//Create the file input stream
				FileInputStream fis = new FileInputStream(filePath);

				//Put the binary file reader into the environment
				env.fileReaders.put(fileReaderId, fis);

				//Return true
				return "1";
				
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray reader_show(EnvManager env) throws Exception {

		//Initialization
		JSONArray list = new JSONArray();

		//Parse all file readers
		for (Entry<String, Object> e : env.fileReaders.entrySet()) {

			//Concatenation
			list.add(e.getKey());

		}

		return list;

	}

	@SuppressWarnings("unchecked")
	public static JSONObject reader_closeall(EnvManager env) throws Exception {

		//Initialization
		JSONObject list = new JSONObject();
		JSONArray allReaders = reader_show(env);

		//Try to close
		try {

			//Parse all file readers
			for (int i=1;i<allReaders.size();i++) {
				
				//Get the current reader
				String currentReaderId = (String) allReaders.get(i);

				//Close each file reader
				reader_close(env, currentReaderId);

				//Concatenation
				list.put(currentReaderId, "Closed");

			}

			return list;

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String reader_get_line(EnvManager env, String fileReaderId) throws Exception {

		//The file reader id cannot be null or empty
		if (reader_exist(env, fileReaderId).equals("0")) {

			throw new Exception("Sorry, the file reader does not exist.");

		}
		
		Object o = env.fileReaders.get(fileReaderId);
		
		if (!(o instanceof BufferedReader)) {
			
			throw new Exception("Sorry, this function must used with 'TEXT' mode.");
			
		}

		//Try to get the line
		try {

			//Get data from file
			return ((BufferedReader) env.fileReaders.get(fileReaderId)).readLine();

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String reader_get_bytes(EnvManager env, String fileReaderId, String nbBytes) throws Exception {

		//The binary file reader id cannot be null or empty
		if (reader_exist(env, fileReaderId).equals("0")) {

			throw new Exception("Sorry, the file reader does not exist.");

		}

		//The number of bytes must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(nbBytes);

		} catch (Exception e) {

			throw new Exception("Sorry, the number of bytes must be a number.");

		}
		
		Object o = env.fileReaders.get(fileReaderId);
		
		if ((o instanceof BufferedReader)) {
			
			throw new Exception("Sorry, this function must used with 'BINARY' mode.");
			
		}

		//Try to get data
		try {

			//Get data from file
			byte[] bytes = new byte[Integer.parseInt(nbBytes)];
			int state = ((FileInputStream) env.fileReaders.get(fileReaderId)).read(bytes);

			//Return the hex code ...
			if (state!=-1) {

				if (Integer.parseInt(nbBytes)==state) return new String(Base64.encodeBase64Chunked(bytes));
				else {

					return new String(Base64.encodeBase64Chunked(Arrays.copyOf(bytes, state)));

				}

			} else return null;

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String writer_exist(EnvManager env, String fileWriterId) throws Exception {

		//The file writer id cannot be null or empty
		if (fileWriterId==null || fileWriterId.equals("")) {

			throw new Exception("Sorry, the file writer id cannot be null or empty.");

		}

		//Check if contains key
		if (env.fileWriters.containsKey(fileWriterId)) {

			//Return true
			return "1";

		} else {

			//Return false
			return "0";

		}

	}

	public static String writer_close(EnvManager env, String fileWriterId) throws Exception {

		//The file writer id cannot be null or empty
		if (writer_exist(env, fileWriterId).equals("0")) {

			throw new Exception("Sorry, the file writer does not exist.");

		}
		
		Object o = env.fileWriters.get(fileWriterId);
		
		boolean is_text = false;
		if (o instanceof BufferedWriter) {
			
			is_text = true;
			
		}

		//Try to close
		try {
			
			if (is_text) {

				//Close the file writer
				((BufferedWriter) env.fileWriters.get(fileWriterId)).close();
	
				//Delete the writer from the environment variable
				env.fileWriters.remove(fileWriterId);
	
				return "1";
				
			} else {
				
				//Close the binary file writer
				((FileOutputStream) env.fileWriters.get(fileWriterId)).close();

				//Delete the binary writer from the environment variable
				env.fileWriters.remove(fileWriterId);

				return "1";
				
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	@SuppressWarnings("unchecked")
	public static JSONArray writer_show(EnvManager env) throws Exception {

		//Initialization
		JSONArray list = new JSONArray();

		//Parse all file readers
		for (Entry<String, Object> e : env.fileWriters.entrySet()) {

			//Concatenation
			list.add(e.getKey());

		}

		return list;

	}

	@SuppressWarnings("unchecked")
	public static JSONObject writer_closeall(EnvManager env) throws Exception {

		//Initialization
		JSONObject list = new JSONObject();
		JSONArray allWriters = writer_show(env);

		//Try to close
		try {

			//Parse all file writers
			for (int i=1;i<allWriters.size();i++) {
				
				//Get the current writer
				String currentWriterId = (String) allWriters.get(i);

				//Close each file writer
				writer_close(env, currentWriterId);

				//Concatenation
				list.put(currentWriterId, "Closed");

			}

			return list;

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String writer_open(String user, EnvManager env, String fileWriterId, String filePath, String appendMode, String type, String encoding) throws Exception {
		
		//All parameters can not be null
		if (fileWriterId==null || fileWriterId.equals("") || filePath==null || filePath.equals("") || appendMode==null || appendMode.equals("") || type==null || type.equals("")) {

			throw new Exception("Sorry, all arguments cannot be null or empty.");

		}

		type = type.toLowerCase();
		
		//Check if the type is TEXT | BINARY
		if (!type.toLowerCase().equals("text") && !type.toLowerCase().equals("binary")) {

			throw new Exception("Sorry, the type must be 'TEXT|BINARY'.");

		}

		//The append mode must be a boolean
		if (!appendMode.equals("1") && !appendMode.equals("0")) {

			throw new Exception("Sorry, the append mode must be a boolean 1 or 0.");

		}

		//Check if the file is a directory
		if (exist(filePath).equals("1") && is_directory(filePath).equals("1")) {

			throw new Exception("Sorry, '"+filePath+"' must be a file.");

		}

		//Close the file writer if already exist
		if (writer_exist(env, fileWriterId).equals("1")) {

			writer_close(env, fileWriterId);

		}
		
		boolean is_text = false;
		if (type.equals("text")) {
			
			is_text = true;
			
		}
		
		if (user!=null && !user.equals("") && is_server_conf(filePath) && !GroupManager.isGrantedUser("sys", user)) {
			throw new Exception("Sorry, to write 'server.conf', "+user+" must be granted with 'sys'.");
		}

		//Initialization
		File file = new File(filePath);

		try {
			
			if (is_text) {
	
				BufferedWriter fw = null;
	
				//Create the file writer
				if (appendMode.equals("0")) {
	
					//Overwrite the file
					file.createNewFile();
					fw = new BufferedWriter(
						    new OutputStreamWriter(
						        new FileOutputStream(filePath, false),
						        encoding
						    )
						);
	
				} else {
	
					//Create the file if don't exist
					if (!file.exists()) file.createNewFile();
	
					//Create the file writer
					fw = new BufferedWriter(
						    new OutputStreamWriter(
						        new FileOutputStream(filePath, true),
						        encoding
						    )
						);
	
				}
	
				//Put the file writer into the environment
				env.fileWriters.put(fileWriterId, fw);
	
				//Return true
				return "1";
				
			} else {
				
				FileOutputStream fis = null;

				//Create the file writer
				if (appendMode.equals("0")) {

					//Overwrite the file
					file.createNewFile();
					fis = new FileOutputStream(file, false);

				}
				else {

					//Create the file if don't exist
					if (!file.exists()) file.createNewFile();

					//Create the file writer
					fis = new FileOutputStream(file, true);

				}

				//Put the binary file writer into the environment
				env.fileWriters.put(fileWriterId, fis);

				//Return true
				return "1";
				
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String writer_flush(EnvManager env, String fileWriterId) throws Exception {

		//The file writer id cannot be null or empty
		if (writer_exist(env, fileWriterId).equals("0")) {

			throw new Exception("Sorry, the file writer does not exist.");

		}
		
		Object o = env.fileWriters.get(fileWriterId);
		
		boolean is_text = false;
		if (o instanceof BufferedWriter) {
			
			is_text = true;
			
		}

		//Try to flush
		try {
			
			if (is_text) {

				//Flush the file writer
				((BufferedWriter) env.fileWriters.get(fileWriterId)).flush();
	
				return "1";
				
			} else {
				
				//Flush the binary file writer
				((FileOutputStream) env.fileWriters.get(fileWriterId)).flush();

				return "1";
				
			}

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String writer_add_line(EnvManager env, String fileWriterId, String data) throws Exception {

		//The file writer id cannot be null or empty
		if (writer_exist(env, fileWriterId).equals("0")) {

			throw new Exception("Sorry, the file writer does not exist.");

		}

		//data cannot be null
		if (data==null) {

			throw new Exception("Sorry, string data cannot be null.");

		}
		
		Object o = env.fileWriters.get(fileWriterId);
		
		if (!(o instanceof BufferedWriter)) {
			
			throw new Exception("Sorry, this function must used with 'TEXT' mode.");
			
		}

		//Try to add
		try {

			//Add data in file
			((BufferedWriter) env.fileWriters.get(fileWriterId)).write(data);

			return "1";

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	public static String writer_add_bytes(EnvManager env, String fileWriterId, String data) throws Exception {

		//The binary file writer id cannot be null or empty
		if (writer_exist(env, fileWriterId).equals("0")) {

			throw new Exception("Sorry, the binary file writer does not exist.");

		}

		//data cannot be null
		if (data==null) {

			throw new Exception("Sorry, string data cannot be null.");

		}
		
		Object o = env.fileWriters.get(fileWriterId);
		
		if ((o instanceof BufferedWriter)) {
			
			throw new Exception("Sorry, this function must used with 'BINARY' mode.");
			
		}

		//Try to add
		try {

			//Get byte file from hex string flow
			byte[] b = Base64.decodeBase64(data);

			//Add data in file
			((FileOutputStream) env.fileWriters.get(fileWriterId)).write(b);
			
			return "1";

		} catch (Exception e) {

			//Generate an error
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	};

	//Get the folder size
	public static final long getFolderSize(File dir) {

		//Initialization
		long size = 0;

		//Parse the file
		for (File file : dir.listFiles()) {

			//Check if file or directory
			if (file.isFile())
				size += file.length();
			else
				size += getFolderSize(file);

		}

		//Return the size
		return size;

	}

	/**
	 * @method b64Read
	 * @description This function allow you to get a binary file in base 64 format
	 * @help Get in String ...
	 * @return "W0NPTk5FQ1RPUl0NClNvY2tldF90aW1lb3V0PTEwMDAwDQp[...]"
	 * 
	 * @param filePath: The file path (example: "conf/server.conf") - String - CANNOT BE NULL - CANNOT BE EMPTY
	 * @throws Exception 
	 */
	public static String b64Read(String filePath) throws Exception {

		//Initialization
		String result = "";

		//Try to encode to base 64
		try {

			//For base 64 encoder
			result = new String(Base64.encodeBase64Chunked(Files.readAllBytes(Paths.get(filePath))));
			
		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

		return result;
	}

	/**
	 * @method b64Write
	 * @description This function allow you to get an encoded binary file in base 64 format
	 * @help Base 64 in String ...
	 * @return true
	 * 
	 * @param b64Object: The string in base 64 format (example: "W0NPTk5FQ1RPUl0NClNvY2tldF90aW1lb3V0PTEwMDAwDQp[...]") - String - CANNOT BE NULL - CANNOT BE EMPTY
	 * @param filePath: The file path (example: "conf/server.conf") - String - CANNOT BE NULL - CANNOT BE EMPTY
	 * @throws Exception 
	 */
	public static boolean b64Write(String b64Object, String filePath) throws Exception {

		//Initialization
		boolean bool = true;
		OutputStream stream = null;

		try {

			// Note preferred way of declaring an array variable
			byte[] data = Base64.decodeBase64(b64Object);
			stream = new FileOutputStream(filePath);

			stream.write(data);

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Close the stream
			try {stream.close();} catch (Exception e) {}

		}

		return bool;
	}
	
	
}
