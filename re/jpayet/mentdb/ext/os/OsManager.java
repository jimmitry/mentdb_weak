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

package re.jpayet.mentdb.ext.os;

import java.io.InputStream;
import java.net.InetAddress;

import org.json.simple.JSONArray;

import re.jpayet.mentdb.ext.json.JsonManager;

public class OsManager {

	public static String execute(String json, String min_wait) throws Exception {

		//Generate an error if null or empty
		if (json==null || json.equals("")) {
			
			throw new Exception("Sorry, the command JSON cannot be null or empty.");
		
		}
		
		try {
			
			Long.parseLong(min_wait);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the min wait must be a valid long.");
			
		}
		
		JSONArray cmds = (JSONArray) JsonManager.load(json);
		
		//Build the java string
		String[] cmdList = new String[cmds.size()];
		for(int i=0;i<cmds.size();i++) {
			
			cmdList[i] = (String) cmds.get(i);
			
		}
		
		//Initialization
		ProcessBuilder process = null;
		InputStream inStream = null;
		Process pr = null;
		
		//try to execute the command
		try {
			
			//Execute the command
			process = new ProcessBuilder(cmdList);
			process.redirectErrorStream(true);
			pr = process.start(); 
			inStream = pr.getInputStream();
			
			class thread extends Thread {

				public String result = null;
				InputStream inStream = null;
				
				thread(InputStream inStream) {

					this.inStream = inStream;
					
				}
				
			    public void run() {

			    	try {
			    	
						//Get the result
						int ch;
						
						StringBuffer outBuf = new StringBuffer();

						while ((ch = inStream.read()) != -1) {
							outBuf.append((char)ch + "");
						}
						
						result = outBuf.toString();
						
			    	} catch (Exception e) {
			    		
			    		
			    		
			    	}
					
			    }
			};
			thread resultThread = new thread(inStream);
			resultThread.start();
			
			Thread.sleep(Long.parseLong(min_wait));
			
			//Wait the end of the process
			pr.waitFor();
			
			//Return the result of the command
			if (pr.exitValue()!=0) throw new Exception(resultThread.result);
			else return resultThread.result;

		} catch (Exception e) {

			//If an error was generated then return the error message in a basic data object
			throw new Exception("Sorry, "+e.getMessage()+".");

		} finally {

			//Try to close or destroy all objects
			try {
				if (process != null) pr.destroy();
				if (inStream != null) inStream.close();
			} catch (Exception e) {}
			
		}

	}
	
	public static String hostname() throws Exception {

		try {

			//Return the host name
			return InetAddress.getLocalHost().getHostName();

		} catch (Exception e) {

			throw new Exception("Sorry, cannot get the hostname.");

		}

	}

	public static String name() {

		//Return the os name
		return System.getProperty("os.name");

	}

	public static String type() {

		//Return the os name
		if (System.getProperty("os.name").toLowerCase().indexOf("windows")>-1) return "Windows";
		else if (System.getProperty("os.name").toLowerCase().indexOf("mac os x")>-1) return "Mac OS X";
		else if (System.getProperty("os.name").toLowerCase().indexOf("linux")>-1) return "Linux";
		else return "unknown";

	}

	public static String user_dir() {

		//Return the user dir
		return System.getProperty("user.dir");

	}

	public static String user_home() {

		//Return the user home
		return System.getProperty("user.home");

	}

	public static String user_lang() {

		//Return the user language
		return System.getProperty("user.language");

	}

	public static String user_name() {

		//Return the user name
		return System.getProperty("user.name");

	}

	public static String user_timezone() {

		//Return the user time zone
		return System.getProperty("user.timezone");

	}

	public static String version() {

		//Return the version
		return System.getProperty("os.version");

	}

	public static String arch() {

		//Return the architecture
		return System.getProperty("os.arch");

	}

}
