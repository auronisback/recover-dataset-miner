package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RPTReaderStrategyTest {

    @Test
    public void testReader() throws Exception {
        DatasetReaderStrategy datasetReaderStrategy = new RPTReaderStrategy();
        List<JobInformationDTO> jobInformationDTOS = datasetReaderStrategy.read("C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/RptTorrent/tr_all_built_commits.csv");
        Assert.assertNotNull(jobInformationDTOS);
    }
}
