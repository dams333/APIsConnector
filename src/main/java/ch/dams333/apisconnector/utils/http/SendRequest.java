package ch.dams333.apisconnector.utils.http;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;

public class SendRequest {
    private final static OkHttpClient httpClient = new OkHttpClient();

    public static JSONObject get(String url, Map<String, String> header) throws Exception {
        Builder builder = new Request.Builder()
                .url(url);
        for(String key : header.keySet()){
            builder.addHeader(key, header.get(key));
        }

        Request request = builder.build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String res = response.body().string();
            if(res.startsWith("[")){
                res = res.substring(1, res.length());
                res = res.substring(0, res.length() - 1);
            }
            return new JSONObject(res);
        }
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static JSONObject post(String url, Map<String, String> header, Map<String, String> body) throws Exception {

        JSONObject jsonObject = new JSONObject();
        for(String key : body.keySet()){
            jsonObject.put(key, body.get(key));
        }
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());

        Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "OkHttp Bot")
                .addHeader("Content-Type", "application/json");
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String res = response.body().string();
            if(res.startsWith("[")){
                res = res.substring(1, res.length());
                res = res.substring(0, res.length() - 1);
            }
            return new JSONObject(res);
        }

    }
}
