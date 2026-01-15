package consultorio.domain.service.impl;

<<<<<<< HEAD
import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.tratamento.PlanoDentalMapper;

import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.repository.PacienteRepository;
=======
import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.PlanoDentalMapper;
import consultorio.domain.entity.PlanoDental;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import consultorio.domain.repository.PlanoDentalRepository;
import consultorio.domain.service.PlanoDentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
=======
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
<<<<<<< HEAD
@Transactional
public class PlanoDentalServiceImpl implements PlanoDentalService {

    private final PlanoDentalRepository planoDentalRepository;
    private final PacienteRepository pacienteRepository;
    private final DentistaRepository dentistaRepository;
    private final PlanoDentalMapper planoDentalMapper;

    @Override
    public PlanoDentalResponse create(PlanoDentalRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Dentista dentista = dentistaRepository.findById(request.dentistaId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado"));

        if (planoDentalRepository.existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(
                paciente.getId(), request.dente(), request.procedimento())) {
            throw new BusinessException("Já existe um plano ativo para este paciente com mesmo dente e procedimento");
        }

        PlanoDental plano = planoDentalMapper.toEntity(request, paciente, dentista);
        plano = planoDentalRepository.save(plano);

        log.info("Plano dental criado: ID {}", plano.getId());
        return planoDentalMapper.toResponse(plano);
=======
public class PlanoDentalServiceImpl implements PlanoDentalService {

    private final PlanoDentalRepository repository;
    private final PlanoDentalMapper mapper;

    @Override
    @Transactional
    public PlanoDentalResponse criar(PlanoDentalRequest request) {
        log.info("Criando plano dental");

        PlanoDental plano = mapper.toEntity(request);
        plano = repository.save(plano);

        log.info("Plano dental {} criado", plano.getId());
        return mapper.toResponse(plano);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public PlanoDentalResponse findById(Long id) {
        PlanoDental plano = planoDentalRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));
        return planoDentalMapper.toResponse(plano);
=======
    public PlanoDentalResponse buscarPorId(Long id) {
        PlanoDental plano = findById(id);
        return mapper.toResponse(plano);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public Page<PlanoDentalResponse> findAll(Pageable pageable) {
        return planoDentalRepository.findAll(pageable)
                .map(planoDentalMapper::toResponse);
    }

    @Override
    public PlanoDentalResponse update(Long id, PlanoDentalRequest request) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível atualizar um plano inativo");
        }

        planoDentalMapper.updateEntityFromRequest(request, plano);

        if (request.pacienteId() != null && !plano.getPaciente().getId().equals(request.pacienteId())) {
            Paciente novoPaciente = pacienteRepository.findById(request.pacienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
            plano.setPaciente(novoPaciente);
        }

        if (request.dentistaId() != null && !plano.getDentista().getId().equals(request.dentistaId())) {
            Dentista novoDentista = dentistaRepository.findById(request.dentistaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado"));
            plano.setDentista(novoDentista);
        }

        plano = planoDentalRepository.save(plano);
        log.info("Plano dental atualizado: ID {}", plano.getId());
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public void delete(Long id) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        plano.desativar();
        planoDentalRepository.save(plano);
        log.info("Plano dental desativado: ID {}", plano.getId());
    }

    @Override
    public PlanoDentalResponse concluir(Long id) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível concluir um plano inativo");
        }

        if (plano.getStatus() == StatusPlano.CANCELADO) {
            throw new BusinessException("Não é possível concluir um plano cancelado");
        }

        plano.concluir();
        plano = planoDentalRepository.save(plano);
        log.info("Plano dental concluído: ID {}", plano.getId());
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public PlanoDentalResponse cancelar(Long id, String motivo) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível cancelar um plano inativo");
        }

        if (plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Não é possível cancelar um plano já concluído");
        }

        plano.cancelar(motivo);
        plano = planoDentalRepository.save(plano);
        log.info("Plano dental cancelado: ID {}, Motivo: {}", plano.getId(), motivo);
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public PlanoDentalResponse iniciar(Long id) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível iniciar um plano inativo");
        }

        if (plano.getStatus() != StatusPlano.PENDENTE) {
            throw new BusinessException("Só é possível iniciar um plano pendente");
        }

        plano.iniciar();
        plano = planoDentalRepository.save(plano);
        log.info("Plano dental iniciado: ID {}", plano.getId());
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public PlanoDentalResponse aplicarDesconto(Long id, BigDecimal desconto) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível aplicar desconto em um plano inativo");
        }

        if (plano.getStatus() == StatusPlano.CANCELADO || plano.getStatus() == StatusPlano.CONCLUIDO) {
            throw new BusinessException("Não é possível aplicar desconto em um plano concluído ou cancelado");
        }

        if (desconto.compareTo(plano.getValor()) > 0) {
            throw new BusinessException("O desconto não pode ser maior que o valor do plano");
        }

