package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClientInitObject {
    private Integer epoch;
    private Integer batchSize;
    private Integer clientIndex;
    private JSONObject initWeights;

    public ClientInitObject() {
    }

    public ClientInitObject(Integer epoch, Integer batchSize, Integer clientIndex, JSONObject initWeights) {
        this.epoch = epoch;
        this.batchSize = batchSize;
        this.clientIndex = clientIndex;
        this.initWeights = initWeights;
    }

    public JSONObject getInitWeights() {
        return initWeights;
    }

    public void setInitWeights(JSONObject initWeights) {
        this.initWeights = initWeights;
    }

    public Integer getEpoch() {
        return epoch;
    }

    public void setEpoch(Integer epoch) {
        this.epoch = epoch;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getClientIndex() {
        return clientIndex;
    }

    public void setClientIndex(Integer clientIndex) {
        this.clientIndex = clientIndex;
    }
}
