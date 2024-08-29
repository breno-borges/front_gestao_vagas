package br.com.brenoborges.front_gestao_vagas.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HostAPIGestaoVagas {

    private String hostAPIGestaoVagas;

    @Value("${host.api.gestao.vagas}")
    public void setHostAPIGestaoVagas(String hostAPIGestaoVagas) {
        this.hostAPIGestaoVagas = hostAPIGestaoVagas;
    }

    public String getHostAPIGestaoVagas() {
        return hostAPIGestaoVagas;
    }
}