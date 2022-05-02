package re.jpayet.mentdb.ext.se;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.ext.fx.DateFx;
import re.jpayet.mentdb.ext.tools.Misc;

public class IndexAI {
	
	public static HashMap<String, Database> all_databases = new HashMap<String, Database>();
	
	public static final int NB_WORDS_BY_SYMBOL = 2500;

	public static Vector<String> write_orders = new Vector<String>();
	public static ConcurrentHashMap<Long, String> session_in_used = new ConcurrentHashMap<Long, String>();
	public static boolean locked = false;
	
	@SuppressWarnings("unchecked")
	public static JSONArray execute(long sessionId, String ordersString) throws Exception {
		
		try {
			
			JSONParser jp = new JSONParser();
			
			JSONArray orders = (JSONArray) jp.parse(ordersString);
			
			JSONArray result = new JSONArray();
			
			//Wait...
			while (wait_or_cancel(sessionId)) {
				
			}
			
			for(int i=0;i<orders.size();i++) {
				
				JSONObject order = (JSONObject) orders.get(i);

				String type = (String) order.get("t");
				String db_key = (String) order.get("db");
				Database db = all_databases.get(db_key);
				if (db==null) {
					all_databases.put(db_key, new Database());
					db = all_databases.get(db_key);
					db.id = db_key;
				}
				
				switch (type) {
				case "CITIES":

					List<Byte> jsonValues = new ArrayList<Byte>();
					for(Byte o: db.cityNames.keySet()) {
						jsonValues.add(o);
					}
					Collections.sort(jsonValues);
					for(Byte o: jsonValues) {
						JSONObject obj = new JSONObject();
						obj.put("k", o);
						obj.put("v", db.cityNames.get(o));
						long nb = 0;
						if (db.companies_by_city.containsKey(o)) {
							nb+=db.companies_by_city.get(o).size();
						}
						obj.put("n", nb);
						result.add(obj);
					}
					
					break;
				case "ZONES":
					String lang = (String) order.get("l");
					if (lang.equals("fr")) {
						jsonValues = new ArrayList<Byte>();
						for(Byte o: db.zoneNamesFr.keySet()) {
							jsonValues.add(o);
						}
						Collections.sort(jsonValues);
						for(Byte o: jsonValues) {
							JSONObject obj = new JSONObject();
							obj.put("k", o);
							obj.put("v", db.zoneNamesFr.get(o));
							result.add(obj);
						}
					} else {
						jsonValues = new ArrayList<Byte>();
						for(Byte o: db.zoneNamesEn.keySet()) {
							jsonValues.add(o);
						}
						Collections.sort(jsonValues);
						for(Byte o: jsonValues) {
							JSONObject obj = new JSONObject();
							obj.put("k", o);
							obj.put("v", db.zoneNamesEn.get(o));
							result.add(obj);
						}
					}
					break;
				case "ADD_CITY":
					byte city = Byte.parseByte(""+order.get("cy"));
					String name = (String) order.get("n");
					add_city(db, city, name);
					break;
				case "ADD_ZONE":
					byte zone = Byte.parseByte(""+order.get("z"));
					String nameEn = (String) order.get("nEn");
					String nameFr = (String) order.get("nFr");
					add_zone(db, zone, nameEn, nameFr);
					break;
				case "RESET_LOCKERS":
					reset_lockers();
					break;
				case "ADD_STIM_REL":
					long r = Long.parseLong(""+order.get("r"));
					add_stim_rel(r);
					break;
				case "ADD_STIM_COMPANY":
					long company = Long.parseLong(""+order.get("c"));
					add_stim_company(db, company);
					break;
				case "DEL_COMPANY":
					write_orders.add(ordersString);
					break;
				case "ADD_DATA_COMPANY":
					write_orders.add(ordersString);
					break;
				case "ADD_DATA_ENTITY":
					write_orders.add(ordersString);
					break;
				case "FIND":
					lang = (String) order.get("l");
					city = Byte.parseByte(""+order.get("cy"));
					zone = Byte.parseByte(""+order.get("z"));
					byte where = Byte.parseByte(""+order.get("cat"));
					byte intensity = Byte.parseByte(""+order.get("int"));
					String text = (String) order.get("txt");
					int depth = Integer.parseInt(""+order.get("d"));
					int page = Integer.parseInt(""+order.get("p"));
					result.add(find(db, lang, city, zone, where, intensity, text, depth, page));
					break;
				case "STAT":
					result.add(stats(db));
					break;
				}
				
			}
			
			session_in_used.remove(sessionId);
			
			return result;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			re.jpayet.mentdb.ext.log.Log.trace("Error Index-AI execute: "+e.getMessage());
			
			try {session_in_used.remove(sessionId);} catch(Exception f) {};
			
			throw e;
			
		}
		
	}
	
