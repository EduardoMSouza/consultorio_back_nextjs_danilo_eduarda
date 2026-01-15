package consultorio.api.dto.response.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
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
public class AgendamentoResumoResponse {

    private Long id;
    private LocalDate dataConsulta;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Long duracaoMinutos;

    private Long dentistaId;
    private String dentistaNome;

    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;

    private StatusAgendamento status;
    private String statusDescricao;
    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;

    private Boolean confirmado;
    private Boolean hoje;
}
