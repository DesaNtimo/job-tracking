package ru.vk.education.job.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Job {
    private final String title;
    private final String company;
    private final Set<String> skills;
    private final int requiredExperience;

    public Job(String title, String company, Set<String> skills, int requiredExperience) {
        this.title = title;
        this.company = company;
        this.skills = new HashSet<>(skills);
        this.requiredExperience = requiredExperience;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public int getRequiredExperience() {
        return requiredExperience;
    }

    @Override
    public String toString() {
        return getTitle() + " at " + getCompany();
    }
}
