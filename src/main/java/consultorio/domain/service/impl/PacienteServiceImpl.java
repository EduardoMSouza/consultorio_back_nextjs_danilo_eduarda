package consultorio.api.mapper.service.impl;

import consultorio.api.dto.request.PacienteRequest;
import consultorio.api.dto.response.PacienteResponse;
import consultorio.api.dto.response.PacienteResumoResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.PacienteMapper;
import consultorio.domain.entity.Paciente;

import consultorio.domain.repository.PacienteRepository;
import consultorio.api.mapper.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    @Override
    @Transactional
    public PacienteResponse criar(PacienteRequest request) {
        validarDuplicidade(request, null);

        Paciente paciente = mapper.toEntity(request);
        paciente = repository.save(paciente);

        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = findById(id);
        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPorProntuario(String prontuarioNumero) {
        Paciente paciente = repository.findByDadosBasicosProntuarioNumero(prontuarioNumero)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com prontuário: " + prontuarioNumero));
        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResumoResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResumoResponse> listarPorStatus(Boolean status, Pageable pageable) {
        return repository.findByDadosBasicosStatus(status, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResumoResponse> buscar(String termo, Pageable pageable) {
        return repository.buscarPorTermo(termo, pageable)
                .map(mapper::toResumoResponse);
    }

    @Override
    @Transactional
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        Paciente paciente = findById(id);
        validarDuplicidade(request, id);

        mapper.updateEntityFromRequest(request, paciente);
        paciente = repository.save(paciente);

        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional
    public void inativar(Long id) {
        Paciente paciente = findById(id);
        paciente.getDadosBasicos().setStatus(false);
        repository.save(paciente);
    }

    @Override
    @Transactional
    public void ativar(Long id) {
        Paciente paciente = findById(id);
        paciente.getDadosBasicos().setStatus(true);
        repository.save(paciente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Paciente findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    private void validarDuplicidade(PacienteRequest request, Long idAtual) {
        String cpf = request.getDadosBasicos().getCpf();
        String prontuario = request.getDadosBasicos().getProntuarioNumero();

        // Valida CPF duplicado
        if (cpf != null && !cpf.isBlank()) {
            repository.findByDadosBasicosCpf(cpf)
                    .filter(p -> !p.getId().equals(idAtual))
                    .ifPresent(p -> {
                        throw new BusinessException("Já existe um paciente cadastrado com este CPF");
                    });
        }

        // Valida prontuário duplicado
        if (prontuario != null && !prontuario.isBlank()) {
            repository.findByDadosBasicosProntuarioNumero(prontuario)
                    .filter(p -> !p.getId().equals(idAtual))
                    .ifPresent(p -> {
                        throw new BusinessException("Já existe um paciente cadastrado com este número de prontuário");
                    });
        }
    }
}