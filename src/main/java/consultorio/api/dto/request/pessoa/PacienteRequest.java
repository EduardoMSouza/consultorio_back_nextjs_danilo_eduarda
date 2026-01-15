package consultorio.api.dto.request.pessoa;

import consultorio.api.dto.request.pessoa.embedded.paciente.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteRequest {

    @Valid
    @NotNull(message = "Dados básicos são obrigatórios")
    private DadosBasicosRequest dadosBasicos;

    @Valid
    private ResponsavelRequest responsavel;

    @Valid
    private AnamneseRequest anamnese;

    @Valid
    private ConvenioRequest convenio;

    @Valid
    private InspecaoBucalRequest inspecaoBucal;

    @Valid
    private QuestionarioSaudeRequest questionarioSaude;
}
