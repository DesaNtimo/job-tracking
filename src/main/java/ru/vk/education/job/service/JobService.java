package ru.vk.education.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.repository.JobRepository;

import java.util.*;

@Service
public class JobService {
    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void addJob(Job job) {
        jobRepository.save(job);
    }

    public Collection<Job> getJobs() {
        return Collections.unmodifiableCollection(jobRepository.findAll());
    }

    public void processJobCommand(String line) {
        String[] tokens = line.split("\\s+");
        String title = tokens[1];
        String company = null;
        Set<String> skills = new HashSet<>();
        int requiredExperience = 0;

        for (int i = 2; i < tokens.length; i++) {
            if (tokens[i].startsWith("--company=")) {
                company = tokens[i].substring(tokens[i].indexOf('=') + 1);
            } else if (tokens[i].startsWith("--tags=")) {
                String tags = tokens[i].substring(tokens[i].indexOf('=') + 1);
                String[] stringSkills = tags.split(",");
                Collections.addAll(skills, stringSkills);
            } else if (tokens[i].startsWith("--exp=")) {
                String expPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                requiredExperience = Integer.parseInt(expPart);
            }
        }

        if (!skills.isEmpty() && requiredExperience >= 0 && company != null) {
            Job job = new Job(title, company, skills, requiredExperience);
            jobRepository.save(job);
        }
    }
}
