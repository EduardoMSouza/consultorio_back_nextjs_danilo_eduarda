package consultorio.domain.service.impl;

import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.DentistaMapper;
import consultorio.domain.entity.Dentista;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.service.DentistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DentistaServiceImpl implements DentistaService {

    private final DentistaRepository repository;
    private final DentistaMapper mapper;

    @Override
    @Transactional
    public DentistaResponse criar(DentistaRequest request) {
        validarCroDuplicado(request.getCro(), null);

        Dentista dentista = mapper.toEntity(request);
        dentista = repository.save(dentista);

        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional(readOnly = true)
    public DentistaResponse buscarPorId(Long id) {
        Dentista dentista = findById(id);
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional(readOnly = true)
    public DentistaResponse buscarPorCro(String cro) {
        Dentista dentista = repository.findByCro(cro)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com CRO: " + cro));
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarPorStatus(Boolean ativo, Pageable pageable) {
        return repository.findByAtivo(ativo, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarPorEspecialidade(String especialidade, Pageable pageable) {
        return repository.findByEspecialidade(especialidade, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> buscar(String termo, Pageable pageable) {
        return repository.buscarPorTermo(termo, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public DentistaResponse atualizar(Long id, DentistaRequest request) {
        Dentista dentista = findById(id);
        validarCroDuplicado(request.getCro(), id);

        mapper.updateEntityFromRequest(request, dentista);
        dentista = repository.save(dentista);

        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        Dentista dentista = findById(id);
        dentista.setAtivo(false);
        repository.save(dentista);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        Dentista dentista = findById(id);
        dentista.setAtivo(true);
        repository.save(dentista);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dentista não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Dentista findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com id: " + id));
    }

    private void validarCroDuplicado(String cro, Long idAtual) {
        if (cro != null && !cro.isBlank()) {
            repository.findByCro(cro)
                    .filter(d -> !d.getId().equals(idAtual))
                    .ifPresent(d -> {
                        throw new BusinessException("Já existe um dentista cadastrado com este CRO");
                    });
        }
    }
}