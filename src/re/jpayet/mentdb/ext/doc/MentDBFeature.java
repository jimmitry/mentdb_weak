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

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.tools.Misc;

//The MentDB feature object
public class MentDBFeature {

	public static LinkedHashMap<String, Vector<MentDBFeature>> features = new LinkedHashMap<String, Vector<MentDBFeature>>();
	public static LinkedHashMap<String, Integer> featuresPercent = new LinkedHashMap<String, Integer>();

	//Properties
	public String featureId = "";
	public String mentdb_info = "";
	public String jimmitry_info = "";
	public int percent = 0;
	
	//Constructor
	public MentDBFeature(String featureId, String mentdb_info, String jimmitry_info, int percent) {

		this.featureId = featureId;
		this.mentdb_info = mentdb_info;
		this.jimmitry_info = jimmitry_info;
		this.percent = percent;
		
	}
	
	//To HTML
public static String to_html() {
		
		//Initialization
		String result = "";
		
		int j=0;
		for (Entry<String, Vector<MentDBFeature>> e : MentDBFeature.features.entrySet()) {

			if (j==0) {
				result += "<div class='featuresTab'><div class='featuresTrTitle'><div class='featuresTdTitle2' style='text-align:left'>";
				if (MentDBFeature.featuresPercent.get(e.getKey())!=100) {
					result += "<div style='vertical-align:middle;display:inline-block;height:15px;width:100px;border:1px #515151 solid'><div style='height:15px;width:"+MentDBFeature.featuresPercent.get(e.getKey())+"px;background-color:rgb(41, 102, 146)'></div></div>";
				} else {
					result += "<div style='vertical-align:middle;display:inline-block;height:15px;width:100px;border:1px #515151 solid'><div style='height:15px;width:"+MentDBFeature.featuresPercent.get(e.getKey())+"px;background-color:rgb(112, 134, 68)'></div></div>";
				}
				result += " &nbsp; "+e.getKey()+"</div><div class='featuresTdTitle3'>MENTDB</div></div>";
			} else {
				result += "<div class='featuresTab'><div class='featuresTrTitle'><div class='featuresTdTitle2' style='text-align:left'>";
				
				if (MentDBFeature.featuresPercent.get(e.getKey())!=100) {
					result += "<div style='vertical-align:middle;display:inline-block;height:15px;width:100px;border:1px #515151 solid'><div style='height:15px;width:"+MentDBFeature.featuresPercent.get(e.getKey())+"px;background-color:rgb(41, 102, 146)'></div></div>";
				} else {
					result += "<div style='vertical-align:middle;display:inline-block;height:15px;width:100px;border:1px #515151 solid'><div style='height:15px;width:"+MentDBFeature.featuresPercent.get(e.getKey())+"px;background-color:rgb(112, 134, 68)'></div></div>";
				}
				result += " &nbsp; "+e.getKey()+"</div><div class='featuresTdTitle3'></div></div>";
			}
			
			for(int i=0;i<e.getValue().size();i++) {
			
				//Get the current MQLDocumentation object
				
				MentDBFeature currentFeature = e.getValue().get(i);
			
				result += "<div class='featuresTr'><div class='featuresTd2'>";
				if (currentFeature.percent!=100) {
					result += "<div style='vertical-align:middle;display:inline-block;height:10px;width:100px;border:1px #515151 solid'><div style='height:10px;width:"+currentFeature.percent+"px;background-color:#4c9ed9'></div></div>";
				} else {
					result += "<div style='vertical-align:middle;display:inline-block;height:10px;width:100px;border:1px #515151 solid'><div style='height:10px;width:"+currentFeature.percent+"px;background-color:rgb(167, 210, 80)'></div></div>";
				}
				result += " &nbsp; "+currentFeature.featureId+" </div><div class='featuresTd3'>"+currentFeature.mentdb_info.replace("[OK]", "<img src='images/ok.png'>&nbsp; ").replace("[KO]", "<img src='images/cross.png'>&nbsp; ")+"</div></div>";
			
			}
			j++;
			result += "</div>";
			
		}
		
		return result;
		
	}
	
