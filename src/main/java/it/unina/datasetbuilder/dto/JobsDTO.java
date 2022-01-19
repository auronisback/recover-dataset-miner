package it.unina.datasetbuilder.dto;

public class JobsDTO {
    private JobDTO job;
    private CommitDTO commit;

    public JobsDTO() {
    }

    public JobsDTO(JobDTO job, CommitDTO commit) {
        this.job = job;
        this.commit = commit;
    }

    public JobDTO getJob() {
        return job;
    }

    public void setJob(JobDTO job) {
        this.job = job;
    }

    public CommitDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitDTO commit) {
        this.commit = commit;
    }
}
