package ru.vk.education.job.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.Job;

import java.util.*;

@Repository
public class JobRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JobRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void save(Job job) {
        String checkSql = "SELECT COUNT(*) FROM jobs WHERE title = :title";
        Integer count = jdbcTemplate.queryForObject(checkSql, Map.of("title", job.getTitle()), Integer.class);

        if (count != null && count == 0) {
            String insertJobSql = "INSERT INTO jobs (title, company, required_experience) VALUES (:title, :company, :required_experience)";
            jdbcTemplate.update(insertJobSql, Map.of(
                    "title", job.getTitle(),
                    "company", job.getCompany(),
                    "required_experience", job.getRequiredExperience()
            ));

            if (!job.getSkills().isEmpty()) {
                String insertSkillSql = "INSERT INTO job_skills (job_title, skill) VALUES (:title, :skill)";
                MapSqlParameterSource[] skillParams = job.getSkills().stream()
                        .map(skill -> new MapSqlParameterSource()
                                .addValue("title", job.getTitle())
                                .addValue("skill", skill))
                        .toArray(MapSqlParameterSource[]::new);
                jdbcTemplate.batchUpdate(insertSkillSql, skillParams);
            }
        }
    }

    public Collection<Job> findAll() {
        String sql = "SELECT j.title, j.company, j.required_experience, s.skill " +
                "FROM jobs j " +
                "LEFT JOIN job_skills s ON j.title = s.job_title";

        return jdbcTemplate.query(sql, jobResultSetExtractor());
    }

    private ResultSetExtractor<List<Job>> jobResultSetExtractor() {
        return rs -> {
            Map<String, String> companyMap = new LinkedHashMap<>();
            Map<String, Integer> expMap = new HashMap<>();
            Map<String, Set<String>> skillsMap = new HashMap<>();

            while (rs.next()) {
                String title = rs.getString("title");
                String company = rs.getString("company");
                int requiredExperience = rs.getInt("required_experience");
                String skill = rs.getString("skill");

                companyMap.putIfAbsent(title, company);
                expMap.putIfAbsent(title, requiredExperience);
                skillsMap.putIfAbsent(title, new HashSet<>());

                if (skill != null) {
                    skillsMap.get(title).add(skill);
                }
            }

            List<Job> jobs = new ArrayList<>();
            for (String title : companyMap.keySet()) {
                jobs.add(new Job(title, companyMap.get(title), skillsMap.get(title), expMap.get(title)));
            }
            return jobs;
        };
    }
}