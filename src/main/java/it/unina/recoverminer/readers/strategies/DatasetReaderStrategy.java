package it.unina.recoverminer.readers.strategies;

import it.unina.recoverminer.dto.JobInformationDTO;

import java.util.List;

public interface DatasetReaderStrategy {

    List<JobInformationDTO> read(String path) throws Exception;
}
