package ru.vk.education.job.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.List;
import java.util.Set;

@SpringBootTest
@Testcontainers
class SuggestServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void testSuggestWithDatabase() {
        User user1 = new User("alice", Set.of("java", "sql"), 3);
        User user2 = new User("bob", Set.of("python", "django"), 5);
        userRepository.save(user1);
        userRepository.save(user2);

        Job job1 = new Job("Java Backend", "VK", Set.of("java", "spring"), 2);
        Job job2 = new Job("Data Engineer", "Yandex", Set.of("python", "sql"), 3);
        jobRepository.save(job1);
        jobRepository.save(job2);

        List<Job> aliceJobs = suggestService.suggest("alice");
        List<Job> bobJobs = suggestService.suggest("bob");

        Assertions.assertNotNull(aliceJobs);
        Assertions.assertFalse(aliceJobs.isEmpty());
        Assertions.assertTrue(aliceJobs
                .stream()
                .anyMatch(j -> j.getTitle().equals("Java Backend") || j.getTitle().equals("Data Engineer")));

        Assertions.assertNotNull(bobJobs);
        Assertions.assertFalse(bobJobs.isEmpty());
        Assertions.assertTrue(bobJobs.stream()
                .anyMatch(j -> j.getTitle().equals("Data Engineer")));
    }
}