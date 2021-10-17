package ch.dams333.apisconnector.client.discord;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import ch.dams333.apisconnector.client.discord.interfaces.MessageInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;

public class DiscordClient{
    private String token;
    private String name;
    private JDA jda;

    public Map<String, MessageInterface<User, String>> messageInterfaces;

    public DiscordClient(String token, String name) {
        this.token = token;
        this.name = name;
        messageInterfaces = new HashMap<>();
        this.init();
    }

    public String getName() {
        return this.name;
    }

    private void init(){
        try {
            jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new BotListener(name));
            System.out.println("Bot Discord " + name + " démarré avec succès !");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        jda.shutdown();
        jda = null;
    }

    public void sendMessage(String channelID, String message){
        jda.getTextChannelById(channelID).sendMessage(message).queue();
    }
    public void sendMessage(String channelID, EmbedBuilder message){
        jda.getTextChannelById(channelID).sendMessage(message.build()).queue();
    }

    public void addMessageReceivedListener(String channelID, MessageInterface<User, String> messageInterface){
        messageInterfaces.put(channelID, messageInterface);
    }
}
