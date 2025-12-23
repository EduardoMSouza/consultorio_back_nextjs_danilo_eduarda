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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository repository;
    private final DentistaRepository dentistaRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoHistoricoRepository historicoRepository;
    private final AgendamentoMapper mapper;
    private final FilaEsperaService filaEsperaService;

    private static final int MAX_CONSULTAS_POR_DIA = 20;
    private static final LocalTime HORA_INICIO_EXPEDIENTE = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIM_EXPEDIENTE = LocalTime.of(18, 0);
    private static final int DURACAO_MINIMA_MINUTOS = 30;
    private static final int DURACAO_MAXIMA_MINUTOS = 240;

    @Override
    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        log.info("Criando agendamento para dentista {} e paciente {}",
                request.getDentistaId(), request.getPacienteId());

        validarHorario(request);
        validarDataConsulta(request.getDataConsulta());
        validarHorarioComercial(request.getHoraInicio(), request.getHoraFim());
        validarDuracaoConsulta(request.getHoraInicio(), request.getHoraFim());

        Dentista dentista = findDentistaById(request.getDentistaId());
        Paciente paciente = findPacienteById(request.getPacienteId());

        validarDentistaAtivo(dentista);
        validarCapacidadeDiaria(request.getDentistaId(), request.getDataConsulta());

        validarConflitoDentista(request.getDentistaId(), request.getDataConsulta(),
                request.getHoraInicio(), request.getHoraFim(), null);
        validarConflitoPaciente(request.getPacienteId(), request.getDataConsulta(),
                request.getHoraInicio(), request.getHoraFim(), null);

        Agendamento agendamento = mapper.toEntity(request, dentista, paciente);
        agendamento = repository.save(agendamento);

        registrarHistorico(agendamento.getId(), AgendamentoHistorico.TipoAcao.CRIACAO,
                null, "Agendamento criado");

        filaEsperaService.processarFilaAposCriacao(agendamento);

        log.info("Agendamento {} criado com sucesso", agendamento.getId());
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarTodos(Pageable pageable) {
        return repository.findAllAtivos(pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        log.info("Atualizando agendamento {}", id);

        Agendamento agendamento = findByIdAtivo(id);

        validarStatusParaEdicao(agendamento);
        validarHorario(request);
        validarDataConsulta(request.getDataConsulta());
        validarHorarioComercial(request.getHoraInicio(), request.getHoraFim());
        validarDuracaoConsulta(request.getHoraInicio(), request.getHoraFim());

        Dentista dentista = findDentistaById(request.getDentistaId());
        Paciente paciente = findPacienteById(request.getPacienteId());

        validarDentistaAtivo(dentista);

        boolean mudouDentista = !agendamento.getDentista().getId().equals(request.getDentistaId());
        boolean mudouHorario = !agendamento.getDataConsulta().equals(request.getDataConsulta()) ||
                !agendamento.getHoraInicio().equals(request.getHoraInicio()) ||
                !agendamento.getHoraFim().equals(request.getHoraFim());

        if (mudouDentista || mudouHorario) {
            if (mudouDentista || !agendamento.getDataConsulta().equals(request.getDataConsulta())) {
                validarCapacidadeDiaria(request.getDentistaId(), request.getDataConsulta());
            }

            validarConflitoDentista(request.getDentistaId(), request.getDataConsulta(),
                    request.getHoraInicio(), request.getHoraFim(), id);
            validarConflitoPaciente(request.getPacienteId(), request.getDataConsulta(),
                    request.getHoraInicio(), request.getHoraFim(), id);
        }

        mapper.updateEntityFromRequest(request, agendamento, dentista, paciente);
        agendamento = repository.save(agendamento);

        registrarHistorico(agendamento.getId(), AgendamentoHistorico.TipoAcao.ATUALIZACAO,
                null, "Agendamento atualizado");

        log.info("Agendamento {} atualizado com sucesso", id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando agendamento {}", id);

        Agendamento agendamento = findByIdAtivo(id);

        registrarHistorico(id, AgendamentoHistorico.TipoAcao.EXCLUSAO,
                null, "Agendamento excluído");

        agendamento.desativar();
        repository.save(agendamento);

        filaEsperaService.processarFilaAposCancelamento(agendamento);

        log.info("Agendamento {} deletado com sucesso", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarPorDentista(Long dentistaId, Pageable pageable) {
        return repository.findByDentistaId(dentistaId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarPorPaciente(Long pacienteId, Pageable pageable) {
        return repository.findByPacienteId(pacienteId, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarPorStatus(StatusAgendamento status, Pageable pageable) {
        return repository.findByStatus(status, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        validarPeriodo(dataInicio, dataFim);
        return repository.findByPeriodo(dataInicio, dataFim, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarAgendaDoDia(Long dentistaId, LocalDate data) {
        List<Agendamento> agendamentos = repository.findAgendaDoDia(dentistaId, data);
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarAgendaDoDia(LocalDate data) {
        List<Agendamento> agendamentos = repository.findAllAgendaDoDia(data);
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarProximosAgendamentos(Long pacienteId) {
        List<Agendamento> agendamentos = repository.findProximosAgendamentos(pacienteId, LocalDate.now());
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarProximosAgendamentosDentista(Long dentistaId) {
        List<Agendamento> agendamentos = repository.findProximosAgendamentosDentista(dentistaId, LocalDate.now());
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, StatusAgendamento status) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.setStatus(status);
        agendamento = repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, status, "Status atualizado manualmente");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public void confirmar(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        validarTransicaoStatus(agendamento, StatusAgendamento.CONFIRMADO);

        agendamento.confirmar("system");
        repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, StatusAgendamento.CONFIRMADO, "Agendamento confirmado");
    }

    @Override
    @Transactional
    public void iniciarAtendimento(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        validarTransicaoStatus(agendamento, StatusAgendamento.EM_ATENDIMENTO);

        agendamento.iniciarAtendimento("system");
        repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, StatusAgendamento.EM_ATENDIMENTO, "Atendimento iniciado");
    }

    @Override
    @Transactional
    public void concluir(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        validarTransicaoStatus(agendamento, StatusAgendamento.CONCLUIDO);

        agendamento.concluir("system");
        repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, StatusAgendamento.CONCLUIDO, "Agendamento concluído");

        filaEsperaService.processarFilaAposConclusao(agendamento);
    }

    @Override
    @Transactional
    public void cancelar(Long id, String motivo) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        validarTransicaoStatus(agendamento, StatusAgendamento.CANCELADO);

        agendamento.cancelar(motivo, "system");
        repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, StatusAgendamento.CANCELADO,
                "Agendamento cancelado: " + motivo);

        filaEsperaService.processarFilaAposCancelamento(agendamento);
    }

    @Override
    @Transactional
    public void marcarFalta(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        validarTransicaoStatus(agendamento, StatusAgendamento.FALTOU);

        agendamento.marcarFalta("system");
        repository.save(agendamento);

        registrarMudancaStatus(id, statusAnterior, StatusAgendamento.FALTOU, "Paciente faltou");

        filaEsperaService.processarFilaAposCancelamento(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        List<Agendamento> conflitos = repository.findConflitos(dentistaId, data, horaInicio, horaFim);
        return conflitos.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalTime[]> buscarHorariosDisponiveis(Long dentistaId, LocalDate data, int duracaoMinutos) {
        List<Agendamento> agendamentos = repository.findAgendamentosAtivos(dentistaId, data);
        List<LocalTime[]> horariosDisponiveis = new ArrayList<>();

        LocalTime horaAtual = HORA_INICIO_EXPEDIENTE;
        LocalTime horaFimDisponivel = horaAtual.plusMinutes(duracaoMinutos);

        for (Agendamento agendamento : agendamentos) {
            if (horaFimDisponivel.isBefore(agendamento.getHoraInicio()) ||
                    horaFimDisponivel.equals(agendamento.getHoraInicio())) {
                horariosDisponiveis.add(new LocalTime[]{horaAtual, agendamento.getHoraInicio()});
            }
            horaAtual = agendamento.getHoraFim();
            horaFimDisponivel = horaAtual.plusMinutes(duracaoMinutos);
        }

        if (horaFimDisponivel.isBefore(HORA_FIM_EXPEDIENTE) ||
                horaFimDisponivel.equals(HORA_FIM_EXPEDIENTE)) {
            horariosDisponiveis.add(new LocalTime[]{horaAtual, HORA_FIM_EXPEDIENTE});
        }

        return horariosDisponiveis;
    }

    @Override
    @Transactional
    public void enviarLembretes(LocalDate data) {
        List<Agendamento> consultas = repository.findConsultasParaLembrete(data);

        for (Agendamento agendamento : consultas) {
            try {
                // Aqui você implementaria o envio do lembrete (email, SMS, etc)
                log.info("Enviando lembrete para agendamento {}", agendamento.getId());

                agendamento.marcarLembreteEnviado();
                repository.save(agendamento);

                registrarHistorico(agendamento.getId(), AgendamentoHistorico.TipoAcao.LEMBRETE_ENVIADO,
                        null, "Lembrete enviado");
            } catch (Exception e) {
                log.error("Erro ao enviar lembrete para agendamento {}", agendamento.getId(), e);
            }
        }
    }

    @Override
    @Transactional
    public void marcarLembreteEnviado(Long id) {
        Agendamento agendamento = findByIdAtivo(id);
        agendamento.marcarLembreteEnviado();
        repository.save(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarConsultasDoDia(Long dentistaId, LocalDate data) {
        return repository.countConsultasDoDia(dentistaId, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarFaltasPaciente(Long pacienteId) {
        return repository.countFaltasPaciente(pacienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarConsultasPassadasNaoFinalizadas() {
        List<Agendamento> agendamentos = repository.findConsultasPassadasNaoFinalizadas(LocalDate.now());
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResumoResponse> buscarConsultasEmAtendimento() {
        List<Agendamento> agendamentos = repository.findConsultasEmAtendimento();
        return mapper.toResumoResponseList(agendamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> buscarHistoricoConsultasPaciente(Long pacienteId, Pageable pageable) {
        return repository.findHistoricoConsultasPaciente(pacienteId, pageable)
                .map(mapper::toResumoResponse);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Agendamento findByIdAtivo(Long id) {
        return repository.findByIdAtivo(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado com id: " + id));
    }

    private Dentista findDentistaById(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com id: " + id));
    }

    private Paciente findPacienteById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    private void validarDentistaAtivo(Dentista dentista) {
        if (!dentista.getAtivo()) {
            throw new BusinessException("Dentista está inativo e não pode receber agendamentos");
        }
    }

    private void validarHorario(AgendamentoRequest request) {
        if (request.getHoraFim().isBefore(request.getHoraInicio()) ||
                request.getHoraFim().equals(request.getHoraInicio())) {
            throw new BusinessException("Hora de fim deve ser posterior à hora de início");
        }
    }

    private void validarDataConsulta(LocalDate dataConsulta) {
        if (dataConsulta.isBefore(LocalDate.now())) {
            throw new BusinessException("Não é possível agendar consultas no passado");
        }
    }

    private void validarHorarioComercial(LocalTime horaInicio, LocalTime horaFim) {
        if (horaInicio.isBefore(HORA_INICIO_EXPEDIENTE)) {
            throw new BusinessException("Horário de início deve ser após " + HORA_INICIO_EXPEDIENTE);
        }
        if (horaFim.isAfter(HORA_FIM_EXPEDIENTE)) {
            throw new BusinessException("Horário de fim deve ser antes de " + HORA_FIM_EXPEDIENTE);
        }
    }

    private void validarDuracaoConsulta(LocalTime horaInicio, LocalTime horaFim) {
        long minutos = Duration.between(horaInicio, horaFim).toMinutes();

        if (minutos < DURACAO_MINIMA_MINUTOS) {
            throw new BusinessException("Duração mínima da consulta: " + DURACAO_MINIMA_MINUTOS + " minutos");
        }
        if (minutos > DURACAO_MAXIMA_MINUTOS) {
            throw new BusinessException("Duração máxima da consulta: " + DURACAO_MAXIMA_MINUTOS + " minutos");
        }
    }

    private void validarCapacidadeDiaria(Long dentistaId, LocalDate data) {
        Long consultasDia = repository.countConsultasDoDia(dentistaId, data);
        if (consultasDia >= MAX_CONSULTAS_POR_DIA) {
            throw new BusinessException("Dentista já atingiu capacidade máxima do dia (" +
                    MAX_CONSULTAS_POR_DIA + " consultas)");
        }
    }

    private void validarConflitoDentista(Long dentistaId, LocalDate data,
                                         LocalTime horaInicio, LocalTime horaFim,
                                         Long agendamentoIdExcluir) {
        List<Agendamento> conflitos;

        if (agendamentoIdExcluir != null) {
            conflitos = repository.findConflitosExcluindo(dentistaId, data, horaInicio, horaFim, agendamentoIdExcluir);
        } else {
            conflitos = repository.findConflitos(dentistaId, data, horaInicio, horaFim);
        }

        if (!conflitos.isEmpty()) {
            throw new BusinessException("Já existe um agendamento neste horário para o dentista selecionado");
        }
    }

    private void validarConflitoPaciente(Long pacienteId, LocalDate data,
                                         LocalTime horaInicio, LocalTime horaFim,
                                         Long agendamentoIdExcluir) {
        List<Agendamento> conflitos;

        if (agendamentoIdExcluir != null) {
            conflitos = repository.findConflitosParaPacienteExcluindo(pacienteId, data,
                    horaInicio, horaFim, agendamentoIdExcluir);
        } else {
            conflitos = repository.findConflitosParaPaciente(pacienteId, data, horaInicio, horaFim);
        }

        if (!conflitos.isEmpty()) {
            throw new BusinessException("Paciente já possui consulta neste horário");
        }
    }

    private void validarStatusParaEdicao(Agendamento agendamento) {
        if (!agendamento.isPodeSerEditado()) {
            throw new BusinessException("Não é possível editar um agendamento com status: " +
                    agendamento.getStatus().getDescricao());
        }
    }

    private void validarTransicaoStatus(Agendamento agendamento, StatusAgendamento novoStatus) {
        StatusAgendamento statusAtual = agendamento.getStatus();

        switch (novoStatus) {
            case CONFIRMADO:
                if (statusAtual != StatusAgendamento.AGENDADO) {
                    throw new BusinessException("Só é possível confirmar agendamentos com status AGENDADO");
                }
                break;
            case EM_ATENDIMENTO:
                if (statusAtual != StatusAgendamento.AGENDADO && statusAtual != StatusAgendamento.CONFIRMADO) {
                    throw new BusinessException("Só é possível iniciar atendimento de agendamentos AGENDADOS ou CONFIRMADOS");
                }
                break;
            case CONCLUIDO:
                if (statusAtual != StatusAgendamento.EM_ATENDIMENTO) {
                    throw new BusinessException("Só é possível concluir agendamentos EM_ATENDIMENTO");
                }
                break;
            case CANCELADO:
                if (!agendamento.isPodeSerCancelado()) {
                    throw new BusinessException("Não é possível cancelar agendamentos já finalizados");
                }
                break;
            case FALTOU:
                if (statusAtual == StatusAgendamento.CONCLUIDO || statusAtual == StatusAgendamento.CANCELADO) {
                    throw new BusinessException("Não é possível marcar falta em agendamentos já finalizados");
                }
                break;
        }
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio.isAfter(dataFim)) {
            throw new BusinessException("Data de início deve ser anterior à data de fim");
        }
    }

    private void registrarHistorico(Long agendamentoId, AgendamentoHistorico.TipoAcao acao,
                                    String usuario, String descricao) {
        AgendamentoHistorico historico = AgendamentoHistorico.criar(
                agendamentoId, acao, usuario != null ? usuario : "system", descricao
        );
        historicoRepository.save(historico);
    }

    private void registrarMudancaStatus(Long agendamentoId, StatusAgendamento statusAnterior,
                                        StatusAgendamento statusNovo, String descricao) {
        AgendamentoHistorico historico = AgendamentoHistorico.criarMudancaStatus(
                agendamentoId, statusAnterior, statusNovo, "system", descricao
        );
        historicoRepository.save(historico);
    }
}