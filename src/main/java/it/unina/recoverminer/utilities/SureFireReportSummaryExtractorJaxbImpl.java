package it.unina.recoverminer.utilities;

import it.unina.recoverminer.dto.Testsuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SureFireReportSummaryExtractorJaxbImpl implements  SureFireReportSummaryExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SureFireReportSummaryExtractorJaxbImpl.class);
    private static final String SURE_FIRE_SUMMARY_FILE = "sureFireSummary.txt";
    private String sureFireReportBasePath;
    private BufferedWriter fw;

    @Override
    public void extractSummaryReport() {
        try (Stream<Path> stream = Files.find(Paths.get(sureFireReportBasePath),3,
                (path, attr) -> path.getFileName().toString().startsWith("TEST-") )) {
            stream.forEach(filePath-> {
                File file = filePath.toFile();
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Testsuite.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    Testsuite testsuite = (Testsuite) jaxbUnmarshaller.unmarshal(file);
                    for (Testsuite.Testcase testcase: testsuite.getTestcase()) {
                        if(testcase.getError()!=null || testcase.getFailure()!=null)
                            try {
                                //NomePackage.package1.package2Classe#nomemetodo()
                                fw.write(testcase.getClassname()+"#"+testcase.getName());
                                fw.newLine();
                            } catch (IOException e) {
                                LOGGER.error("Error when writing on sureFireSummaryReport");
                            }
                    }
                    fw.flush();
                } catch (JAXBException | IOException e) {
                    LOGGER.error("Error in extractSummaryReport");
                }
            });
        } catch (IOException e) {
            LOGGER.error("Error in extractSummaryReport",e);
        }

    }

    public SureFireReportSummaryExtractorJaxbImpl(String sureFireReportBasePath) {
        this.sureFireReportBasePath = sureFireReportBasePath;
        FilesUtility.createFile(this.sureFireReportBasePath +"/"+ SURE_FIRE_SUMMARY_FILE);
        try {
            fw=new BufferedWriter(new FileWriter(this.sureFireReportBasePath +"/"+ SURE_FIRE_SUMMARY_FILE,true));
        } catch (IOException e) {
            LOGGER.error("Error in SureFireReportSummaryExtractorJaxbImpl constructor");
        }
    }
}
