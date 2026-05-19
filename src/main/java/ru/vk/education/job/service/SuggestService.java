package ru.vk.education.job.service;

import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.*;

@Service
public class SuggestService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public SuggestService(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }

    public Collection<Job> getJobs() {
        return Collections.unmodifiableCollection(jobRepository.findAll());
    }

    public List<Job> suggest(String userName) {
        User user = getUser(userName);
        if (user == null) {
            return Collections.emptyList();
        }

        List<JobMatch> matches = new ArrayList<>();
        for (Job job : getJobs()) {
            double score = 0;
            for (String skill : user.getSkills()) {
                if (job.getSkills().contains(skill)) {
                    score++;
                }
            }

            if (score == 0) continue;
            if (user.getExperience() < job.getRequiredExperience()) score /= 2;
            matches.add(new JobMatch(job, score));
        }

        matches.sort(Comparator.comparingDouble(JobMatch::getScore).reversed());
        int limit = Math.min(2, matches.size());
        return matches.stream()
                .limit(limit)
                .map(JobMatch::getJob)
                .toList();
    }
}