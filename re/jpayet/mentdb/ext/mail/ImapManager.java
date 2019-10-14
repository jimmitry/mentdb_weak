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
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.ReceivedDateTerm;

import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.mail.util.BASE64DecoderStream;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class ImapManager {

	@SuppressWarnings("unchecked")
	public static String parse(String output_dir, 
			String nbMsgToDownload, String unreadOrAll, String copyMessageInAnotherFolder, 
			String deleteMsgAfterDownload, String markAsRead, 
			String startDate, String endDate, String fromCondition, 
			String subjectCondition,
			String json, EnvManager env, SessionThread sessionMql, String parent_pid, String current_pid) throws Exception {

		//Generate an error if the output directory is null or empty
		if (output_dir==null || output_dir.equals("")) {

			throw new Exception("Sorry, the output directory cannot be null or empty.");

		}
		
		JSONObject conf = (JSONObject) JsonManager.load(json);

		String server = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String username = (String) conf.get("user");
		String password = (String) conf.get("password");
		String auth = (String) conf.get("auth");
		String starttls = (String) conf.get("tls");
		String ssl = (String) conf.get("ssl");
		String cnttimeout = (String) conf.get("connectTimeout");
		String timeout = (String) conf.get("sessionTimeout");
		
		//Generate an error if the server name is null or empty
		if (server==null || server.equals("")) {

			throw new Exception("Sorry, the hostname cannot be null or empty.");

		}
		
		//Generate an error if fromCondition is null or empty
		if (fromCondition==null || fromCondition.equals("")) {

			throw new Exception("Sorry, the from condition cannot be null or empty.");

		}
		
		//Generate an error if subjectCondition is null or empty
		if (subjectCondition==null || subjectCondition.equals("")) {

			throw new Exception("Sorry, the subject condition cannot be null or empty.");

		}
		
		//Generate an error if the markAsRead field is not valid (must be 1 or 0)
		if (markAsRead==null || (!markAsRead.equals("1") && !markAsRead.equals("0"))) {

			throw new Exception("Sorry, markAsRead field must be 1 or 0.");

		}
		
		//Generate an error if the deleteMsgAfterDownload field is not valid (must be 1 or 0)
		if (deleteMsgAfterDownload==null || (!deleteMsgAfterDownload.equals("1") && !deleteMsgAfterDownload.equals("0"))) {

			throw new Exception("Sorry, delete after download field must be 1 or 0.");

		}
		
		//Generate an error if the unreadOrAll field is not valid (must be unread or all)
		if (unreadOrAll==null || (!unreadOrAll.equals("unread") && !unreadOrAll.equals("all"))) {

			throw new Exception("Sorry, unreadOrAll field must be 'unread' or 'all'.");

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

		//Generate an error if the auth field is not valid (must be 1 or 0)
		if (auth==null || (!auth.equals("1") && !starttls.equals("0"))) {

			throw new Exception("Sorry, auth field must be 1 or 0.");

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
		int tot = 0, unread = 0;
		String ts_dir = "";
		Folder fNew = null;
		Message msg = null;
		
		String[] logString = {""};
		LogOutputStream losStdOut  = new LogOutputStream() {
			
			@Override
		    protected void processLine(String line, int level) {
				logString[0] += line+"\n";
		    }
		};
		
		Properties props = new Properties();

		try {
			
			//Load the session
			props.setProperty("mail.imap.connectionpooltimeout", cnttimeout);
			props.setProperty("mail.imap.connectiontimeout", cnttimeout);
			props.setProperty("mail.imap.timeout", timeout);
			props.setProperty("mail.imap.port", port);
			
			if (auth.equals("1")) props.setProperty( "mail.imap.auth.login.disable", "false" ); 
			else props.setProperty( "mail.imap.auth.login.disable", "true" ); 
			if (ssl.equals("1")) props.setProperty( "mail.imap.ssl.enable", "true");
			else props.setProperty( "mail.imap.ssl.enable", "false");
			if (starttls.equals("1")) props.setProperty("mail.imap.starttls.enable", "true");
			else props.setProperty("mail.imap.starttls.enable", "false");
			
			Session session = Session.getInstance(props);
			session.setDebugOut(new PrintStream(losStdOut));
			session.setDebug(true);
			
			store = session.getStore("imap");
			
			//Connect to the server
			store.connect(server, username, password);
			
			//Get the INBOX directory
			folder = store.getDefaultFolder().getFolder("INBOX");
			
			//Open in read only mode
			folder.open(Folder.READ_WRITE);
			
			//Get all messages
			Message[] messages = null;
			SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd");
			
			if (unreadOrAll.equals("all")) {
				
				if (startDate!=null && !startDate.equals("") && endDate!=null && !endDate.equals("")) {
					
					SearchTerm ge = new ReceivedDateTerm(ComparisonTerm.GE, new Date(p.parse(startDate).getTime()));
					SearchTerm le = new ReceivedDateTerm(ComparisonTerm.LE, new Date(p.parse(endDate).getTime()));
					SearchTerm andTerm = new AndTerm(ge, le);
					
					messages = folder.search(andTerm);
					
				} else if (startDate!=null && !startDate.equals("")) {
					
					SearchTerm ge = new ReceivedDateTerm(ComparisonTerm.GE, new Date(p.parse(startDate).getTime()));
					
					messages = folder.search(ge);
					
				} else if (endDate!=null && !endDate.equals("")) {
					
					SearchTerm le = new ReceivedDateTerm(ComparisonTerm.LE, new Date(p.parse(endDate).getTime()));
					
					messages = folder.search(le);
					
				} else {
					
					messages = folder.getMessages();
					
				}
				
			} else {
				
				if (startDate!=null && !startDate.equals("") && endDate!=null && !endDate.equals("")) {
					
					SearchTerm ge = new ReceivedDateTerm(ComparisonTerm.GE, new Date(p.parse(startDate).getTime()));
					SearchTerm le = new ReceivedDateTerm(ComparisonTerm.LE, new Date(p.parse(endDate).getTime()));
					SearchTerm andTerm = new AndTerm(ge, le);
					
					// search for all "unseen" messages
					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					
					SearchTerm andTermFinal = new AndTerm(andTerm, unseenFlagTerm);
					messages = folder.search(andTermFinal);
					
				} else if (startDate!=null && !startDate.equals("")) {
					
					SearchTerm ge = new ReceivedDateTerm(ComparisonTerm.GE, new Date(p.parse(startDate).getTime()));
					
					// search for all "unseen" messages
					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					
					SearchTerm andTermFinal = new AndTerm(ge, unseenFlagTerm);
					messages = folder.search(andTermFinal);
					
				} else if (endDate!=null && !endDate.equals("")) {
					
					SearchTerm le = new ReceivedDateTerm(ComparisonTerm.LE, new Date(p.parse(endDate).getTime()));
					
					// search for all "unseen" messages
					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					
					SearchTerm andTermFinal = new AndTerm(le, unseenFlagTerm);
					messages = folder.search(andTermFinal);
					
				} else {
					
					// search for all "unseen" messages
					Flags seen = new Flags(Flags.Flag.SEEN);
					FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
					messages = folder.search(unseenFlagTerm);
					
				}
				
			}
			
			//Get the current timestamp
			if (!output_dir.endsWith("/")) output_dir+="/";
			ts_dir = output_dir+DateFx.systimestamp_min();
			
			//Create the directory
			if (!(new File(ts_dir)).exists()) (new File(ts_dir)).mkdir();
			
			unread = folder.getUnreadMessageCount();
			tot = folder.getMessageCount();
			
			//Parse all messages
			for (int i=0; i < messages.length; ++i) {
				
				if (i>=Integer.parseInt(nbMsgToDownload)) {
					
					break;
					
				}
				
				//Get the current message
				msg = messages[i];
				
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
				try {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					meta.put("ReceivedDate", dateFormat.format(msg.getReceivedDate().getTime()));
				} catch (Exception zz) {};
				try {
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					meta.put("SentDate", dateFormat.format(msg.getSentDate().getTime()));
				} catch (Exception zz) {};
				try {meta.put("Size", msg.getSize());} catch (Exception zz) {};
				try {meta.put("Subject", msg.getSubject());} catch (Exception zz) {};
				
				//MQL conditions
				env.set("[imap_from]", From.toJSONString());
				env.set("[imap_subject]", msg.getSubject());
				
				String b_fromCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, fromCondition, env, parent_pid, current_pid);
				String b_subjectCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, subjectCondition, env, parent_pid, current_pid);
				
				if (b_fromCondition.equals("1")) {
					
					if (b_subjectCondition.equals("1")) {
						
						//Save the parts
						saveParts(msg, msg.getContent(), ts_dir+"/"+(i+1), 1, Parts);
						
						//Save the from
						Misc.create(ts_dir+"/mail"+(i+1)+".json", JsonManager.format_Gson(meta.toJSONString()));
						
						//Mark as read
						if (markAsRead.equals("1")) {
							
							msg.setFlag(Flags.Flag.SEEN, true);
						
						} else if (markAsRead.equals("0")) {
							
							msg.setFlag(Flags.Flag.SEEN, false);
							
						}
						
						if (copyMessageInAnotherFolder!=null && !copyMessageInAnotherFolder.equals("")) {
							
							String newFolder = copyMessageInAnotherFolder;
							
							if (newFolder!=null && !newFolder.equals("")) {
								
								Message[] theMsg = new Message[1];
								theMsg[0] = msg;
								
								fNew = store.getFolder(newFolder);
								if (!fNew.exists()) fNew.create(Folder.HOLDS_MESSAGES);
								folder.copyMessages(theMsg, fNew);
								
							}
							
						}
						
						//Delete the message
						if (deleteMsgAfterDownload.equals("1")) {
							
							msg.setFlag(Flags.Flag.DELETED, true);
						
						}
						
						nbMessageReceived++;
						
						unread = folder.getUnreadMessageCount();
						tot = folder.getMessageCount();
						
					}
					
				}
				
			}
			
			//Close the folder
			folder.close(true);
			
		} catch (Exception e) {
			
			//Generate an exception
			throw new Exception("Sorry, "+e.getMessage()+".\n"+props.toString()+"\n"+logString[0]);

		} finally {

			//Finally close all objects
			if (fNew != null) { try {fNew.close(true);} catch (Exception e) {}; }
			if (folder != null) { try {folder.close(true);} catch (Exception e) {}; }
			if (store != null) { try {store.close();} catch (Exception e) {}; }
			
		}
		
		JSONObject r = new JSONObject();
		r.put("NbReceived", nbMessageReceived);
		r.put("Total", tot);
		r.put("Unread", unread);
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
								
							} else if (!part.isMimeType("message/rfc822")) {
								
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
