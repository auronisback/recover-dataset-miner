package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.utilities.enums.MavenBuildResultEnum;
import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MavenInvokerExecutor implements IMavenCommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MavenInvokerExecutor.class);


    private String projectPath;
    private String mavenHomePath;
    private String logFilePath;
    private BufferedWriter fw;

    public MavenInvokerExecutor(String projectPath,String mavenHomePath) {
        System.getProperties().setProperty("maven.multiModuleProjectDirectory", mavenHomePath);
        this.projectPath=projectPath;
        this.mavenHomePath=mavenHomePath;
        String project = projectPath.substring(0,projectPath.lastIndexOf("/"));
        logFilePath = project + "/mavenCli.log";
        FilesUtility.createFile(logFilePath);
        try {
            fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFilePath, true), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Error in maveninvokeExecutor constructor");
        }
    }

    @Override
    public MavenBuildResultEnum executeCommands(String[] commands) {
        InvocationRequest request = new DefaultInvocationRequest();
        File file = new File(projectPath + "/pom.xml");
        request.setPomFile(file);
        request.setGoals( Arrays.asList( commands) );
        request.setBatchMode(true);
        InvocationResult result=null;
        Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(s -> {
            try {
                fw.write(s);
                fw.newLine();
            } catch (IOException e) {
                LOGGER.error("Error when writing mavenCli");
            }
        });
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        try {
            result = invoker.execute(request);
            fw.flush();
        } catch (MavenInvocationException | IOException e) {
            LOGGER.error("Error in maven invoke or flush stream");
        }
        if(result!=null && result.getExitCode() == 0 && result.getExecutionException() == null)
            return MavenBuildResultEnum.SUCCESS;
        else
            return MavenBuildResultEnum.TEST_ERROR;
    }

    @Override
    public String getLogFilePath() {
        return logFilePath;
    }

    @Override
    public void closeStream() {
        try {
            fw.close();
        } catch (IOException e) {
            LOGGER.error("Error in closeWriter constructor");
        }
    }
}
