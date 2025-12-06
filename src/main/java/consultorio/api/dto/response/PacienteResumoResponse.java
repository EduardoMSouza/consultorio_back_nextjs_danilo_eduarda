package consultorio.api.dto.response;

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
    private LocalDate dataNascimento;
    private String convenio;
    private Boolean status;
}
