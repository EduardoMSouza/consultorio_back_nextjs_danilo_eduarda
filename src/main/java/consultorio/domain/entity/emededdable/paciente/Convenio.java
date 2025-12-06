package consultorio.domain.entity.emededdable.paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Convenio {

    @Column(name = "convenio_paciente")
    private String nome;

    @Column(name = "numero_inscricao_convenio")
    private String numeroInscricao;
}