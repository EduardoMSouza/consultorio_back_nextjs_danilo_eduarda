package consultorio.api.controller;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.service.AgendamentoService;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        AgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(
            @PathVariable Long dentistaId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorDentista(dentistaId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorPaciente(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(
            @PathVariable StatusAgendamento status,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorPeriodo(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agenda/dentista/{dentistaId}")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarAgendaDoDentista(
            @PathVariable Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResumoResponse> response = service.buscarAgendaDoDia(dentistaId, data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agenda/dia")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarAgendaDoDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResumoResponse> response = service.buscarAgendaDoDia(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/proximos")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosAgendamentos(
            @PathVariable Long pacienteId) {
        List<AgendamentoResumoResponse> response = service.buscarProximosAgendamentos(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibilidade")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidade(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFim) {
        boolean disponivel = service.verificarDisponibilidade(dentistaId, data, horaInicio, horaFim);
        return ResponseEntity.ok(Map.of("disponivel", disponivel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable Long id) {
        service.confirmar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarAtendimento(@PathVariable Long id) {
        service.iniciarAtendimento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable Long id) {
        service.concluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/falta")
    public ResponseEntity<Void> marcarFalta(@PathVariable Long id) {
        service.marcarFalta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}