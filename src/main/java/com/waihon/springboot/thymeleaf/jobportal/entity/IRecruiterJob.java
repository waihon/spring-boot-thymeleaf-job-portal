package com.waihon.springboot.thymeleaf.jobportal.entity;

public interface IRecruiterJob {

    Long getTotalCandidates();

    int getJobPostId();

    String getJobTitle();

    int getLocationId();

    String getCity();

    String getState();

    String getCountry();

    int getCompanyId();

    String getName();

}
