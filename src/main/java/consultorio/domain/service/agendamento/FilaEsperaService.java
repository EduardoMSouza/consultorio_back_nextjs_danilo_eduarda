package consultorio.domain.service.agendamento;

import consultorio.api.dto.request.agendamento.fila_espera.FilaEsperaRequest;
import consultorio.api.dto.response.agendamento.fila_espera.FilaEsperaResponse;
import consultorio.domain.entity.agendamento.FilaEspera;

import java.time.LocalDate;
import java.util.List;

public interface FilaEsperaService {

    FilaEsperaResponse criar(FilaEsperaRequest request);

    FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request);

    FilaEsperaResponse buscarPorId(Long id);

    List<FilaEsperaResponse> listarTodas();

    List<FilaEsperaResponse> listarPorStatus(FilaEspera.StatusFila status);

    List<FilaEsperaResponse> listarPorPaciente(Long pacienteId);

    List<FilaEsperaResponse> listarPorDentista(Long dentistaId);

    List<FilaEsperaResponse> listarAtivas();

    FilaEsperaResponse notificar(Long id);

    FilaEsperaResponse converterEmAgendamento(Long id, Long agendamentoId);

    FilaEsperaResponse cancelar(Long id);

    void expirarFilasAntigas(LocalDate dataLimite);

    void deletar(Long id);
}