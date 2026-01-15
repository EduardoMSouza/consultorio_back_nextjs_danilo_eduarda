package consultorio.domain.service.impl;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.api.mapper.pessoa.DentistaMapper;
import consultorio.domain.entity.pessoa.Dentista;
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
        validarEmailUnico(request.getEmail(), null);
        validarCroUnico(request.getCro(), null);

        Dentista dentista = mapper.toEntity(request);
        Dentista salvo = repository.save(dentista);
        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public DentistaResponse buscarPorId(Long id) {
        Dentista dentista = encontrarPorId(id);
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarAtivos(Pageable pageable) {
        return repository.findByAtivoTrue(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public DentistaResponse atualizar(Long id, DentistaRequest request) {
        Dentista dentista = encontrarPorId(id);
        validarEmailUnico(request.getEmail(), id);

        mapper.updateEntity(request, dentista);
        Dentista atualizado = repository.save(dentista);
        return mapper.toResponse(atualizado);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        Dentista dentista = encontrarPorId(id);
        dentista.desativar();
        repository.save(dentista);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        Dentista dentista = encontrarPorId(id);
        dentista.ativar();
        repository.save(dentista);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Dentista dentista = encontrarPorId(id);
        repository.delete(dentista);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DentistaResponse> buscarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContaining(nome, pageable).map(mapper::toResponse);
    }

    private Dentista encontrarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado com ID: " + id));
    }

    private void validarEmailUnico(String email, Long idAtual) {
        repository.findByEmail(email.toLowerCase())
                .ifPresent(dentista -> {
                    if (idAtual == null || !dentista.getId().equals(idAtual)) {
                        throw new RuntimeException("Email já cadastrado: " + email);
                    }
                });
    }

    private void validarCroUnico(String cro, Long idAtual) {
        repository.findByCro(cro.toUpperCase())
                .ifPresent(dentista -> {
                    if (idAtual == null || !dentista.getId().equals(idAtual)) {
                        throw new RuntimeException("CRO já cadastrado: " + cro);
                    }
                });
    }
}