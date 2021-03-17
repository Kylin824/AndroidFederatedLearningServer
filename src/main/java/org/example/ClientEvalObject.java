package org.example;

public class ClientEvalObject {

    private double testAcc;
    private double testLoss;

    public ClientEvalObject() {
    }

    public ClientEvalObject(double testAcc, double testLoss) {
        this.testAcc = testAcc;
        this.testLoss = testLoss;
    }

    public double getTestAcc() {
        return testAcc;
    }

    public void setTestAcc(double testAcc) {
        this.testAcc = testAcc;
    }

    public double getTestLoss() {
        return testLoss;
    }

    public void setTestLoss(double testLoss) {
        this.testLoss = testLoss;
    }
}
