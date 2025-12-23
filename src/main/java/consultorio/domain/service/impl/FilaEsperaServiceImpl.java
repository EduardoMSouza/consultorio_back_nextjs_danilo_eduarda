package consultorio.domain.service.impl;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.FilaEsperaMapper;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Dentista;
import consultorio.domain.entity.FilaEspera;
import consultorio.domain.entity.FilaEspera.StatusFila;
import consultorio.domain.entity.Paciente;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.repository.FilaEsperaRepository;
import consultorio.domain.repository.PacienteRepository;
import consultorio.domain.service.FilaEsperaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilaEsperaServiceImpl implements FilaEsperaService {

    private final FilaEsperaRepository repository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final FilaEsperaMapper mapper;

    private static final int MAX_TENTATIVAS_CONTATO = 3;
    private static final int DIAS_PARA_NOTIFICACAO_NOVAMENTE = 2;

    @Override
    @Transactional
    public FilaEsperaResponse criar(FilaEsperaRequest request) {
        log.info("Criando fila de espera para paciente {}", request.getPacienteId());

        Paciente paciente = findPacienteById(request.getPacienteId());
        Dentista dentista = request.getDentistaId() != null ?
                findDentistaById(request.getDentistaId()) : null;

        // Validações
        if (dentista != null) {
            validarDentistaAtivo(dentista);
        }

        validarPacienteNaoTemFilaAtiva(request.getPacienteId(), request.getDentistaId());

        // Criar fila de espera
        FilaEspera filaEspera = mapper.toEntity(request, paciente, dentista);
        filaEspera = repository.save(filaEspera);

        log.info("Fila de espera {} criada com sucesso", filaEspera.getId());
        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional(readOnly = true)
    public FilaEsperaResponse buscarPorId(Long id) {
        FilaEspera filaEspera = findById(id);
        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarTodas() {
        List<FilaEspera> filas = repository.findAll();
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilaEsperaResponse> listarTodas(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request) {
        log.info("Atualizando fila de espera {}", id);

        FilaEspera filaEspera = findById(id);

        if (!filaEspera.isAtiva()) {
            throw new BusinessException("Não é possível atualizar uma fila de espera inativa");
        }

        Paciente paciente = findPacienteById(request.getPacienteId());
        Dentista dentista = request.getDentistaId() != null ?
                findDentistaById(request.getDentistaId()) : null;

        if (dentista != null) {
            validarDentistaAtivo(dentista);
        }

        mapper.updateEntityFromRequest(request, filaEspera, paciente, dentista);
        filaEspera = repository.save(filaEspera);

        log.info("Fila de espera {} atualizada com sucesso", id);
        return mapper.toResponse(filaEspera);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando fila de espera {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fila de espera não encontrada com id: " + id);
        }

        repository.deleteById(id);
        log.info("Fila de espera {} deletada com sucesso", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarAtivas() {
        List<FilaEspera> filas = repository.findAllAtivas();
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorDentista(Long dentistaId) {
        List<FilaEspera> filas = repository.findByDentistaAtivas(dentistaId);
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorPaciente(Long pacienteId) {
        List<FilaEspera> filas = repository.findByPaciente(pacienteId);
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> listarPorPacienteAtivas(Long pacienteId) {
        List<FilaEspera> filas = repository.findByPacienteAtivas(pacienteId);
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilaEsperaResponse> listarPorStatus(StatusFila status, Pageable pageable) {
        return repository.findByStatus(status, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void notificar(Long id) {
        log.info("Notificando paciente da fila de espera {}", id);

        FilaEspera filaEspera = findById(id);

        if (!filaEspera.podeSerNotificado()) {
            throw new BusinessException("Fila de espera não pode ser notificada no momento");
        }

        filaEspera.notificar();
        repository.save(filaEspera);

        // Aqui você implementaria o envio da notificação (email, SMS, etc)
        log.info("Notificação enviada para fila de espera {}", id);
    }

    @Override
    @Transactional
    public void converterEmAgendamento(Long filaEsperaId, Long agendamentoId) {
        log.info("Convertendo fila de espera {} em agendamento {}", filaEsperaId, agendamentoId);

        FilaEspera filaEspera = findById(filaEsperaId);

        if (!filaEspera.isAtiva()) {
            throw new BusinessException("Fila de espera não está ativa");
        }

        // Assumindo que o agendamento já foi criado
        filaEspera.setStatus(StatusFila.CONVERTIDO);
        filaEspera.setConvertidoEm(LocalDateTime.now());
        repository.save(filaEspera);

        log.info("Fila de espera {} convertida com sucesso", filaEsperaId);
    }

    @Override
    @Transactional
    public void cancelar(Long id) {
        log.info("Cancelando fila de espera {}", id);

        FilaEspera filaEspera = findById(id);
        filaEspera.cancelar();
        repository.save(filaEspera);

        log.info("Fila de espera {} cancelada com sucesso", id);
    }

    @Override
    @Transactional
    public void processarFilaAutomaticamente() {
        log.info("Processando fila de espera automaticamente");

        List<FilaEspera> filasAtivas = repository.findAllAtivas();

        for (FilaEspera fila : filasAtivas) {
            try {
                // Verificar se o paciente ainda está disponível
                // Verificar se há horários disponíveis
                // Notificar paciente se encontrar horário disponível

                if (fila.podeSerNotificado()) {
                    notificar(fila.getId());
                }
            } catch (Exception e) {
                log.error("Erro ao processar fila de espera {}", fila.getId(), e);
            }
        }

        log.info("Processamento automático da fila concluído");
    }

    @Override
    @Transactional
    public void processarFilaAposCriacao(Agendamento agendamento) {
        // Implementar lógica para verificar se há outros pacientes na fila
        // que poderiam ser encaixados nos horários livres restantes
        log.debug("Processando fila após criação do agendamento {}", agendamento.getId());
    }

    @Override
    @Transactional
    public void processarFilaAposCancelamento(Agendamento agendamento) {
        log.info("Processando fila após cancelamento do agendamento {}", agendamento.getId());

        // Buscar filas compatíveis com o horário que ficou disponível
        List<FilaEspera> filasCompativeis = repository.findCompativeis(
                agendamento.getDentista().getId(),
                agendamento.getDataConsulta(),
                agendamento.getTipoProcedimento()
        );

        if (!filasCompativeis.isEmpty()) {
            // Notificar o primeiro da fila
            FilaEspera primeiraFila = filasCompativeis.get(0);

            if (primeiraFila.podeSerNotificado()) {
                try {
                    notificar(primeiraFila.getId());
                    log.info("Paciente {} notificado sobre horário disponível",
                            primeiraFila.getPaciente().getDadosBasicos().getNome());
                } catch (Exception e) {
                    log.error("Erro ao notificar paciente da fila {}", primeiraFila.getId(), e);
                }
            }
        }
    }

    @Override
    @Transactional
    public void processarFilaAposConclusao(Agendamento agendamento) {
        // Similar ao cancelamento, mas pode ter lógicas diferentes
        log.debug("Processando fila após conclusão do agendamento {}", agendamento.getId());
    }

    @Override
    @Transactional
    public void expirarFilasAnteriores() {
        log.info("Expirando filas de espera antigas");

        List<FilaEspera> filasExpiradas = repository.findExpiradas(LocalDate.now());

        for (FilaEspera fila : filasExpiradas) {
            fila.expirar();
            repository.save(fila);
            log.info("Fila de espera {} expirada", fila.getId());
        }

        log.info("Total de {} filas expiradas", filasExpiradas.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> buscarCompativeisComAgendamento(Long dentistaId,
                                                                    LocalDate data,
                                                                    Agendamento.TipoProcedimento tipoProcedimento) {
        List<FilaEspera> filas = repository.findCompativeis(dentistaId, data, tipoProcedimento);
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FilaEsperaResponse> buscarCompativeisComDentista(Long dentistaId) {
        List<FilaEspera> filas = repository.findCompatíveisPorDentista(dentistaId);
        return mapper.toResponseList(filas);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAtivasPorDentista(Long dentistaId) {
        return repository.countAtivasPorDentista(dentistaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarTotalAtivas() {
        return repository.countTotalAtivas();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAtivasPorPaciente(Long pacienteId) {
        return repository.countAtivasPorPaciente(pacienteId);
    }

    @Override
    @Transactional
    public void enviarNotificacoesPendentes() {
        log.info("Enviando notificações pendentes");

        List<FilaEspera> filasPendentes = repository.findPendentesNotificacao();

        for (FilaEspera fila : filasPendentes) {
            try {
                if (fila.getTentativasContato() < MAX_TENTATIVAS_CONTATO) {
                    notificar(fila.getId());
                } else {
                    log.warn("Fila {} atingiu número máximo de tentativas de contato", fila.getId());
                }
            } catch (Exception e) {
                log.error("Erro ao enviar notificação para fila {}", fila.getId(), e);
            }
        }

        log.info("Notificações pendentes enviadas");
    }

    @Override
    @Transactional
    public void incrementarTentativaContato(Long id) {
        FilaEspera filaEspera = findById(id);
        filaEspera.incrementarTentativaContato();
        repository.save(filaEspera);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private FilaEspera findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fila de espera não encontrada com id: " + id));
    }

    private Paciente findPacienteById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    private Dentista findDentistaById(Long id) {
        return dentistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com id: " + id));
    }

    private void validarDentistaAtivo(Dentista dentista) {
        if (!dentista.getAtivo()) {
            throw new BusinessException("Dentista está inativo");
        }
    }

    private void validarPacienteNaoTemFilaAtiva(Long pacienteId, Long dentistaId) {
        if (dentistaId != null) {
            boolean exists = repository.existsByPacienteAndDentistaAtiva(pacienteId, dentistaId);
            if (exists) {
                throw new BusinessException("Paciente já possui uma fila de espera ativa para este dentista");
            }
        } else {
            boolean exists = repository.existsByPacienteAtiva(pacienteId);
            if (exists) {
                throw new BusinessException("Paciente já possui uma fila de espera ativa");
            }
        }
    }
}