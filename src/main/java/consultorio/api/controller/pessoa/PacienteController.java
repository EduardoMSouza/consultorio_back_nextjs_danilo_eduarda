package consultorio.api.controller.pessoa;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import consultorio.domain.service.pessoa.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    @Operation(summary = "Criar paciente")
    public ResponseEntity<PacienteResponse> criar(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar paciente")
    public ResponseEntity<PacienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(pacienteService.atualizar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    @Operation(summary = "Inativar paciente")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        pacienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar paciente")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        pacienteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir paciente")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/prontuario/{prontuario}")
    @Operation(summary = "Buscar por prontuário")
    public ResponseEntity<PacienteResponse> buscarPorProntuario(@PathVariable String prontuario) {
        return ResponseEntity.ok(pacienteService.buscarPorProntuario(prontuario));
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar por CPF")
    public ResponseEntity<PacienteResponse> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.buscarPorCpf(cpf));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar por email")
    public ResponseEntity<PacienteResponse> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(pacienteService.buscarPorEmail(email));
    }

    @GetMapping
    @Operation(summary = "Listar todos")
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar paginado")
    public ResponseEntity<Page<PacienteResponse>> listarPaginado(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listarTodos(pageable));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar ativos")
    public ResponseEntity<List<PacienteResponse>> listarAtivos() {
        return ResponseEntity.ok(pacienteService.listarAtivos());
    }

    @GetMapping("/ativos/paginado")
    @Operation(summary = "Listar ativos paginado")
    public ResponseEntity<Page<PacienteResponse>> listarAtivosPaginado(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listarAtivos(pageable));
    }

    @GetMapping("/inativos")
    @Operation(summary = "Listar inativos")
    public ResponseEntity<List<PacienteResponse>> listarInativos() {
        return ResponseEntity.ok(pacienteService.listarInativos());
    }

    @GetMapping("/resumo")
    @Operation(summary = "Listar resumo")
    public ResponseEntity<List<PacienteResumoResponse>> listarResumo() {
        return ResponseEntity.ok(pacienteService.listarResumo());
    }

    @GetMapping("/resumo/paginado")
    @Operation(summary = "Listar resumo paginado")
    public ResponseEntity<Page<PacienteResumoResponse>> listarResumoPaginado(Pageable pageable) {
        return ResponseEntity.ok(pacienteService.listarResumo(pageable));
    }

    @GetMapping("/buscar/nome")
    @Operation(summary = "Buscar por nome")
    public ResponseEntity<List<PacienteResponse>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(pacienteService.buscarPorNome(nome));
    }

    @GetMapping("/buscar/nome/paginado")
    @Operation(summary = "Buscar por nome paginado")
    public ResponseEntity<Page<PacienteResponse>> buscarPorNomePaginado(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(pacienteService.buscarPorNome(nome, pageable));
    }

    @GetMapping("/buscar/convenio/{convenio}")
    @Operation(summary = "Buscar por convênio")
    public ResponseEntity<List<PacienteResponse>> buscarPorConvenio(@PathVariable String convenio) {
        return ResponseEntity.ok(pacienteService.buscarPorConvenio(convenio));
    }

    @GetMapping("/buscar/convenio/{convenio}/paginado")
    @Operation(summary = "Buscar por convênio paginado")
    public ResponseEntity<Page<PacienteResponse>> buscarPorConvenioPaginado(@PathVariable String convenio, Pageable pageable) {
        return ResponseEntity.ok(pacienteService.buscarPorConvenio(convenio, pageable));
    }

    @GetMapping("/convenios")
    @Operation(summary = "Listar convênios distintos")
    public ResponseEntity<List<String>> listarConvenios() {
        return ResponseEntity.ok(pacienteService.listarConveniosDistintos());
    }

    @GetMapping("/buscar/data-nascimento")
    @Operation(summary = "Buscar por data de nascimento")
    public ResponseEntity<List<PacienteResponse>> buscarPorDataNascimento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(pacienteService.buscarPorDataNascimento(data));
    }

    @GetMapping("/buscar/data-nascimento/entre")
    @Operation(summary = "Buscar por intervalo de data de nascimento")
    public ResponseEntity<List<PacienteResponse>> buscarPorDataNascimentoEntre(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(pacienteService.buscarPorDataNascimentoEntre(inicio, fim));
    }

    @GetMapping("/aniversariantes/mes/{mes}")
    @Operation(summary = "Buscar aniversariantes do mês")
    public ResponseEntity<List<PacienteResponse>> buscarAniversariantesMes(@PathVariable Integer mes) {
        return ResponseEntity.ok(pacienteService.buscarAniversariantesDoMes(mes));
    }

    @GetMapping("/buscar/telefone")
    @Operation(summary = "Buscar por telefone")
    public ResponseEntity<List<PacienteResponse>> buscarPorTelefone(@RequestParam String telefone) {
        return ResponseEntity.ok(pacienteService.buscarPorTelefone(telefone));
    }

    @GetMapping("/filtros")
    @Operation(summary = "Buscar com filtros")
    public ResponseEntity<Page<PacienteResponse>> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String convenio,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Boolean status,
            Pageable pageable) {
        return ResponseEntity.ok(pacienteService.buscarComFiltros(nome, cpf, convenio, ativo, status, pageable));
    }

    @GetMapping("/faixa-etaria")
    @Operation(summary = "Buscar por faixa etária")
    public ResponseEntity<List<PacienteResponse>> buscarPorFaixaEtaria(
            @RequestParam Integer idadeMin,
            @RequestParam Integer idadeMax) {
        return ResponseEntity.ok(pacienteService.buscarPorFaixaEtaria(idadeMin, idadeMax));
    }

    @GetMapping("/menores")
    @Operation(summary = "Buscar menores de idade")
    public ResponseEntity<List<PacienteResponse>> buscarMenores() {
        return ResponseEntity.ok(pacienteService.buscarMenoresDeIdade());
    }

    @GetMapping("/verificar/prontuario/{prontuario}")
    @Operation(summary = "Verificar prontuário")
    public ResponseEntity<Boolean> verificarProntuario(@PathVariable String prontuario) {
        return ResponseEntity.ok(pacienteService.existePorProntuario(prontuario));
    }

    @GetMapping("/verificar/cpf/{cpf}")
    @Operation(summary = "Verificar CPF")
    public ResponseEntity<Boolean> verificarCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(pacienteService.existePorCpf(cpf));
    }

    @GetMapping("/verificar/email/{email}")
    @Operation(summary = "Verificar email")
    public ResponseEntity<Boolean> verificarEmail(@PathVariable String email) {
        return ResponseEntity.ok(pacienteService.existePorEmail(email));
    }

    @GetMapping("/estatisticas/contagem")
    @Operation(summary = "Estatísticas de contagem")
    public ResponseEntity<Object> getEstatisticasContagem() {
        return ResponseEntity.ok(new Object() {
            public final Long ativos = pacienteService.contarAtivos();
            public final Long inativos = pacienteService.contarInativos();
            public final Long comConvenio = pacienteService.contarComConvenio();
            public final Long semConvenio = pacienteService.contarSemConvenio();
            public final Long total = ativos + inativos;
        });
    }

    @GetMapping("/estatisticas/convenio")
    @Operation(summary = "Estatísticas por convênio")
    public ResponseEntity<List<Object[]>> getEstatisticasConvenio() {
        return ResponseEntity.ok(pacienteService.contarPorConvenio());
    }

    @GetMapping("/estatisticas/faixa-etaria")
    @Operation(summary = "Estatísticas por faixa etária")
    public ResponseEntity<List<Object[]>> getEstatisticasFaixaEtaria() {
        return ResponseEntity.ok(pacienteService.contarPorFaixaEtaria());
    }

    @GetMapping("/estatisticas/mes-cadastro")
    @Operation(summary = "Estatísticas por mês de cadastro")
    public ResponseEntity<List<Object[]>> getEstatisticasMesCadastro() {
        return ResponseEntity.ok(pacienteService.contarPorMesCadastro());
    }

    @GetMapping("/saude/diabetes")
    @Operation(summary = "Pacientes com diabetes")
    public ResponseEntity<List<PacienteResponse>> getComDiabetes() {
        return ResponseEntity.ok(pacienteService.buscarComDiabetes());
    }

    @GetMapping("/saude/hipertensao")
    @Operation(summary = "Pacientes com hipertensão")
    public ResponseEntity<List<PacienteResponse>> getComHipertensao() {
        return ResponseEntity.ok(pacienteService.buscarComHipertensao());
    }

    @GetMapping("/saude/fumantes")
    @Operation(summary = "Pacientes fumantes")
    public ResponseEntity<List<PacienteResponse>> getFumantes() {
        return ResponseEntity.ok(pacienteService.buscarFumantes());
    }

    @GetMapping("/responsaveis")
    @Operation(summary = "Pacientes com responsável")
    public ResponseEntity<List<PacienteResponse>> getComResponsavel() {
        return ResponseEntity.ok(pacienteService.buscarComResponsavel());
    }

    @GetMapping("/buscar/responsavel")
    @Operation(summary = "Buscar por nome do responsável")
    public ResponseEntity<List<PacienteResponse>> buscarPorResponsavel(@RequestParam String nome) {
        return ResponseEntity.ok(pacienteService.buscarPorNomeResponsavel(nome));
    }

    @GetMapping("/relatorios/cadastro")
    @Operation(summary = "Relatório de cadastro")
    public ResponseEntity<List<PacienteResponse>> gerarRelatorioCadastro(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(pacienteService.gerarRelatorioCadastro(inicio, fim));
    }

    @GetMapping("/relatorios/naturalidades")
    @Operation(summary = "Relatório de naturalidades")
    public ResponseEntity<List<Object[]>> gerarRelatorioNaturalidades() {
        return ResponseEntity.ok(pacienteService.gerarRelatorioNaturalidades());
    }

    @GetMapping("/relatorios/novos-12-meses")
    @Operation(summary = "Relatório novos últimos 12 meses")
    public ResponseEntity<List<Object[]>> gerarRelatorioNovos12Meses() {
        return ResponseEntity.ok(pacienteService.gerarRelatorioNovosPacientesUltimos12Meses());
    }
}