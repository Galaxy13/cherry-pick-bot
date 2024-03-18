package com.galaxy13.cherrypickbot.components;

import com.fasterxml.jackson.core.JsonParseException;
import com.galaxy13.cherrypickbot.configs.ConnectionProperties;
import com.galaxy13.cherrypickbot.dto.Comment;
import com.galaxy13.cherrypickbot.dto.Commit;
import com.galaxy13.cherrypickbot.dto.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class Client {
    private final RestClient restClient;

    @Autowired
    public Client(ConnectionProperties properties){
        RestClient.Builder restBuilder = RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(properties.getBase_url_api());
        try{
            for (Map.Entry<String, String> entry: properties.getHeaders().entrySet()){
                restBuilder = restBuilder.defaultHeader(entry.getKey(), entry.getValue());
            }
        } catch (NullPointerException e){
            System.out.println("No headers for Rest Builder");
        }
        restClient = restBuilder.build();
    }

    public Notification[] getNotifications(String header) {
        Logger.logWrite("Notifications retrieving");
        return this.restClient.get().uri("notifications")
                .header(header, String.valueOf(true))
                .retrieve()
                .body(Notification[].class);
    }

    public boolean closeNotification(Notification notification) {
        ResponseEntity<Void> responseEntity = restClient.delete()
                .uri("notifications/threads/" + notification.getNotificationId())
                .retrieve()
                .toBodilessEntity();
        Logger.logWrite("Closing notification " + notification.getNotificationId());
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    //currently unused
//    public PullRequest getPullRequest() throws Exception {
//        this.getNotifications("all");
//        if (this.notifications.length == 0){
//            throw new Exception("No information in Notifications");
//        }
//        return this.restClient
//                .get()
//                .uri(this.notifications[notifications.length-1].getRequestURL())
//                .retrieve()
//                .body(PullRequest.class);
//    }

    public Comment getComment(Notification notification) throws JsonParseException {
        return notification.getComment(restClient);
    }

    // currently unused
    public Commit[] getRequestCommits(Notification notification) throws Exception {
        String requestURL = notification.getRequestURL();
        if (requestURL.isEmpty()) {
            throw new Exception("No pull request information");
        }
        Logger.logWrite("Retrieving commits from " + notification.getNotificationId());
        return this.restClient
                .get()
                .uri(requestURL + "/commits")
                .retrieve()
                .body(Commit[].class);
    }
}
