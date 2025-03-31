SELECT COUNT(s.user_id) AS totalCandidates, j.job_post_id AS jobPostId,
    j.job_title AS jobTitle, l.id AS locationId, l.city, l.state, l.country,
    c.id AS companyId, c.name
FROM job_post_activities j
INNER JOIN job_locations l
ON j.job_location_id = l.id
INNER JOIN job_companies c
ON j.job_company_id = c.id
LEFT JOIN job_seeker_applies s
ON s.job_id = j.job_post_id
WHERE j.posted_by_id = 9
GROUP BY j.job_post_id;
