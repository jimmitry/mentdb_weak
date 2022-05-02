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
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage.RecipientType;
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
import org.json.simple.parser.JSONParser;

import com.sun.mail.imap.IMAPNestedMessage;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.QPDecoderStream;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.fx.StringFx;
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
		
		long[] nb_b64 = {0};
		long[] nb_qs = {0};
		long[] nb_str = {0};
		long[] nb_html = {0};
		long[] nb_plain = {0};
		long[] nb_unknow = {0};
		
		String server = (String) conf.get("hostname");
		String port = (String) conf.get("port");
		String username = (String) conf.get("user");
		String password = (String) conf.get("password");
		String auth = (String) conf.get("auth");
		String starttls = (String) conf.get("tls");
		String ssl = (String) conf.get("ssl");
		String cnttimeout = (String) conf.get("connectTimeout");
		String timeout = (String) conf.get("sessionTimeout");
		
		String IN_FOLDER = (String) conf.get("inFolder");
		
		if (IN_FOLDER==null || IN_FOLDER.equals("")) {
			IN_FOLDER = "INBOX";
		}
		
		String ALTERNATE_SUB_DIR = (String) conf.get("alternateSubDir");
		
		String xoauth2_scope = (String) conf.get("xoauth2_scope");
		
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
		if (auth==null || (!auth.equals("1") && !auth.equals("0"))) {

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
		int nbMessages = 0;
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

		String err = null;
		String err_mail = null;
		JSONArray fileNames = new JSONArray();
		String timestamp = "";
		
		String[] log_jim = {""};
		
		try {
			
			//Load the session
			props.setProperty("mail.imap.connectionpooltimeout", cnttimeout);
			props.setProperty("mail.imap.connectiontimeout", cnttimeout);
			props.setProperty("mail.imap.timeout", timeout);
			
			props.setProperty("mail.imap.port", port);
			props.setProperty("mail.imap.fetchsize", "819200");
			
			if (xoauth2_scope!=null && xoauth2_scope.equals("1")) {
				
				props.put("mail.imap.auth", "true");
				props.put("mail.imap.auth.login.disable", "true");
				props.put("mail.imap.auth.plain.disable", "true");
				props.put("mail.imaps.ssl.enable", "true");
				props.put("mail.imaps.sasl.enable", "true");
				props.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
				
			} else {
				
				if (auth.equals("1")) props.setProperty( "mail.imap.auth.login.disable", "false" ); 
				else props.setProperty( "mail.imap.auth.login.disable", "true" ); 
				if (ssl.equals("1")) props.setProperty( "mail.imap.ssl.enable", "true");
				else props.setProperty( "mail.imap.ssl.enable", "false");
				if (starttls.equals("1")) {
					props.setProperty("mail.imap.starttls.enable", "true");
					props.setProperty("mail.imap.ssl.protocols", "TLSv1 TLSv1.1 TLSv1.2");
				} else props.setProperty("mail.imap.starttls.enable", "false");
				
			}
			
			Session session = Session.getInstance(props);
			session.setDebugOut(new PrintStream(losStdOut));
			session.setDebug(true);
			
			if (xoauth2_scope!=null && xoauth2_scope.equals("1")) {
				store = session.getStore("imaps");
			} else {
				store = session.getStore("imap");
			}
			
			//Connect to the server
			store.connect(server, username, password);
			
			//Get the INBOX directory
			folder = store.getDefaultFolder().getFolder(IN_FOLDER);
			
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
			
			timestamp = DateFx.systimestamp_min();
			
			if (ALTERNATE_SUB_DIR!=null && !ALTERNATE_SUB_DIR.equals("")) {
				ts_dir = ALTERNATE_SUB_DIR;
				
			} else {
				ts_dir = output_dir+timestamp;
				
				//Create the directory
				if (!(new File(ts_dir)).exists()) (new File(ts_dir)).mkdir();
			}
			
			unread = folder.getUnreadMessageCount();
			tot = folder.getMessageCount();
			
			if ((fromCondition!=null && !fromCondition.equals("true") && !fromCondition.equals("1")) || 
					(subjectCondition!=null && !subjectCondition.equals("true") && !subjectCondition.equals("1"))) {
				
				//Parse all messages
				for (int i=0; i < messages.length; ++i) {

					msg = messages[i];
					
					JSONArray From = new JSONArray();
					try {
						for(int z=0;z<msg.getFrom().length;z++) {
							
							try {From.add(msg.getFrom()[z].toString());} catch (Exception zz) {};
							
						}
					} catch (Exception zz) {};

					env.set("[imap_from]", From.toJSONString());
					env.set("[imap_subject]", msg.getSubject());
					
					String b_fromCondition = "1";
					if (fromCondition!=null && !fromCondition.equals("true") && !fromCondition.equals("1")) b_fromCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, fromCondition, env, parent_pid, current_pid);
					if (b_fromCondition.equals("1")) {
						
						String b_subjectCondition = "1";
						if ((subjectCondition!=null && !subjectCondition.equals("true") && !subjectCondition.equals("1"))) b_subjectCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, subjectCondition, env, parent_pid, current_pid);
						if (b_subjectCondition.equals("1")) {
							
							nbMessages++;
							
						}
						
					}
					
				}
				
			} else {

				nbMessages = messages.length;
				
			}
			
			//Parse all messages
			for (int i=0; i < nbMessages; ++i) {
				
				if (i>=Integer.parseInt(nbMsgToDownload)) {
					
					break;
					
				}
				
				log_jim[0] = "";
				
				try {

					JSONObject eml_metas = new JSONObject();
					int[] nb_eml = {0};
					
					//Get the current message
					msg = messages[i];
					
					JSONObject meta = new JSONObject();
					JSONArray Parts = new JSONArray();
					meta.put("Parts", Parts);
					JSONArray From = new JSONArray();

					meta.put("EMLMETA", eml_metas);
					
					get_info(msg, meta, log_jim, From);
					
					//MQL conditions
					env.set("[imap_from]", From.toJSONString());
					log_jim[0] += "\np_9";
					env.set("[imap_subject]", msg.getSubject());
					log_jim[0] += "\np_10";
					
					String b_fromCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, fromCondition, env, parent_pid, current_pid);
					log_jim[0] += "\np_11";
					String b_subjectCondition = re.jpayet.mentdb.ext.statement.Statement.eval(sessionMql, subjectCondition, env, parent_pid, current_pid);
					log_jim[0] += "\np_12";
					
					if (b_fromCondition.equals("1")) {
						log_jim[0] += "\np_13";
						
						if (b_subjectCondition.equals("1")) {
							log_jim[0] += "\np_14";
							
							//Save the parts
							long begin = System.currentTimeMillis();
							long end = begin+Long.parseLong(timeout);
							
							err_mail = meta.toJSONString();
							
							CheckSendPart check = new CheckSendPart(store, folder, msg, end, markAsRead);
							Thread t = new Thread(check);
							t.start();
							
							saveParts(nb_eml, log_jim, msg, msg.getContent(), ts_dir+"/"+(i+1), 1, Parts, nb_b64, nb_qs, nb_str, nb_html, nb_plain, nb_unknow, eml_metas);
							
							check.play = false;
							while (t.isAlive()) {
								
							}
							
							if (!store.isConnected()) {
								
								err = "Sorry, timeout to read attachements.";
								
								throw new Exception("Sorry, timeout to read attachements.");
								
							}
							
							log_jim[0] += "\np_15";
							//Save the from
							String mail_name = "mail_"+timestamp+"_"+(i+1)+".json";
							
							Misc.create(ts_dir+"/"+mail_name, JsonManager.format_Gson(meta.toJSONString()));
							fileNames.add(mail_name);
							
							if (copyMessageInAnotherFolder!=null && !copyMessageInAnotherFolder.equals("")) {
								
								log_jim[0] += "\np_16";
								String newFolder = AtomFx.get(copyMessageInAnotherFolder, "1", "|");
								
								if (newFolder!=null && !newFolder.equals("")) {

									log_jim[0] += "\np_17";
									Message[] theMsg = new Message[1];
									log_jim[0] += "\np_18";
									theMsg[0] = msg;

									log_jim[0] += "\np_19";
									fNew = store.getFolder(newFolder);

									log_jim[0] += "\np_20";
									if (!fNew.exists()) fNew.create(Folder.HOLDS_MESSAGES);
									log_jim[0] += "\np_21";
									folder.copyMessages(theMsg, fNew);
									log_jim[0] += "\np_22";
									
								}
								log_jim[0] += "\np_23";
								
							}
							log_jim[0] += "\np_24";
							
							log_jim[0] += "\np_25";
							//Mark as read
							if (markAsRead!=null && markAsRead.equals("1")) {
								log_jim[0] += "\np_26";
								msg.setFlag(Flags.Flag.SEEN, true);
							} else if (markAsRead!=null && markAsRead.equals("0")) {
								log_jim[0] += "\np_27";
								msg.setFlag(Flags.Flag.SEEN, false);
							}
							log_jim[0] += "\np_28";
							
							//Delete the message
							if (deleteMsgAfterDownload.equals("1")) {

								log_jim[0] += "\np_29";
								msg.setFlag(Flags.Flag.DELETED, true);
							
							}

							log_jim[0] += "\np_30";
							nbMessageReceived++;

							log_jim[0] += "\np_31";
							unread = folder.getUnreadMessageCount();
							log_jim[0] += "\np_32";
							tot = folder.getMessageCount();
							log_jim[0] += "\np_33";
							
						}
						log_jim[0] += "\np_34";
						
					}
					log_jim[0] += "\np_35";
					
				} catch (Exception ef) {
					
					try {
						
						if (copyMessageInAnotherFolder!=null && !copyMessageInAnotherFolder.equals("") && AtomFx.size(copyMessageInAnotherFolder, "|").equals("2")) {
							
							log_jim[0] += "\np_36";
							String newFolder = AtomFx.get(copyMessageInAnotherFolder, "2", "|");
							
							log_jim[0] += "\np_37";
							Message[] theMsg = new Message[1];
							log_jim[0] += "\np_38";
							theMsg[0] = msg;

							log_jim[0] += "\np_39";
							fNew = store.getFolder(newFolder);

							log_jim[0] += "\np_40";
							if (!fNew.exists()) fNew.create(Folder.HOLDS_MESSAGES);
							log_jim[0] += "\np_41";
							folder.copyMessages(theMsg, fNew);
							log_jim[0] += "\np_42";
							
						}
						log_jim[0] += "\np_43";
						
						log_jim[0] += "\np_44";
						//Mark as read
						if (markAsRead!=null && markAsRead.equals("1")) {
							log_jim[0] += "\np_45";
							msg.setFlag(Flags.Flag.SEEN, true);
						} else if (markAsRead!=null && markAsRead.equals("0")) {
							log_jim[0] += "\np_46";
							msg.setFlag(Flags.Flag.SEEN, false);
						}
						log_jim[0] += "\np_47";
						
						//Delete the message
						if (deleteMsgAfterDownload.equals("1")) {

							log_jim[0] += "\np_48";
							msg.setFlag(Flags.Flag.DELETED, true);
						
						}
						
					} catch (Exception e) {
						
						
						
					}
					
					if (!(""+ef.getMessage()).startsWith("Sorry, timeout to read attachements.")) {
					
						err = ef.getMessage();
					
					}
					
					break;
						
				}
				
			}
			
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
		r.put("NbMessages", nbMessages);
		r.put("Total", tot);
		r.put("Unread", unread);
		r.put("Directory", ts_dir);
		r.put("Error", err);
		if (err==null) {
			err_mail = null;
		}
		if (err_mail!=null) {
			JSONParser jp = new JSONParser();
			JSONObject js = (JSONObject) jp.parse(err_mail);
			r.put("Error_mail", js);
		} else {
			r.put("Error_mail", null);
		}
		r.put("nb_b64", nb_b64[0]);
		r.put("nb_qs", nb_qs[0]);
		r.put("nb_str", nb_str[0]);
		r.put("nb_html", nb_html[0]);
		r.put("nb_plain", nb_plain[0]);
		r.put("nb_unknow", nb_unknow[0]);
		r.put("log", log_jim[0]);
		r.put("FileNames", fileNames);
		r.put("Timestamp", timestamp);
		
		return r.toJSONString();

	}
	
	@SuppressWarnings("unchecked")
	public static void get_info(Message msg, JSONObject meta, String[] log_jim, JSONArray From) {
		
		JSONArray replyTo = new JSONArray();
		meta.put("ReplyTo", replyTo);
		JSONArray AllRecipients = new JSONArray();
		meta.put("Recipients", AllRecipients);
		JSONArray Recipients_to = new JSONArray();
		meta.put("Recipients_to", Recipients_to);
		JSONArray Recipients_cc = new JSONArray();
		meta.put("Recipients_cc", Recipients_cc);
		JSONArray Recipients_bcc = new JSONArray();
		meta.put("Recipients_bcc", Recipients_bcc);
		JSONArray Recipients_newsgroups = new JSONArray();
		meta.put("Recipients_newsgroups", Recipients_newsgroups);
		meta.put("From", From);
		log_jim[0] += "\np_1";
		try {
			for(int z=0;z<msg.getReplyTo().length;z++) {
				
				try {replyTo.add(msg.getReplyTo()[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		log_jim[0] += "\np_2";
		
		try {
			for(int z=0;z<msg.getAllRecipients().length;z++) {
				
				try {AllRecipients.add(msg.getAllRecipients()[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		
		try {
			Address[] adr = msg.getRecipients(RecipientType.TO);
			for(int z=0;z<adr.length;z++) {
				
				try {Recipients_to.add(adr[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		
		try {
			Address[] adr = msg.getRecipients(RecipientType.CC);
			for(int z=0;z<adr.length;z++) {
				
				try {Recipients_cc.add(adr[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		
		try {
			Address[] adr = msg.getRecipients(RecipientType.BCC);
			for(int z=0;z<adr.length;z++) {
				
				try {Recipients_bcc.add(adr[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		
		try {
			Address[] adr = msg.getRecipients(RecipientType.NEWSGROUPS);
			for(int z=0;z<adr.length;z++) {
				
				try {Recipients_newsgroups.add(adr[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		log_jim[0] += "\np_3";
		
		try {
			for(int z=0;z<msg.getFrom().length;z++) {
				
				try {From.add(msg.getFrom()[z].toString());} catch (Exception zz) {};
				
			}
		} catch (Exception zz) {};
		log_jim[0] += "\np_4";
		
		try {meta.put("ContentType", msg.getContentType());} catch (Exception zz) {};
		try {meta.put("Description", msg.getDescription());} catch (Exception zz) {};
		try {meta.put("Disposition", msg.getDisposition());} catch (Exception zz) {};
		try {meta.put("FileName", msg.getFileName());} catch (Exception zz) {};
		try {meta.put("LineCount", msg.getLineCount());} catch (Exception zz) {};
		try {meta.put("MessageNumber", msg.getMessageNumber());} catch (Exception zz) {};
		log_jim[0] += "\np_5";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			meta.put("ReceivedDate", dateFormat.format(msg.getReceivedDate().getTime()));
		} catch (Exception zz) {};
		log_jim[0] += "\np_6";
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			meta.put("SentDate", dateFormat.format(msg.getSentDate().getTime()));
		} catch (Exception zz) {};
		log_jim[0] += "\np_7";
		try {meta.put("Size", msg.getSize());} catch (Exception zz) {};
		try {meta.put("Subject", msg.getSubject());} catch (Exception zz) {};
		log_jim[0] += "\np_8";
		
	}

	//Save the parts
	@SuppressWarnings("unchecked")
	public static void saveParts(int[] nb_eml, String[] log_jim, Message msg, Object content, String TS, int i, JSONArray Parts, long[] nb_b64, long[] nb_qs, long[] nb_str, long[] nb_html, long[] nb_plain, long[] nb_unknow, JSONObject eml_metas) throws Exception {
		
		//Initialization
		log_jim[0] += "\np_141";
		try {
			
			if (content instanceof Multipart) {
				log_jim[0] += "\np_142";
				//Get the multipart
				Multipart multi = ((Multipart)content);
				int parts = multi.getCount();
				log_jim[0] += "\np_143";
				//Parse all parts
				for (int j=0; j < parts; ++j) {
					log_jim[0] += "\np_144";
					//Get the mime bodu part
					MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
					
					log_jim[0] += "\np_145";
					if (part.getContent() instanceof Multipart) {
						log_jim[0] += "\np_146 > Multipart";
						// part-within-a-part, do some recursion...
						saveParts(nb_eml, log_jim, msg, part.getContent(), TS, i+1, Parts, nb_b64, nb_qs, nb_str, nb_html, nb_plain, nb_unknow, eml_metas);
						log_jim[0] += "\np_147";
					} else {
						log_jim[0] += "\np_148";
						if (part.isMimeType("text/html")) {
							log_jim[0] += "\np_149 > text/html";
							JSONObject o = new JSONObject();
							o.put("eml", nb_eml[0]);
							o.put("type", "body");
							o.put("is_multipart", "1");
							o.put("mime_type", "text/html");
							o.put("content_text", part.getContent().toString());
							Parts.add(o);
							nb_html[0]++;
							log_jim[0] += "\np_1410";
						} else {
							log_jim[0] += "\np_1411";
							if (part.isMimeType("text/plain")) {
								log_jim[0] += "\np_1412 > text/plain";
								JSONObject o = new JSONObject();
								o.put("eml", nb_eml[0]);
								o.put("type", "body");
								o.put("is_multipart", "1");
								o.put("mime_type", "text/plain");
								o.put("content_text", part.getContent().toString());
								Parts.add(o);
								nb_plain[0]++;
								log_jim[0] += "\np_1413";
							} else if (!part.isMimeType("message/rfc822")) {
								log_jim[0] += "\np_1414 > message/rfc822";
								//Try to get the name of the attachment
								String decoded = MimeUtility.decodeText(part.getFileName()); 
								String name = Normalizer.normalize(decoded, Normalizer.Form.NFC);
								log_jim[0] += "\np_1415";
								JSONObject o = new JSONObject();
								
								o.put("type", "file");
								o.put("is_multipart", "1");
								o.put("filename", name);
								o.put("mime_type", "unknown");
								log_jim[0] += "\np_1416";
								if (part.getContent() instanceof String) {
									log_jim[0] += "\np_1417 > String";
									nb_str[0]++;
									o.put("content_b64", StringFx.encode_b64((String)part.getContent()));
									log_jim[0] += "\np_1418";
									o.put("eml", nb_eml[0]);
									Parts.add(o);
							    } else if (part.getContent() instanceof QPDecoderStream) {
							    		log_jim[0] += "\np_1419 > QPDecoderStream";
							    		/*InputStream bis = new InputStream(part.getContent());
									ByteArrayOutputStream baos = new ByteArrayOutputStream();
									while (true) {
									    int c = bis.read();
									    if (c == -1) {
									      break;
									    }
									    baos.write(c);
									}*/
									nb_qs[0]++;
									o.put("content_b64", StringFx.encode_b64(IOUtils.toString(part.getInputStream())));
									log_jim[0] += "\np_1420";
									o.put("eml", nb_eml[0]);
									Parts.add(o);
							    } else if (part.getContent() instanceof BASE64DecoderStream) {
							    		log_jim[0] += "\np_1421 > BASE64DecoderStream";	
							    		BASE64DecoderStream base64DecoderStream = (BASE64DecoderStream) part.getContent();
								    byte[] byteArray = IOUtils.toByteArray(base64DecoderStream);
								    byte[] encodeBase64 = Base64.encodeBase64(byteArray);
								    nb_b64[0]++;
									o.put("content_b64", new String(encodeBase64, "UTF-8"));
									o.put("eml", nb_eml[0]);
									Parts.add(o);
									log_jim[0] += "\np_1422";
							    } else {
							    		log_jim[0] += "\np_1423 > other";
							    		nb_unknow[0]++;
							    		o.put("content_b64", StringFx.encode_b64(IOUtils.toString(part.getInputStream())));
									o.put("eml", nb_eml[0]);
									Parts.add(o);
								}
								
								log_jim[0] += "\np_1424";
								
							} else if (part.isMimeType("message/rfc822")) {
								
								/*JSONObject o = new JSONObject();
								o.put("type", "eml");
								o.put("is_multipart", "1");
								o.put("filename", "message_included" + nb_eml[0] + ".eml");
								o.put("mime_type", "rfc822");
								nb_eml[0]++;
								IMAPNestedMessage imapNestedMessage = (IMAPNestedMessage) part.getContent();
							    
								byte[] byteArray = IOUtils.toByteArray(imapNestedMessage.getInputStream());
							    byte[] encodeBase64 = Base64.encodeBase64(byteArray);
								//byte[] encodeBase64 = Base64.encodeBase64(baos.toByteArray());
							    nb_b64[0]++;
								o.put("content_b64", new String(encodeBase64, "UTF-8"));
								Parts.add(o);*/

								log_jim[0] += "\np_1425 > "+part.getContentType();
								Part p2 = (Part) ((IMAPNestedMessage) part.getContent());
								
								//JSONObject meta = new JSONObject();
								//JSONArray From = new JSONArray();
								//get_info(msg, meta, log_jim, From);
								
								JSONObject meta = new JSONObject();
								JSONArray From = new JSONArray();
								get_info((Message) p2, meta, log_jim, From);
								
								nb_eml[0]++;
								
								eml_metas.put(nb_eml[0], meta);
								
								Multipart mp = (Multipart) p2.getContent();
								
								saveParts(nb_eml, log_jim, msg, mp, TS, i+1, Parts, nb_b64, nb_qs, nb_str, nb_html, nb_plain, nb_unknow, eml_metas);
								
							}
							
						}
						
						log_jim[0] += "\np_1426";

					}
					
					log_jim[0] += "\np_1427";
				
				}
				
				log_jim[0] += "\np_1428";
				
			} else {
				
				log_jim[0] += "\np_1429";
				
				Part part = msg;
				
				if (part.isMimeType("text/html")) {
					log_jim[0] += "\np_1430 > text/html";
					JSONObject o = new JSONObject();
					o.put("eml", nb_eml[0]);
					o.put("type", "body");
					o.put("is_multipart", "0");
					o.put("mime_type", "text/html");
					o.put("content_text", content.toString());
					Parts.add(o);

					nb_html[0]++;
					log_jim[0] += "\np_1431";
				} else {
					log_jim[0] += "\np_1432";
					if (part.isMimeType("text/plain")) {
						log_jim[0] += "\np_1433 > text/plain";
						JSONObject o = new JSONObject();
						o.put("eml", nb_eml[0]);
						o.put("type", "body");
						o.put("is_multipart", "0");
						o.put("mime_type", "text/plain");
						o.put("content_text", content.toString());
						Parts.add(o);
						
						nb_plain[0]++;
						log_jim[0] += "\np_1434";
					} else {
						log_jim[0] += "\np_1435 > other 2";
						//Try to get the name of the attachment
						String decoded = MimeUtility.decodeText(part.getFileName()); 
						String name = Normalizer.normalize(decoded, Normalizer.Form.NFC); 
						
						JSONObject o = new JSONObject();
						o.put("eml", nb_eml[0]);
						o.put("type", "file");
						o.put("is_multipart", "0");
						o.put("filename", name);
						o.put("mime_type", "unknown");
						o.put("content_text", content.toString());
						Parts.add(o);
						
						nb_unknow[0]++;
						log_jim[0] += "\np_1436";
					}
					log_jim[0] += "\np_1437";
				}
				log_jim[0] += "\np_1438";
			}
			log_jim[0] += "\np_1439";
		} catch (Exception e) {
			
			log_jim[0] += "\np_1440"+e.getMessage()+e.getLocalizedMessage()+e.getCause();
			
		} finally {
			log_jim[0] += "\np_1441";
			
		}
		log_jim[0] += "\np_1442";
	}
	
}
