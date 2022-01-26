package it.unina.recoverminer.utilities;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

@Ignore
public class GitUtilityTest {

    @Test
    public void gitCloneTest() throws GitAPIException, TimeoutException {
        GitUtility.cloneRepo("https://github.com/apache/dubbo-spring-boot-project.git",
                "C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/GitUtility/test",null,5);
        Assert.assertTrue(true);
    }
    @Test
    public void gitCloneAtSpecificCommitTest() throws GitAPIException, TimeoutException {
        Git git = GitUtility.cloneRepo("https://github.com/apache/dubbo.git",
                "C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/GitUtility/testSha", "a8af5ce028e7",5);
        Assert.assertNotNull(git);
    }

    @Test
    public void gitCheckoutAtSpecificCommitTest() throws GitAPIException, TimeoutException {
        Git git = GitUtility.cloneRepo("https://github.com/apache/dubbo.git",
                "C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/GitUtility/testCheckoutSha",null,5);
        Assert.assertNotNull(git);
        Git a8af5ce028e7 = GitUtility.checkoutAtSpecificVersion(git, "a8af5ce028e7");
        Assert.assertNotNull(a8af5ce028e7);
    }

}
