package it.unina.recoverminer;

import it.unina.recoverminer.dto.JobInformationDTO;
import it.unina.recoverminer.processor.IJobsProcessor;
import it.unina.recoverminer.readers.strategies.ReaderStrategyExecutor;
import it.unina.recoverminer.utilities.ReportWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class DatasetBuilderApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatasetBuilderApplication.class);

	@Autowired
	ReaderStrategyExecutor readerStrategyExecutor;
	@Autowired
	IJobsProcessor jobsProcessor;
	@Autowired
	ReportWriter reportWriter;

	public static void main(String[] args) {
		SpringApplication.run(DatasetBuilderApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {
		if(args!=null && args.length==2) {
			String irSourcePath = args[0];
			String rtpSourcePath = args[1];
			List<JobInformationDTO> jobs = new ArrayList<>();
			LOGGER.info("Reading IR dataset jobs...");
			List<JobInformationDTO> irJobs = readerStrategyExecutor.executeStrategy("IR", irSourcePath);
			LOGGER.info("Reading RTPTorrent jobs...");
			List<JobInformationDTO> rtpJobs = readerStrategyExecutor.executeStrategy("RTP", rtpSourcePath);
			LOGGER.info("Merging jobs...");
			jobs.addAll(irJobs);
			jobs.addAll(rtpJobs);
			jobsProcessor.process(jobs);
			reportWriter.writeReports(jobs);

		}else{
			throw new IllegalArgumentException("Missing required arguments: <ir_projectinfo_path> <rtp_package_folder>");
		}
	}
}
