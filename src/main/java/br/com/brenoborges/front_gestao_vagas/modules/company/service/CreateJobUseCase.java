package br.com.brenoborges.front_gestao_vagas.modules.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class CreateJobUseCase {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public String execute(String token, CreateJobDTO job) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/company/job/");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<CreateJobDTO> request = new HttpEntity<>(job, headers);

        return restTemplate.postForObject(url, request,
                String.class);
    }
}
