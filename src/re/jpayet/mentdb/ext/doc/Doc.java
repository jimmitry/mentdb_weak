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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

//The documentation pages
public class Doc {
	
	//Page object
	public static JSONObject pages = new JSONObject();

	@SuppressWarnings("unchecked")
	public static void createPage(String pageId, String title) {
		
		JSONObject page = new JSONObject();
		page.put("id", pageId);
		page.put("title", title);
		page.put("sections", new JSONArray());
		
		pages.put(pageId, page);
		
	}

	@SuppressWarnings("unchecked")
	public static void createSection(String pageId, String title, String description) {
		
		JSONObject section = new JSONObject();
		section.put("title", title);
		section.put("description", description);
		section.put("blocks", new JSONArray());
		
		((JSONArray) ((JSONObject) pages.get(pageId)).get("sections")).add(section);
		
	}

	@SuppressWarnings("unchecked")
	public static void createBlockMql(String pageId, String classId, String functionId) {
		
		JSONObject block = new JSONObject();
		block.put("type", "mql");
		block.put("classId", classId);
		block.put("functionId", functionId);
		
		JSONArray allSections = ((JSONArray) ((JSONObject) pages.get(pageId)).get("sections"));
		
		((JSONArray) ((JSONObject) allSections.get(allSections.size()-1)).get("blocks")).add(block);
		
	}

	@SuppressWarnings("unchecked")
	public static void createBlockText(String pageId, String text) {
		
		JSONObject block = new JSONObject();
		block.put("type", "txt");
		block.put("text", text);
		
		JSONArray allSections = ((JSONArray) ((JSONObject) pages.get(pageId)).get("sections"));
		
		((JSONArray) ((JSONObject) allSections.get(allSections.size()-1)).get("blocks")).add(block);
		
	}

	@SuppressWarnings("unchecked")
	public static void createBlockCHAT(String pageId, String in, String out) {
		
		JSONObject block = new JSONObject();
		block.put("type", "chat");
		block.put("in", in);
		block.put("out", out);
		
		JSONArray allSections = ((JSONArray) ((JSONObject) pages.get(pageId)).get("sections"));
		
		((JSONArray) ((JSONObject) allSections.get(allSections.size()-1)).get("blocks")).add(block);
		
	}
	
	//Get the html source code of the page
	public static String to_html(String pageId) throws Exception {
		
		String result = "";
		
		JSONObject page = ((JSONObject) pages.get(pageId));
		String title = ""+page.get("title");
		result += "<h1 style='border-bottom:1px #000 solid;padding-bottom:4px'><a href='javascript:window.history.back()' style='color:#000'>Back</a> / "+title+"</h1>";
		
		JSONArray sections = (JSONArray) page.get("sections");
		for(int i=0;i<sections.size();i++) {
			JSONObject section = (JSONObject) sections.get(i);
			result += "<a href='#section"+i+"' style='color:#000'>"+section.get("title")+"</a><br>";
		}

		result += "<br><div style='border-bottom: 1px solid #D9DEE4;'></div>";
		
		for(int i=0;i<sections.size();i++) {
			JSONObject section = (JSONObject) sections.get(i);
			title = ""+section.get("title");
			String description = ""+section.get("description");
			
			result += "<h2 id='section"+i+"'>"+title+"</h2>";
			if (description!=null && !description.equals("")) result += "<div style='text-align:justify;margin-bottom:15px'>"+description+"</div>";
			
			JSONArray blocks = (JSONArray) section.get("blocks");
			
			for(int j=0;j<blocks.size();j++) {
				JSONObject block = (JSONObject) blocks.get(j);
				String type = ""+block.get("type");
				
				if (type.equals("txt")) {
					String txt = ""+block.get("text");
					result += "<div style='text-align:justify;'>"+txt+"</div>";
				} else if (type.equals("mql")) {
					String classId = ""+block.get("classId");
					String functionId = ""+block.get("functionId");
					result += MQLDocumentation.mqlFuntion_to_html(classId, functionId);
				}
				
				result += "<div style='padding-bottom:15px;'></div>";
				
			}
			
			if (i<sections.size()-1) result += "<div style='margin-bottom:15px;padding-bottom:15px;border-bottom: 1px #F0F0F0 solid;'></div>";
			
		}
		
		return result;
		
	}

}