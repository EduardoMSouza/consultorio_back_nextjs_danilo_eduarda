package consultorio.domain.repository.tratamento;

import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvolucaoTratamentoRepository extends JpaRepository<EvolucaoTratamento, Long> {

    // Buscar por paciente ordenado por data decrescente
    List<EvolucaoTratamento> findByPacienteIdOrderByDataDesc(Long pacienteId);

    Page<EvolucaoTratamento> findByPacienteIdOrderByDataDesc(Long pacienteId, Pageable pageable);

    // Buscar por dentista ordenado por data decrescente
    List<EvolucaoTratamento> findByDentistaIdOrderByDataDesc(Long dentistaId);

    Page<EvolucaoTratamento> findByDentistaIdOrderByDataDesc(Long dentistaId, Pageable pageable);

    // Buscar por data específica ordenado por nome do paciente
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.data = :data ORDER BY e.paciente.dadosBasicos.nome ASC")
    List<EvolucaoTratamento> findByDataOrderByPacienteNomeAsc(@Param("data") LocalDate data);

    // Buscar por período
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.data DESC, e.paciente.dadosBasicos.nome ASC")
    List<EvolucaoTratamento> findByPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                           @Param("dataFim") LocalDate dataFim);

    // Buscar por paciente e período
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId AND e.data BETWEEN :dataInicio AND :dataFim ORDER BY e.data DESC")
    List<EvolucaoTratamento> findByPacienteAndPeriodo(@Param("pacienteId") Long pacienteId,
                                                      @Param("dataInicio") LocalDate dataInicio,
                                                      @Param("dataFim") LocalDate dataFim);

    // Buscar última evolução de um paciente
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId ORDER BY e.data DESC, e.id DESC")
    Optional<EvolucaoTratamento> findUltimaEvolucaoPaciente(@Param("pacienteId") Long pacienteId);

    // Buscar evoluções com texto específico (busca textual)
    @Query("SELECT e FROM EvolucaoTratamento e WHERE LOWER(e.evolucaoEIntercorrencias) LIKE LOWER(CONCAT('%', :texto, '%')) ORDER BY e.data DESC")
    List<EvolucaoTratamento> findByTextoEvolucao(@Param("texto") String texto);

    // Buscar por paciente e data específica
    Optional<EvolucaoTratamento> findByPacienteIdAndData(@Param("pacienteId") Long pacienteId,
                                                         @Param("data") LocalDate data);

    // Buscar evoluções do dia atual
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.data = CURRENT_DATE ORDER BY e.paciente.dadosBasicos.nome ASC")
    List<EvolucaoTratamento> findEvolucoesDoDia();

    // Contar evoluções por paciente
    @Query("SELECT COUNT(e) FROM EvolucaoTratamento e WHERE e.paciente.id = :pacienteId")
    Long countByPacienteId(@Param("pacienteId") Long pacienteId);

    // Buscar evoluções com limite de resultados
    @Query("SELECT e FROM EvolucaoTratamento e ORDER BY e.data DESC, e.paciente.dadosBasicos.nome ASC")
    Page<EvolucaoTratamento> findAllComPaginacao(Pageable pageable);

    // Verificar se existe evolução para paciente na data
    boolean existsByPacienteIdAndData(Long pacienteId, LocalDate data);

    // Buscar evoluções por dentista e data
    @Query("SELECT e FROM EvolucaoTratamento e WHERE e.dentista.id = :dentistaId AND e.data = :data ORDER BY e.paciente.dadosBasicos.nome ASC")
    List<EvolucaoTratamento> findByDentistaIdAndData(@Param("dentistaId") Long dentistaId,
                                                     @Param("data") LocalDate data);
}