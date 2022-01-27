package it.unina.recoverminer.sqlite.recover.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectDto {

  private Long id;

  private String owner;

  private String name;

  private String source;

  private final Map<String, VersionDto> commits = new HashMap<>();

  private final Map<Long, BuildPairDto> builds = new HashMap<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<VersionDto> getCommits() {
    return commits.values();
  }

  public void addCommit(VersionDto commit) {
    if(commit != null && !this.commits.containsKey(commit.getSha()))
      this.commits.put(commit.getSha(), commit);
  }

  public Collection<BuildPairDto> getBuilds() {
    return builds.values();
  }

  public void addBuild(BuildPairDto build) {
    if(build != null && !this.builds.containsKey(build.getBuildId())) {
      this.builds.put(build.getBuildId(), build);
      build.setProject(this);
    }
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }
}
