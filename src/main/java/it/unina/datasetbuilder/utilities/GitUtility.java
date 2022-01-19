package it.unina.datasetbuilder.utilities;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.concurrent.*;

public class GitUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitUtility.class);

    /**
     *
     * @param uri
     * @param clonePath: it must be an empty folder
     * @param shaCommit: git commit identifier; if not null, it is used to checkout that specific commit
     * @throws GitAPIException
     * @return git ref
     */
    public static Git cloneRepo(String uri, String clonePath, String shaCommit,long timeoutMinutes) throws GitAPIException, TimeoutException {
        Git git;
        Callable<Git> call= () -> Git.cloneRepository()
                .setURI(uri)
                .setDirectory(new File(clonePath))
                //.setBranchesToClone(Collections.singletonList("refs/heads/master"))
                .call();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Git> gitCloned = executor.submit(call);
        try {
            git = gitCloned.get(timeoutMinutes, TimeUnit.MINUTES);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            LOGGER.error("Timeout ERROR:{}",shaCommit,e);
            git = closeExecutor(uri, clonePath, gitCloned);
            executor.shutdownNow();
            throw  new TimeoutException();
        } finally {
            executor.shutdownNow();
        }

        if(shaCommit!=null && !shaCommit.isEmpty()){
            try {
                git.checkout()
                        .setCreateBranch(true)
                        .setName(shaCommit)
                        .setStartPoint(shaCommit)
                        .call();
            }catch (RefNotFoundException| InvalidPathException |JGitInternalException e){
                LOGGER.error("Clone: {} Ref: {} cannot be resolved",uri,shaCommit);
                String project = clonePath.substring(0,clonePath.lastIndexOf("/"));
                FilesUtility.createFile(project +"/RefNotFound.txt");
                throw new RefNotFoundException("Error in cloning specific commit");
            }finally {
                closeConnection(git);
            }
        }
        return git;
    }

    private static Git closeExecutor(String uri, String clonePath, Future<Git> gitCloned)  {
        String project = clonePath.substring(0,clonePath.lastIndexOf("/"));
        FilesUtility.createFile(project +"/CloneTimeout.txt");
        Git git=null;
        try {
            git = Git.open(new File(clonePath));
            closeConnection(git);
        } catch (IOException ex) {
            LOGGER.error("IOException while close repo");
        }
        gitCloned.cancel(true);
        return git;
    }

    public static Git checkoutAtSpecificVersion(Git git,String shaCommit) throws GitAPIException {
        try {
            git.checkout()
                    .setCreateBranch(true)
                    .setName(shaCommit)
                    .setStartPoint(shaCommit)
                    .call();
        }catch (RefNotFoundException e){
            LOGGER.error("Ref: {} cannot be resolved",shaCommit);
            throw new RefNotFoundException("Error in cloning specific commit");
        }
        return git;
    }

    public static void closeConnection(Git git){
        git.getRepository().close();
        git.close();
    }
}
