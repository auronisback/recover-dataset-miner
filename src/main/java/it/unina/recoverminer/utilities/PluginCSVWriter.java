package it.unina.recoverminer.utilities;

import it.unina.recoverminer.dto.JobInformationDTO;
import org.apache.maven.model.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginCSVWriter {
    private static final String[] HEADER=new String[]
            { "SLUG_NAME","JOB_ID","GROUP_ID","PLUGIN_NAME" };
    public static void writePluginsToCSV(CSVWriter csvWriter, JobInformationDTO job, List<Plugin> plugins) {
        if(plugins!=null) {
            List<String[]> dataLines = new ArrayList<>();
            for (Plugin plugin : plugins) {
                String[] row = new String[]{job.getSlugName(), job.getJobId(), plugin.getGroupId(), plugin.getArtifactId()};
                dataLines.add(row);
            }
            csvWriter.writeCSV(dataLines);
        }
    }
    public static String[] getHeader(){
        return HEADER;
    }
}
