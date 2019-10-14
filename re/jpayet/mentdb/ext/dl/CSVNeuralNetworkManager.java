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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.encog.ConsoleStatusReportable;
import org.encog.Encog;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;
import org.encog.util.simple.EncogUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import re.jpayet.mentdb.ext.env.EnvManager;

//Deep learning
public class CSVNeuralNetworkManager {

	public static String run(String config) throws Exception {

		String errorMsg = "", result = "";

		try {
			
			JSONObject conf = (JSONObject) (new JSONParser()).parse(config);
			JSONArray cols = (JSONArray) conf.get("cols");
			Vector<Integer> indexes = new Vector<Integer>();

			String csvFilePath = (String) conf.get("filePath");
			String modelPath = (String) conf.get("modelPath");
			String helperPath = (String) conf.get("helperPath");
			String nbLoop = (String) conf.get("nbLoop");
			double validationPercent = Double.parseDouble(""+conf.get("validationPercent"));
			boolean shuffle = Boolean.parseBoolean(""+conf.get("shuffle"));//Boolean
			int seed = Integer.parseInt(""+conf.get("seed"));

			// Download the data that we will attempt to model.
			File irisFile = new File(csvFilePath);

			// Define the format of the data file.
			// This area will change, depending on the columns and 
			// format of the file that you are trying to model.
			VersatileDataSource source = new CSVDataSource(irisFile, false, CSVFormat.DECIMAL_POINT);
			VersatileMLDataSet data = new VersatileMLDataSet(source);

			for(int i=0;i<cols.size();i++) {

				JSONObject o = (JSONObject) cols.get(i);

				if ((""+o.get("type")).toLowerCase().equals("in")) {
					data.defineSourceColumn(""+o.get("title"), Integer.parseInt(""+o.get("index")), ColumnType.continuous);
					indexes.add(Integer.parseInt(""+o.get("index")));
				}

			}

			// Define the column that we are trying to predict.
			ColumnDefinition outputColumn = null;
			int indexOut = -1;
			for(int i=0;i<cols.size();i++) {

				JSONObject o = (JSONObject) cols.get(i);

				if ((""+o.get("type")).toLowerCase().equals("out")) {
					outputColumn = data.defineSourceColumn(""+o.get("title"), Integer.parseInt(""+o.get("index")), ColumnType.nominal);
					indexOut = Integer.parseInt(""+o.get("index"));
				}

			}

			// Analyze the data, determine the min/max/mean/sd of every column.
			data.analyze();

			// Map the prediction column to the output of the model, and all
			// other columns to the input.
			data.defineSingleOutputOthersInput(outputColumn);

			// Create feedforward neural network as the model type. MLMethodFactory.TYPE_FEEDFORWARD.
			// You could also other model types, such as:
			// MLMethodFactory.SVM:  Support Vector Machine (SVM)
			// MLMethodFactory.TYPE_RBFNETWORK: RBF Neural Network
			// MLMethodFactor.TYPE_NEAT: NEAT Neural Network
			// MLMethodFactor.TYPE_PNN: Probabilistic Neural Network
			EncogModel model = new EncogModel(data);
			model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);

			// Send any output to the console.
			model.setReport(new ConsoleStatusReportable());

			// Now normalize the data.  Encog will automatically determine the correct normalization
			// type based on the model you chose in the last step.
			data.normalize();

			// Hold back some data for a final validation.
			// Shuffle the data into a random ordering.
			// Use a seed of 1001 so that we always use the same holdback and will get more consistent results.
			model.holdBackValidation(validationPercent, shuffle, seed);

			// Choose whatever is the default training type for this model.
			model.selectTrainingType(data);

			// Use a 5-fold cross-validated train.  Return the best method found.
			MLRegression bestMethod = (MLRegression)model.crossvalidate(Integer.parseInt(nbLoop), true);

			// Display the training and validation errors.
			System.out.println( "Training error: " + EncogUtility.calculateRegressionError(bestMethod, model.getTrainingDataset()));
			System.out.println( "Validation error: " + EncogUtility.calculateRegressionError(bestMethod, model.getValidationDataset()));

			// Display our normalization parameters.
			NormalizationHelper helper = data.getNormHelper();
			System.out.println(helper.toString());

			// Display the final model.
			System.out.println("Final model: " + bestMethod);

			persistModel(bestMethod, modelPath);
			persistHelper(helper, helperPath);

			// Loop over the entire, original, dataset and feed it through the model.
			// This also shows how you would process new data, that was not part of your
			// training set.  You do not need to retrain, simply use the NormalizationHelper
			// class.  After you train, you can save the NormalizationHelper to later
			// normalize and denormalize your data.
			ReadCSV csv = new ReadCSV(irisFile, false, CSVFormat.DECIMAL_POINT);
			String[] line = new String[indexes.size()];
			MLData input = helper.allocateInputVector();

			double nbWhatIs = 0;
			double nbWhatGood = 0;

			while(csv.next()) {

				nbWhatIs++;

				StringBuilder r = new StringBuilder();
				for(int i=0;i<indexes.size();i++) {
					line[i] = csv.get(indexes.get(i));
				}

				String correct = csv.get(indexOut);
				helper.normalizeInputVector(line,input.getData(),false);
				MLData output = bestMethod.compute(input);
				String irisChosen = helper.denormalizeOutputVectorToString(output)[0];

				String OKKO = "KO";
				if (irisChosen.equals(correct)) {
					nbWhatGood++;
					OKKO = "ok";
				}
				r.append(OKKO+" - "+Arrays.toString(line));
				r.append(" -> predicted: ");
				r.append(irisChosen);
				r.append("(correct: ");
				r.append(correct);
				r.append(")" + ", accuracy: "+(100D*nbWhatGood/nbWhatIs)+"%");

				System.out.println(r.toString());

			}

		} catch (Exception ex) {

			errorMsg = ""+ex.getMessage();

		}

