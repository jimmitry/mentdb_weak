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

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import re.jpayet.mentdb.core.db.file.node.NodeManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.mysql.MYSQLManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.sql.SQLManager;

public class BotManager {

	public static void create_bot(String bot, String lang, String is_male, String firstname, String lastname, String cancel_key, String not_found_response) throws Exception {
		
		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (cancel_key==null || StringFx.lrtrim(cancel_key).equals("")) {
			throw new Exception("Sorry, the cancel_key is required.");
		}

		if (not_found_response==null || StringFx.lrtrim(not_found_response).equals("")) {
			throw new Exception("Sorry, the not_found_response is required.");
		}

		if (firstname==null || StringFx.lrtrim(firstname).equals("")) {
			throw new Exception("Sorry, the firstname is required.");
		}

		if (lastname==null || StringFx.lrtrim(lastname).equals("")) {
			throw new Exception("Sorry, the lastname is required.");
		}

		if (lang==null || StringFx.lrtrim(lang).equals("")) {
			throw new Exception("Sorry, the lang is required.");
		}

		if (is_male==null || StringFx.lrtrim(is_male).equals("")) {
			throw new Exception("Sorry, the is_male field is required.");
		}

		if (bot_exist(bot).equals("1")) {
			throw new Exception("Sorry, the bot '"+bot+"' already exist.");
		} else {
			
			if (!lang.equals("fr") && !lang.equals("en")) {
				throw new Exception("Sorry, the lang must be fr or en.");
			}

			if (!is_male.equals("1") && !is_male.equals("0")) {
				throw new Exception("Sorry, the is_male must be 1 or 0.");
			}
			
			MYSQLManager.executeUpdate("INSERT INTO mona_bot (bot, lang, is_male, firstname, lastname, cancel_key, not_found_response) values("+SQLManager.encode(bot)+", "+SQLManager.encode(lang)+", "+is_male+", "+SQLManager.encode(firstname)+", "+SQLManager.encode(lastname)+", "+SQLManager.encode(cancel_key)+", "+SQLManager.encode(not_found_response)+");", true);
		
		}
		
	}

