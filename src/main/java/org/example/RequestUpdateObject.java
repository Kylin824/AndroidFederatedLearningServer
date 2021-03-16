package org.example;

import org.json.JSONObject;

public class RequestUpdateObject {

    private String modelId;
    private Integer currentRound;
    private JSONObject weights;

    public RequestUpdateObject() {
    }

    public RequestUpdateObject(String modelId, Integer currentRound, JSONObject weights) {
        this.modelId = modelId;
        this.currentRound = currentRound;
        this.weights = weights;
    }

    public JSONObject getWeights() {
        return weights;
    }

    public void setWeights(JSONObject weights) {
        this.weights = weights;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }
}
