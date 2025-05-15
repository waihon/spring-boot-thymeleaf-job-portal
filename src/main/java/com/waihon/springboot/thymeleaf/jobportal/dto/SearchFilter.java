package com.waihon.springboot.thymeleaf.jobportal.dto;

public class SearchFilter {
    private String partTime;
    private String fullTime;
    private String freelance;

    private String remoteOnly;
    private String officeOnly;
    private String partialRemote;

    private boolean today;
    private boolean days7;
    private boolean days30;

    private String job;
    private String location;

    public SearchFilter() {
    }

    public SearchFilter(String partTime, String fullTime, String freelance,
                        String remoteOnly, String officeOnly, String partialRemote,
                        boolean today, boolean days7, boolean days30,
                        String job, String location) {
        this.partTime = partTime;
        this.fullTime = fullTime;
        this.freelance = freelance;

        this.remoteOnly = remoteOnly;
        this.officeOnly = officeOnly;
        this.partialRemote = partialRemote;

        this.today = today;
        this.days7 = days7;
        this.days30 = days30;

        this.job = job;
        this.location = location;
    }

    public String getPartTime() {
        return partTime;
    }

    public void setPartTime(String partTime) {
        this.partTime = partTime;
    }

    public String getFullTime() {
        return fullTime;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }

    public String getFreelance() {
        return freelance;
    }

    public void setFreelance(String freelance) {
        this.freelance = freelance;
    }

    public String getRemoteOnly() {
        return remoteOnly;
    }

    public void setRemoteOnly(String remoteOnly) {
        this.remoteOnly = remoteOnly;
    }

    public String getOfficeOnly() {
        return officeOnly;
    }

    public void setOfficeOnly(String officeOnly) {
        this.officeOnly = officeOnly;
    }

    public String getPartialRemote() {
        return partialRemote;
    }

    public void setPartialRemote(String partialRemote) {
        this.partialRemote = partialRemote;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isDays7() {
        return days7;
    }

    public void setDays7(boolean days7) {
        this.days7 = days7;
    }

    public boolean isDays30() {
        return days30;
    }

    public void setDays30(boolean days30) {
        this.days30 = days30;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
