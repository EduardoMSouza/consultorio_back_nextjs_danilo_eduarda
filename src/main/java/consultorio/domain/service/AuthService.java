package consultorio.domain.service;

import consultorio.api.dto.request.LoginRequest;
import consultorio.api.dto.request.RefreshTokenRequest;
import consultorio.api.dto.response.LoginResponse;
import consultorio.api.dto.response.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.mapper.UserMapper;
import consultorio.domain.entity.User;
import consultorio.domain.repository.UserRepository;
import consultorio.infrastructure.secutiry.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Value("${jwt.expiration:21600000}")
    private Long jwtExpiration;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Tentativa de login: {}", request.username());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        user.registrarLogin();
        repository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user);

        log.info("Login realizado com sucesso: {}", user.getUsername());
        return new LoginResponse(accessToken, refreshToken, jwtExpiration, userResponse);
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.refreshToken());

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!jwtService.isTokenValid(request.refreshToken(), user)) {
            throw new BusinessException("Refresh token inválido");
        }

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user);

        log.info("Token renovado para: {}", username);
        return new LoginResponse(accessToken, newRefreshToken, jwtExpiration, userResponse);
    }
}