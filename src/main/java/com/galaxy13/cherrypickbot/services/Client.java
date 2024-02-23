package com.galaxy13.cherrypickbot.services;

import com.galaxy13.cherrypickbot.dto.Comment;
import com.galaxy13.cherrypickbot.dto.Notification;
import com.galaxy13.cherrypickbot.dto.PullRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class Client {
    private final RestClient restClient;

    @Value("${github.base_url}")
    private String baseUrl;

    @Value("${github.headers}")
    private Map<String, String> headers;
    public Client(){
        RestClient.Builder restBuilder = RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(baseUrl);
        for (Map.Entry<String, String> entry: headers.entrySet()){
            restBuilder = restBuilder.defaultHeader(entry.getKey(), entry.getValue());
        }
        restClient = restBuilder.build();
    }

    public Notification[] getNotifications(String header) {
        return this.restClient.get().uri("notifications")
                .header(header, String.valueOf(true))
                .retrieve()
                .body(Notification[].class);
    }

    public PullRequest getPullRequest(Notification notification) throws Exception {
        if (notification.getRequestURL().isEmpty()){
            throw new Exception("No information in Notification");
        }
        return this.restClient
                .get()
                .uri(notification.getRequestURL())
                .retrieve()
                .body(PullRequest.class);
    }

    public Comment getComment(Notification notification) throws Exception{
        if (notification.getCommentURL().isEmpty()){
            throw new Exception("No information in Notification");
        }
        return this.restClient
                .get()
                .uri(notification.getCommentURL())
                .retrieve()
                .body(Comment.class);
    }

}
