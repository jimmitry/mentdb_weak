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

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.json.JsonManager;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.encog.Encog;
import org.encog.EncogError;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.train.strategy.ResetStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.platformspecific.j2se.TrainingDialog;
import org.encog.platformspecific.j2se.data.image.ImageMLData;
import org.encog.platformspecific.j2se.data.image.ImageMLDataSet;
import org.encog.util.downsample.Downsample;
import org.encog.util.downsample.RGBDownsample;
import org.encog.util.downsample.SimpleIntensityDownsample;
import org.encog.util.obj.SerializeObject;
import org.encog.util.simple.EncogUtility;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Deep learning
public class ImageNeuralNetworkManager {

	public static void config_training(EnvManager env, String writerId, String width, String height, String type) throws Exception {
		
		if (writerId==null || width==null || height==null || type==null
				|| writerId.equals("") || width.equals("") || height.equals("") || type.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		try {
			
			if (Integer.parseInt(width)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the width is not a valid number >0.");
			
		}
		
		try {
			
			if (Integer.parseInt(height)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the height is not a valid number >0.");
			
		}
		
		if (!type.equals("0") && !type.equals("1")) {
			
			throw new Exception("Sorry, the rgb field must be true or false (or 1|0).");
			
		}
		 
		FileFx.writer_add_line(env, writerId, "CreateTraining: width:"+width+", height:"+height+", rgb:"+type);
		
	}

	public static void config_add_image(EnvManager env, String writerId, String imgPath, String identity) throws Exception {
		
		if (writerId==null || imgPath==null || identity==null
				|| writerId.equals("") || imgPath.equals("") || identity.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		if (FileFx.exist(imgPath).equals("0")) {
			
			throw new Exception("Sorry, the file '"+imgPath+"' does not exist.");
			
		}
		 
		FileFx.writer_add_line(env, writerId, "\nInput: image:"+imgPath+", identity:"+identity);
		
	}
	
	public static String activations = "|BiPolar|BipolarSteepenedSigmoid|ClippedLinear|Competitive|Elliott|ElliottSymmetric|Gaussian|Linear|LOG|Ramp|ReLU|Sigmoid|SIN|SoftMax|SteepenedSigmoid|Step|TANH|";

	public static void config_create_hidden_layers(EnvManager env, String writerId, String nbNeuron, String activation, String hasBias) throws Exception {
		
		if (writerId==null || nbNeuron==null || hasBias==null || activation==null
				|| writerId.equals("") || nbNeuron.equals("") || hasBias.equals("") || activation.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		try {
			
			if (Integer.parseInt(nbNeuron)<0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the nbNeuron is not a valid number >=0.");
			
		}
		
		if (!hasBias.equals("1") && !hasBias.equals("0")) {
			
			throw new Exception("Sorry, the hasBias field must be true or false (or 1|0).");
			
		}
		
		if (activations.toLowerCase().indexOf("|"+activation.toLowerCase()+"|")==-1) {
			
			throw new Exception("Sorry, the activation does not exist (must be "+(activations.substring(0, activations.length()-1))+").");
			
		}
		 
		FileFx.writer_add_line(env, writerId, "\nNetworkHidden: nbNeuron:"+nbNeuron+", activation:"+activation+", hasBias:"+hasBias);
		
	}

	public static void config_create_network(EnvManager env, String writerId, String outputActivation, String saveNetworkPath) throws Exception {
		
		if (writerId==null || saveNetworkPath==null
				|| writerId.equals("") || saveNetworkPath.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		if (outputActivation!=null && !outputActivation.equals("") && activations.toLowerCase().indexOf("|"+outputActivation.toLowerCase()+"|")==-1) {
			
			throw new Exception("Sorry, the activation does not exist (must be "+(activations.substring(0, activations.length()-1))+").");
			
		}
		 
		if (outputActivation!=null && !outputActivation.equals("")) FileFx.writer_add_line(env, writerId, "\nNetworkCreateOrLoad: outputActivation:"+outputActivation+", saveNetworkPath:"+saveNetworkPath);
		else FileFx.writer_add_line(env, writerId, "\nNetworkCreateOrLoad: outputActivation:null, saveNetworkPath:"+saveNetworkPath);
		
	}

	public static void config_train(EnvManager env, String writerId, String mode, String minutes, String strategyError, String strategyCycles, String saveNetworkPath) throws Exception {
		
		if (writerId==null || mode==null || minutes==null || strategyError==null || strategyCycles==null
				|| writerId.equals("") || mode.equals("") || minutes.equals("") || strategyError.equals("") || strategyCycles.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		try {
			
			if (Integer.parseInt(minutes)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the minutes field is not a valid number >0.");
			
		}
		
		try {
			
			if (Integer.parseInt(strategyCycles)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the strategyCycles field is not a valid number >0.");
			
		}
		
		try {
			
			if (Double.parseDouble(strategyError)<=0) {
				
				throw new Exception("err");
				
			}
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the strategyError field is not a valid double >0.");
			
		}
		 
		FileFx.writer_add_line(env, writerId, "\nTrain: Mode:"+mode+", Minutes:"+minutes+", StrategyError:"+strategyError+", StrategyCycles:"+strategyCycles+", saveNetworkPath:"+saveNetworkPath);
		
	}

	public static void config_predict(EnvManager env, String writerId, String imgPath, String identity) throws Exception {
		
		if (writerId==null || imgPath==null || identity==null
				|| writerId.equals("") || imgPath.equals("") || identity.equals("")) {
			
			throw new Exception("Sorry, all parameters cannot be null or empty.");
			
		}
		
		if (FileFx.exist(imgPath).equals("0")) {
			
			throw new Exception("Sorry, the file '"+imgPath+"' does not exist.");
			
		}
		 
		FileFx.writer_add_line(env, writerId, "\nWhatis: image:"+imgPath+", identity:"+identity);
		
	}
	
	class ImagePair {
		private final File file;
		private final int identity;

		public ImagePair(final File file, final int identity) {
			super();
			this.file = file;
			this.identity = identity;
		}

		public File getFile() {
			return this.file;
		}

		public int getIdentity() {
			return this.identity;
		}
	}

	public static String exe(EnvManager env, String filePath) throws Exception {
		
		String errorMsg = "", result = "";
		try {
			final ImageNeuralNetworkManager program = new ImageNeuralNetworkManager();
			result = program.execute(env, filePath);
		} catch (final Exception e) {
			errorMsg = ""+e.getMessage();
		}
		
		Encog.getInstance().shutdown();
		
		if (!errorMsg.equals("")) {
			
			throw new Exception(errorMsg);
			
		}
		
		return result;
		
	}

	private final List<ImagePair> imageList = new ArrayList<ImagePair>();
	private final Map<String, String> args = new HashMap<String, String>();
	private final Map<String, Integer> identity2neuron = new HashMap<String, Integer>();
	private final Map<Integer, String> neuron2identity = new HashMap<Integer, String>();
	private JSONObject jsonNeuron2identity = new JSONObject();
	private String networkPath = new String();
	private String isRGBTraining = new String();
	private ImageMLDataSet training;
	private String line;
	private int outputCount;
	private int downsampleWidth;
	private int downsampleHeight;
	private BasicNetwork network;
	private double nbWhatIs = 0;
	private double nbWhatGood = 0;
	private JSONObject hiddens = null;

	private Downsample downsample;

	@SuppressWarnings("unchecked")
	private int assignIdentity(final String identity) {

		if (this.identity2neuron.containsKey(identity.toLowerCase())) {
			return this.identity2neuron.get(identity.toLowerCase());
		}

		final int result = this.outputCount;
		this.identity2neuron.put(identity.toLowerCase(), result);
		this.neuron2identity.put(result, identity.toLowerCase());
		jsonNeuron2identity.put(result, identity.toLowerCase());
		this.outputCount++;
		return result;
	}

	public String execute(EnvManager env, final String file) throws Exception {
		
		final FileInputStream fstream = new FileInputStream(file);
		
		final DataInputStream in = new DataInputStream(fstream);
		
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));

		this.nbWhatIs = 0;
		
		while ((this.line = br.readLine()) != null) {
			
			executeLine(env);
			
		}
		
		in.close();
		
		String result = "dl img load_network \""+networkPath.replace("\"", "\\\"")+"\";\n"+
				"dl img predict \"dir/image.jpg\" "+(isRGBTraining.equals("1")?"true":"false")+" "+downsampleHeight+" "+downsampleWidth+" \""+JsonManager.format_Gson(jsonNeuron2identity.toJSONString()).replace("\"", "\\\"")+"\";";
		
		return result;
		
	}

	private void executeCommand(EnvManager env, final String command,
			final Map<String, String> args) throws Exception {
		if (command.equals("input")) {
			processInput();
		} else if (command.equals("createtraining")) {
			processCreateTraining();
		} else if (command.equals("train")) {
			processTrain(env);
		} else if (command.equals("networkhidden")) {
			processHidden();
		} else if (command.equals("networkcreateorload")) {
			processNetwork();
		} else if (command.equals("whatis")) {
			processWhatIs();
		}

	}

	public void executeLine(EnvManager env) throws Exception {
		final int index = this.line.indexOf(':');
		if (index == -1) {
			throw new EncogError("Invalid command: " + this.line);
		}

		final String command = this.line.substring(0, index).toLowerCase()
				.trim();
		final String argsStr = this.line.substring(index + 1).trim();
		final StringTokenizer tok = new StringTokenizer(argsStr, ",");
		this.args.clear();
		while (tok.hasMoreTokens()) {
			final String arg = tok.nextToken();
			final int index2 = arg.indexOf(':');
			if (index2 == -1) {
				throw new EncogError("Invalid command: " + this.line);
			}
			final String key = arg.substring(0, index2).toLowerCase().trim();
			final String value = arg.substring(index2 + 1).trim();
			this.args.put(key, value);
		}

		executeCommand(env, command, this.args);
	}

	private String getArg(final String name) {
		final String result = this.args.get(name);
		if (result == null) {
			throw new EncogError("Missing argument " + name + " on line: "
					+ this.line);
		}
		return result;
	}

	private void processCreateTraining() {
		final String strWidth = getArg("width");
		final String strHeight = getArg("height");
		final String isRGB = getArg("rgb");
		
		isRGBTraining = isRGB;

		this.downsampleHeight = Integer.parseInt(strHeight);
		this.downsampleWidth = Integer.parseInt(strWidth);

		if (isRGB.equals("1")) {
			this.downsample = new RGBDownsample();
		} else {
			this.downsample = new SimpleIntensityDownsample();
		}

		this.training = new ImageMLDataSet(this.downsample, false, 1, -1);
		
		hiddens = new JSONObject();
		
		System.out.println("Training set created");
	}

	private void processInput() throws IOException {
		final String image = getArg("image");
		final String identity = getArg("identity");

		final int idx = assignIdentity(identity);
		final File file = new File(image);

		this.imageList.add(new ImagePair(file, idx));

		System.out.println("Added input image:" + image);
	}

	@SuppressWarnings("unchecked")
	private void processHidden() throws IOException {
		
		final String nbneuron = getArg("nbneuron");
		final String activation = getArg("activation").toLowerCase().trim();
		final String hasbias = getArg("hasbias");
		
		JSONObject hidden = new JSONObject();
		hidden.put("nbneuron", nbneuron);
		hidden.put("activation", activation);
		hidden.put("hasbias", hasbias);
		
		hiddens.put(""+hiddens.size(), hidden);
		
	}

	private void processNetwork() throws Exception {
		System.out.println("Downsampling images...");

		for (final ImagePair pair : this.imageList) {
			final MLData ideal = new BasicMLData(this.outputCount);
			final int idx = pair.getIdentity();
			for (int i = 0; i < this.outputCount; i++) {
				if (i == idx) {
					ideal.setData(i, 1);
				} else {
					ideal.setData(i, -1);
				}
			}
			
			final Image img = ImageIO.read(pair.getFile());
			
			final ImageMLData data = new ImageMLData(img);
			
			this.training.add(data, ideal);
			
		}

		final String saveNetworkPath = getArg("savenetworkpath");
		networkPath = saveNetworkPath;
		final String outputactivation = getArg("outputactivation");
		
		this.training.downsample(this.downsampleHeight, this.downsampleWidth);
		
		File saveFile = new File(saveNetworkPath);
		
		if (!saveFile.exists()) {
			
			this.network = createNetwork(this.training.getInputSize(), this.training.getIdealSize(), this.hiddens, outputactivation);
			
			System.out.println("Created network: " + this.network.toString());
			
		} else {
			
			this.network = (BasicNetwork) SerializeObject.load(saveFile);
			
			System.out.println("Loaded network: " + this.network.toString());
			
		}
		
	}

	public static void load_network(EnvManager env, String saveFile) throws IOException, ClassNotFoundException {
		
		env.bn = (BasicNetwork) SerializeObject.load(new File(saveFile));
		
	}
	
	public static BasicNetwork createNetwork(final int input, final int output, JSONObject hiddens, String outputactivation) throws Exception {
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,input));
		
		for(int i=0;i<hiddens.size();i++) {
			
			JSONObject h = (JSONObject) hiddens.get(""+i);
			String nbneuron = (String) h.get("nbneuron");
			String activation = (String) h.get("activation");
			String hasbias = (String) h.get("hasbias"); 
			
			boolean hasBias = false;
			if (hasbias.equals("1")) hasBias = true;
			
			network.addLayer(new BasicLayer(getActivationFunction(activation),hasBias,Integer.parseInt(nbneuron)));
			
		}
		
		network.addLayer(new BasicLayer(getActivationFunction(outputactivation),false,output));
		
		network.getStructure().finalizeStructure();
		network.reset();
		
		return network;
		
	}
	
	private static ActivationFunction getActivationFunction(String activation) throws Exception {
		
		ActivationFunction af = null;
		
		switch (activation.toLowerCase()) {
		case "null":
			break;
		case "bipolar":
			af = new org.encog.engine.network.activation.ActivationBiPolar();
			break;
		case "bipolarsteepenedsigmoid":
			af = new org.encog.engine.network.activation.ActivationBipolarSteepenedSigmoid();
			break;
		case "clippedlinear":
			af = new org.encog.engine.network.activation.ActivationClippedLinear();
			break;
		case "competitive":
			af = new org.encog.engine.network.activation.ActivationCompetitive();
			break;
		case "elliott":
			af = new org.encog.engine.network.activation.ActivationElliott();
			break;
		case "elliottsymmetric":
			af = new org.encog.engine.network.activation.ActivationElliottSymmetric();
			break;
		case "gaussian":
			af = new org.encog.engine.network.activation.ActivationGaussian();
			break;
		case "linear":
			af = new org.encog.engine.network.activation.ActivationLinear();
			break;
		case "log":
			af = new org.encog.engine.network.activation.ActivationLOG();
			break;
		case "ramp":
			af = new org.encog.engine.network.activation.ActivationRamp();
			break;
		case "relu":
			af = new org.encog.engine.network.activation.ActivationReLU();
			break;
		case "sigmoid":
			af = new org.encog.engine.network.activation.ActivationSigmoid();
			break;
		case "sin":
			af = new org.encog.engine.network.activation.ActivationSIN();
			break;
		case "softmax":
			af = new org.encog.engine.network.activation.ActivationSoftMax();
			break;
		case "steepenedsigmoid":
			af = new org.encog.engine.network.activation.ActivationSteepenedSigmoid();
			break;
		case "step":
			af = new org.encog.engine.network.activation.ActivationStep();
			break;
		case "tanh":
			af = new org.encog.engine.network.activation.ActivationTANH();
			break;
		default:
			
			throw new Exception("Sorry, no algorithm found ("+activations.substring(1, activations.length()-1)+").");
		
		}
		
		return af;
		
	}

	private void processTrain(EnvManager env) throws IOException {
		final String strMode = getArg("mode");
		final String strMinutes = getArg("minutes");
		final String strStrategyError = getArg("strategyerror");
		final String strStrategyCycles = getArg("strategycycles");
		final String saveNetworkPath = getArg("savenetworkpath");

		System.out.println("Training Beginning... Output patterns="
				+ this.outputCount);

		final double strategyError = Double.parseDouble(strStrategyError);
		final int strategyCycles = Integer.parseInt(strStrategyCycles);
		
		final ResilientPropagation train = new ResilientPropagation(this.network, this.training);//tanh:2>80//1>51//sigmoid:2>79//1>51
		//final Backpropagation train = new Backpropagation(this.network, this.training);//tanh:2>70//1>40//sigmoid:2>//1>40
		//final ManhattanPropagation train = new ManhattanPropagation(this.network, this.training, 0.00001);//tanh:2>50//1>44//sigmoid:2>//1>49
		//final QuickPropagation train = new QuickPropagation(this.network, this.training);//tanh:2>52//1>40//sigmoid:2>//1>40err
		//final ScaledConjugateGradient train = new ScaledConjugateGradient(this.network, this.training);//tanh:2>77//1>74//sigmoid:2>//1>69
		
		train.addStrategy(new ResetStrategy(strategyError, strategyCycles));
		
		if (strMode.equalsIgnoreCase("gui")) {
			TrainingDialog.trainDialog(train, this.network, this.training);
		} else {
			final int minutes = Integer.parseInt(strMinutes);
			EncogUtility.trainConsole(train, this.network, this.training,
					minutes);
		}
		System.out.println("Training Stopped...");
		
		SerializeObject.save(new File(saveNetworkPath), (BasicNetwork)network);
		
		System.out.println("Network saved...");
	}

	public void processWhatIs() throws IOException {
		final String filename = getArg("image");
		final String identity = getArg("identity");
		//System.out.println(filename);
		final File file = new File(filename);
		final Image img = ImageIO.read(file);
		final ImageMLData input = new ImageMLData(img);
		input.downsample(this.downsample, false, this.downsampleHeight,
				this.downsampleWidth, 1, -1);
		final int winner = this.network.winner(input);
		this.nbWhatIs++;
		String OKKO = "KO";
		if (this.neuron2identity.get(winner).equals(identity)) {
			nbWhatGood++;
			OKKO = "ok";
		}
		System.out.println(OKKO+" - What is: " + filename + ", it seems to be: "
				+ this.neuron2identity.get(winner) + ", accuracy: "+(100D*nbWhatGood/nbWhatIs)+"%");
		
	}

	public static String predict(EnvManager env, String filename, String isRGB, String downsampleWidth, String downsampleHeight, String jsonIdentity) throws IOException, ParseException {
		
		final File file = new File(filename);
		final Image img = ImageIO.read(file);
		final ImageMLData input = new ImageMLData(img);
		
		if (isRGB.equals("1")) {
			input.downsample(new RGBDownsample(), false, Integer.parseInt(downsampleHeight), Integer.parseInt(downsampleWidth), 1, -1);
		} else {
			input.downsample(new SimpleIntensityDownsample(), false, Integer.parseInt(downsampleHeight), Integer.parseInt(downsampleWidth), 1, -1);
		}
		
		final int winner = env.bn.winner(input);
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonIdentity);
		
		return (String) jsonObj.get(""+winner);
		
	}

}