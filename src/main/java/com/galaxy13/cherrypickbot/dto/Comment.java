package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.fasterxml.jackson.core.JsonParseException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Comment {
    private String[] targetBranches = new String[]{""};
    private CommandType commandType;
    private String userName;
    private String repository;

    @JsonProperty("body")
    public void analyzeTextBody(String text) throws JsonParseException {
        if (text.isEmpty()){
            throw new JsonParseException("Comment 'body' is empty");
        }
        if (Pattern.compile("cherry-pick").matcher(text).find()) {
            commandType = CommandType.CHERRYPICK;
        } else if (!Pattern.compile("merge").matcher(text).find()) {
            commandType = CommandType.MERGE;
        } else {
            throw new JsonParseException("No command found in comment");
        }
        Matcher branchesPatternMatcher = Pattern.compile(("(?<=" + commandType.getCommand() + " to )[a-z0-9A-Z].+")).matcher(text);
        if (branchesPatternMatcher.find()){
            System.out.println("Log: Found branches in Comment");
            targetBranches = branchesPatternMatcher.group().split(" ");
        } else {
            System.out.println("Log: Branches not found in Comment");
        }
    }

    @JsonProperty("user")
    public void setUserName(Map<String, Object> userInfo) throws JsonParseException {
        if (!userInfo.containsKey("login")) {
            throw new JsonParseException("Username parse error");
        }
        this.userName = userInfo.get("login").toString();
    }

    @JsonProperty("url")
    public void setRepository(String url) throws JsonParseException {
        if (url == null) {
            throw new JsonParseException("Repository parse error");
        }
        this.repository = url.split("/")[5];
    }
}
