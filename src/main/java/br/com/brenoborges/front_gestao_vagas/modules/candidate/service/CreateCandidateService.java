package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.CreateCandidateDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class CreateCandidateService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public void execute(CreateCandidateDTO candidate) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/candidate/");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCandidateDTO> request = new HttpEntity<>(candidate, headers);

        restTemplate.postForObject(url, request, String.class);

    }
}
