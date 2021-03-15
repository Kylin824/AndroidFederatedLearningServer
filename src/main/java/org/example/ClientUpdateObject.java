package org.example;

public class ClientUpdateObject {
    private Integer roundNumber;
    private String weights;

    public ClientUpdateObject() {
    }

    public ClientUpdateObject(Integer roundNumber, String weights) {
        this.roundNumber = roundNumber;
        this.weights = weights;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }
}
