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

package re.jpayet.mentdb.ext.mail;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.Normalizer;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.mail.util.BASE64DecoderStream;

import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.tools.Misc;

public class Pop3Manager {

	@SuppressWarnings("unchecked")
	public static String parse(String output_dir, String nbMsgToDownload, String deleteMsgAfterDownload, String json) throws Exception {

		//Generate an error if the output directory is null or empty
		if (output_dir==null || output_dir.equals("")) {

			throw new Exception("Sorry, the output directory cannot be null or empty.");

		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String server = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String username = (String) conf.get("user");
		String password = (String) conf.get("password");
		String starttls = (String) conf.get("tls");
		String ssl = (String) conf.get("ssl");
		String cnttimeout = (String) conf.get("connectTimeout");
		String timeout = (String) conf.get("sessionTimeout");

		//Generate an error if the server name is null or empty
		if (server==null || server.equals("")) {

			throw new Exception("Sorry, the hostname cannot be null or empty.");

		}

		//The port number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(port);

		} catch (Exception e) {

			throw new Exception("Sorry, the port must be a number.");

		}

		//The nbMaxMsg number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(nbMsgToDownload);

		} catch (Exception e) {

			throw new Exception("Sorry, the number of message max must be a number.");

		}

		//The timeout number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(timeout);

		} catch (Exception e) {

			throw new Exception("Sorry, the timeout must be a number.");

		}

		//The cnttimeout number must be a number
		try {

			@SuppressWarnings("unused")
			int i = Integer.parseInt(cnttimeout);

		} catch (Exception e) {

			throw new Exception("Sorry, the cnttimeout must be a number.");

		}

		//Generate an error if the user name is null or empty
		if (username==null || username.equals("")) {

			throw new Exception("Sorry, the user name cannot be null or empty.");

		}

		//Generate an error if the password is null or empty
		if (password==null || password.equals("")) {

			throw new Exception("Sorry, the password cannot be null or empty.");

		}

		//Generate an error if the ssl field is not valid (must be 1 or 0)
		if (ssl==null || (!ssl.equals("1") && !ssl.equals("0"))) {

			throw new Exception("Sorry, SSL field must be 1 or 0.");

		}

		//Generate an error if the starttls field is not valid (must be 1 or 0)
		if (starttls==null || (!starttls.equals("1") && !starttls.equals("0"))) {

			throw new Exception("Sorry, STARTTLS field must be 1 or 0.");

		}

		//Generate an error if the deleteMsg field is not valid (must be 1 or 0)
		if (deleteMsgAfterDownload==null || (!deleteMsgAfterDownload.equals("1") && !deleteMsgAfterDownload.equals("0"))) {

			throw new Exception("Sorry, deleteMsgAfterDownload field must be 1 or 0.");

		}

		//Initialization
		int nbMessageReceived = 0;
		Folder folder = null;
		Store store = null;
		int tot = 0;
		String ts_dir = "";
		
		Properties props = new Properties();
		
		String[] logString = {""};
		LogOutputStream losStdOut  = new LogOutputStream() {
			
			@Override
		    protected void processLine(String line, int level) {
				logString[0] += line+"\n";
		    }
		};
		
		try {
			
			//Load the session
			props.setProperty("mail.pop3.connectionpooltimeout", cnttimeout);
			props.setProperty("mail.pop3.connectiontimeout", cnttimeout);
			props.setProperty("mail.pop3.timeout", timeout);
			props.setProperty("mail.pop3.port", port);
			
			if (ssl.equals("1")) {
				props.setProperty( "mail.pop3.ssl.enable", "true");
			} else {
				props.setProperty( "mail.pop3.ssl.enable", "false");
			}
			if (starttls.equals("1")) {
				props.setProperty("mail.pop3.starttls.enable", "true");
			} else {
				props.setProperty("mail.pop3.starttls.enable", "false");
			}
			
			//props.put("mail.debug", "true");
			Session session = Session.getDefaultInstance(props);
			
			session.setDebugOut(new PrintStream(losStdOut));
			session.setDebug(true);
			
			store = session.getStore("pop3");
			
			//Connect to the server
			store.connect(server, Integer.parseInt(port), username, password);
			
			//Get the INBOX directory
			folder = store.getDefaultFolder().getFolder("INBOX");
			
			//Open in read only mode
			folder.open(Folder.READ_WRITE);
			
			//Get all messages
			Message[] messages = folder.getMessages();
			
			int unreadMessageCount = folder.getUnreadMessageCount();
			
			//Get the current timestamp
			if (!output_dir.endsWith("/")) output_dir+="/";
			ts_dir = output_dir+DateFx.systimestamp_min();
			
			//Create the directory
			if (!(new File(ts_dir)).exists()) (new File(ts_dir)).mkdir();
			
			//Parse all messages
			for (int i=0; i < messages.length; ++i) {
				
				//Break if all unread message was retrieved
				if (i>=unreadMessageCount || i>=Integer.parseInt(nbMsgToDownload)) {
					
					break;
					
				}
				
				//Get the current message
				Message msg = messages[i];
				
				JSONObject meta = new JSONObject();
				JSONArray replyTo = new JSONArray();
				meta.put("ReplyTo", replyTo);
				JSONArray AllRecipients = new JSONArray();
				meta.put("Recipients", AllRecipients);
				JSONArray From = new JSONArray();
				meta.put("From", From);
				JSONArray Parts = new JSONArray();
				meta.put("Parts", Parts);
				
				for(int z=0;z<msg.getReplyTo().length;z++) {
					
					try {replyTo.add(msg.getReplyTo()[z].toString());} catch (Exception zz) {};
					
				}
				
				for(int z=0;z<msg.getAllRecipients().length;z++) {
					
					try {AllRecipients.add(msg.getAllRecipients()[z].toString());} catch (Exception zz) {};
					
				}
				
				for(int z=0;z<msg.getFrom().length;z++) {
					
					try {From.add(msg.getFrom()[z].toString());} catch (Exception zz) {};
					
				}
				
				try {meta.put("ContentType", msg.getContentType());} catch (Exception zz) {};
				try {meta.put("Description", msg.getDescription());} catch (Exception zz) {};
				try {meta.put("Disposition", msg.getDisposition());} catch (Exception zz) {};
				try {meta.put("FileName", msg.getFileName());} catch (Exception zz) {};
				try {meta.put("LineCount", msg.getLineCount());} catch (Exception zz) {};
				try {meta.put("MessageNumber", msg.getMessageNumber());} catch (Exception zz) {};
				try {meta.put("ReceivedDate", msg.getReceivedDate().toString());} catch (Exception zz) {};
				try {meta.put("SentDate", msg.getSentDate().toString());} catch (Exception zz) {};
				try {meta.put("Size", msg.getSize());} catch (Exception zz) {};
				try {meta.put("Subject", msg.getSubject());} catch (Exception zz) {};
				
				//Save the parts
				saveParts(msg, msg.getContent(), ts_dir+"/"+(i+1), 1, Parts);
				
				//Save the from
				Misc.create(ts_dir+"/mail"+(i+1)+".json", JsonManager.format_Gson(meta.toJSONString()));
				
				//Delete the message
				if (deleteMsgAfterDownload.equals("1")) {
					
					msg.setFlag(Flags.Flag.DELETED, true);
				
				}
				
				nbMessageReceived++;
				
			}
			
			//Close the folder
			folder.close(true);
			
			//Open in read only mode
			folder.open(Folder.READ_ONLY);
			
			tot = folder.getMessageCount();
			
		} catch (Exception e) {

			//Generate an exception
			throw new Exception("Sorry, "+e.getMessage()+".\n"+props.toString()+"\n"+logString[0]);

		} finally {

			//Finally close all objects
			if (folder != null) { try {folder.close(true);} catch (Exception e) {}; }
			if (store != null) { try {store.close();} catch (Exception e) {}; }
			
		}
		
		JSONObject r = new JSONObject();
		r.put("NbReceived", nbMessageReceived);
		r.put("Total", tot);
		r.put("Directory", ts_dir);

		return r.toJSONString();

	}

	//Save the parts
	@SuppressWarnings("unchecked")
	public static void saveParts(Message msg, Object content, String TS, int i, JSONArray Parts) throws Exception {
		
		//Initialization
		InputStream in = null;
		
		try {
			
			if (content instanceof Multipart) {
				
				//Get the multipart
				Multipart multi = ((Multipart)content);
				int parts = multi.getCount();
				
				//Parse all parts
				for (int j=0; j < parts; ++j) {

					//Get the mime bodu part
					MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
					
					if (part.getContent() instanceof Multipart) {

						// part-within-a-part, do some recursion...
						saveParts(msg, part.getContent(), TS, i+1, Parts);
						
					} else {

						if (part.isMimeType("text/html")) {
							
							JSONObject o = new JSONObject();
							o.put("type", "body");
							o.put("is_multipart", "1");
							o.put("mime_type", "text/html");
							o.put("content_text", part.getContent().toString());
							Parts.add(o);
							
						} else {
							
							if (part.isMimeType("text/plain")) {
								
								JSONObject o = new JSONObject();
								o.put("type", "body");
								o.put("is_multipart", "1");
								o.put("mime_type", "text/plain");
								o.put("content_text", part.getContent().toString());
								Parts.add(o);
								
							} else {
								
								//Try to get the name of the attachment
								String decoded = MimeUtility.decodeText(part.getFileName()); 
								String name = Normalizer.normalize(decoded, Normalizer.Form.NFC);
								
								JSONObject o = new JSONObject();
								o.put("type", "file");
								o.put("is_multipart", "1");
								o.put("filename", name);
								o.put("mime_type", "unknown");
								
								BASE64DecoderStream base64DecoderStream = (BASE64DecoderStream) part.getContent();
							    byte[] byteArray = IOUtils.toByteArray(base64DecoderStream);
							    byte[] encodeBase64 = Base64.encodeBase64(byteArray);
							    
								o.put("content_b64", new String(encodeBase64, "UTF-8"));
								Parts.add(o);
								
							}
							
						}

					}
				}
				
			} else {
				
				Part part = msg;
				
				if (part.isMimeType("text/html")) {
					
					JSONObject o = new JSONObject();
					o.put("type", "body");
					o.put("is_multipart", "0");
					o.put("mime_type", "text/html");
					o.put("content_text", content.toString());
					Parts.add(o);
					
				} else {
					
					if (part.isMimeType("text/plain")) {
						
						JSONObject o = new JSONObject();
						o.put("type", "body");
						o.put("is_multipart", "0");
						o.put("mime_type", "text/plain");
						o.put("content_text", content.toString());
						Parts.add(o);
						
					} else {
						
						//Try to get the name of the attachment
						String decoded = MimeUtility.decodeText(part.getFileName()); 
						String name = Normalizer.normalize(decoded, Normalizer.Form.NFC); 
						
						JSONObject o = new JSONObject();
						o.put("type", "file");
						o.put("is_multipart", "0");
						o.put("filename", name);
						o.put("mime_type", "unknown");
						o.put("content_text", content.toString());
						Parts.add(o);
						
					}
				}
				
			}
			
		} finally {
			
			if (in != null) { in.close(); }
			
		}
	}

}