	public static void delete_bot(String bot) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		} else {
			
			MYSQLManager.executeUpdate("DELETE FROM mona_bot WHERE bot="+SQLManager.encode(bot)+";", true);
		
		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject get_bot(String bot) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT bot, lang, is_male, firstname, lastname, cancel_key, not_found_response FROM `mona_bot` where bot="+SQLManager.encode(bot));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			JSONObject row = new JSONObject();
			row.put("bot", rs.getString(1));
			row.put("lang", rs.getString(2));
			row.put("is_male", rs.getString(3));
			row.put("firstname", rs.getString(4));
			row.put("lastname", rs.getString(5));
			row.put("cancel_key", rs.getString(6));
			row.put("not_found_response", rs.getString(7));
			
			return row;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	@SuppressWarnings("unchecked")
	public static String show_bot() throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		JSONArray result = new JSONArray();

		try {
			
			cmo = MYSQLManager.select("SELECT bot, lang, is_male, firstname, lastname, cancel_key, not_found_response FROM `mona_bot` order by bot");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {

				String bot = rs.getString(1);
				String lang = rs.getString(2);
				String is_male = rs.getString(3);
				String firstname = rs.getString(4);
				String lastname = rs.getString(5);
				String cancel_key = rs.getString(6);
				String not_found_response = rs.getString(7);
				
				JSONObject row = new JSONObject();
				row.put("bot", bot);
				row.put("lang", lang);
				row.put("is_male", is_male);
				row.put("firstname", firstname);
				row.put("lastname", lastname);
				row.put("cancel_key", cancel_key);
				row.put("not_found_response", not_found_response);
				result.add(row);
				
			}
			
			return result.toJSONString();
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static String bot_exist(String bot) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {
			
			cmo = MYSQLManager.select("SELECT count(*) FROM `mona_bot` WHERE `bot`= "+SQLManager.encode(bot));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
			if (r==0) {
				return "0";
			} else {
				return "1";
			}
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static void create_user(String bot, String login, String password, String json_obj_vars, String rights) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}

		if (password==null || StringFx.lrtrim(password).equals("")) {
			throw new Exception("Sorry, the password is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		if (rights==null || StringFx.lrtrim(rights).equals("")) {
			rights = "";
		}
		
		if (bot_exist_user(bot, login).equals("1")) {
			throw new Exception("Sorry, the user '"+login+"' for the bot '"+bot+"' already exist.");
		} else {
			
			MYSQLManager.executeUpdate("INSERT INTO mona_user (bot, login, password, vars, rights) values("+SQLManager.encode(bot)+", "+SQLManager.encode(login)+", "+SQLManager.encode(StringFx.md5(password))+", "+SQLManager.encode(json_obj_vars)+", "+SQLManager.encode(rights)+");", true);
		
		}
		
	}

	public static void delete_user(String bot, String login) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		} else {
			
			MYSQLManager.executeUpdate("DELETE FROM mona_user WHERE bot="+SQLManager.encode(bot)+" and login="+SQLManager.encode(login)+";", true);
		
		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject get_user(String bot, String login) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT vars, rights, password, `key` FROM `mona_user` where bot="+SQLManager.encode(bot)+" and login="+SQLManager.encode(login)+";");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			JSONObject row = new JSONObject();
			row.put("bot", bot);
			row.put("login", login);
			row.put("vars", rs.getString(1));
			row.put("rights", rs.getString(2));
			row.put("password", rs.getString(3));
			row.put("key", rs.getString(4));
			
			return row;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	@SuppressWarnings("unchecked")
	public static String show_user(String bot) throws Exception {

		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		JSONArray result = new JSONArray();

		try {
			
			cmo = MYSQLManager.select("SELECT login, vars, rights FROM `mona_user` where bot="+SQLManager.encode(bot)+" order by login");
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {

				String login = rs.getString(1);
				String vars = rs.getString(2);
				String rights = rs.getString(3);
				
				JSONObject row = new JSONObject();
				row.put("bot", bot);
				row.put("login", login);
				row.put("rights", rights);
				row.put("vars", (JSONObject) new JSONParser().parse(vars));
				result.add(row);
				
			}
			
			return result.toJSONString();
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static String bot_exist_user(String bot, String login) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {
			
			cmo = MYSQLManager.select("SELECT count(*) FROM `mona_user` WHERE `bot`= "+SQLManager.encode(bot)+" and login="+SQLManager.encode(login));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
			if (r==0) {
				return "0";
			} else {
				return "1";
			}
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject execute(String bot, String user, String request) throws Exception {
		
		JSONObject result = new JSONObject();

		Vector<BotManager_Relation> bot_index = word_index.get(bot);
		
		if (bot_index==null) {
			result.put("type", "ko");
			result.put("msg", "Sorry, the bot '"+bot+"' is not loaded.");
		} else {

	        JSONObject user_obj = get_user(bot, user);
	        String user_wait_replay = (String) user_obj.get("key");
	        
	        LastContext lContext = lastContext.get(bot);
	        
			HashMap<BotManager_Relation, Double> map = new HashMap<BotManager_Relation, Double>();
			
			for(BotManager_Relation rel : bot_index) {
				
				SimiRes d = similarityCount(request, rel.trigger);
				double dd = d.result + lContext.get(rel.train.context);
				
				if (dd>75) {
					map.put(rel, dd);
				}
				
			}
			
			List<Entry<BotManager_Relation, Double>> list = new LinkedList<Entry<BotManager_Relation, Double>>(map.entrySet());

	        // Sorting the list based on values
	        Collections.sort(list, new Comparator<Entry<BotManager_Relation, Double>>()
	        {
	            public int compare(Entry<BotManager_Relation, Double> o1,
	                    Entry<BotManager_Relation, Double> o2)
	            {
	            	return o2.getValue().compareTo(o1.getValue());
	            }
	        });

	        JSONObject bot_obj = get_bot(bot);
	        
	        //Log
    		String search_log = "";
	        for(int i=0;i<list.size() && i<15;i++) {
	        	search_log += ">>> "+list.get(i).getKey().train.key+" : "+list.get(i).getKey().trigger+" : "+list.get(i).getValue()+"\n";
	        }
	        
	        if (user_wait_replay!=null && !user_wait_replay.equals("")) {
	        	
	        	if (list.size()>0 && list.get(0).getKey().train.key.equals((String) bot_obj.get("cancel_key"))) {
	        		//execute cancel_key
	        		
	        		lContext.add(list.get(0).getKey().train.context, 8);
	        		
	        		String response = "";

			        JSONObject training = training_get(bot, list.get(0).getKey().train.key);
			        String context = (String) training.get("context");
			        String rights = (String) training.get("rights");
			        String out_mql_output = (String) training.get("out_mql_output");
			        
			        JSONObject vars = (JSONObject) new JSONParser().parse((String) user_obj.get("vars"));

			        EnvManager env = new EnvManager();
					SimiRes simi = similarityCount(request, list.get(0).getKey().trigger);
					JSONArray variables = new JSONArray();
					for(int i=0;i<simi.vars.size();i++) {
						variables.add(simi.vars.get(i));
					}
			        env.set("[bot]", bot);
			        env.set("[bot_lang]", (String) bot_obj.get("lang"));
			        env.set("[bot_is_male]", (String) bot_obj.get("is_male"));
			        env.set("[bot_firstname]", (String) bot_obj.get("firstname"));
			        env.set("[bot_lastname]", (String) bot_obj.get("lastname"));
			        env.set("[training_key]", list.get(0).getKey().train.key);
			        env.set("[training_trigger]", list.get(0).getKey().trigger);
			        env.set("[user]", user);
			        env.set("[user_request]", request);
			        env.set("[user_wait_replay]", user_wait_replay);
			        env.set("[training_context]", context);
			        env.set("[training_rights]", rights);
			        env.set("[user_variables]", variables.toJSONString());
			        env.set("[user_rights]", (String) user_obj.get("rights"));
			        for(Object o : vars.keySet()) {
				        env.set((String)o, (String) vars.get((String)o));
			        }
					
			        SessionThread session = new SessionThread();
			        response = re.jpayet.mentdb.ext.statement.Statement.eval(session, out_mql_output, env, null, null);

					result.put("type", "ok");
					result.put("search_log", search_log);
					result.put("env_after", env.show());
					result.put("msg", response);
					result.put("last_context", lContext.to_str());
					env.set("[response]", response);
					env.set("[user_wait_replay]", get_key(bot, user));
					
					BotManager.set_var(bot, user, "[user_last_request]", request);
					
					MYSQLManager.executeUpdate("INSERT INTO public.mona_task (\n"
							+ "							bot,\n"
							+ "							login,\n"
							+ "							key,\n"
							+ "							vars_copy\n"
							+ "						) VALUES (\n"
							+ "							"+SQLManager.encode(bot)+" ,\n"
							+ "							"+SQLManager.encode(user)+" ,\n"
							+ "							"+SQLManager.encode(list.get(0).getKey().train.key)+" ,\n"
							+ "							"+SQLManager.encode(env.show().toJSONString())+"\n"
							+ "						);", true);
					
					
	        	} else {
	        		//execute user_wait_replay
	        		
			        JSONObject training = training_get(bot, user_wait_replay);
			        String context = (String) training.get("context");
	        		lContext.add(context, 8);

	        		String response = "";

			        String rights = (String) training.get("rights");
			        String out_mql_output = (String) training.get("out_mql_output");
			        
			        JSONObject vars = (JSONObject) new JSONParser().parse((String) user_obj.get("vars"));

			        EnvManager env = new EnvManager();
					
			        env.set("[bot]", bot);
			        env.set("[bot_lang]", (String) bot_obj.get("lang"));
			        env.set("[bot_is_male]", (String) bot_obj.get("is_male"));
			        env.set("[bot_firstname]", (String) bot_obj.get("firstname"));
			        env.set("[bot_lastname]", (String) bot_obj.get("lastname"));
			        env.set("[training_key]", user_wait_replay);
			        env.set("[training_trigger]", "");
			        env.set("[user]", user);
			        env.set("[user_request]", request);
			        env.set("[user_wait_replay]", user_wait_replay);
			        env.set("[training_context]", context);
			        env.set("[training_rights]", rights);
			        env.set("[user_variables]", "[]");
			        env.set("[user_rights]", (String) user_obj.get("rights"));
			        for(Object o : vars.keySet()) {
				        env.set((String)o, (String) vars.get((String)o));
			        }
					
			        SessionThread session = new SessionThread();
			        response = re.jpayet.mentdb.ext.statement.Statement.eval(session, out_mql_output, env, null, null);

					result.put("type", "ok");
					result.put("search_log", search_log);
					result.put("env_after", env.show());
					result.put("msg", response);
					result.put("last_context", lContext.to_str());
					env.set("[response]", response);
					env.set("[user_wait_replay]", get_key(bot, user));
					
					BotManager.set_var(bot, user, "[user_last_request]", request);
					
					MYSQLManager.executeUpdate("INSERT INTO public.mona_task (\n"
							+ "							bot,\n"
							+ "							login,\n"
							+ "							key,\n"
							+ "							vars_copy\n"
							+ "						) VALUES (\n"
							+ "							"+SQLManager.encode(bot)+" ,\n"
							+ "							"+SQLManager.encode(user)+" ,\n"
							+ "							"+SQLManager.encode(user_wait_replay)+" ,\n"
							+ "							"+SQLManager.encode(env.show().toJSONString())+"\n"
							+ "						);", true);
					
	        	}
	        	
	        } else {
	        	//execute search
	        	if (list.size()>0) {
	        		//execute first
	        		
	        		lContext.add(list.get(0).getKey().train.context, 8);
	        		
			        String response = "";

			        JSONObject training = training_get(bot, list.get(0).getKey().train.key);
			        String context = (String) training.get("context");
			        String rights = (String) training.get("rights");
			        String out_mql_output = (String) training.get("out_mql_output");
			        
			        JSONObject vars = (JSONObject) new JSONParser().parse((String) user_obj.get("vars"));

			        EnvManager env = new EnvManager();
					SimiRes simi = similarityCount(request, list.get(0).getKey().trigger);
					JSONArray variables = new JSONArray();
					for(int i=0;i<simi.vars.size();i++) {
						variables.add(simi.vars.get(i));
					}
			        env.set("[bot]", bot);
			        env.set("[bot_lang]", (String) bot_obj.get("lang"));
			        env.set("[bot_is_male]", (String) bot_obj.get("is_male"));
			        env.set("[bot_firstname]", (String) bot_obj.get("firstname"));
			        env.set("[bot_lastname]", (String) bot_obj.get("lastname"));
			        env.set("[training_key]", list.get(0).getKey().train.key);
			        env.set("[training_trigger]", list.get(0).getKey().trigger);
			        env.set("[user]", user);
			        env.set("[user_request]", request);
			        env.set("[user_wait_replay]", user_wait_replay);
			        env.set("[training_context]", context);
			        env.set("[training_rights]", rights);
			        env.set("[user_variables]", variables.toJSONString());
			        env.set("[user_rights]", (String) user_obj.get("rights"));
			        for(Object o : vars.keySet()) {
				        env.set((String)o, (String) vars.get((String)o));
			        }
					
			        SessionThread session = new SessionThread();
			        response = re.jpayet.mentdb.ext.statement.Statement.eval(session, out_mql_output, env, null, null);

					result.put("type", "ok");
					result.put("search_log", search_log);
					result.put("env_after", env.show());
					result.put("msg", response);
					result.put("last_context", lContext.to_str());
					env.set("[response]", response);
					env.set("[user_wait_replay]", get_key(bot, user));
					
					BotManager.set_var(bot, user, "[user_last_request]", request);
					
					MYSQLManager.executeUpdate("INSERT INTO public.mona_task (\n"
							+ "							bot,\n"
							+ "							login,\n"
							+ "							key,\n"
							+ "							vars_copy\n"
							+ "						) VALUES (\n"
							+ "							"+SQLManager.encode(bot)+" ,\n"
							+ "							"+SQLManager.encode(user)+" ,\n"
							+ "							"+SQLManager.encode(list.get(0).getKey().train.key)+" ,\n"
							+ "							"+SQLManager.encode(env.show().toJSONString())+"\n"
							+ "						);", true);
	        		
	        	} else {

					result.put("type", "ko");
					result.put("msg", (String) bot_obj.get("not_found_response"));
					
					MYSQLManager.executeUpdate("INSERT INTO public.mona_not_found (\n"
							+ "				bot,\n"
							+ "				login,\n"
							+ "				input\n"
							+ "			) VALUES (\n"
							+ "				"+SQLManager.encode(bot)+" ,\n"
							+ "				"+SQLManager.encode(user)+" ,\n"
							+ "				"+SQLManager.encode(request)+"\n"
							+ "			);", true);
					
	        	}
	        }
	        
		}
		
		return result;
		
	}
	
	public static void main(String[] arg) throws Exception {
		
		System.out.println(similarityCount("bonjou", "bonjour").result);
		
	}
	
	public final static SimiRes similarityCount(String s1, String s2) throws Exception {
		
		SimiRes result = new SimiRes(s1, s2);
		result.search();
		
		if (s2.indexOf("[1]")!=-1) {
		
			HashMap<Integer, Integer> order_variables = new HashMap<Integer, Integer>();
			for(int i = 1; i<10; i++) {
				if (s2.indexOf("["+i+"]")==-1) {
					break;
				} else {
					order_variables.put(i, s2.indexOf("["+i+"]"));
				}
			}
			
			ArrayList<String> result_ordered = new ArrayList<String>();
			if (order_variables.size()>1) {
	
				List<Map.Entry<Integer, Integer> > list = new LinkedList<Map.Entry<Integer, Integer> >(order_variables.entrySet());
				Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
				    public int compare(Map.Entry<Integer, Integer> o1,
				                       Map.Entry<Integer, Integer> o2)
				    {
				        return (o1.getValue()).compareTo(o2.getValue());
				    }
				});
				
				for(Map.Entry<Integer, Integer> e : list) {
					if (e.getKey()<=result.vars.size()) result_ordered.add(result.vars.get(e.getKey()-1));
				}
				
			} else {
				
				result_ordered = result.vars;
			
			}
			
			for(String v : result_ordered) {
				if (!v.equals("")) {
					result.result+=75;
				}
			}
			
		}

		return result;
		
	}

	public static void reload_all() throws Exception {

		JSONArray bots = (JSONArray) new JSONParser().parse(show_bot());
		
		for(int i=0;i<bots.size();i++) {
			
			JSONObject row = (JSONObject) bots.get(i);
			
			reload_index((String) row.get("bot"));
			
		}
		
	}

	public static ConcurrentHashMap<String, Vector<BotManager_Relation>> word_index = new ConcurrentHashMap<String, Vector<BotManager_Relation>>();
	public static ConcurrentHashMap<String, LastContext> lastContext = new ConcurrentHashMap<String, LastContext>();
	
	public synchronized static String reload_index(String bot) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		word_index.put(bot, new Vector<BotManager_Relation>());
		Vector<BotManager_Relation> bot_index = word_index.get(bot);
		
		lastContext.put(bot, new LastContext());
		
		long nb_trigger = 0;
		long nb_training = 0;
		
		try {
			
			cmo = MYSQLManager.select("SELECT key, context, rights, in_trigger_array FROM `mona_training` WHERE `bot`= "+SQLManager.encode(bot));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {

				String key = rs.getString(1);
				String context = rs.getString(2);
				String rights = rs.getString(3);
				String in_trigger_array = rs.getString(4);
				
				BotManager_Training train = new BotManager_Training();
				train.key = key;
				train.context = context;
				train.rights = rights;
				
				nb_training++;
				
				JSONArray in = (JSONArray) new JSONParser().parse(in_trigger_array);
				
				for(Object s : in) {
					
					String str = (String) s;
					
					BotManager_Relation rel = new BotManager_Relation();
					rel.trigger = str;
					rel.train = train;
					
					bot_index.add(rel);
					
					nb_trigger++;
					
				}
				
			}
			
			return "Trigger: "+nb_trigger+"; Training: "+nb_training;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static String training_exist(String bot, String key) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (key==null || StringFx.lrtrim(key).equals("")) {
			throw new Exception("Sorry, the key is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {
			
			cmo = MYSQLManager.select("SELECT count(*) FROM `mona_training` WHERE `bot`= "+SQLManager.encode(bot)+" and `key`="+SQLManager.encode(key));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
			if (r==0) {
				return "0";
			} else {
				return "1";
			}
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}
	
	//Connect a user
	public static boolean connect_ai(String bot, String user, String password) throws Exception {
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		Long r = 0L;

		try {
			
			cmo = MYSQLManager.select("SELECT count(*) FROM `mona_user` WHERE `bot`= "+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(user)+" and `password`="+SQLManager.encode(StringFx.md5(password)));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			r = Long.parseLong(rs.getString(1));
			
			if (r==0) {
				throw new Exception("j12hki95orm35hrm62vni90tkmr33sdy4");
			} else {
				return true;
			}
			
		} catch (Exception e) {
			
			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject training_get(String bot, String key) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (key==null || StringFx.lrtrim(key).equals("")) {
			throw new Exception("Sorry, the key is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		if (training_exist(bot, key).equals("0")) {
			throw new Exception("Sorry, the training '"+key+"' does not exist for the bot '"+key+"'.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT context, `rights`, `desc`, `in_trigger_array`, `out_mql_output`, `consciousness_obj` FROM `mona_training` WHERE `bot`= "+SQLManager.encode(bot)+" and `key`="+SQLManager.encode(key));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			JSONObject result = new JSONObject();
			result.put("bot", bot);
			result.put("key", key);
			result.put("context", rs.getString(1));
			result.put("rights", rs.getString(2));
			result.put("desc", rs.getString(3));
			result.put("in_trigger_array", rs.getString(4));
			result.put("out_mql_output", rs.getString(5));
			result.put("consciousness_obj", rs.getString(6));
			
			return result;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static void training_merge(String bot, String key, String context, String rights, String description, String in_trigger_json,
			String out_mql_output_json, String consciousness_json) throws Exception {
		
		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (key==null || StringFx.lrtrim(key).equals("")) {
			throw new Exception("Sorry, the key is required.");
		}
		
		if (context==null) {
			context = "";
		}
		
		if (description==null) {
			description = "";
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		if (training_exist(bot, key).equals("0")) {
			MYSQLManager.executeUpdate("INSERT INTO mona_training (`bot`, `key`, `context`, `rights`, `desc`, `in_trigger_array`, `out_mql_output`, `consciousness_obj`) "
					+ "VALUES ("+SQLManager.encode(bot)+", "+SQLManager.encode(key)+", "+SQLManager.encode(context)+", "+SQLManager.encode(rights)+", "+SQLManager.encode(description)+", "+SQLManager.encode(in_trigger_json)+", "+SQLManager.encode(out_mql_output_json)+", "+SQLManager.encode(consciousness_json)+");", true);
		} else {
			MYSQLManager.executeUpdate("UPDATE mona_training SET context="+SQLManager.encode(context)+", `rights`="+SQLManager.encode(rights)+", `desc`="+SQLManager.encode(description)+", `in_trigger_array`="+SQLManager.encode(in_trigger_json)+", `out_mql_output`="+SQLManager.encode(out_mql_output_json)+", `consciousness_obj`="+SQLManager.encode(consciousness_json)+" WHERE bot="+SQLManager.encode(bot)+" and `key`="+SQLManager.encode(key)+";", true);
		}
		
	}

	public static String training_generate_merge(String bot, String key) throws Exception {

		String result = "";
		
		JSONObject tr = training_get(bot, key);
		JSONArray in_trigger_array = (JSONArray) new JSONParser().parse(tr.get("in_trigger_array")+"");
		JSONObject consciousness_obj = (JSONObject) new JSONParser().parse(tr.get("consciousness_obj")+"");
		JSONArray done = (JSONArray) consciousness_obj.get("done");
		JSONArray think = (JSONArray) consciousness_obj.get("think");
		JSONArray understand = (JSONArray) consciousness_obj.get("understand");
		JSONArray subject = (JSONArray) consciousness_obj.get("subject");
		//
		
		result += "json load \"trigger\" \"[]\";\n";
		for(Object o : in_trigger_array) {
			result += "json iarray \"trigger\" / \""+(((String) o).replace("\"", "\\\""))+"\" STR;\n";
		}
		result += "json load \"consciousness_done\" \"[]\";\n";
		for(Object o : done) {
			result += "json iarray \"consciousness_done\" / \""+(((String) o).replace("\"", "\\\""))+"\" STR;\n";
		}
		result += "json load \"consciousness_think\" \"[]\";\n";
		for(Object o : think) {
			result += "json iarray \"consciousness_think\" / \""+(((String) o).replace("\"", "\\\""))+"\" STR;\n";
		}
		result += "json load \"consciousness_understand\" \"[]\";\n";
		for(Object o : understand) {
			result += "json iarray \"consciousness_understand\" / \""+(((String) o).replace("\"", "\\\""))+"\" STR;\n";
		}
		result += "json load \"consciousness_subject\" \"[]\";\n";
		for(Object o : subject) {
			result += "json iarray \"consciousness_subject\" / \""+(((String) o).replace("\"", "\\\""))+"\" STR;\n";
		}
		result += "json load \"consciousness\" \"{}\";\n";
		result += "json iobject \"consciousness\" / \"done\" (json doc \"consciousness_done\") ARRAY;\n"
		+ "json iobject \"consciousness\" / \"think\" (json doc \"consciousness_think\") ARRAY;\n"
		+ "json iobject \"consciousness\" / \"understand\" (json doc \"consciousness_understand\") ARRAY;\n"
		+ "json iobject \"consciousness\" / \"subject\" (json doc \"consciousness_subject\") ARRAY;\n"
		+ "bot training_merge \""+bot.replace("\"", "\\\"")+"\" \""+key.replace("\"", "\\\"")+"\" \""+((String) tr.get("context"))+"\"\n"
		+ "	\""+((String) tr.get("rights"))+"\" \""+((String) tr.get("desc"))+"\"\n"
		+ "	(json doc \"trigger\")\n"
		+ "	(mql {"+((String) tr.get("out_mql_output"))+"})\n"
		+ "	(json doc \"consciousness\")\n"
		+ ";";
		
		return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+result;
		
	}

	public static String training_search(SessionThread session, String bot, String key, String context, String description, String in, String out, String consciousness) throws Exception {

		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		StringBuilder result = new StringBuilder();

		try {
			
			cmo = MYSQLManager.select("SELECT bot, `key` FROM mona_training WHERE "
					+ "bot like "+SQLManager.encode(bot)+" and `key` like "+SQLManager.encode(key)+" and context like "+SQLManager.encode(context)+" and `desc` like "+SQLManager.encode(description)+" and in_trigger_array like "+SQLManager.encode(in)+" and out_mql_output like "+SQLManager.encode(out)+" and consciousness_obj like "+SQLManager.encode(consciousness));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			while (rs.next()) {
				
				result.append("bot training_generate_merge \""+(rs.getString(1).replace("\"", "\\\""))+"\" \""+(rs.getString(2).replace("\"", "\\\""))+"\";\n");

			}
			
			return "j23i88m90m76i39t04r09y35p14a96y09e57t36"+result;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}

	public static void training_remove(String bot, String key) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (key==null || StringFx.lrtrim(key).equals("")) {
			throw new Exception("Sorry, the key is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}
		
		if (training_exist(bot, key).equals("0")) {
			throw new Exception("Sorry, the key '"+key+"' does not exist.");
		} else {
			
			MYSQLManager.executeUpdate("DELETE FROM mona_training WHERE bot="+SQLManager.encode(bot)+" and `key`="+SQLManager.encode(key)+";", true);
		
		}
		
	}

	@SuppressWarnings("unchecked")
	public static void set_var(String bot, String login, String varname, String value) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}

		if (varname==null || StringFx.lrtrim(varname).equals("")) {
			throw new Exception("Sorry, the varname is required.");
		}

		if (!EnvManager.is_valid_varname(varname)) {
			throw new Exception("Sorry, the varname must like [var].");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		JSONObject us = get_user(bot, login);
		
		JSONObject vars = (JSONObject) new JSONParser().parse((String) us.get("vars"));
		vars.put(varname, value);
		
		MYSQLManager.executeUpdate("UPDATE mona_user SET vars="+SQLManager.encode(NodeManager.format(vars.toJSONString()))+" WHERE bot="+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login)+";", true);
		
	}

	public static void set_key(String bot, String login, String key) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		if (key==null || StringFx.lrtrim(key).equals("")) {
			MYSQLManager.executeUpdate("UPDATE mona_user SET `key`= null WHERE bot="+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login)+";", true);
		} else {
			MYSQLManager.executeUpdate("UPDATE mona_user SET `key`="+SQLManager.encode(key)+" WHERE bot="+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login)+";", true);
		}
		
	}
	
	public static String get_key(String bot, String login) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT `key` FROM `mona_user` WHERE `bot`= "+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			return rs.getString(1);
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}
	
	public static String get_var(String bot, String login, String varname) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}

		if (varname==null || StringFx.lrtrim(varname).equals("")) {
			throw new Exception("Sorry, the varname is required.");
		}

		if (!EnvManager.is_valid_varname(varname)) {
			throw new Exception("Sorry, the varname must like [var].");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT vars FROM `mona_user` WHERE `bot`= "+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			JSONObject vars = (JSONObject) new JSONParser().parse(rs.getString(1));
			String val = (String) vars.get(varname);
			return val;
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}
	
	public static String get_vars(String bot, String login) throws Exception {

		if (bot==null || StringFx.lrtrim(bot).equals("")) {
			throw new Exception("Sorry, the bot is required.");
		}

		if (login==null || StringFx.lrtrim(login).equals("")) {
			throw new Exception("Sorry, the login is required.");
		}
		
		if (bot_exist(bot).equals("0")) {
			throw new Exception("Sorry, the bot '"+bot+"' does not exist.");
		}

		if (bot_exist_user(bot, login).equals("0")) {
			throw new Exception("Sorry, the user '"+login+"' does not exist.");
		}
		
		Object[] cmo = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			cmo = MYSQLManager.select("SELECT vars FROM `mona_user` WHERE `bot`= "+SQLManager.encode(bot)+" and `login`="+SQLManager.encode(login));
			
			stm = (Statement) cmo[0];
			rs = (ResultSet) cmo[1];
			
			rs.next();
			
			return rs.getString(1);
			
		} catch (Exception e) {

			throw e;

		} finally {

			try {
				if (rs!=null) rs.close();
			} catch (Exception g) {}

			try {
				if (stm!=null) stm.close();
			} catch (Exception g) {}

		}
		
	}
	
	public static String get_bot_options() throws Exception {
		
		String result = "";
		
		JSONArray bots = ((JSONArray) new JSONParser().parse(BotManager.show_bot()));
		for(int i=0;i<bots.size();i++) {
			
			String b = ((JSONObject) bots.get(i)).get("bot")+"";
			result += "<option value='"+b+"'>"+b+"</option>";
			
		}
		
		return result;
		
	}
	
}
