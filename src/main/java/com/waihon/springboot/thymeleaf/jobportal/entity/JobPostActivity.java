package com.waihon.springboot.thymeleaf.jobportal.entity;

import com.waihon.springboot.thymeleaf.jobportal.validation.AdvancedCheck;
import com.waihon.springboot.thymeleaf.jobportal.validation.BasicCheck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_post_activities")
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_post_id")
    private Integer jobPostId;

    @ManyToOne
    @JoinColumn(name = "posted_by_id", referencedColumnName = "user_id")
    private User postedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_location_id", referencedColumnName = "id")
    private JobLocation jobLocation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_company_id", referencedColumnName = "id")
    private JobCompany jobCompany;

    @Transient
    private Boolean active;

    @Transient
    private Boolean saved;

    @NotBlank(message = "Job Description is required.", groups = BasicCheck.class)
    @Size(min = 300, message = "Job Description is too short (minimum 300 characters).", groups = AdvancedCheck.class)
    @Size(max = 10_000, message = "Job Description is too long (maximum 10,000 characters.", groups = AdvancedCheck.class)
    private String descriptionOfJob;

    @NotBlank(message = "Employment Type is required.", groups = BasicCheck.class)
    private String jobType;

    private String salary;

    @NotBlank(message = "Work Model is required.", groups = BasicCheck.class)
    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date postedDate;

    @NotBlank(message = "Job Title is required.", groups = BasicCheck.class)
    @Size(min = 15, message = "Job Title is too short (minimum 15 characters)", groups = AdvancedCheck.class)
    private String jobTitle;

    public JobPostActivity() {
    }

    public JobPostActivity(Integer jobPostId, User postedBy, JobLocation jobLocation, JobCompany jobCompany,
                           Boolean active, Boolean saved, String descriptionOfJob, String jobType,
                           String salary, String remote, Date postedDate, String jobTitle) {
        this.jobPostId = jobPostId;
        this.postedBy = postedBy;
        this.jobLocation = jobLocation;
        this.jobCompany = jobCompany;
        this.active = active;
        this.saved = saved;
        this.descriptionOfJob = descriptionOfJob;
        this.jobType = jobType;
        this.salary = salary;
        this.remote = remote;
        this.postedDate = postedDate;
        this.jobTitle = jobTitle;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public JobLocation getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(JobLocation jobLocation) {
        this.jobLocation = jobLocation;
    }

    public JobCompany getJobCompany() {
        return jobCompany;
    }

    public void setJobCompany(JobCompany jobCompany) {
        this.jobCompany = jobCompany;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "jobPostId=" + jobPostId +
                ", postedBy=" + postedBy +
                ", jobLocation=" + jobLocation +
                ", jobCompany=" + jobCompany +
                ", active=" + active +
                ", saved=" + saved +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", jobType='" + jobType + '\'' +
                ", salary='" + salary + '\'' +
                ", remote='" + remote + '\'' +
                ", postedDate=" + postedDate +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
