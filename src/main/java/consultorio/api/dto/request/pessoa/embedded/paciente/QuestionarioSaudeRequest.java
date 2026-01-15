// QuestionarioSaudeRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionarioSaudeRequest {

    private Boolean sofreDoenca = false;

    @Size(max = 300, message = "Quais doenças deve ter no máximo 300 caracteres")
    private String sofreDoencaQuais;

    private Boolean tratamentoMedicoAtual = false;
    private Boolean gravidez = false;
    private Boolean usoMedicacao = false;

    @Size(max = 300, message = "Quais medicações deve ter no máximo 300 caracteres")
    private String usoMedicacaoQuais;

    @Size(max = 20, message = "Telefone do médico assistente deve ter no máximo 20 caracteres")
    private String medicoAssistenteTelefone;

    private Boolean teveAlergia = false;

    @Size(max = 300, message = "Quais alergias deve ter no máximo 300 caracteres")
    private String teveAlergiaQuais;

    private Boolean foiOperado = false;

    @Size(max = 300, message = "Quais operações deve ter no máximo 300 caracteres")
    private String foiOperadoQuais;

    private Boolean problemasCicatrizacao = false;
    private Boolean problemasAnestesia = false;
    private Boolean problemasHemorragia = false;

    @Size(max = 300, message = "Hábitos deve ter no máximo 300 caracteres")
    private String habitos;

    @Size(max = 500, message = "Antecedentes familiares deve ter no máximo 500 caracteres")
    private String antecedentesFamiliares;
}