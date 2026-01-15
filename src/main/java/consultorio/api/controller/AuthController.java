package consultorio.api.controller;

<<<<<<< HEAD
import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;
import consultorio.domain.entity.pessoa.User;
=======
import consultorio.api.dto.request.ChangePasswordRequest;
import consultorio.api.dto.request.LoginRequest;
import consultorio.api.dto.request.RefreshTokenRequest;
import consultorio.api.dto.response.LoginResponse;
import consultorio.domain.entity.User;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import consultorio.domain.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login, logout e gestão de tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login (por email ou username)")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }

    @PostMapping("/alterar-senha")
    @Operation(summary = "Alterar senha do usuário logado")
    public ResponseEntity<Map<String, String>> alterarSenha(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.alterarSenha(user.getId(), request);
        return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso"));
    }
}