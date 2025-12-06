package consultorio.domain.repository;

import consultorio.domain.entity.Dentista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Optional<Dentista> findByCro(String cro);

    boolean existsByCro(String cro);

    Page<Dentista> findByAtivo(Boolean ativo, Pageable pageable);

    Page<Dentista> findByEspecialidade(String especialidade, Pageable pageable);

    @Query("SELECT d FROM Dentista d WHERE " +
            "LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(d.especialidade) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Dentista> buscarPorTermo(@Param("termo") String termo, Pageable pageable);
}