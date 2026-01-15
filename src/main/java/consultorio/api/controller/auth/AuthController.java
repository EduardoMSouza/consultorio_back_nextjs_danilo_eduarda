package consultorio.api.controller.auth;

import consultorio.api.dto.request.auth.ChangePasswordRequest;
import consultorio.api.dto.request.auth.LoginRequest;
import consultorio.api.dto.request.auth.RefreshTokenRequest;
import consultorio.api.dto.response.auth.LoginResponse;
import consultorio.domain.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader("Authorization") String authHeader,
            @AuthenticationPrincipal UserDetails userDetails) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }

    @PostMapping("/alterar-senha")
    @Operation(summary = "Alterar senha do usuário logado")
    public ResponseEntity<Map<String, String>> alterarSenha(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.alterarSenha(userDetails.getUsername(), request);
        return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso"));
    }

    @PostMapping("/logout-all-devices")
    @Operation(summary = "Logout de todos os dispositivos")
    public ResponseEntity<Map<String, String>> logoutAllDevices(
            @AuthenticationPrincipal UserDetails userDetails) {
        authService.logoutAllDevices(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Logout de todos os dispositivos realizado"));
    }
}