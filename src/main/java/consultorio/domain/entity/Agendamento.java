package consultorio.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos", indexes = {
        @Index(name = "idx_dentista_data", columnList = "dentista_id, data_consulta"),
        @Index(name = "idx_paciente_data", columnList = "paciente_id, data_consulta"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_data_consulta", columnList = "data_consulta")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentista_id", nullable = false)
    private Dentista dentista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_procedimento", length = 20)
    private TipoProcedimento tipoProcedimento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "valor_consulta")
    private Double valorConsulta;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    // Campos de auditoria
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @Column(name = "criado_por", length = 100)
    private String criadoPor;

    @Column(name = "atualizado_por", length = 100)
    private String atualizadoPor;

    // Campos de cancelamento
    @Column(name = "cancelado_por", length = 100)
    private String canceladoPor;

    @Column(name = "cancelado_em")
    private LocalDateTime canceladoEm;

    @Column(name = "motivo_cancelamento", length = 500)
    private String motivoCancelamento;

    // Campos de confirmação e lembrete
    @Column(name = "confirmado_em")
    private LocalDateTime confirmadoEm;

    @Column(name = "lembrete_enviado")
    private Boolean lembreteEnviado = false;

    @Column(name = "lembrete_enviado_em")
    private LocalDateTime lembreteEnviadoEm;

    // Enums
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

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
        if (status == null) {
            status = StatusAgendamento.AGENDADO;
        }
        if (ativo == null) {
            ativo = true;
        }
        if (lembreteEnviado == null) {
            lembreteEnviado = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }

    // Métodos de negócio
    public String getNomePaciente() {
        return paciente != null ? paciente.getDadosBasicos().getNome() : null;
    }

    public String getNomeDentista() {
        return dentista != null ? dentista.getNome() : null;
    }

    public boolean isConflitante(LocalTime outroInicio, LocalTime outroFim) {
        return !(horaFim.compareTo(outroInicio) <= 0 || horaInicio.compareTo(outroFim) >= 0);
    }

    public boolean isPodeSerEditado() {
        return status.podeSerEditado() && ativo;
    }

    public boolean isPodeSerCancelado() {
        return status.podeSerCancelado() && ativo;
    }

    public boolean isFinalizado() {
        return status.isFinalizado();
    }

    public long getDuracaoEmMinutos() {
        return java.time.Duration.between(horaInicio, horaFim).toMinutes();
    }

    public boolean isConsultaPassada() {
        LocalDateTime dataHoraConsulta = LocalDateTime.of(dataConsulta, horaInicio);
        return dataHoraConsulta.isBefore(LocalDateTime.now());
    }

    public boolean isHoje() {
        return dataConsulta.equals(LocalDate.now());
    }

    public void confirmar(String usuario) {
        this.status = StatusAgendamento.CONFIRMADO;
        this.confirmadoEm = LocalDateTime.now();
        this.atualizadoPor = usuario;
    }

    public void iniciarAtendimento(String usuario) {
        this.status = StatusAgendamento.EM_ATENDIMENTO;
        this.atualizadoPor = usuario;
    }

    public void concluir(String usuario) {
        this.status = StatusAgendamento.CONCLUIDO;
        this.atualizadoPor = usuario;
    }

    public void cancelar(String motivo, String usuario) {
        this.status = StatusAgendamento.CANCELADO;
        this.motivoCancelamento = motivo;
        this.canceladoPor = usuario;
        this.canceladoEm = LocalDateTime.now();
        this.atualizadoPor = usuario;
    }

    public void marcarFalta(String usuario) {
        this.status = StatusAgendamento.FALTOU;
        this.atualizadoPor = usuario;
    }

    public void marcarLembreteEnviado() {
        this.lembreteEnviado = true;
        this.lembreteEnviadoEm = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
    }
}