package consultorio.api.controller;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.request.CancelamentoRequest;
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

    // ==================== CRUD ====================

    @PostMapping
    @Operation(summary = "Criar agendamento")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar agendamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // ==================== LISTAGENS ====================

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar agendamentos por dentista")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(
            @PathVariable Long dentistaId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorDentista(dentistaId, pageable));
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar agendamentos por paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar agendamentos por status")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(
            @PathVariable StatusAgendamento status,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar agendamentos por período")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorPeriodo(inicio, fim, pageable));
    }

    // ==================== AGENDA ====================

    @GetMapping("/agenda/dia")
    @Operation(summary = "Buscar agenda do dia")
    public ResponseEntity<List<AgendamentoResumoResponse>> agendaDoDia(
            @RequestParam(required = false) Long dentistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        LocalDate dataConsulta = data != null ? data : LocalDate.now();

        List<AgendamentoResumoResponse> agenda = dentistaId != null
                ? service.buscarAgendaDoDia(dentistaId, dataConsulta)
                : service.buscarAgendaDoDia(dataConsulta);

        return ResponseEntity.ok(agenda);
    }

    @GetMapping("/proximos/paciente/{pacienteId}")
    @Operation(summary = "Buscar próximos agendamentos do paciente")
    public ResponseEntity<List<AgendamentoResumoResponse>> proximosPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.buscarProximosPaciente(pacienteId));
    }

    @GetMapping("/proximos/dentista/{dentistaId}")
    @Operation(summary = "Buscar próximos agendamentos do dentista")
    public ResponseEntity<List<AgendamentoResumoResponse>> proximosDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarProximosDentista(dentistaId));
    }

    // ==================== MUDANÇA DE STATUS ====================

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar atendimento")
    public ResponseEntity<AgendamentoResponse> iniciarAtendimento(@PathVariable Long id) {
        return ResponseEntity.ok(service.iniciarAtendimento(id));
    }

    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir atendimento")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(service.concluir(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id, @Valid @RequestBody CancelamentoRequest request) {
        return ResponseEntity.ok(service.cancelar(id, request.getMotivo()));
    }

    @PatchMapping("/{id}/falta")
    @Operation(summary = "Marcar falta")
    public ResponseEntity<AgendamentoResponse> marcarFalta(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarFalta(id));
    }

    // ==================== DISPONIBILIDADE ====================

    @GetMapping("/disponibilidade")
    @Operation(summary = "Verificar disponibilidade")
    public ResponseEntity<Map<String, Object>> verificarDisponibilidade(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFim) {

        boolean disponivel = service.verificarDisponibilidade(dentistaId, data, horaInicio, horaFim);
        return ResponseEntity.ok(Map.of("disponivel", disponivel));
    }

    @GetMapping("/horarios-disponiveis")
    @Operation(summary = "Buscar horários disponíveis")
    public ResponseEntity<List<Map<String, LocalTime>>> horariosDisponiveis(
            @RequestParam Long dentistaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(defaultValue = "30") int duracaoMinutos) {
        return ResponseEntity.ok(service.buscarHorariosDisponiveis(dentistaId, data, duracaoMinutos));
    }

    // ==================== LEMBRETES ====================

    @PostMapping("/lembretes")
    @Operation(summary = "Enviar lembretes para uma data")
    public ResponseEntity<Map<String, Integer>> enviarLembretes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        int enviados = service.enviarLembretes(data != null ? data : LocalDate.now().plusDays(1));
        return ResponseEntity.ok(Map.of("enviados", enviados));
    }

    // ==================== ESTATÍSTICAS ====================

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas")
    public ResponseEntity<Map<String, Object>> estatisticas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.obterEstatisticas(inicio, fim));
    }

    @GetMapping("/estatisticas/consultas-dia")
    @Operation(summary = "Contar consultas do dia")
    public ResponseEntity<Map<String, Long>> consultasDoDia(
            @RequestParam Long dentistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        long total = service.contarConsultasDoDia(dentistaId, data != null ? data : LocalDate.now());
        return ResponseEntity.ok(Map.of("total", total));
    }

    @GetMapping("/estatisticas/faltas/{pacienteId}")
    @Operation(summary = "Contar faltas do paciente")
    public ResponseEntity<Map<String, Long>> faltasPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(Map.of("totalFaltas", service.contarFaltasPaciente(pacienteId)));
    }

    // ==================== CONSULTAS ESPECIAIS ====================

    @GetMapping("/pendentes")
    @Operation(summary = "Buscar agendamentos passados não finalizados")
    public ResponseEntity<List<AgendamentoResumoResponse>> pendentes() {
        return ResponseEntity.ok(service.buscarPassadosNaoFinalizados());
    }

    @GetMapping("/em-atendimento")
    @Operation(summary = "Buscar agendamentos em atendimento")
    public ResponseEntity<List<AgendamentoResumoResponse>> emAtendimento() {
        return ResponseEntity.ok(service.buscarEmAtendimento());
    }

    @GetMapping("/historico/paciente/{pacienteId}")
    @Operation(summary = "Buscar histórico de consultas do paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> historicoPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.buscarHistoricoPaciente(pacienteId, pageable));
    }
}