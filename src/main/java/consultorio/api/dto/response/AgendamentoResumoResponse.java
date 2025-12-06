package consultorio.api.dto.response;

import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
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
    private String dentistaNome;
    private String pacienteNome;
    private StatusAgendamento status;
    private TipoProcedimento tipoProcedimento;
}