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

import java.util.LinkedHashMap;
import java.util.Vector;

public class Doc_Mentalese_Extract_Load {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {
		
		page_group.put("CSV", "Extract and Load");
		
		functions.put("CSV", new Vector<MQLDocumentation>());
		page_description.put("CSV", "<img src='images/p.png' style='vertical-align: middle;'>CSV parser.");
		mql = new MQLDocumentation(true, "csv parse", "Parse CSV file", "csv parse (mql \"T\") (mql \"/Users/jimmitry/Desktop/file.csv\") (mql \",\") (mql \"'\") (mql \"A,B,C\") {<br><br>	log trace [T_A];<br><br>};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("namespace", "The namespace", "string", true));
		mql.addParam(new MQLParam("filePath", "The file path", "string", true));
		mql.addParam(new MQLParam("columnSeparator", "The column separator", "string", true));
		mql.addParam(new MQLParam("quoteChar", "The quote char", "string", true));
		mql.addParam(new MQLParam("forceColumnNames", "To force the column name (can be empty)", "string", true));
		mql.addParam(new MQLParam("mqlAction", "The MQL action to execut on each line", "string", true));
		functions.get("CSV").add(mql);
		
		functions.put("HTML", new Vector<MQLDocumentation>());
		page_description.put("HTML", "<img src='images/p.png' style='vertical-align: middle;'>HTML parser.");
		mql = new MQLDocumentation(true, "html load_from_str", "Load a HTML document from a string", "html load_from_str \"domId1\" \"...\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		mql.addParam(new MQLParam("html", "The html", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html load_from_url", "Load a HTML document from an url", "html load_from_url \"domId1\" \"https://www.tripadvisor.fr/Hotel_Review-g298470-d1473791-Reviews-LUX_Saint_Gilles-Saint_Gilles_Les_Bains_Arrondissement_of_Saint_Paul.html\" \"get\" 5000 \"{}\" \"{}\" \"{}\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("method", "The method (GET|POST)", "string", true));
		mql.addParam(new MQLParam("timeout", "The timeout (ex: 5000)", "number", true));
		mql.addParam(new MQLParam("jsonHeaders", "The JSON headers key map", "string", true));
		mql.addParam(new MQLParam("jsonCookies", "The JSON cookies key map", "string", true));
		mql.addParam(new MQLParam("jsonData", "The JSON data key map", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html close", "Close a document", "html close \"domId1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html close_all", "Close all documents", "html close_all;", "2", null, null, null, null, false, "");
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html show", "To show all open documents", "html show", "[\"domId1\"];", null, null, null, null, false, "");
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html exist", "To check if a document already exist", "html exist \"domId1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html element", "To load a HTML element into a json object", "html element \"domId1\" \"jsonDoc1\" \"id\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		mql.addParam(new MQLParam("jsonDoc", "The JSON document id", "string", true));
		mql.addParam(new MQLParam("id", "The element id", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "html parse", "To execute actions on HTML elements (JSOUP syntaxe)", "html parse \"domId1\" \"jsonDoc1\" \"TAG\" \"a\" \"\" {\n\n	#Here your MQL source code ...;\n	json select \"jsonDoc1\" \"/text\";\n" + 
				"	json select \"jsonDoc1\" \"/html\";\n" + 
				"	json select \"jsonDoc1\" \"/formVal\";\n" + 
				"	json select \"jsonDoc1\" \"/tagName\";\n" + 
				"	json select \"jsonDoc1\" \"/id\";\n" + 
				"	json select \"jsonDoc1\" \"/nodeName\";\n" + 
				"	json select \"jsonDoc1\" \"/outerHtml\";\n" + 
				"	json select \"jsonDoc1\" \"/ownText\";\n" + 
				"	json select \"jsonDoc1\" \"/wholeText\";\n" + 
				"	json select \"jsonDoc1\" \"/attributes/...\";\n" + 
				"	json select \"jsonDoc1\" \"/classNames[0]\";\n\n};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("domId", "The DOM id", "string", true));
		mql.addParam(new MQLParam("jsonDoc", "The JSON document id", "string", true));
		mql.addParam(new MQLParam("searchType", "The search type (TAG | ATTRIBUTE | SELECT | CLASS | ATTRIBUTE_VALUE | CONTAINING_OWN_TEXT | CONTAINING_TEXT | OWN_TEXT_REGEX | TEXT_REGEX | ATTRIBUTE_STARTING | ATTRIBUTE_VALUE_CONTAINING | ATTRIBUTE_VALUE_ENDING | ATTRIBUTE_VALUE_REGEX | ATTRIBUTE_VALUE_NOT | ATTRIBUTE_VALUE_STARTING)", "string", true));
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("mqlActions", "The MQL actions", "string", true));
		functions.get("HTML").add(mql);
		
		functions.put("JSON", new Vector<MQLDocumentation>());
		page_description.put("JSON", "<img src='images/p.png' style='vertical-align: middle;'>You also have MQL functions to handle JSON in memory.");
		mql = new MQLDocumentation(true, "json load", "To load a json string", "json load \"keyId\" \"{}\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jsonString", "The JSON string", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json show", "To show all JSON object saved into the session", "json show;", "[\"keyId\",\"newKeyId\"]", null, null, null, null, false, "");
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json load select", "To load a json string and select an element", "json load select \"keyId\" \"{\\\"a\\\": 5.0, \\\"b\\\": {\\\"c\\\": 7.0}}\" /a;", "5.0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jsonString", "The JSON string", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json load by ref", "To load by ref a json string from another json object", "json load by ref \"keyId\" /b \"newKeyId\";", "{<br>  \"c\": 7.0<br>}", "json uobject \"newKeyId\" / c 8 NUM;<br>json select \"newKeyId\" /c;", "8.0", "json doc \"keyId\";", "{<br>  \"a\": 5.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  }<br>}", false, "");
		mql.addParam(new MQLParam("fromKey", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("new KeyId", "The JSON string", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json iobject", "To insert an element into an object in a node", 
				"json iobject \"keyId\" / a 5 NUM;", "1", "json iobject \"keyId\" / tab \"[]\" ARRAY;", "1", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json count", "To count element in a node", "json count \"keyId\" /", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json select", "To select an element in a node", "json select \"keyId\" /a", "5.0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json parse_obj", "To execute actions on a JSON object", "json parse_obj \"keyId\" \"/\" \"[key]\" \"[val]\" {\n\n	#Here your MQL source code ...;\n	log trace (concat [key] \"=\" [val]);\n\n};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("docId", "The document id", "string", true));
		mql.addParam(new MQLParam("jsonPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("varKey", "The key variable (ex: [key])", "string", true));
		mql.addParam(new MQLParam("varValue", "The value variable (ex: [val])", "string", true));
		mql.addParam(new MQLParam("mqlActions", "The MQL actions", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "json parse_array", "To execute actions on a JSON array", "json parse_array \"keyId\" \"/\" \"[val]\" {\n\n	#Here your MQL source code ...;\n	[val];\n\n};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("docId", "The document id", "string", true));
		mql.addParam(new MQLParam("jsonPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("varValue", "The value variable (ex: [val])", "string", true));
		mql.addParam(new MQLParam("mqlActions", "The MQL actions", "string", true));
		functions.get("HTML").add(mql);
		mql = new MQLDocumentation(true, "json is object", "To check if an element is an object in a node", "json is object \"keyId\" /", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json is array", "To check if an element is an array in a node", "json is array \"keyId\" /tab", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json exist", "To check if a key is loaded", "json exist \"keyId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json doc", "To get a json document", "json doc \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": []<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json fields", "To get all fields into an object in a node", "json fields \"keyId\" /", "[<br>  \"a\",<br>  \"b\",<br>  \"tab\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json iarray", "To insert an element into an array in a node", "json iarray \"keyId\" /tab test STR;", "1", "json iarray \"keyId\" /tab test2 STR", "1", "json doc \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test\",<br>    \"test2\"<br>  ]<br>}", false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json iarray", "To insert an element into an array in a node", "json iarray \"keyId\" /tab 0 test STR;", "1", "json iarray \"keyId\" /tab test2 STR", "1", "json doc \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test\",<br>    \"test\",<br>    \"test2\",<br>    \"test2\"<br>  ]<br>}", false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index position", "number", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json uarray", "To update an element into an array in a node", "json uarray \"keyId\" /tab 1 test2 STR;", "1", "json doc \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test\",<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ]<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index", "integer", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json uobject", "To update an element into an object in a node", "json uobject \"keyId\" / a 8 NUM;", "1", "json doc \"keyId\"", "{<br>  \"a\": 8.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test\",<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ]<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json darray", "To delete an element from an array in a node", "json darray \"keyId\" /tab 0;", "1", "json doc \"keyId\"", "{<br>  \"a\": 8.0,<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ]<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index", "integer", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json dobject", "To delete an element from an object in a node", "json dobject \"keyId\" / a;", "1", "json doc \"keyId\"", "{<br>  \"b\": {<br>    \"c\": 8.0<br>  },<br>  \"tab\": [<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ]<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json unload", "To unload a json document", "json unload \"keyId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("JSON").add(mql);
		mql = new MQLDocumentation(true, "json unload_all", "To unload all json documents", "json unload_all;", "1", null, null, null, null, false, "");
		functions.get("JSON").add(mql);
		
		functions.put("XML", new Vector<MQLDocumentation>());
		page_description.put("XML", "<img src='images/p.png' style='vertical-align: middle;'>Here you can manage XML data file.");
		mql = new MQLDocumentation(true, "xml load", "To load a xml string", "xml load \"keyId\" \"<data id=\\\"25\\\"><item>A</item><item>B</item><item>C</item></data>\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xmlString", "The XML string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml show", "To show all XML object saved into the session", "xml show;", "[<br>  \"keyId\"<br>]", null, null, null, null, false, "");
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml doc", "Return the xml document", "xml doc \"keyId\";", "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"no\\\"?>\n" + 
				"				<data id=\\\"25\\\">\n" + 
				"				  <item>A</item>\n" + 
				"				  <item>B</item>\n" + 
				"				  <item>C</item>\n" + 
				"				</data>\n" + 
				"				", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml exist", "To check if a key is loaded", "xml exist \"keyId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml count", "To count an object from an xlm document", "xml count \"keyId\" \"/data\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml select node", "To select a node from an xlm document", "xml select node \"keyId\" \"/data\";", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data id=\"25\">\n" + 
				"  <item>A</item>\n" + 
				"  <item>B</item>\n" + 
				"  <item>C</item>\n" + 
				"</data>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml select text", "To select a text from an xlm document", "xml select text \"keyId\" \"/data/item[1]\"", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml select attribute", "To select an attribute from an xlm document", "xml select attribute \"keyId\" \"/data/@id\"", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml xpath", "To generate all xpath string for a node", "xml xpath \"keyId\" \"/data\";", "[<br>  \"/data\",<br>  \"/data[@id\\u003d\\u002725\\u0027]\",<br>  \"/data/item\",<br>  \"/data/item[2]\",<br>  \"/data/item[3]\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml fields", "To show all fields name for a node", "xml fields \"keyId\" \"/data\"", "[<br>  \"item\",<br>  \"item\",<br>  \"item\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml itext", "To insert a new text node", "xml itext \"keyId\" \"/data\" \"number\" \"456\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		mql.addParam(new MQLParam("nodeName", "The node name", "string", true));
		mql.addParam(new MQLParam("text", "The text", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml inode", "To insert node into a xml document", "xml inode \"keyId\" \"/data\" \"<a><a1></a1><a2></a2></a>\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		mql.addParam(new MQLParam("node", "The node", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml utext", "To update a text into a xml document", "xml utext \"keyId\" \"/data/item[2]\" \"Z\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		mql.addParam(new MQLParam("text", "The text", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml iattribute", "To insert a new attribute to a node", "xml iattribute \"keyId\" \"/data\" \"id\" \"987\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		mql.addParam(new MQLParam("attributeName", "The attribute name", "string", true));
		mql.addParam(new MQLParam("text", "The text", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml dattribute", "To delete an attribute to a node", "xml dattribute \"keyId\" \"/data\" \"id\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		mql.addParam(new MQLParam("attributeName", "The attribute name", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml dnode", "To delete a specific node", "xml dnode \"keyId\" \"/data/item[2]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("xPath", "The xPath string", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml unload", "To unload a xml document", "xml unload \"keyId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml unload_all", "To unload all json documents", "xml unload_all;", "1", null, null, null, null, false, "");
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml escape_10", "Escape a xml value", "xml escape_10 \"...\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "string", true));
		functions.get("XML").add(mql);
		mql = new MQLDocumentation(true, "xml escape_11", "Escape a xml value", "xml escape_11 \"...\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "string", true));
		functions.get("XML").add(mql);

		functions.put("File Watcher", new Vector<MQLDocumentation>());
		page_description.put("File Watcher", "<img src='images/p.png' style='vertical-align: middle;'>File watcher services.");
		mql = new MQLDocumentation(true, "file_watcher start", "To start a file watcher", "file_watcher start \"fKey\" \"admin\" \"/Users/jimmitry/Desktop/dir\" \"demo.file.watcher.exe\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "String", true));
		mql.addParam(new MQLParam("user", "The user", "String", true));
		mql.addParam(new MQLParam("directory", "The directory to scann", "String", true));
		mql.addParam(new MQLParam("scriptName", "The script name", "String", true));
		functions.get("File Watcher").add(mql);
		mql = new MQLDocumentation(true, "file_watcher kill", "To kill a file watcher", "file_watcher kill \"fKey\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "String", true));
		functions.get("File Watcher").add(mql);
		mql = new MQLDocumentation(true, "file_watcher exist", "To check if a kill a file watcher already exist", "file_watcher exist \"fKey1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "String", true));
		functions.get("File Watcher").add(mql);
		mql = new MQLDocumentation(true, "file_watcher show", "To show all file watchers", "file_watcher show;", "[]", null, null, null, null, false, "");
		functions.get("File Watcher").add(mql);

		functions.put("File", new Vector<MQLDocumentation>());
		page_description.put("File", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "file create", "Create a new text file", "file create \"data/.id\" (string generate_random_str 12);", "1", "file create \"data/.id\" (string generate_random_str 12;) \"utf-8\"", "1", null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("data", "String data", "String", true));
		mql.addParam(new MQLParam("encoding", "The file encoding (ex: 'UTF-8')", "String", false));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file append", "Append a text into a file", "file append \"data/.id\" (string generate_random_str 12);", "1", "file append \"data/.id\" (string generate_random_str 12;) \"utf-8\"", "1", null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("data", "String data", "String", true));
		mql.addParam(new MQLParam("encoding", "The file encoding (ex: 'UTF-8')", "String", false));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file exist", "To check if a file already exist", "file exist \"data/.id\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file image_resize", "To resize an image", "file image_resize \"data/image.png\" 100 80", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("width", "The target width", "String", true));
		mql.addParam(new MQLParam("height", "The target height", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file image_rotate_90", "To resize an image", "file image_rotate_90 \"data/image.png\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file is_directory", "To check if a file is a directory", "file is_directory \"data/.id\"", "0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file load", "To get text from a file", "file load \"data/.id\";", "XdoZMCbr6o7IUElQ8fDGqtVFoDAksJRRpxbk", "file load \"data/.id\" \"UTF-8\"", "2pvrPdmfuTrz", null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("encoding", "The file encoding (ex: 'UTF-8')", "String", false));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file count_line dir", "Count the number of line in a directory", "file count_line dir \"/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB\" \".java\"", "81354", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dirPath", "The directory", "String", true));
		mql.addParam(new MQLParam("endOfFile", "The end of file (ex: '.java')", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file count_lines", "Count the number of line in a file", "file count_lines \"data/.id\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file meta_data", "Get meta data of a file", "file meta_data \"data/.id\" \"creationTime\"", "2018-02-25T17:55:17Z", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("attribute", "The attribute (creationTime|lastAccessTime|lastModifiedTime|author)", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file cur_abs_dir", "Get the current absolute directory path", "file cur_abs_dir", "/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB/MentDB_Server/.", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file cur_canonical_dir", "Get the current canonical directory path", "file cur_canonical_dir", "/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB/MentDB_Server", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file pwd", "Get the current absolute directory path", "file pwd", "/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB/MentDB_Server/.", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file size", "Get size in bytes of a file or a directory", "file size \"data/.id\"", "36", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file last_modified", "Get last modified timestamp of a file or a directory", "file last_modified \"data/.id\"", "2018-10-10 10:00:00", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file mkdir", "Create a new directory", "file mkdir \"dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dirPath", "The path to the directory", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file delete", "Delete a file or a directory", "file delete \"dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("path", "The path to the directory or to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file copy_dir", "Copy a directory", "file copy_dir \"logs\" \"logs2\";", "1", "file delete \"logs2\"", "1", null, null, false, "");
		mql.addParam(new MQLParam("oldDirPath", "The old directory path", "String", true));
		mql.addParam(new MQLParam("newDirPath", "The new directory path", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file copy_file", "Copy a file", "file copy_file \"read-me.txt\" \"read-me2.txt\";", "1", "file delete \"read-me2.txt\"", "1", null, null, false, "");
		mql.addParam(new MQLParam("oldFilePath", "The old file path", "String", true));
		mql.addParam(new MQLParam("newFilePath", "The new file path", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file copy_format", "Copy a file to another format", "file copy_format \"read-me.txt\" \"utf-8\" \"read-me2.txt\" \"utf-8\";", "1", "file delete \"read-me2.txt\"", "1", null, null, false, "");
		mql.addParam(new MQLParam("source", "The source file path", "String", true));
		mql.addParam(new MQLParam("sourceEncoding", "The source file encoding (ex: 'UTF-8')", "String", false));
		mql.addParam(new MQLParam("target", "The new file path", "String", true));
		mql.addParam(new MQLParam("targetEncoding", "The target file encoding (ex: 'UTF-8')", "String", false));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file ini", "Get a value in a configuration file (.INI file)", "file ini \"conf/server.conf\" \"AI\" \"FIRSTNAME\"", "lisa", null, null, null, null, false, "");
		mql.addParam(new MQLParam("path", "The file path", "String", true));
		mql.addParam(new MQLParam("section", "The section", "String", true));
		mql.addParam(new MQLParam("field", "The field", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file ini_str", "Get a value in a configuration string (.INI)", "file ini_str (file load \"conf/server.conf\") \"AI\" \"FIRSTNAME\"", "lisa", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string config file", "String", true));
		mql.addParam(new MQLParam("section", "The section", "String", true));
		mql.addParam(new MQLParam("field", "The field", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file dir_list", "List all files or folders in a directory", "file dir_list \"/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB\"", "[<br>  \".DS_Store\",<br>  \".git\",<br>  \".gitignore\",<br>  \".metadata\",<br>  \".recommenders\",<br>  \"MentDB_Editor\",<br>  \"MentDB_Server\",<br>  \"README.md\",<br>  \"RemoteSystemsTempFiles\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dirPath", "The path to the directory", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file dir_list_regex", "List all files or folders in a directory with regex filter", "file dir_list_regex \"/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB\" \".*.*\" true true", "[<br>  \".DS_Store\",<br>  \".git\",<br>  \".gitignore\",<br>  \".metadata\",<br>  \".recommenders\",<br>  \"MentDB_Editor\",<br>  \"MentDB_Server\",<br>  \"README.md\",<br>  \"RemoteSystemsTempFiles\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dirPath", "The path to the directory", "String", true));
		mql.addParam(new MQLParam("regexFilter", "The regex expression", "String", true));
		mql.addParam(new MQLParam("getFile", "Get file or not", "String", true));
		mql.addParam(new MQLParam("getDirectory", "Get directory or not", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file b64_read", "This function allow you to get a binary file in base 64 format", "file b64_read \"data/.id\"", "dWdmNjJPUlZzMTkyMlk5eGd4R3I3SU5TM0pLSW1leWppeFph", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file b64_write", "This function allow you to write a binary file from a base 64 format", "file b64_write \"dWdmNjJPUlZzMTkyMlk5eGd4R3I3SU5TM0pLSW1leWppeFph\" \"data/.id\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dataB64", "Data in base 64", "String", true));
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_open", "Open a writer", "file writer_open \"w1\" \"file.txt\" true TEXT \"utf-8\";", "1", "file writer_open \"w2\" \"image.png\" true BINARY null;", "1", null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		mql.addParam(new MQLParam("filePath", "The file path", "String", true));
		mql.addParam(new MQLParam("append", "A boolean to activate the append mode", "String", true));
		mql.addParam(new MQLParam("type", "The type (TEXT|BINARY)", "String", true));
		mql.addParam(new MQLParam("encoding", "The encoding (ex: 'utf-8')", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_exist", "Check if a writer is already opened", "file writer_exist \"w1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_show", "Show all writers", "file writer_show;", "[<br>  \"w1\",<br>  \"w2\"<br>]", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_add_line", "Add a new line to a writer", "file writer_add_line \"w1\" \"data\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		mql.addParam(new MQLParam("str", "The string to add", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_add_bytes", "Add a new bytes to a writer", "file writer_add_bytes \"w2\" \"data\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		mql.addParam(new MQLParam("bytes", "The bytes to add", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_flush", "Flush a writer", "file writer_flush \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_close", "Close a writer", "file writer_close \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file writer_closeall", "Close all writers", "file writer_closeall;", "{}", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_open", "Open a reader", "file reader_open \"r1\" \"file.txt\" TEXT \"utf-8\";", "1", "file reader_open \"r2\" \"image.png\" BINARY null", "1", null, null, false, "");
		mql.addParam(new MQLParam("readerId", "The reader id", "String", true));
		mql.addParam(new MQLParam("filePath", "The path to the file", "String", true));
		mql.addParam(new MQLParam("type", "The type (TEXT|BINARY)", "String", true));
		mql.addParam(new MQLParam("encoding", "The encoding (ex: 'utf-8')", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_exist", "Check if a ready is already opened", "file reader_exist \"r1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("readerId", "The reader id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_show", "Show all readers", "file reader_show;", "[<br>  \"r2\",<br>  \"r1\"<br>]", null, null, null, null, false, "");
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_get_line", "Get line from a reader (TEXT mode)", "file reader_get_line \"r1\"", "data", null, null, null, null, false, "");
		mql.addParam(new MQLParam("readerId", "The reader id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_get_bytes", "Get bytes from a reader (BINARY mode)", "file reader_get_bytes \"r2\" 1024", "data", null, null, null, null, false, "");
		mql.addParam(new MQLParam("readerId", "The reader id", "String", true));
		mql.addParam(new MQLParam("nbBytes", "The number of bytes", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_close", "Close a reader", "file reader_close \"r1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("readerId", "The reader id", "String", true));
		functions.get("File").add(mql);
		mql = new MQLDocumentation(true, "file reader_closeall", "Close all readers", "file reader_closeall;", "{}", "file delete \"file.txt\"", "1", "file delete \"image.png\"", "1", false, "");
		functions.get("File").add(mql);
		
		functions.put("Atom", new Vector<MQLDocumentation>());
		page_description.put("Atom", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "atom before_exclud", "get the atom list before the first occurrence of an atom in an atom list (excluded atom).<br>", "atom before_exclud \" A, B,A ,A ,C, D   \" \"3\" \",\"", " A, B", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("index", "The index atom", "Number", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom before_includ", "get the atom list before the first occurrence of an atom in an atom list (included atom).<br>", "atom before_includ \" A, B,A ,A ,C, D   \" \"3\" \",\"", " A, B,A ", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("index", "The index atom", "Number", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom count", "count the occurrence number of an atom in an atom list.<br>", "atom count \" A, B,A ,A ,C, D   \" \"A \" \",\"", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToCount", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom count_distinct", "count the occurrence number of a distinct atom in an atom list.<br>", "atom count_distinct \" A, B,A ,A ,C, D   \" \"A \" \",\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToCount", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom count_lrtrim", "count the occurrence number of a lrtrim atom in an atom list.<br>", "atom count_lrtrim \" A, B,A ,A ,C, D   \" \"A \" \",\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToCount", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom count_lrtrim_distinct", "count the occurrence number of a lrtrim distinct atom in an atom list.<br>", "atom count_lrtrim_distinct \" A, B,A ,A ,C, D   \" \"A \" \",\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToCount", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom distinct", "get distinct atom for each atom in a list<br>", "atom distinct \"A,B,A,A ,C,D\" \",\"", "A,B,A ,C,D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom distinct_lrtrim", "get lrtrim distinct atom for each atom in a list<br>", "atom distinct_lrtrim \" A, B,A ,A ,C, D   \" \",\"", "A,B,C,D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom distinct_lrtrim_1sbefore", "get lrtrim distinct atom for each atom in a list. add one space before all atoms, except the first.<br>", "atom distinct_lrtrim_1sbefore \" A, B,A ,A ,C, D   \" \",\"", "A, B, C, D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom find", "find the position of atom in an atom list.<br>", "atom find \" A, B,A ,A ,C, D   \" \"A \" \",\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToFind", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom find_lrtrim", "find the position of atom in an atom list. lrtrim on each atom before.<br>", "atom find_lrtrim \" A, B,A ,A ,C, D   \" \"A \" \",\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToFind", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get", "get atom at the specific index in an atom list<br>", "atom get \"A,B,C,D\" 2 \",\"", "B", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("index", "Position of the atom (start to 1)", "Number", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get_first", "get the first atom in an atom list<br>", "atom get_first \"A, B  ,C,D\" \",\"", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get_first_lrtrim", "get the first lrtrim atom in an atom list<br>", "atom get_first_lrtrim \"  A, B  ,C,D\" \",\"", "A", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get_last", "get the last atom in an atom list<br>", "atom get_last \"A, B  ,C,D\" \",\"", "D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get_last_lrtrim", "get the last lrtrim atom in an atom list<br>", "atom get_last_lrtrim \"  A, B  ,C,  D\" \",\"", "D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom get_lrtrim", "get lrtrim atom at the specific index in an atom list<br>", "atom get_lrtrim \"A, B  ,C,D\" 2 \",\"", "B", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("index", "Position of the atom (start to 1)", "Number", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom lrtrim", "get lrtrim atom for each atom in a list<br>", "atom lrtrim \" A, B,A ,A ,C, D   \" \",\"", "A,B,A,A,C,D", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom position", "find the position of atom in an atom list.<br>", "atom position \" A, B,A ,A ,C, D   \" \"A \" \",\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToFind", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom position_lrtrim", "find the position of atom in an atom list. lrtrim on each atom before.<br>", "atom position_lrtrim \" A, B,A ,A ,C, D   \" \"A \" \",\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("atomToFind", "The atom to search", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		mql = new MQLDocumentation(true, "atom size", "get size atom of an atom list (size >=1)<br>", "atom size \"A,B,C,D\" \",\"", "4", null, null, null, null, false, "");
		mql.addParam(new MQLParam("atomList", "Atom list (example A,B,C)", "String", true));
		mql.addParam(new MQLParam("separator", "List Separator (in this example ','), 1 CHAR MAX", "Number", true));
		functions.get("Atom").add(mql);
		
		functions.put("External REST API", new Vector<MQLDocumentation>());
		page_description.put("External REST API", "<img src='images/p.png' style='vertical-align: middle;'>You can call remote web services from MentDB."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>And trigger your REST web services in process executions."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>HTTP or HTTPS.");
		mql = new MQLDocumentation(true, "rest http", "To execute a REST request through HTTP protocol", "rest http get \"http://services.groupkt.com/country/get/all\" \"\" \"{}\" \"[]\";", "{  \"RestResponse\" : {    \"messages\" : [ \"More webservices are available at http://www.groupkt.com/post/f2129b88/services.htm\", \"Total [249] records found.\" ],    \"result\" : [ {      \"name\" : \"Afghanistan\",      \"alpha2_code\" : \"AF\",      \"alpha3_code\" : \"AFG\"    }, {      \"name\" : \"Åland Islands\",      \"alpha2_code\" : \"AX\",      \"alpha3_code\" : \"ALA\"    }, {      \"name\" : \"Albania\",      \"alpha2_code\" : \"AL\",      \"alpha3_code\" : \"ALB\"    }, {      \"name\" : \"Algeria\",      \"alpha2_code\" : \"DZ\",      \"alpha3_code\" : \"DZA\"    }, {      \"name\" : \"American Samoa\",      \"alpha2_code\" : \"AS\",      \"alpha3_code\" : \"ASM\"    }, {      \"name\" : \"Andorra\",      \"alpha2_code\" : \"AD\",      \"alpha3_code\" : \"AND\"    }, {      \"name\" : \"Angola\",      \"alpha2_code\" : \"AO\",      \"alpha3_code\" : \"AGO\"    }, {      \"name\" : \"Anguilla\",      \"alpha2_code\" : \"AI\",      \"alpha3_code\" : \"AIA\"    }, {      \"name\" : \"Antarctica\",      \"alpha2_code\" : \"AQ\",      \"alpha3_code\" : \"ATA\"    }, {      \"name\" : \"Antigua and Barbuda\",      \"alpha2_code\" : \"AG\",      \"alpha3_code\" : \"ATG\"    }, {      \"name\" : \"Argentina\",      \"alpha2_code\" : \"AR\",      \"alpha3_code\" : \"ARG\"    }, {      \"name\" : \"Armenia\",      \"alpha2_code\" : \"AM\",      \"alpha3_code\" : \"ARM\"    }, {      \"name\" : \"Aruba\",      \"alpha2_code\" : \"AW\",      \"alpha3_code\" : \"ABW\"    }, {      \"name\" : \"Australia\",      \"alpha2_code\" : \"AU\",      \"alpha3_code\" : \"AUS\"    }, {      \"name\" : \"Austria\",      \"alpha2_code\" : \"AT\",      \"alpha3_code\" : \"AUT\"    }, {      \"name\" : \"Azerbaijan\",      \"alpha2_code\" : \"AZ\",      \"alpha3_code\" : \"AZE\"    }, {      \"name\" : \"Bahamas\",      \"alpha2_code\" : \"BS\",      \"alpha3_code\" : \"BHS\"    }, {      \"name\" : \"Bahrain\",      \"alpha2_code\" : \"BH\",      \"alpha3_code\" : \"BHR\"    }, {      \"name\" : \"Bangladesh\",      \"alpha2_code\" : \"BD\",      \"alpha3_code\" : \"BGD\"    }, {      \"name\" : \"Barbados\",      \"alpha2_code\" : \"BB\",      \"alpha3_code\" : \"BRB\"    }, {      \"name\" : \"Belarus\",      \"alpha2_code\" : \"BY\",      \"alpha3_code\" : \"BLR\"    }, {      \"name\" : \"Belgium\",      \"alpha2_code\" : \"BE\",      \"alpha3_code\" : \"BEL\"    }, {      \"name\" : \"Belize\",      \"alpha2_code\" : \"BZ\",      \"alpha3_code\" : \"BLZ\"    }, {      \"name\" : \"Benin\",      \"alpha2_code\" : \"BJ\",      \"alpha3_code\" : \"BEN\"    }, {      \"name\" : \"Bermuda\",      \"alpha2_code\" : \"BM\",      \"alpha3_code\" : \"BMU\"    }, {      \"name\" : \"Bhutan\",      \"alpha2_code\" : \"BT\",      \"alpha3_code\" : \"BTN\"    }, {      \"name\" : \"Bolivia (Plurinational State of)\",      \"alpha2_code\" : \"BO\",      \"alpha3_code\" : \"BOL\"    }, {      \"name\" : \"Bonaire, Sint Eustatius and Saba\",      \"alpha2_code\" : \"BQ\",      \"alpha3_code\" : \"BES\"    }, {      \"name\" : \"Bosnia and Herzegovina\",      \"alpha2_code\" : \"BA\",      \"alpha3_code\" : \"BIH\"    }, {      \"name\" : \"Botswana\",      \"alpha2_code\" : \"BW\",      \"alpha3_code\" : \"BWA\"    }, {      \"name\" : \"Bouvet Island\",      \"alpha2_code\" : \"BV\",      \"alpha3_code\" : \"BVT\"    }, {      \"name\" : \"Brazil\",      \"alpha2_code\" : \"BR\",      \"alpha3_code\" : \"BRA\"    }, {      \"name\" : \"British Indian Ocean Territory\",      \"alpha2_code\" : \"IO\",      \"alpha3_code\" : \"IOT\"    }, {      \"name\" : \"Brunei Darussalam\",      \"alpha2_code\" : \"BN\",      \"alpha3_code\" : \"BRN\"    }, {      \"name\" : \"Bulgaria\",      \"alpha2_code\" : \"BG\",      \"alpha3_code\" : \"BGR\"    }, {      \"name\" : \"Burkina Faso\",      \"alpha2_code\" : \"BF\",      \"alpha3_code\" : \"BFA\"    }, {      \"name\" : \"Burundi\",      \"alpha2_code\" : \"BI\",      \"alpha3_code\" : \"BDI\"    }, {      \"name\" : \"Cambodia\",      \"alpha2_code\" : \"KH\",      \"alpha3_code\" : \"KHM\"    }, {      \"name\" : \"Cameroon\",      \"alpha2_code\" : \"CM\",      \"alpha3_code\" : \"CMR\"    }, {      \"name\" : \"Canada\",      \"alpha2_code\" : \"CA\",      \"alpha3_code\" : \"CAN\"    }, {      \"name\" : \"Cabo Verde\",      \"alpha2_code\" : \"CV\",      \"alpha3_code\" : \"CPV\"    }, {      \"name\" : \"Cayman Islands\",      \"alpha2_code\" : \"KY\",      \"alpha3_code\" : \"CYM\"    }, {      \"name\" : \"Central African Republic\",      \"alpha2_code\" : \"CF\",      \"alpha3_code\" : \"CAF\"    }, {      \"name\" : \"Chad\",      \"alpha2_code\" : \"TD\",      \"alpha3_code\" : \"TCD\"    }, {      \"name\" : \"Chile\",      \"alpha2_code\" : \"CL\",      \"alpha3_code\" : \"CHL\"    }, {      \"name\" : \"China\",      \"alpha2_code\" : \"CN\",      \"alpha3_code\" : \"CHN\"    }, {      \"name\" : \"Christmas Island\",      \"alpha2_code\" : \"CX\",      \"alpha3_code\" : \"CXR\"    }, {      \"name\" : \"Cocos (Keeling) Islands\",      \"alpha2_code\" : \"CC\",      \"alpha3_code\" : \"CCK\"    }, {      \"name\" : \"Colombia\",      \"alpha2_code\" : \"CO\",      \"alpha3_code\" : \"COL\"    }, {      \"name\" : \"Comoros\",      \"alpha2_code\" : \"KM\",      \"alpha3_code\" : \"COM\"    }, {      \"name\" : \"Congo\",      \"alpha2_code\" : \"CG\",      \"alpha3_code\" : \"COG\"    }, {      \"name\" : \"Congo (Democratic Republic of the)\",      \"alpha2_code\" : \"CD\",      \"alpha3_code\" : \"COD\"    }, {      \"name\" : \"Cook Islands\",      \"alpha2_code\" : \"CK\",      \"alpha3_code\" : \"COK\"    }, {      \"name\" : \"Costa Rica\",      \"alpha2_code\" : \"CR\",      \"alpha3_code\" : \"CRI\"    }, {      \"name\" : \"Côte d'Ivoire\",      \"alpha2_code\" : \"CI\",      \"alpha3_code\" : \"CIV\"    }, {      \"name\" : \"Croatia\",      \"alpha2_code\" : \"HR\",      \"alpha3_code\" : \"HRV\"    }, {      \"name\" : \"Cuba\",      \"alpha2_code\" : \"CU\",      \"alpha3_code\" : \"CUB\"    }, {      \"name\" : \"Curaçao\",      \"alpha2_code\" : \"CW\",      \"alpha3_code\" : \"CUW\"    }, {      \"name\" : \"Cyprus\",      \"alpha2_code\" : \"CY\",      \"alpha3_code\" : \"CYP\"    }, {      \"name\" : \"Czech Republic\",      \"alpha2_code\" : \"CZ\",      \"alpha3_code\" : \"CZE\"    }, {      \"name\" : \"Denmark\",      \"alpha2_code\" : \"DK\",      \"alpha3_code\" : \"DNK\"    }, {      \"name\" : \"Djibouti\",      \"alpha2_code\" : \"DJ\",      \"alpha3_code\" : \"DJI\"    }, {      \"name\" : \"Dominica\",      \"alpha2_code\" : \"DM\",      \"alpha3_code\" : \"DMA\"    }, {      \"name\" : \"Dominican Republic\",      \"alpha2_code\" : \"DO\",      \"alpha3_code\" : \"DOM\"    }, {      \"name\" : \"Ecuador\",      \"alpha2_code\" : \"EC\",      \"alpha3_code\" : \"ECU\"    }, {      \"name\" : \"Egypt\",      \"alpha2_code\" : \"EG\",      \"alpha3_code\" : \"EGY\"    }, {      \"name\" : \"El Salvador\",      \"alpha2_code\" : \"SV\",      \"alpha3_code\" : \"SLV\"    }, {      \"name\" : \"Equatorial Guinea\",      \"alpha2_code\" : \"GQ\",      \"alpha3_code\" : \"GNQ\"    }, {      \"name\" : \"Eritrea\",      \"alpha2_code\" : \"ER\",      \"alpha3_code\" : \"ERI\"    }, {      \"name\" : \"Estonia\",      \"alpha2_code\" : \"EE\",      \"alpha3_code\" : \"EST\"    }, {      \"name\" : \"Ethiopia\",      \"alpha2_code\" : \"ET\",      \"alpha3_code\" : \"ETH\"    }, {      \"name\" : \"Falkland Islands (Malvinas)\",      \"alpha2_code\" : \"FK\",      \"alpha3_code\" : \"FLK\"    }, {      \"name\" : \"Faroe Islands\",      \"alpha2_code\" : \"FO\",      \"alpha3_code\" : \"FRO\"    }, {      \"name\" : \"Fiji\",      \"alpha2_code\" : \"FJ\",      \"alpha3_code\" : \"FJI\"    }, {      \"name\" : \"Finland\",      \"alpha2_code\" : \"FI\",      \"alpha3_code\" : \"FIN\"    }, {      \"name\" : \"France\",      \"alpha2_code\" : \"FR\",      \"alpha3_code\" : \"FRA\"    }, {      \"name\" : \"French Guiana\",      \"alpha2_code\" : \"GF\",      \"alpha3_code\" : \"GUF\"    }, {      \"name\" : \"French Polynesia\",      \"alpha2_code\" : \"PF\",      \"alpha3_code\" : \"PYF\"    }, {      \"name\" : \"French Southern Territories\",      \"alpha2_code\" : \"TF\",      \"alpha3_code\" : \"ATF\"    }, {      \"name\" : \"Gabon\",      \"alpha2_code\" : \"GA\",      \"alpha3_code\" : \"GAB\"    }, {      \"name\" : \"Gambia\",      \"alpha2_code\" : \"GM\",      \"alpha3_code\" : \"GMB\"    }, {      \"name\" : \"Georgia\",      \"alpha2_code\" : \"GE\",      \"alpha3_code\" : \"GEO\"    }, {      \"name\" : \"Germany\",      \"alpha2_code\" : \"DE\",      \"alpha3_code\" : \"DEU\"    }, {      \"name\" : \"Ghana\",      \"alpha2_code\" : \"GH\",      \"alpha3_code\" : \"GHA\"    }, {      \"name\" : \"Gibraltar\",      \"alpha2_code\" : \"GI\",      \"alpha3_code\" : \"GIB\"    }, {      \"name\" : \"Greece\",      \"alpha2_code\" : \"GR\",      \"alpha3_code\" : \"GRC\"    }, {      \"name\" : \"Greenland\",      \"alpha2_code\" : \"GL\",      \"alpha3_code\" : \"GRL\"    }, {      \"name\" : \"Grenada\",      \"alpha2_code\" : \"GD\",      \"alpha3_code\" : \"GRD\"    }, {      \"name\" : \"Guadeloupe\",      \"alpha2_code\" : \"GP\",      \"alpha3_code\" : \"GLP\"    }, {      \"name\" : \"Guam\",      \"alpha2_code\" : \"GU\",      \"alpha3_code\" : \"GUM\"    }, {      \"name\" : \"Guatemala\",      \"alpha2_code\" : \"GT\",      \"alpha3_code\" : \"GTM\"    }, {      \"name\" : \"Guernsey\",      \"alpha2_code\" : \"GG\",      \"alpha3_code\" : \"GGY\"    }, {      \"name\" : \"Guinea\",      \"alpha2_code\" : \"GN\",      \"alpha3_code\" : \"GIN\"    }, {      \"name\" : \"Guinea-Bissau\",      \"alpha2_code\" : \"GW\",      \"alpha3_code\" : \"GNB\"    }, {      \"name\" : \"Guyana\",      \"alpha2_code\" : \"GY\",      \"alpha3_code\" : \"GUY\"    }, {      \"name\" : \"Haiti\",      \"alpha2_code\" : \"HT\",      \"alpha3_code\" : \"HTI\"    }, {      \"name\" : \"Heard Island and McDonald Islands\",      \"alpha2_code\" : \"HM\",      \"alpha3_code\" : \"HMD\"    }, {      \"name\" : \"Holy See\",      \"alpha2_code\" : \"VA\",      \"alpha3_code\" : \"VAT\"    }, {      \"name\" : \"Honduras\",      \"alpha2_code\" : \"HN\",      \"alpha3_code\" : \"HND\"    }, {      \"name\" : \"Hong Kong\",      \"alpha2_code\" : \"HK\",      \"alpha3_code\" : \"HKG\"    }, {      \"name\" : \"Hungary\",      \"alpha2_code\" : \"HU\",      \"alpha3_code\" : \"HUN\"    }, {      \"name\" : \"Iceland\",      \"alpha2_code\" : \"IS\",      \"alpha3_code\" : \"ISL\"    }, {      \"name\" : \"India\",      \"alpha2_code\" : \"IN\",      \"alpha3_code\" : \"IND\"    }, {      \"name\" : \"Indonesia\",      \"alpha2_code\" : \"ID\",      \"alpha3_code\" : \"IDN\"    }, {      \"name\" : \"Iran (Islamic Republic of)\",      \"alpha2_code\" : \"IR\",      \"alpha3_code\" : \"IRN\"    }, {      \"name\" : \"Iraq\",      \"alpha2_code\" : \"IQ\",      \"alpha3_code\" : \"IRQ\"    }, {      \"name\" : \"Ireland\",      \"alpha2_code\" : \"IE\",      \"alpha3_code\" : \"IRL\"    }, {      \"name\" : \"Isle of Man\",      \"alpha2_code\" : \"IM\",      \"alpha3_code\" : \"IMN\"    }, {      \"name\" : \"Israel\",      \"alpha2_code\" : \"IL\",      \"alpha3_code\" : \"ISR\"    }, {      \"name\" : \"Italy\",      \"alpha2_code\" : \"IT\",      \"alpha3_code\" : \"ITA\"    }, {      \"name\" : \"Jamaica\",      \"alpha2_code\" : \"JM\",      \"alpha3_code\" : \"JAM\"    }, {      \"name\" : \"Japan\",      \"alpha2_code\" : \"JP\",      \"alpha3_code\" : \"JPN\"    }, {      \"name\" : \"Jersey\",      \"alpha2_code\" : \"JE\",      \"alpha3_code\" : \"JEY\"    }, {      \"name\" : \"Jordan\",      \"alpha2_code\" : \"JO\",      \"alpha3_code\" : \"JOR\"    }, {      \"name\" : \"Kazakhstan\",      \"alpha2_code\" : \"KZ\",      \"alpha3_code\" : \"KAZ\"    }, {      \"name\" : \"Kenya\",      \"alpha2_code\" : \"KE\",      \"alpha3_code\" : \"KEN\"    }, {      \"name\" : \"Kiribati\",      \"alpha2_code\" : \"KI\",      \"alpha3_code\" : \"KIR\"    }, {      \"name\" : \"Korea (Democratic People's Republic of)\",      \"alpha2_code\" : \"KP\",      \"alpha3_code\" : \"PRK\"    }, {      \"name\" : \"Korea (Republic of)\",      \"alpha2_code\" : \"KR\",      \"alpha3_code\" : \"KOR\"    }, {      \"name\" : \"Kuwait\",      \"alpha2_code\" : \"KW\",      \"alpha3_code\" : \"KWT\"    }, {      \"name\" : \"Kyrgyzstan\",      \"alpha2_code\" : \"KG\",      \"alpha3_code\" : \"KGZ\"    }, {      \"name\" : \"Lao People's Democratic Republic\",      \"alpha2_code\" : \"LA\",      \"alpha3_code\" : \"LAO\"    }, {      \"name\" : \"Latvia\",      \"alpha2_code\" : \"LV\",      \"alpha3_code\" : \"LVA\"    }, {      \"name\" : \"Lebanon\",      \"alpha2_code\" : \"LB\",      \"alpha3_code\" : \"LBN\"    }, {      \"name\" : \"Lesotho\",      \"alpha2_code\" : \"LS\",      \"alpha3_code\" : \"LSO\"    }, {      \"name\" : \"Liberia\",      \"alpha2_code\" : \"LR\",      \"alpha3_code\" : \"LBR\"    }, {      \"name\" : \"Libya\",      \"alpha2_code\" : \"LY\",      \"alpha3_code\" : \"LBY\"    }, {      \"name\" : \"Liechtenstein\",      \"alpha2_code\" : \"LI\",      \"alpha3_code\" : \"LIE\"    }, {      \"name\" : \"Lithuania\",      \"alpha2_code\" : \"LT\",      \"alpha3_code\" : \"LTU\"    }, {      \"name\" : \"Luxembourg\",      \"alpha2_code\" : \"LU\",      \"alpha3_code\" : \"LUX\"    }, {      \"name\" : \"Macao\",      \"alpha2_code\" : \"MO\",      \"alpha3_code\" : \"MAC\"    }, {      \"name\" : \"Macedonia (the former Yugoslav Republic of)\",      \"alpha2_code\" : \"MK\",      \"alpha3_code\" : \"MKD\"    }, {      \"name\" : \"Madagascar\",      \"alpha2_code\" : \"MG\",      \"alpha3_code\" : \"MDG\"    }, {      \"name\" : \"Malawi\",      \"alpha2_code\" : \"MW\",      \"alpha3_code\" : \"MWI\"    }, {      \"name\" : \"Malaysia\",      \"alpha2_code\" : \"MY\",      \"alpha3_code\" : \"MYS\"    }, {      \"name\" : \"Maldives\",      \"alpha2_code\" : \"MV\",      \"alpha3_code\" : \"MDV\"    }, {      \"name\" : \"Mali\",      \"alpha2_code\" : \"ML\",      \"alpha3_code\" : \"MLI\"    }, {      \"name\" : \"Malta\",      \"alpha2_code\" : \"MT\",      \"alpha3_code\" : \"MLT\"    }, {      \"name\" : \"Marshall Islands\",      \"alpha2_code\" : \"MH\",      \"alpha3_code\" : \"MHL\"    }, {      \"name\" : \"Martinique\",      \"alpha2_code\" : \"MQ\",      \"alpha3_code\" : \"MTQ\"    }, {      \"name\" : \"Mauritania\",      \"alpha2_code\" : \"MR\",      \"alpha3_code\" : \"MRT\"    }, {      \"name\" : \"Mauritius\",      \"alpha2_code\" : \"MU\",      \"alpha3_code\" : \"MUS\"    }, {      \"name\" : \"Mayotte\",      \"alpha2_code\" : \"YT\",      \"alpha3_code\" : \"MYT\"    }, {      \"name\" : \"Mexico\",      \"alpha2_code\" : \"MX\",      \"alpha3_code\" : \"MEX\"    }, {      \"name\" : \"Micronesia (Federated States of)\",      \"alpha2_code\" : \"FM\",      \"alpha3_code\" : \"FSM\"    }, {      \"name\" : \"Moldova (Republic of)\",      \"alpha2_code\" : \"MD\",      \"alpha3_code\" : \"MDA\"    }, {      \"name\" : \"Monaco\",      \"alpha2_code\" : \"MC\",      \"alpha3_code\" : \"MCO\"    }, {      \"name\" : \"Mongolia\",      \"alpha2_code\" : \"MN\",      \"alpha3_code\" : \"MNG\"    }, {      \"name\" : \"Montenegro\",      \"alpha2_code\" : \"ME\",      \"alpha3_code\" : \"MNE\"    }, {      \"name\" : \"Montserrat\",      \"alpha2_code\" : \"MS\",      \"alpha3_code\" : \"MSR\"    }, {      \"name\" : \"Morocco\",      \"alpha2_code\" : \"MA\",      \"alpha3_code\" : \"MAR\"    }, {      \"name\" : \"Mozambique\",      \"alpha2_code\" : \"MZ\",      \"alpha3_code\" : \"MOZ\"    }, {      \"name\" : \"Myanmar\",      \"alpha2_code\" : \"MM\",      \"alpha3_code\" : \"MMR\"    }, {      \"name\" : \"Namibia\",      \"alpha2_code\" : \"NA\",      \"alpha3_code\" : \"NAM\"    }, {      \"name\" : \"Nauru\",      \"alpha2_code\" : \"NR\",      \"alpha3_code\" : \"NRU\"    }, {      \"name\" : \"Nepal\",      \"alpha2_code\" : \"NP\",      \"alpha3_code\" : \"NPL\"    }, {      \"name\" : \"Netherlands\",      \"alpha2_code\" : \"NL\",      \"alpha3_code\" : \"NLD\"    }, {      \"name\" : \"New Caledonia\",      \"alpha2_code\" : \"NC\",      \"alpha3_code\" : \"NCL\"    }, {      \"name\" : \"New Zealand\",      \"alpha2_code\" : \"NZ\",      \"alpha3_code\" : \"NZL\"    }, {      \"name\" : \"Nicaragua\",      \"alpha2_code\" : \"NI\",      \"alpha3_code\" : \"NIC\"    }, {      \"name\" : \"Niger\",      \"alpha2_code\" : \"NE\",      \"alpha3_code\" : \"NER\"    }, {      \"name\" : \"Nigeria\",      \"alpha2_code\" : \"NG\",      \"alpha3_code\" : \"NGA\"    }, {      \"name\" : \"Niue\",      \"alpha2_code\" : \"NU\",      \"alpha3_code\" : \"NIU\"    }, {      \"name\" : \"Norfolk Island\",      \"alpha2_code\" : \"NF\",      \"alpha3_code\" : \"NFK\"    }, {      \"name\" : \"Northern Mariana Islands\",      \"alpha2_code\" : \"MP\",      \"alpha3_code\" : \"MNP\"    }, {      \"name\" : \"Norway\",      \"alpha2_code\" : \"NO\",      \"alpha3_code\" : \"NOR\"    }, {      \"name\" : \"Oman\",      \"alpha2_code\" : \"OM\",      \"alpha3_code\" : \"OMN\"    }, {      \"name\" : \"Pakistan\",      \"alpha2_code\" : \"PK\",      \"alpha3_code\" : \"PAK\"    }, {      \"name\" : \"Palau\",      \"alpha2_code\" : \"PW\",      \"alpha3_code\" : \"PLW\"    }, {      \"name\" : \"Palestine, State of\",      \"alpha2_code\" : \"PS\",      \"alpha3_code\" : \"PSE\"    }, {      \"name\" : \"Panama\",      \"alpha2_code\" : \"PA\",      \"alpha3_code\" : \"PAN\"    }, {      \"name\" : \"Papua New Guinea\",      \"alpha2_code\" : \"PG\",      \"alpha3_code\" : \"PNG\"    }, {      \"name\" : \"Paraguay\",      \"alpha2_code\" : \"PY\",      \"alpha3_code\" : \"PRY\"    }, {      \"name\" : \"Peru\",      \"alpha2_code\" : \"PE\",      \"alpha3_code\" : \"PER\"    }, {      \"name\" : \"Philippines\",      \"alpha2_code\" : \"PH\",      \"alpha3_code\" : \"PHL\"    }, {      \"name\" : \"Pitcairn\",      \"alpha2_code\" : \"PN\",      \"alpha3_code\" : \"PCN\"    }, {      \"name\" : \"Poland\",      \"alpha2_code\" : \"PL\",      \"alpha3_code\" : \"POL\"    }, {      \"name\" : \"Portugal\",      \"alpha2_code\" : \"PT\",      \"alpha3_code\" : \"PRT\"    }, {      \"name\" : \"Puerto Rico\",      \"alpha2_code\" : \"PR\",      \"alpha3_code\" : \"PRI\"    }, {      \"name\" : \"Qatar\",      \"alpha2_code\" : \"QA\",      \"alpha3_code\" : \"QAT\"    }, {      \"name\" : \"Réunion\",      \"alpha2_code\" : \"RE\",      \"alpha3_code\" : \"REU\"    }, {      \"name\" : \"Romania\",      \"alpha2_code\" : \"RO\",      \"alpha3_code\" : \"ROU\"    }, {      \"name\" : \"Russian Federation\",      \"alpha2_code\" : \"RU\",      \"alpha3_code\" : \"RUS\"    }, {      \"name\" : \"Rwanda\",      \"alpha2_code\" : \"RW\",      \"alpha3_code\" : \"RWA\"    }, {      \"name\" : \"Saint Barthélemy\",      \"alpha2_code\" : \"BL\",      \"alpha3_code\" : \"BLM\"    }, {      \"name\" : \"Saint Helena, Ascension and Tristan da Cunha\",      \"alpha2_code\" : \"SH\",      \"alpha3_code\" : \"SHN\"    }, {      \"name\" : \"Saint Kitts and Nevis\",      \"alpha2_code\" : \"KN\",      \"alpha3_code\" : \"KNA\"    }, {      \"name\" : \"Saint Lucia\",      \"alpha2_code\" : \"LC\",      \"alpha3_code\" : \"LCA\"    }, {      \"name\" : \"Saint Martin (French part)\",      \"alpha2_code\" : \"MF\",      \"alpha3_code\" : \"MAF\"    }, {      \"name\" : \"Saint Pierre and Miquelon\",      \"alpha2_code\" : \"PM\",      \"alpha3_code\" : \"SPM\"    }, {      \"name\" : \"Saint Vincent and the Grenadines\",      \"alpha2_code\" : \"VC\",      \"alpha3_code\" : \"VCT\"    }, {      \"name\" : \"Samoa\",      \"alpha2_code\" : \"WS\",      \"alpha3_code\" : \"WSM\"    }, {      \"name\" : \"San Marino\",      \"alpha2_code\" : \"SM\",      \"alpha3_code\" : \"SMR\"    }, {      \"name\" : \"Sao Tome and Principe\",      \"alpha2_code\" : \"ST\",      \"alpha3_code\" : \"STP\"    }, {      \"name\" : \"Saudi Arabia\",      \"alpha2_code\" : \"SA\",      \"alpha3_code\" : \"SAU\"    }, {      \"name\" : \"Senegal\",      \"alpha2_code\" : \"SN\",      \"alpha3_code\" : \"SEN\"    }, {      \"name\" : \"Serbia\",      \"alpha2_code\" : \"RS\",      \"alpha3_code\" : \"SRB\"    }, {      \"name\" : \"Seychelles\",      \"alpha2_code\" : \"SC\",      \"alpha3_code\" : \"SYC\"    }, {      \"name\" : \"Sierra Leone\",      \"alpha2_code\" : \"SL\",      \"alpha3_code\" : \"SLE\"    }, {      \"name\" : \"Singapore\",      \"alpha2_code\" : \"SG\",      \"alpha3_code\" : \"SGP\"    }, {      \"name\" : \"Sint Maarten (Dutch part)\",      \"alpha2_code\" : \"SX\",      \"alpha3_code\" : \"SXM\"    }, {      \"name\" : \"Slovakia\",      \"alpha2_code\" : \"SK\",      \"alpha3_code\" : \"SVK\"    }, {      \"name\" : \"Slovenia\",      \"alpha2_code\" : \"SI\",      \"alpha3_code\" : \"SVN\"    }, {      \"name\" : \"Solomon Islands\",      \"alpha2_code\" : \"SB\",      \"alpha3_code\" : \"SLB\"    }, {      \"name\" : \"Somalia\",      \"alpha2_code\" : \"SO\",      \"alpha3_code\" : \"SOM\"    }, {      \"name\" : \"South Africa\",      \"alpha2_code\" : \"ZA\",      \"alpha3_code\" : \"ZAF\"    }, {      \"name\" : \"South Georgia and the South Sandwich Islands\",      \"alpha2_code\" : \"GS\",      \"alpha3_code\" : \"SGS\"    }, {      \"name\" : \"South Sudan\",      \"alpha2_code\" : \"SS\",      \"alpha3_code\" : \"SSD\"    }, {      \"name\" : \"Spain\",      \"alpha2_code\" : \"ES\",      \"alpha3_code\" : \"ESP\"    }, {      \"name\" : \"Sri Lanka\",      \"alpha2_code\" : \"LK\",      \"alpha3_code\" : \"LKA\"    }, {      \"name\" : \"Sudan\",      \"alpha2_code\" : \"SD\",      \"alpha3_code\" : \"SDN\"    }, {      \"name\" : \"Suriname\",      \"alpha2_code\" : \"SR\",      \"alpha3_code\" : \"SUR\"    }, {      \"name\" : \"Svalbard and Jan Mayen\",      \"alpha2_code\" : \"SJ\",      \"alpha3_code\" : \"SJM\"    }, {      \"name\" : \"Swaziland\",      \"alpha2_code\" : \"SZ\",      \"alpha3_code\" : \"SWZ\"    }, {      \"name\" : \"Sweden\",      \"alpha2_code\" : \"SE\",      \"alpha3_code\" : \"SWE\"    }, {      \"name\" : \"Switzerland\",      \"alpha2_code\" : \"CH\",      \"alpha3_code\" : \"CHE\"    }, {      \"name\" : \"Syrian Arab Republic\",      \"alpha2_code\" : \"SY\",      \"alpha3_code\" : \"SYR\"    }, {      \"name\" : \"Taiwan, Province of China\",      \"alpha2_code\" : \"TW\",      \"alpha3_code\" : \"TWN\"    }, {      \"name\" : \"Tajikistan\",      \"alpha2_code\" : \"TJ\",      \"alpha3_code\" : \"TJK\"    }, {      \"name\" : \"Tanzania, United Republic of\",      \"alpha2_code\" : \"TZ\",      \"alpha3_code\" : \"TZA\"    }, {      \"name\" : \"Thailand\",      \"alpha2_code\" : \"TH\",      \"alpha3_code\" : \"THA\"    }, {      \"name\" : \"Timor-Leste\",      \"alpha2_code\" : \"TL\",      \"alpha3_code\" : \"TLS\"    }, {      \"name\" : \"Togo\",      \"alpha2_code\" : \"TG\",      \"alpha3_code\" : \"TGO\"    }, {      \"name\" : \"Tokelau\",      \"alpha2_code\" : \"TK\",      \"alpha3_code\" : \"TKL\"    }, {      \"name\" : \"Tonga\",      \"alpha2_code\" : \"TO\",      \"alpha3_code\" : \"TON\"    }, {      \"name\" : \"Trinidad and Tobago\",      \"alpha2_code\" : \"TT\",      \"alpha3_code\" : \"TTO\"    }, {      \"name\" : \"Tunisia\",      \"alpha2_code\" : \"TN\",      \"alpha3_code\" : \"TUN\"    }, {      \"name\" : \"Turkey\",      \"alpha2_code\" : \"TR\",      \"alpha3_code\" : \"TUR\"    }, {      \"name\" : \"Turkmenistan\",      \"alpha2_code\" : \"TM\",      \"alpha3_code\" : \"TKM\"    }, {      \"name\" : \"Turks and Caicos Islands\",      \"alpha2_code\" : \"TC\",      \"alpha3_code\" : \"TCA\"    }, {      \"name\" : \"Tuvalu\",      \"alpha2_code\" : \"TV\",      \"alpha3_code\" : \"TUV\"    }, {      \"name\" : \"Uganda\",      \"alpha2_code\" : \"UG\",      \"alpha3_code\" : \"UGA\"    }, {      \"name\" : \"Ukraine\",      \"alpha2_code\" : \"UA\",      \"alpha3_code\" : \"UKR\"    }, {      \"name\" : \"United Arab Emirates\",      \"alpha2_code\" : \"AE\",      \"alpha3_code\" : \"ARE\"    }, {      \"name\" : \"United Kingdom of Great Britain and Northern Ireland\",      \"alpha2_code\" : \"GB\",      \"alpha3_code\" : \"GBR\"    }, {      \"name\" : \"United States of America\",      \"alpha2_code\" : \"US\",      \"alpha3_code\" : \"USA\"    }, {      \"name\" : \"United States Minor Outlying Islands\",      \"alpha2_code\" : \"UM\",      \"alpha3_code\" : \"UMI\"    }, {      \"name\" : \"Uruguay\",      \"alpha2_code\" : \"UY\",      \"alpha3_code\" : \"URY\"    }, {      \"name\" : \"Uzbekistan\",      \"alpha2_code\" : \"UZ\",      \"alpha3_code\" : \"UZB\"    }, {      \"name\" : \"Vanuatu\",      \"alpha2_code\" : \"VU\",      \"alpha3_code\" : \"VUT\"    }, {      \"name\" : \"Venezuela (Bolivarian Republic of)\",      \"alpha2_code\" : \"VE\",      \"alpha3_code\" : \"VEN\"    }, {      \"name\" : \"Viet Nam\",      \"alpha2_code\" : \"VN\",      \"alpha3_code\" : \"VNM\"    }, {      \"name\" : \"Virgin Islands (British)\",      \"alpha2_code\" : \"VG\",      \"alpha3_code\" : \"VGB\"    }, {      \"name\" : \"Virgin Islands (U.S.)\",      \"alpha2_code\" : \"VI\",      \"alpha3_code\" : \"VIR\"    }, {      \"name\" : \"Wallis and Futuna\",      \"alpha2_code\" : \"WF\",      \"alpha3_code\" : \"WLF\"    }, {      \"name\" : \"Western Sahara\",      \"alpha2_code\" : \"EH\",      \"alpha3_code\" : \"ESH\"    }, {      \"name\" : \"Yemen\",      \"alpha2_code\" : \"YE\",      \"alpha3_code\" : \"YEM\"    }, {      \"name\" : \"Zambia\",      \"alpha2_code\" : \"ZM\",      \"alpha3_code\" : \"ZMB\"    }, {      \"name\" : \"Zimbabwe\",      \"alpha2_code\" : \"ZW\",      \"alpha3_code\" : \"ZWE\"    } ]  }}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("method", "The method (get|post|put|delete)", "string", true));
		mql.addParam(new MQLParam("beginUrl", "The begin url", "string", true));
		mql.addParam(new MQLParam("endUrl", "The end url", "string", true));
		mql.addParam(new MQLParam("jsonHeaders", "The json headers", "string", true));
		mql.addParam(new MQLParam("jsonCookies", "The json cookies", "string", true));
		functions.get("External REST API").add(mql);
		mql = new MQLDocumentation(true, "rest http_json_post", "To execute a REST request through HTTP protocol", "rest http_json_post \"http://services.groupkt.com/country/get/all\" \"{}\" \"[]\" \"{}\";", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("jsonHeaders", "The json headers", "string", true));
		mql.addParam(new MQLParam("jsonCookies", "The json cookies", "string", true));
		mql.addParam(new MQLParam("jsonData", "The json data", "string", true));
		functions.get("External REST API").add(mql);
		mql = new MQLDocumentation(true, "rest https", "To execute a REST request through HTTPS protocol", "#REST / example 1;\n" + 
				"json load \"header\" \"{}\";\n" + 
				"json iobject \"header\" / \"x-user\" \"admin\" STR;\n" + 
				"json iobject \"header\" / \"x-password\" \"pwd\" STR;\n" + 
				"rest https get \"https://localhost:9999/api/addition\" \"v1=1&v2=6\" (json doc \"header\") \"[]\";\n" + 
				"\n" + 
				"#Website Browser / example 2;\n" + 
				"json load \"response_1\" (rest https post \"https://localhost:8083/demo/index.jsp\" \"x-user=system&x-password=pwd\" \"{}\" \"[]\");\n" + 
				"json load \"response_2\" (rest https post \"https://localhost:8083/demo/index.jsp\" \"x-user=system&x-password=pwd\" \"{}\" (json select \"response_1\" \"/cookies\"));\n" + 
				"json doc \"response_2\";", "{\n" + 
						"  \"reponse\": \"\\u003c!doctype html\\u003e\\t\\t\\u003chtml lang\\u003d\\\"fr\\\" style\\u003d\\u0027height:100%\\u0027\\u003e\\t\\t\\u003chead\\u003e\\t\\t  \\u003cmeta charset\\u003d\\\"utf-8\\\"\\u003e\\t\\t  \\u003ctitle\\u003eDemonstration\\u003c/title\\u003e\\t\\t  \\u003clink href\\u003d\\\"font_awesome/css/font-awesome.min.css\\\" rel\\u003d\\\"stylesheet\\\"\\u003e\\t\\t  \\u003clink href\\u003d\\\"css/menu-vertical.css\\\" rel\\u003d\\\"stylesheet\\\"\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/menu-vertical.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003clink href\\u003d\\\"dist/css/bootstrap.css\\\" rel\\u003d\\\"stylesheet\\\"\\u003e\\t\\t  \\u003clink href\\u003d\\\"css/tempusdominus-bootstrap-4.min.css\\\" rel\\u003d\\\"stylesheet\\\"\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/jquery.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/moment-with-locales.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/tempusdominus-bootstrap-4.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/utils.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/Chart.bundle.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"dist/js/bootstrap.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/jquery.sparkline.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/raphael-2.1.4.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/justgage.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/progressbar.min.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/ckeditor.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003cscript src\\u003d\\\"js/mentdb.js\\\"\\u003e\\u003c/script\\u003e\\t\\t  \\u003clink rel\\u003d\\\"icon\\\" type\\u003d\\\"image/png\\\" href\\u003d\\\"images/bar_icon.png\\\" /\\u003e\\t\\t  \\u003cstyle\\u003e.jqstooltip{    box-sizing: content-box;}.GaugeMeter{\\tPosition:        Relative;\\tText-Align:      Center;\\tOverflow:        Hidden;\\tCursor:          Default;}.GaugeMeter SPAN,    .GaugeMeter B{    \\tMargin:          0 23%;    \\tWidth:           50%;    \\tPosition:        Absolute;    \\tText-align:      Center;    \\tDisplay:         Inline-Block;    \\tColor:           RGBa(0,0,0,.8);    \\tFont-Weight:     100;    \\tFont-Family:     \\\"Open Sans\\\", Arial;    \\tOverflow:        Hidden;    \\tWhite-Space:     NoWrap;    \\tText-Overflow:   Ellipsis;}.GaugeMeter[data-style\\u003d\\\"Semi\\\"] B{\\tMargin:          0 10%;\\tWidth:           80%;}.GaugeMeter S,    .GaugeMeter U{    \\tText-Decoration: None;    \\tFont-Size:       .49em;    \\tOpacity:         .5;}.GaugeMeter B{\\tColor:           Black;\\tFont-Weight:     300;\\tOpacity:         .8;}.progress \\u003e svg {\\twidth: 100%;\\theight: 100%;\\tdisplay: block;}\\t\\t\\t\\t\\t  \\u003c/style\\u003e\\t\\t\\u003c/head\\u003e\\t\\t\\u003cbody id\\u003d\\\"body\\\" style\\u003d\\u0027height:100%;margin:0px;color:#333;font-family: Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;;font-size: 12px;\\u0027\\u003e\\t\\t\\t\\u003cdiv id\\u003d\\u0027mentdb_alert_container\\u0027\\u003e\\u003c/div\\u003e\\u003cdiv id\\u003d\\u0027mentdb_modal_container\\u0027\\u003e\\u003c/div\\u003e\\u003cdiv style\\u003d\\u0027box-shadow: 0px 0px 8px #313131;z-index: 999;position: fixed;width:100%;height:65px;line-height:63px;\\t\\tbackground-color: #313131;\\t\\tcolor: #fff;\\t\\tfont-size: 22px;\\u0027\\u003e\\t\\t\\u003cimg src\\u003d\\u0027images/refresh.gif\\u0027 style\\u003d\\u0027display:none\\u0027\\u003e\\t     \\u003ca href\\u003d\\u0027index.jsp\\u0027\\u003e\\u003cimg id\\u003d\\u0027top_refresh_icon\\u0027 src\\u003d\\u0027images/logow.png\\u0027 \\t\\t\\tstyle\\u003d\\u0027vertical-align:middle;\\t\\t     width:50px;\\t\\t\\tmargin-top:-2px;\\t\\t\\tmargin-left:10px;\\u0027\\u003e\\u003c/a\\u003e \\u0026nbsp; Demonstration\\t\\t\\u003cdiv style\\u003d\\u0027float:right\\u0027\\u003e\\t\\t\\t\\u003cspan class\\u003d\\u0027hidden-xs\\u0027 style\\u003d\\u0027font-size:18px\\u0027\\u003esystem\\u003c/span\\u003e \\u0026nbsp;\\t\\t\\t\\u003ca style\\u003d\\u0027margin-right: 20px;color:#fff\\u0027 href\\u003d\\u0027index.jsp?app_action\\u003dmenu_toggle\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-bars\\u0027 style\\u003d\\u0027vertical-align: middle;\\u0027\\u003e\\u003c/i\\u003e\\u003c/a\\u003e\\t\\t\\t\\u003ca style\\u003d\\u0027margin-right: 20px;color:#fff\\u0027 href\\u003d\\u0027index.jsp?app_action\\u003dlogout\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-power-off\\u0027 style\\u003d\\u0027vertical-align: middle;\\u0027\\u003e\\u003c/i\\u003e\\u003c/a\\u003e\\t\\t\\u003c/div\\u003e\\t\\t\\u003c/div\\u003e\\u003cdiv style\\u003d\\u0027display:table;width:100%;height:100%;padding-top: 65px;\\u0027\\u003e  \\u003cdiv style\\u003d\\u0027display:table-row\\u0027\\u003e    \\u003cdiv style\\u003d\\u0027display:table-cell;width:70px;border-right: 1px solid rgba(0,0,0,.11);vertical-align: top;\\u0027\\u003e\\u003cdiv style\\u003d\\u0027width: 100%;width:100%;height:100%;\\t\\tbackground-color: #fff;\\u0027\\u003e\\t\\t\\u003cul id\\u003d\\\"menu-v\\\"\\u003e\\u003cli style\\u003d\\u0027border-right: 5px #313131 solid;\\u0027\\u003e\\u003cform action\\u003d\\\"index.jsp\\\" id\\u003d\\u0027menu_page_home\\u0027 method\\u003d\\u0027POST\\u0027 style\\u003d\\u0027margin:0px;padding:0px\\u0027\\u003e\\u003cinput type\\u003d\\\"hidden\\\" name\\u003d\\\"app_page\\\"  value\\u003d\\u0027home\\u0027\\u003e\\u003c/form\\u003e\\u003ca href\\u003d\\u0027javascript:document.getElementById(\\\"menu_page_home\\\").submit();\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-home\\u0027 style\\u003d\\u0027color:#313131;font-size: 23px;    vertical-align: middle;    padding-left: 8px;    padding-right: 7px;\\u0027\\u003e\\u003c/i\\u003e \\u0026nbsp;\\u003c/a\\u003e\\u003cul class\\u003d\\\"sub\\\"\\u003e\\u003cli\\u003e\\u003ca id\\u003d\\u0027id\\u0027 href\\u003d\\u0027index.jsp?app_page\\u003dhome\\u0026app_action\\u003d\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-edit\\u0027 style\\u003d\\u0027color:#313131;font-size: 23px;    vertical-align: middle;    padding-left: 8px;    padding-right: 7px;\\u0027\\u003e\\u003c/i\\u003e \\u0026nbsp;\\u003cspan\\u003eTitle\\u003c/span\\u003e\\u003c/a\\u003e\\u003c/li\\u003e\\u003c/ul\\u003e\\u003c/li\\u003e\\u003cli\\u003e\\u003cform action\\u003d\\\"index.jsp\\\" id\\u003d\\u0027menu_page_user\\u0027 method\\u003d\\u0027POST\\u0027 style\\u003d\\u0027margin:0px;padding:0px\\u0027\\u003e\\u003cinput type\\u003d\\\"hidden\\\" name\\u003d\\\"app_page\\\"  value\\u003d\\u0027user\\u0027\\u003e\\u003c/form\\u003e\\u003ca href\\u003d\\u0027javascript:document.getElementById(\\\"menu_page_user\\\").submit();\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-users\\u0027 style\\u003d\\u0027color:#313131;font-size: 23px;    vertical-align: middle;    padding-left: 8px;    padding-right: 7px;\\u0027\\u003e\\u003c/i\\u003e \\u0026nbsp;\\u003c/a\\u003e\\u003c/li\\u003e\\u003cli\\u003e\\u003cform action\\u003d\\\"index.jsp\\\" id\\u003d\\u0027menu_page_help\\u0027 method\\u003d\\u0027POST\\u0027 style\\u003d\\u0027margin:0px;padding:0px\\u0027\\u003e\\u003cinput type\\u003d\\\"hidden\\\" name\\u003d\\\"app_page\\\"  value\\u003d\\u0027help\\u0027\\u003e\\u003c/form\\u003e\\u003ca href\\u003d\\u0027javascript:document.getElementById(\\\"menu_page_help\\\").submit();\\u0027\\u003e\\u003ci class\\u003d\\u0027fa fa-life-ring\\u0027 style\\u003d\\u0027color:#313131;font-size: 23px;    vertical-align: middle;    padding-left: 8px;    padding-right: 7px;\\u0027\\u003e\\u003c/i\\u003e \\u0026nbsp;\\u003c/a\\u003e\\u003c/li\\u003e\\u003c/ul\\u003e\\t\\t\\u003c/div\\u003e    \\u003c/div\\u003e    \\u003cdiv style\\u003d\\u0027display:table-cell;background-color:#f1f2f3;padding: 8px;\\u0027\\u003e    \\u003c/div\\u003e  \\u003c/div\\u003e\\u003c/div\\u003e\\u003cscript\\u003ewindow.onload \\u003d function() {execute_script(\\u0027YXBwLjEwMC50ZW1wbGF0ZS5kZWZhdWx0LnBhZ2UuaG9tZS5vbmxvYWQuZXhl\\u0027, \\u0027ZTMwPQ\\u003d\\u003d\\u0027);};\\u003c/script\\u003e\\u003c/body\\u003e\\u003c/html\\u003e\",\n" + 
						"  \"reponse_header\": [\n" + 
						"    \"Key : null ,Value : [HTTP/1.1 200 OK]\",\n" + 
						"    \"Key : Server ,Value : [Jetty(9.2.2.v20140723)]\",\n" + 
						"    \"Key : Content-Length ,Value : [5135]\",\n" + 
						"    \"Key : Date ,Value : [Sun, 03 Nov 2019 14:43:42 GMT]\",\n" + 
						"    \"Key : Content-Type ,Value : [text/html; charset\\u003dUTF-8]\"\n" + 
						"  ],\n" + 
						"  \"cookies\": []\n" + 
						"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("method", "The method (get|post|put|delete)", "string", true));
		mql.addParam(new MQLParam("beginUrl", "The begin url", "string", true));
		mql.addParam(new MQLParam("endUrl", "The end url", "string", true));
		mql.addParam(new MQLParam("jsonHeaders", "The json headers", "string", true));
		mql.addParam(new MQLParam("jsonCookies", "The json cookies", "string", true));
		functions.get("External REST API").add(mql);
		mql = new MQLDocumentation(true, "rest https_json_post", "To execute a REST request through HTTPS protocol", "rest https_json_post \"https://services.groupkt.com/country/get/all\" \"{}\" \"[]\" \"{}\";", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("jsonHeaders", "The json headers", "string", true));
		mql.addParam(new MQLParam("jsonCookies", "The json cookies", "string", true));
		mql.addParam(new MQLParam("jsonData", "The json data", "string", true));
		functions.get("External REST API").add(mql);
		
		functions.put("External SOAP API", new Vector<MQLDocumentation>());
		page_description.put("External SOAP API", "<img src='images/p.png' style='vertical-align: middle;'>You can call remote web services from MentDB."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>And trigger your SOAP web services in process executions."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>HTTP or HTTPS.");
		mql = new MQLDocumentation(true, "soap http", "To execute a SOAP request through HTTP protocol", "soap http \"http://www.dneonline.com/calculator.asmx\" \"{}\" \"\" \"text/xml; charset=utf-8\" \"<soap:Envelope xmlns:soap=\\\"http://www.w3.org/2003/05/soap-envelope\\\" xmlns:tem=\\\"http://tempuri.org/\\\">\n" + 
				"   <soap:Header/>\n" + 
				"   <soap:Body>\n" + 
				"      <tem:Add>\n" + 
				"         <tem:intA>3</tem:intA>\n" + 
				"         <tem:intB>4</tem:intB>\n" + 
				"      </tem:Add>\n" + 
				"   </soap:Body>\n" + 
				"</soap:Envelope>\";", "\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><soap:Envelope xmlns:soap=\\\"http://www.w3.org/2003/05/soap-envelope\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns:xsd=\\\"http://www.w3.org/2001/XMLSchema\\\"><soap:Body><AddResponse xmlns=\\\"http://tempuri.org/\\\"><AddResult>7</AddResult></AddResponse></soap:Body></soap:Envelope>\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("jsonHeader", "The JSON header", "string", true));
		mql.addParam(new MQLParam("action", "The action name", "string", true));
		mql.addParam(new MQLParam("contentType", "The content type (text/xml; charset=utf-8)", "string", true));
		mql.addParam(new MQLParam("data", "The data (an XML file...)", "string", true));
		mql.addParam(new MQLParam("proxy_config", "The proxy config (http:127.0.0.1:8080)", "string", false));
		functions.get("External SOAP API").add(mql);
		mql = new MQLDocumentation(true, "soap https", "To execute a SOAP request through HTTPS protocol", "soap https \"https://www.dneonline.com/calculator.asmx\" \"{}\" \"\" \"text/xml; charset=utf-8\" \"<soap:Envelope xmlns:soap=\\\"http://www.w3.org/2003/05/soap-envelope\\\" xmlns:tem=\\\"http://tempuri.org/\\\">\n" + 
				"   <soap:Header/>\n" + 
				"   <soap:Body>\n" + 
				"      <tem:Add>\n" + 
				"         <tem:intA>3</tem:intA>\n" + 
				"         <tem:intB>4</tem:intB>\n" + 
				"      </tem:Add>\n" + 
				"   </soap:Body>\n" + 
				"</soap:Envelope>\";", "\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\"?><soap:Envelope xmlns:soap=\\\"http://www.w3.org/2003/05/soap-envelope\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xmlns:xsd=\\\"http://www.w3.org/2001/XMLSchema\\\"><soap:Body><AddResponse xmlns=\\\"http://tempuri.org/\\\"><AddResult>7</AddResult></AddResponse></soap:Body></soap:Envelope>\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("url", "The url", "string", true));
		mql.addParam(new MQLParam("jsonHeader", "The JSON header", "string", true));
		mql.addParam(new MQLParam("action", "The action name", "string", true));
		mql.addParam(new MQLParam("contentType", "The content type (text/xml; charset=utf-8)", "string", true));
		mql.addParam(new MQLParam("data", "The data (an XML file...)", "string", true));
		mql.addParam(new MQLParam("proxy_config", "The proxy config (http:127.0.0.1:8080)", "string", false));
		functions.get("External SOAP API").add(mql);

		functions.put("Compress", new Vector<MQLDocumentation>());
		page_description.put("Compress", "<img src='images/p.png' style='vertical-align: middle;'>Like all languages, you have a few data manipulation functions.");
		mql = new MQLDocumentation(true, "compress zip", "Compress into ZIP format", "compress zip \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.zip\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("out", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress unzip", "Uncompress from ZIP format", "compress unzip \"archives/logs/cur_log.sql.zip\" \"archives/logs\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("dir", "The path to the directory", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress tar", "Compress into TAR format", "compress tar \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.tar\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("out", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress untar", "Uncompress from TAR format", "compress untar \"archives/logs/cur_log.sql.tar\" \"archives/logs\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("dir", "The path to the directory", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress tarGz", "Compress into TAR GZ format", "compress tarGz \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.tar.gz\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("out", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress untarGz", "Uncompress from TAR.GZ format", "compress untarGz \"archives/logs/cur_log.sql.tar.gz\" \"archives/logs\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("dir", "The path to the directory", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress tarBz2", "Compress into TAR BZ2 format", "compress tarBz2 \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.tar.bz2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("out", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress untarBz2", "Uncompress from TAR.BZ2 format", "compress untarBz2 \"archives/logs/cur_log.sql.tar.bz2\" \"archives/logs\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("dir", "The path to the directory", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress jar", "Compress into JAR format", "compress jar \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.jar\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("out", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress unjar", "Uncompress from JAR format", "compress unjar \"archives/logs/cur_log.sql.jar\" \"archives/logs\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("in", "The path to in file", "String", true));
		mql.addParam(new MQLParam("dir", "The path to the directory", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress gz", "Compress into GZIP format", "compress gz \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.gz\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("inFile", "The path to in file", "String", true));
		mql.addParam(new MQLParam("outFile", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress ungz", "Uncompress from GZIP format", "compress ungz \"archives/logs/cur_log.sql.gz\" \"archives/logs/cur_log.sql\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("inFile", "The path to in file", "String", true));
		mql.addParam(new MQLParam("outFile", "The path to the file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress bz2", "Compress into BZIP2 format", "compress bz2 \"archives/logs/cur_log.sql\" \"archives/logs/cur_log.sql.bz2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("inFile", "The path to in file", "String", true));
		mql.addParam(new MQLParam("outFile", "The path to the out file", "String", true));
		functions.get("Compress").add(mql);
		mql = new MQLDocumentation(true, "compress unbz2", "Uncompress from BZIP2 format", "compress unbz2 \"archives/logs/cur_log.sql.bz2\" \"archives/logs/cur_log.sql\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("inFile", "The path to in file", "String", true));
		mql.addParam(new MQLParam("outFile", "The path to the file", "String", true));
		functions.get("Compress").add(mql);
		
		functions.put("SQL", new Vector<MQLDocumentation>());
		page_description.put("SQL", "<img src='images/p.png' style='vertical-align: middle;'>Here you can manage SQL actions.");
		mql = new MQLDocumentation(true, "sql connect", "Connect to a database", "sql connect \"session1\" {cm get \"demo_cm_mysql\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("configJson", "The JSON connection config", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql show", "Show all opened connections", "sql show", "[\"session1\"]", null, null, null, null, false, "");
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql auto_commit", "Set the connection as auto commit", "sql auto_commit \"session1\" true;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("bool", "The boolean", "number", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql set_timeout", "Set the query timeout", "sql set_timeout 0;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("timeout", "The timeout in second (integer>=0 - 0=no limit)", "number", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql value", "Get a value from the database", "sql value \"session1\" (concat \"select name from products where id=1\")", "car", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql row", "Get a row from the database", "sql row \"session1\" (concat \"select * from products where id=\" (sql encode 1))", "{\n" + 
				"  \"dtcreate\": \"2018-02-15 10:00:00.0\",\n" + 
				"  \"sale\": \"1\",\n" + 
				"  \"quantity\": \"5\",\n" + 
				"  \"subtype\": \"T\",\n" + 
				"  \"price\": \"7.50\",\n" + 
				"  \"cat\": null,\n" + 
				"  \"name\": \"car\",\n" + 
				"  \"weight\": \"23.4567\",\n" + 
				"  \"id\": \"1\",\n" + 
				"  \"type\": \"A\",\n" + 
				"  \"desc\": \"a car ....\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql col_distinct", "Get a col distinct from the database", "sql col_distinct \"session1\" (concat \"select * from products where id=\" (sql encode 1))", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql block open", "Open the resultset to get a data block", "sql block open \"session1\" tablename (concat \"select * from products where id=\" (sql encode 1));", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql block close", "Close the resultset", "sql block close;", "1", null, null, null, null, false, "");
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql block to_json", "Get a data block from the resultset", "sql block to_json 1", "{\n" + 
				"  \"data\": [\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-15 10:00:00.0\",\n" + 
				"      \"sale\": \"1\",\n" + 
				"      \"quantity\": \"5\",\n" + 
				"      \"subtype\": \"T\",\n" + 
				"      \"price\": \"7.50\",\n" + 
				"      \"cat\": null,\n" + 
				"      \"name\": \"car\",\n" + 
				"      \"weight\": \"23.4567\",\n" + 
				"      \"id\": \"1\",\n" + 
				"      \"type\": \"A\",\n" + 
				"      \"desc\": \"a car ....\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"columns\": [\n" + 
				"    \"id\",\n" + 
				"    \"name\",\n" + 
				"    \"quantity\",\n" + 
				"    \"cat\",\n" + 
				"    \"desc\",\n" + 
				"    \"dtcreate\",\n" + 
				"    \"type\",\n" + 
				"    \"subtype\",\n" + 
				"    \"price\",\n" + 
				"    \"sale\",\n" + 
				"    \"weight\"\n" + 
				"  ],\n" + 
				"  \"table\": \"products\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("nbLine", "The Snumber of line to return", "number", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to json", "Get data from the database to JSON", "sql to json \"session1\" \"products\" (concat \"select * from products\")", "{\n" + 
				"  \"data\": [\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-15 10:00:00.0\",\n" + 
				"      \"sale\": \"1\",\n" + 
				"      \"quantity\": \"5\",\n" + 
				"      \"subtype\": \"T\",\n" + 
				"      \"price\": \"7.50\",\n" + 
				"      \"cat\": null,\n" + 
				"      \"name\": \"car\",\n" + 
				"      \"weight\": \"23.4567\",\n" + 
				"      \"id\": \"1\",\n" + 
				"      \"type\": \"A\",\n" + 
				"      \"desc\": \"a car ....\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-16 12:00:00.0\",\n" + 
				"      \"sale\": \"0\",\n" + 
				"      \"quantity\": \"2\",\n" + 
				"      \"subtype\": \"R\",\n" + 
				"      \"price\": \"9.80\",\n" + 
				"      \"cat\": \"\",\n" + 
				"      \"name\": \"pen\",\n" + 
				"      \"weight\": \"29.987\",\n" + 
				"      \"id\": \"2\",\n" + 
				"      \"type\": \"A\",\n" + 
				"      \"desc\": \"a pen ....\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-17 13:00:00.0\",\n" + 
				"      \"sale\": \"0\",\n" + 
				"      \"quantity\": \"200\",\n" + 
				"      \"subtype\": \"1\",\n" + 
				"      \"price\": \"14.70\",\n" + 
				"      \"cat\": \"money\",\n" + 
				"      \"name\": \"yen\",\n" + 
				"      \"weight\": \"89.987\",\n" + 
				"      \"id\": \"3\",\n" + 
				"      \"type\": \"Z\",\n" + 
				"      \"desc\": \"a yen ....\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-18 13:00:00.0\",\n" + 
				"      \"sale\": \"0\",\n" + 
				"      \"quantity\": \"1\",\n" + 
				"      \"subtype\": \"1\",\n" + 
				"      \"price\": \"14.70\",\n" + 
				"      \"cat\": \"human\",\n" + 
				"      \"name\": \"bob\",\n" + 
				"      \"weight\": \"99.098\",\n" + 
				"      \"id\": \"4\",\n" + 
				"      \"type\": \"Z\",\n" + 
				"      \"desc\": \"a human ....\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"dtcreate\": \"2018-02-19 15:00:00.0\",\n" + 
				"      \"sale\": \"1\",\n" + 
				"      \"quantity\": \"19\",\n" + 
				"      \"subtype\": \"T\",\n" + 
				"      \"price\": \"19.40\",\n" + 
				"      \"cat\": \"animal\",\n" + 
				"      \"name\": \"spider\",\n" + 
				"      \"weight\": \"123.08\",\n" + 
				"      \"id\": \"5\",\n" + 
				"      \"type\": \"E\",\n" + 
				"      \"desc\": \"an animal ....\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"columns\": [\n" + 
				"    \"id\",\n" + 
				"    \"name\",\n" + 
				"    \"quantity\",\n" + 
				"    \"cat\",\n" + 
				"    \"desc\",\n" + 
				"    \"dtcreate\",\n" + 
				"    \"type\",\n" + 
				"    \"subtype\",\n" + 
				"    \"price\",\n" + 
				"    \"sale\",\n" + 
				"    \"weight\"\n" + 
				"  ],\n" + 
				"  \"table\": \"products\"\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to xml", "Get data from the database to XML", "sql to xml \"session1\" \"products\" (concat \"select * from products\")", "&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + 
				"&lt;table>\n" + 
				"  &lt;table>products&lt;/table>\n" + 
				"  &lt;columns>\n" + 
				"    &lt;item>id&lt;/item>\n" + 
				"    &lt;item>name&lt;/item>\n" + 
				"    &lt;item>quantity&lt;/item>\n" + 
				"    &lt;item>cat&lt;/item>\n" + 
				"    &lt;item>desc&lt;/item>\n" + 
				"    &lt;item>dtcreate&lt;/item>\n" + 
				"    &lt;item>type&lt;/item>\n" + 
				"    &lt;item>subtype&lt;/item>\n" + 
				"    &lt;item>price&lt;/item>\n" + 
				"    &lt;item>sale&lt;/item>\n" + 
				"    &lt;item>weight&lt;/item>\n" + 
				"  &lt;/columns>\n" + 
				"  &lt;data>\n" + 
				"    &lt;item>\n" + 
				"      &lt;id>1&lt;/id>\n" + 
				"      &lt;name>car&lt;/name>\n" + 
				"      &lt;quantity>5&lt;/quantity>\n" + 
				"      &lt;cat nil=\"true\"/>\n" + 
				"      &lt;desc>a car ....&lt;/desc>\n" + 
				"      &lt;dtcreate>2018-02-15 10:00:00.0&lt;/dtcreate>\n" + 
				"      &lt;type>A&lt;/type>\n" + 
				"      &lt;subtype>T&lt;/subtype>\n" + 
				"      &lt;price>7.50&lt;/price>\n" + 
				"      &lt;sale>1&lt;/sale>\n" + 
				"      &lt;weight>23.4567&lt;/weight>\n" + 
				"    &lt;/item>\n" + 
				"    &lt;item>\n" + 
				"      &lt;id>2&lt;/id>\n" + 
				"      &lt;name>pen&lt;/name>\n" + 
				"      &lt;quantity>2&lt;/quantity>\n" + 
				"      &lt;cat/>\n" + 
				"      &lt;desc>a pen ....&lt;/desc>\n" + 
				"      &lt;dtcreate>2018-02-16 12:00:00.0&lt;/dtcreate>\n" + 
				"      &lt;type>A&lt;/type>\n" + 
				"      &lt;subtype>R&lt;/subtype>\n" + 
				"      &lt;price>9.80&lt;/price>\n" + 
				"      &lt;sale>0&lt;/sale>\n" + 
				"      &lt;weight>29.987&lt;/weight>\n" + 
				"    &lt;/item>\n" + 
				"    &lt;item>\n" + 
				"      &lt;id>3&lt;/id>\n" + 
				"      &lt;name>yen&lt;/name>\n" + 
				"      &lt;quantity>200&lt;/quantity>\n" + 
				"      &lt;cat>money&lt;/cat>\n" + 
				"      &lt;desc>a yen ....&lt;/desc>\n" + 
				"      &lt;dtcreate>2018-02-17 13:00:00.0&lt;/dtcreate>\n" + 
				"      &lt;type>Z&lt;/type>\n" + 
				"      &lt;subtype>1&lt;/subtype>\n" + 
				"      &lt;price>14.70&lt;/price>\n" + 
				"      &lt;sale>0&lt;/sale>\n" + 
				"      &lt;weight>89.987&lt;/weight>\n" + 
				"    &lt;/item>\n" + 
				"    &lt;item>\n" + 
				"      &lt;id>4&lt;/id>\n" + 
				"      &lt;name>bob&lt;/name>\n" + 
				"      &lt;quantity>1&lt;/quantity>\n" + 
				"      &lt;cat>human&lt;/cat>\n" + 
				"      &lt;desc>a human ....&lt;/desc>\n" + 
				"      &lt;dtcreate>2018-02-18 13:00:00.0&lt;/dtcreate>\n" + 
				"      &lt;type>Z&lt;/type>\n" + 
				"      &lt;subtype>1&lt;/subtype>\n" + 
				"      &lt;price>14.70&lt;/price>\n" + 
				"      &lt;sale>0&lt;/sale>\n" + 
				"      &lt;weight>99.098&lt;/weight>\n" + 
				"    &lt;/item>\n" + 
				"    &lt;item>\n" + 
				"      &lt;id>5&lt;/id>\n" + 
				"      &lt;name>spider&lt;/name>\n" + 
				"      &lt;quantity>19&lt;/quantity>\n" + 
				"      &lt;cat>animal&lt;/cat>\n" + 
				"      &lt;desc>an animal ....&lt;/desc>\n" + 
				"      &lt;dtcreate>2018-02-19 15:00:00.0&lt;/dtcreate>\n" + 
				"      &lt;type>E&lt;/type>\n" + 
				"      &lt;subtype>T&lt;/subtype>\n" + 
				"      &lt;price>19.40&lt;/price>\n" + 
				"      &lt;sale>1&lt;/sale>\n" + 
				"      &lt;weight>123.08&lt;/weight>\n" + 
				"    &lt;/item>\n" + 
				"  &lt;/data>\n" + 
				"&lt;/table>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to html", "Get data from the database to HTML", "sql to html \"session1\" \"products\" (concat \"select * from products\")", "&lt;html>&lt;head>\n" + 
				"&lt;style>\n" + 
				"table, td, th {\n" + 
				"    border: 1px solid black;\n" + 
				"    padding: 8px;\n" + 
				"}\n" + 
				"#table1 {\n" + 
				"    border-collapse: collapse;\n" + 
				"}\n" + 
				"&lt;/style>\n" + 
				"&lt;/head>&lt;body>&lt;h3>Table: &lt;b>products&lt;/b>&lt;/h3>&lt;br/>\n" + 
				"&lt;table id='table1'>&lt;tr>&lt;th>id&lt;/th>&lt;th>name&lt;/th>&lt;th>quantity&lt;/th>&lt;th>cat&lt;/th>&lt;th>desc&lt;/th>&lt;th>dtcreate&lt;/th>&lt;th>type&lt;/th>&lt;th>subtype&lt;/th>&lt;th>price&lt;/th>&lt;th>sale&lt;/th>&lt;th>weight&lt;/th>&lt;/tr>&lt;tr>&lt;td>1&lt;/td>&lt;td>car&lt;/td>&lt;td>5&lt;/td>&lt;td style='color:#FF0000'>[NULL]&lt;/td>&lt;td>a car ....&lt;/td>&lt;td>2018-02-15 10:00:00.0&lt;/td>&lt;td>A&lt;/td>&lt;td>T&lt;/td>&lt;td>7.50&lt;/td>&lt;td>1&lt;/td>&lt;td>23.4567&lt;/td>&lt;/tr>&lt;tr>&lt;td>2&lt;/td>&lt;td>pen&lt;/td>&lt;td>2&lt;/td>&lt;td>&lt;/td>&lt;td>a pen ....&lt;/td>&lt;td>2018-02-16 12:00:00.0&lt;/td>&lt;td>A&lt;/td>&lt;td>R&lt;/td>&lt;td>9.80&lt;/td>&lt;td>0&lt;/td>&lt;td>29.987&lt;/td>&lt;/tr>&lt;tr>&lt;td>3&lt;/td>&lt;td>yen&lt;/td>&lt;td>200&lt;/td>&lt;td>money&lt;/td>&lt;td>a yen ....&lt;/td>&lt;td>2018-02-17 13:00:00.0&lt;/td>&lt;td>Z&lt;/td>&lt;td>1&lt;/td>&lt;td>14.70&lt;/td>&lt;td>0&lt;/td>&lt;td>89.987&lt;/td>&lt;/tr>&lt;tr>&lt;td>4&lt;/td>&lt;td>bob&lt;/td>&lt;td>1&lt;/td>&lt;td>human&lt;/td>&lt;td>a human ....&lt;/td>&lt;td>2018-02-18 13:00:00.0&lt;/td>&lt;td>Z&lt;/td>&lt;td>1&lt;/td>&lt;td>14.70&lt;/td>&lt;td>0&lt;/td>&lt;td>99.098&lt;/td>&lt;/tr>&lt;tr>&lt;td>5&lt;/td>&lt;td>spider&lt;/td>&lt;td>19&lt;/td>&lt;td>animal&lt;/td>&lt;td>an animal ....&lt;/td>&lt;td>2018-02-19 15:00:00.0&lt;/td>&lt;td>E&lt;/td>&lt;td>T&lt;/td>&lt;td>19.40&lt;/td>&lt;td>1&lt;/td>&lt;td>123.08&lt;/td>&lt;/tr>&lt;/table>&lt;/body>&lt;/html>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to excel", "Get data from the database to Excel document", "sql to excel \"session1\" \"products\" (concat \"select * from products\") \"/Users/jimmitry/Desktop/test.xls\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("filePath", "The Excel file path", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to excelx", "Get data from the database to ExcelX document", "sql to excelx \"session1\" \"products\" (concat \"select * from products\") \"/Users/jimmitry/Desktop/test.xlsx\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("filePath", "The Excel file path", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to pdf", "Get data from the database to PDF document", "sql to pdf \"session1\" \"products\" (concat \"select * from products\") \"/Users/jimmitry/Desktop/test.pdf\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("filePath", "The Excel file path", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to csv", "Get data from the database to CSV", "sql to csv \"session1\" \"products\" (concat \"select * from products\") \",\" \"'\"", "id,name,quantity,cat,desc,dtcreate,type,subtype,price,sale,weight\n" + 
				"1,car,5,null,a car ....,2018-02-15 10:00:00.0,A,T,7.50,1,23.4567\n" + 
				"2,pen,2,,a pen ....,2018-02-16 12:00:00.0,A,R,9.80,0,29.987\n" + 
				"3,yen,200,money,a yen ....,2018-02-17 13:00:00.0,Z,1,14.70,0,89.987\n" + 
				"4,bob,1,human,a human ....,2018-02-18 13:00:00.0,Z,1,14.70,0,99.098\n" + 
				"5,spider,19,animal,an animal ....,2018-02-19 15:00:00.0,E,T,19.40,1,123.08", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("columnSeparator", "The column separator", "String", true));
		mql.addParam(new MQLParam("quoteChar", "Quote char", "String", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql to csv_file", "Get data from the database to CSV", "sql to csv_file \"session1\" \"/Users/jimmitry/Desktop/data.csv\" (concat \"select * from products\") \",\" \"'\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("filePath", "The file path", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("columnSeparator", "The column separator", "String", true));
		mql.addParam(new MQLParam("quoteChar", "Quote char", "String", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql dml", "Execute a DML request", "sql dml \"session1\" (concat \"insert into products (id, name, quantity) values (4, 'other', '4567');\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("dmlQuery", "The DML query", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql parse", "Parse data", "sql parse \"session1\" \"T\" (concat \"select name from products\") {<br><br>	log trace [T_name];<br><br>};", "", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		mql.addParam(new MQLParam("namespace", "The namespace", "string", true));
		mql.addParam(new MQLParam("selectQuery", "The SELECT query", "string", true));
		mql.addParam(new MQLParam("mqlAction", "The MQL action to execut on each line", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql commit", "Commit a connection", "sql commit \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql rollback", "Rollback a connection", "sql rollback \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql disconnect", "Disconnect from a database", "sql disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql disconnect all", "Disconnect all connections", "sql disconnect all;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sqlId", "The SQL id", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql encode", "Encode a valid value", "sql encode \"data\"", "'data'", "sql encode null", "null", null, null, false, "");
		mql.addParam(new MQLParam("data", "The data", "string", true));
		functions.get("SQL").add(mql);
		
		mql = new MQLDocumentation(true, "sql show tables", "Show tables from a database", "sql show tables \"demo_cm_mysql\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql select", "Show data from a table", "sql select \"demo_cm_mysql\" \"select * from table limit 0, 100\" \"table\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id", "string", true));
		mql.addParam(new MQLParam("query", "The select query", "string", true));
		mql.addParam(new MQLParam("sizeCols", "The size column separate by coma (,)", "string", false));
		mql.addParam(new MQLParam("title", "The editor title", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql show data", "Show data from a table", "sql show data \"demo_cm_mysql\" \"select * from products limit 0, 100\" \"products\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id", "string", true));
		mql.addParam(new MQLParam("query", "The select query", "string", true));
		mql.addParam(new MQLParam("title", "The editor title", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql show desc", "Show table description", "sql show desc \"demo_cm_mysql\" \"products\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id", "string", true));
		mql.addParam(new MQLParam("tablename", "The table name", "string", true));
		functions.get("SQL").add(mql);
		mql = new MQLDocumentation(true, "sql show activity", "Show activity of scripts", "sql show activity DAY\n	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 100)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("groupType", "The group type (SEC|MIN|HOUR|DAY|MONTH|YEAR)", "string", true));
		mql.addParam(new MQLParam("dtMin", "The min date", "string", true));
		mql.addParam(new MQLParam("dtMax", "The max date", "string", true));
		functions.get("SQL").add(mql);
		
		functions.put("MongoDB", new Vector<MQLDocumentation>());
		page_description.put("MongoDB", "<img src='images/p.png' style='vertical-align: middle;'>Here you can control MongoDB server.");
		mql = new MQLDocumentation(true, "mongodb connect", "Connect to a database", "mongodb connect \"mongodb://localhost:27017\" \"client1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("url", "The url (\"mongodb://localhost:27017\")", "string", true));
		mql.addParam(new MQLParam("clientId", "The client id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb client_show_loaded", "Show loaded collection", "mongodb client_show_loaded;", "[\"client1\"]", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb client_exist", "Check if mongodb is already loaded", "mongodb client_exist \"client1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clientId", "The client id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb disconnect", "Disconnect a MongoDB connection", "mongodb disconnect \"client1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clientId", "The client id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb disconnect all", "Disconnect all MongoDB connections", "mongodb disconnect all;", "1", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_show", "Show all databases from a MongoDB server", "mongodb database_show \"client1\";", "[\"admin\",\"config\",\"jim\",\"local\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clientId", "The client id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_load", "Load a database into the memory", "mongodb database_load \"client1\" \"database1\" \"jimdb\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clientId", "The client id", "string", true));
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		mql.addParam(new MQLParam("databaseName", "The database name", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_show_loaded", "Show loaded collection", "mongodb database_show_loaded;", "[\"database1\"]", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_exist", "Check if a database is already loaded", "mongodb database_exist \"database1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_unload", "Unload a database from the memory", "mongodb database_unload \"database1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_unload all", "Unload all databases from the memory", "mongodb database_unload all;", "1", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_show", "Show all collections from a database", "mongodb collection_show \"database1\";", "[\"collection_jim\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_load", "Load a collection into the memory", "mongodb collection_load \"database1\" \"collection1\" \"jimcollection\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		mql.addParam(new MQLParam("collectionName", "The collection name", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_show_loaded", "Show loaded collection", "mongodb collection_show_loaded;", "[\"collection1\"]", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_exist", "Check if a collection is already loaded", "mongodb collection_exist \"collection1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_unload", "Unload a collection from the memory", "mongodb collection_unload \"collection1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_unload all", "Unload all collections from the memory", "mongodb collection_unload all;", "1", null, null, null, null, false, "");
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_insert", "Insert a new element into a collection", "mongodb collection_insert \"collection1\" \"{\\\"name\\\": \\\"payet\\\", \\\"age\\\": 40}\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		mql.addParam(new MQLParam("json", "The json to insert", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_update", "Update elements from a collection", "mongodb collection_update \"collection1\" \"{\\\"name\\\": \\\"jim\\\"}\" \"{\\\"$set\\\": {\\\"age\\\": 99}}\";", "2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		mql.addParam(new MQLParam("jsonTarget", "The json target to elements", "string", true));
		mql.addParam(new MQLParam("jsonAction", "The json action on elements", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_delete", "Delete elements from a collection", "mongodb collection_delete \"collection1\" \"{\\\"name\\\": \\\"jim\\\"}\";", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		mql.addParam(new MQLParam("jsonWhere", "The json where", "string", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb collection_select", "Select elements from a collection", "mongodb collection_select \"collection1\" null null null null null null null null null;", "\"[\n" + 
				"  {\n" + 
				"    \\\"_id\\\": {\n" + 
				"      \\\"$oid\\\": \\\"5f81a9f2af9aad3b3a0b942b\\\"\n" + 
				"    },\n" + 
				"    \\\"age\\\": 20\n" + 
				"  }\n" + 
				"]\"", "mongodb disconnect all;\n" + 
				"mongodb database_unload all;\n" + 
				"mongodb collection_unload all;\n" + 
				"mongodb connect \"mongodb://localhost:27017\" \"client1\";\n" + 
				"mongodb database_load \"client1\" \"database1\" \"jim\";\n" + 
				"mongodb collection_load \"database1\" \"collection1\" \"collection_jim\";\n" + 
				"mongodb collection_delete \"collection1\" \"{\\\"name\\\": \\\"jim\\\"}\";\n" + 
				"mongodb collection_delete \"collection1\" \"{\\\"name\\\": \\\"toto\\\"}\";\n" + 
				"mongodb collection_delete \"collection1\" \"{\\\"name\\\": \\\"payet\\\"}\";\n" + 
				"mongodb collection_insert \"collection1\" \"{\\\"name\\\": \\\"payet\\\", \\\"age\\\": 10}\";\n" + 
				"mongodb collection_insert \"collection1\" \"{\\\"name\\\": \\\"jim\\\", \\\"age\\\": 20}\";\n" + 
				"mongodb collection_insert \"collection1\" \"{\\\"name\\\": \\\"toto\\\", \\\"age\\\": 40}\";\n" + 
				"mongodb collection_select \"collection1\" null null null null null null null null null;\n" + 
				"mongodb collection_select \"collection1\" \"{\\\"name\\\": \\\"jim\\\"}\" null null null null null null null null;\n" + 
				"mongodb collection_select \"collection1\" null \"{\\\"age\\\": 1}\" null null null null null null null;\n" + 
				"mongodb collection_select \"collection1\" null \"{\\\"age\\\": 1}\" \"{\\\"age\\\": 1}\" null null null null null null;\n" + 
				"mongodb collection_select \"collection1\" \"{\\\"name\\\": \\\"jim\\\"}\" \"{\\\"age\\\": 1}\" \"{\\\"age\\\": 1}\" null null null null 1 1;", "[]", null, null, false, "");
		mql.addParam(new MQLParam("collectionId", "The collection id", "string", true));
		mql.addParam(new MQLParam("jsonFilter", "The json filter object", "string", true));
		mql.addParam(new MQLParam("jsonSort", "The json sort object", "string", true));
		mql.addParam(new MQLParam("jsonProjection", "The json projection object", "string", true));
		mql.addParam(new MQLParam("jsonHint", "The json hint object", "string", true));
		mql.addParam(new MQLParam("jsonMin", "The json min object", "string", true));
		mql.addParam(new MQLParam("jsonMax", "The json max object", "string", true));
		mql.addParam(new MQLParam("batchSize", "The batch size", "number", true));
		mql.addParam(new MQLParam("skip", "Skip to position", "number", true));
		mql.addParam(new MQLParam("limit", "Number of row to return", "number", true));
		functions.get("MongoDB").add(mql);
		mql = new MQLDocumentation(true, "mongodb database_stat", "Show stat for a specific database", "mongodb database_stat \"database1\";", "{\"objects\":3,\"dataSize\":135.0,\"indexSize\":32768.0,\"indexes\":1,\"collections\":1,\"storageSize\":32768.0,\"numExtents\":0,\"fsUsedSize\":1.13795637248E11,\"avgObjSize\":45.0,\"ok\":1.0,\"db\":\"jim\",\"views\":0,\"fsTotalSize\":4.99963174912E11}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("databaseId", "The database id", "string", true));
		functions.get("MongoDB").add(mql);
		
		functions.put("CM", new Vector<MQLDocumentation>());
		page_description.put("CM", "<img src='images/p.png' style='vertical-align: middle;'>Here you can manage connections.");
		mql = new MQLDocumentation(true, "cm set mentdb", "Set a connection", "cm set \"demo_cm_mentdb\" {execute \"mentdb.remote.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"9998\"\n" + 
				"	\"[key]\" \"pwd\"\n" + 
				"	\"[user]\" \"admin\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[readTimeout]\" \"30000\"\n" + 
				"	\"[subTunnels]\" (mql {\n" + 
				"\n" + 
				"tunnel execute_hot \"tunnelId1\" {cm get \"demo_cm_mentdb\";} (mql {\n" + 
				"	tunnel execute_hot \"tunnelId2\" {cm get \"demo_cm_mentdb\";} (mql {\n" + 
				"	\n" + 
				"		[MQL_TO_REPLACE]\n" + 
				"		\n" + 
				"	});\n" + 
				"});\n" + 
				"\n" + 
				"})\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set as400", "Set a connection", "cm set \"demo_cm_as400\" {execute \"db.as400.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"8471\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set db2", "Set a connection", "cm set \"demo_cm_db2\" {execute \"db.db2.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"50000\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"db2admin\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set derby_embedded", "Set a connection", "cm set \"demo_cm_derby_embedded\" {execute \"db.derby.embedded.config.get\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set derby_server", "Set a connection", "cm set \"demo_cm_derby_server\" {execute \"db.derby.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"1527\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set firebird", "Set a connection", "cm set \"demo_cm_firebird\" {execute \"db.firebird.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"3050\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"SYSDBA\"\n" + 
				"	\"[password]\" \"masterkey\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set h2_embedded", "Set a connection", "cm set \"demo_cm_h2_embedded\" {execute \"db.h2.embedded.config.get\"\n" + 
				"	\"[database]\" \"dir/test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set h2_server", "Set a connection", "cm set \"demo_cm_h2_server\" {execute \"db.h2.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"9091\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"sa\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set hsql_embedded", "Set a connection", "cm set \"demo_cm_hsql_embedded\" {execute \"db.hsql.embedded.config.get\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"SA\"\n" + 
				"	\"[password]\" \"SA\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set hsql_server", "Set a connection", "cm set \"demo_cm_hsql_server\" {execute \"db.hsql.server.config.get\"\n" + 
				"	\"[hostname]\" \"localhost, 127.0.0.1\"\n" + 
				"	\"[port]\" \"9001\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"SA\"\n" + 
				"	\"[password]\" \"SA\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set mysql", "Set a connection", "cm set \"demo_cm_mysql\" {execute \"db.mysql.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"3306\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set oracle", "Set a connection", "cm set \"demo_cm_oracle\" {execute \"db.oracle.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"1521\"\n" + 
				"	\"[database]\" \"xe\"\n" + 
				"	\"[user]\" \"sys\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set postgresql", "Set a connection", "cm set \"demo_cm_postgresql\" {execute \"db.postgresql.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"5432\"\n" + 
				"	\"[database]\" \"test_db\"\n" + 
				"	\"[user]\" \"postgres\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set sqlserver", "Set a connection", "cm set \"demo_cm_sqlserver\" {execute \"db.sqlserver.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"1195\"\n" + 
				"	\"[user]\" \"sa\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set file", "Set a connection", "cm set \"demo_cm_file\" {execute \"file.local.config.get\"\n" + 
				"	\"[dir]\" \"/Users/jimmitry/Desktop\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set ftp", "Set a connection", "cm set \"demo_cm_ftp\" {execute \"file.remote.ftp.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"21\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set ftps", "Set a connection", "cm set \"demo_cm_ftps\" {execute \"file.remote.ftps.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"21\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[protocol]\" \"TLS\"\n" + 
				"	\"[isImplicit]\" false\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				"	\"[dataTimeout]\" 30000\n" + 
				"	\"[keepAliveTimeout]\" 300\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set sftp", "Set a connection", "cm set \"demo_cm_sftp\" {execute \"file.remote.sftp.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"22\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				"	\"[dataTimeout]\" 30000\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set ssh", "Set a connection", "cm set \"demo_cm_ssh\" {execute \"file.remote.ssh.config.get\"\n" + 
				"	\"[hostname]\" \"localhost\"\n" + 
				"	\"[port]\" \"22\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" 10000\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set cifs", "Set a connection", "cm set \"demo_cm_cifs\" {execute \"file.remote.cifs.config.get\"\n" + 
				"	\"[hostname]\" \"192.168.220.130\"\n" + 
				"	\"[port]\" \"445\"\n" + 
				"	\"[domain]\" \"domain\"\n" + 
				"	\"[user]\" \"bob\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[readTimeout]\" \"30000\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set smtp", "Set a connection", "cm set \"demo_cm_smtp\" {execute \"mail.smtp.config.get\"\n" + 
				"	\"[hostname]\" \"smtp.gmail.com\"\n" + 
				"	\"[port]\" \"465\"\n" + 
				"	\"[sender]\" \"your-account@gmail.com\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[authentication]\" true\n" + 
				"	\"[tls]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set pop3", "Set a connection", "cm set \"demo_cm_pop3\" {execute \"mail.pop3.config.get\"\n" + 
				"	\"[hostname]\" \"pop.gmail.com\"\n" + 
				"	\"[port]\" \"995\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[tls]\" false\n" + 
				"	\"[ssl]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm set imap", "Set a connection", "cm set \"demo_cm_imap\" {execute \"mail.imap.config.get\"\n" + 
				"	\"[hostname]\" \"imap.gmail.com\"\n" + 
				"	\"[port]\" \"993\"\n" + 
				"	\"[user]\" \"your-account@gmail.com\"\n" + 
				"	\"[password]\" \"pwd\"\n" + 
				"	\"[auth]\" false\n" + 
				"	\"[tls]\" false\n" + 
				"	\"[ssl]\" true\n" + 
				"	\"[connectTimeout]\" \"10000\"\n" + 
				"	\"[sessionTimeout]\" \"60000\"\n" + 
				";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("object", "The JSON object", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm show_scrud", "Show all database connections for scrud operation (only MySQL or H2)", "cm show_scrud;", "[<br>  \"MENTDB\"<br>]", null, null, null, null, false, "");
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm show", "Show all connections", "cm show;", "[<br>  \"demo_cm_as400\",<br>  \"demo_cm_cifs\",<br>  \"demo_cm_db2\",<br>  \"demo_cm_derby_embedded\",<br>  \"demo_cm_derby_server\",<br>  \"demo_cm_file\",<br>  \"demo_cm_firebird\",<br>  \"demo_cm_ftp\",<br>  \"demo_cm_ftps\",<br>  \"demo_cm_h2_embedded\",<br>  \"demo_cm_h2_server\",<br>  \"demo_cm_hsql_embedded\",<br>  \"demo_cm_hsql_server\",<br>  \"demo_cm_imap\",<br>  \"demo_cm_mentdb\",<br>  \"demo_cm_mysql\",<br>  \"demo_cm_oracle\",<br>  \"demo_cm_pop3\",<br>  \"demo_cm_postgresql\",<br>  \"demo_cm_sftp\",<br>  \"demo_cm_smtp\",<br>  \"demo_cm_ssh\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The connection type (db|file|cifs|ftp|ftps|sftp|ssh|imap|pop3|smtp|mentdb)", "string", false));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm show_obj", "Show all connections into a JSON object", "cm show_obj;", "{<br>  \"demo_cm_as400\":0,<br>  \"demo_cm_cifs\":0,<br>  \"demo_cm_db2\":0,<br>  \"demo_cm_derby_embedded\":0,<br>  \"demo_cm_derby_server\":0,<br>  \"demo_cm_file\":0,<br>  \"demo_cm_firebird\":0,<br>  \"demo_cm_ftp\":0,<br>  \"demo_cm_ftps\":0,<br>  \"demo_cm_h2_embedded\":0,<br>  \"demo_cm_h2_server\":0,<br>  \"demo_cm_hsql_embedded\":0,<br>  \"demo_cm_hsql_server\":0,<br>  \"demo_cm_imap\":0,<br>  \"demo_cm_mentdb\":0,<br>  \"demo_cm_mysql\":0,<br>  \"demo_cm_oracle\":0,<br>  \"demo_cm_pop3\":0,<br>  \"demo_cm_postgresql\":0,<br>  \"demo_cm_sftp\":0,<br>  \"demo_cm_smtp\":0,<br>  \"demo_cm_ssh\":0<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("type", "The connection type (db|file|cifs|ftp|ftps|sftp|ssh|imap|pop3|smtp|mentdb)", "string", false));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm get", "Get a connection", "cm get \"demo_cm_mysql\";", "{<br>  \"driver\": \"com.mysql.cj.jdbc.Driver\",<br>  \"defaultSchema\": \"test_db\",<br>  \"subType\": \"MySQL\",<br>  \"loginTimeout\": \"10000\",<br>  \"jdbc\": \"jdbc:mysql://localhost:3306/test_db\",<br>  \"type\": \"db\",<br>  \"properties\": {<br>    \"password\": \"pwd\",<br>    \"allowMultiQueries\": \"true\",<br>    \"user\": \"bob\"<br>  }<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm generate_update", "Update a connection", "cm generate_update \"demo_cm_mysql\";", "json load \"tmpCm\" (cm get \"demo_cm_mysql\");<br>json uobject \"tmpCm\" / \"driver\" \"com.mysql.cj.jdbc.Driver\" STR;<br>json uobject \"tmpCm\" / \"defaultSchema\" \"test_db\" STR;<br>json uobject \"tmpCm\" / \"subType\" \"MySQL\" STR;<br>json uobject \"tmpCm\" / \"loginTimeout\" \"10000\" STR;<br>json uobject \"tmpCm\" / \"jdbc\" \"jdbc:mysql://localhost:3306/test_db\" STR;<br>json uobject \"tmpCm\" / \"type\" \"db\" STR;<br>json uobject \"tmpCm\" /properties \"password\" \"pwd\" STR;<br>json uobject \"tmpCm\" /properties \"allowMultiQueries\" \"true\" STR;<br>json uobject \"tmpCm\" /properties \"user\" \"bob\" STR;<br>cm set \"demo_cm_mysql\" {json doc \"tmpCm\"};", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm exist", "Check if a connection id already exist", "cm exist \"demo_cm_mysql\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("type", "The connection type (db|file|cifs|ftp|ftps|sftp|ssh|imap|pop3|smtp|mentdb)", "string", false));
		functions.get("CM").add(mql);
		mql = new MQLDocumentation(true, "cm remove", "Remove a connection", "cm remove \"demo_cm_mysql\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("CM").add(mql);
		
		functions.put("Tunnel", new Vector<MQLDocumentation>());
		page_description.put("Tunnel", "<img src='images/p.png' style='vertical-align: middle;'>Connect to another MentDB engine.");
		mql = new MQLDocumentation(true, "tunnel connect", "Connect to a MentDB server.", "tunnel connect \"session1\" {cm get \"demo_cm_mentdb\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel execute_hot", "Execute a MQL command in hot.", "-> \"[local_var1]\" \"data1\";\n" + 
				"-> \"[local_var2]\" \"data2\";\n" + 
				"-> \"[result1]\" (tunnel execute_hot \"session1\" {cm get \"demo_cm_mentdb\";} (concat \n" + 
				"	\"-> \\\"[remote_var1]\\\" \\\"\" (mql encode [local_var1]) \"\\\";\"\n" + 
				"	\"-> \\\"[remote_var2]\\\" \\\"\" (mql encode [local_var2]) \"\\\";\"\n" + 
				"	(mql {\n" + 
				"		concat [remote_var1] \":\" [remote_var2]\n" + 
				"	})\n" + 
				"));", "data1:data2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		mql.addParam(new MQLParam("mql", "The MQL command to execute", "string", true));
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel execute_hot cluster", "Execute a MQL command in hot throught a cluster.", "include \"cluster.1n.hot.exe\"\n" + 
				"	\"[cluster_id]\" \"cluster_id_1\"\n" + 
				"	\"[method]\" \"LB_50_50|SIGNAL\"\n" + 
				"	\"[tunnel_id]\" \"tunnel_id_1\"\n" + 
				"	\"[request]\" \"name\"\n" + 
				";", "data1:data2", null, null, null, null, false, "");
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel execute cluster", "Execute a MQL command throught a cluster.", "execute \"cluster.1n.exe\"\n" + 
				"	\"[cluster_id]\" \"cluster_id_1\"\n" + 
				"	\"[method]\" \"LB_50_50|SIGNAL\"\n" + 
				"	\"[request]\" \"name\"\n" + 
				";", "data1:data2", null, null, null, null, false, "");
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel execute", "Execute a MQL command.", "-> \"[local_var1]\" \"data1\";\n" + 
				"-> \"[local_var2]\" \"data2\";\n" + 
				"-> \"[result2]\" (tunnel execute \"session1\" (concat \n" + 
				"	\"-> \\\"[remote_var1]\\\" \\\"\" (mql encode [local_var1]) \"\\\";\"\n" + 
				"	\"-> \\\"[remote_var2]\\\" \\\"\" (mql encode [local_var2]) \"\\\";\"\n" + 
				"	(mql {\n" + 
				"		concat [remote_var1] \":\" [remote_var2]\n" + 
				"	})\n" + 
				"));", "data1:data2", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("mql", "The MQL command to execute", "string", true));
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel disconnect", "Disconnect a session.", "tunnel disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "tunnel disconnect all", "Disconnect all sessions.", "tunnel disconnect all;", "0", null, null, null, null, false, "");
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "mql", "Do not parse a string", "mql {name};", "name", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string", "string", true));
		functions.get("Tunnel").add(mql);
		mql = new MQLDocumentation(true, "mql encode", "To get the MQL encode for a specific string", "mql encode \"ad\\\"mn\"", "ad\\\"mn", null, null, null, null, false, "");
		mql.addParam(new MQLParam("str", "The string to encode", "String", true));
		functions.get("Tunnel").add(mql);
		
		functions.put("Mail", new Vector<MQLDocumentation>());
		page_description.put("Mail", "<img src='images/p.png' style='vertical-align: middle;'>Mail sender.");
		mql = new MQLDocumentation(true, "mail download pop3", "Download mail from a POP3 mail box.", "mail download pop3 \"/Users/jimmitry/Desktop/tmp\"\n	3 true\n	{cm get \"demo_cm_pop3\";}", "\"{\n" + 
				"  \\\"Total\\\": 14368,\n" + 
				"  \\\"NbReceived\\\": 1,\n" + 
				"  \\\"Directory\\\": \\\"/Users/jimmitry/Desktop/tmp/20180106160818\\\"\n" + 
				"};\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("output_dir", "The output directory", "string", true));
		mql.addParam(new MQLParam("nbMsgToDownload", "The number of message to download", "string", true));
		mql.addParam(new MQLParam("deleteMsgAfterDownload", "Mark the message as deleted after download (true|false)", "number", true));
		mql.addParam(new MQLParam("jsonPOP3config", "The JSON POP3 config", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail download imap", "Download mail from a IMAP mail box (save mail in json base 64).", 
				"-> \"[receive_dir]\" \"tmp\";\n" + 
				"\n" + 
				"	#Récupération des mails;\n" + 
				"	json load \"receive_state\" (mail download imap [receive_dir]\n" + 
				"		1 \"unread\" null false false\n" + 
				"		\"2019-05-07\" null\n" + 
				"		(mql true)\n" + 
				"		(mql true)\n" + 
				"		{cm get \"demo_cm_imap\";};\n" + 
				"	);\n" + 
				"\n" + 
				"	if (> (json select \"receive_state\" \"/NbReceived\") 0) {\n" + 
				"\n" + 
				"		-> \"[sub_receive_dir]\" (json select \"receive_state\" \"/Directory\");\n" + 
				"\n" + 
				"		json load \"sub_receive_dir\" (file dir_list [sub_receive_dir]);\n" + 
				"		json parse_array \"sub_receive_dir\" \"/\" \"[mail]\" {\n" + 
				"\n" + 
				"			if (string ends_with [mail] \".json\") {\n" + 
				"\n" + 
				"				json load \"current_mail\" (file load (concat [sub_receive_dir] \"/\" [mail]));\n" + 
				"\n" + 
				"				log trace (json doc \"current_mail\");\n" + 
				"\n" + 
				"			};\n" + 
				"		\n" + 
				"		};\n" + 
				"\n" + 
				"	};\n" + 
				"\n" + 
				"	json doc \"receive_state\";", "\"{\n" + 
						"  \\\"Unread\\\": 0,\n" + 
						"  \\\"Total\\\": 14368,\n" + 
						"  \\\"NbReceived\\\": 1,\n" + 
						"  \\\"Directory\\\": \\\"/Users/jimmitry/Desktop/tmp/20180106160818\\\"\n" + 
						"}\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("output_dir", "The output directory", "string", true));
		mql.addParam(new MQLParam("nbMsgToDownload", "The number of message to download", "string", true));
		mql.addParam(new MQLParam("unreadOrAll", "Filter unread or all (unread|all)", "string", true));
		mql.addParam(new MQLParam("copyMessageInAnotherFolder", "If not null or empty, copy mail into the folder", "string", true));
		mql.addParam(new MQLParam("deleteMsgAfterDownload", "Mark the message as deleted after download (true|false)", "number", true));
		mql.addParam(new MQLParam("markAsRead", "Mark messages as read (true|false)", "string", true));
		mql.addParam(new MQLParam("startDate", "Start date", "string", true));
		mql.addParam(new MQLParam("endDate", "End date", "string", true));
		mql.addParam(new MQLParam("fromCondition", "The MQL from condition (variable: [imap_from])", "string", true));
		mql.addParam(new MQLParam("subjectCondition", "The MQL subject condition (variable: [imap_subject])", "string", true));
		mql.addParam(new MQLParam("jsonIMAPconfig", "The JSON IMAP config", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail download imap_disk", "Download mail from a IMAP mail box (save mail in had disk).", 
				"-> \"[receive_dir]\" \"tmp\";\n" + 
				"\n" + 
				"	#Récupération des mails;\n" + 
				"	json load \"receive_state\" (mail download imap_disk [receive_dir]\n" + 
				"		1 \"unread\" null false false\n" + 
				"		\"2019-05-07\" null\n" + 
				"		(mql true)\n" + 
				"		(mql true)\n" + 
				"		{cm get \"demo_cm_imap\";};\n" + 
				"	);\n" + 
				"\n" + 
				"	if (> (json select \"receive_state\" \"/NbReceived\") 0) {\n" + 
				"\n" + 
				"		-> \"[sub_receive_dir]\" (json select \"receive_state\" \"/Directory\");\n" + 
				"\n" + 
				"		json load \"sub_receive_dir\" (file dir_list [sub_receive_dir]);\n" + 
				"		json parse_array \"sub_receive_dir\" \"/\" \"[mail]\" {\n" + 
				"\n" + 
				"			if (string ends_with [mail] \".json\") {\n" + 
				"\n" + 
				"				json load \"current_mail\" (file load (concat [sub_receive_dir] \"/\" [mail]));\n" + 
				"\n" + 
				"				log trace (json doc \"current_mail\");\n" + 
				"\n" + 
				"			};\n" + 
				"		\n" + 
				"		};\n" + 
				"\n" + 
				"	};\n" + 
				"\n" + 
				"	json doc \"receive_state\";", "\"{\n" + 
						"  \\\"Unread\\\": 0,\n" + 
						"  \\\"Total\\\": 14368,\n" + 
						"  \\\"NbReceived\\\": 1,\n" + 
						"  \\\"Directory\\\": \\\"/Users/jimmitry/Desktop/tmp/20180106160818\\\"\n" + 
						"}\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("output_dir", "The output directory", "string", true));
		mql.addParam(new MQLParam("nbMsgToDownload", "The number of message to download", "string", true));
		mql.addParam(new MQLParam("unreadOrAll", "Filter unread or all (unread|all)", "string", true));
		mql.addParam(new MQLParam("copyMessageInAnotherFolder", "If not null or empty, copy mail into the folder", "string", true));
		mql.addParam(new MQLParam("deleteMsgAfterDownload", "Mark the message as deleted after download (true|false)", "number", true));
		mql.addParam(new MQLParam("markAsRead", "Mark messages as read (true|false)", "string", true));
		mql.addParam(new MQLParam("startDate", "Start date", "string", true));
		mql.addParam(new MQLParam("endDate", "End date", "string", true));
		mql.addParam(new MQLParam("fromCondition", "The MQL from condition (variable: [imap_from])", "string", true));
		mql.addParam(new MQLParam("subjectCondition", "The MQL subject condition (variable: [imap_subject])", "string", true));
		mql.addParam(new MQLParam("jsonIMAPconfig", "The JSON IMAP config", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail send", "Add a mail to send into the mail spooler.", "mail send \"demo_cm_smtp\" \"jimmitry.payet@gmail.com\" \"\" \"\" \"subject\" \"body\" \"[]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cm", "The connection id", "string", true));
		mql.addParam(new MQLParam("to", "The mail list to (separate by ; )", "string", true));
		mql.addParam(new MQLParam("cc", "The mail list copy (separate by ; )", "string", true));
		mql.addParam(new MQLParam("bcc", "The mail list secret copy (separate by ; )", "string", true));
		mql.addParam(new MQLParam("subject", "The subject", "string", true));
		mql.addParam(new MQLParam("body", "The body", "string", true));
		mql.addParam(new MQLParam("jsonAttachments", "The JSON attachments", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail process", "Process to send mail.", "mail process;", "1", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail process_limit", "Show the mail config process limit.", "mail process_limit;", "5", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail count_loaded", "Count the number of loaded process.", "mail count_loaded;", "4", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail count_all", "Count all mails to send.", "mail count_all;", "12", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail count_error", "Count all mails with error.", "mail count_error;", "3", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail min_error_date", "Get the min error date (more old date).", "mail min_error_date;", "2017-12-12 10:00:00", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail show", "Show mails to send.", "mail show 15", "[<br>  {<br>    \"lastattempt\": \"2018-02-26 06:43:57.0\",<br>    \"dtcreate\": \"2018-02-25 18:41:33.0\",<br>    \"nbattempt\": \"19\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials 188sm7482946wmx.12 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 123sm6579371wmt.31 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 188sm7482946wmx.12 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"3\",<br>    \"state\": \"E\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": \"2018-02-26 06:43:57.0\",<br>    \"dtcreate\": \"2018-02-25 18:44:40.0\",<br>    \"nbattempt\": \"18\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials 7sm2113093wry.18 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 55sm6642082wrz.6 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 7sm2113093wry.18 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"4\",<br>    \"state\": \"E\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": \"2018-02-26 06:54:23.0\",<br>    \"dtcreate\": \"2018-02-25 18:46:37.0\",<br>    \"nbattempt\": \"17\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials e191sm5921603wmg.12 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP h194sm21470505wma.8 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP e191sm5921603wmg.12 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"5\",<br>    \"state\": \"S\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": \"2018-02-26 06:54:23.0\",<br>    \"dtcreate\": \"2018-02-25 18:51:14.0\",<br>    \"nbattempt\": \"17\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials 8sm1655247wmf.13 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP p104sm12967120wrb.47 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 8sm1655247wmf.13 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"6\",<br>    \"state\": \"S\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 18:59:01.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"7\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:05:22.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"8\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:08:32.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"9\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:15:30.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"10\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:17:53.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"11\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:20:08.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"12\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:23:01.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"13\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:36:39.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"14\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:38:44.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"15\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:41:46.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"16\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  },<br>  {<br>    \"lastattempt\": null,<br>    \"dtcreate\": \"2018-02-25 19:43:18.0\",<br>    \"nbattempt\": \"0\",<br>    \"subject\": \"subject\",<br>    \"errmsg\": null,<br>    \"cm\": \"demo_cm_smtp\",<br>    \"id\": \"17\",<br>    \"state\": \"W\",<br>    \"maillist\": \"jimmitry.payet@gmail.com\",<br>    \"mailbcc\": null,<br>    \"mailcc\": null<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("limit", "The limit (default 15)", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail show_error", "Show mail in error.", "mail show_error 15;", "{<br>  \"data\": [<br>    {<br>      \"lastattempt\": \"2018-02-26 06:43:57.0\",<br>      \"dtcreate\": \"2018-02-25 18:41:33.0\",<br>      \"nbattempt\": \"19\",<br>      \"subject\": \"subject\",<br>      \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials 188sm7482946wmx.12 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 123sm6579371wmt.31 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 188sm7482946wmx.12 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>      \"cm\": \"demo_cm_smtp\",<br>      \"id\": \"3\",<br>      \"state\": \"E\",<br>      \"maillist\": \"jimmitry.payet@gmail.com\",<br>      \"mailbcc\": null,<br>      \"mailcc\": null<br>    },<br>    {<br>      \"lastattempt\": \"2018-02-26 06:43:57.0\",<br>      \"dtcreate\": \"2018-02-25 18:44:40.0\",<br>      \"nbattempt\": \"18\",<br>      \"subject\": \"subject\",<br>      \"errmsg\": \"535-5.7.8 Username and Password not accepted. Learn more at\\n535 5.7.8  https://support.google.com/mail/?p\\u003dBadCredentials 7sm2113093wry.18 - gsmtp\\n\\n\\nDEBUG: setDebug: JavaMail version 1.4.7\\nDEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 55sm6642082wrz.6 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\nDEBUG SMTP: useEhlo true, useAuth true\\nDEBUG SMTP: trying to connect to host \\\"smtp.gmail.com\\\", port 465, isSSL true\\n220 smtp.gmail.com ESMTP 7sm2113093wry.18 - gsmtp\\nDEBUG SMTP: connected to host \\\"smtp.gmail.com\\\", port: 465\\n\\nEHLO macbook-pro-de-jimmitry.home\\n250-smtp.gmail.com at your service, [90.10.152.181]\\n250-SIZE 35882577\\n250-8BITMIME\\n250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\n250-ENHANCEDSTATUSCODES\\n250-PIPELINING\\n250-CHUNKING\\n250 SMTPUTF8\\nDEBUG SMTP: Found extension \\\"SIZE\\\", arg \\\"35882577\\\"\\nDEBUG SMTP: Found extension \\\"8BITMIME\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"AUTH\\\", arg \\\"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\\\"\\nDEBUG SMTP: Found extension \\\"ENHANCEDSTATUSCODES\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"PIPELINING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"CHUNKING\\\", arg \\\"\\\"\\nDEBUG SMTP: Found extension \\\"SMTPUTF8\\\", arg \\\"\\\"\\nDEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM \\nDEBUG SMTP: AUTH LOGIN command trace suppressed\\nDEBUG SMTP: AUTH LOGIN failed\\n\",<br>      \"cm\": \"demo_cm_smtp\",<br>      \"id\": \"4\",<br>      \"state\": \"E\",<br>      \"maillist\": \"jimmitry.payet@gmail.com\",<br>      \"mailbcc\": null,<br>      \"mailcc\": null<br>    }<br>  ],<br>  \"columns\": [<br>    \"id\",<br>    \"state\",<br>    \"dtcreate\",<br>    \"lastattempt\",<br>    \"nbattempt\",<br>    \"subject\",<br>    \"maillist\",<br>    \"mailcc\",<br>    \"mailbcc\",<br>    \"errmsg\",<br>    \"cm\"<br>  ],<br>  \"table\": \"mails\"<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("limit", "The limit (default 15)", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail get_body", "Get the body for a specific mail", "mail get_body 4;", "body", null, null, null, null, false, "");
		mql.addParam(new MQLParam("id", "The mail id", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail get_error", "Get the error message for a specific mail", "mail get_error 4;", "535-5.7.8 Username and Password not accepted. Learn more at<br>535 5.7.8  https://support.google.com/mail/?p=BadCredentials 7sm2113093wry.18 - gsmtp<br><br><br>DEBUG: setDebug: JavaMail version 1.4.7<br>DEBUG: getProvider() returning javax.mail.Provider[TRANSPORT,smtps,com.sun.mail.smtp.SMTPSSLTransport,Oracle]<br>DEBUG SMTP: useEhlo true, useAuth true<br>DEBUG SMTP: trying to connect to host \"smtp.gmail.com\", port 465, isSSL true<br>220 smtp.gmail.com ESMTP 55sm6642082wrz.6 - gsmtp<br>DEBUG SMTP: connected to host \"smtp.gmail.com\", port: 465<br><br>EHLO macbook-pro-de-jimmitry.home<br>250-smtp.gmail.com at your service, [90.10.152.181]<br>250-SIZE 35882577<br>250-8BITMIME<br>250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH<br>250-ENHANCEDSTATUSCODES<br>250-PIPELINING<br>250-CHUNKING<br>250 SMTPUTF8<br>DEBUG SMTP: Found extension \"SIZE\", arg \"35882577\"<br>DEBUG SMTP: Found extension \"8BITMIME\", arg \"\"<br>DEBUG SMTP: Found extension \"AUTH\", arg \"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\"<br>DEBUG SMTP: Found extension \"ENHANCEDSTATUSCODES\", arg \"\"<br>DEBUG SMTP: Found extension \"PIPELINING\", arg \"\"<br>DEBUG SMTP: Found extension \"CHUNKING\", arg \"\"<br>DEBUG SMTP: Found extension \"SMTPUTF8\", arg \"\"<br>DEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM <br>DEBUG SMTP: AUTH LOGIN command trace suppressed<br>DEBUG SMTP: AUTH LOGIN failed<br>DEBUG SMTP: useEhlo true, useAuth true<br>DEBUG SMTP: trying to connect to host \"smtp.gmail.com\", port 465, isSSL true<br>220 smtp.gmail.com ESMTP 7sm2113093wry.18 - gsmtp<br>DEBUG SMTP: connected to host \"smtp.gmail.com\", port: 465<br><br>EHLO macbook-pro-de-jimmitry.home<br>250-smtp.gmail.com at your service, [90.10.152.181]<br>250-SIZE 35882577<br>250-8BITMIME<br>250-AUTH LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH<br>250-ENHANCEDSTATUSCODES<br>250-PIPELINING<br>250-CHUNKING<br>250 SMTPUTF8<br>DEBUG SMTP: Found extension \"SIZE\", arg \"35882577\"<br>DEBUG SMTP: Found extension \"8BITMIME\", arg \"\"<br>DEBUG SMTP: Found extension \"AUTH\", arg \"LOGIN PLAIN XOAUTH2 PLAIN-CLIENTTOKEN OAUTHBEARER XOAUTH\"<br>DEBUG SMTP: Found extension \"ENHANCEDSTATUSCODES\", arg \"\"<br>DEBUG SMTP: Found extension \"PIPELINING\", arg \"\"<br>DEBUG SMTP: Found extension \"CHUNKING\", arg \"\"<br>DEBUG SMTP: Found extension \"SMTPUTF8\", arg \"\"<br>DEBUG SMTP: Attempt to authenticate using mechanisms: LOGIN PLAIN DIGEST-MD5 NTLM <br>DEBUG SMTP: AUTH LOGIN command trace suppressed<br>DEBUG SMTP: AUTH LOGIN failed<br>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("id", "The mail id", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail replay_error_id", "Replay a specific mail en error.", "mail replay_error_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("id", "The mail id", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail replay_error_cm", "Replay all mails for a specific connection id.", "mail replay_error_cm \"smtp_1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cm", "The connection id", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail replay_error_all", "Replay all mails in error", "mail replay_error_all;", "1", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail delete_error_id", "Delete a specific mail in error.", "mail delete_error_id 4;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("id", "The mail id", "number", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail delete_error_cm", "Delete all mails for a specific connection id.", "mail delete_error_cm \"smtp_1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cm", "The connection id", "string", true));
		functions.get("Mail").add(mql);
		mql = new MQLDocumentation(true, "mail delete_error_all", "Delete all mails in error", "mail delete_error_all;", "1", null, null, null, null, false, "");
		functions.get("Mail").add(mql);
		
		functions.put("FTP", new Vector<MQLDocumentation>());
		page_description.put("FTP", "<img src='images/p.png' style='vertical-align: middle;'>All remote protocols.");
		mql = new MQLDocumentation(true, "ftp set timeout", "To set the FTP timeout.", "ftp set timeout 10000;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("connectTimeout", "The connect timeout", "number", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp connect", "Connect to a FTP server. (Use \"anonymous/ftp4j\" for an anonymous connection)", "ftp connect \"session1\" {cm get \"demo_cm_ftp\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp set type", "Set the transfer type. (TEXTUAL | BINARY | AUTO)", "ftp set type \"session1\" \"BINARY\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("type", "The type (TEXTUAL | BINARY | AUTO)", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp active compression", "Active or not the compression.", "ftp active compression \"session1\" true;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("bool", "Boolean (true | false)", "number", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp active", "Set the active mode.", "ftp active \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp passive", "Set the passive mode.", "ftp passive \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp cd", "Go to another remote directory.", "ftp cd \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp pwd", "Get the current remote directory.", "ftp pwd \"session1\";", "/remote/dir", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp mkdir", "Create a remote directory.", "ftp mkdir \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp ls", "Get all files into the current remote directory.", "ftp ls \"session1\" \"*.txt\"", "[]", "ftp ls \"session1\" \"\"", "[]", null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("fileFilterPath", "The file filter path", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp rename", "Rename a remote file or directory.", "ftp rename \"session1\" \"file1\" \"file2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("oldFile", "The old file path", "string", true));
		mql.addParam(new MQLParam("newFile", "The new file path", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp put", "Upload a file.", "ftp put \"session1\" \"file1\" \"RESUME\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		mql.addParam(new MQLParam("mode", "The mode (APPEND | RESUME)", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp get", "Download a file.", "ftp get \"session1\" \"remoteFile1\" \"localFile2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp rm file", "Remove a remote file.", "ftp rm file \"session1\" \"remoteFile1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp rm dir", "Remove a remote directory.", "ftp rm dir \"session1\" \"remoteDir1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteDir", "The remote directory path", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp disconnect", "Disconnect a session.", "ftp disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTP").add(mql);
		mql = new MQLDocumentation(true, "ftp disconnect all", "Disconnect all sessions.", "ftp disconnect all;", "0", null, null, null, null, false, "");
		functions.get("FTP").add(mql);
		
		functions.put("FTPS", new Vector<MQLDocumentation>());
		page_description.put("FTPS", "<img src='images/p.png' style='vertical-align: middle;'>All remote protocols.");
		mql = new MQLDocumentation(true, "ftps connect", "Connect to a FTPS server. (Use \"anonymous/ftp4j\" for an anonymous connection)", "ftps connect \"session1\" {cm get \"demo_cm_ftps\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps parse pbsz", "Define the protection buffer size.", "ftps parse pbsz \"session1\" 0;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("pbsz", "The pbsz size", "number", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps exec prot", "Define the protection protocol.", "ftps exec prot \"session1\" P;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("prot", "The protocol ('C' | 'S' | 'E' | 'P')", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps active", "Set the active mode.", "ftps active \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps passive", "Set the passive mode.", "ftps passive \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps set type", "Set the transfer type. (BINARY | LOCAL | ASCII | EBCDIC)", "ftps set type \"session1\" \"BINARY\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("transferType", "The transfer type (BINARY | LOCAL | ASCII | EBCDIC)", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps cd", "Go to another remote directory.", "ftps cd \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps pwd", "Get the current remote directory.", "ftps pwd \"session1\";", "/remote/dir", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps mkdir", "Create a remote directory.", "ftps mkdir \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps ls", "Get all files from the current remote directory.", "ftps ls \"session1\" \".*.*\"", "[]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("regexFilter", "The regex filter", "string", false));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps ls dirs", "Get all directories from the current remote directory.", "ftps ls dirs \"session1\"", "[]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps rename", "Rename a remote file or directory.", "ftps rename \"session1\" \"file1\" \"file2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("oldFile", "The old file path", "string", true));
		mql.addParam(new MQLParam("newFile", "The new file path", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps put", "Upload a file.", "ftps put \"session1\" \"file1\" \"file2\" \"RESUME\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		mql.addParam(new MQLParam("mode", "The mode (APPEND | RESUME)", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps get", "Download a file.", "ftps get \"session1\" \"remoteFile1\" \"localFile2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps rm", "Remove a remote file.", "ftps rm \"session1\" \"remoteFile1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps rm dir", "Remove a remote directory.", "ftps rm dir \"session1\" \"remoteDir1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteDir", "The remote directory path", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps disconnect", "Disconnect a session.", "ftps disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("FTPS").add(mql);
		mql = new MQLDocumentation(true, "ftps disconnect all", "Disconnect all sessions.", "ftps disconnect all;", "0", null, null, null, null, false, "");
		functions.get("FTPS").add(mql);
		
		functions.put("SFTP", new Vector<MQLDocumentation>());
		page_description.put("SFTP", "<img src='images/p.png' style='vertical-align: middle;'>All remote protocols.");
		mql = new MQLDocumentation(true, "sftp connect", "Connect to a SFTP server.", "sftp connect \"session1\" {cm get \"demo_cm_sftp\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp cd", "Go to another remote directory.", "sftp cd \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp lcd", "Go to another local directory.", "sftp lcd \"session1\" \"/local/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The local directory", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp pwd", "Get the current remote directory.", "sftp pwd \"session1\";", "/remote/dir", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp lpwd", "Get the current local directory.", "sftp lpwd \"session1\";", "/local/dir", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp mkdir", "Create a remote directory.", "sftp mkdir \"session1\" \"/remote/dir\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("directory", "The remote directory", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp ls", "Get all files into the current remote directory.", "sftp ls \"session1\" \"*\"", "[]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("fileFilterPath", "The file filter path", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp rename", "Rename a remote file or directory.", "sftp rename \"session1\" \"file1\" \"file2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("oldFile", "The old file path", "string", true));
		mql.addParam(new MQLParam("newFile", "The new file path", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp home", "Get home.", "sftp home \"session1\";", "/home/dir", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp version", "Get protocol server version.", "sftp version \"session1\";", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp put", "Upload a file.", "sftp put \"session1\" \"file1\" \"file2\" \"RESUME\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		mql.addParam(new MQLParam("mode", "The mode (OVERWRITE | APPEND | RESUME)", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp get", "Download a file.", "sftp get \"session1\" \"remoteFile1\" \"localFile2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp rm", "Remove a remote file.", "sftp rm \"session1\" \"remoteFile1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file path", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp rm dir", "Remove a remote directory.", "sftp rm dir \"session1\" \"remoteDir1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteDir", "The remote directory path", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp disconnect", "Disconnect a session.", "sftp disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SFTP").add(mql);
		mql = new MQLDocumentation(true, "sftp disconnect all", "Disconnect all sessions.", "sftp disconnect all;", "0", null, null, null, null, false, "");
		functions.get("SFTP").add(mql);
		
		functions.put("CIFS", new Vector<MQLDocumentation>());
		page_description.put("CIFS", "<img src='images/p.png' style='vertical-align: middle;'>All remote protocols.");
		mql = new MQLDocumentation(true, "cifs mkdir", "Create a remote directory.", "cifs mkdir \"remoteDir\" {cm get \"demo_cm_cifs\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("remoteDir", "The remote dir", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		mql = new MQLDocumentation(true, "cifs rm", "Delete a remote file or directory.", "cifs rm \"file.txt\" {cm get \"demo_cm_cifs\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("remoteFileDir", "The remote file or directory", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		mql = new MQLDocumentation(true, "cifs ls", "Show a remote directory.", "cifs ls \"sharedFolder\" \".*.*\" {cm get \"demo_cm_cifs\";}", "[]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("remoteDir", "The remote dir", "string", true));
		mql.addParam(new MQLParam("filter", "The file filter", "string", false));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		mql = new MQLDocumentation(true, "cifs rename", "Rename a remote file or directory.", "cifs rename \"file1.txt\" \"file2.txt\" {cm get \"demo_cm_cifs\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("remoteFileDir", "The remote file or directory", "string", true));
		mql.addParam(new MQLParam("newName", "The new name", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		mql = new MQLDocumentation(true, "cifs get", "Download a file.", "cifs get \"file1.txt\" \"/Users/user/Desktop/file2.txt\" {cm get \"demo_cm_cifs\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("remoteFile", "The remote file", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		mql = new MQLDocumentation(true, "cifs put", "Upload a file.", "cifs put \"/Users/user/Desktop/file1.txt\" \"file2.txt\" {cm get \"demo_cm_cifs\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("localFile", "The local file path", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("CIFS").add(mql);
		
		functions.put("SSH", new Vector<MQLDocumentation>());
		page_description.put("SSH", "<img src='images/p.png' style='vertical-align: middle;'>All remote protocols.");
		mql = new MQLDocumentation(true, "ssh connect", "Connect to a SSH server.", "ssh connect \"session1\" {cm get \"demo_cm_ssh\";};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("jsonObject", "The json object information", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh execute_1_cmd", "Execute a remote command.", "ssh execute_1_cmd \"session1\" \"ps -e | grep java\";", "17233 ??        12:18.24 /usr/bin/java -cp core:lib/* -Dlog4j.configuration=file:conf/log4j.xml -Xdock:icon=images/db128x128_editor.png -Xdock:name=Mentalese Trigger -Dfile.encoding=UTF-8 -server re.jpayet.mentdb.editor.Mentalese_Trigger\n" + 
				"57035 ??        11:04.93 /Library/Java/JavaVirtualMachines/jdk1.8.0_152.jdk/Contents/Home/jre/bin/java -cp core:lib/autocomplete-2.6.0.jar:lib/balloontip-1.2.4.1.jar:lib/collections-generic-4.01.jar:lib/colt-1.2.0.jar:lib/commons-io-2.4.jar:lib/commons-lang3-3.4.jar:lib/concurrent-1.3.4.jar:lib/gs-algo-1.3.jar:lib/gs-core-1.3.jar:lib/gs-ui-1.3.jar:lib/gson-2.2.3.jar:lib/j3d-core-1.3.1.jar:lib/java-websocket-1.3.0.jar:lib/javax.el-3.0.0.jar:lib/javax.servlet-api-3.1.0.jar:lib/javax.servlet.jsp-2.3.2.jar:lib/javax.servlet.jsp-api-2.3.1.jar:lib/jetty-all-9.2.2.v20140723.jar:lib/jetty-jsp-jdt-2.3.3.jar:lib/jfreechart-1.5.0.jar:lib/jline-2.12.1.jar:lib/json-simple-1.1.1.jar:lib/JTattoo-1.6.11.jar:lib/log4j-1.2.16.jar:lib/quartz-2.2.1.jar:lib/quartz-jobs-2.2.1.jar:lib/rsyntaxtextarea-2.6.1.jar:lib/slf4j-api-1.6.6.jar:lib/slf4j-log4j12-1.6.6.jar:lib/stax-api-1.0.1.jar:lib/vecmath-1.3.1.jar:lib/wstx-asl-3.2.6.jar -Dlog4j.configuration=file:conf/log4j.xml -Dfile.encoding=UTF-8 re.jpayet.mentdb.editor.Mentalese_Editor localhost 9998 admin pwd pwd 10000 10000\n" + 
				"66213 ??         0:00.00 bash -c ps -e | grep java\n" + 
				"66215 ??         0:00.00 grep java\n" + 
				"65926 ttys000    1:13.42 /usr/bin/java -server -cp core:lib/* -Dlog4j.configuration=file:conf/log4j.xml -Dfile.encoding=UTF-8 -server re.jpayet.mentdb.ext.server.Start", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("shellCommand", "The shell command", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh execute_n_cmd", "Execute a remote command.", "ssh execute_n_cmd \"session1\" \"pwd;ps -e | grep java\";", "<response>\n" + 
				"<validLines>/Users/jimmitry\n" + 
				"17233 ??        12:18.31 /usr/bin/java -cp core:lib/* -Dlog4j.configuration=file:conf/log4j.xml -Xdock:icon=images/db128x128_editor.png -Xdock:name=Mentalese Trigger -Dfile.encoding=UTF-8 -server re.jpayet.mentdb.editor.Mentalese_Trigger\n" + 
				"57035 ??        11:09.69 /Library/Java/JavaVirtualMachines/jdk1.8.0_152.jdk/Contents/Home/jre/bin/java -cp core:lib/autocomplete-2.6.0.jar:lib/balloontip-1.2.4.1.jar:lib/collections-generic-4.01.jar:lib/colt-1.2.0.jar:lib/commons-io-2.4.jar:lib/commons-lang3-3.4.jar:lib/concurrent-1.3.4.jar:lib/gs-algo-1.3.jar:lib/gs-core-1.3.jar:lib/gs-ui-1.3.jar:lib/gson-2.2.3.jar:lib/j3d-core-1.3.1.jar:lib/java-websocket-1.3.0.jar:lib/javax.el-3.0.0.jar:lib/javax.servlet-api-3.1.0.jar:lib/javax.servlet.jsp-2.3.2.jar:lib/javax.servlet.jsp-api-2.3.1.jar:lib/jetty-all-9.2.2.v20140723.jar:lib/jetty-jsp-jdt-2.3.3.jar:lib/jfreechart-1.5.0.jar:lib/jline-2.12.1.jar:lib/json-simple-1.1.1.jar:lib/JTattoo-1.6.11.jar:lib/log4j-1.2.16.jar:lib/quartz-2.2.1.jar:lib/quartz-jobs-2.2.1.jar:lib/rsyntaxtextarea-2.6.1.jar:lib/slf4j-api-1.6.6.jar:lib/slf4j-log4j12-1.6.6.jar:lib/stax-api-1.0.1.jar:lib/vecmath-1.3.1.jar:lib/wstx-asl-3.2.6.jar -Dlog4j.configuration=file:conf/log4j.xml -Dfile.encoding=UTF-8 re.jpayet.mentdb.editor.Mentalese_Editor localhost 9998 admin pwd pwd 10000 10000\n" + 
				"66220 ??         0:00.01 bash -c pwd;ps -e | grep java\n" + 
				"66222 ??         0:00.00 grep java\n" + 
				"65926 ttys000    1:13.83 /usr/bin/java -server -cp core:lib/* -Dlog4j.configuration=file:conf/log4j.xml -Dfile.encoding=UTF-8 -server re.jpayet.mentdb.ext.server.Start</validLines>\n" + 
				"<exitCode>0</exitCode>\n" + 
				"</response>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("shellCommand", "The shell command", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh scp from", "Scp from.", "ssh scp from \"session1\" \"remoteFile\" \"localFile\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh scp to", "Scp to.", "ssh scp to \"session1\" \"localFile\" \"remoteFile\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		mql.addParam(new MQLParam("localFile", "The local file", "string", true));
		mql.addParam(new MQLParam("remoteFile", "The remote file", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh disconnect", "Disconnect a session.", "ssh disconnect \"session1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("SSH").add(mql);
		mql = new MQLDocumentation(true, "ssh disconnect all", "Disconnect all sessions.", "ssh disconnect all;", "0", null, null, null, null, false, "");
		functions.get("SSH").add(mql);
		
		functions.put("Excel", new Vector<MQLDocumentation>());
		page_description.put("Excel", "<img src='images/p.png' style='vertical-align: middle;'>Work with Excel document.");
		mql = new MQLDocumentation(true, "excel load", "Load an Excel document.", "excel load \"excelId\" \"/Users/jimmitry/Desktop/test.xls\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("path", "The file path", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel load empty", "Load an empty Excel document.", "excel load empty \"excelId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel show", "All loaded Excel document.", "excel show", "[\"excelId\"]", null, null, null, null, false, "");
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel save", "Save an Excel document into a xls file.", "excel save \"excelId\" \"/Users/jimmitry/Desktop/test.xls\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("path", "The file path", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel exist", "Check if an Excel document already loaded.", "excel exist \"excelId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel sheet add", "Add a new sheet into an excel document.", "excel sheet add \"excelId\" \"sheet2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel sheet delete", "Delete a sheet from an excel document.", "excel sheet delete \"excelId\" \"sheet2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel sheet max_row", "Get a max row for a specific sheet.", "excel sheet max_row \"excelId\" \"sheet2\"", "432", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel sheet show", "Get all sheets from an Excel document.", "excel sheet show \"excelId\";", "[\"sheet2\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel cell get", "Get a cell value.", "excel cell get \"excelId\" \"sheet1\" 1 1", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel cell eval", "Eval a cell.", "excel cell eval \"excelId\" \"sheet1\" 1 1", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel cell ref", "Get a cell reference.", "excel cell ref \"Z3\";", "{\"col\": 25,\"row\": 2}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cell", "The cell (ex: Z3)", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel cell set", "Set a value into a cell.", "excel cell set \"excelId\" \"sheet1\" 1 1 \"value\" STR|NUM|BOOL|DATETIME|FORMULA|BLANK;", "1", "excel close_all;\n" + 
				"excel load empty \"excelId\";\n" + 
				"excel sheet add \"excelId\" \"sheet1\";\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 1 \"str\" STR;\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 2 \".3478\" NUM;\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 3 true BOOL;\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 4 (date systimestamp;) DATETIME;\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 5 \"C2+1\" FORMULA;\n" + 
				"excel cell set \"excelId\" \"sheet1\" 1 6 \"\" BLANK;\n" + 
				"excel save \"excelId\" \"/Users/jimmitry/Desktop/test.xls\";", "1", null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (STR|NUM|BOOL|DATETIME|FORMULA|BLANK)", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel cell format", "Set the format for a cell.", 
				"json load \"style\" \"{}\";\n\n"+
				"json iobject \"style\" / \"HorizontalAlignment\" \"CENTER\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"CENTER_SELECTION\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"DISTRIBUTED\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"FILL\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"GENERAL\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"JUSTIFY\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"LEFT\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"RIGHT\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"VerticalAlignment\" \"BOTTOM\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"TOP\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"DISTRIBUTED\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"JUSTIFY\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"CENTER\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderBottom\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderTop\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderLeft\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderRight\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BottomBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"LeftBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"RightBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"TopBorderColor\" \"0,0,0\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Hidden\" false STR;\n" + 
				"json iobject \"style\" / \"Locked\" false STR;\n" + 
				"json iobject \"style\" / \"QuotePrefixed\" false STR;\n" + 
				"json iobject \"style\" / \"ShrinkToFit\" false STR;\n" + 
				"json iobject \"style\" / \"WrapText\" false STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Font\" \"{}\" OBJ;\n" + 
				"json iobject \"style\" /Font \"fontName\" \"Arial\" STR;\n" + 
				"json iobject \"style\" /Font \"size\" 16 STR;\n" + 
				"json iobject \"style\" /Font \"color\" \"255,0,0\" STR;\n" + 
				"json iobject \"style\" /Font \"bold\" true STR;\n" + 
				"json iobject \"style\" /Font \"italic\" true STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Rotation\" 90 STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillPattern\" \"solid_foreground\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"alt_bars\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"big_spots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"bricks\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"diamonds\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"fine_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"least_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"less_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"no_fill\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"sparse_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"squares\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_backward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_forward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_horz_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_vert_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_backward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_forward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_horz_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_vert_bands\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillForegroundColor\" \"AQUA\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"AUTOMATIC\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLACK\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLUE_GREY\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BRIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BROWN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"CORAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_RED\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GOLD\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_25_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_40_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_50_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_80_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"INDIGO\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LAVENDER\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LEMON_CHIFFON\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIME\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"MAROON\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"OLIVE_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ORCHID\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PALE_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PINK\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PLUM\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"RED\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ROSE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ROYAL_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"SEA_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"SKY_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TAN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"VIOLET\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"WHITE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"YELLOW\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillBackgroundColor\" \"AQUA\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"AUTOMATIC\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLACK\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLUE_GREY\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BRIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BROWN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"CORAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_RED\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GOLD\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_25_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_40_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_50_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_80_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"INDIGO\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LAVENDER\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LEMON_CHIFFON\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIME\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"MAROON\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"OLIVE_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ORCHID\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PALE_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PINK\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PLUM\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"RED\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ROSE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ROYAL_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"SEA_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"SKY_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TAN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"VIOLET\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"WHITE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"YELLOW\" STR;" + 
				"\n"+
				"excel cell format \"excelId\" \"sheet1\" 1 1 \"@\" (json doc \"style\")", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		mql.addParam(new MQLParam("format", "The format (example: 'm/d/yy h:mm' | '0%' ...)", "string", true));
		mql.addParam(new MQLParam("config", "The json format configuration", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel close", "Close an Excel document.", "excel close \"excelId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel close_all", "Close all Excel documents.", "excel close_all;", "1", null, null, null, null, false, "");
		functions.get("Excel").add(mql);
		mql = new MQLDocumentation(true, "excel build_in_format", "Show all build in format.", "excel build_in_format;", "[<br>  \"General\",<br>  \"0\",<br>  \"0.00\",<br>  \"#,##0\",<br>  \"#,##0.00\",<br>  \"\\\"$\\\"#,##0_);(\\\"$\\\"#,##0)\",<br>  \"\\\"$\\\"#,##0_);[Red](\\\"$\\\"#,##0)\",<br>  \"\\\"$\\\"#,##0.00_);(\\\"$\\\"#,##0.00)\",<br>  \"\\\"$\\\"#,##0.00_);[Red](\\\"$\\\"#,##0.00)\",<br>  \"0%\",<br>  \"0.00%\",<br>  \"0.00E+00\",<br>  \"# ?/?\",<br>  \"# ??/??\",<br>  \"m/d/yy\",<br>  \"d-mmm-yy\",<br>  \"d-mmm\",<br>  \"mmm-yy\",<br>  \"h:mm AM/PM\",<br>  \"h:mm:ss AM/PM\",<br>  \"h:mm\",<br>  \"h:mm:ss\",<br>  \"m/d/yy h:mm\",<br>  \"reserved-0x17\",<br>  \"reserved-0x18\",<br>  \"reserved-0x19\",<br>  \"reserved-0x1A\",<br>  \"reserved-0x1B\",<br>  \"reserved-0x1C\",<br>  \"reserved-0x1D\",<br>  \"reserved-0x1E\",<br>  \"reserved-0x1F\",<br>  \"reserved-0x20\",<br>  \"reserved-0x21\",<br>  \"reserved-0x22\",<br>  \"reserved-0x23\",<br>  \"reserved-0x24\",<br>  \"#,##0_);(#,##0)\",<br>  \"#,##0_);[Red](#,##0)\",<br>  \"#,##0.00_);(#,##0.00)\",<br>  \"#,##0.00_);[Red](#,##0.00)\",<br>  \"_(* #,##0_);_(* (#,##0);_(* \\\"-\\\"_);_(@_)\",<br>  \"_(\\\"$\\\"* #,##0_);_(\\\"$\\\"* (#,##0);_(\\\"$\\\"* \\\"-\\\"_);_(@_)\",<br>  \"_(* #,##0.00_);_(* (#,##0.00);_(* \\\"-\\\"??_);_(@_)\",<br>  \"_(\\\"$\\\"* #,##0.00_);_(\\\"$\\\"* (#,##0.00);_(\\\"$\\\"* \\\"-\\\"??_);_(@_)\",<br>  \"mm:ss\",<br>  \"[h]:mm:ss\",<br>  \"mm:ss.0\",<br>  \"##0.0E+0\",<br>  \"@\"<br>]", null, null, null, null, false, "");
		functions.get("Excel").add(mql);
		
		functions.put("Excelx", new Vector<MQLDocumentation>());
		page_description.put("Excelx", "<img src='images/p.png' style='vertical-align: middle;'>Work with Excelx document.");
		mql = new MQLDocumentation(true, "excelx load", "Load an Excelx document.", "excelx load \"excelId\" \"/Users/jimmitry/Desktop/test.xlsx\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("path", "The file path", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx load empty", "Load an empty Excelx document.", "excelx load empty \"excelId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx show", "All loaded Excelx document.", "excelx show", "[\"excelId\"]", null, null, null, null, false, "");
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx save", "Save an Excelx document into a xlsx file.", "excelx save \"excelId\" \"/Users/jimmitry/Desktop/test.xlsx\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("path", "The file path", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx exist", "Check if an Excelx document already loaded.", "excelx exist \"excelId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx sheet add", "Add a new sheet into an excelx document.", "excelx sheet add \"excelId\" \"sheet2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx sheet delete", "Delete a sheet from an excelx document.", "excelx sheet delete \"excelId\" \"sheet2\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx sheet max_row", "Get a max row for a specific sheet.", "excelx sheet max_row \"excelId\" \"sheet2\"", "432", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx sheet show", "Get all sheets from an Excelx document.", "excelx sheet show \"excelId\"", "[\"sheet2\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx cell get", "Get a cell value.", "excelx cell get \"excelId\" \"sheet1\" 1 1", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx cell eval", "Eval a cell.", "excelx cell eval \"excelId\" \"sheet1\" 1 1", "25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx cell ref", "Get a cell reference.", "excelx cell ref \"Z3\";", "{\"col\": 25,\"row\": 2}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cell", "The cell (ex: Z3)", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx cell set", "Set a value into a cell.", "excelx cell set \"excelId\" \"sheet1\" 1 1 \"value\" STR|NUM|BOOL|DATETIME|FORMULA|BLANK;", "1", "excelx close_all;\n" + 
				"excelx load empty \"excelId\";\n" + 
				"excelx sheet add \"excelId\" \"sheet1\";\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 1 \"str\" STR;\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 2 \".3478\" NUM;\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 3 true BOOL;\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 4 (date systimestamp;) DATETIME;\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 5 \"C2+1\" FORMULA;\n" + 
				"excelx cell set \"excelId\" \"sheet1\" 1 6 \"\" BLANK;\n" + 
				"excelx save \"excelId\" \"/Users/jimmitry/Desktop/test.xlsx\";", "1", null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (STR|NUM|BOOL|DATETIME|FORMULA|BLANK)", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx cell format", "Set the format for a cell.", 
				"json load \"style\" \"{}\";\n\n"+
				"json iobject \"style\" / \"HorizontalAlignment\" \"CENTER\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"CENTER_SELECTION\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"DISTRIBUTED\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"FILL\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"GENERAL\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"JUSTIFY\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"LEFT\" STR;\n" + 
				"json iobject \"style\" / \"HorizontalAlignment\" \"RIGHT\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"VerticalAlignment\" \"BOTTOM\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"TOP\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"DISTRIBUTED\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"JUSTIFY\" STR;\n" + 
				"json iobject \"style\" / \"VerticalAlignment\" \"CENTER\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderBottom\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderBottom\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderTop\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderTop\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderLeft\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderLeft\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BorderRight\" \"DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DOTTED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"DOUBLE\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"HAIR\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASH_DOT_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"MEDIUM_DASHED\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"NONE\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"SLANTED_DASH_DOT\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"THICK\" STR;\n" + 
				"json iobject \"style\" / \"BorderRight\" \"THIN\" STR;\n"+
				"\n"+
				"json iobject \"style\" / \"BottomBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"LeftBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"RightBorderColor\" \"0,0,0\" STR;\n" + 
				"json iobject \"style\" / \"TopBorderColor\" \"0,0,0\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Hidden\" false STR;\n" + 
				"json iobject \"style\" / \"Locked\" false STR;\n" + 
				"json iobject \"style\" / \"QuotePrefixed\" false STR;\n" + 
				"json iobject \"style\" / \"ShrinkToFit\" false STR;\n" + 
				"json iobject \"style\" / \"WrapText\" false STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Font\" \"{}\" OBJ;\n" + 
				"json iobject \"style\" /Font \"fontName\" \"Arial\" STR;\n" + 
				"json iobject \"style\" /Font \"size\" 16 STR;\n" + 
				"json iobject \"style\" /Font \"color\" \"255,0,0\" STR;\n" + 
				"json iobject \"style\" /Font \"bold\" true STR;\n" + 
				"json iobject \"style\" /Font \"italic\" true STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"Rotation\" 90 STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillPattern\" \"solid_foreground\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"alt_bars\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"big_spots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"bricks\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"diamonds\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"fine_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"least_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"less_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"no_fill\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"sparse_dots\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"squares\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_backward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_forward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_horz_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thick_vert_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_backward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_forward_diag\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_horz_bands\" STR;\n" + 
				"json iobject \"style\" / \"FillPattern\" \"thin_vert_bands\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillForegroundColor\" \"AQUA\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"AUTOMATIC\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLACK\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BLUE_GREY\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BRIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"BROWN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"CORAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_RED\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"DARK_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GOLD\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_25_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_40_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_50_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"GREY_80_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"INDIGO\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LAVENDER\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LEMON_CHIFFON\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIGHT_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"LIME\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"MAROON\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"OLIVE_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ORCHID\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PALE_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PINK\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"PLUM\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"RED\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ROSE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"ROYAL_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"SEA_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"SKY_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TAN\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"VIOLET\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"WHITE\" STR;\n" + 
				"json iobject \"style\" / \"FillForegroundColor\" \"YELLOW\" STR;\n" + 
				"\n"+
				"json iobject \"style\" / \"FillBackgroundColor\" \"AQUA\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"AUTOMATIC\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLACK\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BLUE_GREY\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BRIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"BROWN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"CORAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_RED\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"DARK_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GOLD\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_25_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_40_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_50_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"GREY_80_PERCENT\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"INDIGO\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LAVENDER\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LEMON_CHIFFON\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_CORNFLOWER_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIGHT_YELLOW\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"LIME\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"MAROON\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"OLIVE_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ORANGE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ORCHID\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PALE_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PINK\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"PLUM\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"RED\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ROSE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"ROYAL_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"SEA_GREEN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"SKY_BLUE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TAN\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TEAL\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"TURQUOISE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"VIOLET\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"WHITE\" STR;\n" + 
				"json iobject \"style\" / \"FillBackgroundColor\" \"YELLOW\" STR;" + 
				"\n"+
				"excelx cell format \"excelId\" \"sheet1\" 1 1 \"@\" (json doc \"style\")", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		mql.addParam(new MQLParam("sheetName", "The sheet name", "string", true));
		mql.addParam(new MQLParam("rowIndex", "The row index", "number", true));
		mql.addParam(new MQLParam("colIndex", "The col index", "number", true));
		mql.addParam(new MQLParam("format", "The format (example: 'm/d/yy h:mm' | '0%' ...)", "string", true));
		mql.addParam(new MQLParam("config", "The json format configuration", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx close", "Close an Excelx document.", "excelx close \"excelId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("excelId", "The excel id", "string", true));
		functions.get("Excelx").add(mql);
		mql = new MQLDocumentation(true, "excelx close_all", "Close all Excelx documents.", "excelx close_all;", "1", null, null, null, null, false, "");
		functions.get("Excelx").add(mql);
		
		functions.put("MSWord", new Vector<MQLDocumentation>());
		page_description.put("MSWord", "<img src='images/p.png' style='vertical-align: middle;'>Work with Word document.");
		mql = new MQLDocumentation(true, "msword replace", "Replace texts in a Word document.", "json load \"findReplace\" \"{}\";\n"+
				"json iobject \"findReplace\" / \"tagToReplace\" \"jim\" STR;\n"
				+ "msword replace \n	\"/Users/jimmitry/Desktop/word1.doc\" \n	\"/Users/jimmitry/Desktop/word2.doc\" \n	(json doc \"findReplace\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("source", "The source path", "string", true));
		mql.addParam(new MQLParam("destination", "The destination path", "string", true));
		mql.addParam(new MQLParam("jsonKeyValue", "The json findtext/replacetext", "string", true));
		functions.get("MSWord").add(mql);
		
		functions.put("MSWordx", new Vector<MQLDocumentation>());
		page_description.put("MSWordx", "<img src='images/p.png' style='vertical-align: middle;'>Work with Word document.");
		mql = new MQLDocumentation(true, "mswordx replace", "Replace texts in a WordX document.", "json load \"findReplace\" \"{}\";\n"+
				"json iobject \"findReplace\" / \"tagToReplace\" \"jim\" STR;\n"
				+ "mswordx replace \n	\"/Users/jimmitry/Desktop/word1.docx\" \n	\"/Users/jimmitry/Desktop/word2.docx\" \n	(json doc \"findReplace\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("source", "The source path", "string", true));
		mql.addParam(new MQLParam("destination", "The destination path", "string", true));
		mql.addParam(new MQLParam("jsonKeyValue", "The json findtext/replacetext", "string", true));
		functions.get("MSWordx").add(mql);
		
		functions.put("PDF", new Vector<MQLDocumentation>());
		page_description.put("PDF", "<img src='images/p.png' style='vertical-align: middle;'>Manage PDF document.");
		mql = new MQLDocumentation(true, "pdf from html", "Generate a PDF from a HTML document.", "pdf from html (sql to html \"session1\" \"products\" (concat \"select * from products\")) \"/Users/jimmitry/Desktop/test.pdf\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("html", "The html", "string", true));
		mql.addParam(new MQLParam("filePath", "The destination file path", "string", true));
		functions.get("PDF").add(mql);
		
		functions.put("SCRUD", new Vector<MQLDocumentation>());
		page_description.put("SCRUD", "<img src='images/p.png' style='vertical-align: middle;'>Generate SCRUD operations.");
		mql = new MQLDocumentation(true, "scrud select", "Generate a select SCRUD operation.", "scrud select \"demo_cm_mysql\" \"products\";", "script create get \"demo_cm_mysql.products.select\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Return rows from the table 'products' in JSON.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[json_result]\" (sql to json \"session1\" \"products\" (concat \n" + 
				"			\"SELECT \n" + 
				"				`id`,\n" + 
				"				`name`,\n" + 
				"				`quantity`,\n" + 
				"				`cat`,\n" + 
				"				`desc`,\n" + 
				"				`dtcreate`,\n" + 
				"				`type`,\n" + 
				"				`subtype`,\n" + 
				"				`price`,\n" + 
				"				`sale`,\n" + 
				"				`weight`\n" + 
				"			 FROM `products` \n" + 
				"			 WHERE \n" + 
				"				`id`=\" (sql encode [id]) \"\n" + 
				"			 LIMIT 0, 100;\"\n" + 
				"		));\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[json_result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return a JSON Object\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud export", "Generate an export SCRUD operation.", "scrud export \"demo_cm_mysql\" \"products\";", "script create exe \"demo_cm_mysql.products.export\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[directory]\" {true} \"The directory\" is_null:false is_empty:false \"/Users/jimmitry/Desktop\")\n" + 
				"  	(var \"[filename]\" {true} \"The file name\" is_null:false is_empty:false \"test\")\n" + 
				"	(var \"[format]\" {type is_enum [format] \"json,xml,csv,excel,excelx,html,pdf\"} \"the format (json|xml|csv|excel|excelx|html|pdf)\" is_null:false is_empty:false \"json\")\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Export the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[query]\" (concat \"SELECT \n" + 
				"				`id`,\n" + 
				"				`name`,\n" + 
				"				`quantity`,\n" + 
				"				`cat`,\n" + 
				"				`desc`,\n" + 
				"				`dtcreate`,\n" + 
				"				`type`,\n" + 
				"				`subtype`,\n" + 
				"				`price`,\n" + 
				"				`sale`,\n" + 
				"				`weight`\n" + 
				"			 FROM `products` \n" + 
				"			 WHERE \n" + 
				"				`id`=\" (sql encode [id]) \"\n" + 
				"			 LIMIT 0, 100;\");\n" + 
				"		\n" + 
				"		switch ([format])\n" + 
				"			(\"json\") {\n" + 
				"				file create (concat [directory] \"/\" [filename] \".json\") (sql to json \"session1\" \"products\" (concat [query]));\n" + 
				"			}\n" + 
				"			(\"xml\") {\n" + 
				"				file create (concat [directory] \"/\" [filename] \".xml\") (sql to xml \"session1\" \"products\" (concat [query]));\n" + 
				"			}\n" + 
				"			(\"csv\") {\n" + 
				"				file create (concat [directory] \"/\" [filename] \".csv\") (sql to csv \"session1\" \"products\" (concat [query]) \",\" \"'\");\n" + 
				"			}\n" + 
				"			(\"excel\") {\n" + 
				"				sql to excel \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".xls\");\n" + 
				"			}\n" + 
				"			(\"excelx\") {\n" + 
				"				sql to excelx \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".xlsx\");\n" + 
				"			}\n" + 
				"			(\"html\") {\n" + 
				"				file create (concat [directory] \"/\" [filename] \".html\") (sql to html \"session1\" \"products\" (concat [query]));\n" + 
				"			}\n" + 
				"			(\"pdf\") {\n" + 
				"				sql to pdf \"session1\" \"products\" (concat [query]) (concat [directory] \"/\" [filename] \".pdf\");\n" + 
				"			}\n" + 
				"			{exception (1) (\"Sorry, the export format must be 'json|xml|csv|excel|excelx|html|pdf'.\");}\n" + 
				"		;\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return nothing\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud create", "Generate a create SCRUD operation.", "scrud create \"demo_cm_mysql\" \"products\";", "CREATE TABLE `products` (\n" + 
				"	`id` bigint(11) NOT NULL auto_increment,\n" + 
				"	`name` varchar(45) NOT NULL ,\n" + 
				"	`quantity` int(11) NOT NULL ,\n" + 
				"	`cat` varchar(45) NULL DEFAULT NULL,\n" + 
				"	`desc` longtext NULL DEFAULT NULL,\n" + 
				"	`dtcreate` datetime NULL DEFAULT NULL,\n" + 
				"	`type` char(1) NULL DEFAULT 'A',\n" + 
				"	`subtype` enum('1','R','T') NULL DEFAULT NULL,\n" + 
				"	`price` decimal(10,2) NULL DEFAULT NULL,\n" + 
				"	`sale` binary(1) NULL DEFAULT NULL,\n" + 
				"	`weight` double NULL DEFAULT NULL, \n" + 
				"	PRIMARY KEY (\n" + 
				"	`id` \n" + 
				"	)\n" + 
				");", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud parse", "Generate a select SCRUD operation to parse.", "scrud parse \"demo_cm_mysql\" \"products\";", "script create exe \"demo_cm_mysql.products.parse_and_action\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Execute MQL action on the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[json_result]\" (sql parse \"session1\" \"T\" (concat \n" + 
				"			\"SELECT \n" + 
				"				`id`,\n" + 
				"				`name`,\n" + 
				"				`quantity`,\n" + 
				"				`cat`,\n" + 
				"				`desc`,\n" + 
				"				`dtcreate`,\n" + 
				"				`type`,\n" + 
				"				`subtype`,\n" + 
				"				`price`,\n" + 
				"				`sale`,\n" + 
				"				`weight`\n" + 
				"			 FROM `products` \n" + 
				"			 WHERE \n" + 
				"				`id`=\" (sql encode [id]) \"\n" + 
				"			 LIMIT 0, 100;\") {\n" + 
				"			\n" + 
				"			#Here the fields ...;\n" + 
				"			[T_name];\n" + 
				"			[T_quantity];\n" + 
				"			[T_cat];\n" + 
				"			[T_desc];\n" + 
				"			[T_dtcreate];\n" + 
				"			[T_type];\n" + 
				"			[T_subtype];\n" + 
				"			[T_price];\n" + 
				"			[T_sale];\n" + 
				"			[T_weight];\n" + 
				"			\n" + 
				"			#Here your MQL code ...;\n" + 
				"			\n" + 
				"		\n" + 
				"		});\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[json_result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return a JSON result with all return lines.\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud db_to_db", "Generate SCRUD operations from db to db.", "scrud db_to_db \"demo_cm_mysql\" \"products\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud csv_to_db", "Generate SCRUD operations from csv to db.", "scrud csv_to_db \"demo_cm_mysql\" \"products\" \"/Users/jimmitry/Desktop/file.csv\" \",\" \"'\" \"A,B,C\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		mql.addParam(new MQLParam("csv_filePath", "The file path", "string", true));
		mql.addParam(new MQLParam("csv_columnSeparator", "The column separator", "string", true));
		mql.addParam(new MQLParam("csv_quoteChar", "The quote char", "string", true));
		mql.addParam(new MQLParam("csv_forceColumnNames", "To force the column name (can be empty)", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud insert", "Generate an insert SCRUD operation.", "scrud insert \"demo_cm_mysql\" \"products\";", "script create post \"demo_cm_mysql.products.insert\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[name]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[quantity]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[cat]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[desc]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[dtcreate]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[type]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[subtype]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[price]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[sale]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[weight]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Insert a new element into the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[result]\" (sql dml \"session1\" (concat \n" + 
				"			\"INSERT INTO `products` (\n" + 
				"				`id`,\n" + 
				"				`name`,\n" + 
				"				`quantity`,\n" + 
				"				`cat`,\n" + 
				"				`desc`,\n" + 
				"				`dtcreate`,\n" + 
				"				`type`,\n" + 
				"				`subtype`,\n" + 
				"				`price`,\n" + 
				"				`sale`,\n" + 
				"				`weight`\n" + 
				"			) VALUES (\n" + 
				"				\" (sql encode [id]) \" ,\n" + 
				"				\" (sql encode [name]) \" ,\n" + 
				"				\" (sql encode [quantity]) \" ,\n" + 
				"				\" (sql encode [cat]) \" ,\n" + 
				"				\" (sql encode [desc]) \" ,\n" + 
				"				\" (sql encode [dtcreate]) \" ,\n" + 
				"				\" (sql encode [type]) \" ,\n" + 
				"				\" (sql encode [subtype]) \" ,\n" + 
				"				\" (sql encode [price]) \" ,\n" + 
				"				\" (sql encode [sale]) \" ,\n" + 
				"				\" (sql encode [weight]) \"\n" + 
				"			);\"\n" + 
				"		));\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return the number of impacted lines.\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud update", "Generate an update SCRUD operation.", "scrud update \"demo_cm_mysql\" \"products\";", "script create put \"demo_cm_mysql.products.update\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[name]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[quantity]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[cat]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[desc]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[dtcreate]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[type]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[subtype]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[price]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[sale]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[weight]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Update an element into the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[result]\" (sql dml \"session1\" (concat \n" + 
				"			\"UPDATE `products` SET \n" + 
				"				`name`=\" (sql encode [name]) \" ,\n" + 
				"				`quantity`=\" (sql encode [quantity]) \" ,\n" + 
				"				`cat`=\" (sql encode [cat]) \" ,\n" + 
				"				`desc`=\" (sql encode [desc]) \" ,\n" + 
				"				`dtcreate`=\" (sql encode [dtcreate]) \" ,\n" + 
				"				`type`=\" (sql encode [type]) \" ,\n" + 
				"				`subtype`=\" (sql encode [subtype]) \" ,\n" + 
				"				`price`=\" (sql encode [price]) \" ,\n" + 
				"				`sale`=\" (sql encode [sale]) \" ,\n" + 
				"				`weight`=\" (sql encode [weight]) \"\n" + 
				"			WHERE \n" + 
				"				`id`=\" (sql encode [id]) \"\n" + 
				"			;\"\n" + 
				"		));\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return the number of impacted lines.\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud merge", "Generate a merge SCRUD operation.", "scrud merge \"demo_cm_mysql\" \"products\";", "script create post \"demo_cm_mysql.products.merge\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[name]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[quantity]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[cat]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[desc]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[dtcreate]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[type]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[subtype]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[price]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[sale]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  	(var \"[weight]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Insert a new element into the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[result]\" (sql dml \"session1\" (concat \n" + 
				"			\"INSERT INTO `products` (\n" + 
				"				`id`,\n" + 
				"				`name`,\n" + 
				"				`quantity`,\n" + 
				"				`cat`,\n" + 
				"				`desc`,\n" + 
				"				`dtcreate`,\n" + 
				"				`type`,\n" + 
				"				`subtype`,\n" + 
				"				`price`,\n" + 
				"				`sale`,\n" + 
				"				`weight`\n" + 
				"			) VALUES (\n" + 
				"				\" (sql encode [id]) \" ,\n" + 
				"				\" (sql encode [name]) \" ,\n" + 
				"				\" (sql encode [quantity]) \" ,\n" + 
				"				\" (sql encode [cat]) \" ,\n" + 
				"				\" (sql encode [desc]) \" ,\n" + 
				"				\" (sql encode [dtcreate]) \" ,\n" + 
				"				\" (sql encode [type]) \" ,\n" + 
				"				\" (sql encode [subtype]) \" ,\n" + 
				"				\" (sql encode [price]) \" ,\n" + 
				"				\" (sql encode [sale]) \" ,\n" + 
				"				\" (sql encode [weight]) \"\n" + 
				"			) ON DUPLICATE KEY UPDATE \n" + 
				"				`name`=\" (sql encode [name]) \" ,\n" + 
				"				`quantity`=\" (sql encode [quantity]) \" ,\n" + 
				"				`cat`=\" (sql encode [cat]) \" ,\n" + 
				"				`desc`=\" (sql encode [desc]) \" ,\n" + 
				"				`dtcreate`=\" (sql encode [dtcreate]) \" ,\n" + 
				"				`type`=\" (sql encode [type]) \" ,\n" + 
				"				`subtype`=\" (sql encode [subtype]) \" ,\n" + 
				"				`price`=\" (sql encode [price]) \" ,\n" + 
				"				`sale`=\" (sql encode [sale]) \" ,\n" + 
				"				`weight`=\" (sql encode [weight]) \";\"\n" + 
				"		));\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return the number of impacted lines.\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		mql = new MQLDocumentation(true, "scrud delete", "Generate a delete SCRUD operation.", "scrud delete \"demo_cm_mysql\" \"products\";", "script create delete \"demo_cm_mysql.products.delete\" false 1\n" + 
				"  (param\n" + 
				"  	(var \"[id]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n" + 
				"  )\n" + 
				"  \"Delete an element from the table 'products'.\"\n" + 
				"{\n" + 
				"	\n" + 
				"	try {\n" + 
				"		\n" + 
				"		#Connection ...;\n" + 
				"		sql connect \"session1\" {cm get \"demo_cm_mysql\"};\n" + 
				"		\n" + 
				"		-> \"[result]\" (sql dml \"session1\" (concat \n" + 
				"			\"DELETE FROM `products`\n" + 
				"			WHERE \n" + 
				"				`id`=\" (sql encode [id]) \"\n" + 
				"			;\"\n" + 
				"		));\n" + 
				"		\n" + 
				"		#Disconnection ...;\n" + 
				"		sql disconnect \"session1\";\n" + 
				"		\n" + 
				"		# Return the json;\n" + 
				"		[result]\n" + 
				"		\n" + 
				"	} {\n" + 
				"\n" + 
				"		#Close the connection;\n" + 
				"		try {sql disconnect \"session1\"} {} \"[sub_err]\";\n" + 
				"\n" + 
				"		#Generate an error;\n" + 
				"		exception (1) ([err]);\n" + 
				"		\n" + 
				"	} \"[err]\";\n" + 
				"	\n" + 
				"} \"Return the number of impacted lines.\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The connection id (SQL database)", "string", true));
		mql.addParam(new MQLParam("tableName", "The table name", "string", true));
		functions.get("SCRUD").add(mql);
		
		functions.put("Microsoft", new Vector<MQLDocumentation>());
		page_description.put("Microsoft", "<img src='images/p.png' style='vertical-align: middle;'>Microsoft operations.");
		mql = new MQLDocumentation(true, "azure get_token", "Get a token from Microsoft Azure platform.", "azure get_token \"test@domain.onmicrosoft.com\" \"Bal07777\" \"d61b48b2-5bee-4a2e-8ace-99f3c7603426\" \"076306e2-1bc8-4b4a-b69f-e35a53d4dd69\" \"[\\\"https://outlook.office365.com/IMAP.AccessAsUser.All\\\"]\"",  
				"iyJ0eXAiOiJKV1QiLCJub25jZSI6Ik9qeF9BVDZ0dW90Y29hV0RnQzVYRktPd3RuVkNYS3JhdEdxR21lZDZJTFEiLCJhbGciOiJSUzI1NiIsIng1dCI6ImtnMkxZczJUMENUaklmajRydDZKSXluZW4zOCIsImtpZCI6ImtnMkxZczJUMENUaklmajRydDZKSXluZW4zOCJ9.eyJhdWQiOiJodHRwczovL291dGxvb2sub2ZmaWNlMzY1LmNvbSIsImlzcyI6Imh0dHBzOi8vc3RzLndpbmRvd3MubmV0LzA3NjMwNmUyLTFiYzgtNGI0YS1iNjlmLWUzNWE1M2Q0ZGQ2Ni8iLCJpYXQiOjE2MDc1MTIzNzgsIm5iZiI6MTYwNzUxMjM3OCwiZXhwIjoxNjA3NTE2Mjc4LCJhY2N0IjowLCJhY3IiOiIxIiwiYWlvIjoiQVRRQXkvOFJBQUFBWFVTWE1scDNjSUZ1NWRXbC83aDExNWZnQXU5azY4TkQrVW1KSGNVdnhXV2Z2QWhIWjZsTEorb3I1UDQzQTQ2TSIsImFtciI6WyJwd2QiXSwiYXBwX2Rpc3BsYXluYW1lIjoiTWVudERCIiwiYXBwaWQiOiJkNjFiNDhiMi01YmVlLTRhMmUtOGFjZS05OWYzYzc2MDM0MjgiLCJhcHBpZGFjciI6IjAiLCJlbmZwb2xpZHMiOltdLCJpcGFkZHIiOiI5Mi4xMzAuMjMuMjQiLCJuYW1lIjoidGVzdDEiLCJvaWQiOiJmMmY0NjczYS0yNTlmLTRlODUtOTc4Mi1kNjlkOWZjZDNmM2UiLCJwdWlkIjoiMTAwMzIwMDBGMUFFRjU4NyIsInJoIjoiMC5BQUFBNGdaakI4Z2JTa3Uybi1OYVU5VGRackpJRzlidVd5NUtpczZaODhkZ05DaDVBTzQuIiwic2NwIjoiSU1BUC5BY2Nlc3NBc1VzZXIuQWxsIFVzZXIuUmVhZCIsInNpZCI6IjgxNmIwZjExLTdjMTItNDU1NS1iMGZjLTg2ODhlZjVkYjY0NiIsInN1YiI6Ilp1TmhON2lISGFtQ1BnTFAtb2lySkd3UzFIVW5ON0k0REFrMzZMZzdJb0EiLCJ0aWQiOiIwNzYzMDZlMi0xYmM4LTRiNGEtYjY5Zi1lMzVhNTNkNGRkNjYiLCJ1bmlxdWVfbmFtZSI6InRlc3QxQHNhbWlhbGxpYW56Lm9ubWljcm9zb2Z0LmNvbSIsInVwbiI6InRlc3QxQHNhbWlhbGxpYW56Lm9ubWljcm9zb2Z0LmNvbSIsInV0aSI6InBjMHVNbF8zRGthcGpPeFRuUGtRQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdfQ.kjqf__xGSbo3luQ2Wbl6BBVhnlWfjcBKjey6VztZ40MYAgL4Xs9OeZROPZ0bzDe7NDN63XKEE5jWh7xRyYE_NTH4o_awx-epW08aa9CzY4WMqN0pZlwic_4eLMzVXXxjfZS9cwC2cQz1uHgcfwyWqj3q-qtqAsDTUmbeu2nq9Yhcb82IP2cIg2Yq4JCxgko9h3YCNLA__bFaX1THMfDgwYVUX0mdoJiwdjK4gpOZ5nCMiRF8unOULTU9QCkIcv1PmVp5Aa55TyQWDUmcjZ5xova2OISY0v3egFuJHCMVDOWZPN022Zl6iiqkHWkFKCJHC85eHnoRgODVl8UN-liFaA", null, null, null, null, false, "");
		mql.addParam(new MQLParam("email", "The email", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		mql.addParam(new MQLParam("clientId", "The client id (application id)", "string", true));
		mql.addParam(new MQLParam("tenant", "The tenant  id", "string", true));
		mql.addParam(new MQLParam("scope", "The scope (json array)", "string", true));
		functions.get("Microsoft").add(mql);
		
		functions.put("Stripe", new Vector<MQLDocumentation>());
		page_description.put("Stripe", "<img src='images/p.png' style='vertical-align: middle;'>Stripe operation.");
		mql = new MQLDocumentation(true, "stripe session", "Get a Stripe session", "stripe session \"[{\n"
				+ "\\\"Product\\\": \\\"panier\\\",\n"
				+ "\\\"UnitAmount\\\": \\\"1500\\\",\n"
				+ "\\\"Currency\\\": \\\"eur\\\",\n"
				+ "\\\"Quantity\\\": \\\"1\\\"\n"
				+ "}]\" \"https://example.com/success\" \"https://example.com/success\" \"sk_test_51IsiEWGHIUO7eOM2EDWveyR4ut35pehnEfetav3xelcwFoT6kKeetedZghHzdMncAddp6DE5XyEA7S4dfgg05pCrr00W3uJEOM7\"",  
				"{\"id\":\"cs_test_a1LtJAdVf1tEsdfgrBpxZQxKtOgg2JTDxKVMK3WbhhhzyiahhhkXKCT83fTbV\",\"object\":\"checkout.session\",\"allow_promotion_codes\":null,\"amount_subtotal\":1500,\"amount_total\":1500,\"billing_address_collection\":null,\"cancel_url\":\"https://example.com/success\",\"client_reference_id\":null,\"currency\":\"eur\",\"customer\":null,\"customer_details\":null,\"customer_email\":null,\"livemode\":false,\"locale\":null,\"metadata\":{},\"mode\":\"payment\",\"payment_intent\":\"pi_1IsmoMGHIUO7iOM2LNaPh5HP\",\"payment_method_options\":{},\"payment_method_types\":[\"card\"],\"payment_status\":\"unpaid\",\"setup_intent\":null,\"shipping\":null,\"shipping_address_collection\":null,\"submit_type\":null,\"subscription\":null,\"success_url\":\"https://example.com/success\",\"total_details\":{\"amount_discount\":0,\"amount_shipping\":0,\"amount_tax\":0}}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonItems", "The json product items", "string", true));
		mql.addParam(new MQLParam("successUrl", "The success Url", "string", true));
		mql.addParam(new MQLParam("cancelUrl", "The cancel Url", "string", true));
		mql.addParam(new MQLParam("secretKey", "The secret key", "string", true));
		functions.get("Stripe").add(mql);
	}

}
