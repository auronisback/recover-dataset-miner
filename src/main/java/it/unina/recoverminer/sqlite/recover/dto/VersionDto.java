package it.unina.recoverminer.sqlite.recover.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VersionDto {

  private Long id;

  private String sha;

  private Date date;

  private BuildPairDto build;

  private final List<TestCaseDto> testCases = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSha() {
    return sha;
  }

  public void setSha(String sha) {
    this.sha = sha;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public BuildPairDto getBuild() {
    return build;
  }

  public void setBuild(BuildPairDto build) {
    this.build = build;
  }

  public List<TestCaseDto> getTestCases() {
    return testCases;
  }

  public void addTestCase(TestCaseDto testCase) {
    this.testCases.add(testCase);
    testCase.setVersion(this);
  }
}
