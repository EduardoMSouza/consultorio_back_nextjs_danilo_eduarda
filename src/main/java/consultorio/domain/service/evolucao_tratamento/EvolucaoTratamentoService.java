package consultorio.domain.service.evolucao_tratamento;

import consultorio.api.dto.request.tratamento.AtualizarEvolucaoTratamentoRequest;
import consultorio.api.dto.request.tratamento.CriarEvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoDetalheResponse;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.api.dto.response.tratamento.PaginacaoResponse;
import consultorio.api.dto.response.tratamento.ResumoEvolucaoResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EvolucaoTratamentoService {

    // CRUD Básico
    List<EvolucaoTratamentoResponse> listarTodos();
    PaginacaoResponse<EvolucaoTratamentoResponse> listarPaginado(Pageable pageable);
    EvolucaoTratamentoDetalheResponse buscarPorId(Long id);
    EvolucaoTratamentoResponse criar(CriarEvolucaoTratamentoRequest request);
    EvolucaoTratamentoResponse atualizar(Long id, AtualizarEvolucaoTratamentoRequest request);
    void excluir(Long id);

    // Consultas específicas
    List<EvolucaoTratamentoResponse> buscarPorPaciente(Long pacienteId);
    PaginacaoResponse<EvolucaoTratamentoResponse> buscarPorPacientePaginado(Long pacienteId, Pageable pageable);
    List<EvolucaoTratamentoResponse> buscarPorDentista(Long dentistaId);
    List<EvolucaoTratamentoResponse> buscarPorData(LocalDate data);
    List<EvolucaoTratamentoResponse> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);
    List<EvolucaoTratamentoResponse> buscarPorPacienteEPeriodo(Long pacienteId, LocalDate dataInicio, LocalDate dataFim);
    EvolucaoTratamentoDetalheResponse buscarUltimaEvolucaoPaciente(Long pacienteId);
    List<ResumoEvolucaoResponse> buscarEvolucoesDoDia();
    List<ResumoEvolucaoResponse> buscarPorTextoEvolucao(String texto);

    // Estatísticas
    Long contarEvolucoesPorPaciente(Long pacienteId);
    Long contarTotalEvolucoes();
    boolean existeEvolucaoNaData(Long pacienteId, LocalDate data);
}