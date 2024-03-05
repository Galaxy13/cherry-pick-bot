package com.galaxy13.cherrypickbot.components;

import com.galaxy13.cherrypickbot.configs.ConnectionProperties;
import com.galaxy13.cherrypickbot.dto.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

@Component
public class GitWorker {
    private final String originUrl;
    private final String botName;
    private final String token;

    @Autowired
    public GitWorker(ConnectionProperties properties) {
        this.originUrl = properties.getBase_url_git();
        this.botName = properties.getBot().get("username");
        this.token = properties.getBot().get("token");
    }

    private BufferedReader runCommand(Path directory, String... command) throws IOException {
        if (!Files.exists(directory)) {
            throw new IOException("Directory not exist");
        }
        ProcessBuilder builder = new ProcessBuilder()
                .command(command)
                .directory(directory.toFile());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    private void cloneRepository(String repository) throws Exception{
        Path directory = Path.of("C:\\cherry-pick-bot\\Galaxy13");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        runCommand(directory, "git",
                "clone",
                String.format("https://%s:%s@%s/Galaxy13/%s", botName, token, originUrl, repository));
    }

    private boolean gitStatus(Path directory, String branch) throws IOException {
        try {
            BufferedReader reader = runCommand(directory,
                    "git", "status");
            String firstLine = reader.readLine();
            return Pattern.compile(String.format("On branch %s", branch)).matcher(firstLine).find();
        } catch (IOException e) {
            return false;
        }
    }

    private void gitCheckout(Path directory, String branch) throws IOException {
        BufferedReader reader = runCommand(directory, "git", "checkout", branch);
        System.out.println(reader.readLine());
        if (!gitStatus(directory, branch)) {
            throw new IOException(String.format("Checkout to branch %s error", branch));
        }
    }

    private void gitPush(Path directory, String remoteBranch) {

    }

    private void cherryPickCommits(Commit[] commits, String fromBranch, String[] toBranches) {

    }

    public void merge(String repository, String fromBranch, String[] toBranches) throws Exception{
        Path directory = Path.of("C:\\cherry-pick-bot\\Galaxy13\\" + repository);
        cloneRepository(repository);
        for (String branch : toBranches) {
            gitCheckout(directory, branch);
            if (!gitStatus(directory, branch)) {
                throw new Exception("Git checkout error");
            }
            try {
                BufferedReader bufferedReader = runCommand(directory, "git", "merge", fromBranch);
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
                System.out.printf("Branch %s merged with %s%n", fromBranch, branch);
            } catch (IOException e) {
                throw new Exception(String.format("Merge from %s to %s error", fromBranch, branch));
            }
        }
    }
}
