// PacienteService.java
package consultorio.domain.service.pessoa;

import consultorio.api.dto.request.pessoa.PacienteRequest;
import consultorio.api.dto.response.pessoa.PacienteResponse;
import consultorio.api.dto.response.pessoa.PacienteResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PacienteService {

    // ==================== CRUD BÁSICO ====================

    PacienteResponse criar(PacienteRequest request);

    PacienteResponse buscarPorId(Long id);

    PacienteResponse atualizar(Long id, PacienteRequest request);

    void inativar(Long id);

    void ativar(Long id);

    void excluir(Long id);

    // ==================== BUSCAS ====================

    PacienteResponse buscarPorProntuario(String prontuarioNumero);

    PacienteResponse buscarPorCpf(String cpf);

    PacienteResponse buscarPorEmail(String email);

    // ==================== LISTAGENS ====================

    List<PacienteResponse> listarTodos();

    Page<PacienteResponse> listarTodos(Pageable pageable);

    List<PacienteResponse> listarAtivos();

    Page<PacienteResponse> listarAtivos(Pageable pageable);

    List<PacienteResponse> listarInativos();

    List<PacienteResumoResponse> listarResumo();

    Page<PacienteResumoResponse> listarResumo(Pageable pageable);

    // ==================== BUSCAS POR NOME ====================

    List<PacienteResponse> buscarPorNome(String nome);

    Page<PacienteResponse> buscarPorNome(String nome, Pageable pageable);

    // ==================== BUSCAS POR CONVÊNIO ====================

    List<PacienteResponse> buscarPorConvenio(String convenio);

    Page<PacienteResponse> buscarPorConvenio(String convenio, Pageable pageable);

    List<String> listarConveniosDistintos();

    // ==================== BUSCAS POR DATA ====================

    List<PacienteResponse> buscarPorDataNascimento(LocalDate dataNascimento);

    List<PacienteResponse> buscarPorDataNascimentoEntre(LocalDate inicio, LocalDate fim);

    List<PacienteResponse> buscarAniversariantesDoMes(Integer mes);

    // ==================== BUSCAS POR TELEFONE ====================

    List<PacienteResponse> buscarPorTelefone(String telefone);

    // ==================== BUSCAS COM FILTROS ====================

    Page<PacienteResponse> buscarComFiltros(String nome, String cpf, String convenio,
                                            Boolean ativo, Boolean status, Pageable pageable);

    // ==================== BUSCAS POR IDADE ====================

    List<PacienteResponse> buscarPorFaixaEtaria(Integer idadeMin, Integer idadeMax);

    List<PacienteResponse> buscarMenoresDeIdade();

    // ==================== VERIFICAÇÕES ====================

    boolean existePorProntuario(String prontuarioNumero);

    boolean existePorCpf(String cpf);

    boolean existePorCpfExcluindoId(String cpf, Long id);

    boolean existePorEmail(String email);

    // ==================== ESTATÍSTICAS ====================

    Long contarAtivos();

    Long contarInativos();

    Long contarComConvenio();

    Long contarSemConvenio();

    List<Object[]> contarPorConvenio();

    List<Object[]> contarPorFaixaEtaria();

    List<Object[]> contarPorMesCadastro();

    // ==================== BUSCAS POR CONDIÇÕES DE SAÚDE ====================

    List<PacienteResponse> buscarComDiabetes();

    List<PacienteResponse> buscarComHipertensao();

    List<PacienteResponse> buscarFumantes();

    // ==================== BUSCAS POR RESPONSÁVEL ====================

    List<PacienteResponse> buscarComResponsavel();

    List<PacienteResponse> buscarPorNomeResponsavel(String nomeResponsavel);

    // ==================== RELATÓRIOS ====================

    List<PacienteResponse> gerarRelatorioCadastro(LocalDate inicio, LocalDate fim);

    List<Object[]> gerarRelatorioNaturalidades();

    List<Object[]> gerarRelatorioNovosPacientesUltimos12Meses();
}