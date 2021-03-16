package org.example;

import org.json.JSONObject;

public class ClientUpdateObject {
    private Integer roundNumber;
    private JSONObject weights;

    public ClientUpdateObject() {
    }

    public ClientUpdateObject(Integer roundNumber, JSONObject weights) {
        this.roundNumber = roundNumber;
        this.weights = weights;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public JSONObject getWeights() {
        return weights;
    }

    public void setWeights(JSONObject weights) {
        this.weights = weights;
    }
}
