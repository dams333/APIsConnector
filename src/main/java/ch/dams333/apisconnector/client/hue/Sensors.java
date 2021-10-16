package ch.dams333.apisconnector.client.hue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import ch.dams333.apisconnector.utils.http.SendRequest;

public class Sensors {
    public static Map<String, JSONObject> getSensors(String bridgeIP, String bridgeUsername){
        try {
            JSONObject res = SendRequest.get("http://" + bridgeIP + "/api/" + bridgeUsername + "/sensors", new HashMap<>());
            Map<String, JSONObject> result = new HashMap<>();
            for(String id : res.keySet()){
                result.put(id, res.getJSONObject(id));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static JSONObject getSensor(String bridgeIP, String bridgeUsername, int sensorID){
        try {
            return SendRequest.get("http://" + bridgeIP + "/api/" + bridgeUsername + "/sensors/" + sensorID, new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
