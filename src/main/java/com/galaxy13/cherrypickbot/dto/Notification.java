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
    private String notificationType;

    @JsonProperty("id")
    private String notificationId;


    @JsonProperty("subject")
    private void unpackNested(Map<String, Object> subject) {
        Optional<String> requestOptional = Optional.ofNullable((String) subject.get("url"));
        Optional<String> commentOptional = Optional.ofNullable((String) subject.get("latest_comment_url"));
        Optional<String> subjectType = Optional.ofNullable((String) subject.get("type"));
        requestURL = requestOptional.orElse("Empty");
        commentURL = commentOptional.orElse("Empty");
        notificationType = subjectType.orElse("Empty");
    }

    public Comment getComment(RestClient restClient) throws JsonParseException {
        return restClient.get()
                .uri(commentURL)
                .retrieve()
                .body(Comment.class);
    }

    public boolean checkSelf() {
        return (!requestURL.equals("Empty") && !commentURL.equals("Empty") && !notificationType.equals("Empty"));
    }
}