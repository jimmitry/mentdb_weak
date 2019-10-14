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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.json.JsonManager;

public class SshManager {
	
	private static JSch ssh = new JSch();

	public static String connect(EnvManager env, String sessionId, String json) throws Exception {

		//Generate an error if session id is null or empty
		if (sessionId==null || sessionId.equals("")) {

			throw new Exception("Sorry, the ssh session id cannot be null or empty.");

		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String hostname = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String user = (String) conf.get("user");
		String password = (String) conf.get("password");
		String connectTimeout = (String) conf.get("connectTimeout");

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
		if (env.sshObj.containsKey(sessionId)) {

			throw new Exception("Sorry, the ssh session id '"+sessionId+"' already exist.");

		}

		Session session = null;

		//Try to create the session
		try {

			//Get the session
			session = ssh.getSession(user, hostname, Integer.parseInt(port));
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect(Integer.parseInt(connectTimeout));

			//Save the session
			env.sshObj.put(sessionId, session);

		} catch (Exception e) {

			//Try to close the session
			try {session.disconnect();} catch (Exception f) {};

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

		return "1";

	}
	
	public static String exec(EnvManager env, String sessionId, String shellCommand) throws Exception {

		//Generate an error if the session id does not exist
		if (!env.sshObj.containsKey(sessionId)) {

			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");

		}

		//Generate an error if the shell command is null or empty
		if (shellCommand==null || shellCommand.equals("")) {

			throw new Exception("Sorry, the shell command cannot be null or empty.");

		}

		//Try to execute
		try {

			String result = "<response>\n", tmpResult = "";
			Session session = ((Session) env.sshObj.get(sessionId));
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

			channelExec.setCommand(shellCommand);
			InputStream in = channelExec.getInputStream();
			InputStreamReader err=new InputStreamReader(channelExec.getErrStream());
			channelExec.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					tmpResult += new String(tmp, 0, i);
				}
				if (channelExec.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {}

			}
			if (tmpResult.endsWith("\n")) tmpResult = tmpResult.substring(0, tmpResult.length()-1);
			else if (tmpResult.endsWith("\r\n")) tmpResult = tmpResult.substring(0, tmpResult.length()-2);
			if (!tmpResult.equals("")) result += "<validLines>"+tmpResult.replace("<", "&lt;")+"</validLines>\n";
			BufferedReader reader=new BufferedReader(err);
			String incomingLine = "";
			tmpResult = "";
			while ((incomingLine=reader.readLine()) != null) {
				tmpResult += incomingLine+"\n";
			}
			reader.close();
			if (tmpResult.endsWith("\n")) tmpResult = tmpResult.substring(0, tmpResult.length()-1);
			else if (tmpResult.endsWith("\r\n")) tmpResult = tmpResult.substring(0, tmpResult.length()-2);
			if (!tmpResult.equals("")) result += "<errorLines>"+tmpResult.replace("<", "&lt;")+"</errorLines>\n";
			result += "<exitCode>" + channelExec.getExitStatus()+"</exitCode>\n</response>";

			channelExec.disconnect();

			return result;

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}
	
	public static String scp_from(EnvManager env, String sessionId, String remoteFile, String localFile) throws Exception {

		//Initialization
		FileOutputStream fos= null;
		OutputStream out = null;
		InputStream in = null;

		//Generate an error if the session id does not exist
		if (!env.sshObj.containsKey(sessionId)) {

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

		try{

			String prefix=null;
			String[] msgError = {""};
			if(new File(localFile).isDirectory()){
				prefix=localFile+File.separator;
			}

			Session session = ((Session) env.sshObj.get(sessionId));

			// exec 'scp -f rfile' remotely
			String command="scp -f "+remoteFile;
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);

			// get I/O streams for remote scp
			out=channel.getOutputStream();
			in=channel.getInputStream();

			channel.connect();

			byte[] buf=new byte[1024];

			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();

			while(true){
				int c=checkAck(in, msgError);
				if(c!='C'){
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize=0L;
				while(true){
					if(in.read(buf, 0, 1)<0){
						// error
						break; 
					}
					if(buf[0]==' ')break;
					filesize=filesize*10L+(long)(buf[0]-'0');
				}

				String file=null;
				for(int i=0;;i++){
					in.read(buf, i, 1);
					if(buf[i]==(byte)0x0a){
						file=new String(buf, 0, i);
						break;
					}
				}

				// send '\0'
				buf[0]=0; out.write(buf, 0, 1); out.flush();

				// read a content of lfile
				fos=new FileOutputStream(prefix==null ? localFile : prefix+file);
				int foo;
				while(true){
					if(buf.length<filesize) foo=buf.length;
					else foo=(int)filesize;
					foo=in.read(buf, 0, foo);
					if(foo<0){
						// error 
						break;
					}
					fos.write(buf, 0, foo);
					filesize-=foo;
					if(filesize==0L) break;
				}
				fos.close();
				fos=null;

				if(checkAck(in, msgError)!=0){
					throw new Exception(msgError[0]+"");
				}

				// send '\0'
				buf[0]=0; out.write(buf, 0, 1); out.flush();
			}

			return "1";

		} catch(Exception e) {

			//Close object
			try{if(fos!=null) fos.close();}catch(Exception ee){};
			try{if(in!=null) in.close();}catch(Exception ee){};
			try{if(out!=null) out.close();}catch(Exception ee){};

			//Generate an exception
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}
	
	public static String scp_to(EnvManager env, String sessionId, String localFile, String remoteFile) throws Exception {

		//Initialization
		FileInputStream fis = null;
		OutputStream out = null;
		InputStream in = null;

		//Generate an error if the session id does not exist
		if (!env.sshObj.containsKey(sessionId)) {

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

		try {

			Session session = ((Session) env.sshObj.get(sessionId));
			String[] msgError = {""};

			boolean ptimestamp = true;

			// exec 'scp -t rfile' remotely
			String command="scp " + (ptimestamp ? "-p" :"") +" -t "+remoteFile;
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);

			// get I/O streams for remote scp
			out=channel.getOutputStream();
			in=channel.getInputStream();

			channel.connect();

			if(checkAck(in, msgError)!=0){
				throw new Exception(msgError[0]+"");
			}

			File _lfile = new File(localFile);

			if(ptimestamp){
				command="T"+(_lfile.lastModified()/1000)+" 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
				out.write(command.getBytes()); out.flush();
				if(checkAck(in, msgError)!=0){
					throw new Exception(msgError[0]+"");
				}
			}

			// send "C0644 filesize filename", where filename should not include '/'
			long filesize=_lfile.length();
			command="C0644 "+filesize+" ";
			if(localFile.lastIndexOf('/')>0){
				command+=localFile.substring(localFile.lastIndexOf('/')+1);
			}
			else{
				command+=localFile;
			}
			command+="\n";
			out.write(command.getBytes()); out.flush();
			if(checkAck(in, msgError)!=0){
				throw new Exception(msgError[0]+"");
			}

			// send a content of lfile
			fis=new FileInputStream(localFile);
			byte[] buf=new byte[1024];
			while(true){
				int len=fis.read(buf, 0, buf.length);
				if(len<=0) break;
				out.write(buf, 0, len); //out.flush();
			}
			fis.close();
			fis=null;
			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			if(checkAck(in, msgError)!=0){
				throw new Exception(msgError[0]+"");
			}
			out.close();

			return "1";

		} catch(Exception e) {

			//Close object
			try{if(fis!=null) fis.close();}catch(Exception ee){};
			try{if(in!=null) in.close();}catch(Exception ee){};
			try{if(out!=null) out.close();}catch(Exception ee){};

			//Generate an exception
			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}

	static int checkAck(InputStream in, String[] msgError) throws IOException {
		int b=in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			msgError[0] = sb.toString();
		}
		return b;
	}
	
	public static String exec_one_cmd(EnvManager env, String sessionId, String shellCommand) throws Exception {

		//Generate an error if the session id does not exist
		if (!env.sshObj.containsKey(sessionId)) {

			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");

		}

		//Generate an error if the shell command is null or empty
		if (shellCommand==null || shellCommand.equals("")) {

			throw new Exception("Sorry, the shell command cannot be null or empty.");

		}

		//Try to execute
		try {

			String result = "", errorResult = "";
			Session session = ((Session) env.sshObj.get(sessionId));
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

			channelExec.setCommand(shellCommand);
			InputStream in = channelExec.getInputStream();
			InputStreamReader err=new InputStreamReader(channelExec.getErrStream());
			channelExec.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					result += new String(tmp, 0, i);
				}
				if (channelExec.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {}

			}
			if (result.endsWith("\n")) result = result.substring(0, result.length()-1);
			else if (result.endsWith("\r\n")) result = result.substring(0, result.length()-2);

			BufferedReader reader=new BufferedReader(err);
			String incomingLine = "";
			errorResult = "";
			while ((incomingLine=reader.readLine()) != null) {
				errorResult += incomingLine+"\n";
			}
			reader.close();
			if (errorResult.endsWith("\n")) errorResult = errorResult.substring(0, errorResult.length()-1);
			else if (errorResult.endsWith("\r\n")) errorResult = errorResult.substring(0, errorResult.length()-2);

			channelExec.disconnect();

			if (!errorResult.equals("")) {

				throw new Exception("Sorry, "+errorResult+".");

			} else return result;

		} catch (Exception e) {

			throw new Exception("Sorry, "+e.getMessage()+".");

		}

	}
	
	public static String disconnect(EnvManager env, String sessionId) throws Exception {

		//Initialization
		String result = "1";

		//Generate an error if the session id does not exist
		if (!env.sshObj.containsKey(sessionId)) {

			throw new Exception("Sorry, the session id '"+sessionId+"' does not exist.");

		}

		//Close the session
		try {

			try {((Session) env.sshObj.get(sessionId)).disconnect();} catch (Exception e) {};
			env.sshObj.remove(sessionId);

		} catch (Exception e) {

			throw new Exception("Sorry, cannot close the ssh session.");

		}

		return result;

	}
	
	public static String disconnectall(EnvManager env) throws Exception {

		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();

		//Get all keys to close
		for (Entry<String, Session> e : env.sshObj.entrySet()) {

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