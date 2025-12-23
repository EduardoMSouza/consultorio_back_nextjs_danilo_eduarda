package consultorio.api.mapper.service;

import consultorio.api.dto.request.PacienteRequest;
import consultorio.api.dto.response.PacienteResponse;
import consultorio.api.dto.response.PacienteResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PacienteService {

    PacienteResponse criar(PacienteRequest request);

    PacienteResponse buscarPorId(Long id);

    PacienteResponse buscarPorProntuario(String prontuarioNumero);

    Page<PacienteResumoResponse> listarTodos(Pageable pageable);

    Page<PacienteResumoResponse> listarPorStatus(Boolean status, Pageable pageable);

    Page<PacienteResumoResponse> buscar(String termo, Pageable pageable);

    PacienteResponse atualizar(Long id, PacienteRequest request);

    void inativar(Long id);

    void ativar(Long id);

    void deletar(Long id);
}