package ru.vk.education.job.repository;

import org.springframework.stereotype.Repository;
import ru.vk.education.job.domain.Job;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class JobRepository {
    private final Map<String, Job> jobs = new TreeMap<>();

    public void save(Job job) {
        jobs.putIfAbsent(job.getTitle(), job);
    }

    public Collection<Job> findAll() {
        return jobs.values();
    }
}