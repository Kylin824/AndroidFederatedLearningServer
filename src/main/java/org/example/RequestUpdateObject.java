package org.example;

public class RequestUpdateObject {

    private String modelId;
    private Integer currentRound;
    private String weights;

    public RequestUpdateObject() {
    }

    public RequestUpdateObject(String modelId, Integer currentRound, String weights) {
        this.modelId = modelId;
        this.currentRound = currentRound;
        this.weights = weights;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
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
