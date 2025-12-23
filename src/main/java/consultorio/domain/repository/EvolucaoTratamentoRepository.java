package consultorio.domain.repository;

import consultorio.domain.entity.EvolucaoTratamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvolucaoTratamentoRepository extends JpaRepository<EvolucaoTratamento, Long> {

    List<EvolucaoTratamento> findByPacienteIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long pacienteId);

    List<EvolucaoTratamento> findByDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long dentistaId);

    List<EvolucaoTratamento> findByPacienteIdAndDentistaIdAndDeletadoFalseOrderByDataEvolucaoDesc(Long pacienteId, Long dentistaId);

    List<EvolucaoTratamento> findByDataEvolucaoBetweenAndDeletadoFalse(LocalDate inicio, LocalDate fim);
}
