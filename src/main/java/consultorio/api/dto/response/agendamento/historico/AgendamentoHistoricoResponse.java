package consultorio.api.dto.response.agendamento.historico;

import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoHistoricoResponse {

    private Long id;
    private Long agendamentoId;
    private AgendamentoHistorico.TipoAcao acao;
    private StatusAgendamento statusAnterior;
    private StatusAgendamento statusNovo;
    private String usuarioResponsavel;
    private String descricao;
    private String detalhes;
    private LocalDateTime dataHora;
    private String ipOrigem;
}