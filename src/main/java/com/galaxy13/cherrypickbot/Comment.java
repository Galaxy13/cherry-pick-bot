package com.galaxy13.cherrypickbot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

public class Comment {
    @JsonProperty("body")
    private String text;
    private String[] targetBranches;
    private String commitSHA;
    private boolean isCherryPick;

    public void analyzeTextBody() {
        this.isCherryPick = text.contains("cherry-pick");
        this.commitSHA = Pattern.compile("(?<=cherry-pick )[a-f0-9]{40}").matcher(text).toString();
        this.targetBranches = Pattern.compile("(?<=[a-f0-9]{40} ).+").matcher(text).toString().split(" ");
    }

    public boolean isCherryPick() {
        return isCherryPick;
    }

    public String getCommitSHA() {
        return commitSHA;
    }

    public String[] getTargetBranches() {
        return targetBranches;
    }
}
