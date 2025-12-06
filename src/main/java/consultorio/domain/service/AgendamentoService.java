package consultorio.domain.service;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {

    AgendamentoResponse criar(AgendamentoRequest request);

    AgendamentoResponse buscarPorId(Long id);

    Page<AgendamentoResumoResponse> listarTodos(Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorDentista(Long dentistaId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPaciente(Long pacienteId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorStatus(StatusAgendamento status, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    List<AgendamentoResumoResponse> buscarAgendaDoDia(Long dentistaId, LocalDate data);

    List<AgendamentoResumoResponse> buscarAgendaDoDia(LocalDate data);

    List<AgendamentoResumoResponse> buscarProximosAgendamentos(Long pacienteId);

    AgendamentoResponse atualizar(Long id, AgendamentoRequest request);

    AgendamentoResponse atualizarStatus(Long id, StatusAgendamento status);

    void confirmar(Long id);

    void iniciarAtendimento(Long id);

    void concluir(Long id);

    void cancelar(Long id);

    void marcarFalta(Long id);

    void deletar(Long id);

    boolean verificarDisponibilidade(Long dentistaId, LocalDate data, java.time.LocalTime horaInicio, java.time.LocalTime horaFim);
}