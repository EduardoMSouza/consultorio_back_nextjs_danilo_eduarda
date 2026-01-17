package consultorio.domain.service.dentista.impl;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.api.dto.response.pessoa.DentistaResumoResponse;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.exception.BusinessException;
import consultorio.api.mapper.pessoa.DentistaMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.repository.pessoa.DentistaRepository;

import consultorio.domain.service.dentista.DentistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DentistaServiceImpl implements DentistaService {

    private final DentistaRepository repository;
    private final DentistaMapper mapper;

    @Override
    @Transactional
    public DentistaResponse criar(DentistaRequest request) {
        log.info("Criando dentista: {}", request.getNome());

        validarEmailUnico(request.getEmail(), null);
        validarCroUnico(request.getCro(), null);

        Dentista dentista = mapper.toEntity(request);
        dentista = repository.save(dentista);

        log.info("Dentista criado com ID: {}", dentista.getId());
        return mapper.toResponse(dentista);
    }

    @Override
    public DentistaResponse buscarPorId(Long id) {
        log.debug("Buscando dentista por ID: {}", id);
        Dentista dentista = findById(id);
        return mapper.toResponse(dentista);
    }

    @Override
    public DentistaResponse buscarPorCro(String cro) {
        log.debug("Buscando dentista por CRO: {}", cro);
        Dentista dentista = repository.findByCro(cro)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com CRO: " + cro));
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional
    public DentistaResponse atualizar(Long id, DentistaRequest request) {
        log.info("Atualizando dentista ID: {}", id);

        Dentista dentista = findById(id);
        validarEmailUnico(request.getEmail(), id);
        validarCroUnico(request.getCro(), id);

        mapper.updateEntity(request, dentista);
        dentista = repository.save(dentista);

        log.info("Dentista ID: {} atualizado", id);
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando dentista ID: {}", id);

        Dentista dentista = findById(id);

        if (!dentista.getAgendamentos().isEmpty()) {
            throw new BusinessException("Não é possível excluir dentista com agendamentos vinculados");
        }

        repository.delete(dentista);
        log.info("Dentista ID: {} deletado", id);
    }

    @Override
    public Page<DentistaResponse> listarTodos(Pageable pageable) {
        log.debug("Listando todos dentistas - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DentistaResponse> listarAtivos(Pageable pageable) {
        log.debug("Listando dentistas ativos");
        return repository.findByAtivoTrue(pageable).map(mapper::toResponse);
    }

    public Page<DentistaResumoResponse> listarTodosResumo(Pageable pageable) {
        log.debug("Listando todos dentistas (resumo)");
        return repository.findAll(pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<DentistaResponse> buscarPorNome(String nome, Pageable pageable) {
        log.debug("Buscando dentistas por nome: {}", nome);
        return repository.findByNomeContaining(nome, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DentistaResponse> buscarPorTermo(String termo, Pageable pageable) {
        log.debug("Buscando dentistas por termo: {}", termo);
        return repository.buscarPorTermo(termo, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        log.info("Ativando dentista ID: {}", id);
        alterarStatus(id, true);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info("Desativando dentista ID: {}", id);
        alterarStatus(id, false);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existePorCro(String cro) {
        return repository.existsByCro(cro);
    }

    // ============ MÉTODOS PRIVADOS ============

    private Dentista findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com ID: " + id));
    }

    private void alterarStatus(Long id, boolean ativo) {
        Dentista dentista = findById(id);
        if (ativo) {
            dentista.ativar();
        } else {
            dentista.desativar();
        }
        repository.save(dentista);
    }

    private void validarEmailUnico(String email, Long idAtual) {
        if (email != null && !email.isBlank()) {
            boolean emailExiste = idAtual == null
                    ? repository.existsByEmail(email)
                    : repository.findByEmail(email)
                    .map(d -> !d.getId().equals(idAtual))
                    .orElse(false);

            if (emailExiste) {
                throw new BusinessException("Já existe um dentista cadastrado com este email");
            }
        }
    }

    private void validarCroUnico(String cro, Long idAtual) {
        if (cro != null && !cro.isBlank()) {
            boolean croExiste = idAtual == null
                    ? repository.existsByCro(cro)
                    : repository.existsByCroAndIdNot(cro, idAtual);

            if (croExiste) {
                throw new BusinessException("Já existe um dentista cadastrado com este CRO");
            }
        }
    }
}