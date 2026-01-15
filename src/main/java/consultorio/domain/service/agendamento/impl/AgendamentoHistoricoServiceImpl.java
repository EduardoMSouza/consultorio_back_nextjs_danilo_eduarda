package consultorio.domain.service.agendamento.impl;

import consultorio.api.dto.response.agendamento.historico.AgendamentoHistoricoResponse;
import consultorio.api.mapper.agendamento.AgendamentoHistoricoMapper;
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.repository.agendamento.AgendamentoHistoricoRepository;
import consultorio.domain.service.agendamento.AgendamentoHistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoHistoricoServiceImpl implements AgendamentoHistoricoService {

    private final AgendamentoHistoricoRepository historicoRepository;
    private final AgendamentoHistoricoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoHistoricoResponse> buscarPorAgendamento(Long agendamentoId) {
        return historicoRepository.findByAgendamentoIdOrderByDataHoraDesc(agendamentoId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoHistoricoResponse> buscarPorUsuario(String usuario) {
        return historicoRepository.findByUsuarioResponsavelOrderByDataHoraDesc(usuario).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoHistoricoResponse> buscarPorAcao(AgendamentoHistorico.TipoAcao acao) {
        return historicoRepository.findByAcaoOrderByDataHoraDesc(acao).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoHistoricoResponse> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return historicoRepository.findByPeriodo(inicio, fim).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}