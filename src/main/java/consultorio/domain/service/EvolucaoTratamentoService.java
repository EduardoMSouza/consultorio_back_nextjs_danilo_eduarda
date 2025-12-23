package consultorio.domain.service;

import consultorio.api.dto.request.EvolucaoTratamentoRequest;
import consultorio.api.dto.response.EvolucaoTratamentoResponse;

import java.time.LocalDate;
import java.util.List;

public interface EvolucaoTratamentoService {

    EvolucaoTratamentoResponse criar(EvolucaoTratamentoRequest request);

    EvolucaoTratamentoResponse buscarPorId(Long id);

    List<EvolucaoTratamentoResponse> listarTodos();

    List<EvolucaoTratamentoResponse> listarPorPaciente(Long pacienteId);

    List<EvolucaoTratamentoResponse> listarPorDentista(Long dentistaId);

    List<EvolucaoTratamentoResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId);

    List<EvolucaoTratamentoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim);

    EvolucaoTratamentoResponse atualizar(Long id, EvolucaoTratamentoRequest request);

    void deletar(Long id);
}
