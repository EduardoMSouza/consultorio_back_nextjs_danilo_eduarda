// PacienteRepository.java
package consultorio.domain.repository.pessoa;

import consultorio.domain.entity.pessoa.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

    // ==================== BUSCAS POR IDENTIFICADORES ÚNICOS ====================

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.prontuarioNumero = :prontuarioNumero")
    Optional<Paciente> findByProntuarioNumero(@Param("prontuarioNumero") String prontuarioNumero);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.cpf = :cpf")
    Optional<Paciente> findByCpf(@Param("cpf") String cpf);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.rg = :rg AND p.dadosBasicos.orgaoExpedidor = :orgaoExpedidor")
    Optional<Paciente> findByRgAndOrgaoExpedidor(@Param("rg") String rg,
                                                 @Param("orgaoExpedidor") String orgaoExpedidor);

    // ==================== BUSCAS POR NOME (Case-insensitive com ILIKE) ====================

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.nome ILIKE %:nome% AND p.ativo = true")
    List<Paciente> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.nome ILIKE %:nome% AND p.ativo = true")
    Page<Paciente> findByNomeContaining(@Param("nome") String nome, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.nome ILIKE :nome% AND p.ativo = true")
    List<Paciente> findByNomeStartingWith(@Param("nome") String nome);

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE p.ativo = true 
        AND unaccent(LOWER(p.dados_basicos->>'nome')) LIKE unaccent(LOWER('%' || :nome || '%'))
        """, nativeQuery = true)
    List<Paciente> findByNomeContainingSemAcento(@Param("nome") String nome);

    // ==================== BUSCAS POR STATUS ====================

    @Query("SELECT p FROM Paciente p WHERE p.ativo = :ativo")
    List<Paciente> findByAtivo(@Param("ativo") boolean ativo);

    @Query("SELECT p FROM Paciente p WHERE p.ativo = :ativo")
    Page<Paciente> findByAtivo(@Param("ativo") boolean ativo, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.status = :status AND p.ativo = true")
    List<Paciente> findByStatus(@Param("status") boolean status);

    // ==================== BUSCAS POR CONVÊNIO ====================

    @Query("SELECT p FROM Paciente p WHERE p.convenio.nomeConvenio = :convenio AND p.ativo = true")
    List<Paciente> findByConvenio(@Param("convenio") String convenio);

    @Query("SELECT p FROM Paciente p WHERE p.convenio.nomeConvenio ILIKE %:convenio% AND p.ativo = true")
    List<Paciente> findByConvenioContaining(@Param("convenio") String convenio);

    @Query("SELECT p FROM Paciente p WHERE p.convenio.nomeConvenio = :convenio AND p.ativo = true")
    Page<Paciente> findByConvenio(@Param("convenio") String convenio, Pageable pageable);

    @Query("SELECT DISTINCT p.convenio.nomeConvenio FROM Paciente p " +
            "WHERE p.convenio.nomeConvenio IS NOT NULL AND p.ativo = true " +
            "ORDER BY p.convenio.nomeConvenio")
    List<String> findDistinctConvenios();

    // ==================== BUSCAS POR DATA ====================

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.dataNascimento BETWEEN :startDate AND :endDate AND p.ativo = true")
    List<Paciente> findByDataNascimentoBetween(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Paciente p WHERE EXTRACT(MONTH FROM p.dadosBasicos.dataNascimento) = :mes AND p.ativo = true")
    List<Paciente> findByMesAniversario(@Param("mes") int mes);

    @Query("SELECT p FROM Paciente p WHERE EXTRACT(DAY FROM p.dadosBasicos.dataNascimento) = :dia " +
            "AND EXTRACT(MONTH FROM p.dadosBasicos.dataNascimento) = :mes AND p.ativo = true")
    List<Paciente> findByDiaMesAniversario(@Param("dia") int dia, @Param("mes") int mes);

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.dataNascimento = :dataNascimento AND p.ativo = true")
    List<Paciente> findByDataNascimento(@Param("dataNascimento") LocalDate dataNascimento);

    // ==================== BUSCAS POR TELEFONE ====================

    @Query("SELECT p FROM Paciente p WHERE p.dadosBasicos.telefone LIKE %:telefone% AND p.ativo = true")
    List<Paciente> findByTelefoneContaining(@Param("telefone") String telefone);

    // ==================== BUSCAS POR DATA DE CADASTRO ====================

    @Query("SELECT p FROM Paciente p WHERE p.criadoEm BETWEEN :dataInicio AND :dataFim")
    List<Paciente> findByDataCadastroBetween(@Param("dataInicio") LocalDateTime dataInicio,
                                             @Param("dataFim") LocalDateTime dataFim);

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE DATE(p.criado_em) = CURRENT_DATE
        """, nativeQuery = true)
    List<Paciente> findCadastradosHoje();

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE DATE(p.criado_em) = CAST(:data AS DATE)
        """, nativeQuery = true)
    List<Paciente> findByDataCadastro(@Param("data") LocalDate data);

    @Query(value = """
        SELECT 
            COALESCE((p.convenio->>'nomeConvenio')::text, 'Sem Convênio') as convenio,
            COUNT(*) as quantidade,
            ROUND((COUNT(*) * 100.0 / NULLIF((SELECT COUNT(*) FROM paciente WHERE ativo = true), 0)), 2) as percentual
        FROM paciente p
        WHERE p.ativo = true
        GROUP BY (p.convenio->>'nomeConvenio')::text
        ORDER BY quantidade DESC
        """, nativeQuery = true)
    List<Object[]> countByConvenio();

    // ==================== CONSULTAS COM PROJEÇÕES (DTO Projections) ====================

    interface PacienteResumoProjection {
        Long getId();
        String getProntuarioNumero();
        String getNome();
        String getTelefone();
        String getCpf();
        LocalDate getDataNascimento();
        String getConvenio();
    }

    @Query("SELECT p.id as id, " +
            "p.dadosBasicos.prontuarioNumero as prontuarioNumero, " +
            "p.dadosBasicos.nome as nome, " +
            "p.dadosBasicos.telefone as telefone, " +
            "p.dadosBasicos.cpf as cpf, " +
            "p.dadosBasicos.dataNascimento as dataNascimento, " +
            "p.convenio.nomeConvenio as convenio " +
            "FROM Paciente p " +
            "WHERE p.ativo = true")
    List<PacienteResumoProjection> findAllResumoProjection();

    @Query("SELECT p.id as id, " +
            "p.dadosBasicos.nome as nome, " +
            "p.dadosBasicos.telefone as telefone, " +
            "p.convenio.nomeConvenio as convenio " +
            "FROM Paciente p " +
            "WHERE p.ativo = true " +
            "ORDER BY p.dadosBasicos.nome")
    Page<PacienteResumoProjection> findResumoProjection(Pageable pageable);

    // ==================== CONSULTAS ESTATÍSTICAS ====================

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.ativo = true")
    Long countAtivos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.ativo = false")
    Long countInativos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.convenio.nomeConvenio IS NOT NULL AND p.ativo = true")
    Long countComConvenio();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.convenio.nomeConvenio IS NULL AND p.ativo = true")
    Long countSemConvenio();

    @Query(value = """
        SELECT 
            CASE 
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) < 18 THEN '0-17'
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) BETWEEN 18 AND 30 THEN '18-30'
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) BETWEEN 31 AND 50 THEN '31-50'
                ELSE '51+'
            END as faixa_etaria,
            COUNT(*) as quantidade
        FROM paciente p
        WHERE p.ativo = true
        GROUP BY 
            CASE 
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) < 18 THEN '0-17'
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) BETWEEN 18 AND 30 THEN '18-30'
                WHEN EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) BETWEEN 31 AND 50 THEN '31-50'
                ELSE '51+'
            END
        ORDER BY faixa_etaria
        """, nativeQuery = true)
    List<Object[]> countByFaixaEtaria();

    @Query(value = """
        SELECT 
            EXTRACT(YEAR FROM p.criado_em) as ano,
            EXTRACT(MONTH FROM p.criado_em) as mes,
            COUNT(*) as quantidade
        FROM paciente p
        WHERE p.ativo = true
        GROUP BY EXTRACT(YEAR FROM p.criado_em), EXTRACT(MONTH FROM p.criado_em)
        ORDER BY ano DESC, mes DESC
        """, nativeQuery = true)
    List<Object[]> countByMesCadastro();

    // ==================== VERIFICAÇÕES DE EXISTÊNCIA ====================

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Paciente p WHERE p.dadosBasicos.prontuarioNumero = :prontuarioNumero")
    boolean existsByProntuarioNumero(@Param("prontuarioNumero") String prontuarioNumero);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Paciente p WHERE p.dadosBasicos.cpf = :cpf AND p.id != :id")
    boolean existsByCpfAndIdNot(@Param("cpf") String cpf, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Paciente p WHERE p.dadosBasicos.cpf = :cpf")
    boolean existsByCpf(@Param("cpf") String cpf);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Paciente p WHERE p.dadosBasicos.rg = :rg AND p.dadosBasicos.orgaoExpedidor = :orgaoExpedidor")
    boolean existsByRgAndOrgaoExpedidor(@Param("rg") String rg, @Param("orgaoExpedidor") String orgaoExpedidor);


    // ==================== BUSCAS COMPLEXAS COM FILTROS ====================

    @Query("SELECT p FROM Paciente p WHERE " +
            "(:nome IS NULL OR p.dadosBasicos.nome ILIKE CONCAT('%', :nome, '%')) AND " +
            "(:cpf IS NULL OR p.dadosBasicos.cpf = :cpf) AND " +
            "(:convenio IS NULL OR p.convenio.nomeConvenio = :convenio) AND " +
            "(:ativo IS NULL OR p.ativo = :ativo) AND " +
            "(:status IS NULL OR p.dadosBasicos.status = :status)")
    Page<Paciente> findWithFilters(@Param("nome") String nome,
                                   @Param("cpf") String cpf,
                                   @Param("convenio") String convenio,
                                   @Param("ativo") Boolean ativo,
                                   @Param("status") Boolean status,
                                   Pageable pageable);

    // ==================== BUSCAS POR IDADE (usando função AGE do PostgreSQL) ====================

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE p.ativo = true 
        AND EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) BETWEEN :idadeMin AND :idadeMax
        """, nativeQuery = true)
    List<Paciente> findByFaixaEtaria(@Param("idadeMin") int idadeMin,
                                     @Param("idadeMax") int idadeMax);

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE p.ativo = true 
        AND EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) >= :idade
        """, nativeQuery = true)
    List<Paciente> findByIdadeMaiorOuIgual(@Param("idade") int idade);

    @Query(value = """
        SELECT p.* FROM paciente p 
        WHERE p.ativo = true 
        AND EXTRACT(YEAR FROM AGE(p.dados_basicos.data_nascimento)) < 18
        """, nativeQuery = true)
    List<Paciente> findMenoresDeIdade();

    // ==================== BUSCAS PARA RELATÓRIOS ====================

    @Query("SELECT p FROM Paciente p WHERE " +
            "p.criadoEm BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY p.criadoEm DESC")
    List<Paciente> findParaRelatorioCadastro(@Param("dataInicio") LocalDateTime dataInicio,
                                             @Param("dataFim") LocalDateTime dataFim);

    @Query(value = """
        SELECT 
            p.dados_basicos->>'naturalidade' as naturalidade,
            COUNT(*) as quantidade
        FROM paciente p
        WHERE p.ativo = true
        AND p.dados_basicos->>'naturalidade' IS NOT NULL
        AND p.dados_basicos->>'naturalidade' != ''
        GROUP BY p.dados_basicos->>'naturalidade'
        ORDER BY quantidade DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTopNaturalidades();

    // ==================== BUSCAS POR CONDIÇÕES DE SAÚDE ====================

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.diabetes = true AND p.ativo = true")
    List<Paciente> findComDiabetes();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.hipertensaoArterialSistemica = true AND p.ativo = true")
    List<Paciente> findComHipertensao();

    @Query("SELECT p FROM Paciente p WHERE p.anamnese.fumante = true AND p.ativo = true")
    List<Paciente> findFumantes();

    // ==================== BUSCAS POR RESPONSÁVEL ====================

    @Query("SELECT p FROM Paciente p WHERE p.responsavel.nome IS NOT NULL AND p.ativo = true")
    List<Paciente> findComResponsavel();

    @Query("SELECT p FROM Paciente p WHERE p.responsavel.nome ILIKE %:nomeResponsavel% AND p.ativo = true")
    List<Paciente> findByNomeResponsavel(@Param("nomeResponsavel") String nomeResponsavel);

    // ==================== BUSCAS POR ANOTAÇÕES ====================

    @Query("SELECT p FROM Paciente p WHERE p.observacoes IS NOT NULL AND p.observacoes <> '' AND p.ativo = true")
    List<Paciente> findComObservacoes();

    @Query("SELECT p FROM Paciente p WHERE p.observacoes ILIKE %:texto% AND p.ativo = true")
    List<Paciente> findByObservacoesContaining(@Param("texto") String texto);

    // ==================== BUSCAS PARA DASHBOARD ====================

    @Query(value = """
        SELECT 
            TO_CHAR(p.criado_em, 'YYYY-MM') as mes,
            COUNT(*) as novos_pacientes
        FROM paciente p
        WHERE p.criado_em >= CURRENT_DATE - INTERVAL '12 months'
        AND p.ativo = true
        GROUP BY TO_CHAR(p.criado_em, 'YYYY-MM')
        ORDER BY TO_CHAR(p.criado_em, 'YYYY-MM')
        """, nativeQuery = true)
    List<Object[]> findNovosPacientesUltimos12Meses();

    // ==================== CONSULTAS ADICIONAIS ESPECÍFICAS PARA POSTGRESQL ====================

    @Query(value = """
        SELECT 
            p.id,
            p.dados_basicos->>'nome' as nome,
            p.dados_basicos->>'telefone' as telefone,
            (p.dados_basicos->>'dataNascimento')::date as data_nascimento,
            (p.convenio->>'nomeConvenio')::text as convenio,
            p.criado_em
        FROM paciente p
        WHERE p.ativo = true
        AND (:nome IS NULL OR p.dados_basicos->>'nome' ILIKE '%' || :nome || '%')
        AND (:convenio IS NULL OR p.convenio->>'nomeConvenio' = :convenio)
        ORDER BY p.dados_basicos->>'nome'
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Object[]> buscarPacientesPaginados(@Param("nome") String nome,
                                            @Param("convenio") String convenio,
                                            @Param("limit") int limit,
                                            @Param("offset") int offset);

    @Query(value = """
        SELECT 
            TO_CHAR(p.dados_basicos.data_nascimento, 'MM') as mes,
            COUNT(*) as aniversariantes
        FROM paciente p
        WHERE p.ativo = true
        GROUP BY TO_CHAR(p.dados_basicos.data_nascimento, 'MM')
        ORDER BY mes
        """, nativeQuery = true)
    List<Object[]> countAniversariantesPorMes();

    @Query(value = """
        SELECT 
            CASE 
                WHEN p.dados_basicos->>'sexo' = 'M' THEN 'Masculino'
                WHEN p.dados_basicos->>'sexo' = 'F' THEN 'Feminino'
                ELSE 'Não informado'
            END as sexo,
            COUNT(*) as quantidade,
            ROUND((COUNT(*) * 100.0 / NULLIF((SELECT COUNT(*) FROM paciente WHERE ativo = true), 0)), 2) as percentual
        FROM paciente p
        WHERE p.ativo = true
        GROUP BY p.dados_basicos->>'sexo'
        """, nativeQuery = true)
    List<Object[]> countBySexo();
}