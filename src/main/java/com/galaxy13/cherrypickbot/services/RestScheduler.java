package com.galaxy13.cherrypickbot.services;


import com.fasterxml.jackson.core.JsonParseException;
import com.galaxy13.cherrypickbot.components.Client;
import com.galaxy13.cherrypickbot.components.GitWorker;
import com.galaxy13.cherrypickbot.dto.CommandType;
import com.galaxy13.cherrypickbot.dto.Comment;
import com.galaxy13.cherrypickbot.dto.Commit;
import com.galaxy13.cherrypickbot.dto.Notification;
import com.galaxy13.cherrypickbot.errors.NotAPullRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class RestScheduler {

    private final Client client;
    private final GitWorker gitWorker;

    @Autowired
    public RestScheduler(Client client, GitWorker gitWorker) {
        this.gitWorker = gitWorker;
        this.client = client;
    }

    @Scheduled(fixedRate = 50000)
    public void scheduleComments(){
        try {
            Notification[] notifications = client.getNotifications("all");
        } catch (NotAPullRequest e){
            System.out.println(e.getMessage());
            return;
        } catch (JsonParseException){
            System.out.println();
        }
        for (Notification notification: notifications){
            Comment comment = client.getComment(notification);

        }
        if (lastComment.getCommandType() == CommandType.MERGE) {
            gitWorker.merge("TestCherryPickRepository", "develop", lastComment.getTargetBranches(), lastComment.getUserName());
        } else if (lastComment.getCommandType() == CommandType.CHERRYPICK) {
            Commit[] commits = client.getRequestCommits();
            gitWorker.cherryPickCommits(commits, "develop", lastComment.getTargetBranches(), lastComment.getUserName(), lastComment.getRepository());
        }
    }
}
