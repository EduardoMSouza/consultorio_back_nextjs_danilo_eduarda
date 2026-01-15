// DadosBasicosRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Número do prontuário é obrigatório")
    @Size(max = 20, message = "Número do prontuário deve ter no máximo 20 caracteres")
    private String prontuarioNumero;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$|^$",
            message = "Telefone inválido. Use o formato (XX) XXXXX-XXXX")
    private String telefone;

    @Size(max = 20, message = "RG deve ter no máximo 20 caracteres")
    private String rg;

    @Size(max = 20, message = "Órgão expedidor deve ter no máximo 20 caracteres")
    private String orgaoExpedidor;

    @Pattern(regexp = "\\d{11}|^$", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Size(max = 50, message = "Naturalidade deve ter no máximo 50 caracteres")
    private String naturalidade;

    @Size(max = 50, message = "Nacionalidade deve ter no máximo 50 caracteres")
    private String nacionalidade;

    @Size(max = 50, message = "Profissão deve ter no máximo 50 caracteres")
    private String profissao;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String enderecoResidencial;

    @Size(max = 100, message = "Indicado por deve ter no máximo 100 caracteres")
    private String indicadoPor;

    @NotNull(message = "Status é obrigatório")
    private Boolean status = true;
}