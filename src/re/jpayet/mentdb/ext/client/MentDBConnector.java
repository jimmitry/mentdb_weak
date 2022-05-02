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

package re.jpayet.mentdb.ext.client;

import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

//The Java connector
public class MentDBConnector {

	//Initialization
	String hostname = "localhost", login = "admin", password = "pwd", key = "pwd";
	public String subTunnels = "";
	int port = 4444, connectTimeout, readTimeout;
	public long idConnection = 0;
	public int pool_position = -1;
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	public String clientConnectionState = null, serverConnectionState = null, serverConnectionStateId = null;

	public Cipher cipherEncode = null;
	public Cipher cipherDecode = null;
	public Encoder b64encoder = null;
	public Decoder b64decoder = null;

	public MentDBConnector(String hostname, int port, int connectTimeout, int readTimeout, String key) throws Exception {

		//Initialization
		this.hostname = hostname;
		this.port = port;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.key = key;

		this.initConnection();

	}

	public void initConnection() throws Exception {

		//Try to connect and open in and out stream
		try {

			close();

			socket=new Socket();
			socket.connect(new InetSocketAddress(hostname,port), connectTimeout);
			socket.setSoTimeout(readTimeout);

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			byte[] keyData = key.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
			cipherEncode = Cipher.getInstance("Blowfish");
			cipherEncode.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			cipherDecode = Cipher.getInstance("Blowfish");
			cipherDecode.init(Cipher.DECRYPT_MODE, secretKeySpec);

		} catch (UnknownHostException e) {

			//An error was generated
			throw new Exception("MentDB: Don't know about host " + hostname);

		} catch (IOException e) {

			//An error was generated
			throw new Exception("MentDB: Couldn't get I/O for the connection to " + hostname);

		}

	}

	/**
	 * @method close
	 * @description Close the connection
	 */
	public void close() {

		//try to close the connector
		try {socket.close();} catch (Exception e) {};
		try {out.close();} catch (Exception e) {};
		try {in.close();} catch (Exception e) {};

	}

	public String encrypt(String data) {

		try {

			byte[] hasil = cipherEncode.doFinal(data.getBytes());
			return Base64.getEncoder().encodeToString(hasil);

		} catch (Exception e) {
			return "";
		}

	}

	public String decrypt(String data) {

		try {

			byte[] hasil = cipherDecode.doFinal(Base64.getDecoder().decode(data));

			return new String(hasil);

		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * @method connect
	 * @description Connect to MentDB server
	 * @return a string information about new connection
	 * 
	 * @param login : the login
	 * @param password : the password
	 * @throws Exception 
	 */
	public boolean connect(String login, String password) throws Exception {

		try {
			
			//Initialization
			this.login = login;
			this.password = password;
			String connectCmd = "login \""+this.login.replace("'", "\\\"")+"\" \""+this.password.replace("'", "\\\"")+"\" ;";
			
			boolean b = true;
			String fromServer = "";
			
			//Read the first line returned by the server
			try {
				fromServer = new String(Base64.getDecoder().decode(in.readLine()));
			} catch (Exception e) {
				
				throw new Exception("MentDB: Connection read timeout. Are you sure it's a MentDB Server? If so, you can increase the field 'readTimeout' ...");

			}
			
			try {

				//Get server description
				fromServer = get(fromServer, "1", ";");
				
			} catch (Exception e) {
				
				throw new Exception("MentDB: Sorry, this server is not a MentDB Server ...");

			}
			
			//Set the server connection state string
			serverConnectionState = fromServer.substring(1);
			
			out.println(Base64.getEncoder().encodeToString(login.getBytes()));
			
			out.println(encrypt(connectCmd));

			//Read the result of the user connection
			fromServer = decrypt(in.readLine());
			
			if (fromServer==null || fromServer.equals("")) {
				
				b = false;
				clientConnectionState = "Disconnected (Protocol error ... Are you sure of your login or your user key ? ...)";
				serverConnectionStateId = "Server respond null/empty (Protocol error ... Are you sure of your login or your user key ?)";
				
			} else {

				//Get the user connection state string
				clientConnectionState = fromServer.substring(1);
	
				if (fromServer.startsWith("1Connected:")) {
	
					this.idConnection = Long.parseLong(fromServer.substring(11));
	
				}
	
				//Check if the result is the code for close the connector
				switch (fromServer) {
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht0": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (Shutdown server ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht1": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (Session closed by user ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht2": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (User does not exist ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht3": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (Bad password ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht5": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (mentdb is a system user ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht7": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (Protocol error ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				case "1jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht": 
					//return false
					b = false;
					clientConnectionState = "Disconnected (Unknow error ...)";
					serverConnectionStateId = fromServer.substring(1);
					break;
	
				default: socket.setSoTimeout(0);
	
	
				}
				
			}

			return b;

		} catch (Exception f) {

			if (f.getMessage()==null) throw new Exception("MentDB: Exceeded session (Only one user on MentDB).");
			else throw f;

		}

	}

	/**
	 * @method execute
	 * @description Execute a MQL request
	 * @return the result
	 * 
	 * @param request : the MQL request
	 * @throws Exception 
	 */
	public String execute(String request) throws Exception {

		//Initialization
		String result = "", fromServer = "";
		request = LRTRIM(request);
		
		if (!request.toLowerCase().startsWith("begin")) {
			if (!request.endsWith(";")) request += " ;";
		}

		try {
			
			//Send the command to the server
			out.println(encrypt(request));

			//Read the result returned by the server
			fromServer = decrypt(in.readLine());
			
		} catch (Exception e) {
			
			close();
			throw new Exception("Loss of connection (<Session close>, <Session timeout> or <Unknow error>). ["+e.getMessage()+"]");

		}

		//Generate an exception if null
		if (fromServer==null || fromServer.equals("")) {

			close();
			throw new Exception("Loss of connection (<Session close>, <Session timeout> or <Unknow error>). [Server return empty]");

		}

		//1: Executed with successful, N: NULL value, 0: An error was generated
		if (fromServer.startsWith("1")) result = fromServer.substring(1);
		else if (fromServer.startsWith("N")) result = null;
		else if (fromServer.startsWith("0")) throw new Exception(fromServer.substring(1));

		return result;

	}

	private static String get(String atomList, String index, String separator) {

		//Prepare the list
		String copyAtomList=atomList;
		Pattern motif = Pattern.compile("["+separator+"]");
		copyAtomList="d"+separator+copyAtomList+separator+"f";

		//Split the list
		String[] ch = motif.split(copyAtomList, -1);

		//Return the atom
		return ch[Integer.parseInt(index)];

	}

	public static String LRTRIM(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return LTRIM(RTRIM(str));

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String LTRIM(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return str.replaceAll("^\\s+", "");

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

	public static String RTRIM(String str) {

		//Try to delete space characters
		try {

			//Return null if str is null
			if (str==null) {

				return null;

			} else {

				//Delete space characters
				return str.replaceAll("\\s+$", "");

			}

		} catch (Exception e) {

			//If an error was generated then return null
			return null;

		}

	}

}