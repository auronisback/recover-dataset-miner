package it.unina.recoverminer.dto;

import java.util.List;

public class BuildsDTO {
    private List<BuildDTO> builds;

    public BuildsDTO() {

    }

    public BuildsDTO(List<BuildDTO> builds) {
        this.builds = builds;
    }

    public List<BuildDTO> getBuilds() {
        return builds;
    }

    public void setBuilds(List<BuildDTO> builds) {
        this.builds = builds;
    }
}
