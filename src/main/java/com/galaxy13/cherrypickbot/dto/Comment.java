package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.fasterxml.jackson.core.JsonParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Comment {
    private String[] targetBranches = new String[]{""};
    private boolean isCherryPick = false;

    @JsonProperty("body")
    public void analyzeTextBody(String text) throws JsonParseException {
        if (text.isEmpty()){
            throw new JsonParseException("Comment 'body' is empty");
        }
        if (!Pattern.compile("cherry-pick").matcher(text).find()){
            System.out.println("Log: Command 'cherry-pick not found in last notification'");
        } else {
            isCherryPick = true;
        }
        Matcher branchesPatternMatcher = Pattern.compile("(?<=cherry-pick to )[a-z0-9A-Z].+").matcher(text);
        if (branchesPatternMatcher.find()){
            System.out.println("Log: Found branches? in Comment");
            targetBranches = branchesPatternMatcher.group().split(" ");
        } else {
            System.out.println("Log: Branches not found in Comment");
        }
    }
}
