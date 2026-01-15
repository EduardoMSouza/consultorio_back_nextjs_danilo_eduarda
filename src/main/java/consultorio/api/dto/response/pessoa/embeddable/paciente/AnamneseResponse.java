package consultorio.api.dto.response.pessoa.embeddable.paciente;// AnamneseResponse.java


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnamneseResponse {

    private Boolean febreReumatica;
    private Boolean hepatite;
    private Boolean diabetes;
    private Boolean hipertensaoArterialSistemica;
    private Boolean portadorHiv;
    private Boolean alteracaoCoagulacaoSanguinea;
    private Boolean reacoesAlergicas;
    private Boolean doencasSistemicas;
    private Boolean internacaoRecente;
    private Boolean utilizandoMedicacao;
    private Boolean fumante;
    private String fumanteQuantidade;
    private String tempoFumo;
    private Boolean bebidasAlcoolicas;
    private Boolean problemasCardiacos;
    private Boolean problemasRenais;
    private Boolean problemasGastricos;
    private Boolean problemasRespiratorios;
    private Boolean problemasAlergicos;
    private String problemasAlergicosQuais;
    private Boolean problemasArticularesOuReumatismo;
    private String queixaPrincipal;
    private String evolucaoDoencaAtual;
}