package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.dto.JobInformationDTO;

import java.util.List;

public interface DatasetReaderStrategy {

    List<JobInformationDTO> read(String path) throws Exception;
}
