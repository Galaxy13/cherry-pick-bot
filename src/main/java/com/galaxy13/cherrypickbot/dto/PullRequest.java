package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class PullRequest {
    private String label;
    private String[] commits;

    @JsonProperty("head")
    private void unpackNestedHead(Map<String, Object> head) throws JsonParseException {
        Optional<String> labelOptional = Optional.ofNullable(head.get("label").toString());
        if (labelOptional.isPresent()){
            this.label = labelOptional.get();
        } else {
            throw new JsonParseException("Parsing request label error");
        }
    }
}
