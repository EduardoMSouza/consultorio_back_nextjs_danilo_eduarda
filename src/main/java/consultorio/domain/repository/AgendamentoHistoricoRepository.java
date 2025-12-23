package consultorio.domain.repository;

import consultorio.domain.entity.AgendamentoHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoHistoricoRepository extends JpaRepository<AgendamentoHistorico, Long> {

    List<AgendamentoHistorico> findByAgendamentoIdOrderByDataHoraDesc(Long agendamentoId);
}