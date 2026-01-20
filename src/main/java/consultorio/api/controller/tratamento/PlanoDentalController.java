package consultorio.api.controller.tratamento;

import consultorio.api.dto.request.plano_dental.AtualizarPlanoDentalRequest;
import consultorio.api.dto.request.plano_dental.CriarPlanoDentalRequest;
import consultorio.api.dto.response.plano_dental.PlanoDentalDetalheResponse;
import consultorio.api.dto.response.plano_dental.PlanoDentalResponse;
import consultorio.api.dto.response.plano_dental.ResumoPlanoDentalResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import consultorio.domain.service.plano_dental.PlanoDentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planos-dentais")
@RequiredArgsConstructor
@Tag(name = "Plano Dental", description = "Gerenciamento de planos dentais para pacientes")
public class PlanoDentalController {

    private final PlanoDentalService planoDentalService;

    @Operation(summary = "Listar todos os planos dentais", description = "Retorna uma lista com todos os planos dentais cadastrados")
    @GetMapping
    public ResponseEntity<List<PlanoDentalResponse>> listarTodos() {
        List<PlanoDentalResponse> planos = planoDentalService.listarTodos();
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Listar planos dentais paginados", description = "Retorna uma lista paginada de planos dentais")
    @GetMapping("/paginado")
    public ResponseEntity<PaginacaoResponse<PlanoDentalResponse>> listarPaginado(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(defaultValue = "criadoEm") String ordenarPor,
            @RequestParam(defaultValue = "DESC") String direcao) {

        Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenarPor);
        Pageable pageable = PageRequest.of(pagina, tamanho, sort);

        PaginacaoResponse<PlanoDentalResponse> response = planoDentalService.listarPaginado(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar plano dental por ID", description = "Retorna os detalhes completos de um plano dental específico")
    @GetMapping("/{id}")
    public ResponseEntity<PlanoDentalDetalheResponse> buscarPorId(
            @PathVariable Long id) {
        PlanoDentalDetalheResponse response = planoDentalService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Criar novo plano dental", description = "Cadastra um novo plano dental para um paciente")
    @PostMapping
    public ResponseEntity<PlanoDentalResponse> criar(
            @Valid @RequestBody CriarPlanoDentalRequest request) {
        PlanoDentalResponse response = planoDentalService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Atualizar plano dental", description = "Atualiza os dados de um plano dental existente")
    @PutMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPlanoDentalRequest request) {
        PlanoDentalResponse response = planoDentalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Excluir plano dental", description = "Remove um plano dental do sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        planoDentalService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar planos por paciente", description = "Retorna todos os planos dentais de um paciente específico")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorPaciente(
            @PathVariable Long pacienteId) {
        List<PlanoDentalResponse> planos = planoDentalService.buscarPorPaciente(pacienteId);
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos por paciente (paginado)", description = "Retorna planos dentais de um paciente de forma paginada")
    @GetMapping("/paciente/{pacienteId}/paginado")
    public ResponseEntity<PaginacaoResponse<PlanoDentalResponse>> buscarPorPacientePaginado(
            @PathVariable Long pacienteId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {

        Pageable pageable = PageRequest.of(pagina, tamanho);
        PaginacaoResponse<PlanoDentalResponse> response =
                planoDentalService.buscarPorPacientePaginado(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar planos por dentista", description = "Retorna todos os planos dentais realizados por um dentista")
    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorDentista(
            @PathVariable Long dentistaId) {
        List<PlanoDentalResponse> planos = planoDentalService.buscarPorDentista(dentistaId);
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos por dente", description = "Retorna todos os planos dentais para um dente específico")
    @GetMapping("/dente/{dente}")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorDente(
            @PathVariable String dente) {
        List<PlanoDentalResponse> planos = planoDentalService.buscarPorDente(dente);
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos por procedimento", description = "Busca planos dentais por tipo de procedimento")
    @GetMapping("/procedimento")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorProcedimento(
            @RequestParam String procedimento) {
        List<PlanoDentalResponse> planos = planoDentalService.buscarPorProcedimento(procedimento);
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos por período", description = "Retorna planos dentais criados em um período específico")
    @GetMapping("/periodo")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<PlanoDentalResponse> planos = planoDentalService.buscarPorPeriodo(
                dataInicio.toString(), dataFim.toString());
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos por paciente e período", description = "Retorna planos dentais de um paciente em um período específico")
    @GetMapping("/paciente/{pacienteId}/periodo")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPorPacienteEPeriodo(
            @PathVariable Long pacienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        List<PlanoDentalResponse> planos = planoDentalService.buscarPorPacienteEPeriodo(
                pacienteId, dataInicio.toString(), dataFim.toString());
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos com desconto", description = "Retorna todos os planos dentais que possuem desconto aplicado")
    @GetMapping("/com-desconto")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPlanosComDesconto() {
        List<PlanoDentalResponse> planos = planoDentalService.buscarPlanosComDesconto();
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Buscar planos recentes", description = "Retorna os planos dentais mais recentes")
    @GetMapping("/recentes")
    public ResponseEntity<List<ResumoPlanoDentalResponse>> buscarPlanosRecentes(
            @RequestParam(defaultValue = "10") int quantidade) {
        List<ResumoPlanoDentalResponse> planos = planoDentalService.buscarPlanosRecentes(quantidade);
        return ResponseEntity.ok(planos);
    }

    @Operation(summary = "Calcular valor total por paciente", description = "Calcula o valor total de todos os planos de um paciente")
    @GetMapping("/paciente/{pacienteId}/valor-total")
    public ResponseEntity<BigDecimal> calcularValorTotalPorPaciente(
            @PathVariable Long pacienteId) {
        BigDecimal valorTotal = planoDentalService.calcularValorTotalPorPaciente(pacienteId);
        return ResponseEntity.ok(valorTotal);
    }

    @Operation(summary = "Calcular valor total por dentista", description = "Calcula o valor total de todos os planos realizados por um dentista")
    @GetMapping("/dentista/{dentistaId}/valor-total")
    public ResponseEntity<BigDecimal> calcularValorTotalPorDentista(
            @PathVariable Long dentistaId) {
        BigDecimal valorTotal = planoDentalService.calcularValorTotalPorDentista(dentistaId);
        return ResponseEntity.ok(valorTotal);
    }

    @Operation(summary = "Contar planos por paciente", description = "Retorna a quantidade de planos dentais de um paciente")
    @GetMapping("/paciente/{pacienteId}/contagem")
    public ResponseEntity<Long> contarPlanosPorPaciente(@PathVariable Long pacienteId) {
        Long contagem = planoDentalService.contarPlanosPorPaciente(pacienteId);
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Verificar plano para dente", description = "Verifica se já existe plano para um dente específico do paciente")
    @GetMapping("/verificar-dente")
    public ResponseEntity<Boolean> verificarPlanoParaDente(
            @RequestParam Long pacienteId,
            @RequestParam String dente) {
        boolean existe = planoDentalService.existePlanoParaDente(pacienteId, dente);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Aplicar desconto ao plano", description = "Aplica um desconto no valor de um plano dental")
    @PatchMapping("/{id}/aplicar-desconto")
    public ResponseEntity<PlanoDentalResponse> aplicarDesconto(
            @PathVariable Long id,
            @RequestParam BigDecimal valorDesconto) {
        PlanoDentalResponse response = planoDentalService.aplicarDesconto(id, valorDesconto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar top procedimentos", description = "Retorna os procedimentos mais realizados")
    @GetMapping("/top-procedimentos")
    public ResponseEntity<List<Object[]>> buscarTopProcedimentos() {
        List<Object[]> topProcedimentos = planoDentalService.buscarTopProcedimentos();
        return ResponseEntity.ok(topProcedimentos);
    }

    @Operation(summary = "Estatísticas do sistema", description = "Retorna estatísticas gerais sobre planos dentais")
    @GetMapping("/estatisticas")
    public ResponseEntity<?> estatisticas() {
        // Este método pode ser expandido para retornar um DTO específico de estatísticas
        return ResponseEntity.ok("Endpoint de estatísticas - Em desenvolvimento");
    }
}