package consultorio.domain.repository;

import consultorio.domain.entity.PlanoDental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoDentalRepository extends JpaRepository<PlanoDental, Long> {
}
