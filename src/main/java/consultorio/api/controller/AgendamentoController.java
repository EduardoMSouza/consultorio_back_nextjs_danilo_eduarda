package consultorio.api.controller;

<<<<<<< HEAD
import consultorio.api.dto.request.agendamento.AgendamentoCreateRequest;
import consultorio.api.dto.request.agendamento.AgendamentoUpdateRequest;
import consultorio.api.dto.request.agendamento.AgendamentoCancelamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.api.dto.response.agendamento.AgendamentoResumoResponse;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.service.AgendamentoService;
=======
import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.request.CancelamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
<<<<<<< HEAD
=======
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
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
<<<<<<< HEAD
=======
@Tag(name = "Agendamentos", description = "Gerenciamento de agendamentos")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
public class AgendamentoController {

    private final AgendamentoService service;

<<<<<<< HEAD
    // CRUD
    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoCreateRequest request) {
=======
    // ==================== CRUD ====================

    @PostMapping
    @Operation(summary = "Criar agendamento")
    public ResponseEntity<AgendamentoResponse> criar(@Valid @RequestBody AgendamentoRequest request) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
<<<<<<< HEAD
=======
    @Operation(summary = "Buscar agendamento por ID")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoUpdateRequest request) {
=======
    @Operation(summary = "Atualizar agendamento")
    public ResponseEntity<AgendamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AgendamentoRequest request) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Listagens
    @GetMapping
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarTodos(Pageable pageable) {
=======
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
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/dentista/{dentistaId}")
<<<<<<< HEAD
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(@PathVariable Long dentistaId, Pageable pageable) {
=======
    @Operation(summary = "Listar agendamentos por dentista")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorDentista(
            @PathVariable Long dentistaId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.listarPorDentista(dentistaId, pageable));
    }

    @GetMapping("/paciente/{pacienteId}")
<<<<<<< HEAD
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(@PathVariable Long pacienteId, Pageable pageable) {
=======
    @Operation(summary = "Listar agendamentos por paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.listarPorPaciente(pacienteId, pageable));
    }

    @GetMapping("/status/{status}")
<<<<<<< HEAD
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(@PathVariable StatusAgendamento status, Pageable pageable) {
=======
    @Operation(summary = "Listar agendamentos por status")
    public ResponseEntity<Page<AgendamentoResumoResponse>> listarPorStatus(
            @PathVariable StatusAgendamento status,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.listarPorStatus(status, pageable));
    }

    @GetMapping("/periodo")
<<<<<<< HEAD
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
=======
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
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.buscarProximosPaciente(pacienteId));
    }

    @GetMapping("/proximos/dentista/{dentistaId}")
<<<<<<< HEAD
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarProximosDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarProximosDentista(dentistaId));
    }

    // Mudança de Status
    @PutMapping("/{id}/confirmar")
=======
    @Operation(summary = "Buscar próximos agendamentos do dentista")
    public ResponseEntity<List<AgendamentoResumoResponse>> proximosDentista(@PathVariable Long dentistaId) {
        return ResponseEntity.ok(service.buscarProximosDentista(dentistaId));
    }

    // ==================== MUDANÇA DE STATUS ====================

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public ResponseEntity<AgendamentoResponse> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

<<<<<<< HEAD
    @PutMapping("/{id}/iniciar")
=======
    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar atendimento")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public ResponseEntity<AgendamentoResponse> iniciarAtendimento(@PathVariable Long id) {
        return ResponseEntity.ok(service.iniciarAtendimento(id));
    }

<<<<<<< HEAD
    @PutMapping("/{id}/concluir")
=======
    @PatchMapping("/{id}/concluir")
    @Operation(summary = "Concluir atendimento")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public ResponseEntity<AgendamentoResponse> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(service.concluir(id));
    }

<<<<<<< HEAD
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id, @Valid @RequestBody AgendamentoCancelamentoRequest request) {
        return ResponseEntity.ok(service.cancelar(id, request.getMotivoCancelamento()));
    }

    @PutMapping("/{id}/falta")
=======
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento")
    public ResponseEntity<AgendamentoResponse> cancelar(@PathVariable Long id, @Valid @RequestBody CancelamentoRequest request) {
        return ResponseEntity.ok(service.cancelar(id, request.getMotivo()));
    }

    @PatchMapping("/{id}/falta")
    @Operation(summary = "Marcar falta")
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
    public ResponseEntity<AgendamentoResponse> marcarFalta(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarFalta(id));
    }

<<<<<<< HEAD
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
=======
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
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.buscarPassadosNaoFinalizados());
    }

    @GetMapping("/em-atendimento")
<<<<<<< HEAD
    public ResponseEntity<List<AgendamentoResumoResponse>> buscarEmAtendimento() {
=======
    @Operation(summary = "Buscar agendamentos em atendimento")
    public ResponseEntity<List<AgendamentoResumoResponse>> emAtendimento() {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.buscarEmAtendimento());
    }

    @GetMapping("/historico/paciente/{pacienteId}")
<<<<<<< HEAD
    public ResponseEntity<Page<AgendamentoResumoResponse>> buscarHistoricoPaciente(@PathVariable Long pacienteId, Pageable pageable) {
=======
    @Operation(summary = "Buscar histórico de consultas do paciente")
    public ResponseEntity<Page<AgendamentoResumoResponse>> historicoPaciente(
            @PathVariable Long pacienteId,
            @PageableDefault(size = 20, sort = "dataConsulta", direction = Sort.Direction.DESC) Pageable pageable) {
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(service.buscarHistoricoPaciente(pacienteId, pageable));
    }
}