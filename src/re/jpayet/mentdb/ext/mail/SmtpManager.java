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
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.exec.LogOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.mail.smtp.SMTPSSLTransport;
import com.sun.mail.smtp.SMTPTransport;

import re.jpayet.mentdb.core.db.Database;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.json.JsonManager;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.sql.SQLManager;

public class SmtpManager {

	public static AtomicBoolean lock = new AtomicBoolean(false);
	public static AtomicBoolean lock_count = new AtomicBoolean(false);
	public static ConcurrentHashMap<Long, Integer> loadedProcess = new ConcurrentHashMap<Long, Integer>();
	static public int PROCESS_LIMIT = 0;

	public static Long count_to_process() throws Exception {

		//Bloc the execution
		if (!lock_count.compareAndSet(false, true)) {
			
			return 0L;

		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `mails` WHERE `state`='W'");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
		} catch (Exception e) {

			

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

		lock_count.set(false);
		
		return r;
		
	}
	
	public static void process() throws Exception {
		
		if (!lock.compareAndSet(false, true)) {
			
			return;

		}

		//Send only if process limit is OK
		if (loadedProcess.size()<PROCESS_LIMIT) {

			Object[] cmo = null;
			Statement stm = null;
			ResultSet rs = null;

			try {

				cmo = MYSQLManager.select("SELECT `id`,\n" + 
						"    `subject`,\n" + 
						"    `body`,\n" + 
						"    `maillist`,\n" + 
						"    `mailcc`,\n" + 
						"    `mailbcc`,\n" + 
						"    `cm`\n" + 
						"FROM `mails` WHERE `state`='W' order by dtcreate");

				stm = (Statement) cmo[0];
				rs = (ResultSet) cmo[1];

				while (rs.next()) {

					String id = rs.getString(1);
					String subject = rs.getString(2);
					String body = rs.getString(3);
					String maillist = rs.getString(4);
					String mailcc = rs.getString(5);
					String mailbcc = rs.getString(6);
					String cm = rs.getString(7);

					if (loadedProcess.size()<PROCESS_LIMIT) {

						loadedProcess.put(Long.parseLong(id), 0);

						Runnable r = new MailProcess(id, subject, body, maillist, mailcc, mailbcc, cm);
						Thread t = new Thread(r);
						t.start();

					} else break;

				}

			} catch (Exception e) {

				//Nothing to do

			} finally {

				try {
					if (rs!=null) rs.close();
				} catch (Exception g) {}

				try {
					if (stm!=null) stm.close();
				} catch (Exception g) {}

			}

		}

		lock.set(false);

	}

	public static String count_all() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `mails`");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String count_error() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT count(*) FROM `mails` WHERE `state`='E'");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String get_min_date() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;

