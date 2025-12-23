package consultorio.api.controller;

import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;
import consultorio.domain.service.EvolucaoTratamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/evolucao-tratamento")
@RequiredArgsConstructor
@Tag(name = "Evolução de Tratamento", description = "Gerenciamento de evoluções de tratamento")
@SecurityRequirement(name = "bearerAuth")
public class EvolucaoTratamentoController {

    private final EvolucaoTratamentoService service;

    @PostMapping
    @Operation(summary = "Criar evolução de tratamento")
    public ResponseEntity<EvolucaoTratamentoResponse> criar(@Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evolução por ID")
    public ResponseEntity<EvolucaoTratamentoResponse> buscarPorId(@PathVariable Long id) {
        EvolucaoTratamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as evoluções")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarTodos() {
        List<EvolucaoTratamentoResponse> response = service.listarTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar evoluções por paciente")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar evoluções por dentista")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}")
    @Operation(summary = "Listar evoluções por paciente e dentista")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPacienteEDentista(
            @PathVariable Long pacienteId,
            @PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPacienteEDentista(pacienteId, dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar evoluções por período")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evolução de tratamento")
    public ResponseEntity<EvolucaoTratamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evolução de tratamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
