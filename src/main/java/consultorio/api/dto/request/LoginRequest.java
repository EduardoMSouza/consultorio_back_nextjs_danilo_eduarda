package consultorio.api.dto.request;

public record LoginRequest(
        String email,
        String password,
        boolean rememberMe
) {}