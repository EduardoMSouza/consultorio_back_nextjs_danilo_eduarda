package consultorio.domain.entity.tratamento;

import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import consultorio.domain.entity.tratamento.enums.TipoEvolucao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "evolucoes_tratamento", indexes = {
        @Index(name = "idx_evolucao_paciente", columnList = "paciente_id"),
        @Index(name = "idx_evolucao_dentista", columnList = "dentista_id"),
        @Index(name = "idx_evolucao_data", columnList = "data_evolucao"),
        @Index(name = "idx_evolucao_tipo", columnList = "tipo_evolucao"),
        @Index(name = "idx_evolucao_plano", columnList = "plano_dental_id"),
        @Index(name = "idx_evolucao_urgente", columnList = "urgente"),
        @Index(name = "idx_evolucao_ativo", columnList = "ativo")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("ativo = true")
public class EvolucaoTratamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evolucao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_dental_id")
    private PlanoDental planoDental;

    @Column(name = "data_evolucao", nullable = false)
    private LocalDate dataEvolucao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evolucao", nullable = false, length = 50)
    @Builder.Default
    private TipoEvolucao tipoEvolucao = TipoEvolucao.CONSULTA_ROTINA;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "procedimentos_realizados", columnDefinition = "TEXT")
    private String procedimentosRealizados;

    @Column(name = "materiais_utilizados", columnDefinition = "TEXT")
    private String materiaisUtilizados;

    @Column(name = "medicamentos_prescritos", columnDefinition = "TEXT")
    private String medicamentosPrescritos;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "recomendacoes", columnDefinition = "TEXT")
    private String recomendacoes;

    @Column(name = "dores_queixas", columnDefinition = "TEXT")
    private String doresQueixas;

    @Column(name = "proxima_consulta")
    private LocalDate proximaConsulta;

    @Column(name = "tempo_consulta_minutos")
    private Integer tempoConsultaMinutos;

    @Column(name = "urgente")
    @Builder.Default
    private Boolean urgente = false;

    @Column(name = "retorno_necessario")
    @Builder.Default
    private Boolean retornoNecessario = false;

    @Column(name = "assinatura_dentista", length = 100)
    private String assinaturaDentista;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Builder
    public EvolucaoTratamento(Paciente paciente, Dentista dentista, PlanoDental planoDental,
                              LocalDate dataEvolucao, TipoEvolucao tipoEvolucao, String titulo,
                              String descricao, String observacoes) {
        this.paciente = paciente;
        this.dentista = dentista;
        this.planoDental = planoDental;
        this.dataEvolucao = dataEvolucao != null ? dataEvolucao : LocalDate.now();
        this.tipoEvolucao = tipoEvolucao != null ? tipoEvolucao : TipoEvolucao.CONSULTA_ROTINA;
        this.titulo = titulo != null ? titulo : ("Evolução de " + this.dataEvolucao);
        this.descricao = descricao;
        this.observacoes = observacoes;
        this.ativo = true;
        this.urgente = false;
        this.retornoNecessario = false;
    }

    public void marcarComoUrgente() {
        this.urgente = true;
        this.tipoEvolucao = TipoEvolucao.CONSULTA_URGENCIA;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void agendarRetorno(LocalDate dataRetorno, String motivo) {
        this.proximaConsulta = dataRetorno;
        this.retornoNecessario = true;
        if (this.observacoes == null) {
            this.observacoes = "";
        }
        this.observacoes += (this.observacoes.isEmpty() ? "" : "\n") +
                "Retorno agendado para " + dataRetorno + ": " + motivo;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void adicionarProcedimento(String procedimento) {
        if (procedimento != null && !procedimento.trim().isEmpty()) {
            if (this.procedimentosRealizados == null || this.procedimentosRealizados.isEmpty()) {
                this.procedimentosRealizados = procedimento.trim();
            } else {
                this.procedimentosRealizados += "\n• " + procedimento.trim();
            }
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void adicionarMedicamento(String medicamento) {
        if (medicamento != null && !medicamento.trim().isEmpty()) {
            if (this.medicamentosPrescritos == null || this.medicamentosPrescritos.isEmpty()) {
                this.medicamentosPrescritos = medicamento.trim();
            } else {
                this.medicamentosPrescritos += "\n• " + medicamento.trim();
            }
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void adicionarMaterial(String material) {
        if (material != null && !material.trim().isEmpty()) {
            if (this.materiaisUtilizados == null || this.materiaisUtilizados.isEmpty()) {
                this.materiaisUtilizados = material.trim();
            } else {
                this.materiaisUtilizados += "\n• " + material.trim();
            }
            this.atualizadoEm = LocalDateTime.now();
        }
    }

    public void desativar() {
        this.ativo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void ativar() {
        this.ativo = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean estaAtiva() {
        return Boolean.TRUE.equals(ativo);
    }

    public boolean eUrgente() {
        return Boolean.TRUE.equals(urgente) ||
                TipoEvolucao.CONSULTA_URGENCIA.equals(tipoEvolucao);
    }

    public boolean temRetornoAgendado() {
        return Boolean.TRUE.equals(retornoNecessario) && proximaConsulta != null;
    }

    public boolean retornoAtrasado() {
        return temRetornoAgendado() && proximaConsulta.isBefore(LocalDate.now());
    }

    public String getTipoEvolucaoDescricao() {
        return tipoEvolucao.getDescricao();
    }

    public String getProximaConsultaFormatada() {
        if (proximaConsulta == null) {
            return "";
        }
        return proximaConsulta.toString();
    }

    public void setTitulo(String titulo) {
        if (titulo != null) {
            this.titulo = titulo.trim();
        }
    }

    public void setDescricao(String descricao) {
        if (descricao != null) {
            this.descricao = descricao.trim();
        }
    }

    @PrePersist
    protected void prePersist() {
        if (dataEvolucao == null) {
            dataEvolucao = LocalDate.now();
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            titulo = "Evolução de " + dataEvolucao;
        }
        if (tipoEvolucao == null) {
            tipoEvolucao = TipoEvolucao.CONSULTA_ROTINA;
        }
    }
}