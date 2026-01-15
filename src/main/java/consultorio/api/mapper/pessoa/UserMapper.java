package consultorio.api.mapper.pessoa;

import consultorio.api.dto.request.pessoa.UserRequest;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.domain.entity.pessoa.User;
import consultorio.domain.repository.pessoa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User toEntity(UserRequest request) {
        validateUniqueFields(request, null);

        return User.builder()
                .nome(request.nome())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role() != null ? request.role() : User.Role.RECEPCIONISTA)
                .ativo(true)
                .build();
    }

    @Transactional(readOnly = true)
    public void updateEntityFromRequest(UserRequest request, User user) {
        validateUniqueFields(request, user.getId());

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

    private void validateUniqueFields(UserRequest request, Long excludeId) {
        if (excludeId == null) {
            // Criar novo usuário
            if (userRepository.existsByUsername(request.username())) {
                throw new BusinessException("Username já está em uso");
            }
            if (userRepository.existsByEmail(request.email())) {
                throw new BusinessException("Email já está cadastrado");
            }
        } else {
            // Atualizar usuário existente
            if (userRepository.existsByUsernameExcludingId(request.username(), excludeId)) {
                throw new BusinessException("Username já está em uso por outro usuário");
            }
            if (userRepository.existsByEmailExcludingId(request.email(), excludeId)) {
                throw new BusinessException("Email já está cadastrado por outro usuário");
            }
        }
    }
}