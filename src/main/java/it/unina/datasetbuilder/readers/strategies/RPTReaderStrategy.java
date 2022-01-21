package it.unina.datasetbuilder.readers.strategies;

import it.unina.datasetbuilder.dto.BuildDTO;
import it.unina.datasetbuilder.dto.BuildsDTO;
import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.dto.JobsDTO;
import it.unina.datasetbuilder.integration.TraviCIApiInvoker;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Qualifier("RTP")
public class RPTReaderStrategy implements DatasetReaderStrategy {

    @Autowired
    private TraviCIApiInvoker traviCIApiInvoker;

    private static final Logger LOGGER = LoggerFactory.getLogger(RPTReaderStrategy.class);

    private  final Function<Pair<String,String>, JobInformationDTO> mapToItem = (line) -> {

        String[] p = line.getLeft().split(",");// a CSV has comma separated lines

        JobInformationDTO item = new JobInformationDTO();
        JobInformationDTO previousJobInfo = new JobInformationDTO();
        item.setJobId(p[0]);
        item.setSlugName(line.getRight());
        //item.setCommmit(p[1]);

        return item;
    };


    @Override
    public List<JobInformationDTO> read(String path) throws Exception {
        LOGGER.info("RTP Strategy executing");
        //List<JobInformationDTO> jobInformationDTOS = processInputFile(path);
        List<JobInformationDTO> andProcessOffendersCSV = findAndProcessOffendersCSV(path);
        andProcessOffendersCSV.forEach(this::decorateInfos);
        LOGGER.info("RTP Strategy executed");
        return andProcessOffendersCSV;
    }

    private void decorateInfos(JobInformationDTO job){
        try {
            JobsDTO jobResponse = traviCIApiInvoker.getJob(job.getJobId());
            String projectName = job.getSlugName();
            BuildsDTO buildsById = traviCIApiInvoker.getBuildsById(projectName, jobResponse.getJob().getBuildID());
            if(buildsById.getBuilds().size() == 0) {
                LOGGER.warn("No builds obtained for " + job.getSlugName() + " " + job.getJobId());
                return;
            }
            BuildsDTO buildsAfterNumber = traviCIApiInvoker.getBuildsAfterNumber(projectName, buildsById.getBuilds()
                .get(0).getNumber());
            BuildDTO prevBuildDTO = buildsAfterNumber.getBuilds().stream().filter(build -> "passed".equals(build.getState())).findFirst().orElse(null);
            if(prevBuildDTO == null) {
                LOGGER.warn("Previous build not found for " + job.getSlugName() + " " + job.getJobId());
                return;
            }
            JobsDTO prevJob = traviCIApiInvoker.getJob(prevBuildDTO.getJobIds().get(0));
            job.setCommmit(jobResponse.getCommit().getSha());
            job.setBuildID(jobResponse.getJob().getBuildID());
            JobInformationDTO previousJobInfo = new JobInformationDTO();
            previousJobInfo.setJobId(prevJob.getJob().getId());
            previousJobInfo.setBuildID(prevJob.getJob().getBuildID());
            previousJobInfo.setCommmit(prevJob.getCommit().getSha());
            job.setPreviousJobInfo(previousJobInfo);
            job.setSlugName(job.getSlugName());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOGGER.warn(job.getSlugName() + "/" + job.getJobId() + " in error: " + ex.getMessage());
        }
    }

    private List<JobInformationDTO> findAndProcessOffendersCSV(String basePath){
        List<JobInformationDTO> jobInformationDTOS= new ArrayList<>();
        try (Stream<Path> stream = Files.find(Paths.get(basePath),2,
                (path, attr) -> path.getFileName().toString().contains("offenders.csv") )) {
            stream.forEach(filePath-> {
                String filePathString = filePath.toString();
                LOGGER.info(filePathString);
                List<JobInformationDTO> currentJobInformationDTOS = processInputFile(filePathString);
                jobInformationDTOS.addAll(currentJobInformationDTOS);
            });
        } catch (IOException e) {
            LOGGER.error("Error in findCsv from: {}, file to search:offenders.csv",basePath);
        }
        return jobInformationDTOS;
    }

    private List<JobInformationDTO> processInputFile(String inputFilePath) {

        List<JobInformationDTO> inputList = new ArrayList<JobInformationDTO>();

        try{
            String project = inputFilePath.substring(inputFilePath.lastIndexOf("\\")+1,inputFilePath.lastIndexOf("-"));
            String projectReplaced = project.replace("@", "/");
            LOGGER.info("Project Name:{}",projectReplaced);
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).
                    map(line-> Pair.of(line,projectReplaced))
                    .map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            LOGGER.error("Error in read csv file");
        }

        return inputList ;
    }
}
