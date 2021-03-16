package org.example;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;

public class ModelUtils {

    public static JSONArray model0WToJsonArray(MultiLayerNetwork model) {

        JSONArray data = new JSONArray();
        INDArray param;
        param = model.getParam("0_W");
        System.out.println("0_W param : row " +param.rows()  +" col: " +param.columns());
        for (int j = 0; j < param.rows(); j++) {
            for (int k = 0; k < param.columns(); k++) {
                data.put(param.getRow(j).getFloat(k));
            }
        }
        return data;
    }

    public static JSONArray model1WToJsonArray(MultiLayerNetwork model) {

        JSONArray data = new JSONArray();
        INDArray param;
        param = model.getParam("1_W");
        System.out.println("1_W param : row " +param.rows()  +" col: " +param.columns());
        for (int j = 0; j < param.rows(); j++) {
            for (int k = 0; k < param.columns(); k++) {
                data.put(param.getRow(j).getFloat(k));
            }
        }
        return data;
    }

    public static JSONArray model0BToJsonArray(MultiLayerNetwork model) {
        JSONArray data = new JSONArray();
        INDArray param;
        param = model.getParam("0_b"); // 只有1行
        System.out.println("0_b param : row " +param.rows()  +" col: " +param.columns());
        for (int k = 0; k < param.columns(); k++) {
            data.put(param.getRow(0).getFloat(k));
        }
        return data;
    }

    public static JSONArray model1BToJsonArray(MultiLayerNetwork model) {
        JSONArray data = new JSONArray();
        INDArray param;
        param = model.getParam("1_b"); // 只有1行
        System.out.println("1_b param : row " +param.rows()  +" col: " +param.columns());
        for (int k = 0; k < param.columns(); k++) {
            data.put(param.getRow(0).getFloat(k));
        }
        return data;
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

    public static MultiLayerNetwork updateGlobalModel (ArrayList<ClientUpdateObject> currentClientUpdates, MultiLayerNetwork currentModel) {

        int totalClientNum = currentClientUpdates.size();

        INDArray paramW0 = Nd4j.create(currentModel.getParam("0_W").shape());
        INDArray paramW1 = Nd4j.create(currentModel.getParam("1_W").shape());
        INDArray paramB0 = Nd4j.create(currentModel.getParam("0_b").shape());
        INDArray paramB1 = Nd4j.create(currentModel.getParam("1_b").shape());

        for (int i = 0; i < totalClientNum; i++) {
            ClientUpdateObject clientUpdateObject = currentClientUpdates.get(i);

            JSONArray arrW0 = clientUpdateObject.getArrW0();
            double currentValue = 0d;

            for (int j = 0; j < paramW0.rows(); j++) {
                for (int k = 0; k < paramW0.columns(); k++) {
                    currentValue = (double)arrW0.get(j * paramW0.columns() + k);
                    paramW0.putScalar(j, k, currentValue + paramW0.getDouble(j, k));
                }
            }


            JSONArray arrW1 = clientUpdateObject.getArrW1();
            for (int j = 0; j < paramW1.rows(); j++) {
                for (int k = 0; k < paramW1.columns(); k++) {
                    currentValue = (double)arrW1.get(j * paramW1.columns() + k);
                    paramW1.putScalar(j, k, currentValue + paramW1.getDouble(j, k));
                }
            }

            JSONArray arrB0 = clientUpdateObject.getArrB0();

            for (int k = 0; k < paramB0.columns(); k++) {
                currentValue = (double)arrB0.get(k);
                paramB0.putScalar(k, currentValue + paramB0.getDouble(k));
            }

            JSONArray arrB1 = clientUpdateObject.getArrB1();

            for (int k = 0; k < paramB1.columns(); k++) {
                currentValue = (double)arrB1.get(k);
                paramB1.putScalar(k, currentValue + paramB1.getDouble(k));
            }
        }

        paramW0 = paramW0.div(currentClientUpdates.size());  // 取平均值
        paramW1 = paramW1.div(currentClientUpdates.size());  // 取平均值
        paramB0 = paramB0.div(currentClientUpdates.size());  // 取平均值
        paramB1 = paramB1.div(currentClientUpdates.size());  // 取平均值

        currentModel.setParam("0_W", paramW0);
        currentModel.setParam("1_W", paramW1);
        currentModel.setParam("0_b", paramB0);
        currentModel.setParam("1_b", paramB1);

        return currentModel;
    }

    public static void main(String[] args) {
        JSONArray arr = new JSONArray();
        arr.put(0.1d);
        System.out.println(arr.get(0));
        System.out.println(arr.get(0));
    }
}
