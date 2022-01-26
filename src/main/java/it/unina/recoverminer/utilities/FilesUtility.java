package it.unina.recoverminer.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FilesUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtility.class);

    public static void moveFile(String from,String to){
        try {
            Path fromPath = Paths.get(from);
            Path newDir = Paths.get(to);
            move(fromPath, newDir);
        }catch (IOException ex){
            LOGGER.error("Error in move file from: {} to: {}",from,to);
        }
    }

    private static void move(Path fromPath, Path newDir) throws IOException {
        if (Files.notExists(newDir.getParent())) {
            Files.createDirectories(newDir.getParent());
        }
        Files.move(fromPath, newDir);
    }

    public static void createDir(String path) {
        Path newPath = Paths.get(path);
        if(Files.notExists(newPath)){
            try {
                Files.createDirectories(newPath);
            } catch (IOException e) {
                LOGGER.error("Error in createDir path:{}",path);
            }
        }
    }

    public static Path createFile(String path) {
        Path newPath = Paths.get(path);
        Path file=null;
        if(Files.notExists(newPath)){
            try {
                file = Files.createFile(newPath);
            } catch (IOException e) {
                LOGGER.error("Error in createFile path:{}",path);
            }
        }
        return file!=null?file:newPath;
    }

    public static void moveFolder(Path source, Path target, CopyOption... options) {
        try {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    Files.createDirectories(target.resolve(source.relativize(dir)));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    //TODO this is due to maven invoker that lock pom.xml
                    if(!file.endsWith("pom.xml"))
                        Files.move(file, target.resolve(source.relativize(file)), options);
                    else {
                        Files.copy(file, target.resolve(source.relativize(file)), options);
                        //file.toFile().renameTo(target.resolve(source.relativize(file)).toFile());
                        //Files.(file, target.resolve(source.relativize(file)), options);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(
                        Path dir, IOException exc) throws IOException {
                    //TODO this is due to maven invoker that lock pom.xml
                    //Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOGGER.error("Error in moveFolder from path:{} to path:{}",source.toString(),target.toString(),e);
        }
    }

    public static void findFilesAndMove(String basePath,String targetPath,String toSearch){
        try (Stream<Path> stream = Files.find(Paths.get(basePath),10,
                (path, attr) -> path.getFileName().toString().equals(toSearch) )) {
            AtomicInteger atomicInteger=new AtomicInteger();
            stream.forEach(filePath-> {
                int andIncrement = atomicInteger.getAndIncrement();
                StandardCopyOption[] standardCopyOptions = {StandardCopyOption.REPLACE_EXISTING};
                moveFolder(filePath,Paths.get(targetPath+"/"+andIncrement), standardCopyOptions);
            });
        } catch (IOException e) {
            LOGGER.error("Error in findFilesAndMove from: {}, file to search:{}",basePath,toSearch);
        }
    }

    public static boolean searchStringFromFile(String filePath,String wordToSearch) {
        Path path = Paths.get(filePath);
        try(Stream <String> streamOfLines = Files.lines(path, StandardCharsets.UTF_8)) {
            Optional<String> line = streamOfLines.filter(l ->
                    l.contains(wordToSearch))
                    .findFirst();
            return line.isPresent();
        }catch(Exception e) {
            LOGGER.error("Exception in find String in file:{}",filePath,e);
        }
        return false;
    }
}
