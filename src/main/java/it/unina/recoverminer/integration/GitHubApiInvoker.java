package it.unina.recoverminer.integration;

import it.unina.recoverminer.dto.RepositoryDTO;
import it.unina.recoverminer.utilities.exceptions.GitHubRateLimitException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GitHubApiInvoker {

    @Value("${github.user}")
    private String user;
    @Value("${github.pwd}")
    private String pwd;

    public RepositoryDTO getRepoBySlugName(String slugName) throws GitHubRateLimitException {
        RestTemplate restTemplate = new RestTemplate();
        String getRepositoryBySlugName = GitHubEndpoints.GET_REPOSITORY_BY_SLUG_NAME;
        String urlBuilt = getRepositoryBySlugName.replace("{slugname}", slugName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlBuilt);
        HttpHeaders headers= getHeaders();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        try {
            ResponseEntity<RepositoryDTO> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, request, RepositoryDTO.class);
            if (HttpStatus.FORBIDDEN.equals(response.getStatusCode())) {
                throw new GitHubRateLimitException("Rate limit exceeded: 60 per hour, please retry after 1 hour");
            }
            return response.getBody();
        }catch(HttpClientErrorException e){
            throw new GitHubRateLimitException("Rate limit exceeded: 60 per hour, please retry after 1 hour");
        }
    }

    private HttpHeaders getHeaders(){
        String plainCreds = user + ":"+ pwd;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }
}
