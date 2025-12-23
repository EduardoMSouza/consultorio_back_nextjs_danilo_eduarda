package consultorio.domain.repository;

import consultorio.domain.entity.Agendamento;
import consultorio.domain.entity.FilaEspera;
import consultorio.domain.entity.FilaEspera.StatusFila;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    // ==================== BUSCAR POR FILTROS ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAllAtivas();

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    Page<FilaEspera> findAllAtivas(Pageable pageable);

    @Query("SELECT f FROM FilaEspera f WHERE f.dentista.id = :dentistaId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findByDentistaAtivas(@Param("dentistaId") Long dentistaId);

    @Query("SELECT f FROM FilaEspera f WHERE f.paciente.id = :pacienteId " +
            "ORDER BY f.criadoEm DESC")
    List<FilaEspera> findByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT f FROM FilaEspera f WHERE f.paciente.id = :pacienteId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    List<FilaEspera> findByPacienteAtivas(@Param("pacienteId") Long pacienteId);

    @Query("SELECT f FROM FilaEspera f WHERE f.status = :status " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    Page<FilaEspera> findByStatus(@Param("status") StatusFila status, Pageable pageable);

    // ==================== BUSCAR COMPATÍVEIS ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "AND (f.dentista.id = :dentistaId OR f.aceitaQualquerDentista = true) " +
            "AND (f.dataPreferencial IS NULL OR f.dataPreferencial = :data) " +
            "AND (f.tipoProcedimento = :tipoProcedimento OR f.tipoProcedimento IS NULL) " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findCompativeis(@Param("dentistaId") Long dentistaId,
                                     @Param("data") LocalDate data,
                                     @Param("tipoProcedimento") Agendamento.TipoProcedimento tipoProcedimento);

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "AND (f.dentista IS NULL OR f.dentista.id = :dentistaId) " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findCompatíveisPorDentista(@Param("dentistaId") Long dentistaId);

    // ==================== NOTIFICAÇÕES ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'AGUARDANDO' " +
            "AND f.notificado = false " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findPendentesNotificacao();

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'NOTIFICADO' " +
            "AND f.notificadoEm < :dataLimite")
    List<FilaEspera> findNotificadasSemResposta(@Param("dataLimite") java.time.LocalDateTime dataLimite);

    // ==================== EXPIRAÇÃO ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "AND f.dataPreferencial IS NOT NULL " +
            "AND f.dataPreferencial < :data")
    List<FilaEspera> findExpiradas(@Param("data") LocalDate data);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.dentista.id = :dentistaId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    Long countAtivasPorDentista(@Param("dentistaId") Long dentistaId);

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    Long countTotalAtivas();

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.paciente.id = :pacienteId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    Long countAtivasPorPaciente(@Param("pacienteId") Long pacienteId);

    // ==================== VERIFICAÇÕES ====================

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FilaEspera f " +
            "WHERE f.paciente.id = :pacienteId AND f.dentista.id = :dentistaId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    boolean existsByPacienteAndDentistaAtiva(@Param("pacienteId") Long pacienteId,
                                             @Param("dentistaId") Long dentistaId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FilaEspera f " +
            "WHERE f.paciente.id = :pacienteId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    boolean existsByPacienteAtiva(@Param("pacienteId") Long pacienteId);
}