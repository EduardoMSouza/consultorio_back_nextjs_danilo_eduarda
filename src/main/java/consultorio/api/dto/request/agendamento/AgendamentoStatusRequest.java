// AgendamentoStatusRequest.java
package consultorio.api.dto.request.agendamento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoStatusRequest {

    @NotBlank(message = "Usuário é obrigatório")
    private String usuario;
}