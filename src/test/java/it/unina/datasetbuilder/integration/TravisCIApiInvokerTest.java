package it.unina.datasetbuilder.integration;

import it.unina.datasetbuilder.dto.BuildsDTO;
import it.unina.datasetbuilder.dto.JobsDTO;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

@Ignore
public class TravisCIApiInvokerTest {
    @Test
    public void testOrchestration(){
        TraviCIApiInvoker traviCIApiInvoker=new TraviCIApiInvoker();
        ReflectionTestUtils.setField(traviCIApiInvoker,"travisCIKey","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");
        JobsDTO job = traviCIApiInvoker.getJob("10775402");
        BuildsDTO buildsById = traviCIApiInvoker.getBuildsById("adamFisk/LittleProxy", job.getJob().getBuildID());
        BuildsDTO buildsAfterNumber = traviCIApiInvoker.getBuildsAfterNumber("adamFisk/LittleProxy", buildsById.getBuilds()
                .get(0).getNumber());
        Assert.assertTrue(buildsAfterNumber.getBuilds().stream().anyMatch(b->b.getJobIds().stream().anyMatch("10766890"::equals)));
    }
}
