package consultorio.domain.service.impl;

<<<<<<< HEAD
import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.api.mapper.pessoa.DentistaMapper;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.service.DentistaService;
import lombok.RequiredArgsConstructor;
=======
import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.exception.ResourceNotFoundException;
import consultorio.api.mapper.DentistaMapper;
import consultorio.domain.entity.Dentista;
import consultorio.domain.repository.DentistaRepository;
import consultorio.domain.service.DentistaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
@Service
@RequiredArgsConstructor
=======
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
public class DentistaServiceImpl implements DentistaService {

    private final DentistaRepository repository;
    private final DentistaMapper mapper;

<<<<<<< HEAD
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
=======
    private static final String CACHE_ESTATISTICAS = "estatisticasDentistas";
    private static final String CACHE_ESPECIALIDADES = "especialidades";

    // ==================== CRUD ====================

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_ESTATISTICAS, CACHE_ESPECIALIDADES}, allEntries = true)
    public DentistaResponse criar(DentistaRequest request) {
        validarCroDuplicado(request.getCro(), null);

        Dentista dentista = mapper.toEntity(request);
        dentista = repository.save(dentista);

        log.info("Dentista criado: id={}, nome={}", dentista.getId(), dentista.getNome());
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return mapper.toResponse(dentista);
    }

    @Override
<<<<<<< HEAD
    @Transactional(readOnly = true)
=======
    public DentistaResponse buscarPorId(Long id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    public DentistaResponse buscarPorCro(String cro) {
        Dentista dentista = repository.findByCro(cro)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com CRO: " + cro));
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_ESTATISTICAS, CACHE_ESPECIALIDADES}, allEntries = true)
    public DentistaResponse atualizar(Long id, DentistaRequest request) {
        Dentista dentista = findById(id);
        validarCroDuplicado(request.getCro(), id);

        mapper.updateEntityFromRequest(request, dentista);
        dentista = repository.save(dentista);

        log.info("Dentista atualizado: id={}", id);
        return mapper.toResponse(dentista);
    }

    @Override
    @Transactional
    @CacheEvict(value = {CACHE_ESTATISTICAS, CACHE_ESPECIALIDADES}, allEntries = true)
    public void deletar(Long id) {
        Dentista dentista = findById(id);

        if (!dentista.getAgendamentos().isEmpty()) {
            throw new BusinessException("Não é possível excluir dentista com agendamentos vinculados. Inative-o.");
        }

        repository.delete(dentista);
        log.info("Dentista deletado: id={}", id);
    }

    // ==================== LISTAGENS ====================

    @Override
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public Page<DentistaResponse> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
<<<<<<< HEAD
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
=======
    public Page<DentistaResponse> listarPorStatus(Boolean ativo, Pageable pageable) {
        return repository.findByAtivo(ativo, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DentistaResponse> listarPorEspecialidade(String especialidade, Pageable pageable) {
        return repository.findByEspecialidade(especialidade, pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DentistaResponse> buscar(String termo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listarTodos(pageable);
        }
        return repository.buscarPorTermo(termo.trim(), pageable).map(mapper::toResponse);
    }

    @Override
    public Page<DentistaResponse> buscar(String termo, Boolean ativo, Pageable pageable) {
        if (termo == null || termo.isBlank()) {
            return listarPorStatus(ativo, pageable);
        }
        return repository.buscarPorTermoEStatus(termo.trim(), ativo, pageable).map(mapper::toResponse);
    }

    @Override
    public List<DentistaResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    // ==================== STATUS ====================

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void ativar(Long id) {
        alterarStatus(id, true);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }

    @Override
    @Transactional
<<<<<<< HEAD
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
=======
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void inativar(Long id) {
        alterarStatus(id, false);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_ESTATISTICAS, allEntries = true)
    public void alterarStatusEmLote(List<Long> ids, Boolean ativo) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Lista de IDs não pode ser vazia");
        }
        int atualizados = repository.updateStatusEmLote(ids, ativo);
        log.info("Status alterado em lote: {} dentistas atualizados para ativo={}", atualizados, ativo);
    }

    // ==================== DISPONIBILIDADE ====================

    @Override
    public List<DentistaResponse> buscarDisponiveis(LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        validarHorario(horaInicio, horaFim);
        return repository.findDisponiveis(data, horaInicio, horaFim)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean verificarDisponibilidade(Long id, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dentista não encontrado com id: " + id);
        }
        validarHorario(horaInicio, horaFim);
        return repository.isDisponivel(id, data, horaInicio, horaFim);
    }

    // ==================== ESTATÍSTICAS ====================

    @Override
    @Cacheable(value = CACHE_ESTATISTICAS)
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("total", repository.count());
        stats.put("ativos", repository.countAtivos());
        stats.put("inativos", repository.countInativos());
        stats.put("porEspecialidade", obterEstatisticasPorEspecialidade());

        return stats;
    }

    @Override
    @Cacheable(value = CACHE_ESPECIALIDADES)
    public List<String> listarEspecialidades() {
        return repository.listarEspecialidades();
    }

    @Override
    public List<Map<String, Object>> listarAgendaDoDia(LocalDate data) {
        return repository.listarComQuantidadeAgendamentos(data)
                .stream()
                .map(row -> {
                    Dentista d = (Dentista) row[0];
                    Long qtd = (Long) row[1];
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", d.getId());
                    map.put("nome", d.getNome());
                    map.put("especialidade", d.getEspecialidade());
                    map.put("quantidadeAgendamentos", qtd);
                    return map;
                })
                .collect(Collectors.toList());
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

    // ==================== MÉTODOS AUXILIARES ====================

    private Dentista findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentista não encontrado com id: " + id));
    }

    private void alterarStatus(Long id, Boolean ativo) {
        Dentista dentista = findById(id);
        dentista.setAtivo(ativo);
        repository.save(dentista);
        log.info("Status do dentista alterado: id={}, ativo={}", id, ativo);
    }

    private void validarCroDuplicado(String cro, Long idAtual) {
        if (cro == null || cro.isBlank()) return;

        boolean duplicado = idAtual == null
                ? repository.existsByCro(cro)
                : repository.existsByCroExcludingId(cro, idAtual);

        if (duplicado) {
            throw new BusinessException("Já existe um dentista cadastrado com este CRO");
        }
    }

    private void validarHorario(LocalTime horaInicio, LocalTime horaFim) {
        if (horaInicio == null || horaFim == null) {
            throw new BusinessException("Horário de início e fim são obrigatórios");
        }
        if (!horaFim.isAfter(horaInicio)) {
            throw new BusinessException("Horário de fim deve ser após o horário de início");
        }
    }

    private Map<String, Long> obterEstatisticasPorEspecialidade() {
        return repository.countPorEspecialidade()
                .stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Object> mapAutocomplete(Object[] row) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", row[0]);
        map.put("nome", row[1]);
        map.put("cro", row[2]);
        map.put("especialidade", row[3]);
        return map;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    }
}