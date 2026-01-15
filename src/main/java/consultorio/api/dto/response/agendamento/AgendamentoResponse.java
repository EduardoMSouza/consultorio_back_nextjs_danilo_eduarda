package consultorio.api.dto.response.agendamento;

import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.agendamento.enums.TipoProcedimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoResponse {

    private Long id;

    // Dentista
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private String dentistaEspecialidade;
    private String dentistaTelefone;
    private String dentistaEmail;
    private String dentistaCorAgenda;

    // Paciente
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteTelefone;
    private String pacienteCpf;
    private String pacienteEmail;
    private LocalDate pacienteDataNascimento;
    private String pacienteConvenio;
    private String pacienteNumeroCarteirinha;
    private String pacienteAlergias;
    private String pacienteObservacoesSaude;

    // Agendamento
    private LocalDate dataConsulta;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private Long duracaoMinutos;
    private String duracaoFormatada;
    private LocalDateTime dataHoraConsulta;

    // Status
    private StatusAgendamento status;
    private String statusDescricao;
    private String statusCor;
    private String statusIcone;

    // Procedimento
    private TipoProcedimento tipoProcedimento;
    private String tipoProcedimentoDescricao;
    private String tipoProcedimentoCor;
    private String tipoProcedimentoIcone;

    // Informações
    private String observacoes;
    private String observacoesResumidas;
    private Double valorConsulta;
    private String valorFormatado;
    private Double valorPago;
    private String valorPagoFormatado;
    private String formaPagamento;
    private Boolean pagamentoConfirmado;
    private LocalDateTime pagamentoConfirmadoEm;

    // Flags de controle
    private Boolean podeEditar;
    private Boolean podeCancelar;
    private Boolean podeConfirmar;
    private Boolean podeIniciarAtendimento;
    private Boolean podeConcluir;
    private Boolean podeMarcarFalta;
    private Boolean podeReagendar;
    private Boolean podeEnviarLembrete;

    // Status temporais
    private Boolean consultaPassada;
    private Boolean consultaFutura;
    private Boolean hoje;
    private Boolean estaSemana;
    private Boolean esteMes;
    private Boolean emAndamento;
    private Long minutosParaConsulta;
    private String tempoRestante;

    // Confirmação/Lembrete
    private Boolean confirmado;
    private LocalDateTime confirmadoEm;
    private String confirmadoPor;
    private Boolean lembreteEnviado;
    private LocalDateTime lembreteEnviadoEm;
    private String tipoLembreteEnviado;
    private Integer lembretesEnviados;
    private LocalDateTime ultimoLembreteEnviado;

    // Cancelamento
    private String motivoCancelamento;
    private String motivoCancelamentoResumido;
    private String canceladoPor;
    private LocalDateTime canceladoEm;
    private Boolean cancelamentoJustificado;

    // Reagendamento
    private Boolean reagendado;
    private Long agendamentoOriginalId;
    private String motivoReagendamento;
    private LocalDateTime reagendadoEm;
    private String reagendadoPor;

    // Atendimento
    private LocalDateTime atendimentoIniciadoEm;
    private LocalDateTime atendimentoConcluidoEm;
    private String anotacoesAtendimento;
    private String prescricoes;
    private String recomendacoes;
    private String proximosPassos;
    private LocalDate proximaConsultaData;
    private String proximaConsultaObservacoes;
    private Long atendimentoDuracaoMinutos;
    private String atendimentoDuracaoFormatada;

    // Documentos
    private Boolean possuiOrcamento;
    private Boolean possuiReceita;
    private Boolean possuiAtestado;
    private Boolean possuiLaudo;
    private Integer quantidadeDocumentos;
    private Boolean documentosAssinados;

    // Auditoria
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private String criadoPor;
    private String atualizadoPor;
    private String criadoPorNome;
    private String atualizadoPorNome;

    // Sala/Recurso
    private String salaConsulta;
    private String equipamentosNecessarios;
    private String materiaisConsumidos;
    private String auxiliarResponsavel;
    private Boolean necessitaAuxiliar;

    // Convenio/Plano
    private String convenioUtilizado;
    private String numeroAutorizacaoConvenio;
    private Double valorConvenio;
    private Double valorParticular;
    private Boolean coparticipacao;
    private Double valorCoparticipacao;

    // URLs e Links
    private String urlDetalhes;
    private String urlCalendario;
    private String urlWhatsappPaciente;
    private String urlEmailPaciente;
    private String qrCodeCheckin;

    // Métricas
    private Integer avaliacaoAtendimento;
    private String comentarioAvaliacao;
    private Boolean pacienteCompareceu;
    private Boolean pacienteAtrasado;
    private Integer minutosAtraso;
    private Boolean necessitaRetorno;
    private Integer prioridadeRetorno;
    private String motivoRetorno;

    // Histórico
    private Integer quantidadeConsultasAnteriores;
    private LocalDate ultimaConsultaData;
    private String ultimaConsultaProcedimento;
    private Boolean pacienteRecorrente;
    private Integer consultasNoAno;
}