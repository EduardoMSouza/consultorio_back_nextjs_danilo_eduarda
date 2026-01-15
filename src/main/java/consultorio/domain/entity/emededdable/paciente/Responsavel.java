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
public class Responsavel {

    @Column(name = "nome_responsavel")
    private String nome;

    @Column(name = "rg_responsavel")
    private String rg;

    @Column(name = "orgao_expedidor_responsavel")
    private String orgaoExpedidor;

    @Column(name = "cpf_responsavel")
    private String cpf;

    @Column(name = "estado_civil_responsavel")
    private String estadoCivil;

    @Column(name = "conjuge_responsavel")
    private String conjuge;

    @Column(name = "rg_conjuge")
    private String rgConjuge;

    @Column(name = "orgao_expedidor_conjuge")
    private String orgaoExpedidorConjuge;

    @Column(name = "cpf_conjuge")
    private String cpfConjuge;
}