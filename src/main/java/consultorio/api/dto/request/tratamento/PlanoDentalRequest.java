package consultorio.api.dto.request.tratamento;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDentalRequest {

    @NotNull(message = "ID do paciente é obrigatório")
    @Positive(message = "ID do paciente deve ser positivo")
    private Long pacienteId;

    @NotNull(message = "ID do dentista é obrigatório")
    @Positive(message = "ID do dentista deve ser positivo")
    private Long dentistaId;

    @NotBlank(message = "Dente é obrigatório")
    @Size(min = 1, max = 10, message = "Dente deve ter entre 1 e 10 caracteres")
    @Pattern(regexp = "^[0-9]{1,2}[A-Za-z]?$", message = "Formato de dente inválido. Ex: 11, 21A")
    private String dente;

    @Size(max = 10, message = "Face do dente deve ter no máximo 10 caracteres")
    private String faceDente;

    @NotBlank(message = "Procedimento é obrigatório")
    @Size(min = 3, max = 200, message = "Procedimento deve ter entre 3 e 200 caracteres")
    private String procedimento;

    @Size(max = 20, message = "Código do procedimento deve ter no máximo 20 caracteres")
    private String codigoProcedimento;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @DecimalMin(value = "0.00", message = "Desconto não pode ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Desconto deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal valorDesconto;

    private StatusPlano status;

    @Size(max = 50, message = "Prioridade deve ter no máximo 50 caracteres")
    private String prioridade;

    private Boolean urgente;

    @Size(max = 2000, message = "Observações devem ter no máximo 2000 caracteres")
    private String observacoes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPrevista;

}