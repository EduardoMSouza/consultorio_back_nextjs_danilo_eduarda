package consultorio.domain.service;

import consultorio.api.dto.request.LoginRequest;
import consultorio.api.dto.request.RefreshTokenRequest;
import consultorio.api.dto.request.RegisterRequest;
import consultorio.api.dto.response.LoginResponse;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
import consultorio.domain.repository.UserRepository;
import consultorio.infrastructure.secutiry.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        System.out.println("Login feito");
        return new LoginResponse(accessToken, refreshToken, UserResponse.fromEntity(user));
    }

    public LoginResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = User.builder()
                .nome(request.nome())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role("USER")
                .ativo(true)
                .build();

        repository.save(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken, UserResponse.fromEntity(user));
    }

    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String email = jwtService.extractUsername(request.refreshToken());

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!jwtService.isTokenValid(request.refreshToken(), user)) {
            throw new RuntimeException("Refresh token inválido");
        }

        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, newRefreshToken, UserResponse.fromEntity(user));
    }
}