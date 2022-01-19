package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.dto.JobInformationDTO;

public interface IExecutionReportWriter {

    void writeRecord(JobInformationDTO jobInformationDTO);
    void closeStream();

}
