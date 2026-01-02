package consultorio.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Login é obrigatório (email ou username)")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}