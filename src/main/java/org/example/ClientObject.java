package org.example;

public class ClientObject {

    private String clientId;
    private String clientSocketId;
    private String clientGradient;

    public ClientObject() {
    }

    public ClientObject(String clientId, String clientSocketId, String clientGradient) {
        this.clientId = clientId;
        this.clientSocketId = clientSocketId;
        this.clientGradient = clientGradient;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSocketId() {
        return clientSocketId;
    }

    public void setClientSocketId(String clientSocketId) {
        this.clientSocketId = clientSocketId;
    }

    public String getClientGradient() {
        return clientGradient;
    }

    public void setClientGradient(String clientGradient) {
        this.clientGradient = clientGradient;
    }
}
