package it.unina.datasetbuilder.processor;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.utilities.CSVWriter;
import it.unina.datasetbuilder.utilities.IExecutionReportWriter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MultiThreadProcessor {
    CompletableFuture<Void> processSingleJob(Map<String, String> cloneLinkMap, JobInformationDTO job, IExecutionReportWriter... iExecutionReportWriter);
}
