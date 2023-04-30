package de.cloudypanda.discord;

import de.cloudypanda.spring.EnvironmentConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DiscordServiceImpl implements DiscordService {

    private final Logger LOGGER = LoggerFactory.getLogger(DiscordServiceImpl.class);
    private final EnvironmentConfiguration environmentConfiguration;
    private JDA jda;

    public DiscordServiceImpl(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;

    }
    @Override
    public void startBot() throws InterruptedException {

        environmentConfiguration.logEnvVars();

        if(!allEnvVarsSet()){
            LOGGER.error("Missing ENV Var");
            return;
        }

        this.jda = JDABuilder.createDefault(
                environmentConfiguration.getDiscordToken(),
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES)
                .build();

        this.jda.awaitReady();

        jda.updateCommands().addCommands(
                Commands.slash("tempchannel","Sets the channel used as source for tempchannels")
                        .addOption(OptionType.CHANNEL, "voicechannel", "Voicechannel to be used", true)
                        .addOption(OptionType.STRING, "prefix" , "Prefix for the voicechannels", true))
                .queue();
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