	public static int process() throws Exception {
		
		try {
			
			//Cancel...
			if (!wait_or_cancel(-1)) {
				
				String ordersString = write_orders.remove(0);
				
				JSONParser jp = new JSONParser();
				
				JSONArray orders = (JSONArray) jp.parse(ordersString);
				
				for(int i=0;i<orders.size();i++) {
					
					JSONObject order = (JSONObject) orders.get(i);
					
					String type = (String) order.get("t");
					String db_key = (String) order.get("db");
					Database db = all_databases.get(db_key);
					
					switch (type) {
					case "STIM_REL":
						long r = Long.parseLong(""+order.get("r"));
						stim_rel(db, r);
						break;
					case "DEL_COMPANY":
						byte city = Byte.parseByte(""+order.get("cy"));
						long company = Long.parseLong(""+order.get("c"));
						del_company(db, city, company);
						break;
					case "ADD_DATA_COMPANY":
						city = Byte.parseByte(""+order.get("cy"));
						byte zone = Byte.parseByte(""+order.get("z"));
						company = Long.parseLong(""+order.get("c"));
						String name = (String) order.get("n");
						String descEn = (String) order.get("dEn");
						String descFr = (String) order.get("dFr");
						String sentenceEn = (String) order.get("sEn");
						String sentenceFr = (String) order.get("sFr");
						String result = (String) order.get("res");
						add_data_company(db, city, zone, company, name, descEn, descFr, sentenceEn, sentenceFr, result);
						break;
					case "ADD_DATA_ENTITY":
						city = Byte.parseByte(""+order.get("cy"));
						zone = Byte.parseByte(""+order.get("z"));
						company = Long.parseLong(""+order.get("c"));
						String entityEn = (String) order.get("eEn");
						String entityFr = (String) order.get("eFr");
						sentenceEn = (String) order.get("sEn");
						sentenceFr = (String) order.get("sFr");
						result = (String) order.get("res");
						long entity_id = Long.parseLong(""+order.get("eid"));
						byte where = Byte.parseByte(""+order.get("wh"));
						byte intensity = Byte.parseByte(""+order.get("int"));
						String dj1 = (String) order.get("dj1");
						String dj2 = (String) order.get("dj2");
						String dj3 = (String) order.get("dj3");
						String dj4 = (String) order.get("dj4");
						String dj5 = (String) order.get("dj5");
						String dh1 = (String) order.get("dh1");
						String dh2 = (String) order.get("dh2");
						String dh3 = (String) order.get("dh3");
						String dh4 = (String) order.get("dh4");
						String dh5 = (String) order.get("dh5");
						String dt_depart = (String) order.get("dt_depart");
						String dt_fin = (String) order.get("dt_fin");
						add_data_entity(db, entity_id, city, zone, company, where, intensity, entityEn, entityFr, sentenceEn, sentenceFr, result
								, dj1, dh1
								, dj2, dh2
								, dj3, dh3
								, dj4, dh4
								, dj5, dh5
								, dt_depart, dt_fin);
						break;
						
					}
					
				}
				
				locked = false;
				
				return write_orders.size();
				
			} else return 0;
			
		} catch (Exception e) {
			
			re.jpayet.mentdb.ext.log.Log.trace("Error Index-AI process: "+e.getMessage());
			
			locked = false;
			
			return 0;
			
		}
		
	}
	
	public static synchronized boolean wait_or_cancel(long sessionId) throws Exception {
		
		if (sessionId>-1) {
			
			session_in_used.put(sessionId, "");
			
			if (locked) {
				
				return true;
				
			} else {
				
				return false;
			}
			
		} else {
			
			if (session_in_used.size()>0) {
				return true;
			} else if (locked) {
				return true;
			} else {
				
				if (write_orders.size()>0) {
					
					locked = true;
					
					return false;
					
				} else {
					return true;
				}
				
			}
			
		}
		
	}
	
