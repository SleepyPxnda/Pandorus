package de.cloudypanda;

import de.cloudypanda.commands.CommandHandler;
import de.cloudypanda.events.EventHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class PandorusApplication {

    private static Logger LOGGER = LoggerFactory.getLogger(PandorusApplication.class);

    public static Long TempSourceChannelId = null;
    private static String JDAToken = null;


    public static void main(String[] args) {

        if(!allEnvVarsSet()){
            LOGGER.error("Missing ENV Var");
            return;
        }

        TempSourceChannelId = Long.valueOf(System.getenv("TEMP_SOURCE_CHANNEL_ID"));
        JDAToken = System.getenv("JDA_TOKEN");

        JDA jda = JDABuilder.createDefault(JDAToken)
                .addEventListeners(new EventHandler(), new CommandHandler())
                .build();

//        jda.updateCommands().addCommands(
//                Commands.slash("tempchannel","Sets the channel used as source for tempchannels")
//                        .addOption(OptionType.CHANNEL, "voicechannel", "Voicechannel to be used", true));


        SpringApplication.run(PandorusApplication.class, args);
    }

    private static boolean allEnvVarsSet() {
        return TempSourceChannelId == null;
    }
}

