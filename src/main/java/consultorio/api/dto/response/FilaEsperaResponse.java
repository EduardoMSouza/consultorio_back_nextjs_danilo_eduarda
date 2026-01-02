package consultorio.api.dto.response;

import consultorio.domain.entity.Agendamento.TipoProcedimento;
import consultorio.domain.entity.FilaEspera.PeriodoPreferencial;
import consultorio.domain.entity.FilaEspera.StatusFila;
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
public class FilaEsperaResponse {

    private Long id;

    // Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;

    // Dentista
    private Long dentistaId;
    private String dentistaNome;

    // Preferências
    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;
    private LocalDate dataPreferencial;
    private LocalTime horaInicioPreferencial;
    private LocalTime horaFimPreferencial;
    private PeriodoPreferencial periodoPreferencial;
    private String periodoPreferencialDescricao;

    // Status
    private StatusFila status;
    private String statusDescricao;
    private String observacoes;
    private Integer prioridade;
    private Integer posicaoFila;

    // Flags
    private Boolean aceitaQualquerHorario;
    private Boolean aceitaQualquerDentista;
    private Boolean ativa;
    private Boolean expirada;

    // Notificação
    private Boolean notificado;
    private LocalDateTime notificadoEm;
    private Integer tentativasContato;
    private LocalDateTime ultimaTentativaContato;

    // Conversão
    private Long agendamentoId;
    private LocalDateTime convertidoEm;

    // Auditoria
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
}