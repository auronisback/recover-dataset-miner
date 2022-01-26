package it.unina.recoverminer.utilities;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

@Ignore
public class FilesUtilityTest {
    @Test
    public void moveTest() throws IOException {
        //TODO add before to crete dire and file using for moving
        //This method, does not create target new DIR!
        /*Path fromPath = Paths.get("src/test/resources/movetest/movet");
        Path toPath = Paths.get("src/test/resources/movetest/createonfly/movet");
        Files.move(fromPath,toPath);*/
        FilesUtility.moveFile("src/test/resources/movetest/createonfly/movet","src/test/resources/movetestdue/createonflydue/movet");
        Path toPath = Paths.get("src/test/resources/movetestdue/createonflydue/movet");
        Assert.assertTrue(Files.exists(toPath));
    }
    @Test
    public void copyDirTest() throws IOException {
        Path src = Paths.get("src/test/resources/dirToCopy1");
        Path target = Paths.get("src/test/resources/dirToCopy3");
        System.out.println("Paths instantiated");
        CopyOption[] copyOptions = {StandardCopyOption.REPLACE_EXISTING};
        FilesUtility.moveFolder(src,target, copyOptions);
        Assert.assertTrue(Files.exists(target));
    }
    @Test
    public void createDirTest() throws IOException{
        FilesUtility.createDir("src/test/resources/dirCreated/subdir1");
        FilesUtility.createDir("src/test/resources/dirCreated/subdir2");
        Assert.assertTrue(true);
    }
    @Test
    public void createFileTest() throws IOException{
        FilesUtility.createFile("src/test/resources/success.txt");
        Assert.assertTrue(true);
    }

    @Test
    public void findFilesAndMoveTest() throws IOException{
        FilesUtility.findFilesAndMove("src/test/resources","src/test/resources/surefire-reports","dir3");
        Assert.assertTrue(true);
    }

    @Test
    public void searchStringFromFileOKTest() throws IOException{
        FilesUtility.findFilesAndMove("src/test/resources","src/test/resources/surefire-reports","dir3");
        boolean build_failure = FilesUtility.searchStringFromFile("src/test/resources/dir1/dir2/mavenCli.log", "BUILD FAILURE");
        Assert.assertTrue(build_failure);
    }
    @Test
    public void searchStringFromFileNOTOKTest() throws IOException{
        FilesUtility.findFilesAndMove("src/test/resources","src/test/resources/surefire-reports","dir3");
        boolean build_failure = FilesUtility.searchStringFromFile("src/test/resources/dir1/dir2/mavenCli.log", "CARMINE");
        Assert.assertFalse(build_failure);
    }
}
