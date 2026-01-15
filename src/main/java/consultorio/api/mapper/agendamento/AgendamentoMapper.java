package consultorio.api.mapper.agendamento;

import consultorio.api.dto.request.agendamento.AgendamentoCreateRequest;
import consultorio.api.dto.request.agendamento.AgendamentoUpdateRequest;
import consultorio.api.dto.response.agendamento.*;
import consultorio.api.mapper.pessoa.DentistaMapper;
import consultorio.api.mapper.pessoa.PacienteMapper;
import consultorio.domain.entity.agendamento.Agendamento;
import consultorio.domain.entity.agendamento.enums.StatusAgendamento;
import consultorio.domain.entity.pessoa.Dentista;
import consultorio.domain.entity.pessoa.Paciente;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static consultorio.domain.entity.agendamento.enums.StatusAgendamento.*;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    private final DentistaMapper dentistaMapper;
    private final PacienteMapper pacienteMapper;

    public Agendamento toEntity(AgendamentoCreateRequest request, Dentista dentista, Paciente paciente) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDentista(dentista);
        agendamento.setPaciente(paciente);
        agendamento.setDataConsulta(request.getDataConsulta());
        agendamento.setHoraInicio(request.getHoraInicio());
        agendamento.setHoraFim(request.getHoraFim());
        agendamento.setTipoProcedimento(request.getTipoProcedimento());
        agendamento.setObservacoes(request.getObservacoes());
        agendamento.setValorConsulta(request.getValorConsulta());
        agendamento.setCriadoPor(request.getCriadoPor());
        return agendamento;
    }

    public void updateEntityFromRequest(AgendamentoUpdateRequest request, Agendamento agendamento) {
        if (request.getDataConsulta() != null) {
            agendamento.setDataConsulta(request.getDataConsulta());
        }
        if (request.getHoraInicio() != null) {
            agendamento.setHoraInicio(request.getHoraInicio());
        }
        if (request.getHoraFim() != null) {
            agendamento.setHoraFim(request.getHoraFim());
        }
        if (request.getTipoProcedimento() != null) {
            agendamento.setTipoProcedimento(request.getTipoProcedimento());
        }
        if (request.getObservacoes() != null) {
            agendamento.setObservacoes(request.getObservacoes());
        }
        if (request.getValorConsulta() != null) {
            agendamento.setValorConsulta(request.getValorConsulta());
        }
        if (request.getAtualizadoPor() != null) {
            agendamento.setAtualizadoPor(request.getAtualizadoPor());
        }
    }

    public AgendamentoResponse toResponse(Agendamento agendamento) {
        AgendamentoResponse response = new AgendamentoResponse();
        response.setId(agendamento.getId());

        // Dentista
        if (agendamento.getDentista() != null) {
            response.setDentistaId(agendamento.getDentista().getId());
            response.setDentistaNome(agendamento.getDentista().getNome());
            response.setDentistaCro(agendamento.getDentista().getCro());
            response.setDentistaEspecialidade(agendamento.getDentista().getEspecialidade());
        }

        // Paciente
        if (agendamento.getPaciente() != null) {
            response.setPacienteId(agendamento.getPaciente().getId());
            response.setPacienteNome(agendamento.getPaciente().getNome());
            response.setPacienteTelefone(agendamento.getPaciente().getTelefone());
            response.setPacienteCpf(agendamento.getPaciente().getCpf());
        }

        // Agendamento
        response.setDataConsulta(agendamento.getDataConsulta());
        response.setHoraInicio(agendamento.getHoraInicio());
        response.setHoraFim(agendamento.getHoraFim());
        response.setDuracaoMinutos(agendamento.getDuracaoEmMinutos());
        response.setStatus(agendamento.getStatus());
        response.setStatusDescricao(agendamento.getStatus() != null ? agendamento.getStatus().getDescricao() : null);
        response.setTipoProcedimento(agendamento.getTipoProcedimento());
        response.setTipoProcedimentoDescricao(agendamento.getTipoProcedimento() != null ? agendamento.getTipoProcedimento().getDescricao() : null);
        response.setObservacoes(agendamento.getObservacoes());
        response.setValorConsulta(agendamento.getValorConsulta());

        // Flags
        response.setPodeEditar(agendamento.isPodeSerEditado());
        response.setPodeCancelar(agendamento.isPodeSerCancelado());
        response.setConsultaPassada(agendamento.isConsultaPassada());
        response.setHoje(agendamento.isHoje());

        // Confirmação/Lembrete
        response.setConfirmadoEm(agendamento.getConfirmadoEm());
        response.setLembreteEnviado(agendamento.getLembreteEnviado());
        response.setLembreteEnviadoEm(agendamento.getLembreteEnviadoEm());

        // Cancelamento
        response.setMotivoCancelamento(agendamento.getMotivoCancelamento());
        response.setCanceladoPor(agendamento.getCanceladoPor());
        response.setCanceladoEm(agendamento.getCanceladoEm());

        // Auditoria
        response.setCriadoEm(agendamento.getCriadoEm());
        response.setAtualizadoEm(agendamento.getAtualizadoEm());
        response.setCriadoPor(agendamento.getCriadoPor());
        response.setAtualizadoPor(agendamento.getAtualizadoPor());

        return response;
    }

    public AgendamentoResumoResponse toResumoResponse(Agendamento agendamento) {
        AgendamentoResumoResponse response = new AgendamentoResumoResponse();
        response.setId(agendamento.getId());
        response.setDataConsulta(agendamento.getDataConsulta());
        response.setHoraInicio(agendamento.getHoraInicio());
        response.setHoraFim(agendamento.getHoraFim());
        response.setDuracaoMinutos(agendamento.getDuracaoEmMinutos());

        if (agendamento.getDentista() != null) {
            response.setDentistaId(agendamento.getDentista().getId());
            response.setDentistaNome(agendamento.getDentista().getNome());
        }

        if (agendamento.getPaciente() != null) {
            response.setPacienteId(agendamento.getPaciente().getId());
            response.setPacienteNome(agendamento.getPaciente().getNome());
            response.setPacienteTelefone(agendamento.getPaciente().getTelefone());
        }

        response.setStatus(agendamento.getStatus());
        response.setStatusDescricao(agendamento.getStatus() != null ? agendamento.getStatus().getDescricao() : null);
        response.setTipoProcedimento(agendamento.getTipoProcedimento());
        response.setTipoProcedimentoDescricao(agendamento.getTipoProcedimento() != null ? agendamento.getTipoProcedimento().getDescricao() : null);
        response.setConfirmado(agendamento.getStatus() != null && agendamento.getStatus() == StatusAgendamento.CONFIRMADO);
        response.setHoje(agendamento.isHoje());

        return response;
    }

    public AgendamentoCalendarioResponse toCalendarioResponse(Agendamento agendamento) {
        AgendamentoCalendarioResponse response = new AgendamentoCalendarioResponse();
        response.setId(agendamento.getId());
        response.setTitle(agendamento.getPaciente() != null ? agendamento.getPaciente().getNome() : "Agendamento");
        response.setStart(agendamento.getDataConsulta().atTime(agendamento.getHoraInicio()));
        response.setEnd(agendamento.getDataConsulta().atTime(agendamento.getHoraFim()));
        response.setStatus(agendamento.getStatus());
        response.setTipoProcedimento(agendamento.getTipoProcedimento());
        response.setBackgroundColor(getCorCalendario(agendamento.getStatus()));
        response.setBorderColor(getBordaCalendario(agendamento.getStatus()));
        response.setTextColor("#ffffff");
        response.setEditable(agendamento.isPodeSerEditado());
        response.setDentistaNome(agendamento.getDentista() != null ? agendamento.getDentista().getNome() : null);
        response.setPacienteNome(agendamento.getPaciente() != null ? agendamento.getPaciente().getNome() : null);
        response.setObservacoes(agendamento.getObservacoes());

        return response;
    }

    public AgendamentoLembreteResponse toLembreteResponse(Agendamento agendamento) {
        AgendamentoLembreteResponse response = new AgendamentoLembreteResponse();
        response.setId(agendamento.getId());

        if (agendamento.getPaciente() != null) {
            response.setPacienteNome(agendamento.getPaciente().getNome());
            response.setPacienteTelefone(agendamento.getPaciente().getTelefone());
            response.setPacienteEmail(agendamento.getPaciente().getEmail());
        }

        response.setDataHoraConsulta(agendamento.getDataConsulta().atTime(agendamento.getHoraInicio()));
        response.setDentistaNome(agendamento.getDentista() != null ? agendamento.getDentista().getNome() : null);
        response.setLocalConsulta("Consultório Odontológico");
        response.setTipoProcedimento(agendamento.getTipoProcedimento() != null ? agendamento.getTipoProcedimento().getDescricao() : null);
        response.setLembreteEnviado(agendamento.getLembreteEnviado());
        response.setLembreteEnviadoEm(agendamento.getLembreteEnviadoEm());
        response.setConfirmado(agendamento.getStatus() != null && agendamento.getStatus() == StatusAgendamento.CONFIRMADO);
        response.setConfirmadoEm(agendamento.getConfirmadoEm());

        return response;
    }

    public List<AgendamentoResponse> toResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AgendamentoResumoResponse> toResumoResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(this::toResumoResponse)
                .collect(Collectors.toList());
    }

    public List<AgendamentoCalendarioResponse> toCalendarioResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(this::toCalendarioResponse)
                .collect(Collectors.toList());
    }

    public List<AgendamentoLembreteResponse> toLembreteResponseList(List<Agendamento> agendamentos) {
        return agendamentos.stream()
                .map(this::toLembreteResponse)
                .collect(Collectors.toList());
    }

    public AgendamentoPaginadoResponse toPaginadoResponse(Page<Agendamento> page) {
        AgendamentoPaginadoResponse response = new AgendamentoPaginadoResponse();
        response.setConteudo(toResumoResponseList(page.getContent()));
        response.setPagina(page.getNumber());
        response.setTamanho(page.getSize());
        response.setTotalElementos(page.getTotalElements());
        response.setTotalPaginas(page.getTotalPages());
        response.setUltima(page.isLast());
        response.setPrimeira(page.isFirst());
        return response;
    }

    public AgendamentoEstatisticasResponse toEstatisticasResponse(AgendamentoEstatisticas estatisticas) {
        AgendamentoEstatisticasResponse response = new AgendamentoEstatisticasResponse();
        response.setTotalAgendamentos(estatisticas.getTotalAgendamentos());
        response.setAgendamentosHoje(estatisticas.getAgendamentosHoje());
        response.setAgendamentosSemana(estatisticas.getAgendamentosSemana());
        response.setAgendamentosMes(estatisticas.getAgendamentosMes());
        response.setConfirmados(estatisticas.getConfirmados());
        response.setCancelados(estatisticas.getCancelados());
        response.setFaltas(estatisticas.getFaltas());
        response.setConcluidos(estatisticas.getConcluidos());
        response.setTaxaComparecimento(estatisticas.getTaxaComparecimento());
        response.setFaturamentoMes(estatisticas.getFaturamentoMes());
        response.setFaturamentoAno(estatisticas.getFaturamentoAno());
        return response;
    }

    private String getCorCalendario(StatusAgendamento status) {
        if (status == null) return "#3788d8";

        switch (status) {
            case AGENDADO: return "#3788d8"; // Azul
            case CONFIRMADO: return "#28a745"; // Verde
            case EM_ATENDIMENTO: return "#ffc107"; // Amarelo
            case CONCLUIDO: return "#17a2b8"; // Ciano
            case CANCELADO: return "#dc3545"; // Vermelho
            case FALTOU: return "#6c757d"; // Cinza
            default: return "#3788d8";
        }
    }

    private String getBordaCalendario(StatusAgendamento status) {
        return getCorCalendario(status);
    }

    // Helper class for estatisticas
    @Getter
    @Setter
    public static class AgendamentoEstatisticas {
        private Long totalAgendamentos;
        private Long agendamentosHoje;
        private Long agendamentosSemana;
        private Long agendamentosMes;
        private Long confirmados;
        private Long cancelados;
        private Long faltas;
        private Long concluidos;
        private Double taxaComparecimento;
        private Double faturamentoMes;
        private Double faturamentoAno;
    }
}