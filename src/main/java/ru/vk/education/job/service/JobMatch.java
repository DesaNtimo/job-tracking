package ru.vk.education.job.service;

import ru.vk.education.job.domain.Job;

public class JobMatch {
    private final Job job;
    private final int score;

    public JobMatch(Job job, int score) {
        this.job = job;
        this.score = score;
    }

    public Job getJob() {
        return job;
    }

    public int getScore() {
        return score;
    }
}
