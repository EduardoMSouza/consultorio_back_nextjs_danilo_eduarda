package consultorio.api.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {}

















