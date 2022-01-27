package it.unina.recoverminer.sqlite.recover;

import it.unina.recoverminer.sqlite.recover.dto.TestCaseDto;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class SurefireReportsParser {

  public Collection<TestCaseDto> loadSuiteFromReportFolder(String folder) throws IOException {
    return this.loadSuiteFromReportFolder(Paths.get(folder));
  }

  /**
   * Loads a test suite given the folder in which surefire
   * reports are stored.
   *
   * @param folder The folder in which reports related to
   *               single test classes are stored
   *
   * */
  public Collection<TestCaseDto> loadSuiteFromReportFolder(Path folder) throws IOException {
    Map<String, TestCaseDto> testMap = new HashMap<>();
    Files.walk(folder).filter(Files::isRegularFile)
        .filter(f -> f.getFileName().toString().endsWith(".xml"))
        .forEach(f -> this.parseAndAddTests(f, testMap));
    return testMap.values();
  }

  private void parseAndAddTests(Path f, Map<String, TestCaseDto> testMap) {
    try {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(f.toString()));
      NodeList testList = doc.getElementsByTagName("testcase");
      for(int i = 0, listLength = testList.getLength(); i < listLength; i++) {
        Element testCase = (Element) testList.item(i);
        String testName = testCase.getAttribute("name");
        String className = testCase.getAttribute("classname");
        String qualifiedName = className + "#" + testName;
        if (!testMap.containsKey(qualifiedName)) { // Test already there
          TestCaseDto t =  new TestCaseDto();
          t.setName(className + "#" + testName);
          testMap.put(t.getName(), t);
          // If the test case failed, a new fault is added based on the type of error
          if (testCase.getElementsByTagName("failure").getLength() > 0)
            t.setStatus(TestCaseDto.Status.FAIL);
          else if (testCase.getElementsByTagName("error").getLength() > 0)
            t.setStatus(TestCaseDto.Status.ERROR);
          else t.setStatus(TestCaseDto.Status.PASS);
        }
      }
    } catch (ParserConfigurationException | IOException | SAXException ex) {
      Logger.getGlobal().warning(ex.getMessage());
    }
  }
}
