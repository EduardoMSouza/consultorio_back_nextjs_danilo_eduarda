package consultorio.api.dto.request;

import consultorio.domain.entity.Agendamento.TipoProcedimento;
import consultorio.domain.entity.FilaEspera.PeriodoPreferencial;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class FilaEsperaRequest {

    @NotNull(message = "Paciente é obrigatório")
    private Long pacienteId;

    private Long dentistaId;

    private TipoProcedimento tipoProcedimento;

    private LocalDate dataPreferencial;

    private LocalTime horaInicioPreferencial;

    private LocalTime horaFimPreferencial;

    private PeriodoPreferencial periodoPreferencial;

    private String observacoes;

    @Min(value = 0, message = "Prioridade mínima é 0")
    @Max(value = 10, message = "Prioridade máxima é 10")
    private Integer prioridade = 0;

    private Boolean aceitaQualquerHorario = false;

    private Boolean aceitaQualquerDentista = false;
}