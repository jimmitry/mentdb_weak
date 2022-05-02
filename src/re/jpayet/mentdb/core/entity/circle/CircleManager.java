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

package re.jpayet.mentdb.core.entity.circle;

import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.Record2;
import re.jpayet.mentdb.core.entity.relation.RelationManager;
import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.sequence.SequenceManager;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.tools.Misc;

public class CircleManager {

	//Get ids
	@SuppressWarnings("unchecked")
	public static JSONArray getIds(String level, String r_th) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		result.add(r_th);
		JSONObject test = new JSONObject();
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought id is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String circle_id = (String) ((JSONObject) recRelation.get("c")).get(level);
		if (circle_id==null) return result;
		else {
			
			result.remove(0);
			
			JSONObject c = Record2.getNode(circle_id);
			
			//Save for all into the circle
			for(Object o : c.keySet()) {
				
				//get the key
				String k = (String) o;
				String r1 = Misc.atom(k, 2, " ");
				String r2 = Misc.atom(k, 3, " ");
				
				if (!test.containsKey(r1)) {
					result.add(r1);
					test.put(r1, null);
				}
				if (!test.containsKey(r2)) {
					result.add(r2);
					test.put(r2, null);
				}
				
			}
			
			return result;
			
		}
		
	}

	//Get ids
	@SuppressWarnings("unchecked")
	public static JSONArray getIds(String level, String lang, String r_th) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		result.add(r_th);
		JSONObject test = new JSONObject();
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought id is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String circle_id = (String) ((JSONObject) recRelation.get("c")).get(level);
		if (circle_id==null) return result;
		else {
			
			result.remove(0);
			
			JSONObject c = Record2.getNode(circle_id);
			
			//Save for all into the circle
			for(Object o : c.keySet()) {
				
				//get the key
				String k = (String) o;
				
				String r1 = Misc.atom(k, 2, " ");
				String r2 = Misc.atom(k, 3, " ");
				
				if (!test.containsKey(r1)) {
					result.add(r1);
					test.put(r1, null);
				}
				if (!test.containsKey(r2)) {
					result.add(r2);
					test.put(r2, null);
				}
				
			}

			JSONArray ok = new JSONArray();
			JSONArray ko = new JSONArray();
			
			for(int i=0;i<result.size();i++) {
				
				String curRelation = (String) result.get(i);
				
				if (((String) Record2.getNode(curRelation).get("l")).equals(lang)) {
					
					ok.add(curRelation);
					
				} else {
					
					ko.add(curRelation);
					
				}
				
			}
			
			if (ok.size()>0) {
				
				return ok;
				
			} else {
				
				return ko;
				
			}
			
		}
		
	}

	//Get ids
	@SuppressWarnings("unchecked")
	public static String getId(String level, String lang, String r_th) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		result.add(r_th);
		JSONObject test = new JSONObject();
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought id is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String circle_id = (String) ((JSONObject) recRelation.get("c")).get(level);
		if (circle_id==null) return r_th;
		else {
			
			result.remove(0);
			
			JSONObject c = Record2.getNode(circle_id);
			
			//Save for all into the circle
			for(Object o : c.keySet()) {
				
				//get the key
				String k = (String) o;
				
				String r1 = Misc.atom(k, 2, " ");
				String r2 = Misc.atom(k, 3, " ");
				
				if (!test.containsKey(r1)) {
					result.add(r1);
					test.put(r1, null);
				}
				if (!test.containsKey(r2)) {
					result.add(r2);
					test.put(r2, null);
				}
				
			}

			JSONArray ok = new JSONArray();
			JSONArray ko = new JSONArray();
			
			for(int i=0;i<result.size();i++) {
				
				String curRelation = (String) result.get(i);
				
				if (((String) Record2.getNode(curRelation).get("l")).equals(lang)) {
					
					ok.add(curRelation);
					
				} else {
					
					ko.add(curRelation);
					
				}
				
			}
			
			Random ran = new Random();
			
			if (ok.size()>0) {
				
				return ((String) ok.get(ran.nextInt(ok.size())));
				
			} else {
				
				return ((String) ko.get(ran.nextInt(ko.size())));
				
			}
			
		}
		
	}

	public static boolean contains(String level, String r_th, String r_th_toFind) throws Exception {
		
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought id is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String circle_id = (String) ((JSONObject) recRelation.get("c")).get(level);
		if (r_th==r_th_toFind) return true;
		else if (circle_id==null) return true;
		else {
			
			JSONObject c = Record2.getNode(circle_id);
			boolean b = false;
			
			//Save for all into the circle
			for(Object o : c.keySet()) {
				
				//get the key
				String k = (String) o;
				String r1 = Misc.atom(k, 2, " ");
				String r2 = Misc.atom(k, 3, " ");
				
				if (r1.equals(r_th_toFind) || r2.equals(r_th_toFind)) {
					b = true;
					break;
				}
				
			}
			
			return b;
			
		}
		
	}

	//Show all circles
	public static JSONObject showCircle(String level, String r_th) throws Exception {
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought id is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String circle_id = (String) ((JSONObject) recRelation.get("c")).get(level);
		if (circle_id==null) return new JSONObject();
		else {
			
			return Record2.getNode(circle_id);
			
		}
		
	}

	//Add circle
	@SuppressWarnings("unchecked")
	public static void setCurrentCircle(String level, String r_th, String currentCircle) throws Exception {
		
		JSONObject recRelation = Record2.getNode(r_th);
		((JSONObject) recRelation.get("c")).put(level, currentCircle);
		Record2.update(r_th, recRelation.toJSONString());
		
	}

	//Add relations into a circle
	@SuppressWarnings("unchecked")
	public static String mergeCircle(String level, String relationId1, String positions1, String relationId2, String positions2, EnvManager env, SessionThread session) throws Exception {

		positions1 = StringFx.trim(positions1);
		positions2 = StringFx.trim(positions2);
		
		if (relationId1.length()==0) {
	
			throw new Exception("Sorry, the relation id is required.");
	
		}
	
		if (relationId2.length()==0) {
	
			throw new Exception("Sorry, the relation to add is required.");
	
		}
		
		//Try to get the relation 1
		JSONObject recRelation1 = Record2.getNode(relationId1);
	
		if (recRelation1==null) {
	
			throw new Exception("Sorry, the relation "+relationId1+" does not exist.");
	
		}
		
		//Try to get the relation 2
		JSONObject recRelation2 = Record2.getNode(relationId2);
	
		if (recRelation2==null) {
	
			throw new Exception("Sorry, the relation "+relationId2+" does not exist.");
	
		}
		
		//Get actions and conditions
		JSONObject type1 = (JSONObject) recRelation1.get("t");
		JSONArray actions1 = (JSONArray) recRelation1.get("a");
		JSONArray actions2 = (JSONArray) recRelation2.get("a");
		JSONObject conditions2 = (JSONObject) recRelation2.get("dco");
		JSONObject e = (JSONObject) recRelation1.get("e");
		JSONObject n_s = (JSONObject) recRelation1.get("n_s");
		JSONObject n_p = (JSONObject) recRelation1.get("n_p");
		String fi = null;
		if (recRelation1.containsKey("fi")) {
			
			fi = (String) recRelation1.get("fi");
			
		}
		String ct = null;
		if (recRelation1.containsKey("ct")) {
			
			ct = (String) recRelation1.get("ct");
			
		}
		
		//Check if there are actions into the relation 2
		if (actions2.size()>0) {
			
			throw new Exception("Sorry, the relation "+relationId2+" have actions.");
			
		}
		
		//Check if there are conditions into the relation 2
		if (conditions2.size()>0) {
			
			throw new Exception("Sorry, the relation "+relationId2+" have conditions.");
			
		}
		
		//Parse all positions in the relation 2
		int nbPosition = Misc.size(positions2, " ");
	
		if (nbPosition!=RelationManager.showThoughtNodes(relationId2).size()) {
	
			throw new Exception("Sorry, the number of position is not equals to the number of thought/relation into the relation "+relationId2+" ("+RelationManager.showSentence(relationId2, env, session)+").");
	
		}
		
		//Parse all positions in the relation 1
		nbPosition = Misc.size(positions1, " ");
	
		if (nbPosition!=RelationManager.showThoughtNodes(relationId1).size()) {
	
			throw new Exception("Sorry, the number of position is not equals to the number of thought/relation into the relation "+relationId1+" ("+RelationManager.showSentence(relationId1, env, session)+").");
	
		}
		
		//Get the circle 2
		String circle_id_2 = ((String) ((JSONObject) recRelation2.get("c")).get(level));
		JSONObject currentCircle2 = null;
		if (circle_id_2==null) currentCircle2 = new JSONObject();
		else {
			throw new Exception("Sorry, the relation "+relationId2+" is in the circle '"+circle_id_2+"'.");
		}
		
		//Lang
		String lang1 = (String) recRelation1.get("l");
		String lang2 = (String) recRelation2.get("l");
		
		//Get the circle 1
		String circle_id_1 = ((String) ((JSONObject) recRelation1.get("c")).get(level));
		JSONObject currentCircle1 = null;
		if (circle_id_1==null) currentCircle1 = new JSONObject();
		else {
			currentCircle1 = Record2.getNode(circle_id_1);
		}
		
		//Add circle 2 into the circle 1
		for(Object o2 : currentCircle2.keySet()) {
			
			//get the key
			String k = (String) o2;
			String val = (String) currentCircle2.get(k);
			
			if (!currentCircle1.containsKey(k)) {
				
				currentCircle1.put(k, val);
				
			}
			
		}
		
		//Add the link into the circle
		currentCircle1.put(lang1+" "+relationId2+" "+relationId1, positions2);
		//Inverse the position
		currentCircle1.put(lang2+" "+relationId1+" "+relationId2, positions1);
		
		if (circle_id_1 == null) {
			
			circle_id_1 = "CI["+SequenceManager.incr("circle")+"]";
			Record2.add("record", circle_id_1, currentCircle1.toJSONString());
			
		} else {
			
			Record2.update(circle_id_1, currentCircle1.toJSONString());
			
		}
		
		//Save for all into the circle1
		for(Object o1 : currentCircle1.keySet()) {
			
			//get the key
			String k = (String) o1;
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			setCurrentCircle(level, r1, circle_id_1);
			setCurrentCircle(level, r2, circle_id_1);
			
		}
		
		//Save for all into the circle2
		for(Object o2 : currentCircle2.keySet()) {
			
			//get the key
			String k = (String) o2;
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			setCurrentCircle(level, r1, circle_id_1);
			setCurrentCircle(level, r2, circle_id_1);
			
		}
		
		recRelation2 = Record2.getNode(relationId2);
		recRelation2.put("a", actions1);
		recRelation2.put("e", e);
		recRelation2.put("t", type1);
		recRelation2.put("n_s", n_s);
		recRelation2.put("n_p", n_p);
		if (fi!=null) recRelation2.put("fi", fi);
		if (ct!=null) recRelation2.put("ct", ct);
		
		Record2.update(relationId2, recRelation2.toJSONString());
		
		return circle_id_1;
		
	}

	//Add thought into a circle
	@SuppressWarnings("unchecked")
	public static String mergeCircle(String level, String r_th_1, String r_th_2, EnvManager env, SessionThread session) throws Exception {

		if (r_th_1.length()==0) {
	
			throw new Exception("Sorry, the thought id is required.");
	
		}
	
		if (r_th_2.length()==0) {
	
			throw new Exception("Sorry, the thought to add is required.");
	
		}
		
		//Try to get the relation 1
		JSONObject recRelation1 = Record2.getNode(r_th_1);
	
		if (recRelation1==null) {
	
			throw new Exception("Sorry, the thought "+r_th_1+" does not exist.");
	
		}
		
		//Try to get the relation 2
		JSONObject recRelation2 = Record2.getNode(r_th_2);
	
		if (recRelation2==null) {
	
			throw new Exception("Sorry, the thought "+r_th_2+" does not exist.");
	
		}
		
		//Get the circle 2
		String circle_id_2 = ((String) ((JSONObject) recRelation2.get("c")).get(level));
		JSONObject currentCircle2 = null;
		if (circle_id_2==null) currentCircle2 = new JSONObject();
		else {
			currentCircle2 = Record2.getNode(circle_id_2);
		}
		
		//Lang
		String lang1 = (String) recRelation1.get("l");
		String lang2 = (String) recRelation2.get("l");
		
		//Get the circle 1
		String circle_id_1 = ((String) ((JSONObject) recRelation1.get("c")).get(level));
		JSONObject currentCircle1 = null;
		if (circle_id_1==null) currentCircle1 = new JSONObject();
		else {
			currentCircle1 = Record2.getNode(circle_id_1);
		}
		
		//Add circle 2 into the circle 1
		for(Object o2 : currentCircle2.keySet()) {
			
			//get the key
			String k = (String) o2;
			String val = (String) currentCircle2.get(k);
			
			if (!currentCircle1.containsKey(k)) {
				
				currentCircle1.put(k, val);
				
			}
			
		}
		
		//Add the link into the circle
		currentCircle1.put(lang1+" "+r_th_2+" "+r_th_1, "1");
		//Inverse the position
		currentCircle1.put(lang2+" "+r_th_1+" "+r_th_2, "1");
		
		if (circle_id_1 == null) {
			
			circle_id_1 = "CI["+SequenceManager.incr("circle")+"]";
			Record2.add("record", circle_id_1, currentCircle1.toJSONString());
			
		} else {
			
			Record2.update(circle_id_1, currentCircle1.toJSONString());
			
		}
		
		if (circle_id_2 != null && !circle_id_2.equals(circle_id_1)) {
			
			Record2.remove(circle_id_2);
			
		}
		
		//Save for all into the circle1
		for(Object o1 : currentCircle1.keySet()) {
			
			//get the key
			String k = (String) o1;
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			setCurrentCircle(level, r1, circle_id_1);
			setCurrentCircle(level, r2, circle_id_1);
			
		}
		
		//Save for all into the circle2
		for(Object o2 : currentCircle2.keySet()) {
			
			//get the key
			String k = (String) o2;
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			setCurrentCircle(level, r1, circle_id_1);
			setCurrentCircle(level, r2, circle_id_1);
			
		}
		
		((JSONObject) recRelation2.get("c")).put(level, circle_id_1);
		
		Record2.update(r_th_2, recRelation2.toJSONString());
		
		return circle_id_1;
		
	}

	//Delete circle
	@SuppressWarnings("unchecked")
	public static void deleteCircle(String level, String r_th) throws Exception {
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought to delete is required.");
	
		}
		
		//Try to get the relation 1
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		//Get the current circle
		String circle_id = ((String) ((JSONObject) recRelation.get("c")).get(level));
		JSONObject currentCircle = null;
		if (circle_id==null) currentCircle = new JSONObject();
		else {
			currentCircle = Record2.getNode(circle_id);
		}
		
		//Initialize the deleted keys
		JSONArray deletedKeys = new JSONArray();
		JSONObject deletedRelations = new JSONObject();
		
		//Delete all occurrences
		for(Object o1 : currentCircle.keySet()) {
			
			//get the current key
			String k = (String) o1;
			
			if (k.indexOf(r_th)>-1) {
				
				deletedKeys.add(k);
	
				deletedRelations.put(Misc.atom(k, 2, " "), false);
				deletedRelations.put(Misc.atom(k, 3, " "), true);
				
			}
			
		}
		for(int i=0;i<deletedKeys.size();i++) {
			currentCircle.remove((String) deletedKeys.get(i));
		}
		
		if (circle_id != null) {
			
			if (currentCircle.size()==0) Record2.remove(circle_id);
			else Record2.update(circle_id, currentCircle.toJSONString());
			
		}
		
		//Clear the circle for all deleted keys
		for(Object d : deletedRelations.keySet()) {
			
			//get the key
			String r = (String) d;
			
			setCurrentCircle(level, r, null);
			
		}
		
		//Save for all into the circle
		for(Object o1 : currentCircle.keySet()) {
			
			//get the key
			String k = (String) o1;
			String r1 = Misc.atom(k, 2, " ");
			String r2 = Misc.atom(k, 3, " ");
			
			setCurrentCircle(level, r1, circle_id);
			setCurrentCircle(level, r2, circle_id);
			
		}
		
	}

	//Exist circle
	public static String existCircle(String level, String r_th) throws Exception {
	
		if (r_th.length()==0) {
	
			throw new Exception("Sorry, the relation/thought to find is required.");
	
		}
		
		//Try to get the relation
		JSONObject recRelation = Record2.getNode(r_th);
	
		if (recRelation==null) {
	
			throw new Exception("Sorry, the relation/thought "+r_th+" does not exist.");
	
		}
		
		String currentCircle = ((String) ((JSONObject) recRelation.get("c")).get(level));
		
		if (currentCircle==null) return "0";
		return "1";
		
	}
	
	

}
