package ru.vk.education.job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vk.education.job.service.JobService;
import ru.vk.education.job.service.StatService;
import ru.vk.education.job.service.SuggestService;
import ru.vk.education.job.service.UserService;
import ru.vk.education.job.util.FileService;

import java.util.Scanner;

@Component
public class CliRunner implements CommandLineRunner {

    private final JobService jobService;
    private final UserService userService;
    private final SuggestService suggestService;
    private final StatService statService;

    public CliRunner(JobService jobService, UserService userService, SuggestService suggestService, StatService statService) {
        this.jobService = jobService;
        this.userService = userService;
        this.suggestService = suggestService;
        this.statService = statService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        // Восстанавливаем историю
        for (String savedLine : FileService.getHistory()) {
            if (savedLine.startsWith("user ")) {
                userService.processUserCommand(savedLine);
            } else if (savedLine.startsWith("job ")) {
                jobService.processJobCommand(savedLine);
            }
        }

        System.out.println("Spring Boot CLI запущен. Введите команду:");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            // Завершает работу и CLI, и веб-сервера Spring
            if (line.equals("exit")) {
                System.exit(0);
            }

            String[] tokens = line.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "user":
                    userService.processUserCommand(line);
                    break;
                case "user-list":
                    userService.getUsers().forEach(System.out::println);
                    break;
                case "job":
                    jobService.processJobCommand(line);
                    break;
                case "job-list":
                    jobService.getJobs().forEach(System.out::println);
                    break;
                case "suggest":
                    String userName = tokens[1];
                    suggestService.suggest(userName).forEach(System.out::println);
                    break;
                case "stat":
                    String cmd = tokens[1];
                    int number = Integer.parseInt(tokens[2]);
                    switch (cmd) {
                        case "--exp" -> statService.getJobsByExperience(number).forEach(System.out::println);
                        case "--match" -> statService.getUsersByMatches(number).forEach(System.out::println);
                        case "--top-skills" -> statService.getTopSkills(number).forEach(System.out::println);
                    }
                    break;
                case "history":
                    FileService.getHistory().forEach(System.out::println);
                    break;
                default:
                    System.out.println("Неизвестная команда.");
            }

            FileService.saveCommand(line);
        }
    }
}
