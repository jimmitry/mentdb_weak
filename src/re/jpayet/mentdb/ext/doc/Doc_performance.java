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

package re.jpayet.mentdb.ext.doc;

//Basic doc page
public class Doc_performance {
	
	static String p = "performance";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Performance");

		String html = "<style>table {\n";
				html += "    border-collapse: collapse;\n";
				html += "}\n";
				html += "\n";
				html += "table, th, td {\n";
				html += "    border: 1px solid black;padding: 8px;\n";
				html += "}</style><table style='width:100%;text-align:center'>\n"+
				"<tbody>\n"+
				"<tr>\n"+
				"<td style='text-align:left;font-weight:bold'>OBJECT</td>\n"+
				"<td style='width:100px;font-weight:bold'>READ</td>\n"+
				"<td style='width:100px;font-weight:bold'>WRITE</td>\n"+
				"<td style='width:100px;font-weight:bold'>STIMULATION</td>\n"+
				"</tr>\n"+
				"<tr>\n"+
				"<td style='text-align:left'>Symbol</td>\n"+
				"<td style='color:#00B;font-weight:bold'>25333/s</td>\n"+
				"<td>1551/s</td>\n"+
				"<td>284/s</td>\n"+
				"</tr>\n"+
				"<tr>\n"+
				"<td style='text-align:left'>Word</td>\n"+
				"<td style='color:#090;font-weight:bold'>28035/s</td>\n"+
				"<td>56/s</td>\n"+
				"<td>72/s</td>\n"+
				"</tr>\n"+
				"<tr>\n"+
				"<td style='text-align:left'>Thought</td>\n"+
				"<td style='color:#090;font-weight:bold'>28035/s</td>\n"+
				"<td>349/s</td>\n"+
				"<td>233/s</td>\n"+
				"</tr>\n"+
				"<tr>\n"+
				"<td style='text-align:left'>Relation</td>\n"+
				"<td style='color:#00B;font-weight:bold'>26129/s</td>\n"+
				"<td>114/s</td>\n"+
				"<td>54/s</td>\n"+
				"</tr>\n"+
				"<tr>\n"+
				"<td style='text-align:left'>JSON Node</td>\n"+
				"<td style='color:#00B;font-weight:bold'>22500/s</td>\n"+
				"<td>2642/s</td>\n"+
				"<td></td>\n"+
				"</tr>\n"+
				"</tbody>\n"+
				"</table>\n";

			Doc.createSection(p, "System", "MacBook Pro<br>"+
					"Intel Core i5 2,6 GHz<br>"+
					"Processor :	1, Core : 2<br>"+
					"Mémoire :	8 Go<br>"+
					"APPLE SSD SM0256F : 251 Go");
			Doc.createSection(p, "Object performance", html);
		
	}

}
