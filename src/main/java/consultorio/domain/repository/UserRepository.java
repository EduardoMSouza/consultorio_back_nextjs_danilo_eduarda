package consultorio.domain.repository;

import consultorio.domain.entity.pessoa.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ==================== BUSCAS PARA LOGIN ====================

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);

    // ==================== VERIFICAÇÕES ====================

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.id <> :id")
    boolean existsByUsernameExcludingId(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailExcludingId(@Param("email") String email, @Param("id") Long id);

    // ==================== LISTAGENS ====================

    Page<User> findByAtivo(Boolean ativo, Pageable pageable);

    Page<User> findByRole(User.Role role, Pageable pageable);

    Page<User> findByRoleAndAtivo(User.Role role, Boolean ativo, Pageable pageable);

    List<User> findByAtivoTrue();

    // ==================== BUSCA TEXTUAL ====================

    @Query("""
        SELECT u FROM User u WHERE
        LOWER(u.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR
        LOWER(u.username) LIKE LOWER(CONCAT('%', :termo, '%')) OR
        LOWER(u.email) LIKE LOWER(CONCAT('%', :termo, '%'))
        """)
    Page<User> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(u) FROM User u WHERE u.ativo = true")
    long countAtivos();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") User.Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.ativo = true")
    long countAtivosByRole(@Param("role") User.Role role);

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    List<Object[]> countPorRole();

    // ==================== UPDATES ====================

    @Modifying
    @Query("UPDATE User u SET u.ultimoLogin = :agora WHERE u.id = :id")
    int registrarLogin(@Param("id") Long id, @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE User u SET u.ativo = :ativo, u.atualizadoEm = :agora WHERE u.id = :id")
    int updateStatus(@Param("id") Long id, @Param("ativo") Boolean ativo, @Param("agora") LocalDateTime agora);

    @Modifying
    @Query("UPDATE User u SET u.password = :senha, u.atualizadoEm = :agora WHERE u.id = :id")
    int updateSenha(@Param("id") Long id, @Param("senha") String senha, @Param("agora") LocalDateTime agora);
}