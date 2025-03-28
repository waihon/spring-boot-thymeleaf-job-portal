USE `jobportal`;

CREATE TABLE `job_companies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `job_locations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `job_post_activities` (
  `job_post_id` int NOT NULL AUTO_INCREMENT,
  `description_of_job` varchar(10000) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `job_type` varchar(255) DEFAULT NULL,
  `posted_date` datetime(6) DEFAULT NULL,
  `remote` varchar(255) DEFAULT NULL,
  `salary` varchar(255) DEFAULT NULL,
  `job_company_id` int DEFAULT NULL,
  `job_location_id` int DEFAULT NULL,
  `posted_by_id` int DEFAULT NULL,
  PRIMARY KEY (`job_post_id`),
  KEY `fk_job_post_activities_job_company_id` (`job_company_id`),
  KEY `fk_job_post_activities_job_location_id` (`job_location_id`),
  KEY `fk_job_post_activities_posted_by_id` (`posted_by_id`),
  CONSTRAINT `fk_job_post_activities_job_company_id` FOREIGN KEY (`job_company_id`) REFERENCES `job_companies` (`id`),
  CONSTRAINT `fk_job_post_activities_job_location_id` FOREIGN KEY (`job_location_id`) REFERENCES `job_locations` (`id`),
  CONSTRAINT `fk_job_post_activities_posted_by_id` FOREIGN KEY (`posted_by_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
