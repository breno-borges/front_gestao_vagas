package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

@Service
public class FindJobService {

    public String execute(String token, String filter) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/candidate/job")
                .queryParam("filter", filter);

        try {
            var result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,
                    String.class);

            return result.getBody();
        } catch (Unauthorized e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
