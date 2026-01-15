package consultorio.domain.entity.tratamento.enums;

public enum TipoEvolucao {
    CONSULTA_ROTINA("Consulta de Rotina"),
    CONSULTA_URGENCIA("Consulta de Urgência"),
    RETORNO("Retorno"),
    CIRURGIA("Cirurgia"),
    PROCEDIMENTO("Procedimento"),
    AVALIACAO("Avaliação"),
    EXAME("Exame"),
    CONTROLE("Controle"),
    LIMPEZA("Limpeza"),
    RESTAURACAO("Restauração");

    private final String descricao;

    TipoEvolucao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
