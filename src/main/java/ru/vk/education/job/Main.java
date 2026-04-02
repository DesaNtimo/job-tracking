package ru.vk.education.job;

import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.JobMatch;
import ru.vk.education.job.service.JobService;
import ru.vk.education.job.util.FileService;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JobService jobService = new JobService();

        for (String savedLine : FileService.getHistory()) {
            if (savedLine.startsWith("user ")) {
                processUserCommand(savedLine, jobService);
            } else if (savedLine.startsWith("job ")) {
                processJobCommand(savedLine, jobService);
            }
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            if (line.equals("exit")) {
                System.exit(0);
            }

            String[] tokens = line.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "user":
                    processUserCommand(line, jobService);
                    break;
                case "user-list":
                    jobService.getUsers().forEach(System.out::println);
                    break;
                case "job":
                    processJobCommand(line, jobService);
                    break;
                case "job-list":
                    jobService.getJobs().forEach(System.out::println);
                    break;
                case "suggest":
                    String userName = tokens[1];
                    User user = jobService.getUser(userName);
                    List<JobMatch> matches = new ArrayList<>();

                    for (Job job : jobService.getJobs()) {
                        int score = 0;

                        for (String skill : user.getSkills()) {
                            if (job.getSkills().contains(skill)) {
                                score++;
                            }
                        }

                        if (score == 0) continue;
                        if (user.getExperience() < job.getRequiredExperience()) score /= 2;
                        matches.add(new JobMatch(job, score));
                    }
                    matches.sort(Comparator.comparingInt(JobMatch::getScore).reversed());
                    int limit = Math.min(2, matches.size());
                    for (int i = 0; i < limit; i++) {
                        System.out.println(matches.get(i).getJob());
                    }
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

    private static void processUserCommand(String line, JobService jobService) {
        String[] tokens = line.split("\\s+");
        String username = tokens[1];
        Set<String> userSkills = new HashSet<>();
        byte userExperience = 0;

        for (int i = 2; i < tokens.length; i++) {
            if (tokens[i].startsWith("--skills=")) {
                String skillPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                String[] stringSkills = skillPart.split(",");
                Collections.addAll(userSkills, stringSkills);
            } else if (tokens[i].startsWith("--exp=")) {
                String expPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                userExperience = Byte.parseByte(expPart);
            }
        }

        if (!userSkills.isEmpty() && userExperience >= 0) {
            User user = new User(username, userSkills, userExperience);
            jobService.addUser(user);
        }
    }

    private static void processJobCommand(String line, JobService jobService) {
        String[] tokens = line.split("\\s+");
        String title = tokens[1];
        String company = null;
        Set<String> skills = new HashSet<>();
        byte requiredExperience = 0;

        for (int i = 2; i < tokens.length; i++) {
            if (tokens[i].startsWith("--company=")) {
                company = tokens[i].substring(tokens[i].indexOf('=') + 1);
            } else if (tokens[i].startsWith("--tags=")) {
                String tags = tokens[i].substring(tokens[i].indexOf('=') + 1);
                String[] stringSkills = tags.split(",");
                Collections.addAll(skills, stringSkills);
            } else if (tokens[i].startsWith("--exp=")) {
                String expPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                requiredExperience = Byte.parseByte(expPart);
            }
        }

        if (!skills.isEmpty() && requiredExperience >= 0 && company != null) {
            Job job = new Job(title, company, skills, requiredExperience);
            jobService.addJob(job);
        }
    }
}