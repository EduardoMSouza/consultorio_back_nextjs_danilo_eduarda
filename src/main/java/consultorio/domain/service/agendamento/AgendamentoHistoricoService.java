package consultorio.domain.service.agendamento;

import consultorio.api.dto.response.agendamento.historico.AgendamentoHistoricoResponse;
import consultorio.domain.entity.agendamento.AgendamentoHistorico;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoHistoricoService {

    List<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId);

    List<AgendamentoHistoricoResponse> buscarPorUsuario(String usuario);

    List<AgendamentoHistoricoResponse> buscarPorAcao(AgendamentoHistorico.TipoAcao acao);

    List<AgendamentoHistoricoResponse> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}