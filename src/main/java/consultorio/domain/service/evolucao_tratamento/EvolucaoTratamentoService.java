package consultorio.domain.service.evolucao_tratamento;

import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;

import java.time.LocalDate;
import java.util.List;

public interface EvolucaoTratamentoService {

    // CRUD básico
    EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request);
    EvolucaoTratamentoResponse buscarPorId(Long id);
    List<EvolucaoTratamentoResponse> listarTodos();
    EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request);
    EvolucaoTratamentoResponse atualizarParcial(Long id, EvolucaoTratamentoRequest request);
    void desativar(Long id);
    void ativar(Long id);
    void excluir(Long id);

    // Buscas específicas
    List<EvolucaoTratamentoResponse> buscarPorPaciente(Long pacienteId);
    List<EvolucaoTratamentoResponse> buscarPorDentista(Long dentistaId);
    List<EvolucaoTratamentoResponse> buscarPorPlanoDental(Long planoDentalId);
    List<EvolucaoTratamentoResponse> buscarPorData(LocalDate data);
    List<EvolucaoTratamentoResponse> buscarPorPeriodo(LocalDate inicio, LocalDate fim);
    List<EvolucaoTratamentoResponse> buscarUrgentes();
    List<EvolucaoTratamentoResponse> buscarComRetornoNecessario();
    List<EvolucaoTratamentoResponse> buscarRetornosAtrasados();

    // Métodos específicos da entidade
    EvolucaoTratamentoResponse marcarComoUrgente(Long id);
    EvolucaoTratamentoResponse agendarRetorno(Long id, LocalDate dataRetorno, String motivo);
    EvolucaoTratamentoResponse adicionarProcedimento(Long id, String procedimento);
    EvolucaoTratamentoResponse adicionarMedicamento(Long id, String medicamento);
    EvolucaoTratamentoResponse adicionarMaterial(Long id, String material);

    // Buscas com filtros combinados
    List<EvolucaoTratamentoResponse> buscarComFiltros(
            Long pacienteId,
            Long dentistaId,
            Long planoDentalId,
            LocalDate dataInicio,
            LocalDate dataFim,
            String tipoEvolucao,
            Boolean urgente
    );

    // Métodos auxiliares
    Long contarPorPaciente(Long pacienteId);
    EvolucaoTratamentoResponse buscarUltimaEvolucaoPorPaciente(Long pacienteId);
    boolean existePorId(Long id);
}