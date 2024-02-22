package com.galaxy13.cherrypickbot;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.io.FileInputStream;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        String gpg;
        try (FileInputStream inputStream = new FileInputStream("application.txt")) {
            gpg = new String(inputStream.readAllBytes());
        }
        RestClient restClient = RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("Authorization", "Bearer " + gpg)
                .defaultHeader("X-Github-Api-Version", "2022-11-28")
                .build();
        Notification[] response = restClient.get().uri("https://api.github.com/notifications")
                .header("all", String.valueOf(true))
                .retrieve()
                .body(Notification[].class);
        assert response != null;
        Notification testNotification = response[0];
        PullRequest pullRequest = restClient.get().uri(testNotification.getRequestURL())
                .retrieve()
                .body(PullRequest.class);
        assert pullRequest != null;
        Comment comment = restClient.get().uri(testNotification.getCommentURL()).retrieve().body(Comment.class);
        assert comment != null;
        comment.analyzeTextBody();
    }

}
