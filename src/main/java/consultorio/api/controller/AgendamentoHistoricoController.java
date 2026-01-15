package consultorio.api.controller;

import consultorio.domain.service.AgendamentoHistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historico")
@RequiredArgsConstructor
public class AgendamentoHistoricoController {

    private final AgendamentoHistoricoService service;

    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(service.buscarPorAgendamento(agendamentoId));
    }

    @GetMapping("/agendamento/{agendamentoId}/paginado")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorAgendamentoPaginado(
            @PathVariable Long agendamentoId, Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorAgendamento(agendamentoId, pageable));
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim,
            Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorPeriodo(inicio, fim, pageable));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<Page<AgendamentoHistoricoResponse>> buscarPorUsuario(
            @PathVariable String usuario, Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuario, pageable));
    }

    @GetMapping("/ultimo/{agendamentoId}")
    public ResponseEntity<AgendamentoHistoricoResponse> buscarUltimo(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(service.buscarUltimo(agendamentoId));
    }

    @GetMapping("/cancelamentos")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarCancelamentos(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.buscarCancelamentos(inicio, fim));
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        return ResponseEntity.ok(service.obterEstatisticas(inicio, fim));
    }
}