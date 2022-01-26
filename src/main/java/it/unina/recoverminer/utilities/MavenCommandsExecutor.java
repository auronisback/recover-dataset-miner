package it.unina.recoverminer.utilities;

import it.unina.recoverminer.utilities.enums.MavenBuildResultEnum;
import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Path;

public class MavenCommandsExecutor implements IMavenCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MavenCommandsExecutor.class);

    private String projectPath;
    private String logFilePath;
    private MavenCli cli;
    private  PrintStream stdin;

    public MavenCommandsExecutor(String projectPath,String mavenHomePath) {
        this.projectPath = projectPath;
        System.getProperties().setProperty("maven.multiModuleProjectDirectory", mavenHomePath);
        cli = new MavenCli();
        try {
            String project = projectPath.substring(0,projectPath.lastIndexOf("/"));
            logFilePath = project + "/mavenCli.log";
            Path file = FilesUtility.createFile(logFilePath);
            stdin = new PrintStream(file.toFile());
        } catch (FileNotFoundException e) {
            LOGGER.error("ERROR in PrintStream creation for logging maven");
        }
    }

    public MavenBuildResultEnum executeCommands(String[] commands){
        return MavenBuildResultEnum.valueOfResult(cli.doMain(commands,projectPath, stdin, stdin));
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void closeStream(){
        stdin.close();
    }
}
