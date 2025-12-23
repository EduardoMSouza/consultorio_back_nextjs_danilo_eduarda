package consultorio.api.controller;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.service.AgendamentoService;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    @Operation(summary = "Criar novo agendamento")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        AgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar agendamentos por dentista")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(
            @PathVariable Long dentistaId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorDentista(dentistaId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar agendamentos por paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorPaciente(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar agendamentos por status")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(
            @PathVariable StatusAgendamento status,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar agendamentos por período")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.listarPorPeriodo(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agenda/dentista/{dentistaId}")
    @Operation(summary = "Buscar agenda do dia de um dentista")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarAgendaDoDentista(
            @PathVariable Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResumoResponse> response = service.buscarAgendaDoDia(dentistaId, data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agenda/dia")
    @Operation(summary = "Buscar agenda do dia de todos os dentistas")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarAgendaDoDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<AgendamentoResumoResponse> response = service.buscarAgendaDoDia(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/proximos")
    @Operation(summary = "Buscar próximos agendamentos do paciente")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosAgendamentos(
            @PathVariable Long pacienteId) {
        List<AgendamentoResumoResponse> response = service.buscarProximosAgendamentos(pacienteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}/proximos")
    @Operation(summary = "Buscar próximos agendamentos do dentista")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosAgendamentosDentista(
            @PathVariable Long dentistaId) {
        List<AgendamentoResumoResponse> response = service.buscarProximosAgendamentosDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disponibilidade")
    @Operation(summary = "Verificar disponibilidade de horário")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidade(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFim) {
        boolean disponivel = service.verificarDisponibilidade(dentistaId, data, horaInicio, horaFim);
        return ResponseEntity.ok(Map.of("disponivel", disponivel));
    }

    @GetMapping("/horarios-disponiveis")
    @Operation(summary = "Buscar horários disponíveis")
    public ResponseEntity<List<LocalTime[]>> buscarHorariosDisponiveis(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(defaultValue = "30") int duracaoMinutos) {
        List<LocalTime[]> horarios = service.buscarHorariosDisponiveis(dentistaId, data, duracaoMinutos);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/consultas-passadas-nao-finalizadas")
    @Operation(summary = "Buscar consultas passadas não finalizadas")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarConsultasPassadasNaoFinalizadas() {
        List<AgendamentoResumoResponse> response = service.buscarConsultasPassadasNaoFinalizadas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consultas-em-atendimento")
    @Operation(summary = "Buscar consultas em atendimento")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarConsultasEmAtendimento() {
        List<AgendamentoResumoResponse> response = service.buscarConsultasEmAtendimento();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/historico")
    @Operation(summary = "Buscar histórico de consultas do paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> buscarHistoricoConsultasPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AgendamentoResumoResponse> response = service.buscarHistoricoConsultasPaciente(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/estatisticas/consultas-dia")
    @Operation(summary = "Contar consultas do dia")
    public ResponseEntity<Map<String, Long>> contarConsultasDoDia(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        Long total = service.contarConsultasDoDia(dentistaId, data);
        return ResponseEntity.ok(Map.of("total", total));
    }

    @GetMapping("/estatisticas/faltas-paciente/{pacienteId}")
    @Operation(summary = "Contar faltas do paciente")
    public ResponseEntity<Map<String, Long>> contarFaltasPaciente(@PathVariable Long pacienteId) {
        Long total = service.contarFaltasPaciente(pacienteId);
        return ResponseEntity.ok(Map.of("total", total));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento")
    public ResponseEntity<AgendamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {
        AgendamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do agendamento")
    public ResponseEntity<AgendamentoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusAgendamento status) {
        AgendamentoResponse response = service.atualizarStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento")
    public ResponseEntity<Void> confirmar(@PathVariable Long id) {
        service.confirmar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar atendimento")
    public ResponseEntity<Void> iniciarAtendimento(@PathVariable Long id) {
        service.iniciarAtendimento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir agendamento")
    public ResponseEntity<Void> concluir(@PathVariable Long id) {
        service.concluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento")
    public ResponseEntity<Void> cancelar(
            @PathVariable Long id,
            @RequestParam(required = false) String motivo) {
        service.cancelar(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/falta")
    @Operation(summary = "Marcar falta")
    public ResponseEntity<Void> marcarFalta(@PathVariable Long id) {
        service.marcarFalta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enviar-lembretes")
    @Operation(summary = "Enviar lembretes do dia")
    public ResponseEntity<Void> enviarLembretes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        service.enviarLembretes(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar agendamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}