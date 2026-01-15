package consultorio.api.controller;

<<<<<<< HEAD
import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;

import consultorio.domain.entity.tratamento.enums.StatusPlano;
import consultorio.domain.service.PlanoDentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
=======
import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;
import consultorio.domain.service.PlanoDentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/planos-dentais")
@RequiredArgsConstructor
public class PlanoDentalController {

    private final PlanoDentalService planoDentalService;

    @PostMapping
    public ResponseEntity<PlanoDentalResponse> create(@Valid @RequestBody PlanoDentalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planoDentalService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(planoDentalService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PlanoDentalResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(planoDentalService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> update(@PathVariable Long id, @Valid @RequestBody PlanoDentalRequest request) {
        return ResponseEntity.ok(planoDentalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planoDentalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<PlanoDentalResponse> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(planoDentalService.concluir(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PlanoDentalResponse> cancelar(@PathVariable Long id, @RequestParam String motivo) {
        return ResponseEntity.ok(planoDentalService.cancelar(id, motivo));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<PlanoDentalResponse> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(planoDentalService.iniciar(id));
    }

    @PatchMapping("/{id}/aplicar-desconto")
    public ResponseEntity<PlanoDentalResponse> aplicarDesconto(@PathVariable Long id, @RequestParam BigDecimal desconto) {
        return ResponseEntity.ok(planoDentalService.aplicarDesconto(id, desconto));
    }

    @PatchMapping("/{id}/atualizar-valor")
    public ResponseEntity<PlanoDentalResponse> atualizarValor(@PathVariable Long id, @RequestParam BigDecimal valor) {
        return ResponseEntity.ok(planoDentalService.atualizarValor(id, valor));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<PlanoDentalResponse> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(planoDentalService.ativar(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PlanoDentalResponse>> findByPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(planoDentalService.findByPacienteId(pacienteId));
    }

    @GetMapping("/paciente/{pacienteId}/pagina")
    public ResponseEntity<Page<PlanoDentalResponse>> findByPacienteIdPage(@PathVariable Long pacienteId, Pageable pageable) {
        return ResponseEntity.ok(planoDentalService.findByPacienteId(pacienteId, pageable));
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<PlanoDentalResponse>> findByDentistaId(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(planoDentalService.findByDentistaId(dentistaId));
    }

    @GetMapping("/dentista/{dentistaId}/pagina")
    public ResponseEntity<Page<PlanoDentalResponse>> findByDentistaIdPage(@PathVariable Long dentistaId, Pageable pageable) {
        return ResponseEntity.ok(planoDentalService.findByDentistaId(dentistaId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> findByStatus(@PathVariable StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.findByStatus(status));
    }

    @GetMapping("/status/{status}/pagina")
    public ResponseEntity<Page<PlanoDentalResponse>> findByStatusPage(@PathVariable StatusPlano status, Pageable pageable) {
        return ResponseEntity.ok(planoDentalService.findByStatus(status, pageable));
    }

    @GetMapping("/paciente/{pacienteId}/ordenados")
    public ResponseEntity<List<PlanoDentalResponse>> findAtivosByPacienteIdOrderByDataPrevista(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(planoDentalService.findAtivosByPacienteIdOrderByDataPrevista(pacienteId));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<PlanoDentalResponse>> findByDataPrevistaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(planoDentalService.findByDataPrevistaBetween(inicio, fim));
    }

    @GetMapping("/urgentes/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> findUrgentesByStatus(@PathVariable StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.findUrgentesByStatus(status));
    }

    @GetMapping("/paciente/{pacienteId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> findByPacienteIdAndStatus(
            @PathVariable Long pacienteId, @PathVariable StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.findByPacienteIdAndStatus(pacienteId, status));
    }

    @GetMapping("/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> findByDentistaIdAndStatus(
            @PathVariable Long dentistaId, @PathVariable StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.findByDentistaIdAndStatus(dentistaId, status));
    }

    @GetMapping("/paciente/{pacienteId}/total")
    public ResponseEntity<BigDecimal> calcularTotalPorPacienteAndStatus(
            @PathVariable Long pacienteId, @RequestParam StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.calcularTotalPorPacienteAndStatus(pacienteId, status));
    }

    @GetMapping("/paciente/{pacienteId}/quantidade-ativos")
    public ResponseEntity<Long> countAtivosByPacienteId(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(planoDentalService.countAtivosByPacienteId(pacienteId));
    }

    @GetMapping("/status-lista")
    public ResponseEntity<List<PlanoDentalResponse>> findByStatusIn(@RequestParam List<StatusPlano> statuses) {
        return ResponseEntity.ok(planoDentalService.findByStatusIn(statuses));
    }

    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> findByPacienteIdAndDentistaIdAndStatus(
            @PathVariable Long pacienteId, @PathVariable Long dentistaId, @PathVariable StatusPlano status) {
        return ResponseEntity.ok(planoDentalService.findByPacienteIdAndDentistaIdAndStatus(pacienteId, dentistaId, status));
    }
}
=======
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
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
