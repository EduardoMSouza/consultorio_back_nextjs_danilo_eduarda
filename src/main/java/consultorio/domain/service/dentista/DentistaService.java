package consultorio.domain.service.dentista;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.api.dto.response.pessoa.DentistaResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistaService {

    // CRUD básico
    DentistaResponse criar(DentistaRequest request);
    DentistaResponse buscarPorId(Long id);
    DentistaResponse buscarPorCro(String cro);
    DentistaResponse atualizar(Long id, DentistaRequest request);
    void deletar(Long id);

    // Listagens
    Page<DentistaResponse> listarTodos(Pageable pageable);
    Page<DentistaResponse> listarAtivos(Pageable pageable);
    Page<DentistaResponse> buscarPorNome(String nome, Pageable pageable);
    Page<DentistaResponse> buscarPorTermo(String termo, Pageable pageable);
    Page<DentistaResumoResponse> listarTodosResumo(Pageable pageable);

    // Status
    void ativar(Long id);
    void desativar(Long id);

    // Validações
    boolean existePorEmail(String email);
    boolean existePorCro(String cro);
}