package it.unina.datasetbuilder.integration;

public final class TravicCIEndpoints {

    private TravicCIEndpoints(){}

    public static final String GET_JOB = "https://api.travis-ci.org/jobs/{jobId}";
    public static final String GET_BUILDS = "https://api.travis-ci.org/repos/{slugName}/builds";
}
