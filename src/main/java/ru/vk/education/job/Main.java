package ru.vk.education.job;

import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;
import ru.vk.education.job.service.JobService;
import ru.vk.education.job.util.FileService;
import ru.vk.education.job.util.JobMatchTask;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JobService jobService = new JobService(new UserRepository(), new JobRepository());

        for (String savedLine : FileService.getHistory()) {
            if (savedLine.startsWith("user ")) {
                jobService.processUserCommand(savedLine);
            } else if (savedLine.startsWith("job ")) {
                jobService.processJobCommand(savedLine);
            }
        }

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable matchTask = new JobMatchTask(jobService);
        scheduler.scheduleAtFixedRate(matchTask, 0, 1, TimeUnit.MINUTES);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            if (line.equals("exit")) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduler.shutdownNow();
                }
                System.exit(0);
            }

            String[] tokens = line.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "user":
                    jobService.processUserCommand(line);
                    break;
                case "user-list":
                    jobService.getUsers().forEach(System.out::println);
                    break;
                case "job":
                    jobService.processJobCommand(line);
                    break;
                case "job-list":
                    jobService.getJobs().forEach(System.out::println);
                    break;
                case "suggest":
                    String userName = tokens[1];
                    jobService.suggestJobs(userName).forEach(System.out::println);
                    break;
                case "stat":
                    String cmd = tokens[1];
                    int number = Integer.parseInt(tokens[2]);
                    switch (cmd) {
                        case "--exp" -> jobService.getJobsByExperience(number).forEach(System.out::println);
                        case "--match" -> jobService.getUsersByMatches(number).forEach(System.out::println);
                        case "--top-skills" -> jobService.getTopSkills(number).forEach(System.out::println);
                    }
                    break;
                case "history":
                    FileService.getHistory().forEach(System.out::println);
                    break;
            }

            FileService.saveCommand(line);
        }
    }
}