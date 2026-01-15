package consultorio.domain.service.auth.impl;

import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;
import consultorio.api.dto.response.pessoa.UserResponse;
import consultorio.api.exception.BusinessException;
import consultorio.api.mapper.pessoa.UserMapper;
import consultorio.domain.entity.pessoa.User;
import consultorio.domain.repository.pessoa.UserRepository;
import consultorio.domain.service.auth.AuthService;
import consultorio.infrastructure.config.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        log.debug("Tentativa de login para: {}", maskLogin(request.login()));

        try {
            // Primeiro, tentar encontrar o usuário
            User user = repository.findByUsernameOrEmail(request.login())
                    .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

            if (!user.getAtivo()) {
                log.warn("Tentativa de login para usuário inativo: {}", maskLogin(request.login()));
                throw new BusinessException("Usuário inativo. Contate o administrador.");
            }

            // Autenticar usando o username real do usuário encontrado
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
            );

            // Registra login
            user.setUltimoLogin(LocalDateTime.now());
            user = repository.save(user);

            // Gera tokens
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            // Armazena refresh token para controle
            jwtService.storeRefreshToken(user.getUsername(), refreshToken);

            UserResponse userResponse = userMapper.toResponse(user);

            log.info("Login realizado com sucesso: {} (role: {})",
                    user.getUsername(), user.getRole());

            return new LoginResponse(accessToken, refreshToken, jwtExpiration, userResponse);

        } catch (BadCredentialsException e) {
            log.warn("Credenciais inválidas para: {}", maskLogin(request.login()));
            throw new BadCredentialsException("Credenciais inválidas");
        }
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.refreshToken());

        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        // Valida se o refresh token está na lista de tokens válidos
        if (!jwtService.isRefreshTokenValid(username, request.refreshToken())) {
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        // Remove o refresh token antigo
        jwtService.invalidateRefreshToken(username, request.refreshToken());

        // Gera novos tokens
        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Armazena o novo refresh token
        jwtService.storeRefreshToken(username, newRefreshToken);

        UserResponse userResponse = userMapper.toResponse(user);

        log.debug("Token renovado para: {}", username);
        return new LoginResponse(accessToken, newRefreshToken, jwtExpiration, userResponse);
    }

    @Override
    @Transactional
    public void alterarSenha(String username, ChangePasswordRequest request) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!user.getAtivo()) {
            throw new BusinessException("Usuário inativo");
        }

        // Validações
        if (!request.novaSenha().equals(request.confirmacaoSenha())) {
            throw new BusinessException("Nova senha e confirmação não conferem");
        }

        if (request.novaSenha().equals(request.senhaAtual())) {
            throw new BusinessException("A nova senha deve ser diferente da senha atual");
        }

        if (!passwordEncoder.matches(request.senhaAtual(), user.getPassword())) {
            throw new BusinessException("Senha atual incorreta");
        }

        // Atualiza senha
        user.setPassword(passwordEncoder.encode(request.novaSenha()));
        user.setAtualizadoEm(LocalDateTime.now());
        repository.save(user);

        // Invalida todos os tokens antigos
        jwtService.invalidateAllUserTokens(username);

        log.info("Senha alterada para usuário: {}", username);
    }

    @Override
    @Transactional
    public void logout(String token, String username) {
        jwtService.invalidateToken(token);
        log.info("Logout realizado para usuário: {}", username);
    }

    @Override
    @Transactional
    public void logoutAllDevices(String username) {
        jwtService.invalidateAllUserTokens(username);
        log.info("Logout de todos os dispositivos para usuário: {}", username);
    }

    private String maskLogin(String login) {
        if (login == null || login.length() <= 3) {
            return "***";
        }
        return login.charAt(0) + "***" + login.charAt(login.length() - 1);
    }
}