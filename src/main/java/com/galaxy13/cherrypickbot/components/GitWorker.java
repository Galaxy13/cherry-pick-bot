package com.galaxy13.cherrypickbot.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GitWorker {
    @Value("$github.base_url")
    private final String baseUrl;
    private final String repository;
    private final String pullBranch;
    private final String[] pushBranches;

    @Autowired
    public GitWorker(Client client) {

    }
}
