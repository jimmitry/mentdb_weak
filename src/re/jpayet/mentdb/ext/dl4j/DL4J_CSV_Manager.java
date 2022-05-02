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

package re.jpayet.mentdb.ext.dl4j;

import java.io.File;
import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.CacheMode;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.LearningRatePolicy;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.distribution.BinomialDistribution;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.distribution.UniformDistribution;
import org.deeplearning4j.nn.conf.layers.ActivationLayer;
import org.deeplearning4j.nn.conf.layers.AutoEncoder;
import org.deeplearning4j.nn.conf.layers.BatchNormalization;
import org.deeplearning4j.nn.conf.layers.CenterLossOutputLayer;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer.AlgoMode;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.DropoutLayer;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.GlobalPoolingLayer;
import org.deeplearning4j.nn.conf.layers.GravesBidirectionalLSTM;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.LocalResponseNormalization;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.conf.layers.RBM;
import org.deeplearning4j.nn.conf.layers.RBM.HiddenUnit;
import org.deeplearning4j.nn.conf.layers.RBM.VisibleUnit;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class DL4J_CSV_Manager {
	
	public static String train_and_save_model(EnvManager env, String json_config) throws Exception {

		System.out.println("Load dataset ...");
		
		JSONObject conf_json = (JSONObject) JsonManager.load(json_config);
		
		String dataTrainFile = (String) conf_json.get("dataTrainFile");
		String pathToSaveModel = (String) conf_json.get("pathToSaveModel");
		String pathToSaveNormalizer = (String) conf_json.get("pathToSaveNormalizer");

		String csv_test = (String) conf_json.get("csvTest");
		String testBatchSize = (String) conf_json.get("testBatchSize");
		
		int numClasses = Integer.parseInt(""+conf_json.get("numClasses"));
		int labelIndex = Integer.parseInt(""+conf_json.get("labelIndex"));
		int batchSizeTraining = Integer.parseInt(""+conf_json.get("batchSizeTraining"));

		String activation = (String) conf_json.get("activation");
		String weightInit = (String) conf_json.get("weightInit");
		String updater = (String) conf_json.get("updater");
		String optimizationAlgo = (String) conf_json.get("optimizationAlgo");
		String cacheMode = (String) conf_json.get("cacheMode");
		String convolutionMode = (String) conf_json.get("convolutionMode");
		String gradientNormalization = (String) conf_json.get("gradientNormalization");
		String learningRateDecayPolicy = (String) conf_json.get("learningRateDecayPolicy");
		
		DataSet trainingData = readCSVDataset(dataTrainFile, batchSizeTraining, labelIndex, numClasses);

		// shuffle our training data to avoid any impact of ordering
		trainingData.shuffle();

		// Neural nets all about numbers. Lets normalize our data
		DataNormalization normalizer = new NormalizerStandardize();
		// Collect the statistics from the training data. This does
		// not modify the input data
		normalizer.fit(trainingData);

		// Apply normalization to the training data
		normalizer.transform(trainingData);
		
		System.out.println("Building model ...");
		NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
		
		if (conf_json.get("biasInit")!=null && !(""+conf_json.get("biasInit")).equals("")) {
			builder.biasInit(Double.parseDouble(""+conf_json.get("biasInit")));
		}
		if (gradientNormalization!=null && !gradientNormalization.equals("")) {
			builder.gradientNormalization(GradientNormalization.valueOf(gradientNormalization));
		}
		if (conf_json.get("gradientNormalizationThreshold")!=null && !(""+conf_json.get("gradientNormalizationThreshold")).equals("")) {
			builder.gradientNormalizationThreshold(Double.parseDouble(""+conf_json.get("gradientNormalizationThreshold")));
		}
		if (conf_json.get("seed")!=null && !(""+conf_json.get("seed")).equals("")) {
			builder.seed(Long.parseLong(""+conf_json.get("seed")));
		}
		if (conf_json.get("dropOut")!=null && !(""+conf_json.get("dropOut")).equals("")) {
			builder.dropOut(Double.parseDouble(""+conf_json.get("dropOut")));
		}
		if (conf_json.get("useDropConnect")!=null && !(""+conf_json.get("useDropConnect")).equals("")) {
			builder.useDropConnect(convert_bool(""+conf_json.get("useDropConnect")));
		}
		if (convolutionMode!=null && !convolutionMode.equals("")) {
			builder.convolutionMode(ConvolutionMode.valueOf(convolutionMode));
		}
		if (optimizationAlgo!=null && !optimizationAlgo.equals("")) {
			builder.optimizationAlgo(OptimizationAlgorithm.valueOf(optimizationAlgo));
		}
		if (conf_json.get("iterations")!=null && !(""+conf_json.get("iterations")).equals("")) {
			builder.iterations(Integer.parseInt(""+conf_json.get("iterations")));
		}
		if (activation!=null && !activation.equals("")) {
			builder.activation(Activation.valueOf(activation));
		}
		if (weightInit!=null && !weightInit.equals("")) {
			builder.weightInit(WeightInit.valueOf(weightInit));
		}
		if (weightInit!=null && weightInit.toUpperCase().equals("DISTRIBUTION")) {
			if (conf_json.get("dist")!=null && !(""+conf_json.get("dist")).equals("")) {
				String di = (""+conf_json.get("dist"));
				if (di.toLowerCase().startsWith("uniformdistribution")) {
					builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(di, "2", ":")), Double.parseDouble(AtomFx.get(di, "3", ":"))));
				} else if (di.toLowerCase().startsWith("binomialdistribution")) {
					builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(di, "2", ":")), Double.parseDouble(AtomFx.get(di, "3", ":"))));
				} else if (di.toLowerCase().startsWith("normaldistribution")) {
					builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(di, "2", ":")), Double.parseDouble(AtomFx.get(di, "3", ":"))));
				}
			}
		}
		if (conf_json.get("learningRate")!=null && !(""+conf_json.get("learningRate")).equals("")) {
			builder.learningRate(Double.parseDouble(""+conf_json.get("learningRate")));
		}
		if (conf_json.get("biasLearningRate")!=null && !(""+conf_json.get("biasLearningRate")).equals("")) {
			builder.biasLearningRate(Double.parseDouble(""+conf_json.get("biasLearningRate")));
		}
		if (learningRateDecayPolicy!=null && !learningRateDecayPolicy.equals("")) {
			builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(learningRateDecayPolicy));
		}
		if (conf_json.get("lrPolicyDecayRate")!=null && !(""+conf_json.get("lrPolicyDecayRate")).equals("")) {
			builder.lrPolicyDecayRate(Double.parseDouble(""+conf_json.get("lrPolicyDecayRate")));
		}
		if (conf_json.get("lrPolicyPower")!=null && !(""+conf_json.get("lrPolicyPower")).equals("")) {
			builder.lrPolicyPower(Double.parseDouble(""+conf_json.get("lrPolicyPower")));
		}
		if (conf_json.get("lrPolicySteps")!=null && !(""+conf_json.get("lrPolicySteps")).equals("")) {
			builder.lrPolicySteps(Double.parseDouble(""+conf_json.get("lrPolicySteps")));
		}
		if (conf_json.get("regularization")!=null && !(""+conf_json.get("regularization")).equals("")) {
			builder.regularization(convert_bool(""+conf_json.get("regularization")));
		}
		if (cacheMode!=null && !cacheMode.equals("")) {
			builder.cacheMode(CacheMode.valueOf(cacheMode));
		}
		if (conf_json.get("l1")!=null && !(""+conf_json.get("l1")).equals("")) {
			builder.l1(Double.parseDouble(""+conf_json.get("l1")));
		}
		if (conf_json.get("l1Bias")!=null && !(""+conf_json.get("l1Bias")).equals("")) {
			builder.l1Bias(Double.parseDouble(""+conf_json.get("l1Bias")));
		}
		if (conf_json.get("l2")!=null && !(""+conf_json.get("l2")).equals("")) {
			builder.l2(Double.parseDouble(""+conf_json.get("l2")));
		}
		if (conf_json.get("l2Bias")!=null && !(""+conf_json.get("l2Bias")).equals("")) {
			builder.l2Bias(Double.parseDouble(""+conf_json.get("l2Bias")));
		}
		if (conf_json.get("maxNumLineSearchIterations")!=null && !(""+conf_json.get("maxNumLineSearchIterations")).equals("")) {
			builder.maxNumLineSearchIterations(Integer.parseInt(""+conf_json.get("maxNumLineSearchIterations")));
		}
		if (conf_json.get("miniBatch")!=null && !(""+conf_json.get("miniBatch")).equals("")) {
			builder.miniBatch(convert_bool(""+conf_json.get("miniBatch")));
		}
		if (conf_json.get("minimize")!=null && !(""+conf_json.get("minimize")).equals("")) {
			builder.minimize(convert_bool(""+conf_json.get("minimize")));
		}
		if (updater!=null && !updater.equals("")) {
			builder.updater(Updater.valueOf(updater));
		}
		
		ListBuilder list_builder = builder.list();
		
		//###################################################################################################################
		
		JSONArray layers = (JSONArray) conf_json.get("layers");
		for(int i_layer = 0;i_layer<layers.size(); i_layer++) {
			
			JSONObject current_layer = (JSONObject) layers.get(i_layer);
 			String type_layer = (String) current_layer.get("type");

 			if (type_layer.toLowerCase().equals("subsamplinglayer")) {
 				
 				String l_kernelSize = (String) current_layer.get("kernelSize");
				int[] kernelSize = new int[Integer.parseInt(AtomFx.size(l_kernelSize, ":"))];
				for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_kernelSize, ":")); ik++) {
					kernelSize[ik-1] = Integer.parseInt(AtomFx.get(l_kernelSize, ""+ik, ":"));
				}
				
 				org.deeplearning4j.nn.conf.layers.SubsamplingLayer.Builder d_builder = new SubsamplingLayer.Builder(kernelSize);

 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

				String l_stride = (String) current_layer.get("stride");
				if (l_stride!=null && !l_stride.equals("")) {
 					int[] stride = new int[Integer.parseInt(AtomFx.size(l_stride, ":"))];
 					for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_stride, ":")); ik++) {
 						stride[ik-1] = Integer.parseInt(AtomFx.get(l_stride, ""+ik, ":"));
 					}
 					d_builder.stride(stride);
				}
 				
				String l_padding = (String) current_layer.get("padding");
 				if (l_padding!=null && !l_padding.equals("")) {
 					int[] padding = new int[Integer.parseInt(AtomFx.size(l_padding, ":"))];
 					for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_padding, ":")); ik++) {
 						padding[ik-1] = Integer.parseInt(AtomFx.get(l_padding, ""+ik, ":"));
 					}
 					d_builder.padding(padding);
				}
 				
 				String l_convolutionMode = (String) current_layer.get("convolutionMode");
 				if (l_convolutionMode!=null && !l_convolutionMode.equals("")) {
 					d_builder.convolutionMode(ConvolutionMode.valueOf(l_convolutionMode));
 				}
 				
 				String eps = (String) current_layer.get("eps");
 				if (eps!=null && !eps.equals("")) {
 					d_builder.eps(Double.parseDouble(eps));
 				}
 				
 				String pnorm = (String) current_layer.get("pnorm");
 				if (pnorm!=null && !pnorm.equals("")) {
 					d_builder.pnorm(Integer.parseInt(pnorm));
 				}

 				String l_pooling = (String) current_layer.get("poolingType");
 				if (l_pooling!=null && !l_pooling.equals("")) {
 					d_builder.poolingType(org.deeplearning4j.nn.conf.layers.SubsamplingLayer.PoolingType.valueOf(l_pooling));
				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("rbm")) {
 				
 				org.deeplearning4j.nn.conf.layers.RBM.Builder d_builder = new RBM.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String hiddenUnit = (String) current_layer.get("hiddenUnit");
 				if (hiddenUnit!=null && !hiddenUnit.equals("")) {
 					d_builder.hiddenUnit(HiddenUnit.valueOf(hiddenUnit));
 				}
 				
 				String k = (String) current_layer.get("k");
 				if (k!=null && !k.equals("")) {
 					d_builder.k(Integer.parseInt(k));
 				}
 				
 				String lossFunction = (String) current_layer.get("lossFunction");
				if (lossFunction!=null && !lossFunction.equals("")) {
 					d_builder.lossFunction(LossFunctions.LossFunction.valueOf(lossFunction));
 				}
 				
 				String preTrainIterations = (String) current_layer.get("preTrainIterations");
 				if (preTrainIterations!=null && !preTrainIterations.equals("")) {
 					d_builder.preTrainIterations(Integer.parseInt(preTrainIterations));
 				}
 				
 				String sparsity = (String) current_layer.get("sparsity");
 				if (sparsity!=null && !sparsity.equals("")) {
 					d_builder.sparsity(Double.parseDouble(sparsity));
 				}
 				
 				String visibleBiasInit = (String) current_layer.get("visibleBiasInit");
 				if (visibleBiasInit!=null && !visibleBiasInit.equals("")) {
 					d_builder.visibleBiasInit(Double.parseDouble(visibleBiasInit));
 				}
 				
 				String visibleUnit = (String) current_layer.get("visibleUnit");
 				if (visibleUnit!=null && !visibleUnit.equals("")) {
 					d_builder.visibleUnit(VisibleUnit.valueOf(visibleUnit));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("lstm")) {
 				
 				org.deeplearning4j.nn.conf.layers.LSTM.Builder d_builder = new LSTM.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String forgetGateBiasInit = (String) current_layer.get("forgetGateBiasInit");
 				if (forgetGateBiasInit!=null && !forgetGateBiasInit.equals("")) {
 					d_builder.forgetGateBiasInit(Double.parseDouble(forgetGateBiasInit));
 				}

 				String gateActivationFunction = (String) current_layer.get("gateActivationFunction");
 				if (gateActivationFunction!=null && !gateActivationFunction.equals("")) {
					d_builder.gateActivationFunction(Activation.valueOf(gateActivationFunction));
				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("localresponsenormalization")) {
 				
 				org.deeplearning4j.nn.conf.layers.LocalResponseNormalization.Builder d_builder = new LocalResponseNormalization.Builder();

 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}
 				
 				String alpha = (String) current_layer.get("alpha");
 				if (alpha!=null && !alpha.equals("")) {
 					d_builder.alpha(Double.parseDouble(alpha));
 				}
 				
 				String beta = (String) current_layer.get("beta");
 				if (beta!=null && !beta.equals("")) {
 					d_builder.beta(Double.parseDouble(beta));
 				}
 				
 				String k = (String) current_layer.get("k");
 				if (k!=null && !k.equals("")) {
 					d_builder.k(Double.parseDouble(k));
 				}
 				
 				String n = (String) current_layer.get("n");
 				if (n!=null && !n.equals("")) {
 					d_builder.n(Double.parseDouble(n));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("graveslstm")) {
 				
 				org.deeplearning4j.nn.conf.layers.GravesLSTM.Builder d_builder = new GravesLSTM.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String forgetGateBiasInit = (String) current_layer.get("forgetGateBiasInit");
 				if (forgetGateBiasInit!=null && !forgetGateBiasInit.equals("")) {
 					d_builder.forgetGateBiasInit(Double.parseDouble(forgetGateBiasInit));
 				}

 				String gateActivationFunction = (String) current_layer.get("gateActivationFunction");
 				if (gateActivationFunction!=null && !gateActivationFunction.equals("")) {
					d_builder.gateActivationFunction(Activation.valueOf(gateActivationFunction));
				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("gravesbidirectionallstm")) {
 				
 				org.deeplearning4j.nn.conf.layers.GravesBidirectionalLSTM.Builder d_builder = new GravesBidirectionalLSTM.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String forgetGateBiasInit = (String) current_layer.get("forgetGateBiasInit");
 				if (forgetGateBiasInit!=null && !forgetGateBiasInit.equals("")) {
 					d_builder.forgetGateBiasInit(Double.parseDouble(forgetGateBiasInit));
 				}

 				String gateActivationFunction = (String) current_layer.get("gateActivationFunction");
 				if (gateActivationFunction!=null && !gateActivationFunction.equals("")) {
					d_builder.gateActivationFunction(Activation.valueOf(gateActivationFunction));
				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("autoencoder")) {
				
 				org.deeplearning4j.nn.conf.layers.AutoEncoder.Builder d_builder = new AutoEncoder.Builder();
 				
				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String corruptionLevel = (String) current_layer.get("corruptionLevel");
 				if (corruptionLevel!=null && !corruptionLevel.equals("")) {
 					d_builder.corruptionLevel(Double.parseDouble(corruptionLevel));
 				}
 				
 				String preTrainIterations = (String) current_layer.get("preTrainIterations");
 				if (preTrainIterations!=null && !preTrainIterations.equals("")) {
 					d_builder.preTrainIterations(Integer.parseInt(preTrainIterations));
 				}
 				
 				String sparsity = (String) current_layer.get("sparsity");
 				if (sparsity!=null && !sparsity.equals("")) {
 					d_builder.sparsity(Double.parseDouble(sparsity));
 				}
 				
 				String visibleBiasInit = (String) current_layer.get("visibleBiasInit");
 				if (visibleBiasInit!=null && !visibleBiasInit.equals("")) {
 					d_builder.visibleBiasInit(Double.parseDouble(visibleBiasInit));
 				}
				
				String lossFunction = (String) current_layer.get("lossFunction");
				if (lossFunction!=null && !lossFunction.equals("")) {
 					d_builder.lossFunction(LossFunctions.LossFunction.valueOf(lossFunction));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("batchnormalization")) {
 				
 				org.deeplearning4j.nn.conf.layers.BatchNormalization.Builder d_builder = new BatchNormalization.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String eps = (String) current_layer.get("eps");
 				if (eps!=null && !eps.equals("")) {
 					d_builder.eps(Double.parseDouble(eps));
 				}
 				
 				String beta = (String) current_layer.get("beta");
 				if (beta!=null && !beta.equals("")) {
 					d_builder.beta(Double.parseDouble(beta));
 				}
 				
 				String decay = (String) current_layer.get("decay");
 				if (decay!=null && !decay.equals("")) {
 					d_builder.decay(Double.parseDouble(decay));
 				}
 				
 				String gamma = (String) current_layer.get("gamma");
 				if (gamma!=null && !gamma.equals("")) {
 					d_builder.gamma(Double.parseDouble(gamma));
 				}
 				
 				String lockGammaBeta = (String) current_layer.get("lockGammaBeta");
 				if (lockGammaBeta!=null && !lockGammaBeta.equals("")) {
 					d_builder.lockGammaBeta(convert_bool(lockGammaBeta));
 				}
 				
 				String minibatch = (String) current_layer.get("minibatch");
 				if (minibatch!=null && !minibatch.equals("")) {
 					d_builder.minibatch(convert_bool(minibatch));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("globalpoolinglayer")) {

 				org.deeplearning4j.nn.conf.layers.GlobalPoolingLayer.Builder d_builder = new GlobalPoolingLayer.Builder();

 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_pooling = (String) current_layer.get("poolingType");
 				if (l_pooling!=null && !l_pooling.equals("")) {
 					d_builder.poolingType(PoolingType.valueOf(l_pooling));
				}
 				
 				String pnorm = (String) current_layer.get("pnorm");
 				if (pnorm!=null && !pnorm.equals("")) {
 					d_builder.pnorm(Integer.parseInt(pnorm));
 				}
 				
 				String collapseDimensions = (String) current_layer.get("collapseDimensions");
 				if (collapseDimensions!=null && !collapseDimensions.equals("")) {
 					d_builder.collapseDimensions(convert_bool(collapseDimensions));
 				}
				
 				String l_poolingDimensions = (String) current_layer.get("poolingDimensions");
				int[] poolingDimensions = new int[Integer.parseInt(AtomFx.size(l_poolingDimensions, ":"))];
				for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_poolingDimensions, ":")); ik++) {
					poolingDimensions[ik-1] = Integer.parseInt(AtomFx.get(l_poolingDimensions, ""+ik, ":"));
				}
				d_builder.poolingDimensions(poolingDimensions);
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("emmbeddinglayer")) {
 				
 				org.deeplearning4j.nn.conf.layers.EmbeddingLayer.Builder d_builder = new EmbeddingLayer.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("dropoutlayer")) {

 				org.deeplearning4j.nn.conf.layers.DropoutLayer.Builder d_builder = new DropoutLayer.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("activationlayer")) {
 				
 				org.deeplearning4j.nn.conf.layers.ActivationLayer.Builder d_builder = new ActivationLayer.Builder();

 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("denselayer")) {
 				
 				org.deeplearning4j.nn.conf.layers.DenseLayer.Builder d_builder = new DenseLayer.Builder();

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("convolutionlayer")) {
				
 				String l_kernelSize = (String) current_layer.get("kernelSize");
				int[] kernelSize = new int[Integer.parseInt(AtomFx.size(l_kernelSize, ":"))];
				for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_kernelSize, ":")); ik++) {
					kernelSize[ik-1] = Integer.parseInt(AtomFx.get(l_kernelSize, ""+ik, ":"));
				}
				
 				org.deeplearning4j.nn.conf.layers.ConvolutionLayer.Builder d_builder = new ConvolutionLayer.Builder(kernelSize);

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
				String l_stride = (String) current_layer.get("stride");
				if (l_stride!=null && !l_stride.equals("")) {
 					int[] stride = new int[Integer.parseInt(AtomFx.size(l_stride, ":"))];
 					for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_stride, ":")); ik++) {
 						stride[ik-1] = Integer.parseInt(AtomFx.get(l_stride, ""+ik, ":"));
 					}
 					d_builder.stride(stride);
				}
 				
				String l_padding = (String) current_layer.get("padding");
 				if (l_padding!=null && !l_padding.equals("")) {
 					int[] padding = new int[Integer.parseInt(AtomFx.size(l_padding, ":"))];
 					for(int ik=1;ik<=Integer.parseInt(AtomFx.size(l_padding, ":")); ik++) {
 						padding[ik-1] = Integer.parseInt(AtomFx.get(l_padding, ""+ik, ":"));
 					}
 					d_builder.padding(padding);
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String epsilon = (String) current_layer.get("epsilon");
 				if (epsilon!=null && !epsilon.equals("")) {
 					d_builder.epsilon(Double.parseDouble(epsilon));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String momentum = (String) current_layer.get("momentum");
 				if (momentum!=null && !momentum.equals("")) {
 					d_builder.momentum(Double.parseDouble(momentum));
 				}
 				
 				String rho = (String) current_layer.get("rho");
 				if (rho!=null && !rho.equals("")) {
 					d_builder.rho(Double.parseDouble(rho));
 				}
 				
 				String rmsDecay = (String) current_layer.get("rmsDecay");
 				if (rmsDecay!=null && !rmsDecay.equals("")) {
 					d_builder.rmsDecay(Double.parseDouble(rmsDecay));
 				}
 				
 				String l_convolutionMode = (String) current_layer.get("convolutionMode");
 				if (l_convolutionMode!=null && !l_convolutionMode.equals("")) {
 					d_builder.convolutionMode(ConvolutionMode.valueOf(l_convolutionMode));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				String adamMeanDecay = (String) current_layer.get("adamMeanDecay");
 				if (adamMeanDecay!=null && !adamMeanDecay.equals("")) {
 					d_builder.adamMeanDecay(Double.parseDouble(adamMeanDecay));
 				}
 				
 				String adamVarDecay = (String) current_layer.get("adamVarDecay");
 				if (adamVarDecay!=null && !adamVarDecay.equals("")) {
 					d_builder.adamVarDecay(Double.parseDouble(adamVarDecay));
 				}
 				
 				String cudnnAlgoMode = (String) current_layer.get("cudnnAlgoMode");
 				if (cudnnAlgoMode!=null && !cudnnAlgoMode.equals("")) {
 					d_builder.cudnnAlgoMode(AlgoMode.valueOf(cudnnAlgoMode));
 				}
 				
 				String cudnnBwdDataMode = (String) current_layer.get("cudnnBwdDataMode");
 				if (cudnnBwdDataMode!=null && !cudnnBwdDataMode.equals("")) {
 					d_builder.cudnnBwdDataMode(org.deeplearning4j.nn.conf.layers.ConvolutionLayer.BwdDataAlgo.valueOf(cudnnBwdDataMode));
 				}
 				
 				String cudnnBwdFilterMode = (String) current_layer.get("cudnnBwdFilterMode");
 				if (cudnnBwdFilterMode!=null && !cudnnBwdFilterMode.equals("")) {
 					d_builder.cudnnBwdFilterMode(org.deeplearning4j.nn.conf.layers.ConvolutionLayer.BwdFilterAlgo.valueOf(cudnnBwdFilterMode));
 				}
 				
 				String cudnnFwdMode = (String) current_layer.get("cudnnFwdMode");
 				if (cudnnFwdMode!=null && !cudnnFwdMode.equals("")) {
 					d_builder.cudnnFwdMode(org.deeplearning4j.nn.conf.layers.ConvolutionLayer.FwdAlgo.valueOf(cudnnFwdMode));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("outputlayer")) {
				
				String lossFunction = (String) current_layer.get("lossFunction");
				
				org.deeplearning4j.nn.conf.layers.OutputLayer.Builder d_builder = null;
				
				if (lossFunction==null || lossFunction.equals("")) {
					d_builder = new OutputLayer.Builder();
				} else {
					d_builder = new OutputLayer.Builder(LossFunctions.LossFunction.valueOf(lossFunction));
				}
				
				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("centerlossoutputlayer")) {
				
				String lossFunction = (String) current_layer.get("lossFunction");
				
				org.deeplearning4j.nn.conf.layers.CenterLossOutputLayer.Builder d_builder = null;
				
				if (lossFunction==null || lossFunction.equals("")) {
					d_builder = new CenterLossOutputLayer.Builder();
				} else {
					d_builder = new CenterLossOutputLayer.Builder(LossFunctions.LossFunction.valueOf(lossFunction));
				}

				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String alpha = (String) current_layer.get("alpha");
 				if (alpha!=null && !alpha.equals("")) {
 					d_builder.alpha(Double.parseDouble(alpha));
 				}
 				
 				String lambda = (String) current_layer.get("lambda");
 				if (lambda!=null && !lambda.equals("")) {
 					d_builder.lambda(Double.parseDouble(lambda));
 				}
 				
 				String gradientCheck = (String) current_layer.get("gradientCheck");
 				if (gradientCheck!=null && !gradientCheck.equals("")) {
 					d_builder.gradientCheck(convert_bool(gradientCheck));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
				
 				list_builder.layer(i_layer, d_builder.build());
 				
			} else if (type_layer.toLowerCase().equals("rnnoutputlayer")) {
				
				String lossFunction = (String) current_layer.get("lossFunction");
				
				org.deeplearning4j.nn.conf.layers.RnnOutputLayer.Builder d_builder = null;
				
				if (lossFunction==null || lossFunction.equals("")) {
					d_builder = new RnnOutputLayer.Builder();
				} else {
					d_builder = new RnnOutputLayer.Builder(LossFunctions.LossFunction.valueOf(lossFunction));
				}
				
				String nIn = (String) current_layer.get("nIn");
 				String nOut = (String) current_layer.get("nOut");
 				if (nIn!=null && !nIn.equals("")) {
 					d_builder.nIn(Integer.parseInt(nIn));
 				}
 				if (nOut!=null && !nOut.equals("")) {
 					d_builder.nOut(Integer.parseInt(nOut));
 				}
 				
 				String dropOut = (String) current_layer.get("dropOut");
 				if (dropOut!=null && !dropOut.equals("")) {
 					d_builder.dropOut(Double.parseDouble(dropOut));
 				}

 				String l_activation = (String) current_layer.get("activation");
 				if (l_activation!=null && !l_activation.equals("")) {
					d_builder.activation(Activation.valueOf(l_activation));
				}

 				String l_weightInit = (String) current_layer.get("weightInit");
 				if (l_weightInit!=null && !l_weightInit.equals("")) {
					d_builder.weightInit(WeightInit.valueOf(l_weightInit));
				}
 				
 				String dist = (String) current_layer.get("dist");
 				if (dist!=null && !dist.equals("")) {
 					
 					if (dist.toLowerCase().startsWith("uniformdistribution")) {
 						d_builder.dist(new UniformDistribution(Double.parseDouble(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("binomialdistribution")) {
 						d_builder.dist(new BinomialDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					} else if (dist.toLowerCase().startsWith("normaldistribution")) {
 						d_builder.dist(new NormalDistribution(Integer.parseInt(AtomFx.get(dist, "2", ":")), Double.parseDouble(AtomFx.get(dist, "3", ":"))));
 					}
 				}
 				
 				String biasInit = (String) current_layer.get("biasInit");
 				if (biasInit!=null && !biasInit.equals("")) {
 					d_builder.biasInit(Double.parseDouble(biasInit));
 				}
 				
 				String biasLearningRate = (String) current_layer.get("biasLearningRate");
 				if (biasLearningRate!=null && !biasLearningRate.equals("")) {
 					d_builder.biasLearningRate(Double.parseDouble(biasLearningRate));
 				}
 				
 				String l1 = (String) current_layer.get("l1");
 				if (l1!=null && !l1.equals("")) {
 					d_builder.l1(Double.parseDouble(l1));
 				}
 				
 				String l1Bias = (String) current_layer.get("l1Bias");
 				if (l1Bias!=null && !l1Bias.equals("")) {
 					d_builder.l1Bias(Double.parseDouble(l1Bias));
 				}
 				
 				String l2 = (String) current_layer.get("l2");
 				if (l2!=null && !l2.equals("")) {
 					d_builder.l2(Double.parseDouble(l2));
 				}
 				
 				String l2Bias = (String) current_layer.get("l2Bias");
 				if (l2Bias!=null && !l2Bias.equals("")) {
 					d_builder.l2Bias(Double.parseDouble(l2Bias));
 				}
 				
 				String l_gradientNormalization = (String) current_layer.get("gradientNormalization");
 				if (l_gradientNormalization!=null && !l_gradientNormalization.equals("")) {
 					d_builder.gradientNormalization(GradientNormalization.valueOf(l_gradientNormalization));
 				}
 				
 				String gradientNormalizationThreshold = (String) current_layer.get("gradientNormalizationThreshold");
 				if (gradientNormalizationThreshold!=null && !gradientNormalizationThreshold.equals("")) {
 					d_builder.gradientNormalizationThreshold(Double.parseDouble(gradientNormalizationThreshold));
 				}
 				
 				String learningRate = (String) current_layer.get("learningRate");
 				if (learningRate!=null && !learningRate.equals("")) {
 					d_builder.learningRate(Double.parseDouble(learningRate));
 				}
 				
 				String l_learningRateDecayPolicy = (String) current_layer.get("learningRateDecayPolicy");
 				if (l_learningRateDecayPolicy!=null && !l_learningRateDecayPolicy.equals("")) {
 					d_builder.learningRateDecayPolicy(LearningRatePolicy.valueOf(l_learningRateDecayPolicy));
 				}
 				
 				String l_updater = (String) current_layer.get("updater");
 				if (l_updater!=null && !l_updater.equals("")) {
 					d_builder.updater(Updater.valueOf(l_updater));
 				}
 				
 				list_builder.layer(i_layer, d_builder.build());
 				
			}
			
		}
		
		//###################################################################################################################
		
		if (conf_json.get("backprop")!=null && !(""+conf_json.get("backprop")).equals("")) {
			list_builder.backprop(convert_bool(""+conf_json.get("backprop")));
		}
		if (conf_json.get("pretrain")!=null && !(""+conf_json.get("pretrain")).equals("")) {
			list_builder.pretrain(convert_bool(""+conf_json.get("pretrain")));
		}
		
		MultiLayerConfiguration conf = list_builder.build();
		
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(100));
		
		DataSet testData = readCSVDataset(csv_test, Integer.parseInt(testBatchSize), labelIndex, numClasses);
		normalizer.transform(testData);

		Evaluation eval = null;
		
		System.out.println("Train model ...");
		for (int epoch = 0; epoch < Integer.parseInt(""+conf_json.get("epochs")); epoch++) {
			System.out.println("- Start epoch "+(epoch+1)+" ...");
	        model.fit(trainingData);
	        System.out.println("- End epoch "+(epoch+1)+".");
	        System.out.println("Evaluate model....");
	        
	        eval = new Evaluation(numClasses);
	        INDArray output = model.output(testData.getFeatureMatrix());
	        eval.eval(testData.getLabels(), output);
	        System.out.println(eval.stats());
	        
	    }
		
		//Save the model
		System.out.println("Save model ...");
		ModelSerializer.writeModel(model, new File(pathToSaveModel), false);
		
		NormalizerSerializer saver = NormalizerSerializer.getDefault();
		File normalsFile = new File(pathToSaveNormalizer);
		saver.write(normalizer,normalsFile);
		
		System.out.println("Completed.");
		
		return eval.stats();
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {
		
		JSONArray result = new JSONArray();
		
		for (Object entry : env.dl4jModel.keySet()) {
			result.add(entry.toString());
		}
		
		return result;
		
	}
	
	public static boolean convert_bool(String str) throws Exception {
		
		if (str==null) {
			return false;
		} else if (str.equals("1") || str.toLowerCase().equals("true")) {
			return true;
		} else {
			return false;
		}
		
	}

	public static void load_model(EnvManager env, String dl4jId, String json_config) throws Exception {
		
		JSONObject conf_json = (JSONObject) JsonManager.load(json_config);

		String pathToLoadModel = (String) conf_json.get("pathToSaveModel");
		String pathToLoadNormalizer = (String) conf_json.get("pathToSaveNormalizer");

		//Load the model
		env.dl4jModel.put(dl4jId, ModelSerializer.restoreMultiLayerNetwork(new File(pathToLoadModel)));
		
		NormalizerSerializer loader = NormalizerSerializer.getDefault();
		env.dl4jNormalizer.put(dl4jId, loader.restore(pathToLoadNormalizer));

	}

	@SuppressWarnings("unchecked")
	public static String predict_row(EnvManager env, String dl4jId, String json_config, String csv_test, String testBatchSize) throws Exception {
		
		if (!env.dl4jModel.containsKey(dl4jId)) {
			throw new Exception("Sorry, the DL4J object '"+dl4jId+"' does not exist.");
		}
		
		JSONObject conf_json = (JSONObject) JsonManager.load(json_config);

		int numClasses = Integer.parseInt(""+conf_json.get("numClasses"));
		int labelIndex = Integer.parseInt(""+conf_json.get("labelIndex"));
		
		MultiLayerNetwork model = env.dl4jModel.get(dl4jId);
		DataNormalization normalizer = env.dl4jNormalizer.get(dl4jId);
		
		DataSet testData = readCSVDataset(csv_test, Integer.parseInt(testBatchSize), labelIndex, numClasses);
		normalizer.transform(testData);
		
        INDArray networkPrediction = model.output(testData.getFeatureMatrix());
        
        JSONArray result = new JSONArray();
        for(int j=0;j<networkPrediction.rows();j++) {
	    		
        		INDArray row = networkPrediction.getRow(j);
        		
        		JSONObject line = new JSONObject();
        		JSONArray result_a = new JSONArray();
        		line.put("probs", result_a);
        		
        		for(int i=0;i<row.columns();i++) {
            		result_a.add(row.getFloat(i));
            }
        		
        		line.put("prob_index", maxIndex(getFloatArrayFromSlice(row)));
        		line.put("prob_class", "class"+(maxIndex(getFloatArrayFromSlice(row))+1));
        		
        		result.add(line);
        	
	    }
        
		return result.toString();

	}

	public static DataSet readCSVDataset(String csvFileClasspath, int batchSize, int labelIndex, int numClasses)
			throws IOException, InterruptedException {

		RecordReader rr = new CSVRecordReader();
		rr.initialize(new FileSplit(new File(csvFileClasspath)));
		DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, labelIndex, numClasses);
		return iterator.next();
	}

	private static float[] getFloatArrayFromSlice(INDArray rowSlice) {
		float[] result = new float[rowSlice.columns()];
		for (int i = 0; i < rowSlice.columns(); i++) {
			result[i] = rowSlice.getFloat(i);
		}
		return result;
	}

	private static int maxIndex(float[] vals) {
		int maxIndex = 0;
		for (int i = 1; i < vals.length; i++) {
			float newnumber = vals[i];
			if ((newnumber > vals[maxIndex])) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}
