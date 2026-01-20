package consultorio.domain.entity.pessoa;

import consultorio.domain.entity.pessoa.embedded.paciente.*;
import consultorio.domain.entity.tratamento.EvolucaoTratamento;
import consultorio.domain.entity.tratamento.PlanoDental;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um paciente do consultório odontológico.
 * Contém todas as informações pessoais, médicas e odontológicas do paciente.
 *
 * @author Sistema Consultório
 * @since 1.0
 */
@Entity
@Table(name = "pacientes", indexes = {
        @Index(name = "idx_paciente_prontuario", columnList = "prontuario_numero", unique = true),
        @Index(name = "idx_paciente_cpf", columnList = "cpf", unique = true),
        @Index(name = "idx_paciente_telefone", columnList = "telefone_paciente"),
        @Index(name = "idx_paciente_nome", columnList = "nome_paciente"),
        @Index(name = "idx_paciente_status", columnList = "status_paciente"),
        @Index(name = "idx_paciente_ativo", columnList = "ativo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("ativo = true")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    // Dados básicos do paciente
    @Embedded
    private DadosBasicos dadosBasicos = new DadosBasicos();

    // Responsável pelo tratamento (para menores ou dependentes)
    @Embedded
    private Responsavel responsavel = new Responsavel();

    // Anamnese (histórico médico)
    @Embedded
    private Anamnese anamnese = new Anamnese();

    // Convênio médico
    @Embedded
    private Convenio convenio = new Convenio();

    // Inspeção bucal inicial
    @Embedded
    private InspecaoBucal inspecaoBucal = new InspecaoBucal();

    // Questionário de saúde
    @Embedded
    private QuestionarioSaude questionarioSaude = new QuestionarioSaude();

    // Status e controle
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Timestamps
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    // Relacionamentos
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanoDental> planosDentais = new ArrayList<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvolucaoTratamento> evolucoesTratamento = new ArrayList<>();

    /**
     * Desativa o paciente no sistema (soft delete).
     */
    public void desativar() {
        this.ativo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Reativa o paciente no sistema.
     */
    public void ativar() {
        this.ativo = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Verifica se o paciente está ativo no sistema.
     *
     * @return true se o paciente está ativo
     */
    public boolean estaAtivo() {
        return Boolean.TRUE.equals(ativo);
    }

    /**
     * Calcula a idade do paciente.
     *
     * @return idade em anos
     */
    public int getIdade() {
        if (dadosBasicos.getDataNascimento() == null) {
            return 0;
        }
        return java.time.Period.between(
                dadosBasicos.getDataNascimento(),
                LocalDate.now()
        ).getYears();
    }

    /**
     * Verifica se o paciente possui convênio cadastrado.
     *
     * @return true se possui convênio
     */
    public boolean possuiConvenio() {
        return convenio != null &&
                convenio.getNomeConvenio() != null &&
                !convenio.getNomeConvenio().trim().isEmpty();
    }

    /**
     * Formata o CPF para exibição.
     *
     * @return CPF formatado (xxx.xxx.xxx-xx)
     */
    public String getCpfFormatado() {
        String cpf = dadosBasicos.getCpf();
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    /**
     * Define o CPF removendo caracteres não numéricos.
     *
     * @param cpf CPF a ser formatado
     */
    public void setCpf(String cpf) {
        if (cpf != null) {
            dadosBasicos.setCpf(cpf.replaceAll("[^0-9]", ""));
        }
    }

    /**
     * Define o nome removendo espaços extras.
     *
     * @param nome nome do paciente
     */
    public void setNome(String nome) {
        if (nome != null) {
            dadosBasicos.setNome(nome.trim());
        }
    }

    /**
     * Método auxiliar para obter o nome do paciente
     */
    public String getNome() {
        return dadosBasicos != null ? dadosBasicos.getNome() : null;
    }

    /**
     * Método auxiliar para obter o CPF do paciente
     */
    public String getCpf() {
        return dadosBasicos != null ? dadosBasicos.getCpf() : null;
    }

    /**
     * Método auxiliar para obter o telefone do paciente
     */
    public String getTelefone() {
        return dadosBasicos != null ? dadosBasicos.getTelefone() : null;
    }

    /**
     * Método auxiliar para obter a data de nascimento
     */
    public LocalDate getDataNascimento() {
        return dadosBasicos != null ? dadosBasicos.getDataNascimento() : null;
    }

    /**
     * Método auxiliar para obter o prontuário
     */
    public String getProntuarioNumero() {
        return dadosBasicos != null ? dadosBasicos.getProntuarioNumero() : null;
    }
}