package consultorio.domain.service.impl;

import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.mapper.pessoa.UserMapper;
import consultorio.domain.entity.pessoa.User;
import consultorio.domain.repository.UserRepository;
import consultorio.domain.service.AuthService;
import consultorio.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration:21600000}")
    private Long jwtExpiration;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Tentativa de login: {}", request.login());
        log.info(request.login());
        log.info(request.password());

        // Busca usuário por username OU email
        User user = repository.findByUsernameOrEmail(request.login())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        try {
            // Autentica usando o username real do usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
            );
        } catch (BadCredentialsException e) {
            log.warn("Falha no login para: {}", request.login());
            throw new BadCredentialsException("Credenciais inválidas");
        }

        // Registra login
        repository.registrarLogin(user.getId(), LocalDateTime.now());

        // Gera tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        UserResponse userResponse = userMapper.toResponse(user);

        log.info("Login realizado com sucesso: {} (via {})",
                user.getUsername(),
                request.login().contains("@") ? "email" : "username");

        return new LoginResponse(accessToken, refreshToken, jwtExpiration, userResponse);
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.refreshToken());

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!jwtService.isTokenValid(request.refreshToken(), user)) {
            throw new BusinessException("Refresh token inválido ou expirado");
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

    @Override
    @Transactional
    public void alterarSenha(Long userId, ChangePasswordRequest request) {
        if (!request.novaSenha().equals(request.confirmacaoSenha())) {
            throw new BusinessException("Nova senha e confirmação não conferem");
        }

        User user = repository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), user.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        String novaSenhaEncoded = passwordEncoder.encode(request.novaSenha());
        repository.updateSenha(userId, novaSenhaEncoded, LocalDateTime.now());

        log.info("Senha alterada para usuário: {}", user.getUsername());
    }

    @Override
    public void logout(String token) {
        // Implementar blacklist de tokens se necessário
        // Por enquanto, o logout é apenas client-side (descarta o token)
        log.info("Logout solicitado");
    }
}