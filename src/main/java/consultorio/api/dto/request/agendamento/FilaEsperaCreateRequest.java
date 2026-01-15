// FilaEsperaCreateRequest.java
package consultorio.api.dto.request.agendamento;

import consultorio.domain.entity.agendamento.FilaEspera.PeriodoPreferencial;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilaEsperaCreateRequest {

    @NotNull(message = "Paciente é obrigatório")
    private Long pacienteId;

    private Long dentistaId;

    @NotNull(message = "Tipo de procedimento é obrigatório")
    private TipoProcedimento tipoProcedimento;

    private LocalDate dataPreferencial;

    private LocalTime horaInicioPreferencial;
    private LocalTime horaFimPreferencial;

    private PeriodoPreferencial periodoPreferencial;

    @Size(max = 500, message = "Observações deve ter no máximo 500 caracteres")
    private String observacoes;

    @Min(value = 1, message = "Prioridade deve ser no mínimo 1")
    @Max(value = 10, message = "Prioridade deve ser no máximo 10")
    private Integer prioridade = 5;

    private Boolean aceitaQualquerHorario = false;
    private Boolean aceitaQualquerDentista = false;

    private String criadoPor;
}