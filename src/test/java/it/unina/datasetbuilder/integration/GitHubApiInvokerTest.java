package it.unina.datasetbuilder.integration;

import it.unina.datasetbuilder.dto.RepositoryDTO;
import it.unina.datasetbuilder.utilities.exceptions.GitHubRateLimitException;
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
