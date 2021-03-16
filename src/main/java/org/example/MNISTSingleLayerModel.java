package org.example;

import com.google.gson.JsonObject;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.nn.api.Layer;
import org.json.JSONObject;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MNISTSingleLayerModel {

    private static Logger log = LoggerFactory.getLogger(MNISTSingleLayerModel.class);

    private MultiLayerNetwork model;
    private Layer[][] currentWeights;


    private MultiLayerNetwork buildModel() {

        int numRows = 28;
        int numColumns = 28;
        int outputNum = 10; // number of output classes
        int randomSeed = 123; // random number seed for reproducibility

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(randomSeed) //include a random seed for reproducibility
                // use stochastic gradient descent as an optimization algorithm
                .updater(new Nesterovs(0.006, 0.9))
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder() //create the first, input layer with xavier initialization
                        .nIn(numRows * numColumns)
                        .nOut(1000)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .nIn(1000)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        return model;
    }


    public static void main(String[] args) throws Exception {

        log.info("Build model....");


        MNISTSingleLayerModel mnist = new MNISTSingleLayerModel();
        MultiLayerNetwork model = mnist.buildModel();

        int batchSize = 128; // batch size for each epoch
        int numEpochs = 15; // number of epochs to perform
        int seed = 123;


        //Get the DataSetIterators:
        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, seed);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, seed);





        JSONObject jsonObject = new JSONObject();


        int layerNum = model.getLayers().length;
        jsonObject.put("layerNum", layerNum);

        String key;
        for (int i = 0; i < layerNum; i++) {
            key = i + "_W";
            jsonObject.put(key, model.getParam(key));
            key = i + "_b";
            jsonObject.put(key, model.getParam(key));
        }

        INDArray paramStr;
        layerNum = jsonObject.getInt("layerNum");
        for (int i = 0; i < layerNum; i++) {
            key = i + "_W";
            paramStr = (INDArray)jsonObject.get(key);
            model.setParam(key, paramStr);
            key = i + "_b";
            paramStr = (INDArray)jsonObject.get(key);
            model.setParam(key, paramStr);
        }
        
//        for (Layer layer : model.getLayers()) {
//            INDArray w = layer.getParam("W");
//            INDArray b = layer.getParam("b");
////            System.out.println(w.toString());
//            INDArray p = layer.params();
//            System.out.println(p.shapeInfoToString());
////            log.info("Print params....");
////            long w_row = w.shape()[0];
////            long w_col = w.shape()[1];
////            log.info("w : [" + w_row + " ," + w_col + "]");
////            long b_row = b.shape()[0];
////            long b_col = b.shape()[1];
////            log.info("b : [" + b_row + " ," + b_col + "]");
////        }


//        //print the score with every 1 iteration
//        model.setListeners(new ScoreIterationListener(1));
//
//        log.info("Train model....");
//        model.fit(mnistTrain, numEpochs);
//
//
//        log.info("Evaluate model....");
//        Evaluation eval = model.evaluate(mnistTest);
//        log.info(eval.stats());
//        log.info("****************Example finished********************");

    }
}
