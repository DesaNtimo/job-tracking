package ru.vk.education.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @Autowired
    public JobService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(userRepository.findAll());
    }

    public void addJob(Job job) {
        jobRepository.save(job);
    }

    public Collection<Job> getJobs() {
        return Collections.unmodifiableCollection(jobRepository.findAll());
    }

    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }

    public List<Job> suggestJobs(String userName) {
        User user = getUser(userName);
        if (user == null) {
            return Collections.emptyList();
        }

        List<JobMatch> matches = new ArrayList<>();
        for (Job job : getJobs()) {
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
        return matches.stream()
                .limit(limit)
                .map(JobMatch::getJob)
                .toList();
    }

    public List<Job> getJobsByExperience(int experience) {
        return getJobs()
                .stream()
                .filter(job -> job.getRequiredExperience() >= experience)
                .sorted(Comparator.comparing(Job::getTitle))
                .toList();
    }

    public List<User> getUsersByMatches(int matches) {
        return getUsers()
                .stream()
                .filter(user -> getJobs()
                        .stream()
                        .filter(job -> !Collections.disjoint(user.getSkills(), job.getSkills()))
                        .count() >= matches)
                .sorted(Comparator.comparing(User::getName))
                .toList();
    }

    public List<String> getTopSkills(int n) {
        return getUsers()
                .stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public void processUserCommand(String line) {
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
            userRepository.save(user);
        }
    }

    public void processJobCommand(String line) {
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
            jobRepository.save(job);
        }
    }
}
