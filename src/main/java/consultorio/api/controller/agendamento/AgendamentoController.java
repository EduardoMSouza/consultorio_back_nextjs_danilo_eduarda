package consultorio.api.controller.agendamento;

import consultorio.api.dto.request.agendamento.AgendamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.service.agendamento.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id,
                                                         @Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.ok(agendamentoService.atualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponse>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.listarTodos());
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(agendamentoService.listarPorDentista(dentistaId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(agendamentoService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarPorData(data));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(agendamentoService.listarPorPeriodo(dataInicio, dataFim));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorStatus(@PathVariable StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.listarPorStatus(status));
    }

    @GetMapping("/dentista/{dentistaId}/data/{data}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorDentistaEData(
            @PathVariable Long dentistaId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.listarPorDentistaEData(dentistaId, data));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id,
                                                         @RequestParam String usuario) {
        return ResponseEntity.ok(agendamentoService.confirmar(id, usuario));
    }

    @PatchMapping("/{id}/iniciar-atendimento")
    public ResponseEntity<AgendamentoResponse> iniciarAtendimento(@PathVariable Long id,
                                                                  @RequestParam String usuario) {
        return ResponseEntity.ok(agendamentoService.iniciarAtendimento(id, usuario));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id,
                                                        @RequestParam String usuario) {
        return ResponseEntity.ok(agendamentoService.concluir(id, usuario));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id,
                                                        @RequestParam String motivo,
                                                        @RequestParam String usuario) {
        return ResponseEntity.ok(agendamentoService.cancelar(id, motivo, usuario));
    }

    @PatchMapping("/{id}/marcar-falta")
    public ResponseEntity<AgendamentoResponse> marcarFalta(@PathVariable Long id,
                                                           @RequestParam String usuario) {
        return ResponseEntity.ok(agendamentoService.marcarFalta(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verificar-disponibilidade")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidade(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam String horaInicio,
            @RequestParam String horaFim) {
        boolean disponivel = agendamentoService.verificarDisponibilidade(dentistaId, data, horaInicio, horaFim);
        return ResponseEntity.ok(Map.of("disponivel", disponivel));
    }

    @GetMapping("/lembretes/{data}")
    public ResponseEntity<List<AgendamentoResponse>> buscarAgendamentosParaLembrete(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosParaLembrete(data));
    }

    @PatchMapping("/{id}/marcar-lembrete-enviado")
    public ResponseEntity<Void> marcarLembreteEnviado(@PathVariable Long id) {
        agendamentoService.marcarLembreteEnviado(id);
        return ResponseEntity.ok().build();
    }
}