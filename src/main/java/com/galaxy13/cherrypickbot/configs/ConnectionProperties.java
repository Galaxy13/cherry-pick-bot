package com.galaxy13.cherrypickbot.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "github")
@Getter
@Setter
public class ConnectionProperties {

    private Map<String, String> headers;
    private String base_url_api;
    private Map<String, String> notification;
    private String base_url_git;
    private Map<String, String> bot;
}
