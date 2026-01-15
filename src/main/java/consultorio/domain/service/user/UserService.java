package consultorio.domain.service.user;

import consultorio.api.dto.request.pessoa.UserRequest;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.domain.entity.pessoa.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface UserService {

    // ==================== CRUD ====================

    UserResponse criar(UserRequest request, String criadoPor);

    UserResponse buscarPorId(Long id);

    UserResponse buscarPorUsername(String username);

    UserResponse buscarPorEmail(String email);

    UserResponse atualizar(Long id, UserRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS ====================

    Page<UserResponse> listarTodos(Pageable pageable);

    Page<UserResponse> listarPorStatus(Boolean ativo, Pageable pageable);

    Page<UserResponse> listarPorRole(User.Role role, Pageable pageable);

    Page<UserResponse> buscar(String termo, Pageable pageable);

    // ==================== STATUS ====================

    void ativar(Long id);

    void inativar(Long id);

    // ==================== ADMIN ====================

    void criarAdminInicial();

    // ==================== ESTATÍSTICAS ====================

    Map<String, Object> obterEstatisticas();
}