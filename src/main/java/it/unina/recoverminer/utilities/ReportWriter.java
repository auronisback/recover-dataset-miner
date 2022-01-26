package it.unina.recoverminer.utilities;

import it.unina.recoverminer.config.AppConfig;
import it.unina.recoverminer.dto.JobInformationDTO;
import it.unina.recoverminer.utilities.enums.JobStatusEnum;
import it.unina.recoverminer.utilities.enums.ProcessResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ReportWriter {

    @Autowired
    private AppConfig appConfig;

    public void writeReports(List<JobInformationDTO> jobs){
        IExecutionReportWriter csvReportWriter= new ExecutionReportCSVWriter(appConfig.getClonebasepath() + "report.csv");
        IExecutionReportWriter htmlReportWriter= new ExecutionReportHTMLWriter(appConfig.getClonebasepath() + "reportHTML.html");

        for(JobInformationDTO job : jobs){
            setJobStatusByFolder(job);

            csvReportWriter.writeRecord(job);
            htmlReportWriter.writeRecord(job);

        }
    }


    private void setJobStatusByFolder(JobInformationDTO job){
        String slugName = job.getSlugName().replace("/", "@");

        Path successPath = Paths.get(appConfig.getClonebasepath() + job.getSlugName()+"/"+ ProcessResultEnum.SUCCESS+"/"+job.getJobId()+"/"+"done.txt");
        Path failurePath = Paths.get(appConfig.getClonebasepath() + job.getSlugName()+"/"+ProcessResultEnum.FAILURE+"/"+job.getJobId()+"/"+"done.txt");
        Path errorPath = Paths.get(appConfig.getClonebasepath() + job.getSlugName()+"/"+ProcessResultEnum.ERROR+"/"+job.getJobId()+"/"+"done.txt");

        if(Files.exists(successPath)){
            job.setStatus(JobStatusEnum.SUCCESS);
        }
        else if(Files.exists(failurePath)){
            job.setStatus(JobStatusEnum.FAILURE);
        }
        else if(Files.exists(errorPath)){
            if(Files.exists(Paths.get(errorPath.getParent().toString() + "/" + job.getCommmit() + "/" + "RefNotFound.txt"))
                || Files.exists(Paths.get(errorPath.getParent().toString() + "/" + job.getPreviousJobInfo().getCommmit() + "/" + "RefNotFound.txt"))){
                job.setStatus(JobStatusEnum.REF_NOT_FOUND);
            }
            else
                job.setStatus(JobStatusEnum.ERROR);
        }
    }


}
