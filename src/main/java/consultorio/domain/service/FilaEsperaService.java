package consultorio.domain.service;

import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.Agendamento.TipoProcedimento;
import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FilaEsperaService {

    // ==================== CRUD ====================

    FilaEsperaResponse criar(FilaEsperaRequest request);

    FilaEsperaResponse buscarPorId(Long id);

    FilaEsperaResponse atualizar(Long id, FilaEsperaRequest request);

    void deletar(Long id);

    // ==================== LISTAGENS ====================

    Page<FilaEsperaResponse> listarTodas(Pageable pageable);

    Page<FilaEsperaResponse> listarAtivas(Pageable pageable);

    Page<FilaEsperaResponse> listarPorStatus(StatusFila status, Pageable pageable);

    List<FilaEsperaResponse> listarAtivasPorDentista(Long dentistaId);

    List<FilaEsperaResponse> listarPorPaciente(Long pacienteId);

    List<FilaEsperaResponse> listarAtivasPorPaciente(Long pacienteId);

    // ==================== AÇÕES ====================

    FilaEsperaResponse notificar(Long id);

    FilaEsperaResponse cancelar(Long id);

    FilaEsperaResponse converterEmAgendamento(Long filaId, Long agendamentoId);

    void incrementarTentativaContato(Long id);

    // ==================== PROCESSAMENTO AUTOMÁTICO ====================

    void processarFilaAposCancelamento(Agendamento agendamento);

    void processarFilaAposConclusao(Agendamento agendamento);

    int expirarFilasVencidas();

    int enviarNotificacoesPendentes();

    // ==================== COMPATÍVEIS ====================

    List<FilaEsperaResponse> buscarCompativeis(Long dentistaId, LocalDate data, TipoProcedimento tipoProcedimento);

    List<FilaEsperaResponse> buscarCompatíveisPorDentista(Long dentistaId);

    // ==================== ESTATÍSTICAS ====================

    Map<String, Object> obterEstatisticas();

    long contarAtivas();

    long contarAtivasPorDentista(Long dentistaId);

    long contarAtivasPorPaciente(Long pacienteId);

    int calcularPosicao(Long filaId);
}