package de.cloudypanda;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(EnvironmentConfiguration.class);

    @Autowired
    public Environment env;

    public String getDiscordToken(){
        LOGGER.info("JDA_TOKEN: " + env.getProperty("JDA_TOKEN"));
        return env.getProperty("JDA_TOKEN");
    }

    public String getTempChannelSourceId(){
        LOGGER.info("TEMP_SOURCE_CHANNEL_ID: " + env.getProperty("TEMP_SOURCE_CHANNEL_ID"));
        return env.getProperty("TEMP_SOURCE_CHANNEL_ID");
    }
}
