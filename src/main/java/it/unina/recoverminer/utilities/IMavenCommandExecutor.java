package it.unina.recoverminer.utilities;

import it.unina.recoverminer.utilities.enums.MavenBuildResultEnum;

public interface IMavenCommandExecutor {
    MavenBuildResultEnum executeCommands(String[] commands);
    String getLogFilePath();
    void closeStream();
}
