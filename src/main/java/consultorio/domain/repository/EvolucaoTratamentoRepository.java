package consultorio.domain.repository;

<<<<<<< HEAD
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.entity.tratamento.enums.TipoEvolucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
=======
import consultorio.domain.entity.EvolucaoTratamento;
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
<<<<<<< HEAD
public interface EvolucaoTratamentoRepository extends JpaRepository<EvolucaoTratamento, Long>, JpaSpecificationExecutor<EvolucaoTratamento> {

    List<EvolucaoTratamento> findByPacienteId(Long pacienteId);
    Page<EvolucaoTratamento> findByPacienteId(Long pacienteId, Pageable pageable);

    List<EvolucaoTratamento> findByDentistaId(Long dentistaId);
    Page<EvolucaoTratamento> findByDentistaId(Long dentistaId, Pageable pageable);

    List<EvolucaoTratamento> findByPlanoDentalId(Long planoDentalId);
    Page<EvolucaoTratamento> findByPlanoDentalId(Long planoDentalId, Pageable pageable);

    List<EvolucaoTratamento> findByTipoEvolucao(TipoEvolucao tipoEvolucao);
    Page<EvolucaoTratamento> findByTipoEvolucao(TipoEvolucao tipoEvolucao, Pageable pageable);

    List<EvolucaoTratamento> findByUrgente(Boolean urgente);
    Page<EvolucaoTratamento> findByUrgente(Boolean urgente, Pageable pageable);

    List<EvolucaoTratamento> findByAtivo(Boolean ativo);
    Page<EvolucaoTratamento> findByAtivo(Boolean ativo, Pageable pageable);

    List<EvolucaoTratamento> findByDataEvolucaoBetween(LocalDate inicio, LocalDate fim);
    Page<EvolucaoTratamento> findByDataEvolucaoBetween(LocalDate inicio, LocalDate fim, Pageable pageable);

    List<EvolucaoTratamento> findByDataEvolucao(LocalDate dataEvolucao);
    List<EvolucaoTratamento> findByDataEvolucaoAfter(LocalDate dataEvolucao);
    List<EvolucaoTratamento> findByDataEvolucaoBefore(LocalDate dataEvolucao);

    List<EvolucaoTratamento> findByProximaConsultaBetween(LocalDate inicio, LocalDate fim);
    List<EvolucaoTratamento> findByProximaConsulta(LocalDate proximaConsulta);
    List<EvolucaoTratamento> findByProximaConsultaAfter(LocalDate data);
    List<EvolucaoTratamento> findByProximaConsultaBefore(LocalDate data);

    List<EvolucaoTratamento> findByRetornoNecessario(Boolean retornoNecessario);

    List<EvolucaoTratamento> findByPacienteIdAndTipoEvolucao(Long pacienteId, TipoEvolucao tipoEvolucao);
    List<EvolucaoTratamento> findByDentistaIdAndTipoEvolucao(Long dentistaId, TipoEvolucao tipoEvolucao);

    List<EvolucaoTratamento> findByPacienteIdAndDataEvolucaoBetween(Long pacienteId, LocalDate inicio, LocalDate fim);
    List<EvolucaoTratamento> findByDentistaIdAndDataEvolucaoBetween(Long dentistaId, LocalDate inicio, LocalDate fim);

    List<EvolucaoTratamento> findByPacienteIdAndUrgente(Long pacienteId, Boolean urgente);
    List<EvolucaoTratamento> findByDentistaIdAndUrgente(Long dentistaId, Boolean urgente);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.planoDental.id = :planoDentalId ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByPlanoDentalIdOrderByDataEvolucaoDesc(@Param("planoDentalId") Long planoDentalId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByPacienteIdOrderByDataEvolucaoDesc(@Param("pacienteId") Long pacienteId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.dentista.id = :dentistaId ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByDentistaIdOrderByDataEvolucaoDesc(@Param("dentistaId") Long dentistaId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.tipoEvolucao = :tipoEvolucao ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByPacienteIdAndTipoEvolucaoOrderByDataEvolucaoDesc(@Param("pacienteId") Long pacienteId, @Param("tipoEvolucao") TipoEvolucao tipoEvolucao);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.dentista.id = :dentistaId AND e.tipoEvolucao = :tipoEvolucao ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByDentistaIdAndTipoEvolucaoOrderByDataEvolucaoDesc(@Param("dentistaId") Long dentistaId, @Param("tipoEvolucao") TipoEvolucao tipoEvolucao);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.planoDental.id = :planoDentalId AND e.ativo = true ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findAtivasByPlanoDentalIdOrderByDataEvolucaoDesc(@Param("planoDentalId") Long planoDentalId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.ativo = true ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findAtivasByPacienteIdOrderByDataEvolucaoDesc(@Param("pacienteId") Long pacienteId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.retornoNecessario = true AND e.proximaConsulta <= :dataLimite")
    List<EvolucaoTratamento> findRetornosPendentesAte(@Param("dataLimite") LocalDate dataLimite);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.retornoNecessario = true AND e.proximaConsulta < CURRENT_DATE")
    List<EvolucaoTratamento> findRetornosAtrasados();

    @Query("SELECT COUNT(e) FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.ativo = true")
    Long countAtivasByPacienteId(@Param("pacienteId") Long pacienteId);

    @Query("SELECT COUNT(e) FROM EvolucaoTratamento e WHERE e.dentista.id = :dentistaId AND e.ativo = true")
    Long countAtivasByDentistaId(@Param("dentistaId") Long dentistaId);

    @Query("SELECT COUNT(e) FROM EvolucaoTratamento e WHERE e.planoDental.id = :planoDentalId AND e.ativo = true")
    Long countAtivasByPlanoDentalId(@Param("planoDentalId") Long planoDentalId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.dentista.id = :dentistaId ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByPacienteIdAndDentistaIdOrderByDataEvolucaoDesc(@Param("pacienteId") Long pacienteId, @Param("dentistaId") Long dentistaId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.tipoEvolucao IN :tipos ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByPacienteIdAndTipoEvolucaoIn(@Param("pacienteId") Long pacienteId, @Param("tipos") List<TipoEvolucao> tipos);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.dentista.id = :dentistaId AND e.tipoEvolucao IN :tipos ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByDentistaIdAndTipoEvolucaoIn(@Param("dentistaId") Long dentistaId, @Param("tipos") List<TipoEvolucao> tipos);

    boolean existsByPlanoDentalIdAndAtivoTrue(Long planoDentalId);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.descricao LIKE %:termo% OR e.observacoes LIKE %:termo% OR e.procedimentosRealizados LIKE %:termo%")
    List<EvolucaoTratamento> findByTermo(@Param("termo") String termo);

    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND (e.descricao LIKE %:termo% OR e.observacoes LIKE %:termo%)")
    List<EvolucaoTratamento> findByPacienteIdAndTermo(@Param("pacienteId") Long pacienteId, @Param("termo") String termo);
}
=======
public interface EvolucaoTratamentoRepository extends JpaRepository<EvolucaoTratamento, Long> {

    List<EvolucaoTratamento> findByPacienteIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long pacienteId);

    List<EvolucaoTratamento> findByDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long dentistaId);

    List<EvolucaoTratamento> findByPacienteIdAndDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long pacienteId, Long dentistaId);

    List<EvolucaoTratamento> findByDataEvolucaoBetweenAndDeletadoFalse(LocalDate inicio, LocalDate fim);
}
>>>>>>> aac8f9c1ddb79fb2c76c9249edd60166d1195cfb
