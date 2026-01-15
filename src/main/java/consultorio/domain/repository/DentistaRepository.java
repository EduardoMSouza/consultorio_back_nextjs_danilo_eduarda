package consultorio.domain.repository;

import consultorio.domain.entity.pessoa.Dentista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Optional<Dentista> findByEmail(String email);

    Optional<Dentista> findByCro(String cro);

    boolean existsByEmail(String email);

    boolean existsByCro(String cro);

    @Query("SELECT d FROM Dentista d WHERE d.ativo = true")
    Page<Dentista> findAllAtivos(Pageable pageable);

    Page<Dentista> findByAtivoTrue(Pageable pageable);

    @Query("SELECT d FROM Dentista d WHERE LOWER(d.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Dentista> findByNomeContaining(String nome, Pageable pageable);
}