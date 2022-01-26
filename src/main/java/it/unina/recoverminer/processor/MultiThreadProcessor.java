package it.unina.recoverminer.processor;

import it.unina.recoverminer.dto.JobInformationDTO;
import it.unina.recoverminer.utilities.IExecutionReportWriter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MultiThreadProcessor {
    CompletableFuture<Void> processSingleJob(Map<String, String> cloneLinkMap, JobInformationDTO job, IExecutionReportWriter... iExecutionReportWriter);
}
