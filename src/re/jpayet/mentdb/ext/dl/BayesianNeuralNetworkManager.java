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

package re.jpayet.mentdb.ext.dl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.encog.mathutil.probability.CalcProbability;
import org.encog.ml.bayesian.BayesianEvent;
import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.EventType;
import org.encog.ml.bayesian.query.enumerate.EnumerationQuery;
import org.encog.util.Format;
import org.encog.util.text.BagOfWords;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.json.JsonManager;

public class BayesianNeuralNetworkManager {
	
	public static HashMap<String, BayesianNeuralNetworkManager> all_bayesian_networks = new HashMap<String, BayesianNeuralNetworkManager>();

	private int k;

	private BagOfWords totalBag;
	private JSONArray categories = null;
	private HashMap<String, Integer> categories_key = new HashMap<String, Integer>();
	
	private Vector<BayesianNeuralNetworkObj> dataObj = new Vector<BayesianNeuralNetworkObj>();
	
	public BayesianNeuralNetworkManager(JSONArray categories) {
		
		this.categories = categories;
		for(int i=0;i<categories.size();i++) {
			
			dataObj.add(new BayesianNeuralNetworkObj());
			categories_key.put(""+categories.get(i), i);
			
		}
		
	}

	public void init(int theK) {

		this.k = theK;

		for(int i=0;i<dataObj.size();i++) {
			
			dataObj.get(i).bag = new BagOfWords(this.k);

		}

		this.totalBag = new BagOfWords(this.k);
		
		for(int i=0;i<dataObj.size();i++) {
			
			for(String line: dataObj.get(i).DATA) {
				dataObj.get(i).bag.process(line);
				totalBag.process(line);
			}
			
		}

		for(int i=0;i<dataObj.size();i++) {
			
			dataObj.get(i).bag.setLaplaceClasses(totalBag.getUniqueWords());
			
		}

	}

