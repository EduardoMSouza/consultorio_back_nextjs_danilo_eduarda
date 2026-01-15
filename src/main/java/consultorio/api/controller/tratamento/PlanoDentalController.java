package consultorio.api.controller.tratamento;

import consultorio.api.dto.request.tratamento.PlanoDentalRequest;
import consultorio.api.dto.response.tratamento.PlanoDentalResponse;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import consultorio.domain.service.plano_dental.PlanoDentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/planos-dentais")
@RequiredArgsConstructor
@Tag(name = "Planos Dentais", description = "API para gerenciamento de planos de tratamento odontológico")
public class PlanoDentalController {

    private final PlanoDentalService planoDentalService;

    @Operation(summary = "Criar novo plano dental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plano criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente ou dentista não encontrado")
    })
    @PostMapping
    public ResponseEntity<PlanoDentalResponse> criar(@Valid @RequestBody PlanoDentalRequest request) {
        PlanoDentalResponse response = planoDentalService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Buscar plano por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> buscarPorId(@PathVariable Long id) {
        PlanoDentalResponse response = planoDentalService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os planos (paginado)")
    @GetMapping("/paginado")
    public ResponseEntity<Page<PlanoDentalResponse>> listarPaginado(Pageable pageable) {
        Page<PlanoDentalResponse> response = planoDentalService.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os planos")
    @GetMapping
    public ResponseEntity<List<PlanoDentalResponse>> listarTodos() {
        List<PlanoDentalResponse> response = planoDentalService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar plano dental")
    @PutMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlanoDentalRequest request) {
        PlanoDentalResponse response = planoDentalService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualização parcial do plano")
    @PatchMapping("/{id}")
    public ResponseEntity<PlanoDentalResponse> atualizarParcial(
            @PathVariable Long id,
            @RequestBody PlanoDentalRequest request) {
        PlanoDentalResponse response = planoDentalService.atualizarParcial(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desativar plano")
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        planoDentalService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar plano")
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        planoDentalService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir plano permanentemente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        planoDentalService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Concluir plano")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<PlanoDentalResponse> concluir(@PathVariable Long id) {
        PlanoDentalResponse response = planoDentalService.concluir(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancelar plano")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PlanoDentalResponse> cancelar(
            @PathVariable Long id,
            @RequestParam String motivo) {
        PlanoDentalResponse response = planoDentalService.cancelar(id, motivo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Iniciar plano pendente")
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<PlanoDentalResponse> iniciar(@PathVariable Long id) {
        PlanoDentalResponse response = planoDentalService.iniciar(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Aplicar desconto ao plano")
    @PatchMapping("/{id}/desconto")
    public ResponseEntity<PlanoDentalResponse> aplicarDesconto(
            @PathVariable Long id,
            @RequestParam BigDecimal desconto) {
        PlanoDentalResponse response = planoDentalService.aplicarDesconto(id, desconto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualizar valor do plano")
    @PatchMapping("/{id}/valor")
    public ResponseEntity<PlanoDentalResponse> atualizarValor(
            @PathVariable Long id,
            @RequestParam BigDecimal valor) {
        PlanoDentalResponse response = planoDentalService.atualizarValor(id, valor);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar plano como urgente")
    @PatchMapping("/{id}/urgente")
    public ResponseEntity<PlanoDentalResponse> marcarComoUrgente(@PathVariable Long id) {
        PlanoDentalResponse response = planoDentalService.marcarComoUrgente(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remover urgência do plano")
    @PatchMapping("/{id}/remover-urgencia")
    public ResponseEntity<PlanoDentalResponse> removerUrgencia(@PathVariable Long id) {
        PlanoDentalResponse response = planoDentalService.removerUrgencia(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por paciente")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por paciente (paginado)")
    @GetMapping("/paciente/{pacienteId}/paginado")
    public ResponseEntity<Page<PlanoDentalResponse>> listarPorPacientePaginado(
            @PathVariable Long pacienteId,
            Pageable pageable) {
        Page<PlanoDentalResponse> response = planoDentalService.listarPorPaciente(pacienteId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por dentista")
    @GetMapping("/dentista/{dentistaId}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por dentista (paginado)")
    @GetMapping("/dentista/{dentistaId}/paginado")
    public ResponseEntity<Page<PlanoDentalResponse>> listarPorDentistaPaginado(
            @PathVariable Long dentistaId,
            Pageable pageable) {
        Page<PlanoDentalResponse> response = planoDentalService.listarPorDentista(dentistaId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por paciente e dentista")
    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPacienteEDentista(
            @PathVariable Long pacienteId,
            @PathVariable Long dentistaId) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorPacienteEDentista(pacienteId, dentistaId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorStatus(@PathVariable StatusPlano status) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por status (paginado)")
    @GetMapping("/status/{status}/paginado")
    public ResponseEntity<Page<PlanoDentalResponse>> listarPorStatusPaginado(
            @PathVariable StatusPlano status,
            Pageable pageable) {
        Page<PlanoDentalResponse> response = planoDentalService.listarPorStatus(status, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por múltiplos status")
    @GetMapping("/status")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorStatusEm(
            @RequestParam List<StatusPlano> statuses) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorStatusEm(statuses);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos ativos por paciente ordenados por data prevista")
    @GetMapping("/paciente/{pacienteId}/ativos-ordenados")
    public ResponseEntity<List<PlanoDentalResponse>> listarAtivosOrdenadosPorDataPrevista(
            @PathVariable Long pacienteId) {
        List<PlanoDentalResponse> response = planoDentalService.listarAtivosPorPacienteOrdenadosPorDataPrevista(pacienteId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por data prevista")
    @GetMapping("/data-prevista")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorDataPrevistaEntre(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime inicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fim) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorDataPrevistaEntre(inicio, fim);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos urgentes")
    @GetMapping("/urgentes")
    public ResponseEntity<List<PlanoDentalResponse>> listarUrgentes() {
        List<PlanoDentalResponse> response = planoDentalService.listarUrgentes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos urgentes por status")
    @GetMapping("/urgentes/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> listarUrgentesPorStatus(@PathVariable StatusPlano status) {
        List<PlanoDentalResponse> response = planoDentalService.listarUrgentesPorStatus(status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por paciente e status")
    @GetMapping("/paciente/{pacienteId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPacienteEStatus(
            @PathVariable Long pacienteId,
            @PathVariable StatusPlano status) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorPacienteEStatus(pacienteId, status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por dentista e status")
    @GetMapping("/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorDentistaEStatus(
            @PathVariable Long dentistaId,
            @PathVariable StatusPlano status) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorDentistaEStatus(dentistaId, status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar planos por paciente, dentista e status")
    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<List<PlanoDentalResponse>> listarPorPacienteDentistaEStatus(
            @PathVariable Long pacienteId,
            @PathVariable Long dentistaId,
            @PathVariable StatusPlano status) {
        List<PlanoDentalResponse> response = planoDentalService.listarPorPacienteDentistaEStatus(pacienteId, dentistaId, status);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar planos com filtros avançados")
    @GetMapping("/filtros")
    public ResponseEntity<List<PlanoDentalResponse>> buscarComFiltros(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long dentistaId,
            @RequestParam(required = false) StatusPlano status,
            @RequestParam(required = false) String dente,
            @RequestParam(required = false) String procedimento,
            @RequestParam(required = false) Boolean urgente,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataFim) {

        List<PlanoDentalResponse> response = planoDentalService.buscarComFiltros(
                pacienteId, dentistaId, status, dente, procedimento, urgente, ativo, dataInicio, dataFim);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar planos ativos com filtros")
    @GetMapping("/filtros/ativos")
    public ResponseEntity<List<PlanoDentalResponse>> buscarAtivosComFiltros(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long dentistaId,
            @RequestParam(required = false) StatusPlano status,
            @RequestParam(required = false) String dente,
            @RequestParam(required = false) String procedimento,
            @RequestParam(required = false) Boolean urgente) {

        List<PlanoDentalResponse> response = planoDentalService.buscarAtivosComFiltros(
                pacienteId, dentistaId, status, dente, procedimento, urgente);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Calcular valor total por paciente e status")
    @GetMapping("/calcular-total/paciente/{pacienteId}/status/{status}")
    public ResponseEntity<BigDecimal> calcularTotalPorPacienteEStatus(
            @PathVariable Long pacienteId,
            @PathVariable StatusPlano status) {
        BigDecimal total = planoDentalService.calcularTotalPorPacienteEStatus(pacienteId, status);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Calcular valor total por dentista e status")
    @GetMapping("/calcular-total/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<BigDecimal> calcularTotalPorDentistaEStatus(
            @PathVariable Long dentistaId,
            @PathVariable StatusPlano status) {
        BigDecimal total = planoDentalService.calcularTotalPorDentistaEStatus(dentistaId, status);
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Calcular valor total de planos ativos")
    @GetMapping("/calcular-total/ativos")
    public ResponseEntity<BigDecimal> calcularValorTotalAtivos() {
        BigDecimal total = planoDentalService.calcularValorTotalAtivos();
        return ResponseEntity.ok(total);
    }

    @Operation(summary = "Contar planos ativos por paciente")
    @GetMapping("/contar/ativos/paciente/{pacienteId}")
    public ResponseEntity<Long> contarAtivosPorPaciente(@PathVariable Long pacienteId) {
        Long contagem = planoDentalService.contarAtivosPorPaciente(pacienteId);
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Contar planos por paciente e status")
    @GetMapping("/contar/paciente/{pacienteId}/status/{status}")
    public ResponseEntity<Long> contarPorPacienteEStatus(
            @PathVariable Long pacienteId,
            @PathVariable StatusPlano status) {
        Long contagem = planoDentalService.contarPorPacienteEStatus(pacienteId, status);
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Contar planos por dentista e status")
    @GetMapping("/contar/dentista/{dentistaId}/status/{status}")
    public ResponseEntity<Long> contarPorDentistaEStatus(
            @PathVariable Long dentistaId,
            @PathVariable StatusPlano status) {
        Long contagem = planoDentalService.contarPorDentistaEStatus(dentistaId, status);
        return ResponseEntity.ok(contagem);
    }

    @Operation(summary = "Obter estatísticas por status")
    @GetMapping("/estatisticas/status")
    public ResponseEntity<List<Object[]>> obterEstatisticasPorStatus() {
        List<Object[]> estatisticas = planoDentalService.obterEstatisticasPorStatus();
        return ResponseEntity.ok(estatisticas);
    }

    @Operation(summary = "Verificar se plano existe")
    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> existePorId(@PathVariable Long id) {
        boolean existe = planoDentalService.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Verificar se plano está ativo")
    @GetMapping("/verificar/{id}/ativo")
    public ResponseEntity<Boolean> estaAtivo(@PathVariable Long id) {
        boolean ativo = planoDentalService.estaAtivo(id);
        return ResponseEntity.ok(ativo);
    }

    @Operation(summary = "Verificar se existe plano ativo para paciente, dente e procedimento")
    @GetMapping("/verificar/existe-plano-ativo")
    public ResponseEntity<Boolean> existePlanoAtivoParaPacienteDenteProcedimento(
            @RequestParam Long pacienteId,
            @RequestParam String dente,
            @RequestParam String procedimento) {
        boolean existe = planoDentalService.existePlanoAtivoParaPacienteDenteProcedimento(pacienteId, dente, procedimento);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Buscar planos com data prevista vencida")
    @GetMapping("/vencidos")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPlanosComDataPrevistaVencida() {
        List<PlanoDentalResponse> response = planoDentalService.buscarPlanosComDataPrevistaVencida();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar planos recentes")
    @GetMapping("/recentes")
    public ResponseEntity<List<PlanoDentalResponse>> buscarPlanosRecentes() {
        List<PlanoDentalResponse> response = planoDentalService.buscarPlanosRecentes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar planos urgentes recentes")
    @GetMapping("/urgentes/recentes")
    public ResponseEntity<List<PlanoDentalResponse>> buscarUrgentesRecentes() {
        List<PlanoDentalResponse> response = planoDentalService.buscarUrgentesRecentes();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Plano Dental API is up and running");
    }
}