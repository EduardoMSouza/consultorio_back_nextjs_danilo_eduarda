// ResponsavelRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsavelRequest {

    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 20, message = "RG deve ter no máximo 20 caracteres")
    private String rg;

    @Size(max = 20, message = "Órgão expedidor deve ter no máximo 20 caracteres")
    private String orgaoExpedidor;

    @Pattern(regexp = "\\d{11}|^$", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @Size(max = 20, message = "Estado civil deve ter no máximo 20 caracteres")
    private String estadoCivil;

    @Size(max = 100, message = "Cônjuge deve ter no máximo 100 caracteres")
    private String conjuge;

    @Size(max = 20, message = "RG do cônjuge deve ter no máximo 20 caracteres")
    private String rgConjuge;

    @Size(max = 20, message = "Órgão expedidor do cônjuge deve ter no máximo 20 caracteres")
    private String orgaoExpedidorConjuge;

    @Pattern(regexp = "\\d{11}|^$", message = "CPF do cônjuge deve conter 11 dígitos numéricos")
    private String cpfConjuge;
}