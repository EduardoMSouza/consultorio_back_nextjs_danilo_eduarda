package consultorio.api.dto.response;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn,
        String tokenType,
        UserResponse user
) {
    public LoginResponse(String accessToken, String refreshToken, Long expiresIn, UserResponse user) {
        this(accessToken, refreshToken, expiresIn, "Bearer", user);
    }
}
