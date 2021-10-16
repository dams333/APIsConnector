package ch.dams333.apisconnector.client.hue;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;

import ch.dams333.apisconnector.APIsConnector;
import ch.dams333.apisconnector.utils.http.SendRequest;

public class HueClient {
    
    public HueClient() {
        this.initClient();
    }

    private String bridgeIP;
    private String bridgeUsername;

    private boolean initClient(){
        try {
            bridgeIP = SendRequest.get("https://discovery.meethue.com/", new HashMap<>()).getString("internalipaddress");
            if(bridgeIP == null){
                System.out.println("Impossible de trouver un bridge Hue sur votre réseau");
                return false;
            }
            System.out.println("Starting Hue client with bridge at " + bridgeIP);
            YamlFile config = APIsConnector.getConfig();
            if(!config.getKeys(false).contains("HueUsername")){
                HashMap<String, String> body = new HashMap<>();
                body.put("devicetype", "APIsConnector#HueClient");
                JSONObject res = SendRequest.post("http://" + bridgeIP + "/api", new HashMap<>(), body);
                if(res.has("error") && res.getJSONObject("error").getInt("type") == 101){
                    System.out.println("Veuillez appuyer sur le bouton de votre bridge Hue et taper la commande 'hueconnect' pour se connecter à celui-ci");
                    return true;
                }
                System.out.println("Impossible de contacter le bridge Hue pour créer un nouvel utilisateur");
                return false;
            }
            this.bridgeUsername = config.getString("HueUsername");
            System.out.println("Connexion avec le bridge Hue éffectué sous le nom d'utilisateur: " + bridgeUsername);
            return true;
        } catch (Exception e) {  
            e.printStackTrace();
            return false;
        }
    }

    public void bridgeConnect() {
        HashMap<String, String> body = new HashMap<>();
        body.put("devicetype", "APIsConnector#HueClient");
        try {
            JSONObject res = SendRequest.post("http://" + bridgeIP + "/api", new HashMap<>(), body);
            if(res.has("error") && res.getJSONObject("error").getInt("type") == 101){
                System.out.println("Veuillez appuyer sur le bouton de votre bridge Hue et taper la commande 'hueconnect' pour se connecter à celui-ci");
                return;
            }
            if(res.has("success")){
                bridgeUsername = res.getJSONObject("success").getString("username");
                YamlFile config = APIsConnector.getConfig();
                config.set("HueUsername", bridgeUsername);
                try {
                    config.save();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Connexion avec le bridge Hue éffectué sous le nom d'utilisateur: " + bridgeUsername);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
