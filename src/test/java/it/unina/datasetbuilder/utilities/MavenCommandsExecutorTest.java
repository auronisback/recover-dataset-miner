package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.utilities.enums.MavenBuildResultEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class MavenCommandsExecutorTest {

    @Test
    public void executeCommandTest(){
        MavenCommandsExecutor mavenCommandsExecutor =  new MavenCommandsExecutor("C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/ManualOpenClover/la4j/c8e61571c878b5957832a35d4a11d1e042d7aeed",
                "$MAVEN_HOME");
        //mvn clean clover:setup test clover:aggregate clover:clover
        MavenBuildResultEnum result = mavenCommandsExecutor.executeCommands(new String[]{"clean","install"});
        Assert.assertEquals(MavenBuildResultEnum.SUCCESS,result);
    }

    @Test
    public void executeCloverCommandTest(){
        MavenCommandsExecutor mavenCommandsExecutor =  new MavenCommandsExecutor("C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/ManualOpenClover/la4j/c8e61571c878b5957832a35d4a11d1e042d7aeed",
                "$MAVEN_HOME");
        //mvn clean clover:setup test clover:aggregate clover:clover
        MavenBuildResultEnum result = mavenCommandsExecutor.executeCommands(new String[]{"clean","clover:setup",
                "test","clover:aggregate","clover:clover"});
        Assert.assertEquals(MavenBuildResultEnum.SUCCESS,result);
    }

    @Test
    public void executeSkipTestCommandTest(){
        MavenCommandsExecutor mavenCommandsExecutor =  new MavenCommandsExecutor("C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/ManualOpenClover/la4j/c8e61571c878b5957832a35d4a11d1e042d7aeed",
                "$MAVEN_HOME");
        //N.B la fase di compile, viene prima della fase di test, ergo, non serve skipTest
        MavenBuildResultEnum result = mavenCommandsExecutor.executeCommands(new String[]{"clean","install","-DskipTests"});
        Assert.assertEquals(MavenBuildResultEnum.SUCCESS,result);
    }
}
