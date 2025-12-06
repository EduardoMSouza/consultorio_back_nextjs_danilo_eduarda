package consultorio.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DentistaResponse {

    private Long id;
    private String nome;
    private String cro;
    private String especialidade;
    private String telefone;
    private String email;
    private Boolean ativo;
}