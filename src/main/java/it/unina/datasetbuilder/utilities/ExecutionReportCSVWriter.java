package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.dto.JobInformationDTO;

import java.util.Collections;

public class ExecutionReportCSVWriter implements IExecutionReportWriter{

    private static final String[] HEADER=
            new String[]{"PROJECT","Build","Job","Previous Build","Previous Job","SHA Commit","Previous SHA Commit","Status"};

    private CSVWriter csvWriter;

    public ExecutionReportCSVWriter(String path) {
        csvWriter= new CSVWriter(path,HEADER);
    }

    @Override
    public void writeRecord(JobInformationDTO job) {
        JobInformationDTO prevJob = job.getPreviousJobInfo();
        String[] record= new String[]{job.getSlugName(),job.getBuildID(),job.getJobId(), prevJob.getBuildID(),
                prevJob.getJobId(),job.getCommmit(), prevJob.getCommmit(),job.getStatus().name()};
        csvWriter.writeCSV(Collections.singletonList(record));
    }

    @Override
    public void closeStream() {
        //TODO write summary or LOG
    }

}
