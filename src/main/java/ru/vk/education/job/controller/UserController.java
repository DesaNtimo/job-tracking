package ru.vk.education.job.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.service.JobService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final JobService jobService;

    @Autowired
    public UserController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        jobService.addUser(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return jobService.getUser(username);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return jobService.getUsers();
    }
}
