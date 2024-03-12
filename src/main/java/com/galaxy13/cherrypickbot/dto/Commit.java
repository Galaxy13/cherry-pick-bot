package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Commit {
    @JsonProperty("sha")
    private String commitSHA;
}
