package consultorio.domain.repository;

import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.Agendamento.StatusAgendamento;
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // ==================== BUSCAS BÁSICAS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.id = :id AND a.ativo = true")
    Optional<Agendamento> findByIdAtivo(@Param("id") Long id);

    @Query("SELECT a FROM Agendamento a WHERE a.ativo = true")
    Page<Agendamento> findAllAtivos(Pageable pageable);

    // ==================== FILTROS PRINCIPAIS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.ativo = true")
    Page<Agendamento> findByDentistaId(@Param("dentistaId") Long dentistaId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.ativo = true")
    Page<Agendamento> findByPacienteId(@Param("pacienteId") Long pacienteId, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.status = :status AND a.ativo = true")
    Page<Agendamento> findByStatus(@Param("status") StatusAgendamento status, Pageable pageable);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    Page<Agendamento> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, Pageable pageable);

    // ==================== AGENDA DO DIA ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findAgendaDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findAllAgendaDoDia(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta = :data AND a.status = 'CONFIRMADO' AND a.ativo = true ORDER BY a.horaInicio")
    List<Agendamento> findConfirmadosDoDia(@Param("data") LocalDate data);

    // ==================== CONFLITOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitos(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data,
                                    @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.id <> :idExcluir AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitosExcluindo(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data,
                                             @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim,
                                             @Param("idExcluir") Long idExcluir);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    List<Agendamento> findConflitoPaciente(@Param("pacienteId") Long pacienteId, @Param("data") LocalDate data,
                                           @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    // ==================== PRÓXIMOS AGENDAMENTOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId
        AND a.dataConsulta >= :hoje AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        ORDER BY a.dataConsulta, a.horaInicio
        """)
    List<Agendamento> findProximosPaciente(@Param("pacienteId") Long pacienteId, @Param("hoje") LocalDate hoje);

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta >= :hoje AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        ORDER BY a.dataConsulta, a.horaInicio
        """)
    List<Agendamento> findProximosDentista(@Param("dentistaId") Long dentistaId, @Param("hoje") LocalDate hoje);

    // ==================== HORÁRIOS OCUPADOS ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data AND a.ativo = true
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        ORDER BY a.horaInicio
        """)
    List<Agendamento> findOcupadosDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    // ==================== LEMBRETES ====================

    @Query("""
        SELECT a FROM Agendamento a WHERE a.dataConsulta = :data
        AND a.lembreteEnviado = false AND a.ativo = true
        AND a.status IN ('AGENDADO', 'CONFIRMADO')
        """)
    List<Agendamento> findParaLembrete(@Param("data") LocalDate data);

    @Modifying
    @Query("UPDATE Agendamento a SET a.lembreteEnviado = true, a.lembreteEnviadoEm = :agora WHERE a.id IN :ids")
    int marcarLembretesEnviados(@Param("ids") List<Long> ids, @Param("agora") LocalDateTime agora);

    // ==================== CONSULTAS ESPECIAIS ====================

    @Query("SELECT a FROM Agendamento a WHERE a.dataConsulta < :hoje AND a.status = 'AGENDADO' AND a.ativo = true")
    List<Agendamento> findPassadosNaoFinalizados(@Param("hoje") LocalDate hoje);

    @Query("SELECT a FROM Agendamento a WHERE a.status = 'EM_ATENDIMENTO' AND a.ativo = true ORDER BY a.dataConsulta, a.horaInicio")
    List<Agendamento> findEmAtendimento();

    // ==================== HISTÓRICO ====================

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.status = 'CONCLUIDO' AND a.ativo = true ORDER BY a.dataConsulta DESC")
    Page<Agendamento> findHistoricoPaciente(@Param("pacienteId") Long pacienteId, Pageable pageable);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dentista.id = :dentistaId AND a.dataConsulta = :data AND a.ativo = true AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    long countConsultasDoDia(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.paciente.id = :pacienteId AND a.status = 'FALTOU' AND a.ativo = true")
    long countFaltasPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.dataConsulta = :data AND a.ativo = true AND a.status NOT IN ('CANCELADO', 'FALTOU')")
    long countTotalDoDia(@Param("data") LocalDate data);

    @Query("SELECT a.status, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true GROUP BY a.status")
    List<Object[]> countPorStatusNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT a.tipoProcedimento, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true AND a.tipoProcedimento IS NOT NULL GROUP BY a.tipoProcedimento")
    List<Object[]> countPorProcedimentoNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT a.dentista.id, a.dentista.nome, COUNT(a) FROM Agendamento a WHERE a.dataConsulta BETWEEN :inicio AND :fim AND a.ativo = true GROUP BY a.dentista.id, a.dentista.nome ORDER BY COUNT(a) DESC")
    List<Object[]> countPorDentistaNoPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    // ==================== UPDATES EM LOTE ====================

    @Modifying
    @Query("UPDATE Agendamento a SET a.status = :status, a.atualizadoEm = :agora, a.atualizadoPor = :usuario WHERE a.id IN :ids")
    int updateStatusEmLote(@Param("ids") List<Long> ids, @Param("status") StatusAgendamento status,
                           @Param("agora") LocalDateTime agora, @Param("usuario") String usuario);

    @Modifying
    @Query("UPDATE Agendamento a SET a.ativo = false, a.atualizadoEm = :agora WHERE a.id = :id")
    int desativar(@Param("id") Long id, @Param("agora") LocalDateTime agora);

    // ==================== VERIFICAÇÕES ====================

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Agendamento a WHERE a.id = :id AND a.ativo = true")
    boolean existsAtivo(@Param("id") Long id);
}