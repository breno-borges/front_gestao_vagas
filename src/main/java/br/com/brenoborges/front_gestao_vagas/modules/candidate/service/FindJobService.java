package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.JobDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

import org.springframework.web.client.RestTemplate;

@Service
public class FindJobService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public List<JobDTO> execute(String token, String filter) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/candidate/job");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url)
                .queryParam("filter", filter);

        ParameterizedTypeReference<List<JobDTO>> responseType = new ParameterizedTypeReference<List<JobDTO>>() {
        };

        try {
            var result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,
                    responseType);

            return result.getBody();
        } catch (Unauthorized e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
