package ch.dams333.apisconnector.client.hue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import ch.dams333.apisconnector.utils.http.SendRequest;

public class Lights {
    public static Map<String, JSONObject> getLights(String bridgeIP, String bridgeUsername){
        try {
            JSONObject res = SendRequest.get("http://" + bridgeIP + "/api/" + bridgeUsername + "/lights", new HashMap<>());
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

    public static JSONObject getLight(String bridgeIP, String bridgeUsername, int lightID){
        try {
            return SendRequest.get("http://" + bridgeIP + "/api/" + bridgeUsername + "/lights/" + lightID, new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLightName(String bridgeIP, String bridgeUsername, int lampID){
        try {
            JSONObject res = SendRequest.get("http://" + bridgeIP + "/api/" + bridgeUsername + "/lights/" + lampID, new HashMap<>());
            return res.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setState(String bridgeIP, String bridgeUsername, int lightID, Map<String, Object> state){
        try {
            SendRequest.put("http://" + bridgeIP + "/api/" + bridgeUsername + "/lights/" + lightID + "/state", new HashMap<>(), state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
