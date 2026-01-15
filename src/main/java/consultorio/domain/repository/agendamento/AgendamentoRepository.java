package consultorio.domain.repository.agendamento;

import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
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

    List<Agendamento> findByDentistaIdAndDataConsultaAndAtivoTrue(Long dentistaId, LocalDate dataConsulta);

    List<Agendamento> findByPacienteIdAndAtivoTrue(Long pacienteId);

    List<Agendamento> findByDentistaIdAndAtivoTrue(Long dentistaId);

    List<Agendamento> findByDataConsultaAndAtivoTrue(LocalDate dataConsulta);

    List<Agendamento> findByDataConsultaBetweenAndAtivoTrue(LocalDate dataInicio, LocalDate dataFim);

    List<Agendamento> findByStatusAndAtivoTrue(StatusAgendamento status);

    List<Agendamento> findByDentistaIdAndStatusAndAtivoTrue(Long dentistaId, StatusAgendamento status);

    List<Agendamento> findByPacienteIdAndStatusAndAtivoTrue(Long pacienteId, StatusAgendamento status);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data " +
            "AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitantes(@Param("dentistaId") Long dentistaId,
                                       @Param("data") LocalDate data,
                                       @Param("horaInicio") LocalTime horaInicio,
                                       @Param("horaFim") LocalTime horaFim);

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data " +
            "AND a.ativo = true " +
            "AND a.id != :agendamentoId " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitantesExcluindoAgendamento(@Param("dentistaId") Long dentistaId,
                                                           @Param("data") LocalDate data,
                                                           @Param("horaInicio") LocalTime horaInicio,
                                                           @Param("horaFim") LocalTime horaFim,
                                                           @Param("agendamentoId") Long agendamentoId);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data " +
            "AND a.status = 'AGENDADO' " +
            "AND a.lembreteEnviado = false " +
            "AND a.ativo = true")
    List<Agendamento> findAgendamentosParaLembrete(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta < :data " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO') " +
            "AND a.ativo = true")
    List<Agendamento> findAgendamentosAtrasados(@Param("data") LocalDate data);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.status = :status " +
            "AND a.ativo = true")
    Long countByDentistaAndPeriodoAndStatus(@Param("dentistaId") Long dentistaId,
                                            @Param("dataInicio") LocalDate dataInicio,
                                            @Param("dataFim") LocalDate dataFim,
                                            @Param("status") StatusAgendamento status);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.ativo = true")
    Long countByPacienteAndPeriodo(@Param("pacienteId") Long pacienteId,
                                   @Param("dataInicio") LocalDate dataInicio,
                                   @Param("dataFim") LocalDate dataFim);

    Optional<Agendamento> findByIdAndAtivoTrue(Long id);

    @Query("SELECT a FROM Agendamento a " +
            "LEFT JOIN FETCH a.dentista " +
            "LEFT JOIN FETCH a.paciente " +
            "WHERE a.id = :id AND a.ativo = true")
    Optional<Agendamento> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT a FROM Agendamento a " +
            "LEFT JOIN FETCH a.dentista " +
            "LEFT JOIN FETCH a.paciente " +
            "WHERE a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.ativo = true " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findAllWithDetailsByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                                  @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Agendamento a " +
            "WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.ativo = true " +
            "ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findByDentistaAndPeriodo(@Param("dentistaId") Long dentistaId,
                                               @Param("dataInicio") LocalDate dataInicio,
                                               @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Agendamento a " +
            "WHERE a.paciente.id = :pacienteId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim " +
            "AND a.ativo = true " +
            "ORDER BY a.dataConsulta DESC, a.horaInicio DESC")
    List<Agendamento> findByPacienteAndPeriodo(@Param("pacienteId") Long pacienteId,
                                               @Param("dataInicio") LocalDate dataInicio,
                                               @Param("dataFim") LocalDate dataFim);

    boolean existsByPacienteIdAndDataConsultaAndStatusNotAndAtivoTrue(Long pacienteId,
                                                                      LocalDate dataConsulta,
                                                                      StatusAgendamento status);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a " +
            "WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta = :data " +
            "AND a.ativo = true " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    boolean existsAgendamentoAtivoPorDentistaEData(@Param("dentistaId") Long dentistaId,
                                                   @Param("data") LocalDate data);
}