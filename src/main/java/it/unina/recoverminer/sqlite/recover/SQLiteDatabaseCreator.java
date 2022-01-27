package it.unina.recoverminer.sqlite.recover;

import it.unina.recoverminer.config.AppConfig;
import it.unina.recoverminer.sqlite.recover.dto.BuildPairDto;
import it.unina.recoverminer.sqlite.recover.dto.ProjectDto;
import it.unina.recoverminer.sqlite.recover.dto.TestCaseDto;
import it.unina.recoverminer.sqlite.recover.dto.VersionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SQLiteDatabaseCreator {

  @Autowired
  private AppConfig appConfig;

  private Connection connection;

  public void createSchema() throws SQLException {
    Path location = Paths.get(appConfig.getClonebasepath(), "ReCover.db");
    this.connection = DriverManager.getConnection("jdbc:sqlite:" + location);
    Statement st = this.connection.createStatement();
    st.executeUpdate("DROP VIEW IF EXISTS VersionOverwiev;" +
        "DROP TABLE IF EXISTS TestCase;" +
        "DROP TABLE IF EXISTS Version;" +
        "DROP TABLE IF EXISTS BuildPair;" +
        "DROP TABLE IF EXISTS Project;" +
        "CREATE TABLE Project (" +
        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "  name VARCHAR(256)," +
        "  owner VARCHAR(256)," +
        "  source CHAR(3) CHECK(source IN ('IR', 'RTP'))" +
        ");" +
        "CREATE TABLE BuildPair (" +
        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "  buildId INTEGER," +
        "  project INTEGER," +
        "  FOREIGN KEY (project) REFERENCES Project(id)" +
        ");" +
        "CREATE TABLE Version (" +
        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "  pair INTEGER," +
        "  type CHAR(4), " +
        "  sha VARCHAR(40)," +
        "  committed DATETIME," +
        "  locs INTEGER," +
        "  classes INTEGER," +
        "  FOREIGN KEY (pair) REFERENCES BuildPair(id)" +
        ");" +
        "CREATE TABLE TestCase (" +
        "  name VARCHAR(1024)," +
        "  status CHECK(status in ('P', 'F', 'E'))," +
        "  version INTEGER," +
        "  FOREIGN KEY (version) REFERENCES Version(id)" +
        ");" +
        "CREATE VIEW VersionOverwiev AS" +
        "    SELECT P.owner, P.name, P.source, B.buildId, V.sha, V.type, COUNT(T.name) AS tests," +
        "           SUM(CASE WHEN T.status = 'E' THEN 1 ELSE 0 END) AS errors," +
        "           SUM(CASE WHEN T.status = 'F' THEN 1 ELSE 0 END) AS failures" +
        "    FROM Project P, BuildPair B, Version V, TestCase T" +
        "    WHERE B.project = P.id AND V.pair = B.id AND T.version = V.id" +
        "    GROUP BY V.id;"
    );
  }

  public void loadDataFromRecoverRoot() throws SQLException, IOException {
    this.loadDataFromRecoverRoot(Paths.get(this.appConfig.getClonebasepath()), "IR");
  }

  public void loadDataFromRecoverRoot(Path root, String type) throws IOException, SQLException {
    List<ProjectDto> projects = this.walkFolders(root, type);
    for(ProjectDto project : projects) {
      System.out.println(project.getOwner() + "@" + project.getName() + " " + project.getSource());
      this.save(project);
      for(BuildPairDto pair : project.getBuilds()) {
        System.out.println("  Build: " + pair.getBuildId());
        this.save(pair);
        System.out.println("    Prev: " + pair.getLeftCommit().getSha());
        this.save(pair.getLeftCommit());
        System.out.println("    Curr: " + pair.getRightCommit().getSha());
        this.save(pair.getRightCommit());
      }
    }
  }

  public void save(ProjectDto project) throws SQLException {
    if(project.getId() == null)
      this.create(project);
    else this.update(project);
  }

  public void save(BuildPairDto pair) throws SQLException {
    if(pair.getId() == null)
      this.create(pair);
    else this.update(pair);
  }

  public void save(VersionDto version) throws SQLException {
    if(version.getId() == null)
      this.create(version);
    else this.update(version);
  }

  private void create(VersionDto version) throws SQLException {
    PreparedStatement st = this.connection.prepareStatement("INSERT INTO Version" +
        " (pair, sha, type) VALUES (?, ?, ?)");
    st.setLong(1, version.getBuild().getId());
    st.setString(2, version.getSha());
    st.setString(3, version == version.getBuild().getLeftCommit() ? "PREV" : "CURR");
    st.executeUpdate();
    ResultSet rs = this.connection.createStatement().executeQuery("SELECT last_insert_rowid();");
    while(rs.next())
      version.setId(rs.getLong(1));
    if(version.getTestCases().size() > 0) {
      StringBuilder sql = new StringBuilder("INSERT INTO TestCase (name, status, version) VALUES ");
      String prefix = "";
      for (TestCaseDto t : version.getTestCases()) {
        sql.append(prefix);
        prefix = ", ";
        sql.append("(?, ?, ?)");
      }
      sql.append(";");
      st = this.connection.prepareStatement(sql.toString());
      int i = 1;
      for (TestCaseDto t : version.getTestCases()) {
        st.setString(i++, t.getName());
        st.setString(i++, (t.getStatus().equals(TestCaseDto.Status.PASS) ? "P" :
            (t.getStatus().equals(TestCaseDto.Status.ERROR) ? "E" : "F")));
        st.setLong(i++, t.getVersion().getId());
      }
      st.executeUpdate();
    }
  }

  private void update(VersionDto version) {

  }

  private void create(ProjectDto project) throws SQLException {
    PreparedStatement st = this.connection.prepareStatement("INSERT INTO Project " +
        "(name, owner, source) VALUES (?, ?, ?);");
    st.setString(1, project.getName());
    st.setString(2, project.getOwner());
    st.setString(3, project.getSource());
    st.executeUpdate();
    ResultSet rs = this.connection.createStatement().executeQuery("SELECT last_insert_rowid();");
    while(rs.next())
      project.setId(rs.getLong(1));
  }

  private void update(ProjectDto project) {

  }

  private void create(BuildPairDto pair) throws SQLException {
    PreparedStatement st = this.connection.prepareStatement("INSERT INTO BuildPair " +
        "(buildId, project) VALUES (?, ?);");
    st.setLong(1, pair.getBuildId());
    st.setLong(2, pair.getProject().getId());
    st.executeUpdate();
    ResultSet rs = this.connection.createStatement().executeQuery("SELECT last_insert_rowid();");
    while(rs.next())
      pair.setId(rs.getLong(1));
  }

  private void update(BuildPairDto pair) {

  }

  private List<ProjectDto> walkFolders(Path folder, String type) throws IOException {
    List<ProjectDto> projects = new ArrayList<>();
    List<Path> projectPaths = Files.walk(folder, 1)
        .filter(f -> !f.equals(folder))
        .filter(Files::isDirectory)
        .collect(Collectors.toList());
    for(Path p : projectPaths) {
      ProjectDto project = this.createProject(p, type);
      if(project != null)
        projects.add(project);
    }
    return projects;
  }

  private ProjectDto createProject(Path projectFolder, String type) throws IOException {
    Path successPath = Paths.get(projectFolder.toString(), "SUCCESS");
    if(!Files.exists(successPath))
      return null;
    ProjectDto project = new ProjectDto();
    this.setProjectNameAndOwnerByPath(projectFolder, project);
    project.setSource(type);
    this.walkProjectFolder(successPath, project);
    return project;
  }

  private void setProjectNameAndOwnerByPath(Path p, ProjectDto project) {
    String[] tokens = p.getFileName().toString().split("@");
    if(tokens.length > 1) {
      project.setOwner(tokens[0]);
      project.setName(tokens[1]);
    } else project.setName(tokens[0]);
  }

  private void walkProjectFolder(Path projectFolder, ProjectDto project)
      throws IOException {
    List<Path> buildFolders = Files.walk(projectFolder, 1)
        .filter(f -> !projectFolder.equals(f))
        .filter(Files::isDirectory)
        .collect(Collectors.toList());
    for(Path p : buildFolders) {
      project.addBuild(this.createBuildPair(p));
    }
  }

  private BuildPairDto createBuildPair(Path buildFolder) throws IOException {
    BuildPairDto pair = new BuildPairDto();
    pair.setBuildId(Long.parseLong(buildFolder.getFileName().toString()));
    List<Path> versions = Files.walk(buildFolder, 1)
        .filter(Files::isDirectory)
        .filter(v -> !v.equals(buildFolder))
        .collect(Collectors.toList());
    for(Path v : versions) {
      VersionDto versionDto = this.loadVersionInformation(v);
      if(this.isLeftSha(v))
        pair.setLeftCommit(versionDto);
      else pair.setRightCommit(versionDto);
    }
    return pair;
  }

  private VersionDto loadVersionInformation(Path versionFolder) throws IOException {
    VersionDto version = new VersionDto();
    version.setSha(versionFolder.getFileName().toString());
    Path surefireFolder = Paths.get(versionFolder.toString(), "surefire-reports");
    if(Files.isDirectory(surefireFolder)) {
      Collection<TestCaseDto> tests = new SurefireReportsParser().loadSuiteFromReportFolder(surefireFolder);
      for (TestCaseDto test : tests)
        version.addTestCase(test);
    } else
      System.out.println("Surefire reports not found: " + surefireFolder);
    return version;
  }

  private boolean isLeftSha(Path commitFolder) {
    String commitSha = commitFolder.getFileName().toString();
    Path prev = Paths.get(commitFolder.getParent().toString(), commitSha + "_previous.txt");
    return Files.exists(prev);
  }

  public void close() throws SQLException {
    this.connection.close();
  }
}
