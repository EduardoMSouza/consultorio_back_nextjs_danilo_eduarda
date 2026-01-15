package consultorio.domain.entity.agendamento.enums;

public enum TipoProcedimento {
    CONSULTA("Consulta"),
    LIMPEZA("Limpeza"),
    RESTAURACAO("Restauração"),
    EXTRACAO("Extração"),
    CANAL("Tratamento de Canal"),
    CLAREAMENTO("Clareamento"),
    ORTODONTIA("Ortodontia"),
    IMPLANTE("Implante"),
    PROTESE("Prótese"),
    AVALIACAO("Avaliação"),
    RETORNO("Retorno"),
    EMERGENCIA("Emergência"),
    OUTROS("Outros");

    private final String descricao;

    TipoProcedimento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
