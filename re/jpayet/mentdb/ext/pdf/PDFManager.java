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

package re.jpayet.mentdb.ext.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;

//PDF
public class PDFManager {

	//Generate PDF from HTML
	public static void pdfFromHtml(String html, String filePath) throws Exception {
		
		XRLog.setLoggingEnabled(false);
		OutputStream os = null;

		try {
			os = new FileOutputStream(filePath);

			try {
				
				PdfRendererBuilder builder = new PdfRendererBuilder();
				
				builder.withHtmlContent(html, "file:/");
				
				builder.toStream(os);
				
				builder.run();
				
			} catch (Exception e) {
				
				throw new Exception("Sorry, there is a PDF error ("+e.getMessage()+").");

			} finally {
				try {
					os.close();
				} catch (IOException e) {}
			}
		} catch (IOException e1) {}

	}

}