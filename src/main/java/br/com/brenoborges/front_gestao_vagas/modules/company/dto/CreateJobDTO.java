package br.com.brenoborges.front_gestao_vagas.modules.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

    private String description;
    private String benefits;
    private String level;
}
