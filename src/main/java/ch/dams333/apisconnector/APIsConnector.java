package ch.dams333.apisconnector;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;

import com.sun.net.httpserver.*;

import ch.dams333.apisconnector.client.APIsConnectorClient;
import ch.dams333.apisconnector.client.spotify.http.SpotifyHttpHandler;

public class APIsConnector implements Runnable{

    private static boolean running;
    private final Scanner scanner = new Scanner(System.in);
    public static APIsConnectorClient client;

    public static void main(String[] args) {
        running = true;
        try {
            APIsConnector apisConnector = new APIsConnector();

            new Thread(apisConnector, "APIsConnector").start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            startWebServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Démarrage de l'app de test");
        client = APIsConnectorClient.builder()
            .addHueClient()
            .addSpotifyClient()
            .build();

        while(running) {
            if(scanner.hasNextLine()) {
                performCommand(scanner.nextLine());
            }
        }
        client.getHueClient().cancelListeners();
        server.stop(0);
        System.out.println("App de test quittée");
        System.exit(0);
    }

    private static HttpServer server;

    private void startWebServer() throws Exception{
        server = HttpServer.create(new InetSocketAddress("localhost", 8333), 0);
        server.createContext("/spotify", new  SpotifyHttpHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Serveur web démarré. Il écoute sur le port 8333");
    }

    private void performCommand(String command){
        if(command.equalsIgnoreCase("hueconnect")){
            client.getHueClient().bridgeConnect();
        }
        if(command.equalsIgnoreCase("huelights")){
            client.getHueClient().printHueLights();
        }
        if(command.startsWith("huetoggle")){
            client.getHueClient().toggleLight(Integer.parseInt(command.split(" ")[1]));
        }
        if(command.startsWith("huesat")){
            client.getHueClient().lightSaturation(Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2]));
        }
        if(command.startsWith("huebri")){
            client.getHueClient().lightBrightness(Integer.parseInt(command.split(" ")[1]), Integer.parseInt(command.split(" ")[2]));
        }
        if(command.startsWith("huecolor")){
            client.getHueClient().lightColor(Integer.parseInt(command.split(" ")[1]), Double.parseDouble(command.split(" ")[2]), Double.parseDouble(command.split(" ")[3]));
        }
        if(command.equalsIgnoreCase("huesensors")){
            client.getHueClient().printHueSensors();
        }

        if(command.equalsIgnoreCase("spotCurrent")){
            JSONObject current = client.getSpotifyClient().getCurrentSong();
            if(current == null){
                System.out.println("Il n'y a pas de musique en cours de lecture");
            }else{
                System.out.println("La musique en cours de lecture est " + current.getJSONObject("item").getString("name") + " (" + current.getJSONObject("item").getJSONArray("artists").getJSONObject(0).getString("name") + ")");
            }
        }

        if(command.equalsIgnoreCase("stop")){
            running = false;
        }
    }

    public static YamlFile getConfig() {
        final YamlFile yamlFile = new YamlFile("config.yml");

        try {
            if (!yamlFile.exists()) {
                System.out.println("Le fichier de configuration a bien été créé: " + yamlFile.getFilePath());
                yamlFile.createNewFile(true);
            }
            yamlFile.load();
            return yamlFile;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
