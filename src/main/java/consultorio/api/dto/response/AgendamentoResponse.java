package consultorio.api.dto.response;

import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
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

    // Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private String dentistaEspecialidade;

    // Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteCpf;

    // Agendamento
    private LocalDate dataConsulta;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Long duracaoMinutos;
    private StatusAgendamento status;
    private String statusDescricao;
    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;
    private String observacoes;
    private Double valorConsulta;

    // Flags
    private Boolean podeEditar;
    private Boolean podeCancelar;
    private Boolean consultaPassada;
    private Boolean hoje;

    // Confirmação/Lembrete
    private LocalDateTime confirmadoEm;
    private Boolean lembreteEnviado;
    private LocalDateTime lembreteEnviadoEm;

    // Cancelamento
    private String motivoCancelamento;
    private String canceladoPor;
    private LocalDateTime canceladoEm;

    // Auditoria
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
    private String atualizadoPor;
}