package consultorio.domain.entity.pessoa.embedded.paciente;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anamnese {

    @Column(name = "febre_reumatica")
    private Boolean febreReumatica;

    @Column(name = "hepatite")
    private Boolean hepatite;

    @Column(name = "diabetes")
    private Boolean diabetes;

    @Column(name = "hipertensao_arterial_sistemica")
    private Boolean hipertensaoArterialSistemica;

    @Column(name = "portador_hiv")
    private Boolean portadorHiv;

    @Column(name = "alteracao_coagulacao_sanguinea")
    private Boolean alteracaoCoagulacaoSanguinea;

    @Column(name = "reacoes_alergicas")
    private Boolean reacoesAlergicas;

    @Column(name = "doencas_sistemicas")
    private Boolean doencasSistemicas;

    @Column(name = "internacao_recente")
    private Boolean internacaoRecente;

    @Column(name = "utilizando_medicacao")
    private Boolean utilizandoMedicacao;

    @Column(name = "fumante")
    private Boolean fumante;

    @Column(name = "fumante_quantidade")
    private String fumanteQuantidade;

    @Column(name = "tempo_fumo")
    private String tempoFumo;

    @Column(name = "bebidas_alcoolicas")
    private Boolean bebidasAlcoolicas;

    @Column(name = "problemas_cardiacos")
    private Boolean problemasCardiacos;

    @Column(name = "problemas_renais")
    private Boolean problemasRenais;

    @Column(name = "problemas_gastricos")
    private Boolean problemasGastricos;

    @Column(name = "problemas_respiratorios")
    private Boolean problemasRespiratorios;

    @Column(name = "problemas_alergicos")
    private Boolean problemasAlergicos;

    @Column(name = "problemas_alergicos_quais")
    private String problemasAlergicosQuais;

    @Column(name = "problemas_articulares_ou_reumatismo")
    private Boolean problemasArticularesOuReumatismo;

    @Column(name = "queixa_principal")
    private String queixaPrincipal;

    @Column(name = "evolucao_doenca_atual")
    private String evolucaoDoencaAtual;
}