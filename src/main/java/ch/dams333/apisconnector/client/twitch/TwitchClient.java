package ch.dams333.apisconnector.client.twitch;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;

import ch.dams333.apisconnector.APIsConnector;
import ch.dams333.apisconnector.Secret;
import ch.dams333.apisconnector.utils.http.SendRequest;

public class TwitchClient {

    private String access_token;
    private Date expirationDate;

    public TwitchClient() {
        this.init();
    }
    
    private void init(){
        YamlFile config = APIsConnector.getConfig();
        if(!config.getKeys(false).contains("TwitchRefreshToken")){
            Runtime rt = Runtime.getRuntime();
            String url = "https://id.twitch.tv/oauth2/authorize?client_id=mud1yv8nh2b03nrkpp7dxtky63zfc8&redirect_uri=http://localhost:8333/twitch&response_type=code&scope=channel_subscriptions+user_read";
            try {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.relog(config.getString("TwitchRefreshToken"));
            System.out.println("Connexion a Twitch éffectuée sur le compte " + getUsername());
        }
    }

    public void log(String accessToken) {
        YamlFile config = APIsConnector.getConfig();
        try {
            JSONObject res = SendRequest.postOAuth2("https://id.twitch.tv/oauth2/token?client_id=mud1yv8nh2b03nrkpp7dxtky63zfc8&client_secret=" + Secret.TWITCH_SECRET + "&code=" + accessToken + "&grant_type=authorization_code&redirect_uri=http://localhost:8333/twitch", new HashMap<>(), new HashMap<>());
            expirationDate = new Date(new Date().getTime() + (res.getInt("expires_in") * 1000));
            this.access_token = res.getString("access_token");
            config.set("TwitchRefreshToken", res.getString("refresh_token"));
            config.save();
            System.out.println("Connexion a Twitch éffectuée sur le compte " + getUsername());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> getAuthorizationHeader(){
        if(new Date().getTime() > expirationDate.getTime()){
            YamlFile config = APIsConnector.getConfig();
            relog(config.getString("TwitchRefreshToken"));
        }
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + access_token);
        header.put("Client-ID", "mud1yv8nh2b03nrkpp7dxtky63zfc8");
        return header;
    }

    public String getUsername(){
        try {
            return SendRequest.get("https://api.twitch.tv/helix/users", getAuthorizationHeader()).getJSONArray("data").getJSONObject(0).getString("display_name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void relog(String refresh_token){
        try {
            JSONObject res = SendRequest.postOAuth2("https://id.twitch.tv/oauth2/token?client_id=mud1yv8nh2b03nrkpp7dxtky63zfc8&client_secret=" + Secret.TWITCH_SECRET + "&refresh_token=" + refresh_token + "&grant_type=refresh_token", new HashMap<>(), new HashMap<>());
            expirationDate = new Date(new Date().getTime() + (res.getInt("expires_in") * 1000));
            this.access_token = res.getString("access_token");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
