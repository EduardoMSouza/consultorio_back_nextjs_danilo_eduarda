package consultorio.domain.service;

import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;

public interface AuthService {

    /**
     * Login por email ou username
     */
    LoginResponse login(LoginRequest request);

    /**
     * Renovar tokens usando refresh token
     */
    LoginResponse refreshToken(RefreshTokenRequest request);

    /**
     * Alterar senha do usuário logado
     */
    void alterarSenha(Long userId, ChangePasswordRequest request);

    /**
     * Logout (invalidar token se implementar blacklist)
     */
    void logout(String token);
}