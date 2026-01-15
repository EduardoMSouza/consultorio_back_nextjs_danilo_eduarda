package consultorio.domain.service;

<<<<<<< HEAD
import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;
=======
import consultorio.api.dto.request.ChangePasswordRequest;
import consultorio.api.dto.request.LoginRequest;
import consultorio.api.dto.request.RefreshTokenRequest;
import consultorio.api.dto.response.LoginResponse;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb

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