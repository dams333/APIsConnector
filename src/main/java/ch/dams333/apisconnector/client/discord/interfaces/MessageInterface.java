package ch.dams333.apisconnector.client.discord.interfaces;

import net.dv8tion.jda.api.entities.User;

public interface MessageInterface<U, M> {
    void received (User user, String message);
}
