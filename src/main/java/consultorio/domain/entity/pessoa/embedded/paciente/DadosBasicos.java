package consultorio.domain.entity.pessoa.embedded.paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosBasicos {

    @Column(name = "prontuario_numero", unique = true, nullable = false)
    private String prontuarioNumero;

    @Column(name = "nome_paciente")
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Column(name = "email_paciente")
    private String email;

    @Column(name = "telefone_paciente")
    private String telefone;

    @Column(name = "rg_paciente")
    private String rg;

    @Column(name = "orgao_expedidor")
    private String orgaoExpedidor;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "naturalidade_paciente")
    private String naturalidade;

    @Column(name = "nacionalidade_paciente")
    private String nacionalidade;

    @Column(name = "profissao_paciente")
    private String profissao;

    @Column(name = "endereco_residencial")
    private String enderecoResidencial;

    @Column(name = "indicado_por")
    private String indicadoPor;

    @Column(name = "status_paciente", nullable = false)
    private Boolean status = true;
}
