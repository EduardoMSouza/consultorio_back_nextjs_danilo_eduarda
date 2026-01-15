package consultorio.domain.service;

<<<<<<< HEAD
import consultorio.api.dto.request.pessoa.UserRequest;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.domain.entity.pessoa.User;
=======
import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
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