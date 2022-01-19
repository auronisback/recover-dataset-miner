package it.unina.datasetbuilder.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "appconfig")
public class AppConfig {

    private List<String> pluginstoremove;
    private String clonebasepath;
    private String cloverdbdir;
    private String cloverMergedbdir;
    private String surefirereportdir;
    private long timeoutMins;
    private Boolean requireAllTestOnPrev;

    public String getSurefirereportdir() {
        return surefirereportdir;
    }

    public void setSurefirereportdir(String surefirereportdir) {
        this.surefirereportdir = surefirereportdir;
    }

    public String getCloverdbdir() {
        return cloverdbdir;
    }

    public void setCloverdbdir(String cloverdbdir) {
        this.cloverdbdir = cloverdbdir;
    }

    public String getClonebasepath() {
        return clonebasepath;
    }

    public void setClonebasepath(String clonebasepath) {
        this.clonebasepath = clonebasepath;
    }

    public List<String> getPluginstoremove() {
        return pluginstoremove;
    }

    public void setPluginstoremove(List<String> pluginstoremove) {
        this.pluginstoremove = pluginstoremove;
    }

    public String getCloverMergedbdir() {
        return cloverMergedbdir;
    }

    public void setCloverMergedbdir(String cloverMergedbdir) {
        this.cloverMergedbdir = cloverMergedbdir;
    }

    public long getTimeoutMins() {
        return timeoutMins;
    }

    public void setTimeoutMins(long timeoutMins) {
        this.timeoutMins = timeoutMins;
    }

    public Boolean getRequireAllTestOnPrev() {
        return requireAllTestOnPrev;
    }

    public void setRequireAllTestOnPrev(Boolean requireAllTestOnPrev) {
        this.requireAllTestOnPrev = requireAllTestOnPrev;
    }
}
