package com.galaxy13.cherrypickbot.components;

import com.galaxy13.cherrypickbot.configs.ConnectionProperties;
import com.galaxy13.cherrypickbot.dto.Comment;
import com.galaxy13.cherrypickbot.dto.Commit;
import com.galaxy13.cherrypickbot.dto.Notification;
import com.galaxy13.cherrypickbot.dto.PullRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class Client {
    private final RestClient restClient;
    private Notification[] notifications;

    @Autowired
    public Client(ConnectionProperties properties){
        RestClient.Builder restBuilder = RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(properties.getBase_url());
        try{
            for (Map.Entry<String, String> entry: properties.getHeaders().entrySet()){
                restBuilder = restBuilder.defaultHeader(entry.getKey(), entry.getValue());
            }
        } catch (NullPointerException e){
            System.out.println("No headers for Rest Builder");
        }
        restClient = restBuilder.build();
    }

    private void getNotifications(String header) {
        this.notifications = this.restClient.get().uri("notifications")
                .header(header, String.valueOf(true))
                .retrieve()
                .body(Notification[].class);
    }

    public PullRequest getPullRequest() throws Exception {
        this.getNotifications("all");
        if (this.notifications.length == 0){
            throw new Exception("No information in Notifications");
        }
        return this.restClient
                .get()
                .uri(this.notifications[notifications.length-1].getRequestURL())
                .retrieve()
                .body(PullRequest.class);
    }

    public Comment getComment() throws Exception{
        this.getNotifications("all");
        if (notifications.length == 0){
            throw new Exception("No information in Notification");
        }
        return this.restClient
                .get()
                .uri(this.notifications[0].getCommentURL())
                .retrieve()
                .body(Comment.class);
    }

    public Commit[] getRequestCommits() throws Exception {
        this.getNotifications("all");
        if (this.notifications.length == 0) {
            throw new Exception("No information in Notifications");
        }
        String requestURL = notifications[0].getRequestURL();
        if (requestURL.isEmpty()) {
            throw new Exception("No pull request information");
        }
        return this.restClient
                .get()
                .uri(requestURL + "/commits")
                .retrieve()
                .body(Commit[].class);
    }

}
