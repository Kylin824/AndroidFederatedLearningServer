package org.example;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class FLServer {

    private static final Integer MIN_NUM_WORKERS = 1;
    private static final Integer MAX_NUM_ROUNDS = 50;
    private static final Integer NUM_CLIENTS_CONTACTED_PER_ROUND = 2;
    private static final Integer ROUNDS_BETWEEN_VALIDATIONS = 2;


    private String modelId;
    private HashSet<UUID> readyClientSids = new HashSet<>();
    private Integer currentRound = -1;
    private ArrayList<ClientUpdateObject> currentRoundClientUpdates = new ArrayList<>();
    private ArrayList<ClientEvalObject> evalClientUpdates = new ArrayList<>();
    private SocketIOServer socketIOServer;

    public FLServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        socketIOServer = new SocketIOServer(config);
        modelId = UUID.randomUUID().toString();
        registerHandles();
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
            }
        });

        socketIOServer.addEventListener("client_wake_up", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String str, AckRequest ackRequest) {
                System.out.println("client wake up : " + socketIOClient.getSessionId());

                // emit client init settings
                ClientInitObject clientInitObject = new ClientInitObject();
                clientInitObject.setInitWeights("init_weights");
                clientInitObject.setBatchSize(50);
                clientInitObject.setClientIndex(0);
                clientInitObject.setEpoch(1);

                socketIOServer.getClient(socketIOClient.getSessionId()).sendEvent("init", clientInitObject);
            }
        });

        socketIOServer.addEventListener("client_ready", ClientReadyObject.class, new DataListener<ClientReadyObject>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ClientReadyObject obj, AckRequest ackRequest) {
                System.out.println("client ready : " + socketIOClient.getSessionId() + " train size: " + obj.getTrainSize());
                readyClientSids.add(socketIOClient.getSessionId());
                if (readyClientSids.size() >= FLServer.MIN_NUM_WORKERS && currentRound == -1)
                    trainNextRound();
            }
        });

        socketIOServer.addEventListener("client_update", ClientUpdateObject.class, new DataListener<ClientUpdateObject>() {
            @Override
            public void onData(SocketIOClient socketIOClient, ClientUpdateObject data, AckRequest ackRequest) {
                System.out.println("client update : " + socketIOClient.getSessionId());
                // 舍弃不是本轮的更新
                if (data.getRoundNumber().equals(currentRound)) {
                    currentRoundClientUpdates.add(data);
                    System.out.println("client update weights : " + data.getWeights());
                    if (currentRound >= FLServer.MAX_NUM_ROUNDS) {
                        stopAndEval();
                    }
                    else {
                        trainNextRound();
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
            requestUpdateObject.setWeights("Weight_Round" + currentRound);
            requestUpdateObject.setModelId(modelId);
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
