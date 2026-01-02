package consultorio.api.controller;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import consultorio.domain.entity.FilaEspera.StatusFila;
import consultorio.domain.service.FilaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fila-espera")
@RequiredArgsConstructor
@Tag(name = "Fila de Espera", description = "Gerenciamento da fila de espera")
public class FilaEsperaController {

    private final FilaEsperaService service;

    // ==================== CRUD ====================

    @PostMapping
    @Operation(summary = "Criar entrada na fila")
    public ResponseEntity<FilaEsperaResponse> criar(@Valid @RequestBody FilaEsperaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<FilaEsperaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar entrada na fila")
    public ResponseEntity<FilaEsperaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody FilaEsperaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar entrada da fila")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // ==================== LISTAGENS ====================

    @GetMapping
    @Operation(summary = "Listar todas as entradas")
    public ResponseEntity<Page<FilaEsperaResponse>> listarTodas(
            @PageableDefault(size = 20, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodas(pageable));
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar filas ativas")
    public ResponseEntity<Page<FilaEsperaResponse>> listarAtivas(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.listarAtivas(pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar por status")
    public ResponseEntity<Page<FilaEsperaResponse>> listarPorStatus(
            @PathVariable StatusFila status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar filas ativas por dentista")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.listarAtivasPorDentista(dentistaId));
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar filas por paciente")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId));
    }

    @GetMapping("/paciente/{pacienteId}/ativas")
    @Operation(summary = "Listar filas ativas por paciente")
    public ResponseEntity<List<FilaEsperaResponse>> listarAtivasPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.listarAtivasPorPaciente(pacienteId));
    }

    // ==================== AÇÕES ====================

    @PatchMapping("/{id}/notificar")
    @Operation(summary = "Notificar paciente")
    public ResponseEntity<FilaEsperaResponse> notificar(@PathVariable Long id) {
        return ResponseEntity.ok(service.notificar(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar entrada na fila")
    public ResponseEntity<FilaEsperaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }

    @PatchMapping("/{filaId}/converter/{agendamentoId}")
    @Operation(summary = "Converter fila em agendamento")
    public ResponseEntity<FilaEsperaResponse> converterEmAgendamento(
            @PathVariable Long filaId,
            @PathVariable Long agendamentoId) {
        return ResponseEntity.ok(service.converterEmAgendamento(filaId, agendamentoId));
    }

    // ==================== PROCESSAMENTO ====================

    @PostMapping("/expirar")
    @Operation(summary = "Expirar filas vencidas")
    public ResponseEntity<Map<String, Integer>> expirarVencidas() {
        int total = service.expirarFilasVencidas();
        return ResponseEntity.ok(Map.of("expiradas", total));
    }

    @PostMapping("/notificacoes")
    @Operation(summary = "Enviar notificações pendentes")
    public ResponseEntity<Map<String, Integer>> enviarNotificacoes() {
        int total = service.enviarNotificacoesPendentes();
        return ResponseEntity.ok(Map.of("enviadas", total));
    }

    // ==================== COMPATÍVEIS ====================

    @GetMapping("/compativeis")
    @Operation(summary = "Buscar filas compatíveis com horário")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompativeis(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) TipoProcedimento tipoProcedimento) {
        return ResponseEntity.ok(service.buscarCompativeis(dentistaId, data, tipoProcedimento));
    }

    @GetMapping("/compativeis/dentista/{dentistaId}")
    @Operation(summary = "Buscar filas compatíveis por dentista")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompatíveisPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarCompatíveisPorDentista(dentistaId));
    }

    // ==================== ESTATÍSTICAS ====================

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        return ResponseEntity.ok(service.obterEstatisticas());
    }

    @GetMapping("/contagem")
    @Operation(summary = "Contar filas ativas")
    public ResponseEntity<Map<String, Long>> contagem(
            @RequestParam(required = false) Long dentistaId,
            @RequestParam(required = false) Long pacienteId) {

        long total;
        if (dentistaId != null) {
            total = service.contarAtivasPorDentista(dentistaId);
        } else if (pacienteId != null) {
            total = service.contarAtivasPorPaciente(pacienteId);
        } else {
            total = service.contarAtivas();
        }

        return ResponseEntity.ok(Map.of("total", total));
    }

    @GetMapping("/{id}/posicao")
    @Operation(summary = "Calcular posição na fila")
    public ResponseEntity<Map<String, Integer>> posicao(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("posicao", service.calcularPosicao(id)));
    }
}