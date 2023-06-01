package de.cloudypanda;

import de.cloudypanda.commands.ServerinfoCommandHandler;
import de.cloudypanda.commands.TempchannelCommandHandler;
import de.cloudypanda.database.repositories.ServerinfoRepository;
import de.cloudypanda.database.repositories.TempChannelRepository;
import de.cloudypanda.discord.DiscordService;
import de.cloudypanda.events.TempchannelEventHandler;
import de.cloudypanda.models.TempChannelHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties
public class PandorusApplication implements CommandLineRunner {

    private final TempChannelRepository tempChannelRepository;
    private final DiscordService botService;
    private final TempChannelHandler tempChannelHandler;
    private final ServerinfoRepository serverinfoRepository;

    public PandorusApplication(DiscordService botService,
                               TempChannelRepository tempChannelRepository,
                               TempChannelHandler tempChannelHandler,
                               ServerinfoRepository serverinfoRepository){
        this.tempChannelRepository = tempChannelRepository;
        this.botService = botService;
        this.tempChannelHandler = tempChannelHandler;
        this.serverinfoRepository = serverinfoRepository;
    }


    public static void main(String[] args) {
        SpringApplication.run(PandorusApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.botService.startBot();
        this.botService.registerListeners(
                new ServerinfoCommandHandler(serverinfoRepository),
                new TempchannelEventHandler(tempChannelRepository, tempChannelHandler),
                new TempchannelCommandHandler(tempChannelRepository));
    }
}

