package it.unina.datasetbuilder.processor;

import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.utilities.ExecutionReportHTMLWriter;
import it.unina.datasetbuilder.utilities.IExecutionReportWriter;
import it.unina.datasetbuilder.utilities.enums.JobStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReportToHTMLTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReportToHTMLTransformer.class);

    private static final Function<String, JobInformationDTO> mapToItem = (line) -> {

        String[] p = line.split(";");// a CSV has comma separated lines

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
        item.setStatus(JobStatusEnum.valueOf(p[7]));
        return item;
    };


    public static void main(String[] args) throws Exception {
        if(args!=null && args[0]!=null && args[1]!=null){
            String csvPath=args[0];
            List<JobInformationDTO> jobInformationDTOS = processInputFile(csvPath);
            //C:/Users/carmi/Desktop/projectinfoExcelFormattedCSV.csv
            IExecutionReportWriter iExecutionReportWriter= new ExecutionReportHTMLWriter(args[1]);
            for (JobInformationDTO job:jobInformationDTOS) {
                LOGGER.info("Writing job: {}",job.getJobId());
                iExecutionReportWriter.writeRecord(job);
            }
            iExecutionReportWriter.closeStream();
            LOGGER.info("End processing: {}",jobInformationDTOS.size());

        }else{
            LOGGER.error("NO BASE PATH IN INPUT");
        }
    }

    private static List<JobInformationDTO> processInputFile(String inputFilePath) {

        List<JobInformationDTO> inputList = new ArrayList<JobInformationDTO>();

        try{

            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }

        return inputList ;
    }
}
