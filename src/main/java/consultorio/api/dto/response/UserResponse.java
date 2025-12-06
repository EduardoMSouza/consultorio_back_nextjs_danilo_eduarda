package consultorio.api.dto.response;

import consultorio.domain.entity.User;

public record UserResponse(
        Long id,
        String nome,
        String email,
        String role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getRole()
        );
    }
}