package consultorio.api.controller;

import consultorio.api.dto.request.UserRequest;
import consultorio.api.dto.response.UserResponse;
import consultorio.domain.entity.User;
import consultorio.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários (apenas ADMIN)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo usuário (apenas ADMIN)")
    public ResponseEntity<UserResponse> criar(
            @Valid @RequestBody UserRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = service.criar(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable Long id) {
        UserResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuário por username")
    public ResponseEntity<UserResponse> buscarPorUsername(@PathVariable String username) {
        UserResponse response = service.buscarPorUsername(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<Page<UserResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{ativo}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários por status")
    public ResponseEntity<Page<UserResponse>> listarPorStatus(
            @PathVariable Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> response = service.listarPorStatus(ativo, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuários por role")
    public ResponseEntity<Page<UserResponse>> listarPorRole(
            @PathVariable User.Role role,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> response = service.listarPorRole(role, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UserResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        UserResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Inativar usuário")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ativar usuário")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuário logado")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = service.buscarPorUsername(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}