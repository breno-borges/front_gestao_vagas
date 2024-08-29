package br.com.brenoborges.front_gestao_vagas.modules.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.company.dto.CreateCompanyDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class CreateCompanyService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public void execute(CreateCompanyDTO company) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/company/");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCompanyDTO> request = new HttpEntity<>(company, headers);

        restTemplate.postForObject(url, request, String.class);

    }
}
