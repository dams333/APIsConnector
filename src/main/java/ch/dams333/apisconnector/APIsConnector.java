package ch.dams333.apisconnector;

import java.util.Scanner;

import org.simpleyaml.configuration.file.YamlFile;

import ch.dams333.apisconnector.client.APIsConnectorClient;

public class APIsConnector implements Runnable{

    private static boolean running;
    private final Scanner scanner = new Scanner(System.in);
    private static APIsConnectorClient client;

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
        System.out.println("Démarrage de l'app de test");
        client = APIsConnectorClient.builder()
            .addHueClient()
            .build();
        System.out.println("Client démarré avec l'id: " + client.getId().toString());
        while(running) {
            if(scanner.hasNextLine()) {
                performCommand(scanner.nextLine());
            }
        }
        System.out.println("App de test quittée");
        System.exit(0);
    }

    private void performCommand(String command){
        if(command.equalsIgnoreCase("hueconnect")){
            client.getHueClient().bridgeConnect();
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
