package consultorio.domain.service;

import consultorio.api.dto.request.DentistaRequest;
import consultorio.api.dto.response.DentistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistaService {

    DentistaResponse criar(DentistaRequest request);

    DentistaResponse buscarPorId(Long id);

    DentistaResponse buscarPorCro(String cro);

    Page<DentistaResponse> listarTodos(Pageable pageable);

    Page<DentistaResponse> listarPorStatus(Boolean ativo, Pageable pageable);

    Page<DentistaResponse> listarPorEspecialidade(String especialidade, Pageable pageable);

    Page<DentistaResponse> buscar(String termo, Pageable pageable);

    DentistaResponse atualizar(Long id, DentistaRequest request);

    void inativar(Long id);

    void ativar(Long id);

    void deletar(Long id);
}