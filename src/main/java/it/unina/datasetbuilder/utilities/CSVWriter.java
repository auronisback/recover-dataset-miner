package it.unina.datasetbuilder.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVWriter.class);
    private static final String SEPARATOR="\r\n";

    private String csvFilePath;

    public CSVWriter(String csvFilePath,String [] header) {
        this.csvFilePath = csvFilePath;
        if (Files.notExists(Paths.get(csvFilePath))) {
            try {
                FilesUtility.createFile(csvFilePath);
                Files.write(
                        Paths.get(csvFilePath),
                        convertToCSV(header).getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException ex) {
                LOGGER.error("Error in file {} creation",csvFilePath);
            }
        }
    }

    public void writeCSV(List<String[]> dataLines) {
        dataLines.stream()
                .map(CSVWriter::convertToCSV)
                .forEach(contentToAppend-> {
                    try {
                        Files.write(
                                Paths.get(csvFilePath),
                                contentToAppend.getBytes(),
                                StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static String convertToCSV(String[] data) {
        String row = Stream.of(data)
                .map(CSVWriter::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
        return row + SEPARATOR;
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData="";
        if(data!=null){
            escapedData = data.replaceAll("\\R", " ");
            if (data.contains(",") || data.contains("\"") || data.contains("'")) {
                data = data.replace("\"", "\"\"");
                escapedData = "\"" + data + "\"";
            }
        }
        return escapedData;
    }
}
