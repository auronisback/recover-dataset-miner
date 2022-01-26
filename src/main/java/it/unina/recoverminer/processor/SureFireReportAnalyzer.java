package it.unina.recoverminer.processor;

import it.unina.recoverminer.utilities.SureFireReportSummaryExtractor;
import it.unina.recoverminer.utilities.SureFireReportSummaryExtractorJaxbImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SureFireReportAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SureFireReportAnalyzer.class);

    //C:/Users/carmi/Documents/UNI/TIROCINIO/WorkSpaces/clonedRepo2
    public static void main(String[] args){
        LOGGER.info("RUNNING SUREFIRE-REPORTS ANALYZER");
        if(args!=null && args[0]!=null){
            String basePath=args[0];
            LOGGER.info("BASE PATH:{}",basePath);
            try {
                findAndProcessSureFireReports(basePath);
            } catch (Exception e) {
                LOGGER.error("ERROR DURING WALKING DIR TREE");
            }
            LOGGER.info("SUREFIRE-REPORTS ANALYZER FINISHED");

        }else{
            LOGGER.error("NO BASE PATH IN INPUT");
        }
    }

    private static void findAndProcessSureFireReports(String basePath){

        try (Stream<Path> stream = Files.find(Paths.get(basePath),5,
                (path, attr) -> path.getFileName().toString().equals("surefire-reports") )) {
            stream.forEach(filePath-> {
                String filePathString = filePath.toString();
                if(filePathString.contains("SUCCESS")){
                    LOGGER.info(filePathString);
                    SureFireReportSummaryExtractor sureFireReportSummaryExtractor=new SureFireReportSummaryExtractorJaxbImpl(filePathString);
                    sureFireReportSummaryExtractor.extractSummaryReport();
                }
            });
        } catch (IOException e) {
            LOGGER.error("Error in findSureFireReports from: {}, file to search:surefire-reports",basePath);
        }
    }

}

