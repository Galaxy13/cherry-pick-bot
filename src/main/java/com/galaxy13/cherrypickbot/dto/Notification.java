package com.galaxy13.cherrypickbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.galaxy13.cherrypickbot.errors.NotAPullRequest;
import lombok.Getter;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Optional;

@Getter
public class Notification {
    private String requestURL;
    private String commentURL;


    @JsonProperty("subject")
    private void unpackNested(Map<String, Object> subject) throws JsonParseException, NotAPullRequest {
        Optional<String> requestOptional = Optional.ofNullable((String) subject.get("url"));
        Optional<String> commentOptional = Optional.ofNullable((String) subject.get("latest_comment_url"));
        Optional<String> subjectType = Optional.ofNullable((String) subject.get("type"));
        if (requestOptional.isPresent() && commentOptional.isPresent() && subjectType.isPresent()){
            this.requestURL = requestOptional.get();
            this.commentURL = commentOptional.get();
            if (!subjectType.get().equals("PullRequest")){
                throw new NotAPullRequest("Not a PullRequest");
            }
        } else {
            throw new JsonParseException("Wrong Json schema provided for Notification class");
        }
    }

    public Comment getComment(RestClient restClient){
        return restClient.get()
                .uri(commentURL)
                .retrieve()
                .body(Comment.class);
    }
}