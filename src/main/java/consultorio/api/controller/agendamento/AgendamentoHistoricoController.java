package consultorio.api.controller.agendamento;

import consultorio.api.dto.response.agendamento.historico.AgendamentoHistoricoResponse;
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.service.agendamento.AgendamentoHistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos/historico")
@RequiredArgsConstructor
public class AgendamentoHistoricoController {

    private final AgendamentoHistoricoService historicoService;

    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(historicoService.buscarPorAgendamento(agendamentoId));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorUsuario(@PathVariable String usuario) {
        return ResponseEntity.ok(historicoService.buscarPorUsuario(usuario));
    }

    @GetMapping("/acao/{acao}")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorAcao(
            @PathVariable AgendamentoHistorico.TipoAcao acao) {
        return ResponseEntity.ok(historicoService.buscarPorAcao(acao));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AgendamentoHistoricoResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(historicoService.buscarPorPeriodo(inicio, fim));
    }
}