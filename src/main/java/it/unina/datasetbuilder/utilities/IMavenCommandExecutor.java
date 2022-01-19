package it.unina.datasetbuilder.utilities;

import it.unina.datasetbuilder.utilities.enums.MavenBuildResultEnum;

public interface IMavenCommandExecutor {
    MavenBuildResultEnum executeCommands(String[] commands);
    String getLogFilePath();
    void closeStream();
}
