package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.processor.JobsProcessor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class IRReaderStrategyTest {
    /*@InjectMocks
    IJobsProcessor jobsProcessor;*/
    @Test
    public void parsingTest() throws Exception {
        IRReaderStrategy IRReaderStrategy = new IRReaderStrategy();
        List<JobInformationDTO> jobInformationDTOS =
                IRReaderStrategy.read("src/test/resources/projectinfoTest.csv");
        Assert.assertNotNull(jobInformationDTOS);
        Assert.assertEquals(2,jobInformationDTOS.size());
        JobInformationDTO firstElement = jobInformationDTOS.get(0);
        Assert.assertEquals("apache/incubator-dubbo",firstElement.getSlugName());
        Assert.assertEquals("478229404",firstElement.getBuildID());
        Assert.assertEquals("478229405",firstElement.getJobId());
        Assert.assertEquals("0721544fed8f",firstElement.getCommmit());

        Assert.assertEquals("apache/incubator-dubbo",firstElement.getPreviousJobInfo().getSlugName());
        Assert.assertEquals("478226842",firstElement.getPreviousJobInfo().getBuildID());
        Assert.assertEquals("478226844",firstElement.getPreviousJobInfo().getJobId());
        Assert.assertEquals("a8af5ce028e7",firstElement.getPreviousJobInfo().getCommmit());

        JobInformationDTO secondElement = jobInformationDTOS.get(1);
        Assert.assertEquals("zhang-rf/mybatis-boost",secondElement.getSlugName());
        Assert.assertEquals("397707235",secondElement.getBuildID());
        Assert.assertEquals("397707236",secondElement.getJobId());
        Assert.assertEquals("7987ae02fec2",secondElement.getCommmit());

        Assert.assertEquals("zhang-rf/mybatis-boost",secondElement.getPreviousJobInfo().getSlugName());
        Assert.assertEquals("397699450",secondElement.getPreviousJobInfo().getBuildID());
        Assert.assertEquals("397699451",secondElement.getPreviousJobInfo().getJobId());
        Assert.assertEquals("490f36b6605a",secondElement.getPreviousJobInfo().getCommmit());

    }
    @Test
    public void processTest() throws Exception {
        JobsProcessor jobsProcessor = new JobsProcessor();
        IRReaderStrategy IRReaderStrategy = new IRReaderStrategy();
        List<JobInformationDTO> jobInformationDTOS =
                IRReaderStrategy.read("src/test/resources/projectinfoTest.csv");
        jobsProcessor.process(jobInformationDTOS);
    }
}
