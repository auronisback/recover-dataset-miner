package it.unina.datasetbuilder.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class BuildDTO {
    private String id;
    private String number;
    @JsonAlias({ "jobIds", "job_ids" })
    private List<String>jobIds;
    private String state;

    public BuildDTO() {}

    public BuildDTO(String id, String number, List<String> jobIds,String state) {
        this.id = id;
        this.number = number;
        this.jobIds = jobIds;
        this.state=state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<String> getJobIds() {
        return jobIds;
    }

    public void setJobIds(List<String> jobIds) {
        this.jobIds = jobIds;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}