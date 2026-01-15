package consultorio.api.controller.pessoa;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;

import consultorio.domain.service.dentista.DentistaService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Dentistas", description = "Gerenciamento de dentistas")
public class DentistaController {

    private final DentistaService dentistaService;

    @PostMapping
    @Operation(summary = "Criar novo dentista")
    public ResponseEntity<DentistaResponse> criar(@Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = dentistaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dentista por ID")
    public ResponseEntity<DentistaResponse> buscarPorId(@PathVariable Long id) {
        DentistaResponse response = dentistaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cro/{cro}")
    @Operation(summary = "Buscar dentista por CRO")
    public ResponseEntity<DentistaResponse> buscarPorCro(@PathVariable String cro) {
        DentistaResponse response = dentistaService.buscarPorCro(cro);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dentista")
    public ResponseEntity<DentistaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = dentistaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar dentista")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        dentistaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos dentistas")
    public ResponseEntity<Page<DentistaResponse>> listarTodos(Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar dentistas ativos")
    public ResponseEntity<Page<DentistaResponse>> listarAtivos(Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.listarAtivos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/nome")
    @Operation(summary = "Buscar dentistas por nome")
    public ResponseEntity<Page<DentistaResponse>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar dentistas por termo")
    public ResponseEntity<Page<DentistaResponse>> buscarPorTermo(
            @RequestParam String termo,
            Pageable pageable) {
        Page<DentistaResponse> response = dentistaService.buscarPorTermo(termo, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar dentista")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        dentistaService.ativar(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar dentista")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        dentistaService.desativar(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verificar/email")
    @Operation(summary = "Verificar se email existe")
    public ResponseEntity<Boolean> verificarEmail(@RequestParam String email) {
        boolean existe = dentistaService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/verificar/cro")
    @Operation(summary = "Verificar se CRO existe")
    public ResponseEntity<Boolean> verificarCro(@RequestParam String cro) {
        boolean existe = dentistaService.existePorCro(cro);
        return ResponseEntity.ok(existe);
    }
}