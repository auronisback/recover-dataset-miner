package it.unina.datasetbuilder.utilities;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class MavenPomEditor {

    private Model pomModel;
    private String pomPath;

    public MavenPomEditor(String pomPath) throws IOException, XmlPullParserException {
        MavenXpp3Reader mavenXpp3Reader =  new MavenXpp3Reader();
        this.pomPath=pomPath;
        this.pomModel = mavenXpp3Reader.read(new FileReader(pomPath));
    }
    public void addPlugin(String groupId,String artifactId){
        Build build = Optional.ofNullable(pomModel.getBuild()).orElse(new Build());
        boolean alreadyExist = build.getPlugins().stream().anyMatch(dependency -> groupId.equals(dependency.getGroupId()) &&
                artifactId.equals(dependency.getArtifactId()));
        if(!alreadyExist){
            Plugin plugin = new Plugin();
            plugin.setGroupId(groupId);
            plugin.setArtifactId(artifactId);
            if(pomModel.getBuild()==null)
                pomModel.setBuild(build);
            pomModel.getBuild().addPlugin(plugin);
        }
    }
    public void addDependency(String groupId,String artifactID,String scope,String version,String type){
        boolean alreadyExist = pomModel.getDependencies().stream().anyMatch(dependency -> groupId.equals(dependency.getGroupId()) &&
                artifactID.equals(dependency.getArtifactId()));
        if(!alreadyExist){
            Dependency dep = new Dependency();
            dep.setGroupId(groupId);
            dep.setArtifactId(artifactID);
            if(scope!=null && !scope.isEmpty())
                dep.setScope(scope);
            dep.setVersion(version);
            if(type!=null && !type.isEmpty())
                dep.setType(type);
            pomModel.addDependency(dep);
        }
    }
    public void removeDependency(String groupId,String artifactID){
        pomModel.getDependencies().stream().filter(dependency -> groupId.equals(dependency.getGroupId()) &&
                artifactID.equals(dependency.getArtifactId())).findFirst().ifPresent(toRemove -> pomModel.removeDependency(toRemove));
    }
    public void removePlugin(String artifactID){
        Optional.ofNullable(pomModel.getBuild()).orElse(new Build()).getPlugins().stream().filter(dependency ->
                artifactID.equals(dependency.getArtifactId())).findFirst().ifPresent(toRemove -> pomModel.getBuild().removePlugin(toRemove));
    }

    public void flushPom() throws IOException {
        MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
        FileOutputStream fileOutputStream =  new FileOutputStream(pomPath);
        mavenXpp3Writer.write(fileOutputStream,pomModel);
        fileOutputStream.close();
    }

    public Model getPomModel() {
        return pomModel;
    }

}
