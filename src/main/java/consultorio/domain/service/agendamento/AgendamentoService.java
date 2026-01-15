package consultorio.domain.service.agendamento;

import consultorio.api.dto.request.agendamento.AgendamentoRequest;
import consultorio.api.dto.response.agendamento.AgendamentoResponse;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoService {

    AgendamentoResponse criar(AgendamentoRequest request);

    AgendamentoResponse atualizar(Long id, AgendamentoRequest request);

    AgendamentoResponse buscarPorId(Long id);

    List<AgendamentoResponse> listarTodos();

    List<AgendamentoResponse> listarPorDentista(Long dentistaId);

    List<AgendamentoResponse> listarPorPaciente(Long pacienteId);

    List<AgendamentoResponse> listarPorData(LocalDate data);

    List<AgendamentoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

    List<AgendamentoResponse> listarPorStatus(StatusAgendamento status);

    List<AgendamentoResponse> listarPorDentistaEData(Long dentistaId, LocalDate data);

    AgendamentoResponse confirmar(Long id, String usuario);

    AgendamentoResponse iniciarAtendimento(Long id, String usuario);

    AgendamentoResponse concluir(Long id, String usuario);

    AgendamentoResponse cancelar(Long id, String motivo, String usuario);

    AgendamentoResponse marcarFalta(Long id, String usuario);

    void deletar(Long id);

    boolean verificarDisponibilidade(Long dentistaId, LocalDate data, String horaInicio, String horaFim);

    boolean verificarDisponibilidadeParaAtualizacao(Long agendamentoId, Long dentistaId, LocalDate data, String horaInicio, String horaFim);

    List<AgendamentoResponse> buscarAgendamentosParaLembrete(LocalDate data);

    void marcarLembreteEnviado(Long id);
}