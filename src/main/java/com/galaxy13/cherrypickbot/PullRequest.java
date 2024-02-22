package com.galaxy13.cherrypickbot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PullRequest {
    private String title;

    @JsonProperty("head")
    private void unpackNested(Map<String, Object> head) {
        this.title = head.get("ref").toString();
    }

    public String getTitle() {
        return title;
    }
}
