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

package re.jpayet.mentdb.ext.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class WordxManager {

	public static void replace_tags(String source, String destination, String jsonMap) throws Exception {

		//Generate an error if the source does not exist
		if (FileFx.exist(source).equals("0")) {

			throw new Exception("Sorry, the source '"+source+"' does not exist.");

		}

		//Generate an error if the destination already exist
		if (FileFx.exist(destination).equals("1")) {

			throw new Exception("Sorry, the destination '"+destination+"' already exist.");

		}

		JSONObject map = (JSONObject) JsonManager.load(jsonMap);

		XWPFDocument doc = new XWPFDocument(new FileInputStream(source));

		for(Object o : map.keySet()) {

			String findText = (String) o;
			String replaceText = (String) map.get(findText);
			replaceText(doc, findText, replaceText);

		}

		//Write the document
		FileOutputStream out = new FileOutputStream(destination);

		try {

			doc.write(out);

		} catch (Exception e) {

			throw e;

		} finally {

			try {doc.close();} catch (Exception e) {};
			try {out.close();} catch (Exception e) {};

		}

	}

	private static void replaceText(XWPFDocument doc, String findText, String replaceText) {

		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null && text.contains(findText)) {
		                text = text.replace(findText, replaceText);
		                r.setText(text, 0);
		            }
		        }
		    }
		}
		for (XWPFTable tbl : doc.getTables()) {
		   for (XWPFTableRow row : tbl.getRows()) {
		      for (XWPFTableCell cell : row.getTableCells()) {
		         for (XWPFParagraph p : cell.getParagraphs()) {
		            for (XWPFRun r : p.getRuns()) {
		              String text = r.getText(0);
		              if (text != null && text.contains(findText)) {
		                text = text.replace(findText, replaceText);
		                r.setText(text,0);
		              }
		            }
		         }
		      }
		   }
		}
		
	}

}
