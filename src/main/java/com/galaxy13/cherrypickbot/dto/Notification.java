package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class Notification {
    private String requestURL;
    private String commentURL;

    @JsonProperty("subject")
    private void unpackNested(Map<String, Object> subject) throws JsonParseException {
        Optional<String> requestOptional = Optional.ofNullable((String) subject.get("url"));
        Optional<String> commentOptional = Optional.ofNullable((String) subject.get("latest_comment_url"));
        if (requestOptional.isPresent() && commentOptional.isPresent()){
            this.requestURL = requestOptional.get();
            this.commentURL = commentOptional.get();
        } else {
            throw new JsonParseException("Wrong Json schema provided for Notification class");
        }
    }

}