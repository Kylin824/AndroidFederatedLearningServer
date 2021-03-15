package org.example;

public class ClientInitObject {
    private Integer epoch;
    private Integer batchSize;
    private Integer clientIndex;
    private String initWeights;

    public ClientInitObject() {
    }

    public ClientInitObject(Integer epoch, Integer batchSize, Integer clientIndex, String initWeights) {
        this.epoch = epoch;
        this.batchSize = batchSize;
        this.clientIndex = clientIndex;
        this.initWeights = initWeights;
    }

    public String getInitWeights() {
        return initWeights;
    }

    public void setInitWeights(String initWeights) {
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
