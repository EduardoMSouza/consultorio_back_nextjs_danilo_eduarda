package consultorio.domain.service;

<<<<<<< HEAD
import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

=======
import consultorio.api.dto.request.PacienteRequest;
import consultorio.api.dto.response.PacienteResponse;
import consultorio.api.dto.response.PacienteResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import java.util.List;
import java.util.Map;

public interface PacienteService {

    // ==================== CRUD ====================

    PacienteResponse criar(PacienteRequest request);

    PacienteResponse buscarPorId(Long id);

    PacienteResponse buscarPorProntuario(String prontuarioNumero);

    PacienteResponse buscarPorCpf(String cpf);

    PacienteResponse atualizar(Long id, PacienteRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS ====================

    Page<PacienteResumoResponse> listarTodos(Pageable pageable);

    Page<PacienteResumoResponse> listarPorStatus(Boolean status, Pageable pageable);

    Page<PacienteResumoResponse> buscar(String termo, Pageable pageable);

    Page<PacienteResumoResponse> buscar(String termo, Boolean status, Pageable pageable);

    Page<PacienteResumoResponse> listarPorConvenio(String nomeConvenio, Pageable pageable);

    // ==================== STATUS ====================

    void ativar(Long id);

    void inativar(Long id);

    void alterarStatusEmLote(List<Long> ids, Boolean status);

    // ==================== ANIVERSARIANTES ====================

    List<PacienteResumoResponse> buscarAniversariantesHoje();

    List<PacienteResumoResponse> buscarAniversariantesDoMes(int mes);

    // ==================== FILTROS SAÚDE ====================

    List<PacienteResumoResponse> buscarPacientesRisco();

    List<PacienteResumoResponse> buscarGestantes();

    List<PacienteResumoResponse> buscarAlergicos();

    // ==================== ESTATÍSTICAS ====================

    Map<String, Object> obterEstatisticas();

    Map<String, Long> obterEstatisticasPorConvenio();

    // ==================== AUTOCOMPLETE ====================

    List<Map<String, Object>> autocompleteNome(String prefix);

    List<Map<String, Object>> autocompleteCpf(String prefix);
}