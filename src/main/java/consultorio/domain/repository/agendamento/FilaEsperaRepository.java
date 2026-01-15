package consultorio.domain.repository.agendamento;

import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    List<FilaEspera> findByStatusOrderByPrioridadeDescCriadoEmAsc(FilaEspera.StatusFila status);

    List<FilaEspera> findByPacienteIdAndStatusIn(Long pacienteId, List<FilaEspera.StatusFila> statuses);

    List<FilaEspera> findByDentistaIdAndStatusOrderByPrioridadeDescCriadoEmAsc(Long dentistaId, FilaEspera.StatusFila status);

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'AGUARDANDO' " +
            "AND (f.dentista.id = :dentistaId OR f.aceitaQualquerDentista = true) " +
            "AND (f.tipoProcedimento = :tipoProcedimento OR f.tipoProcedimento IS NULL) " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findCandidatosParaVaga(@Param("dentistaId") Long dentistaId,
                                            @Param("tipoProcedimento") TipoProcedimento tipoProcedimento);

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'AGUARDANDO' " +
            "AND f.dataPreferencial <= :data " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAguardandoAteData(@Param("data") LocalDate data);

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "AND f.dataPreferencial IS NOT NULL " +
            "AND f.dataPreferencial < :data")
    List<FilaEspera> findExpirados(@Param("data") LocalDate data);

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.paciente.id = :pacienteId " +
            "AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    Long countAtivasByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.dentista.id = :dentistaId " +
            "AND f.status = 'AGUARDANDO'")
    Long countAguardandoByDentista(@Param("dentistaId") Long dentistaId);

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'NOTIFICADO' " +
            "AND f.notificadoEm < :dataLimite " +
            "AND f.agendamento IS NULL")
    List<FilaEspera> findNotificadosSemResposta(@Param("dataLimite") java.time.LocalDateTime dataLimite);

    List<FilaEspera> findByStatusAndTipoProcedimentoOrderByPrioridadeDescCriadoEmAsc(FilaEspera.StatusFila status,
                                                                                     TipoProcedimento tipoProcedimento);

    @Query("SELECT f FROM FilaEspera f " +
            "LEFT JOIN FETCH f.paciente " +
            "LEFT JOIN FETCH f.dentista " +
            "WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') " +
            "ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAllAtivasWithDetails();

    boolean existsByPacienteIdAndStatusIn(Long pacienteId, List<FilaEspera.StatusFila> statuses);
}