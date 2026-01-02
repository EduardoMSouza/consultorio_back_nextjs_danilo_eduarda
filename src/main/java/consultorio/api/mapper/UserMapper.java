package consultorio.api.mapper;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRequest request) {
        return User.builder()
                .nome(request.nome())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : User.Role.RECEPCIONISTA)
                .ativo(true)
                .build();
    }

    public void updateEntityFromRequest(UserRequest request, User user) {
        user.setNome(request.nome());
        user.setUsername(request.username());
        user.setEmail(request.email());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.role() != null) {
            user.setRole(request.role());
        }
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNome(user.getNome());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setAtivo(user.getAtivo());
        response.setUltimoLogin(user.getUltimoLogin());
        response.setCriadoEm(user.getCriadoEm());
        response.setAtualizadoEm(user.getAtualizadoEm());
        response.setCriadoPor(user.getCriadoPor());
        return response;
    }
}