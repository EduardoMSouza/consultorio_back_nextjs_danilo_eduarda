package consultorio.domain.repository;

import consultorio.domain.entity.PlanoDental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoDentalRepository extends JpaRepository<PlanoDental, Long> {

    List<PlanoDental> findByPacienteIdAndDeletadoFalse(Long pacienteId);

    List<PlanoDental> findByDentistaIdAndDeletadoFalse(Long dentistaId);

    @Query("SELECT p FROM PlanoDental p WHERE p.pacienteId = :pacienteId AND p.dentistaId = :dentistaId AND p.deletado = false")
    List<PlanoDental> findByPacienteAndDentista(@Param("pacienteId") Long pacienteId, @Param("dentistaId") Long dentistaId);

    List<PlanoDental> findByStatusAndDeletadoFalse(PlanoDental.StatusPlano status);
}
