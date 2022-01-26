package it.unina.recoverminer.utilities;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
@Ignore
public class MavenPomEditorTest {

    @Test
    public void constructorTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        Model pomModel = mavenPomEditor.getPomModel();
        Assert.assertEquals("it.unina.utilities",pomModel.getGroupId());
    }

    @Test
    public void addDependencyTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        mavenPomEditor.addDependency("org.openclover","clover-maven-plugin",null,"4.4.1","maven-plugin");
        Model pomModel = mavenPomEditor.getPomModel();
        Assert.assertNotNull(pomModel);
        Assert.assertTrue(pomModel.getDependencies().stream().anyMatch(dep->"clover-maven-plugin".equals(dep.getArtifactId())));
    }
    @Test
    public void addPluginTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        mavenPomEditor.addPlugin("org.openclover","clover-maven-plugin");
        Model pomModel = mavenPomEditor.getPomModel();
        Assert.assertNotNull(pomModel);
        Assert.assertTrue(pomModel.getBuild().getPlugins().stream().anyMatch(dep->"clover-maven-plugin".equals(dep.getArtifactId())));
        mavenPomEditor.flushPom();
    }

    @Test
    public void removeDependencyTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        mavenPomEditor.removeDependency("org.openclover","clover-maven-plugin");
        Model pomModel = mavenPomEditor.getPomModel();
        Assert.assertNotNull(pomModel);
        Assert.assertFalse(pomModel.getDependencies().stream().anyMatch(dep->"clover-maven-plugin".equals(dep.getArtifactId())));
        mavenPomEditor.flushPom();
    }

    @Test
    public void removePluginTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        mavenPomEditor.removePlugin("clover-maven-plugin");
        Model pomModel = mavenPomEditor.getPomModel();
        Assert.assertNotNull(pomModel);
        Assert.assertFalse(pomModel.getBuild().getPlugins().stream().anyMatch(dep->"clover-maven-plugin".equals(dep.getArtifactId())));
        mavenPomEditor.flushPom();
    }
    @Test
    public void flushTest() throws IOException, XmlPullParserException {
        MavenPomEditor mavenPomEditor = new MavenPomEditor("src/test/resources/pomtest.xml");
        mavenPomEditor.addDependency("org.openclover","clover-maven-plugin",null,"4.4.1","maven-plugin");
        mavenPomEditor.flushPom();
        Assert.assertTrue(true);
    }
}
