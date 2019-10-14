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

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class WordManager {

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

		HWPFDocument doc = new HWPFDocument(new FileInputStream(source));

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

	private static void replaceText(HWPFDocument doc, String findText, String replaceText) {

		Range r = doc.getRange();
		for (int i = 0; i < r.numSections(); ++i) {
			Section s = r.getSection(i);
			for (int j = 0; j < s.numParagraphs(); j++) {
				Paragraph p = s.getParagraph(j);
				for (int k = 0; k < p.numCharacterRuns(); k++) {
					CharacterRun run = p.getCharacterRun(k);
					String text = run.text();
					if (text.contains(findText)) {
						run.replaceText(findText, replaceText);
					}
				}
			}
		}

	}

}
