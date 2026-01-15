package consultorio.domain.service;

import consultorio.api.dto.request.tratamento.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.tratamento.EvolucaoTratamentoResponse;

import java.time.LocalDate;
import java.util.List;

public interface EvolucaoTratamentoService {

    EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request);

    EvolucaoTratamentoResponse buscarPorId(Long id);

    List<EvolucaoTratamentoResponse> listarTodos();

    List<EvolucaoTratamentoResponse> listarPorPaciente(Long pacienteId);

    List<EvolucaoTratamentoResponse> listarPorDentista(Long dentistaId);

    List<EvolucaoTratamentoResponse> listarPorPlanoDental(Long planoDentalId);

    List<EvolucaoTratamentoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

    List<EvolucaoTratamentoResponse> listarUrgentes();

    List<EvolucaoTratamentoResponse> listarComRetornoAgendado();

    EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request);

    EvolucaoTratamentoResponse atualizarParcial(Long id, EvolucaoTratamentoRequest request);

    void desativar(Long id);

    void ativar(Long id);

    void excluir(Long id);

    void marcarComoUrgente(Long id);

    void agendarRetorno(Long id, LocalDate dataRetorno, String motivo);

    void adicionarProcedimento(Long id, String procedimento);

    void adicionarMedicamento(Long id, String medicamento);

    void adicionarMaterial(Long id, String material);

    boolean verificarRetornoAtrasado(Long id);

    boolean verificarSeUrgente(Long id);

    boolean verificarSeAtiva(Long id);
}