	//Initialization
	public static void init() throws Exception {
		
		features.put("Priority 1", new Vector<MentDBFeature>());featuresPercent.put("Priority 1", 0);
		features.get("Priority 1").add(new MentDBFeature("<span style='font-weight:bold;color:#F00'>========></span> Extend the index file in the Experience Database", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("<span style='font-weight:bold;color:#F00'>===></span> Import file from the MQL Editor", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("Code generator to import XML into a database", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("Code generator to import JSON into a database", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("<span style='font-weight:bold;color:#F00'>==></span> Code generator to import Excel into a database", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("<span style='font-weight:bold;color:#F00'>==></span> Code generator to import Excelx into a database", "[KO]", "[KO]", 0));
		features.get("Priority 1").add(new MentDBFeature("<span style='font-weight:bold;color:#F00'>=></span> More Machine Learning algorithm", "[KO]", "[KO]", 0));

		features.put("Priority 2", new Vector<MentDBFeature>());featuresPercent.put("Priority 2", 0);
		features.get("Priority 2").add(new MentDBFeature("Access RS232", "[KO]", "[KO]", 0));
		features.get("Priority 2").add(new MentDBFeature("Access files throught FTP with the Editor", "[KO]", "[KO]", 0));
		features.get("Priority 2").add(new MentDBFeature("Access files throught FTPs with the Editor", "[KO]", "[KO]", 0));
		features.get("Priority 2").add(new MentDBFeature("Access files throught sFTP with the Editor", "[KO]", "[KO]", 0));
		features.get("Priority 2").add(new MentDBFeature("Access files throught SAMBA (CIFS) with the Editor", "[KO]", "[KO]", 0));

		features.put("MENTALESE STRUCTURE", new Vector<MentDBFeature>());featuresPercent.put("MENTALESE STRUCTURE", 100);
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Languages", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Symbols", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Words", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Thoughts", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Relations", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Perception", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("Storage > Experience", "[OK]", "[OK]", 100));
		features.get("MENTALESE STRUCTURE").add(new MentDBFeature("MQL engine", "[OK]", "[OK]", 100));
		
		features.put("Analytics and Machine Learning", new Vector<MentDBFeature>());featuresPercent.put("Analytics and Machine Learning", 100);
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Predictive analysis", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Linear regression", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Multiple regression", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Cluster", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Distance", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Neural network", "[OK]", "[KO]", 100));
		features.get("Analytics and Machine Learning").add(new MentDBFeature("Cognitive research (Mentalese)", "[OK]", "[KO]", 100));
		
		features.put("Data-Quality", new Vector<MentDBFeature>());featuresPercent.put("Data-Quality", 100);
		features.get("Data-Quality").add(new MentDBFeature("SQL database analysis", "[OK]", "[KO]", 100));
		features.get("Data-Quality").add(new MentDBFeature("Multiple analysis in a selection", "[OK]", "[KO]", 100));
		features.get("Data-Quality").add(new MentDBFeature("24 standard algorithms", "[OK]", "[KO]", 100));
		features.get("Data-Quality").add(new MentDBFeature("1 cognitive algorithms", "[OK]", "[KO]", 100));
		
		features.put("APP MANAGER", new Vector<MentDBFeature>());featuresPercent.put("APP MANAGER", 100);
		features.get("APP MANAGER").add(new MentDBFeature("Web framework", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("Virtual host", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("Web server", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("1 theme by default", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("HTML + CSS + BOOTSTRAP 4", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("AJAX development server side", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("AJAX without web-service (included in the default session)", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("SCRUD generator", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("User and right already created", "[OK]", "[KO]", 100));
		features.get("APP MANAGER").add(new MentDBFeature("Chart ready to use", "[OK]", "[KO]", 100));
		
		features.put("ESB", new Vector<MentDBFeature>());featuresPercent.put("ESB", 100);
		features.get("ESB").add(new MentDBFeature("Stack (Asyncronous execution)", "[OK]", "[KO]", 100));
		features.get("ESB").add(new MentDBFeature("Job (Triggered execution in time)", "[OK]", "[KO]", 100));
		
		features.put("ETL Source/Destination", new Vector<MentDBFeature>());featuresPercent.put("ETL Source/Destination", 100);
		features.get("ETL Source/Destination").add(new MentDBFeature("Transform data", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("CSV", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("HTML", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("JSON", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("XML", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Atom (string in list separate by a charactere)", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("File", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("File Watcher", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("REST", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("SOAP", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Compress", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("SQL Database (14 engines)", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("MentDB", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Pop3", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Imap", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("SMTP", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("FTP", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("FTPS", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("SFTP", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("CIFS", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("SSH", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Excel", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Excelx", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Word (export)", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("Wordx (export)", "[OK]", "[KO]", 100));
		features.get("ETL Source/Destination").add(new MentDBFeature("PDF", "[OK]", "[KO]", 100));
		
		features.put("SOA Architecture", new Vector<MentDBFeature>());featuresPercent.put("SOA Architecture", 100);
		features.get("SOA Architecture").add(new MentDBFeature("High Availability", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Calculates parallel", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Connect to remote MentDB server", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Organize all your developments in scripts", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Immediately REST Full (Web service)", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Secured by users and encrypted", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Documented", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Log trace manager", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("Execute business action by web service", "[OK]", "[KO]", 100));
		features.get("SOA Architecture").add(new MentDBFeature("User and Right", "[OK]", "[KO]", 100));
		
		features.put("CLIENT GUI", new Vector<MentDBFeature>());featuresPercent.put("CLIENT GUI", 100);
		features.get("CLIENT GUI").add(new MentDBFeature("MQL Editor", "[OK]", "[OK]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Bot Access", "[OK]", "[OK]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Chat with the AI in your language", "[OK]", "[OK]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Access to MentDB in MQL", "[OK]", "[KO]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Syntax highlighter", "[OK]", "[KO]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Portal", "[OK]", "[KO]", 100));
		features.get("CLIENT GUI").add(new MentDBFeature("Only one session by user (web socket for mentdb)", "[OK]", "[OK]", 100));
		
		features.put("SERVER", new Vector<MentDBFeature>());featuresPercent.put("SERVER", 100);
		features.get("SERVER").add(new MentDBFeature("Server", "MentDB Core", "My brain", 100));
		features.get("SERVER").add(new MentDBFeature("Log file rolling", "[OK] logs/mentdbX.log", "[OK] My diseases", 100));
		features.get("SERVER").add(new MentDBFeature("Synchronous or asynchronous", "[OK]", "[OK]", 100));
		features.get("SERVER").add(new MentDBFeature("Multiple threads", "[OK]", "[OK]", 100));
		features.get("SERVER").add(new MentDBFeature("Stat / Metric", "[OK]", "[KO]", 100));
		features.get("SERVER").add(new MentDBFeature("Single configuration file", "[OK]", "[KO]", 100));
		features.get("SERVER").add(new MentDBFeature("Sequence", "[OK]", "[KO]", 100));
		features.get("SERVER").add(new MentDBFeature("Node", "[OK]", "[KO]", 100));
		
		features.put("API", new Vector<MentDBFeature>());featuresPercent.put("API", 100);
		features.get("API").add(new MentDBFeature("Java", "Java Connector", "My 5 senses", 100));
		features.get("API").add(new MentDBFeature("Web socket", "JSON message through web socket", "My 5 senses", 100));
		features.get("API").add(new MentDBFeature("REST", "Access to mental script in RESP API", "My 5 senses", 100));
		features.get("API").add(new MentDBFeature("REST Web Interface", "Try script in a web interface", "[KO]", 100));
		
		features.put("DOCUMENTATION", new Vector<MentDBFeature>());featuresPercent.put("DOCUMENTATION", 100);
		features.get("DOCUMENTATION").add(new MentDBFeature("MQL Documentation", "[OK]", "[KO]", 100));
		features.get("DOCUMENTATION").add(new MentDBFeature("Source code", "[OK]", "[KO] LOL - no documentation", 100));

		features.put("OS & PROGRAMING LANGUAGE", new Vector<MentDBFeature>());featuresPercent.put("OS & PROGRAMING LANGUAGE", 100);
		features.get("OS & PROGRAMING LANGUAGE").add(new MentDBFeature("Programing language", "JAVA", "My DNA", 100));
		features.get("OS & PROGRAMING LANGUAGE").add(new MentDBFeature("MacOSX - Linux - Windows", "[OK]", "LOL, My body v_1980", 100));

		Misc.create("docs"+File.separator+"features-roadmap.html", Misc.load("docs"+File.separator+"features-roadmap_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[Features]", MentDBFeature.to_html()).replace("[Features]", MentDBFeature.to_html()).replace("[MQL_VERSION]", Start.version));
		
	}

}