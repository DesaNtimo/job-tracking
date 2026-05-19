package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.StatService;

import java.util.List;

@RestController
@RequestMapping("/stat")
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping("/top-skills/{n}")
    public List<String> getTopSkills(@PathVariable int n) {
        return statService.getTopSkills(n);
    }

    @GetMapping("/jobs-by-experience/{experience}")
    public List<Job> getJobsByExperience(@PathVariable int experience) {
        return statService.getJobsByExperience(experience);
    }

    @GetMapping("/users-by-matches/{matches}")
    public List<User> getUsersByMatches(@PathVariable int matches) {
        return statService.getUsersByMatches(matches);
    }
}
