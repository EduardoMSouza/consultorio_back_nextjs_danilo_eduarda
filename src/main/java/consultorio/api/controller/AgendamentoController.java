package consultorio.api.controller;

import consultorio.api.dto.request.agendamento.AgendamentoCreateRequest;
import consultorio.api.dto.request.agendamento.AgendamentoUpdateRequest;
import consultorio.api.dto.request.agendamento.AgendamentoCancelamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.api.dto.response.agendamento.AgendamentoResumoResponse;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.service.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // CRUD
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoUpdateRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Listagens
    @GetMapping
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(@PathVariable Long dentistaId, Pageable pageable) {
        return ResponseEntity.ok(service.listarPorDentista(dentistaId, pageable));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(@PathVariable Long pacienteId, Pageable pageable) {
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(@PathVariable StatusAgendamento status, Pageable pageable) {
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPeriodo(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorPeriodo(inicio, fim, pageable));
    }

    // Agenda
    @GetMapping("/agenda/dia")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarAgendaDoDia(
            @RequestParam(required = false) Long dentistaId,
            @RequestParam LocalDate data) {
        if (dentistaId != null) {
            return ResponseEntity.ok(service.buscarAgendaDoDia(dentistaId, data));
        } else {
            return ResponseEntity.ok(service.buscarAgendaDoDia(data));
        }
    }

    @GetMapping("/proximos/paciente/{pacienteId}")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.buscarProximosPaciente(pacienteId));
    }

    @GetMapping("/proximos/dentista/{dentistaId}")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarProximosDentista(dentistaId));
    }

    // Mudança de Status
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<AgendamentoResponse> iniciarAtendimento(@PathVariable Long id) {
        return ResponseEntity.ok(service.iniciarAtendimento(id));
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(service.concluir(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id, @Valid @RequestBody AgendamentoCancelamentoRequest request) {
        return ResponseEntity.ok(service.cancelar(id, request.getMotivoCancelamento()));
    }

    @PutMapping("/{id}/falta")
    public ResponseEntity<AgendamentoResponse> marcarFalta(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarFalta(id));
    }

    // Disponibilidade
    @GetMapping("/disponibilidade")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @RequestParam Long dentistaId,
            @RequestParam LocalDate data,
            @RequestParam LocalTime horaInicio,
            @RequestParam LocalTime horaFim) {
        return ResponseEntity.ok(service.verificarDisponibilidade(dentistaId, data, horaInicio, horaFim));
    }

    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<Map<String, LocalTime>>> buscarHorariosDisponiveis(
            @RequestParam Long dentistaId,
            @RequestParam LocalDate data,
            @RequestParam int duracaoMinutos) {
        return ResponseEntity.ok(service.buscarHorariosDisponiveis(dentistaId, data, duracaoMinutos));
    }

    // Lembretes
    @PostMapping("/lembretes")
    public ResponseEntity<Integer> enviarLembretes(@RequestParam LocalDate data) {
        return ResponseEntity.ok(service.enviarLembretes(data));
    }

    // Estatísticas
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return ResponseEntity.ok(service.obterEstatisticas(inicio, fim));
    }

    @GetMapping("/contar/dia")
    public ResponseEntity<Long> contarConsultasDoDia(
            @RequestParam Long dentistaId,
            @RequestParam LocalDate data) {
        return ResponseEntity.ok(service.contarConsultasDoDia(dentistaId, data));
    }

    @GetMapping("/contar/faltas/{pacienteId}")
    public ResponseEntity<Long> contarFaltasPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.contarFaltasPaciente(pacienteId));
    }

    // Consultas Especiais
    @GetMapping("/passados-nao-finalizados")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarPassadosNaoFinalizados() {
        return ResponseEntity.ok(service.buscarPassadosNaoFinalizados());
    }

    @GetMapping("/em-atendimento")
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarEmAtendimento() {
        return ResponseEntity.ok(service.buscarEmAtendimento());
    }

    @GetMapping("/historico/paciente/{pacienteId}")
    public ResponseEntity<Page<AgendamentoResumoResponse>> buscarHistoricoPaciente(@PathVariable Long pacienteId, Pageable pageable) {
        return ResponseEntity.ok(service.buscarHistoricoPaciente(pacienteId, pageable));
    }
}