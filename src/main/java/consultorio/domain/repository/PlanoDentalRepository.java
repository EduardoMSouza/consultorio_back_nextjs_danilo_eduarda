package consultorio.domain.repository;

<<<<<<< HEAD
import consultorio.domain.entity.tratamento.PlanoDental;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
=======
import consultorio.domain.entity.PlanoDental;
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoDentalRepository extends JpaRepository<PlanoDental, Long>, JpaSpecificationExecutor<PlanoDental> {

    List<PlanoDental> findByPacienteId(Long pacienteId);
    Page<PlanoDental> findByPacienteId(Long pacienteId, Pageable pageable);
    List<PlanoDental> findByPacienteIdAndStatus(Long pacienteId, StatusPlano status);
    Page<PlanoDental> findByPacienteIdAndStatus(Long pacienteId, StatusPlano status, Pageable pageable);
    List<PlanoDental> findByDentistaId(Long dentistaId);
    Page<PlanoDental> findByDentistaId(Long dentistaId, Pageable pageable);
    List<PlanoDental> findByDentistaIdAndStatus(Long dentistaId, StatusPlano status);
    Page<PlanoDental> findByDentistaIdAndStatus(Long dentistaId, StatusPlano status, Pageable pageable);
    List<PlanoDental> findByStatus(StatusPlano status);
    Page<PlanoDental> findByStatus(StatusPlano status, Pageable pageable);
    List<PlanoDental> findByUrgente(Boolean urgente);
    Page<PlanoDental> findByUrgente(Boolean urgente, Pageable pageable);
    List<PlanoDental> findByPrioridade(String prioridade);
    Page<PlanoDental> findByPrioridade(String prioridade, Pageable pageable);
    List<PlanoDental> findByAtivo(Boolean ativo);
    Page<PlanoDental> findByAtivo(Boolean ativo, Pageable pageable);

    List<PlanoDental> findByDente(String dente);
    Page<PlanoDental> findByDente(String dente, Pageable pageable);
    List<PlanoDental> findByProcedimentoContainingIgnoreCase(String procedimento);
    Page<PlanoDental> findByProcedimentoContainingIgnoreCase(String procedimento, Pageable pageable);
    List<PlanoDental> findByCodigoProcedimento(String codigoProcedimento);

    Optional<PlanoDental> findByIdAndAtivoTrue(Long id);
    List<PlanoDental> findByPacienteIdAndAtivoTrue(Long pacienteId);
    List<PlanoDental> findByDentistaIdAndAtivoTrue(Long dentistaId);

    List<PlanoDental> findByDataPrevistaBetween(LocalDateTime inicio, LocalDateTime fim);
    Page<PlanoDental> findByDataPrevistaBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
    List<PlanoDental> findByDataPrevistaBefore(LocalDateTime data);
    List<PlanoDental> findByDataPrevistaAfter(LocalDateTime data);
    List<PlanoDental> findByCriadoEmBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.status = :status")
    Long countByPacienteIdAndStatus(@Param("pacienteId") Long pacienteId, @Param("status") StatusPlano status);

    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.status = :status")
    BigDecimal sumValorFinalByPacienteIdAndStatus(@Param("pacienteId") Long pacienteId, @Param("status") StatusPlano status);

    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.dentista.id = :dentistaId AND p.status = :status")
    Long countByDentistaIdAndStatus(@Param("dentistaId") Long dentistaId, @Param("status") StatusPlano status);

    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.dentista.id = :dentistaId")
    List<PlanoDental> findByPacienteIdAndDentistaId(@Param("pacienteId") Long pacienteId, @Param("dentistaId") Long dentistaId);

    @Query("SELECT p FROM PlanoDental p WHERE LOWER(p.dente) LIKE LOWER(CONCAT('%', :dente, '%'))")
    List<PlanoDental> findByDenteContainingIgnoreCase(@Param("dente") String dente);

    @Query("SELECT p FROM PlanoDental p WHERE p.valorFinal > :valor")
    List<PlanoDental> findByValorFinalGreaterThan(@Param("valor") BigDecimal valor);

    @Query("SELECT p FROM PlanoDental p WHERE p.valorFinal < :valor")
    List<PlanoDental> findByValorFinalLessThan(@Param("valor") BigDecimal valor);

    @Query("SELECT p FROM PlanoDental p WHERE p.valorFinal BETWEEN :valorInicio AND :valorFim")
    List<PlanoDental> findByValorFinalBetween(@Param("valorInicio") BigDecimal valorInicio, @Param("valorFim") BigDecimal valorFim);

    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.dente = :dente")
    List<PlanoDental> findByPacienteIdAndDente(@Param("pacienteId") Long pacienteId, @Param("dente") String dente);

    @Query("SELECT p FROM PlanoDental p WHERE p.status = :status AND p.urgente = true")
    List<PlanoDental> findUrgentesByStatus(@Param("status") StatusPlano status);

    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.ativo = true ORDER BY p.dataPrevista ASC")
    List<PlanoDental> findAtivosByPacienteIdOrderByDataPrevista(@Param("pacienteId") Long pacienteId);

    @Query("SELECT p FROM PlanoDental p WHERE p.dentista.id = :dentistaId AND p.dataPrevista >= :dataInicio AND p.dataPrevista <= :dataFim")
    List<PlanoDental> findByDentistaIdAndDataPrevistaBetween(@Param("dentistaId") Long dentistaId,
                                                             @Param("dataInicio") LocalDateTime dataInicio,
                                                             @Param("dataFim") LocalDateTime dataFim);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.status = :status WHERE p.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") StatusPlano status);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.status = :status, p.dataRealizacao = :dataRealizacao WHERE p.id = :id")
    void concluirPlano(@Param("id") Long id,
                       @Param("status") StatusPlano status,
                       @Param("dataRealizacao") LocalDateTime dataRealizacao);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.status = :status, p.motivoCancelamento = :motivo, p.dataCancelamento = :dataCancelamento WHERE p.id = :id")
    void cancelarPlano(@Param("id") Long id,
                       @Param("status") StatusPlano status,
                       @Param("motivo") String motivo,
                       @Param("dataCancelamento") LocalDateTime dataCancelamento);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.ativo = false WHERE p.id = :id")
    void softDelete(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.valorDesconto = :desconto WHERE p.id = :id")
    void updateDesconto(@Param("id") Long id, @Param("desconto") BigDecimal desconto);

    @Modifying
    @Query("UPDATE PlanoDental p SET p.valor = :valor WHERE p.id = :id")
    void updateValor(@Param("id") Long id, @Param("valor") BigDecimal valor);

    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.ativo = true")
    Long countAtivosByPacienteId(@Param("pacienteId") Long pacienteId);

    @Query("SELECT p FROM PlanoDental p WHERE p.status IN (:statuses) ORDER BY p.dataPrevista ASC")
    List<PlanoDental> findByStatusInOrderByDataPrevista(@Param("statuses") List<StatusPlano> statuses);

    boolean existsByPacienteIdAndDenteAndProcedimentoAndAtivoTrue(Long pacienteId, String dente, String procedimento);

    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.dentista.id = :dentistaId AND p.status = :status")
    List<PlanoDental> findByPacienteIdAndDentistaIdAndStatus(@Param("pacienteId") Long pacienteId,
                                                             @Param("dentistaId") Long dentistaId,
                                                             @Param("status") StatusPlano status);
}
=======
import java.util.List;

@Repository
public interface PlanoDentalRepository extends JpaRepository<PlanoDental, Long> {

    List<PlanoDental> findByPacienteIdAndDeletadoFalse(Long pacienteId);

    List<PlanoDental> findByDentistaIdAndDeletadoFalse(Long dentistaId);

    @Query("SELECT p FROM PlanoDental p WHERE p.pacienteId = :pacienteId AND p.dentistaId = :dentistaId AND p.deletado = false")
    List<PlanoDental> findByPacienteAndDentista(@Param("pacienteId") Long pacienteId, @Param("dentistaId") Long dentistaId);

    List<PlanoDental> findByStatusAndDeletadoFalse(PlanoDental.StatusPlano status);
}
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
