package re.jpayet.mentdb.ext.mail;

import java.io.InputStream;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;

import com.sun.mail.util.BASE64DecoderStream;

public class CheckSendPart implements Runnable {
	public Store store = null;
	public Folder folder = null;
	public Message msg = null;
	public String markAsRead = null;
	public long end = 0;
	public InputStream initialStream = null;
	public BASE64DecoderStream initialStreamb64 = null;
	
	public boolean play = true;
	
	public CheckSendPart(Store store, Folder folder, Message msg, long end, String markAsRead) {
		this.store = store;
		this.folder = folder;
		this.end = end;
		this.msg = msg;
		this.markAsRead = markAsRead;
	}
	
	public void run() {
		re.jpayet.mentdb.ext.log.Log.trace("1 - IMAP|CheckSendPart= "+end);
		while (play && System.currentTimeMillis()<end) {
			re.jpayet.mentdb.ext.log.Log.trace("2 - IMAP|CheckSendPart= "+(end-System.currentTimeMillis()));
			try {
				//re.jpayet.mentdb.ext.log.Log.trace("1#####"+(end-System.currentTimeMillis()));
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			
		}
		re.jpayet.mentdb.ext.log.Log.trace("3 - IMAP|CheckSendPart= "+play);
		if (play) {
			re.jpayet.mentdb.ext.log.Log.trace("4 - IMAP|CheckSendPart");

			try {
				initialStream.close();
			} catch (Exception e) {}

			re.jpayet.mentdb.ext.log.Log.trace("41 - IMAP|CheckSendPart");
			try {
				initialStreamb64.close();
			} catch (Exception e) {}

			re.jpayet.mentdb.ext.log.Log.trace("42 - IMAP|CheckSendPart");
			try {
				folder.close(true);
			} catch (Exception e) {}

			re.jpayet.mentdb.ext.log.Log.trace("43 - IMAP|CheckSendPart");
			try {
				store.close();
			} catch (Exception e) {}
			
		}
		re.jpayet.mentdb.ext.log.Log.trace("5 - IMAP|CheckSendPart");
	}
}
