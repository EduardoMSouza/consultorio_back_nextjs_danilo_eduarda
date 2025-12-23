package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_dental", indexes = {
    @Index(name = "idx_plano_paciente", columnList = "paciente_id"),
    @Index(name = "idx_plano_dentista", columnList = "dentista_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanoDental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "dentista_id", nullable = false)
    private Long dentistaId;

    @Column(nullable = false, length = 10)
    private String dente;

    @Column(nullable = false, length = 200)
    private String procedimento;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StatusPlano status = StatusPlano.PENDENTE;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "deletado")
    @Builder.Default
    private Boolean deletado = false;

    public enum StatusPlano {
        PENDENTE,
        APROVADO,
        EM_ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
