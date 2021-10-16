package ch.dams333.apisconnector.client;

import java.util.UUID;

import ch.dams333.apisconnector.client.hue.HueClient;
import ch.dams333.apisconnector.client.spotify.SpotifyClient;

public class APIsConnectorClient {

    private UUID id;
    private HueClient hueClient;
    private SpotifyClient spotifyClient;

    public APIsConnectorClient(UUID id, HueClient hueClient, SpotifyClient spotifyClient) {
        this.id = id;
        this.hueClient = hueClient;
        this.spotifyClient = spotifyClient;
    }

    public UUID getId() {
        return this.id;
    }

    public HueClient getHueClient() {
        return this.hueClient;
    }

    public SpotifyClient getSpotifyClient() {
        return this.spotifyClient;
    }



    public static Builder builder() { 
        return new Builder(); 
    }

    public static final class Builder {

        private HueClient hueClient = null;
        private SpotifyClient spotifyClient = null;

        private Builder() {}

        public Builder addHueClient(){
            this.hueClient = new HueClient();
            return this;
        }

        public Builder addSpotifyClient(){
            this.spotifyClient = new SpotifyClient();
            return this;
        }

        public APIsConnectorClient build(){
            return new APIsConnectorClient(UUID.randomUUID(), this.hueClient, this.spotifyClient);
        }
    }

}
