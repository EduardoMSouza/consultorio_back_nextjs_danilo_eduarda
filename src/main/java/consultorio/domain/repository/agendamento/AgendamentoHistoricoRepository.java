package consultorio.domain.repository.agendamento;

import consultorio.domain.entity.agendamento.AgendamentoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoHistoricoRepository extends JpaRepository<AgendamentoHistorico, Long> {

    List<AgendamentoHistorico> findByAgendamentoIdOrderByDataHoraDesc(Long agendamentoId);

    List<AgendamentoHistorico> findByUsuarioResponsavelOrderByDataHoraDesc(String usuario);

    List<AgendamentoHistorico> findByAcaoOrderByDataHoraDesc(AgendamentoHistorico.TipoAcao acao);

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.dataHora BETWEEN :inicio AND :fim " +
            "ORDER BY h.dataHora DESC")
    List<AgendamentoHistorico> findByPeriodo(@Param("inicio") LocalDateTime inicio,
                                             @Param("fim") LocalDateTime fim);

    @Query("SELECT h FROM AgendamentoHistorico h WHERE h.agendamentoId = :agendamentoId " +
            "AND h.acao = :acao " +
            "ORDER BY h.dataHora DESC")
    List<AgendamentoHistorico> findByAgendamentoAndAcao(@Param("agendamentoId") Long agendamentoId,
                                                        @Param("acao") AgendamentoHistorico.TipoAcao acao);

    @Query("SELECT COUNT(h) FROM AgendamentoHistorico h WHERE h.usuarioResponsavel = :usuario " +
            "AND h.dataHora BETWEEN :inicio AND :fim")
    Long countByUsuarioAndPeriodo(@Param("usuario") String usuario,
                                  @Param("inicio") LocalDateTime inicio,
                                  @Param("fim") LocalDateTime fim);
}