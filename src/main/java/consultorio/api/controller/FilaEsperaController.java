package consultorio.api.controller;

import consultorio.api.dto.request.agendamento.FilaEsperaCreateRequest;
import consultorio.api.dto.request.agendamento.FilaEsperaUpdateRequest;
import consultorio.api.dto.response.agendamento.FilaEsperaResponse;
import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import consultorio.domain.service.FilaEsperaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fila-espera")
@RequiredArgsConstructor
public class FilaEsperaController {

    private final FilaEsperaService service;

    // CRUD
    @PostMapping
    public ResponseEntity<FilaEsperaResponse> criar(@Valid @RequestBody FilaEsperaCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilaEsperaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilaEsperaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody FilaEsperaUpdateRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Listagens
    @GetMapping
    public ResponseEntity<Page<FilaEsperaResponse>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/ativas")
    public ResponseEntity<Page<FilaEsperaResponse>> listarAtivas(Pageable pageable) {
        return ResponseEntity.ok(service.listarAtivas(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<FilaEsperaResponse>> listarPorStatus(@PathVariable StatusFila status, Pageable pageable) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @GetMapping("/dentista/{dentistaId}/ativas")
    public ResponseEntity<List<FilaEsperaResponse>> listarAtivasPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.listarAtivasPorDentista(dentistaId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    @GetMapping("/paciente/{pacienteId}/ativas")
    public ResponseEntity<List<FilaEsperaResponse>> listarAtivasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarAtivasPorPaciente(pacienteId));
    }

    // Ações
    @PutMapping("/{id}/notificar")
    public ResponseEntity<FilaEsperaResponse> notificar(@PathVariable Long id) {
        return ResponseEntity.ok(service.notificar(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<FilaEsperaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @PutMapping("/{id}/converter/{agendamentoId}")
    public ResponseEntity<FilaEsperaResponse> converterEmAgendamento(@PathVariable Long id, @PathVariable Long agendamentoId) {
        return ResponseEntity.ok(service.converterEmAgendamento(id, agendamentoId));
    }

    @PutMapping("/{id}/tentativa-contato")
    public ResponseEntity<Void> incrementarTentativaContato(@PathVariable Long id) {
        service.incrementarTentativaContato(id);
        return ResponseEntity.ok().build();
    }

    // Compativeis
    @GetMapping("/compativeis")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompativeis(
            @RequestParam Long dentistaId,
            @RequestParam LocalDate data,
            @RequestParam TipoProcedimento tipoProcedimento) {
        return ResponseEntity.ok(service.buscarCompativeis(dentistaId, data, tipoProcedimento));
    }

    @GetMapping("/compativeis/dentista/{dentistaId}")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompativeisPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarCompatíveisPorDentista(dentistaId));
    }

    // Estatísticas
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        return ResponseEntity.ok(service.obterEstatisticas());
    }

    @GetMapping("/contar/ativas")
    public ResponseEntity<Long> contarAtivas() {
        return ResponseEntity.ok(service.contarAtivas());
    }

    @GetMapping("/contar/ativas/dentista/{dentistaId}")
    public ResponseEntity<Long> contarAtivasPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.contarAtivasPorDentista(dentistaId));
    }

    @GetMapping("/contar/ativas/paciente/{pacienteId}")
    public ResponseEntity<Long> contarAtivasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.contarAtivasPorPaciente(pacienteId));
    }

    @GetMapping("/posicao/{filaId}")
    public ResponseEntity<Integer> calcularPosicao(@PathVariable Long filaId) {
        return ResponseEntity.ok(service.calcularPosicao(filaId));
    }
}