package ru.vk.education.job.service;

import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;

import java.util.*;
import java.util.stream.Collectors;

public class JobService {
    private final Map<String, User> users = new TreeMap<>();
    private final Map<String, Job> jobs = new TreeMap<>();

    public void addUser(User user) {
        users.putIfAbsent(user.getName(), user);
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    public void addJob(Job job) {
        jobs.putIfAbsent(job.getTitle(), job);
    }

    public Collection<Job> getJobs() {
        return Collections.unmodifiableCollection(jobs.values());
    }

    public User getUser(String name) {
        return users.get(name);
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
}
