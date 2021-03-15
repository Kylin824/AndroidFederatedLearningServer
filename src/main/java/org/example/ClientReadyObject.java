package org.example;

public class ClientReadyObject {
    private Integer trainSize; // 训练数据数量

    public ClientReadyObject() {
    }

    public ClientReadyObject(Integer trainSize) {
        this.trainSize = trainSize;
    }

    public Integer getTrainSize() {
        return trainSize;
    }

    public void setTrainSize(Integer trainSize) {
        this.trainSize = trainSize;
    }
}
