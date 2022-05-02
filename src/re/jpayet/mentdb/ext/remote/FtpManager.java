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

package re.jpayet.mentdb.ext.remote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;

public class FtpManager {

	public static String set_timeout(EnvManager env, String connectTimeout) throws Exception {
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Try to set the timeout
		try {
	        
	        //Set the parameter
	        System.setProperty("ftp4j.activeDataTransfer.acceptTimeout", connectTimeout);
	        
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";

	}
	
	public static String connect(EnvManager env, String sessionId, String json) throws Exception {
		
		//Generate an error if session id is null or empty
		if (sessionId==null || sessionId.equals("")) {
			
			throw new Exception("Sorry, the ftp session id cannot be null or empty.");
			
		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		
		//Generate an error if host name is null or empty
		if (hostname==null || hostname.equals("")) {
			
			throw new Exception("Sorry, the host name cannot be null or empty.");
			
		}
		
		//Generate an error if the port is not a number
		try {

			Integer.parseInt(port);

		} catch (Exception e) {

			throw new Exception("Sorry, the port must be numbers.");

		}
		
		//Generate an error if user is null or empty
		if (user==null || user.equals("")) {
			
			throw new Exception("Sorry, the user cannot be null or empty.");
			
		}
		
		//Generate an error if password is null or empty
		if (password==null || password.equals("")) {
			
			throw new Exception("Sorry, the password cannot be null or empty.");
			
		}
		
		//Generate an error if the session id already exist
		if (env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the ftp session id '"+sessionId+"' already exist.");
			
		}
		
		FTPClient session = new FTPClient();
		
		//Try to create the session
		try {
	        
	        //Connect to the server
	        session.connect(hostname, Integer.parseInt(port));
	        session.login(user, password);
	        
			//Save the session
			env.ftpObj.put(sessionId, session);
		
		} catch (Exception e) {
			
			//Try to close the session
			try {session.disconnect(false);} catch (Exception f) {};
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";

	}
	
	public static String set_type(EnvManager env, String sessionId, String type) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the type is null or empty
		if (type==null || type.equals("")) {
			
			throw new Exception("Sorry, the type cannot be null or empty.");
			
		}
		
		//Generate an error if the type field is not TEXTUAL or BINARY or AUTO
		if (!type.equals("TEXTUAL") && !type.equals("BINARY") && !type.equals("AUTO")) {
			
			throw new Exception("Sorry, the type field must be TEXTUAL, BINARY or AUTO.");
			
		}
		
		//Try to set
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
			if (type.equals("TEXTUAL")) ftp.setType(FTPClient.TYPE_TEXTUAL);
			else if (type.equals("BINARY")) ftp.setType(FTPClient.TYPE_BINARY);
			else ftp.setType(FTPClient.TYPE_AUTO);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String set_compression(EnvManager env, String sessionId, String bool) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the bool is null or empty
		if (bool==null || bool.equals("")) {
			
			throw new Exception("Sorry, the bool cannot be null or empty.");
			
		}
		
		//Generate an error if the bool field is not 1 or 0
		if (!bool.equals("1") && !bool.equals("0")) {
			
			throw new Exception("Sorry, the type field must be 1 or 0.");
			
		}
		
		//Try to set
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
			if (bool.equals("1")) ftp.setCompressionEnabled(true);
			else ftp.setCompressionEnabled(false);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String active(EnvManager env, String sessionId) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to set the active mode
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.setPassive(false);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String passive(EnvManager env, String sessionId) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to set the passive mode
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.setPassive(true);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String cd(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to go
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.changeDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String pwd(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get the current directory location
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        result = ftp.currentDirectory();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String mkdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to create
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.createDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray ls(EnvManager env, String sessionId, String fileFilterPath) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if file filter path is null
		if (fileFilterPath==null) {
			
			throw new Exception("Sorry, the file filter path cannot be null.");
			
		}
		
		//Try to get files
		try {
			
			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
			
			//Filter
	        FTPFile[] files = ftp.list(fileFilterPath);
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        //Parse all files
	        for (FTPFile file : files) {
	        	
	        		//Check if is a directory
	            if (file.getType()==FTPFile.TYPE_DIRECTORY) {
	            	
	            		JSONObject o = new JSONObject();
	            		o.put("name", file.getName());
	            		o.put("isDir", "1");
	            		o.put("lastModified", formatter.format(file.getModifiedDate()));
	            		result.add(o);
	                
	            } else {
	            	
	            		JSONObject o = new JSONObject();
	            		o.put("name", file.getName());
	            		o.put("isDir", "0");
	            		o.put("lastModified", formatter.format(file.getModifiedDate()));
	            		o.put("size", ""+file.getSize());
	            		result.add(o);
	            	
	            }
	            
	        }
	        
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String rename(EnvManager env, String sessionId, String oldFile, String newFile) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the old file is null or empty
		if (oldFile==null || oldFile.equals("")) {
			
			throw new Exception("Sorry, the old file cannot be null or empty.");
			
		}
		
		//Generate an error if the new file is null or empty
		if (newFile==null || newFile.equals("")) {
			
			throw new Exception("Sorry, the new file cannot be null or empty.");
			
		}
		
		//Try to rename
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.rename(oldFile, newFile);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String put(EnvManager env, String sessionId, String localFile, String mode) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the local file is null or empty
		if (localFile==null || localFile.equals("")) {
			
			throw new Exception("Sorry, the local file cannot be null or empty.");
			
		}
		
		//Generate an error if the mode field is null or empty
		if (mode==null || mode.equals("")) {
			
			throw new Exception("Sorry, the mode field cannot be null or empty.");
			
		}
		
		//Generate an error if the mode field is not APPEND or RESUME
		if (!mode.equals("APPEND") && !mode.equals("RESUME") ) {
			
			throw new Exception("Sorry, the overWrite field must be APPEND or RESUME.");
			
		}
		
		//Try to upload
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));

			if (mode.equals("APPEND")) ftp.append(new File(localFile));
			else ftp.upload(new File(localFile));
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String get(EnvManager env, String sessionId, String remoteFile, String localFile) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the local file is null or empty
		if (localFile==null || localFile.equals("")) {
			
			throw new Exception("Sorry, the local file cannot be null or empty.");
			
		}
		
		//Generate an error if the remote file is null or empty
		if (remoteFile==null || remoteFile.equals("")) {
			
			throw new Exception("Sorry, the remote file cannot be null or empty.");
			
		}
		
		//Try to download
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));

			ftp.download(remoteFile, new File(localFile));
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String rm(EnvManager env, String sessionId, String file) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the file is null or empty
		if (file==null || file.equals("")) {
			
			throw new Exception("Sorry, the file cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.deleteFile(file);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String rmdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			FTPClient ftp = ((FTPClient) env.ftpObj.get(sessionId));
	        
	        ftp.deleteDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String disconnect(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the session id does not exist
		if (!env.ftpObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Close the session
		try {

			try {((FTPClient) env.ftpObj.get(sessionId)).disconnect(true);} catch (Exception e) {};
			env.ftpObj.remove(sessionId);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot close the ftp session.");
			
		}
		
		return result;
		
	}
	
	public static String disconnectall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, FTPClient> e : env.ftpObj.entrySet()) {
			
			allKeysToDelete.add(e.getKey());
			
		}
		
		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {
			
			try {
			
				//Close the session
				disconnect(env, allKeysToDelete.get(i));
				nbClosed++;
				
			} catch (Exception e) {
				
				//Nothing to do
				
			}
			
		}
		
		return ""+nbClosed;
		
	}

}