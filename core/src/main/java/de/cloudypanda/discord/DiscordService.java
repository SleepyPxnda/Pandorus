package de.cloudypanda.discord;

import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;

@Service
public interface DiscordService {

    void startBot() throws LoginException, InterruptedException;

    void shutdownBot();

    void registerListeners(Object... listeners);

    JDA getJda();

}
