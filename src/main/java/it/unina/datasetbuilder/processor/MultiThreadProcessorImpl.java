package it.unina.datasetbuilder.processor;

import it.unina.datasetbuilder.config.AppConfig;
import it.unina.datasetbuilder.dto.JobInformationDTO;
import it.unina.datasetbuilder.dto.RepositoryDTO;
import it.unina.datasetbuilder.integration.GitHubApiInvoker;
import it.unina.datasetbuilder.utilities.*;
import it.unina.datasetbuilder.utilities.enums.JobStatusEnum;
import it.unina.datasetbuilder.utilities.enums.MavenBuildResultEnum;
import it.unina.datasetbuilder.utilities.enums.ProcessResultEnum;
import it.unina.datasetbuilder.utilities.exceptions.GitHubRateLimitException;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class MultiThreadProcessorImpl implements MultiThreadProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadProcessorImpl.class);
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private GitHubApiInvoker gitHubApiInvoker;
    @Async("taskExecutor")
    public CompletableFuture<Void> processSingleJob(Map<String, String> cloneLinkMap, JobInformationDTO job,IExecutionReportWriter... iExecutionReportWriter) {
        String slugName=replaceSlashChar(job.getSlugName());
        CSVWriter csvWriter=null;
        if(!isJobAlreadyProcessed(job)){
            LOGGER.info("Processing project: {}, jobid:{}",slugName,job.getJobId());
            //CREATE JOB FOLDER
            String jobPath = appConfig.getClonebasepath() + slugName + "/" + job.getJobId();
            String clonePath = jobPath + "/" + job.getCommmit() + "/sources";
            String prevClonePath = jobPath +"/"+ job.getPreviousJobInfo().getCommmit() + "/sources";
            FilesUtility.createDir(clonePath);
            FilesUtility.createFile(jobPath + "/" + job.getCommmit() + "_current.txt");
            FilesUtility.createDir(prevClonePath);
            FilesUtility.createFile(jobPath +"/"+ job.getPreviousJobInfo().getCommmit()+"_previous.txt");
            //START CLONING PHASE
            LOGGER.info("GET CLONEURL of slugname: {} job: {}",slugName,job.getJobId());

            String cloneUrl=getCloneUrl(cloneLinkMap, job.getSlugName());
            try {
                LOGGER.info("START CLONE of current slugname: {} job: {} commit: {}",slugName,job.getJobId(),job.getCommmit());
                GitUtility.cloneRepo(cloneUrl, clonePath, job.getCommmit(),appConfig.getTimeoutMins());
                LOGGER.info("START CLONE of prev slugname: {} job: {} commit: {}",slugName,job.getJobId(),job.getPreviousJobInfo().getCommmit());
                GitUtility.cloneRepo(cloneUrl, prevClonePath, job.getPreviousJobInfo().getCommmit(),appConfig.getTimeoutMins());
                LOGGER.info("END CLONE slugname: {} job: {}",slugName,job.getJobId());
                LOGGER.info("START MAVEN PROCESS slugname: {} job: {}",slugName,job.getJobId());
                //START MAVEN PHASES
                MavenPomEditor mavenPomEditor = new MavenPomEditor(clonePath + "/pom.xml");
                removeIncompatiblePlugins(mavenPomEditor);
                MavenPomEditor previousMavenPomEditor = new MavenPomEditor(prevClonePath + "/pom.xml");
                IMavenCommandExecutor mavenCommandsExecutor = new MavenInvokerExecutor(clonePath,"$MAVEN_HOME");
                MavenBuildResultEnum result = mavenCommandsExecutor.executeCommands(new String[]{ "test","-fn"});
                boolean isTestFailed = FilesUtility.searchStringFromFile(mavenCommandsExecutor.getLogFilePath(), "There are test failures");
                if(MavenBuildResultEnum.SUCCESS.equals(result) && isTestFailed){
                    LOGGER.info("Job:{} currentSHA:{} compiled and has some test errors correctly",slugName,job.getCommmit());
                    IMavenCommandExecutor mavenCommandsPrevExecutor = new MavenInvokerExecutor(prevClonePath,"$MAVEN_HOME");
                    MavenBuildResultEnum resultPrev = mavenCommandsPrevExecutor.executeCommands(new String[]{ "compile"});
                    if(MavenBuildResultEnum.SUCCESS.equals(resultPrev)){
                        LOGGER.info("Job:{} previousSHA:{} compiled",slugName,job.getPreviousJobInfo().getCommmit());
                        addClover(previousMavenPomEditor);
                        String[] commands = !appConfig.getRequireAllTestOnPrev() ? new String[]{"clover:setup", "test", "-fn", "clover:aggregate", "clover:clover"}
                                : new String[]{"clover:setup", "test", "clover:aggregate", "clover:clover"};
                        String[] commands1 = new String[]{"mvn test -Dmaven.test.failure.ignore=true"};
                        MavenBuildResultEnum resultCloverPrev = mavenCommandsPrevExecutor.executeCommands(commands);
                        MavenBuildResultEnum resultCloverPrev1 = mavenCommandsPrevExecutor.executeCommands(commands1);
                        if(MavenBuildResultEnum.SUCCESS.equals(resultCloverPrev)){
                            LOGGER.info("Job:{} previousSHA:{} with clover tested",slugName,job.getPreviousJobInfo().getCommmit());
                            job.setStatus(JobStatusEnum.SUCCESS);
                            mavenCommandsExecutor.closeStream();
                            mavenCommandsPrevExecutor.closeStream();
                            String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.SUCCESS.name(), job.getJobId());
                            moveCloverFile(job, targetFolder);

                            String surefireTargetPath = targetFolder + "/" +
                                    job.getCommmit() + "/surefire-reports";
                            String surefirePrevPath = targetFolder + "/" +
                                    job.getPreviousJobInfo().getCommmit() + "/surefire-reports";
                            FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getCommmit() +"/sources", surefireTargetPath,"surefire-reports");
                            FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources", surefirePrevPath,"surefire-reports");

                            synchronized (this){
                                SureFireReportSummaryExtractor sureFireReportSummaryExtractor=new SureFireReportSummaryExtractorJaxbImpl(surefireTargetPath);
                                sureFireReportSummaryExtractor.extractSummaryReport();
                                SureFireReportSummaryExtractor sureFireReportSummaryExtractor1=new SureFireReportSummaryExtractorJaxbImpl(surefirePrevPath);
                                sureFireReportSummaryExtractor1.extractSummaryReport();
                            }
                            FilesUtility.createFile(targetFolder+"/"+"done.txt");
                        }else {
                            boolean isntTestFailed = !FilesUtility.searchStringFromFile(mavenCommandsPrevExecutor.getLogFilePath(), "There are test failures");
                            if(isntTestFailed){
                                LOGGER.info("Job:{} previousSHA:{} with clover failure",slugName,job.getPreviousJobInfo().getCommmit());
                                job.setStatus(JobStatusEnum.FAILURE);
                                mavenCommandsExecutor.closeStream();
                                mavenCommandsPrevExecutor.closeStream();
                                String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.FAILURE.name(), job.getJobId());
                                moveCloverFile(job, targetFolder);
                                LOGGER.info("Clover file moved:{}",job.getJobId());

                                String currSourcesFolder = targetFolder +"/"+ job.getCommmit() +"/sources";
                                String prevSourcesFolder = targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources";
                                deleteSourcesDirectory(currSourcesFolder);
                                deleteSourcesDirectory(prevSourcesFolder);

                                FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getCommmit() +"/sources", targetFolder+"/"+
                                        job.getCommmit()+ "/surefire-reports","surefire-reports");
                                LOGGER.info("SureFireReportMoved: {}",job.getJobId());
                                synchronized (this){
                                    csvWriter= new CSVWriter(appConfig.getClonebasepath()+"pluginsError.csv",PluginCSVWriter.getHeader());
                                    LOGGER.info("Plugin csw created: {}",job.getJobId());
                                }
                                List<Plugin> plugins = Optional.ofNullable(previousMavenPomEditor.getPomModel().getBuild()).orElse(new Build())
                                        .getPlugins();
                                plugins=Optional.ofNullable(plugins).orElse(new ArrayList<>()).stream().filter(plugin -> !"clover-maven-plugin".equals(plugin.getArtifactId()))
                                        .collect(Collectors.toList());
                                PluginCSVWriter.writePluginsToCSV(csvWriter, job, plugins);
                                LOGGER.info("Plugin csw wrote: {}",job.getJobId());
                                FilesUtility.createFile(targetFolder+"/"+"cloverProblem.txt");
                            }else{
                                LOGGER.info("Job:{} previousSHA:{} without clover test failure",slugName,job.getPreviousJobInfo().getCommmit());
                                job.setStatus(JobStatusEnum.PREV_TEST_FAILED);
                                mavenCommandsExecutor.closeStream();
                                mavenCommandsPrevExecutor.closeStream();



                                String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.ERROR.name(), job.getJobId());

                                String currSourcesFolder = targetFolder +"/"+ job.getCommmit() +"/sources";
                                String prevSourcesFolder = targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources";
                                deleteSourcesDirectory(currSourcesFolder);
                                deleteSourcesDirectory(prevSourcesFolder);

                                FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getCommmit() +"/sources", targetFolder+"/"+
                                        job.getCommmit()+ "/surefire-reports","surefire-reports");
                                FilesUtility.createFile(targetFolder+"/"+"done.txt");
                            }
                        }
                    }else{
                        LOGGER.info("Job:{} previousSHA:{} compile error",slugName,job.getPreviousJobInfo().getCommmit());
                        job.setStatus(JobStatusEnum.PREV_NO_COMPILE);
                        mavenCommandsExecutor.closeStream();
                        mavenCommandsPrevExecutor.closeStream();

                        String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.ERROR.name(), job.getJobId());

                        String currSourcesFolder = targetFolder +"/"+ job.getCommmit() +"/sources";
                        String prevSourcesFolder = targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources";
                        deleteSourcesDirectory(currSourcesFolder);
                        deleteSourcesDirectory(prevSourcesFolder);

                        FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getCommmit() +"/sources", targetFolder+"/"+
                                job.getCommmit()+ "/surefire-reports","surefire-reports");
                        FilesUtility.createFile(targetFolder+"/"+"done.txt");
                    }
                }else{
                    LOGGER.info("Job:{} currentSHA:{} build failed or NO test failed",slugName,job.getCommmit());
                    if(!isTestFailed)
                        LOGGER.info("there are no test failures");
                    else
                        LOGGER.info("there are test failures, so build failed");
                    job.setStatus(isTestFailed? JobStatusEnum.CURR_NO_TEST_FAIL:JobStatusEnum.CURR_NO_COMPILE);
                    mavenCommandsExecutor.closeStream();

                    String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.ERROR.name(), job.getJobId());

                    String currSourcesFolder = targetFolder +"/"+ job.getCommmit() +"/sources";
                    String prevSourcesFolder = targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources";
                    deleteSourcesDirectory(currSourcesFolder);
                    deleteSourcesDirectory(prevSourcesFolder);

                    FilesUtility.findFilesAndMove(targetFolder +"/"+ job.getCommmit() +"/sources", targetFolder+"/"+
                            job.getCommmit()+ "/surefire-reports","surefire-reports");
                    FilesUtility.createFile(targetFolder+"/"+"done.txt");
                }
            } catch (IOException | XmlPullParserException | GitAPIException| TimeoutException ex){
                if(ex instanceof CheckoutConflictException) {
                    LOGGER.error("Error In clonig job: {} CheckoutConflict", job.getJobId());
                    job.setStatus(JobStatusEnum.ERROR);
                }else if(ex instanceof GitAPIException) {
                    LOGGER.error("Error In clonig job: {}", job.getJobId());
                    job.setStatus(JobStatusEnum.REF_NOT_FOUND);
                }else if(ex instanceof TimeoutException){
                    LOGGER.error("Error In clonig job: {}",job.getJobId());
                    job.setStatus(JobStatusEnum.CLONE_TIMEOUT);
                }
                else {
                    LOGGER.error("Error {}", job.getJobId(), ex);
                    job.setStatus(JobStatusEnum.ERROR);
                }
                String targetFolder = moveFolderByResult(slugName, jobPath, ProcessResultEnum.ERROR.name(), job.getJobId());

                String currSourcesFolder = targetFolder +"/"+ job.getCommmit() +"/sources";
                String prevSourcesFolder = targetFolder +"/"+ job.getPreviousJobInfo().getCommmit() +"/sources";
                try {
                    deleteSourcesDirectory(currSourcesFolder);
                    deleteSourcesDirectory(prevSourcesFolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FilesUtility.createFile(targetFolder+"/"+"done.txt");
            }
        }else{
            LOGGER.info("Job {} already processed",slugName + "@" + job.getJobId()
                + (job.getStatus() == JobStatusEnum.ALREADY_PROCESSED_ERROR ? " in ERROR" :
                   job.getStatus() == JobStatusEnum.ALREADY_PROCESSED_SUCCESS ? " in SUCCESS" : " in FAILURE" ));
        }
        LOGGER.info("Job correctly processed:{}",job.getJobId());
        //WRITE results in report
        synchronized (this){
            for (int i=0;i<iExecutionReportWriter.length;i++){
                if(job.getStatus()==null) job.setStatus(JobStatusEnum.ERROR);
                iExecutionReportWriter[i].writeRecord(job);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    private void deleteSourcesDirectory(String sourcesPath) throws IOException {
        File tempRoot = new File(sourcesPath);

        for(File file : tempRoot.listFiles()){
            if(file.isFile())
                file.delete();
            if(file.isDirectory()) {
                deleteSourcesDirectory(sourcesPath + "/" + file.getName());
                if(file.listFiles().length == 0)
                    file.delete();
            }
        }
    }

    private void moveCloverFile(JobInformationDTO job, String targetFolder) {
        FilesUtility.moveFile(targetFolder + "/" + job.getPreviousJobInfo().getCommmit() + "/sources" + appConfig.getCloverdbdir(), targetFolder + "/" +
                job.getPreviousJobInfo().getCommmit() + "/clover.db");
        FilesUtility.moveFile(targetFolder + "/" + job.getPreviousJobInfo().getCommmit() + "/sources" + appConfig.getCloverMergedbdir(), targetFolder + "/" +
                job.getPreviousJobInfo().getCommmit() + "/cloverMerge.db");
    }

    private String moveFolderByResult(String slugName, String jobPath, String folderType, String jobId) {
        Path src = Paths.get(jobPath);
        String targetString = appConfig.getClonebasepath() + slugName + "/"+ folderType +"/"+ jobId;
        Path target = Paths.get(targetString);
        CopyOption[] copyOptions = {StandardCopyOption.REPLACE_EXISTING};
        FilesUtility.moveFolder(src,target,copyOptions);
        return targetString;
    }

    private String getCloneUrl(Map<String, String> cloneLinkMap, String slugName) {
        if(!cloneLinkMap.containsKey(slugName)){
//            try {
//                LOGGER.info("calling git hub for: {}",slugName);
//                RepositoryDTO repositoryDTO = gitHubApiInvoker.getRepoBySlugName(slugName);
                // Skipping the invocation to GitHub APIs
                String [] tokens = slugName.split("/");
                RepositoryDTO repositoryDTO = new RepositoryDTO();
                System.out.println(slugName);
                if(tokens.length == 2) {
                    repositoryDTO.setName(tokens[1]);
                    repositoryDTO.setFullName(slugName);
                    repositoryDTO.setCloneUrl("https://github.com/" + tokens[0] + "/" + tokens[1] + ".git");
                    LOGGER.info("Creating clone URL skipping GitHub API: " + repositoryDTO.getCloneUrl());
                    cloneLinkMap.put(slugName, repositoryDTO.getCloneUrl());
                } else {
                    LOGGER.error("Invalid slugname: " + slugName);
                }
//            }catch(GitHubRateLimitException e) {
//                LOGGER.error("Call to Git hub API, Rate limit excedeed, skip other slugName");
//            }
        }
        return cloneLinkMap.get(slugName);
    }

    private boolean isJobAlreadyProcessed(JobInformationDTO job){
        String slugName = job.getSlugName().replace("/", "@");
        Path successPath = Paths.get(appConfig.getClonebasepath() + slugName +"/"+ProcessResultEnum.SUCCESS+"/"+job.getJobId()+"/"+"done.txt");
        Path failurePath = Paths.get(appConfig.getClonebasepath() + slugName +"/"+ProcessResultEnum.FAILURE+"/"+job.getJobId()+"/"+"done.txt");
        Path errorPath = Paths.get(appConfig.getClonebasepath() + slugName +"/"+ProcessResultEnum.ERROR+"/"+job.getJobId()+"/"+"done.txt");


        if(Files.exists(successPath)){
            job.setStatus(JobStatusEnum.ALREADY_PROCESSED_SUCCESS);
            return true;
        }
        else if(Files.exists(failurePath)){
            job.setStatus(JobStatusEnum.ALREADY_PROCESSED_FAILURE);
            return true;
        }
        else if(Files.exists(errorPath)){
            job.setStatus(JobStatusEnum.ALREADY_PROCESSED_ERROR);
            return true;
        }
        return false;
        //return Files.exists(errorPath)||Files.exists(failurePath)||Files.exists(successPath);
    }

    private void addClover(MavenPomEditor mavenPomEditor) {
        try {
            removeIncompatiblePlugins(mavenPomEditor);
            mavenPomEditor.addDependency("org.openclover","clover-maven-plugin",null,"4.4.1","maven-plugin");
            mavenPomEditor.addPlugin("org.openclover","clover-maven-plugin");
            mavenPomEditor.flushPom();
        } catch (Exception e) {
            LOGGER.error("ERRORE in aggiunta dipendenza clover",e);
        }
    }

    private void removeIncompatiblePlugins(MavenPomEditor mavenPomEditor) {
        try{
            appConfig.getPluginstoremove().forEach(mavenPomEditor::removePlugin);
            mavenPomEditor.flushPom();
        } catch (Exception e) {
            LOGGER.error("ERRORE in rimozione dipendenze incompatibili",e);
        }
    }

    private String replaceSlashChar(String fullName) {
        return fullName.replace("/", "@");
    }
}
