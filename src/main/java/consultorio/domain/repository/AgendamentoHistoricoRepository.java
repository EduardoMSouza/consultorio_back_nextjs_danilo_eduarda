package consultorio.domain.repository;

<<<<<<< HEAD
import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import consultorio.domain.entity.agendamento.AgendamentoHistorico.TipoAcao;
=======
import consultorio.domain.entity.AgendamentoHistorico;
import consultorio.domain.entity.AgendamentoHistorico.TipoAcao;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoHistoricoRepository extends JpaRepository<AgendamentoHistorico, Long> {

    // ==================== POR AGENDAMENTO ====================

    List<AgendamentoHistorico> findByAgendamentoIdOrderByDataHoraDesc(Long agendamentoId);

    Page<AgendamentoHistorico> findByAgendamentoId(Long agendamentoId, Pageable pageable);

    // ==================== POR AÇÃO ====================

    List<AgendamentoHistorico> findByAcaoOrderByDataHoraDesc(TipoAcao acao);

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.agendamentoId = :agendamentoId AND h.acao = :acao ORDER BY h.dataHora DESC")
    List<AgendamentoHistorico> findByAgendamentoAndAcao(@Param("agendamentoId") Long agendamentoId, @Param("acao") TipoAcao acao);

    // ==================== POR PERÍODO ====================

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.dataHora BETWEEN :inicio AND :fim ORDER BY h.dataHora DESC")
    Page<AgendamentoHistorico> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.agendamentoId = :agendamentoId AND h.dataHora BETWEEN :inicio AND :fim ORDER BY h.dataHora DESC")
    List<AgendamentoHistorico> findByAgendamentoAndPeriodo(@Param("agendamentoId") Long agendamentoId,
                                                           @Param("inicio") LocalDateTime inicio,
                                                           @Param("fim") LocalDateTime fim);

    // ==================== POR USUÁRIO ====================

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.usuarioResponsavel = :usuario ORDER BY h.dataHora DESC")
    Page<AgendamentoHistorico> findByUsuario(@Param("usuario") String usuario, Pageable pageable);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT h.acao, COUNT(h) FROM AgendamentoHistorico h WHERE h.dataHora BETWEEN :inicio AND :fim GROUP BY h.acao")
    List<Object[]> countPorAcaoNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT h.usuarioResponsavel, COUNT(h) FROM AgendamentoHistorico h WHERE h.dataHora BETWEEN :inicio AND :fim AND h.usuarioResponsavel IS NOT NULL GROUP BY h.usuarioResponsavel ORDER BY COUNT(h) DESC")
    List<Object[]> countPorUsuarioNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT COUNT(h) FROM AgendamentoHistorico h WHERE h.agendamentoId = :agendamentoId")
    long countByAgendamento(@Param("agendamentoId") Long agendamentoId);

    // ==================== ÚLTIMO REGISTRO ====================

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.agendamentoId = :agendamentoId ORDER BY h.dataHora DESC LIMIT 1")
    AgendamentoHistorico findUltimoByAgendamento(@Param("agendamentoId") Long agendamentoId);

    // ==================== CANCELAMENTOS ====================

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.acao = 'CANCELAMENTO' AND h.dataHora BETWEEN :inicio AND :fim ORDER BY h.dataHora DESC")
    List<AgendamentoHistorico> findCancelamentosNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}