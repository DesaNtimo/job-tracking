package ru.vk.education.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(userRepository.findAll());
    }

    public User getUser(String name) {
        return userRepository.findByUsername(name);
    }

    public void processUserCommand(String line) {
        String[] tokens = line.split("\\s+");
        String username = tokens[1];
        Set<String> userSkills = new HashSet<>();
        int userExperience = 0;

        for (int i = 2; i < tokens.length; i++) {
            if (tokens[i].startsWith("--skills=")) {
                String skillPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                String[] stringSkills = skillPart.split(",");
                Collections.addAll(userSkills, stringSkills);
            } else if (tokens[i].startsWith("--exp=")) {
                String expPart = tokens[i].substring(tokens[i].indexOf('=') + 1);
                userExperience = Integer.parseInt(expPart);
            }
        }

        if (!userSkills.isEmpty() && userExperience >= 0) {
            User user = new User(username, userSkills, userExperience);
            addUser(user);
        }
    }
}
