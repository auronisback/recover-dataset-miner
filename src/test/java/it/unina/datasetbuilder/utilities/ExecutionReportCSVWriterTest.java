package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.utilities.enums.JobStatusEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
@Ignore
public class ExecutionReportCSVWriterTest {
    @Test
    public void testWriter(){
        IExecutionReportWriter iExecutionReportWriter= new ExecutionReportCSVWriter("src/test/resources/report.csv");
        List<JobInformationDTO> jobList=new ArrayList<>();
        JobInformationDTO job1 = getJobInformationDTO("hs-web/hsweb-framework",JobStatusEnum.SUCCESS);
        JobInformationDTO job2 = getJobInformationDTO("apache/servicecomb-pack",JobStatusEnum.ERROR);
        JobInformationDTO job3 = getJobInformationDTO("zhang-rf/mybatis-boost",JobStatusEnum.CURR_NO_COMPILE);
        jobList.add(job1);
        jobList.add(job2);
        jobList.add(job3);
        for (JobInformationDTO job:jobList) {
            iExecutionReportWriter.writeRecord(job);
        }
        Assert.assertTrue(true);
    }

    private JobInformationDTO getJobInformationDTO(String project, JobStatusEnum status) {
        JobInformationDTO job1= new JobInformationDTO();
        job1.setSlugName(project);
        job1.setBuildID("build1");
        job1.setJobId("job1");
        JobInformationDTO previousJobInfo = new JobInformationDTO();
        job1.setPreviousJobInfo(previousJobInfo);
        previousJobInfo.setBuildID("prevBuild");
        previousJobInfo.setJobId("prevJob");
        job1.setCommmit("commit");
        previousJobInfo.setCommmit("prevCommit");
        job1.setStatus(status);
        return job1;
    }
}
