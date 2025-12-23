package consultorio.domain.service;

import consultorio.api.dto.request.AgendamentoRequest;
import consultorio.api.dto.response.AgendamentoResponse;
import consultorio.api.dto.response.AgendamentoResumoResponse;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AgendamentoService {

    // ==================== CRUD BÁSICO ====================

    AgendamentoResponse criar(AgendamentoRequest request);

    AgendamentoResponse buscarPorId(Long id);

    Page<AgendamentoResumoResponse> listarTodos(Pageable pageable);

    AgendamentoResponse atualizar(Long id, AgendamentoRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS POR FILTROS ====================

    Page<AgendamentoResumoResponse> listarPorDentista(Long dentistaId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPaciente(Long pacienteId, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorStatus(StatusAgendamento status, Pageable pageable);

    Page<AgendamentoResumoResponse> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    // ==================== AGENDA ====================

    List<AgendamentoResumoResponse> buscarAgendaDoDia(Long dentistaId, LocalDate data);

    List<AgendamentoResumoResponse> buscarAgendaDoDia(LocalDate data);

    List<AgendamentoResumoResponse> buscarProximosAgendamentos(Long pacienteId);

    List<AgendamentoResumoResponse> buscarProximosAgendamentosDentista(Long dentistaId);

    // ==================== MUDANÇAS DE STATUS ====================

    AgendamentoResponse atualizarStatus(Long id, StatusAgendamento status);

    void confirmar(Long id);

    void iniciarAtendimento(Long id);

    void concluir(Long id);

    void cancelar(Long id, String motivo);

    void marcarFalta(Long id);

    // ==================== VALIDAÇÕES E DISPONIBILIDADE ====================

    boolean verificarDisponibilidade(Long dentistaId, LocalDate data, LocalTime horaInicio, LocalTime horaFim);

    List<LocalTime[]> buscarHorariosDisponiveis(Long dentistaId, LocalDate data, int duracaoMinutos);

    // ==================== LEMBRETES ====================

    void enviarLembretes(LocalDate data);

    void marcarLembreteEnviado(Long id);

    // ==================== ESTATÍSTICAS ====================

    Long contarConsultasDoDia(Long dentistaId, LocalDate data);

    Long contarFaltasPaciente(Long pacienteId);

    // ==================== CONSULTAS ESPECIAIS ====================

    List<AgendamentoResumoResponse> buscarConsultasPassadasNaoFinalizadas();

    List<AgendamentoResumoResponse> buscarConsultasEmAtendimento();

    Page<AgendamentoResumoResponse> buscarHistoricoConsultasPaciente(Long pacienteId, Pageable pageable);
}