package consultorio.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AgendamentoHistoricoService {

    List<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId);

    Page<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId, Pageable pageable);

    Page<AgendamentoHistoricoResponse> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    Page<AgendamentoHistoricoResponse> buscarPorUsuario(String usuario, Pageable pageable);

    AgendamentoHistoricoResponse buscarUltimo(Long agendamentoId);

    List<AgendamentoHistoricoResponse> buscarCancelamentos(LocalDateTime inicio, LocalDateTime fim);

    Map<String, Object> obterEstatisticas(LocalDateTime inicio, LocalDateTime fim);
}