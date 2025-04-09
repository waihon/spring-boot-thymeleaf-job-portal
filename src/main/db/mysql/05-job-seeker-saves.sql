USE `jobportal`;

CREATE TABLE `job_seeker_saves` (
  `id` int NOT NULL AUTO_INCREMENT,
  `job_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1vn1w4dxfiavb5q2gu1n0whxo` (`user_id`,`job_id`),
  KEY `FKpb44x040gkdltxqy9m7jmvvf3` (`job_id`),
  CONSTRAINT `FK96dyvgd8hmdohqsfdpvyl89mg` FOREIGN KEY (`user_id`) REFERENCES `job_seeker_profiles` (`user_account_id`),
  CONSTRAINT `FKpb44x040gkdltxqy9m7jmvvf3` FOREIGN KEY (`job_id`) REFERENCES `job_post_activities` (`job_post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
