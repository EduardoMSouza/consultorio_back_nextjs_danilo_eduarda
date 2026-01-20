package consultorio.domain.repository.pessoa;

import consultorio.domain.entity.pessoa.Dentista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    // Buscas por campos únicos
    Optional<Dentista> findByEmail(String email);
    Optional<Dentista> findByCro(String cro);

    // Buscas com filtros
    Page<Dentista> findByAtivoTrue(Pageable pageable);
    Page<Dentista> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Dentista> findByEspecialidadeContainingIgnoreCase(String especialidade, Pageable pageable);

    // Verificações de existência
    boolean existsByEmail(String email);
    boolean existsByCro(String cro);
    boolean existsByCroAndIdNot(String cro, Long id);

    // Contagens
    Long countByAtivoTrue();

    // Busca personalizada
    @Query("SELECT d FROM Dentista d WHERE " +
            "LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(d.email) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(d.especialidade) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Dentista> buscarPorTermo(@Param("termo") String termo, Pageable pageable);
}