// DadosBasicosResponse.java
package consultorio.api.dto.response.pessoa.embeddable.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosBasicosResponse {

    private String prontuarioNumero;
    private String nome;
    private String telefone;
    private String rg;
    private String orgaoExpedidor;
    private String cpf;
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;
    private String profissao;
    private String enderecoResidencial;
    private String indicadoPor;
    private Boolean status;
}