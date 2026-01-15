package consultorio.domain.repository;

<<<<<<< HEAD
import consultorio.domain.entity.agendamento.FilaEspera;
import consultorio.domain.entity.agendamento.FilaEspera.StatusFila;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
=======
import consultorio.domain.entity.Agendamento.TipoProcedimento;
import consultorio.domain.entity.FilaEspera;
import consultorio.domain.entity.FilaEspera.StatusFila;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    // ==================== LISTAGENS ATIVAS ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAllAtivas();

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') ORDER BY f.prioridade DESC, f.criadoEm ASC")
    Page<FilaEspera> findAllAtivas(Pageable pageable);

    Page<FilaEspera> findByStatus(StatusFila status, Pageable pageable);

    // ==================== POR DENTISTA ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.dentista.id = :dentistaId AND f.status IN ('AGUARDANDO', 'NOTIFICADO') ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAtivasByDentista(@Param("dentistaId") Long dentistaId);

    @Query("SELECT f FROM FilaEspera f WHERE (f.dentista.id = :dentistaId OR f.aceitaQualquerDentista = true) AND f.status IN ('AGUARDANDO', 'NOTIFICADO') ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findCompatíveisPorDentista(@Param("dentistaId") Long dentistaId);

    // ==================== POR PACIENTE ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.paciente.id = :pacienteId ORDER BY f.criadoEm DESC")
    List<FilaEspera> findByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT f FROM FilaEspera f WHERE f.paciente.id = :pacienteId AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    List<FilaEspera> findAtivasByPaciente(@Param("pacienteId") Long pacienteId);

    // ==================== COMPATÍVEIS ====================

    @Query("""
        SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO')
        AND (f.dentista.id = :dentistaId OR f.aceitaQualquerDentista = true)
        AND (f.dataPreferencial IS NULL OR f.dataPreferencial = :data)
        AND (f.tipoProcedimento = :tipoProcedimento OR f.tipoProcedimento IS NULL)
        ORDER BY f.prioridade DESC, f.criadoEm ASC
        """)
    List<FilaEspera> findCompativeis(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data,
                                     @Param("tipoProcedimento") TipoProcedimento tipoProcedimento);

    @Query("""
        SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO')
        AND (f.dentista.id = :dentistaId OR f.aceitaQualquerDentista = true)
        AND (f.dataPreferencial IS NULL OR f.dataPreferencial = :data)
        ORDER BY f.prioridade DESC, f.criadoEm ASC
        """)
    List<FilaEspera> findCompatíveisParaData(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data);

    // ==================== NOTIFICAÇÕES ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'AGUARDANDO' AND f.notificado = false ORDER BY f.prioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findPendentesNotificacao();

    @Query("SELECT f FROM FilaEspera f WHERE f.status = 'NOTIFICADO' AND f.notificadoEm < :dataLimite")
    List<FilaEspera> findNotificadasSemResposta(@Param("dataLimite") LocalDateTime dataLimite);

    // ==================== EXPIRAÇÃO ====================

    @Query("SELECT f FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO') AND f.dataPreferencial IS NOT NULL AND f.dataPreferencial < :hoje")
    List<FilaEspera> findExpiradas(@Param("hoje") LocalDate hoje);

    @Modifying
    @Query("UPDATE FilaEspera f SET f.status = 'EXPIRADO', f.atualizadoEm = :agora WHERE f.id IN :ids")
    int expirarEmLote(@Param("ids") List<Long> ids, @Param("agora") LocalDateTime agora);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    long countAtivas();

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.dentista.id = :dentistaId AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    long countAtivasByDentista(@Param("dentistaId") Long dentistaId);

    @Query("SELECT COUNT(f) FROM FilaEspera f WHERE f.paciente.id = :pacienteId AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    long countAtivasByPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT f.status, COUNT(f) FROM FilaEspera f GROUP BY f.status")
    List<Object[]> countPorStatus();

    @Query("""
        SELECT f.dentista.id, f.dentista.nome, COUNT(f)
        FROM FilaEspera f
        WHERE f.dentista IS NOT NULL AND f.status IN ('AGUARDANDO', 'NOTIFICADO')
        GROUP BY f.dentista.id, f.dentista.nome
        ORDER BY COUNT(f) DESC
        """)
    List<Object[]> countAtivasPorDentista();

    // ==================== POSIÇÃO NA FILA ====================

    @Query("""
        SELECT COUNT(f) + 1 FROM FilaEspera f
        WHERE f.status IN ('AGUARDANDO', 'NOTIFICADO')
        AND (f.prioridade > :prioridade OR (f.prioridade = :prioridade AND f.criadoEm < :criadoEm))
        """)
    int calcularPosicao(@Param("prioridade") Integer prioridade, @Param("criadoEm") LocalDateTime criadoEm);

    @Query("""
        SELECT COUNT(f) + 1 FROM FilaEspera f
        WHERE f.dentista.id = :dentistaId
        AND f.status IN ('AGUARDANDO', 'NOTIFICADO')
        AND (f.prioridade > :prioridade OR (f.prioridade = :prioridade AND f.criadoEm < :criadoEm))
        """)
    int calcularPosicaoPorDentista(@Param("dentistaId") Long dentistaId, @Param("prioridade") Integer prioridade, @Param("criadoEm") LocalDateTime criadoEm);

    // ==================== VERIFICAÇÕES ====================

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FilaEspera f WHERE f.paciente.id = :pacienteId AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    boolean existsAtivaPorPaciente(@Param("pacienteId") Long pacienteId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FilaEspera f WHERE f.paciente.id = :pacienteId AND f.dentista.id = :dentistaId AND f.status IN ('AGUARDANDO', 'NOTIFICADO')")
    boolean existsAtivaPorPacienteEDentista(@Param("pacienteId") Long pacienteId, @Param("dentistaId") Long dentistaId);

    // ==================== UPDATES ====================

    @Modifying
    @Query("UPDATE FilaEspera f SET f.status = :status, f.atualizadoEm = :agora WHERE f.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") StatusFila status, @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE FilaEspera f SET f.notificado = true, f.notificadoEm = :agora, f.status = 'NOTIFICADO', f.atualizadoEm = :agora WHERE f.id = :id")
    int marcarNotificado(@Param("id") Long id, @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE FilaEspera f SET f.tentativasContato = f.tentativasContato + 1, f.ultimaTentativaContato = :agora, f.atualizadoEm = :agora WHERE f.id = :id")
    int incrementarTentativa(@Param("id") Long id, @Param("agora") LocalDateTime agora);
}