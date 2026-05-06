package ru.vk.education.job.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Job {
    @Getter
    private final String title;
    @Getter
    private final String company;
    private final Set<String> skills;
    @Getter
    private final int requiredExperience;

    public Job(
            @JsonProperty("title") String title,
            @JsonProperty("company") String company,
            @JsonProperty("tags") Set<String> skills,
            @JsonProperty("experience") int requiredExperience) {
        this.title = title;
        this.company = company;
        this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
        this.requiredExperience = requiredExperience;
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    @Override
    public String toString() {
        return getTitle() + " at " + getCompany();
    }
}