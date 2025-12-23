package consultorio.domain.repository;

import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // ==================== BUSCAR POR FILTROS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.ativo = true")
    Page<Agendamento> findAllAtivos(Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.id = :id AND a.ativo = true")
    Optional<Agendamento> findByIdAtivo(@Param("id") Long id);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.ativo = true")
    Page<Agendamento> findByDentistaId(@Param("dentistaId") Long dentistaId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.ativo = true")
    Page<Agendamento> findByPacienteId(@Param("pacienteId") Long pacienteId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.status = :status AND a.ativo = true")
    Page<Agendamento> findByStatus(@Param("status") StatusAgendamento status, Pageable pageable);

    // ==================== AGENDA DO DIA ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data AND a.ativo = true " +
            "ORDER BY a.horaInicio")
    List<Agendamento> findAgendaDoDia(@Param("dentistaId") Long dentistaId,
                                      @Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data AND a.ativo = true " +
            "ORDER BY a.horaInicio")
    List<Agendamento> findAllAgendaDoDia(@Param("data") LocalDate data);

    // ==================== BUSCAR POR PERÍODO ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    Page<Agendamento> findByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                    @Param("dataFim") LocalDate dataFim,
                                    Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim AND a.ativo = true " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findByDentistaAndPeriodo(@Param("dentistaId") Long dentistaId,
                                               @Param("dataInicio") LocalDate dataInicio,
                                               @Param("dataFim") LocalDate dataFim);

    // ==================== VERIFICAÇÃO DE CONFLITOS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitos(@Param("dentistaId") Long dentistaId,
                                    @Param("data") LocalDate data,
                                    @Param("horaInicio") LocalTime horaInicio,
                                    @Param("horaFim") LocalTime horaFim);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data AND a.id != :agendamentoId AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitosExcluindo(@Param("dentistaId") Long dentistaId,
                                             @Param("data") LocalDate data,
                                             @Param("horaInicio") LocalTime horaInicio,
                                             @Param("horaFim") LocalTime horaFim,
                                             @Param("agendamentoId") Long agendamentoId);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta = :data AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitosParaPaciente(@Param("pacienteId") Long pacienteId,
                                                @Param("data") LocalDate data,
                                                @Param("horaInicio") LocalTime horaInicio,
                                                @Param("horaFim") LocalTime horaFim);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta = :data AND a.id != :agendamentoId AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitosParaPacienteExcluindo(@Param("pacienteId") Long pacienteId,
                                                         @Param("data") LocalDate data,
                                                         @Param("horaInicio") LocalTime horaInicio,
                                                         @Param("horaFim") LocalTime horaFim,
                                                         @Param("agendamentoId") Long agendamentoId);

    // ==================== HORÁRIOS DISPONÍVEIS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') ORDER BY a.horaInicio")
    List<Agendamento> findAgendamentosAtivos(@Param("dentistaId") Long dentistaId,
                                             @Param("data") LocalDate data);

    // ==================== PRÓXIMOS AGENDAMENTOS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta >= :dataAtual AND a.ativo = true " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO') " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findProximosAgendamentos(@Param("pacienteId") Long pacienteId,
                                               @Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta >= :dataAtual AND a.ativo = true " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO') " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findProximosAgendamentosDentista(@Param("dentistaId") Long dentistaId,
                                                       @Param("dataAtual") LocalDate dataAtual);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.status = :status AND a.ativo = true")
    Long countByDentistaAndPeriodoAndStatus(@Param("dentistaId") Long dentistaId,
                                            @Param("dataInicio") LocalDate dataInicio,
                                            @Param("dataFim") LocalDate dataFim,
                                            @Param("status") StatusAgendamento status);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    Long countConsultasDoDia(@Param("dentistaId") Long dentistaId,
                             @Param("data") LocalDate data);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.status = 'FALTOU' AND a.ativo = true")
    Long countFaltasPaciente(@Param("pacienteId") Long pacienteId);

    // ==================== CONSULTAS PARA LEMBRETES ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data " +
            "AND a.lembreteEnviado = false AND a.ativo = true " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO')")
    List<Agendamento> findConsultasParaLembrete(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.lembreteEnviado = false AND a.ativo = true " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO')")
    List<Agendamento> findConsultasParaLembreteNoPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                                         @Param("dataFim") LocalDate dataFim);

    // ==================== CONSULTAS PARA ANÁLISE ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta < :data " +
            "AND a.status = 'AGENDADO' AND a.ativo = true")
    List<Agendamento> findConsultasPassadasNaoFinalizadas(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.status = 'EM_ATENDIMENTO' " +
            "AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findConsultasEmAtendimento();

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data " +
            "AND a.status = 'CONFIRMADO' AND a.ativo = true " +
            "ORDER BY a.horaInicio")
    List<Agendamento> findConsultasConfirmadasDoDia(@Param("data") LocalDate data);

    // ==================== HISTÓRICO DO PACIENTE ====================

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.status = 'CONCLUIDO' AND a.ativo = true " +
            "ORDER BY a.dataConsulta DESC, a.horaInicio DESC")
    Page<Agendamento> findHistoricoConsultasPaciente(@Param("pacienteId") Long pacienteId,
                                                     Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta < :dataAtual AND a.ativo = true " +
            "ORDER BY a.dataConsulta DESC, a.horaInicio DESC")
    List<Agendamento> findConsultasAnterioresPaciente(@Param("pacienteId") Long pacienteId,
                                                      @Param("dataAtual") LocalDate dataAtual);

    // ==================== VERIFICAÇÕES ESPECÍFICAS ====================

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a " +
            "WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    boolean existsAgendamentoAtivoNoDia(@Param("dentistaId") Long dentistaId,
                                        @Param("data") LocalDate data);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a " +
            "WHERE a.id = :id AND a.ativo = true")
    boolean existsByIdAtivo(@Param("id") Long id);
}