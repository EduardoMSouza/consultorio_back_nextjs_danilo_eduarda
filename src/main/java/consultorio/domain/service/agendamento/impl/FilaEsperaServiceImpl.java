package consultorio.domain.service.agendamento.impl;

import consultorio.api.dto.request.agendamento.fila_espera.FilaEsperaRequest;
import consultorio.api.dto.response.agendamento.fila_espera.FilaEsperaResponse;
import consultorio.api.mapper.agendamento.FilaEsperaMapper;
import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.repository.agendamento.AgendamentoRepository;
import consultorio.domain.repository.agendamento.FilaEsperaRepository;
import consultorio.domain.repository.pessoa.DentistaRepository;
import consultorio.domain.repository.pessoa.PacienteRepository;
import consultorio.domain.service.agendamento.FilaEsperaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilaEsperaServiceImpl implements FilaEsperaService {

    private final FilaEsperaRepository filaEsperaRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final FilaEsperaMapper mapper;

    @Override
    @Transactional
    public FilaEsperaResponse criar(FilaEsperaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Dentista dentista = null;
        if (request.getDentistaId() != null) {
            dentista = dentistaRepository.findById(request.getDentistaId())
                    .orElseThrow(() -> new EntityNotFoundException("Dentista não encontrado"));
        }

        boolean jaExiste = filaEsperaRepository.existsByPacienteIdAndStatusIn(
                request.getPacienteId(),
                Arrays.asList(FilaEspera.StatusFila.AGUARDANDO, FilaEspera.StatusFila.NOTIFICADO)
        );

        if (jaExiste) {
            throw new IllegalStateException("Paciente já possui uma solicitação ativa na fila de espera");
        }

        FilaEspera filaEspera = mapper.toEntity(request, paciente, dentista);
        filaEspera = filaEsperaRepository.save(filaEspera);

        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional
    public FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);

        if (!filaEspera.isAtiva()) {
            throw new IllegalStateException("Apenas filas com status AGUARDANDO ou NOTIFICADO podem ser atualizadas");
        }

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Dentista dentista = null;
        if (request.getDentistaId() != null) {
            dentista = dentistaRepository.findById(request.getDentistaId())
                    .orElseThrow(() -> new EntityNotFoundException("Dentista não encontrado"));
        }

        mapper.updateEntityFromRequest(request, filaEspera, paciente, dentista);
        filaEspera = filaEsperaRepository.save(filaEspera);

        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional(readOnly = true)
    public FilaEsperaResponse buscarPorId(Long id) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);
        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarTodas() {
        return filaEsperaRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorStatus(FilaEspera.StatusFila status) {
        return filaEsperaRepository.findByStatusOrderByPrioridadeDescCriadoEmAsc(status).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorPaciente(Long pacienteId) {
        return filaEsperaRepository.findByPacienteIdAndStatusIn(
                        pacienteId,
                        Arrays.asList(FilaEspera.StatusFila.values())
                ).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorDentista(Long dentistaId) {
        return filaEsperaRepository.findByDentistaIdAndStatusOrderByPrioridadeDescCriadoEmAsc(
                        dentistaId, FilaEspera.StatusFila.AGUARDANDO
                ).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarAtivas() {
        return filaEsperaRepository.findAllAtivasWithDetails().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FilaEsperaResponse notificar(Long id) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);

        if (!filaEspera.podeSerNotificado()) {
            throw new IllegalStateException("Fila não pode ser notificada no status atual");
        }

        filaEspera.notificar();
        filaEspera = filaEsperaRepository.save(filaEspera);

        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional
    public FilaEsperaResponse converterEmAgendamento(Long id, Long agendamentoId) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);

        if (!filaEspera.isAtiva()) {
            throw new IllegalStateException("Apenas filas ativas podem ser convertidas em agendamento");
        }

        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        filaEspera.converterEmAgendamento(agendamento);
        filaEspera = filaEsperaRepository.save(filaEspera);

        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional
    public FilaEsperaResponse cancelar(Long id) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);

        if (!filaEspera.isAtiva()) {
            throw new IllegalStateException("Apenas filas ativas podem ser canceladas");
        }

        filaEspera.cancelar();
        filaEspera = filaEsperaRepository.save(filaEspera);

        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional
    public void expirarFilasAntigas(LocalDate dataLimite) {
        List<FilaEspera> expiradas = filaEsperaRepository.findExpirados(dataLimite);

        expiradas.forEach(FilaEspera::expirar);
        filaEsperaRepository.saveAll(expiradas);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        FilaEspera filaEspera = buscarFilaOuLancarExcecao(id);
        filaEsperaRepository.delete(filaEspera);
    }

    private FilaEspera buscarFilaOuLancarExcecao(Long id) {
        return filaEsperaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fila de espera não encontrada"));
    }
}