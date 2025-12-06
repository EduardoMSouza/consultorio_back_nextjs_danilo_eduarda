package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "evolucao_tratamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvolucaoTratamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDate dataProcedimento;
    private String evolucaoIntercorrenciasTratamento;

    // RELACIONAMENTO COM PACIENTE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
}
