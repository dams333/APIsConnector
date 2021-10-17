package ch.dams333.apisconnector.client.spotify;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;

import ch.dams333.apisconnector.APIsConnector;
import ch.dams333.apisconnector.Secret;
import ch.dams333.apisconnector.utils.http.SendRequest;

public class SpotifyClient {

    private String access_token;
    private Date expirationDate;

    public SpotifyClient() {
        this.initClient();
        this.access_token = null;
        this.expirationDate = new Date();
    }

    private boolean initClient(){
        YamlFile config = APIsConnector.getConfig();
        if(!config.getKeys(false).contains("SpotifyRefreshToken")){
            Runtime rt = Runtime.getRuntime();
            String url = "https://accounts.spotify.com/authorize?client_id=75fedb44ef994f53ade53dda2bb85a62&scope=user-read-currently-playing+user-modify-playback-state+user-read-playback-state&response_type=code&redirect_uri=http://localhost:8333/spotify";
            try {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        this.relog(config.getString("SpotifyRefreshToken"));
        System.out.println("Connexion a Spotify éffectuée sur le compte " + getUserName());
        return true;
    }

    public void log(String accessToken) {
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("code", accessToken);
        body.put("redirect_uri", "http://localhost:8333/spotify");

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", Secret.SPOTIFY_SECRET);
        YamlFile config = APIsConnector.getConfig();
        try {
            JSONObject res = SendRequest.postOAuth2("https://accounts.spotify.com/api/token", header, body);
            expirationDate = new Date(new Date().getTime() + (res.getInt("expires_in") * 1000));
            this.access_token = res.getString("access_token");
            config.set("SpotifyRefreshToken", res.getString("refresh_token"));
            config.save();
            System.out.println("Connexion a Spotify éffectuée sur le compte " + getUserName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> getAuthorizationHeader(){
        if(new Date().getTime() > expirationDate.getTime()){
            YamlFile config = APIsConnector.getConfig();
            relog(config.getString("SpotifyRefreshToken"));
        }
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + access_token);
        return header;
    }

    private void relog(String refresh_token){
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refresh_token);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", Secret.SPOTIFY_SECRET);
        try {
            JSONObject res = SendRequest.postOAuth2("https://accounts.spotify.com/api/token", header, body);
            expirationDate = new Date(new Date().getTime() + (res.getInt("expires_in") * 1000));
            this.access_token = res.getString("access_token");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    public String getUserName(){
        try {
            return SendRequest.get("https://api.spotify.com/v1/me", getAuthorizationHeader()).getString("display_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getCurrentSong(){
        try {
            return SendRequest.get("https://api.spotify.com/v1/me/player/currently-playing", getAuthorizationHeader());
        } catch (Exception e) {
            if(!(e instanceof JSONException)){
                e.printStackTrace();
            }
            return null;
        }
    }

    public String getActiveDeviceID(){
        try {
            JSONArray devices = SendRequest.get("https://api.spotify.com/v1/me/player/devices", getAuthorizationHeader()).getJSONArray("devices");
            for(int i = 0; i < devices.length(); i++){
                if(devices.getJSONObject(i).getBoolean("is_active")){
                    return devices.getJSONObject(i).getString("id");
                }
            }
            return "0";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void nextSong(){
        String deviceID = getActiveDeviceID();
        if(deviceID.equals("0")){
            System.out.println("Il n'y a pas de lecture en cours");
        }else{
            Songs.nextSong(deviceID, getAuthorizationHeader());
        }
    }
    
    public void prevSong(){
        String deviceID = getActiveDeviceID();
        if(deviceID.equals("0")){
            System.out.println("Il n'y a pas de lecture en cours");
        }else{
            Songs.prevSong(deviceID, getAuthorizationHeader());
        }
    }

    public void play(){
        String deviceID = getActiveDeviceID();
        if(deviceID.equals("0")){
            System.out.println("Il n'y a pas de lecture en cours");
        }else{
            Songs.play(deviceID, getAuthorizationHeader());
        }
    }

    public void pause(){
        String deviceID = getActiveDeviceID();
        if(deviceID.equals("0")){
            System.out.println("Il n'y a pas de lecture en cours");
        }else{
            Songs.pause(deviceID, getAuthorizationHeader());
        }
    }

    public void togglePause(){
        JSONObject currentSong = getCurrentSong();
        if(currentSong != null){
            if(currentSong.getBoolean("is_playing")){
                pause();
            }else{
                play();
            }
        }else{
            System.out.println("Il n'y a pas de lecture en cours");
        }
    }
}
