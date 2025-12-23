package consultorio.api.mapper;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .role(request.role())
                .ativo(true)
                .build();
    }

    public UserResponse toResponse(User entity) {
        return new UserResponse(
                entity.getId(),
                entity.getNome(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getUltimoLogin()
        );
    }

    public List<UserResponse> toResponseList(List<User> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public void updateEntityFromRequest(UserRequest request, User entity) {
        entity.setNome(request.nome());
        entity.setUsername(request.username());
        entity.setEmail(request.email());
        entity.setRole(request.role());

        if (request.password() != null && !request.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(request.password()));
        }
    }
}