package it.unina.datasetbuilder.utilities.exceptions;

public class GitHubRateLimitException extends Exception {

    public GitHubRateLimitException(String message) {
        super(message);
    }
}
