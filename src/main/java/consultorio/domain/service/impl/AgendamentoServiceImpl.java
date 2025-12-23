package consultorio.api.mapper.service.impl;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.AgendamentoMapper;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.Paciente;
import consultorio.domain.repository.AgendamentoRepository;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.repository.PacienteRepository;
import consultorio.api.mapper.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository repository;
    private final DentistaRepository dentistaRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendamentoMapper mapper;

    @Override
    @Transactional
    public AgendamentoResponse criar(AgendamentoRequest request) {
        validarHorario(request);

        Dentista dentista = findDentistaById(request.getDentistaId());
        Paciente paciente = findPacienteById(request.getPacienteId());

        validarDentistaAtivo(dentista);
        validarConflito(request.getDentistaId(), request.getDataConsulta(),
                request.getHoraInicio(), request.getHoraFim(), null);

        Agendamento agendamento = mapper.toEntity(request, dentista, paciente);
        agendamento = repository.save(agendamento);

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long id) {
        Agendamento agendamento = findById(id);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgendamentoResumoResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResumoResponse);
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
    @Transactional
    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = findById(id);

        validarStatusParaEdicao(agendamento);
        validarHorario(request);

        Dentista dentista = findDentistaById(request.getDentistaId());
        Paciente paciente = findPacienteById(request.getPacienteId());

        validarDentistaAtivo(dentista);
        validarConflito(request.getDentistaId(), request.getDataConsulta(),
                request.getHoraInicio(), request.getHoraFim(), id);

        mapper.updateEntityFromRequest(request, agendamento, dentista, paciente);
        agendamento = repository.save(agendamento);

        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoResponse atualizarStatus(Long id, StatusAgendamento status) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(status);
        agendamento = repository.save(agendamento);
        return mapper.toResponse(agendamento);
    }

    @Override
    @Transactional
    public void confirmar(Long id) {
        Agendamento agendamento = findById(id);
        validarTransicaoStatus(agendamento, StatusAgendamento.CONFIRMADO);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        repository.save(agendamento);
    }

    @Override
    @Transactional
    public void iniciarAtendimento(Long id) {
        Agendamento agendamento = findById(id);
        validarTransicaoStatus(agendamento, StatusAgendamento.EM_ATENDIMENTO);
        agendamento.setStatus(StatusAgendamento.EM_ATENDIMENTO);
        repository.save(agendamento);
    }

    @Override
    @Transactional
    public void concluir(Long id) {
        Agendamento agendamento = findById(id);
        validarTransicaoStatus(agendamento, StatusAgendamento.CONCLUIDO);
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        repository.save(agendamento);
    }

    @Override
    @Transactional
    public void cancelar(Long id) {
        Agendamento agendamento = findById(id);
        validarTransicaoStatus(agendamento, StatusAgendamento.CANCELADO);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        repository.save(agendamento);
    }

    @Override
    @Transactional
    public void marcarFalta(Long id) {
        Agendamento agendamento = findById(id);
        validarTransicaoStatus(agendamento, StatusAgendamento.FALTOU);
        agendamento.setStatus(StatusAgendamento.FALTOU);
        repository.save(agendamento);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Agendamento não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        List<Agendamento> conflitos = repository.findConflitos(dentistaId, data, horaInicio, horaFim);
        return conflitos.isEmpty();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Agendamento findById(Long id) {
        return repository.findById(id)
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

    private void validarConflito(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim, Long agendamentoIdExcluir) {
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

    private void validarStatusParaEdicao(Agendamento agendamento) {
        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO ||
                agendamento.getStatus() == StatusAgendamento.CANCELADO ||
                agendamento.getStatus() == StatusAgendamento.FALTOU) {
            throw new BusinessException("Não é possível editar um agendamento com status: " + agendamento.getStatus());
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
                if (statusAtual == StatusAgendamento.CONCLUIDO || statusAtual == StatusAgendamento.FALTOU) {
                    throw new BusinessException("Não é possível cancelar agendamentos já finalizados");
                }
                break;
            case FALTOU:
                if (statusAtual == StatusAgendamento.CONCLUIDO || statusAtual == StatusAgendamento.CANCELADO) {
                    throw new BusinessException("Não é possível marcar falta em agendamentos já finalizados");
                }
                break;
            default:
                break;
        }
    }
}