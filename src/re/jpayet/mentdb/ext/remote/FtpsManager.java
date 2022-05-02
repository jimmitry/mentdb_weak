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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class FtpsManager {
	
	public static String connect(EnvManager env, String sessionId, String json) throws Exception {
		
		//Generate an error if the session is null or empty
		if (sessionId==null || sessionId.equals("")) {
			
			throw new Exception("Sorry, the ftps session id cannot be null or empty.");
			
		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");
		String dataTimeout = (String) conf.get("dataTimeout");
		String keepAliveTimeout = (String) conf.get("keepAliveTimeout");
		String isImplicit = (String) conf.get("isImplicit");
		String protocol = (String) conf.get("protocol");
		
		//Generate an error if the connect timeout is not valid
		try {

			if (Integer.parseInt(connectTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the connect timeout is not valid.");

		}
		
		//Generate an error if the data timeout is not valid
		try {

			if (Integer.parseInt(dataTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the data timeout is not valid.");

		}
		
		//Generate an error if the keep alive timeout is not valid
		try {

			if (Integer.parseInt(keepAliveTimeout)<0) {
				
				throw new Exception("err");
				
			}

		} catch (Exception e) {

			throw new Exception("Sorry, the keep alive timeout is not valid.");

		}
		
		//Generate an error if host name is null or empty
		if (hostname==null || hostname.equals("")) {
			
			throw new Exception("Sorry, the host name cannot be null or empty.");
			
		}
		
		//Generate an error if isImplicit is null or empty
		if (isImplicit==null || isImplicit.equals("")) {
			
			throw new Exception("Sorry, the isImplicit cannot be null or empty.");
			
		}
		
		//Generate an error if isImplicit is not valid
		if (!isImplicit.equals("1") && !isImplicit.equals("0")) {
			
			throw new Exception("Sorry, the isImplicit must be 1 or 0.");
			
		}
		
		//Generate an error if protocol is null or empty
		if (protocol==null || protocol.equals("")) {
			
			throw new Exception("Sorry, the protocol cannot be null or empty.");
			
		}
		
		//Generate an error if protocol is not valid
		if (!protocol.equals("TLS") && !protocol.equals("SSL")) {
			
			throw new Exception("Sorry, the protocol must be TLS or SSL.");
			
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
		if (env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the ftps session id '"+sessionId+"' already exist.");
			
		}
		
		FTPSClient session = null;
		if (isImplicit.equals("1")) session = new FTPSClient(protocol, true);
		else session = new FTPSClient(protocol, false);
		session.setAutodetectUTF8(true);
		session.setRemoteVerificationEnabled(false);
		session.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
		session.setControlKeepAliveTimeout(Long.parseLong(keepAliveTimeout));
		session.setConnectTimeout(Integer.parseInt(connectTimeout));
		session.setDataTimeout(Integer.parseInt(dataTimeout));
		
		//Try to create the session
		try {
	        
	        //Connect to the server
	        session.connect(hostname, Integer.parseInt(port));
	        
	        // After connection attempt, you should check the reply code to verify if success.
            int reply = session.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
            	
	            	//Close the session
	    			session.disconnect();
                
            } else {
            		
	            	if (session.login(user, password)) {
	            		
	        			//Save the session
	        			env.ftpsObj.put(sessionId, session);
	            		
	            	} else {
	            		
	            		throw new Exception("FTPS login failed");
	            		
	            	}
            	
            }
		
		} catch (Exception e) {
			
			//Try to close the session
			try {session.logout();} catch (Exception f) {};
			try {session.disconnect();} catch (Exception f) {};
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";

	}
	
	public static String parse_pbsz(EnvManager env, String sessionId, String pbsz) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		try {
			
			Integer.parseInt(pbsz);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the buffer size must be a number.");
			
		}
		
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.execPBSZ(Integer.parseInt(pbsz));
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String exec_prot(EnvManager env, String sessionId, String prot) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if protocol is null or empty
		if (prot==null || prot.equals("")) {
			
			throw new Exception("Sorry, the protocol cannot be null or empty.");
			
		}
		
		//Generate an error if protocol is not valid
		if (!prot.equals("C") && !prot.equals("S") && !prot.equals("E") && !prot.equals("P")) {
			
			throw new Exception("Sorry, the protocol must be C|S|E|P.");
			
		}
		
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.execPROT(prot);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String active(EnvManager env, String sessionId) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to set the active mode
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.enterLocalActiveMode();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String passive(EnvManager env, String sessionId) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to set the passive mode
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.enterLocalPassiveMode();

		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String set_file_type(EnvManager env, String sessionId, String transferType) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the transferType is null or empty
		if (transferType==null || transferType.equals("")) {
			
			throw new Exception("Sorry, the bool cannot be null or empty.");
			
		}
		
		//Generate an error if the type field is not valid
		if (!transferType.equals("BINARY") && !transferType.equals("LOCAL") && !transferType.equals("ASCII") && !transferType.equals("EBCDIC")) {
			
			throw new Exception("Sorry, the type field must be BINARY|LOCAL|ASCII|EBCDIC.");
			
		}
		
		//Try to set
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
			
			if (transferType.equals("BINARY")) ftps.setFileType(FTP.BINARY_FILE_TYPE);
			else if (transferType.equals("LOCAL")) ftps.setFileType(FTP.LOCAL_FILE_TYPE);
			else if (transferType.equals("ASCII")) ftps.setFileType(FTP.ASCII_FILE_TYPE);
			else if (transferType.equals("EBCDIC")) ftps.setFileType(FTP.EBCDIC_FILE_TYPE);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String cd(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to go
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.changeWorkingDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String pwd(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "";
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get the current directory location
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        result = ftps.printWorkingDirectory();
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String mkdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to create
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.makeDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray ls_files(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get files
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
            
	        FTPFile[] files = ftps.listFiles();
	        
	        //Parse all files
	        for (FTPFile file : files) {
	        	
		        	JSONObject o = new JSONObject();
	        		o.put("name", file.getName());
	        		o.put("isDir", "0");
	        		o.put("size", ""+file.getSize());
            		o.put("lastModified", DateFx.long_to_ts(""+file.getTimestamp().getTimeInMillis()));
	        		result.add(o);
	            
	        }
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray ls_files(EnvManager env, String sessionId, final String regex) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get files
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
			
	        //Filter
			FTPFileFilter filter = new FTPFileFilter() {
				 
                @Override
                public boolean accept(FTPFile ftpFile) {
 
                    return (ftpFile.isFile() && ftpFile.getName().matches(regex));
                }
            };
            
	        FTPFile[] files = ftps.listFiles(".", filter);	        

	        //Parse all files
	        for (FTPFile file : files) {
	        	
		        	JSONObject o = new JSONObject();
	        		o.put("name", file.getName());
	        		o.put("isDir", "0");
	        		o.put("size", ""+file.getSize());
            		o.put("lastModified", DateFx.long_to_ts(""+file.getTimestamp().getTimeInMillis()));
	        		result.add(o);
	            
	        }
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray ls_dirs(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Try to get files
		try {
			
			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));

	        //Filter
	        FTPFile[] files = ftps.listDirectories();

	        //Parse all files
	        for (FTPFile file : files) {
	        	
	        	result.add(file.getName());
	            
	        }
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String rename(EnvManager env, String sessionId, String oldFile, String newFile) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
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

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.rename(oldFile, newFile);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
	}
	
	public static String put(EnvManager env, String sessionId, String localFile, String remoteFile, String mode) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
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
		if (!mode.equals("APPEND") && !mode.equals("RESUME") ) {
			
			throw new Exception("Sorry, the overWrite field must be APPEND or RESUME.");
			
		}
		
		FileInputStream fis = null;
		
		//Try to upload
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
			
			fis = new FileInputStream(localFile);
			
			if (mode.equals("APPEND")) ftps.appendFile(remoteFile, (InputStream) fis);
			else ftps.storeFile(remoteFile, (InputStream) fis);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		} finally {
			
			try {
				fis.close();
			} catch(Exception f) {};
			
		}
		
	}
	
	public static String get(EnvManager env, String sessionId, String remoteFile, String localFile) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
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
		
		FileOutputStream fos = null;
		
		//Try to download
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
			
			fos = new FileOutputStream(localFile);
			ftps.retrieveFile(remoteFile, fos);
	        
	        return "1";
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		} finally {
			
			try {
				fos.close();
			} catch(Exception f) {};
			
		}
		
	}
	
	public static String rm(EnvManager env, String sessionId, String file) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the file is null or empty
		if (file==null || file.equals("")) {
			
			throw new Exception("Sorry, the file cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.deleteFile(file);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String rmdir(EnvManager env, String sessionId, String directory) throws Exception {
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Generate an error if the directory is null or empty
		if (directory==null || directory.equals("")) {
			
			throw new Exception("Sorry, the directory cannot be null or empty.");
			
		}
		
		//Try to remove
		try {

			FTPSClient ftps = ((FTPSClient) env.ftpsObj.get(sessionId));
	        
	        ftps.removeDirectory(directory);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return "1";
		
	}
	
	public static String disconnect(EnvManager env, String sessionId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the session id does not exist
		if (!env.ftpsObj.containsKey(sessionId)) {
			
			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");
			
		}
		
		//Close the session
		try {


			try {((FTPSClient) env.ftpsObj.get(sessionId)).logout();} catch (Exception f) {};
			try {((FTPSClient) env.ftpsObj.get(sessionId)).disconnect();} catch (Exception e) {};
			env.ftpsObj.remove(sessionId);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot close the ftps session.");
			
		}
		
		return result;
		
	}
	
	public static String disconnectall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, FTPSClient> e : env.ftpsObj.entrySet()) {
			
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