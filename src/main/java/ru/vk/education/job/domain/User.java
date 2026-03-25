package ru.vk.education.job.domain;

import java.util.*;

public class User {
    private final String name;
    private final Set<String> skills;
    private final byte experience;

    public User(String name, Set<String> skills, byte experience) {
        this.name = name;
        this.skills = new HashSet<>(skills);
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public String getSkillsString() {
        return String.join(",", getSkills());
    }

    public byte getExperience() {
        return experience;
    }

    @Override
    public String toString() {
        return getName() + " " + getSkillsString() + " " + getExperience();
    }
}
