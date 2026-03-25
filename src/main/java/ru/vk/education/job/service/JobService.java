package ru.vk.education.job.service;

import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;

import java.util.*;

public class JobService {
    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Job> jobs = new LinkedHashMap<>();

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
}
