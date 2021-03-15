package org.example;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

import java.util.UUID;

public class FLServer {

    private static UUID clientId;


    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);


        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                clientId = socketIOClient.getSessionId();
                System.out.println("connect! socket id : " + socketIOClient.getSessionId());

            }
        });

        server.addEventListener("new message", ClientObject.class, new DataListener<ClientObject>() {
            @Override
            public void onData(SocketIOClient client, ClientObject clientObject, AckRequest ackRequest) {
                // broadcast messages to all clients

                System.out.println(clientObject.getClientId());
                System.out.println(clientObject.getClientGradient());
                System.out.println(clientObject.getClientSocketId());
                System.out.println(clientObject.getExtraMsg());

            }
        });

        server.addEventListener("add user", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String str, AckRequest ackRequest) throws Exception {
                System.out.println("add user: " + str);

            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

    static class LoginObject {
        private int numUsers;

        public int getNumUsers() {
            return numUsers;
        }

        public void setNumUsers(int numUsers) {
            this.numUsers = numUsers;
        }
    }
}
