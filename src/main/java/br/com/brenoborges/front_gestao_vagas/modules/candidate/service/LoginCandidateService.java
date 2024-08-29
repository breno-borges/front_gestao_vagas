package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.Token;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class LoginCandidateService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public Token login(String username, String password) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/candidate/auth");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(data, headers);

        Token result = restTemplate.postForObject(url, request, Token.class);

        return result;
    }
}
