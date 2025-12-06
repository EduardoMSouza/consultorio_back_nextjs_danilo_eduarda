package consultorio.api.controller;

import consultorio.api.dto.request.PacienteRequest;
import consultorio.api.dto.response.PacienteResponse;
import consultorio.api.dto.response.PacienteResumoResponse;
import consultorio.domain.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @PostMapping
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        PacienteResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prontuario/{prontuarioNumero}")
    public ResponseEntity<PacienteResponse> buscarPorProntuario(@PathVariable String prontuarioNumero) {
        PacienteResponse response = service.buscarPorProntuario(prontuarioNumero);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResumoResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PacienteResumoResponse> response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PacienteResumoResponse>> listarPorStatus(
            @PathVariable Boolean status,
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PacienteResumoResponse> response = service.listarPorStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<PacienteResumoResponse>> buscar(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PacienteResumoResponse> response = service.buscar(termo, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        PacienteResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}