package ru.vk.education.job.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {
    private static final Path filePath = Path.of("history.txt");

    public static void saveCommand(String command) {
        try {
            Files.writeString(filePath,
                    command + System.lineSeparator(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Unable to save command: " + command);
        }
    }

    public static List<String> getHistory() {
        try {
            if (!Files.exists(filePath)) {
                return List.of();
            }
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("Unable to get history: " + e.getMessage());
            return List.of();
        }
    }
}
