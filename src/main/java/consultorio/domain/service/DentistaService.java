package consultorio.domain.service;

import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface DentistaService {

    // ==================== CRUD ====================

    DentistaResponse criar(DentistaRequest request);

    DentistaResponse buscarPorId(Long id);

    DentistaResponse buscarPorCro(String cro);

    DentistaResponse atualizar(Long id, DentistaRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS ====================

    Page<DentistaResponse> listarTodos(Pageable pageable);

    Page<DentistaResponse> listarPorStatus(Boolean ativo, Pageable pageable);

    Page<DentistaResponse> listarPorEspecialidade(String especialidade, Pageable pageable);

    Page<DentistaResponse> buscar(String termo, Pageable pageable);

    Page<DentistaResponse> buscar(String termo, Boolean ativo, Pageable pageable);

    List<DentistaResponse> listarAtivos();

    // ==================== STATUS ====================

    void ativar(Long id);

    void inativar(Long id);

    void alterarStatusEmLote(List<Long> ids, Boolean ativo);

    // ==================== DISPONIBILIDADE ====================

    List<DentistaResponse> buscarDisponiveis(LocalDate data, LocalTime horaInicio, LocalTime horaFim);

    boolean verificarDisponibilidade(Long id, LocalDate data, LocalTime horaInicio, LocalTime horaFim);

    // ==================== ESTATÍSTICAS ====================

    Map<String, Object> obterEstatisticas();

    List<String> listarEspecialidades();

    List<Map<String, Object>> listarAgendaDoDia(LocalDate data);

    // ==================== AUTOCOMPLETE ====================

    List<Map<String, Object>> autocompleteNome(String prefix);
}