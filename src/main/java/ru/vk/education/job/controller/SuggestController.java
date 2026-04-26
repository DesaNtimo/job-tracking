package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/suggest")
public class SuggestController {
    private final JobService jobService;

    @Autowired
    public SuggestController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/{username}")
    public List<Job> suggestJobs(@PathVariable String username) {
        return jobService.suggestJobs(username);
    }
}
