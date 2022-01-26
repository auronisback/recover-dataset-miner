package it.unina.recoverminer.utilities.exceptions;

public class GitHubRateLimitException extends Exception {

    public GitHubRateLimitException(String message) {
        super(message);
    }
}
