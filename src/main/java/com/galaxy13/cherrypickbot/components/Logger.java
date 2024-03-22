package com.galaxy13.cherrypickbot.components;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Logger {
    private static final File logFile = new File("logs/log.txt");

    private static boolean writeToFile(String logMessage) {
        try {
            if (!logFile.exists()) {
                if (logFile.mkdirs()) {
                    System.out.println("Directories for logging created");
                }
                if (logFile.createNewFile()) {
                    System.out.println("New log file created");
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            Date currentTime = new Date();
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH::mm::ss");
            writer.write(formatter.format(currentTime) + ": " + logMessage);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            System.out.println("Log write error");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean logStart() {
        return writeToFile("Service cherry-pick-bot started");
    }

    public static boolean logFinish() {
        return writeToFile("Service finished");
    }

    public static boolean logWrite(String customMessage) {
        return writeToFile(customMessage);
    }

    public static boolean logErr(Throwable e) {
        return writeToFile(e.getMessage());
    }
}
