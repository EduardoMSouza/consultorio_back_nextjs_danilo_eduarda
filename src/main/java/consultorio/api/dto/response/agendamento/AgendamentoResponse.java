package consultorio.api.dto.response.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponse {

    private Long id;
    private Long dentistaId;
    private String nomeDentista;
    private Long pacienteId;
    private String nomePaciente;
    private LocalDate dataConsulta;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private StatusAgendamento status;
    private TipoProcedimento tipoProcedimento;
    private String observacoes;
    private Double valorConsulta;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
    private String atualizadoPor;
    private String canceladoPor;
    private LocalDateTime canceladoEm;
    private String motivoCancelamento;
    private LocalDateTime confirmadoEm;
    private Boolean lembreteEnviado;
    private LocalDateTime lembreteEnviadoEm;
    private Long duracaoEmMinutos;
    private Boolean podeSerEditado;
    private Boolean podeSerCancelado;
    private Boolean finalizado;
    private Boolean consultaPassada;
    private Boolean hoje;
}