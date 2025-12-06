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

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Buscar por dentista
    Page<Agendamento> findByDentistaId(Long dentistaId, Pageable pageable);

    // Buscar por paciente
    Page<Agendamento> findByPacienteId(Long pacienteId, Pageable pageable);

    // Buscar por status
    Page<Agendamento> findByStatus(StatusAgendamento status, Pageable pageable);

    // Agenda do dia de um dentista
    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data ORDER BY a.horaInicio")
    List<Agendamento> findAgendaDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    // Agenda do dia - todos os dentistas
    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data ORDER BY a.horaInicio")
    List<Agendamento> findAllAgendaDoDia(@Param("data") LocalDate data);

    // Buscar por período
    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta BETWEEN :dataInicio AND :dataFim ORDER BY a.dataConsulta, a.horaInicio")
    Page<Agendamento> findByPeriodo(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim, Pageable pageable);

    // Buscar por período e dentista
    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta BETWEEN :dataInicio AND :dataFim ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findByDentistaAndPeriodo(@Param("dentistaId") Long dentistaId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    // Verificar conflito de horário
    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitos(@Param("dentistaId") Long dentistaId,
                                    @Param("data") LocalDate data,
                                    @Param("horaInicio") LocalTime horaInicio,
                                    @Param("horaFim") LocalTime horaFim);

    // Verificar conflito excluindo um agendamento (para update)
    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data " +
            "AND a.id != :agendamentoId " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') " +
            "AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))")
    List<Agendamento> findConflitosExcluindo(@Param("dentistaId") Long dentistaId,
                                             @Param("data") LocalDate data,
                                             @Param("horaInicio") LocalTime horaInicio,
                                             @Param("horaFim") LocalTime horaFim,
                                             @Param("agendamentoId") Long agendamentoId);

    // Horários disponíveis - buscar agendamentos do dia para calcular disponibilidade
    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data " +
            "AND a.status NOT IN ('CANCELADO', 'FALTOU') ORDER BY a.horaInicio")
    List<Agendamento> findAgendamentosAtivos(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    // Buscar próximos agendamentos do paciente
    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.dataConsulta >= :dataAtual " +
            "AND a.status IN ('AGENDADO', 'CONFIRMADO') ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findProximosAgendamentos(@Param("pacienteId") Long pacienteId, @Param("dataAtual") LocalDate dataAtual);

    // Contar agendamentos por status e período
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId " +
            "AND a.dataConsulta BETWEEN :dataInicio AND :dataFim AND a.status = :status")
    Long countByDentistaAndPeriodoAndStatus(@Param("dentistaId") Long dentistaId,
                                            @Param("dataInicio") LocalDate dataInicio,
                                            @Param("dataFim") LocalDate dataFim,
                                            @Param("status") StatusAgendamento status);
}