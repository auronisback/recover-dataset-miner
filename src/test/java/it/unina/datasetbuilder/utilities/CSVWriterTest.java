package it.unina.datasetbuilder.utilities;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Ignore
public class CSVWriterTest {
    @Test
    public void testCSV() {
        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]
                { "Peppe", "Doe", "38", "Comment Data\nAnother line of comment data" });
        dataLines.add(new String[]
                { "Carmine", "Doe, Jr.", "19", "She said \"I'm being quoted\"" });
        CSVWriter csvWriter = new CSVWriter("src/test/resources/filecsv2.csv",PluginCSVWriter.getHeader());
        csvWriter.writeCSV(dataLines);
        Assert.assertTrue(Files.exists(Paths.get("src/test/resources/filecsv2.csv")));
    }
}
