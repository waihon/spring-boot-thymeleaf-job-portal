USE `jobportal`;

CREATE TABLE `job_seeker_saves` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_seeker_saves_user_id_job_id` (`user_id`,`job_id`),
  KEY `fk_job_seeker_saves_job_id` (`job_id`),
  CONSTRAINT `fk_job_seeker_saves_user_id` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profiles` (`user_account_id`),
  CONSTRAINT `fk_job_seeker_saves_job_id` FOREIGN KEY (`job_id`) REFERENCES `job_post_activities` (`job_post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
