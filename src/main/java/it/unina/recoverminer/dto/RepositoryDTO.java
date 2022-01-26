package it.unina.recoverminer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class RepositoryDTO {
    private String name;
    @JsonAlias({ "fullName", "full_name" })
    private String fullName;
    @JsonAlias({ "cloneUrl", "clone_url" })
    private String cloneUrl;

    public RepositoryDTO() {
    }

    public RepositoryDTO(String name, String fullName, String cloneUrl) {
        this.name = name;
        this.fullName = fullName;
        this.cloneUrl = cloneUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }
}
