package org.example;

import org.json.JSONArray;

public class RequestUpdateObject {

    private Integer currentRound;
    private Double testAcc;
    private Double testLoss;
    private JSONArray arrW0;
    private JSONArray arrB0;
    private JSONArray arrW1;
    private JSONArray arrB1;

    public RequestUpdateObject() {
    }

    public RequestUpdateObject(Integer currentRound, Double testAcc, Double testLoss, JSONArray arrW0, JSONArray arrB0, JSONArray arrW1, JSONArray arrB1) {
        this.currentRound = currentRound;
        this.testAcc = testAcc;
        this.testLoss = testLoss;
        this.arrW0 = arrW0;
        this.arrB0 = arrB0;
        this.arrW1 = arrW1;
        this.arrB1 = arrB1;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
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

    public Double getTestAcc() {
        return testAcc;
    }

    public void setTestAcc(Double testAcc) {
        this.testAcc = testAcc;
    }

    public Double getTestLoss() {
        return testLoss;
    }

    public void setTestLoss(Double testLoss) {
        this.testLoss = testLoss;
    }
}
