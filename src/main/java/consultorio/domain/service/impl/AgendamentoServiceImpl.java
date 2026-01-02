package consultorio.domain.service.impl;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.AgendamentoMapper;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.AgendamentoHistorico;
import consultorio.domain.entity.AgendamentoHistorico.TipoAcao;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.Paciente;
import consultorio.domain.repository.AgendamentoHistoricoRepository;
import consultorio.domain.repository.AgendamentoRepository;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.repository.PacienteRepository;
import consultorio.domain.service.AgendamentoService;
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
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoHistoricoRepository historicoRepository;
    private final DentistaRepository dentistaRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper mapper;
    private final FilaEsperaService filaEsperaService;

    private static final LocalTime HORARIO_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORARIO_FIM = LocalTime.of(18, 0);
    private static final int INTERVALO_MINUTOS = 30;

    // ==================== CRUD ====================

    @Override
    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Dentista dentista = findDentista(request.getDentistaId());
        Paciente paciente = findPaciente(request.getPacienteId());

        validarDentistaAtivo(dentista);
        validarHorario(request.getHoraInicio(), request.getHoraFim());
        validarDataFutura(request.getDataConsulta());
        validarConflitoDentista(request.getDentistaId(), request.getDataConsulta(), request.getHoraInicio(), request.getHoraFim(), null);
        validarConflitoPaciente(request.getPacienteId(), request.getDataConsulta(), request.getHoraInicio(), request.getHoraFim());

        Agendamento agendamento = mapper.toEntity(request, dentista, paciente);
        agendamento.setCriadoPor(getUsuarioLogado());
        agendamento = repository.save(agendamento);

        registrarHistorico(agendamento.getId(), TipoAcao.CRIACAO, null, StatusAgendamento.AGENDADO, "Agendamento criado");

        log.info("Agendamento criado: id={}, dentista={}, paciente={}, data={}",
                agendamento.getId(), dentista.getNome(), paciente.getDadosBasicos().getNome(), request.getDataConsulta());

        return mapper.toResponse(agendamento);
    }

    @Override
    public AgendamentoResponse buscarPorId(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = findById(id);

        if (!agendamento.isPodeSerEditado()) {
            throw new BusinessException("Agendamento não pode ser editado no status atual");
        }

        Dentista dentista = findDentista(request.getDentistaId());
        Paciente paciente = findPaciente(request.getPacienteId());

        validarDentistaAtivo(dentista);
        validarHorario(request.getHoraInicio(), request.getHoraFim());
        validarConflitoDentista(request.getDentistaId(), request.getDataConsulta(), request.getHoraInicio(), request.getHoraFim(), id);

        mapper.updateEntityFromRequest(request, agendamento, dentista, paciente);
        agendamento.setAtualizadoPor(getUsuarioLogado());
        agendamento = repository.save(agendamento);

        registrarHistorico(id, TipoAcao.ATUALIZACAO, null, null, "Agendamento atualizado");

        log.info("Agendamento atualizado: id={}", id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = findById(id);
        repository.desativar(id, LocalDateTime.now());
        registrarHistorico(id, TipoAcao.EXCLUSAO, agendamento.getStatus(), null, "Agendamento excluído");
        log.info("Agendamento deletado: id={}", id);
    }

    // ==================== LISTAGENS ====================

    @Override
    public Page<AgendamentoResumoResponse> listarTodos(Pageable pageable) {
        return repository.findAllAtivos(pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<AgendamentoResumoResponse> listarPorDentista(Long dentistaId, Pageable pageable) {
        return repository.findByDentistaId(dentistaId, pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<AgendamentoResumoResponse> listarPorPaciente(Long pacienteId, Pageable pageable) {
        return repository.findByPacienteId(pacienteId, pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<AgendamentoResumoResponse> listarPorStatus(StatusAgendamento status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<AgendamentoResumoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable) {
        return repository.findByPeriodo(inicio, fim, pageable).map(mapper::toResumoResponse);
    }

    // ==================== AGENDA ====================

    @Override
    public List<AgendamentoResumoResponse> buscarAgendaDoDia(Long dentistaId, LocalDate data) {
        return mapper.toResumoResponseList(repository.findAgendaDoDia(dentistaId, data));
    }

    @Override
    public List<AgendamentoResumoResponse> buscarAgendaDoDia(LocalDate data) {
        return mapper.toResumoResponseList(repository.findAllAgendaDoDia(data));
    }

    @Override
    public List<AgendamentoResumoResponse> buscarProximosPaciente(Long pacienteId) {
        return mapper.toResumoResponseList(repository.findProximosPaciente(pacienteId, LocalDate.now()));
    }

    @Override
    public List<AgendamentoResumoResponse> buscarProximosDentista(Long dentistaId) {
        return mapper.toResumoResponseList(repository.findProximosDentista(dentistaId, LocalDate.now()));
    }

    // ==================== MUDANÇA DE STATUS ====================

    @Override
    @Transactional
    public AgendamentoResponse confirmar(Long id) {
        Agendamento agendamento = findById(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        if (statusAnterior != StatusAgendamento.AGENDADO) {
            throw new BusinessException("Apenas agendamentos com status AGENDADO podem ser confirmados");
        }

        agendamento.confirmar(getUsuarioLogado());
        repository.save(agendamento);
        registrarHistorico(id, TipoAcao.CONFIRMACAO, statusAnterior, StatusAgendamento.CONFIRMADO, "Agendamento confirmado");

        log.info("Agendamento confirmado: id={}", id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse iniciarAtendimento(Long id) {
        Agendamento agendamento = findById(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        if (statusAnterior != StatusAgendamento.AGENDADO && statusAnterior != StatusAgendamento.CONFIRMADO) {
            throw new BusinessException("Apenas agendamentos AGENDADO ou CONFIRMADO podem iniciar atendimento");
        }

        agendamento.iniciarAtendimento(getUsuarioLogado());
        repository.save(agendamento);
        registrarHistorico(id, TipoAcao.INICIO_ATENDIMENTO, statusAnterior, StatusAgendamento.EM_ATENDIMENTO, "Atendimento iniciado");

        log.info("Atendimento iniciado: id={}", id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse concluir(Long id) {
        Agendamento agendamento = findById(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        if (statusAnterior != StatusAgendamento.EM_ATENDIMENTO) {
            throw new BusinessException("Apenas agendamentos EM_ATENDIMENTO podem ser concluídos");
        }

        agendamento.concluir(getUsuarioLogado());
        repository.save(agendamento);
        registrarHistorico(id, TipoAcao.CONCLUSAO, statusAnterior, StatusAgendamento.CONCLUIDO, "Atendimento concluído");

        filaEsperaService.processarFilaAposConclusao(agendamento);

        log.info("Agendamento concluído: id={}", id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse cancelar(Long id, String motivo) {
        Agendamento agendamento = findById(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        if (!agendamento.isPodeSerCancelado()) {
            throw new BusinessException("Agendamento não pode ser cancelado no status atual");
        }

        agendamento.cancelar(motivo, getUsuarioLogado());
        repository.save(agendamento);
        registrarHistorico(id, TipoAcao.CANCELAMENTO, statusAnterior, StatusAgendamento.CANCELADO, "Cancelado: " + motivo);

        filaEsperaService.processarFilaAposCancelamento(agendamento);

        log.info("Agendamento cancelado: id={}, motivo={}", id, motivo);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse marcarFalta(Long id) {
        Agendamento agendamento = findById(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        if (statusAnterior == StatusAgendamento.CONCLUIDO || statusAnterior == StatusAgendamento.CANCELADO) {
            throw new BusinessException("Não é possível marcar falta para este agendamento");
        }

        agendamento.marcarFalta(getUsuarioLogado());
        repository.save(agendamento);
        registrarHistorico(id, TipoAcao.FALTA, statusAnterior, StatusAgendamento.FALTOU, "Paciente faltou");

        log.info("Falta marcada: id={}", id);
        return mapper.toResponse(agendamento);
    }

    // ==================== DISPONIBILIDADE ====================

    @Override
    public boolean verificarDisponibilidade(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        return repository.findConflitos(dentistaId, data, horaInicio, horaFim).isEmpty();
    }

    @Override
    public List<Map<String, LocalTime>> buscarHorariosDisponiveis(Long dentistaId, LocalDate data, int duracaoMinutos) {
        List<Agendamento> ocupados = repository.findOcupadosDoDia(dentistaId, data);
        List<Map<String, LocalTime>> disponiveis = new ArrayList<>();

        LocalTime atual = HORARIO_INICIO;

        while (atual.plusMinutes(duracaoMinutos).compareTo(HORARIO_FIM) <= 0) {
            LocalTime fimSlot = atual.plusMinutes(duracaoMinutos);
            boolean livre = true;

            for (Agendamento a : ocupados) {
                if (!(fimSlot.compareTo(a.getHoraInicio()) <= 0 || atual.compareTo(a.getHoraFim()) >= 0)) {
                    livre = false;
                    break;
                }
            }

            if (livre) {
                Map<String, LocalTime> slot = new LinkedHashMap<>();
                slot.put("inicio", atual);
                slot.put("fim", fimSlot);
                disponiveis.add(slot);
            }

            atual = atual.plusMinutes(INTERVALO_MINUTOS);
        }

        return disponiveis;
    }

    // ==================== LEMBRETES ====================

    @Override
    @Transactional
    public int enviarLembretes(LocalDate data) {
        List<Agendamento> agendamentos = repository.findParaLembrete(data);

        if (agendamentos.isEmpty()) {
            return 0;
        }

        List<Long> ids = agendamentos.stream().map(Agendamento::getId).collect(Collectors.toList());

        // Aqui implementaria envio real (email, SMS, push)
        for (Agendamento a : agendamentos) {
            log.info("Lembrete enviado para: {} - Consulta em {} às {}",
                    a.getPaciente().getDadosBasicos().getNome(), a.getDataConsulta(), a.getHoraInicio());
        }

        int atualizados = repository.marcarLembretesEnviados(ids, LocalDateTime.now());
        log.info("Lembretes enviados: {}", atualizados);
        return atualizados;
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    public Map<String, Object> obterEstatisticas(LocalDate inicio, LocalDate fim) {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("porStatus", repository.countPorStatusNoPeriodo(inicio, fim).stream()
                .collect(Collectors.toMap(r -> ((StatusAgendamento) r[0]).name(), r -> r[1])));

        stats.put("porProcedimento", repository.countPorProcedimentoNoPeriodo(inicio, fim).stream()
                .collect(Collectors.toMap(r -> r[0] != null ? r[0].toString() : "N/A", r -> r[1])));

        stats.put("porDentista", repository.countPorDentistaNoPeriodo(inicio, fim).stream()
                .map(r -> Map.of("id", r[0], "nome", r[1], "total", r[2]))
                .collect(Collectors.toList()));

        return stats;
    }

    @Override
    public long contarConsultasDoDia(Long dentistaId, LocalDate data) {
        return repository.countConsultasDoDia(dentistaId, data);
    }

    @Override
    public long contarFaltasPaciente(Long pacienteId) {
        return repository.countFaltasPaciente(pacienteId);
    }

    // ==================== CONSULTAS ESPECIAIS ====================

    @Override
    public List<AgendamentoResumoResponse> buscarPassadosNaoFinalizados() {
        return mapper.toResumoResponseList(repository.findPassadosNaoFinalizados(LocalDate.now()));
    }

    @Override
    public List<AgendamentoResumoResponse> buscarEmAtendimento() {
        return mapper.toResumoResponseList(repository.findEmAtendimento());
    }

    @Override
    public Page<AgendamentoResumoResponse> buscarHistoricoPaciente(Long pacienteId, Pageable pageable) {
        return repository.findHistoricoPaciente(pacienteId, pageable).map(mapper::toResumoResponse);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Agendamento findById(Long id) {
        return repository.findByIdAtivo(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + id));
    }

    private Dentista findDentista(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado: " + id));
    }

    private Paciente findPaciente(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado: " + id));
    }

    private void validarDentistaAtivo(Dentista dentista) {
        if (!dentista.getAtivo()) {
            throw new BusinessException("Dentista está inativo");
        }
    }

    private void validarHorario(LocalTime inicio, LocalTime fim) {
        if (!fim.isAfter(inicio)) {
            throw new BusinessException("Horário de fim deve ser após o horário de início");
        }
        if (inicio.isBefore(HORARIO_INICIO) || fim.isAfter(HORARIO_FIM)) {
            throw new BusinessException("Horário fora do expediente (08:00 - 18:00)");
        }
    }

    private void validarDataFutura(LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            throw new BusinessException("Não é possível agendar em datas passadas");
        }
    }

    private void validarConflitoDentista(Long dentistaId, LocalDate data, LocalTime inicio, LocalTime fim, Long idExcluir) {
        List<Agendamento> conflitos = idExcluir == null
                ? repository.findConflitos(dentistaId, data, inicio, fim)
                : repository.findConflitosExcluindo(dentistaId, data, inicio, fim, idExcluir);

        if (!conflitos.isEmpty()) {
            throw new BusinessException("Dentista já possui agendamento neste horário");
        }
    }

    private void validarConflitoPaciente(Long pacienteId, LocalDate data, LocalTime inicio, LocalTime fim) {
        List<Agendamento> conflitos = repository.findConflitoPaciente(pacienteId, data, inicio, fim);
        if (!conflitos.isEmpty()) {
            throw new BusinessException("Paciente já possui agendamento neste horário");
        }
    }

    private void registrarHistorico(Long agendamentoId, TipoAcao acao, StatusAgendamento anterior, StatusAgendamento novo, String descricao) {
        AgendamentoHistorico historico = new AgendamentoHistorico();
        historico.setAgendamentoId(agendamentoId);
        historico.setAcao(acao);
        historico.setStatusAnterior(anterior);
        historico.setStatusNovo(novo);
        historico.setUsuarioResponsavel(getUsuarioLogado());
        historico.setDescricao(descricao);
        historico.setDataHora(LocalDateTime.now());
        historicoRepository.save(historico);
    }

    private String getUsuarioLogado() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            return "sistema";
        }
    }
}