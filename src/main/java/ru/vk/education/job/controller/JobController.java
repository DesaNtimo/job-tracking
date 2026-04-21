package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.JobService;

import java.util.Collection;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public void createJob(@RequestBody Job job) {
        jobService.addJob(job);
    }

    @GetMapping
    public Collection<Job> getJobs() {
        return jobService.getJobs();
    }
}
