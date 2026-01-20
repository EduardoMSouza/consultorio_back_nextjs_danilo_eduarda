package consultorio.api.controller.tratamento;

import consultorio.api.dto.request.tratamento.CriarEvolucaoTratamentoRequest;
import consultorio.api.dto.request.tratamento.AtualizarEvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoDetalheResponse;
import consultorio.api.dto.response.tratamento.ResumoEvolucaoResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import consultorio.domain.service.evolucao_tratamento.EvolucaoTratamentoService;
import consultorio.infrastructure.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/evolucoes-tratamento")
@RequiredArgsConstructor
@Tag(name = "Evolução de Tratamento", description = "Endpoints para gerenciamento de evoluções de tratamento odontológico")
public class EvolucaoTratamentoController {

    private final EvolucaoTratamentoService evolucaoService;

    @GetMapping
    @Operation(summary = "Listar todas as evoluções", description = "Retorna lista de todas as evoluções ordenadas por data decrescente")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> listarTodos() {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar evoluções paginadas", description = "Retorna lista paginada de evoluções")
    public ResponseEntity<ApiResponse<PaginacaoResponse<EvolucaoTratamentoResponse>>> listarPaginado(
            @PageableDefault(size = 20) @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        PaginacaoResponse<EvolucaoTratamentoResponse> response = evolucaoService.listarPaginado(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evolução por ID", description = "Retorna detalhes completos de uma evolução específica")
    public ResponseEntity<ApiResponse<EvolucaoTratamentoDetalheResponse>> buscarPorId(
            @PathVariable @Parameter(description = "ID da evolução") Long id) {
        EvolucaoTratamentoDetalheResponse evolucao = evolucaoService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(evolucao));
    }

    @PostMapping
    @Operation(summary = "Criar nova evolução", description = "Registra uma nova evolução de tratamento para um paciente")
    public ResponseEntity<ApiResponse<EvolucaoTratamentoResponse>> criar(
            @Valid @RequestBody @Parameter(description = "Dados da evolução a ser criada") CriarEvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse evolucao = evolucaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(evolucao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evolução", description = "Atualiza os dados de uma evolução existente")
    public ResponseEntity<ApiResponse<EvolucaoTratamentoResponse>> atualizar(
            @PathVariable @Parameter(description = "ID da evolução") Long id,
            @Valid @RequestBody @Parameter(description = "Dados atualizados da evolução") AtualizarEvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse evolucao = evolucaoService.atualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success(evolucao));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir evolução", description = "Remove uma evolução do sistema (permitido apenas para evoluções recentes)")
    public ResponseEntity<ApiResponse<Void>> excluir(
            @PathVariable @Parameter(description = "ID da evolução a ser excluída") Long id) {
        evolucaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar evoluções por paciente", description = "Retorna todas as evoluções de um paciente específico ordenadas por data decrescente")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> buscarPorPaciente(
            @PathVariable @Parameter(description = "ID do paciente") Long pacienteId) {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/paciente/{pacienteId}/paginado")
    @Operation(summary = "Buscar evoluções por paciente paginadas", description = "Retorna evoluções de um paciente com paginação")
    public ResponseEntity<ApiResponse<PaginacaoResponse<EvolucaoTratamentoResponse>>> buscarPorPacientePaginado(
            @PathVariable @Parameter(description = "ID do paciente") Long pacienteId,
            @PageableDefault(size = 20) @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        PaginacaoResponse<EvolucaoTratamentoResponse> response = evolucaoService.buscarPorPacientePaginado(pacienteId, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Buscar evoluções por dentista", description = "Retorna todas as evoluções registradas por um dentista específico")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> buscarPorDentista(
            @PathVariable @Parameter(description = "ID do dentista") Long dentistaId) {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.buscarPorDentista(dentistaId);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/data/{data}")
    @Operation(summary = "Buscar evoluções por data", description = "Retorna todas as evoluções de uma data específica")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> buscarPorData(
            @PathVariable @Parameter(description = "Data no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.buscarPorData(data);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Buscar evoluções por período", description = "Retorna evoluções dentro de um período específico")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> buscarPorPeriodo(
            @RequestParam @Parameter(description = "Data inicial no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @Parameter(description = "Data final no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/paciente/{pacienteId}/periodo")
    @Operation(summary = "Buscar evoluções por paciente e período", description = "Retorna evoluções de um paciente específico dentro de um período")
    public ResponseEntity<ApiResponse<List<EvolucaoTratamentoResponse>>> buscarPorPacienteEPeriodo(
            @PathVariable @Parameter(description = "ID do paciente") Long pacienteId,
            @RequestParam @Parameter(description = "Data inicial no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @Parameter(description = "Data final no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<EvolucaoTratamentoResponse> evolucoes = evolucaoService.buscarPorPacienteEPeriodo(pacienteId, dataInicio, dataFim);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/paciente/{pacienteId}/ultima")
    @Operation(summary = "Buscar última evolução de um paciente", description = "Retorna a evolução mais recente de um paciente específico")
    public ResponseEntity<ApiResponse<EvolucaoTratamentoDetalheResponse>> buscarUltimaEvolucaoPaciente(
            @PathVariable @Parameter(description = "ID do paciente") Long pacienteId) {
        EvolucaoTratamentoDetalheResponse evolucao = evolucaoService.buscarUltimaEvolucaoPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success(evolucao));
    }

    @GetMapping("/hoje")
    @Operation(summary = "Buscar evoluções do dia", description = "Retorna todas as evoluções registradas no dia atual")
    public ResponseEntity<ApiResponse<List<ResumoEvolucaoResponse>>> buscarEvolucoesDoDia() {
        List<ResumoEvolucaoResponse> evolucoes = evolucaoService.buscarEvolucoesDoDia();
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/buscar-texto")
    @Operation(summary = "Buscar evoluções por texto", description = "Realiza busca textual no conteúdo das evoluções")
    public ResponseEntity<ApiResponse<List<ResumoEvolucaoResponse>>> buscarPorTextoEvolucao(
            @RequestParam @Parameter(description = "Texto a ser buscado nas evoluções") String texto) {
        List<ResumoEvolucaoResponse> evolucoes = evolucaoService.buscarPorTextoEvolucao(texto);
        return ResponseEntity.ok(ApiResponse.success(evolucoes));
    }

    @GetMapping("/paciente/{pacienteId}/contar")
    @Operation(summary = "Contar evoluções por paciente", description = "Retorna a quantidade total de evoluções de um paciente")
    public ResponseEntity<ApiResponse<Long>> contarEvolucoesPorPaciente(
            @PathVariable @Parameter(description = "ID do paciente") Long pacienteId) {
        Long quantidade = evolucaoService.contarEvolucoesPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success(quantidade));
    }

    @GetMapping("/estatisticas/total")
    @Operation(summary = "Contar total de evoluções", description = "Retorna a quantidade total de evoluções no sistema")
    public ResponseEntity<ApiResponse<Long>> contarTotalEvolucoes() {
        Long total = evolucaoService.contarTotalEvolucoes();
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    @GetMapping("/validar/data")
    @Operation(summary = "Verificar se existe evolução na data", description = "Verifica se já existe evolução registrada para um paciente em uma data específica")
    public ResponseEntity<ApiResponse<Boolean>> existeEvolucaoNaData(
            @RequestParam @Parameter(description = "ID do paciente") Long pacienteId,
            @RequestParam @Parameter(description = "Data a verificar no formato ISO (yyyy-MM-dd)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        Boolean existe = evolucaoService.existeEvolucaoNaData(pacienteId, data);
        return ResponseEntity.ok(ApiResponse.success(existe));
    }
}