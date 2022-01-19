package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class ReaderStrategyExecutor {

    private Map<String, DatasetReaderStrategy> strategies;

    public ReaderStrategyExecutor(List<DatasetReaderStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(
                        toMap(k -> k.getClass().getDeclaredAnnotation(Qualifier.class).value(),
                                Function.identity()));
    }

    public List<JobInformationDTO> executeStrategy(String name, String path) throws Exception {
        if (!strategies.containsKey(name)) {
            throw new IllegalArgumentException("The strategy " + name + " does not exist.");
        }
        return strategies.get(name).read(path);
    }
}
