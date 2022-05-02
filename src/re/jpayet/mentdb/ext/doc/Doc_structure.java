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
public class Doc_structure {
	
	static String p = "structure";
	
	//Load the page
	public static void load() {
		
		Doc.createPage(p, "Mentalese structure");

		String structure = "<p>Machine learning and deep learning are very interesting to finally reproduce the 5 senses, perception and roughly the habit ...</p>";

		structure += "<p>But all this is very far from the grail we are looking for, that is, a machine that thinks in its head, cooperates or not, thanks to the emotions it receives / feels, and not just an algorithm that finds solutions.</p>";

		structure += "<p>We will completely fill this puzzle, only when we have written a thinking algorithm. And so, the famous language of thought that makes us think in our heads and sometimes aloud.</p>";

		structure += "<p>We are, in a way, a super self-programmable process manager, linked to an emotion manager, above a totally symbolic storage structure (allowing cognitive fast restitution).</p>";

		structure += "<p>It is the use of these 3 main blocks \"the emotion manager + the mental process manager + the cognitive storage engine\" (There are many more), which do most of the work . Like a database: \"a session manager + stored procedure manager + a storage structure\" ...</p>";

		structure += "<br><div style='padding:0px 20px 20px 20px;border:1px #E0E0E0 solid;background-color:#F5F5F5;text-align:center;color:#777;-webkit-border-radius: 10px;-moz-border-radius: 10px;border-radius: 10px;'>";
		structure += "<h2>All in a single \"Mentalese Query Language\" (MQL)</h2>";
		
		structure += "<div style='text-align:center;color:#6B8D25;border:1px #e0e0e0 solid;-webkit-border-radius: 10px;-moz-border-radius: 10px;border-radius: 10px;background: #F9F9F9;', endColorstr='#b4df5b',GradientType=0 ); /* IE6-9 */'>";
		structure += "<span style='color:#555'><h3>Emotion manager (Coming soon ...)</h3></span>";
		structure += "It is here that I copy my feelings (I know it seems impossible ... but you will see ...).<br>";
		structure += "That I would deduce mental processes by mixing emotional levels,<br>";
		structure += "linked to relationships and thoughts ...<br>&nbsp;";
		structure += "</div>";
		structure += "<br><div style='text-align:center;color:#A16224;border:1px #e0e0e0 solid;-webkit-border-radius: 10px;-moz-border-radius: 10px;border-radius: 10px;";
		structure += "background: #F9F9F9;'>";
		structure += "<h3>Mental process</h3>";
		structure += "A sequence of mental processes that can be called / recalled at any time.<br>";
		structure += "To consider / reconsider things. To think, to generate common sense,<br>";
		structure += "to follow its logical objectives or its emotional reflexes ...<br>&nbsp;";
		structure += "</div>";
		structure += "<br><div style='text-align:center;color:#255a90;border:1px #e0e0e0 solid;-webkit-border-radius: 10px;-moz-border-radius: 10px;border-radius: 10px;background: #F9F9F9;'>";
		structure += "<h3>Cognitive storage engine</h3>";
		structure += "A cognitive structure to save symbols, words, thoughts, relations and all emotional colorations.<br>";
		structure += "Structured data to be able to calculate perception,<br>";
		structure += "and all things that the mental process manager needed ...<br>&nbsp;";
		structure += "</div>";

		structure += "</div>";
		
		Doc.createSection(p, "MentDB structure", structure);
		
	}

}
