package consultorio.domain.repository;

import consultorio.domain.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    // ==================== BUSCAS DIRETAS (ÍNDICES) ====================

    Optional<Paciente> findByDadosBasicosProntuarioNumero(String prontuarioNumero);

    Optional<Paciente> findByDadosBasicosCpf(String cpf);

    Optional<Paciente> findByDadosBasicosEmail(String email);

    // ==================== VERIFICAÇÕES EXISTÊNCIA ====================

    boolean existsByDadosBasicosProntuarioNumero(String prontuarioNumero);

    boolean existsByDadosBasicosCpf(String cpf);

    boolean existsByDadosBasicosEmail(String email);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Paciente p WHERE p.dadosBasicos.cpf = :cpf AND p.id <> :id")
    boolean existsByCpfExcludingId(@Param("cpf") String cpf, @Param("id") Long id);

    // ==================== LISTAGENS COM PAGINAÇÃO ====================

    Page<Paciente> findByDadosBasicosStatus(Boolean status, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.convenio.nome IS NOT NULL AND p.convenio.nome <> ''")
    Page<Paciente> findAllComConvenio(Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.convenio.nome = :nomeConvenio")
    Page<Paciente> findByConvenio(@Param("nomeConvenio") String nomeConvenio, Pageable pageable);

    // ==================== BUSCA TEXTUAL OTIMIZADA ====================

    @Query("""
        SELECT p FROM Paciente p WHERE
        LOWER(p.dadosBasicos.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR
        p.dadosBasicos.cpf LIKE CONCAT('%', :termo, '%') OR
        p.dadosBasicos.prontuarioNumero LIKE CONCAT('%', :termo, '%') OR
        p.dadosBasicos.telefone LIKE CONCAT('%', :termo, '%')
        """)
    Page<Paciente> buscarPorTermo(@Param("termo") String termo, Pageable pageable);

    @Query("""
        SELECT p FROM Paciente p WHERE
        p.dadosBasicos.status = :status AND (
            LOWER(p.dadosBasicos.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR
            p.dadosBasicos.cpf LIKE CONCAT('%', :termo, '%') OR
            p.dadosBasicos.prontuarioNumero LIKE CONCAT('%', :termo, '%')
        )
        """)
    Page<Paciente> buscarPorTermoEStatus(@Param("termo") String termo, @Param("status") Boolean status, Pageable pageable);

    // ==================== PROJEÇÕES PARA LISTAGEM (PERFORMANCE) ====================

    @Query("""
        SELECT new consultorio.api.dto.response.PacienteResumoResponse(
            p.id,
            p.dadosBasicos.prontuarioNumero,
            p.dadosBasicos.nome,
            p.dadosBasicos.telefone,
            p.dadosBasicos.cpf,
            p.dadosBasicos.dataNascimento,
            p.convenio.nome,
            p.dadosBasicos.status
        ) FROM Paciente p
        """)
    Page<PacienteResumoProjection> listarResumo(Pageable pageable);

    @Query("""
        SELECT new consultorio.api.dto.response.PacienteResumoResponse(
            p.id,
            p.dadosBasicos.prontuarioNumero,
            p.dadosBasicos.nome,
            p.dadosBasicos.telefone,
            p.dadosBasicos.cpf,
            p.dadosBasicos.dataNascimento,
            p.convenio.nome,
            p.dadosBasicos.status
        ) FROM Paciente p WHERE p.dadosBasicos.status = :status
        """)
    Page<PacienteResumoProjection> listarResumoPorStatus(@Param("status") Boolean status, Pageable pageable);

    // ==================== INTERFACE PROJECTION (MAIS LEVE QUE DTO) ====================

    interface PacienteResumoProjection {
        Long getId();
        String getProntuarioNumero();
        String getNome();
        String getTelefone();
        String getCpf();
        LocalDate getDataNascimento();
        String getConvenio();
        Boolean getStatus();
    }

    @Query(nativeQuery = true, value = """
        SELECT
            p.id,
            p.prontuario_numero AS prontuarioNumero,
            p.nome_paciente AS nome,
            p.telefone_paciente AS telefone,
            p.cpf,
            p.data_nascimento AS dataNascimento,
            p.convenio_paciente AS convenio,
            p.status_paciente AS status
        FROM pacientes p
        WHERE p.status_paciente = :status
        ORDER BY p.nome_paciente
        """,
            countQuery = "SELECT COUNT(*) FROM pacientes WHERE status_paciente = :status")
    Page<PacienteResumoProjection> listarResumoNativo(@Param("status") Boolean status, Pageable pageable);

    // ==================== BUSCA POR DATA ====================

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.dataNascimento = :data")
    List<Paciente> findAniversariantes(@Param("data") LocalDate data);

    @Query("SELECT p FROM Paciente p WHERE MONTH(p.dadosBasicos.dataNascimento) = :mes AND DAY(p.dadosBasicos.dataNascimento) = :dia")
    List<Paciente> findAniversariantesHoje(@Param("mes") int mes, @Param("dia") int dia);

    @Query("SELECT p FROM Paciente p WHERE MONTH(p.dadosBasicos.dataNascimento) = :mes")
    List<Paciente> findAniversariantesDoMes(@Param("mes") int mes);

    @Query("SELECT p FROM Paciente p WHERE p.createdAt >= :dataInicio AND p.createdAt <= :dataFim")
    List<Paciente> findCadastradosNoPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    // ==================== FILTROS ESPECÍFICOS SAÚDE ====================

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.diabetes = true")
    List<Paciente> findDiabeticos();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.hipertensaoArterialSistemica = true")
    List<Paciente> findHipertensos();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.portadorHiv = true")
    List<Paciente> findPortadoresHiv();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.alteracaoCoagulacaoSanguinea = true")
    List<Paciente> findComAlteracaoCoagulacao();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.reacoesAlergicas = true OR p.questionarioSaude.teveAlergia = true")
    List<Paciente> findAlergicos();

    @Query("SELECT p FROM Paciente p WHERE p.questionarioSaude.gravidez = true")
    List<Paciente> findGestantes();

    @Query("""
        SELECT p FROM Paciente p WHERE
        p.anamnese.problemasCardiacos = true OR
        p.anamnese.problemasRenais = true OR
        p.anamnese.problemasRespiratorios = true OR
        p.anamnese.alteracaoCoagulacaoSanguinea = true
        """)
    List<Paciente> findPacientesRisco();

    // ==================== ESTATÍSTICAS ====================

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.dadosBasicos.status = true")
    long countAtivos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.dadosBasicos.status = false")
    long countInativos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.convenio.nome IS NOT NULL AND p.convenio.nome <> ''")
    long countComConvenio();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.convenio.nome IS NULL OR p.convenio.nome = ''")
    long countParticular();

    @Query("SELECT p.convenio.nome, COUNT(p) FROM Paciente p WHERE p.convenio.nome IS NOT NULL GROUP BY p.convenio.nome ORDER BY COUNT(p) DESC")
    List<Object[]> countPorConvenio();

    @Query("SELECT MONTH(p.createdAt), COUNT(p) FROM Paciente p WHERE YEAR(p.createdAt) = :ano GROUP BY MONTH(p.createdAt) ORDER BY MONTH(p.createdAt)")
    List<Object[]> countCadastrosPorMes(@Param("ano") int ano);

    // ==================== UPDATES EM LOTE ====================

    @Modifying
    @Query("UPDATE Paciente p SET p.dadosBasicos.status = :status WHERE p.id IN :ids")
    int updateStatusEmLote(@Param("ids") List<Long> ids, @Param("status") Boolean status);

    @Modifying
    @Query("UPDATE Paciente p SET p.dadosBasicos.status = false WHERE p.updatedAt < :dataLimite")
    int inativarPacientesSemMovimentacao(@Param("dataLimite") LocalDateTime dataLimite);

    // ==================== AUTOCOMPLETE / TYPEAHEAD ====================

    @Query("""
        SELECT p.id, p.dadosBasicos.nome, p.dadosBasicos.cpf
        FROM Paciente p
        WHERE LOWER(p.dadosBasicos.nome) LIKE LOWER(CONCAT(:prefix, '%'))
        AND p.dadosBasicos.status = true
        ORDER BY p.dadosBasicos.nome
        LIMIT 10
        """)
    List<Object[]> autocompleteNome(@Param("prefix") String prefix);

    @Query(value = """
        SELECT id, nome_paciente, cpf
        FROM pacientes
        WHERE cpf LIKE CONCAT(:prefix, '%')
        AND status_paciente = true
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> autocompleteCpf(@Param("prefix") String prefix);

    // ==================== BUSCA COM FETCH JOIN (EVITA N+1) ====================

    @Query("SELECT DISTINCT p FROM Paciente p WHERE p.id = :id")
    Optional<Paciente> findByIdComDados(@Param("id") Long id);

    // ==================== IDS PARA PROCESSAMENTO EM LOTE ====================

    @Query("SELECT p.id FROM Paciente p WHERE p.dadosBasicos.status = true")
    List<Long> findAllIdsByStatusAtivo();

    @Query("SELECT p.id FROM Paciente p WHERE p.createdAt >= :dataInicio")
    List<Long> findIdsCadastradosApos(@Param("dataInicio") LocalDateTime dataInicio);
}