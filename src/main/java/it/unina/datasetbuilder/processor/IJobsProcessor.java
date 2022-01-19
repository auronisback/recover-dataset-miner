package it.unina.datasetbuilder.processor;

import it.unina.datasetbuilder.dto.JobInformationDTO;

import java.util.List;

public interface IJobsProcessor {
    boolean process(List<JobInformationDTO> jobs);
}
