package consultorio.domain.repository.tratamento;

import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoDentalRepository extends JpaRepository<PlanoDental, Long> {

    // ========== BUSCAS BÁSICAS ==========

    /**
     * Busca plano por ID e ativo
     */
    Optional<PlanoDental> findByIdAndAtivoTrue(Long id);

    /**
     * Verifica se plano existe por ID e ativo
     */
    boolean existsByIdAndAtivoTrue(Long id);

    // ========== BUSCAS POR PACIENTE ==========

    List<PlanoDental> findByPacienteId(Long pacienteId);
    Page<PlanoDental> findByPacienteId(Long pacienteId, Pageable pageable);
    List<PlanoDental> findByPacienteIdAndAtivoTrue(Long pacienteId);
    List<PlanoDental> findByPacienteIdAndAtivoTrueOrderByCriadoEmDesc(Long pacienteId);

    // ========== BUSCAS POR DENTISTA ==========

    List<PlanoDental> findByDentistaId(Long dentistaId);
    Page<PlanoDental> findByDentistaId(Long dentistaId, Pageable pageable);
    List<PlanoDental> findByDentistaIdAndAtivoTrue(Long dentistaId);
    List<PlanoDental> findByDentistaIdAndAtivoTrueOrderByCriadoEmDesc(Long dentistaId);

    // ========== BUSCAS POR PACIENTE E DENTISTA ==========

    List<PlanoDental> findByPacienteIdAndDentistaId(Long pacienteId, Long dentistaId);
    List<PlanoDental> findByPacienteIdAndDentistaIdAndAtivoTrue(Long pacienteId, Long dentistaId);

    // ========== BUSCAS POR STATUS ==========

    List<PlanoDental> findByStatus(StatusPlano status);
    Page<PlanoDental> findByStatus(StatusPlano status, Pageable pageable);
    List<PlanoDental> findByStatusAndAtivoTrue(StatusPlano status);

    // ========== BUSCAS POR DENTE E PROCEDIMENTO ==========

    List<PlanoDental> findByDente(String dente);
    List<PlanoDental> findByDenteAndAtivoTrue(String dente);
    List<PlanoDental> findByProcedimentoContainingIgnoreCase(String procedimento);
    List<PlanoDental> findByProcedimentoContainingIgnoreCaseAndAtivoTrue(String procedimento);
    List<PlanoDental> findByDenteAndProcedimentoContainingIgnoreCase(String dente, String procedimento);

    // ========== BUSCAS ESPECÍFICAS ==========

    /**
     * Busca planos ativos de um paciente ordenados por data prevista
     */
    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.ativo = true ORDER BY p.dataPrevista ASC")
    List<PlanoDental> findAtivosByPacienteIdOrderByDataPrevista(@Param("pacienteId") Long pacienteId);

    /**
     * Busca planos por data prevista entre datas
     */
    List<PlanoDental> findByDataPrevistaBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca planos criados entre datas
     */
    List<PlanoDental> findByCriadoEmBetween(LocalDateTime inicio, LocalDateTime fim);

    /**
     * Busca planos por status e urgência
     */
    @Query("SELECT p FROM PlanoDental p WHERE p.urgente = true AND p.status = :status")
    List<PlanoDental> findUrgentesByStatus(@Param("status") StatusPlano status);

    /**
     * Busca planos urgentes e ativos
     */
    List<PlanoDental> findByUrgenteTrueAndAtivoTrue();

    /**
     * Busca planos por prioridade
     */
    List<PlanoDental> findByPrioridadeAndAtivoTrue(String prioridade);

    /**
     * Busca planos por valor maior que
     */
    List<PlanoDental> findByValorFinalGreaterThan(BigDecimal valor);

    /**
     * Busca planos por valor entre
     */
    List<PlanoDental> findByValorFinalBetween(BigDecimal minValor, BigDecimal maxValor);

    // ========== BUSCAS COMBINADAS ==========

    List<PlanoDental> findByPacienteIdAndStatus(Long pacienteId, StatusPlano status);
    List<PlanoDental> findByDentistaIdAndStatus(Long dentistaId, StatusPlano status);
    List<PlanoDental> findByPacienteIdAndDentistaIdAndStatus(Long pacienteId, Long dentistaId, StatusPlano status);

    /**
     * Busca planos por paciente e urgência
     */
    List<PlanoDental> findByPacienteIdAndUrgenteTrueAndAtivoTrue(Long pacienteId);

    /**
     * Busca planos por dentista e urgência
     */
    List<PlanoDental> findByDentistaIdAndUrgenteTrueAndAtivoTrue(Long dentistaId);

    // ========== BUSCAS COM MÚLTIPLOS STATUS ==========

    List<PlanoDental> findByStatusInOrderByDataPrevista(List<StatusPlano> statuses);
    List<PlanoDental> findByStatusInAndAtivoTrueOrderByDataPrevista(List<StatusPlano> statuses);

    // ========== VALIDAÇÕES E VERIFICAÇÕES ==========

    /**
     * Verifica se existe plano ativo para paciente, dente e procedimento
     */
    boolean existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(Long pacienteId, String dente, String procedimento);

    /**
     * Verifica se existe plano com mesmo dente e procedimento (para qualquer paciente)
     */
    boolean existsByDenteAndProcedimento(String dente, String procedimento);

    // ========== CONTAGENS E ESTATÍSTICAS ==========

    /**
     * Conta planos ativos por paciente
     */
    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.ativo = true")
    Long countAtivosByPacienteId(@Param("pacienteId") Long pacienteId);

    /**
     * Conta planos por status e paciente
     */
    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.status = :status")
    Long countByPacienteIdAndStatus(@Param("pacienteId") Long pacienteId, @Param("status") StatusPlano status);

    /**
     * Conta planos por status e dentista
     */
    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.dentista.id = :dentistaId AND p.status = :status")
    Long countByDentistaIdAndStatus(@Param("dentistaId") Long dentistaId, @Param("status") StatusPlano status);

    /**
     * Soma valor final dos planos por paciente e status
     */
    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.status = :status")
    BigDecimal sumValorFinalByPacienteIdAndStatus(@Param("pacienteId") Long pacienteId, @Param("status") StatusPlano status);

    /**
     * Soma valor final dos planos por dentista e status
     */
    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.dentista.id = :dentistaId AND p.status = :status")
    BigDecimal sumValorFinalByDentistaIdAndStatus(@Param("dentistaId") Long dentistaId, @Param("status") StatusPlano status);

    /**
     * Calcula valor total de todos os planos ativos
     */
    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.ativo = true")
    BigDecimal sumValorFinalTotal();

    // ========== BUSCAS COM FILTROS AVANÇADOS ==========

    /**
     * Busca planos com filtros múltiplos
     */
    @Query("SELECT p FROM PlanoDental p WHERE " +
            "(:pacienteId IS NULL OR p.paciente.id = :pacienteId) AND " +
            "(:dentistaId IS NULL OR p.dentista.id = :dentistaId) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:dente IS NULL OR p.dente = :dente) AND " +
            "(:procedimento IS NULL OR LOWER(p.procedimento) LIKE LOWER(CONCAT('%', :procedimento, '%'))) AND " +
            "(:urgente IS NULL OR p.urgente = :urgente) AND " +
            "(:ativo IS NULL OR p.ativo = :ativo) AND " +
            "(:dataInicio IS NULL OR p.criadoEm >= :dataInicio) AND " +
            "(:dataFim IS NULL OR p.criadoEm <= :dataFim) " +
            "ORDER BY p.criadoEm DESC")
    List<PlanoDental> findByFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("dentistaId") Long dentistaId,
            @Param("status") StatusPlano status,
            @Param("dente") String dente,
            @Param("procedimento") String procedimento,
            @Param("urgente") Boolean urgente,
            @Param("ativo") Boolean ativo,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca planos ativos com filtros múltiplos
     */
    @Query("SELECT p FROM PlanoDental p WHERE p.ativo = true AND " +
            "(:pacienteId IS NULL OR p.paciente.id = :pacienteId) AND " +
            "(:dentistaId IS NULL OR p.dentista.id = :dentistaId) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:dente IS NULL OR p.dente = :dente) AND " +
            "(:procedimento IS NULL OR LOWER(p.procedimento) LIKE LOWER(CONCAT('%', :procedimento, '%'))) AND " +
            "(:urgente IS NULL OR p.urgente = :urgente) " +
            "ORDER BY p.criadoEm DESC")
    List<PlanoDental> findAtivosByFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("dentistaId") Long dentistaId,
            @Param("status") StatusPlano status,
            @Param("dente") String dente,
            @Param("procedimento") String procedimento,
            @Param("urgente") Boolean urgente);

    // ========== BUSCAS PARA RELATÓRIOS ==========

    /**
     * Busca planos com data prevista vencida
     */
    @Query("SELECT p FROM PlanoDental p WHERE p.dataPrevista < :dataAtual AND p.status NOT IN :statusExcluidos AND p.ativo = true")
    List<PlanoDental> findComDataPrevistaVencida(
            @Param("dataAtual") LocalDateTime dataAtual,
            @Param("statusExcluidos") List<StatusPlano> statusExcluidos);

    /**
     * Busca planos por período e status
     */
    @Query("SELECT p FROM PlanoDental p WHERE p.criadoEm BETWEEN :inicio AND :fim AND p.status = :status")
    List<PlanoDental> findByPeriodoEStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") StatusPlano status);

    // ========== BUSCAS PARA DASHBOARD ==========

    /**
     * Conta planos por status
     */
    @Query("SELECT p.status, COUNT(p) FROM PlanoDental p WHERE p.ativo = true GROUP BY p.status")
    List<Object[]> countByStatusGroup();

    /**
     * Busca planos recentes
     */
    List<PlanoDental> findTop10ByAtivoTrueOrderByCriadoEmDesc();

    /**
     * Busca planos urgentes recentes
     */
    List<PlanoDental> findTop10ByUrgenteTrueAndAtivoTrueOrderByCriadoEmDesc();
}