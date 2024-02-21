package com.galaxy13.cherrypickbot;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class Client {
    public static void main(String[] args) {
        RestClient restClient = RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl("https://api.github.com")
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("Authorization", "Bearer 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")
                .defaultHeader("X-Github-Api-Version", "2022-11-28")
                .build();
        Notification[] response = restClient.get().uri("https://api.github.com/notifications")
                .header("all", String.valueOf(true))
                .retrieve()
                .body(Notification[].class);
        assert response != null;
        Notification test_notification = response[0];
        System.out.println(test_notification.commentURL);
    }

}
