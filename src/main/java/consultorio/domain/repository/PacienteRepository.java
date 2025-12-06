package consultorio.domain.repository;

//import consultorio.api.dto.response.PacienteResponse;

import consultorio.domain.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByDadosBasicosProntuarioNumero(String prontuarioNumero);

    Optional<Paciente> findByDadosBasicosCpf(String cpf);

    boolean existsByDadosBasicosProntuarioNumero(String prontuarioNumero);

    boolean existsByDadosBasicosCpf(String cpf);

    Page<Paciente> findByDadosBasicosStatus(Boolean status, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE " +
            "LOWER(p.dadosBasicos.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "p.dadosBasicos.cpf LIKE CONCAT('%', :termo, '%') OR " +
            "p.dadosBasicos.prontuarioNumero LIKE CONCAT('%', :termo, '%')")
    Page<Paciente> buscarPorTermo(@Param("termo") String termo, Pageable pageable);
}