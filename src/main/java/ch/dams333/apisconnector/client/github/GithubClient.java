package ch.dams333.apisconnector.client.github;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;

import ch.dams333.apisconnector.APIsConnector;
import ch.dams333.apisconnector.Secret;
import ch.dams333.apisconnector.utils.http.SendRequest;

public class GithubClient {
    
    public GithubClient() {
        this.init();
    }


    private void init(){
        YamlFile config = APIsConnector.getConfig();
        if(!config.getKeys(false).contains("GithubToken")){
            Runtime rt = Runtime.getRuntime();
            String state = String.valueOf(new Date().getTime());
            String url = "https://github.com/login/oauth/authorize?client_id=fba2e458c9964672df2e&scope=user&response_type=code&redirect_uri=http://localhost:8333/github&state=" + state;
            try {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Connexion à Github éffectuée sur le compte " + getUsername());
        }
    }

    public void log(String accessToken) {
        YamlFile config = APIsConnector.getConfig();
        try {
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "application/json");
            JSONObject res = SendRequest.postOAuth2("https://github.com/login/oauth/access_token?client_id=fba2e458c9964672df2e&client_secret=" + Secret.GITHUB_SECRET + "&code=" + accessToken + "&grant_type=authorization_code&redirect_uri=http://localhost:8333/github", header, new HashMap<>());
            config.set("GithubToken", res.getString("access_token"));
            config.save();
            System.out.println("Connexion à Github éffectuée sur le compte " + getUsername());
            getUsername();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Map<String, String> getAuthorizationHeader(){
        Map<String, String> header = new HashMap<>();
        YamlFile config = APIsConnector.getConfig();
        header.put("Accept", "application/json");
        header.put("Authorization", "token " + config.getString("GithubToken"));
        return header;
    }

    public String getUsername(){
        try {
            return SendRequest.get("https://api.github.com/user", getAuthorizationHeader()).getString("login");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
