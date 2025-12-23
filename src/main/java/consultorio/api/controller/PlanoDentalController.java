package consultorio.api.controller;

import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;
import consultorio.domain.service.PlanoDentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plano-dental")
@RequiredArgsConstructor
@Tag(name = "Plano Dental", description = "Gerenciamento de planos dentais")
@SecurityRequirement(name = "bearerAuth")
public class PlanoDentalController {

    private final PlanoDentalService service;

    @PostMapping
    @Operation(summary = "Criar plano dental")
    public ResponseEntity<PlanoDentalResponse> criar(@Valid @RequestBody PlanoDentalRequest request) {
        PlanoDentalResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar plano dental por ID")
    public ResponseEntity<PlanoDentalResponse> buscarPorId(@PathVariable Long id) {
        PlanoDentalResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os planos dentais")
    public ResponseEntity<List<PlanoDentalResponse>> listarTodos() {
        List<PlanoDentalResponse> response = service.listarTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar planos por paciente")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<PlanoDentalResponse> response = service.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar planos por dentista")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        List<PlanoDentalResponse> response = service.listarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}")
    @Operation(summary = "Listar planos por paciente e dentista")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPacienteEDentista(
            @PathVariable Long pacienteId,
            @PathVariable Long dentistaId) {
        List<PlanoDentalResponse> response = service.listarPorPacienteEDentista(pacienteId, dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar planos por status")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorStatus(@PathVariable String status) {
        List<PlanoDentalResponse> response = service.listarPorStatus(status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar plano dental")
    public ResponseEntity<PlanoDentalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoDentalRequest request) {
        PlanoDentalResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar plano dental")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
