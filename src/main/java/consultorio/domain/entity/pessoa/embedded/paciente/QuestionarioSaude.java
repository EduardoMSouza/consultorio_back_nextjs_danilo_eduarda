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
public class QuestionarioSaude {

    @Column(name = "sofre_doenca")
    private Boolean sofreDoenca;

    @Column(name = "sofre_doenca_quais")
    private String sofreDoencaQuais;

    @Column(name = "tratamento_medico_atual")
    private Boolean tratamentoMedicoAtual;

    @Column(name = "gravidez")
    private Boolean gravidez;

    @Column(name = "uso_medicacao")
    private Boolean usoMedicacao;

    @Column(name = "uso_medicacao_quais")
    private String usoMedicacaoQuais;

    @Column(name = "medico_assistente_telefone")
    private String medicoAssistenteTelefone;

    @Column(name = "teve_alergia")
    private Boolean teveAlergia;

    @Column(name = "teve_alergia_quais")
    private String teveAlergiaQuais;

    @Column(name = "foi_operado")
    private Boolean foiOperado;

    @Column(name = "foi_operado_quais")
    private String foiOperadoQuais;

    @Column(name = "problemas_cicatrizacao")
    private Boolean problemasCicatrizacao;

    @Column(name = "problemas_anestesia")
    private Boolean problemasAnestesia;

    @Column(name = "problemas_hemorragia")
    private Boolean problemasHemorragia;

    @Column(name = "habitos")
    private String habitos;

    @Column(name = "antecedentes_familiares")
    private String antecedentesFamiliares;
}