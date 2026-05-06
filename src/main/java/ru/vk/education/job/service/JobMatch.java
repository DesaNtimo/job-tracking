package ru.vk.education.job.service;

import lombok.Getter;
import ru.vk.education.job.domain.Job;

@Getter
public class JobMatch {
    private final Job job;
    private final double score;

    public JobMatch(Job job, double score) {
        this.job = job;
        this.score = score;
    }
}
