package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    // Dados do Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private String dentistaEspecialidade;

    // Dados do Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;
    private String pacienteProntuario;

    // Dados do Agendamento
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

    private String observacoes;
    private Double valorConsulta;

    // Flags
    private Boolean ativo;
    private Boolean podeSerEditado;
    private Boolean podeSerCancelado;
    private Boolean isFinalizado;
    private Boolean isHoje;
    private Boolean isPassado;
    private Boolean lembreteEnviado;

    // Auditoria
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;

    private String criadoPor;
    private String atualizadoPor;

    // Confirmação
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmadoEm;

    // Cancelamento
    private String canceladoPor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceladoEm;

    private String motivoCancelamento;
}