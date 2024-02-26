package com.galaxy13.cherrypickbot.services;


import com.galaxy13.cherrypickbot.components.Client;
import com.galaxy13.cherrypickbot.dto.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
        Commit[] commits = client.getRequestCommits();
        System.out.println(Arrays.stream(commits).map(Commit::getCommit).toList());
    }
}
