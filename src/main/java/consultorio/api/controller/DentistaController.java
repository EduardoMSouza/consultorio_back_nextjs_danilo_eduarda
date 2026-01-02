package consultorio.api.controller;

import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import consultorio.domain.service.DentistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/dentistas")
@RequiredArgsConstructor
@Tag(name = "Dentistas", description = "Gerenciamento de dentistas")
public class DentistaController {

    private final DentistaService service;

    // ==================== CRUD ====================

    @PostMapping
    @Operation(summary = "Criar dentista")
    public ResponseEntity<DentistaResponse> criar(@Valid @RequestBody DentistaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dentista por ID")
    public ResponseEntity<DentistaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cro/{cro}")
    @Operation(summary = "Buscar dentista por CRO")
    public ResponseEntity<DentistaResponse> buscarPorCro(@PathVariable String cro) {
        return ResponseEntity.ok(service.buscarPorCro(cro));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dentista")
    public ResponseEntity<DentistaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DentistaRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar dentista")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // ==================== LISTAGENS ====================

    @GetMapping
    @Operation(summary = "Listar todos os dentistas")
    public ResponseEntity<Page<DentistaResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar dentistas por termo")
    public ResponseEntity<Page<DentistaResponse>> buscar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<DentistaResponse> response = (ativo != null)
                ? service.buscar(termo, ativo, pageable)
                : service.buscar(termo, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/especialidade/{especialidade}")
    @Operation(summary = "Listar dentistas por especialidade")
    public ResponseEntity<Page<DentistaResponse>> listarPorEspecialidade(
            @PathVariable String especialidade,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorEspecialidade(especialidade, pageable));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar dentistas ativos")
    public ResponseEntity<List<DentistaResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    // ==================== STATUS ====================

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar dentista")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long id) {
        service.ativar(id);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar dentista")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        service.inativar(id);
    }

    @PatchMapping("/status")
    @Operation(summary = "Alterar status em lote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarStatusEmLote(
            @RequestParam List<Long> ids,
            @RequestParam Boolean ativo) {
        service.alterarStatusEmLote(ids, ativo);
    }

    // ==================== DISPONIBILIDADE ====================

    @GetMapping("/disponiveis")
    @Operation(summary = "Buscar dentistas disponíveis")
    public ResponseEntity<List<DentistaResponse>> buscarDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFim) {
        return ResponseEntity.ok(service.buscarDisponiveis(data, horaInicio, horaFim));
    }

    @GetMapping("/{id}/disponivel")
    @Operation(summary = "Verificar disponibilidade do dentista")
    public ResponseEntity<Map<String, Boolean>> verificarDisponibilidade(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFim) {
        boolean disponivel = service.verificarDisponibilidade(id, data, horaInicio, horaFim);
        return ResponseEntity.ok(Map.of("disponivel", disponivel));
    }

    // ==================== ESTATÍSTICAS ====================

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        return ResponseEntity.ok(service.obterEstatisticas());
    }

    @GetMapping("/especialidades")
    @Operation(summary = "Listar especialidades disponíveis")
    public ResponseEntity<List<String>> listarEspecialidades() {
        return ResponseEntity.ok(service.listarEspecialidades());
    }

    @GetMapping("/agenda-dia")
    @Operation(summary = "Listar agenda do dia por dentista")
    public ResponseEntity<List<Map<String, Object>>> agendaDoDia(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "Data (padrão: hoje)") LocalDate data) {
        return ResponseEntity.ok(service.listarAgendaDoDia(data != null ? data : LocalDate.now()));
    }

    // ==================== AUTOCOMPLETE ====================

    @GetMapping("/autocomplete")
    @Operation(summary = "Autocomplete por nome")
    public ResponseEntity<List<Map<String, Object>>> autocomplete(@RequestParam String q) {
        return ResponseEntity.ok(service.autocompleteNome(q));
    }
}