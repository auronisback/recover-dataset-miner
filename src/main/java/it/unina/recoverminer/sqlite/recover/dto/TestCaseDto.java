package it.unina.recoverminer.sqlite.recover.dto;

public class TestCaseDto {

  private String name;

  private Status status;

  private VersionDto version;

  public enum Status {
    PASS, FAIL, ERROR;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public VersionDto getVersion() {
    return version;
  }

  public void setVersion(VersionDto version) {
    this.version = version;
  }
}
