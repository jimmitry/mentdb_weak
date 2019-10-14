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

package re.jpayet.mentdb.core.entity.symbol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record;
import re.jpayet.mentdb.core.db.basic.BasicNode;
import re.jpayet.mentdb.core.db.file.experience.ExperienceManagerL1;
import re.jpayet.mentdb.core.entity.concentration.ConcentrationManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.tools.Misc;

public class SymbolManager {
	
	//Add a new symbol
	@SuppressWarnings("unchecked")
	public static void create(long sessionId, String symbolName, String lang) throws Exception {
		
		symbolName = "S["+symbolName+"]";
		
		if (symbolName.length()!=4) {
			
			throw new Exception("Sorry, the symbol <"+symbolName+"> is not valid [required and char(1)].");
			
		}
		
		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, symbolName);
		
		if (rec!=null) {
			
			//Add fields
			
			JSONObject langObj = (JSONObject) rec.get("fl");
			
			if (((JSONObject) rec.get("fl")).containsKey(lang)) {
				
				throw new Exception("Sorry, the symbol <"+symbolName+"> already exist in "+lang+".");
				
			} else {
				
				JSONObject obj = new JSONObject();
				obj.put("ftl", null); //First tab link
				obj.put("ltl", null); //Last tab link
				langObj.put(lang, obj);
				
				Record.update(sessionId, symbolName, rec.toJSONString());
				
			}
			
		} else {
			
			//Build the value
			BasicNode bs = new BasicNode(symbolName, BasicNode.SYMBOL_TYPE, lang);
			
			//Add the record
			Record.add(sessionId, symbolName, bs.dataNode.toJSONString());
			
		}
		
	}
	
	
	
	
	
	//Show languages
	@SuppressWarnings("unchecked")
	public static JSONArray showLanguages(long sessionId, String symbolName) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		symbolName = "S["+symbolName+"]";
		
		if (symbolName.length()!=4) {
			
			throw new Exception("Sorry, the symbol <"+symbolName+"> is not valid [required and char(1)].");
			
		}
		
		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, symbolName);
		
		if (rec==null) {
			
			throw new Exception("Sorry, the symbol "+symbolName+" does not exist.");
			
		} else {
			
			JSONObject langObj = (JSONObject) rec.get("fl");
			
			for(Object o : langObj.keySet()) {
				
				result.add((String) o);
				
			}
			
		}
		
		return result;
		
	}
	
	//Get the first symbol tab link
	public static String firstTabLink(long sessionId, String symbol, String lang) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		return ExperienceManagerL1.getFirstTabLink(sessionId, "S["+symbol+"]", lang, true);

	}
	
	//Get the last symbol tab link
	public static String lastTabLink(long sessionId, String symbol, String lang) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		return ExperienceManagerL1.getLastTabLink(sessionId, "S["+symbol+"]", lang, true);

	}
	
	//Get a specific symbol tab links
	public static String getTabLink(long sessionId, String symbol, int position, String lang) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		if (position>=ConcentrationManager.getConcentrationDepth("C[symbol]")) {

			throw new Exception("Sorry, the position > concentration depth.");

		}
		
		String result = "";
		
		//Get the first
		String tl = ExperienceManagerL1.getFirstTabLink(sessionId, "S["+symbol+"]", lang, true);
		
		int index = 0;
		
		//Add only if null
		if (tl!=null) {
			
			if (position==index) {
				result = tl;
			}
			
			//Get the down node
			tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
			int i = 0;
			
			while(tl!=null && i<ConcentrationManager.getConcentrationDepth("C[symbol]")) {
				
				index++;
				
				if (position==index) {
					result = tl;
				}
				
				i++;
				
				//Get the next down node
				tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
				
			}
			
		} else {
			
			throw new Exception("Sorry, not found tab link for the symbol S["+symbol+"].");
			
		}
		
		if (result.equals("")) throw new Exception("Sorry, not found tab link at the position "+position+".");
		else return result;

	}
	
	//Show symbol tab links
	@SuppressWarnings("unchecked")
	public static JSONArray showTabLinks(long sessionId, String symbol, String lang, int depth) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		JSONArray result = new JSONArray();
		
		//Get the first
		String tl = ExperienceManagerL1.getFirstTabLink(sessionId, "S["+symbol+"]", lang, true);
		
		//Add only if null
		if (tl!=null) {
			
			result.add(tl);
			
			//Get the down node
			tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
			int i = 0;
			
			while(tl!=null && i+1<depth) {
				
				result.add(tl);
				
				i++;
				
				//Get the next down node
				tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
				
			}
			
		}
		
		return result;

	}
	
	//Show symbol show perception tab links
	@SuppressWarnings("unchecked")
	public static JSONArray showPerceptionTabLinks(long sessionId, String symbol, String lang) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		int nodePerception = Integer.parseInt(""+rec.get("w"));
		
		JSONArray result = new JSONArray();
		
		//Get the first
		String tl = ExperienceManagerL1.getFirstTabLink(sessionId, "S["+symbol+"]", lang, true);
		
		//Add only if null
		if (tl!=null) {
			
			JSONObject obj = new JSONObject();
			obj.put("k", tl);
			
			JSONObject bd = Record.getNode(sessionId, tl);
			int perception = Integer.parseInt(""+bd.get("w"));

			obj.put("nw", nodePerception);
			obj.put("w", perception);
			
			if (nodePerception==0) obj.put("v", 0D);
			else obj.put("v", Double.parseDouble(""+perception)/Double.parseDouble(""+nodePerception)*100);
			
			result.add(obj);
			
			//Get the down node
			tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
			int i = 0;
			
			while(tl!=null && i+1<ConcentrationManager.getConcentrationDepth("C[symbol]")) {
				
				obj = new JSONObject();
				obj.put("k", tl);
				
				bd = Record.getNode(sessionId, tl);
				perception = Integer.parseInt(""+bd.get("w"));

				obj.put("nw", nodePerception);
				obj.put("w", perception);
				
				if (nodePerception==0) obj.put("v", 0D);
				else obj.put("v", Double.parseDouble(""+perception)/Double.parseDouble(""+nodePerception)*100);
				
				result.add(obj);
				
				i++;
				
				//Get the next down node
				tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
				
			}
			
		}
		
		return result;

	}
	
	//Show symbol tab links
	@SuppressWarnings("unchecked")
	public static String showWords(long sessionId, String symbol, String lang) throws Exception {

		if (symbol.length()==0) {

			throw new Exception("Sorry, the symbol "+symbol+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, "S["+symbol+"]");

		if (rec==null) {

			throw new Exception("Sorry, the symbol "+symbol+" does not exist.");

		}
		
		JSONArray result = new JSONArray();
		
		//Get the first
		String tl = ExperienceManagerL1.getFirstTabLink(sessionId, "S["+symbol+"]", lang, true);
		
		//Add only if null
		if (tl!=null) {
			
			result.add(StringFx.hex_to_str(Misc.atom(Misc.atom(Misc.atom(tl, 2, "\\["), 1, "\\]"), 1, " ")));
			
			//Get the down node
			tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
			int i = 0;
			
			while(tl!=null && i+1<ConcentrationManager.getConcentrationDepth("C[symbol]")) {
				
				result.add(StringFx.hex_to_str(Misc.atom(Misc.atom(Misc.atom(tl, 2, "\\["), 1, "\\]"), 1, " ")));
				
				i++;
				
				//Get the next down node
				tl = ExperienceManagerL1.getDownTabLink(sessionId, "S["+symbol+"]", tl, lang, true);
				
			}
			
		}
		
		return result.toJSONString();

	}

	//Stimulate a tab link
	public static void stimulate(long sessionId, String tabLinkId) throws Exception {

		if (tabLinkId.length()==0) {

			throw new Exception("Sorry, the tab link id "+tabLinkId+" is not valid [required].");

		}

		//Try to get the record
		JSONObject rec = Record.getNode(sessionId, tabLinkId);

		if (rec==null) {

			throw new Exception("Sorry, the tab link id "+tabLinkId+" does not exist.");

		} else {
			
			int perception = Integer.parseInt(""+rec.get("w"));
			
			int position = Integer.parseInt(Misc.atom(tabLinkId, 2, " "));
			String wordName = StringFx.hex_to_str(Misc.atom(tabLinkId, 1, " ").substring(3));
			String lang = Misc.atom(tabLinkId, 3, " ").substring(0, 2);
			String currentLetter = wordName.substring(position, position+1);
			ExperienceManagerL1.deleteNodeTabLink(sessionId, "S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+position+" "+lang+"]", lang);
			ExperienceManagerL1.addNodeTabLink(sessionId, "S["+currentLetter+"]", "TL["+StringFx.str_to_hex(wordName)+" "+position+" "+lang+"]", lang, "symbol", false, false, perception+1);
		
		}

	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject pearlNecklaceSymbols(long sessionId, String symbolList) throws Exception {
		
		//Initialization
		JSONObject result = new JSONObject();
		JSONObject nodes = new JSONObject();result.put("n", nodes);
		JSONArray edges = new JSONArray();result.put("e", edges);
		int[] autoincrement = {0};

		String symbol_last_node_id = "";
		int symbol_last_id = 0;
		
		int concentration = ConcentrationManager.getConcentrationDepth("C[graph-symbol]");

		//Initialization
		for(int s=0;s<symbolList.length();s++) {
			
			//Get the current symbol
			String symbolName = symbolList.substring(s, s+1);
			int id_symbol = autoincrement[0]++;
			
			nodes.put("S"+id_symbol+"_"+symbolName, "S["+symbolName+"]");
			
			if (s>0) {
				
				JSONObject e = new JSONObject();
				e.put("k", "#"+id_symbol+"_"+symbol_last_id);
				e.put("n1", symbol_last_node_id);
				e.put("n2", "S"+id_symbol+"_"+symbolName);
				edges.add(e);
				
			}
			
			symbol_last_node_id = "S"+id_symbol+"_"+symbolName;
			symbol_last_id = id_symbol;
			
			//Get all languages
			JSONArray langs = SymbolManager.showLanguages(sessionId, symbolName);
			
			for(int i=0;i<langs.size();i++) {
				
				//Get the current language
				String lang = (String) langs.get(i);
				int id_lang = autoincrement[0]++;
				
				nodes.put("L"+id_lang+"_"+lang, "L["+lang+"]");
				
				JSONObject e = new JSONObject();
				e.put("k", id_symbol+"_"+id_lang);
				e.put("n1", "S"+id_symbol+"_"+symbolName);
				e.put("n2", "L"+id_lang+"_"+lang);
				edges.add(e);
				
				String last_node_id = "L"+id_lang+"_"+lang;
				int last_id = id_lang;
				
				JSONArray list = SymbolManager.showTabLinks(sessionId, symbolName, lang, concentration);
				for(int j=0;j<list.size();j++) {
					
					String wordName = StringFx.hex_to_str(Misc.atom((list.get(j)+""), 1, " ").substring(3));
					int id_word = autoincrement[0]++;
					
					nodes.put("W"+id_word+"_"+lang+"_"+wordName, wordName);
					e = new JSONObject();
					e.put("k", last_id+"_"+id_word);
					e.put("n1", last_node_id);
					e.put("n2", "W"+id_word+"_"+lang+"_"+wordName);
					edges.add(e);
					
					last_node_id = "W"+id_word+"_"+lang+"_"+wordName;
					last_id = id_word;
					
				}
				
			}
			
		}
		
		return result;
		
	}

}
