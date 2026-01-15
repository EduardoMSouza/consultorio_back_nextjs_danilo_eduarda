package consultorio.domain.entity.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento_historico", indexes = {
        @Index(name = "idx_historico_agendamento", columnList = "agendamento_id"),
        @Index(name = "idx_historico_data", columnList = "data_hora")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agendamento_id", nullable = false)
    private Long agendamentoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoAcao acao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20)
    private StatusAgendamento statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", length = 20)
    private StatusAgendamento statusNovo;

    @Column(name = "usuario_responsavel", length = 100)
    private String usuarioResponsavel;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String detalhes;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    public enum TipoAcao {
        CRIACAO("Criação"),
        ATUALIZACAO("Atualização"),
        CONFIRMACAO("Confirmação"),
        INICIO_ATENDIMENTO("Início de Atendimento"),
        CONCLUSAO("Conclusão"),
        CANCELAMENTO("Cancelamento"),
        FALTA("Marcação de Falta"),
        LEMBRETE_ENVIADO("Lembrete Enviado"),
        EXCLUSAO("Exclusão");

        private final String descricao;

        TipoAcao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    @PrePersist
    protected void onCreate() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }

    public static AgendamentoHistorico criar(Long agendamentoId, TipoAcao acao, String usuario, String descricao) {
        AgendamentoHistorico historico = new AgendamentoHistorico();
        historico.setAgendamentoId(agendamentoId);
        historico.setAcao(acao);
        historico.setUsuarioResponsavel(usuario);
        historico.setDescricao(descricao);
        historico.setDataHora(LocalDateTime.now());
        return historico;
    }

    public static AgendamentoHistorico criarMudancaStatus(Long agendamentoId,
                                                          StatusAgendamento statusAnterior,
                                                          StatusAgendamento statusNovo,
                                                          String usuario,
                                                          String descricao) {
        AgendamentoHistorico historico = new AgendamentoHistorico();
        historico.setAgendamentoId(agendamentoId);
        historico.setStatusAnterior(statusAnterior);
        historico.setStatusNovo(statusNovo);
        historico.setUsuarioResponsavel(usuario);
        historico.setDescricao(descricao);
        historico.setDataHora(LocalDateTime.now());

        switch (statusNovo) {
            case CONFIRMADO:
                historico.setAcao(TipoAcao.CONFIRMACAO);
                break;
            case EM_ATENDIMENTO:
                historico.setAcao(TipoAcao.INICIO_ATENDIMENTO);
                break;
            case CONCLUIDO:
                historico.setAcao(TipoAcao.CONCLUSAO);
                break;
            case CANCELADO:
                historico.setAcao(TipoAcao.CANCELAMENTO);
                break;
            case FALTOU:
                historico.setAcao(TipoAcao.FALTA);
                break;
            default:
                historico.setAcao(TipoAcao.ATUALIZACAO);
        }

        return historico;
    }
}