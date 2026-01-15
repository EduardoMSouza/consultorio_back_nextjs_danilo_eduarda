package consultorio.domain.service;

<<<<<<< HEAD
import consultorio.api.dto.request.agendamento.AgendamentoRequest;
import consultorio.domain.entity.agendamento.Agendamento.StatusAgendamento;
=======
import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface AgendamentoService {

    // ==================== CRUD ====================

    AgendamentoResponse criar(AgendamentoRequest request);

    AgendamentoResponse buscarPorId(Long id);

    AgendamentoResponse atualizar(Long id, AgendamentoRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS ====================

    Page<AgendamentoResumoResponse> listarTodos(Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorDentista(Long dentistaId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPaciente(Long pacienteId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorStatus(StatusAgendamento status, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim, Pageable pageable);

    // ==================== AGENDA ====================

    List<AgendamentoResumoResponse> buscarAgendaDoDia(Long dentistaId, LocalDate data);

    List<AgendamentoResumoResponse> buscarAgendaDoDia(LocalDate data);

    List<AgendamentoResumoResponse> buscarProximosPaciente(Long pacienteId);

    List<AgendamentoResumoResponse> buscarProximosDentista(Long dentistaId);

    // ==================== MUDANÇA DE STATUS ====================

    AgendamentoResponse confirmar(Long id);

    AgendamentoResponse iniciarAtendimento(Long id);

    AgendamentoResponse concluir(Long id);

    AgendamentoResponse cancelar(Long id, String motivo);

    AgendamentoResponse marcarFalta(Long id);

    // ==================== DISPONIBILIDADE ====================

    boolean verificarDisponibilidade(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim);

    List<Map<String, LocalTime>> buscarHorariosDisponiveis(Long dentistaId, LocalDate data, int duracaoMinutos);

    // ==================== LEMBRETES ====================

    int enviarLembretes(LocalDate data);

    // ==================== ESTATÍSTICAS ====================

    Map<String, Object> obterEstatisticas(LocalDate inicio, LocalDate fim);

    long contarConsultasDoDia(Long dentistaId, LocalDate data);

    long contarFaltasPaciente(Long pacienteId);

    // ==================== CONSULTAS ESPECIAIS ====================

    List<AgendamentoResumoResponse> buscarPassadosNaoFinalizados();

    List<AgendamentoResumoResponse> buscarEmAtendimento();

    Page<AgendamentoResumoResponse> buscarHistoricoPaciente(Long pacienteId, Pageable pageable);
}