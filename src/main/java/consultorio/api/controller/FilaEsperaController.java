package consultorio.api.controller;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.domain.entity.Agendamento;
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

    @PostMapping
    @Operation(summary = "Adicionar paciente na fila de espera")
    public ResponseEntity<FilaEsperaResponse> criar(@Valid @RequestBody FilaEsperaRequest request) {
        FilaEsperaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar registro da fila por ID")
    public ResponseEntity<FilaEsperaResponse> buscarPorId(@PathVariable Long id) {
        FilaEsperaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as filas de espera")
    public ResponseEntity<Page<FilaEsperaResponse>> listarTodas(
            @PageableDefault(size = 20, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FilaEsperaResponse> response = service.listarTodas(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar filas ativas (aguardando ou notificadas)")
    public ResponseEntity<List<FilaEsperaResponse>> listarAtivas() {
        List<FilaEsperaResponse> response = service.listarAtivas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar filas por dentista")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        List<FilaEsperaResponse> response = service.listarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar todas as filas do paciente")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<FilaEsperaResponse> response = service.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/ativas")
    @Operation(summary = "Listar filas ativas do paciente")
    public ResponseEntity<List<FilaEsperaResponse>> listarPorPacienteAtivas(@PathVariable Long pacienteId) {
        List<FilaEsperaResponse> response = service.listarPorPacienteAtivas(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar filas por status")
    public ResponseEntity<Page<FilaEsperaResponse>> listarPorStatus(
            @PathVariable StatusFila status,
            @PageableDefault(size = 20, sort = "criadoEm", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FilaEsperaResponse> response = service.listarPorStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/compativeis")
    @Operation(summary = "Buscar filas compatíveis com agendamento")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompativeisComAgendamento(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Agendamento.TipoProcedimento tipoProcedimento) {
        List<FilaEsperaResponse> response = service.buscarCompativeisComAgendamento(
                dentistaId, data, tipoProcedimento);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/compativeis/dentista/{dentistaId}")
    @Operation(summary = "Buscar filas compatíveis com dentista")
    public ResponseEntity<List<FilaEsperaResponse>> buscarCompativeisComDentista(@PathVariable Long dentistaId) {
        List<FilaEsperaResponse> response = service.buscarCompativeisComDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estatisticas/ativas-dentista/{dentistaId}")
    @Operation(summary = "Contar filas ativas por dentista")
    public ResponseEntity<Map<String, Long>> contarAtivasPorDentista(@PathVariable Long dentistaId) {
        Long total = service.contarAtivasPorDentista(dentistaId);
        return ResponseEntity.ok(Map.of("total", total));
    }

    @GetMapping("/estatisticas/total-ativas")
    @Operation(summary = "Contar total de filas ativas")
    public ResponseEntity<Map<String, Long>> contarTotalAtivas() {
        Long total = service.contarTotalAtivas();
        return ResponseEntity.ok(Map.of("total", total));
    }

    @GetMapping("/estatisticas/ativas-paciente/{pacienteId}")
    @Operation(summary = "Contar filas ativas por paciente")
    public ResponseEntity<Map<String, Long>> contarAtivasPorPaciente(@PathVariable Long pacienteId) {
        Long total = service.contarAtivasPorPaciente(pacienteId);
        return ResponseEntity.ok(Map.of("total", total));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar registro da fila")
    public ResponseEntity<FilaEsperaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FilaEsperaRequest request) {
        FilaEsperaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/notificar")
    @Operation(summary = "Notificar paciente sobre vaga disponível")
    public ResponseEntity<Void> notificar(@PathVariable Long id) {
        service.notificar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/converter")
    @Operation(summary = "Converter fila em agendamento")
    public ResponseEntity<Void> converterEmAgendamento(
            @PathVariable Long id,
            @RequestParam Long agendamentoId) {
        service.converterEmAgendamento(id, agendamentoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar registro da fila")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/incrementar-tentativa")
    @Operation(summary = "Incrementar tentativa de contato")
    public ResponseEntity<Void> incrementarTentativaContato(@PathVariable Long id) {
        service.incrementarTentativaContato(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/processar-automaticamente")
    @Operation(summary = "Processar fila automaticamente")
    public ResponseEntity<Void> processarFilaAutomaticamente() {
        service.processarFilaAutomaticamente();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/enviar-notificacoes-pendentes")
    @Operation(summary = "Enviar notificações pendentes")
    public ResponseEntity<Void> enviarNotificacoesPendentes() {
        service.enviarNotificacoesPendentes();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/expirar-antigas")
    @Operation(summary = "Expirar filas antigas")
    public ResponseEntity<Void> expirarFilasAnteriores() {
        service.expirarFilasAnteriores();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar registro da fila")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}