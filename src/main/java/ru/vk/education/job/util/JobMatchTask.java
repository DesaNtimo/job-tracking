package ru.vk.education.job.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.JobMatch;
import ru.vk.education.job.service.JobService;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

@Component
public class JobMatchTask implements Runnable {
    private final JobService jobService;

    @Autowired
    public JobMatchTask(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void run() {
        Collection<User> users = jobService.getUsers();
        for (User user : users) {
            Optional<JobMatch> bestMatch = jobService
                    .getJobs()
                    .stream()
                    .map(job -> {
                        int score = 0;
                        for (String skill : user.getSkills()) {
                            if (job.getSkills().contains(skill)) {
                                score++;
                            }
                        }
                        if (score > 0 && user.getExperience() < job.getRequiredExperience()) score /= 2;
                        return new JobMatch(job, score);
                    })
                    .filter(jobMatch -> jobMatch.getScore() > 0)
                    .max(Comparator.comparingInt(JobMatch::getScore));

            bestMatch.ifPresent(jobMatch ->
                    System.out.println(user.getName() +
                            ", лучшее предложение — " +
                            jobMatch.getJob().getTitle() +
                            " at " +
                            jobMatch.getJob().getCompany()));
        }
    }
}
