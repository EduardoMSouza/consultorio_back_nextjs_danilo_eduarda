package consultorio.domain.repository.tratamento;

import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvolucaoTratamentoRepository extends JpaRepository<EvolucaoTratamento, Long> {

    // Busca por paciente
    List<EvolucaoTratamento> findByPacienteId(Long pacienteId);

    // Busca por dentista
    List<EvolucaoTratamento> findByDentistaId(Long dentistaId);

    // Busca por plano dental
    List<EvolucaoTratamento> findByPlanoDentalId(Long planoDentalId);

    // Busca por data
    List<EvolucaoTratamento> findByDataEvolucao(LocalDate dataEvolucao);

    // Busca por período
    List<EvolucaoTratamento> findByDataEvolucaoBetween(LocalDate inicio, LocalDate fim);

    // Busca por urgência
    List<EvolucaoTratamento> findByUrgenteTrue();

    // Busca por necessidade de retorno
    List<EvolucaoTratamento> findByRetornoNecessarioTrue();

    // Busca por tipo de evolução
    List<EvolucaoTratamento> findByTipoEvolucao(String tipoEvolucao);

    // Busca por paciente e dentista
    List<EvolucaoTratamento> findByPacienteIdAndDentistaId(Long pacienteId, Long dentistaId);

    // Busca por status ativo
    List<EvolucaoTratamento> findByAtivoTrue();

    // Busca por status inativo
    List<EvolucaoTratamento> findByAtivoFalse();

    // Busca evoluções com retorno atrasado
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.retornoNecessario = true AND e.proximaConsulta < :hoje AND e.ativo = true")
    List<EvolucaoTratamento> findRetornosAtrasados(@Param("hoje") LocalDate hoje);

    // Contagem por paciente
    @Query("SELECT COUNT(e) FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.ativo = true")
    Long countByPacienteId(@Param("pacienteId") Long pacienteId);

    // Última evolução de um paciente
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.ativo = true ORDER BY e.dataEvolucao DESC LIMIT 1")
    Optional<EvolucaoTratamento> findUltimaEvolucaoByPacienteId(@Param("pacienteId") Long pacienteId);

    // Busca com filtros múltiplos
    @Query("SELECT e FROM EvolucaoTratamento e WHERE " +
            "(:pacienteId IS NULL OR e.paciente.id = :pacienteId) AND " +
            "(:dentistaId IS NULL OR e.dentista.id = :dentistaId) AND " +
            "(:planoDentalId IS NULL OR e.planoDental.id = :planoDentalId) AND " +
            "(:dataInicio IS NULL OR e.dataEvolucao >= :dataInicio) AND " +
            "(:dataFim IS NULL OR e.dataEvolucao <= :dataFim) AND " +
            "(:tipoEvolucao IS NULL OR e.tipoEvolucao = :tipoEvolucao) AND " +
            "(:urgente IS NULL OR e.urgente = :urgente) AND " +
            "e.ativo = true " +
            "ORDER BY e.dataEvolucao DESC")
    List<EvolucaoTratamento> findByFiltros(
            @Param("pacienteId") Long pacienteId,
            @Param("dentistaId") Long dentistaId,
            @Param("planoDentalId") Long planoDentalId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("tipoEvolucao") String tipoEvolucao,
            @Param("urgente") Boolean urgente
    );
}