package consultorio.domain.repository.tratamento;

import consultorio.domain.entity.tratamento.PlanoDental;
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

    // Buscar por paciente
    List<PlanoDental> findByPacienteIdOrderByCriadoEmDesc(Long pacienteId);
    Page<PlanoDental> findByPacienteIdOrderByCriadoEmDesc(Long pacienteId, Pageable pageable);

    // Buscar por dentista
    List<PlanoDental> findByDentistaIdOrderByCriadoEmDesc(Long dentistaId);
    Page<PlanoDental> findByDentistaIdOrderByCriadoEmDesc(Long dentistaId, Pageable pageable);

    // Buscar por dente
    List<PlanoDental> findByDenteOrderByCriadoEmDesc(String dente);

    // Buscar por procedimento (containing)
    List<PlanoDental> findByProcedimentoContainingIgnoreCaseOrderByCriadoEmDesc(String procedimento);

    // Buscar por período
    @Query("SELECT p FROM PlanoDental p WHERE p.criadoEm BETWEEN :dataInicio AND :dataFim ORDER BY p.criadoEm DESC")
    List<PlanoDental> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio,
                                    @Param("dataFim") LocalDateTime dataFim);

    // Buscar por paciente e período
    @Query("SELECT p FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.criadoEm BETWEEN :dataInicio AND :dataFim ORDER BY p.criadoEm DESC")
    List<PlanoDental> findByPacienteAndPeriodo(@Param("pacienteId") Long pacienteId,
                                               @Param("dataInicio") LocalDateTime dataInicio,
                                               @Param("dataFim") LocalDateTime dataFim);

    // Buscar planos com desconto
    @Query("SELECT p FROM PlanoDental p WHERE p.valor != p.valorFinal")
    List<PlanoDental> findPlanosComDesconto();

    // Buscar planos por valor maior que
    List<PlanoDental> findByValorGreaterThanOrderByValorDesc(BigDecimal valor);

    // Buscar planos por valor final maior que
    List<PlanoDental> findByValorFinalGreaterThanOrderByValorFinalDesc(BigDecimal valorFinal);

    // Somar valor total por paciente
    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.paciente.id = :pacienteId")
    Optional<BigDecimal> somarValorTotalPorPaciente(@Param("pacienteId") Long pacienteId);

    // Somar valor total por dentista
    @Query("SELECT SUM(p.valorFinal) FROM PlanoDental p WHERE p.dentista.id = :dentistaId")
    Optional<BigDecimal> somarValorTotalPorDentista(@Param("dentistaId") Long dentistaId);

    // Contar quantidade de planos por paciente
    @Query("SELECT COUNT(p) FROM PlanoDental p WHERE p.paciente.id = :pacienteId")
    Long countByPacienteId(@Param("pacienteId") Long pacienteId);

    // Buscar planos mais recentes
    @Query("SELECT p FROM PlanoDental p ORDER BY p.criadoEm DESC")
    List<PlanoDental> findPlanosRecentes(Pageable pageable);

    // Verificar se existe plano para o dente do paciente
    @Query("SELECT COUNT(p) > 0 FROM PlanoDental p WHERE p.paciente.id = :pacienteId AND p.dente = :dente")
    boolean existsByPacienteAndDente(@Param("pacienteId") Long pacienteId, @Param("dente") String dente);

    // Buscar top procedimentos mais realizados
    @Query("SELECT p.procedimento, COUNT(p) as quantidade FROM PlanoDental p GROUP BY p.procedimento ORDER BY quantidade DESC")
    List<Object[]> findTopProcedimentos();
}