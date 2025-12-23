package consultorio.domain.service.impl;

import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.PlanoDentalMapper;
import consultorio.domain.entity.PlanoDental;
import consultorio.domain.repository.PlanoDentalRepository;
import consultorio.domain.service.PlanoDentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
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
    }

    @Override
    @Transactional(readOnly = true)
    public PlanoDentalResponse buscarPorId(Long id) {
        PlanoDental plano = findById(id);
        return mapper.toResponse(plano);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> listarTodos() {
        return repository.findAll().stream()
                .filter(p -> !p.getDeletado())
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteIdAndDeletadoFalse(pacienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> listarPorDentista(Long dentistaId) {
        return repository.findByDentistaIdAndDeletadoFalse(dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanoDentalResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId) {
        return repository.findByPacienteAndDentista(pacienteId, dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
