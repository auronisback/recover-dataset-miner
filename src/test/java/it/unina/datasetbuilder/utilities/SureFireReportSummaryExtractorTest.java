package it.unina.datasetbuilder.utilities;

import org.junit.Ignore;
import org.junit.Test;
@Ignore
public class SureFireReportSummaryExtractorTest {
    @Test
    public void extractTest(){
        SureFireReportSummaryExtractor sureFireReportSummaryExtractor=
                new SureFireReportSummaryExtractorJaxbImpl("src/test/resources/surefire-reports");
        sureFireReportSummaryExtractor.extractSummaryReport();

    }
}
