package org.example;

import org.json.JSONArray;

public class ClientUpdateObject {
    private Integer currentRound;
    private Double[] arrW0;
    private Double[] arrW1;
    private Double[] arrB0;
    private Double[] arrB1;

    public ClientUpdateObject() {
    }

    public ClientUpdateObject(Integer currentRound, Double[] arrW0, Double[] arrW1, Double[] arrB0, Double[] arrB1) {
        this.currentRound = currentRound;
        this.arrW0 = arrW0;
        this.arrW1 = arrW1;
        this.arrB0 = arrB0;
        this.arrB1 = arrB1;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public Double[] getArrW0() {
        return arrW0;
    }

    public void setArrW0(Double[] arrW0) {
        this.arrW0 = arrW0;
    }

    public Double[] getArrW1() {
        return arrW1;
    }

    public void setArrW1(Double[] arrW1) {
        this.arrW1 = arrW1;
    }

    public Double[] getArrB0() {
        return arrB0;
    }

    public void setArrB0(Double[] arrB0) {
        this.arrB0 = arrB0;
    }

    public Double[] getArrB1() {
        return arrB1;
    }

    public void setArrB1(Double[] arrB1) {
        this.arrB1 = arrB1;
    }
}
