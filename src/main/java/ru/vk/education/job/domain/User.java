package ru.vk.education.job.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

public class User {
    @Getter
    private final String name;
    private final Set<String> skills;
    @Getter
    private final int experience;

    // Изменили "username" на "name", чтобы точно совпадало с ключом в JSON
    public User(
            @JsonProperty("name") String name,
            @JsonProperty("skills") Set<String> skills,
            @JsonProperty("experience") int experience) {
        this.name = name;
        this.skills = skills != null ? new HashSet<>(skills) : new HashSet<>();
        this.experience = experience;
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public String getSkillsString() {
        return String.join(",", getSkills());
    }

    @Override
    public String toString() {
        return getName() + " " + getSkillsString() + " " + getExperience();
    }
}