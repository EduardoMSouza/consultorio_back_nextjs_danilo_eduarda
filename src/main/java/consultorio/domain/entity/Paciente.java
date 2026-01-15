package consultorio.domain.entity;

import consultorio.domain.entity.emededdable.paciente.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Dados básicos do paciente
    @Embedded
    private DadosBasicos dadosBasicos = new DadosBasicos();

    // Responsável pelo tratamento
    @Embedded
    private Responsavel responsavel = new Responsavel();

    // Anamnese (histórico médico)
    @Embedded
    private Anamnese anamnese = new Anamnese();

    // Convênio médico
    @Embedded
    private Convenio convenio = new Convenio();

    // Inspeção bucal
    @Embedded
    private InspecaoBucal inspecaoBucal = new InspecaoBucal();

    // Questionário de saúde
    @Embedded
    private QuestionarioSaude questionarioSaude = new QuestionarioSaude();

    // Timestamps
    @Column(name = "criado_em")
    private LocalDateTime createdAt;

    @Column(name = "atualizado_em")
    private LocalDateTime updatedAt;

    // Enums
    public enum Sexo {
        MASCULINO, FEMININO, OUTRO
    }

    public enum EstadoCivil {
        SOLTEIRO, CASADO, DIVORCIADO, VIUVO, UNIAO_ESTAVEL
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}