package it.unina.recoverminer.dto;

public class CommitDTO {
    private String sha;

    public CommitDTO(String sha) {
        this.sha = sha;
    }

    public CommitDTO() {
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }
}
