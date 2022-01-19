package it.unina.datasetbuilder.processor;

import it.unina.datasetbuilder.config.AppConfig;
import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.utilities.ExecutionReportCSVWriter;
import it.unina.datasetbuilder.utilities.ExecutionReportHTMLWriter;
import it.unina.datasetbuilder.utilities.IExecutionReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobsProcessor implements IJobsProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsProcessor.class);
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MultiThreadProcessor multiThreadProcessor;
    @Override
    public boolean process(List<JobInformationDTO> jobs) {
        long start = System.currentTimeMillis();
        LOGGER.info("START PROCESSING {} elements",jobs!=null && jobs.size()>0 ? jobs.size():0);
        Map<String,String> cloneLinkMap;
        if (jobs!=null && jobs.size()>0){
            cloneLinkMap=new ConcurrentHashMap<>();
            CompletableFuture<?>[] completableFutures=new CompletableFuture[jobs.size()];
            IExecutionReportWriter csvReportWriter= new ExecutionReportCSVWriter(appConfig.getClonebasepath() + "report.csv");
            IExecutionReportWriter htmlReportWriter= new ExecutionReportHTMLWriter(appConfig.getClonebasepath() + "reportHTML.html");
            for (int i=0;i<jobs.size();i++){
                CompletableFuture<Void> voidCompletableFuture = multiThreadProcessor.processSingleJob(cloneLinkMap, jobs.get(i),csvReportWriter,htmlReportWriter);
                completableFutures[i]=voidCompletableFuture;
            }
            CompletableFuture.allOf(completableFutures).join();
            csvReportWriter.closeStream();
            htmlReportWriter.closeStream();
        }

        long end = System.currentTimeMillis();
        LOGGER.info("END PROCESSING, elapsedTime: {} (s)" , (end - start) / 1000F);
        LOGGER.info("# TOTAL JOBS:{}",jobs!=null?jobs.size():0);
        return true;
    }
}
