package consultorio.api.dto.request;

import consultorio.domain.entity.PlanoDental.StatusPlano;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PlanoDentalRequest(
        @NotNull(message = "ID do paciente é obrigatório")
        Long pacienteId,

        @NotNull(message = "ID do dentista é obrigatório")
        Long dentistaId,

        @NotBlank(message = "Dente é obrigatório")
        @Size(max = 10, message = "Dente deve ter no máximo 10 caracteres")
        String dente,

        @NotBlank(message = "Procedimento é obrigatório")
        @Size(max = 200, message = "Procedimento deve ter no máximo 200 caracteres")
        String procedimento,

        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
        BigDecimal valor,

        StatusPlano status,

        @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
        String observacoes
) {}
