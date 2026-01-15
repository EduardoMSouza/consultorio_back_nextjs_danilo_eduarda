// PacienteResponse.java
package consultorio.api.dto.response.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.api.dto.response.pessoa.embeddable.paciente.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {

    private Long id;
    private DadosBasicosResponse dadosBasicos;
    private ResponsavelResponse responsavel;
    private AnamneseResponse anamnese;
    private ConvenioResponse convenio;
    private InspecaoBucalResponse inspecaoBucal;
    private QuestionarioSaudeResponse questionarioSaude;
    private String observacoes;
    private Boolean ativo;
    private Integer idade;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;


}