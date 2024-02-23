package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.fasterxml.jackson.core.JsonParseException;

import java.util.regex.Pattern;

@Getter
public class Comment {
    private String[] targetBranches;
    private String commitSHA;
    private boolean isCherryPick;

    @JsonProperty("body")
    public void analyzeTextBody(String text) throws JsonParseException {
        if (text.isEmpty()){
            throw new JsonParseException("Comment 'body' is empty");
        }
        this.isCherryPick = text.contains("cherry-pick");
        this.commitSHA = Pattern.compile("(?<=cherry-pick )[a-f0-9]{40}").matcher(text).toString();
        this.targetBranches = Pattern.compile("(?<=[a-f0-9]{40} ).+").matcher(text).toString().split(" ");
    }
}
