package consultorio.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelamentoRequest {

    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(max = 500, message = "Motivo deve ter no máximo 500 caracteres")
    private String motivo;
}
