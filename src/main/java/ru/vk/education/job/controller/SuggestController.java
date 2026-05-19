package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.service.SuggestService;

import java.util.List;

@RestController
@RequestMapping("/suggest")
public class SuggestController {
    private final SuggestService suggestService;

    @Autowired
    public SuggestController(SuggestService suggestService) {
        this.suggestService = suggestService;
    }

    @GetMapping("/{username}")
    public List<Job> suggestJobs(@PathVariable String username) {
        return suggestService.suggest(username);
    }
}
