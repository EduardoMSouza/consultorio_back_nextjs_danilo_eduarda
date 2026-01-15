// AnamneseRequest.java
package consultorio.api.dto.request.pessoa.embedded.paciente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnamneseRequest {

    private Boolean febreReumatica = false;
    private Boolean hepatite = false;
    private Boolean diabetes = false;
    private Boolean hipertensaoArterialSistemica = false;
    private Boolean portadorHiv = false;
    private Boolean alteracaoCoagulacaoSanguinea = false;
    private Boolean reacoesAlergicas = false;
    private Boolean doencasSistemicas = false;
    private Boolean internacaoRecente = false;
    private Boolean utilizandoMedicacao = false;
    private Boolean fumante = false;
    private String fumanteQuantidade;
    private String tempoFumo;
    private Boolean bebidasAlcoolicas = false;
    private Boolean problemasCardiacos = false;
    private Boolean problemasRenais = false;
    private Boolean problemasGastricos = false;
    private Boolean problemasRespiratorios = false;
    private Boolean problemasAlergicos = false;
    private String problemasAlergicosQuais;
    private Boolean problemasArticularesOuReumatismo = false;

    @jakarta.validation.constraints.Size(max = 500, message = "Queixa principal deve ter no máximo 500 caracteres")
    private String queixaPrincipal;

    @jakarta.validation.constraints.Size(max = 1000, message = "Evolução da doença atual deve ter no máximo 1000 caracteres")
    private String evolucaoDoencaAtual;
}