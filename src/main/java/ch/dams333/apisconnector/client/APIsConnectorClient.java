package ch.dams333.apisconnector.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dams333.apisconnector.client.discord.DiscordClient;
import ch.dams333.apisconnector.client.github.GithubClient;
import ch.dams333.apisconnector.client.hue.HueClient;
import ch.dams333.apisconnector.client.spotify.SpotifyClient;
import ch.dams333.apisconnector.client.twitch.TwitchClient;

public class APIsConnectorClient {

    private UUID id;
    private HueClient hueClient;
    private SpotifyClient spotifyClient;
    private TwitchClient twitchClient;
    private GithubClient githubClient;
    private List<DiscordClient> discordClients;

    public APIsConnectorClient(UUID id, HueClient hueClient, SpotifyClient spotifyClient, List<DiscordClient> discordClients, TwitchClient twitchClient, GithubClient githubClient) {
        this.id = id;
        this.hueClient = hueClient;
        this.spotifyClient = spotifyClient;
        this.discordClients = discordClients;
        this.twitchClient = twitchClient;
        this.githubClient = githubClient;
    }

    public TwitchClient getTwitchClient() {
        return this.twitchClient;
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

    public GithubClient getGithubClient() {
        return this.githubClient;
    }


    public DiscordClient getDiscordClient(String name){
        for(DiscordClient client : this.discordClients){
            if(client.getName().equalsIgnoreCase(name)){
                return client;
            }
        }
        return null;
    }

    public void stopDiscordBots(){
        for(DiscordClient client : this.discordClients){
            client.stop();
        }
    }


    public static Builder builder() { 
        return new Builder(); 
    }

    public static final class Builder {

        private HueClient hueClient = null;
        private SpotifyClient spotifyClient = null;
        private TwitchClient twitchClient = null;
        private GithubClient githubClient = null;
        private List<DiscordClient> discordClients = new ArrayList<>();

        private Builder() {}

        public Builder addHueClient(){
            this.hueClient = new HueClient();
            return this;
        }

        public Builder addSpotifyClient(){
            this.spotifyClient = new SpotifyClient();
            return this;
        }

        public Builder addTwitchClient(){
            this.twitchClient = new TwitchClient();
            return this;
        }

        public Builder addGithubClient(){
            this.githubClient = new GithubClient();
            return this;
        }

        public Builder addDiscordClient(String token, String name){
            this.discordClients.add(new DiscordClient(token, name));
            return this;
        }

        public APIsConnectorClient build(){
            return new APIsConnectorClient(UUID.randomUUID(), this.hueClient, this.spotifyClient, this.discordClients, this.twitchClient, this.githubClient);
        }
    }

}
