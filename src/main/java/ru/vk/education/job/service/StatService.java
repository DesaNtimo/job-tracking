package ru.vk.education.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatService {

    private final UserService userService;
    private final JobService jobService;

    @Autowired
    public StatService(UserService userService, JobService jobService) {
        this.userService = userService;
        this.jobService = jobService;
    }

    public List<Job> getJobsByExperience(int experience) {
        return jobService.getJobs()
                .stream()
                .filter(job -> job.getRequiredExperience() >= experience)
                .sorted(Comparator.comparing(Job::getTitle))
                .toList();
    }

    public List<User> getUsersByMatches(int matches) {
        return userService.getUsers()
                .stream()
                .filter(user -> jobService.getJobs()
                        .stream()
                        .filter(job -> !Collections.disjoint(user.getSkills(), job.getSkills()))
                        .count() >= matches)
                .sorted(Comparator.comparing(User::getName))
                .toList();
    }

    public List<String> getTopSkills(int n) {
        return userService.getUsers()
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
