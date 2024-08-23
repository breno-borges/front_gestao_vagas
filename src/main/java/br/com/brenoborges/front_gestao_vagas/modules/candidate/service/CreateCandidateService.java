package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.CreateCandidateDTO;

@Service
public class CreateCandidateService {

    public void execute(CreateCandidateDTO candidate) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCandidateDTO> request = new HttpEntity<>(candidate, headers);

        restTemplate.postForObject("http://localhost:8080/candidate/", request, String.class);

    }
}
