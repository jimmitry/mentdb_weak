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

package re.jpayet.mentdb.ext.bot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.parameter.ParameterManager;
import re.jpayet.mentdb.ext.xml.XmlManager;

public class BotManager {

	public static HashMap<String, Bot> bots = new HashMap<String, Bot>();
	public static HashMap<String, HashMap<String, String>> sessions = new HashMap<String, HashMap<String, String>>();
	
	public static void init() throws Exception {
		
		MagicStrings.default_bot_response = ParameterManager.get_value("AIML_DEFAULT_RESPONSE");
		reload_all_bot();
		
	}
	
	public static String get_bot_options() throws Exception {
		
		String result = "";
		
		JSONArray bots = BotManager.show();
		for(int i=0;i<bots.size();i++) {
			
			result += "<option value='"+bots.get(i)+"'>"+bots.get(i)+"</option>";
			
		}
		
		return result;
		
	}
	
	public static void reload_all_bot() throws Exception {
		
		JSONArray b = FileFx.dir_list("bots");
		MagicBooleans.trace_mode = false;
		
		PrintStream ps = System.out;
		System.setOut(new PrintStream(new OutputStream() {

		     @Override
		     public void write(int arg0) throws IOException {

		     }
		  }));
		
		for(int i=0;i<b.size();i++) {
			
			String botName = b.get(i)+"";
			
			if (!botName.startsWith(".")) {
				bots.put(botName, new Bot(botName, getResourcesPath(), "aiml2csv"));
			}
			
		}
		
		System.setOut(ps);
		
	}
	
	public static void load_a_bot(String botname) throws Exception {
		
		MagicBooleans.trace_mode = false;
		
		PrintStream ps = System.out;
		System.setOut(new PrintStream(new OutputStream() {

		     @Override
		     public void write(int arg0) throws IOException {

		     }
		  }));
		
		bots.put(botname, new Bot(botname, getResourcesPath(), "aiml2csv"));
		//bots.get(botname).setAllPaths("bots", botname);
				
		System.setOut(ps);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show() throws Exception {
		
		JSONArray result = new JSONArray();
		JSONArray b = FileFx.dir_list("bots");
		
		for(int i=0;i<b.size();i++) {
			
			String botName = b.get(i)+"";
			
			if (!botName.startsWith(".")) {
				result.add(botName);
			}
			
		}
		
		return result;
		
	}
	
	public static void create_bot(String botName, String is_male, String lang) throws Exception {
		
		if (exist_bot(botName).equals("1")) {
			throw new Exception("Sorry, the bot '"+botName+"' alreay exist.");
		}
		
		if (is_male==null || (!is_male.equals("1") && !is_male.equals("0"))) {
			
			throw new Exception("Sorry, the field 'is_male' must be 1|0.");
			
		}
		
		if (lang==null || (!lang.equals("fr") && !lang.equals("en"))) {
			
			throw new Exception("Sorry, the language must be fr|en.");
			
		}
		
		FileFx.mkdir("bots"+File.separator+botName);
		FileFx.mkdir("bots"+File.separator+botName+File.separator+"aiml");
		
		FileFx.create("bots"+File.separator+botName+File.separator+"bot.conf", "[BOT]\n"
				+ "IS_MALE="+is_male+"\n"
				+ "LANG="+lang+"\n");
		
		load_a_bot(botName);
		
	}
	
	public static String exist_bot(String botName) throws Exception {
		
		return FileFx.exist("bots"+File.separator+botName);
		
	}
	
	public static void delete_bot(String botName) throws Exception {
		
		if (exist_bot(botName).equals("0")) {
			throw new Exception("Sorry, the bot '"+botName+"' does not exist.");
		}
		
		bots.remove(botName);
		
		FileFx.delete("bots"+File.separator+botName);
		
	}
	
	public static String set_aiml_file(EnvManager env, String botName, String filename, String xml) throws Exception {
		
		if (exist_bot(botName).equals("0")) {
			throw new Exception("Sorry, the bot '"+botName+"' does not exist.");
		}
		
		if (!filename.endsWith(".aiml")) {
			throw new Exception("Sorry, the filename '"+filename+"' does not ends with '.aiml'.");
		}
		
		try {
			
			XmlManager.load(env, "tmpXmlAimlFile", xml);
			XmlManager.unload(env, "tmpXmlAimlFile");
			
		} catch (Exception e) {
			throw new Exception("Sorry, the AIML file is not a valid XML file.");
		}
		
		if (FileFx.exist("bots"+File.separator+botName+File.separator+"aiml"+File.separator+filename).equals("1")) {
			FileFx.delete("bots"+File.separator+botName+File.separator+"aiml"+File.separator+filename);
		}
		
		if (FileFx.exist("bots"+File.separator+botName+File.separator+"aimlif"+File.separator+filename+".csv").equals("1")) {
			FileFx.delete("bots"+File.separator+botName+File.separator+"aimlif"+File.separator+filename+".csv");
		}
		
		FileFx.create("bots"+File.separator+botName+File.separator+"aiml"+File.separator+filename, xml);
		
		return bots.get(botName).addCategoriesFromAIML(filename);
		
	}
	
	public static void remove_aiml_file(EnvManager env, String botName, String filename) throws Exception {
		
		if (exist_bot(botName).equals("0")) {
			throw new Exception("Sorry, the bot '"+botName+"' does not exist.");
		}
		
		if (!filename.endsWith(".aiml")) {
			throw new Exception("Sorry, the filename '"+filename+"' does not ends with '.aiml'.");
		}
		
		FileFx.delete("bots"+File.separator+botName+File.separator+"aiml"+File.separator+filename);
		
		if (FileFx.exist("bots"+File.separator+botName+File.separator+"aimlif"+File.separator+filename+".csv").equals("1")) {
			FileFx.delete("bots"+File.separator+botName+File.separator+"aimlif"+File.separator+filename+".csv");
		}
		
		//bots.get(botName).writeQuit();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static JSONObject execute(String botName, String user, String request) throws Exception {
		
		Chat chatSession = new Chat(bots.get(botName));
		
		JSONObject result = new JSONObject();
		result.put("that", ""+((History) chatSession.thatHistory.get(0)).get(0));
		result.put("topic", chatSession.predicates.get("topic"));
		result.put("response", chatSession.multisentenceRespond(request));
		result.put("customerId", chatSession.customerId);
		result.put("bot", chatSession.bot.name);

		return result;
		
	}
 
    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        
        return path;
    }

}
