package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "evolucao_tratamento", indexes = {
        @Index(name = "idx_evolucao_paciente", columnList = "paciente_id"),
        @Index(name = "idx_evolucao_dentista", columnList = "dentista_id"),
        @Index(name = "idx_evolucao_data", columnList = "data_evolucao")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvolucaoTratamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "dentista_id", nullable = false)
    private Long dentistaId;

    @Column(name = "data_evolucao", nullable = false)
    private LocalDate dataEvolucao;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String procedimentosRealizados;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "proxima_consulta")
    private LocalDate proximaConsulta;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "deletado")
    @Builder.Default
    private Boolean deletado = false;

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
