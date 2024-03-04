package com.galaxy13.cherrypickbot.components;

import com.galaxy13.cherrypickbot.configs.ConnectionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class GitWorker {
    private final String baseUrl;

    @Autowired
    public GitWorker(ConnectionProperties properties) {
        this.baseUrl = properties.getBase_url();
    }

    private void cloneRepository(String repository) throws Exception{
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
                String.format("mkdir \"C:\\cherry-pick\\%s\"", repository),
                String.format("cd \"C:\\cherry-pick-bot\\%s\"", repository),
                String.format("git clone %s\\%s", baseUrl, repository));
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println(reader.readLine());
    }

    public void merge(String repository, String fromBranch, String[] toBranches) throws Exception{
        cloneRepository(repository);
    }
}
