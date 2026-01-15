// InspecaoBucalRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspecaoBucalRequest {

    @Size(max = 200, message = "Língua deve ter no máximo 200 caracteres")
    private String lingua;

    @Size(max = 200, message = "Mucosa deve ter no máximo 200 caracteres")
    private String mucosa;

    @Size(max = 200, message = "Palato deve ter no máximo 200 caracteres")
    private String palato;

    @Size(max = 200, message = "Lábios deve ter no máximo 200 caracteres")
    private String labios;

    @Size(max = 200, message = "Gengivas deve ter no máximo 200 caracteres")
    private String gengivas;

    @Size(max = 200, message = "Nariz deve ter no máximo 200 caracteres")
    private String nariz;

    @Size(max = 200, message = "Face deve ter no máximo 200 caracteres")
    private String face;

    @Size(max = 200, message = "Gânglios deve ter no máximo 200 caracteres")
    private String ganglios;

    @Size(max = 200, message = "Glândulas salivares deve ter no máximo 200 caracteres")
    private String glandulasSalivares;

    private Boolean alteracaoOclusao = false;

    @Size(max = 100, message = "Tipo de alteração oclusal deve ter no máximo 100 caracteres")
    private String alteracaoOclusaoTipo;

    private Boolean protese = false;

    @Size(max = 100, message = "Tipo de prótese deve ter no máximo 100 caracteres")
    private String proteseTipo;

    @Size(max = 500, message = "Outras observações deve ter no máximo 500 caracteres")
    private String outrasObservacoes;
}