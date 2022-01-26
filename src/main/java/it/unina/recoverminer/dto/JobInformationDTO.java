package it.unina.recoverminer.dto;

import it.unina.recoverminer.utilities.enums.JobStatusEnum;

public class JobInformationDTO {
    private String slugName;
    private String buildID;
    private String jobId;
    private String commmit;
    private JobInformationDTO previousJobInfo;
    private JobStatusEnum status;

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCommmit() {
        return commmit;
    }

    public void setCommmit(String commmit) {
        this.commmit = commmit;
    }

    public JobInformationDTO getPreviousJobInfo() {
        return previousJobInfo;
    }

    public void setPreviousJobInfo(JobInformationDTO previousJobInfo) {
        this.previousJobInfo = previousJobInfo;
    }

    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }
}
