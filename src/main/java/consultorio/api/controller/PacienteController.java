package consultorio.api.controller;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import consultorio.domain.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public class PacienteController {

    private final PacienteService service;

    // ==================== CRUD ====================

    @PostMapping
    @Operation(summary = "Criar paciente")
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/prontuario/{prontuarioNumero}")
    @Operation(summary = "Buscar paciente por prontuário")
    public ResponseEntity<PacienteResponse> buscarPorProntuario(@PathVariable String prontuarioNumero) {
        return ResponseEntity.ok(service.buscarPorProntuario(prontuarioNumero));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar paciente por CPF")
    public ResponseEntity<PacienteResponse> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar paciente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    // ==================== LISTAGENS ====================

    @GetMapping
    @Operation(summary = "Listar todos os pacientes")
    public ResponseEntity<Page<PacienteResumoResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pacientes por termo")
    public ResponseEntity<Page<PacienteResumoResponse>> buscar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Boolean status,
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<PacienteResumoResponse> response = (status != null)
                ? service.buscar(termo, status, pageable)
                : service.buscar(termo, pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/convenio/{nomeConvenio}")
    @Operation(summary = "Listar pacientes por convênio")
    public ResponseEntity<Page<PacienteResumoResponse>> listarPorConvenio(
            @PathVariable String nomeConvenio,
            @PageableDefault(size = 20, sort = "dadosBasicos.nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPorConvenio(nomeConvenio, pageable));
    }

    // ==================== STATUS ====================

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar paciente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable Long id) {
        service.ativar(id);
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar paciente")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable Long id) {
        service.inativar(id);
    }

    @PatchMapping("/status")
    @Operation(summary = "Alterar status em lote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarStatusEmLote(
            @RequestParam List<Long> ids,
            @RequestParam Boolean status) {
        service.alterarStatusEmLote(ids, status);
    }

    // ==================== ANIVERSARIANTES ====================

    @GetMapping("/aniversariantes/hoje")
    @Operation(summary = "Listar aniversariantes do dia")
    public ResponseEntity<List<PacienteResumoResponse>> aniversariantesHoje() {
        return ResponseEntity.ok(service.buscarAniversariantesHoje());
    }

    @GetMapping("/aniversariantes/mes/{mes}")
    @Operation(summary = "Listar aniversariantes do mês")
    public ResponseEntity<List<PacienteResumoResponse>> aniversariantesDoMes(
            @PathVariable @Parameter(description = "Mês (1-12)") int mes) {
        return ResponseEntity.ok(service.buscarAniversariantesDoMes(mes));
    }

    // ==================== FILTROS SAÚDE ====================

    @GetMapping("/saude/risco")
    @Operation(summary = "Listar pacientes de risco")
    public ResponseEntity<List<PacienteResumoResponse>> pacientesRisco() {
        return ResponseEntity.ok(service.buscarPacientesRisco());
    }

    @GetMapping("/saude/gestantes")
    @Operation(summary = "Listar gestantes")
    public ResponseEntity<List<PacienteResumoResponse>> gestantes() {
        return ResponseEntity.ok(service.buscarGestantes());
    }

    @GetMapping("/saude/alergicos")
    @Operation(summary = "Listar pacientes alérgicos")
    public ResponseEntity<List<PacienteResumoResponse>> alergicos() {
        return ResponseEntity.ok(service.buscarAlergicos());
    }

    // ==================== ESTATÍSTICAS ====================

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas gerais")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        return ResponseEntity.ok(service.obterEstatisticas());
    }

    @GetMapping("/estatisticas/convenios")
    @Operation(summary = "Obter estatísticas por convênio")
    public ResponseEntity<Map<String, Long>> estatisticasPorConvenio() {
        return ResponseEntity.ok(service.obterEstatisticasPorConvenio());
    }

    // ==================== AUTOCOMPLETE ====================

    @GetMapping("/autocomplete/nome")
    @Operation(summary = "Autocomplete por nome")
    public ResponseEntity<List<Map<String, Object>>> autocompleteNome(@RequestParam String q) {
        return ResponseEntity.ok(service.autocompleteNome(q));
    }

    @GetMapping("/autocomplete/cpf")
    @Operation(summary = "Autocomplete por CPF")
    public ResponseEntity<List<Map<String, Object>>> autocompleteCpf(@RequestParam String q) {
        return ResponseEntity.ok(service.autocompleteCpf(q));
    }
}