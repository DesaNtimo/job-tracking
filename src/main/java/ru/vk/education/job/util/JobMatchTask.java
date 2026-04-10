package ru.vk.education.job.util;

import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.JobMatch;
import ru.vk.education.job.service.JobService;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class JobMatchTask implements Runnable {
    private final JobService jobService;

    public JobMatchTask(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
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
