package consultorio.api.dto.response.pessoa.embeddable.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspecaoBucalResponse {

    private String lingua;
    private String mucosa;
    private String palato;
    private String labios;
    private String gengivas;
    private String nariz;
    private String face;
    private String ganglios;
    private String glandulasSalivares;
    private Boolean alteracaoOclusao;
    private String alteracaoOclusaoTipo;
    private Boolean protese;
    private String proteseTipo;
    private String outrasObservacoes;
}