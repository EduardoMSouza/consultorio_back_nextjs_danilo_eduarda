package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_procedimento")
    private TipoProcedimento tipoProcedimento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "valor_consulta")
    private Double valorConsulta;

    @Column(name = "criado_em")
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em")
    private LocalDateTime updatedAt;

    // Enums
    public enum StatusAgendamento {
        AGENDADO,
        CONFIRMADO,
        EM_ATENDIMENTO,
        CONCLUIDO,
        CANCELADO,
        FALTOU
    }

    public enum TipoProcedimento {
        CONSULTA,
        LIMPEZA,
        RESTAURACAO,
        EXTRACAO,
        CANAL,
        CLAREAMENTO,
        ORTODONTIA,
        IMPLANTE,
        PROTESE,
        AVALIACAO,
        RETORNO,
        EMERGENCIA,
        OUTROS
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = StatusAgendamento.AGENDADO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Métodos auxiliares
    public String getNomePaciente() {
        return paciente != null ? paciente.getNome() : null;
    }

    public String getNomeDentista() {
        return dentista != null ? dentista.getNome() : null;
    }

    public boolean isConflitante(LocalTime outroInicio, LocalTime outroFim) {
        return !(horaFim.compareTo(outroInicio) <= 0 || horaInicio.compareTo(outroFim) >= 0);
    }
}