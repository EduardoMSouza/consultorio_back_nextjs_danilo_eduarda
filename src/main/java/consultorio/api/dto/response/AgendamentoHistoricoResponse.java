package consultorio.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.AgendamentoHistorico.TipoAcao;

import java.time.LocalDateTime;

public record AgendamentoHistoricoResponse(
        Long id,
        Long agendamentoId,
        TipoAcao acao,
        String acaoDescricao,
        StatusAgendamento statusAnterior,
        StatusAgendamento statusNovo,
        String usuarioResponsavel,
        String descricao,
        String detalhes,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime dataHora,

        String ipOrigem
) {}