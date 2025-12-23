package consultorio.domain.service.impl;

import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.EvolucaoTratamentoMapper;
import consultorio.domain.entity.EvolucaoTratamento;
import consultorio.domain.repository.EvolucaoTratamentoRepository;
import consultorio.domain.service.EvolucaoTratamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvolucaoTratamentoServiceImpl implements EvolucaoTratamentoService {

    private final EvolucaoTratamentoRepository repository;
    private final EvolucaoTratamentoMapper mapper;

    @Override
    @Transactional
    public EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request) {
        log.info("Criando evolução de tratamento");

        EvolucaoTratamento evolucao = mapper.toEntity(request);
        evolucao = repository.save(evolucao);

        log.info("Evolução de tratamento {} criada", evolucao.getId());
        return mapper.toResponse(evolucao);
    }

    @Override
    @Transactional(readOnly = true)
    public EvolucaoTratamentoResponse buscarPorId(Long id) {
        EvolucaoTratamento evolucao = findById(id);
        return mapper.toResponse(evolucao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarTodos() {
        return repository.findAll().stream()
                .filter(e -> !e.getDeletado())
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteIdAndDeletadoFalseOrderByDataEvolucaoDesc(pacienteId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorDentista(Long dentistaId) {
        return repository.findByDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId) {
        return repository.findByPacienteIdAndDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(pacienteId, dentistaId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvolucaoTratamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataEvolucaoBetweenAndDeletadoFalse(inicio, fim).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request) {
        log.info("Atualizando evolução de tratamento {}", id);

        EvolucaoTratamento evolucao = findById(id);
        mapper.updateEntityFromRequest(request, evolucao);
        evolucao = repository.save(evolucao);

        log.info("Evolução de tratamento {} atualizada", id);
        return mapper.toResponse(evolucao);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando evolução de tratamento {}", id);

        EvolucaoTratamento evolucao = findById(id);
        evolucao.setDeletado(true);
        repository.save(evolucao);

        log.info("Evolução de tratamento {} deletada", id);
    }

    private EvolucaoTratamento findById(Long id) {
        return repository.findById(id)
                .filter(e -> !e.getDeletado())
                .orElseThrow(() -> new ResourceNotFoundException("Evolução de tratamento não encontrada com id: " + id));
    }
}
