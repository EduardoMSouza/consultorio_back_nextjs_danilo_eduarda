package consultorio.domain.service.impl;

import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.Agendamento.TipoProcedimento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.repository.AgendamentoRepository;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.repository.FilaEsperaRepository;
import consultorio.domain.repository.PacienteRepository;
import consultorio.domain.service.FilaEsperaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FilaEsperaServiceImpl implements FilaEsperaService {

    private final FilaEsperaRepository repository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final FilaEsperaMapper mapper;

    private static final int MAX_TENTATIVAS = 3;

    // ==================== CRUD ====================

    @Override
    @Transactional
    public FilaEsperaResponse criar(FilaEsperaRequest request) {
        Paciente paciente = findPaciente(request.getPacienteId());
        Dentista dentista = request.getDentistaId() != null ? findDentista(request.getDentistaId()) : null;

        validarDuplicidade(request.getPacienteId(), request.getDentistaId());

        if (dentista != null && !dentista.getAtivo()) {
            throw new BusinessException("Dentista está inativo");
        }

        FilaEspera fila = mapper.toEntity(request, paciente, dentista);
        fila.setCriadoPor(getUsuarioLogado());
        fila = repository.save(fila);

        log.info("Fila de espera criada: id={}, paciente={}", fila.getId(), paciente.getDadosBasicos().getNome());
        return mapper.toResponse(fila);
    }

    @Override
    public FilaEsperaResponse buscarPorId(Long id) {
        FilaEspera fila = findById(id);
        FilaEsperaResponse response = mapper.toResponse(fila);
        response.setPosicaoFila(calcularPosicao(id));
        return response;
    }

    @Override
    @Transactional
    public FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request) {
        FilaEspera fila = findById(id);

        if (!fila.isAtiva()) {
            throw new BusinessException("Não é possível atualizar fila inativa");
        }

        Paciente paciente = findPaciente(request.getPacienteId());
        Dentista dentista = request.getDentistaId() != null ? findDentista(request.getDentistaId()) : null;

        mapper.updateEntityFromRequest(request, fila, paciente, dentista);
        fila = repository.save(fila);

        log.info("Fila de espera atualizada: id={}", id);
        return mapper.toResponse(fila);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fila de espera não encontrada: " + id);
        }
        repository.deleteById(id);
        log.info("Fila de espera deletada: id={}", id);
    }

    // ==================== LISTAGENS ====================

    @Override
    public Page<FilaEsperaResponse> listarTodas(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<FilaEsperaResponse> listarAtivas(Pageable pageable) {
        return repository.findAllAtivas(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<FilaEsperaResponse> listarPorStatus(StatusFila status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(mapper::toResponse);
    }

    @Override
    public List<FilaEsperaResponse> listarAtivasPorDentista(Long dentistaId) {
        return mapper.toResponseList(repository.findAtivasByDentista(dentistaId));
    }

    @Override
    public List<FilaEsperaResponse> listarPorPaciente(Long pacienteId) {
        return mapper.toResponseList(repository.findByPaciente(pacienteId));
    }

    @Override
    public List<FilaEsperaResponse> listarAtivasPorPaciente(Long pacienteId) {
        return mapper.toResponseList(repository.findAtivasByPaciente(pacienteId));
    }

    // ==================== AÇÕES ====================

    @Override
    @Transactional
    public FilaEsperaResponse notificar(Long id) {
        FilaEspera fila = findById(id);

        if (!fila.isAtiva()) {
            throw new BusinessException("Fila não está ativa para notificação");
        }

        repository.marcarNotificado(id, LocalDateTime.now());

        // Aqui implementaria envio real (email, SMS)
        log.info("Paciente notificado: fila={}, paciente={}", id, fila.getPaciente().getDadosBasicos().getNome());

        return buscarPorId(id);
    }

    @Override
    @Transactional
    public FilaEsperaResponse cancelar(Long id) {
        FilaEspera fila = findById(id);

        if (!fila.isAtiva()) {
            throw new BusinessException("Fila já está finalizada");
        }

        fila.cancelar();
        repository.save(fila);

        log.info("Fila de espera cancelada: id={}", id);
        return mapper.toResponse(fila);
    }

    @Override
    @Transactional
    public FilaEsperaResponse converterEmAgendamento(Long filaId, Long agendamentoId) {
        FilaEspera fila = findById(filaId);

        if (!fila.isAtiva()) {
            throw new BusinessException("Fila não está ativa para conversão");
        }

        Agendamento agendamento = agendamentoRepository.findByIdAtivo(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + agendamentoId));

        fila.converterEmAgendamento(agendamento);
        repository.save(fila);

        log.info("Fila convertida em agendamento: fila={}, agendamento={}", filaId, agendamentoId);
        return mapper.toResponse(fila);
    }

    @Override
    @Transactional
    public void incrementarTentativaContato(Long id) {
        repository.incrementarTentativa(id, LocalDateTime.now());
        log.debug("Tentativa de contato incrementada: fila={}", id);
    }

    // ==================== PROCESSAMENTO AUTOMÁTICO ====================

    @Override
    @Transactional
    public void processarFilaAposCancelamento(Agendamento agendamento) {
        log.info("Processando fila após cancelamento: agendamento={}", agendamento.getId());

        List<FilaEspera> compativeis = repository.findCompatíveisParaData(
                agendamento.getDentista().getId(),
                agendamento.getDataConsulta()
        );

        if (!compativeis.isEmpty()) {
            FilaEspera primeira = compativeis.get(0);
            if (primeira.getStatus() == StatusFila.AGUARDANDO) {
                try {
                    notificar(primeira.getId());
                    log.info("Paciente da fila notificado sobre horário disponível: {}", primeira.getId());
                } catch (Exception e) {
                    log.error("Erro ao notificar paciente da fila: {}", primeira.getId(), e);
                }
            }
        }
    }

    @Override
    @Transactional
    public void processarFilaAposConclusao(Agendamento agendamento) {
        log.debug("Processando fila após conclusão: agendamento={}", agendamento.getId());
        // Pode implementar lógica específica se necessário
    }

    @Override
    @Transactional
    public int expirarFilasVencidas() {
        List<FilaEspera> expiradas = repository.findExpiradas(LocalDate.now());

        if (expiradas.isEmpty()) {
            return 0;
        }

        List<Long> ids = expiradas.stream().map(FilaEspera::getId).collect(Collectors.toList());
        int total = repository.expirarEmLote(ids, LocalDateTime.now());

        log.info("Filas expiradas: {}", total);
        return total;
    }

    @Override
    @Transactional
    public int enviarNotificacoesPendentes() {
        List<FilaEspera> pendentes = repository.findPendentesNotificacao();
        int enviados = 0;

        for (FilaEspera fila : pendentes) {
            if (fila.getTentativasContato() < MAX_TENTATIVAS) {
                try {
                    notificar(fila.getId());
                    enviados++;
                } catch (Exception e) {
                    log.error("Erro ao notificar fila: {}", fila.getId(), e);
                    incrementarTentativaContato(fila.getId());
                }
            } else {
                log.warn("Fila atingiu máximo de tentativas: {}", fila.getId());
            }
        }

        log.info("Notificações enviadas: {}", enviados);
        return enviados;
    }

    // ==================== COMPATÍVEIS ====================

    @Override
    public List<FilaEsperaResponse> buscarCompativeis(Long dentistaId, LocalDate data, TipoProcedimento tipoProcedimento) {
        return mapper.toResponseList(repository.findCompativeis(dentistaId, data, tipoProcedimento));
    }

    @Override
    public List<FilaEsperaResponse> buscarCompatíveisPorDentista(Long dentistaId) {
        return mapper.toResponseList(repository.findCompatíveisPorDentista(dentistaId));
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalAtivas", repository.countAtivas());

        stats.put("porStatus", repository.countPorStatus().stream()
                .collect(Collectors.toMap(r -> ((StatusFila) r[0]).name(), r -> r[1])));

        stats.put("porDentista", repository.countAtivasPorDentista().stream()
                .map(r -> Map.of("id", r[0], "nome", r[1], "total", r[2]))
                .collect(Collectors.toList()));

        return stats;
    }

    @Override
    public long contarAtivas() {
        return repository.countAtivas();
    }

    @Override
    public long contarAtivasPorDentista(Long dentistaId) {
        return repository.countAtivasByDentista(dentistaId);
    }

    @Override
    public long contarAtivasPorPaciente(Long pacienteId) {
        return repository.countAtivasByPaciente(pacienteId);
    }

    @Override
    public int calcularPosicao(Long filaId) {
        FilaEspera fila = findById(filaId);

        if (!fila.isAtiva()) {
            return 0;
        }

        if (fila.getDentista() != null) {
            return repository.calcularPosicaoPorDentista(
                    fila.getDentista().getId(),
                    fila.getPrioridade(),
                    fila.getCriadoEm()
            );
        }

        return repository.calcularPosicao(fila.getPrioridade(), fila.getCriadoEm());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private FilaEspera findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fila de espera não encontrada: " + id));
    }

    private Paciente findPaciente(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + id));
    }

    private Dentista findDentista(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado: " + id));
    }

    private void validarDuplicidade(Long pacienteId, Long dentistaId) {
        boolean existe = dentistaId != null
                ? repository.existsAtivaPorPacienteEDentista(pacienteId, dentistaId)
                : repository.existsAtivaPorPaciente(pacienteId);

        if (existe) {
            throw new BusinessException("Paciente já possui fila de espera ativa" +
                    (dentistaId != null ? " para este dentista" : ""));
        }
    }

    private String getUsuarioLogado() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "sistema";
        }
    }
}