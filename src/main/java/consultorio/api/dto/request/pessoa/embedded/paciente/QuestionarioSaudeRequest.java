package consultorio.api.dto.request.pessoa.embedded.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionarioSaudeRequest {

    private Boolean sofreDoenca;
    private String sofreDoencaQuais;
    private Boolean tratamentoMedicoAtual;
    private Boolean gravidez;
    private Boolean usoMedicacao;
    private String usoMedicacaoQuais;
    private String medicoAssistenteTelefone;
    private Boolean teveAlergia;
    private String teveAlergiaQuais;
    private Boolean foiOperado;
    private String foiOperadoQuais;
    private Boolean problemasCicatrizacao;
    private Boolean problemasAnestesia;
    private Boolean problemasHemorragia;
    private String habitos;
    private String antecedentesFamiliares;
}

