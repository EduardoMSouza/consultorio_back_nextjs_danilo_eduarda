package consultorio.api.controller;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;

import consultorio.domain.entity.tratamento.enums.StatusPlano;
import consultorio.domain.service.PlanoDentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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