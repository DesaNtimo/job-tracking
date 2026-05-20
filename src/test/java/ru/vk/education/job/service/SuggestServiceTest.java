package ru.vk.education.job.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.education.job.domain.Job;
import ru.vk.education.job.domain.User;
import ru.vk.education.job.repository.JobRepository;
import ru.vk.education.job.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class SuggestServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private SuggestService suggestService;

    @Test
    void suggestTest() {
        User user = new User("david", Set.of("java", "spring"), 3);
        Job job1 = new Job("Java Backend", "VK", Set.of("java", "spring"), 2);
        Job job2 = new Job("Frontend", "Yandex", Set.of("react"), 2);
        Job job3 = new Job("Junior Java", "Sber", Set.of("java"), 1);

        Mockito.when(userRepository.findByUsername("david")).thenReturn(user);
        Mockito.when(jobRepository.findAll()).thenReturn(List.of(job1, job2, job3));

        List<Job> result = suggestService.suggest("david");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Java Backend", result.get(0).getTitle());
    }

    @Test
    void emptyVacanciesTest() {
        User user = new User("david", Set.of("java"), 3);

        Mockito.when(userRepository.findByUsername("david")).thenReturn(user);
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        List<Job> result = suggestService.suggest("david");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void singleVacancyTest() {
        User user = new User("david", Set.of("java"), 3);
        Job job = new Job("Java Backend", "VK", Set.of("java"), 2);

        Mockito.when(userRepository.findByUsername("david")).thenReturn(user);
        Mockito.when(jobRepository.findAll()).thenReturn(List.of(job));

        List<Job> result = suggestService.suggest("david");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Java Backend", result.get(0).getTitle());
    }

    @Test
    void userNotFoundTest() {
        Mockito.when(userRepository.findByUsername("unknown")).thenReturn(null);

        List<Job> result = suggestService.suggest("unknown");

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}