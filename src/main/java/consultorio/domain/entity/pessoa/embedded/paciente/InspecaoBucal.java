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
public class InspecaoBucal {

    @Column(name = "lingua")
    private String lingua;

    @Column(name = "mucosa")
    private String mucosa;

    @Column(name = "palato")
    private String palato;

    @Column(name = "labios")
    private String labios;

    @Column(name = "gengivas")
    private String gengivas;

    @Column(name = "nariz")
    private String nariz;

    @Column(name = "face")
    private String face;

    @Column(name = "ganglios")
    private String ganglios;

    @Column(name = "glandulas_salivares")
    private String glandulasSalivares;

    @Column(name = "alteracao_oclusao")
    private Boolean alteracaoOclusao;

    @Column(name = "alteracao_oclusao_tipo")
    private String alteracaoOclusaoTipo;

    @Column(name = "protese")
    private Boolean protese;

    @Column(name = "protese_tipo")
    private String proteseTipo;

    @Column(name = "outras_observacoes")
    private String outrasObservacoes;
}