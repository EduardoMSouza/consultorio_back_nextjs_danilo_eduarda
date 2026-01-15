package consultorio.domain.entity.tratamento;

import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.enums.StatusPlano;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um plano de tratamento odontológico para um paciente.
 * Cada plano está associado a um procedimento específico em um dente.
 *
 * @author Sistema Consultório
 * @since 1.0
 */
@Entity
@Table(name = "planos_dentais", indexes = {
        @Index(name = "idx_plano_paciente", columnList = "paciente_id"),
        @Index(name = "idx_plano_dentista", columnList = "dentista_id"),
        @Index(name = "idx_plano_status", columnList = "status"),
        @Index(name = "idx_plano_dente", columnList = "dente"),
        @Index(name = "idx_plano_data_criacao", columnList = "criado_em"),
        @Index(name = "idx_plano_ativo", columnList = "ativo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("ativo = true")
public class PlanoDental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @Column(name = "dente", nullable = false, length = 10)
    private String dente;

    @Column(name = "face_dente", length = 10)
    private String faceDente;

    @Column(name = "procedimento", nullable = false, length = 200)
    private String procedimento;

    @Column(name = "codigo_procedimento", length = 20)
    private String codigoProcedimento;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_desconto", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal valorDesconto = BigDecimal.ZERO;

    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private StatusPlano status = StatusPlano.PENDENTE;

    @Column(name = "prioridade", length = 20)
    @Builder.Default
    private String prioridade = "NORMAL";

    @Column(name = "urgente")
    @Builder.Default
    private Boolean urgente = false;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;

    @Column(name = "data_prevista")
    private LocalDateTime dataPrevista;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    // Relacionamento com evoluções
    @OneToMany(mappedBy = "planoDental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvolucaoTratamento> evolucoes = new ArrayList<>();

    /**
     * Construtor builder para criar plano com dados básicos.
     */
    @Builder
    public PlanoDental(Paciente paciente, Dentista dentista, String dente,
                       String procedimento, BigDecimal valor, String observacoes) {
        this.paciente = paciente;
        this.dentista = dentista;
        this.dente = dente;
        this.procedimento = procedimento;
        this.valor = valor;
        this.observacoes = observacoes;
        this.status = StatusPlano.PENDENTE;
        this.ativo = true;
        this.valorDesconto = BigDecimal.ZERO;
        this.prioridade = "NORMAL";
        this.urgente = false;
        calcularValorFinal();
    }

    /**
     * Marca o plano como concluído.
     */
    public void concluir() {
        this.status = StatusPlano.CONCLUIDO;
        this.dataRealizacao = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Cancela o plano com motivo.
     *
     * @param motivo motivo do cancelamento
     */
    public void cancelar(String motivo) {
        this.status = StatusPlano.CANCELADO;
        this.motivoCancelamento = motivo;
        this.dataCancelamento = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Inicia o plano de tratamento.
     */
    public void iniciar() {
        if (this.status == StatusPlano.PENDENTE) {
            this.status = StatusPlano.EM_ANDAMENTO;
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    /**
     * Calcula o valor final com desconto.
     */
    public void calcularValorFinal() {
        if (this.valor != null) {
            if (this.valorDesconto != null && this.valorDesconto.compareTo(BigDecimal.ZERO) > 0) {
                this.valorFinal = this.valor.subtract(this.valorDesconto);
            } else {
                this.valorFinal = this.valor;
            }

            // Garante que o valor final não seja negativo
            if (this.valorFinal.compareTo(BigDecimal.ZERO) < 0) {
                this.valorFinal = BigDecimal.ZERO;
            }
        }
    }

    /**
     * Desativa o plano (soft delete).
     */
    public void desativar() {
        this.ativo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Reativa o plano.
     */
    public void ativar() {
        this.ativo = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * Verifica se o plano está ativo.
     *
     * @return true se o plano está ativo
     */
    public boolean estaAtivo() {
        return Boolean.TRUE.equals(ativo);
    }

    /**
     * Verifica se o plano está pendente.
     *
     * @return true se o plano está pendente
     */
    public boolean estaPendente() {
        return StatusPlano.PENDENTE.equals(status);
    }

    /**
     * Verifica se o plano está em andamento.
     *
     * @return true se o plano está em andamento
     */
    public boolean estaEmAndamento() {
        return StatusPlano.EM_ANDAMENTO.equals(status);
    }

    /**
     * Verifica se o plano está concluído.
     *
     * @return true se o plano está concluído
     */
    public boolean estaConcluido() {
        return StatusPlano.CONCLUIDO.equals(status);
    }

    /**
     * Verifica se o plano está cancelado.
     *
     * @return true se o plano está cancelado
     */
    public boolean estaCancelado() {
        return StatusPlano.CANCELADO.equals(status);
    }

    /**
     * Aplica desconto ao plano.
     *
     * @param desconto valor do desconto a ser aplicado
     */
    public void aplicarDesconto(BigDecimal desconto) {
        if (desconto != null && desconto.compareTo(BigDecimal.ZERO) >= 0) {
            this.valorDesconto = desconto;
            calcularValorFinal();
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    /**
     * Define o valor do plano e recalcula o valor final.
     *
     * @param valor novo valor do plano
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
        calcularValorFinal();
    }

    /**
     * Define o desconto e recalcula o valor final.
     *
     * @param valorDesconto novo valor de desconto
     */
    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto != null ? valorDesconto : BigDecimal.ZERO;
        calcularValorFinal();
    }

    /**
     * Valida e formata o código do dente.
     *
     * @param dente código do dente
     */
    public void setDente(String dente) {
        if (dente != null) {
            this.dente = dente.trim().toUpperCase();
        }
    }

    /**
     * Formata o dente para exibição.
     *
     * @return dente formatado com face (se existir)
     */
    public String getDenteFormatado() {
        if (dente == null) {
            return "";
        }
        if (faceDente != null && !faceDente.trim().isEmpty()) {
            return dente + " (" + faceDente + ")";
        }
        return dente;
    }

    @PrePersist
    @PreUpdate
    protected void calcularValores() {
        calcularValorFinal();
        if (valorFinal == null && valor != null) {
            valorFinal = valor;
        }
    }
}