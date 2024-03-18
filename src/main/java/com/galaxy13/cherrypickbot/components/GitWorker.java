package com.galaxy13.cherrypickbot.components;

import com.galaxy13.cherrypickbot.configs.ConnectionProperties;
import com.galaxy13.cherrypickbot.dto.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private void cloneRepository(String repository, Path directory, String userName) throws Exception {
        Logger.logWrite("Cloning repository...");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        try {
            BufferedReader reader = runCommand(directory, "git",
                    "clone",
                    String.format("https://oauth2:%s@%s/%s/%s", token, originUrl, userName, repository), ".");
            printReader(reader);
        } catch (IOException e) {
            Logger.logErr(e);
        }
        Logger.logWrite("Cloning done");
    }

    private boolean gitStatus(Path directory, String branch) {
        try {
            BufferedReader reader = runCommand(directory,
                    "git", "status");
            String firstLine = reader.readLine();
            return Pattern.compile(String.format("On branch %s", branch)).matcher(firstLine).find();
        } catch (IOException e) {
            Logger.logErr(e);
            return false;
        }
    }

    private void gitCheckout(Path directory, String branch) throws IOException {
        Logger.logWrite("Perform checkout to branch " + branch);
        BufferedReader reader = runCommand(directory, "git", "checkout", branch);
        System.out.println(reader.readLine());
        if (!gitStatus(directory, branch)) {
            IOException exp = new IOException(String.format("Checkout to branch %s error", branch));
            Logger.logErr(exp);
            throw exp;
        }
    }

    private void gitCommit(Path directory, String branch) throws IOException {
        gitCheckout(directory, branch);
        runCommand(directory, "git", "commit");
    }

    private void printReader(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }

    private void gitPush(Path directory, String remoteBranch) throws IOException {
        Logger.logWrite("Perfrom push to origin " + remoteBranch);
        gitCheckout(directory, remoteBranch);
        if (!gitStatus(directory, remoteBranch)) {
            IOException ioException = new IOException("Status check error");
            Logger.logErr(ioException);
            throw ioException;
        }
        try {
            BufferedReader reader = runCommand(directory, "git", "push");
            printReader(reader);
        } catch (IOException e) {
            Logger.logErr(e);
            throw new IOException("Push branch to origin error");
        }
    }

    public void cherryPickCommits(Commit[] commits, String fromBranch, String[] toBranches, String userName, String repository) throws Exception {
        Logger.logWrite("Cherry pick " + commits.toString() + " from " + fromBranch + " to " + toBranches + " started...");
        Path directory = makeDirectoryPath(repository, userName);
        cloneRepository(repository, directory, userName);
        for (String branch : toBranches) {
            gitCheckout(directory, branch);
            if (!gitStatus(directory, branch)) {
                Exception exception = new Exception("Git checkout error");
                Logger.logErr(exception);
                throw exception;
            }
            for (Commit commit : commits) {
                try {
                    BufferedReader reader = runCommand(directory, "git", "cherry-pick", commit.getCommitSHA());
                    printReader(reader);
                    Logger.logWrite(String.format("Commit %s cherry-picked from %s to branch %s%n", commit.getCommitSHA(), fromBranch, branch));
                } catch (IOException e) {
                    Logger.logErr(e);
                    throw new Exception(String.format("Cherry-pick %s commit error to branch %s%n", commit.getCommitSHA(), branch));
                }
            }
            try {
                gitPush(directory, branch);
                Logger.logWrite("Successful push to branch " + branch);
            } catch (IOException e) {
                Logger.logErr(e);
            }
        }
    }

    private Path makeDirectoryPath(String repository, String userName) {
        return Path.of(String.format("C:\\cherry-pick-bot\\%s\\%s", userName, repository));
    }

    public void merge(String repository, String fromBranch, String[] toBranches, String userName) throws Exception {
        Path directory = makeDirectoryPath(repository, userName);
        cloneRepository(repository, directory, userName);
        for (String branch : toBranches) {
            gitCheckout(directory, branch);
            if (!gitStatus(directory, branch)) {
                Exception exception = new Exception("Git checkout error");
                Logger.logErr(exception);
                throw exception;
            }
            try {
                BufferedReader bufferedReader = runCommand(directory, "git", "merge", fromBranch);
                printReader(bufferedReader);
                System.out.printf("Branch %s merged with %s%n", fromBranch, branch);
            } catch (IOException e) {
                Logger.logErr(e);
                throw new Exception(String.format("Merge from %s to %s error", fromBranch, branch));
            }
//            gitCommit(directory, branch);
            gitPush(directory, branch);
            Logger.logWrite("Merge completed");
        }
    }
}
