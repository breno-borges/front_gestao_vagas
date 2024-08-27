package br.com.brenoborges.front_gestao_vagas.modules.company.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.company.dto.CreateCompanyDTO;

@Service
public class CreateCompanyService {
    public void execute(CreateCompanyDTO company) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCompanyDTO> request = new HttpEntity<>(company, headers);

        restTemplate.postForObject("http://localhost:8080/company/", request, String.class);

    }
}
