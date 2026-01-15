package consultorio.api.controller.tratamento;

import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;

import consultorio.domain.service.evolucao_tratamento.EvolucaoTratamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/evolucoes-tratamento")
@RequiredArgsConstructor
@Tag(name = "Evoluções de Tratamento", description = "API para gerenciamento de evoluções de tratamento odontológico")
public class EvolucaoTratamentoController {

    private final EvolucaoTratamentoService evolucaoTratamentoService;

    @Operation(summary = "Criar uma nova evolução de tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evolução criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Recursos relacionados não encontrados")
    })
    @PostMapping
    public ResponseEntity<EvolucaoTratamentoResponse> criar(
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar evolução por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução encontrada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvolucaoTratamentoResponse> buscarPorId(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todas as evoluções ativas")
    @GetMapping
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarTodos() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar evolução completa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvolucaoTratamentoResponse> atualizar(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualização parcial da evolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução atualizada parcialmente"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EvolucaoTratamentoResponse> atualizarParcial(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.atualizarParcial(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desativar evolução (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @DeleteMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar evolução previamente desativada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução ativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir permanentemente uma evolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução excluída permanentemente"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar evoluções por paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções por dentista")
    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarPorDentista(
            @Parameter(description = "ID do dentista") @PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções por data")
    @GetMapping("/data/{data}")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarPorData(
            @Parameter(description = "Data no formato yyyy-MM-dd")
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarPorData(data);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções por período")
    @GetMapping("/periodo")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarPorPeriodo(
            @Parameter(description = "Data inicial no formato yyyy-MM-dd")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
            @Parameter(description = "Data final no formato yyyy-MM-dd")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções urgentes")
    @GetMapping("/urgentes")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarUrgentes() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarUrgentes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções com retorno necessário")
    @GetMapping("/retorno-necessario")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarComRetornoNecessario() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarComRetornoNecessario();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções com retorno atrasado")
    @GetMapping("/retornos-atrasados")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarRetornosAtrasados() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarRetornosAtrasados();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar evolução como urgente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução marcada como urgente"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @PatchMapping("/{id}/urgente")
    public ResponseEntity<EvolucaoTratamentoResponse> marcarComoUrgente(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.marcarComoUrgente(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Agendar retorno para uma evolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno agendado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    @PostMapping("/{id}/agendar-retorno")
    public ResponseEntity<EvolucaoTratamentoResponse> agendarRetorno(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @Parameter(description = "Data do retorno no formato yyyy-MM-dd")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRetorno,
            @Parameter(description = "Motivo do retorno") @RequestParam String motivo) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.agendarRetorno(id, dataRetorno, motivo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evoluções com filtros combinados")
    @GetMapping("/filtros")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> buscarComFiltros(
            @Parameter(description = "ID do paciente") @RequestParam(required = false) Long pacienteId,
            @Parameter(description = "ID do dentista") @RequestParam(required = false) Long dentistaId,
            @Parameter(description = "ID do plano dental") @RequestParam(required = false) Long planoDentalId,
            @Parameter(description = "Data inicial no formato yyyy-MM-dd")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataInicio,
            @Parameter(description = "Data final no formato yyyy-MM-dd")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataFim,
            @Parameter(description = "Tipo de evolução") @RequestParam(required = false) String tipoEvolucao,
            @Parameter(description = "Filtrar por urgência") @RequestParam(required = false) Boolean urgente) {

        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.buscarComFiltros(
                pacienteId, dentistaId, planoDentalId, dataInicio, dataFim, tipoEvolucao, urgente);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Contar evoluções de um paciente")
    @GetMapping("/paciente/{pacienteId}/contagem")
    public ResponseEntity<Long> contarPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        Long contagem = evolucaoTratamentoService.contarPorPaciente(pacienteId);
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Buscar última evolução de um paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Última evolução encontrada"),
            @ApiResponse(responseCode = "404", description = "Nenhuma evolução encontrada para o paciente")
    })
    @GetMapping("/paciente/{pacienteId}/ultima")
    public ResponseEntity<EvolucaoTratamentoResponse> buscarUltimaEvolucaoPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.buscarUltimaEvolucaoPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verificar se evolução existe")
    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existePorId(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        boolean existe = evolucaoTratamentoService.existePorId(id);
        return ResponseEntity.ok(existe);
    }
}