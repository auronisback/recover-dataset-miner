package it.unina.datasetbuilder;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.processor.IJobsProcessor;
import it.unina.datasetbuilder.readers.strategies.ReaderStrategyExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class DatasetBuilderApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatasetBuilderApplication.class);

	@Autowired
	ReaderStrategyExecutor readerStrategyExecutor;
	@Autowired
	IJobsProcessor jobsProcessor;

	public static void main(String[] args) {
		SpringApplication.run(DatasetBuilderApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {
		if(args!=null && args.length==2) {
			String readerStrategy = args[0];
			String sourcePath= args[1];
			LOGGER.info("Start Execution with strategy: {}", readerStrategy);
			List<JobInformationDTO> jobInformationDTOS = readerStrategyExecutor.executeStrategy(readerStrategy,sourcePath);
			jobsProcessor.process(jobInformationDTOS);
		}else{
			throw new IllegalArgumentException("Please insert dataset type and source path to processing");
		}
	}
}
