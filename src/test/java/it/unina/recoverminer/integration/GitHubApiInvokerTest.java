package it.unina.recoverminer.integration;

import it.unina.recoverminer.dto.RepositoryDTO;
import it.unina.recoverminer.utilities.exceptions.GitHubRateLimitException;
import org.junit.Assert;
import org.junit.Test;

public class GitHubApiInvokerTest {

    @Test
    public void getRepoBySlugNameTest() throws GitHubRateLimitException {
        GitHubApiInvoker gitHubApiInvoker = new GitHubApiInvoker();
        RepositoryDTO repo = gitHubApiInvoker.getRepoBySlugName("apache/incubator-dubbo");
        Assert.assertNotNull(repo);
    }
}
