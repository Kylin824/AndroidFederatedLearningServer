package org.example;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;

public class ModelUtils {

    public static JSONObject modelToJson(MultiLayerNetwork model) {

        JSONObject modelInJson = new JSONObject();

        int layerNum = model.getLayers().length;
        modelInJson.put("layerNum", layerNum);
        String key;
        for (int i = 0; i < layerNum; i++) {
            key = i + "_W";
            modelInJson.put(key, model.getParam(key));
            key = i + "_b";
            modelInJson.put(key, model.getParam(key));
        }
        return modelInJson;
    }

//    public static String modelToString(MultiLayerNetwork model) {
//        JSONObject obj = modelToJson(model);
//        return obj.toString();
//    }
//
//    public static MultiLayerNetwork stringToModel(String str, MultiLayerNetwork model) {
//
//        JSONObject jsonObject = JSONObject.parseObject(str);
//
//
//        return jsonToModel(jsonObject, model);
//    }

    public static MultiLayerNetwork jsonToModel(JSONObject jsonObject, MultiLayerNetwork model) {
        INDArray param;
        String key;
        int layerNum = jsonObject.getInt("layerNum");
        for (int i = 0; i < layerNum; i++) {
            key = i + "_W";
            param = (INDArray)jsonObject.get(key);
            model.setParam(key, param);
            key = i + "_b";
            param = (INDArray)jsonObject.get(key);
            model.setParam(key, param);
        }
        return model;
    }

    public static MultiLayerNetwork updateModelWeigths (ArrayList<ClientUpdateObject> currentClientUpdates, MultiLayerNetwork currentModel) {

        int layerNum = currentModel.getLayers().length;
        String key;
        for (int i = 0; i < layerNum; i++) {
            key = i + "_W";
            INDArray param = Nd4j.create(currentModel.getParam(key).shape());
            for (int j = 0; j < currentClientUpdates.size(); j++) {
                param = param.add((INDArray)currentClientUpdates.get(i).getWeights().get(key));
            }
            param = param.div(currentClientUpdates.size());  // 取平均值
            currentModel.setParam(key, param);

            key = i + "_b";
            param = Nd4j.create(currentModel.getParam(key).shape());
            for (int j = 0; j < currentClientUpdates.size(); j++) {
                param = param.add((INDArray)currentClientUpdates.get(i).getWeights().get(key));
            }
            param = param.div(currentClientUpdates.size());  // 取平均值
            currentModel.setParam(key, param);
        }
        return currentModel;
    }
}
