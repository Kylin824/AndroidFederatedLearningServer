package org.example;

import org.json.JSONArray;

public class ClientInitObject {
    private Integer epoch;
    private Integer batchSize;
    private Integer clientIndex;
    private JSONArray arrW0;
    private JSONArray arrB0;
    private JSONArray arrW1;
    private JSONArray arrB1;
    private Integer layerNum;

    public ClientInitObject() {
    }

    public ClientInitObject(Integer epoch, Integer batchSize, Integer clientIndex, JSONArray arrW0, JSONArray arrB0, JSONArray arrW1, JSONArray arrB1, Integer layerNum) {
        this.epoch = epoch;
        this.batchSize = batchSize;
        this.clientIndex = clientIndex;
        this.arrW0 = arrW0;
        this.arrB0 = arrB0;
        this.arrW1 = arrW1;
        this.arrB1 = arrB1;
        this.layerNum = layerNum;
    }

    public JSONArray getArrW1() {
        return arrW1;
    }

    public void setArrW1(JSONArray arrW1) {
        this.arrW1 = arrW1;
    }

    public JSONArray getArrB1() {
        return arrB1;
    }

    public void setArrB1(JSONArray arrB1) {
        this.arrB1 = arrB1;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public void setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
    }

    public JSONArray getArrW0() {
        return arrW0;
    }

    public void setArrW0(JSONArray arrW0) {
        this.arrW0 = arrW0;
    }

    public JSONArray getArrB0() {
        return arrB0;
    }

    public void setArrB0(JSONArray arrB0) {
        this.arrB0 = arrB0;
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
