package com.galaxy13.cherrypickbot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Notification {
    private String requestURL;
    private String commentURL;

    @JsonProperty("subject")
    private void unpackNested(Map<String, Object> subject) {
        this.requestURL = (String) subject.get("url");
        this.commentURL = (String) subject.get("latest_comment_url");
    }

    public String getCommentURL() {
        return commentURL;
    }

    public String getRequestURL() {
        return requestURL;
    }
}