package consultorio.domain.service.impl;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.pessoa.PacienteMapper;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.repository.PacienteRepository;
import consultorio.domain.service.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository repository;
    private final PacienteMapper mapper;

    private static final String CACHE_ESTATISTICAS = "estatisticasPacientes";

    // ==================== CRUD ====================

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public PacienteResponse criar(PacienteRequest request) {
        validarDuplicidade(request, null);

        Paciente paciente = mapper.toEntity(request);
        paciente = repository.save(paciente);

        log.info("Paciente criado: id={}, nome={}", paciente.getId(), paciente.getDadosBasicos().getNome());
        return mapper.toResponse(paciente);
    }

    @Override
    public PacienteResponse buscarPorId(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    public PacienteResponse buscarPorProntuario(String prontuarioNumero) {
        Paciente paciente = repository.findByDadosBasicosProntuarioNumero(prontuarioNumero)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com prontuário: " + prontuarioNumero));
        return mapper.toResponse(paciente);
    }

    @Override
    public PacienteResponse buscarPorCpf(String cpf) {
        Paciente paciente = repository.findByDadosBasicosCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com CPF: " + cpf));
        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public PacienteResponse atualizar(Long id, PacienteRequest request) {
        Paciente paciente = findById(id);
        validarDuplicidade(request, id);

        mapper.updateEntityFromRequest(request, paciente);
        paciente = repository.save(paciente);

        log.info("Paciente atualizado: id={}", id);
        return mapper.toResponse(paciente);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente não encontrado com id: " + id);
        }
        repository.deleteById(id);
        log.info("Paciente deletado: id={}", id);
    }

    // ==================== LISTAGENS ====================

    @Override
    public Page<PacienteResumoResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<PacienteResumoResponse> listarPorStatus(Boolean status, Pageable pageable) {
        return repository.findByDadosBasicosStatus(status, pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<PacienteResumoResponse> buscar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listarTodos(pageable);
        }
        return repository.buscarPorTermo(termo.trim(), pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<PacienteResumoResponse> buscar(String termo, Boolean status, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listarPorStatus(status, pageable);
        }
        return repository.buscarPorTermoEStatus(termo.trim(), status, pageable).map(mapper::toResumoResponse);
    }

    @Override
    public Page<PacienteResumoResponse> listarPorConvenio(String nomeConvenio, Pageable pageable) {
        return repository.findByConvenio(nomeConvenio, pageable).map(mapper::toResumoResponse);
    }

    // ==================== STATUS ====================

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void ativar(Long id) {
        alterarStatus(id, true);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void inativar(Long id) {
        alterarStatus(id, false);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void alterarStatusEmLote(List<Long> ids, Boolean status) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Lista de IDs não pode ser vazia");
        }
        int atualizados = repository.updateStatusEmLote(ids, status);
        log.info("Status alterado em lote: {} pacientes atualizados para status={}", atualizados, status);
    }

    // ==================== ANIVERSARIANTES ====================

    @Override
    public List<PacienteResumoResponse> buscarAniversariantesHoje() {
        LocalDate hoje = LocalDate.now();
        return repository.findAniversariantesHoje(hoje.getMonthValue(), hoje.getDayOfMonth())
                .stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PacienteResumoResponse> buscarAniversariantesDoMes(int mes) {
        if (mes < 1 || mes > 12) {
            throw new BusinessException("Mês deve estar entre 1 e 12");
        }
        return repository.findAniversariantesDoMes(mes)
                .stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    // ==================== FILTROS SAÚDE ====================

    @Override
    public List<PacienteResumoResponse> buscarPacientesRisco() {
        return repository.findPacientesRisco()
                .stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PacienteResumoResponse> buscarGestantes() {
        return repository.findGestantes()
                .stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PacienteResumoResponse> buscarAlergicos() {
        return repository.findAlergicos()
                .stream()
                .map(mapper::toResumoResponse)
                .collect(Collectors.toList());
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    @Cacheable(value = CACHE_ESTATISTICAS)
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalPacientes", repository.count());
        stats.put("ativos", repository.countAtivos());
        stats.put("inativos", repository.countInativos());
        stats.put("comConvenio", repository.countComConvenio());
        stats.put("particulares", repository.countParticular());
        stats.put("cadastrosAnoAtual", obterCadastrosPorMes(Year.now().getValue()));

        return stats;
    }

    @Override
    public Map<String, Long> obterEstatisticasPorConvenio() {
        return repository.countPorConvenio()
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private Map<Integer, Long> obterCadastrosPorMes(int ano) {
        return repository.countCadastrosPorMes(ano)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Integer) row[0],
                        row -> (Long) row[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // ==================== AUTOCOMPLETE ====================

    @Override
    public List<Map<String, Object>> autocompleteNome(String prefix) {
        if (prefix == null || prefix.length() < 2) {
            return List.of();
        }
        return repository.autocompleteNome(prefix.trim())
                .stream()
                .map(this::mapAutocomplete)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> autocompleteCpf(String prefix) {
        if (prefix == null || prefix.length() < 3) {
            return List.of();
        }
        return repository.autocompleteCpf(prefix.trim())
                .stream()
                .map(this::mapAutocomplete)
                .collect(Collectors.toList());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private Paciente findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com id: " + id));
    }

    private void alterarStatus(Long id, Boolean status) {
        Paciente paciente = findById(id);
        paciente.getDadosBasicos().setStatus(status);
        repository.save(paciente);
        log.info("Status do paciente alterado: id={}, status={}", id, status);
    }

    private void validarDuplicidade(PacienteRequest request, Long idAtual) {
        String cpf = request.getDadosBasicos().getCpf();
        String prontuario = request.getDadosBasicos().getProntuarioNumero();

        if (cpf != null && !cpf.isBlank()) {
            boolean cpfDuplicado = idAtual == null
                    ? repository.existsByDadosBasicosCpf(cpf)
                    : repository.existsByCpfExcludingId(cpf, idAtual);

            if (cpfDuplicado) {
                throw new BusinessException("Já existe um paciente cadastrado com este CPF");
            }
        }

        if (prontuario != null && !prontuario.isBlank()) {
            repository.findByDadosBasicosProntuarioNumero(prontuario)
                    .filter(p -> !p.getId().equals(idAtual))
                    .ifPresent(p -> {
                        throw new BusinessException("Já existe um paciente cadastrado com este número de prontuário");
                    });
        }
    }

    private Map<String, Object> mapAutocomplete(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[0]);
        map.put("nome", row[1]);
        map.put("cpf", row[2]);
        return map;
    }
}