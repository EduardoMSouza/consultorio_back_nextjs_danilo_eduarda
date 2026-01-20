package consultorio.api.controller.pessoa;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import consultorio.api.dto.response.pessoa.DentistaResumoResponse;
import consultorio.domain.service.dentista.DentistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dentistas")
@RequiredArgsConstructor
@Tag(name = "Dentistas", description = "API para gerenciamento de dentistas do consultório")
public class DentistaController {

    private final DentistaService dentistaService;

    @PostMapping
    @Operation(summary = "Criar novo dentista",
            description = "Cria um novo dentista no sistema. Valida unicidade de email e CRO.")
    public ResponseEntity<DentistaResponse> criar(@Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = dentistaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dentista por ID",
            description = "Busca um dentista específico pelo seu ID")
    public ResponseEntity<DentistaResponse> buscarPorId(
            @Parameter(description = "ID do dentista") @PathVariable Long id) {
        DentistaResponse response = dentistaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cro/{cro}")
    @Operation(summary = "Buscar dentista por CRO",
            description = "Busca um dentista pelo seu número de CRO (Cadastro Regional de Odontologia)")
    public ResponseEntity<DentistaResponse> buscarPorCro(
            @Parameter(description = "Número do CRO") @PathVariable String cro) {
        DentistaResponse response = dentistaService.buscarPorCro(cro);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dentista",
            description = "Atualiza os dados de um dentista existente")
    public ResponseEntity<DentistaResponse> atualizar(
            @Parameter(description = "ID do dentista") @PathVariable Long id,
            @Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = dentistaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar dentista",
            description = "Remove um dentista do sistema. Não permite exclusão se houver agendamentos vinculados.")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do dentista") @PathVariable Long id) {
        dentistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos dentistas",
            description = "Lista todos os dentistas cadastrados com paginação")
    public ResponseEntity<Page<DentistaResponse>> listarTodos(
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resumo")
    @Operation(summary = "Listar todos dentistas (resumo)",
            description = "Lista todos os dentistas com informações resumidas")
    public ResponseEntity<Page<DentistaResumoResponse>> listarTodosResumo(
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResumoResponse> response = dentistaService.listarTodosResumo(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar dentistas ativos",
            description = "Lista apenas os dentistas que estão ativos no sistema")
    public ResponseEntity<Page<DentistaResponse>> listarAtivos(
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.listarAtivos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/nome")
    @Operation(summary = "Buscar dentistas por nome",
            description = "Busca dentistas pelo nome (busca parcial, case insensitive)")
    public ResponseEntity<Page<DentistaResponse>> buscarPorNome(
            @Parameter(description = "Termo de busca pelo nome") @RequestParam String nome,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/especialidade")
    @Operation(summary = "Buscar dentistas por especialidade",
            description = "Busca dentistas pela especialidade (busca parcial, case insensitive)")
    public ResponseEntity<Page<DentistaResponse>> buscarPorEspecialidade(
            @Parameter(description = "Termo de busca pela especialidade") @RequestParam String especialidade,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.buscarPorEspecialidade(especialidade, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar dentistas por termo",
            description = "Busca dentistas por qualquer termo (nome, email, CRO ou especialidade)")
    public ResponseEntity<Page<DentistaResponse>> buscarPorTermo(
            @Parameter(description = "Termo de busca geral") @RequestParam String termo,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.buscarPorTermo(termo, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar dentista",
            description = "Ativa um dentista que estava desativado")
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do dentista") @PathVariable Long id) {
        dentistaService.ativar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar dentista",
            description = "Desativa um dentista (não permite se houver agendamentos vinculados)")
    public ResponseEntity<Void> desativar(
            @Parameter(description = "ID do dentista") @PathVariable Long id) {
        dentistaService.desativar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verificar/email")
    @Operation(summary = "Verificar se email existe",
            description = "Verifica se já existe um dentista cadastrado com o email informado")
    public ResponseEntity<Boolean> verificarEmail(
            @Parameter(description = "Email a ser verificado") @RequestParam String email) {
        boolean existe = dentistaService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar/cro")
    @Operation(summary = "Verificar se CRO existe",
            description = "Verifica se já existe um dentista cadastrado com o CRO informado")
    public ResponseEntity<Boolean> verificarCro(
            @Parameter(description = "CRO a ser verificado") @RequestParam String cro) {
        boolean existe = dentistaService.existePorCro(cro);
        return ResponseEntity.ok(existe);
    }
}