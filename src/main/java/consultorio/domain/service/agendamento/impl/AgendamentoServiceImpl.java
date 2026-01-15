package consultorio.domain.service.agendamento.impl;

import consultorio.api.dto.request.agendamento.AgendamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.api.mapper.agendamento.AgendamentoMapper;
import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;

import consultorio.domain.repository.agendamento.AgendamentoHistoricoRepository;
import consultorio.domain.repository.agendamento.AgendamentoRepository;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.service.agendamento.AgendamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final DentistaRepository dentistaRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoHistoricoRepository historicoRepository;
    private final AgendamentoMapper mapper;

    @Override
    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                .orElseThrow(() -> new EntityNotFoundException("Dentista não encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        validarHorarios(request.getHoraInicio(), request.getHoraFim());

        List<Agendamento> conflitos = agendamentoRepository.findConflitantes(
                request.getDentistaId(),
                request.getDataConsulta(),
                request.getHoraInicio(),
                request.getHoraFim()
        );

        if (!conflitos.isEmpty()) {
            throw new IllegalStateException("Horário indisponível para o dentista");
        }

        Agendamento agendamento = mapper.toEntity(request, dentista, paciente);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistorico(agendamento.getId(), AgendamentoHistorico.TipoAcao.CRIACAO,
                request.getCriadoPor(), "Agendamento criado");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);

        if (!agendamento.isPodeSerEditado()) {
            throw new IllegalStateException("Agendamento não pode ser editado no status atual");
        }

        Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                .orElseThrow(() -> new EntityNotFoundException("Dentista não encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        validarHorarios(request.getHoraInicio(), request.getHoraFim());

        List<Agendamento> conflitos = agendamentoRepository.findConflitantesExcluindoAgendamento(
                request.getDentistaId(),
                request.getDataConsulta(),
                request.getHoraInicio(),
                request.getHoraFim(),
                id
        );

        if (!conflitos.isEmpty()) {
            throw new IllegalStateException("Horário indisponível para o dentista");
        }

        mapper.updateEntityFromRequest(request, agendamento, dentista, paciente);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistorico(id, AgendamentoHistorico.TipoAcao.ATUALIZACAO,
                request.getCriadoPor(), "Agendamento atualizado");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorDentista(Long dentistaId) {
        return agendamentoRepository.findByDentistaIdAndAtivoTrue(dentistaId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPaciente(Long pacienteId) {
        return agendamentoRepository.findByPacienteIdAndAtivoTrue(pacienteId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorData(LocalDate data) {
        return agendamentoRepository.findByDataConsultaAndAtivoTrue(data).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return agendamentoRepository.findAllWithDetailsByPeriodo(dataInicio, dataFim).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatusAndAtivoTrue(status).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorDentistaEData(Long dentistaId, LocalDate data) {
        return agendamentoRepository.findByDentistaIdAndDataConsultaAndAtivoTrue(dentistaId, data).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgendamentoResponse confirmar(Long id, String usuario) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.confirmar(usuario);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistoricoMudancaStatus(id, statusAnterior, StatusAgendamento.CONFIRMADO,
                usuario, "Agendamento confirmado");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse iniciarAtendimento(Long id, String usuario) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.iniciarAtendimento(usuario);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistoricoMudancaStatus(id, statusAnterior, StatusAgendamento.EM_ATENDIMENTO,
                usuario, "Atendimento iniciado");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse concluir(Long id, String usuario) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.concluir(usuario);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistoricoMudancaStatus(id, statusAnterior, StatusAgendamento.CONCLUIDO,
                usuario, "Agendamento concluído");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse cancelar(Long id, String motivo, String usuario) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);

        if (!agendamento.isPodeSerCancelado()) {
            throw new IllegalStateException("Agendamento não pode ser cancelado");
        }

        StatusAgendamento statusAnterior = agendamento.getStatus();
        agendamento.cancelar(motivo, usuario);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistoricoMudancaStatus(id, statusAnterior, StatusAgendamento.CANCELADO,
                usuario, "Agendamento cancelado: " + motivo);

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse marcarFalta(Long id, String usuario) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        StatusAgendamento statusAnterior = agendamento.getStatus();

        agendamento.marcarFalta(usuario);
        agendamento = agendamentoRepository.save(agendamento);

        registrarHistoricoMudancaStatus(id, statusAnterior, StatusAgendamento.FALTOU,
                usuario, "Paciente faltou ao agendamento");

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        agendamento.desativar();
        agendamentoRepository.save(agendamento);

        registrarHistorico(id, AgendamentoHistorico.TipoAcao.EXCLUSAO,
                null, "Agendamento desativado");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Long dentistaId, LocalDate data, String horaInicio, String horaFim) {
        LocalTime inicio = LocalTime.parse(horaInicio);
        LocalTime fim = LocalTime.parse(horaFim);

        List<Agendamento> conflitos = agendamentoRepository.findConflitantes(dentistaId, data, inicio, fim);
        return conflitos.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidadeParaAtualizacao(Long agendamentoId, Long dentistaId,
                                                           LocalDate data, String horaInicio, String horaFim) {
        LocalTime inicio = LocalTime.parse(horaInicio);
        LocalTime fim = LocalTime.parse(horaFim);

        List<Agendamento> conflitos = agendamentoRepository.findConflitantesExcluindoAgendamento(
                dentistaId, data, inicio, fim, agendamentoId);
        return conflitos.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResponse> buscarAgendamentosParaLembrete(LocalDate data) {
        return agendamentoRepository.findAgendamentosParaLembrete(data).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void marcarLembreteEnviado(Long id) {
        Agendamento agendamento = buscarAgendamentoOuLancarExcecao(id);
        agendamento.marcarLembreteEnviado();
        agendamentoRepository.save(agendamento);

        registrarHistorico(id, AgendamentoHistorico.TipoAcao.LEMBRETE_ENVIADO,
                null, "Lembrete enviado ao paciente");
    }

    private Agendamento buscarAgendamentoOuLancarExcecao(Long id) {
        return agendamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
    }

    private void validarHorarios(LocalTime horaInicio, LocalTime horaFim) {
        if (horaInicio.isAfter(horaFim) || horaInicio.equals(horaFim)) {
            throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
        }
    }

    private void registrarHistorico(Long agendamentoId, AgendamentoHistorico.TipoAcao acao,
                                    String usuario, String descricao) {
        AgendamentoHistorico historico = AgendamentoHistorico.criar(agendamentoId, acao, usuario, descricao);
        historicoRepository.save(historico);
    }

    private void registrarHistoricoMudancaStatus(Long agendamentoId, StatusAgendamento statusAnterior,
                                                 StatusAgendamento statusNovo, String usuario, String descricao) {
        AgendamentoHistorico historico = AgendamentoHistorico.criarMudancaStatus(
                agendamentoId, statusAnterior, statusNovo, usuario, descricao);
        historicoRepository.save(historico);
    }
}