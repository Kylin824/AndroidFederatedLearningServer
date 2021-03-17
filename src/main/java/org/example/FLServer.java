package org.example;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class FLServer {

    private static final Integer MIN_NUM_WORKERS = 1;
    private static final Integer MAX_NUM_ROUNDS = 50;
    private static final Integer NUM_CLIENTS_CONTACTED_PER_ROUND = 1;
    private static final Integer ROUNDS_BETWEEN_VALIDATIONS = 2;


    private HashSet<UUID> readyClientSids = new HashSet<>();
    private Integer currentRound = 0;
    private ArrayList<ClientUpdateObject> currentRoundClientUpdates = new ArrayList<>();
    private ArrayList<ClientEvalObject> evalClientUpdates = new ArrayList<>();
    private SocketIOServer socketIOServer;
    private Integer clientUpdateAmount;
    private MultiLayerNetwork globalModel;

    public FLServer() {
        globalModel = buildGlobalModel();
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        socketIOServer = new SocketIOServer(config);
//        modelId = UUID.randomUUID().toString();
        registerHandles();
    }

    private MultiLayerNetwork buildGlobalModel() {

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
                        .nOut(100)
                        .activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .nIn(100)
                        .nOut(outputNum)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        return model;
    }

    private void registerHandles() {

        socketIOServer.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                // broadcast messages to all clients
                System.out.println(data.getUserName() + " : " + data.getMessage());
                socketIOServer.getBroadcastOperations().sendEvent("chatevent", data);

            }
        });

        // 新连接
        socketIOServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                UUID clientId = socketIOClient.getSessionId();
                System.out.println("client connected : " + clientId);
            }
        });

        // 断开连接
        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                UUID clientId = socketIOClient.getSessionId();
                System.out.println("client disconnected: " + clientId);
                readyClientSids.remove(clientId);
            }
        });

        socketIOServer.addEventListener("client_wake_up", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String str, AckRequest ackRequest) {
                System.out.println("client wake up : " + socketIOClient.getSessionId());

                // emit client init settings
                ClientInitObject clientInitObject = new ClientInitObject();
                clientInitObject.setArrW0(ModelUtils.model0WToJsonArray(globalModel));
                clientInitObject.setArrB0(ModelUtils.model0BToJsonArray(globalModel));
                clientInitObject.setArrW1(ModelUtils.model1WToJsonArray(globalModel));
                clientInitObject.setArrB1(ModelUtils.model1BToJsonArray(globalModel));
                clientInitObject.setBatchSize(50);
                clientInitObject.setClientIndex(0);
                clientInitObject.setEpoch(1);
                clientInitObject.setLayerNum(globalModel.getLayers().length);

//                JSONArray arr = new JSONArray();
//
//                for (int i = 0; i < 800000; i++) {
//                    arr.put(0.001d);
//                }
//
//                JSONArray arr1 = new JSONArray();
//                arr1.put(10d);
//                arr1.put(20d);
//                JSONArray arr2 = new JSONArray();
//                arr2.put(30d);
//                arr2.put(0.9d);

//                clientInitObject.setArrW(arr);
//                clientInitObject.setArrB(arr);
//                arr.put(arr1);
//                arr.put(arr2);
//
                System.out.println("len: " + clientInitObject.getArrW0().length());
//                System.out.println(clientInitObject.getArrB().get(0));

                System.out.println("init model");

                System.out.println("send init object");

                socketIOServer.getClient(socketIOClient.getSessionId()).sendEvent("init", clientInitObject);
            }
        });

        socketIOServer.addEventListener("client_ready", ClientReadyObject.class, new DataListener<ClientReadyObject>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ClientReadyObject obj, AckRequest ackRequest) {
                System.out.println("client ready : " + socketIOClient.getSessionId() + " train size: " + obj.getTrainSize());
                readyClientSids.add(socketIOClient.getSessionId());
                if (readyClientSids.size() >= FLServer.MIN_NUM_WORKERS && currentRound == 0)
                    trainNextRound();
            }
        });

        socketIOServer.addEventListener("client_update", ClientUpdateObject.class, new DataListener<ClientUpdateObject>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ClientUpdateObject data, AckRequest ackRequest) {
                System.out.println("client update : " + socketIOClient.getSessionId());
                // 舍弃不是本轮的更新
                if (data.getRoundNumber().equals(currentRound)) {
                    clientUpdateAmount += 1;
                    currentRoundClientUpdates.add(data);
                    System.out.println("client update weights ");

                    // 收到足够数量的更新，舍弃其他超时的
                    if (clientUpdateAmount >= FLServer.NUM_CLIENTS_CONTACTED_PER_ROUND && currentRoundClientUpdates.size() > 0) {

                        // 更新全局模型
                        globalModel = ModelUtils.updateGlobalModel(currentRoundClientUpdates, globalModel);


                        System.out.println("update global model success");
                        // TODO 测试全局模型精度


//                        if (currentRound >= FLServer.MAX_NUM_ROUNDS) {
//                            stopAndEval();
//                        }
//                        else {
//                            trainNextRound();
//                        }
                    }
                }
            }
        });
    }

    public void trainNextRound() {
        currentRound += 1;
        currentRoundClientUpdates.clear();
        System.out.println("### Round " + currentRound + " ###");

        for (UUID clientId : readyClientSids) {
            RequestUpdateObject requestUpdateObject = new RequestUpdateObject();
//            requestUpdateObject.setWeights(ModelUtils.modelToJson(globalModel));
            requestUpdateObject.setArrW0(ModelUtils.model0WToJsonArray(globalModel));
            requestUpdateObject.setArrB0(ModelUtils.model0BToJsonArray(globalModel));
            requestUpdateObject.setArrW1(ModelUtils.model1WToJsonArray(globalModel));
            requestUpdateObject.setArrB1(ModelUtils.model1BToJsonArray(globalModel));
            requestUpdateObject.setCurrentRound(currentRound);
            socketIOServer.getClient(clientId).sendEvent("request_update", requestUpdateObject);
        }
    }

    public void stopAndEval() {
        evalClientUpdates.clear();

        // 测试全局精度
        Float globalTestAcc = 0.9f;
        ClientEvalObject clientEvalObject = new ClientEvalObject();
        clientEvalObject.setFinalGlobalTestAcc(globalTestAcc);
        // 测试本地精度
        for (UUID clientId : readyClientSids) {
            socketIOServer.getClient(clientId).sendEvent("stop_and_eval", clientEvalObject);
        }
    }

    public void start() throws InterruptedException {
        socketIOServer.start();
        Thread.sleep(Integer.MAX_VALUE);
        socketIOServer.stop();
    }

    public static void main(String[] args) throws Exception {
        FLServer server = new FLServer();
        server.start();
    }
}
