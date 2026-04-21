package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/stat")
public class StatController {
    private final JobService jobService;

    @Autowired
    public StatController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/top-skills/{n}")
    public List<String> getTopSkills(@PathVariable int n) {
        return jobService.getTopSkills(n);
    }

    @GetMapping("/jobs-by-experience/{experience}")
    public List<Job> getJobsByExperience(@PathVariable int experience) {
        return jobService.getJobsByExperience(experience);
    }

    @GetMapping("/users-by-matches/{matches}")
    public List<User> getUsersByMatches(@PathVariable int matches) {
        return jobService.getUsersByMatches(matches);
    }
}
