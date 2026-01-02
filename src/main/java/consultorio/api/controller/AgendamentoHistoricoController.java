package consultorio.api.controller;

import consultorio.api.dto.response.AgendamentoHistoricoResponse;
import consultorio.domain.service.AgendamentoHistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos/historico")
@RequiredArgsConstructor
@Tag(name = "Histórico de Agendamentos", description = "Consulta de histórico/auditoria")
public class AgendamentoHistoricoController {

    private final AgendamentoHistoricoService service;

    @GetMapping("/agendamento/{agendamentoId}")
    @Operation(summary = "Buscar histórico por agendamento")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(service.buscarPorAgendamento(agendamentoId));
    }

    @GetMapping("/agendamento/{agendamentoId}/paginado")
    @Operation(summary = "Buscar histórico por agendamento (paginado)")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorAgendamentoPaginado(
            @PathVariable Long agendamentoId,
            @PageableDefault(size = 20, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorAgendamento(agendamentoId, pageable));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar histórico por período")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @PageableDefault(size = 20, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        return ResponseEntity.ok(service.buscarPorPeriodo(inicioDateTime, fimDateTime, pageable));
    }

    @GetMapping("/usuario/{usuario}")
    @Operation(summary = "Buscar histórico por usuário")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorUsuario(
            @PathVariable String usuario,
            @PageableDefault(size = 20, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuario, pageable));
    }

    @GetMapping("/agendamento/{agendamentoId}/ultimo")
    @Operation(summary = "Buscar último registro do agendamento")
    public ResponseEntity<AgendamentoHistoricoResponse> buscarUltimo(@PathVariable Long agendamentoId) {
        AgendamentoHistoricoResponse ultimo = service.buscarUltimo(agendamentoId);
        return ultimo != null ? ResponseEntity.ok(ultimo) : ResponseEntity.noContent().build();
    }

    @GetMapping("/cancelamentos")
    @Operation(summary = "Buscar cancelamentos no período")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarCancelamentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        return ResponseEntity.ok(service.buscarCancelamentos(inicioDateTime, fimDateTime));
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de histórico")
    public ResponseEntity<Map<String, Object>> estatisticas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        return ResponseEntity.ok(service.obterEstatisticas(inicioDateTime, fimDateTime));
    }
}