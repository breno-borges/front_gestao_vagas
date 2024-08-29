package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class ApplyJobService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public String execute(String token, UUID jobId) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/candidate/job/apply");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<UUID> request = new HttpEntity<>(jobId, headers);

        String result = restTemplate.postForObject(url, request, String.class);

        return result;
    }
}