        plano.aplicarDesconto(desconto);
        plano = planoDentalRepository.save(plano);
        log.info("Desconto aplicado ao plano dental: ID {}, Desconto: {}", plano.getId(), desconto);
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    public PlanoDentalResponse atualizarValor(Long id, BigDecimal valor) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        if (!plano.getAtivo()) {
            throw new BusinessException("Não é possível atualizar valor de um plano inativo");
        }

        if (plano.getStatus() == StatusPlano.CONCLUIDO || plano.getStatus() == StatusPlano.CANCELADO) {
            throw new BusinessException("Não é possível atualizar valor de um plano concluído ou cancelado");
        }

        plano.setValor(valor);
        plano = planoDentalRepository.save(plano);
        log.info("Valor do plano dental atualizado: ID {}, Novo valor: {}", plano.getId(), valor);
        return planoDentalMapper.toResponse(plano);
=======
    public List<PlanoDentalResponse> listarTodos() {
        return repository.findAll().stream()
                .filter(p -> !p.getDeletado())
                .map(mapper::toResponse)
                .toList();
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public List<PlanoDentalResponse> findByPacienteId(Long pacienteId) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByPacienteIdAndAtivoTrue(pacienteId)
        );
=======
    public List<PlanoDentalResponse> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteIdAndDeletadoFalse(pacienteId).stream()
                .map(mapper::toResponse)
                .toList();
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public Page<PlanoDentalResponse> findByPacienteId(Long pacienteId, Pageable pageable) {
        return planoDentalRepository.findByPacienteId(pacienteId, pageable)
                .map(planoDentalMapper::toResponse);
=======
    public List<PlanoDentalResponse> listarPorDentista(Long dentistaId) {
        return repository.findByDentistaIdAndDeletadoFalse(dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public List<PlanoDentalResponse> findByDentistaId(Long dentistaId) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByDentistaIdAndAtivoTrue(dentistaId)
        );
=======
    public List<PlanoDentalResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId) {
        return repository.findByPacienteAndDentista(pacienteId, dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional(readOnly = true)
<<<<<<< HEAD
    public Page<PlanoDentalResponse> findByDentistaId(Long dentistaId, Pageable pageable) {
        return planoDentalRepository.findByDentistaId(dentistaId, pageable)
                .map(planoDentalMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByStatus(StatusPlano status) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByStatus(status)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanoDentalResponse> findByStatus(StatusPlano status, Pageable pageable) {
        return planoDentalRepository.findByStatus(status, pageable)
                .map(planoDentalMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findAtivosByPacienteIdOrderByDataPrevista(Long pacienteId) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findAtivosByPacienteIdOrderByDataPrevista(pacienteId)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByDataPrevistaBetween(LocalDateTime inicio, LocalDateTime fim) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByDataPrevistaBetween(inicio, fim)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findUrgentesByStatus(StatusPlano status) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findUrgentesByStatus(status)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByPacienteIdAndStatus(Long pacienteId, StatusPlano status) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByPacienteIdAndStatus(pacienteId, status)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByDentistaIdAndStatus(Long dentistaId, StatusPlano status) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByDentistaIdAndStatus(dentistaId, status)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPorPacienteAndStatus(Long pacienteId, StatusPlano status) {
        BigDecimal total = planoDentalRepository.sumValorFinalByPacienteIdAndStatus(pacienteId, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAtivosByPacienteId(Long pacienteId) {
        return planoDentalRepository.countAtivosByPacienteId(pacienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByStatusIn(List<StatusPlano> statuses) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByStatusInOrderByDataPrevista(statuses)
        );
    }

    @Override
    public PlanoDentalResponse ativar(Long id) {
        PlanoDental plano = planoDentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado"));

        plano.ativar();
        plano = planoDentalRepository.save(plano);
        log.info("Plano dental reativado: ID {}", plano.getId());
        return planoDentalMapper.toResponse(plano);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> findByPacienteIdAndDentistaIdAndStatus(Long pacienteId, Long dentistaId, StatusPlano status) {
        return planoDentalMapper.toResponseList(
                planoDentalRepository.findByPacienteIdAndDentistaIdAndStatus(pacienteId, dentistaId, status)
        );
    }
}
=======
    public List<PlanoDentalResponse> listarPorStatus(String status) {
        PlanoDental.StatusPlano statusEnum = PlanoDental.StatusPlano.valueOf(status.toUpperCase());
        return repository.findByStatusAndDeletadoFalse(statusEnum).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public PlanoDentalResponse atualizar(Long id, PlanoDentalRequest request) {
        log.info("Atualizando plano dental {}", id);

        PlanoDental plano = findById(id);
        mapper.updateEntityFromRequest(request, plano);
        plano = repository.save(plano);

        log.info("Plano dental {} atualizado", id);
        return mapper.toResponse(plano);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando plano dental {}", id);

        PlanoDental plano = findById(id);
        plano.setDeletado(true);
        repository.save(plano);

        log.info("Plano dental {} deletado", id);
    }

    private PlanoDental findById(Long id) {
        return repository.findById(id)
                .filter(p -> !p.getDeletado())
                .orElseThrow(() -> new ResourceNotFoundException("Plano dental não encontrado com id: " + id));
    }
}
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
