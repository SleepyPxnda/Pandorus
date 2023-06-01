package de.cloudypanda.discord;

import de.cloudypanda.spring.EnvironmentConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.utils.PermissionUtil;
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
                .setActivity(Activity.listening(" no music currently!"))
                .build();

        this.jda.awaitReady();

        this.jda.getGuilds().forEach(guild -> {
            Member botInGuild = guild.getMember(this.jda.getSelfUser());

            if(!PermissionUtil.checkPermission(botInGuild, Permission.MANAGE_CHANNEL, Permission.VOICE_MOVE_OTHERS)){
                LOGGER.warn("Bot cannot operate on guild " + guild.getName() + " due to missing rights!");
            } else {
                LOGGER.info("Bot operating on guild " + guild.getName());
            }

        });


        jda.updateCommands().addCommands(
                Commands.slash("tempchannel","Handles all commands regarding the voicechannel module")
                        .addOption(OptionType.CHANNEL, "voicechannel", "Voicechannel to be used", true)
                        .addOption(OptionType.STRING, "prefix" , "Prefix for the voicechannels", true),
                Commands.slash("serverinfo", "Handles all commands regarding the serverinfo module")
                        .addSubcommands(
                                new SubcommandData("add", "adds an server to the serverinfo")
                                .addOption(OptionType.STRING, "servername", "Name of the server", true)
                                .addOption(OptionType.STRING, "ipaddress", "Address of the server", true),

                                new SubcommandData("remove", "removes an server from the serverinfo by its name")
                                .addOption(OptionType.STRING, "servername", "Name of the server which will be removed", true),

                                new SubcommandData("list", "lists all configured servers for this server")
                        ))
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

        return environmentConfiguration.getDiscordToken() != null;
    }
}
