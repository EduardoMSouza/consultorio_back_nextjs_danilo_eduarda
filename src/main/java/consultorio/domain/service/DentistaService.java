package consultorio.domain.service;

import consultorio.api.dto.request.pessoa.DentistaRequest;
import consultorio.api.dto.response.pessoa.DentistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistaService {

    DentistaResponse criar(DentistaRequest request);

    DentistaResponse buscarPorId(Long id);

    Page<DentistaResponse> listarTodos(Pageable pageable);

    Page<DentistaResponse> listarAtivos(Pageable pageable);

    DentistaResponse atualizar(Long id, DentistaRequest request);

    void desativar(Long id);

    void ativar(Long id);

    void deletar(Long id);

    Page<DentistaResponse> buscarPorNome(String nome, Pageable pageable);
}