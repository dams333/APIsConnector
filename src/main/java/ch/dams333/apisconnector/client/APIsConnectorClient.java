package ch.dams333.apisconnector.client;

import java.util.UUID;

import ch.dams333.apisconnector.client.hue.HueClient;

public class APIsConnectorClient {

    private UUID id;
    private HueClient hueClient;

    public APIsConnectorClient(UUID id, HueClient hueClient) {
        this.id = id;
        this.hueClient = hueClient;
    }

    public UUID getId() {
        return this.id;
    }

    public HueClient getHueClient() {
        return this.hueClient;
    }



    public static Builder builder() { 
        return new Builder(); 
    }

    public static final class Builder {

        private HueClient hueClient = null;

        private Builder() {}

        public Builder addHueClient(){
            this.hueClient = new HueClient();
            return this;
        }

        public APIsConnectorClient build(){
            return new APIsConnectorClient(UUID.randomUUID(), this.hueClient);
        }
    }

}
