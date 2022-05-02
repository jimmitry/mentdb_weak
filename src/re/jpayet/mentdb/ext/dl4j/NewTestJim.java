package re.jpayet.mentdb.ext.dl4j;


import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
//import org.deeplearning4j.ui.storage.FileStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NewTestJim { 
	
    protected static final Logger log = LoggerFactory.getLogger(NewTestJim.class);
	
    protected static int height=32;
    protected static int width=32;

    protected static int channels = 3;
    protected static int batchSize=150;// tested 50, 100, 200
    protected static long seed = 123;
    protected static Random rng = new Random(seed);
    protected static int nEpochs = 10; // tested 50, 100, 200
    protected static double splitTrainTest = 0.8;
    protected static boolean save = true;
    private static int numLabels;

    public static void execute(String args[]) throws Exception{

        System.out.println("Loading data....");

        /**
         * Setting up data
         */
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        File mainPath = new File("/Users/jimmitry/Dropbox/INNOV-AI/LAB/MentDB_Server_2/demo/animals2");
        FileSplit fileSplit = new FileSplit(mainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
        int numExamples = Math.toIntExact(fileSplit.length());
        numLabels = fileSplit.getRootDir().listFiles(File::isDirectory).length; //This only works if your root is clean: only label subdirs.
        BalancedPathFilter pathFilter = new BalancedPathFilter(rng, labelMaker, numExamples, numLabels, batchSize);

        /**
         * Split data: 80% training and 20% testing
         */
        InputSplit[] inputSplit = fileSplit.sample(pathFilter, splitTrainTest, 1 - splitTrainTest);
        InputSplit trainData = inputSplit[0];
        InputSplit testData = inputSplit[1];

        /**
         *  Create extra synthetic training data by flipping, rotating
         #  images on our data set.
         */
        ImageTransform flipTransform1 = new FlipImageTransform(rng);
        ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));

        List<ImageTransform> transforms = Arrays.asList(new ImageTransform[]{flipTransform1, flipTransform2});
        /**
         * Normalization
         **/
        System.out.println("Fitting to dataset");
        ImagePreProcessingScaler preProcessor = new ImagePreProcessingScaler(0, 1);
        /**
         * Define our network architecture:
          */
        
        NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
        builder.seed(seed);
        builder.l2(0.005);
        builder.activation(Activation.RELU);
        builder.weightInit(WeightInit.XAVIER);
        builder.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT);
        ListBuilder list_builder = builder.list();
        list_builder.layer(0, convInit("cnn1", channels, 50 ,  new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0));
        list_builder.layer(1, maxPool("maxpool1", new int[]{2,2}));
        list_builder.layer(2, conv3x3("cnn2", 64, 0));
        list_builder.layer(3, conv3x3("cnn3", 64,1));
        list_builder.layer(4, maxPool("maxpool2", new int[]{2,2}));
        list_builder.layer(5, new DenseLayer.Builder().activation(Activation.RELU)
                .nOut(512).dropOut(0.5).build());
        list_builder.layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                .nOut(numLabels)
                .activation(Activation.SOFTMAX)
                .build());
        
        MultiLayerConfiguration conf = list_builder.build();
        
        
        System.out.println("Build model....");
        /*MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.005) // tried 0.0001, 0.0005
                .activation(Activation.RELU)
                //.learningRate(0.05) // tried 0.001, 0.005, 0.01
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list()
                .layer(0, convInit("cnn1", channels, 32 ,  new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0))
                .layer(1, maxPool("maxpool1", new int[]{2,2}))
                .layer(2, conv3x3("cnn2", 64, 0))
                .layer(3, conv3x3("cnn3", 64,1))
                .layer(4, maxPool("maxpool2", new int[]{2,2}))
                .layer(5, new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(512).dropOut(0.5).build())
                .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                        .nOut(numLabels)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutional(height, width, channels))
                .build();*/
        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        
        
        //UIServer uiServer = UIServer.getInstance();
        //StatsStorage statsStorage = new FileStatsStorage(new File(System.getProperty("java.io.tmpdir"), "ui-stats.dl4j"));
        //uiServer.attach(statsStorage);

        network.init();
        network.setListeners(new ScoreIterationListener(1));
        
        /**
         * Load data
         */
        System.out.println("Load data....");
        ImageRecordReader recordReader = new ImageRecordReader(height, width, channels, labelMaker);
        DataSetIterator dataIter;
        MultipleEpochsIterator trainIter;
        
        log.info("Train model....");

        System.out.println("Train model....");
        // Train without transformations
        recordReader.initialize(trainData, null);
        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
        preProcessor.fit(dataIter);
        dataIter.setPreProcessor(preProcessor);
        trainIter = new MultipleEpochsIterator(nEpochs, dataIter);
        network.fit(trainIter);

        // Train with transformations
        for (ImageTransform transform : transforms) {
            System.out.print("\nTraining on transformation: " + transform.getClass().toString() + "\n\n");
            recordReader.initialize(trainData, transform);
            dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
            preProcessor.fit(dataIter);
            dataIter.setPreProcessor(preProcessor);
            trainIter = new MultipleEpochsIterator(nEpochs, dataIter);
            network.fit(trainIter);
        }

        System.out.println("Evaluate model....");
        recordReader.initialize(testData);
        dataIter = new RecordReaderDataSetIterator(recordReader, batchSize, 1, numLabels);
        preProcessor.fit(dataIter);
        dataIter.setPreProcessor(preProcessor);
        Evaluation eval = network.evaluate(dataIter);
        System.out.println(eval.stats(true));

        if (save) {
            System.out.println("Save model....");
            ModelSerializer.writeModel(network,  "/Users/jimmitry/Dropbox/INNOV-AI/LAB/MentDB_Server_2/demo/bird2.bin", true);
        }
        System.out.println("**************** Bird Classification finished ********************");
    }
    
    private static ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
    }
    
    private static ConvolutionLayer conv3x3(String name, int out, double bias) {
        return new ConvolutionLayer.Builder(new int[]{3,3}, new int[] {1,1}, new int[] {1,1}).name(name).nOut(out).biasInit(bias).build();
    }

    private static SubsamplingLayer maxPool(String name, int[] kernel) {
        return new SubsamplingLayer.Builder(kernel, new int[]{2,2}).name(name).build();
    }
}