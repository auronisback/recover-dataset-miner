package it.unina.recoverminer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class JobDTO {
    private String id;
    @JsonAlias({ "buildID", "build_id" })
    private String buildID;
    private String state;
    public JobDTO() {
    }

    public JobDTO(String id, String buildID, String state) {
        this.id = id;
        this.buildID = buildID;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
