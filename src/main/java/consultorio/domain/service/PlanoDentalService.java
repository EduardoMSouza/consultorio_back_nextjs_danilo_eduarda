package consultorio.domain.service;

import consultorio.api.dto.request.PlanoDentalRequest;
import consultorio.api.dto.response.PlanoDentalResponse;

import java.util.List;

public interface PlanoDentalService {

    PlanoDentalResponse criar(PlanoDentalRequest request);

    PlanoDentalResponse buscarPorId(Long id);

    List<PlanoDentalResponse> listarTodos();

    List<PlanoDentalResponse> listarPorPaciente(Long pacienteId);

    List<PlanoDentalResponse> listarPorDentista(Long dentistaId);

    List<PlanoDentalResponse> listarPorPacienteEDentista(Long pacienteId, Long dentistaId);

    List<PlanoDentalResponse> listarPorStatus(String status);

    PlanoDentalResponse atualizar(Long id, PlanoDentalRequest request);

    void deletar(Long id);
}
