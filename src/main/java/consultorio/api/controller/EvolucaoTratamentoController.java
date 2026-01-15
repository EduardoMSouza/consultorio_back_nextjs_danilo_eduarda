package consultorio.api.controller;

<<<<<<< HEAD
import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.domain.service.EvolucaoTratamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
=======
import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;
import consultorio.domain.service.EvolucaoTratamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
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
<<<<<<< HEAD
@RequestMapping("/api/evolucoes-tratamento")
@RequiredArgsConstructor
@Tag(name = "Evolução de Tratamento", description = "API para gerenciamento de evoluções de tratamento odontológico")
class EvolucaoTratamentoController {

    private final EvolucaoTratamentoService evolucaoTratamentoService;

    @PostMapping
    @Operation(summary = "Criar nova evolução de tratamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evolução criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente, dentista ou plano não encontrado")
    })
    public ResponseEntity<EvolucaoTratamentoResponse> criar(
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.criar(request);
=======
@RequestMapping("/api/evolucao-tratamento")
@RequiredArgsConstructor
@Tag(name = "Evolução de Tratamento", description = "Gerenciamento de evoluções de tratamento")
@SecurityRequirement(name = "bearerAuth")
public class EvolucaoTratamentoController {

    private final EvolucaoTratamentoService service;

    @PostMapping
    @Operation(summary = "Criar evolução de tratamento")
    public ResponseEntity<EvolucaoTratamentoResponse> criar(@Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = service.criar(request);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evolução por ID")
<<<<<<< HEAD
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução encontrada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<EvolucaoTratamentoResponse> buscarPorId(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.buscarPorId(id);
=======
    public ResponseEntity<EvolucaoTratamentoResponse> buscarPorId(@PathVariable Long id) {
        EvolucaoTratamentoResponse response = service.buscarPorId(id);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(response);
    }

    @GetMapping
<<<<<<< HEAD
    @Operation(summary = "Listar todas as evoluções ativas")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarTodos() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarTodos();
=======
    @Operation(summary = "Listar todas as evoluções")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarTodos() {
        List<EvolucaoTratamentoResponse> response = service.listarTodos();
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar evoluções por paciente")
<<<<<<< HEAD
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPaciente(
            @Parameter(description = "ID do paciente") @PathVariable Long pacienteId) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarPorPaciente(pacienteId);
=======
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPaciente(pacienteId);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dentista/{dentistaId}")
    @Operation(summary = "Listar evoluções por dentista")
<<<<<<< HEAD
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorDentista(
            @Parameter(description = "ID do dentista") @PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarPorDentista(dentistaId);
=======
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorDentista(@PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorDentista(dentistaId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{pacienteId}/dentista/{dentistaId}")
    @Operation(summary = "Listar evoluções por paciente e dentista")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPacienteEDentista(
            @PathVariable Long pacienteId,
            @PathVariable Long dentistaId) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPacienteEDentista(pacienteId, dentistaId);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(response);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar evoluções por período")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarPorPeriodo(
<<<<<<< HEAD
            @Parameter(description = "Data inicial (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/urgentes")
    @Operation(summary = "Listar evoluções urgentes")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarUrgentes() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarUrgentes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/retornos-agendados")
    @Operation(summary = "Listar evoluções com retorno agendado")
    public ResponseEntity<List<EvolucaoTratamentoResponse>> listarComRetornoAgendado() {
        List<EvolucaoTratamentoResponse> response = evolucaoTratamentoService.listarComRetornoAgendado();
=======
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<EvolucaoTratamentoResponse> response = service.listarPorPeriodo(inicio, fim);
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
<<<<<<< HEAD
    @Operation(summary = "Atualizar evolução completamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução atualizada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<EvolucaoTratamentoResponse> atualizar(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar evolução parcialmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução atualizada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<EvolucaoTratamentoResponse> atualizarParcial(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = evolucaoTratamentoService.atualizarParcial(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/marcar-urgente")
    @Operation(summary = "Marcar evolução como urgente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução marcada como urgente"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Void> marcarComoUrgente(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.marcarComoUrgente(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/agendar-retorno")
    @Operation(summary = "Agendar retorno para evolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retorno agendado"),
            @ApiResponse(responseCode = "400", description = "Data de retorno inválida"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Void> agendarRetorno(
            @Parameter(description = "ID da evolução") @PathVariable Long id,
            @Parameter(description = "Data do retorno (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRetorno,
            @Parameter(description = "Motivo do retorno") @RequestParam String motivo) {
        evolucaoTratamentoService.agendarRetorno(id, dataRetorno, motivo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar evolução (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução desativada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Reativar evolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução reativada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir evolução permanentemente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evolução excluída"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        evolucaoTratamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/verificar-retorno-atrasado")
    @Operation(summary = "Verificar se retorno está atrasado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação realizada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Boolean> verificarRetornoAtrasado(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        boolean atrasado = evolucaoTratamentoService.verificarRetornoAtrasado(id);
        return ResponseEntity.ok(atrasado);
    }

    @GetMapping("/{id}/verificar-urgente")
    @Operation(summary = "Verificar se evolução é urgente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação realizada"),
            @ApiResponse(responseCode = "404", description = "Evolução não encontrada")
    })
    public ResponseEntity<Boolean> verificarSeUrgente(
            @Parameter(description = "ID da evolução") @PathVariable Long id) {
        boolean urgente = evolucaoTratamentoService.verificarSeUrgente(id);
        return ResponseEntity.ok(urgente);
    }
}
=======
    @Operation(summary = "Atualizar evolução de tratamento")
    public ResponseEntity<EvolucaoTratamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EvolucaoTratamentoRequest request) {
        EvolucaoTratamentoResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evolução de tratamento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
