// ConvenioRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvenioRequest {

    @jakarta.validation.constraints.Size(max = 100, message = "Nome do convênio deve ter no máximo 100 caracteres")
    private String nomeConvenio;

    @jakarta.validation.constraints.Size(max = 50, message = "Número de inscrição deve ter no máximo 50 caracteres")
    private String numeroInscricao;
}