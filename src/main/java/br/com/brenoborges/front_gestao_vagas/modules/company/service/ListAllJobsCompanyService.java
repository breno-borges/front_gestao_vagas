package br.com.brenoborges.front_gestao_vagas.modules.company.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.company.dto.JobDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class ListAllJobsCompanyService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public List<JobDTO> execute(String token) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/company/job/");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ParameterizedTypeReference<List<JobDTO>> responseType = new ParameterizedTypeReference<List<JobDTO>>() {
        };

        try {
            var result = restTemplate.exchange(url, HttpMethod.GET, request,
                    responseType);

            return result.getBody();
        } catch (Unauthorized e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
