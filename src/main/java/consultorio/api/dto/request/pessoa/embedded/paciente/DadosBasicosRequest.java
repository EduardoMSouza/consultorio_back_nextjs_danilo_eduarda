package consultorio.api.dto.request.pessoa.embedded.paciente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosBasicosRequest {

    private String prontuarioNumero;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
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
    private Boolean status = true;
}

