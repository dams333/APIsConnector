package ch.dams333.apisconnector.client.spotify;

import java.util.HashMap;
import java.util.Map;

import ch.dams333.apisconnector.utils.http.SendRequest;

public class Songs {
    public static void nextSong(String deviceID, Map<String, String> header){
        try {
            SendRequest.post("https://api.spotify.com/v1/me/player/next?device_id=" + deviceID, header, new HashMap<>());
            System.out.println("Lecture passée au morceau suivant");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prevSong(String deviceID, Map<String, String> header){
        try {
            SendRequest.post("https://api.spotify.com/v1/me/player/previous?device_id=" + deviceID, header, new HashMap<>());
            System.out.println("Lecture revenu au morceau précédent");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pause(String deviceID, Map<String, String> header){
        try {
            SendRequest.put("	https://api.spotify.com/v1/me/player/pause?device_id=" + deviceID, header, new HashMap<>());
            System.out.println("Lecture mise en pause");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void play(String deviceID, Map<String, String> header){
        try {
            SendRequest.put("	https://api.spotify.com/v1/me/player/play?device_id=" + deviceID, header, new HashMap<>());
            System.out.println("Lecture démarrée");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
