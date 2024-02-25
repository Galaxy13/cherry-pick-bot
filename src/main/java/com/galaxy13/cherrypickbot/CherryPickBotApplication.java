package com.galaxy13.cherrypickbot;

import com.galaxy13.cherrypickbot.configs.RestBotClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RestBotClientProperties.class)
public class CherryPickBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CherryPickBotApplication.class, args);
    }

}
