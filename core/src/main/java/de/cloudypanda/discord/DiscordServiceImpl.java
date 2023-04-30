package de.cloudypanda.discord;

import de.cloudypanda.EnvironmentConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.security.auth.login.LoginException;


public class DiscordServiceImpl implements DiscordService {

    private final Logger LOGGER = LoggerFactory.getLogger(DiscordServiceImpl.class);
    private final EnvironmentConfiguration environmentConfiguration;
    private JDA jda;

    public DiscordServiceImpl(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;

    }
    @Override
    public void startBot() throws InterruptedException {

        if(!allEnvVarsSet()){
            LOGGER.error("Missing ENV Var");
            return;
        }

        this.jda = JDABuilder.createDefault(environmentConfiguration.getDiscordToken(), GatewayIntent.GUILD_MESSAGES)
                .build();

        this.jda.awaitReady();

//        jda.updateCommands().addCommands(
//                Commands.slash("tempchannel","Sets the channel used as source for tempchannels")
//                        .addOption(OptionType.CHANNEL, "voicechannel", "Voicechannel to be used", true))
//                .queue();
    }

    @Override
    public void shutdownBot() {
        this.jda.shutdown();
    }

    @Override
    public void registerListeners(Object... listeners) {
        this.jda.addEventListener(listeners);
    }

    @Override
    public JDA getJda() {
        return this.jda;
    }

    private boolean allEnvVarsSet() {

        return environmentConfiguration.getTempChannelSourceId() != null
                && environmentConfiguration.getDiscordToken() != null;
    }
}
