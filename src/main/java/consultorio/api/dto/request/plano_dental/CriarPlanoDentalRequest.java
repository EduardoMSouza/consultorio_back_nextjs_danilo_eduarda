package consultorio.api.dto.request.plano_dental;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriarPlanoDentalRequest {

    @NotNull(message = "Paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @NotNull(message = "Dentista é obrigatório")
    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistaId;

    @NotBlank(message = "Dente é obrigatório")
    @Size(max = 2, message = "Dente deve ter no máximo 2 caracteres")
    @Pattern(regexp = "^[0-9]{1,2}$", message = "Dente deve conter apenas números")
    private String dente;

    @NotBlank(message = "Procedimento é obrigatório")
    @Size(max = 200, message = "Procedimento deve ter no máximo 200 caracteres")
    private String procedimento;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @DecimalMin(value = "0.0", inclusive = true, message = "Valor final não pode ser negativo")
    @Digits(integer = 8, fraction = 2, message = "Valor final deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valorFinal;

    @Size(max = 1000, message = "Observações devem ter no máximo 1000 caracteres")
    private String observacoes;
}