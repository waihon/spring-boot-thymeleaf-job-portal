USE `jobportal`;

CREATE TABLE `job_seeker_applies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apply_date` datetime(6) DEFAULT NULL,
  `cover_letter` varchar(255) DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8v6qok40anljlhpkc486nsdmu` (`user_id`,`job_id`),
  KEY `FKmfhx9q4uclbb74vm49lv9dmf4` (`job_id`),
  CONSTRAINT `FKmfhx9q4uclbb74vm49lv9dmf4` FOREIGN KEY (`job_id`) REFERENCES `job_post_activities` (`job_post_id`),
  CONSTRAINT `FKs9fftlyxws2ak05q053vi57qv` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profiles` (`user_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
