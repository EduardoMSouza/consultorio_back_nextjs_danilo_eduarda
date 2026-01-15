// PacienteResumoResponse.java
package consultorio.api.dto.response.pessoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResumoResponse {

    private Long id;
    private String prontuarioNumero;
    private String nome;
    private String telefone;
    private String cpf;
    private String cpfFormatado;
    private LocalDate dataNascimento;
    private Integer idade;
    private String convenio;
    private String numeroInscricaoConvenio;
    private Boolean status;
    private Boolean ativo;

    public String getCpfFormatado() {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}