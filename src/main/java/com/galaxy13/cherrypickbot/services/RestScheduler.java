package com.galaxy13.cherrypickbot.services;


import com.galaxy13.cherrypickbot.components.Client;
import com.galaxy13.cherrypickbot.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RestScheduler {

    private final Client client;

    @Autowired
    public RestScheduler(Client client){
        this.client = client;
    }
    @Scheduled(fixedRate = 5000)
    public void scheduleComment() throws Exception {
        Comment comment = client.getComment();
        System.out.println(comment.getCommitSHA());
    }
}
