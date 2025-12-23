package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteEmail;

    private Long dentistaId;
    private String dentistaNome;

    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPreferencial;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicioPreferencial;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFimPreferencial;

    private PeriodoPreferencial periodoPreferencial;
    private String periodoPreferencialDescricao;

    private StatusFila status;
    private String statusDescricao;

    private String observacoes;
    private Integer prioridade;
    private Boolean aceitaQualquerHorario;
    private Boolean aceitaQualquerDentista;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime atualizadoEm;

    private String criadoPor;

    private Long agendamentoId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime convertidoEm;

    private Boolean notificado;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime notificadoEm;

    private Integer tentativasContato;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ultimaTentativaContato;

    private Boolean isAtiva;
    private Boolean isExpirada;
    private Integer posicaoFila;
}