	public static void reset_lockers() throws Exception {
		
		session_in_used = new ConcurrentHashMap<Long, String>();
		locked = false;
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add_stim_rel(long r) throws Exception {
		
		JSONArray array = new JSONArray();
		
		JSONObject o = new JSONObject();
		o.put("t", "STIM_REL");
		o.put("r", r);
		
		array.add(o);
		
		write_orders.add(array.toJSONString());
		
	}
	
	@SuppressWarnings("unchecked")
	public static void add_stim_company(Database db, long company) throws Exception {
		
		if (db.companies.containsKey(company)) {
		
			Vector<Long> v_rel = db.companies.get(company);
			for(int i=0;i<v_rel.size();i++) {
				
				JSONArray array = new JSONArray();
				
				JSONObject o = new JSONObject();
				o.put("t", "STIM_REL");
				o.put("r", v_rel.get(i));
				
				array.add(o);
				
				write_orders.add(array.toJSONString());
				
			}
			
		}
		
	}
	
	public static void stim_rel(Database db, long r) throws Exception {
		
		if (db.relations.containsKey(r)) {
		
			Relation rel = db.relations.get(r);
			String[] sp = rel.sentence.split(" ");
			for(int i=0;i<sp.length;i++) {
				
				String word = sp[i];
				
				if (rel.is_en) {
					
					db.pilesEn.get(word).removeElement(r);
					db.pilesEn.get(word).add(0, r);
					
					Symbol.stimulateEn(db, word);
					
				} else {
					
					db.pilesFr.get(word).removeElement(r);
					db.pilesFr.get(word).add(0, r);
					
					Symbol.stimulateFr(db, word);
					
				}
				
			}

			db.zones_relations.get(rel.zone).removeElement(rel);
			db.zones_relations.get(rel.zone).add(0, rel);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject find(Database db, String lang, byte city, byte zone, byte where, byte intensity, String text, int depth, int page) throws Exception {
		
		lang = lang.toLowerCase();

		String sysdate = DateFx.sysdate();
		String systime = DateFx.systime();
		
		text = StringUtils.normalizeSpace(text.toLowerCase().replace("?", " ").replace(".", " ").replace(",", " ").replace(";", " ").replace("!", " ").replace(":", " ")
				.replace("\"", " ").replace("'", " ").replace("-", " ").replace("_", " ").replace("(", " ").replace(")", " ")
				.replace("{", " ").replace("}", " ").replace("[", " ").replace("]", " ")
				.replace("ç", "c").replace("æ", "oe").replace("œ", "oe").replace("ñ", "n")
				.replace("á", "a").replace("à", "a").replace("â", "a").replace("ä", "a")
				.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
				.replace("í", "i").replace("ì", "i").replace("î", "i").replace("ï", "i")
				.replace("ò", "o").replace("ó", "o").replace("ô", "o").replace("ö", "o")
				.replace("ú", "u").replace("ù", "u").replace("û", "u").replace("ü", "u")
				.replace("ý", "u").replace("ÿ", "u"));
		
		if (text.equals("")) {
			
			if (zone==-1) {

				Vector<Byte> keys = new Vector<Byte>();
				Vector<Integer> keys_pos = new Vector<Integer>();
				for(byte z : db.zones_relations.keySet()) {
					keys.add(z);
					keys_pos.add(0);
				}
				
				Vector<Relation> vec = new Vector<Relation>();
				
				int i = 0, current_pos = 0;
				while (i<depth && keys.size()>0) {
					
					byte current_zone = keys.get(current_pos);
					
					if (db.zones_relations.get(current_zone).size()>keys_pos.get(current_pos)) {
						
						vec.add(db.zones_relations.get(current_zone).get(keys_pos.get(current_pos)));
						
						keys_pos.set(current_pos, keys_pos.get(current_pos)+1);
						
						current_pos++;
					} else {
						keys_pos.remove(current_pos);
						keys.remove(current_pos);
					}
					
					if (current_pos>=keys.size()) {
						current_pos = 0;
					}
					
					i++;
				}
				
				if (vec.size()==0) {
					
					JSONObject result = new JSONObject();
					JSONArray result_list = new JSONArray();
					result.put("result", result_list);
					result.put("nb", 0);
					
					return result;
					
				}
				
				LinkedHashMap<Long, Double> found = new LinkedHashMap<Long, Double>();
				boolean b_en = true;
				if (lang.equals("fr")) {
					b_en = false;
				}
				for(i=0;i<vec.size() && i<=depth;i++) {
					
					Relation rel = vec.get(i);
					if (rel.is_en==b_en && (city==-1 || city==rel.city) && (zone==-1 || zone==rel.zone)) {
						found.put(rel.id, (depth-i)+0.0);
					}
					
				}
				
				HashMap<Long, Entry<Long, Double>> company_already_used = new HashMap<Long, Entry<Long, Double>>();
				HashMap<Long, Entry<Long, Double>> entity_already_used = new HashMap<Long, Entry<Long, Double>>();
				ArrayList<Long> to_remove = new ArrayList<Long>();
				
				for(long id : found.keySet()) {
					
					Relation r = db.relations.get(id);
					
					Map.Entry<Long, Double> e = new AbstractMap.SimpleEntry<Long, Double>(id, found.get(id));
					
					if (r.entity==null) {
						
						if (!company_already_used.containsKey(r.company)) {
							company_already_used.put(r.company, e);
						} else {
							
							if (e.getValue()>company_already_used.get(r.company).getValue()) {
								to_remove.add(company_already_used.get(r.company).getKey());
								company_already_used.put(r.company, e);
							} else {
								to_remove.add(id);
							}
							
						}
						
					} else {
						
						if (where>-1 && where!=r.entity.where) {
							to_remove.add(id);
							entity_already_used.put(r.entity.id, e);
						} else {
							
							if (intensity>-1 && intensity!=r.entity.intensity) {
								to_remove.add(id);
								entity_already_used.put(r.entity.id, e);
							} else {
						
								if (!entity_already_used.containsKey(r.entity.id)) {
									entity_already_used.put(r.entity.id, e);
								} else {
									if (e.getValue()>entity_already_used.get(r.entity.id).getValue()) {
										to_remove.add(entity_already_used.get(r.entity.id).getKey());
										entity_already_used.put(r.entity.id, e);
									} else {
										to_remove.add(id);
									}
								}
								
							}
						
						}
					
					}
					
				}
				
				for(long id : to_remove) {
					found.remove(id);
					
				}
				List<Map.Entry<Long, Double>> list = new LinkedList<>( found.entrySet() );
				
				Collections.sort( list, new Comparator<Map.Entry<Long, Double>>() {
					@Override
					public int compare( Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2 )
					{
						if (o2.getValue()>o1.getValue()) {
							return 1;
						} else if (o2.getValue()<o1.getValue()) {
							return -1;
						} else {
							return 0;
						}
					}
				} );
				
				JSONObject result = new JSONObject();
				JSONArray result_list = new JSONArray();
				JSONObject words_search = new JSONObject();
				result.put("result", result_list);
				result.put("nb", list.size());
				result.put("w", words_search);
				result.put("txt", text);

				Random ran = new Random();
				Vector<Long> keys_for_random = new Vector<Long>();
				for(i=((page-1)*10);i<(((page-1)*10)+10) && i<list.size();i++) {
					
					keys_for_random.add(list.get(i).getKey());
					
				}
				
				while (keys_for_random.size()>0) {
					
					JSONObject row = new JSONObject();
					
					long id = keys_for_random.remove(ran.nextInt(keys_for_random.size()));
					
					Relation r = db.relations.get(id);
					row.put("id", id);
					row.put("cy_id", r.city);
					row.put("cy", db.cityNames.get(r.city));
					row.put("z_id", r.zone);
					if (lang.equals("fr")) row.put("z", db.zoneNamesFr.get(r.zone));
					else row.put("z", db.zoneNamesEn.get(r.zone));
					row.put("c_id", r.company);
					row.put("c", db.companyNames.get(r.company));
					if (lang.equals("fr")) row.put("cd", db.companyDescriptionsFr.get(r.company));
					else row.put("cd", db.companyDescriptionsEn.get(r.company));
					row.put("s", r.sentence);
					row.put("res", r.result.result);
					row.put("v", found.get(id));
					
					if (r.entity!=null) {
						
						if (lang.equals("fr")) row.put("e", r.entity.fr);
						else row.put("e", r.entity.en);
						
						row.put("e_id", r.entity.id);
						row.put("e_int", r.entity.intensity);
						row.put("e_cat", r.entity.where);
						
					}
					
					result_list.add(row);
					
				}
				
				return result;
				
			} else {
				
				Vector<Relation> vec = db.zones_relations.get(zone);
				
				if (vec==null || vec.size()==0) {
					
					JSONObject result = new JSONObject();
					JSONArray result_list = new JSONArray();
					result.put("result", result_list);
					result.put("nb", 0);
					
					return result;
					
				}
				
				LinkedHashMap<Long, SEValue> found = new LinkedHashMap<Long, SEValue>();
				boolean b_en = true;
				if (lang.equals("fr")) {
					b_en = false;
				}
				for(int i=0;i<vec.size() && i<=depth;i++) {
					
					Relation rel = vec.get(i);
					if (rel.is_en==b_en && (city==-1 || city==rel.city) && (zone==-1 || zone==rel.zone)) {
						if (rel.entity==null) {
							found.put(rel.id, new SEValue((depth-i)+0.0, 0));
						} else {
							found.put(rel.id, new SEValue((depth-i)+0.0, vec.get(i).entity.get_dispo(sysdate, systime)));
						}
					}
					
				}
				
				HashMap<Long, Entry<Long, Double>> company_already_used = new HashMap<Long, Entry<Long, Double>>();
				HashMap<Long, Entry<Long, Double>> entity_already_used = new HashMap<Long, Entry<Long, Double>>();
				ArrayList<Long> to_remove = new ArrayList<Long>();
				
				for(long id : found.keySet()) {
					
					Relation r = db.relations.get(id);
					
					Map.Entry<Long, Double> e = new AbstractMap.SimpleEntry<Long, Double>(id, found.get(id).depth);
					
					if (r.entity==null) {
						
						if (!company_already_used.containsKey(r.company)) {
							company_already_used.put(r.company, e);
						} else {
							
							if (e.getValue()>company_already_used.get(r.company).getValue()) {
								to_remove.add(company_already_used.get(r.company).getKey());
								company_already_used.put(r.company, e);
							} else {
								to_remove.add(id);
							}
							
						}
						
					} else {
						
						if (where>-1 && where!=r.entity.where) {
							to_remove.add(id);
							entity_already_used.put(r.entity.id, e);
						} else {
							
							if (intensity>-1 && intensity!=r.entity.intensity) {
								to_remove.add(id);
								entity_already_used.put(r.entity.id, e);
							} else {
						
								if (!entity_already_used.containsKey(r.entity.id)) {
									entity_already_used.put(r.entity.id, e);
								} else {
									if (e.getValue()>entity_already_used.get(r.entity.id).getValue()) {
										to_remove.add(entity_already_used.get(r.entity.id).getKey());
										entity_already_used.put(r.entity.id, e);
									} else {
										to_remove.add(id);
									}
								}
								
							}
							
						}
					
					}
					
				}
				
				for(long id : to_remove) {
					found.remove(id);
				}
				List<Map.Entry<Long, SEValue>> list = new LinkedList<>( found.entrySet() );
				
				Collections.sort( list, new Comparator<Map.Entry<Long, SEValue>>() {
					@Override
					public int compare( Map.Entry<Long, SEValue> o1, Map.Entry<Long, SEValue> o2 )
					{
						if (o1.getValue().dispo>o2.getValue().dispo) {
							return 1;
						} else if (o1.getValue().dispo<o2.getValue().dispo) {
							return -1;
						} else {
							if (o2.getValue().depth>o1.getValue().depth) {
								return 1;
							} else if (o2.getValue().depth<o1.getValue().depth) {
								return -1;
							} else {
								return 0;
							}
						}
					}
				} );
				
				JSONObject result = new JSONObject();
				JSONArray result_list = new JSONArray();
				JSONObject words_search = new JSONObject();
				result.put("result", result_list);
				result.put("nb", list.size());
				result.put("w", words_search);
				result.put("txt", text);

				Random ran = new Random();
				Vector<Long> keys_for_random = new Vector<Long>();
				for(int i=((page-1)*10);i<(((page-1)*10)+10) && i<list.size();i++) {
					
					keys_for_random.add(list.get(i).getKey());
					
				}
				
				while (keys_for_random.size()>0) {
					
					JSONObject row = new JSONObject();
					
					long id = 0;
					if (zone==1) {
						id = keys_for_random.remove(0);
					} else {
						id = keys_for_random.remove(ran.nextInt(keys_for_random.size()));
					}
					
					Relation r = db.relations.get(id);
					row.put("id", id);
					row.put("cy_id", r.city);
					row.put("cy", db.cityNames.get(r.city));
					row.put("z_id", r.zone);
					if (lang.equals("fr")) row.put("z", db.zoneNamesFr.get(r.zone));
					else row.put("z", db.zoneNamesEn.get(r.zone));
					row.put("c_id", r.company);
					row.put("c", db.companyNames.get(r.company));
					if (lang.equals("fr")) row.put("cd", db.companyDescriptionsFr.get(r.company));
					else row.put("cd", db.companyDescriptionsEn.get(r.company));
					row.put("s", r.sentence);
					row.put("res", r.result.result);
					row.put("v", found.get(id).depth);
					row.put("disp", found.get(id).dispo);
					
					if (r.entity!=null) {
						
						if (lang.equals("fr")) row.put("e", r.entity.fr);
						else row.put("e", r.entity.en);
						
						row.put("e_id", r.entity.id);
						row.put("e_int", r.entity.intensity);
						row.put("e_cat", r.entity.where);
						
					}
					
					result_list.add(row);
					
				}
				
				return result;
				
			}
			
		} else {
		
			String[] txt = text.split(" ");
			ArrayList<String> words_l_0 = new ArrayList<String>();
			ArrayList<String> words_l_sup = new ArrayList<String>();
			
			for(int i=0;i<txt.length;i++) {
				
				String word = txt[i];
				
				List<Map.Entry<String, Integer>> list = null;
				
				if (lang.equals("fr")) list = Symbol.findWordFr(db, word);
				else list = Symbol.findWordEn(db, word);
				
				for(int j=0;j<list.size();j++) {
					
					Entry<String, Integer> e = list.get(j);
					
					if (e.getValue()>3) {
						
						break;
						
					}
					
					if (e.getValue()>0) {
						
						words_l_sup.add(e.getKey());
						
						break;
						
					}
					
				}
				
				if (lang.equals("fr")) {
				
					if (db.pilesFr.containsKey(word)) {
						
						if (!words_l_0.contains(word)) words_l_0.add(word);
						
					} else {
						
						if (list.size()>0 && list.get(0).getValue()<=2) {
							
							word = list.get(0).getKey();
							if (!words_l_0.contains(word)) words_l_0.add(word);
							
						}
						
					}
				
				} else {
					
					if (db.pilesEn.containsKey(word)) {
						
						if (!words_l_0.contains(word)) words_l_0.add(word);
						
					} else {
						
						if (list.size()>0 && list.get(0).getValue()<=2) {
							
							word = list.get(0).getKey();
							if (!words_l_0.contains(word)) words_l_0.add(word);
							
						}
						
					}
					
				}
				
			}
			
			if (words_l_0.size()>0) {
				
				ConcurrentHashMap<Long, Double> found = new ConcurrentHashMap<Long, Double>();
				ConcurrentHashMap<Long, Integer> found_depth = new ConcurrentHashMap<Long, Integer>();
				
				//Initialization
				ExecutorService executor_l_0 = Executors.newFixedThreadPool(words_l_0.size());
				
				//Create workers
				for(int i=0;i<words_l_0.size();i++) {
					
					if (lang.equals("fr")) executor_l_0.execute((Runnable) new SearchThreadFr(db, city, zone, words_l_0.get(i), depth, found, found_depth, text));
					else executor_l_0.execute((Runnable) new SearchThreadEn(db, city, zone, words_l_0.get(i), depth, found, found_depth, text));
		            
		        }
				
				if (words_l_sup.size()>0) {
	
					ExecutorService executor_l_sup = Executors.newFixedThreadPool(words_l_sup.size());
					//Create workers
					for(int i=0;i<words_l_sup.size();i++) {
						
						if (lang.equals("fr")) executor_l_sup.execute((Runnable) new SearchThreadFr(db, city, zone, words_l_sup.get(i), depth, found, found_depth, text));
						else executor_l_sup.execute((Runnable) new SearchThreadEn(db, city, zone, words_l_sup.get(i), depth, found, found_depth, text));
			            
			        }
					executor_l_sup.shutdown();
					while (!executor_l_sup.isTerminated()) {
			        }
					
				}
				
				//Wait
				executor_l_0.shutdown();
		        while (!executor_l_0.isTerminated()) {
		        }
				
				HashMap<Long, Entry<Long, Double>> company_already_used = new HashMap<Long, Entry<Long, Double>>();
				HashMap<Long, Entry<Long, Double>> entity_already_used = new HashMap<Long, Entry<Long, Double>>();
				ArrayList<Long> to_remove = new ArrayList<Long>();
				
				for(long id : found.keySet()) {
					
					found.put(id, found.get(id)+((depth-found_depth.get(id))/1000000.0));
					
					Relation r = db.relations.get(id);
					
					Map.Entry<Long, Double> e = new AbstractMap.SimpleEntry<Long, Double>(id, found.get(id));
					
					if (r.entity==null) {
						
						if (!company_already_used.containsKey(r.company)) {
							company_already_used.put(r.company, e);
						} else {
							
							if (e.getValue()>company_already_used.get(r.company).getValue()) {
								to_remove.add(company_already_used.get(r.company).getKey());
								company_already_used.put(r.company, e);
							} else {
								to_remove.add(id);
							}
							
						}
						
					} else {
						
						if (where>-1 && where!=r.entity.where) {
							to_remove.add(id);
							entity_already_used.put(r.entity.id, e);
						} else {
							
							if (intensity>-1 && intensity!=r.entity.intensity) {
								to_remove.add(id);
								entity_already_used.put(r.entity.id, e);
							} else {
						
								if (!entity_already_used.containsKey(r.entity.id)) {
									entity_already_used.put(r.entity.id, e);
								} else {
									if (e.getValue()>entity_already_used.get(r.entity.id).getValue()) {
										to_remove.add(entity_already_used.get(r.entity.id).getKey());
										entity_already_used.put(r.entity.id, e);
									} else {
										to_remove.add(id);
									}
								}
								
							}
						
						}
					
					}
					
				}
				
				for(long id : to_remove) {
					found.remove(id);
					
				}
				List<Map.Entry<Long, Double>> list = new LinkedList<>( found.entrySet() );
				
				Collections.sort( list, new Comparator<Map.Entry<Long, Double>>() {
					@Override
					public int compare( Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2 )
					{
						return o2.getValue().compareTo(o1.getValue());
					}
				} );
				
				JSONObject result = new JSONObject();
				JSONArray result_list = new JSONArray();
				JSONObject words_search = new JSONObject();
				for(int i=0;i<words_l_0.size();i++) {
					String w = words_l_0.get(i);
					if (!words_search.containsKey(w)) {
						words_search.put(w, "1");
					}
				}
				for(int i=0;i<words_l_sup.size();i++) {
					String w = words_l_sup.get(i);
					if (!words_search.containsKey(w)) {
						words_search.put(w, "1");
					}
				}
				result.put("result", result_list);
				result.put("nb", list.size());
				result.put("w", words_search);
				result.put("txt", text);
	
				for(int i=((page-1)*10);i<(((page-1)*10)+10) && i<list.size();i++) {
					
					JSONObject row = new JSONObject();
					
					long id = list.get(i).getKey();
					
					Relation r = db.relations.get(id);
					row.put("id", id);
					row.put("cy_id", r.city);
					row.put("cy", db.cityNames.get(r.city));
					row.put("z_id", r.zone);
					if (lang.equals("fr")) row.put("z", db.zoneNamesFr.get(r.zone));
					else row.put("z", db.zoneNamesEn.get(r.zone));
					row.put("c_id", r.company);
					row.put("c", db.companyNames.get(r.company));
					if (lang.equals("fr")) row.put("cd", db.companyDescriptionsFr.get(r.company));
					else row.put("cd", db.companyDescriptionsEn.get(r.company));
					row.put("s", r.sentence);
					row.put("res", r.result.result);
					row.put("v", list.get(i).getValue());
					
					if (r.entity!=null) {
						
						if (lang.equals("fr")) row.put("e", r.entity.fr);
						else row.put("e", r.entity.en);

						row.put("e_id", r.entity.id);
						row.put("e_int", r.entity.intensity);
						row.put("e_cat", r.entity.where);
						
					}
					
					result_list.add(row);
					
				}
				
				return result;
		        
			} else {
				
				JSONObject result = new JSONObject();
				JSONArray result_list = new JSONArray();
				result.put("result", result_list);
				result.put("nb", 0);
				
				return result;
				
			}
        
		}
		
	}
	
	public static void add_data_company(Database db, byte city, byte zone, Long company, String name, String descriptionEn, String descriptionFr, String sentenceEn, String sentenceFr, String result) throws Exception {

		descriptionEn = Misc.lrtrim(descriptionEn);
		descriptionFr = Misc.lrtrim(descriptionFr);
		
		db.companies.put(company, new Vector<Long>());
		db.companyNames.put(company, name);
		db.companyDescriptionsEn.put(company, descriptionEn);
		db.companyDescriptionsFr.put(company, descriptionFr);
		db.companyEntities.put(company, new Vector<Long>());
		
		if (!db.companies_by_city.containsKey(city)) {
			db.companies_by_city.put(city, new Vector<Long>());
		}
		db.companies_by_city.get(city).add(company);
		
		Vector<Long> v_rel = db.companies.get(company);

		sentenceEn = StringUtils.normalizeSpace((name+" "+descriptionEn+(!descriptionEn.endsWith(".") && !descriptionEn.endsWith(";") && !descriptionEn.endsWith("!")?".":"")+sentenceEn).toLowerCase().replace("&amp;", "and").replace("\"", " ").replace("'", " ").replace("-", " ").replace("_", " ").replace("(", " ").replace(")", " ")
				.replace("{", " ").replace("}", " ").replace("[", " ").replace("]", " ")
				.replace("ç", "c").replace("æ", "oe").replace("œ", "oe").replace("ñ", "n")
				.replace("á", "a").replace("à", "a").replace("â", "a").replace("ä", "a")
				.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
				.replace("í", "i").replace("ì", "i").replace("î", "i").replace("ï", "i")
				.replace("ò", "o").replace("ó", "o").replace("ô", "o").replace("ö", "o")
				.replace("ú", "u").replace("ù", "u").replace("û", "u").replace("ü", "u")
				.replace("ý", "u").replace("ÿ", "u"));

		sentenceFr = StringUtils.normalizeSpace((name+" "+descriptionFr+(!descriptionFr.endsWith(".") && !descriptionFr.endsWith(";") && !descriptionFr.endsWith("!")?".":"")+sentenceFr).toLowerCase().replace("&amp;", "et").replace("\"", " ").replace("'", " ").replace("-", " ").replace("_", " ").replace("(", " ").replace(")", " ")
				.replace("{", " ").replace("}", " ").replace("[", " ").replace("]", " ")
				.replace("ç", "c").replace("æ", "oe").replace("œ", "oe").replace("ñ", "n")
				.replace("á", "a").replace("à", "a").replace("â", "a").replace("ä", "a")
				.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
				.replace("í", "i").replace("ì", "i").replace("î", "i").replace("ï", "i")
				.replace("ò", "o").replace("ó", "o").replace("ô", "o").replace("ö", "o")
				.replace("ú", "u").replace("ù", "u").replace("û", "u").replace("ü", "u")
				.replace("ý", "u").replace("ÿ", "u"));
		
		int start = 0;
		int end = 0;
		
		Result res = new Result(result);
		
		while (start<sentenceEn.length()) {
			
			ArrayList<Integer> min = new ArrayList<Integer>();
			
			int pos = sentenceEn.indexOf("?", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(",", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(".", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(";", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf("!", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(":", start);
			if (pos>-1) min.add(pos);
			if (min.size()==0) min.add(-1);
			
			end = Collections.min(min);
			
			String s = null;
			
			if (end==-1) {
				s = sentenceEn.substring(start);
				start = sentenceEn.length();
			} else {
				s = sentenceEn.substring(start, end);
				start = end+1;
			}
			
			s = s.trim();
			
			add_relationEn(db, v_rel, city, zone, company, s, res);
			
		}

		start = 0;
		end = 0;
		
		while (start<sentenceFr.length()) {
			
			ArrayList<Integer> min = new ArrayList<Integer>();
			
			int pos = sentenceFr.indexOf("?", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(",", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(".", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(";", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf("!", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(":", start);
			if (pos>-1) min.add(pos);
			if (min.size()==0) min.add(-1);
			
			end = Collections.min(min);
			
			String s = null;
			
			if (end==-1) {
				s = sentenceFr.substring(start);
				start = sentenceFr.length();
			} else {
				s = sentenceFr.substring(start, end);
				start = end+1;
			}
			
			s = s.trim();
			
			add_relationFr(db, v_rel, city, zone, company, s, res);
			
		}
		
	}
	
	public static void add_data_entity(Database db, long entity_id, byte city, byte zone, long company, byte where, byte intensity, String entityEn, String entityFr, String sentenceEn, String sentenceFr, String result
			, String dispoj1, String dispoh1
			, String dispoj2, String dispoh2
			, String dispoj3, String dispoh3
			, String dispoj4, String dispoh4
			, String dispoj5, String dispoh5
			, String dt_depart, String dt_fin) throws Exception {
		
		Vector<Long> v_rel = db.companies.get(company);
		Vector<Long> v_ent = db.companyEntities.get(company);

		Entity ent = new Entity(entity_id, entityEn, entityFr, where, intensity
				, dispoj1, dispoh1
				, dispoj2, dispoh2
				, dispoj3, dispoh3
				, dispoj4, dispoh4
				, dispoj5, dispoh5
				, dt_depart, dt_fin);
		db.entities.put(entity_id, ent);
		v_ent.add(entity_id);
		
		String name = db.companyNames.get(company);

		sentenceEn = StringUtils.normalizeSpace((name+" "+entityEn+(!entityEn.endsWith(".") && !entityEn.endsWith(";") && !entityEn.endsWith("!")?".":"")+" "+sentenceEn).toLowerCase().replace("\"", " ").replace("'", " ").replace("-", " ").replace("_", " ").replace("(", " ").replace(")", " ")
				.replace("{", " ").replace("}", " ").replace("[", " ").replace("]", " ")
				.replace("ç", "c").replace("æ", "oe").replace("œ", "oe").replace("ñ", "n")
				.replace("á", "a").replace("à", "a").replace("â", "a").replace("ä", "a")
				.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
				.replace("í", "i").replace("ì", "i").replace("î", "i").replace("ï", "i")
				.replace("ò", "o").replace("ó", "o").replace("ô", "o").replace("ö", "o")
				.replace("ú", "u").replace("ù", "u").replace("û", "u").replace("ü", "u")
				.replace("ý", "u").replace("ÿ", "u"));

		sentenceFr = StringUtils.normalizeSpace((name+" "+entityFr+(!entityFr.endsWith(".") && !entityFr.endsWith(";") && !entityFr.endsWith("!")?".":"")+" "+sentenceFr).toLowerCase().replace("\"", " ").replace("'", " ").replace("-", " ").replace("_", " ").replace("(", " ").replace(")", " ")
				.replace("{", " ").replace("}", " ").replace("[", " ").replace("]", " ")
				.replace("ç", "c").replace("æ", "oe").replace("œ", "oe").replace("ñ", "n")
				.replace("á", "a").replace("à", "a").replace("â", "a").replace("ä", "a")
				.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
				.replace("í", "i").replace("ì", "i").replace("î", "i").replace("ï", "i")
				.replace("ò", "o").replace("ó", "o").replace("ô", "o").replace("ö", "o")
				.replace("ú", "u").replace("ù", "u").replace("û", "u").replace("ü", "u")
				.replace("ý", "u").replace("ÿ", "u"));

		int start = 0;
		int end = 0;
		
		Result res = new Result(result);
		
		while (start<sentenceEn.length()) {
			
			ArrayList<Integer> min = new ArrayList<Integer>();
			
			int pos = sentenceEn.indexOf("?", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(",", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(".", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(";", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf("!", start);
			if (pos>-1) min.add(pos);
			pos = sentenceEn.indexOf(":", start);
			if (pos>-1) min.add(pos);
			if (min.size()==0) min.add(-1);
			
			end = Collections.min(min);
			
			String s = null;
			
			if (end==-1) {
				s = sentenceEn.substring(start);
				start = sentenceEn.length();
			} else {
				s = sentenceEn.substring(start, end);
				start = end+1;
			}
			
			s = s.trim();
			
			add_relationEnEntity(db, v_rel, city, zone, company, ent, s, res);
			
		}

		start = 0;
		end = 0;
		
		while (start<sentenceFr.length()) {
			
			ArrayList<Integer> min = new ArrayList<Integer>();
			
			int pos = sentenceFr.indexOf("?", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(",", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(".", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(";", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf("!", start);
			if (pos>-1) min.add(pos);
			pos = sentenceFr.indexOf(":", start);
			if (pos>-1) min.add(pos);
			if (min.size()==0) min.add(-1);
			
			end = Collections.min(min);
			
			String s = null;
			
			if (end==-1) {
				s = sentenceFr.substring(start);
				start = sentenceFr.length();
			} else {
				s = sentenceFr.substring(start, end);
				start = end+1;
			}
			
			s = s.trim();
			
			add_relationFrEntity(db, v_rel, city, zone, company, ent, s, res);
			
		}
		
	}
	
	public static void add_city(Database db, byte id, String name) {
		
		db.cityNames.put(id, name);
		
	}
	
	public static void add_zone(Database db, byte id, String nameEn, String nameFr) {

		db.zoneNamesEn.put(id, nameEn);
		db.zoneNamesFr.put(id, nameFr);
		
	}
	
	public static void del_company(Database db, byte city, long company) {
		
		if (db.companyNames.containsKey(company)) {
			
			db.companyNames.remove(company);
			
		}
		
		if (db.companyDescriptionsFr.containsKey(company)) {
			
			db.companyDescriptionsFr.remove(company);
			
		}
		
		if (db.companyDescriptionsEn.containsKey(company)) {
			
			db.companyDescriptionsEn.remove(company);
			
		}
		
		if (db.companyEntities.containsKey(company)) {
			
			Vector<Long> v_ent = db.companyEntities.get(company);
			
			for(int i=0;i<v_ent.size();i++) {
				
				long e = v_ent.get(i);
				
				db.entities.remove(e);
				
			}
				
			db.companyEntities.remove(company);
			
		}
		
		if (db.companies.containsKey(company)) {
			
			Vector<Long> v_rel = db.companies.get(company);
			
			for(int i=0;i<v_rel.size();i++) {
				
				long r = v_rel.get(i);
				
				Relation rel = db.relations.get(r);
				
				db.zones_relations.get(rel.zone).removeElement(rel);
				
				city = rel.city;
				
				String[] words = rel.sentence.split(" ");
				for(int j=0;j<words.length;j++) {
					
					if (rel.is_en) db.pilesEn.get(words[j]).removeElement(r);
					else db.pilesFr.get(words[j]).removeElement(r);
					
				}
						
				db.relations.remove(r);
				
			}
			
			db.companies.remove(company);
			
			db.companies_by_city.get(city).removeElement(company);
			
		}
		
	}
	
	public static String stats(Database db) {
		
		StringBuilder result = new StringBuilder();

		Runtime runtime = Runtime.getRuntime();
		String mem = ((runtime.totalMemory() - runtime.freeMemory())/(1024*1024))+"/"+
				(runtime.totalMemory()/(1024*1024))+"/"+
				(runtime.maxMemory()/(1024*1024));
		
		result.append("[MEMORY = "+mem+"]\n");
		result.append("[waiting_orders = "+write_orders.size()+"]\n");
		result.append("[nb_relations = "+db.relations.size()+"]\n");
		result.append("[nb_entities = "+db.entities.size()+"]\n");
		result.append("[nb_companies = "+db.companies.size()+"]\n");
		result.append("[PILES:nb_words_en = "+db.pilesEn.size()+"]\n");
		result.append("[PILES:nb_words_fr = "+db.pilesFr.size()+"]\n");
		result.append("[SYMBOLS:nb = "+db.allSymbolsFr.size()+"]\n");
		
		return result.toString();
		
	}
	
	private static void add_relationEn(Database db, Vector<Long> v_rel, byte city, byte zone, long company, String sentence, Result result) throws Exception {

		sentence = StringUtils.normalizeSpace(sentence);
		
		if (!sentence.equals("")) {
			
			long r_id = get_r_id(db);
			
			Relation r = new Relation(r_id, city, zone, company, true, sentence, result);
			
			if (!db.zones_relations.containsKey(zone)) {
				db.zones_relations.put(zone, new Vector<Relation>());
			}
			db.zones_relations.get(zone).add(r);
			
			v_rel.add(r_id);
			db.relations.put(r_id, r);
			
			String[] sp = sentence.split(" ");
			for(int i=0;i<sp.length;i++) {
				
				String word = sp[i];
				
				if (!db.pilesEn.containsKey(word)) {
					db.pilesEn.put(word, new Vector<Long>());
				}
				
				Vector<Long> v_pile = db.pilesEn.get(word);
	
				v_pile.add(0, r_id);
				
				Symbol.stimulateEn(db, word);
				
			}
			
		}
		
	}
	
	private static void add_relationFr(Database db, Vector<Long> v_rel, byte city, byte zone, long company, String sentence, Result result) throws Exception {

		sentence = StringUtils.normalizeSpace(sentence);
		
		if (!sentence.equals("")) {
			
			long r_id = get_r_id(db);
			
			Relation r = new Relation(r_id, city, zone, company, false, sentence, result);
			
			if (!db.zones_relations.containsKey(zone)) {
				db.zones_relations.put(zone, new Vector<Relation>());
			}
			db.zones_relations.get(zone).add(r);
			
			v_rel.add(r_id);
			db.relations.put(r_id, r);
			
			String[] sp = sentence.split(" ");
			for(int i=0;i<sp.length;i++) {
				
				String word = sp[i];
				
				if (!db.pilesFr.containsKey(word)) {
					db.pilesFr.put(word, new Vector<Long>());
				}
				
				Vector<Long> v_pile = db.pilesFr.get(word);
	
				v_pile.add(0, r_id);
				
				Symbol.stimulateFr(db, word);
				
			}
			
		}
		
	}
	
	private static void add_relationEnEntity(Database db, Vector<Long> v_rel, byte city, byte zone, long company, Entity entity, String sentence, Result result) throws Exception {

		sentence = StringUtils.normalizeSpace(sentence);
		
		if (!sentence.equals("")) {
			
			long r_id = get_r_id(db);
			
			Relation r = new Relation(r_id, city, zone, company, entity, true, sentence, result);
			
			if (!db.zones_relations.containsKey(zone)) {
				db.zones_relations.put(zone, new Vector<Relation>());
			}
			db.zones_relations.get(zone).add(r);
			
			v_rel.add(r_id);
			db.relations.put(r_id, r);
			
			String[] sp = sentence.split(" ");
			for(int i=0;i<sp.length;i++) {
				
				String word = sp[i];
				
				if (!db.pilesEn.containsKey(word)) {
					db.pilesEn.put(word, new Vector<Long>());
				}
				
				Vector<Long> v_pile = db.pilesEn.get(word);
	
				v_pile.add(0, r_id);
				
				Symbol.stimulateEn(db, word);
				
			}
			
		}
		
	}
	
	private static void add_relationFrEntity(Database db, Vector<Long> v_rel, byte city, byte zone, long company, Entity entity, String sentence, Result result) throws Exception {

		sentence = StringUtils.normalizeSpace(sentence);
		
		if (!sentence.equals("")) {
			
			long r_id = get_r_id(db);
			
			Relation r = new Relation(r_id, city, zone, company, entity, false, sentence, result);
			
			if (!db.zones_relations.containsKey(zone)) {
				db.zones_relations.put(zone, new Vector<Relation>());
			}
			db.zones_relations.get(zone).add(r);
			
			v_rel.add(r_id);
			db.relations.put(r_id, r);
			
			String[] sp = sentence.split(" ");
			for(int i=0;i<sp.length;i++) {
				
				String word = sp[i];
				
				if (!db.pilesFr.containsKey(word)) {
					db.pilesFr.put(word, new Vector<Long>());
				}
				
				Vector<Long> v_pile = db.pilesFr.get(word);
	
				v_pile.add(0, r_id);
				
				Symbol.stimulateFr(db, word);
				
			}
			
		}
		
	}
	
	private static synchronized long get_r_id(Database db) {
		
		return db.id_rel++;
		
	}

}
