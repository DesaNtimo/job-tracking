package ru.vk.education.job.repository;

import org.springframework.stereotype.Repository;
import ru.vk.education.job.domain.User;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class UserRepository {
    private final Map<String, User> users = new TreeMap<>();

    public void save(User user) {
        users.putIfAbsent(user.getName(), user);
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public Collection<User> findAll() {
        return users.values();
    }
}
