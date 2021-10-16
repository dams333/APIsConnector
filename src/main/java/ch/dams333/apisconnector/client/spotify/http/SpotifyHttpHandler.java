package ch.dams333.apisconnector.client.spotify.http;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.*;

import org.apache.commons.lang3.StringEscapeUtils;

import ch.dams333.apisconnector.APIsConnector;

public class SpotifyHttpHandler implements HttpHandler{
    @Override    
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null; 
        if("GET".equals(httpExchange.getRequestMethod())) { 
            requestParamValue = handleGetRequest(httpExchange);
        }
        if(requestParamValue.startsWith("error=")){
            System.out.println("La connexion a Spotify n'a pas pu etre éffectuée");
        }else{
            APIsConnector.client.getSpotifyClient().log(requestParamValue.split("=")[1]);
        }
        handleResponse(httpExchange,requestParamValue); 
    }
    private String handleGetRequest(HttpExchange httpExchange) {
            return httpExchange.getRequestURI()
                    .toString()
                    .split("\\?")[1];
    }
    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
            OutputStream outputStream = httpExchange.getResponseBody();
            StringBuilder htmlBuilder = new StringBuilder();          
            htmlBuilder.append("Vous pouvez maintenant fermer cette page");
            String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
    }
}
