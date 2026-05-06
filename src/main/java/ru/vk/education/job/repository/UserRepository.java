package ru.vk.education.job.repository;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vk.education.job.domain.User;

import java.util.*;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void save(User user) {
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = :username";
        Integer count = jdbcTemplate.queryForObject(checkSql, Map.of("username", user.getName()), Integer.class);

        if (count != null && count == 0) {
            String insertUserSql = "INSERT INTO users (username, experience) VALUES (:username, :experience)";
            jdbcTemplate.update(insertUserSql, Map.of(
                    "username", user.getName(),
                    "experience", user.getExperience()
            ));

            if (!user.getSkills().isEmpty()) {
                String insertSkillSql = "INSERT INTO user_skills (username, skill) VALUES (:username, :skill)";
                MapSqlParameterSource[] skillParams = user.getSkills().stream()
                        .map(skill -> new MapSqlParameterSource()
                                .addValue("username", user.getName())
                                .addValue("skill", skill))
                        .toArray(MapSqlParameterSource[]::new);
                jdbcTemplate.batchUpdate(insertSkillSql, skillParams);
            }
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT u.username, u.experience, s.skill " +
                "FROM users u " +
                "LEFT JOIN user_skills s ON u.username = s.username " +
                "WHERE u.username = :username";

        return Objects.requireNonNull(jdbcTemplate.query(sql, Map.of("username", username), userResultSetExtractor()))
                .stream()
                .findFirst()
                .orElse(null);
    }

    public Collection<User> findAll() {
        String sql = "SELECT u.username, u.experience, s.skill " +
                "FROM users u " +
                "LEFT JOIN user_skills s ON u.username = s.username";

        return jdbcTemplate.query(sql, userResultSetExtractor());
    }

    private ResultSetExtractor<List<User>> userResultSetExtractor() {
        return rs -> {
            Map<String, Integer> expMap = new LinkedHashMap<>();
            Map<String, Set<String>> skillsMap = new HashMap<>();

            while (rs.next()) {
                String username = rs.getString("username");
                int experience = rs.getInt("experience");
                String skill = rs.getString("skill");

                expMap.putIfAbsent(username, experience);
                skillsMap.putIfAbsent(username, new HashSet<>());

                if (skill != null) {
                    skillsMap.get(username).add(skill);
                }
            }

            List<User> users = new ArrayList<>();
            for (String username : expMap.keySet()) {
                users.add(new User(username, skillsMap.get(username), expMap.get(username)));
            }
            return users;
        };
    }
}