		try {

			cmo = MYSQLManager.select("SELECT min(dtcreate) FROM `mails` WHERE `state`='E'");

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			rs.next();
			return rs.getString(1).substring(0, 19);

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	@SuppressWarnings("unchecked")
	public static String show(String limit) throws Exception {
		
		//Generate an error if the limit to search is null or empty
		if (limit==null || limit.equals("")) {
			
			throw new Exception("Sorry, the limit cannot be null or empty.");
			
		}
		
		try {
			
			Integer.parseInt(limit);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the limit must be a valid number.");
			
		}

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			cmo = MYSQLManager.select("SELECT `id`,\n" + 
					"    `state`,\n" + 
					"    `dtcreate`,\n" + 
					"    `lastattempt`,\n" + 
					"    `nbattempt`,\n" + 
					"    `subject`,\n" + 
					"    `maillist`,\n" + 
					"    `mailcc`,\n" + 
					"    `mailbcc`,\n" + 
					"    `errmsg`,\n" + 
					"    `cm`\n" + 
					"FROM `mails` ORDER BY `dtcreate` LIMIT 0, "+limit);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			JSONArray data = new JSONArray();

			while (rs.next()) {
				
				//Reset the line
				JSONObject line = new JSONObject();

				line.put("id", rs.getString(1));
				line.put("state", rs.getString(2));
				line.put("dtcreate", rs.getString(3));
				line.put("lastattempt", rs.getString(4));
				line.put("nbattempt", rs.getString(5));
				line.put("subject", rs.getString(6));
				line.put("maillist", rs.getString(7));
				line.put("mailcc", rs.getString(8));
				line.put("mailbcc", rs.getString(9));
				line.put("errmsg", rs.getString(10));
				line.put("cm", rs.getString(11));
				
				data.add(line);
				
			}
			
			return data.toJSONString();

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	@SuppressWarnings("unchecked")
	public static String show_error(String limit) throws Exception {
		
		//Generate an error if the limit to search is null or empty
		if (limit==null || limit.equals("")) {
			
			throw new Exception("Sorry, the limit cannot be null or empty.");
			
		}
		
		try {
			
			Integer.parseInt(limit);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the limit must be a valid number.");
			
		}

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			cmo = MYSQLManager.select("SELECT `id`,\n" + 
					"    `state`,\n" + 
					"    `dtcreate`,\n" + 
					"    `lastattempt`,\n" + 
					"    `nbattempt`,\n" + 
					"    `subject`,\n" + 
					"    `maillist`,\n" + 
					"    `mailcc`,\n" + 
					"    `mailbcc`,\n" + 
					"    `errmsg`,\n" + 
					"    `cm`\n" + 
					"FROM `mails` WHERE `state`='E' ORDER BY `dtcreate` LIMIT 0, "+limit);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			JSONObject table = new JSONObject();
			table.put("table", "mails");
			JSONArray columns = new JSONArray();
			table.put("columns", columns);
			JSONArray data = new JSONArray();
			table.put("data", data);

			columns.add("id");
			columns.add("state");
			columns.add("dtcreate");
			columns.add("lastattempt");
			columns.add("nbattempt");
			columns.add("subject");
			columns.add("maillist");
			columns.add("mailcc");
			columns.add("mailbcc");
			columns.add("errmsg");
			columns.add("cm");
			
			while (rs.next()) {
				
				//Reset the line
				JSONObject line = new JSONObject();

				line.put("id", rs.getString(1));
				line.put("state", rs.getString(2));
				line.put("dtcreate", rs.getString(3));
				line.put("lastattempt", rs.getString(4));
				line.put("nbattempt", rs.getString(5));
				line.put("subject", rs.getString(6));
				line.put("maillist", rs.getString(7));
				line.put("mailcc", rs.getString(8));
				line.put("mailbcc", rs.getString(9));
				line.put("errmsg", rs.getString(10));
				line.put("cm", rs.getString(11));
				
				data.add(line);
				
			}
			
			return table.toJSONString();

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String get_body(String mailId) throws Exception {
		
		//Generate an error if the mail id is null or empty
		if (mailId==null || mailId.equals("")) {
			
			throw new Exception("Sorry, the mail id cannot be null or empty.");
			
		}
		
		try {
			
			Integer.parseInt(mailId);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the mail id must be a valid number.");
			
		}

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			cmo = MYSQLManager.select("SELECT `body`\n" + 
					"FROM `mails` WHERE id="+mailId);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			String result = "";
			
			while (rs.next()) {
				
				result = rs.getString(1);
				
			}
			
			return result;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	public static String get_error(String mailId) throws Exception {
		
		//Generate an error if the mail id is null or empty
		if (mailId==null || mailId.equals("")) {
			
			throw new Exception("Sorry, the mail id cannot be null or empty.");
			
		}
		
		try {
			
			Integer.parseInt(mailId);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the mail id must be a valid number.");
			
		}

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {

			cmo = MYSQLManager.select("SELECT `errmsg`\n" + 
					"FROM `mails` WHERE id="+mailId);

			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			String result = "";
			
			while (rs.next()) {
				
				result = rs.getString(1);
				
			}
			
			return result;

		} catch (Exception e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}

	}

	//Mail process execution
	public static class MailProcess implements Runnable {

		private String id;
		private String subject;
		private String body;
		private String maillist;
		private String mailcc;
		private String mailbcc;
		private String cm;

		public MailProcess(String id, String subject, String body, String maillist, String mailcc, String mailbcc, String cm) {
			this.id = id;
			this.subject = subject;
			this.body = body;
			this.maillist = maillist;
			this.mailcc = mailcc;
			this.mailbcc = mailbcc;
			this.cm = cm;
		}

		public void run() {
			
			try {
				
				markAsSendMail(id);

				sendMail(id, subject, body, maillist, mailcc, mailbcc, (JSONObject) JsonManager.load(Database.execute_admin_mql(null, "cm get \""+(cm.replace("\"", "\\\""))+"\";")));

				deleteMail(id);

			} catch (Exception e) {

				errorMail(id, ""+e.getMessage());

			}

			loadedProcess.remove(Long.parseLong(id));

		}

	}

	public static void replay_id(String id) throws Exception {

		MYSQLManager.executeUpdate("UPDATE `mails` SET "
				+ "`state`='W' "
				+ "WHERE `state`='E' and id="+id, true);

	}

	public static void replay_cm(String cm) throws Exception {

		MYSQLManager.executeUpdate("UPDATE `mails` SET "
				+ "`state`='W' "
				+ "WHERE `state`='E' and cm="+SQLManager.encode(cm), true);

	}

	public static void replay_all() throws Exception {

		MYSQLManager.executeUpdate("UPDATE `mails` SET "
				+ "`state`='W' "
				+ "WHERE `state`='E'", true);

	}

	public static void delete_id(String id) throws Exception {

		MYSQLManager.executeUpdate("DELETE FROM `mails` WHERE `state`='E' and id="+id, true);

	}

	public static void delete_cm(String cm) throws Exception {

		MYSQLManager.executeUpdate("DELETE FROM `mails` WHERE `state`='E' and cm="+SQLManager.encode(cm), true);

	}

	public static void delete_all() throws Exception {

		MYSQLManager.executeUpdate("DELETE FROM `mails` WHERE `state`='E'", true);

	}

	public static void markAsSendMail(String id) throws Exception {

		MYSQLManager.executeUpdate("UPDATE `mails` SET "
				+ "`state`='S', "
				+ "`nbattempt`=`nbattempt`+1, "
				+ "lastattempt=CURRENT_TIMESTAMP() "
				+ "WHERE id="+id, true);

	}
	
	//SMTP authenticator class
	private static class SMTPAuthenticator extends javax.mail.Authenticator {

		//Initialization
		private String USERNAME="";
		private String PASSWORD="";

		//Constructor
		public SMTPAuthenticator(String username, String password) {
			super();

			//Set all defaults fields
			this.USERNAME=username;
			this.PASSWORD=password;

		}

		//getPasswordAuthentication
		public PasswordAuthentication getPasswordAuthentication() {

			//Return the password authentication object
			return new PasswordAuthentication(USERNAME, PASSWORD);

		}
		
	}

	public static void sendMail(String id, String subject, String body, String maillist, String mailcc, String mailbcc, JSONObject cm) throws Exception {

		//Initialization
		Session session = null;
		Properties props = new Properties();
		SMTPTransport transport =null;
		SMTPSSLTransport secureTransport =null;
		String[] logString = {""};
		LogOutputStream losStdOut  = new LogOutputStream() {
			
			@Override
		    protected void processLine(String line, int level) {
				logString[0] += line+"\n";
		    }
		};
		
		try {
			
			String startTLS = (String) cm.get("tls");
			String smtpServer = (String) cm.get("hostname");
			String smtpPort = (String) cm.get("port");
			String javamail_cnt_timeout = (String) cm.get("connectTimeout");
			String javamail_timeout = (String) cm.get("sessionTimeout");
			String smtpUser = (String) cm.get("user");
			String smtpPassword = (String) cm.get("password");
			String smtpSender = (String) cm.get("sender");
			String smtpAuthenticate = (String) cm.get("authentication");

			//Add handlers for main MIME types
			MailcapCommandMap mcap = new MailcapCommandMap();
			mcap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			mcap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			mcap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			mcap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true");
			mcap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(mcap);

			//Get an instance of session
			if (startTLS.equals("1")) {

				//TLS was activated
				props.put("mail.smtps.host", smtpServer);
				props.put("mail.smtps.port", smtpPort);
				props.put("mail.smtps.connectiontimeout", javamail_cnt_timeout);
				props.put("mail.smtps.timeout", javamail_timeout);
				props.put("mail.smtps.auth", "true");
				Authenticator auth = new SMTPAuthenticator(smtpUser, smtpPassword);
				session = Session.getInstance(props, auth);

			} else {

				//TLS was activated
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.host", smtpServer);
				props.put("mail.smtp.port", smtpPort);
				props.put("mail.smtp.connectiontimeout", javamail_cnt_timeout);
				props.put("mail.smtp.timeout", javamail_timeout);
				props.put("mail.smtp.from", smtpSender);
				session = Session.getInstance(props, null);

			}
			
			session.setDebugOut(new PrintStream(losStdOut));
			session.setDebug(true);

			//Create the message
			Message msg = new MimeMessage(session);

			//Set the sender
			msg.setFrom(new InternetAddress(smtpSender));

			//Set mail recipients
			for(int i=1;i<=Integer.parseInt(AtomFx.size(maillist, ";"));i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(AtomFx.get(maillist, ""+i, ";")));
			}

			if (mailcc!=null && !mailcc.equals("")) for (int i = 1; i <= Integer.parseInt(AtomFx.size(mailcc, ";")); i++) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(AtomFx.get(mailcc, ""+i, ";")));
			}

			if (mailbcc!=null && !mailbcc.equals("")) for (int i = 1; i <= Integer.parseInt(AtomFx.size(mailbcc, ";")); i++) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(AtomFx.get(mailbcc, ""+i, ";")));
			}

			//Set the subject
			msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));

			//Create the body part object
			BodyPart messageBodyPart = new MimeBodyPart();

			//The body
			messageBodyPart.setContent(body, "text/html;charset=utf-8");

			//Add the body in a multipart object
			Multipart multipart = new MimeMultipart("related");
			multipart.addBodyPart(messageBodyPart);

			//File here ...
			JSONArray files_at = MYSQLManager.select_rows("select * from mail_files where id_mail="+id);
			
			FileFx.mkdir("tmp"+File.separator+"mail_"+id);
			
			for(int z = 0;z<files_at.size();z++) {
				
				JSONObject o = (JSONObject) files_at.get(z);
				
				FileFx.b64_write(null, o.get("b64")+"", "tmp"+File.separator+"mail_"+id+File.separator+o.get("filename"));
				messageBodyPart = new MimeBodyPart();
				File f = new File("tmp"+File.separator+"mail_"+id+File.separator+o.get("filename"));
		        DataSource source = new ByteArrayDataSource(Files.readAllBytes(f.toPath()), "application/octet-stream");
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        messageBodyPart.setFileName(o.get("filename")+"");
		        multipart.addBodyPart(messageBodyPart);
		        
			}
	        
	        FileFx.delete("tmp"+File.separator+"mail_"+id);

			//Set the mail content
			msg.setContent(multipart);

			//Save changes
			msg.saveChanges();

			//Get the transport object
			if (startTLS.equals("1")) {
				secureTransport = (SMTPSSLTransport)session.getTransport("smtps");
			}
			else {
				transport = (SMTPTransport)session.getTransport("smtp");
			}

			//Connect to the server
			if (smtpAuthenticate.equals("1")) {

				//With authentication
				if (startTLS.equals("1")) {
					secureTransport.connect(smtpServer, smtpUser, smtpPassword);
				}
				else {
					transport.connect(smtpServer, smtpUser, smtpPassword);
				}

			} else {

				//Without authentication
				if (startTLS.equals("1")) secureTransport.connect();
				else transport.connect();

			}

			//Send the message
			if (startTLS.equals("1")) secureTransport.sendMessage(msg, msg.getAllRecipients());
			else transport.sendMessage(msg, msg.getAllRecipients());

		} catch (Exception e) {

			throw new Exception(e.getMessage()+"\n\n"+logString[0]);

		} finally {

			try {
				transport.close();
			} catch (Exception e) {};

			try {
				secureTransport.close();
			} catch (Exception e) {};
			
		}

	}

	public static void errorMail(String id, String msg) {

		try {

			MYSQLManager.executeUpdate("UPDATE `mails` SET "
					+ "`state`='E', "
					+ "`errmsg`= "+SQLManager.encode(msg)+" "
					+ "WHERE id="+id, true);

		} catch (Exception e) {

			//Nothing

		}

	}

	public static void deleteMail(String id) {

		try {

			MYSQLManager.executeUpdate("DELETE FROM `mails` WHERE id="+id, true);

		} catch (Exception e) {

			//Nothing

		}

	}


	//Add the mail to send
	public static synchronized void send(String cm, String to, String cc, String bcc, String subject, String body, String jsonFilenames) throws Exception {
		
		if (to==null || to.equals("")) {

			throw new Exception("Sorry, the 'to' field cannot be null or empty.");

		}

		if (subject==null || subject.equals("")) {

			throw new Exception("Sorry, the 'subject' cannot be null or empty.");

		}

		if (body==null || body.equals("")) {

			throw new Exception("Sorry, the 'body' cannot be null or empty.");

		}

		if (cc.equals("")) cc = null;
		if (bcc.equals("")) bcc = null;

		//Generate an error the a file does not exist
		JSONArray filenames = (JSONArray) JsonManager.load(jsonFilenames);
		for(int i=0;i<filenames.size();i++) {

			String filename = (String) filenames.get(i);

			if (!(new File(filename)).exists()) {

				throw new Exception("Sorry, the file '"+filename+"' does not exist.");

			}

		}

		String mail_id = null;

		try {

			mail_id = MYSQLManager.syncExecuteUpdate("INSERT INTO `mails`\n" + 
					"(`state`,\n" + 
					"`lastattempt`,\n" + 
					"`nbattempt`,\n" + 
					"`subject`,\n" + 
					"`body`,\n" + 
					"`maillist`,\n" + 
					"`mailcc`,\n" + 
					"`mailbcc`,\n" + 
					"`errmsg`,\n" + 
					"`cm`)\n" + 
					"VALUES\n" + 
					"('W',\n" + 
					"null,\n" + 
					"0,\n" + 
					SQLManager.encode(subject)+",\n" + 
					SQLManager.encode(body)+",\n" + 
					SQLManager.encode(to)+",\n" + 
					SQLManager.encode(cc)+",\n" + 
					SQLManager.encode(bcc)+",\n" + 
					"null,\n" + 
					SQLManager.encode(cm)+");", true, true);

			//Add all files
			for(int i=0;i<filenames.size();i++) {

				String filename = (String) filenames.get(i);

				String fname = (new File(filename)).getName();

				MYSQLManager.executeUpdate("INSERT INTO `mail_files`\n" + 
						"(`id_mail`,\n" + 
						"`filename`,\n" + 
						"`b64`)\n" + 
						"VALUES\n" + 
						"("+mail_id+",\n" + 
						SQLManager.encode(fname)+",\n" + 
						SQLManager.encode(FileFx.b64_read(null, filename))+");", true);

			}

		} catch (Exception e) {

			if (mail_id!=null) {

				MYSQLManager.executeUpdate("DELETE FROM `mails` WHERE id="+mail_id, true);

			}

			throw new Exception(""+e.getMessage());

		}

	}

}
