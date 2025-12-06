package consultorio.api.controller;

import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import consultorio.domain.service.DentistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dentistas")
@RequiredArgsConstructor
public class DentistaController {

    private final DentistaService service;

    @PostMapping
    public ResponseEntity<DentistaResponse> criar(@Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistaResponse> buscarPorId(@PathVariable Long id) {
        DentistaResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cro/{cro}")
    public ResponseEntity<DentistaResponse> buscarPorCro(@PathVariable String cro) {
        DentistaResponse response = service.buscarPorCro(cro);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DentistaResponse>> listarTodos(
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DentistaResponse> response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{ativo}")
    public ResponseEntity<Page<DentistaResponse>> listarPorStatus(
            @PathVariable Boolean ativo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DentistaResponse> response = service.listarPorStatus(ativo, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<Page<DentistaResponse>> listarPorEspecialidade(
            @PathVariable String especialidade,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DentistaResponse> response = service.listarPorEspecialidade(especialidade, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<DentistaResponse>> buscar(
            @RequestParam String termo,
            @PageableDefault(size = 20, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DentistaResponse> response = service.buscar(termo, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DentistaRequest request) {
        DentistaResponse response = service.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        service.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}