	public List<String> separateSpaces(String str) {
		List<String> result = new ArrayList<String>();
		StringBuilder word = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch != '\'' && !Character.isLetterOrDigit(ch)) {
				if (word.length() > 0) {
					result.add(word.toString());
					word.setLength(0);
				}
			} else {
				word.append(ch);
			}
		}

		if (word.length() > 0) {
			result.add(word.toString());
		}

		return result;
	}

	public double probability(String m, int pos) {
		List<String> words = separateSpaces(m);
		
		BayesianNetwork network = new BayesianNetwork();
		BayesianEvent spamEvent = network.createEvent("search");
		
		int index = 0;
		for( String word: words) {
			BayesianEvent event = network.createEvent(word+index);
			network.createDependency(spamEvent, event);
			index++;
		}
		
		network.finalizeStructure();
		
		//SamplingQuery query = new SamplingQuery(network);
		EnumerationQuery query = new EnumerationQuery(network);
		
		CalcProbability messageProbability = new CalcProbability(this.k);
		
		for(int i=0;i<dataObj.size();i++) {
			
			messageProbability.addClass(dataObj.get(i).DATA.size());
			
		}
		
		double probSpam = messageProbability.calculate(pos);
		
		spamEvent.getTable().addLine(probSpam, true);
		query.defineEventType(spamEvent, EventType.Outcome);
		query.setEventValue(spamEvent, true);
		
		index = 0;
		for( String word: words) {
			String word2 = word+index;
			BayesianEvent event = network.getEvent(word2);
			
			for(int i=0;i<dataObj.size();i++) {
				if (pos==i) event.getTable().addLine(dataObj.get(i).bag.probability(word), true, true);
				else event.getTable().addLine(dataObj.get(i).bag.probability(word), true, false);
			}
			
			query.defineEventType(event, EventType.Evidence);
			query.setEventValue(event, true);
			index++;
			
		}
		
		//query.setSampleSize(100000000);
		query.execute();
		return query.getProbability();		
	}

	@SuppressWarnings("unchecked")
	public JSONObject test(String message) {
		
		JSONObject result = new JSONObject();
		JSONArray probArray = new JSONArray();
		result.put("probabilities", probArray);
		double bestProba = -1;
		int bestIndex = -1;
		
		for(int i=0;i<categories.size();i++) {
			
			double d = probability(message, i);
			
			JSONObject cat = new JSONObject();
			cat.put("index", i);
			cat.put("key", ""+categories.get(i));
			cat.put("prob_double", d);
			cat.put("prob_percent", Format.formatPercent(d));
			probArray.add(cat);
			
			if (d>bestProba) {
				bestProba = d;
				bestIndex = i;
			}
			
		}
		
		result.put("input", message);
		result.put("best_double", bestProba);
		result.put("best_percent", Format.formatPercent(bestProba));
		result.put("best_index", bestIndex);
		result.put("prediction", ""+categories.get(bestIndex));
		
		return result;
		
	}
	
	public void add_data(String cat, String sentence) {
		
		dataObj.get(categories_key.get(cat)).DATA.addElement(sentence);
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show() {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		for(String k : all_bayesian_networks.keySet()) {
			
			result.add(k);
			
		}
		
		return result;
		
	}

	public static String exist(String bayesianId) throws Exception {
		
		//Check if the network already exist
		if (all_bayesian_networks.containsKey(bayesianId)) {
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}

	public static void create(String bayesianId, String cats) throws Exception {
		
		if (exist(bayesianId).equals("1")) {
			
			throw new Exception("Sorry, the Bayesian network '"+bayesianId+"' already exist.");
			
		}
		
		JSONArray cats_array = (JSONArray) JsonManager.load(cats);
		
		all_bayesian_networks.put(bayesianId, new BayesianNeuralNetworkManager(cats_array));
		
	}

	public static void add_sentence(String bayesianId, String cat, String sentence) throws Exception {
		
		if (exist(bayesianId).equals("0")) {
			
			throw new Exception("Sorry, the Bayesian network '"+bayesianId+"' does not exist.");
			
		}
		
		all_bayesian_networks.get(bayesianId).add_data(cat, sentence);
		
	}

	public static JSONObject predict(String bayesianId, String sentence) throws Exception {
		
		if (exist(bayesianId).equals("0")) {
			
			throw new Exception("Sorry, the Bayesian network '"+bayesianId+"' does not exist.");
			
		}
		
		return all_bayesian_networks.get(bayesianId).test(sentence);
		
	}

	public static void delete(String bayesianId) throws Exception {
		
		if (exist(bayesianId).equals("0")) {
			
			throw new Exception("Sorry, the Bayesian network '"+bayesianId+"' does not exist.");
			
		}
		
		all_bayesian_networks.remove(bayesianId);
		
	}

	public static void init(String bayesianId, String laplace_int) throws Exception {
		
		if (exist(bayesianId).equals("0")) {
			
			throw new Exception("Sorry, the Bayesian network '"+bayesianId+"' does not exist.");
			
		}
		
		all_bayesian_networks.get(bayesianId).init(Integer.parseInt(laplace_int));;
		
	}

	@SuppressWarnings("unchecked")
	public static final void main(String[] args) {

		JSONArray cat = new JSONArray();
		cat.add("positif");
		cat.add("neutre");
		cat.add("négatif");
		BayesianNeuralNetworkManager program = new BayesianNeuralNetworkManager(cat);
		program.add_data("positif", "super bon");
		program.add_data("positif", "délicieux");
		program.add_data("neutre", "soleil");
		program.add_data("neutre", "poissons");
		program.add_data("négatif", "dégueulace");
		program.add_data("négatif", "dégueu");
/*
		System.out.println("Using Laplace of 0"); 
		program.init(0);
		program.test("le repas c'est super bon"); 
		program.test("le dîner c'est délicieux"); 
		program.test("le film c'est horrible"); 
		program.test("tu es dégueulace"); 
		program.test("tu es dégueu"); 
		program.test("le soleil tourne sur lui même"); 
		program.test("la mer contient des poissons"); 
*/
		System.out.println("Using Laplace of 1"); 
		program.init(1); 
		program.test("délicieux"); 
		//program.test("le dîner c'est délicieux"); 
		//program.test("tu es dégueulace"); 
		//program.test("tu es dégueu"); 
		//program.test("le soleil tourne sur lui même"); 
		//program.test("la mer contient des poissons"); 

	}

}