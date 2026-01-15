package consultorio.api.dto.request.tratamento;

import consultorio.domain.entity.tratamento.enums.StatusPlano;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlanoDentalRequest(
        @NotNull(message = "ID do paciente é obrigatório")
        @Positive(message = "ID do paciente deve ser positivo")
        Long pacienteId,

        @NotNull(message = "ID do dentista é obrigatório")
        @Positive(message = "ID do dentista deve ser positivo")
        Long dentistaId,

        @NotBlank(message = "Dente é obrigatório")
        @Size(min = 1, max = 10, message = "Dente deve ter entre 1 e 10 caracteres")
        @Pattern(regexp = "^[0-9]{1,2}[A-Za-z]?$", message = "Formato de dente inválido. Ex: 11, 21A")
        String dente,

        @NotBlank(message = "Procedimento é obrigatório")
        @Size(min = 3, max = 200, message = "Procedimento deve ter entre 3 e 200 caracteres")
        String procedimento,

        @Size(max = 20, message = "Código do procedimento deve ter no máximo 20 caracteres")
        String codigoProcedimento,

        @Size(max = 10, message = "Face do dente deve ter no máximo 10 caracteres")
        String faceDente,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 10 dígitos inteiros e 2 decimais")
        BigDecimal valor,

        @DecimalMin(value = "0.00", message = "Desconto não pode ser negativo")
        @Digits(integer = 10, fraction = 2, message = "Desconto deve ter no máximo 10 dígitos inteiros e 2 decimais")
        BigDecimal valorDesconto,

        StatusPlano status,

        @Size(max = 50, message = "Prioridade deve ter no máximo 50 caracteres")
        String prioridade,

        @NotNull(message = "Campo urgente é obrigatório")
        Boolean urgente,

        @Size(max = 2000, message = "Observações devem ter no máximo 2000 caracteres")
        String observacoes,

        LocalDateTime dataPrevista
) {
    public PlanoDentalRequest {
        if (valorDesconto == null) {
            valorDesconto = BigDecimal.ZERO;
        }
        if (status == null) {
            status = StatusPlano.PENDENTE;
        }
        if (prioridade == null) {
            prioridade = "NORMAL";
        }
        if (urgente == null) {
            urgente = false;
        }
        if (dente != null) {
            dente = dente.trim().toUpperCase();
        }
        if (faceDente != null) {
            faceDente = faceDente.trim().toUpperCase();
        }
    }

    /**
     * Calcula o valor final considerando o desconto.
     */
    public BigDecimal getValorFinal() {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal desconto = valorDesconto != null ? valorDesconto : BigDecimal.ZERO;
        BigDecimal valorFinal = valor.subtract(desconto);
        return valorFinal.compareTo(BigDecimal.ZERO) >= 0 ? valorFinal : BigDecimal.ZERO;
    }
}