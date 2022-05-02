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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.encog.util.Format;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizer;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class TextCatManager {
	
	public static HashMap<String, DoccatModel> all_doc_cats = new HashMap<String, DoccatModel>();
	
	@SuppressWarnings("unchecked")
	public static JSONArray show() {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		for(String k : all_doc_cats.keySet()) {
			
			result.add(k);
			
		}
		
		return result;
		
	}

	public static String exist(String bayesianId) throws Exception {
		
		//Check if the network already exist
		if (all_doc_cats.containsKey(bayesianId)) {
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void create_model(String lang, String train_file_path, String iterations_param, String model_file_path_to_save) throws IOException {
		
		// read the training data
        InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File(train_file_path));
        ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
        ObjectStream sampleStream = new DocumentSampleStream(lineStream);

        // define the training parameters
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, iterations_param);
        params.put(TrainingParameters.CUTOFF_PARAM, 0+"");
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        
        // create a model from traning data
        DoccatModel model = DocumentCategorizerME.train(lang, sampleStream, params, new DoccatFactory());
        
        // save the model to local
        BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(model_file_path_to_save));
        model.serialize(modelOut);
		
	}

	public static void load(String docCatId, String model_file_path) throws Exception {
		
		if (exist(docCatId).equals("1")) {
			
			throw new Exception("Sorry, the doc cat '"+docCatId+"' already exist.");
			
		}
		
		InputStream modelIn = new FileInputStream(model_file_path);
		DoccatModel model = null;
		
		try {
			model = new DoccatModel(modelIn);
		} catch (IOException e) {
		  throw e;
		} finally {
		  if (null != modelIn) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		
		all_doc_cats.put(docCatId, model);
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject predict(String docCatId, String txt) throws Exception {
		
		if (exist(docCatId).equals("0")) {
			
			throw new Exception("Sorry, the doc cat '"+docCatId+"' does not exist.");
			
		}
		
		// test the model file by subjecting it to prediction
        DocumentCategorizer doccat = new DocumentCategorizerME(all_doc_cats.get(docCatId));
        String[] docWords = txt.replaceAll("[^A-Za-z]", " ").split(" ");
        double[] aProbs = doccat.categorize(docWords);
        
        JSONObject result = new JSONObject();
		JSONArray probArray = new JSONArray();
		result.put("probabilities", probArray);
		double bestProba = -1;
		int bestIndex = -1;
		
		for(int i=0;i<doccat.getNumberOfCategories();i++) {
			
			double d = aProbs[i];
			
			JSONObject cat = new JSONObject();
			cat.put("index", i);
			cat.put("key", doccat.getCategory(i)+"");
			cat.put("prob_double", d);
			cat.put("prob_percent", Format.formatPercent(d));
			probArray.add(cat);
			
			if (d>bestProba) {
				bestProba = d;
				bestIndex = i;
			}
			
        }
		
		result.put("input", txt);
		result.put("best_double", bestProba);
		result.put("best_percent", Format.formatPercent(bestProba));
		result.put("best_index", bestIndex);
		result.put("prediction", doccat.getBestCategory(aProbs)+"");
		
		return result;
		
	}

	public static void delete(String docCatId) throws Exception {
		
		if (exist(docCatId).equals("0")) {
			
			throw new Exception("Sorry, the doc cat '"+docCatId+"' does not exist.");
			
		}
		
		all_doc_cats.remove(docCatId);
		
	}
	
}