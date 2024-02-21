package com.galaxy13.cherrypickbot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Notification {
    String requestURL;
    String commentURL;

    @JsonProperty("subject")
    private void unpackNested(Map<String, Object> subject) {
        this.requestURL = (String) subject.get("url");
        this.commentURL = (String) subject.get("latest_comment_url");
    }

}