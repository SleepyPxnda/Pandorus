package de.cloudypanda.spring;


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
        return env.getProperty("JDA_TOKEN");
    }

    public void logEnvVars() {
        LOGGER.info("JDA_TOKEN: " + env.getProperty("JDA_TOKEN"));
    }
}
