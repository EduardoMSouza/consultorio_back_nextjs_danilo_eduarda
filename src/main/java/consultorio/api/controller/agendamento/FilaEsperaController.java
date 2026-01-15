package consultorio.api.controller.agendamento;

import consultorio.api.dto.request.agendamento.fila_espera.FilaEsperaRequest;
import consultorio.api.dto.response.agendamento.fila_espera.FilaEsperaResponse;
import consultorio.domain.entity.agendamento.FilaEspera;

import consultorio.domain.service.agendamento.FilaEsperaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fila-espera")
@RequiredArgsConstructor
public class FilaEsperaController {

    private final FilaEsperaService filaEsperaService;

    @PostMapping
    public ResponseEntity<FilaEsperaResponse> criar(@Valid @RequestBody FilaEsperaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filaEsperaService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilaEsperaResponse> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody FilaEsperaRequest request) {
        return ResponseEntity.ok(filaEsperaService.atualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilaEsperaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<FilaEsperaResponse>> listarTodas() {
        return ResponseEntity.ok(filaEsperaService.listarTodas());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorStatus(@PathVariable FilaEspera.StatusFila status) {
        return ResponseEntity.ok(filaEsperaService.listarPorStatus(status));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(filaEsperaService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(filaEsperaService.listarPorDentista(dentistaId));
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<FilaEsperaResponse>> listarAtivas() {
        return ResponseEntity.ok(filaEsperaService.listarAtivas());
    }

    @PatchMapping("/{id}/notificar")
    public ResponseEntity<FilaEsperaResponse> notificar(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.notificar(id));
    }

    @PatchMapping("/{id}/converter")
    public ResponseEntity<FilaEsperaResponse> converterEmAgendamento(@PathVariable Long id,
                                                                     @RequestParam Long agendamentoId) {
        return ResponseEntity.ok(filaEsperaService.converterEmAgendamento(id, agendamentoId));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<FilaEsperaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(filaEsperaService.cancelar(id));
    }

    @PostMapping("/expirar")
    public ResponseEntity<Void> expirarFilasAntigas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataLimite) {
        filaEsperaService.expirarFilasAntigas(dataLimite);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        filaEsperaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}