package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long dentistaId;
    private String dentistaNome;

    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataConsulta;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFim;

    private Long duracaoMinutos;

    private StatusAgendamento status;
    private String statusDescricao;

    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;

    private Boolean isHoje;
    private Boolean isPassado;
    private Boolean lembreteEnviado;
}