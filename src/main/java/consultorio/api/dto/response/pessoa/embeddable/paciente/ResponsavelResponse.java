// ResponsavelResponse.java
package consultorio.api.dto.response.pessoa.embeddable.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsavelResponse {

    private String nome;
    private String rg;
    private String orgaoExpedidor;
    private String cpf;
    private String cpfFormatado; // Populado pelo backend com formatação
    private String estadoCivil;
    private String conjuge;
    private String rgConjuge;
    private String orgaoExpedidorConjuge;
    private String cpfConjuge;
    private String cpfConjugeFormatado; // Populado pelo backend com formatação
}