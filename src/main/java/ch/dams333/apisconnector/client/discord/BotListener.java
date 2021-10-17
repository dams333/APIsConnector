package ch.dams333.apisconnector.client.discord;

import ch.dams333.apisconnector.APIsConnector;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class BotListener implements EventListener{

    private String name;
    public BotListener(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if(event instanceof MessageReceivedEvent){
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            for(String channelID : APIsConnector.client.getDiscordClient(name).messageInterfaces.keySet()){
                if(e.getTextChannel().getId().equals(channelID)){
                    System.out.println("Message de " + e.getAuthor().getName() + " détecté sur le channel '" + e.getTextChannel().getName() + "'");
                    APIsConnector.client.getDiscordClient(name).messageInterfaces.get(channelID).received(e.getAuthor(), e.getMessage().getContentDisplay());
                }
            }
        }        
    }
    
}
