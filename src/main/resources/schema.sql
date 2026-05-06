CREATE TABLE IF NOT EXISTS users
(
    username   VARCHAR(255) PRIMARY KEY,
    experience INT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_skills
(
    username VARCHAR(255) REFERENCES users (username) ON DELETE CASCADE,
    skill    VARCHAR(255) NOT NULL,
    PRIMARY KEY (username, skill)
);

CREATE TABLE IF NOT EXISTS jobs
(
    title               VARCHAR(255) PRIMARY KEY,
    company             VARCHAR(255) NOT NULL,
    required_experience INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS job_skills
(
    job_title VARCHAR(255) REFERENCES jobs (title) ON DELETE CASCADE,
    skill     VARCHAR(255) NOT NULL,
    PRIMARY KEY (job_title, skill)
);