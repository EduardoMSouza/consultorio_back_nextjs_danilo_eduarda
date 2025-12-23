package consultorio.domain.service;

import consultorio.api.dto.request.FilaEsperaRequest;
import consultorio.api.dto.response.FilaEsperaResponse;
import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.FilaEspera.StatusFila;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface FilaEsperaService {

    // ==================== CRUD BÁSICO ====================

    FilaEsperaResponse criar(FilaEsperaRequest request);

    FilaEsperaResponse buscarPorId(Long id);

    List<FilaEsperaResponse> listarTodas();

    Page<FilaEsperaResponse> listarTodas(Pageable pageable);

    FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS POR FILTROS ====================

    List<FilaEsperaResponse> listarAtivas();

    List<FilaEsperaResponse> listarPorDentista(Long dentistaId);

    List<FilaEsperaResponse> listarPorPaciente(Long pacienteId);

    List<FilaEsperaResponse> listarPorPacienteAtivas(Long pacienteId);

    Page<FilaEsperaResponse> listarPorStatus(StatusFila status, Pageable pageable);

    // ==================== GERENCIAMENTO DE FILA ====================

    void notificar(Long id);

    void converterEmAgendamento(Long filaEsperaId, Long agendamentoId);

    void cancelar(Long id);

    void processarFilaAutomaticamente();

    void processarFilaAposCriacao(Agendamento agendamento);

    void processarFilaAposCancelamento(Agendamento agendamento);

    void processarFilaAposConclusao(Agendamento agendamento);

    void expirarFilasAnteriores();

    // ==================== BUSCAR COMPATÍVEIS ====================

    List<FilaEsperaResponse> buscarCompativeisComAgendamento(Long dentistaId,
                                                             LocalDate data,
                                                             Agendamento.TipoProcedimento tipoProcedimento);

    List<FilaEsperaResponse> buscarCompativeisComDentista(Long dentistaId);

    // ==================== ESTATÍSTICAS ====================

    Long contarAtivasPorDentista(Long dentistaId);

    Long contarTotalAtivas();

    Long contarAtivasPorPaciente(Long pacienteId);

    // ==================== NOTIFICAÇÕES ====================

    void enviarNotificacoesPendentes();

    void incrementarTentativaContato(Long id);
}