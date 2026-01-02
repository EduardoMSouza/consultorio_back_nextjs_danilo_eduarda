package consultorio.api.dto.response;

import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.AgendamentoHistorico.TipoAcao;
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
    private TipoAcao acao;
    private String acaoDescricao;
    private StatusAgendamento statusAnterior;
    private String statusAnteriorDescricao;
    private StatusAgendamento statusNovo;
    private String statusNovoDescricao;
    private String usuarioResponsavel;
    private String descricao;
    private String detalhes;
    private LocalDateTime dataHora;
    private String ipOrigem;
}