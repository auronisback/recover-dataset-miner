package it.unina.recoverminer.sqlite.recover.dto;

public class BuildPairDto {

  private Long id;

  private Long buildId;

  private ProjectDto project;

  private VersionDto leftCommit;

  private VersionDto rightCommit;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBuildId() {
    return buildId;
  }

  public void setBuildId(Long buildId) {
    this.buildId = buildId;
  }

  public ProjectDto getProject() {
    return project;
  }

  public void setProject(ProjectDto project) {
    this.project = project;
  }

  public VersionDto getLeftCommit() {
    return leftCommit;
  }

  public void setLeftCommit(VersionDto leftCommit) {
    this.leftCommit = leftCommit;
    leftCommit.setBuild(this);
  }

  public VersionDto getRightCommit() {
    return rightCommit;
  }

  public void setRightCommit(VersionDto rightCommit) {
    this.rightCommit = rightCommit;
    rightCommit.setBuild(this);
  }
}
