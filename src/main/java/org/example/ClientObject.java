package org.example;

public class ClientObject {

    private String clientId;
    private String clientSocketId;
    private String clientGradient;
    private String extraMsg;

    public ClientObject() {
    }

    public ClientObject(String clientId, String clientSocketId, String clientGradient, String extraMsg) {
        this.clientId = clientId;
        this.clientSocketId = clientSocketId;
        this.clientGradient = clientGradient;
        this.extraMsg = extraMsg;
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

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }
}
