package com.galaxy13.cherrypickbot.services;


import com.fasterxml.jackson.core.JsonParseException;
import com.galaxy13.cherrypickbot.components.Client;
import com.galaxy13.cherrypickbot.components.GitWorker;
import com.galaxy13.cherrypickbot.components.Logger;
import com.galaxy13.cherrypickbot.dto.CommandType;
import com.galaxy13.cherrypickbot.dto.Comment;
import com.galaxy13.cherrypickbot.dto.Commit;
import com.galaxy13.cherrypickbot.dto.Notification;
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
        Logger.logStart();
        this.gitWorker = gitWorker;
        this.client = client;
    }

    @Scheduled(fixedRate = 50000)
    public void scheduleComments(){
        Notification[] notifications = client.getNotifications("all");
        if (notifications.length == 0) {
            Logger.logWrite("No active notifications");
            return;
        }
        for (Notification notification: notifications){
            if (!notification.getNotificationType().equals("PullRequest") || !notification.checkSelf()) {
                if (!client.closeNotification(notification)) {
                    Logger.logWrite(notification.getNotificationId() + "cannot be closed");
                }
                continue;
            }
            Comment comment;
            try {
                comment = client.getComment(notification);
                Logger.logWrite("Comment " + notification.getCommentURL() + " handled");
            } catch (JsonParseException e) {
                Logger.logErr(e);
                client.closeNotification(notification);
                continue;
            }
            if (comment.getCommandType() == CommandType.MERGE) {
                try {
                    gitWorker.merge("TestCherryPickRepository", "develop", comment.getTargetBranches(), comment.getUserName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (comment.getCommandType() == CommandType.CHERRYPICK) {
                try {
                    Commit[] commits = client.getRequestCommits(notification);
                    gitWorker.cherryPickCommits(commits, "develop", comment.getTargetBranches(), comment.getUserName(), comment.getRepository());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            client.closeNotification(notification);
        }

    }
}
