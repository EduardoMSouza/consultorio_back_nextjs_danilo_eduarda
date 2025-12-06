package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plano_dental")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoDental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String dente;
    private String procedimento;
    private Double valor;

    // RELACIONAMENTO COM PACIENTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
}