package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.config.AppConfig;
import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.utilities.ExecutionReportCSVWriter;
import it.unina.datasetbuilder.utilities.enums.JobStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier("IR")
public class IRReaderStrategy implements DatasetReaderStrategy {
    @Autowired
    private AppConfig appConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(IRReaderStrategy.class);

    private  final Function<String, JobInformationDTO> mapToItem = (line) -> {

        String[] p = line.split(",");// a CSV has comma separated lines

        JobInformationDTO item = new JobInformationDTO();
        JobInformationDTO previousJobInfo = new JobInformationDTO();
        item.setPreviousJobInfo(previousJobInfo);

        item.setSlugName(p[0]);
        previousJobInfo.setSlugName(p[0]);
        item.setBuildID(p[1]);
        item.setJobId(p[2]);
        previousJobInfo.setBuildID(p[3]);
        previousJobInfo.setJobId(p[4]);
        item.setCommmit(p[5]);
        previousJobInfo.setCommmit(p[6]);

        return item;
    };

    @Override
    public List<JobInformationDTO> read(String path) throws Exception {
        LOGGER.info("IR Strategy executing");
        List<JobInformationDTO> jobInformationDTOS = processInputFile(path);
        LOGGER.info("IR Strategy executed");
        return jobInformationDTOS;
    }

    private List<JobInformationDTO> processInputFile(String inputFilePath) {

        List<JobInformationDTO> inputList = new ArrayList<JobInformationDTO>();
        List<JobInformationDTO> filteredList = new ArrayList<JobInformationDTO>();
        try{

            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            System.out.println("size " + inputList.size());
            filteredList = deleteDuplicatedCommit(inputList);
            br.close();
        } catch (IOException e) {
            LOGGER.error("Error in read csv file");
        }

        return filteredList ;
    }

    private List<JobInformationDTO> deleteDuplicatedCommit(List<JobInformationDTO> inputList) {
        ExecutionReportCSVWriter processedJobsCsvWriter = new ExecutionReportCSVWriter(appConfig.getClonebasepath() + "processedJobs.csv");
        ExecutionReportCSVWriter removedJobsCsvWriter = new ExecutionReportCSVWriter(appConfig.getClonebasepath() + "removedJobs.csv");

        List<JobInformationDTO> filteredJobList = new LinkedList<>();
        boolean found;

        for(JobInformationDTO job1 : inputList){
            found = false;
            for(JobInformationDTO job2 : filteredJobList){
                if(job1.getSlugName().equals(job2.getSlugName())){
                    if(job1.getCommmit().equals(job2.getCommmit()) &&
                            job1.getPreviousJobInfo().getCommmit().equals(job2.getPreviousJobInfo().getCommmit())){

                        if(job1.getStatus() == null)
                            job1.setStatus(JobStatusEnum.SKIPPED);
                        removedJobsCsvWriter.writeRecord(job1);
                        found = true;
                        break;
                    }
                }
            }
            if(found == false){
                if(job1.getStatus() == null)
                    job1.setStatus(JobStatusEnum.PROCESSED);
                processedJobsCsvWriter.writeRecord(job1);
                filteredJobList.add(job1);
            }
        }
        return filteredJobList;
    }

    /*public void deleteDuplicatedCommit(String inputFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
        String line;
        List<String> lines = br.lines().skip(1).collect(Collectors.toList());
        for(String s : lines)
            System.out.println(s);

    }*/
}
