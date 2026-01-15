package consultorio.api.dto.response.agendamento.fila_espera;

import consultorio.domain.entity.agendamento.FilaEspera;
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
public class FilaEsperaResponse {

    private Long id;
    private Long pacienteId;
    private String nomePaciente;
    private Long dentistaId;
    private String nomeDentista;
    private TipoProcedimento tipoProcedimento;
    private LocalDate dataPreferencial;
    private LocalTime horaInicioPreferencial;
    private LocalTime horaFimPreferencial;
    private FilaEspera.PeriodoPreferencial periodoPreferencial;
    private FilaEspera.StatusFila status;
    private String observacoes;
    private Integer prioridade;
    private Boolean aceitaQualquerHorario;
    private Boolean aceitaQualquerDentista;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
    private Long agendamentoId;
    private LocalDateTime convertidoEm;
    private Boolean notificado;
    private LocalDateTime notificadoEm;
    private Integer tentativasContato;
    private LocalDateTime ultimaTentativaContato;
    private Boolean ativa;
    private Boolean expirado;
}