package de.cloudypanda.spring;

import de.cloudypanda.discord.DiscordService;
import de.cloudypanda.discord.DiscordServiceImpl;
import de.cloudypanda.models.TempChannelHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfiguration {

    private final EnvironmentConfiguration environmentConfiguration;

    public SpringApplicationConfiguration(EnvironmentConfiguration environmentConfiguration){
        this.environmentConfiguration = environmentConfiguration;
    }
    @Bean
    public DiscordService botService() {
        return new DiscordServiceImpl(environmentConfiguration);
    }

    @Bean
    public TempChannelHandler tempChannelHandler() {
        return new TempChannelHandler();
    }

}

