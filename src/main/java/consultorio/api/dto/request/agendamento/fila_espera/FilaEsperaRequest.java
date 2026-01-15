package consultorio.api.dto.request.agendamento.fila_espera;

import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    private Long dentistaId;

    private TipoProcedimento tipoProcedimento;

    private LocalDate dataPreferencial;

    private LocalTime horaInicioPreferencial;

    private LocalTime horaFimPreferencial;

    private FilaEspera.PeriodoPreferencial periodoPreferencial;

    @Size(max = 5000, message = "Observações não podem exceder 5000 caracteres")
    private String observacoes;

    private Integer prioridade;

    private Boolean aceitaQualquerHorario;

    private Boolean aceitaQualquerDentista;

    private String criadoPor;
}