package consultorio.domain.service;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserResponse criar(UserRequest request, String criadoPor);

    UserResponse buscarPorId(Long id);

    UserResponse buscarPorUsername(String username);

    Page<UserResponse> listarTodos(Pageable pageable);

    Page<UserResponse> listarPorStatus(Boolean ativo, Pageable pageable);

    Page<UserResponse> listarPorRole(User.Role role, Pageable pageable);

    UserResponse atualizar(Long id, UserRequest request);

    void inativar(Long id);

    void ativar(Long id);

    void deletar(Long id);

    void criarAdminInicial();
}