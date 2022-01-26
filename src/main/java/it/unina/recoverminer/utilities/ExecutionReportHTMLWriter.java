package it.unina.recoverminer.utilities;

import com.hp.gagawa.java.elements.Head;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import it.unina.recoverminer.constants.HTMLConstants;
import it.unina.recoverminer.dto.JobInformationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ExecutionReportHTMLWriter implements IExecutionReportWriter{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionReportHTMLWriter.class);
    private String htmlFilePath;

    public ExecutionReportHTMLWriter(String path) {
        this.htmlFilePath = path;
        Head head=new Head();
        String headString = HTMLConstants.HTML_START_TAG + head.appendText(HTMLConstants.HEAD_CONTENT).write() + HTMLConstants.HTML_BODY_OPEN;
        if (Files.notExists(Paths.get(htmlFilePath))) {
            try {
                FilesUtility.createFile(htmlFilePath);
                Files.write(
                        Paths.get(htmlFilePath),
                        headString.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND);
            } catch (IOException ex) {
                LOGGER.error("Error in file {} creation",htmlFilePath);
            }
        }
        else {
            try {
                new PrintWriter(htmlFilePath).close();
                Files.write(
                        Paths.get(htmlFilePath),
                        headString.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.APPEND);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writeRecord(JobInformationDTO job) {
        JobInformationDTO prevJob = job.getPreviousJobInfo();
        String projectName = job.getSlugName();
        String buildID = job.getBuildID();
        String jobId = job.getJobId();
        String prevJobBuildID = prevJob.getBuildID();
        String prevJobJobId = prevJob.getJobId();
        String commit = job.getCommmit();
        String prevJobCommit = prevJob.getCommmit();
        String jobStatus = job.getStatus().name();
        Tr row= new Tr();
        Td td1=new Td();
        td1.appendText(projectName);
        Td td2=new Td();
        td2.appendText(buildID);
        Td td3=new Td();
        td3.appendText(jobId);
        Td td4=new Td();
        td4.appendText(prevJobBuildID);
        Td td5=new Td();
        td5.appendText(prevJobJobId);
        Td td6=new Td();
        td6.appendText(commit);
        Td td7=new Td();
        td7.appendText(prevJobCommit);
        Td td8=new Td();
        td8.appendText(jobStatus);
        row.appendChild(td1,td2,td3,td4,td5,td6,td7,td8);
        try {
            Files.write(
                    Paths.get(htmlFilePath),
                    row.write().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.error("Error in html write, writing job:{}",job.getJobId());
        }
    }

    @Override
    public void closeStream() {
        try {
            Files.write(
                    Paths.get(htmlFilePath),
                    HTMLConstants.HTML_END_TAG.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.error("Error in html write, in close stream");
        }
    }
}
