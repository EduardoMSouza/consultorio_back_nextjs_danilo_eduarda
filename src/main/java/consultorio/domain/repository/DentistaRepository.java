package consultorio.domain.repository;

import consultorio.domain.entity.Dentista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    // ==================== BUSCAS DIRETAS ====================

    Optional<Dentista> findByCro(String cro);

    Optional<Dentista> findByEmail(String email);

    // ==================== VERIFICAÇÕES ====================

    boolean existsByCro(String cro);

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Dentista d WHERE d.cro = :cro AND d.id <> :id")
    boolean existsByCroExcludingId(@Param("cro") String cro, @Param("id") Long id);

    // ==================== LISTAGENS ====================

    Page<Dentista> findByAtivo(Boolean ativo, Pageable pageable);

    Page<Dentista> findByEspecialidade(String especialidade, Pageable pageable);

    Page<Dentista> findByEspecialidadeAndAtivo(String especialidade, Boolean ativo, Pageable pageable);

    List<Dentista> findByAtivoTrue();

    // ==================== BUSCA TEXTUAL ====================

    @Query("""
        SELECT d FROM Dentista d WHERE
        LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR
        LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%')) OR
        LOWER(d.especialidade) LIKE LOWER(CONCAT('%', :termo, '%'))
        """)
    Page<Dentista> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("""
        SELECT d FROM Dentista d WHERE d.ativo = :ativo AND (
            LOWER(d.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR
            LOWER(d.cro) LIKE LOWER(CONCAT('%', :termo, '%')) OR
            LOWER(d.especialidade) LIKE LOWER(CONCAT('%', :termo, '%'))
        )
        """)
    Page<Dentista> buscarPorTermoEStatus(@Param("termo") String termo, @Param("ativo") Boolean ativo, Pageable pageable);

    // ==================== PROJEÇÃO LEVE ====================

    interface DentistaResumoProjection {
        Long getId();
        String getNome();
        String getCro();
        String getEspecialidade();
        Boolean getAtivo();
    }

    @Query("SELECT d.id AS id, d.nome AS nome, d.cro AS cro, d.especialidade AS especialidade, d.ativo AS ativo FROM Dentista d WHERE d.ativo = true")
    List<DentistaResumoProjection> listarAtivosResumo();

    // ==================== DISPONIBILIDADE ====================

    @Query("""
        SELECT d FROM Dentista d WHERE d.ativo = true AND d.id NOT IN (
            SELECT a.dentista.id FROM Agendamento a
            WHERE a.dataConsulta = :data
            AND a.status NOT IN ('CANCELADO', 'FALTOU')
            AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        )
        """)
    List<Dentista> findDisponiveis(@Param("data") LocalDate data, @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    @Query("""
        SELECT CASE WHEN COUNT(a) = 0 THEN true ELSE false END FROM Agendamento a
        WHERE a.dentista.id = :dentistaId
        AND a.dataConsulta = :data
        AND a.status NOT IN ('CANCELADO', 'FALTOU')
        AND ((a.horaInicio < :horaFim AND a.horaFim > :horaInicio))
        """)
    boolean isDisponivel(@Param("dentistaId") Long dentistaId, @Param("data") LocalDate data, @Param("horaInicio") LocalTime horaInicio, @Param("horaFim") LocalTime horaFim);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(d) FROM Dentista d WHERE d.ativo = true")
    long countAtivos();

    @Query("SELECT COUNT(d) FROM Dentista d WHERE d.ativo = false")
    long countInativos();

    @Query("SELECT d.especialidade, COUNT(d) FROM Dentista d WHERE d.especialidade IS NOT NULL GROUP BY d.especialidade ORDER BY COUNT(d) DESC")
    List<Object[]> countPorEspecialidade();

    @Query("SELECT DISTINCT d.especialidade FROM Dentista d WHERE d.especialidade IS NOT NULL AND d.especialidade <> '' ORDER BY d.especialidade")
    List<String> listarEspecialidades();

    // ==================== AGENDA DO DIA ====================

    @Query("""
        SELECT d, COUNT(a) FROM Dentista d
        LEFT JOIN d.agendamentos a ON a.dataConsulta = :data AND a.status NOT IN ('CANCELADO', 'FALTOU')
        WHERE d.ativo = true
        GROUP BY d
        ORDER BY d.nome
        """)
    List<Object[]> listarComQuantidadeAgendamentos(@Param("data") LocalDate data);

    // ==================== UPDATES EM LOTE ====================

    @Modifying
    @Query("UPDATE Dentista d SET d.ativo = :ativo WHERE d.id IN :ids")
    int updateStatusEmLote(@Param("ids") List<Long> ids, @Param("ativo") Boolean ativo);

    // ==================== AUTOCOMPLETE ====================

    @Query("""
        SELECT d.id, d.nome, d.cro, d.especialidade
        FROM Dentista d
        WHERE d.ativo = true AND LOWER(d.nome) LIKE LOWER(CONCAT(:prefix, '%'))
        ORDER BY d.nome
        LIMIT 10
        """)
    List<Object[]> autocompleteNome(@Param("prefix") String prefix);
}