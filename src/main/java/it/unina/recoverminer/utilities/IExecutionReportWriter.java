package it.unina.recoverminer.utilities;

import it.unina.recoverminer.dto.JobInformationDTO;

public interface IExecutionReportWriter {

    void writeRecord(JobInformationDTO jobInformationDTO);
    void closeStream();

}
