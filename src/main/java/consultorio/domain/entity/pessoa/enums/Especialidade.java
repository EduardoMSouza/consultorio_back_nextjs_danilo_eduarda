package consultorio.domain.entity.pessoa.enums;

public enum Especialidade {
    ORTODONTIA("Ortodontia"),
    IMPLANTODONTIA("Implantodontia"),
    ENDODONTIA("Endodontia"),
    PERIODONTIA("Periodontia"),
    ODONTOPEDIATRIA("Odontopediatria"),
    CIRURGIA_BUCOMAXILOFACIAL("Cirurgia Bucomaxilofacial"),
    DENTISTICA("Dentística"),
    PROTESE_DENTARIA("Prótese Dentária"),
    GERAL("Clínico Geral");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}