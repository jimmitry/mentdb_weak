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

package re.jpayet.mentdb.ext.session;

import java.net.*;
import java.util.Base64;
import java.util.Vector;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;

import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandManager;
import re.jpayet.mentdb.core.db.command.CommandSyncAccess;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;

//The server threads
public class SessionThread extends Thread {

	//Initialization
	public Socket socket = null;
	public static long nbConnection = 0;
	public static long nbOpenConnection = 0;
	public long idConnection = 0;
	public long life = System.currentTimeMillis();
	public PrintWriter out = null;
	public BufferedReader in = null;
	public int nbExecution = 1;
	public String user = "";
	public String gridMenu = "BRANCH";
	public JSONObject groups = null;
	public String graph_type = null;
	public String graph_obj = null;
	public Vector<String> lock_records = new Vector<String>();
	public boolean isInterrupted = false;
	
	public Cipher cipherEncode = null;
	public Cipher cipherDecode = null;
	public Encoder b64encoder = null;
	public Decoder b64decoder = null;
	
	//The environment variable
	public EnvManager env = new EnvManager();

	//The constructor
	public SessionThread(String user, long idConnection) {

		//Set the default values
		super("ServerThread");

		this.idConnection = idConnection;
		this.user = user;

	}

	//The constructor
	public SessionThread(Socket socket) {

		//Set the default values
		super("ServerThread");
		
		if (socket!=null) {
		
			nbConnection++;
			if (SessionThreadAgent.allServerThread.containsKey(nbConnection)) nbConnection++;
			idConnection = nbConnection;
			this.socket = socket;
			
		}
		
	}

	//The constructor
	public SessionThread() {

		//Set the default values
		super("ServerThread");
		user = "mentdb";
		idConnection = 0;

	}
	
	public String encrypt(String data) {
        
        try {
        	
        		if (cipherEncode!=null) {
        		
	        		byte[] hasil = cipherEncode.doFinal(data.getBytes());
	
		        return b64encoder.encodeToString(hasil);
		        
        		} else return b64encoder.encodeToString(data.getBytes());
	        
        } catch (Exception e) {
        	return "";
        }
        
    }
     
	public String decrypt(String data) {
    	
        try {
        	
        		if (cipherEncode!=null) {
			
		        byte[] hasil = cipherDecode.doFinal(b64decoder.decode(data));
		        
		        return new String(hasil);
		        
        		} else return new String(b64decoder.decode(data));
	        
		} catch (Exception e) {
			return "";
		}
        
    }

	//Thread run method
	public void run() {
		
		//Increment the number of thread
		nbOpenConnection++;

		//Try to get streams
		try {
			
			//Out and in stream initialization
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//Initialization
			String inputLine;

			//Send the server version and others ... (the first data line)
			out.println(Base64.getEncoder().encodeToString(("1MentDB v_"+Start.version+", Connection id: "+idConnection).getBytes()));
			
			inputLine = in.readLine();
			inputLine = new String(Base64.getDecoder().decode(inputLine));
			
			byte[] keyData = Database.execute_admin_mql(null, "user secret_key \""+inputLine.replace("\"", "\\\"")+"\";").getBytes();
			
	        SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
			try {
		        cipherEncode = Cipher.getInstance("Blowfish");
		        cipherEncode.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				cipherDecode = Cipher.getInstance("Blowfish");
				cipherDecode.init(Cipher.DECRYPT_MODE, secretKeySpec);
				b64encoder = Base64.getEncoder();
				b64decoder = Base64.getDecoder();
			} catch (Exception e) {};
			
			boolean restricted_session = Database.execute_admin_mql(null, "group is granted user \""+inputLine.replace("\"", "\\\"")+"\" \"api-mql\";").equals("0");
			Vector<Vector<MQLValue>> parse = null;
			
			//Read data from client
			while ((inputLine = decrypt(in.readLine())) != null) {
				
				if(inputLine.isEmpty()) {
					inputLine = "exit;";
			    }
				
				//Reset the life counter
				life=System.currentTimeMillis();

				//The result container
				String result = "";
				
				//Try to evaluate the request
				try {
					
					//Evaluate the request
					
					if (restricted_session) {
						parse = Misc.splitRestrictedCommand(inputLine);
					} else {
						parse = Misc.splitCommand(inputLine);
					}
				    
					result = CommandManager.executeAllCommands(true, this, parse, this.env, null, null);
				    
					//Prepare the result
					if (result==null) {
						
						out.println(encrypt("N"));
					
					} else {
						
						//Reset the socket read timeout to 0 (infinite)
						if (nbExecution==1) {
							
							socket.setSoTimeout(0);
						
						}
						
						out.println(encrypt("1"+result));
					
					}

				} catch (Exception e) {
					
					//Protocol connection error
					if (nbExecution==1) {
						
						result = "jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht7";
						out.println(encrypt("1"+result));
					
					} else {
						
						//An error was generated
						if (e.getMessage()==null) out.println(encrypt("0 unexpected error (Null returned)."));
						else out.println(encrypt("0"+(e.getMessage()+"")));
						
					}

				}
				
				//Reset the interruption
				isInterrupted = false;

				//Increment the number of execution
				nbExecution++;

				//Break if the exit error code was returned
				if (result!=null && result.startsWith("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht")) {
					break;
				}
				
				//Reset the life counter
				life=System.currentTimeMillis();

			}

			//Close the socket
			socket.close();

		} catch (Exception e) {
			
			//Nothing to do ...
			
		} finally {
			
			if (SessionThreadAgent.allServerThread.containsKey(idConnection)) SessionThreadAgent.allServerThread.remove(idConnection);
			
			if (idConnection>0) {
				
				closeSession(env, idConnection);
				
			}
			
			//Decrement the number of thread
			nbOpenConnection--;
			
			//Close out and in stream
			try {in.close();} catch (Exception e) {};
			try {out.close();} catch (Exception e) {};
			try {socket.close();} catch (Exception e) {};
			
		}
		
	}
	
	public static void closeEnv(EnvManager env, long idConnection) {
		
		try {
			CommandSyncAccess.execute(idConnection, null, null, null, 8, null, null, env, null, null, null);
		} catch (Exception e) {
		}
		
	}
	
	public static void closeSession(EnvManager env, long idConnection) {
		
		try {
			CommandSyncAccess.execute(idConnection, null, null, null, 7, null, null, env, null, null, null);
		} catch (Exception e) {
		}
		
	}

}