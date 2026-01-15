package consultorio.domain.entity.agendamento.enums;

public enum StatusAgendamento {
    AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    EM_ATENDIMENTO("Em Atendimento"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado"),
    FALTOU("Faltou");

    private final String descricao;

    StatusAgendamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isFinalizado() {
        return this == CONCLUIDO || this == CANCELADO || this == FALTOU;
    }

    public boolean podeSerEditado() {
        return this == AGENDADO || this == CONFIRMADO;
    }

    public boolean podeSerCancelado() {
        return this != CONCLUIDO && this != FALTOU;
    }
}