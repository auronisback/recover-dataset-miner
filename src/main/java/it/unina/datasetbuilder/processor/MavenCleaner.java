package it.unina.datasetbuilder.processor;

import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class MavenCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MavenCleaner.class);

    public static void main(String[] args){
        LOGGER.info("RUNNING MAVEN CLEANER");
        if(args!=null && args[0]!=null){
            String basePath=args[0];
            LOGGER.info("BASE PATH:{}",basePath);
            try {
                findAndProcessMavenFolder(basePath);
            } catch (Exception e) {
                LOGGER.error("ERROR DURING WALKING DIR TREE");
            }
            LOGGER.info("MAVEN CLEANER ANALYZER FINISHED");

        }else{
            LOGGER.error("NO BASE PATH IN INPUT");
        }

    }

    private static void findAndProcessMavenFolder(String basePath){

        try (Stream<Path> stream = Files.find(Paths.get(basePath),6,
                (path, attr) -> path.getFileName().toString().equals("pom.xml") )) {
            stream.forEach(filePath-> {
                String filePathString = filePath.toString();
                if(filePathString.contains("SUCCESS") || filePathString.contains("ERROR") || filePathString.contains("FAILURE")) {
                    LOGGER.info(filePathString);
                    executeCommands(new String[]{ "clean"},filePathString);
                }
            });
        } catch (IOException e) {
            LOGGER.error("Error in findAndProcessMavenFolder from: {}, file to search:pom.xml",basePath);
        }
    }

    public static void executeCommands(String[] commands,String pomPath) {
        InvocationRequest request = new DefaultInvocationRequest();
        File file = new File(pomPath);
        request.setPomFile(file);
        request.setGoals( Arrays.asList( commands) );
        request.setBatchMode(true);
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        try {invoker.execute(request);
        } catch (MavenInvocationException e) {
            LOGGER.error("Error in  Maven CLEANER : maven invoke");
        }
    }
}
