package consultorio.domain.service.auth;

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
    void alterarSenha(String username, ChangePasswordRequest request);

    /**
     * Logout (invalida token)
     */
    void logout(String token, String username);

    /**
     * Logout de todos os dispositivos do usuário
     */
    void logoutAllDevices(String username);
}