package consultorio.domain.service.impl;

<<<<<<< HEAD
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.entity.agendamento.AgendamentoHistorico.TipoAcao;
=======
import consultorio.api.dto.response.AgendamentoHistoricoResponse;
import consultorio.api.mapper.AgendamentoHistoricoMapper;
import consultorio.domain.entity.AgendamentoHistorico;
import consultorio.domain.entity.AgendamentoHistorico.TipoAcao;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import consultorio.domain.repository.AgendamentoHistoricoRepository;
import consultorio.domain.service.AgendamentoHistoricoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgendamentoHistoricoServiceImpl implements AgendamentoHistoricoService {

    private final AgendamentoHistoricoRepository repository;
    private final AgendamentoHistoricoMapper mapper;

    @Override
    public List<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId) {
        return mapper.toResponseList(repository.findByAgendamentoIdOrderByDataHoraDesc(agendamentoId));
    }

    @Override
    public Page<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId, Pageable pageable) {
        return repository.findByAgendamentoId(agendamentoId, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<AgendamentoHistoricoResponse> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        return repository.findByPeriodo(inicio, fim, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<AgendamentoHistoricoResponse> buscarPorUsuario(String usuario, Pageable pageable) {
        return repository.findByUsuario(usuario, pageable).map(mapper::toResponse);
    }

    @Override
    public AgendamentoHistoricoResponse buscarUltimo(Long agendamentoId) {
        AgendamentoHistorico ultimo = repository.findUltimoByAgendamento(agendamentoId);
        return ultimo != null ? mapper.toResponse(ultimo) : null;
    }

    @Override
    public List<AgendamentoHistoricoResponse> buscarCancelamentos(LocalDateTime inicio, LocalDateTime fim) {
        return mapper.toResponseList(repository.findCancelamentosNoPeriodo(inicio, fim));
    }

    @Override
    public Map<String, Object> obterEstatisticas(LocalDateTime inicio, LocalDateTime fim) {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("porAcao", repository.countPorAcaoNoPeriodo(inicio, fim).stream()
                .collect(Collectors.toMap(r -> ((TipoAcao) r[0]).name(), r -> r[1])));

        stats.put("porUsuario", repository.countPorUsuarioNoPeriodo(inicio, fim).stream()
                .map(r -> Map.of("usuario", r[0], "total", r[1]))
                .collect(Collectors.toList()));

        return stats;
    }
}