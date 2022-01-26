package it.unina.recoverminer.processor;

import it.unina.recoverminer.dto.JobInformationDTO;

import java.util.List;

public interface IJobsProcessor {
    boolean process(List<JobInformationDTO> jobs);
}
