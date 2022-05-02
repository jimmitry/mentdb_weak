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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;

public class SftpManager {
	
	private static JSch ssh = new JSch();
	
	public static String connect(EnvManager env, String sessionId, String json) throws Exception {
		
		//Generate an error if session id is null or empty
		if (sessionId==null || sessionId.equals("")) {
			
			throw new Exception("Sorry, the sftp session id cannot be null or empty.");
			
		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String dataTimeout = (String) conf.get("dataTimeout");
		
		//Generate an error if hostname is null or empty
		if (hostname==null || hostname.equals("")) {
			
			throw new Exception("Sorry, the host name cannot be null or empty.");
			
		}
		
		//Generate an error if the port is not a number
		try {

			Integer.parseInt(port);

		} catch (Exception e) {

			throw new Exception("Sorry, the port must be numbers.");

		}
		
		//Generate an error if the data transfer timeout is not valid
		try {

			if (Integer.parseInt(dataTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if user is null or empty
		if (user==null || user.equals("")) {
			
			throw new Exception("Sorry, the user cannot be null or empty.");
			
		}
		
		//Generate an error if password is null or empty
		if (password==null || password.equals("")) {
			
			throw new Exception("Sorry, the password cannot be null or empty.");
			
		}
		
		//Generate an error if the session already exist
		if (env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the sftp session id '"+sessionId+"' already exist.");
			
		}
		
		Session session = null;
		Channel channel = null;
		
		//Try to create the session
		try {
			
			//Get the session
			session = ssh.getSession(user, hostname, Integer.parseInt(port));
			java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
	        session.setPassword(password);
	        session.connect(Integer.parseInt(connectTimeout));
	        session.setTimeout(Integer.parseInt(dataTimeout));
	        channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftp = (ChannelSftp) channel;
			
			//Save the session
			env.sftpObj1.put(sessionId, session);
			env.sftpObj2.put(sessionId, channel);
			env.sftpObj3.put(sessionId, sftp);
		
		} catch (Exception e) {
			
			//Try to close the session
			try {session.disconnect();} catch (Exception f) {};
			try {channel.disconnect();} catch (Exception f) {};
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";

	}
	
	public static String cd(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to go
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.cd(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String lcd(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to go
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.lcd(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String pwd(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get the current directory location
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        result = sftp.pwd();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String lpwd(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get the current directory location
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        result = sftp.lpwd();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String mkdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to create
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.mkdir(directory);
		
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
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if file filter path is null
		if (fileFilterPath==null) {
			
			throw new Exception("Sorry, the file filter path cannot be null.");
			
		}
		
		//Try to get files
		try {

	        ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        //Filter
			Vector<ChannelSftp.LsEntry> files = sftp.ls(fileFilterPath);

	        //Parse all files
	        for (ChannelSftp.LsEntry file : files) {
	        	
	        	//Check if it is a directory
	            if (file.getAttrs().isDir()) {
	            	
		            	JSONObject o = new JSONObject();
	            		o.put("name", file.getFilename());
	            		o.put("isDir", "1");
	            		result.add(o);
	                
	            } else {
	            		
		            	JSONObject o = new JSONObject();
	            		o.put("name", file.getFilename());
	            		o.put("isDir", "0");
	            		o.put("size", ""+file.getAttrs().getSize());
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
		if (!env.sftpObj1.containsKey(sessionId)) {
			
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

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.rename(oldFile, newFile);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String gethome(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get home
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        result = sftp.getHome();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String getversion(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get version
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        result = ""+sftp.getServerVersion();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String put(EnvManager env, String sessionId, String localFile, String remoteFile, String mode) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
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
		
		//Generate an error if the mode field is null or empty
		if (mode==null || mode.equals("")) {
			
			throw new Exception("Sorry, the mode field cannot be null or empty.");
			
		}
		
		//Generate an error if the mode field is not valid
		if (!mode.equals("OVERWRITE") && !mode.equals("APPEND") && !mode.equals("RESUME") ) {
			
			throw new Exception("Sorry, the overWrite field must be OVERWRITE, APPEND or RESUME.");
			
		}
		
		//Try to upload
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));

			File f = new File(localFile);
			if (mode.equals("OVERWRITE")) sftp.put(new FileInputStream(f), remoteFile, ChannelSftp.OVERWRITE);
			else if (mode.equals("APPEND")) sftp.put(new FileInputStream(f), remoteFile, ChannelSftp.APPEND);
			else sftp.put(new FileInputStream(f), remoteFile);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String get(EnvManager env, String sessionId, String remoteFile, String localFile) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
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

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));

			File f = new File(localFile);
			sftp.get(remoteFile, new FileOutputStream(f));
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String rm(EnvManager env, String sessionId, String file) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the file is null or empty
		if (file==null || file.equals("")) {
			
			throw new Exception("Sorry, the file cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.rm(file);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String rmdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			ChannelSftp sftp = ((ChannelSftp) env.sftpObj3.get(sessionId));
	        
	        sftp.rmdir(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String disconnect(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the session id does not exist
		if (!env.sftpObj1.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Close the session
		try {

			try {((Session) env.sftpObj1.get(sessionId)).disconnect();} catch (Exception e) {};
			env.sftpObj1.remove(sessionId);
			try {((Channel) env.sftpObj2.get(sessionId)).disconnect();} catch (Exception e) {};
			env.sftpObj2.remove(sessionId);
			try {((ChannelSftp) env.sftpObj3.get(sessionId)).disconnect();} catch (Exception e) {};
			env.sftpObj3.remove(sessionId);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot close the sftp session.");
			
		}
		
		return result;
		
	}
	
	public static String disconnectall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, Session> e : env.sftpObj1.entrySet()) {
			
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