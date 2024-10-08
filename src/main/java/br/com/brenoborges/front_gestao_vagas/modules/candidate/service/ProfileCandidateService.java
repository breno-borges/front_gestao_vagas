package br.com.brenoborges.front_gestao_vagas.modules.candidate.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import br.com.brenoborges.front_gestao_vagas.modules.candidate.dto.ProfileUserDTO;
import br.com.brenoborges.front_gestao_vagas.utils.HostAPIGestaoVagas;

@Service
public class ProfileCandidateService {

    @Autowired
    HostAPIGestaoVagas hostAPIGestaoVagas;

    public ProfileUserDTO execute(String token) {

        String url = hostAPIGestaoVagas.getHostAPIGestaoVagas().concat("/candidate/");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(headers);

        try {
            var result = restTemplate.exchange(url, HttpMethod.GET, request,
                    ProfileUserDTO.class);
            return result.getBody();
        } catch (Unauthorized e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
    }
}
