package it.unina.datasetbuilder.integration;

import it.unina.datasetbuilder.dto.BuildDTO;
import it.unina.datasetbuilder.dto.BuildsDTO;
import it.unina.datasetbuilder.dto.JobsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TraviCIApiInvoker {
    @Value("${travisKey}")
    private String travisCIKey;
    /*        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(TmdbUrlContants.SEARCH_MOVIE_URL)
                // Add query parameter
                .queryParam("api_key", TmdbApiConfig.getInstance().getApiKey())
                .queryParam("query", filter.getTitle());
restTemplate.getForEntity(builder.build().toUri(), MovieListResponse.class).getBody();
                */
    public JobsDTO getJob(String jobId){
        RestTemplate restTemplate = new RestTemplate();
        String getJobById = TravicCIEndpoints.GET_JOB;
        String urlBuilt = getJobById.replace("{jobId}", jobId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlBuilt);
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<JobsDTO> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, request, JobsDTO.class);
        return response.getBody();
    }

    public BuildsDTO getBuildsById(String slugName, String ids){
        RestTemplate restTemplate = new RestTemplate();
        String getJobById = TravicCIEndpoints.GET_BUILDS;
        String urlBuilt = getJobById.replace("{slugName}", slugName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlBuilt);
        builder.queryParam("ids",ids);
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, request, BuildsDTO.class).getBody();
    }

    public BuildsDTO getBuildsAfterNumber(String slugName, String afterNumber){
        RestTemplate restTemplate = new RestTemplate();
        String getJobById = TravicCIEndpoints.GET_BUILDS;
        String urlBuilt = getJobById.replace("{slugName}", slugName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlBuilt);
        builder.queryParam("after_number",afterNumber);
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        return restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, request, BuildsDTO.class).getBody();
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", travisCIKey);
        headers.add("Accept","application/vnd.travis-ci.2.1+json");
        return headers;
    }
}
