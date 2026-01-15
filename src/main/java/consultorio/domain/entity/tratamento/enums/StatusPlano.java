package consultorio.domain.entity.tratamento.enums;

public enum StatusPlano {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado"),
    ADIADO("Adiado"),
    AGUARDANDO_MATERIAL("Aguardando Material"),
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
    AGENDADO("Agendado");

    private final String descricao;

    StatusPlano(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Verifica se o status permite edição.
     *
     * @return true se pode ser editado
     */
    public boolean podeSerEditado() {
        return this == PENDENTE || this == EM_ANDAMENTO || this == AGENDADO;
    }

    /**
     * Verifica se o status é final.
     *
     * @return true se é status final
     */
    public boolean isFinal() {
        return this == CONCLUIDO || this == CANCELADO;
    }
}