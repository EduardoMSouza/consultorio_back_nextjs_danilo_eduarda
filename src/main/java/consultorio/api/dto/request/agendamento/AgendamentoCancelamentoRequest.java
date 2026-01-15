// AgendamentoCancelamentoRequest.java
package consultorio.api.dto.request.agendamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoCancelamentoRequest {

    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(max = 500, message = "Motivo do cancelamento deve ter no máximo 500 caracteres")
    private String motivoCancelamento;

    @NotBlank(message = "Usuário que está cancelando é obrigatório")
    private String canceladoPor;
}