		// Delete data file ande shut down.

		Encog.getInstance().shutdown();

		if (!errorMsg.equals("")) {

			throw new Exception(errorMsg);

		}

		return result;

	}

	//save model
	public static void persistModel(MLRegression helper, String path) throws Exception {

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(helper);
			oos.flush();
		} catch (IOException e) {

			throw new Exception(""+e.getMessage());

		} finally {

			try {

				oos.close();

			} catch (Exception f) {}

		}

	}

	//reload model
	public static MLRegression reloadModels(String path) throws Exception {

		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(path)));
			MLRegression helper = (MLRegression) ois.readObject();
			return helper;
		} catch (Exception e) {
			throw new Exception(""+e.getMessage());
		} finally {

			try {

				ois.close();

			} catch (Exception f) {}

		}

	}

	public static void load_model(EnvManager env, String modelFile, String helperFile) throws Exception {

		env.mlr = reloadModels(modelFile);
		env.helper = reloadHelper(helperFile);

	}

	// since you do data cleaning, this function help you save the cleaner and you can reload it when predict new data
	public static void persistHelper(NormalizationHelper helper, String HelperPath) throws Exception {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(HelperPath));
			oos.writeObject(helper);
			oos.flush();

		} catch (IOException e) {
			throw new Exception(""+e.getMessage());
		} finally {

			try {

				oos.close();

			} catch (Exception f) {}

		}
	}

	// reload data cleaner
	public static NormalizationHelper reloadHelper(String HelperPath) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(HelperPath)));
			NormalizationHelper helper = (NormalizationHelper) ois.readObject();
			return helper;
		} catch (IOException e) {
			throw new Exception(""+e.getMessage());
		} finally {

			try {

				ois.close();

			} catch (Exception f) {}

		}
	}

	public static String predict(EnvManager env, String jsonArrayIn) throws IOException, ParseException {

		JSONArray arrayIn = (JSONArray) (new JSONParser()).parse(jsonArrayIn);

		String[] line = new String[arrayIn.size()];
		MLData input = env.helper.allocateInputVector();

		for(int i=0;i<arrayIn.size();i++) {
			line[i] = ""+arrayIn.get(i);
		}

		env.helper.normalizeInputVector(line,input.getData(),false);
		MLData output = env.mlr.compute(input);
		
		return env.helper.denormalizeOutputVectorToString(output)[0];

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		JSONObject config = new JSONObject();
		config.put("filePath", "demo/iris.data.txt");
		config.put("modelPath", "demo/iris.eg");
		config.put("helperPath", "demo/iris.hl");
		config.put("nbLoop", "6");
		config.put("validationPercent", "0.3");
		config.put("shuffle", "true");
		config.put("seed", "1001");
		JSONArray cols = new JSONArray();
		config.put("cols", cols);

		JSONObject col = new JSONObject();
		col.put("index", "0");
		col.put("title", "sepal-length");
		col.put("type", "in"); //in|out
		cols.add(col);

		col = new JSONObject();
		col.put("index", "1");
		col.put("title", "sepal-width");
		col.put("type", "in"); //in|out
		cols.add(col);

		col = new JSONObject();
		col.put("index", "2");
		col.put("title", "petal-length");
		col.put("type", "in"); //in|out
		cols.add(col);

		col = new JSONObject();
		col.put("index", "3");
		col.put("title", "petal-width");
		col.put("type", "in"); //in|out
		cols.add(col);

		col = new JSONObject();
		col.put("index", "4");
		col.put("title", "species");
		col.put("type", "out"); //in|out
		cols.add(col);

		run(config.toJSONString());

	}

}