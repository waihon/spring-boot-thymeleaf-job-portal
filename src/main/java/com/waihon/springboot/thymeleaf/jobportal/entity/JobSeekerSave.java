package com.waihon.springboot.thymeleaf.jobportal.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "job_seeker_saves", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user", "job"})
})
public class JobSeekerSave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_account_id")
    private JobSeekerProfile user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id", referencedColumnName = "job_post_id")
    private JobPostActivity job;

    public JobSeekerSave() {
    }

    public JobSeekerSave(Integer id, JobSeekerProfile user, JobPostActivity job) {
        this.id = id;
        this.user = user;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JobSeekerProfile getUser() {
        return user;
    }

    public void setUser(JobSeekerProfile user) {
        this.user = user;
    }

    public JobPostActivity getJob() {
        return job;
    }

    public void setJob(JobPostActivity job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "JobSeekerSave{" +
                "id=" + id +
                ", user=" + user.toString() +
                ", job=" + job.toString() +
                '}';
    }

}
